package net.kibotu.android.deviceinfo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class IpApiComResponseModel {

    String as;
    String city;
    String country;
    String countryCode;
    String isp;
    @SerializedName("lat")
    double latitude;
    @SerializedName("lon")
    String longitude;
    String org;
    String query;
    String region;
    String regionName;
    String status;
    String timezone;
    String zip;

    public String getAs() {
        return as;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getIsp() {
        return isp;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOrg() {
        return org;
    }

    public String getQuery() {
        return query;
    }

    public String getRegion() {
        return region;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getStatus() {
        return status;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpApiComResponseModel that = (IpApiComResponseModel) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (as != null ? !as.equals(that.as) : that.as != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) return false;
        if (isp != null ? !isp.equals(that.isp) : that.isp != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;
        if (org != null ? !org.equals(that.org) : that.org != null) return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (regionName != null ? !regionName.equals(that.regionName) : that.regionName != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (timezone != null ? !timezone.equals(that.timezone) : that.timezone != null) return false;
        return zip != null ? zip.equals(that.zip) : that.zip == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = as != null ? as.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (isp != null ? isp.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (org != null ? org.hashCode() : 0);
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (regionName != null ? regionName.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (timezone != null ? timezone.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IpApiComResponseModel{" +
                "as='" + as + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", isp='" + isp + '\'' +
                ", latitude=" + latitude +
                ", longitude='" + longitude + '\'' +
                ", org='" + org + '\'' +
                ", query='" + query + '\'' +
                ", region='" + region + '\'' +
                ", regionName='" + regionName + '\'' +
                ", status='" + status + '\'' +
                ", timezone='" + timezone + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
