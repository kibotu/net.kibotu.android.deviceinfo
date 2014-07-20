package net.kibotu.android.deviceinfo;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIM {

    private final Context context;

    public String simCountry;
    public String simOperatorCode;
    public String simOperatorName;
    public String simSerial;
    public String simState;

    public SIM(final Context context) {
        this.context = context;
        load();
    }

    public void load() {

        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

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
                simSerial = telephonyManager.getSimSerialNumber();
                break;
            case (TelephonyManager.SIM_STATE_UNKNOWN):
                simState = "SIM_STATE_UNKNOWN";
                break;
        }
    }
}
