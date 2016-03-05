package net.kibotu.android.deviceinfo.library.hardware.battery;

import android.os.BatteryManager;

import java.text.DecimalFormat;

public class Battery {

    int scale = 0;
    int level = 0;
    int voltage = 0;
    /**
     * The returned value is an int representing, for example, 27.5 Degrees Celcius as "275" ,
     * so it is accurate to a tenth of a centigrade. Simply cast this to a float and divide by 10.
     */
    int temperature = 0;
    int health = 0;
    int plugged = 0;
    boolean present = true;
    String technology = "";
    int status;

    public Battery() {
    }

    public int getScale() {
        return scale;
    }

    public Battery setScale(int scale) {
        this.scale = scale;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Battery setLevel(int level) {
        this.level = level;
        return this;
    }

    public int getVoltage() {
        return voltage;
    }

    public Battery setVoltage(int voltage) {
        this.voltage = voltage;
        return this;
    }

    public int getTemperature() {
        return temperature;
    }

    public Battery setTemperature(int temperature) {
        this.temperature = temperature;
        return this;
    }

    public int getHealth() {
        return health;
    }

    public Battery setHealth(int health) {
        this.health = health;
        return this;
    }

    public int getPlugged() {
        return plugged;
    }

    public Battery setPlugged(int plugged) {
        this.plugged = plugged;
        return this;
    }

    public boolean isPresent() {
        return present;
    }

    public Battery setPresent(boolean present) {
        this.present = present;
        return this;
    }

    public String getTechnology() {
        return technology;
    }

    public Battery setTechnology(String technology) {
        this.technology = technology;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Battery setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getStatusAsString() {
        return nameStatus(status);
    }

    public String getPluggedAsString() {
        return pluggedName(plugged);
    }

    public String getHealthAsString() {
        return healthName(status);
    }

    public float getChargingLevel() {
        return level / (float) scale;
    }

    public String getChargingLevelAsString() {
        return DecimalFormat.getPercentInstance().format(getChargingLevel());
    }

    public String getTemperatureCelcius() {
        return DecimalFormat.getNumberInstance().format(temperature / 10f) + " °C";
    }

    public String getTemperatureFarenheit() {
        return DecimalFormat.getNumberInstance().format(convertCelciusToFahrenheit(temperature / 10f)) + " °F";
    }

    // Converts to celcius
    private float convertFahrenheitToCelcius(float fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    // Converts to fahrenheit
    private float convertCelciusToFahrenheit(float celsius) {
        return ((celsius * 9) / 5) + 32;
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
            default:
                ret = "Unknown " + status;
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

    public static String healthName(final int health) {

        String ret;
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                ret = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                ret = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Battery battery = (Battery) o;

        if (scale != battery.scale) return false;
        if (level != battery.level) return false;
        if (voltage != battery.voltage) return false;
        if (temperature != battery.temperature) return false;
        if (health != battery.health) return false;
        if (plugged != battery.plugged) return false;
        if (present != battery.present) return false;
        if (status != battery.status) return false;
        return technology != null ? technology.equals(battery.technology) : battery.technology == null;

    }

    @Override
    public int hashCode() {
        int result = scale;
        result = 31 * result + level;
        result = 31 * result + voltage;
        result = 31 * result + temperature;
        result = 31 * result + health;
        result = 31 * result + plugged;
        result = 31 * result + (present ? 1 : 0);
        result = 31 * result + (technology != null ? technology.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }

    @Override
    public String toString() {
        return "Battery{" +
                "scale=" + scale +
                ", level=" + level +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                ", health=" + health +
                ", plugged=" + plugged +
                ", present=" + present +
                ", technology='" + technology + '\'' +
                ", status=" + status +
                '}';
    }
}