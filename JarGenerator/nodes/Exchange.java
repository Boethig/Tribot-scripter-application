package scripts.JarGenerator.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import scripts.JarGenerator.PuroJars;
import scripts.JarGenerator.data.Constants;
import scripts.JarGenerator.data.Vars;
import scripts.JarGenerator.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.boe_api.npc.NPCFinder;
import scripts.dax_api.walker.utils.AccurateMouse;


public class Exchange extends Node {

	public Exchange(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() {
		return Constants.PURO.contains(Player.getPosition()) && Utils.hasImplings();
	}

	@Override
	public void execute() {
		if (Interfaces.isInterfaceSubstantiated(540, 125)) {
			RSInterface confirm = Interfaces.get(540, 125);
			if (collectGenerator(confirm)) {
				Vars.get().isGenCharged = true;
				incrementCounters();
			}
		} else if (Interfaces.isInterfaceSubstantiated(540, 120)) {
			RSInterface select = Interfaces.get(540, 120);
			if (AccurateMouse.click(select)) {
				Timing.waitCondition(() -> {
					General.sleep(100, 200);
					return Interfaces.isInterfaceSubstantiated(540, 125);
				}, General.random(2000, 4000));
			}
		} else {
			tradeElnok();
		}
	}

	public boolean collectGenerator(RSInterface confirm) {
		return AccurateMouse.click(confirm) && Timing.waitCondition(() -> {
				General.sleep(100, 200);
				return !Interfaces.isInterfaceSubstantiated(540) && Inventory.getCount(Constants.GENERATOR) > 0;
			}, General.random(2000, 4000));
	}

	public boolean tradeElnok() {
		RSNPC elnok = NPCFinder.findFirst(Constants.ELNOK);
		if (elnok != null) {
			if (!elnok.isOnScreen()|| !elnok.isClickable()) {
				aCamera.turnToTile(elnok.getPosition());
			}
			if (AccurateMouse.click(elnok, "Trade")) {
				Timing.waitCondition(() -> {
					General.sleep(100, 200);
					return Interfaces.isInterfaceValid(540);
				}, General.random(4000,6000));
			}
		}
		return false;
	}

	private void incrementCounters() {
		Vars.get().natUsed += 1;
		Vars.get().eclUsed += 2;
		Vars.get().essUsed += 3;
	}

	@Override
	public String status() {
		return "Collecting Generator";
	}
}	


