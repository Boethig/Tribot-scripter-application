package scripts.POHplanks.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import scripts.POHplanks.antiban.Antiban;
import scripts.POHplanks.data.Constants;
import scripts.POHplanks.data.Vars;
import scripts.POHplanks.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Bank extends Node {

	public Bank(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public String status() {
		return "Banking: ";
	}
	
	@Override
	public boolean validate() {
		return Utils.get().isInBank() || !Utils.get().canMakePlanks();
	}
	
	@Override
	public void execute() {
		Antiban.timedActions();
		if (Utils.get().isInBank()) {
			if (Banking.isBankScreenOpen() && Banking.isBankLoaded()) {
				if (!Utils.get().bankHasLogs()) {
					Vars.get().shouldRun = false;
					Vars.get().errorMessage = "No logs left found in bank";
				}
				Banking.depositAllExcept(Constants.COINS, Constants.LAW_RUNE, Constants.AIR_RUNE, Constants.EARTH_RUNE, Vars.get().plank.getLogId());
				if (Vars.get().useRingOfLife && !Utils.hasRingOfLife()) {
					if (Utils.bankHasRingOfLife()) {
						if (Banking.withdraw(1, Constants.RING_OF_LIFE)) {
							Timing.waitCondition(() -> Inventory.getCount(Constants.RING_OF_LIFE) > 0, General.random(2000, 3000));
							RSItem ringOfLife = OSInventory.findFirstNearestToMouse(Constants.RING_OF_LIFE);
							if (ringOfLife != null) {
								if (AccurateMouse.click(ringOfLife, "Wear")) {
									Antiban.getReactionTime();
									Timing.waitCondition(() -> Equipment.isEquipped(Constants.RING_OF_LIFE), General.random(3000, 4000));
									Antiban.sleepReactionTime();
								}
							}
						}
					}
				}
				if (!Utils.get().hasLogs()) {
					Vars.get().subStatus = "Withdrawing logs";
					if (Banking.withdraw(0, Vars.get().plank.getLogId())) {
						Antiban.getReactionTime();
						Timing.waitCondition(() -> Utils.get().hasLogs(), General.random(3000, 5000));
						Antiban.sleepReactionTime();
					}
				}
				if (Utils.get().hasLogs() && Utils.get().hasCoins() && !Utils.get().hasPlanks() && (!Vars.get().useRingOfLife || Utils.hasRingOfLife())) {
					Vars.get().subStatus = "Closing bank";
					if (Banking.close()) {
						Timing.waitCondition(() -> !Banking.isBankScreenOpen(), General.random(4000, 6000));
					}
				}
			} else {
				Vars.get().subStatus = "Opening bank";
				Utils.get().openBankAndWait();
			}
		} else {
			Vars.get().subStatus = "Teleporting to bank";
			teleportToBank();
		}
	}

	private void teleportToBank() {
		if (!Utils.get().doWeHaveRunes()) {
			Vars.get().shouldRun = false;
			Vars.get().errorMessage = "Could not teleport to bank: no runes for spell found";
		}
		Antiban.switchTab(GameTab.TABS.MAGIC);
		if (Magic.selectSpell(Vars.get().teleport)) {
			Timing.waitCondition(() -> Utils.get().isInBank(), General.random(5500, 6500));
		}
	}
}
