package scripts.DemonicGorillas.Methods;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.interfaces.Clickable07;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Projectiles;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSNPCDefinition;
import org.tribot.api2007.types.RSProjectile;
import org.tribot.api2007.types.RSTile;

import scripts.DemonicGorillas.Antiban;
import scripts.DemonicGorillas.Constants;
import scripts.DemonicGorillas.DemonicGorillas;
import scripts.DemonicGorillas.Locations;
import scripts.DemonicGorillas.Utils;
import scripts.DemonicGorillas.Variables;
import scripts.DemonicGorillas.Node;
import scripts.webwalker_logic.WebWalker;

public class Attack extends Node {
	
	List<Integer> abc2WaitTimes = new ArrayList<>();
	Thread counter = new Thread(new Runnable(){
		public void run() {
			RSNPC[] gorilla = getAttackingNPC();
			Variables.counter = 0;
		if(gorilla.length > 0 && gorilla != null) {	
			int prev = Skills.SKILLS.HITPOINTS.getCurrentLevel();
			while (Variables.counter < 3) {
				if((Variables.projTarget && (Variables.projID == Constants.MAGE_ATTACK || Variables.projID == Constants.RANGED_ATTACK)
						&& Variables.projTile.equals(Player.getPosition())) || gorilla[0].getAnimation() == 7226) {
					int curr = Skills.SKILLS.HITPOINTS.getCurrentLevel();
					if (prev - curr == 0 || prev - curr == -1) {
						Variables.counter++;
					}
					prev = curr;					
				}
			}
		}
	}
});
	
	public boolean validate() {
		return Utils.doWeHaveFood() && Locations.GORILLAS.contains(Player.getPosition());
	}
	
	public void execute() {
		DemonicGorillas.status = "Killing Demonic Gorillas";		

		RSNPC[] gorilla = getAttackingNPC();
		
		//Initiate combat
		if (gorilla != null && gorilla.length > 0) {
			if(Clicking.click("Attack",gorilla)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return Player.getAnimation() != -1;
					}	
				}, General.random(1000, 2000));
			}
		}
		
		while (Combat.isUnderAttack()) {
			counter.start(); // Counter thread activated
			
			RSGroundItem[] LOOT = GroundItems.findNearest(Loot.NAME);
			if (Eat.shouldEat()|| LOOT.length > 0) { // Break case
				break;
			}
			
			if (gorilla != null && gorilla.length > 0) { //NPC fouund				
				if (!gorilla[0].isOnScreen()) {
					Camera.turnToTile(gorilla[0].getPosition());
				}	
				
				// Ranged case
				if (Variables.projID == Constants.RANGED_ATTACK && Variables.projTarget) 
				{
					System.out.println("Detected Ranged Attack");
					Utils.enablePrayer(PRAYERS.PROTECT_FROM_MISSILES);
					while(Variables.counter < 3){
						checkOverHead(gorilla[0]);
					}
					Variables.counter = 0; // Reset counter
					if (gorilla[0].getAnimation() == 7226 || gorilla[0].getAnimation() == -1) {
						Utils.enablePrayer(PRAYERS.PROTECT_FROM_MELEE);
					}
					else {
						Utils.enablePrayer(PRAYERS.PROTECT_FROM_MISSILES);
					}
				}
				
				// Magic case
				else if (Variables.projID == Constants.MAGE_ATTACK && Variables.projTarget) 
				{
					System.out.println("Detected Magic Attack");
					Utils.enablePrayer(PRAYERS.PROTECT_FROM_MAGIC);
					while(Variables.counter < 3){
						checkOverHead(gorilla[0]);
					}
					Variables.counter = 0; // Reset counter
					if(gorilla[0].getAnimation() == 7226 || gorilla[0].getAnimation() == -1) {
						Utils.enablePrayer(PRAYERS.PROTECT_FROM_MELEE);
					}
					else {
						Utils.enablePrayer(PRAYERS.PROTECT_FROM_MISSILES);
					}
				}
				
				// Melee case
				else if (gorilla[0].getAnimation() == 7226) 
				{
					System.out.println("Detected Melee Attack");
					Utils.enablePrayer(PRAYERS.PROTECT_FROM_MELEE);
					while(Variables.counter < 3){
						checkOverHead(gorilla[0]);
					}
					Variables.counter = 0; // Reset counter
					int pray = General.random(1, 2); // Guess on protection prayer
					if (pray == 1) {
						Utils.enablePrayer(PRAYERS.PROTECT_FROM_MAGIC);
					}
					else {
						Utils.enablePrayer(PRAYERS.PROTECT_FROM_MISSILES);
					}
				}
			}
		}
	}
	
	private int getOverHead(RSNPC npc) {
		if (npc.isValid()) {
		RSNPCDefinition overhead = npc.getDefinition();
			if (overhead != null) {
			 return overhead.getHeadIcon();
			}
			else { 
				return -1;
			}
		}
		else { 
			return -1;
		}
	}
	private void checkOverHead(RSNPC npc) {
		if (npc.isValid()){
			if (getOverHead(npc) == 1) {
				for(int equipment : Variables.Melee) {
					if (!Utils.isEquipped(equipment) && !Variables.FallingRock) {
						Utils.equipItem(equipment);
					}
				}
			}
			else if (getOverHead(npc) == 0) {
				for(int equipment : Variables.Ranged) {
					if (!Utils.isEquipped(equipment) && !Variables.FallingRock) {
						Utils.equipItem(equipment);
					}
				}
			}
			else {
				General.sleep(100,300);
			}
		}
	}
    private RSNPC[] getAttackingNPC() {
    	RSNPC[] npc = null;
    	RSCharacter[] ch = Combat.getAttackingEntities();
    	if (ch instanceof RSNPC[]){
    		npc = (RSNPC[])ch;
    	}
    	return npc;
    }   
}
