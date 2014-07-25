package net.kibotu.android.deviceinfo;

import android.os.BatteryManager;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import net.kibotu.android.deviceinfo.GPU.OpenGLGles10Info;
import net.kibotu.android.deviceinfo.GPU.OpenGLGles20Info;
import net.kibotu.android.deviceinfo.fragments.list.vertical.DeviceInfoFragment;
import net.kibotu.android.deviceinfo.fragments.list.vertical.DeviceInfoItemAsync;
import net.kibotu.android.deviceinfo.fragments.list.vertical.IGetInfoFragment;
import net.kibotu.android.deviceinfo.utils.CustomWebView;
import net.kibotu.android.deviceinfo.utils.Utils;
import net.kibotu.android.error.tracking.Logger;
import net.kibotu.android.error.tracking.ReflectionHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.os.Build.*;
import static net.kibotu.android.deviceinfo.Device.context;
import static net.kibotu.android.deviceinfo.SharedPreferenceHelper.shared;
import static net.kibotu.android.deviceinfo.utils.Utils.*;

public enum Registry implements IGetInfoFragment {

    // region Build

    Build(R.drawable.build, R.drawable.build_i) {
        @Override
        public void createFragmentList() {

            // http://developer.android.com/reference/android/os/Build.html
            cachedList.addItem("Android Id", "More specifically, Settings.Secure.ANDROID_ID. A 64-bit number (as a hex string) that is randomly generated on the device's first boot and should remain constant for the lifetime of the device (The value may change if a factory reset is performed on the device.)", Device.getAndroidId()).setHorizontal();
            cachedList.addItem("Board", "The name of the underlying board, like \"goldfish\"", BOARD).setHorizontal();
            cachedList.addItem("Bootloader", "The system bootloader version number", BOOTLOADER).setHorizontal();
            cachedList.addItem("Brand", "The consumer-visible brand with which the product/hardware will be associated, if any.", BRAND).setHorizontal();
            cachedList.addItem("CPU_ABI", "The name of the instruction set (CPU type + ABI convention) of native code.", CPU_ABI).setHorizontal();
            cachedList.addItem("CPU_ABI2", "The name of the second instruction set (CPU type + ABI convention) of native code.", CPU_ABI2).setHorizontal();
            cachedList.addItem("Device", "The name of the industrial design.", DEVICE).setHorizontal();
            cachedList.addItem("Display", "A build ID string meant for displaying to the user.", DISPLAY).setHorizontal();
            cachedList.addItem("Fingerprint", "A string that uniquely identifies this build.", FINGERPRINT).textAppearance = android.R.style.TextAppearance_Small;
            cachedList.addItem("Hardware", "The name of the hardware (from the kernel command line or /proc).", HARDWARE).setHorizontal();
            cachedList.addItem("Host", "description", HOST).setHorizontal();
            cachedList.addItem("Id", "Either a changelist number, or a label like \"M4-rc20\".", ID).setHorizontal();
            cachedList.addItem("Manufacturer", "The manufacturer of the product/hardware.", MANUFACTURER).setHorizontal();
            cachedList.addItem("Model", "The end-user-visible name for the end product.", MODEL).setHorizontal();
            cachedList.addItem("Product", "The name of the overall product.", PRODUCT).setHorizontal();
            cachedList.addItem("Radio", "The radio firmware version number. Note API >= 14: use getRadioVersion()", "" + Device.getRadio()).setHorizontal();
            cachedList.addItem("Serial", "A hardware serial number, if available.", SERIAL).setHorizontal();
            cachedList.addItem("Tags", "Comma-separated tags describing the build, like \"unsigned,debug\".", TAGS).setHorizontal();
            cachedList.addItem("Time", "Build Time: " + TIME, new Date(TIME).toString()).textAppearance = android.R.style.TextAppearance_Small;
            cachedList.addItem("Type", "The type of build, like \"user\" or \"eng\".", TYPE).setHorizontal();
            cachedList.addItem("User", "description", USER).setHorizontal();
            cachedList.addItem("Unknown", "Value used for when a build property is unknown.", UNKNOWN).setHorizontal();

            // http://developer.android.com/reference/android/os/Build.VERSION.html
            cachedList.addItem("Codename", "The current development codename, or the string \"REL\" if this is a release build.", VERSION.CODENAME).setHorizontal();
            cachedList.addItem("Incremental", "The internal value used by the underlying source control to represent this build. E.g., a perforce changelist number or a git hash. ", VERSION.INCREMENTAL).setHorizontal();
            cachedList.addItem("Release", "The user-visible version string. E.g., \"1.0\" or \"3.4b5\". ", VERSION.RELEASE).setHorizontal();
            cachedList.addItem("SDK", "The user-visible SDK version of the framework in its raw String representation; use SDK_INT instead", VERSION.SDK).setHorizontal();
            cachedList.addItem("SDK_INT", "The user-visible SDK version of the framework; its possible values are defined in Build.VERSION_CODES.", "" + VERSION.SDK_INT).setHorizontal();

            final JSONArray features = Device.getFeatures();
            cachedList.addItem("Features", "List of features that are available on the system. This device supports " + features.length() + " Features.", jsonArrayToString(sort(features))).textAppearance = android.R.style.TextAppearance_Small;
        }
    },

