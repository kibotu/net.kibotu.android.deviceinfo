package net.kibotu.android.deviceinfo.library.storage;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static net.kibotu.android.deviceinfo.library.version.Version.isAtLeastVersion;

public enum StorageSpace {

    ROOT(Environment.getRootDirectory().getAbsolutePath()),
    DATA(Environment.getDataDirectory().getAbsolutePath()),
    EXTERNAL(Environment.getExternalStorageDirectory().getAbsolutePath());

    private final StatFs statFs;
    public final String absolutePath;

    StorageSpace(final String absolutePath) {
        this.absolutePath = absolutePath;
        statFs = new StatFs(absolutePath);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public long getTotal() {
        return isAtLeastVersion(JELLY_BEAN_MR2)
                ? statFs.getBlockCountLong() * statFs.getBlockSizeLong()
                : statFs.getBlockCount() * statFs.getBlockSize();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public long getAvailable() {
        return isAtLeastVersion(JELLY_BEAN_MR2)
                ? statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()
                : statFs.getAvailableBlocks() * statFs.getBlockSize();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public long getFree() {
        return isAtLeastVersion(JELLY_BEAN_MR2)
                ? statFs.getFreeBlocksLong() * statFs.getBlockSizeLong()
                : statFs.getFreeBlocks() * statFs.getBlockSize();
    }
}