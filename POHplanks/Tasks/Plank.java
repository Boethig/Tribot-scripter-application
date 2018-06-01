package scripts.POHplanks.Tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

import scripts.POHplanks.Antiban;
import scripts.POHplanks.Conditions;
import scripts.POHplanks.Node;
import scripts.POHplanks.POHplanks;
import scripts.POHplanks.Vars;

public class Plank extends Node {
	@Override
	public void execute() {
		// stop script condition
		if (Inventory.getCount(Vars.COINS) < General.random(20000, 40000) || Inventory.getCount(Vars.LAW) == 0) {
			Bank.Teleport();
			Login.logout();
			POHplanks.conditions = false;
		}
		if (Banking.isBankScreenOpen()) {
			POHplanks.status = "Closing bank";
			Banking.close();
		}
		
		RSObject[] Portal = Objects.find(10, 4525);
		RSNPC[] Butler = NPCs.findNearest(229);
		List<Integer> abc2WaitTimes = new ArrayList<>();
		
		if (Portal != null  && Portal.length > 0) {
			// we are in POH 
			if (Butler != null && Butler.length > 0 && Player.getPosition().distanceTo(Butler[0].getPosition()) < 4) {
				// butler is in our area
				
				final RSInterface _continue = NPCChat.getClickContinueInterface();
				final RSInterface enterx = Vars.getEnterInterface();
				final RSInterface exit = Vars.getbondInterface();
				String Options[] = NPCChat.getOptions();
				
				if (exit != null) { // bond interface clicked on accident, click out
					RSInterface bond = exit.getChild(13);					
					if (bond != null && Clicking.click(bond)){
						Timing.waitCondition(Conditions.Clicked(), General.random(2000,3000));
					}
				}
				if (_continue != null) {
					// click to continue option, press space
					abc2WaitTimes.add(Antiban.getReactionTime());
					Antiban.sleepReactionTime();
					Keyboard.typeSend(" ");
					Timing.waitCondition(Conditions.ClickedContinue(), General.random(2000,3000));
					Antiban.generateTrackers(calculateAverage(abc2WaitTimes), false);	
				}
				else if (enterx != null && !enterx.isHidden()) {
					abc2WaitTimes.add(Antiban.getReactionTime());
					Antiban.sleepReactionTime();
					Keyboard.typeSend("" + Inventory.getCount(Vars.LOG_NAME));
					General.sleep(1500,2500);
					Antiban.generateTrackers(calculateAverage(abc2WaitTimes), false);	
				}
				else if (Options != null) {
						if (Arrays.asList(Options).contains("Yes") || Arrays.asList(Options).contains("Pay servant 10000 coins") 
							|| Arrays.asList(Options).contains("Okay, here's 10,000 coins.")) {
							abc2WaitTimes.add(Antiban.getReactionTime());
							Antiban.sleepReactionTime();
							Keyboard.typeSend("1");
							Timing.waitCondition(Conditions.ClickedOptions(), General.random(2000,3000));
							Antiban.generateTrackers(calculateAverage(abc2WaitTimes), false);
						}
						else if (Arrays.asList(Options).contains("Sawmill")) {
							abc2WaitTimes.add(Antiban.getReactionTime());
							Antiban.sleepReactionTime();
							if (NPCChat.selectOption("Sawmill", true)) {
								Timing.waitCondition(Conditions.ClickedOptions(), General.random(2000,3200));
								Antiban.generateTrackers(calculateAverage(abc2WaitTimes), false);
							}
						}
						else if (Arrays.asList(Options).contains("Take to sawmill: " + "" + Inventory.getCount(Vars.LOGS) + " x " + Vars.LOG_NAME)) {
							POHplanks.status = "Attempting to make planks";
							abc2WaitTimes.add(Antiban.getReactionTime());
							Antiban.sleepReactionTime();
							Keyboard.typeSend("1");
							Timing.waitCondition(Conditions.ClickedOptions(), General.random(2000,3000));
							Antiban.generateTrackers(calculateAverage(abc2WaitTimes), false);
						} else {
							RSItem[] log = Inventory.find(Vars.LOGS);
							if (log != null) {
								if (Game.getItemSelectionState() == 1 && Game.getSelectedItemName().equals("" + Vars.LOG_NAME)) {
									abc2WaitTimes.add(Antiban.getReactionTime());
									Antiban.sleepReactionTime();
									if (Clicking.click("Use " + Vars.LOG_NAME + " ->", Butler[0])) {
									Timing.waitCondition(Conditions.ClickedOptions(), General.random(2000,3000));
									Antiban.generateTrackers(calculateAverage(abc2WaitTimes), false);
									}
								} else {
									if (Clicking.click("Use", log)) {
										Timing.waitCondition(new Condition() {
											public boolean active() {
												General.sleep(100, 200);
												return Game.getItemSelectionState() == 1;
											}
										}, General.random(2000, 3000));
										Antiban.waitItemInteractionDelay();
									}
								}
								
							}
						}
				}
				else if (isStuck()) {
					if (Butler[0].isClickable() && DynamicClicking.clickRSNPC(Butler[0], "Talk-to")) {
						Timing.waitCondition(Conditions.Clicked(), General.random(1500,2500));
					}
				}
			}
			else if (Butler == null) {
				System.out.println("Butler not found");
				Bank.Teleport();
				Login.logout();
				POHplanks.conditions = false;
			}
			else { // We want to call the butler
				final RSInterface home = Vars.getHomeInterface();
				final RSInterface call = Vars.getCallInterface();
				if (Interfaces.isInterfaceValid(370) && call != null) {
					abc2WaitTimes.add(Antiban.getReactionTime());
					Antiban.sleepReactionTime();
						if (Clicking.click(call)) {
							Timing.waitCondition(Conditions.Clicked(),General.random(1000,2000));
							Antiban.generateTrackers(calculateAverage(abc2WaitTimes), false);
						}
					
				}
				else if (home != null && home.isClickable()) {
					abc2WaitTimes.add(Antiban.getReactionTime());
					Antiban.sleepReactionTime();
					if (Clicking.click("House Options", home)) {
						Timing.waitCondition(Conditions.ClickedInt(), General.random(1000, 2000));
						Antiban.generateTrackers(calculateAverage(abc2WaitTimes), false);
					}
				}
				else {
					GameTab.open(GameTab.TABS.OPTIONS);
				}
			} 
		}
		else {
			Teleport();
		}
	}

	@Override
	public boolean validate() {
		return Inventory.getCount(Vars.LOGS) >= 20 && Inventory.isFull();
	}

	//public functions
	public void Teleport() {
		if (Magic.selectSpell("Teleport to House")) {
			Timing.waitCondition(Conditions.SelectSpell(), General.random(5000, 6000));
		}
	}
	public boolean isStuck() {
		RSNPC[] Butler = NPCs.findNearest(229);
		String[] op = NPCChat.getOptions();
		RSInterface con = NPCChat.getClickContinueInterface();
		if (Butler != null && Butler.length > 0) {
			if (Butler[0].isInteractingWithMe() && op == null && con == null) {
					return true;
			}
			return false;
		}
		return false;
	}
	public int calculateAverage(List<Integer> times) {
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

