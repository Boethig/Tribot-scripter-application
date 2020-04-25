package scripts.JarGenerator.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import scripts.JarGenerator.PuroJars;
import scripts.JarGenerator.antiban.Antiban;
import scripts.JarGenerator.data.Constants;
import scripts.JarGenerator.data.Vars;
import scripts.JarGenerator.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;

public class Bank extends Node {

	public Bank(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public boolean validate() {
		return Constants.BANK.contains(Player.getPosition()) && (Inventory.isFull() || !Utils.hasGenerator());
	}

	@Override
	public void execute() {
		Antiban.timedActions();
		if (Banking.isBankScreenOpen() && Banking.isBankLoaded()) {
			if (!Utils.bankHasImplings()) {
				Vars.get().shouldRun = false;
			}
			Banking.depositAllExcept(Constants.GENERATOR, Constants.NAT_IMPLING, Constants.ECL_IMPLING, Constants.ESS_IMPLING);
			if (Utils.hasGenerator() && Inventory.getCount(Constants.JAR) == 0) {
				Banking.close();
			} else {
				if (Inventory.getCount(Constants.NAT_IMPLING) < 1 && Banking.find(Constants.NAT_IMPLING).length > 0) {
					withdraw(1, Constants.NAT_IMPLING);
				}
				if (Inventory.getCount(Constants.ECL_IMPLING) < 2 && Banking.find(Constants.ECL_IMPLING).length > 0) {
					withdraw(2, Constants.ECL_IMPLING);
				}
				if (Inventory.getCount(Constants.ESS_IMPLING) < 3 && Banking.find(Constants.ESS_IMPLING).length > 0) {
					withdraw(3, Constants.ESS_IMPLING);
				}
				if (Utils.hasImplings()) {
					Banking.close();
				}
			}
		} else {
			if (Banking.openBank()) {
				Timing.waitCondition(() -> {
					General.sleep(100, 300);
					return Banking.isBankScreenOpen();
				}, General.random(2000, 4000));
			}
		}
	}

	private boolean withdraw(int count, int item) {
		if (Banking.withdraw(count, item)) {
			return Timing.waitCondition(() -> {
				General.random(100, 300);
				return Inventory.getCount(item) > count;
			}, General.random(3000, 5000));
		}
		return false;
	}

	@Override
	public String status() {
		return "Banking";
	}
}
