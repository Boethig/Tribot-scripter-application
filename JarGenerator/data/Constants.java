package scripts.JarGenerator.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants {
	public final static int JAR = 11260;
	public final static int GRAIN = 1947;
	public final static int GENERATOR = 11258;
	public final static int ESS_IMPLING = 11246;
	public final static int ECL_IMPLING = 11248;
	public final static int NAT_IMPLING = 11250;
	public final static int CROP_CIRCLE = 24991;
	public final static int ELNOK = 5734;
	public final static int EXIT_PORTAL = 25014;
	public final static int[] ENERGY = {3008, 3010, 3012, 3014};

	public final static RSArea BANK = new RSArea(new RSTile[] { new RSTile(2387, 4455, 0), new RSTile(2386, 4454, 0), new RSTile(2382, 4454, 0), new RSTile(2380, 4456, 0), new RSTile(2380, 4460, 0), new RSTile(2381, 4464, 0), new RSTile(2385, 4464, 0), new RSTile(2387, 4461, 0), new RSTile(2388, 4461, 0), new RSTile(2388, 4457, 0), new RSTile(2387, 4455, 0) });
	public final static RSArea PURO = new RSArea(new RSTile[] { new RSTile(2599, 4312, 0), new RSTile(2584, 4328, 0)});
	public final static RSArea CROP_AREA = new RSArea(new RSTile[] { new RSTile(2424, 4444, 0), new RSTile(2430, 4449, 0)});
}
