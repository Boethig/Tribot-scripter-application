package scripts.DemonicGorillas;

import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Locations {
	//Grand tree location
	public static RSArea TREE = new RSArea( new RSTile(2464,3497, 0), new RSTile(2467,3493,0));
	
	//Bank area
	public static RSArea BANK = new RSArea( new RSTile(2450,3482,1), new RSTile(2448,3482,1));
	
	//Opening area & tiles
	public static RSArea OPENING = new RSArea( new RSTile(2433,3519,0), new RSTile(2437,3517,0));
	public static RSTile ENTERED = new RSTile(1987,5568,0);
	
	//Area of Demonic Gorillas
	public static RSArea GORILLAS = new RSArea(new RSTile[] { new RSTile(2120, 5659, 0), new RSTile(2120, 5662, 0), new RSTile(2119, 5663, 0), new RSTile(2119, 5664, 0), new RSTile(2118, 5663, 0), new RSTile(2117, 5664, 0), new RSTile(2116, 5663, 0), new RSTile(2116, 5664, 0), new RSTile(2115, 5664, 0), new RSTile(2115, 5665, 0), new RSTile(2114, 5665, 0), new RSTile(2113, 5665, 0), new RSTile(2113, 5666, 0), new RSTile(2112, 5666, 0), new RSTile(2111, 5665, 0), new RSTile(2110, 5664, 0), new RSTile(2109, 5663, 0), new RSTile(2108, 5662, 0), new RSTile(2107, 5662, 0), new RSTile(2106, 5661, 0), new RSTile(2104, 5661, 0), new RSTile(2103, 5660, 0), new RSTile(2100, 5660, 0), new RSTile(2098, 5661, 0), new RSTile(2095, 5661, 0), new RSTile(2094, 5660, 0), new RSTile(2093, 5659, 0), new RSTile(2093, 5657, 0), new RSTile(2092, 5656, 0), new RSTile(2092, 5655, 0), new RSTile(2093, 5654, 0), new RSTile(2092, 5647, 0), new RSTile(2094, 5647, 0), new RSTile(2094, 5645, 0), new RSTile(2093, 5644, 0), new RSTile(2096, 5643, 0), new RSTile(2095, 5641, 0), new RSTile(2096, 5639, 0), new RSTile(2099, 5640, 0), new RSTile(2100, 5639, 0), new RSTile(2101, 5638, 0), new RSTile(2104, 5639, 0), new RSTile(2110, 5645, 0), new RSTile(2112, 5645, 0), new RSTile(2114, 5648, 0), new RSTile(2115, 5649, 0), new RSTile(2114, 5651, 0), new RSTile(2115, 5653, 0), new RSTile(2115, 5654, 0), new RSTile(2115, 5655, 0), new RSTile(2115, 5657, 0), new RSTile(2117, 5656, 0), new RSTile(2119, 5658, 0), new RSTile(2120, 5659, 0) });
	//Path to cave tiles
	public static RSArea area1_path = new RSArea ( new RSTile(1994,5582,0), new RSTile(1997,5579,0));
	public static RSArea area2_path = new RSArea ( new RSTile(2005,5589,0), new RSTile(2008,5585,0));
	public static RSArea area3_path = new RSArea ( new RSTile(2014,5601,0), new RSTile(2017,5597,0));
	public static RSArea CAVERN_ENTERANCE = new RSArea( new RSTile(2026,5611,0), new RSTile(2029,5609,0));
	public static RSTile path1;
	public static RSTile path2;
	public static RSTile path3;
	public static RSTile Cave;
	public static RSTile[] Path_to_cavern_entrance = { path1, path2, path3, Cave};
	
	//Inside Cavern & Cavern Path
	public static RSArea CAVERN_AREA = new RSArea ( new RSTile(2128,5648,0), new RSTile(2131,5646,0));
	public static RSTile[] cavern_Path = { new RSTile(2120,5660,0), new RSTile(2111,5654,0), new RSTile(2109,5649)};
	public static RSTile[] obstruction = { new RSTile(2105,5651,0),new RSTile(2105,5652,0),new RSTile(2105,5653,0),
			new RSTile(2106,5651,0),new RSTile(2106,5652,0),new RSTile(2106,5653,0),
			new RSTile(2107,5651,0),new RSTile(2107,5652,0),new RSTile(2107,5653,0),
			new RSTile(2103,5646,0),new RSTile(2103,5647,0),new RSTile(2103,5648,0),
			new RSTile(2102,5646,0),new RSTile(2102,5647,0),new RSTile(2102,5648,0), new RSTile(2102,5649,0),
			new RSTile(2101,5646,0),new RSTile(2101,5647,0),new RSTile(2101,5648,0), new RSTile(2101,5649,0),
			new RSTile(2100,5646,0),new RSTile(2106,5647,0),new RSTile(2106,5648,0),
			new RSTile(2110,5656,0),new RSTile(2110,5657,0),new RSTile(2110,5658,0), new RSTile(2110,5659,0),
			new RSTile(2109,5656,0),new RSTile(2109,5657,0),new RSTile(2109,5658,0), new RSTile(2109,5659,0),
			new RSTile(2108,5656,0),new RSTile(2108,5657,0),new RSTile(2108,5658,0), new RSTile(2108,5659,0),
			new RSTile(2111,5656,0),new RSTile(2111,5657,0),new RSTile(2111,5658,0), new RSTile(2111,5659,0),
			new RSTile(2107,5657,0),new RSTile(2107,5658,0),
			new RSTile(2115,5659,0),new RSTile(2115,5660,0),new RSTile(2115,5661,0),
			new RSTile(2116,5659,0),new RSTile(2116,5660,0),new RSTile(2116,5661,0),
			new RSTile(2117,5659,0),new RSTile(2117,5660,0),new RSTile(2117,5661,0),
			new RSTile(2100,5654,0),new RSTile(2100,5655,0),new RSTile(2100,5656,0),
			new RSTile(2099,5653,0),new RSTile(2099,5654,0),new RSTile(2099,5655,0), new RSTile(2099,5656,0),
			new RSTile(2098,5653,0),new RSTile(2098,5654,0),new RSTile(2098,5655,0), new RSTile(2098,5656,0),
			new RSTile(2097,5653,0),new RSTile(2097,5654,0),new RSTile(2097,5655,0), new RSTile(2097,5656,0),
			};

}
