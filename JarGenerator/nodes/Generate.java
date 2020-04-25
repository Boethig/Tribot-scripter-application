package scripts.JarGenerator.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import scripts.JarGenerator.PuroJars;
import scripts.JarGenerator.antiban.Antiban;
import scripts.JarGenerator.data.Constants;
import scripts.JarGenerator.data.Vars;
import scripts.JarGenerator.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.dax_api.walker.utils.AccurateMouse;


public class Generate extends Node {

	public Generate(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() {
		return !Inventory.isFull() && Utils.hasGenerator() && Constants.BANK.contains(Player.getPosition()) && !Banking.isBankScreenOpen();
	}

	@Override
	public void execute() {
		RSItem generator = OSInventory.findFirstNearestToMouse(Constants.GENERATOR);
		if (generator != null) {
			if (Vars.get().isGenCharged) {
				while (!Inventory.isFull() && Vars.get().isGenCharged) {
					handleSelection();
					if (generate(generator)) {
						Vars.get().jarCount += 1;
					}
				}
			} else {
				destroy(generator);
			}
		}
	}

	public void handleSelection() {
		if (Game.getItemSelectionState() == 1 && Game.getSelectedItemName().equals("Impling Jar")) {
			RSItem jar = OSInventory.findFirstNearestToMouse(Constants.JAR);
			if (jar != null) {
				if (AccurateMouse.click(jar)) {
					Timing.waitCondition(() -> {
						General.sleep(100, 200);
						return Game.getItemSelectionState() == 0;
					}, General.random(1500, 2500));
				}
			}
		}
	}

	public boolean generate(RSItem generator) {
		int jarCount = Inventory.getCount(Constants.JAR);
		return AccurateMouse.click(generator, "Impling-jar") && Timing.waitCondition(() -> {
			General.sleep(50, 70);
			return Inventory.getCount(Constants.JAR) > jarCount;
		}, General.random(1500, 2500));
	}

	public void destroy(RSItem generator) {
		if (Interfaces.isInterfaceSubstantiated(584)) {
			RSInterface destroy = Interfaces.get(584, 1);
			if (destroy != null) {
				if (AccurateMouse.click(destroy)) {
					Timing.waitCondition(() -> {
						General.sleep(100, 200);
						return Inventory.getCount(Constants.GENERATOR) == 0;
					}, General.random(2000, 3000));
				}
			}
		} else {
			if (AccurateMouse.click(generator, "Destroy")) {
				Timing.waitCondition(() -> {
					General.sleep(100, 200);
					return Interfaces.isInterfaceSubstantiated(584, 1);
				}, General.random(2000, 4000));
			}
		}
	}

	@Override
	public String status() {
		return "Generating Impling Jars";
	}
}

