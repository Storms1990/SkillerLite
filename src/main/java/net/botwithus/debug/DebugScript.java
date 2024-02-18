package net.botwithus.debug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.events.impl.InventoryUpdateEvent;
import net.botwithus.rs3.events.impl.ServerTickedEvent;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.js5.types.vars.VarDomainType;
import net.botwithus.rs3.game.queries.builders.animations.SpotAnimationQuery;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.items.GroundItemQuery;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.queries.results.ResultSet;
import net.botwithus.rs3.game.scene.entities.animation.SpotAnimation;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.characters.player.Player;
import net.botwithus.rs3.game.scene.entities.item.GroundItem;
import net.botwithus.rs3.game.skills.Skill;
import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.game.vars.VarManager;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class DebugScript extends LoopingScript {
    public DebugScript(String name, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(name, scriptConfig, scriptDefinition);
    }

    public Map<Integer, Varp> varps = new ConcurrentHashMap<>();
    public Map<Integer, Varbit> varbits = new ConcurrentHashMap<>();

    public boolean runScript = false;
    private Random rand = new Random();

    @Override
    public boolean initialize() {
        this.sgc = new DebugGraphicsContext(getConsole(), this);
        this.loopDelay = 590;
        subscribe(ServerTickedEvent.class, serverTickedEvent -> {
            println("Server ticks since login: %d", serverTickedEvent.getTicks());
        });
        subscribe(InventoryUpdateEvent.class, inventoryUpdateEvent -> {
            if (inventoryUpdateEvent.getInventoryId() == 93) {
                println("My backpack received item %d!", inventoryUpdateEvent.getNewItem().getId());
            }
        });
        return super.initialize();
    }

    public static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
            .create();

    @Override
    public void onLoop() {

        LocalPlayer player = Client.getLocalPlayer();
        if (player == null) {
            Execution.delay(rand.nextLong(1500,4000));
            return;
        }

        npcQueryExample();

        /*Item box = InventoryItemQuery.newQuery(93).name("Archaeological soil box")
                .results().first();
        if(box != null) {
            Component comp = ComponentQuery.newQuery(517).item(box.getId()).results().first();
            if(comp != null) {
                boolean success = comp.interact(9);
                if(!success) {
                    System.out.println("Failed to interact with soil box.");
                }
            } else {
                System.out.println("Component was not found.");
            }
        } else {
            System.out.println("Item was not found.");
        }*/

        //interfaceQuery();
        //spotAnimQuery();
        //mageOfZammy();
        //System.out.println(VarManager.getVarValue(VarDomainType.PLAYER, 689));

        groundItemQuery();

        Execution.delay(5000);
    }

    public void spotAnimQuery() {
        for (SpotAnimation result : SpotAnimationQuery.newQuery().results()) {
            if (result != null) {
                println(result.getId());
                println(result.getCoordinate());
            }
        }
    }

    public void mageOfZammy() {
        EntityResultSet<Npc> results = NpcQuery.newQuery().name("Mage of Zamorak").option("Teleport").results();
        if(!results.isEmpty()) {
            Npc mage = results.first();
            if (mage != null) {
                println(mage.getName());
                for (String option : mage.getOptions()) {
                    println(option);
                }
            }
        }
    }

    public void npcQueryExample() {
        EntityResultSet<Npc> results = NpcQuery.newQuery().name("Chicken").results();
        System.out.println(results.size());
        for (Npc chicken : results) {
            System.out.println("Name= " + chicken.getName() + " Id= " + chicken.getId());
        }
    }

    public void playerExample() {
        Player self = Client.getLocalPlayer();
        if (self == null) {
            println("oops");
        } else {
            self.getHitmarks();
            self.getHeadbars();
        }
    }

    private void variableExample() {
        //Skill dialogue, selected item
        int itemId = VarManager.getVarValue(VarDomainType.PLAYER, 1170);
        println(itemId);
        boolean inCombat = VarManager.getVarbitValue(1899) == 1;
        println("Is in combat= " + inCombat);
    }

    public void interfaceQuery() {
        /*Component rune_bar = ComponentQuery.newQuery(37).option("Rune bar", String::contains)
                .results().first();

        System.out.println(rune_bar);*/

        Component lodestone = ComponentQuery.newQuery(1465).option("Lodestone network")
                .results().first();
        if (lodestone != null) {
            for (String option : lodestone.getOptions()) {
                System.out.println(option);
            }
            System.out.println(lodestone.interact("Lodestone network"));
        }

        /*ComponentQuery.newQuery(37)
                .componentIndex(62)
                .subComponentIndex(3)
                .results()
                .first()
                .map(c -> c.doAction(1));*/
       /* Component first = ComponentQuery.newQuery(37)
                .componentIndex(62)
                .subComponentIndex(3)
                .results()
                .first();
        if (first != null) {
            first.interact(1);
        }*/
    }

    public void skillsExample() {
        Skill skill = Skills.SLAYER.getSkill();
        System.out.println(skill);
    }

    public void inventoryQuery() {
        //Find what inventory 1512 belongs too.
        InventoryItemQuery query = InventoryItemQuery.newQuery().ids(1512);
        ResultSet<Item> results = query.results();
        for (Item result : results) {
            System.out.printf("InvId= %d Name= %s Id= %d\n", result.getInventoryType().getId(), result.getName(), result.getId());
        }
        //Find Logs in local player inventory or Backpack
        InventoryItemQuery query1 = InventoryItemQuery.newQuery(93).name("Logs");
        Item first = query1.results().first();
        if (first != null) {
            System.out.println(first);
        }
    }

    public void groundItemQuery() {
        for (GroundItem result : GroundItemQuery.newQuery().results()) {
            System.out.println(result.getName() + " - " + result.getId() + " - " + result.getStackSize() + " - " + result.getCoordinate());
        }
    }

}
