package net.kibotu.android.deviceinfo.library.legacy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import static net.kibotu.android.deviceinfo.library.Device.getContext;

/**
 * @see <a href="http://developer.android.com/reference/android/os/BatteryManager.html">BatteryManager</a>
 */
public class Battery extends BroadcastReceiver {

    int scale = 0;
    int level = 0;
    int voltage = 0;
    int temperature = 0;
    String health = "Good";
    String plugged = "Unknown";
    boolean present = true;
    String technology = "";
    int status;

    public Battery() {
    }

    public Battery registerReceiver() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getContext().registerReceiver(this, filter);
        return this;
    }

    public Battery unregesterReceiver() {
        getContext().unregisterReceiver(this);
        return this;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        health = healthName(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0));
        plugged = pluggedName(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0));
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
        temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true);
        technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
    }

    public float getChargingLevel() {
        return level / (float) scale;
    }

    public static String healthName(final int health) {

        String ret;

        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                ret = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                ret = "Good";
                break;
            case 7: // api 11 - BatteryManager.BATTERY_HEALTH_COLD
                ret = "Cold";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                ret = "Overheat";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                ret = "Over Voltage";
                break;
            default:
                ret = "" + health;
        }

        return ret;
    }

    public static String pluggedName(final int plugged) {

        String ret;

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                ret = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                ret = "USB";
                break;
            case 4: // api 17 - BatteryManager.BATTERY_PLUGGED_WIRELESS:
                ret = "Wireless";
            default:
                ret = "" + plugged;
        }

        return ret;
    }

    public static String nameStatus(final int status) {

        String ret;

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                ret = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                ret = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                ret = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                ret = "Not Charging";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                ret = "Unknown";
                break;
            default:
                ret = "" + status;
        }

        return ret;
    }

    public String getTemperatureCelcius() {
        return convertFahrenheitToCelcius(temperature / 10f) + " °C";
    }

    public String getTemperatureFarenheit() {
        return convertCelciusToFahrenheit(temperature / 10f) + " °F";
    }

    // Converts to celcius
    private float convertFahrenheitToCelcius(float fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    // Converts to fahrenheit
    private float convertCelciusToFahrenheit(float celsius) {
        return ((celsius * 9) / 5) + 32;
    }

    public String getStatus() {
        return nameStatus(status);
    }

    public String getTechnology() {
        return technology;
    }

    public int getVoltage() {
        return voltage;
    }

    public String getHealth() {
        return health;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getPlugged() {
        return plugged.equalsIgnoreCase("0")
                ? "Battery"
                : plugged;
    }

    public boolean getPresent() {
        return present;
    }
}
