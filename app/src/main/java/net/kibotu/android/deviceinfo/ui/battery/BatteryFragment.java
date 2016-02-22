package net.kibotu.android.deviceinfo.ui.battery;

import android.os.BatteryManager;
import com.orhanobut.hawk.Hawk;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.legacy.Battery;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.getBattery;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.formatBool;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class BatteryFragment extends ListFragment {

    private Battery battery;

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_battery);
    }

    @Override
    public void onDestroyView() {
        battery.unregesterReceiver();
        super.onDestroyView();
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        battery = getBattery();
        battery.registerReceiver();

        addListItemVertically("Technology", battery.getTechnology(), "Technology of the current battery.");
        addListItemVertically("Status", battery.getStatus(), "Current status constant.");
        addListItemVertically("Charging Level", (int) (battery.getChargingLevel() * 100) + " %", "Current battery level, from 0 to the maximum battery level.");
        addListItemVertically("Voltage", battery.getVoltage() + " mV", "Current battery voltage level.");
        addListItemVertically("Temperature", battery.getTemperatureCelcius() + " °C [" + battery.getTemperatureFarenheit() + "]", "Current battery temperature.");
        addListItemVertically("Voltage", battery.getVoltage() + " mV", "Current battery voltage level.");
        addListItemVertically("Health", battery.getHealth(), "Current health constant.");
        addListItemVertically("Power Source", battery.getPlugged(), "Indicating whether the device is plugged in to a power source; 0 means it is on battery, other constants are different types of power sources.");
        addListItemVertically("Last Charging Source", getLastChargingSource(), "Last recorded charging power source.");
        addListItemVertically("Battery Present", formatBool(battery.getPresent()), "Indicating whether a battery is present or not.");
    }

    public String getLastChargingSource() {
        final String value = Hawk.get(BatteryManager.EXTRA_PLUGGED, "Unknown");
        Hawk.put(BatteryManager.EXTRA_PLUGGED, battery.getPlugged());
        return value;
    }
}
