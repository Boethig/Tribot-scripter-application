package scripts.AASkeletons;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;

import scripts.webwalker_logic.WebWalker;

public class Attack extends Node {
	public Attack(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}

	public int[] prayerPotion = {4,3,2,1};
	public int[] antiPoison;
	public int[] greeGree;
	public int[] Chins;
	public int skele;
	

	@Override
	public boolean validate() {
		// We are in the training location
		if (Vars.area.contains(Player.getPosition())) {
			// We have Prayer potions
			if (doWeHavePotion(prayerPotion)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void execute() {
		// Walk to main area tile if not already
		if (Player.getPosition() != Vars.areaTile) {
			WebWalker.walkTo(Vars.areaTile);
		}
		// Enable Prayer if not already
		enablePrayer(PRAYERS.PROTECT_FROM_MELEE);
		
		switch(getTask()) {
		case 0:
			drinkPotion(prayerPotion);
			break;
		case 1:
			drinkPotion(antiPoison);
			break;
		case 2:
			equipGG();
			attackSkele();
			break;
		}
		
	}

	@Override
	public String status() {
		return Vars.stringStatus = "In Combat";
	}
	
	public int getTask() {
		// We need to restore prayer
		if (SKILLS.PRAYER.getCurrentLevel() <= Vars.drinkAt) {
			return 0;
		}
		// We are poisioned
		else if (Game.getSetting(102) > 0) {
			return 1;
		}
		// We want to AFK or initiate combat
		else {
			return 2;
		}
	}
	
	public static boolean doWeHavePotion(int []id) {
		return Inventory.find(id).length > 0;
	}
	
	public static void drinkPotion(int[] potions) {
		// We want to find the potion with lowest dosage
		RSItem[] potion = Inventory.find(potions);
		// The smallest dose will start from the full dose & decrement accordingly
		int smallestDose = 0;
		int sIndex = 0;
		for(RSItem p: potion) {
			if (p.getID() < smallestDose) {
				smallestDose = p.getID();
				sIndex = p.getIndex();
			}
		}
			
		// We want to drink our prayer potion
		if (potion.length > 0) {
			if (potion[sIndex].click("Drink")) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return Skills.SKILLS.PRAYER.getCurrentLevel() > Vars.drinkAt;
					}
				}, General.random(2500, 3500));
			}
			// Generate new drinkAt value
			Vars.drinkAt = General.random(18, Skills.getActualLevel(SKILLS.PRAYER) - (Skills.getActualLevel(SKILLS.PRAYER) / 4 + 7));
		}
	}
	
    public static void enablePrayer(Prayer.PRAYERS pray) {
    	if (!Prayer.isPrayerEnabled(pray)) {
			if (Prayer.enable(pray)) {
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.sleep(100, 200);
					return Prayer.isPrayerEnabled(pray);
					}
				}, General.random(2500, 4100));
			}
		}
    }
    
    public void equipGG() {
    	// If greegree is equiped, equip Chins
    				if (Equipment.isEquipped(greeGree)) {
    					RSItem[] chins = Inventory.find(Chins);
    					if (chins.length > 0) {
    						if (chins[0].click("Wield")) {
    							Timing.waitCondition(new Condition() {
    								public boolean active() {
    									return Equipment.isEquipped(Chins);
    								}
    							}, General.random(2000, 3000));
    						}
    					}
    				}
    }
    
    public void attackSkele() {
    	// If Player is not already in combat, attack skeleton
    				if (!Player.getRSPlayer().isInCombat()) {
    					RSNPC[] skeleton = NPCs.findNearest(skele);
    					if (skeleton.length > 0) {
    						if (skeleton[0].isOnScreen()) {
    							if (DynamicClicking.clickRSNPC(skeleton[0], "Attack")) {
    								Timing.waitCondition(new Condition() {
    									public boolean active() {
    										return Player.getRSPlayer().isInCombat();
    									}
    								}, General.random(2000, 3000));
    							}
    						} else {
    							this.aCamera.turnToTile(skeleton[0]);
    						}
    					}
    				}
    }

}
