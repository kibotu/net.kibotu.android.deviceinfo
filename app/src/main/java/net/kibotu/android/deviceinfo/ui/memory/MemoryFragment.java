package net.kibotu.android.deviceinfo.ui.memory;

import android.os.Environment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class MemoryFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_memory);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

//        final LinearLayout lExternal = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);
//        Memory.threads.add(cachedList.addItem("External Storage", "Every Android-compatible device supports a shared \"external storage\" that you can use to save files. This can be a removable storage media (such as an SD card) or an internal (non-removable) storage. Files saved to the external storage are world-readable and can be modified by the user when they enable USB mass storage to transfer files on a computer.", 1f, true, new DeviceInfoItemAsync(0) {
//            @Override
//            protected void async() {
//                customView = lExternal;
//                Storage.EXTERNAL.update();
//                setMap(mapStorage(Storage.EXTERNAL));
//            }
//        }));
//
//        final LinearLayout lData = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);
//        Memory.threads.add(cachedList.addItem("Internal Storage", "You can save files directly on the device's internal storage. By default, files saved to the internal storage are private to your application and other applications cannot access them (nor can the user). When the user uninstalls your application, these files are removed.", 1f, true, new DeviceInfoItemAsync(2) {
//            @Override
//            protected void async() {
//                customView = lData;
//                Storage.DATA.update();
//                setMap(mapStorage(Storage.DATA));
//            }
//        }));
//
////            final LinearLayout lRoot = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);
////            cachedList.addItem("Root Storage", "description", 1f, true, new DeviceInfoItemAsync(3) {
////                @Override
////                protected void async() {
////                    customView = lRoot;
////                    Storage.ROOT.update();
////                    setMap(mapStorage(Storage.ROOT));
////                }
////            });
//
//        final LinearLayout l = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.ram, null);
//
//        Memory.threads.add(cachedList.addItem("RAM", "Output /proc/meminfo.", 1f, true, new DeviceInfoItemAsync(5) {
//
//            @Override
//            protected void async() {
//                customView = l;
//                setMap(parseRam(DeviceOld.getContentRandomAccessFile("/proc/meminfo")));
////                    setMap(parseRamSmall(Device.getContentRandomAccessFile("/proc/meminfo")));
//            }
//        }));
//
//        cachedList.addItem("External Storage State", "Returns the current state of the primary \"external\" storage device.", firstLetterToUpperCase(Environment.getExternalStorageState()), 6);
//
//        Memory.threads.add(cachedList.addItem("Low Memory", "description", 1f, true, new DeviceInfoItemAsync(7) {
//            @Override
//            protected void async() {
//                value = formatBool(DeviceOld.isLowMemory());
//            }
//        }));
//
//        cachedList.addItem("Memory Class", "description", String.format("%.2f MB", (float) DeviceOld.getMemoryClass()), 8);
//        // cachedList.addItem("Large Memory Class", "description", Device.getLargeMemoryClass() + " MB");
//
//        cachedList.addItem("Root Directory", "description", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getRootDirectory());
//            }
//        });
//
//        cachedList.addItem("External Storage Directory", "description", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStorageDirectory());
//            }
//        });
//
//        cachedList.addItem("Download Cache Directory", "description", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getDownloadCacheDirectory());
//            }
//        });
//
//        // http://developer.android.com/reference/android/os/Environment.html
//        cachedList.addItem("Directory Alarms", "Standard directory in which to place any audio files that should be in the list of alarms that the user can select (not as regular music).", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
//            }
//        });
//
//        cachedList.addItem("Directory DCIM", "The traditional location for pictures and videos when mounting the device as a camera.", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
//            }
//        });
//
//        cachedList.addItem("Directory Documents", "Standard directory in which to place documents that have been created by the user.", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//
//                if (!DeviceOld.supportsApi(19))
//                    value = "Added in API level 19.";
//                else {
//                    final String result = ReflectionHelper.getPublicStaticField(Environment.class, "DIRECTORY_DOCUMENTS");
//                    value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(result));
//                }
//            }
//        });
//
//        cachedList.addItem("Directory Downloads", "Standard directory in which to place files that have been downloaded by the user.", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
//            }
//        });
//
//        cachedList.addItem("Directory Movies", "Standard directory in which to place movies that are available to the user.", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
//            }
//        });
//
//        cachedList.addItem("Directory Music", "Standard directory in which to place any audio files that should be in the regular list of music for the user.", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
//            }
//        });
//
//        cachedList.addItem("Directory Notifications", "Standard directory in which to place any audio files that should be in the list of notifications that the user can select (not as regular music).", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
//            }
//        });
//
//        cachedList.addItem("Directory Pictures", "Standard directory in which to place pictures that are available to the user.", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
//            }
//        });
//
//        cachedList.addItem("Directory Podcasts", "Standard directory in which to place any audio files that should be in the list of podcasts that the user can select (not as regular music).", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
//            }
//        });
//
//        cachedList.addItem("Directory Ringtones", "Standard directory in which to place any audio files that should be in the list of ringtones that the user can select (not as regular music).", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));
//            }
//        });
    }
}
