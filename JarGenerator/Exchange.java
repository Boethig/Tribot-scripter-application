package scripts.JarGenerator;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import scripts.JarGenerator.ACamera;

public class Exchange extends Node {

	public Exchange(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() {
		return Vars.puro.contains(Player.getPosition()) && Inventory.getCount(Vars.nature) == 1 && Inventory.getCount(Vars.eclectic) == 2 && Inventory.getCount(Vars.essence) == 3;
	}

	@Override
	public void execute() {
		PuroJars.status = "Purchasing Generator";
		//Grain Fail-safe
		if (Inventory.getCount(1947) == 1){
			RSItem[] vial = Inventory.find(1947);
			if (vial.length > 0) {
				if (Clicking.click("Drop", vial)) {
					Antiban.waitItemInteractionDelay();
					Timing.waitCondition(new Condition() {
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(1947) == 0;
						}
					}, General.random(500, 700));
				}
			}
		}
		
		if (Interfaces.isInterfaceValid(540)) {
			RSInterface select = Interfaces.get(540,125);
			RSInterface option = Interfaces.get(540,120);
			if (option != null && !option.isHidden()){
				if (Clicking.click(option)) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Clicking.click(option);
						}
					}, General.random(3000,4000));
				}
			}
			if (select != null && !select.isHidden()){
				if (Clicking.click(select)) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(Vars.generator) == 1;
						}
					}, General.random(3000,4000));
				}
				// If Generator purchased, counters
				if (Inventory.getCount(Vars.generator) > 0) {
					Vars.Gen = true; //Continue to generate
					Vars.countReset = true; // reset counter
					PuroJars.genStatus = "Generator bought: true";
					Vars.natC++;
					Vars.essC = Vars.essC + 3;
					Vars.eclC = Vars.eclC + 2;
						}
					}
				}
		else {
			if (interactMovingNpc(Vars.elnok, "Trade", 2500, 3500)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Interfaces.isInterfaceValid(540);
						}
					}, General.random(5000,6000));
				}
			}
	}
	
	public boolean interactMovingNpc(int npc, String action, int min, int max) {		
		RSNPC[] npcs = NPCs.findNearest(npc, 20);
		if (npcs.length > 0) {
				if (npcs[0].getDefinition() != null) {
							if (npcs[0].isOnScreen()) {
									DynamicClicking.clickRSModel(npcs[0].getModel(),action + " " + npcs[0].getName());
									General.sleep(min, max);	
									}
							else {	
								this.aCamera.turnToTile(npcs[0].getAnimablePosition());
							}					
						}	
					}			
		return false;	
	}
}	


