package scripts.DemonicGorillas.Methods;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItemDefinition;

import scripts.DemonicGorillas.DemonicGorillas;
import scripts.DemonicGorillas.Node;

public class Loot extends Node {
	
	public static int[] NAME = {560, 563, 1079,1093,1113,19582,1602,2362,2364,4587,385,2486,216,214,
			5289,5314,5300,5295,5304,5315,5316,19580,19589,19586};
	
	List<Integer> abc2WaitTimes = new ArrayList<>();

	@Override
	public void execute() {
		DemonicGorillas.status = "Looting";
		abc2WaitTimes.add(General.random(1200, 2800));
		final RSGroundItem[] LOOT = GroundItems.findNearest(NAME);
		if(LOOT.length > 0) {
			if (!LOOT[0].isOnScreen()) {
				Camera.turnToTile(LOOT[0]);
			}
			RSItemDefinition def = LOOT[0].getDefinition();
			String itemName = null;
			if (def != null) {
				itemName = def.getName();
				}
			if (Clicking.click("Take " + itemName, LOOT[0])) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return GroundItems.getAt(LOOT[0].getPosition()).length < 1;
					}
				}, General.random(1600, 2050));
				}
			}
		}		

	@Override
	public boolean validate() {
		 return lootOnScreen() && !Inventory.isFull();
	}
	
	private boolean lootOnScreen() {
		return GroundItems.findNearest(NAME).length > 0;
	}
}
