package net.kibotu.android.deviceinfo.ui.network;

import com.canelmas.let.AskPermission;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.legacy.Bluetooth;
import net.kibotu.android.deviceinfo.library.legacy.ProxySettings;
import net.kibotu.android.deviceinfo.library.legacy.SIM;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static android.Manifest.permission.READ_PHONE_STATE;
import static net.kibotu.android.deviceinfo.library.Device.*;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.BR;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class NetworkFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_network);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        addSimInfos();

        addListItemHorizontally("IMSI No", getSubscriberIdFromTelephonyManager(), "");
        addListItemHorizontally("hwID", Device.getSerialNumber(), "");

        addImeiNumber();

        addListItemVertically("MAC Address: wlan0", getMACAddress("wlan0"), "");
        addListItemVertically("MAC Address: eth0", getMACAddress("eth0"), "");
        addListItemHorizontally("IP4 Address", getIPAddress(true), "");
        addListItemHorizontally("IP6 Address", getIPAddress(false), "");
        addListItemVertically("UserAgent", Device.getUserAgent(), "");

        addProxySettings();

        // addBluetooth(); // TODO: 22.02.2016 http://stackoverflow.com/questions/30222409/android-broadcast-receiver-bluetooth-events-catching

/*
//
            addListItemHorizontally("EXTRA_BOND_STATE", "Used as an int extra field in ACTION_BOND_STATE_CHANGED intents. Contains the bond state of the remote device.\n" +
                    "Possible values are: BOND_NONE, BOND_BONDING, BOND_BONDED.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.BOND_STATE", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "EXTRA_CLASS:  " + bluetooth.BOND_STATE + " dBm";
                }
            });

            addListItemHorizontally("EXTRA_CLASS", "Used as a Parcelable BluetoothClass extra field in ACTION_FOUND and ACTION_CLASS_CHANGED intents.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.CLASS", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.CLASS + " dBm";
                }
            });

            addListItemHorizontally("EXTRA_DEVICE", "Used as a Parcelable BluetoothDevice extra field in every intent broadcast by this class. It contains the BluetoothDevice that the intent applies to.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.DEVICE", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.DEVICE + " dBm";
                }
            });

            addListItemHorizontally("EXTRA_NAME", "Used as a String extra field in ACTION_NAME_CHANGED and ACTION_FOUND intents. It contains the friendly Bluetooth name.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.NAME", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.NAME + " dBm";
                }
            });

            addListItemHorizontally("EXTRA_PAIRING_KEY", "Used as an int extra field in ACTION_PAIRING_REQUEST intents as the value of passkey.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PAIRING_KEY\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PAIRING_KEY + " dBm";
                }
            });

            addListItemHorizontally("EXTRA_PAIRING_VARIANT", "Used as an int extra field in ACTION_PAIRING_REQUEST intents to indicate pairing method used. Possible values are: PAIRING_VARIANT_PIN, PAIRING_VARIANT_PASSKEY_CONFIRMATION,\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PAIRING_VARIANT\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PAIRING_VARIANT + " dBm";
                }
            });

            addListItemHorizontally("EXTRA_PREVIOUS_BOND_STATE", "Used as an int extra field in ACTION_BOND_STATE_CHANGED intents. Contains the previous bond state of the remote device.\n" +
                    "Possible values are: BOND_NONE, BOND_BONDING, BOND_BONDED.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PREVIOUS_BOND_STATE\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PREVIOUS_BOND_STATE + " dBm";
                }
            });

            addListItemHorizontally("EXTRA_RSSI", "Used as an optional short extra field in ACTION_FOUND intents. Contains the RSSI value of the remote device as reported by the Bluetooth hardware.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.RSSI\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.RSSI + " dBm";
                }
            });

            addListItemHorizontally("EXTRA_UUID", "Used as an extra field in ACTION_UUID intents, Contains the ParcelUuids of the remote device which is a parcelable version of UUID.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.UUID\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.UUID + " dBm";
                }
            });

            addListItemHorizontally("PAIRING_VARIANT_PASSKEY_CONFIRMATION", "The user will be prompted to confirm the passkey displayed on the screen or an app will confirm the passkey for the user.\n" +
                    "Constant Value: 2 (0x00000002)", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.VARIANT_PASSKEY_CONFIRMATION + " dBm";
                }
            });

            addListItemHorizontally("PAIRING_VARIANT_PIN", "The user will be prompted to enter a pin or an app will enter a pin for user.\n" +
                    "Constant Value: 0 (0x00000000)", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.VARIANT_PIN + " dBm";
                }
            });
            addListItemHorizontally("NFC", "description", "").setHorizontal();
            addListItemHorizontally("CONSUMER_IR_SERVICE", "description", "").setHorizontal();
            addListItemHorizontally("NETWORK_INFO_WIFI", "description", "").setHorizontal();
            addListItemHorizontally("WIFI_BSSID", "description", "").setHorizontal();
            addListItemHorizontally("WIFI_HIDDEN_SSID", "description", "").setHorizontal();
            addListItemHorizontally("WIFI_IP_ADDRESS", "description", "").setHorizontal();
            addListItemHorizontally("WIFI_LINK_SPEED", "description", "").setHorizontal();
            addListItemHorizontally("WIFI_MAC_ADDRESS", "description", "").setHorizontal();
            addListItemHorizontally("WIFI_RSSI", "description", "").setHorizontal();
            addListItemHorizontally("WIFI_SSID", "description", "").setHorizontal();

            addListItemHorizontally("CALL_STATE", "description", "").setHorizontal();
            addListItemHorizontally("CELL_LOCATION", "description", "").setHorizontal();
            addListItemHorizontally("CELL_ACTIVITY", "description", "").setHorizontal();
            addListItemHorizontally("DATA_ACTIVITY", "description", "").setHorizontal();
            addListItemHorizontally("DATA_STATE", "description", "").setHorizontal();
            addListItemHorizontally("DEVICE_ID", "description", "").setHorizontal();
            addListItemHorizontally("DEVICE_SOFTWARE_VERSION", "description", "").setHorizontal();
            addListItemHorizontally("DEVICE_SOFTWARE_VERSION", "description", "").setHorizontal();
            addListItemHorizontally("LINE1_NUMBER", "description", "").setHorizontal();
            addListItemHorizontally("MMS_UA_PROF_URL", "description", "").setHorizontal(); // 19
            addListItemHorizontally("MMS_USER_AGENT", "description", "").setHorizontal(); // 19
            addListItemHorizontally("NEIGHBORING_CELL_INFO", "description", "").setHorizontal();
            addListItemHorizontally("NETWORK_COUNTRY_ISO", "description", "").setHorizontal();
            addListItemHorizontally("NETWORK_OPERATOR", "description", "").setHorizontal();
            addListItemHorizontally("NETWORK_OPERATOR_NAME", "description", "").setHorizontal();
            addListItemHorizontally("NETWORK_TYPE", "description", "").setHorizontal();
            addListItemHorizontally("PHONE_TYPE", "description", "").setHorizontal();
            addListItemHorizontally("SUBSCRIBER_ID", "description", "").setHorizontal();
            addListItemHorizontally("VOICE_MAIL_ALPHA_TAG", "description", "").setHorizontal();
            addListItemHorizontally("VOICE_MAIL_NUMBER", "description", "").setHorizontal();
*/
    }

    @AskPermission(READ_PHONE_STATE)
    private void addImeiNumber() {
        addListItemHorizontally("IMEI No", getDeviceIdFromTelephonyManager(), "");
    }

    @AskPermission(READ_PHONE_STATE)
    private void addSimInfos() {
        final SIM sim = new SIM();

        String keys = "Country:" + BR;
        String values = sim.simCountry + BR;
        keys += "Operator Code:" + BR;
        values += sim.simOperatorCode + BR;
        keys += "Operator Name:" + BR;
        values += sim.simOperatorName + BR;
        keys += "Serial:" + BR;
        values += sim.simSerial + BR;
        keys += "State:" + BR;
        values += sim.simState + BR;
        addListItemWithTitle("SIM", keys, values, "");
    }

    private void addProxySettings() {

        final ProxySettings proxySettings = getProxySettings();

        String keys = "Host:" + BR;
        String values = proxySettings.Host == null ? "" : proxySettings.Host + BR;
        keys += "Port:" + BR;
        values += proxySettings.Port == 0 ? "" : proxySettings.Port + BR;
        keys += "Exclusion List:" + BR;
        values += proxySettings.ExclusionList == null ? "" : proxySettings.ExclusionList + BR;

        addListItemWithTitle("Proxy Settings", keys, values, "");
    }

    private void addBluetooth() {

        final Bluetooth bluetooth = Device.getBluetooth();

        String keys = "" + BR;
        String values = "" + BR;

        addListItemWithTitle("Bluetooth", keys, values, "");
    }
}
