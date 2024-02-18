package net.botwithus.debug;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;

import java.util.Map;

public class DebugGraphicsContext extends ScriptGraphicsContext {

    private final DebugScript script;



    public DebugGraphicsContext(ScriptConsole console, DebugScript script) {
        super(console);
        this.script = script;
    }


    public void renderVarbitDebug() {
        if(ImGui.Button("Hide all")) {
            for (Varbit value : script.varbits.values()) {
                value.setHidden(true);
            }
        }
        ImGui.SameLine();
        if(ImGui.Button("Unhide all")) {
            for (Varbit value : script.varbits.values()) {
                value.setHidden(false);
            }
        }
        ImGui.SameLine();
        if(ImGui.Button("Clear all")) {
            int[] keys = script.varbits.entrySet().stream().filter(entry -> !entry.getValue().isHidden())
                    .mapToInt(Map.Entry::getKey).toArray();
            for (int key : keys) {
                script.varbits.remove(key);
            }
        }
        ImGui.Separator();
        if(ImGui.BeginTable("##varbits_table", 4, 0)) {
            ImGui.TableSetupColumn("Varbit ID", 0);
            ImGui.TableSetupColumn("Varbit Value", 0);
            ImGui.TableSetupColumn("Domain", 0);
            ImGui.TableSetupColumn("Last Updated", 0);

            ImGui.TableHeadersRow();

            for (Varbit value : script.varbits.values()) {
                if(!value.isHidden()) {
                    ImGui.TableNextRow();

                    ImGui.TableNextColumn();
                    ImGui.Text("%d", value.getId());
                    ImGui.Separator();

                    ImGui.TableNextColumn();
                    ImGui.Text("%d", value.getValue());
                    ImGui.Separator();

                    ImGui.TableNextColumn();
                    ImGui.Text(value.getDomain().name());
                    ImGui.Separator();

                    ImGui.TableNextColumn();
                    ImGui.Text(value.getLastUpdated().toString());
                    ImGui.Separator();
                }
            }

            ImGui.EndTable();
        }
    }

    public void renderVarpDebug() {
        if(ImGui.Button("Hide all")) {
            for (Varp value : script.varps.values()) {
                value.setHidden(true);
            }
        }
        ImGui.SameLine();
        if(ImGui.Button("Unhide all")) {
            for (Varp value : script.varps.values()) {
                value.setHidden(false);
            }
        }
        ImGui.SameLine();
        if(ImGui.Button("Clear all")) {
            int[] keys = script.varps.entrySet().stream().filter(entry -> !entry.getValue().isHidden())
                    .mapToInt(Map.Entry::getKey).toArray();
            for (int key : keys) {
                script.varps.remove(key);
            }
        }
        ImGui.Separator();
        if(ImGui.BeginTable("##varp_table", 4, 0)) {
            ImGui.TableSetupColumn("Varp ID", 0);
            ImGui.TableSetupColumn("Varp Value", 0);
            ImGui.TableSetupColumn("Last Updated", 0);

            ImGui.TableHeadersRow();

            for (Varp value : script.varps.values()) {
                if(!value.isHidden()) {
                    ImGui.TableNextRow();

                    ImGui.TableNextColumn();
                    ImGui.Text("%d", value.getId());
                    ImGui.Separator();

                    ImGui.TableNextColumn();
                    ImGui.Text("%d", value.getValue());
                    ImGui.Separator();

                    ImGui.TableNextColumn();
                    ImGui.Text(value.getLastUpdated().toString());
                    ImGui.Separator();
                }
            }

            ImGui.EndTable();
        }
    }

    private String result = "";

    @Override
    public void drawSettings() {
        ImGui.SetWindowSize(200.f, 200.f);
        if(ImGui.Begin("Debug Settings", 0)) {
            if(ImGui.Button("Send Game Message")) {
                System.out.println("Running Script...");
                script.runScript = true;
            }
            /*if(ImGui.BeginTabBar("##variable_debug", 0)) {

                if(ImGui.BeginTabItem("Varps", 0)) {
                    renderVarpDebug();
                    ImGui.EndTabItem();
                }
                if(ImGui.BeginTabItem("Varbits", 0)) {
                    renderVarbitDebug();
                    ImGui.EndTabItem();
                }

                ImGui.EndTabBar();
            }*/
            ImGui.End();
        }
    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