    // endregion

    // region Configuration

    Configuration(R.drawable.config, R.drawable.config_i) {
        @Override
        public void createFragmentList() {

            // http://developer.android.com/reference/android/content/res/Configuration.html
//            cachedList.addItem("densityDpi", "The target screen density being rendered to, corresponding to density resource qualifier.", Utils.formatOrientation(context().getResources().getConfiguration().densityDpi)).setHorizontal(); // 17
            cachedList.addItem("Font Scale", "Current user preference for the scaling factor for fonts, relative to the base density scaling.", "" + context().getResources().getConfiguration().fontScale).setHorizontal();
            cachedList.addItem("Hard Keyboard Hidden", "A flag indicating whether the hard keyboard has been hidden. This will be set on a device with a mechanism to hide the keyboard from the user, when that mechanism is closed. One of: HARDKEYBOARDHIDDEN_NO, HARDKEYBOARDHIDDEN_YES.", formatKeyBoardHidden(context().getResources().getConfiguration().hardKeyboardHidden)).setHorizontal();
            cachedList.addItem("Keyboard", "The kind of keyboard attached to the device. One of: KEYBOARD_NOKEYS, KEYBOARD_QWERTY, KEYBOARD_12KEY.", formatKeyboard(context().getResources().getConfiguration().keyboard)).setHorizontal();
            cachedList.addItem("Keyboard Hidden", "A flag indicating whether any keyboard is available. Unlike hardKeyboardHidden, this also takes into account a soft keyboard, so if the hard keyboard is hidden but there is soft keyboard available, it will be set to NO. Value is one of: KEYBOARDHIDDEN_NO, KEYBOARDHIDDEN_YES.", Utils.formatKeyboardHidden(context().getResources().getConfiguration().keyboardHidden)).setHorizontal();
            cachedList.addItem("Locale", "Current user preference for the locale, corresponding to locale resource qualifier.", context().getResources().getConfiguration().locale.toString()).setHorizontal();
            cachedList.addItem("IMSI MCC", "IMSI MCC (Mobile Country Code), corresponding to mcc resource qualifier.", "" + context().getResources().getConfiguration().mcc).setHorizontal();
            cachedList.addItem("IMSI MNC", "IMSI MNC (Mobile Network Code), corresponding to mnc resource qualifier.", "" + context().getResources().getConfiguration().mnc).setHorizontal();
            cachedList.addItem("Navigation", "The kind of navigation method available on the device. One of: NAVIGATION_NONAV, NAVIGATION_DPAD, NAVIGATION_TRACKBALL, NAVIGATION_WHEEL.", formatNavigation(context().getResources().getConfiguration().navigation)).setHorizontal();
            cachedList.addItem("Navigation Hidden", "A flag indicating whether any 5-way or DPAD navigation available.", formatNavigationHidden(context().getResources().getConfiguration().navigationHidden)).setHorizontal();
            cachedList.addItem("Orientation", "Overall orientation of the screen.", formatOrientation(context().getResources().getConfiguration().orientation)).setHorizontal();
//            cachedList.addItem("ScreenHeightDp", "The current height of the available screen space, in dp units, corresponding to screen height resource qualifier.", Utils.formatOrientation(context().getResources().getConfiguration().screenHeightDp)).setHorizontal(); // 13
            cachedList.addItem("Screen Layout", "Bit mask of overall layout of the screen. Currently there are two fields:\n" +
                    "The SCREENLAYOUT_SIZE_MASK bits define the overall size of the screen. They may be one of SCREENLAYOUT_SIZE_SMALL, SCREENLAYOUT_SIZE_NORMAL, SCREENLAYOUT_SIZE_LARGE, or SCREENLAYOUT_SIZE_XLARGE.\n" +
                    "The SCREENLAYOUT_LONG_MASK defines whether the screen is wider/taller than normal. They may be one of SCREENLAYOUT_LONG_NO or SCREENLAYOUT_LONG_YES.\n" +
                    "The SCREENLAYOUT_LAYOUTDIR_MASK defines whether the screen layout is either LTR or RTL. They may be one of SCREENLAYOUT_LAYOUTDIR_LTR or SCREENLAYOUT_LAYOUTDIR_RTL.\n" +
                    "See Supporting Multiple Screens for more information.", formatScreenLayout(context().getResources().getConfiguration().screenLayout)).setHorizontal();
//            cachedList.addItem("screenWidthDp", "The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.", Utils.formatOrientation(context().getResources().getConfiguration().screenWidthDp)).setHorizontal(); // 13
//            cachedList.addItem("smallestScreenWidthDp", "The smallest screen size an application will see in normal operation, corresponding to smallest screen width resource qualifier.", Utils.formatOrientation(context().getResources().getConfiguration().smallestScreenWidthDp)).setHorizontal(); // 13
            cachedList.addItem("Touchscreen", "The kind of touch screen attached to the device. One of: TOUCHSCREEN_NOTOUCH, TOUCHSCREEN_FINGER.", formatTouchscreen(context().getResources().getConfiguration().touchscreen)).setHorizontal();
            cachedList.addItem("UIMode", "Bit mask of the ui mode. Currently there are two fields:\n" +
                    "The UI_MODE_TYPE_MASK bits define the overall ui mode of the device. They may be one of UI_MODE_TYPE_UNDEFINED, UI_MODE_TYPE_NORMAL, UI_MODE_TYPE_DESK, UI_MODE_TYPE_CAR, UI_MODE_TYPE_TELEVISION, UI_MODE_TYPE_APPLIANCE, or UI_MODE_TYPE_WATCH.\n" +
                    "The UI_MODE_NIGHT_MASK defines whether the screen is in a special mode. They may be one of UI_MODE_NIGHT_UNDEFINED, UI_MODE_NIGHT_NO or UI_MODE_NIGHT_YES.", formatUIMode(context().getResources().getConfiguration().uiMode)).setHorizontal();
        }
    },

