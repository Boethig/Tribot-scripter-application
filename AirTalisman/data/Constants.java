package scripts.AirTalisman.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants {
    public static final int MAX_DROP_LIMIT = 18;
    public static final int AIR_TALISMAN = 1438;
    public static final int AIR_TALISMAN_NOTED = 1439;
    public static final int RUNE_MYSTERIES = 63;
    public static final int QUEST_STARTED = 1;
    public static final int RUNE_MYSTERIES_COMPLETE = 3;
    public static final String[] OPTIONS = { "Have you any quests for me?"};
    public static final RSArea QUEST_START = new RSArea(new RSTile [] {
            new RSTile(3206, 3225, 1),new RSTile(3213, 3225, 1),
            new RSTile(3213, 3218, 1),new RSTile(3206, 3218, 1)
    });
}
