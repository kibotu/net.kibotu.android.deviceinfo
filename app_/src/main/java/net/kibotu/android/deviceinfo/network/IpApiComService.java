package net.kibotu.android.deviceinfo.network;

import com.orhanobut.wasp.Callback;
import com.orhanobut.wasp.WaspRequest;
import com.orhanobut.wasp.http.GET;
import net.kibotu.android.deviceinfo.model.IpApiComResponseModel;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public interface IpApiComService {

    @GET("/json")
    WaspRequest getGeoIp(Callback<IpApiComResponseModel> response);
}