    // endregion

    // region CPU

    CPU(R.drawable.cpu, R.drawable.cpu_i) {

        final int cores = Cpu.getNumCores();

        @Override
        public void createFragmentList() {

            final LinearLayout lCores = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);
            CPU.threads.add(cachedList.addItem("CPU Utilization", "Output from /proc/stat.", 1f, true, new DeviceInfoItemAsync(0) {
                @Override
                protected void async() {

                    customView = lCores;
                    useHtml = true;

                    final float[] cpuUsages = Cpu.getCpuUsage();

                    keys = "Cores: " + BR;
                    value = cores + BR;
                    keys += "Utilization All Cores:" + BR;
                    value += formatPercent(cpuUsages[0]) + BR;

                    for (int i = 1; i < cores + 1; ++i) {
                        if (cpuUsages.length <= i) break;
                        final float usage = cpuUsages[i];
                        keys += "Utilization Core " + i + BR;
                        value += usage <= 0.01f ? "Idle" : Utils.formatPercent(usage) + BR;
                    }
                }
            }));

            final LinearLayout lFreq = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);
            CPU.threads.add(cachedList.addItem("Frequency", "Output cpuinfo_max_freq, cpuinfo_min_freq and scaling_cur_freq from /sys/devices/system/cpu/cpu0/cpufreq/.", 1f, true, new DeviceInfoItemAsync(cores + 2) {
                @Override
                protected void async() {

                    customView = lFreq;
                    useHtml = true;

                    keys = "Max:" + BR;
                    value = formatFrequency(Cpu.getMaxCpuFreq()) + BR;
                    keys += "Min:" + BR;
                    value += formatFrequency(Cpu.getMinCpuFreq()) + BR;
                    keys += "Current:" + BR;
                    value += formatFrequency(Cpu.getCurrentCpuFreq());
                }
            }));

