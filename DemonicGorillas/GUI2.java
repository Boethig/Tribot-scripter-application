package scripts.DemonicGorillas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.border.CompoundBorder;

import org.tribot.api2007.Combat;

import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.JDesktopPane;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.SpinnerNumberModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class GUI2 extends JFrame {

	public JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI2 window = new GUI2();
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
	public GUI2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 441, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Demonic Gorilla Killer");
		
		JPanel contentpane =  new  JPanel();
		frame.setContentPane(contentpane);
		contentpane.setLayout(null);
		
	   // Title //
		JLabel lblBoesDemonicGorilla = new JLabel("Boe123's Demonic Gorilla Killer");
		lblBoesDemonicGorilla.setFont(new Font("AR JULIAN", Font.BOLD | Font.ITALIC, 24));
		lblBoesDemonicGorilla.setBounds(12, 13, 394, 27);
		contentpane.add(lblBoesDemonicGorilla);
		
	   // Potion Selection //
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Use Super Combat Potions");
		chckbxNewCheckBox_1.setBackground(UIManager.getColor("Button.background"));
		chckbxNewCheckBox_1.setBounds(203, 53, 203, 27);
		contentpane.add(chckbxNewCheckBox_1);
		chckbxNewCheckBox_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		if(chckbxNewCheckBox_1.isSelected()){
			Variables.meleeOn = true;
		}
		else {
			Variables.meleeOn = false;
		}
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Use Ranged Potions");
		chckbxNewCheckBox.setBackground(UIManager.getColor("Button.background"));
		chckbxNewCheckBox.setBounds(203, 85, 159, 27);
		chckbxNewCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
		contentpane.add(chckbxNewCheckBox);
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		if(chckbxNewCheckBox.isSelected()){
			Variables.rangedOn = true;
		}
		else {
			Variables.rangedOn = false;
		}
			
		// Prayer Potions text & Amount
		JLabel lblPrayerPotions = new JLabel("Prayer Potions");
		lblPrayerPotions.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPrayerPotions.setBounds(12, 116, 98, 36);
		contentpane.add(lblPrayerPotions);
		
		JSpinner spinner = new JSpinner();
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Variables.ppotCount = (int)spinner.getValue();
			}
		});
		spinner.setModel(new SpinnerNumberModel(1, 1, 5, 1));
		spinner.setBounds(122, 121, 46, 27);
		contentpane.add(spinner);
		spinner.setToolTipText("");
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		// Buttons
		JButton btnConfigureEquipment = new JButton("Configure Equipment");
		btnConfigureEquipment.setForeground(SystemColor.desktop);
		btnConfigureEquipment.setIcon(null);
		btnConfigureEquipment.setBackground(SystemColor.controlHighlight);
		btnConfigureEquipment.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnConfigureEquipment.setBounds(12, 63, 183, 36);
		contentpane.add(btnConfigureEquipment);
		btnConfigureEquipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GUI3 equipment = new GUI3();
				equipment.setVisible(true);
			}
		});
		
		JButton btnNewButton = new JButton("START");
		btnNewButton.setBackground(SystemColor.controlHighlight);
		btnNewButton.setBounds(225, 195, 147, 53);
		contentpane.add(btnNewButton);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				String str = textField.getText();
				Variables.foodID = Integer.parseInt(str);				
			}
		});
		
		JButton btnDocumentation = new JButton("Documentation");
		btnDocumentation.setBackground(SystemColor.controlHighlight);
		btnDocumentation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDocumentation.setBounds(225, 129, 147, 53);
		contentpane.add(btnDocumentation);
		
		// Food ID entry & Eat percentage
		JLabel lblFoodId = new JLabel("Food ID:");
		lblFoodId.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFoodId.setBounds(12, 154, 84, 27);
		contentpane.add(lblFoodId);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setBounds(122, 154, 46, 27);
		contentpane.add(textField);
		textField.setColumns(1);
		
		JLabel lblEatAt = new JLabel("Eat at %:");
		lblEatAt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblEatAt.setBounds(12, 184, 64, 27);
		contentpane.add(lblEatAt);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value =(int)spinner_1.getValue();
				Variables.hpToEatAt = value;
			}
		});
		spinner_1.setModel(new SpinnerNumberModel(60, 1, 99, 1));
		spinner_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		spinner_1.setBounds(122, 187, 46, 27);
		contentpane.add(spinner_1);
	}
}
