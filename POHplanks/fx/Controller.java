package scripts.POHplanks.fx;

import com.allatori.annotations.DoNotRename;
import com.jfoenix.controls.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.tribot.api.General;
import scripts.POHplanks.data.LogInfo;
import scripts.POHplanks.data.Vars;
import scripts.boe_api.images.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@DoNotRename
public class Controller implements Initializable
{
    private Stage hostStage;
    private boolean isDone = false, wasCancelled = true;

    @FXML @DoNotRename
    ImageView guiTitle;

    @FXML @DoNotRename
    JFXComboBox<Label> logType;

    @FXML @DoNotRename
    JFXComboBox<Label> teleportLocation;

    @FXML @DoNotRename
    JFXComboBox<Label> world;

    @FXML @DoNotRename
    JFXCheckBox useServantsMoneybag;

    @FXML @DoNotRename
    JFXTextField offerAmount;

    @FXML @DoNotRename
    JFXTextField offerAt;

    @FXML @DoNotRename
    JFXCheckBox useRingOfLife;

    @FXML @DoNotRename
    JFXButton startScript;

    @FXML @DoNotRename
    JFXSlider abcReactionTime;

    @FXML @DoNotRename
    void handleServantMoneybag(ActionEvent event) {
        if (useServantsMoneybag.isSelected()) {
            offerAt.setDisable(false);
            offerAmount.setDisable(false);
        } else {
            offerAt.setDisable(true);
            offerAmount.setDisable(true);
        }
    }

    @FXML @DoNotRename
    void handleButtonAction(ActionEvent event) {
        close(false);
        switch (teleportLocation.getValue().getText()) {
            case "Camelot":
                Vars.get().teleport = "Camelot teleport";
                break;
            case "Lumbridge":
            default:
                Vars.get().teleport = "Lumbridge teleport";
                break;
        }

        switch (logType.getValue().getText()) {
            case "Regular":
            default:
                Vars.get().plank = LogInfo.NORMAL;
                break;
            case "Oak":
                Vars.get().plank = LogInfo.OAK;
                break;
            case "Teak":
                Vars.get().plank = LogInfo.TEAK;
                break;
            case "Mahogany":
                Vars.get().plank = LogInfo.MAHOGANY;
        }

        switch (world.getValue().getText()) {
            case "Random":
                int[] worlds = {325, 337, 392};
                Vars.get().pvpWorld = worlds[General.random(0, worlds.length - 1)];
                break;
            case "325":
            case "337":
            case "392":
                Vars.get().pvpWorld = Integer.parseInt(world.getValue().getText());
                break;
        }
        Vars.get().useRingOfLife = useRingOfLife.isSelected();
        if (useServantsMoneybag.isSelected()) {
            Vars.get().useServantsMoneybag = true;
            Vars.get().refillCoinsAt = Integer.parseInt(offerAt.getText());
            Vars.get().coinDepositAmount = Integer.parseInt(offerAmount.getText());
        }
        Vars.get().abcReactionPercentage = abcReactionTime.getValue() / 100;
    }

    public boolean isDone()
    {
        return isDone;
    }
    public boolean wasCancelled()
    {
        return wasCancelled;
    }

    public Stage getHostStage()
    {
        return hostStage;
    }

    public void close(boolean isCancelled) {
        isDone = true;
        wasCancelled = isCancelled;
        if (hostStage != null)
            hostStage.close();
    }

    public void setHostStage(Stage hostStage)
    {
        this.hostStage = hostStage;
    }


    public void setDone(boolean done)
    {
        isDone = done;
    }

    public void setWasCancelled(boolean wasCancelled)
    {
        this.wasCancelled = wasCancelled;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        offerAmount.setDisable(true);
        offerAt.setDisable(true);
        guiTitle.setImage(SwingFXUtils.toFXImage(ImageHelper.getImage("https://i.imgur.com/aNuDn9N.png"), null));
        logType.getItems().addAll(new Label("Regular"), new Label("Oak"), new Label("Teak"), new Label("Mahogany"));
        logType.getSelectionModel().selectFirst();
        teleportLocation.getItems().addAll(new Label("Lumbridge"), new Label("Camelot"));
        teleportLocation.getSelectionModel().selectFirst();
        world.getItems().addAll(new Label("Random"), new Label("325"), new Label("337"), new Label("392"));
        world.getSelectionModel().selectFirst();
    }
}