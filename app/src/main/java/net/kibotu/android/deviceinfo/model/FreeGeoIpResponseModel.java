package net.kibotu.android.deviceinfo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class FreeGeoIpResponseModel {

    String ip;
    @SerializedName("country_code")
    String countryCode;
    @SerializedName("country_name")
    String countryName;
    @SerializedName("region_code")
    String regionCode;
    @SerializedName("region_name")
    String regionName;
    String city;
    @SerializedName("zip_code")
    String zipCode;
    @SerializedName("time_zone")
    String timeZone;
    double latitude;
    double longitude;
    @SerializedName("metro_code")
    int metroCode;

    public String getIp() {
        return ip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getMetroCode() {
        return metroCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FreeGeoIpResponseModel that = (FreeGeoIpResponseModel) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (metroCode != that.metroCode) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) return false;
        if (countryName != null ? !countryName.equals(that.countryName) : that.countryName != null) return false;
        if (regionCode != null ? !regionCode.equals(that.regionCode) : that.regionCode != null) return false;
        if (regionName != null ? !regionName.equals(that.regionName) : that.regionName != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (zipCode != null ? !zipCode.equals(that.zipCode) : that.zipCode != null) return false;
        return timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (countryName != null ? countryName.hashCode() : 0);
        result = 31 * result + (regionCode != null ? regionCode.hashCode() : 0);
        result = 31 * result + (regionName != null ? regionName.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + metroCode;
        return result;
    }

    @Override
    public String toString() {
        return "FreeGeoIpResponseModel{" +
                "ip='" + ip + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", regionName='" + regionName + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", metroCode=" + metroCode +
                '}';
    }
}
