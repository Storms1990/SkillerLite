package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.js5.types.vars.VarDomainType;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.game.vars.VarManager;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.ScriptConsole;

import java.util.Random;

public class Goldmaker {
    private static Random random = new Random();



    public static void makeGold(){
        EntityResultSet<SceneObject> Furnaces = SceneObjectQuery.newQuery().name("Furnace").option("Smelt").results();
        if (Furnaces.isEmpty()){
            ScriptConsole.println("no Furnace found move to area with furnace and bank");
            MultiSkillerLite.botState= MultiSkillerLite.BotState.Idle;
        }
        SceneObject Furnace = Furnaces.nearestTo(MultiSkillerLite.player);
        delay();

        boolean success = Furnace.interact("Smelt");
        if (!success)
        {
            success = Furnace.interact("Smelt");
            delay();
            if (!success)
            {
                ScriptConsole.println("Failed to interact with Smelter. Error");
            }
        }
        Execution.delayUntil(15000, () -> Interfaces.isOpen(37));

        if(MultiSkillerLite.familyCrestCheck){
            if(Backpack.contains("Gold bar")) {
                ScriptConsole.println("depositing gold");
                MiniMenu.interact(ComponentAction.COMPONENT.getType(), 1, -1, 2424999);
                delay();
            }
        }

        if (VarManager.getVarValue(VarDomainType.PLAYER, 8333) == 2357){
            ScriptConsole.println("gold bar already selected ");
        }
        else{
            MiniMenu.interact(ComponentAction.COMPONENT.getType(), 1, 5, 2424957);
        }
        delay();
        if (VarManager.getVarValue(VarDomainType.PLAYER, 8336) > 0) {
            MiniMenu.interact(ComponentAction.COMPONENT.getType(), 1, -1, 2424995);
            ScriptConsole.println("interacting with beginproject");
            Execution.delayUntil(3000, ()-> Interfaces.isOpen(1251));
        }
        else {
            ScriptConsole.println("we have run out of matierals to make bars");
            MultiSkillerLite.botState= MultiSkillerLite.BotState.Idle;
        }

        delay();
        if (Interfaces.isOpen(1251))
        {
            MultiSkillerLite.botState = MultiSkillerLite.BotState.CraftingDelay;
        }



    }

    private static void delay() {
        Execution.delay(random.nextLong(800, 1200));
    }
    private static void delay(long lhs, long rhs) {
        Execution.delay(random.nextLong(lhs, rhs));
    }
}
