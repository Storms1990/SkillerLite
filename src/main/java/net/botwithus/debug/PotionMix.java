package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.hud.inventories.BackpackInventory;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.js5.types.vars.VarDomainType;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.game.vars.VarManager;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.ScriptConsole;

import java.util.Random;
import java.util.regex.Pattern;

public class PotionMix {
    private static final Random random = new Random();

    public static void mixPotions()
    {
        ScriptConsole.println("Starting Mixing");
        EntityResultSet<SceneObject> wells = SceneObjectQuery.newQuery().name("Portable well").option("Mix Potions").results();


        boolean wellCheck = wells.isEmpty();
        ScriptConsole.println(wellCheck);

        if (!wellCheck) {
            SceneObject Well = wells.first();
            if(Well!= null){
                ScriptConsole.println("Mixing is: " + Well.interact("Mix Potions"));
            }else{
                ScriptConsole.println("No station found ERROR");
                return;
            }
            Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));
            if (Interfaces.isOpen(1370))
            {
            delay();
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350);
            delay(1500,2200);
            }

        }
        else{ ///will not make overloads and other combo potions without portable, will try finding all the ingredients and using on each other.

            ScriptConsole.println("Mixing from inventory");
            boolean success = Backpack.interact(getPotionSlot(), "Mix");
            if (!success)
            {
                ScriptConsole.println("could not mix from inventory waiting for station");
                MultiSkillerLite.botState= MultiSkillerLite.BotState.StationCheck;
                return;
            }

            delay(1000,1200);
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350);
            delay(1500,2200);


        }
        if (Interfaces.isOpen(1251))
        {
            MultiSkillerLite.botState = MultiSkillerLite.BotState.CraftingDelay;
        }

    }
    public static void makeUnf(){
        ScriptConsole.println("Making Unfinished");
        Backpack.interact("Vial of water", "Make");
        Execution.delayUntil(5000, ()-> Interfaces.isOpen(1370));
        delay(1000,1200);
        if (VarManager.getVarValue(VarDomainType.PLAYER, 8846) > 0) {
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, 89784350);
        }else{
            ScriptConsole.println("ran out of materials");
            if(MultiSkillerLite.Logout){
                MultiSkillerLite.Logout();
            }else{
                MultiSkillerLite.botState = MultiSkillerLite.BotState.Idle;
            }
        }
        delay(1500,2200);

        if (Interfaces.isOpen(1251))
        {
            MultiSkillerLite.botState = MultiSkillerLite.BotState.CraftingDelay;
        }

    }

    public static void cleanherbs()
    {
        ScriptConsole.println("Selecting herb");
        MultiSkillerLite.inventory = Backpack.getItems();
        boolean haveHerbs = false;
        for( Item item : MultiSkillerLite.inventory){
            if (item != null) {
                if (item.getName().contains("Grimy")) {
                    haveHerbs = true;
                    break;
                }
            }else{
                ScriptConsole.println("failed to find herbs Nullcheck");
                MultiSkillerLite.botState = MultiSkillerLite.BotState.Idle;
            }
        }
        if (haveHerbs) {
            MiniMenu.interact(ComponentAction.COMPONENT.getType(), 1, 0, 96534533);
            Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));
        }else{
            ScriptConsole.println("No herbs Found going idle");
            MultiSkillerLite.botState = MultiSkillerLite.BotState.Idle;
        }
        if (Interfaces.isOpen(1370))
        {
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350);

        }
        delay(2000,2500);

        if (Interfaces.isOpen(1251))
        {
            MultiSkillerLite.botState = MultiSkillerLite.BotState.CraftingDelay;
        }

    }

    private static int getPotionSlot()
    {
        int slotInt= 0;
        for (Item item : MultiSkillerLite.inventory)
        {
            if (item != null) {
                if (item.getName().contains("potion")) {
                    slotInt = item.getSlot();
                    break;
                }
            }
        }

        return slotInt;
    }




    private static void delay() {
        Execution.delay(random.nextLong(800, 1300));
    }
    private static void delay(long lhs, long rhs) {
        Execution.delay(random.nextLong(lhs, rhs));
    }
}
