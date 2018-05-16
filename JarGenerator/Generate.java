package scripts.JarGenerator;

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
					PuroJars.status = "Destroying Generator";
					if (Clicking.click("Destroy", generator[0])) {
						Antiban.waitItemInteractionDelay();
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								General.sleep(100, 200);
								return Interfaces.isInterfaceValid(584);
									}
								}, General.random(1000,2000));
						}
					if (Interfaces.isInterfaceValid(584)) {
						RSInterface destroy = Interfaces.get(584,1);							
						if ( destroy != null && Clicking.click(destroy)) {
							Timing.waitCondition(new Condition() {
								@Override
								public boolean active() {
									General.sleep(100, 200);
									return Inventory.getCount(Vars.generator) == 0;
										}
									}, General.random(1000,2000));
								}
							}				
						}
				else {
				while (!Inventory.isFull() && Vars.Gen && !Banking.isBankScreenOpen()) {
					// Jar Failsafe
					if(Game.getItemSelectionState() == 1) {
						PuroJars.status = "Unselecting Jar";
						if(Game.getSelectedItemName().equals("Impling jar")) {
							RSItem[] jar = Inventory.find(Vars.jar);
							if (jar.length > 0) {
								if (Clicking.click(jar[0])) {
									Timing.waitCondition(new Condition() {
										@Override
										public boolean active() {
											General.sleep(100, 200);
										return Game.getItemSelectionState() == 0;
											}
										}, General.random(1500,2500));
									}
								}
						}
					}
					int jars = Inventory.getCount(Vars.jar);
					if (Clicking.click("Impling-jar", generator[0])) {
						Antiban.waitItemInteractionDelay(General.random(1, 7));
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								General.sleep(100, 200);
								return Inventory.getCount(Vars.jar) > jars;
								}
							}, General.random(300,500));
							//Counters
							if (Inventory.getCount(Vars.jar) > jars) {
								//++Vars.count;
								//System.out.println(Vars.count);
								++Vars.jarC;
									}
								}
							}
						}
					}
				}
		}
	
	public static void generate() {		
		RSItem[] generator = Inventory.find(Vars.generator);
		if (generator.length > 0) 
		{
		// Jar Failsafe
		if(Game.getItemSelectionState() == 1) {
			PuroJars.status = "Unselecting Jar";
			if(Game.getSelectedItemName().equals("Impling jar")) {
				RSItem[] jar = Inventory.find(Vars.jar);
				if (jar.length > 0) {
					if (Clicking.click(jar[0])) {
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								General.sleep(100, 200);
							return Game.getItemSelectionState() == 0;
								}
							}, General.random(1500,2500));
						}
					}
			}
		}
		int jars = Inventory.getCount(Vars.jar);
		if (Clicking.click("Impling-jar", generator[0])) {
			Antiban.waitItemInteractionDelay(General.random(1, 7));
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.sleep(100, 200);
					return Inventory.getCount(Vars.jar) > jars;
					}
				}, General.random(300,500));
				//Counters
				if (Inventory.getCount(Vars.jar) > jars) {
					//++Vars.count;
					//System.out.println(Vars.count);
					++Vars.jarC;
				}
		}
		}
	}
}

