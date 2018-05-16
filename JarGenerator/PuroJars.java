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

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.Player;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import scripts.JarGenerator.Antiban;
import scripts.JarGenerator.Node;
import scripts.webwalker_logic.WebWalker;
import scripts.JarGenerator.ACamera;

@ScriptManifest(authors = { "boe123" }, category = "Tools", name = "PJTest")
public class PuroJars extends Script implements Painting, MessageListening07 {
	
	public static String genStatus;
	public static String status;
	public static boolean stopScript = false;
	public static ABCUtil abc = new ABCUtil();
	public static ArrayList<Node> nodes = new ArrayList<Node>();
	private ACamera aCamera = new ACamera(this);
	public void run() {
		Vars.drinkAt = General.random(10, 42);
		Mouse.setSpeed(General.random(80, 108));
		General.useAntiBanCompliance(true);
		Collections.addAll(nodes, new Bank(aCamera), new HandleWalking(aCamera), new Exchange(aCamera), new Generate(aCamera));
		while(!stopScript) {
			if (Login.getLoginState() != STATE.LOGINSCREEN) {
				for(final Node n : nodes){
					if(n.validate()){
						n.execute();
					}
					General.sleep(20,30);
					}
				}
				General.sleep(80,100);	
				}
	}
	
	private static final long startTime = System.currentTimeMillis();
	Font font = new Font("Verdana", Font.BOLD, 13);
	
	//Profit counter
	int natPrice = getPrice(Vars.nature);
	int essPrice = getPrice(Vars.essence);
	int eclPrice = getPrice(Vars.eclectic);
	int jarPrice = getPrice(Vars.jar);
	
	private Image getImage(String url) {
	    try {
	        return ImageIO.read(new URL(url));
	    } catch(IOException e) {
	        return null;
	    }
	}
	private final Image IMG = getImage("https://vignette4.wikia.nocookie.net/2007scape/images/f/ff/Impling_jar.png/revision/latest?cb=20140728191300"); // http://i.imgur.com/TUlYgDr.png
	private final Image IMG2 = getImage("https://vignette2.wikia.nocookie.net/2007scape/images/a/a9/Jar_generator.png/revision/latest?cb=20140810112034");
	public void onPaint(Graphics g) {
		Graphics2D gg = (Graphics2D)g;
	    gg.drawImage(IMG, 390, 255, null);
	    gg.drawImage(IMG2, 415, 255, null);
	    
		long timeRan = System.currentTimeMillis() - startTime;
		int profit = (Vars.jarC * jarPrice) - ((Vars.essC * essPrice) + (Vars.eclC * eclPrice) + (Vars.natC * natPrice));
		int pH = (int) ((profit/1000) * 3600000d / timeRan);
		 g.setFont(font);
	     g.setColor(Color.WHITE);
	     g.drawString("PuroJars", 310, 285);
	     g.drawString("Running for: " + Timing.msToString(timeRan), 310, 300);
	     g.drawString("Status : " + status, 310, 315);
	     g.drawString("Profit: " + pH + "k/h", 310, 330);
	}
	private int getPrice(int id){
	    try {
	        URL url = new URL("http://api.rsbuddy.com/grandExchange?a=guidePrice&i=" + id);
	        URLConnection con = url.openConnection();
	        con.setUseCaches(true);
	        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String[] data = br.readLine().replace("{", "").replace("}", "").split(",");
	        return Integer.parseInt(data[0].split(":")[1]);
	    } catch(Exception e){
	    }
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
		if( arg0.equals("You have 1 charges left in your jar generator.") || arg0.equals("You don't have enough charges left on your jar generator to make an impling jar.")){
			Vars.Gen = false;
			genStatus = "No charges left: false";
		}		
	}
	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
