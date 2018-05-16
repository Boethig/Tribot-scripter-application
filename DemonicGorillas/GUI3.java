package scripts.DemonicGorillas;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.types.RSItem;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class GUI3 extends JFrame{
	
	private final Filetools filetools = new Filetools();
	//Melee labels
	private ImageIcon defaultIcon = new ImageIcon();
	private JLabel lblHelmet = new JLabel(defaultIcon);
	private JLabel lblCape = new JLabel(defaultIcon);
	private JLabel lblNecklace = new JLabel(defaultIcon);
	private JLabel lblAmmo = new JLabel(defaultIcon);
	private JLabel lblWeapon = new JLabel(defaultIcon);
	private JLabel lblChest = new JLabel(defaultIcon);
	private JLabel lblShield = new JLabel(defaultIcon);
	private JLabel lblLegs = new JLabel(defaultIcon);
	private JLabel lblGloves = new JLabel(defaultIcon);
	private JLabel lblRing = new JLabel(defaultIcon);
	private JLabel lblBoots = new JLabel(defaultIcon);

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI3 window = new GUI3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI3() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int setup;
		frame = new JFrame();
		frame.setAutoRequestFocus(false);
		frame.setBounds(100, 100, 287, 425);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
				lblWeapon.setBounds(58, 126, 40, 40);
				frame.getContentPane().add(lblWeapon);
				lblChest.setBounds(115, 126, 40, 40);
				frame.getContentPane().add(lblChest);
				lblShield.setBounds(171, 126, 40, 40);
				frame.getContentPane().add(lblShield);
				lblLegs.setBounds(115, 166, 40, 40);
				frame.getContentPane().add(lblLegs);
				lblGloves.setBounds(58, 207, 40, 40);
				frame.getContentPane().add(lblGloves);
				lblBoots.setBounds(115, 207, 40, 40);
				frame.getContentPane().add(lblBoots);		
				lblRing.setBounds(171, 207, 40, 40);
				frame.getContentPane().add(lblRing);
				lblCape.setBounds(0, 0, 264, -5);
				frame.getContentPane().add(lblCape);
				lblHelmet.setBounds(0, 0, 264, 10);
				frame.getContentPane().add(lblHelmet);
				lblNecklace.setBounds(0, 0, 264, -5);
				frame.getContentPane().add(lblNecklace);
				lblAmmo.setBounds(0, 0, 264, -5);
				frame.getContentPane().add(lblAmmo);
					

				String[] types = {"Melee","Ranged"};
				JComboBox comboBox = new JComboBox(types);
				comboBox.setBounds(102, 8, 70, 22);
				frame.getContentPane().add(comboBox);
				JButton btnSetGear = new JButton("Set gear");
				btnSetGear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						lblHelmet.setIcon(new ImageIcon(combineImage(getID(SLOTS.HELMET))));
						lblCape.setIcon(new ImageIcon(combineImage(getID(SLOTS.CAPE))));
						lblNecklace.setIcon(new ImageIcon(combineImage(getID(SLOTS.AMULET))));
						lblAmmo.setIcon(new ImageIcon(combineImage(getID(SLOTS.ARROW))));
						lblWeapon.setIcon(new ImageIcon(combineImage(getID(SLOTS.WEAPON))));
						lblChest.setIcon(new ImageIcon(combineImage(getID(SLOTS.BODY))));
						lblShield.setIcon(new ImageIcon(combineImage(getID(SLOTS.SHIELD))));
						lblLegs.setIcon(new ImageIcon(combineImage(getID(SLOTS.LEGS))));
						lblGloves.setIcon(new ImageIcon(combineImage(getID(SLOTS.GLOVES))));
						lblBoots.setIcon(new ImageIcon(combineImage(getID(SLOTS.BOOTS))));
						lblRing.setIcon(new ImageIcon(combineImage(getID(SLOTS.RING))));
						
						String type = (String)comboBox.getSelectedItem();
						if (type.equals("Melee")){
						for(RSItem i : Equipment.getItems()){
							Variables.Melee.add(i.getID());
						 }
						}
					else {
						for(RSItem i : Equipment.getItems()){
							Variables.Ranged.add(i.getID());
						 }
					}
				}
				});
				btnSetGear.setBounds(33, 326, 204, 25);
				frame.getContentPane().add(btnSetGear);
				
				JLabel equipmentLabel = new JLabel();
				equipmentLabel.setIcon(filetools.getIcon("Worn_equipment_tab"));
				equipmentLabel.setBounds(33, 38, 204, 275);
				frame.getContentPane().add(equipmentLabel);
				lblCape.setBounds(0, 0, 264, -5);
	}
	
	private BufferedImage combineImage(int ID) {
		BufferedImage image = null;
		BufferedImage overlay = null;
		try {
			image = ImageIO.read(filetools.getImage("equipmentbg"));
			overlay = ImageIO.read(filetools.getImage("" + ID));
		} catch (IOException e) {
		}
		if(image != null && overlay != null){
			int w = Math.max(image.getWidth(), overlay.getWidth());
			int h = Math.max(image.getHeight(), overlay.getHeight());
			BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics g = combined.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.drawImage(overlay, 4, 4, null);
			return combined;
		} else {
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
		
}
	private int getID(SLOTS slot){
		if(Equipment.getItem(slot) != null){
			return Equipment.getItem(slot).getID();
		} 
		return 0;
	}
	private void updateGear(String name){
		resetGearIcons();
		String gearString = filetools.loadPropertyFromFile(name);
		if(gearString != null && gearString.length() > 1){
			String[] gear = gearString.split(";");
			for(String i : gear){
				String[] temp = i.split(",");
				if(temp[1].equals("HELMET")){
					lblHelmet.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("AMULET")){
					lblNecklace.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("ARROW")){
					lblAmmo.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("WEAPON")){
					lblWeapon.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("BODY")){
					lblChest.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("SHIELD")){
					lblShield.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("LEGS")){
					lblLegs.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("GLOVES")){
					lblGloves.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("BOOTS")){
					lblBoots.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("RING")){
					lblRing.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				} else if (temp[1].equals("CAPE")){
					lblCape.setIcon(new ImageIcon(combineImage(Integer.parseInt(temp[0]))));
				}
			}
		}
	}
	
	private void resetGearIcons(){
		lblHelmet.setIcon(defaultIcon);
		lblCape.setIcon(defaultIcon);
		lblNecklace.setIcon(defaultIcon);
		lblAmmo.setIcon(defaultIcon);
		lblWeapon.setIcon(defaultIcon);
		lblChest.setIcon(defaultIcon);
		lblShield.setIcon(defaultIcon);
		lblLegs.setIcon(defaultIcon);
		lblGloves.setIcon(defaultIcon);
		lblBoots.setIcon(defaultIcon);
		lblRing.setIcon(defaultIcon);
	}	
}
