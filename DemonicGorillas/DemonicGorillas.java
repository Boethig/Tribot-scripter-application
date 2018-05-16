package scripts.DemonicGorillas;

import java.awt.Color;
import java.awt.EventQueue;
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
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projectiles;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.types.RSProjectile;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;

import scripts.DemonicGorillas.Node;
import scripts.DemonicGorillas.Antiban;
import scripts.DemonicGorillas.Methods.Attack;
import scripts.DemonicGorillas.Methods.Bank;
import scripts.DemonicGorillas.Methods.Eat;
import scripts.DemonicGorillas.Methods.HandleWalking;
import scripts.DemonicGorillas.Methods.Loot;


public class DemonicGorillas extends Script implements Painting {
	
	public static String status;
	public static boolean stopScript = false;
	public static ABCUtil abc = new ABCUtil();
	public static ArrayList<Node> nodes = new ArrayList<Node>();
	private GUI2 frame;
	
	Thread Tracker = new Thread (new Runnable()
	{
		@Override
		public void run() 
		{
			while (!DemonicGorillas.stopScript) 
			{
				RSProjectile[] projectiles = Projectiles.getAll();
				for (RSProjectile proj: projectiles) {
					Variables.projID = proj.getGraphicID();
					Variables.projTarget = proj.isTargetingMe();		
					Variables.projTile = proj.getPosition();
					if(proj.getGraphicID() == Constants.BOULDER && proj.getPosition().equals(Player.getPosition()))
					{
						Variables.FallingRock = true;
						status = "Boulder detected";
						Utils.stepTile();
					}
					else 
					{
						Variables.FallingRock = false;
					}
					General.sleep(50,70);
				}
				General.sleep(30,60);
			}
		}		
	});
	
public void run() {
	Mouse.setSpeed(General.random(120, 140));
	General.useAntiBanCompliance(true);
	ThreadSettings.get().setClickingAPIUseDynamic(true);
	Collections.addAll(nodes, new Eat(), new Attack(), new Loot(), new Bank(), new HandleWalking());		
	if (Login.getLoginState() == STATE.INGAME) {
		initialize();
	}	
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
	
private void initialize() {
	Variables.drinkAt = General.random(18, Skills.getActualLevel(SKILLS.PRAYER) - (Skills.getActualLevel(SKILLS.PRAYER) / 4 + 7));
    initGUI();
    Combat.setAutoRetaliate(false);
    Tracker.start();
}
private void initGUI(){
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				frame = new GUI2();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
}
private static final long startTime = System.currentTimeMillis();
Font font = new Font("Verdana", Font.BOLD, 12);

private Image getImage(String url) {
    try {
        return ImageIO.read(new URL(url));
    } catch(IOException e) {
        return null;
    }
}
private final Image IMG = getImage("http://imgur.com/sNhCwQT.png"); // http://i.imgur.com/TUlYgDr.png

public void onPaint(Graphics g) {
	Graphics2D gg = (Graphics2D)g;
    gg.drawImage(IMG, 200, 345, null);
    
	long timeRan = System.currentTimeMillis() - startTime;
	int profit;
	
	 g.setFont(font);
     g.setColor(Color.WHITE);
     g.drawString("Demonic_Gorillas V1.0", 279, 360);
     g.drawString("Running for: " + Timing.msToString(timeRan), 279, 375);
     g.drawString("Status : " + status, 279, 390);
}
// Gets the price of an item from the rsbuddy api
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
}
