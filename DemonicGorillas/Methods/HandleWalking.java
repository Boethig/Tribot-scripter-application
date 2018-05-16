package scripts.DemonicGorillas.Methods;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Interfaces;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.DemonicGorillas.Antiban;
import scripts.DemonicGorillas.Constants;
import scripts.DemonicGorillas.DemonicGorillas;
import scripts.DemonicGorillas.Locations;
import scripts.DemonicGorillas.Node;
import scripts.DemonicGorillas.Utils;
import scripts.DemonicGorillas.Variables;
import scripts.webwalker_logic.WebWalker;

public class HandleWalking extends Node {
	
	List<Integer> abc2WaitTimes = new ArrayList<>();

	public void execute() {
		abc2WaitTimes.add(General.random(1200, 2800));		
		if(!Locations.GORILLAS.contains(Player.getPosition()) 
			&& Utils.doWeHaveFood() && Utils.doWeHaveItem(Constants.SEED)) {
			walktoGorillas();
		}
		else if ((Inventory.isFull() && !Utils.doWeHaveFood()) || !Utils.doWeHaveFood() && !Locations.BANK.contains(Player.getPosition())) {	
			walktoBank();		
		}		
	}

	@Override
	public boolean validate() {
		return (Inventory.isFull() || !Utils.doWeHaveFood()) || (!Locations.GORILLAS.contains(Player.getPosition()) 
				&& Utils.doWeHaveFood());
			}
	
	
	
	public void walktoBank() {
		DemonicGorillas.status = "Banking";
		if (!Banking.isInBank()) {
			Teleport();
		}
	// Walk to Bank Booth
	if (WebWalker.walkTo(Locations.BANK.getRandomTile())) {	
      	Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(100, 200);
				return Locations.BANK.contains(Player.getPosition());
			}
		}, General.random(2000, 3000));
	}
}
		
	public void walktoGorillas() {
		DemonicGorillas.status = " Walking to Gorillas";		

		RSObject[] Opening = Objects.find(10, 28807);
	if (Locations.OPENING.contains(Player.getPosition())) {
		//RSObject[] OPENING = Objects.find(10, 28807);
		if (Opening.length > 0){
			if (!Opening[0].isClickable() || !Opening[0].isOnScreen()) {
				Camera.turnToTile(Opening[0]);
			}
			if(Clicking.hover(Opening) && Clicking.click( "Pass-through", Opening)) {
			Timing.waitCondition(new Condition() {
				public boolean active() {
					General.sleep(100, 200);
					return Interfaces.isInterfaceValid(219);
					}
				}, General.random(3500, 5500));
			}	
		}
		EnterGate();
	}
		//Entered opening, walking to Cavern Entrance
	else if(Player.getPosition().equals(Locations.ENTERED)) {
		
		//Generating random path to cavern entrance
		RSTile path1 = Locations.area1_path.getRandomTile();
		RSTile path2 = Locations.area2_path.getRandomTile();
		RSTile path3 = Locations.area3_path.getRandomTile();
		RSTile cave = Locations.CAVERN_ENTERANCE.getRandomTile();
		RSTile[] path = {path1,path2,path3,cave};
		
		//Walking the path
		if (Walking.walkPath(path)) {
				Antiban.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
				abc2WaitTimes.add(Antiban.getReactionTime());
				General.println("Sleeping for " + Antiban.getReactionTime());
				Antiban.sleepReactionTime();
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return Locations.CAVERN_ENTERANCE.contains(Player.getPosition());
					}
				}, General.random(6000, 7000));
			}
		}
	// If near Cavern Entrance, enter it
	else if (Locations.CAVERN_ENTERANCE.contains(Player.getPosition())) {
		RSObject[] Cavern_Enterance = Objects.find(10, 28686);
		if (Cavern_Enterance.length > 0) {
			if (Clicking.click("Enter", Cavern_Enterance)) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return Locations.CAVERN_AREA.contains(Player.getPosition());
					}
				}, General.random(3500, 4500));
			}
			
		}
	}
	// If inside Cavern, walk to Gorillas
	else if (Locations.CAVERN_AREA.contains(Player.getPosition())) {
		if (Walking.walkPath(Locations.cavern_Path)) {
			Antiban.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
			abc2WaitTimes.add(Antiban.getReactionTime());
			General.println("Sleeping for " + Antiban.getReactionTime());
			Antiban.sleepReactionTime();
			Timing.waitCondition(new Condition() {
				public boolean active() {
					General.sleep(100, 200);
					return Locations.GORILLAS.contains(Player.getPosition());
				}
			}, General.random(3500, 4500));
		}
	}
	else {
		// Walk to Opening
				if (!Locations.TREE.contains(Player.getPosition())) {
					Teleport();
				}
				if (WebWalking.walkTo(Locations.OPENING.getRandomTile())) {
					Antiban.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
					abc2WaitTimes.add(Antiban.getReactionTime());
					General.println("Sleeping for " + Antiban.getReactionTime());
					Antiban.sleepReactionTime();
					Timing.waitCondition(new Condition() {
						public boolean active() {
							General.sleep(100, 200);
							return Locations.OPENING.contains(Player.getPosition());
						}
					}, General.random(3500, 4500));
				}
		}
	}
	
	public void Teleport() {
		RSItem[] SEED = Inventory.find(19564);
		if(SEED.length > 0) {
			Clicking.click("Commune", SEED[0]);
			Timing.waitCondition(new Condition() {
				public boolean active() {
					General.sleep(100,200);
					return Locations.TREE.contains(Player.getPosition());
				}
			}, General.randomLong(5000, 6000));
		}		
	}
	public void EnterGate() {
		final RSInterface Opening = Variables.getOpeningInterface();
		if(Opening != null) {
			RSInterface yes = Opening.getChild(1);
			if(Clicking.click(yes)) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return Player.getPosition() == Locations.ENTERED;
						}
					}, General.random(4500, 6500));
			}
		}
	}
}
