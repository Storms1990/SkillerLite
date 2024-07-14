package net.botwithus.debug;


import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.events.impl.SkillUpdateEvent;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.login.LoginManager;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.api.game.hud.inventories.Backpack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MultiSkillerLite extends LoopingScript {

    public static LocalPlayer player;
    public static BotState botState = BotState.Idle;
    private static Random random = new Random();
    public static boolean Logout = false;
    public static List<Item> inventory;
    long startTime;
    public int xpGained = 0;
    public static boolean usePortableRange = false;
    public static boolean familyCrestCheck = false;
    public static BotState returnState = BotState.Idle;
    private static int goIdle=0;


    public enum BotState{
        Idle,
        CuttingGems,
        SetInitialInventory ,
        MixPotions,
        UnfinishedPotions,
        CleanHerb,
        MakeTar,
        Cooking,
        GoldMaker,
        StationCheck,
        CraftingDelay,
        Banking
    }



    public MultiSkillerLite(String name, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(name, scriptConfig, scriptDefinition);
        this.sgc = new MultiSkillerLiteGraphicsContext(this.getConsole(), this);
        Logout = false;

    }

    public boolean initialize(){
        super.initialize();
        setActive(false);
        subscribe(SkillUpdateEvent.class, skillUpdateEvent ->
        {
            if (skillUpdateEvent.getId() == Skills.HERBLORE.getId()){
                if (skillUpdateEvent.getExperience() - skillUpdateEvent.getOldExperience() > 0) {
                    xpGained += skillUpdateEvent.getExperience() - skillUpdateEvent.getOldExperience();
                }
            }

            if (skillUpdateEvent.getId() == Skills.CRAFTING.getId()) {
                if (skillUpdateEvent.getExperience() - skillUpdateEvent.getOldExperience() > 0) {
                    xpGained += skillUpdateEvent.getExperience() - skillUpdateEvent.getOldExperience();
                }
            }
            if (skillUpdateEvent.getId() == Skills.COOKING.getId()) {
                if (skillUpdateEvent.getExperience() - skillUpdateEvent.getOldExperience() > 0) {
                    xpGained += skillUpdateEvent.getExperience() - skillUpdateEvent.getOldExperience();
                }
            }

        });


        return true;
    }


    @Override
    public void onLoop() {

        this.loopDelay = 550;
        if (!boot())
        {
            return;
        }

        switch (botState) {
            case Idle -> {
                delay(2000,3000);
            }

            case CuttingGems -> {
                //bankPreset();
                Gemcutter.cutGems();
            }

            case SetInitialInventory ->{
                setInitialInventory();

            }

            case MixPotions -> {
               // bankPreset();
                PotionMix.mixPotions();
            }

            case UnfinishedPotions ->{
                PotionMix.makeUnf();
            }

            case CleanHerb ->{
                //bankPreset();
                PotionMix.cleanherbs();

            }

            case MakeTar -> {
               // bankPreset();
                TarMaker.makeTar();
            }

            case Cooking ->{
               // bankPreset();
                Cooker.handleCooking();
            }

            case GoldMaker ->{

                Goldmaker.makeGold();
            }

            case CraftingDelay ->{
                if (Interfaces.isOpen(1251))
                {
                    println("Waiting for crafting to finish");
                    delay();
                }
                if (!Interfaces.isOpen(1251))
                {
                    setBotState(BotState.Banking);
                }
            }

            case StationCheck ->
            {// might make into switch later

                if(returnState == BotState.MixPotions){// this needs to be tested. in case they are trying to make combo potions without wells wait for well to apear.
                    EntityResultSet<SceneObject> wells = SceneObjectQuery.newQuery().name("Portable well").option("Mix Potions").results();
                    if (!wells.isEmpty()){
                        botState = returnState;
                    }
                    delay(5000,6000);
                }

            }

            case Banking-> {
                if (familyCrestCheck)
                {
                    setBotState(returnState);

                }else {
                    bankPreset();
                }
            }



        }

    }
    private void bankPreset()
    {
        SceneObject bank = SceneObjectQuery.newQuery().name("Bank chest").results().nearestTo(player);
        if (bank == null)
        {
            println("Using Banker");
            Npc bankNpc = NpcQuery.newQuery().option("Load Last Preset from").results().nearestTo(player);
            if (bankNpc == null){
                println("failed to find bank. Please move to banking area");
                setBotState(BotState.Idle);
            }
            assert bankNpc != null;
            bankNpc.interact("Load Last Preset from");
            delay();
        }
        else {
            println("Using Bank Chest");
            bank.interact("Load Last Preset from");
            delay(1000,1500);

            if (player.isMoving())
            {
                Execution.delayUntil( 15000, ()-> !player.isMoving());
            }
        }
        delay();
        handleLogout();
        if ((Client.getGameState() == Client.GameState.LOGGED_IN))
        {
            setBotState(returnState);
        }
    }


    public static void handleLogout()
    {
        switch (botState) {

            case MixPotions, UnfinishedPotions -> {
                if(Logout && doesInventoryMatch(Backpack.getItems()))
                {
                    LoginManager.setAutoLogin(false);
                    Logout();
                    delay(4000,5000);
                    botState = BotState.Idle;
                    ScriptConsole.println("Finished");

                } else if (doesInventoryMatch(Backpack.getItems())) {
                    botState = BotState.Idle;
                   ScriptConsole.println("Finished");
                }
            }

            case CuttingGems, Cooking, CleanHerb -> {
                if(Logout && Backpack.isEmpty())
                {
                    LoginManager.setAutoLogin(false);
                    Logout();
                    delay(4000,5000);
                    botState = BotState.Idle;
                }
                else if( Backpack.isEmpty()){
                    botState = BotState.Idle;
                }
            }

            case MakeTar -> {
                if (Logout && !Backpack.isFull()){
                    LoginManager.setAutoLogin(false);
                    Logout();
                    delay(4000,5000);
                    botState = BotState.Idle;
                }
                if(Logout && TarMaker.tarCount <15){
                    LoginManager.setAutoLogin(false);
                    Logout();
                    delay(4000,5000);
                    botState = BotState.Idle;
                }
                else if (!Backpack.isFull() || TarMaker.tarCount < 15){
                    botState = BotState.Idle;
                }
            }

            case GoldMaker -> {

                if(!familyCrestCheck)
                {
                    if(Logout && Backpack.isEmpty())
                    {
                        LoginManager.setAutoLogin(false);
                        Logout();
                        delay(4000,5000);
                        botState = BotState.Idle;
                    }
                    else if( Backpack.isEmpty()){
                        botState = BotState.Idle;
                    }
                }


            }


        }
    }


    public static void setInitialInventory(){
        inventory = Backpack.getItems();
        ScriptConsole.println("set inventory");
        botState = returnState;
    }

    private static boolean doesInventoryMatch(List<Item> input)
    {
        return !inventory.equals(input);
    }

    public static void Logout() {
        MiniMenu.interact(ComponentAction.COMPONENT.getType(), 1, 7,  93782016);

        Component logoutmenu = ComponentQuery.newQuery(1433).componentIndex(71).results().first();
        if (logoutmenu != null) {
            ScriptConsole.println("Logout Status: " + logoutmenu.interact(1));
        } else {
            ScriptConsole.println("Logout Button not found.");
        }
    }


    public void setLogout( boolean input)
    {
        Logout = input;
    }
    public boolean logoutBool() {
        return Logout;
    }

    public void setPortableRange( boolean input)
    {
        usePortableRange = input;
    }
    public boolean portableRangeBool() {
        return usePortableRange;
    }

    public void setFamilyCrestCheck( boolean input)
    {
        familyCrestCheck = input;
    }
    public boolean FamilyCrestCheckBool() {
        return familyCrestCheck;
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        MultiSkillerLite.botState = botState;
    }
    public String getElapsedTime(){
        long now = System.currentTimeMillis();

        long elapsedTime = now - startTime;
        long days = TimeUnit.MILLISECONDS.toDays(elapsedTime);
        elapsedTime -= TimeUnit.DAYS.toMillis(days);

        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
        elapsedTime -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        elapsedTime -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);

        return String.format("%dh %dm %ds", hours, minutes, seconds);

    }

    private void delay() {
        Execution.delay(random.nextLong(800, 1300));
    }
    private static void delay(long lhs, long rhs) {
        Execution.delay(random.nextLong(lhs, rhs));
    }
    private boolean boot() {
        player = Client.getLocalPlayer();
        if (player == null)
        {
            println("Player Fail");
            Execution.delay(random.nextLong(3000,7000));
            return false;
        }
        else if (Client.getGameState() != Client.GameState.LOGGED_IN)
        {
            println("Not Logged In");
            Execution.delay(random.nextLong(3000,7000));
            return false;
        }
        else
        {
            return true;
        }

    }

}
