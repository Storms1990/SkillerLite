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

            this.script.setLogout(ImGui.Checkbox("Logout when done?",this.script.logoutBool()));
            ImGui.SameLine();
            ImGui.Text("Logout Status: " + MultiSkillerLite.Logout);
            if (ImGui.Button("Stop Script")) {
                this.script.setBotState(MultiSkillerLite.BotState.Idle);
            }
            ImGui.Text("Time since start: "+ this.script.getElapsedTime());
            ImGui.Text("XP Gained : "+ this.script.xpGained);
            ImGui.Text("***********************************");

            ImGui.Text("Important Note: all these scripts function off of having a bank preset already loaded");
            ImGui.Text("So make sure your Preset is loaded!!!!");
            ImGui.Text("Will use Portable or Well if nearby");

            ImGui.Text("*********** Potion Mixer **************");
            ImGui.Text("Important Note: Make sure you START the script with first batch of ingredients already in Backpack.");
            ImGui.Text("Takes a snapshot on start and will run until no longer matches snapshot");
            ImGui.Text("Will only make certain potions without portable well nearby");

            if (ImGui.Button("Start Making Potions")) {
                this.script.println(" Making Potions");
                this.script.startTime = System.currentTimeMillis();
                this.script.xpGained = 0;
                MultiSkillerLite.returnState = MultiSkillerLite.BotState.MixPotions;
                this.script.setBotState(MultiSkillerLite.BotState.SetInitialInventory);
            }
            ImGui.Text("*********** Unfinished Potions ****************");
            ImGui.Text("Makes in inventory, should work at any banker or bank chest");
            if (ImGui.Button("Make Unfinished ")) {
                this.script.startTime = System.currentTimeMillis();
                this.script.xpGained = 0;
                MultiSkillerLite.returnState = MultiSkillerLite.BotState.UnfinishedPotions;
                this.script.setBotState(MultiSkillerLite.BotState.SetInitialInventory);
            }

            ImGui.Text("*********** Herb Cleaner ****************");
            ImGui.Text("should work at any banker or bank chest");
            if (ImGui.Button("Clean Herbs")) {
                this.script.println("Cleaning Herbs");
                this.script.startTime = System.currentTimeMillis();
                this.script.xpGained = 0;
                MultiSkillerLite.returnState = MultiSkillerLite.BotState.CleanHerb;
                this.script.setBotState(MultiSkillerLite.BotState.CleanHerb);
            }
            ImGui.Text("*********** Gem Cutter ****************");
            ImGui.Text("Will prioritize using portables");
            if (ImGui.Button("Start Cutting Gems")) {
                this.script.println("Cutting Gems");
                this.script.xpGained = 0;
                MultiSkillerLite.returnState = MultiSkillerLite.BotState.CuttingGems;
                this.script.setBotState(MultiSkillerLite.BotState.CuttingGems);
                this.script.startTime = System.currentTimeMillis();

            }
            ImGui.Text("*********** Gold bar maker ****************");
            ImGui.Text("Best to run in fort or artisans workshops ");
            this.script.setFamilyCrestCheck(ImGui.Checkbox("Family Crest Completed?",this.script.FamilyCrestCheckBool()));

            if (ImGui.Button("Start making bars")) {
                this.script.println("Gold maker");
                this.script.xpGained = 0;
                MultiSkillerLite.returnState = MultiSkillerLite.BotState.GoldMaker;
                this.script.setBotState(MultiSkillerLite.BotState.GoldMaker);
                this.script.startTime = System.currentTimeMillis();

            }
            ImGui.Text("*********** Cooker **************");
            ImGui.Text("Currently only works in Fort will prioritize Using portables");
            this.script.setPortableRange(ImGui.Checkbox("Use Portable Range?",this.script.portableRangeBool()));
            ImGui.Text("***********************************");

            if (ImGui.Button("Start Cooking")) {
                this.script.println("Cooking");
                this.script.xpGained = 0;
                MultiSkillerLite.returnState = MultiSkillerLite.BotState.Cooking;
                this.script.setBotState(MultiSkillerLite.BotState.Cooking);
                this.script.startTime = System.currentTimeMillis();

            }
            ImGui.Text("*********** Tar Maker **************");
            ImGui.Text("Should work at any banker or bank chest");
            ImGui.Text("Select which you want to make");
            ImGui.Combo("Herbs", TarMaker.pickedTar, TarMaker.tarsOptions);
            if (ImGui.Button("Start Making Tar")) {
                this.script.println("Making Tar");
                this.script.xpGained = 0;
                MultiSkillerLite.returnState = MultiSkillerLite.BotState.MakeTar;
                this.script.setBotState(MultiSkillerLite.BotState.MakeTar);
                this.script.startTime = System.currentTimeMillis();

            }

            ImGui.End();
        }
    }



    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
