package scripts.AirTalisman.tasks;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.AirTalisman.data.Constants;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;

public class Walking extends Node {

    public Walking(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public boolean validate() {
        return Inventory.getCount(Constants.AIR_TALISMAN) >= Constants.MAX_DROP_LIMIT && Constants.QUEST_START.contains(Player.getPosition()) ||
                (Banking.isInBank() && !Banking.isBankScreenOpen());
    }

    @Override
    public void execute() {
        if (Constants.QUEST_START.contains(Player.getPosition())) {
            DaxWalker.walkToBank(RunescapeBank.LUMBRIDGE_TOP);
        } else {
            DaxWalker.walkTo(Constants.QUEST_START.getRandomTile());
        }
    }

    @Override
    public String status() {
        return "Walking";
    }
}
