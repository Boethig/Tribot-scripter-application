package scripts.AASkeletons;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.webwalker_logic.WebWalker;
import scripts.webwalker_logic.WebWalker.Offset;

public class WalktoBank extends Node {
	
	public WalktoBank(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}

	public RSArea BANK = new RSArea(new RSTile(0, 0, 0), new RSTile(0, 0, 0));
	public int[] prayerPotions;
	public int[] antiPoison;

	@Override
	public boolean validate() {
		// We are not in the bank AND
		if (!Banking.isInBank()) {
			// We are out of prayer potions
			if (!doWeHavePotion(prayerPotions)) {
				return true;
			}
			// We are low hp, and poisoned
			else if (!doWeHavePotion(antiPoison) && Game.getSetting(102) > 0 && Skills.getCurrentLevel(Skills.SKILLS.HITPOINTS) < General.random(10, 20)) {
				return true;
			}
			// We have run out of Chins
			else if (Vars.noChinsLeft) {
				return true;
			}
			// We are just very low health (Safety)
			else if (Skills.getCurrentLevel(Skills.SKILLS.HITPOINTS) < 10) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void execute() {
		WebWalker.setPathOffset(Offset.LOW);
		WebWalker.walkTo(BANK.getRandomTile());
	}

	@Override
	public String status() {
		return Vars.stringStatus = "Walking to Bank";
	}
	
	public static boolean doWeHavePotion(int []id) {
		return Inventory.find(id).length > 0;
	}

}
