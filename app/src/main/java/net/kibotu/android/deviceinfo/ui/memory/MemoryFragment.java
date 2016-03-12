package net.kibotu.android.deviceinfo.ui.memory;

import android.support.v7.widget.RecyclerView;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.memory.Ram;
import net.kibotu.android.deviceinfo.library.memory.RamUsage;
import net.kibotu.android.deviceinfo.library.misc.UpdateTimer;
import net.kibotu.android.deviceinfo.library.storage.StorageSpace;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.ui.ViewHelper.BYTES_TO_KB;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.formatBytes;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class MemoryFragment extends ListFragment {

    private RamUsage ramUsage;

    @Override
    protected String getTitle() {
        return getContext().getString(R.string.menu_item_memory);
    }

    @Override
    public void onDestroyView() {
        ramUsage.stop();
        super.onDestroyView();
    }

    @Override
    protected RecyclerView.Adapter injectAdapterAnimation(RecyclerView.Adapter adapter) {
        // overriding default behaviour, so we don't inject an animation adapter
        return adapter;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        ramUsage = new RamUsage();

        addRam();

        addAllStorageSpace();

        addSwap();

//
//        cachedList.addItem("External StorageSpace State", "Returns the current state of the primary \"external\" storage device.", firstLetterToUpperCase(Environment.getExternalStorageState()), 6);
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
//        cachedList.addItem("External StorageSpace Directory", "description", new DeviceInfoItemAsync() {
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
//        cachedList.addItem("Directory Alarms", "Standard directory in which to place any audio files that should be in the fragment_list of alarms that the user can select (not as regular music).", new DeviceInfoItemAsync() {
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
//        cachedList.addItem("Directory Music", "Standard directory in which to place any audio files that should be in the regular fragment_list of music for the user.", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
//            }
//        });
//
//        cachedList.addItem("Directory Notifications", "Standard directory in which to place any audio files that should be in the fragment_list of notifications that the user can select (not as regular music).", new DeviceInfoItemAsync() {
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
//        cachedList.addItem("Directory Podcasts", "Standard directory in which to place any audio files that should be in the fragment_list of podcasts that the user can select (not as regular music).", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
//            }
//        });
//
//        cachedList.addItem("Directory Ringtones", "Standard directory in which to place any audio files that should be in the fragment_list of ringtones that the user can select (not as regular music).", new DeviceInfoItemAsync() {
//            @Override
//            protected void async() {
//                useDirectoryLayout();
//                value = DeviceOld.getFileSize(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));
//            }
//        });

        // updating adapter
        ramUsage.addObserver(new UpdateTimer.UpdateListener<Ram>() {
            @Override
            protected void update(Ram cpu) {
                notifyDataSetChanged();
            }
        }).start();
    }

    //
    private void addAllStorageSpace() {
        addStorageSpace(StorageSpace.EXTERNAL, "External StorageSpace", "Environment.getExternalStorageDirectory() Every Android-compatible device supports a shared \"external storage\" that you can use to save files. This can be a removable storage media (such as an SD card) or an internal (non-removable) storage. Files saved to the external storage are world-readable and can be modified by the user when they enable USB mass storage to transfer files on a computer.");
        addStorageSpace(StorageSpace.DATA, "Internal StorageSpace", "Environment.getDataDirectory() You can save files directly on the device's internal storage. By default, files saved to the internal storage are private to your application and other applications cannot access them (nor can the user). When the user uninstalls your application, these files are removed.");
        addStorageSpace(StorageSpace.ROOT, "Root", "Environment.getRootDirectory()");
    }

    private void addStorageSpace(StorageSpace storageSpace, String label, String description) {
        ListItem item = new ListItem().setLabel(label).setDescription(description)
                .addChild(new ListItem().setLabel("Path").setValue(storageSpace.absolutePath))
                .addChild(new ListItem().setLabel("Available").setValue(formatBytes(storageSpace.getAvailable())))
                .addChild(new ListItem().setLabel("Free").setValue(formatBytes(storageSpace.getFree())))
                .addChild(new ListItem().setLabel("Total").setValue(formatBytes(storageSpace.getTotal() - storageSpace.getFree())));

        if (storageSpace.getFree() - storageSpace.getAvailable() > 0)
            item.addChild(new ListItem().setLabel("Busy").setValue(formatBytes(storageSpace.getFree() - storageSpace.getAvailable())));

        addSubListItem(item);
    }

    private void addRam() {

        final ListItem item = new ListItem().setLabel("Ram").setDescription("Output from /proc/meminfo.");
        addSubListItem(item);

        ramUsage.addObserver(new UpdateTimer.UpdateListener<Ram>() {

            @Override
            protected void update(Ram ram) {
                item.clear()
                        .addChild(new ListItem().setLabel("Path").setValue("/proc/meminfo"))
                        .addChild(new ListItem().setLabel("Available").setValue(formatBytes((ram.getAvailableInBytes()))))
                        .addChild(new ListItem().setLabel("Free").setValue(formatBytes(ram.getFreeInBytes())))
                        .addChild(new ListItem().setLabel("Total").setValue(formatBytes(ram.getTotalInBytes())))
                        .addChild(new ListItem().setLabel("Used").setValue(formatBytes(ram.getUsedInBytes())));
            }
        });
    }

    private void addSwap() {

        final ListItem item = new ListItem().setLabel("Swap").setDescription("Output from /proc/meminfo.");
        addSubListItem(item);

        ramUsage.addObserver(new UpdateTimer.UpdateListener<Ram>() {

            @Override
            protected void update(Ram ram) {
                item.clear()
                        .addChild(new ListItem().setLabel("Path").setValue("/proc/meminfo"))
                        .addChild(new ListItem().setLabel("Free").setValue(formatBytes(ram.SwapFree * BYTES_TO_KB)))
                        .addChild(new ListItem().setLabel("Total").setValue(formatBytes(ram.SwapTotal * BYTES_TO_KB)))
                        .addChild(new ListItem().setLabel("Used").setValue(formatBytes((ram.getUsedSwapInBytes()))));
            }
        });
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.memory;
    }
}
