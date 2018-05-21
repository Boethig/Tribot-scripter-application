package scripts.scripts.JarGenerator;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;

import scripts.webwalker_logic.WebWalker;

public class Generate extends Node {

	public Generate(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public boolean validate() {
		return !Inventory.isFull() && Banking.isInBank() && !Banking.isBankScreenOpen();
	}

	@Override
	public void execute() {
				
		if (Inventory.getCount(Vars.generator) == 1) {
			PuroJars.status = "Generating jars";			
			RSItem[] generator = Inventory.find(Vars.generator);
			if (generator.length > 0) {
				if (!Vars.Gen) {
					// We want to destroy the generator
					PuroJars.status = "Destroying Generator";
					if (Clicking.click("Destroy", generator[0])) {
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								General.sleep(100, 200);
								return Interfaces.isInterfaceValid(584);
							}
						}, General.random(1000,2000));
					}
					// Destroy interface is valid
					if (Interfaces.isInterfaceValid(584)) {
						RSInterface destroy = Interfaces.get(584,1);							
						if ( destroy != null && Clicking.click(destroy)) {
							Timing.waitCondition(new Condition() {
								@Override
								public boolean active() {
									General.sleep(100, 200);
									return Inventory.getCount(Vars.generator) < 1;
								}
							}, General.random(1000,2000));
						}
					}				
				} 
				else {
					// We want to Generate jars
					generateJars(generator[0]);
				}
			}
		}
	}
	
	public static void generateJars(RSItem generator) {
		// Generate loop
		while (!Inventory.isFull() && Vars.Gen && !Banking.isBankScreenOpen()) {		
			// Selected Jar Failsafe
			if (Game.getItemSelectionState() == 1) {
				PuroJars.status = "Unselecting Jar";
				if (Game.getSelectedItemName().equals("Impling jar")) {
					RSItem[] jar = Inventory.find(Vars.jar);
					if (jar.length > 0) {
						if (Clicking.click(jar[0])) {
							Timing.waitCondition(new Condition() {
								@Override
								public boolean active() {
									General.sleep(100, 200);
									return Game.getItemSelectionState() == 0;
								}
							}, General.random(1000,2000));
						}
					}
				}
			}
			// We want to use generator
			int jars = Inventory.getCount(Vars.jar);
			int sleep = Antiban.getReactionTime();
			if (Clicking.click("Impling-jar", generator)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return Inventory.getCount(Vars.jar) > jars;
					}
				}, General.random(100,200));
				// We have sucessfully generated a jar, increment counter
				if (Inventory.getCount(Vars.jar) > jars) {
					Vars.jar += 1;
					System.out.println("Jar generated");
				}
				// Wait reaction time before clicking again
				General.println("ABC2 generated reaction time: " + sleep);
				Antiban.sleepReactionTime();
				Antiban.generateTrackers(300);
			}
		}
	}
}

