package scripts.JarGenerator.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.WorldHopper;
import scripts.JarGenerator.data.Vars;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;

public class HandleWorldHopper extends Node {

	public HandleWorldHopper(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() {
		/**
		 * Condition:
		 * We still have hops AND
		 * We are freq time from the last hop
		 */
		return Vars.get().worldHops >= 1 && (System.currentTimeMillis() > (Vars.get().lastHopTime + (Vars.get().worldHopFreq * 60 * 1000))) ;
	}

	@Override
	public void execute() {
		int world = WorldHopper.getRandomWorld(true);
		if (WorldHopper.changeWorld(world)) {
			Timing.waitCondition(() -> {
				return Game.getCurrentWorld() == world;
			}, General.random(5000, 7000));
		}
	}

	@Override
	public String status() {
		return null;
	}
}
