package scripts.POHplanks;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSInterface;

public class Vars {
	
public static int reg_log = 1511;
public static int reg_plank = 960;
public static int oak_log = 1521;
public static int oak_plank = 8778;
public static int teak_log = 6333;
public static int teak_plank = 8780;
public static int mahog_log = 6332;
public static int mahog_plank = 8782;

public static int PLANK;
public static int LOGS;
public static String LOG_NAME;
public static String TELEPORT;
public static RSArea TeleLoc;
public static int AIR = 556;
public static int EARTH= 557;
public static int COINS = 995;
public static int LAW = 563;
public static int STAFF = 20736;
public static int planks_made;
public static int iP;

public static RSInterface getHomeInterface() {
	return Interfaces.get(261, 77);
}
public static RSInterface getCallInterface() {
	return Interfaces.get(370, 19);
}
public static RSInterface getEnterInterface(){
	return Interfaces.get(162,32);
}
public static RSInterface getbondInterface(){
	return Interfaces.get(65,3);
}

}
