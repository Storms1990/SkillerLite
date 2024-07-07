package net.botwithus.debug;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;


public class MultiSkillerLiteGraphicsContext extends ScriptGraphicsContext {

    private final MultiSkillerLite script;

    public MultiSkillerLiteGraphicsContext(ScriptConsole console, MultiSkillerLite script) {
        super(console);
        this.script = script;
    }


    @Override
    public void drawSettings() {

        ImGui.SetWindowSize(200.f, 200.f);
        if(ImGui.Begin("MultiSkillerLite Settings", 0)) {
            ImGui.Text("Current state is: " + this.script.getBotState());
            ImGui.Text("Logout Status: " + this.script.Logout);
            ImGui.Text("Make sure your Preset is loaded!!!!");
            ImGui.Text("Start Near Bank Chest, Lumby skilling Area or Fort Workshop");
            ImGui.Text("will now work at any bank (BETA)");
            ImGui.Text("***********************************");
            ImGui.Text("Will use Portable Crafting station or Well if nearby");
            ImGui.Text("***********************************");
            ImGui.Text("For Potions make sure you START the script with first batch of ingredients already in Backpack.");
            ImGui.Text("***********************************");

            this.script.setLogout(ImGui.Checkbox("Logout when done?",this.script.logoutBool()));

            if (ImGui.Button("Mix Potions")) {
                this.script.println("Potions");
                this.script.startTime = System.currentTimeMillis();
                this.script.xpGained = 0;
                this.script.setBotState(MultiSkillerLite.BotState.SetInitialInventory );
            }

            if (ImGui.Button("Cut Gems")) {
                this.script.println("Gems");
                this.script.xpGained = 0;
                this.script.setBotState(MultiSkillerLite.BotState.CuttingGems);
                this.script.startTime = System.currentTimeMillis();

            }

            ImGui.Combo("Hotspots", TarMaker.pickedTar, TarMaker.tarsOptions);
            if (ImGui.Button("Start Making Tar")) {
                this.script.println("Making Tar");
                this.script.xpGained = 0;
                this.script.setBotState(MultiSkillerLite.BotState.MakeTar);
                this.script.startTime = System.currentTimeMillis();

            }


            if (ImGui.Button("Stop")) {
                this.script.setBotState(MultiSkillerLite.BotState.Idle);
            }

            ImGui.Text("Time since start: "+ this.script.getElapsedTime());
            ImGui.Text("XP Gained : "+ this.script.xpGained);
            ImGui.End();
        }
    }



    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
