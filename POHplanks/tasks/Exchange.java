package scripts.POHplanks.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import scripts.POHplanks.antiban.Antiban;
import scripts.POHplanks.data.Vars;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.WalkingCondition;

public class Exchange extends Node {

	public Exchange(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public String status() {
		return Vars.get().status = "Exchanging: ";
	}
	
	@Override
	public boolean validate() {
		return false;
	}

	@Override
	public void execute() {
		// Check if we are in a PVP world
//		if (ArrayUtils.contains(Vars.get().PVP_WORLDS, Game.getCurrentWorld())) {
//			// Hop To a different World
//			if (WorldHopper.atSelectWorldScreen()) {
//				int world = WorldHopper.getRandomWorld(true);
//				if (WorldHopper.changeWorld(world)) {
//					Timing.waitCondition(() -> Game.getCurrentWorld() == world, General.random(8000 + Vars.get().sleepOffset, 10000 + Vars.get().sleepOffset));
//				}
//			}
//			else {
//				WorldHopper.openWorldSelect();
//			}
//		}
		RSNPC clerk = getNearestNPC("Grand Exchange Clerk");
		// We are in the Grand Exchange
		if (clerk != null) {
			
			// Is G.E Window Open?
			
			if (Interfaces.isInterfaceSubstantiated(GrandExchange.INTERFACE_EXCHANGE_ID)) {
				// Offer Window is up
				if (Interfaces.isInterfaceSubstantiated(GrandExchange.INTERFACE_EXCHANGE_OFFER_ID)) {
					// Set Price to Configuration Price
					if (Interfaces.isInterfaceSubstantiated(GrandExchange.INTERFACE_EXCHANGE_NEW_OFFER_CUSTOM_PRICE_ID)) {
						// Enter Custom Price
						if (AccurateMouse.click(Interfaces.get(GrandExchange.INTERFACE_EXCHANGE_NEW_OFFER_CUSTOM_PRICE_ID), "Enter price")) {
							Keyboard.typeSend(Integer.toString(0));
							// Check if price has been updated							
							String price = Interfaces.get(GrandExchange.INTERFACE_EXCHANGE_NEW_OFFER_PRICE_ID).getText();
							price.replaceAll("coins", ""); // Parse out coins							
							Timing.waitCondition(() -> price.equals(Integer.toString(0)), General.random(3000, 4000));
						}
					}
				}
				RSItem planks = getNotedItem(Vars.get().plank.getId()); // Find Noted Items
				if (planks != null) {
					// Offer Items
					if (AccurateMouse.click(planks, "Offer")) {
						Timing.waitCondition(() -> Interfaces.isInterfaceSubstantiated(GrandExchange.INTERFACE_EXCHANGE_OFFER_ID), General.random(5000, 7000));
					}
				}
			}
			else {
				// Open G.E window
				Vars.get().subStatus = "Opening Exchange";
				if (AccurateMouse.click(clerk, "Exchange")) {
					Timing.waitCondition(() -> Interfaces.isInterfaceSubstantiated(GrandExchange.INTERFACE_EXCHANGE_ID), General.random(5000, 7000));
				}
			}
			
			// Sell Planks
			if (Inventory.find(Vars.get().plank.getId()).length > 0) {
			}
			else {
				// Open bank and grab planks
				if (Banking.isBankScreenOpen()) {
					Vars.get().subStatus = "Grabbing Items";
				}
				else {
					Vars.get().subStatus = "Opening Bank";
					Banking.openBank();
				}
			}	
		}
		// Go To G.E
		Vars.get().subStatus = "Walking to G.E";
		DaxWalker.walkToBank(RunescapeBank.GRAND_EXCHANGE, new WalkingCondition() {
			@Override
			public State action() {
				Antiban.activateRun();
				return WalkingCondition.State.CONTINUE_WALKER;
			}		
		});
	}
	
	public RSNPC getNearestNPC(String name) {
		RSNPC[] npc = NPCs.findNearest(name);
		return npc.length > 0 ? npc[0] : null;
	}
	
	public RSItem getNotedItem(int id) {
		RSItem[] noted = Inventory.find(id + 1); // Find Noted Items
		return noted.length > 0 ? noted[0] : null;
	}
}