            cachedList.addItem("CPU Details", "Output /proc/cpuinfo.", new DeviceInfoItemAsync(cores + 5) {
                @Override
                protected void async() {
                    value = Device.getCpuInfo();
                }
            });
        }
    },

    // endregion

    // region GPU

    GPU(R.drawable.gpu, R.drawable.gpu_i) {
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

    // region Memory

    Memory(R.drawable.memory, R.drawable.memory_i) {
        @Override
        public void createFragmentList() {

            final LinearLayout lExternal = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);
            cachedList.addItem("External Storage", "Every Android-compatible device supports a shared \"external storage\" that you can use to save files. This can be a removable storage media (such as an SD card) or an internal (non-removable) storage. Files saved to the external storage are world-readable and can be modified by the user when they enable USB mass storage to transfer files on a computer.", 1f, true, new DeviceInfoItemAsync(0) {
                @Override
                protected void async() {
                    customView = lExternal;
                    Storage.EXTERNAL.update();
                    setMap(mapStorage(Storage.EXTERNAL));
                }
            });

            final LinearLayout lData = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);
            cachedList.addItem("Internal Storage", "You can save files directly on the device's internal storage. By default, files saved to the internal storage are private to your application and other applications cannot access them (nor can the user). When the user uninstalls your application, these files are removed.", 1f, true, new DeviceInfoItemAsync(2) {
                @Override
                protected void async() {
                    customView = lData;
                    Storage.DATA.update();
                    setMap(mapStorage(Storage.DATA));
                }
            });

//            final LinearLayout lRoot = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);
//            cachedList.addItem("Root Storage", "description", 1f, true, new DeviceInfoItemAsync(3) {
//                @Override
//                protected void async() {
//                    customView = lRoot;
//                    Storage.ROOT.update();
//                    setMap(mapStorage(Storage.ROOT));
//                }
//            });

            final LinearLayout l = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);

            Memory.threads.add(cachedList.addItem("RAM", "Output /proc/meminfo.", 1f, true, new DeviceInfoItemAsync(5) {

                @Override
                protected void async() {
                    customView = l;
                    setMap(parseRam(Device.getContentRandomAccessFile("/proc/meminfo")));
//                    setMap(parseRamSmall(Device.getContentRandomAccessFile("/proc/meminfo")));
                }
            }));

            cachedList.addItem("External Storage State", "Returns the current state of the primary \"external\" storage device.", firstLetterToUpperCase(Environment.getExternalStorageState()), 6);

            Memory.threads.add(cachedList.addItem("Low Memory", "description", 1f, true, new DeviceInfoItemAsync(7) {
                @Override
                protected void async() {
                    value = formatBool(Device.isLowMemory());
                }
            }));

            cachedList.addItem("Memory Class", "description", String.format("%.2f MB", (float) Device.getMemoryClass()), 8);
            // cachedList.addItem("Large Memory Class", "description", Device.getLargeMemoryClass() + " MB");

            cachedList.addItem("Root Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getRootDirectory());
                }
            });

            cachedList.addItem("External Storage Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStorageDirectory());
                }
            });

            cachedList.addItem("Download Cache Directory", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getDownloadCacheDirectory());
                }
            });

            // http://developer.android.com/reference/android/os/Environment.html
            cachedList.addItem("Directory Alarms", "Standard directory in which to place any audio files that should be in the list of alarms that the user can select (not as regular music).", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
                }
            });

            cachedList.addItem("Directory DCIM", "The traditional location for pictures and videos when mounting the device as a camera.", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
                }
            });

            cachedList.addItem("Directory Documents", "Standard directory in which to place documents that have been created by the user.", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();

                    if (!Device.supportsApi(19))
                        value = "Added in API level 19.";
                    else {
                        final String result = ReflectionHelper.getPublicStaticField(Environment.class, "DIRECTORY_DOCUMENTS");
                        value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(result));
                    }
                }
            });

            cachedList.addItem("Directory Downloads", "Standard directory in which to place files that have been downloaded by the user.", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                }
            });

            cachedList.addItem("Directory Movies", "Standard directory in which to place movies that are available to the user.", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
                }
            });

            cachedList.addItem("Directory Music", "Standard directory in which to place any audio files that should be in the regular list of music for the user.", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
                }
            });

            cachedList.addItem("Directory Notifications", "Standard directory in which to place any audio files that should be in the list of notifications that the user can select (not as regular music).", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
                }
            });

            cachedList.addItem("Directory Pictures", "Standard directory in which to place pictures that are available to the user.", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                }
            });

            cachedList.addItem("Directory Podcasts", "Standard directory in which to place any audio files that should be in the list of podcasts that the user can select (not as regular music).", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
                }
            });

            cachedList.addItem("Directory Ringtones", "Standard directory in which to place any audio files that should be in the list of ringtones that the user can select (not as regular music).", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    useDirectoryLayout();
                    value = Device.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));
                }
            });
        }
    },

    // endregion

    // region Battery

    Battery(R.drawable.battery, R.drawable.battery_i) {
        @Override
        public void createFragmentList() {

            final Battery battery = Device.getBattery();

            cachedList.addItem("Technology", "Technology of the current battery.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.technology;
                    order = 0;
                }
            });

            cachedList.addItem("Status", "Current status constant.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.getStatus();
                    order = 1;
                }
            });

            Battery.threads.add(cachedList.addItem("Charging Level", "Current battery level, from 0 to the maximum battery level.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "" + (int) (battery.getChargingLevel() * 100) + " %";
                    order = 2;
                }
            }));

            Battery.threads.add(cachedList.addItem("Voltage", "Current battery voltage level.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.voltage + " mV";
                    order = 3;
                }
            }));

            Battery.threads.add(cachedList.addItem("Temperature", "Current battery temperature.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = (battery.temperature / 10f) + " °C [" + battery.getTemperatureFarenheit() + "]";
                    order = 4;
                }
            }));

            Battery.threads.add(cachedList.addItem("Health", "Current health constant.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = battery.health;
                    order = 5;
                }
            }));

            Battery.threads.add(cachedList.addItem("Power Source", "Indicating whether the device is plugged in to a power source; 0 means it is on battery, other constants are different types of power sources.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {

                    if (battery.plugged.equalsIgnoreCase("0")) {
                        value = "Battery";
                    } else {
                        value = battery.plugged;
                    }

                    order = 6;
                }
            }));

            Battery.threads.add(cachedList.addItem("Last Charging Source", "Last recorded charging power source.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {

                    if (battery.plugged.equalsIgnoreCase("0")) {
                        value = shared.prefs().getString(BatteryManager.EXTRA_PLUGGED, "Unknown");
                    } else {
                        shared.editor().putString(BatteryManager.EXTRA_PLUGGED, value = battery.plugged);
                        shared.editor().commit();
                    }

                    order = 7;
                }
            }));

            Battery.threads.add(cachedList.addItem("Battery Present", "Indicating whether a battery is present or not.", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = formatBool(battery.present);
                    order = 8;
                }
            }));
        }
    },

    // endregion

    // region Monitor
