package scripts.AirTalisman.utilities;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;
import scripts.dax_api.walker.utils.AccurateMouse;

public class BankHelper {
    private static final int BANKING_INTERFACE = 12;
    private static final int SELECTED_TEXTURE = 813;

    public enum Widgets {
        SWAP(14, "Swap"), INSERT(16, "Insert"), ITEM(19, "Item"), NOTE(21, "Note");
        String name;
        int index;

        Widgets(final int index, final String name) {
            this.index = index;
            this.name = name;
        }
    }

    /**
     * @param widget The widget to check (swap/insert/item/note)     * @return true if the widget is selected (in red)
     */
    public static boolean isSelected(final Widgets widget) {
        if (!Banking.isBankScreenOpen() || Interfaces.isInterfaceSubstantiated(BANKING_INTERFACE)) return false;
        final RSInterfaceChild itemWidget = Interfaces.get(BANKING_INTERFACE, widget.index);
        if (itemWidget != null) {
            return itemWidget.getTextureID() == SELECTED_TEXTURE;
        }
        return false;
    }

    /**
     * @param widget The widget to enable     * @return true if the widget was successfully selected
     */
    public static boolean select(final Widgets widget) {
        if (!Banking.isBankScreenOpen() || Interfaces.isInterfaceSubstantiated(BANKING_INTERFACE)) return false;
        if (isSelected(widget)) return false;
        final RSInterfaceChild itemWidget = Interfaces.get(BANKING_INTERFACE, widget.index);
        if (itemWidget != null) {
            if (AccurateMouse.click(itemWidget, widget.name)) {
                return Timing.waitCondition(() -> isSelected(widget), General.random(4000,6000));
            }
        }
        return false;
    }
}