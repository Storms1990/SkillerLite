package net.botwithus.debug;

import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.ScriptConsole;

import java.util.Random;

public class Cooker {
    private static Random random = new Random();

    public static void handleCooking() {
        ScriptConsole.println("Looking for Range");
        EntityResultSet<SceneObject> Ranges = SceneObjectQuery.newQuery().name("Range").option("Cook-at").results();
        EntityResultSet<SceneObject> portable = SceneObjectQuery.newQuery().name("Portable range").option("Cook").results();
        boolean stationCheck = Ranges.isEmpty();
        boolean rangeCheck = portable.isEmpty();
        SceneObject range;
        if (rangeCheck && stationCheck) {
            if(MultiSkillerLite.usePortableRange)
            {
                MultiSkillerLite.botState = MultiSkillerLite.BotState.StationCheck;
            }else {
                ScriptConsole.println("please move to Fort cooking area");
                MultiSkillerLite.botState = MultiSkillerLite.BotState.Idle;
            }
        }

        if (!rangeCheck){
            range = portable.nearestTo(MultiSkillerLite.player);
            ScriptConsole.println("Found Range");
            delay();
            if(range != null) {
                ScriptConsole.println(range.interact("Cook"));
            }else{
                ScriptConsole.println("no range error.");
                return;
            }

            Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));
        }
        else{
            range = Ranges.nearestTo(MultiSkillerLite.player);
            if(range != null) {
                ScriptConsole.println(range.interact("Cook"));
            }else{
                ScriptConsole.println("no range error.");
                return;
            }
            delay();
        }
        if (Interfaces.isOpen(1370)) {
            delay();
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, 89784350);
        }

        Execution.delayUntil(5000, ()-> Interfaces.isOpen(1251));
        if (Interfaces.isOpen(1251)){
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
