package scripts.POHplanks.Tasks;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.POHplanks.Conditions;
import scripts.POHplanks.Node;
import scripts.POHplanks.POHplanks;
import scripts.POHplanks.Vars;

public class Bank extends Node {
	@Override
	public void execute() {
	//stop script cases	
	if (Inventory.getCount(Vars.COINS) < General.random(20000, 40000) || Inventory.getCount(Vars.LAW) == 0) {
		Login.logout();
		POHplanks.conditions = false;
	}
	
	RSObject[] bank = Objects.find(10, 7411, 7414);
		
	if (Vars.TeleLoc.contains(Player.getPosition()) && bank != null && bank.length > 0) {
		POHplanks.status = "Attempting to bank";
		pBank();
	}
	else {
		POHplanks.status = "Attempting to teleport";
		if (Interfaces.isInterfaceValid(370)) {
			if (Player.getPosition().click()) {
				Timing.waitCondition(Conditions.Clicked(), General.random(2000,4000));
			}
		}
		Teleport();
	}
}

	@Override
	public boolean validate() {
		return Inventory.getCount(Vars.LOGS) < 20 || Inventory.getCount(Vars.PLANK) > 0;
	}
	
	//public functions
	public static void Teleport() {
				if (Magic.selectSpell("" + Vars.TELEPORT)) {
					Timing.waitCondition(Conditions.atBank(), General.random(4500,5500));
				}
		}
	
public void pBank(){
	if (Banking.isBankScreenOpen()) {
		RSItem[] planks = Banking.find(Vars.PLANK);
		RSItem[] logs = Banking.find(Vars.LOGS);
		//Tracking planks made
		if (planks.length > 0) {
			if (POHplanks.start) {
				Vars.iP = planks[0].getStack();
				POHplanks.start = false;
			}
			Vars.planks_made = planks[0].getStack() - Vars.iP;
		}
			
		if (logs != null) {
			if (logs.length > 0 && logs[0].getStack() < 26) {
				Login.logout();
				POHplanks.conditions = false;
			}
			else if (Inventory.getCount(Vars.LOGS) <= 25) {
				POHplanks.status = "Withdrawing logs";
				if (Banking.withdraw(0, Vars.LOGS)) {
					Timing.waitCondition(Conditions.Full(), General.random(5000, 7500));
				}
				Banking.depositAllExcept(Vars.COINS,Vars.LAW,Vars.LOGS,Vars.AIR,Vars.EARTH);
			}
			else if (Inventory.getCount(Vars.PLANK) > 0) {
				POHplanks.status = "Depositing planks";
				if (Banking.deposit(0, Vars.PLANK)) {
					Timing.waitCondition(Conditions.Banked(), General.random(5000,7000));
				}	
			}
		}
		else {
			Login.logout();
			POHplanks.conditions = false;
		}
	} else {
			POHplanks.status = "Opening bank";
			if (Vars.TELEPORT.equals("Lumbridge Teleport")) {
				if (Banking.openBank()) {
					Timing.waitCondition(Conditions.bankOpen(), General.random(5000,7000));
				}
			}
			else {
				RSObject[] cammy = Objects.find(10, 7414);
				if (cammy.length > 0) {
					if (Clicking.click("Use", cammy[0])) {
						Timing.waitCondition(new Condition() {
							public boolean active() {
								General.sleep(100, 200);
								return Banking.isBankScreenOpen();
							}
						}, General.random(2000, 3000));
					}
				}
			}
			
		}
	}

}
