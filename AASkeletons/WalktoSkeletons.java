package scripts.AASkeletons;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;

import scripts.webwalker_logic.WebWalker;
import scripts.webwalker_logic.WebWalker.Offset;
import scripts.webwalker_logic.local.walker_engine.WalkingCondition;

public class WalktoSkeletons extends Node {	
	
	public WalktoSkeletons(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}


	public int[] greeGree;
	public int[] prayerPotions;

	@Override
	public boolean validate() {
		if (!Player.getPosition().equals(Vars.areaTile) && Attack.doWeHavePotion(prayerPotions)) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		WebWalker.setPathOffset(Offset.LOW);
		// Walk to Skeletons
		WebWalker.walkTo(Vars.areaTile, new WalkingCondition() {

			@Override
			public State action() {
				
				// Monkey is nearby in cave
				RSNPC[] monkey = NPCs.findNearest("Monkey Zombie");
				if (monkey.length > 0) {
					if (Player.getPosition().distanceTo(monkey[0].getPosition()) < 10) {
						RSItem[] gree = Inventory.find(greeGree);
						
						// We want to equip our greegree
						if (gree.length > 0) {
							if (gree[0].click("Hold")) {
								Timing.waitCondition(new Condition() {

									public boolean active() {
										return Equipment.isEquipped(greeGree);
									}					
								}, General.random(1000, 2000));
							}
						} else {
							
							// We have no greegree, enable protection prayers
							Attack.enablePrayer(PRAYERS.PROTECT_FROM_MELEE);
						}
						
					}
				}
				return State.CONTINUE_WALKER;
			}
			
		});
	}


	@Override
	public String status() {
		return Vars.stringStatus = "Walking to Skeletons";
	}

}
