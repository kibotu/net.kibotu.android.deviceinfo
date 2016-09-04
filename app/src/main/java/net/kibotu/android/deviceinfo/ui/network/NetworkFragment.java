package net.kibotu.android.deviceinfo.ui.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.bluetooth.Bluetooth;
import net.kibotu.android.deviceinfo.library.buildinfo.BuildInfo;
import net.kibotu.android.deviceinfo.library.network.Network;
import net.kibotu.android.deviceinfo.library.network.ProxySettings;
import net.kibotu.android.deviceinfo.library.network.SIM;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.library.Device.getDeviceIdFromTelephonyManager;
import static net.kibotu.android.deviceinfo.library.Device.getSubscriberIdFromTelephonyManager;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class NetworkFragment extends ListFragment {

    @Override
    public String getTitle() {
        return getString(R.string.menu_item_network);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.network;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addSimInfos();

        addVerticallyCard("MAC Address: wlan0", Network.getMACAddress("wlan0"), "");
        addVerticallyCard("MAC Address: eth0", Network.getMACAddress("eth0"), "");
        addVerticallyCard("IP4 Address", Network.getIPAddress(true), "");
        addVerticallyCard("IP6 Address", Network.getIPAddress(false), "");
        addVerticallyCard("UserAgent", Network.getUserAgent(), "");

        addHorizontallyCard("IMSI No", getSubscriberIdFromTelephonyManager(), "");
        addHorizontallyCard("hwID", BuildInfo.getSerialNumber(), "");

        addImeiNumber();

        addProxySettings();

        // addBluetooth(); // TODO: 22.02.2016 http://stackoverflow.com/questions/30222409/android-broadcast-receiver-bluetooth-events-catching

/*
//
            addHorizontallyCard("EXTRA_BOND_STATE", "Used as an int extra field in ACTION_BOND_STATE_CHANGED intents. Contains the bond state of the remote device.\n" +
                    "Possible values are: BOND_NONE, BOND_BONDING, BOND_BONDED.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.BOND_STATE", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "EXTRA_CLASS:  " + bluetooth.BOND_STATE + " dBm";
                }
            });

            addHorizontallyCard("EXTRA_CLASS", "Used as a Parcelable BluetoothClass extra field in ACTION_FOUND and ACTION_CLASS_CHANGED intents.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.CLASS", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.CLASS + " dBm";
                }
            });

            addHorizontallyCard("EXTRA_DEVICE", "Used as a Parcelable BluetoothDevice extra field in every intent broadcast by this class. It contains the BluetoothDevice that the intent applies to.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.DEVICE", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.DEVICE + " dBm";
                }
            });

            addHorizontallyCard("EXTRA_NAME", "Used as a String extra field in ACTION_NAME_CHANGED and ACTION_FOUND intents. It contains the friendly Bluetooth name.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.NAME", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.NAME + " dBm";
                }
            });

            addHorizontallyCard("EXTRA_PAIRING_KEY", "Used as an int extra field in ACTION_PAIRING_REQUEST intents as the value of passkey.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PAIRING_KEY\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PAIRING_KEY + " dBm";
                }
            });

            addHorizontallyCard("EXTRA_PAIRING_VARIANT", "Used as an int extra field in ACTION_PAIRING_REQUEST intents to indicate pairing method used. Possible values are: PAIRING_VARIANT_PIN, PAIRING_VARIANT_PASSKEY_CONFIRMATION,\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PAIRING_VARIANT\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PAIRING_VARIANT + " dBm";
                }
            });

            addHorizontallyCard("EXTRA_PREVIOUS_BOND_STATE", "Used as an int extra field in ACTION_BOND_STATE_CHANGED intents. Contains the previous bond state of the remote device.\n" +
                    "Possible values are: BOND_NONE, BOND_BONDING, BOND_BONDED.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PREVIOUS_BOND_STATE\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PREVIOUS_BOND_STATE + " dBm";
                }
            });

            addHorizontallyCard("EXTRA_RSSI", "Used as an optional short extra field in ACTION_FOUND intents. Contains the RSSI value of the remote device as reported by the Bluetooth hardware.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.RSSI\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.RSSI + " dBm";
                }
            });

            addHorizontallyCard("EXTRA_UUID", "Used as an extra field in ACTION_UUID intents, Contains the ParcelUuids of the remote device which is a parcelable version of UUID.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.UUID\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.UUID + " dBm";
                }
            });

            addHorizontallyCard("PAIRING_VARIANT_PASSKEY_CONFIRMATION", "The user will be prompted to confirm the passkey displayed on the screen or an app will confirm the passkey for the user.\n" +
                    "Constant Value: 2 (0x00000002)", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.VARIANT_PASSKEY_CONFIRMATION + " dBm";
                }
            });

            addHorizontallyCard("PAIRING_VARIANT_PIN", "The user will be prompted to enter a pin or an app will enter a pin for user.\n" +
                    "Constant Value: 0 (0x00000000)", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.VARIANT_PIN + " dBm";
                }
            });
            addHorizontallyCard("NFC", "description", "").setHorizontal();
            addHorizontallyCard("CONSUMER_IR_SERVICE", "description", "").setHorizontal();
            addHorizontallyCard("NETWORK_INFO_WIFI", "description", "").setHorizontal();
            addHorizontallyCard("WIFI_BSSID", "description", "").setHorizontal();
            addHorizontallyCard("WIFI_HIDDEN_SSID", "description", "").setHorizontal();
            addHorizontallyCard("WIFI_IP_ADDRESS", "description", "").setHorizontal();
            addHorizontallyCard("WIFI_LINK_SPEED", "description", "").setHorizontal();
            addHorizontallyCard("WIFI_MAC_ADDRESS", "description", "").setHorizontal();
            addHorizontallyCard("WIFI_RSSI", "description", "").setHorizontal();
            addHorizontallyCard("WIFI_SSID", "description", "").setHorizontal();

            addHorizontallyCard("CALL_STATE", "description", "").setHorizontal();
            addHorizontallyCard("CELL_LOCATION", "description", "").setHorizontal();
            addHorizontallyCard("CELL_ACTIVITY", "description", "").setHorizontal();
            addHorizontallyCard("DATA_ACTIVITY", "description", "").setHorizontal();
            addHorizontallyCard("DATA_STATE", "description", "").setHorizontal();
            addHorizontallyCard("DEVICE_ID", "description", "").setHorizontal();
            addHorizontallyCard("DEVICE_SOFTWARE_VERSION", "description", "").setHorizontal();
            addHorizontallyCard("DEVICE_SOFTWARE_VERSION", "description", "").setHorizontal();
            addHorizontallyCard("LINE1_NUMBER", "description", "").setHorizontal();
            addHorizontallyCard("MMS_UA_PROF_URL", "description", "").setHorizontal(); // 19
            addHorizontallyCard("MMS_USER_AGENT", "description", "").setHorizontal(); // 19
            addHorizontallyCard("NEIGHBORING_CELL_INFO", "description", "").setHorizontal();
            addHorizontallyCard("NETWORK_COUNTRY_ISO", "description", "").setHorizontal();
            addHorizontallyCard("NETWORK_OPERATOR", "description", "").setHorizontal();
            addHorizontallyCard("NETWORK_OPERATOR_NAME", "description", "").setHorizontal();
            addHorizontallyCard("NETWORK_TYPE", "description", "").setHorizontal();
            addHorizontallyCard("PHONE_TYPE", "description", "").setHorizontal();
            addHorizontallyCard("SUBSCRIBER_ID", "description", "").setHorizontal();
            addHorizontallyCard("VOICE_MAIL_ALPHA_TAG", "description", "").setHorizontal();
            addHorizontallyCard("VOICE_MAIL_NUMBER", "description", "").setHorizontal();
*/
    }

    //    @AskPermission(READ_PHONE_STATE)
    private void addImeiNumber() {
        addHorizontallyCard("IMEI No", getDeviceIdFromTelephonyManager(), "");
    }

    //    @AskPermission(READ_PHONE_STATE)
    private void addSimInfos() {
        final SIM sim = new SIM();

        addSubListItem(new ListItem().setLabel("SIM")
                .addChild(new ListItem().setLabel("Country").setValue(sim.simCountry))
                .addChild(new ListItem().setLabel("Operator Code").setValue(sim.simOperatorCode))
                .addChild(new ListItem().setLabel("Operator Name").setValue(sim.simOperatorName))
                .addChild(new ListItem().setLabel("Serial").setValue(sim.simSerial))
                .addChild(new ListItem().setLabel("State").setValue(sim.simState))
        );
    }

    private void addProxySettings() {

        final ProxySettings proxySettings = Network.getProxySettings();

        addSubListItem(new ListItem().setLabel("Proxy Settings")
                .addChild(new ListItem().setLabel("Host").setValue(proxySettings.Host == null ? "" : proxySettings.Host))
                .addChild(new ListItem().setLabel("Port").setValue(proxySettings.Port == 0 ? "" : proxySettings.Port))
                .addChild(new ListItem().setLabel("Exclusion List").setValue(proxySettings.ExclusionList == null ? "" : proxySettings.ExclusionList))
        );
    }

    private void addBluetooth() {

        final Bluetooth bluetooth = Device.getBluetooth();

        addSubListItem(new ListItem().setLabel("Bluetooth"));
    }
}
