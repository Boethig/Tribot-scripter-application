package scripts.DemonicGorillas;

	import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
	import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Interfaces;
	import org.tribot.api2007.Inventory;
	import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;

import scripts.DemonicGorillas.Methods.Attack;


	public class Utils {
		
		// Method for determining if we have food
		public static boolean doWeHaveFood() {
			return Inventory.find(Variables.foodID).length > 0;
		}
		public static boolean doWeHavePotion(int []id) {
			return Inventory.find(id).length > 0;
		}
		
		// Method to get hp percent
	    public static double hpPercent(){
	        double currentHP = Skills.SKILLS.HITPOINTS.getCurrentLevel();
	        double totalHP = Skills.SKILLS.HITPOINTS.getActualLevel();
	        return currentHP * 100 / totalHP;   
	    }
	    
	    //Check if items are equipped
	    public static boolean isEquipped(int id) {
	    	return Equipment.isEquipped(id);
	    }
	    //Do we have item in inventory?
	    public static boolean doWeHaveItem(int id) {
	    	return Inventory.find(id).length > 0;
	    }
	    
	    // Step a tile away
	    public static void stepTile() {
	    	
	    	RSTile tile = new RSTile(Player.getPosition().getX()-General.random(-2,2), Player.getPosition().getY()-General.random(-2, 2),0);
	    	//Generated 0,0
	    	if(!Locations.GORILLAS.contains(tile)){
	    		stepTile();
	    	}
	    	else if(tile.equals(Player.getPosition())){
	    		stepTile();
	    	}
	    	else {
			if (tile.isClickable() && Clicking.click("Walk here", tile)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Player.getPosition() == tile;
						}
					}, General.random(1000, 2000));
				}
	    	}
	    }
	    
	    // Enable Prayer
	    public static void enablePrayer(Prayer.PRAYERS pray) {
	    	if(!Prayer.isPrayerEnabled(pray)) {
				if (Prayer.enable(pray)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Prayer.isPrayerEnabled(pray);
						}
					}, General.random(2000, 3000));
				}
			}
	    }
	    
	    // Equip item
	    public static void equipItem(int id) {
	    	RSItem[] weapon = Inventory.find(id);
			if (weapon.length > 0) {
				if (Clicking.click("Wield", weapon[0])) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Utils.isEquipped(id);
						}
					}, General.random(1500, 2200));
					Antiban.waitItemInteractionDelay();
				}
			}
	    }
		
		// Determine whether accidently misclicked and went up the ladder in house
		public static boolean isUpLadder() {
			return Player.getPosition().getPlane() == 1;
		}
		
		// Checks if bank items are loaded or not
		public static boolean isBankItemsLoaded() {
	        return getCurrentBankSpace() == Banking.getAll().length;
	    }
		
		// Gets current bank space
		public static int getCurrentBankSpace() {
			RSInterface amount = Interfaces.get(Constants.BANK_MASTER,Constants.BANK_AMOUNT_CHILD);
			if (amount != null) {
				String txt = amount.getText();
				if (txt != null) {
					try {
						int toInt = Integer.parseInt(txt);
						if (toInt > 0)
							return toInt;
					} catch (NumberFormatException e) {
						return -1;
					}
				}
			}
			return -1;
		}
		
		/**
		 * Method to calculate average wait time from a List of times
		 * @param times 
		 * @return
		 */
		public static int calculateAverage(List<Integer> times) {
			Integer sum = 0;
			if (!times.isEmpty()) {
				for (Integer holder : times) {
					sum += holder;
				}
				return sum.intValue() / times.size();
			}
			return sum;
		}
	}