/*
    Monitor(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {
            Monitor.threads.add(cachedList.addItem("Time", "description", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = String.valueOf(Calendar.getInstance().getTime());
                }
            }));
        }
    },
*/
    // endregion

    // region Display

    Display(R.drawable.display, R.drawable.display_i) {
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

            cachedList.addItem("Display ID", "Each logical display has a unique id. The default display has id DEFAULT_DISPLAY. (" + android.view.Display.DEFAULT_DISPLAY + ")", "" + context().getWindowManager().getDefaultDisplay().getDisplayId());

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

    // region Network

    Network(R.drawable.network, R.drawable.network_i) {
        @Override
        public void createFragmentList() {

            final SIM sim = new SIM(Device.context());

            final LinearLayout l = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.sim, null);
            cachedList.addItem("SIM", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {

                    customView = l;
                    useHtml = true;

                    keys = "Country:" + BR;
                    value = sim.simCountry + BR;
                    keys += "Operator Code:" + BR;
                    value += sim.simOperatorCode + BR;
                    keys += "Operator Name:" + BR;
                    value += sim.simOperatorName + BR;
                    keys += "Serial:" + BR;
                    value += sim.simSerial + BR;
                    keys += "State:" + BR;
                    value += sim.simState + BR;
                }
            });

            cachedList.addItem("IMSI No", "description", Device.getSubscriberIdFromTelephonyManager()).setHorizontal();
            cachedList.addItem("hwID", "description", Device.getSerialNummer()).setHorizontal();
            cachedList.addItem("IMEI No", "description", Device.getDeviceIdFromTelephonyManager()).setHorizontal();

            cachedList.addItem("MAC Address: wlan0", "description", Device.getMACAddress("wlan0")).setHorizontal();
            cachedList.addItem("MAC Address: eth0", "description", Device.getMACAddress("eth0")).setHorizontal();
            cachedList.addItem("IP4 Address", "description", Device.getIPAddress(true)).setHorizontal();
            cachedList.addItem("IP6 Address", "description", Device.getIPAddress(false)).setHorizontal();
            cachedList.addItem("UserAgent", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    Device.getUserAgent(new Device.AsyncCallback<String>() {
                        @Override
                        public void onComplete(final String result) {
                            value = result;
                        }
                    });
                }
            });

            final ProxySettings proxySettings = Device.getProxySettings();
            final LinearLayout lProxy = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.sim, null);
            cachedList.addItem("Proxy Settings", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {

                    customView = lProxy;
                    useHtml = true;

                    keys = "Host:" + BR;
                    value = proxySettings.Host == null ? "" : proxySettings.Host + BR;
                    keys += "Port:" + BR;
                    value += proxySettings.Port == 0 ? "" : proxySettings.Port + BR;
                    keys += "Exclusion List:" + BR;
                    value += proxySettings.ExclusionList == null ? "" : proxySettings.ExclusionList + BR;
                }
            });
