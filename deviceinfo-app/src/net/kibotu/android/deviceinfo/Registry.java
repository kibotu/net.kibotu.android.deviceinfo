package net.kibotu.android.deviceinfo;

import android.os.BatteryManager;
import android.os.Environment;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import net.kibotu.android.deviceinfo.GPU.OpenGLGles10Info;
import net.kibotu.android.deviceinfo.GPU.OpenGLGles20Info;
import net.kibotu.android.deviceinfo.fragments.list.vertical.DeviceInfoFragment;
import net.kibotu.android.deviceinfo.fragments.list.vertical.DeviceInfoItemAsync;
import net.kibotu.android.deviceinfo.fragments.list.vertical.IGetInfoFragment;
import net.kibotu.android.deviceinfo.utils.CustomWebView;
import net.kibotu.android.deviceinfo.utils.Utils;
import org.json.JSONObject;

import java.util.*;

import static android.os.Build.*;
import static net.kibotu.android.deviceinfo.Device.context;
import static net.kibotu.android.deviceinfo.SharedPreferenceHelper.shared;
import static net.kibotu.android.deviceinfo.utils.Utils.*;

public enum Registry implements IGetInfoFragment {

    // region Unsorted

    Unsorted(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {


            cachedList.addItem("BRAND", "description", BRAND);
            cachedList.addItem("DEVICE", "description", DEVICE);
            cachedList.addItem("RADIO", "description", RADIO);
            cachedList.addItem("SERIAL", "description", SERIAL);
            cachedList.addItem("TIME", "description", "" + new Date(TIME));
            cachedList.addItem("TYPE", "description", TYPE);
            cachedList.addItem("UNKNOWN", "description", UNKNOWN);
            cachedList.addItem("USER", "description", USER);

            cachedList.addItem("ID", "description", "" + context().getWindowManager().getDefaultDisplay().getDisplayId());
            cachedList.addItem("Manufacturer", "description", MANUFACTURER);
            cachedList.addItem("Model", "description", MODEL);
            cachedList.addItem("Device", "description", DEVICE);
            cachedList.addItem("Product", "description", PRODUCT);
            cachedList.addItem("Brand", "description", BRAND);

            cachedList.addItem("IMSI No", "description", Device.getSubscriberIdFromTelephonyManager());
            cachedList.addItem("hwID", "description", Device.getSerialNummer());
            cachedList.addItem("AndroidID", "description", Device.getAndroidId());
            cachedList.addItem("AppVersion", "description", "" + Device.getVersionFromPackageManager());
        }
    },

    // endregion

    // region Monitor

