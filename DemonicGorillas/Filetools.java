package scripts.DemonicGorillas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.tribot.api2007.Player;
import org.tribot.util.Util;

public class Filetools {
	
		private Properties properties = new Properties();
		private String profilePath = Util.getWorkingDirectory().toString() + "\\GBarrows\\character profiles\\";
		private String spritePath = Util.getWorkingDirectory().toString() + "\\GBarrows\\sprites\\";
		private String website = "https://gscripting.com/images/runescape/itemsprites/";

		
		public void savePropertyToFile(String propertyname, String value){
			try (FileReader fr = new FileReader(profilePath + Player.getRSPlayer().getName()); FileWriter frw = new FileWriter(profilePath + Player.getRSPlayer().getName());) {
				properties.load(fr);
				properties.put(propertyname, value);
				properties.store(frw, "");
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}	
		
		public String loadPropertyFromFile(String property){
			try (FileReader fr = new FileReader(profilePath + Player.getRSPlayer().getName())){
				properties.load(fr);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return properties.getProperty(property);
		}	
		
		
		public boolean createCharacterSaveFile(){
			if(!new File(profilePath, Player.getRSPlayer().getName()).isFile()){
				File f = new File(profilePath);
				File f2 = new File(profilePath, Player.getRSPlayer().getName());
				f.mkdirs();
				try {
					f2.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return new File(profilePath, Player.getRSPlayer().getName()).isFile();
		}
		
		public ImageIcon getIcon(String id) {
			if(!new File(spritePath, "" + id + ".png").isFile()){
				try {
					 URL url = new URL(website + id + ".png");
	                 BufferedImage img = ImageIO.read(url);
	                 File f = new File(spritePath + id + ".png");
	 				 f.mkdirs();
	                 ImageIO.write(img, "png", f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			return new ImageIcon(spritePath + id + ".png");
		}
		
		public File getImage(String name) {
			if(!new File(spritePath, "" + name + ".png").isFile()){
				try {
					 URL url = new URL(website + name + ".png");
	                 BufferedImage img = ImageIO.read(url);
	                 File f = new File(spritePath + name + ".png");
	 				 f.mkdirs();
	                 ImageIO.write(img, "png", f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			return new File(spritePath, "" + name + ".png");
		}
	
}
