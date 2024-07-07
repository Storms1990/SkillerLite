package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.ScriptConsole;

import java.util.Random;

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
            ScriptConsole.println("Mixing is: " + Well.interact("Mix Potions"));
            Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));
            if (!Interfaces.isOpen(1370))
            {
                ScriptConsole.println("Mixing is: " + Well.interact("Mix Potions"));
                Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));
            }
            delay();
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350);
            delay(1500,2200);

        }
        else{
            ScriptConsole.println("no well found");
            Backpack.interact(1, "Mix");
            delay(1000,1200);
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350);
            delay(1500,2200);
            ScriptConsole.println("Mixing from inventory");

        }
        while (Interfaces.isOpen(1251))
        {
            delay(1000,1500);
        }
        ScriptConsole.println("finished");
    }


    private static void delay() {
        Execution.delay(random.nextLong(200, 1300));
    }
    private static void delay(long lhs, long rhs) {
        Execution.delay(random.nextLong(lhs, rhs));
    }
}