    Monitor(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {
            Monitor.threads.add(cachedList.addItem("Time", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = String.valueOf(Calendar.getInstance().getTime());
                }
            }));
            Monitor.threads.add(cachedList.addItem("threads count", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + Thread.getAllStackTraces().keySet().size();
                    order = 1;
                }
            }));
        }
    },

    // endregion

    // region General

    Build(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("MANUFACTURER", "description", MANUFACTURER).setHorizontal();
            cachedList.addItem("MODEL", "description", MODEL).setHorizontal();
            cachedList.addItem("PRODUCT", "description", PRODUCT).setHorizontal();
            cachedList.addItem("IMEI No", "description", Device.getDeviceIdFromTelephonyManager()).setHorizontal();
            cachedList.addItem("Locale", "description", context().getResources().getConfiguration().locale.toString()).setHorizontal();
        }
    },

    // endregion

    // region Battery

    Battery(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            final Battery battery = Device.getBattery();

            cachedList.addItem("Technology", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.technology;
                    order = 0;
                }
            });

            cachedList.addItem("Status", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.getStatus();
                    order = 1;
                }
            });

            Battery.threads.add(cachedList.addItem("Charging Level", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + (int) (battery.getChargingLevel() * 100) + " %";
                    order = 2;
                }
            }));

            Battery.threads.add(cachedList.addItem("Voltage", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.voltage + " mV";
                    order = 3;
                }
            }));

            Battery.threads.add(cachedList.addItem("Temperature", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = (battery.temperature / 10f) + " °C [" + battery.getTemperatureFarenheit() + "]";
                    order = 4;
                }
            }));

            Battery.threads.add(cachedList.addItem("Health", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.health;
                    order = 5;
                }
            }));

            Battery.threads.add(cachedList.addItem("Last Charging Source", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {

                    if (battery.plugged.equalsIgnoreCase("0")) {
                        value = shared.prefs().getString(BatteryManager.EXTRA_PLUGGED, "Unknown");
                    } else {
                        shared.editor().putString(BatteryManager.EXTRA_PLUGGED, value = battery.plugged);
                        shared.editor().commit();
                    }

                    order = 6;
                }
            }));

            Battery.threads.add(cachedList.addItem("Battery Present", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = formatBool(battery.present);
                    order = 7;
                }
            }));
        }
    },

    // endregion

    // region Display

    Display(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("Display Screen Resolution", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getResolution() + " px | " + Device.getResolutionDp() + " dp";
                    order = 0;
                }
            });

            cachedList.addItem("Usable Screen Resolution", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getUsableResolution() + " px | " + Device.getUsableResolutionDp() + " dp";
                    order = 1;
                }
            });

            cachedList.addItem("Screen Diagonal Length", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.inchToCm(DisplayHelper.getScreenDiagonalInches()) + " | " + Utils.formatInches(DisplayHelper.getScreenDiagonalInches()) + " | " + Utils.formatPixel(DisplayHelper.getScreenDiagonalPixel());
                    order = 2;
                }
            });

            cachedList.addItem("Refresh Rate", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = context().getWindowManager().getDefaultDisplay().getRefreshRate() + " FPS";
                    order = 3;
                }
            });

            cachedList.addItem("Is Tablet", "description", formatBool(DisplayHelper.isTablet()));

            cachedList.addItem("Has Softkeys", "description", formatBool(DisplayHelper.hasSoftKeys()));

            cachedList.addItem("Screen Class", "description", context().getString(R.string.screen_size));

            cachedList.addItem("Density", "description", context().getString(R.string.density) + " | " + Device.getDisplayMetrics().densityDpi + " | " + Device.getDisplayMetrics().density);

            cachedList.addItem("DPI", "description", DisplayHelper.xDpi + " x " + DisplayHelper.yDpi);

            Display.threads.add(cachedList.addItem("Orientation", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = context().getString(R.string.orientation);
                }
            }));

            Display.threads.add(cachedList.addItem("Rotation", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = nameForRotation(context().getWindowManager().getDefaultDisplay().getRotation());
                }
            }));

            cachedList.addItem("Density Class", "description", "");
