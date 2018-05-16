package scripts.JarGenerator;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;

import scripts.JarGenerator.Antiban;
import scripts.JarGenerator.Utils;
import scripts.webwalker_logic.WebWalker;
import scripts.webwalker_logic.local.walker_engine.WalkingCondition;

public class HandleWalking extends Node {

	public HandleWalking(ACamera aCamera) {
		super(aCamera);
	}
	@Override
	public boolean validate() {
		return (Inventory.getCount(Vars.nature) == 1 && Inventory.getCount(Vars.eclectic) == 2 && Inventory.getCount(Vars.essence) == 3 && !Vars.puro.contains(Player.getPosition()))
				|| !Vars.bank.contains(Player.getPosition()) && Inventory.getCount(Vars.generator) == 1;
	}

	@Override
	public void execute() {
		PuroJars.status = "Walking";
		List<Integer> abc2WaitTimes = new ArrayList<>();	
		if (Inventory.getCount(Vars.nature) == 1 && Inventory.getCount(Vars.eclectic) == 2 && Inventory.getCount(Vars.essence) == 3) {
			RSObject[] circle = Objects.find(10, Vars.circle);
			if (circle.length > 0 && circle[0].isOnScreen()) 
			{
						if(Player.getRSPlayer().getAnimation() == 6601)
						{
							while(!Vars.puro.contains(Player.getPosition()))
							{
								General.sleep(1000,2000);
							}
						}
						else 
						{
							if (Clicking.click("Enter Centre of crop circle", circle[0])) 
							{
								Timing.waitCondition(new Condition() {
									@Override
									public boolean active() {
										General.sleep(100, 200);
										return Player.getRSPlayer().getAnimation() == 6601;
									}
								}, General.random(3000,4000));
							}
						}
			}
			else {
				WebWalker.walkTo(Vars.crop.getRandomTile(),  new WalkingCondition() {
				@Override
				public State action() {
					if (Game.getRunEnergy() < Vars.drinkAt) {
						Running();
					}
					return State.CONTINUE_WALKER;
				}});
			}
		}
		else if (!Vars.bank.contains(Player.getPosition()) && Inventory.getCount(Vars.generator) == 1){
			
			if (Vars.puro.contains(Player.getPosition())) 
			{
			RSObject[] portal = Objects.find(10, "Portal");
			if (portal != null && portal.length > 0) {
				if (!portal[0].isOnScreen()) {
					this.aCamera.turnToTile(portal[0].getPosition());
				}
				if (Clicking.click("Escape Portal", portal[0])) {
					while(!Vars.crop.contains(Player.getPosition())){
						General.sleep(1000,2000);
					}
				}
			}
		}
			else {
			WebWalker.walkToBank(new WalkingCondition() {
				@Override
				public State action() {
					Generate.generate();
					if (Game.getRunEnergy() < Vars.drinkAt) {
						Running();
					}
					return State.CONTINUE_WALKER;
				}});
			}
		}
	}
	public static void Running() {
		RSItem[] energy = Inventory.find(Vars.energy1,Vars.energy2,Vars.energy3,Vars.energy4);
		if (energy.length > 0) {
			if(Clicking.click("Drink", energy)) {
				Antiban.waitItemInteractionDelay(General.random(1, 3));
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return Game.getRunEnergy() > Vars.drinkAt;
					}
				}, General.random(600, 800));
			}
			Vars.drinkAt = General.random(General.random(15, 25), General.random(45, 55));
		}
		if(Inventory.getCount(229) == 1){
			RSItem[] vial = Inventory.find(229);
			if (vial.length > 0) {
				if(Clicking.click("Drop", vial)) {
					Antiban.waitItemInteractionDelay();
					Timing.waitCondition(new Condition() {
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(229) == 0;
						}
					}, General.random(1000, 2000));
				}
			}
		}
	}
}
