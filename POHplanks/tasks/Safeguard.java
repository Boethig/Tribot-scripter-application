package scripts.POHplanks.tasks;

import scripts.POHplanks.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;

public class Safeguard extends Node {

    public Safeguard(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public boolean validate() {
        return Utils.get().getInHousePortal() == null && !Utils.get().isInSafeArea();
    }

    @Override
    public void execute() {
        Utils.get().teleportToSafety();
    }

    @Override
    public String status() {
        return "Teleporting to safety";
    }
}
