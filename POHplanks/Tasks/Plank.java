package scripts.POHplanks.tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import scripts.POHplanks.antiban.Antiban;
import scripts.POHplanks.data.Vars;
import scripts.POHplanks.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.npc.NPCFinder;
import scripts.boe_api.objects.ObjectFinder;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

import java.util.List;

public class Plank extends Node {

	public Plank(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public String status() {
		return "Plank: ";
	}
	
	@Override
	public boolean validate() {
		return Utils.get().canMakePlanks() && Inventory.isFull() && !Banking.isBankScreenOpen();
	}
	
	@Override
	public void execute() {
		RSObject portal = Utils.get().getInHousePortal();
		if (portal != null) {
			if (Utils.get().shouldRefillMoneybag()) {
				if (Utils.get().isEnterAmountInterfaceOpen()) {
					Keyboard.typeSend(Integer.toString(Vars.get().coinDepositAmount));
					if (Timing.waitCondition(() -> NPCInteraction.isConversationWindowUp(), General.random(3000, 4000))) {
						Vars.get().coinsInMoneybag += Vars.get().coinDepositAmount;
					}
				}
				if (NPCInteraction.isConversationWindowUp()) {
					Vars.get().subStatus = "Depositing coins";
					NPCInteraction.handleConversation("Deposit coins");
				} else {
					Vars.get().subStatus = "Finding Servant's moneybag";
					RSObject moneyBag = ObjectFinder.findFirst(30, "Servant's moneybag");
					if (moneyBag != null) {
						if (WebWalking.walkTo(moneyBag.getPosition())) {
							if (AccurateMouse.click(moneyBag, "Use")) {
								Timing.waitCondition(() -> NPCInteraction.isConversationWindowUp(), General.random(2000, 3000));
							}
						}
					}
				}
			} else if (Utils.get().isEnterAmountInterfaceOpen()) {
				Vars.get().subStatus = "Entering log amount";
				Vars.get().abc2WaitTimes.add(Antiban.getReactionTime());
				Antiban.sleepReactionTime();
				Keyboard.typeSend("" + Inventory.getCount(Vars.get().plank.getLogName()));
				Antiban.sleepReactionTime();
				Antiban.generateTrackers(calculateAverage(Vars.get().abc2WaitTimes));
			} else if (NPCInteraction.isConversationWindowUp()) {
				if (NPCInteraction.hasOption("Go to the sawmill...")) {
					Vars.get().subStatus = "Using logs on butler";
					if (useLogsOnButler()) {
						NPCInteraction.handleConversation();
					}
				} else {
					Vars.get().subStatus = "Talking to butler";
					int logCount = Inventory.getCount(Vars.get().plank.getLogId());
							NPCInteraction.handleConversation(
							"Take to sawmill: " + logCount + " x " + Vars.get().plank.getLogName(),
							"Sawmill",
							"Yes",
							"Okay, here's 10,000 coins.");
					if (!Utils.get().hasLogs()) {
						Vars.get().planksMade += logCount;
					}
				}
			} else if (Utils.get().isBondInterfaceOpen()) {
				Vars.get().subStatus = "Closing bond interface";
				RSInterface closeButton = Interfaces.get(65,2,13);
				if (closeButton != null) {
					if (AccurateMouse.click(closeButton, "Close")) {
						Timing.waitCondition(() -> !Utils.get().isBondInterfaceOpen(), General.random(2500, 3500));
					}
				}
			} else {
				RSNPC butler = NPCFinder.findFirst((rsnpc) -> rsnpc != null && rsnpc.getName().equals("Demon butler") && rsnpc.getPosition().distanceTo(Player.getPosition()) <= 5);
				if (butler != null && butler.isOnScreen()) {
					if (Utils.get().isButlerStuck()) {
						Vars.get().subStatus = "Talking to butler";
						if (!butler.isClickable()) {
							aCamera.turnToTile(butler.getPosition());
						}
						if (AccurateMouse.click(butler, "Talk-to")) {
							Timing.waitCondition(() -> NPCInteraction.isConversationWindowUp(), General.random(2500, 4000));
						}
					}
				} else {
					if (Utils.get().isHouseOptionsOpen()) {
						Vars.get().subStatus = "Calling butler";
						RSInterface callServant = Utils.get().getCallServant();
						if (callServant != null) {
							Vars.get().abc2WaitTimes.add(Antiban.getReactionTime());
							if (AccurateMouse.click(callServant, "Call Servant")) {
								Antiban.sleepReactionTime();
								Antiban.generateTrackers(calculateAverage(Vars.get().abc2WaitTimes));
								Timing.waitCondition(() -> NPCFinder.findFirst("Demon butler") != null, General.random(2500, 3500));
							}
						}
					} else if (Utils.get().isOptionsOpen()) {
						Vars.get().subStatus = "Selecting house options";
						RSInterface houseOptions = Utils.get().getHouseOptions();
						if (houseOptions != null) {
							Vars.get().abc2WaitTimes.add(Antiban.getReactionTime());
							if (AccurateMouse.click(houseOptions, "View House Options")) {
								Antiban.sleepReactionTime();
								Antiban.generateTrackers(calculateAverage(Vars.get().abc2WaitTimes));
								Timing.waitCondition(() -> Utils.get().isHouseOptionsOpen(), General.random(2000, 3000));
							}
						}
					} else {
						Vars.get().subStatus = "Switching to options tab";
						Antiban.switchTab(GameTab.TABS.OPTIONS);
					}
				}
			}
		} else {
			Vars.get().subStatus = "Teleporting to POH";
			teleportToPOH();
		}
	}

	private boolean useLogsOnButler() {
		if (isLogsSelected()) {
			RSNPC butler = NPCFinder.findFirst("Demon butler");
			if (butler != null && AccurateMouse.click(butler, "Use")) {
				return Timing.waitCondition(() -> NPCInteraction.isConversationWindowUp(), General.random(2500, 4000));
			}
		} else {
			RSItem logs = OSInventory.findFirstNearestToMouse(Vars.get().plank.getLogId());
			if (logs != null) {
				if (AccurateMouse.click(logs, "Use")) {
					Timing.waitCondition(() -> isLogsSelected(), General.random(3000, 5000));
				}
			}
		}
		return false;
	}

	private boolean isLogsSelected() {
		return Game.getItemSelectionState() == 1 && Game.getSelectedItemName().equals(Vars.get().plank.getLogName());
	}

	private void teleportToPOH() {
		if (!Utils.get().doWeHaveRunes()) {
			Vars.get().shouldRun = false;
			Vars.get().errorMessage = "Could not teleport to POH: no runes for spell found";
		}
		Antiban.switchTab(GameTab.TABS.MAGIC);
		if (Magic.selectSpell("Teleport to House")) {
			Timing.waitCondition(() -> Objects.find(10, "Portal").length > 0, General.random(5000,7000));
		}
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

