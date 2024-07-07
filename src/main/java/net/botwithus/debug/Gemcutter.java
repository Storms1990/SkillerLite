package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.ScriptConsole;

import java.util.Random;

public class Gemcutter {
    private static Random random = new Random();

    public static void cutGems() {

        EntityResultSet<SceneObject> craftingStation = SceneObjectQuery.newQuery().name("Portable crafter").option("Cut Gems").results();
        boolean StationCheck = craftingStation.isEmpty();
        if (!StationCheck) {
            SceneObject station = craftingStation.first();
            ScriptConsole.println("Using Station: " + station.interact("Cut Gems"));
            Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));
            if (!Interfaces.isOpen(1370))
            {
                ScriptConsole.println("Cutting is: " + station.interact("Cut Gems"));
                Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));

            }
            delay();
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350);

            delay(1500,2200);}
        else{
            ScriptConsole.println("No Crafter Found");
            Backpack.interact(1, "Craft");
            Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));
            delay();
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350);
            delay(1500,2200);
            ScriptConsole.println("Crafting from inventory");
        }
        while (Interfaces.isOpen(1251))
        {
            delay(1000,2500);
        }
        ScriptConsole.println("finished");


    }
    private static void delay() {
        Execution.delay(random.nextLong(300, 1200));
    }
    private static void delay(long lhs, long rhs) {
        Execution.delay(random.nextLong(lhs, rhs));
    }
}
