package scripts.JarGenerator.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.JarGenerator.PuroJars;
import scripts.JarGenerator.antiban.Antiban;
import scripts.JarGenerator.data.Constants;
import scripts.JarGenerator.data.Vars;
import scripts.JarGenerator.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.objects.ObjectFinder;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.WalkingCondition;

public class HandleWalking extends Node {

	private String subStatus;

	public HandleWalking(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public boolean validate() {
		return (Utils.hasImplings() && !Constants.PURO.contains(Player.getPosition())) || (!Constants.BANK.contains(Player.getPosition()) && Utils.hasGenerator());
	}
	@Override
	public void execute() {
		grainFailsafe();
		if (Utils.hasImplings()) {
			if (Constants.CROP_AREA.contains(Player.getPosition())) {
				subStatus = "Puro-Puro";
				RSObject cropCircle = ObjectFinder.findFirst(15, Constants.CROP_CIRCLE);
				if (cropCircle != null) {
					if (!cropCircle.isClickable() || !cropCircle.isOnScreen()) {
						aCamera.turnToTile(cropCircle.getPosition());
					}
					if (AccurateMouse.click(cropCircle, "Enter")) {
						Timing.waitCondition(() -> {
							General.sleep(100, 200);
							return Constants.PURO.contains(Player.getPosition());
						}, General.randomLong(10000, 12000));
					}
				}
			} else {
				subStatus = "Crop Circle";
				DaxWalker.walkTo(Constants.CROP_AREA.getRandomTile());
			}
		} else if (Constants.PURO.contains(Player.getPosition()) && Utils.hasGenerator()) {
			RSObject portal = ObjectFinder.findFirst(10, Constants.EXIT_PORTAL);
			if (portal != null) {
				if (!portal.isClickable() || !portal.isOnScreen()) {
					aCamera.turnToTile(portal.getPosition());
				}
				subStatus ="Bank from Puro-Puro";
				if (AccurateMouse.click(portal, "Escape")) {
					Timing.waitCondition(() -> {
						General.sleep(100, 200);
						Antiban.timedActions();
						return !Constants.PURO.contains(Player.getPosition());
					}, General.randomLong(10000, 12000));
				}
			}
		} else {
			subStatus = "Bank";
			DaxWalker.walkTo(Constants.BANK.getRandomTile());
		}
	}

	public void grainFailsafe() {
		RSItem grain = OSInventory.findFirstNearestToMouse(Constants.GRAIN);
		if (grain != null) {
			if (AccurateMouse.click(grain, "Drop")) {
				Timing.waitCondition(() -> {
					General.sleep(100, 200);
					return Inventory.getCount(Constants.GRAIN) == 0;
				}, General.random(2000, 4000));
			}
		}
	}

	@Override
	public String status() {
		return "Walking to: " + subStatus;
	}
}
