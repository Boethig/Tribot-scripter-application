package scripts.JarGenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.JarGenerator.antiban.Antiban;
import scripts.JarGenerator.data.Constants;
import scripts.JarGenerator.data.Vars;
import scripts.JarGenerator.nodes.Bank;
import scripts.JarGenerator.nodes.Exchange;
import scripts.JarGenerator.nodes.Generate;
import scripts.JarGenerator.nodes.HandleWalking;
import scripts.JarGenerator.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker_engine.WalkingCondition;

@ScriptManifest(authors = { "boe123" }, category = "Tools", name = "ImplingJarGenerator")
public class PuroJars extends Script implements Painting, MessageListening07, Starting, Ending {

	public static String status;
	public static boolean stopScript = false;
	private final Font font = new Font("Verdana", Font.BOLD, 13);
	public static ArrayList<Node> nodes = new ArrayList<Node>();
	private static final long startTime = System.currentTimeMillis();
	private ACamera aCamera = new ACamera(this);

	// Paint vars
	private final int natPrice = getPrice(Constants.NAT_IMPLING);
	private final int essPrice = getPrice(Constants.ESS_IMPLING);
	private final int eclPrice = getPrice(Constants.ECL_IMPLING);
	private final int jarPrice = getPrice(Constants.JAR);
	private final Image IMG = getImage("https://vignette4.wikia.nocookie.net/2007scape/images/f/ff/Impling_jar.png/revision/latest?cb=20140728191300");
	private final Image IMG2 = getImage("https://vignette2.wikia.nocookie.net/2007scape/images/a/a9/Jar_generator.png/revision/latest?cb=20140810112034");

	public void run() {
		initialize();
		while (Vars.get().shouldRun) {
			if (Login.getLoginState() != STATE.LOGINSCREEN) {
				for (final Node n : nodes) {
					if (n.validate()) {
						n.execute();
						status = n.status();
					} else {
						status = "Antiban";
						Antiban.timedActions();
					}
				}
			}
			General.sleep(50,70);
		}
	}
	
	public void initialize() {
		Collections.addAll(nodes, new Bank(aCamera), new HandleWalking(aCamera), new Exchange(aCamera), new Generate(aCamera));
		Mouse.setSpeed(General.random(103, 142));
		DaxWalker.setGlobalWalkingCondition(() -> {
			if (Game.getRunEnergy() <= Antiban.getRunAt()) {
				Utils.drinkEnergy();
			}
			return WalkingCondition.State.CONTINUE_WALKER;
		});
	}

	private Image getImage(String url) {
	    try {
	        return ImageIO.read(new URL(url));
	    } catch(IOException e) {
	        return null;
	    }
	}

	public void onPaint(Graphics g) {
		Graphics2D gg = (Graphics2D)g;
	    gg.drawImage(IMG, 390, 255, null);
	    gg.drawImage(IMG2, 415, 255, null);
	    
		long timeRan = System.currentTimeMillis() - startTime;
		int profit = (Vars.get().jarCount * jarPrice) - ((Vars.get().essUsed * essPrice) + (Vars.get().eclUsed * eclPrice) + (Vars.get().natUsed * natPrice));
		int pH = (int) ((profit/1000) * 3600000d / timeRan);
		 g.setFont(font);
	     g.setColor(Color.WHITE);
	     g.drawString("PuroJars", 310, 285);
	     g.drawString("Running for: " + Timing.msToString(timeRan), 310, 300);
	     g.drawString("Status : " + status, 310, 315);
	     g.drawString("Profit: " + pH + "k/h", 310, 330);
	}

	private int getPrice(int id) {
	    try {
	    	URL url = new URL("http://api.rsbuddy.com/grandExchange?a=guidePrice&i=" + id);
	        URLConnection con = url.openConnection();
	        con.setUseCaches(true);
	        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String[] data = br.readLine().replace("{", "").replace("}", "").split(",");
	        return Integer.parseInt(data[0].split(":")[1]);
	    } catch(Exception e) {}
	    return -1;
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void duelRequestReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void serverMessageReceived(String arg0) {
		if(arg0.equals("You have 1 charges left in your jar generator.") || arg0.equals("You don't have enough charges left on your jar generator to make an impling jar.")){
			Vars.get().isGenCharged = false;
		}		
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onEnd() {
		General.println("Thank's for using Boe123's PuroJars");
		General.println("Total Time Ran: " + Timing.msToString(System.currentTimeMillis() - startTime));
		General.println("Total Estimated Profit: " + (int) (((Vars.get().jarCount * jarPrice) - ((Vars.get().essUsed * essPrice) + (Vars.get().eclUsed * eclPrice) + (Vars.get().natUsed * natPrice))/1000)) + "K");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	}
}
