package scripts.JarGenerator.utils;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import scripts.JarGenerator.PuroJars;
import scripts.JarGenerator.antiban.Antiban;
import scripts.JarGenerator.data.Constants;
import scripts.boe_api.inventory.OSInventory;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Utils {

    public static boolean hasImplings() {
        return Inventory.getCount(Constants.NAT_IMPLING) >= 1 && Inventory.getCount(Constants.ECL_IMPLING) >= 2 && Inventory.getCount(Constants.ESS_IMPLING) >= 3;
    }

    public static boolean hasGenerator() {
        return Inventory.getCount(Constants.GENERATOR) > 0;
    }

    public static boolean bankHasImplings() {
        return Banking.find(Constants.NAT_IMPLING).length > 0 && Banking.find(Constants.ESS_IMPLING).length > 0 && Banking.find(Constants.ECL_IMPLING).length > 0;
    }

    public static void drinkEnergy() {
        RSItem[] energy = OSInventory.findNearestToMouse(Filters.Items.nameContains("Energy"));
        if (energy.length > 0) {
            if (AccurateMouse.click(energy[0],"Drink")) {
                Antiban.waitItemInteractionDelay();
                Timing.waitCondition(() -> {
                    General.sleep(100, 200);
                    return (Game.getRunEnergy() >= Antiban.getRunAt());
                }, General.random(1500, 2500));
            }
        }
        RSItem[] vial = Inventory.find(229);
        if (vial.length > 0){
            if (AccurateMouse.click(vial[0],"Drop")) {
                Timing.waitCondition(() -> {
                    General.sleep(100, 200);
                    return Inventory.getCount(229) == 0;
                }, General.random(2000, 4000));
            }
        }
    }
}
