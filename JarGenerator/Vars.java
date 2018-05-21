package scripts.scripts.JarGenerator;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Vars {
	
	//ints
	static int jar = 11260;
	static int generator = 11258;
	static int essence = 11246;
	static int eclectic = 11248;
	static int nature = 11250;
	static int circle = 24991;
	static int elnok = 5734;
	static int portal = 25014;
	static int energy4 = 3008;
	static int energy3 = 3010;
	static int energy2 = 3012;
	static int energy1 = 3014;
	static int drinkAt;
	
	//Counters
	static int count;
	static int bankType;
	static int randBank;
	static int natC = 0;
	static int essC = 0;
	static int eclC = 0;
	static int jarC = 0;
	
	
	//boolean
	static boolean countReset = true;
	static boolean Gen = true;
	
	//RSTiles
	static RSArea bank = new RSArea(new RSTile[] { new RSTile(2387, 4455, 0), new RSTile(2386, 4454, 0), new RSTile(2382, 4454, 0), new RSTile(2380, 4456, 0), new RSTile(2380, 4460, 0), new RSTile(2381, 4464, 0), new RSTile(2385, 4464, 0), new RSTile(2387, 4461, 0), new RSTile(2388, 4461, 0), new RSTile(2388, 4457, 0), new RSTile(2387, 4455, 0) });
	static RSArea puro = new RSArea(new RSTile[] { new RSTile(2599, 4312, 0), new RSTile(2584, 4312, 0), new RSTile(2584, 4328, 0), new RSTile(2600, 4327, 0) });
	static RSArea crop = new RSArea(new RSTile[] { new RSTile(2429, 4448, 0), new RSTile(2429, 4444, 0), new RSTile(2425, 4444, 0), new RSTile(2425, 4448, 0) });
	static RSArea pExit = new RSArea(new RSTile[] {
	    new RSTile(2590, 4321, 0), new RSTile(2590, 4320, 0), 
	    new RSTile(2590, 4319, 0), new RSTile(2590, 4318, 0), 
	    new RSTile(2591, 4318, 0), new RSTile(2592, 4318, 0), 
	    new RSTile(2593, 4318, 0), new RSTile(2593, 4319, 0), 
	    new RSTile(2593, 4320, 0), new RSTile(2593, 4321, 0), 
	    new RSTile(2592, 4321, 0), new RSTile(2591, 4321, 0)});
}
