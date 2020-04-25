package scripts.AirTalisman;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Starting;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.AirTalisman.data.Constants;
import scripts.AirTalisman.tasks.Bank;
import scripts.AirTalisman.tasks.Collect;
import scripts.AirTalisman.tasks.Walking;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

import java.util.ArrayList;
import java.util.Collections;

public class AirTalisman extends Script implements Starting, Ending {

    public static ArrayList<Node> nodes = new ArrayList<Node>();
    private ACamera aCamera = new ACamera(this);
    private boolean shouldRun = true;

    @Override
    public void run() {
        configureDaxWalker();
        Collections.addAll(nodes, new Walking(aCamera), new Bank(aCamera), new Collect(aCamera));
        while (shouldRun) {
            if (Login.getLoginState() != Login.STATE.LOGINSCREEN) {
                for (final Node node: nodes) {
                    if (node.validate()) {
                        node.execute();
                    }
                }
            }
            General.sleep(50, 70);
        }
    }

    public void configureDaxWalker() {
        DaxWalker.setCredentials(new DaxCredentialsProvider(){
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });
    }

    @Override
    public void onStart() {
        if (!isInProgress()) {
            shouldRun = false;
            System.out.println("This Script can only be ran with the Rune Mysteries Quest: In Progress");
        }
    }



    public boolean isInProgress() {
        int state = Game.getSetting(Constants.RUNE_MYSTERIES);
        return Constants.RUNE_MYSTERIES_COMPLETE > state && state >= Constants.QUEST_STARTED;
    }

    @Override
    public void onEnd() {
    }
}
