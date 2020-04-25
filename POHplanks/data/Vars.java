package scripts.POHplanks.data;

import java.util.ArrayList;
import java.util.List;

public class Vars {

	private static Vars vars;
	
	public static Vars get() {
		return vars == null ? vars = new Vars() : vars;
	}

	public boolean shouldRun = true;
	public String status;
	public String subStatus = "";
	public String errorMessage = "";
	public List<Integer> abc2WaitTimes = new ArrayList<>();
	public long timeRan;
	public LogInfo plank = LogInfo.NORMAL;
	public String teleport;
	public long breakTimes;
	public boolean useServantsMoneybag;
	public boolean useRingOfLife;
	public int planksMade = 0;
	public int pvpWorld = 325;
	public int coinsInMoneybag = 0;
	public int refillCoinsAt = 0;
	public int coinDepositAmount = 0;
	public double abcReactionPercentage;
}
