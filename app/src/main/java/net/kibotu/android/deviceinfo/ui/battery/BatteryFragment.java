package net.kibotu.android.deviceinfo.ui.battery;

import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.orhanobut.hawk.Hawk;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.battery.Battery;
import net.kibotu.android.deviceinfo.library.battery.BatteryReceiver;
import net.kibotu.android.deviceinfo.library.battery.BatteryUpdateListener;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.getBatteryReceiver;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatBool;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class BatteryFragment extends ListFragment {

    private BatteryReceiver batteryReceiver;

    @Override
    public String getTitle() {
        return getString(R.string.menu_item_battery);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.battery;
    }

    @Override
    public void onDestroyView() {
        batteryReceiver.unregisterReceiver();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        batteryReceiver = getBatteryReceiver().addObserver(new BatteryUpdateListener() {

            @Override
            protected void update(Battery battery) {
                updateWithBattery(battery);
            }
        }).registerReceiver();
    }

    private void updateWithBattery(Battery battery) {
        clear();

        addHorizontallyCard("Technology", battery.getTechnology(), "Technology of the current batteryReceiver.");
        addHorizontallyCard("Status", battery.getStatusAsString(), "Current status constant.");
        addHorizontallyCard("Charging Level", battery.getChargingLevelAsString(), "Current batteryReceiver level, from 0 to the maximum batteryReceiver level.");
        addHorizontallyCard("Voltage", battery.getVoltage() + " mV", "Current batteryReceiver voltage level.");
        addHorizontallyCard("Temperature", battery.getTemperatureCelcius() + " [" + battery.getTemperatureFarenheit() + "]", "Current batteryReceiver temperature.");
        addHorizontallyCard("Battery Present", formatBool(battery.isPresent()), "Indicating whether a batteryReceiver is present or not.");
        addHorizontallyCard("Health", battery.getHealthAsString(), "Current health constant.");
        addHorizontallyCard("Power Source", battery.getPluggedAsString(), "Indicating whether the device is plugged in to a power source; 0 means it is on batteryReceiver, other constants are different types of power sources.");
        addHorizontallyCard("Last Charging Source", getLastChargingSource(battery), "Last recorded charging power source.");

        notifyDataSetChanged();
    }

    public String getLastChargingSource(Battery battery) {
        final String value = Hawk.get(BatteryManager.EXTRA_PLUGGED, "Unknown");
        Hawk.put(BatteryManager.EXTRA_PLUGGED, battery.getPluggedAsString());
        return value;
    }
}
