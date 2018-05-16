package scripts.DemonicGorillas.Methods;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;

import scripts.DemonicGorillas.Antiban;
import scripts.DemonicGorillas.Constants;
import scripts.DemonicGorillas.DemonicGorillas;
import scripts.DemonicGorillas.Node;
import scripts.DemonicGorillas.Utils;
import scripts.DemonicGorillas.Variables;

public class Bank extends Node {
	
	public boolean validate() {
		return nearBooth() && (!Utils.doWeHaveFood() || !Utils.doWeHavePotion(Variables.PrayerPot) || Inventory.isFull());
	}

	@Override
	public void execute() {
		DemonicGorillas.status = "Banking";		
		if (!Banking.isBankScreenOpen()) {
			if (Banking.openBank()) {
				Timing.waitCondition(new Condition() {
						public boolean active() {
							General.sleep(100,300);
							return Banking.isBankScreenOpen();
						}
				}, General.random(2500,3500));
			}
		}
		else {
			if(Utils.isBankItemsLoaded()) {
				if(!doesBankHaveFood() || !doesBankHavePotions()) {
					DemonicGorillas.stopScript = true;
				}
					Banking.depositAllExcept(Variables.foodID,Constants.SEED,Constants.PRAY4,Constants.RANGE4, Constants.SUPER_COMBAT4, Variables.Range_weapon, Variables.Melee_weapon);
					Withdraw();
					Banking.close();
			}
		}
	}
	
	private boolean nearBooth() {
		RSObject[] booth = Objects.findNearest(10, "Bank Booth");
		if (booth.length > 0) {
			if (PathFinding.canReach(booth[0].getPosition(), true) && booth[0].getPosition().distanceTo(Player.getPosition()) < 8 
					&& Player.getPosition().distanceTo(Constants.BANK_SPOT) < 8) {
				return true;
			}
		}
		return false;
	}
	private boolean doesBankHaveFood() {
		return Banking.find(Variables.foodID).length > 0;
	}
	private boolean doesBankHavePotions() {
		return Banking.find(Constants.PRAY4).length > 0 || Banking.find(Constants.SUPER_COMBAT4).length > 0 || Banking.find(Constants.PRAY4).length > 0;
	}
	private void Withdraw() {
		//Find all pots still in inventory
	int ppot = Inventory.getCount(Constants.PRAY4);
	int seed = Inventory.getCount(Constants.SEED);
	
	if(seed < 1) {
		if (Banking.withdraw(1, Constants.SEED)) {
            Timing.waitCondition(new Condition() {
                public boolean active() {
                    General.sleep(100, 200);
                    return Inventory.getCount(Constants.SEED) > 0;
                }
            }, General.random(3000, 4000));
		}
	}	
	if(Inventory.getCount(Constants.SUPER_COMBAT4) != -1) {
		if (Banking.withdraw(1, Constants.SUPER_COMBAT4)) {
            Timing.waitCondition(new Condition() {
                public boolean active() {
                    General.sleep(100, 200);
                    return Inventory.getCount(Constants.SUPER_COMBAT4) > 0;
                }
            }, General.random(3000, 4000));
		}
	}
	if(Inventory.getCount(Constants.RANGE4) != 1) {
		if (Banking.withdraw(1, Constants.RANGE4)) {
            Timing.waitCondition(new Condition() {
                public boolean active() {
                    General.sleep(100, 200);
                    return Inventory.getCount(Constants.RANGE4) > 0;
                }
            }, General.random(3000, 4000));
		}
	}
	if(Inventory.getCount(Constants.PRAY4) != Variables.ppotCount) {
		if (Banking.withdraw(Variables.ppotCount-ppot, Constants.PRAY4)) {
            Timing.waitCondition(new Condition() {
                public boolean active() {
                    General.sleep(100, 200);
                    return Inventory.getCount(Constants.PRAY4) > 0;
                }
            }, General.random(3000, 4000));
		}
	}
		if (Banking.withdraw(0, Variables.foodID)) {
            Timing.waitCondition(new Condition() {
                public boolean active() {
                    General.sleep(100, 200);
                    return Inventory.isFull();
                }
            }, General.random(3000, 4000));
		}	
	}
}
