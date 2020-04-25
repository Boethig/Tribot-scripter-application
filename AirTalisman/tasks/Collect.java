package scripts.AirTalisman.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.AirTalisman.data.Constants;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.InteractionHelper;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public class Collect extends Node {

    public Collect(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public boolean validate() {
        return Constants.QUEST_START.contains(Player.getPosition()) && NPCs.find("Duke Horacio").length > 0;
    }

    @Override
    public void execute() {

        RSGroundItem[] groundTalismans = GroundItems.find(Constants.AIR_TALISMAN);
        General.println(groundTalismans.length);
        RSItem[] talismans = OSInventory.findNearestToMouse(Constants.AIR_TALISMAN);

        if (groundTalismans.length >= Constants.MAX_DROP_LIMIT) {
            for (final RSGroundItem talisman : groundTalismans) {
                if (talisman != null) {
                    if (talisman.isOnScreen() && talisman.isClickable()) {
                        pickUpTalisman(talisman);
                    } else aCamera.turnToTile(talisman.getPosition());
                }
            }
        } else if (talismans.length > 0) {
            int count = Inventory.getAll().length;
            if (Inventory.drop(talismans) > 0) {
                Timing.waitCondition(() -> Inventory.getAll().length < count, General.random(2000, 4000));
            }

        } else if (NPCInteraction.isConversationWindowUp()) {
            NPCInteraction.handleConversation(Constants.OPTIONS);
        } else {
            RSNPC duke = getDukeHoratio();
            if (duke != null) {
                if (InteractionHelper.click(duke, "Talk-to")) {
                    NPCInteraction.waitForConversationWindow();
                }
            }
        }
    }


    public boolean pickUpTalisman(RSGroundItem talisman) {
        int count = Inventory.getAll().length;
        if (AccurateMouse.click(talisman, "Take")) {
            return Timing.waitCondition(() -> Inventory.getAll().length > count, General.random(2000, 4000));
        }
        return false;
    }

    public RSNPC getDukeHoratio() {
        RSNPC[] duke = NPCs.find("Duke Horacio");
        return duke.length > 0 ? duke[0] : null;
    }

    @Override
    public String status() {
        return "Collecting Talismans";
    }
}
