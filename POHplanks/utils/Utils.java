package scripts.POHplanks.utils;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.POHplanks.antiban.Antiban;
import scripts.POHplanks.data.Constants;
import scripts.POHplanks.data.Vars;
import scripts.boe_api.npc.NPCFinder;
import scripts.boe_api.objects.ObjectFinder;
import scripts.dax_api.shared.helpers.BankHelper;
import scripts.dax_api.shared.helpers.InterfaceHelper;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class Utils {

    private static Utils utils;

    private HashMap<String, RSInterface> actionInterfaces = new HashMap();

    public static Utils get() {
        return utils == null ? utils = new Utils() : utils;
    }

    public boolean hasCoins() {
        return Inventory.getCount(Constants.COINS) > 0;
    }

    public boolean bankHasCoins() {
        return Banking.find(Constants.COINS).length > 0;
    }

    public boolean hasLawRunes() {
        return Inventory.getCount(Constants.LAW_RUNE) > 0;
    }

    public boolean hasLogs() {
        return Inventory.getCount(Vars.get().plank.getLogId()) > 0;
    }

    public boolean bankHasLogs() {
        RSItem[] logs = Banking.find(Vars.get().plank.getLogId());
        if (logs.length > 0) {
            return logs[0].getStack() > 26;
        }
        return false;
    }

    public boolean hasPlanks() {
        return Inventory.getCount(Vars.get().plank.getId()) > 0;
    }

    public static boolean hasRingOfLife() {
        return Equipment.isEquipped(Constants.RING_OF_LIFE);
    }

    public static boolean bankHasRingOfLife() {
        return Banking.find(Constants.RING_OF_LIFE).length > 0;
    }

    public RSObject getBankChest() {
        RSObject[] bankChest = Objects.find(10, (rsObject) -> rsObject.getDefinition().getName().equals("Bank chest")
                && Arrays.asList(rsObject.getDefinition().getActions()).containsAll(Arrays.asList("Use", "Collect")));
        return bankChest.length > 0 ? bankChest[0] : null;
    }

    public RSObject getInHousePortal() {
        return ObjectFinder.findFirst(20, (rsObject) -> rsObject.getDefinition().getName().equals("Portal")
                && Arrays.stream(rsObject.getDefinition().getActions()).anyMatch((action) -> action.equals("Lock")));
    }

    public boolean isInSafeArea() {
        RSTile playerPosition = Player.getPosition();
        return isInBank() || Constants.CAMELOT.contains(playerPosition) || Constants.LUMBRIDGE.contains(playerPosition);
    }

    public boolean isInBank() {
        return getBankChest() != null || BankHelper.isInBank() || Banking.isInBank();
    }

    public boolean openBankAndWait() {
        RSObject bankChest = getBankChest();
        if (bankChest != null) {
            if (!bankChest.isOnScreen() || !bankChest.isClickable()) {
                Camera.turnToTile(bankChest.getPosition());
            }
            if (AccurateMouse.click(bankChest, "Use")) {
                return Timing.waitCondition(() -> Banking.isBankScreenOpen(), General.random(4000, 6000));
            }
        }
        return Banking.openBank();
    }

    public boolean hideAttackOption() {
        General.println("Hiding attack option");
        if (Antiban.switchTab(GameTab.TABS.OPTIONS)) {
            if (Options.getSelectedTab().equals(Options.TABS.CONTROLS)) {
                RSInterface attackOptions = Interfaces.get(261, 89, 4);
                if (attackOptions != null) {
                    if (Interfaces.isInterfaceSubstantiated(261, 106) && !attackOptions.getText().equals("Hidden")) {
                        RSInterface hiddenOption = Interfaces.get(261, 106, 4);
                        if (hiddenOption != null) {
                            return AccurateMouse.click(hiddenOption) && Timing.waitCondition(() -> attackOptions.getText().equals("Hidden"), General.random(1500, 2500));
                        }
                    } else {
                        if (AccurateMouse.click(attackOptions)) {
                            Timing.waitCondition(() -> Interfaces.isInterfaceSubstantiated(261, 106), General.random(1500, 2500));
                        }
                    }
                }
            } else {
                Options.openTab(Options.TABS.CONTROLS);
            }
        }
        return false;
    }

    public boolean doWeHaveEnoughCoins() {
        RSItem[] coins = Inventory.find(Constants.COINS);
        if (coins.length > 0) {
            return coins[0].getStack() > Inventory.getCount(Vars.get().plank.getLogId()) * Vars.get().plank.getCost();
        }
        return false;
    }

    public boolean doWeHaveRunes() {
        RSItem staff = Equipment.getItem(Equipment.SLOTS.WEAPON);
        return Inventory.getCount(Constants.LAW_RUNE) > 0 &&
                (Inventory.getCount(Constants.AIR_RUNE) >= 5 || (Inventory.getCount(Constants.AIR_RUNE) >= 3 && Inventory.getCount(Constants.EARTH_RUNE) > 0)) ||
                (staff != null && staff.getDefinition().getName().contains("staff"));
    }

    public boolean canMakePlanks() {
        return doWeHaveEnoughCoins() && hasLogs() && hasLawRunes();
    }

    public boolean shouldRefillMoneybag() {
        return Vars.get().useServantsMoneybag && Vars.get().coinsInMoneybag > 0 && Vars.get().coinsInMoneybag <= Vars.get().refillCoinsAt && hasCoins();
    }

    public void enableSafetyFeatures() {
        setPrivateOff();
        disableAcceptAid();
        hideAttackOption();
        Options.setRemoveRoofsEnabled(false);
    }

    public boolean disableAcceptAid() {
        General.println("Disabling accept aid");
        Antiban.switchTab(GameTab.TABS.OPTIONS);
        RSInterface options = Interfaces.get(261);
        if (options != null) {
            RSInterface acceptAid = getInterfaceWithAction(options, "Toggle Accept Aid");
            if (acceptAid != null) {
                if (acceptAid.getTextureID() == 762) {
                    return AccurateMouse.click(acceptAid, "Toggle Accept Aid");
                }
            }
        }
        return false;
    }

    public boolean teleportToSafety() {
        if (!doWeHaveRunes()) return false;
        if (isInBank()) return false;
        if (Antiban.switchTab(GameTab.TABS.MAGIC) && Magic.selectSpell(Vars.get().teleport)) {
            return Timing.waitCondition(() -> isInBank(), General.random(5500, 6500));
        }
        return false;
    }

    public boolean setPrivateOff() {
        RSInterface privateStatus = Interfaces.get(162,22);
        if (privateStatus != null && privateStatus.getText().contains("Off")) {
            return true;
        }
        RSInterface chatOptions = Interfaces.get(162);
        if (chatOptions != null) {
            RSInterface privacy = getInterfaceWithAction(chatOptions,"<col=ffff00>Private:</col> Off");
            if (privacy != null) {
                return AccurateMouse.click(privacy, "Private: Off");
            }
        }
        return false;
    }

    public boolean isButlerStuck() {
        RSNPC Butler = NPCFinder.findFirst("Demon butler");
        if (Butler != null) {
            return (Butler.isInteractingWithMe() && !NPCInteraction.isConversationWindowUp());
        }
        return false;
    }

    public boolean isEnterAmountInterfaceOpen() {
        return Interfaces.isInterfaceSubstantiated(162,44);
    }

    public boolean isBondInterfaceOpen() {
        return Interfaces.isInterfaceSubstantiated(65,2,13);
    }

    public boolean isHouseOptionsOpen() {
        return Interfaces.isInterfaceSubstantiated(Constants.HOUSE_OPTIONS_MASTER);
    }

    public boolean isOptionsOpen() {
        return Interfaces.isInterfaceSubstantiated(261);
    }

    public RSInterface getHouseOptions() {
        RSInterface options = Interfaces.get(261);
        return options != null ? getInterfaceWithAction(options, "View House Options") : options;
    }

    public RSInterface getCallServant() {
        RSInterface houseOptions = Interfaces.get(Constants.HOUSE_OPTIONS_MASTER);
        return houseOptions != null ? getInterfaceWithAction(houseOptions, "Call Servant") : houseOptions;
    }

    public RSInterface getInterfaceWithAction(RSInterface master, String action) {
        if (actionInterfaces.containsKey(action)) {
            System.out.println("Returning interface from cached value");
            return actionInterfaces.get(action);
        }
        Optional<RSInterface> optional = InterfaceHelper.getAllInterfaces(master)
                .stream()
                .filter(rsInterface -> rsInterface != null && InterfaceHelper.getActions(rsInterface).contains(action))
                .findAny();
        if (optional.isPresent()) {
            actionInterfaces.put(action, optional.get());
            return optional.get();
        }
        return null;
    }
}
