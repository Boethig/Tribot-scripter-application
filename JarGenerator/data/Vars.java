package scripts.JarGenerator.data;

public class Vars {
	
	private static Vars vars;
	
	public static Vars get() {
		return vars == null ? vars = new Vars() : vars;
	}
	public boolean shouldRun = true;
	public int natUsed = 0;
	public int essUsed = 0;
	public int eclUsed = 0;
	public int jarCount = 0;
	public boolean isGenCharged = true;
	public int worldHops;
	public int worldHopFreq;
	public long lastHopTime;
}
