package net.kibotu.android.deviceinfo;

import android.os.Environment;
import net.kibotu.android.deviceinfo.GPU.OpenGLGles10Info;
import net.kibotu.android.deviceinfo.GPU.OpenGLGles20Info;
import net.kibotu.android.deviceinfo.fragments.list.DeviceInfoFragment;
import net.kibotu.android.deviceinfo.fragments.list.DeviceInfoItemAsync;
import net.kibotu.android.deviceinfo.fragments.list.IGetInfoFragment;
import net.kibotu.android.deviceinfo.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.os.Build.*;
import static net.kibotu.android.deviceinfo.Device.context;

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
            cachedList.addItem("Shared Libraries", "description", Utils.jsonArrayToString(Device.getSharedLibraries()));

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
                }
            }));
        }
    },

    // endregion

    // region General

    General(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("MANUFACTURER", "description", MANUFACTURER);
            cachedList.addItem("MODEL", "description", MODEL);
            cachedList.addItem("PRODUCT", "description", PRODUCT);
            cachedList.addItem("IMEI No", "description", Device.getDeviceIdFromTelephonyManager());

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
                }
            });

            cachedList.addItem("Status", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.getStatus();
                }
            });

            Battery.threads.add(cachedList.addItem("Charging Level", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + (int) (battery.getChargingLevel() * 100) + " %";
                }
            }));

            Battery.threads.add(cachedList.addItem("Voltage", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.voltage + " mV";
                }
            }));

            Battery.threads.add(cachedList.addItem("Temperature", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    //value = battery.getTemperatureFarenheit();
                    value = (battery.temperature / 10f) + " °C";
                }
            }));

            Battery.threads.add(cachedList.addItem("Health", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.health;
                }
            }));

            Battery.threads.add(cachedList.addItem("Charging Source", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.plugged;
                }
            }));

            Battery.threads.add(cachedList.addItem("Battery Present", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.present ? "Yes" : "No";
                }
            }));
        }
    },

    // endregion

    // region Display

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

            Display.threads.add(cachedList.addItem("Orientation", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = context().getString(R.string.orientation);
                }
            }));

            Display.threads.add(cachedList.addItem("Rotation", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + context().getWindowManager().getDefaultDisplay().getRotation();
                }
            }));

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


            cachedList.addItem("Screen Class", "description", ""); // medium
            cachedList.addItem("Resolution", "description", "");
            cachedList.addItem("Density Class", "description", "");
            cachedList.addItem("Density DPI", "description", "");
        }
    },

    // endregion

    // region Directories

    Directories(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

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

    // endregion

    // region Memory

    Memory(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("Total RAM", "description", "");
            cachedList.addItem("Available RAM", "description", "");
            cachedList.addItem("Total Internal Storage", "description", "");
            cachedList.addItem("Available Internal Storage", "description", "");
            cachedList.addItem("Total External Storage", "description", "");
            cachedList.addItem("Available External Storage", "description", "");

            cachedList.addItem("Total Memory Environment", "description", Utils.formatBytes(Device.getTotalMemoryByEnvironment()));

            Memory.threads.add(cachedList.addItem("Available Memory Activity", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatBytes(Device.getFreeMemoryByActivityService());
                }
            }));

            Memory.threads.add(cachedList.addItem("Available Memory Environment", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatBytes(Device.getFreeMemoryByEnvironment());
                }
            }));

            cachedList.addItem("Max Heap Memory", "description", Utils.formatBytes(Device.getMaxMemory()));

            Memory.threads.add(cachedList.addItem("Low Memory", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + Device.isLowMemory();
                }
            }));

            cachedList.addItem("Memory Class", "description", String.format("%.2f MB", (float) Device.getMemoryClass()));
//            cachedList.addItem("Large Memory Class", "description", Device.getLargeMemoryClass() + " MB");

            Memory.threads.add(cachedList.addItem("Total Memory by this App", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatBytes(Device.getRuntimeTotalMemory());
                }
            }));

            Memory.threads.add(cachedList.addItem("Used Memory by this App", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatBytes(Device.getUsedMemorySize());
                }
            }));

            Memory.threads.add(cachedList.addItem("Free Runtime Memory by this App", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatBytes(Device.getRuntimeFreeMemory());
                }
            }));

            Memory.threads.add(cachedList.addItem("Free Disc Space", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatBytes(Device.getFreeDiskSpace());
                }
            }));

            cachedList.addItem("External Storage State", "description", Environment.getExternalStorageState());
        }
    },

    // endregion

    // region CPU

    CPU(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {
            cachedList.addItem("CPU Cores", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + Device.getNumCores();

                    CPU.threads.add(cachedList.addItem("CPU Usage All Cores", "description", 1f, true, new DeviceInfoItemAsync() {
                        @Override
                        protected void async() {
                            value = "" + Device.getCpuUsage()[0] + " %";
                        }
                    }));

                    for (int i = 1; i < Device.getNumCores() + 1; ++i) {
                        final int finalI = i;
                        CPU.threads.add(cachedList.addItem("CPU Usage Core " + i, "description", 1f, true, new DeviceInfoItemAsync() {
                            @Override
                            protected void async() {
                                float usage = Device.getCpuUsage()[finalI];
                                value = usage <= 0.01f ? "Idle" : usage + " %";
                            }
                        }));
                    }
                }
            });

            cachedList.addItem("Min Frequency", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatFrequency(Device.getMinCpuFreq());
                }
            });

            cachedList.addItem("Max Frequency", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatFrequency(Device.getMaxCpuFreq());
                }
            });

            cachedList.addItem("Current Frequency", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Utils.formatFrequency(Device.getCurrentCpuFreq());
                }
            });

            cachedList.addItem("CPU Details", "description", new DeviceInfoItemAsync() {
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
                            value = Utils.formatOpenGles20info(info);
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
                            value = Utils.formatOpenGles10info(info);

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
            cachedList.addItem("Features", "description", Utils.jsonArrayToString(Device.getFeatures()));


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
                        value = "Type: " + Utils.getSensorName(s.getType()) + "\n" +
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
            cachedList.addItem("UserAgent", "description", Device.getUserAgent());
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
            createFragmentList();
            startRefreshingList(1);
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