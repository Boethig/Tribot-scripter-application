package scripts.POHplanks;

import javafx.application.Platform;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.*;
import scripts.POHplanks.antiban.Antiban;
import scripts.POHplanks.data.Vars;
import scripts.POHplanks.fx.Controller;
import scripts.POHplanks.tasks.Bank;
import scripts.POHplanks.tasks.HopWorlds;
import scripts.POHplanks.tasks.Plank;
import scripts.POHplanks.tasks.Safeguard;
import scripts.POHplanks.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.boe_api.gui.GuiLoader;
import scripts.boe_api.images.ImageHelper;
import scripts.rsitem_services.GrandExchange;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ScriptManifest(authors = { "boe123" }, category = "Money Making", name = "POHplanker")
public class POHplanks extends Script implements Painting, Starting, Ending, Breaking, PreBreaking, MessageListening07, PreEnding {

	public static ArrayList<Node> nodes = new ArrayList<>();
	public static ACamera camera = new ACamera();
	public final Image paintImage = ImageHelper.getImage("https://i.imgur.com/ZLdJyGS.png");
	public final Font font  = new Font("Calibri", Font.PLAIN, 15);
	private Controller controller = new Controller();
	private final GuiLoader guiLoader = new GuiLoader.Builder()
			.setFxmlString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					"\n" +
					"<?import com.jfoenix.controls.JFXButton?>\n" +
					"<?import com.jfoenix.controls.JFXCheckBox?>\n" +
					"<?import com.jfoenix.controls.JFXComboBox?>\n" +
					"<?import com.jfoenix.controls.JFXSlider?>\n" +
					"<?import com.jfoenix.controls.JFXTextField?>\n" +
					"<?import javafx.scene.control.Label?>\n" +
					"<?import javafx.scene.image.ImageView?>\n" +
					"<?import javafx.scene.layout.AnchorPane?>\n" +
					"<?import javafx.scene.text.Text?>\n" +
					"\n" +
					"<AnchorPane prefHeight=\"400.0\" prefWidth=\"600.0\" xmlns=\"http://javafx.com/javafx/11.0.1\" xmlns:fx=\"http://javafx.com/fxml/1\">\n" +
					"   <children>\n" +
					"      <JFXButton fx:id=\"startScript\" buttonType=\"RAISED\" layoutX=\"242.0\" layoutY=\"340.0\" onAction=\"#handleButtonAction\" prefHeight=\"46.0\" prefWidth=\"117.0\" style=\"-fx-background-color: #ffffff;\" text=\"Start Script\" />\n" +
					"      <AnchorPane layoutX=\"1.0\" prefHeight=\"329.0\" prefWidth=\"600.0\">\n" +
					"         <children>\n" +
					"            <JFXComboBox fx:id=\"logType\" layoutX=\"14.0\" layoutY=\"107.0\" prefHeight=\"46.0\" prefWidth=\"237.0\" />\n" +
					"            <JFXCheckBox fx:id=\"useServantsMoneybag\" layoutX=\"321.0\" layoutY=\"85.0\" onAction=\"#handleServantMoneybag\" prefHeight=\"18.0\" prefWidth=\"184.0\" text=\"Use Servant's moneybag\" />\n" +
					"            <JFXComboBox fx:id=\"teleportLocation\" layoutX=\"14.0\" layoutY=\"186.0\" prefHeight=\"46.0\" prefWidth=\"237.0\" />\n" +
					"            <ImageView fx:id=\"guiTitle\" fitHeight=\"94.0\" fitWidth=\"338.0\" layoutX=\"140.0\" layoutY=\"-9.0\" pickOnBounds=\"true\" preserveRatio=\"true\" />\n" +
					"            <JFXSlider fx:id=\"abcReactionTime\" blockIncrement=\"5.0\" layoutX=\"330.0\" layoutY=\"277.0\" minorTickCount=\"5\" prefHeight=\"14.0\" prefWidth=\"250.0\" showTickLabels=\"true\" showTickMarks=\"true\" snapToTicks=\"true\" />\n" +
					"            <Text layoutX=\"14.0\" layoutY=\"98.0\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"Type of logs:\" wrappingWidth=\"165.13671875\" />\n" +
					"            <Text layoutX=\"14.0\" layoutY=\"178.0\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"Teleport Location:\" />\n" +
					"            <Text layoutX=\"320.0\" layoutY=\"259.0\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"Antiban Reaction Time %:\" />\n" +
					"            <JFXCheckBox fx:id=\"useRingOfLife\" layoutX=\"321.0\" layoutY=\"209.0\" prefHeight=\"18.0\" prefWidth=\"134.0\" text=\"Use Ring of life\" />\n" +
					"            <JFXTextField fx:id=\"offerAmount\" layoutX=\"427.0\" layoutY=\"110.0\" />\n" +
					"            <Label layoutX=\"336.0\" layoutY=\"122.0\" text=\"Offer amount:\" />\n" +
					"            <Label layoutX=\"336.0\" layoutY=\"165.0\" prefHeight=\"17.0\" prefWidth=\"75.0\" text=\"Refill coins at:\" />\n" +
					"            <JFXTextField fx:id=\"offerAt\" layoutX=\"427.0\" layoutY=\"153.0\" />\n" +
					"            <JFXComboBox fx:id=\"world\" layoutX=\"14.0\" layoutY=\"270.0\" prefHeight=\"38.0\" prefWidth=\"237.0\" />\n" +
					"            <Label layoutX=\"14.0\" layoutY=\"246.0\" prefHeight=\"17.0\" prefWidth=\"75.0\" text=\"PVP World:\" />\n" +
					"         </children>\n" +
					"      </AnchorPane>\n" +
					"   </children>\n" +
					"</AnchorPane>")
			.setController(controller)
			.build();
	public int logPrice;
	public int plankPrice;

	@Override
	public void run() {
		Collections.addAll(nodes, new HopWorlds(camera), new Safeguard(camera), new Plank(camera), new Bank(camera));
		if (guiLoader.loadGui()) {
			initialize();
			while (Vars.get().shouldRun) {
				if (Login.getLoginState() != STATE.LOGINSCREEN) {
					for (final Node node : nodes) {
						if (node.validate()) {
							Vars.get().status = node.status();
							node.execute();
						}
						Antiban.timedActions();
					}
				}
				General.sleep(50,70);
			}
		}
	}
	
	public void onStart() {
		Vars.get().status = "Initializing script";
	}
	
	public void onEnd() {
		if (Login.getLoginState().equals(STATE.INGAME)) {
			if (Utils.get().teleportToSafety()) {
				Login.logout();
			}
		}
		General.println("Thank you for using boe123's POHPlanks");
		General.println("Total run time: " + Timing.msToString(System.currentTimeMillis() - Vars.get().timeRan));
		General.println(Vars.get().errorMessage.length() > 0 ? ("Script terminated because: " + Vars.get().errorMessage) : "Total Planks Made: " + Vars.get().planksMade);
	}
	
	private void initialize() {
		Vars.get().timeRan = Timing.currentTimeMillis();
		Vars.get().abc2WaitTimes.add(General.random(500, 1500));
		Mouse.setSpeed(General.random(100, 140));
		logPrice = GrandExchange.getPrice(Vars.get().plank.getLogId());
		plankPrice = GrandExchange.getPrice(Vars.get().plank.getLogId());
		while (Login.getLoginState() != STATE.INGAME)
			General.sleep(500);
		Utils.get().enableSafetyFeatures();
	}

	public void onPaint(Graphics g) {
		long timeRan = System.currentTimeMillis() - Vars.get().timeRan - Vars.get().breakTimes;
		int profit = (Vars.get().planksMade * plankPrice) - (Vars.get().planksMade * Vars.get().plank.getCost() + logPrice);
		int hourlyPlanksMade = (int) (Vars.get().planksMade * 3600000d / timeRan);
		Graphics2D gg = (Graphics2D) g;
		gg.drawImage(paintImage, 1, 280, null);
		g.setFont(font);
		g.drawString("Runtime: " + Timing.msToString(timeRan), 10, 360);
		g.drawString("Status : " + Vars.get().status + " " + Vars.get().subStatus, 10, 385);
		g.drawString("Profit: " + " (" + (int)((profit/1000) * 3600000d / timeRan) + "k/hr)", 10, 410);
		g.drawString("Planks Made: " + Vars.get().planksMade + " (" + hourlyPlanksMade + ")", 10, 435);
		g.drawString("Servant's moneybag: " + (long) (Vars.get().coinsInMoneybag/1000) + "k", 10, 460);
	}

	@Override
	public void onBreakEnd() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBreakStart(long breakTime) {
		Vars.get().breakTimes += breakTime;
		Vars.get().status = "On break";
		Vars.get().subStatus = "";
	}

	@Override
	public void onPreBreakStart(long l) {
		Utils.get().teleportToSafety();
	}

	@Override
	public void serverMessageReceived(String message) {
		if (message.contains("You need a bell pull in your house") || message.contains("You must hire a servant")) {
			Vars.get().shouldRun = false;
			Vars.get().errorMessage = message;
		} else if (message.contains("Your servant takes some payment from the moneybag.")) {
			String payment = message.replace("<col=ef1020>", "");
			Matcher m = Pattern.compile("\\d+(,\\d+)*").matcher(payment);
			while (m.find()) {
				Vars.get().coinsInMoneybag = Integer.parseInt(m.group().replace(",", ""));
			}
		}
	}

	@Override
	public void onPreEnd() {
		Platform.runLater(() -> controller.close(true));
	}
}
