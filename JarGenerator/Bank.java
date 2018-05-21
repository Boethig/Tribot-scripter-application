package scripts.scripts.JarGenerator;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;

import scripts.JarGenerator.Antiban;


public class Bank extends Node {

	public Bank(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public boolean validate() {
		return (( Inventory.isFull()) && Banking.isInBank())
				|| Banking.isInBank() && Inventory.getCount(Vars.generator) == 0;
	}
	@Override
	public void execute() {
		PuroJars.status = "Banking";
		// Jar Failsafe
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
							}, General.random(1500,2500));
						Antiban.waitItemInteractionDelay(General.random(1, 5));
						}
					}
			}
		}
		// We Want to open bank
		if (!Banking.isBankScreenOpen()) {
			if (Banking.openBank()) {
				Timing.waitCondition(new Condition() {
						public boolean active() {
							General.sleep(100,300);
							return Banking.isBankScreenOpen();
						}
				}, General.random(2000,3000));
			}
		}		
		else {
			//Counters
			RSItem[] nat = Banking.find(Vars.nature);
			RSItem[] ess = Banking.find(Vars.essence);
			RSItem[] ecl = Banking.find(Vars.eclectic);
			RSItem[] energy = Banking.find(Vars.energy1,Vars.energy2,Vars.energy3,Vars.energy4);		
			//Ends script if no impling jars are found
			if (nat.length == 0 || ess.length == 0 || ecl.length == 0) {
				PuroJars.stopScript = true;
			}			
			Banking.depositAllExcept(Vars.generator,Vars.energy1,Vars.energy2,Vars.energy3,Vars.energy4,Vars.eclectic,Vars.nature,Vars.essence);
				
			if (Inventory.getCount(Vars.generator) == 0 && !Vars.Gen) {
				//withdraw energy potions if necessary
				if (Inventory.getCount(Vars.energy1,Vars.energy2,Vars.energy3,Vars.energy4) == 0 && energy.length > 0){
					if(Banking.withdraw(1, Vars.energy4)){
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								General.sleep(100, 200);
								return Inventory.getCount(Vars.energy4) > 0;
							}
						}, General.random(1000,2000));
					}
				}
				Withdraw();
			}
			Banking.close();
		}
	}
	
	public void Withdraw()
	{
		int nat = Inventory.getCount(Vars.nature);
		int ess = Inventory.getCount(Vars.essence);
		int ecl = Inventory.getCount(Vars.eclectic);
		
		if (nat < 1) {
			if(Banking.withdraw(1, Vars.nature)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Inventory.getCount(Vars.nature) == 1;
					}
				}, General.random(1000,2000));
			}
		}
		if (ecl < 2) {
			if(Banking.withdraw(2-ecl, Vars.eclectic)){
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Inventory.getCount(Vars.eclectic) == 2;
					}
				}, General.random(1000,2000));
			}
		}
		if (ess < 3) {
			if (Banking.withdraw(3-ess, Vars.essence)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Inventory.getCount(Vars.essence) == 3;
					}
				}, General.random(1000,2000));
			}
		}
	}
	
	public void randomWithdraw(){
		int rand = General.random(1, 6);
		if (Vars.randBank > 90) {
			rand = Vars.bankType;
		}
		int nat = Inventory.getCount(Vars.nature);
		int ess = Inventory.getCount(Vars.essence);
		int ecl = Inventory.getCount(Vars.eclectic);
		if(rand == 1){
			if (nat < 1) {
				if(Banking.withdraw(1, Vars.nature)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.nature) == 1;
						}
					}, General.random(1500,2500));
				}
			}
			if (ecl < 2) {
				if(Banking.withdraw(2, Vars.eclectic)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.eclectic) == 2;
						}
					}, General.random(1500,2500));
				}
			}
			if (ess < 3) {
				if(Banking.withdraw(3, Vars.essence)) {
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.essence) == 3;
						}
					}, General.random(1500,2500));
				}
			}
			Vars.randBank = General.random(1, 100);
			Vars.bankType = rand;
		}
		else if(rand == 2) {
			if (nat < 1) {
				if(Banking.withdraw(1, Vars.nature)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.nature) == 1;
						}
					}, General.random(1500,2500));
				}
			}
			if (ess < 3) {
				if(Banking.withdraw(3, Vars.essence)) {
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.essence) == 3;
						}
					}, General.random(1500,2500));
				}
			}
			if (ecl < 2) {
				if(Banking.withdraw(2, Vars.eclectic)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.eclectic) == 2;
						}
					}, General.random(1500,2500));
				}
			}
			Vars.randBank = General.random(1, 100);
			Vars.bankType = rand;
		}
		else if(rand == 3){
			if (ecl < 2) {
				if(Banking.withdraw(2, Vars.eclectic)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.eclectic) == 2;
						}
					}, General.random(1500,2500));
				}
			}
			if (nat < 1) {
				if(Banking.withdraw(1, Vars.nature)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.nature) == 1;
						}
					}, General.random(1500,2500));
				}
			}
			if (ess < 3) {
				if(Banking.withdraw(3, Vars.essence)) {
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.essence) == 3;
						}
					}, General.random(1500,2500));
				}
			}
			Vars.randBank = General.random(1, 100);
			Vars.bankType = rand;
		}
		else if(rand == 4){
			if (ecl < 2) {
				if(Banking.withdraw(2, Vars.eclectic)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.eclectic) == 2;
						}
					}, General.random(1500,2500));
				}
			}
			if (ess < 3) {
				if(Banking.withdraw(3, Vars.essence)) {
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.essence) == 3;
						}
					}, General.random(1500,2500));
				}
			}
			if (nat < 1) {
				if(Banking.withdraw(1, Vars.nature)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.nature) == 1;
						}
					}, General.random(1500,2500));
				}
			}
			Vars.randBank = General.random(1, 100);
			Vars.bankType = rand;
		}
		else if(rand == 5){
			if (ess < 3) {
				if(Banking.withdraw(3, Vars.essence)) {
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.essence) == 3;
						}
					}, General.random(1500,2500));
				}
			}
			if (ecl < 2) {
				if(Banking.withdraw(2, Vars.eclectic)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.eclectic) == 2;
						}
					}, General.random(1500,2500));
				}
			}
			if (nat < 1) {
				if(Banking.withdraw(1, Vars.nature)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.nature) == 1;
						}
					}, General.random(1500,2500));
				}
			}
			Vars.randBank = General.random(1, 100);
			Vars.bankType = rand;
		}
		else if(rand == 6) {
			if (ess < 3) {
				if(Banking.withdraw(3, Vars.essence)) {
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.essence) == 3;
						}
					}, General.random(1500,2500));
				}
			}
			if (nat < 1) {
				if(Banking.withdraw(1, Vars.nature)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.nature) == 1;
						}
					}, General.random(1500,2500));
				}
			}
			if (ecl < 2) {
				if(Banking.withdraw(2, Vars.eclectic)){
					Antiban.waitItemInteractionDelay(General.random(2, 6));
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.eclectic) == 2;
						}
					}, General.random(1500,2500));
				}
			}
			Vars.randBank = General.random(1, 100);
			Vars.bankType = rand;
		}
	}

}
