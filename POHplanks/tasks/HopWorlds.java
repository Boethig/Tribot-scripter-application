package scripts.POHplanks.tasks;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.RSInterface;
import scripts.POHplanks.data.Vars;
import scripts.POHplanks.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.dax_api.walker.utils.AccurateMouse;

public class HopWorlds extends Node {

    public HopWorlds(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public boolean validate() {
        return WorldHopper.getWorld() != Vars.get().pvpWorld;
    }

    @Override
    public void execute() {
        if (Utils.get().isInSafeArea()) {
            Vars.get().subStatus = "Switching to PVP world";
            if (Interfaces.isInterfaceSubstantiated(193)) {
                RSInterface highRiskWorld = Interfaces.get(193);
                if (highRiskWorld != null) {
                    RSInterface switchToWorld = Utils.get().getInterfaceWithAction(highRiskWorld, "Switch to it");
                    if (switchToWorld != null) {
                        AccurateMouse.click(switchToWorld, "Switch to it");
                    }
                }
            } else {
                WorldHopper.changeWorld(Vars.get().pvpWorld);
            }
        } else {
            Vars.get().subStatus = "Teleporting to safety before hopping";
            Utils.get().teleportToSafety();
        }
    }

    @Override
    public String status() {
        return "Hopping:";
    }
}
