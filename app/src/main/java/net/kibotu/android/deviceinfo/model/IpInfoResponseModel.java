package net.kibotu.android.deviceinfo.model;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class IpInfoResponseModel {

    String ip;
    String hostname;
    String city;
    String region;
    String country;
    String loc;
    String org;
    String postal;

    public String getIp() {
        return ip;
    }

    public String getHostname() {
        return hostname;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getLoc() {
        return loc;
    }

    public String getOrg() {
        return org;
    }

    public String getPostal() {
        return postal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpInfoResponseModel that = (IpInfoResponseModel) o;

        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (hostname != null ? !hostname.equals(that.hostname) : that.hostname != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (loc != null ? !loc.equals(that.loc) : that.loc != null) return false;
        if (org != null ? !org.equals(that.org) : that.org != null) return false;
        return postal != null ? postal.equals(that.postal) : that.postal == null;

    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (hostname != null ? hostname.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (loc != null ? loc.hashCode() : 0);
        result = 31 * result + (org != null ? org.hashCode() : 0);
        result = 31 * result + (postal != null ? postal.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IpInfoResponseModel{" +
                "ip='" + ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", loc='" + loc + '\'' +
                ", org='" + org + '\'' +
                ", postal='" + postal + '\'' +
                '}';
    }
}
