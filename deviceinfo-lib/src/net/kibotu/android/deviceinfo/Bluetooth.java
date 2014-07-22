package net.kibotu.android.deviceinfo;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class Bluetooth extends BroadcastReceiver {

    public volatile String UUID;
    public volatile String BOND_STATE;
    public volatile String CLASS;
    public volatile String DEVICE;
    public volatile String NAME;
    public volatile String PAIRING_KEY;
    public volatile String PAIRING_VARIANT;
    public volatile String PREVIOUS_BOND_STATE;
    public volatile int RSSI;
    public volatile String VARIANT_PASSKEY_CONFIRMATION;
    public volatile String VARIANT_PIN;

    public Bluetooth(final Context context) {
        registerReceiver(context);

        UUID = "-1";
        BOND_STATE = "-1";
        CLASS = "-1";
        DEVICE = "-1";
        NAME = "-1";
        PAIRING_KEY = "-1";
        PAIRING_VARIANT = "-1";
        PREVIOUS_BOND_STATE = "-1";
        RSSI = -1;
        VARIANT_PASSKEY_CONFIRMATION = "-1";
        VARIANT_PIN = "-1";
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            RSSI = intent.getShortExtra(BluetoothDevice.ACTION_BOND_STATE_CHANGED, Short.MIN_VALUE);
        }
//            rssi = intent.getShortExtra(BluetoothDevice.EXTRA_NAME, Short.MIN_VALUE);
//            rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
//            rssi = intent.getShortExtra(BluetoothDevice.EXTRA_CLASS, Short.MIN_VALUE);
//            rssi = intent.getShortExtra(BluetoothDevice.EXTRA_BOND_STATE, Short.MIN_VALUE);
//            rssi = intent.getShortExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, Short.MIN_VALUE);


//            public static final java.lang.String EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE";
//            public static final java.lang.String EXTRA_NAME = "android.bluetooth.device.extra.NAME";
//            public static final java.lang.String EXTRA_RSSI = "android.bluetooth.device.extra.RSSI";
//            public static final java.lang.String EXTRA_CLASS = "android.bluetooth.device.extra.CLASS";
//            public static final java.lang.String EXTRA_BOND_STATE = "android.bluetooth.device.extra.BOND_STATE";
//            public static final java.lang.String EXTRA_PREVIOUS_BOND_STATE = "android.bluetooth.device.extra.PREVIOUS_BOND_STATE";

    }

    public void registerReceiver(final Context context) {
        final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(this, filter);
    }
}