//            cachedList.addItem("Density DPI", "description", DisplayHelper.hasSoftKeys());

            cachedList.addItem("DISPLAY", "description", DISPLAY);

            cachedList.addItem("PixelFormat", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    // todo find a better way
                    // PixelFormat pi = new PixelFormat();
                    // PixelFormat.getPixelFormatInfo(context().getWindowManager().getDefaultDisplay().getPixelFormat(),pi);
                    value = nameForPixelFormat(context().getWindowManager().getDefaultDisplay().getPixelFormat());
                }
            });
        }
    },

    // endregion

    // region Memory

    Memory(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("External Storage", "description", 1f, true, new DeviceInfoItemAsync(0) {
                @Override
                protected void async() {
                    Storage.EXTERNAL.update();
                    value = Utils.formatStorage(Storage.EXTERNAL);
                }
            });

            cachedList.addItem("Internal Storage", "description", 1f, true, new DeviceInfoItemAsync(2) {
                @Override
                protected void async() {
                    Storage.DATA.update();
                    value = Utils.formatStorage(Storage.DATA);
                }
            });

            cachedList.addItem("Root Storage", "description", 1f, true, new DeviceInfoItemAsync(3) {
                @Override
                protected void async() {
                    Storage.ROOT.update();
                    value = Utils.formatStorage(Storage.ROOT);
                }
            });

            Memory.threads.add(cachedList.addItem("Runtime Memory by this App", "description", 1f, true, new DeviceInfoItemAsync(4) {
                @Override
                protected void async() {
                    value = "Total:\t\t" + formatBytes(Device.getRuntimeTotalMemory()) + "\n" +
                            "Free:\t\t" + formatBytes(Device.getRuntimeFreeMemory()) + "\n" +
                            "Used:\t\t" + formatBytes(Device.getUsedMemorySize());
                }
            }));

            final LinearLayout l = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);

            Memory.threads.add(cachedList.addItem("RAM", "description", 1f, true, new DeviceInfoItemAsync(5) {

                @Override
                protected void async() {
                    customView = l;
                    setMap(Utils.parseRam(Device.getContentRandomAccessFile("/proc/meminfo")));
                }
            }));

            cachedList.addItem("External Storage State", "description", Utils.firstLetterToUpperCase(Environment.getExternalStorageState()), 6);

            Memory.threads.add(cachedList.addItem("Low Memory", "description", 1f, true, new DeviceInfoItemAsync(7) {
                @Override
                protected void async() {
                    value = formatBool(Device.isLowMemory());
                }
            }));

            cachedList.addItem("Memory Class", "description", String.format("%.2f MB", (float) Device.getMemoryClass()), 8);
            // cachedList.addItem("Large Memory Class", "description", Device.getLargeMemoryClass() + " MB");

            cachedList.addItem("APK Storage Path", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(context().getPackageCodePath());
                }
            });

            cachedList.addItem("Internal Storage Path", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(context().getFilesDir().getParent());
                }
            });

            cachedList.addItem("Root Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getRootDirectory());
                }
            });

            cachedList.addItem("Data Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getDataDirectory());
                }
            });

            cachedList.addItem("External Storage Director", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStorageDirectory());
                }
            });

            cachedList.addItem("Download Cache Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getDownloadCacheDirectory());
                }
            });

            cachedList.addItem("Directory Alarms", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
                }
            });

            cachedList.addItem("Directory DCIM", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
                }
            });

            cachedList.addItem("Directory Downloads", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                }
            });

            cachedList.addItem("Directory Movies", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
                }
            });

            cachedList.addItem("Directory Music", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
                }
            });

            cachedList.addItem("Directory Notifications", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
                }
            });

            cachedList.addItem("Directory Pictures", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                }
            });

            cachedList.addItem("Directory Podcasts", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
                }
            });

            cachedList.addItem("Directory Ringtones", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    setHorizontal();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));
                }
            });
        }
    },

    // endregion

    // region CPU

    CPU(android.R.drawable.ic_menu_search) {

        final int cores = Device.getNumCores();

        @Override
        public void createFragmentList() {
            cachedList.addItem("CPU Cores", "description", new DeviceInfoItemAsync(1) {
                @Override
                protected void async() {
                    value = "" + cores;

                    CPU.threads.add(cachedList.addItem("CPU Utilization All Cores", "description", 1f, true, new DeviceInfoItemAsync(0) {
                        @Override
                        protected void async() {
                            value = "" + Device.getCpuUsage()[0] + " %";
                        }
                    }));

                    for (int i = 1; i < cores + 1; ++i) {
                        final int finalI = i;
                        CPU.threads.add(cachedList.addItem("CPU Utilization Core " + i, "description", 1f, true, new DeviceInfoItemAsync(finalI + 1) {
                            @Override
                            protected void async() {
                                float usage = Device.getCpuUsage()[finalI];
                                value = usage <= 0.01f ? "Idle" : usage + " %";
                            }
                        }));
                    }
                }
            });

            CPU.threads.add(cachedList.addItem("Current Frequency", "description", 1f, true, new DeviceInfoItemAsync(cores + 2) {
                @Override
                protected void async() {
                    value = formatFrequency(Device.getCurrentCpuFreq());
                }
            }));

            CPU.threads.add(cachedList.addItem("Min Frequency", "description", 1f, true, new DeviceInfoItemAsync(cores + 2) {
                @Override
                protected void async() {
                    value = formatFrequency(Device.getMinCpuFreq());
                }
            }));

            CPU.threads.add(cachedList.addItem("Max Frequency", "description", 1f, true, new DeviceInfoItemAsync(cores + 4) {
                @Override
                protected void async() {
                    value = formatFrequency(Device.getMaxCpuFreq());
                }
            }));

            cachedList.addItem("CPU Details", "description", 1f, false, new DeviceInfoItemAsync(cores + 5) {
                @Override
                protected void async() {
                    value = Device.getCpuInfo();
                }
            });
        }
    },

    // endregion

    // region GPU

    GPU(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            final GPU gpu = new GPU(Device.context());

            cachedList.addItem("OpenGL ES 2.0", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    gpu.loadOpenGLGles20Info(new GPU.OnCompleteCallback<OpenGLGles20Info>() {

                        @Override
                        public void onComplete(final OpenGLGles20Info info) {
                            value = formatOpenGles20info(info);
                        }
                    });
                }
            });

            cachedList.addItem("OpenGL ES-CM 1.1", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    gpu.loadOpenGLGles10Info(new GPU.OnCompleteCallback<OpenGLGles10Info>() {

                        @Override
                        public void onComplete(final OpenGLGles10Info info) {
                            value = formatOpenGles10info(info);

                            cachedList.addItem("Graphic Modes", "description", new DeviceInfoItemAsync() {

                                @Override
                                protected void async() {
                                    value = "";
                                    for (final GPU.Egl egl : info.eglconfigs)
                                        value += egl.toString() + "\n";
                                }
                            });
                        }
                    });
                }
            });
        }
    },

    // endregion

    // region Hardware

    Hardware(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("BOARD", "description", BOARD);
            cachedList.addItem("HARDWARE", "description", HARDWARE);
            cachedList.addItem("CPU_ABI", "description", CPU_ABI);
            cachedList.addItem("CPU_ABI2", "description", CPU_ABI2);


            cachedList.addItem("FINGERPRINT", "description", FINGERPRINT);
            cachedList.addItem("HOST", "description", HOST);
            cachedList.addItem("TAGS", "description", TAGS);
            cachedList.addItem("Features", "description", jsonArrayToString(Device.getFeatures()));


//developer.apple.com/library/ios/#documentation/3DDrawing/Conceptual/OpenGLES_ProgrammingGuide/DeterminingOpenGLESCapabilities/DeterminingOpenGLESCapabilities.html
            //        cachedList.addItem("supportsOpenGLES2: " + supportsOpenGLES2();
//        cachedList.addItem("OpenGL Version", Device.getOpenGLVersion());

//        cachedList.addItem("OpenGL Constraints", Device.getOpenGLShaderConstraints());
        }
    },

    // endregion

    // region Software

    Software(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {
            cachedList.addItem("Rooted", "description", "" + Device.isPhoneRooted());

            cachedList.addItem("Installed Apps", "description", "" + Device.installedApps().size());

            cachedList.addItem("SDK", "description", VERSION.SDK);
            cachedList.addItem("SDK_INT", "description", "" + VERSION.SDK_INT);
            cachedList.addItem("CODENAME", "description", VERSION.CODENAME);
            cachedList.addItem("INCREMENTAL", "description", VERSION.INCREMENTAL);
            cachedList.addItem("RELEASE", "description", VERSION.RELEASE);
            cachedList.addItem("BOOTLOADER", "description", BOOTLOADER);
            cachedList.addItem("ID", "description", ID);
            cachedList.addItem("Build (Tags)", "description", DISPLAY + " (" + TAGS + ")");
            cachedList.addItem("Shared Libraries", "description", jsonArrayToString(Device.getSharedLibraries()));

            cachedList.addItem("Permissions", "description", jsonArrayToString(Device.getPermissions()));
        }
    },

    // endregion

    // region Sensor

    Sensor(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            List<android.hardware.Sensor> list = Device.getSensorList();
            for (final android.hardware.Sensor s : list) {
                cachedList.addItem(s.getName(), "description", new DeviceInfoItemAsync() {
                    @Override
                    protected void async() {
                        value = "Type: " + getSensorName(s.getType()) + "\n" +
                                "Vendor: " + s.getVendor() + "\n" +
                                "Version: " + s.getVersion() + "\n" +
                                "Resolution: " + s.getResolution() + "\n" +
                                "Max Range: " + s.getMaximumRange() + "\n" +
                                "Min Delay: " + s.getMinDelay() + "\n" +
                                "Power: " + s.getPower();
                    }
                });
            }
        }
    },

    // endregion

    // region Network

    Network(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {
            cachedList.addItem("MAC Address (wlan0)", "description", Device.getMACAddress("wlan0"));
            cachedList.addItem("MAC Address (eth0)", "description", Device.getMACAddress("eth0"));
            cachedList.addItem("IP4 Address", "description", Device.getIPAddress(true));
            cachedList.addItem("IP6 Address", "description", Device.getIPAddress(false));

            cachedList.addItem("Mobile County/Network code", "description", context().getResources().getConfiguration().mcc + "/" + context().getResources().getConfiguration().mnc);
            cachedList.addItem("UserAgent", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    Device.getUserAgent(new Device.AsyncCallback<String>() {
                        @Override
                        public void onComplete(final String result) {
                            value = result;
                            Logger.v(result);
                        }
                    });
                }
            });
        }
    },

    // endregion

    // region Geolocation

    Geolocation(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            // todo freegeoip.net

            NetworkHelper.request("http://www.telize.com/geoip", new Device.AsyncCallback<JSONObject>() {
                @Override
                public void onComplete(final JSONObject result) {

                    final LinearLayout l = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);
                    final Map<String, String> geoMap = Utils.parseTelize(result);

                    cachedList.addItem("<b>Geolocation</b>", "description", new DeviceInfoItemAsync(0) {

                        @Override
                        protected void async() {
                            customView = l;
                            setMap(geoMap);
                        }
                    });

                    cachedList.addItem("<b>Google Maps</b>", "description", new DeviceInfoItemAsync(1) {

                        @Override
                        protected void async() {
                            context().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final LinearLayout linearWeb = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.linearlayoutwithtag, null);
                                    final WebView webView = CustomWebView.createWebView(context());
                                    linearWeb.addView(webView);
                                    // final String url2 = "https://www.google.com/maps/embed/v1/view?key="+API_KEY+"&center=" + geoMap.get("Latitude") + "," + geoMap.get("Longitude");
                                    final String url = "http://www.google.de/maps/?q=" + geoMap.get("Latitude") + "," + geoMap.get("Longitude") + "&t=h&z=17";
                                    webView.loadUrl(url);
                                    customView = linearWeb;
                                }
                            });
                        }
                    });
                }
            });
        }
    };

    // endregion

    // region Register

    public int iconR;
    protected DeviceInfoFragment cachedList;
    private volatile boolean isRefreshing;
    private List<Thread> threads;

    public DeviceInfoFragment getFragmentList() {
        if (cachedList == null) {
            cachedList = new DeviceInfoFragment(context());
            startRefreshingList(1);
            createFragmentList();
        }

        return cachedList;
    }

    private Registry(final int iconR) {
        this.iconR = iconR;
        isRefreshing = false;
        threads = new ArrayList<Thread>(50);
    }

    // endregion

    // region Threads

    public synchronized void startRefreshingList(final float intervalInSeconds) {

        if (isRefreshing)
            return;

        isRefreshing = true;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRefreshing) {

                    try {
                        Thread.sleep((long) (intervalInSeconds * 1000));
                    } catch (final InterruptedException e) {
                        Logger.e("" + e.getMessage(), e);
                    }

//                    Runtime.getRuntime().gc();

                    Device.context().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // redraw listview
                            cachedList.list.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    public synchronized void resumeThreads() {
        for (final Thread t : threads) {
            t.notify();
        }
    }

    public synchronized void pauseThreads() {
        for (final Thread t : threads) {
            try {
                t.wait();
            } catch (final InterruptedException e) {
                Logger.e("" + e.getMessage(), e);
            }
        }
    }

    public synchronized void stopRefreshing() {
        isRefreshing = false;
//        pauseThreads();
    }

    // endregion
}