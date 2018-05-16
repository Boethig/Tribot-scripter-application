package scripts.DemonicGorillas.Methods;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;

import scripts.DemonicGorillas.Antiban;
import scripts.DemonicGorillas.DemonicGorillas;
import scripts.DemonicGorillas.Locations;
import scripts.DemonicGorillas.Node;
import scripts.DemonicGorillas.Utils;
import scripts.DemonicGorillas.Variables;

public class Eat extends Node {

	@Override
	public void execute() {
		if (!Variables.FallingRock) {
			if (Utils.hpPercent() < Variables.hpToEatAt  && Utils.doWeHaveFood()) {
				eat();
				}
			if (SKILLS.PRAYER.getCurrentLevel() < Variables.drinkAt) {
				drink();
				}
			if (SKILLS.RANGED.getCurrentLevel() == SKILLS.RANGED.getActualLevel() && Utils.doWeHavePotion(Variables.RangePot)) {
				boost(Variables.RangePot, Skills.SKILLS.RANGED);
				}
			if (Utils.doWeHavePotion(Variables.CombatPot) && SKILLS.STRENGTH.getCurrentLevel() == SKILLS.STRENGTH.getActualLevel()) {
				boost(Variables.CombatPot, Skills.SKILLS.STRENGTH);	
				}
		}
	}

	public boolean validate() {
		return shouldEat();
	}
	
	public static boolean shouldEat() {
		return (Utils.hpPercent() < Variables.hpToEatAt  && Utils.doWeHaveFood())
				|| (SKILLS.PRAYER.getCurrentLevel() < Variables.drinkAt) && Utils.doWeHavePotion(Variables.PrayerPot)
				|| (Locations.GORILLAS.contains(Player.getPosition()) && SKILLS.RANGED.getCurrentLevel() == SKILLS.RANGED.getActualLevel() && Utils.doWeHavePotion(Variables.RangePot))
				|| (Locations.GORILLAS.contains(Player.getPosition()) && Utils.doWeHavePotion(Variables.CombatPot) && SKILLS.STRENGTH.getCurrentLevel() == SKILLS.STRENGTH.getActualLevel());
	}
	
	public static void eat() {
		DemonicGorillas.status = "Eating";
		RSItem[] food = Inventory.find(Variables.foodID);
		if (food.length > 0) {
			if (Clicking.click("Eat", food[0])) {
				Antiban.waitItemInteractionDelay();
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return (Skills.SKILLS.HITPOINTS.getCurrentLevel() > Variables.hpToEatAt);
					}
				}, General.random(1000, 2000));
			}
		}
	}
	public static void drink() {
		DemonicGorillas.status = "Drinking prayer potion";
		RSItem[] pray = Inventory.find(Variables.PrayerPot);
		if (pray.length > 0) {
			if(Clicking.click("Drink", pray[0])) {
				Antiban.waitItemInteractionDelay();
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return (Skills.SKILLS.PRAYER.getCurrentLevel() > Variables.drinkAt);
					}
				}, General.random(1000, 2000));
			}
			Variables.drinkAt = General.random(18, Skills.getActualLevel(SKILLS.PRAYER) - (Skills.getActualLevel(SKILLS.PRAYER) / 4 + 7));
		}
	}
	public static void boost(int []potion, Skills.SKILLS skill) {
		DemonicGorillas.status = "Boosting stats";
		RSItem[] pot = Inventory.find(potion);
		if (pot.length > 0) {
			if(Clicking.click("Drink", pot)) {
				Antiban.waitItemInteractionDelay();
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return (skill.getCurrentLevel() > skill.getActualLevel());
					}
				}, General.random(1000, 2000));
			}
		}
		General.sleep(50,70);
	}

}
