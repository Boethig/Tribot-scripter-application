package scripts.AirTalisman.tasks;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.data.Node;
import scripts.AirTalisman.data.Constants;
import scripts.AirTalisman.utilities.BankHelper;

public class Bank extends Node {

    public Bank(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public boolean validate() {
        return ((Banking.isInBank() || scripts.dax_api.shared.helpers.BankHelper.isInBank()) && Inventory.getCount(Constants.AIR_TALISMAN) >= Constants.MAX_DROP_LIMIT)
                || Banking.isBankScreenOpen();
    }

    @Override
    public void execute() {
        if (Banking.isBankScreenOpen() && Banking.isBankLoaded()) {
            if (Banking.deposit(0, Constants.AIR_TALISMAN)) {
                if (Banking.find(Constants.AIR_TALISMAN).length > 0) {
                    if (BankHelper.isSelected(BankHelper.Widgets.NOTE)) {
                        Banking.withdraw(0, Constants.AIR_TALISMAN);
                        if (Banking.find(Constants.AIR_TALISMAN).length == 0) {
                            Banking.close();
                        }
                    } else {
                        BankHelper.select(BankHelper.Widgets.NOTE);
                    }
                }
            }
        } else {
            scripts.dax_api.shared.helpers.BankHelper.openBankAndWait();
        }
    }

    @Override
    public String status() {
        return "Depositing & Withdrawing Air Talismans";
    }
}

