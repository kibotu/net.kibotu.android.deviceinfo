package net.kibotu.android.deviceinfo.library.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import static net.kibotu.android.deviceinfo.library.Device.getContext;

/**
 * @see <a href="http://developer.android.com/reference/android/os/BatteryManager.html">BatteryManager</a>
 */
public class BatteryReceiver extends BroadcastReceiver {

    public final BatteryObservable batteryObservable;

    public BatteryReceiver() {
        batteryObservable = new BatteryObservable();
    }

    public BatteryReceiver registerReceiver() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getContext().registerReceiver(this, filter);
        return this;
    }

    public BatteryReceiver unregisterReceiver() {
        getContext().unregisterReceiver(this);
        return this;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final Battery battery = new Battery()
                .setHealth(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0))
                .setPlugged(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0))
                .setLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0))
                .setScale(intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0))
                .setTemperature(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0))
                .setVoltage(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0))
                .setPresent(intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true))
                .setTechnology(intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY))
                .setStatus(intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0));

        batteryObservable.notifyObservers(battery);
    }
}
