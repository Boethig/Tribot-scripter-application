package scripts.POHplanks;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.POHplanks.Tasks.Bank;
import scripts.POHplanks.Tasks.Plank;

@ScriptManifest(authors = { "boe123" }, category = "Money Making", name = "POHplanks")
public class POHplanks extends Script implements Painting {
	
	public static String status;
	public static boolean conditions = true;
	public static boolean start = true;
	public static ArrayList<Node> nodes = new ArrayList<Node>();
	private static final long startTime = Timing.currentTimeMillis();
	
	@Override
	public void run() {
		// preloop
		preloop();
		// Add all nodes to nodelist
		Collections.addAll(nodes, new Bank(), new Plank());
		// start main loop
		loop(50,70);
	}
	
	private void preloop() {
		General.useAntiBanCompliance(true);
		Mouse.setSpeed(General.random(150, 220));
		ThreadSettings.get().setClickingAPIUseDynamic(true);
		Enums.main();
	}
	
	private void loop(int min, int max) {
		while (conditions) {
		if (Login.getLoginState() != STATE.LOGINSCREEN) {
			for (final Node node : nodes) {
				if (node.validate()) {
					node.execute();
				}
				sleep(General.random(min,max));
				}
			}
		}
	}
	Font font = new Font("Verdana", Font.BOLD, 13);
	private final Image img = getImage("https://i.imgur.com/3HXcxZz.png?1");
	
	public void onPaint(Graphics g) {   
		long timeRan = System.currentTimeMillis() - startTime;
		int planks = Vars.planks_made;
		int planksPerHr = (int) (planks * 3600000d / timeRan);
		
		Graphics2D gg = (Graphics2D)g;
		gg.drawImage(img, 0, 250, null);
		
		 g.setFont(font);
	     g.setColor(Color.white);
	     g.drawString("v1.46", 300, 350);
	     g.drawString("Running for: " + Timing.msToString(timeRan), 230, 375);
	     g.drawString("Status : " + status, 230, 390);
	     g.drawString("Planks made: " + planks + " (" + planksPerHr + "/hr)", 230, 405);
	}
	
	private Image getImage(String url) {
	    try {
	        return ImageIO.read(new URL(url));
	    } catch(IOException e) {
	        return null;
	    }
	}
}
