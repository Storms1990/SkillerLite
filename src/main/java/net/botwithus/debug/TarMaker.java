package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.minimenu.actions.SelectableAction;
import net.botwithus.rs3.game.queries.builders.ItemQuery;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.imgui.NativeInteger;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.ScriptConsole;

import java.util.Random;

public class TarMaker {

    public static String[] tarsOptions = new String[]{"guam", "marrentill","tarromin","harralander"};
    public static NativeInteger pickedTar = new NativeInteger(0);
    private static Random random = new Random();
    public static String pickedTarName;
    public static Item tar;
    public static Item secondary;
    public static int tarCount = 15;


    public static void makeTar()
    {
        ScriptConsole.println("loading config");
        setTar();
        delay();

        ScriptConsole.println("getting tar");
//        tar = InventoryItemQuery.newQuery().name("Swamp tar").results().first();
        tar = Backpack.getItem("Swamp tar");
        tarCount = tar.getStackSize();

        delay();
        ScriptConsole.println("getting Herb");
        secondary = InventoryItemQuery.newQuery().name(("Clean " + pickedTarName)).results().first();

        delay();
        if (tar == null || secondary == null)
        {
            ScriptConsole.println("Not able to set Target craft please reload preset");
            delay();
            MultiSkillerLite.botState = MultiSkillerLite.BotState.Idle;
        }
        else{
            ScriptConsole.println(secondary.getName());
            ScriptConsole.println(tar.getName());
            delay();
            MiniMenu.interact(SelectableAction.SELECTABLE_COMPONENT.getType(), 0, tar.getSlot(), 96534533);
            delay();
            MiniMenu.interact(SelectableAction.SELECT_COMPONENT_ITEM.getType(), 0, secondary.getSlot(), 96534533);
            delay();
            Execution.delayUntil(3000, () -> Interfaces.isOpen(1370));
            if (Interfaces.isOpen(1370))
            {
                delay(1000,1200);
                MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350);

            }

        }
        Execution.delayUntil(3000, () -> Interfaces.isOpen(1251));
        boolean canProceed = Interfaces.isOpen(1251);
        if (!canProceed)
        {
            MultiSkillerLite.handleLogout();
        }
        while (Interfaces.isOpen(1251))
        {
            delay(1000,1500);
        }
        ScriptConsole.println("finished");
        delay(1000,5000);


    };


    public static void setTar()
    {
        pickedTarName = tarsOptions[pickedTar.get()];

        ScriptConsole.println("set tar to: " + pickedTarName);
    }

    private static void delay() {
        Execution.delay(random.nextLong(400, 1000));
    }
    private static void delay(long lhs, long rhs) {
        Execution.delay(random.nextLong(lhs, rhs));
    }
}
