package net.kibotu.android.deviceinfo.library.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import static net.kibotu.ContextHelper.getApplication;
import static net.kibotu.android.deviceinfo.library.Device.getContext;

public class SIM {

    public String simCountry;
    public String simOperatorCode;
    public String simOperatorName;
    public String simSerial;
    public String simState;

    /**
     * Requires {@link android.Manifest.permission#READ_PHONE_STATE}
     */
    public SIM() {
        load();
    }

    public void load() {

        final TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getSimState()) {

            case (TelephonyManager.SIM_STATE_ABSENT):
                simState = "SIM_STATE_ABSENT";
                break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
                simState = "SIM_STATE_NETWORK_LOCKED";
                break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED):
                simState = "SIM_STATE_PIN_REQUIRED";
                break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED):
                simState = "SIM_STATE_PUK_REQUIRED";
                break;
            case (TelephonyManager.SIM_STATE_READY):
                simState = "SIM_STATE_READY";
                // Get the SIM country ISO code
                simCountry = telephonyManager.getSimCountryIso();
                // Get the operator code of the active SIM (MCC + MNC)
                simOperatorCode = telephonyManager.getSimOperator();
                // Get the name of the SIM operator
                simOperatorName = telephonyManager.getSimOperatorName();
                // Get the SIM’s serial number
                if (hasReadPhonePermission())
                    simSerial = telephonyManager.getSimSerialNumber();
                break;
            case (TelephonyManager.SIM_STATE_UNKNOWN):
                simState = "SIM_STATE_UNKNOWN";
                break;
        }
    }

    public static boolean hasReadPhonePermission() {
        return ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }
}
