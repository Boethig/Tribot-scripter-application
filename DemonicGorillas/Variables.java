package scripts.DemonicGorillas;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

public class Variables {
	//Food
	public static int foodID;
	public static int hpToEatAt;
	public static int drinkAt;
	public static boolean rangedOn;
	public static boolean meleeOn;
	public static int ppotCount;
	public static int counter;
	
	//Projectiles
	public static int projID;
	public static boolean projTarget;
	public static RSTile projTile;
	public static boolean FallingRock = false;
	
	public static int PrayerPot[] = {143,141,139,2434};
	public static int RangePot[] = {173,171,169,2444};
	public static int CombatPot[] = {12701,12699,12697,12695};
	public static RSInterface getOpeningInterface() {
		return Interfaces.get(219, 0);
	}
	public static int Range_weapon=12926;
	public static int Melee_weapon=12006;
	
	public static List<Integer> Melee = new ArrayList<>();
	public static List<Integer> Ranged = new ArrayList<>();
	//
}
