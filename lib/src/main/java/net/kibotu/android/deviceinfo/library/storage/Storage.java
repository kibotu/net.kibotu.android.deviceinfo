package net.kibotu.android.deviceinfo.library.storage;

import android.os.Environment;
import android.os.StatFs;
import net.kibotu.android.deviceinfo.library.misc.ReflectionHelper;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static net.kibotu.android.deviceinfo.library.version.Version.isAtLeastVersion;

public enum Storage {

    ROOT(Environment.getRootDirectory().getAbsolutePath()),
    DATA(Environment.getDataDirectory().getAbsolutePath()),
    EXTERNAL(Environment.getExternalStorageDirectory().getAbsolutePath());

    public long total;
    public long available;
    public long free;

    private final StatFs statFs;
    public final String absolutePath;

    Storage(final String absolutePath) {
        this.absolutePath = absolutePath;
        statFs = new StatFs(absolutePath);
        update();
    }

    public void update() {
        // TODO: 22.02.2016
        final long blockSize = statFs.getBlockSize();
        total = statFs.getBlockCount() * blockSize;
        if (isAtLeastVersion(JELLY_BEAN_MR2))
            available = ReflectionHelper.get(StatFs.class, "getAvailableBlocksLong", null);
        else
            available = statFs.getAvailableBlocks() * blockSize;
        free = statFs.getFreeBlocks() * blockSize;
    }
}