/*
            final Bluetooth bluetooth = new Bluetooth(Device.context());

            cachedList.addItem("EXTRA_BOND_STATE", "Used as an int extra field in ACTION_BOND_STATE_CHANGED intents. Contains the bond state of the remote device.\n" +
                    "Possible values are: BOND_NONE, BOND_BONDING, BOND_BONDED.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.BOND_STATE", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "EXTRA_CLASS:  " + bluetooth.BOND_STATE + " dBm";
                }
            });

            cachedList.addItem("EXTRA_CLASS", "Used as a Parcelable BluetoothClass extra field in ACTION_FOUND and ACTION_CLASS_CHANGED intents.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.CLASS", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.CLASS + " dBm";
                }
            });

            cachedList.addItem("EXTRA_DEVICE", "Used as a Parcelable BluetoothDevice extra field in every intent broadcast by this class. It contains the BluetoothDevice that the intent applies to.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.DEVICE", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.DEVICE + " dBm";
                }
            });

            cachedList.addItem("EXTRA_NAME", "Used as a String extra field in ACTION_NAME_CHANGED and ACTION_FOUND intents. It contains the friendly Bluetooth name.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.NAME", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.NAME + " dBm";
                }
            });

            cachedList.addItem("EXTRA_PAIRING_KEY", "Used as an int extra field in ACTION_PAIRING_REQUEST intents as the value of passkey.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PAIRING_KEY\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PAIRING_KEY + " dBm";
                }
            });

            cachedList.addItem("EXTRA_PAIRING_VARIANT", "Used as an int extra field in ACTION_PAIRING_REQUEST intents to indicate pairing method used. Possible values are: PAIRING_VARIANT_PIN, PAIRING_VARIANT_PASSKEY_CONFIRMATION,\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PAIRING_VARIANT\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PAIRING_VARIANT + " dBm";
                }
            });

            cachedList.addItem("EXTRA_PREVIOUS_BOND_STATE", "Used as an int extra field in ACTION_BOND_STATE_CHANGED intents. Contains the previous bond state of the remote device.\n" +
                    "Possible values are: BOND_NONE, BOND_BONDING, BOND_BONDED.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.PREVIOUS_BOND_STATE\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.PREVIOUS_BOND_STATE + " dBm";
                }
            });

            cachedList.addItem("EXTRA_RSSI", "Used as an optional short extra field in ACTION_FOUND intents. Contains the RSSI value of the remote device as reported by the Bluetooth hardware.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.RSSI\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.RSSI + " dBm";
                }
            });

            cachedList.addItem("EXTRA_UUID", "Used as an extra field in ACTION_UUID intents, Contains the ParcelUuids of the remote device which is a parcelable version of UUID.\n" +
                    "Constant Value: \"android.bluetooth.device.extra.UUID\"", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.UUID + " dBm";
                }
            });

            cachedList.addItem("PAIRING_VARIANT_PASSKEY_CONFIRMATION", "The user will be prompted to confirm the passkey displayed on the screen or an app will confirm the passkey for the user.\n" +
                    "Constant Value: 2 (0x00000002)", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.VARIANT_PASSKEY_CONFIRMATION + " dBm";
                }
            });

            cachedList.addItem("PAIRING_VARIANT_PIN", "The user will be prompted to enter a pin or an app will enter a pin for user.\n" +
                    "Constant Value: 0 (0x00000000)", 1f, true, new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = "RSSI:  " + bluetooth.VARIANT_PIN + " dBm";
                }
            });
            cachedList.addItem("NFC", "description", "").setHorizontal();
            cachedList.addItem("CONSUMER_IR_SERVICE", "description", "").setHorizontal();
            cachedList.addItem("NETWORK_INFO_WIFI", "description", "").setHorizontal();
            cachedList.addItem("WIFI_BSSID", "description", "").setHorizontal();
            cachedList.addItem("WIFI_HIDDEN_SSID", "description", "").setHorizontal();
            cachedList.addItem("WIFI_IP_ADDRESS", "description", "").setHorizontal();
            cachedList.addItem("WIFI_LINK_SPEED", "description", "").setHorizontal();
            cachedList.addItem("WIFI_MAC_ADDRESS", "description", "").setHorizontal();
            cachedList.addItem("WIFI_RSSI", "description", "").setHorizontal();
            cachedList.addItem("WIFI_SSID", "description", "").setHorizontal();

            cachedList.addItem("CALL_STATE", "description", "").setHorizontal();
            cachedList.addItem("CELL_LOCATION", "description", "").setHorizontal();
            cachedList.addItem("CELL_ACTIVITY", "description", "").setHorizontal();
            cachedList.addItem("DATA_ACTIVITY", "description", "").setHorizontal();
            cachedList.addItem("DATA_STATE", "description", "").setHorizontal();
            cachedList.addItem("DEVICE_ID", "description", "").setHorizontal();
            cachedList.addItem("DEVICE_SOFTWARE_VERSION", "description", "").setHorizontal();
            cachedList.addItem("DEVICE_SOFTWARE_VERSION", "description", "").setHorizontal();
            cachedList.addItem("LINE1_NUMBER", "description", "").setHorizontal();
            cachedList.addItem("MMS_UA_PROF_URL", "description", "").setHorizontal(); // 19
            cachedList.addItem("MMS_USER_AGENT", "description", "").setHorizontal(); // 19
            cachedList.addItem("NEIGHBORING_CELL_INFO", "description", "").setHorizontal();
            cachedList.addItem("NETWORK_COUNTRY_ISO", "description", "").setHorizontal();
            cachedList.addItem("NETWORK_OPERATOR", "description", "").setHorizontal();
            cachedList.addItem("NETWORK_OPERATOR_NAME", "description", "").setHorizontal();
            cachedList.addItem("NETWORK_TYPE", "description", "").setHorizontal();
            cachedList.addItem("PHONE_TYPE", "description", "").setHorizontal();
            cachedList.addItem("SUBSCRIBER_ID", "description", "").setHorizontal();
            cachedList.addItem("VOICE_MAIL_ALPHA_TAG", "description", "").setHorizontal();
            cachedList.addItem("VOICE_MAIL_NUMBER", "description", "").setHorizontal();
*/
        }
    },

    // endregion

    // region Sensor

    Sensor(R.drawable.sensors, R.drawable.sensors_i) {
        @Override
        public void createFragmentList() {

//            stopRefreshing();

            final List<android.hardware.Sensor> list = Device.getSensorList();
            for (final android.hardware.Sensor s : list) {
                final View v = LayoutInflater.from(context()).inflate(R.layout.sensors, null);
                cachedList.addItem(s.getName(), "description", new DeviceInfoItemAsync() {
                    @Override
                    protected void async() {

                        customView = v;
                        useHtml = true;

                        keys = "Type:" + BR;
                        value = getSensorName(s.getType()) + BR;
                        keys += "Vendor:" + BR;
                        value += s.getVendor() + BR;
                        keys += "Version:" + BR;
                        value += s.getVersion() + BR;
                        keys += "Resolution:" + BR;
                        value += s.getResolution() + BR;
                        keys += "Min Delay:" + BR;
                        value += s.getMinDelay() + BR;
                        keys += "Max Range:" + BR;
                        value += s.getMaximumRange() + BR;
                        keys += "Power:";
                        value += s.getPower();
                    }
                });
            }
        }
    },

    // endregion

    // region Java

    Java(R.drawable.java, R.drawable.java_i) {
        @Override
        public void createFragmentList() {

            // java vm
            cachedList.addItem("java.vm.name", "description", System.getProperty("java.vm.name")).setJavaSpecs();
            cachedList.addItem("java.vm.version", "description", System.getProperty("java.vm.version")).setJavaSpecs();
            cachedList.addItem("java.vm.vendor.url", "description", System.getProperty("java.vm.vendor.url")).setJavaSpecs();

            // java.vm.specification
            cachedList.addItem("java.vm.specification.version", "description", System.getProperty("java.vm.specification.version")).setJavaSpecs();
            cachedList.addItem("java.vm.specification.vendor", "description", System.getProperty("java.vm.specification.vendor")).setJavaSpecs();

            // java.specification
            cachedList.addItem("java.specification.name", "description", System.getProperty("java.specification.name")).setJavaSpecs();
            cachedList.addItem("java.specification.version", "description", System.getProperty("java.specification.version")).setJavaSpecs();
            cachedList.addItem("java.specification.vendor", "description", System.getProperty("java.specification.vendor")).setJavaSpecs();

            // java.vendor
            cachedList.addItem("java.vendor.url", "description", System.getProperty("java.vendor.url")).setJavaSpecs();
            cachedList.addItem("java.vendor", "description", System.getProperty("java.vendor")).setJavaSpecs();

            // java
            cachedList.addItem("java.version", "description", System.getProperty("java.version")).setJavaSpecs();
            cachedList.addItem("java.home", "description", System.getProperty("java.home")).setJavaSpecs();
            cachedList.addItem("java.runtime.version", "description", System.getProperty("java.runtime.version")).setJavaSpecs();
            cachedList.addItem("java.runtime.name", "description", System.getProperty("java.runtime.name")).setJavaSpecs();
            cachedList.addItem("java.class.version", "description", System.getProperty("java.class.version")).setJavaSpecs();
            cachedList.addItem("java.boot.class.path", "description", System.getProperty("java.boot.class.path")).textAppearance = android.R.style.TextAppearance_Small;
            cachedList.addItem("java.io.tmpdir", "description", System.getProperty("java.io.tmpdir")).textAppearance = android.R.style.TextAppearance_Small;
            cachedList.addItem("java.library.path", "description", System.getProperty("java.library.path")).setJavaSpecs();
            cachedList.addItem("java.compiler", "description", System.getProperty("java.compiler")).setJavaSpecs();
            cachedList.addItem("java.class.path", "description", System.getProperty("java.class.path")).setJavaSpecs();
            cachedList.addItem("java.ext.dirs", "description", System.getProperty("java.ext.dirs")).setJavaSpecs();

            // android
            cachedList.addItem("android.openssl.version", "description", System.getProperty("android.openssl.version")).setJavaSpecs();
            cachedList.addItem("android.icu.library.version", "description", System.getProperty("android.icu.library.version")).setJavaSpecs();
            cachedList.addItem("android.zlib.version", "description", System.getProperty("android.zlib.version")).setJavaSpecs();

            // user
            cachedList.addItem("user.dir", "description", System.getProperty("user.dir")).setJavaSpecs();
            cachedList.addItem("user.region", "description", System.getProperty("user.region")).setJavaSpecs();
            cachedList.addItem("user.home", "description", System.getProperty("user.home")).setJavaSpecs();
            cachedList.addItem("user.language", "description", System.getProperty("user.language")).setJavaSpecs();
            cachedList.addItem("user.name", "description", System.getProperty("user.name")).setJavaSpecs();
            cachedList.addItem("http.agent", "description", System.getProperty("http.agent")).textAppearance = android.R.style.TextAppearance_Small;

            // os
            cachedList.addItem("os.name", "description", System.getProperty("os.name")).setJavaSpecs();
            cachedList.addItem("os.arch", "description", System.getProperty("os.arch")).setJavaSpecs();
            cachedList.addItem("os.version", "description", System.getProperty("os.version")).setJavaSpecs();

            // file
            cachedList.addItem("file.encoding", "description", System.getProperty("file.encoding")).setJavaSpecs();
            cachedList.addItem("line.separator", "description", Utils.formatLineSeparator(System.getProperty("line.separator"))).setJavaSpecs();
            cachedList.addItem("path.separator", "description", System.getProperty("path.separator")).setJavaSpecs();
            cachedList.addItem("file.separator", "description", System.getProperty("file.separator")).setJavaSpecs();
        }
    },

    // endregion

    // region Audio
