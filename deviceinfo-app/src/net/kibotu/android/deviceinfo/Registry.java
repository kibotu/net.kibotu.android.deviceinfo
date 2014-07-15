package net.kibotu.android.deviceinfo;

import android.os.Environment;
import net.kibotu.android.deviceinfo.fragments.list.DeviceInfoFragment;
import net.kibotu.android.deviceinfo.fragments.list.DeviceInfoItemAsync;
import net.kibotu.android.deviceinfo.fragments.list.IGetInfoFragment;
import net.kibotu.android.deviceinfo.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import static android.os.Build.*;
import static net.kibotu.android.deviceinfo.Device.context;

public enum Registry implements IGetInfoFragment {

    Unsorted(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("BOARD", "description", BOARD);
            cachedList.addItem("BOOTLOADER", "description", BOOTLOADER);
            cachedList.addItem("BRAND", "description", BRAND);
            cachedList.addItem("CPU_ABI", "description", CPU_ABI);
            cachedList.addItem("CPU_ABI2", "description", CPU_ABI2);
            cachedList.addItem("DEVICE", "description", DEVICE);
            cachedList.addItem("FINGERPRINT", "description", FINGERPRINT);
            cachedList.addItem("HOST", "description", HOST);
            cachedList.addItem("ID", "description", ID);
            cachedList.addItem("RADIO", "description", RADIO);
            cachedList.addItem("SERIAL", "description", SERIAL);
            cachedList.addItem("TAGS", "description", TAGS);
            cachedList.addItem("TIME", "description", "" + new Date(TIME));
            cachedList.addItem("TYPE", "description", TYPE);
            cachedList.addItem("UNKNOWN", "description", UNKNOWN);
            cachedList.addItem("USER", "description", USER);

            cachedList.addItem("IMEI No", "description", Device.getDeviceIdFromTelephonyManager());
            cachedList.addItem("IMSI No", "description", Device.getSubscriberIdFromTelephonyManager());
            cachedList.addItem("hwID", "description", Device.getSerialNummer());
            cachedList.addItem("AndroidID", "description", Device.getAndroidId());
            cachedList.addItem("AppVersion", "description", "" + Device.getVersionFromPackageManager());
        }
    },

    General(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("Time", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = String.valueOf(Calendar.getInstance().getTime());
                }
            });
        }
    },

    Battery(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList = new DeviceInfoFragment(context());

            cachedList.addItem("Charging", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + Device.getCharging();
                }
            });

            cachedList.addItem("Charge Level", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + Device.getChargeLevel();
                }
            });
        }
    },

    Display(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("DISPLAY", "description", DISPLAY);

            cachedList.addItem("Density", "description", context().getString(R.string.density) + " (" + Device.getDisplayMetrics().density + ")");
            cachedList.addItem("DensityDpi", "description", Device.getDisplayMetrics().densityDpi + " (" + Device.getDisplayMetrics().scaledDensity + ")");

//            cachedList.addItem("DPI X/Y", "description", Device.getRealDisplayMetrics().xdpi + " / " + Device.getRealDisplayMetrics().ydpi);

            cachedList.addItem("Screen size", "description", context().getString(R.string.screen_size));

            cachedList.addItem("Screen resolution", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getResolution();
                }
            });

            cachedList.addItem("Orientation", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = context().getString(R.string.orientation);
                }
            });

            cachedList.addItem("Rotation", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + context().getWindowManager().getDefaultDisplay().getRotation();
                }
            });

            cachedList.addItem("PixelFormat", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + context().getWindowManager().getDefaultDisplay().getPixelFormat();
                }
            });

            cachedList.addItem("RefreshRate", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + context().getWindowManager().getDefaultDisplay().getRefreshRate();
                }
            });
            cachedList.addItem("Locale", "description", context().getResources().getConfiguration().locale.toString());
        }
    },

    Memory(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("Total Memory Environment", "description",  Utils.formatBytes(Device.getTotalMemoryByEnvironment()));
            cachedList.addItem("Available Memory Activity", "description", Utils.formatBytes(Device.getFreeMemoryByActivityService()));
            cachedList.addItem("Available Memory Environment", "description", Utils.formatBytes(Device.getFreeMemoryByEnvironment()));
            cachedList.addItem("Max Heap Memory", "description", Utils.formatBytes(Device.getMaxMemory()));
            cachedList.addItem("Low Memory", "description", "" + Device.isLowMemory());
            cachedList.addItem("Memory Class", "description", Device.getMemoryClass() + " MB");
//            cachedList.addItem("Large Memory Class", "description", Device.getLargeMemoryClass() + " MB");

            cachedList.addItem("Total Memory by this App", "description",Utils.formatBytes( Device.getRuntimeTotalMemory()));
            cachedList.addItem("Used Memory by this App", "description", Utils.formatBytes(Device.getUsedMemorySize()));
            cachedList.addItem("Free Runtime Memory by this App", "description", Utils.formatBytes(Device.getRuntimeFreeMemory()));

            cachedList.addItem("Free Disc Space", "description", Utils.formatBytes(Device.getFreeDiskSpace()));

            cachedList.addItem("External Storage State", "description", Environment.getExternalStorageState());

            cachedList.addItem("Internal Storage Path", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(context().getFilesDir().getParent());
                }
            });

            cachedList.addItem("APK Storage Path", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(context().getPackageCodePath());
                }
            });

            cachedList.addItem("Root Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getRootDirectory());
                }
            });

            cachedList.addItem("Data Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getDataDirectory());
                }
            });

            cachedList.addItem("External Storage Director", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStorageDirectory());
                }
            });

            cachedList.addItem("Download Cache Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getDownloadCacheDirectory());
                }
            });

            cachedList.addItem("Directory Alarms", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
                }
            });

            cachedList.addItem("Directory DCIM", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
                }
            });

            cachedList.addItem("Directory Downloads", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                }
            });

            cachedList.addItem("Directory Movies", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
                }
            });

            cachedList.addItem("Directory Music", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
                }
            });

            cachedList.addItem("Directory Notifications", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
                }
            });

            cachedList.addItem("Directory Pictures", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                }
            });

            cachedList.addItem("Directory Podcasts", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
                }
            });

            cachedList.addItem("Directory Ringtones", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));
                }
            });
        }
    },

    Hardware(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("Rooted", "description", "" + Device.isPhoneRooted());

            cachedList.addItem("HARDWARE", "description", HARDWARE);
            cachedList.addItem("MANUFACTURER", "description", MANUFACTURER);
            cachedList.addItem("MODEL", "description", MODEL);
            cachedList.addItem("PRODUCT", "description", PRODUCT);

            cachedList.addItem("SDK", "description", VERSION.SDK);
            cachedList.addItem("ID", "description", "" + context().getWindowManager().getDefaultDisplay().getDisplayId());
            cachedList.addItem("SDK_INT", "description", "" + VERSION.SDK_INT);
            cachedList.addItem("CODENAME", "description", VERSION.CODENAME);
            cachedList.addItem("INCREMENTAL", "description", VERSION.INCREMENTAL);
            cachedList.addItem("RELEASE", "description", VERSION.RELEASE);
            cachedList.addItem("Manufacturer", "description", MANUFACTURER);
            cachedList.addItem("Model", "description", MODEL);
            cachedList.addItem("Device", "description", DEVICE);
            cachedList.addItem("Product", "description", PRODUCT);
            cachedList.addItem("Brand", "description", BRAND);
            cachedList.addItem("CPU+ABI", "description", CPU_ABI);
            cachedList.addItem("Build (Tags)", "description", DISPLAY + " (" + TAGS + ")");
            cachedList.addItem("Features", "description", Utils.jsonArrayToString(Device.getFeatures()));
            cachedList.addItem("Shared Libraries", "description", Utils.jsonArrayToString(Device.getSharedLibraries()));

//developer.apple.com/library/ios/#documentation/3DDrawing/Conceptual/OpenGLES_ProgrammingGuide/DeterminingOpenGLESCapabilities/DeterminingOpenGLESCapabilities.html
            //        cachedList.addItem("supportsOpenGLES2: " + supportsOpenGLES2();
//        cachedList.addItem("OpenGL Version", Device.getOpenGLVersion());

//        cachedList.addItem("OpenGL Constraints", Device.getOpenGLShaderConstraints());
        }
    },

    Software(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {
            cachedList = new DeviceInfoFragment(context());
            cachedList.addItem("Software", "description", "Value");
        }
    },

    Sensor(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList = new DeviceInfoFragment(context());
            cachedList.addItem("Sensor", "description", "Value");
        }
    },

    Network(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {
            cachedList.addItem("Network", "description", "Value");

            cachedList.addItem("MAC Address (wlan0)", "description", Device.getMACAddress("wlan0"));
            cachedList.addItem("MAC Address (eth0)", "description", Device.getMACAddress("eth0"));
            cachedList.addItem("IP4 Address", "description", Device.getIPAddress(true));
            cachedList.addItem("IP6 Address", "description", Device.getIPAddress(false));

            cachedList.addItem("Mobile County/Network code", "description", context().getResources().getConfiguration().mcc + "/" + context().getResources().getConfiguration().mnc);
            cachedList.addItem("UserAgent", "description", Device.getUserAgent());
        }
    };

    public int iconR;
    protected DeviceInfoFragment cachedList;

    public DeviceInfoFragment getFragmentList() {
        if (cachedList == null) {
            cachedList = new DeviceInfoFragment(context());
            createFragmentList();
        }
        return cachedList;
    }

    private Registry(final int iconR) {
        this.iconR = iconR;
    }
}