/*
    Audio(android.R.drawable.ic_menu_search) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("MODE", "description", "").setHorizontal();
            cachedList.addItem("RINGER_MODE", "description", "").setHorizontal();
            cachedList.addItem("STREAM_DTMF", "description", "").setHorizontal();
            cachedList.addItem("STREAM_MUSIC", "description", "").setHorizontal();
            cachedList.addItem("STREAM_NOTIFICATION", "description", "").setHorizontal();
            cachedList.addItem("STREAM_RING", "description", "").setHorizontal();
            cachedList.addItem("STREAM_SYSTEM", "description", "").setHorizontal();
            cachedList.addItem("STREAM_VOICE_CALL", "description", "").setHorizontal();
            cachedList.addItem("BLUETOOTH_A2DP_ON", "description", "").setHorizontal();
            cachedList.addItem("BLUETOOH_SCO_AVAILABLE_OFF_CALL", "description", "").setHorizontal();
            cachedList.addItem("BLUETOOTH_SCO_ON", "description", "").setHorizontal();
            cachedList.addItem("MICROPHONE_MUTE", "description", "").setHorizontal();
            cachedList.addItem("MUSIC_ACTIVE", "description", "").setHorizontal();
            cachedList.addItem("SPEAKERPHONE_ON", "description", "").setHorizontal();
        }
    },
*/
    // endregion

    // region Geolocation

    Geolocation(R.drawable.geo, R.drawable.geo_i) {
        @Override
        public void createFragmentList() {

            // todo freegeoip.net

            NetworkHelper.request("http://www.telize.com/geoip", new Device.AsyncCallback<JSONObject>() {
                @Override
                public void onComplete(final JSONObject result) {

                    final LinearLayout l = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);
                    final Map<String, String> geoMap = parseTelize(result);

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
    },

    // endregion

    // region App

    App(R.drawable.info, R.drawable.info_i) {
        @Override
        public void createFragmentList() {

            cachedList.addItem("AppVersion", "description", "" + Device.getVersionFromPackageManager(), 0).setHorizontal();

//            App.threads.add(cachedList.addItem("threads count", "description", 1f, true, new DeviceInfoItemAsync() {
//                @Override
//                protected void async() {
//                    value = "" + Thread.getAllStackTraces().keySet().size();
//                    order = 1;
//                }
//            }));

            final LinearLayout lApp = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);
            App.threads.add(cachedList.addItem("Runtime Memory App", "Currently reserved runtime memory by this App.", 1f, true, new DeviceInfoItemAsync(2) {
                @Override
                protected void async() {

                    customView = lApp;
                    useHtml = true;

                    keys = "Max:" + BR;
                    value = formatBytes(Device.getRuntimeMaxMemory()) + BR;
                    keys += "Total:" + BR;
                    value += formatBytes(Device.getRuntimeTotalMemory()) + BR;
                    keys += "Free:" + BR;
                    value += formatBytes(Device.getRuntimeFreeMemory()) + BR;
                    keys += "Used:" + BR;
                    value += formatBytes(Device.getUsedMemorySize()) + BR;
                }
            }));

            cachedList.addItem("APK Storage Path", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(context().getPackageCodePath());
                    order = 3;
//                    textAppearance = android.R.style.TextAppearance_Small;
                }
            });

            cachedList.addItem("Internal Storage Path", "description", new DeviceInfoItemAsync() {
                @Override
                protected void async() {
                    value = Device.getFileSize(context().getFilesDir().getParent());
                    order = 4;
//                    textAppearance = android.R.style.TextAppearance_Small;
                }
            });

            cachedList.addItem("Permissions", "All enabled permissions for this app.", jsonArrayToString(Device.getPermissions()), 5);

            cachedList.addItem("Shared Libraries", "List of shared libraries that are available on the system.", jsonArrayToString(Device.getSharedLibraries()), 6);
        }
    },

    // endregion

    // region Other

    Other(R.drawable.others, R.drawable.others_i) {
        @Override
        public void createFragmentList() {
            cachedList.addItem("Rooted", "Determines if this device has been rooted.", "" + Device.isPhoneRooted()).setHorizontal();
            cachedList.addItem("Installed Apps", "Amount of currently installed applications.", "" + Device.installedApps().size()).setHorizontal();
        }
    };

    // endregion

    // region Register

    public int iconR;
    public int iconR_i;
    protected volatile DeviceInfoFragment cachedList;
    private volatile boolean isRefreshing;
    private volatile List<Thread> threads;

    public DeviceInfoFragment getFragmentList() {
        if (cachedList == null) {
            cachedList = new DeviceInfoFragment(context());
            startRefreshingList(1);
            createFragmentList();

//            iconR_i = new BitmapDrawable(Device.context().getResources(), Utils.invert(BitmapFactory.decodeResource(Device.context().getResources(), iconR)));
        }

        return cachedList;
    }

    private Registry(final int iconR, final int iconR_i) {
        this.iconR = iconR;
        this.iconR_i = iconR_i;
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
                        Logger.e(e);
                    }

//                    Runtime.getRuntime().gc();

                    Device.context().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // redraw listview
                            if (cachedList != null) cachedList.list.notifyDataSetChanged();
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
                Logger.e(e);
            }
        }
    }

    public synchronized void stopRefreshing() {
        isRefreshing = false;
//        pauseThreads();
    }

    // endregion
}