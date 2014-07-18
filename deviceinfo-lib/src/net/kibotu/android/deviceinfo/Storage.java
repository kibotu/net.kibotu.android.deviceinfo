package net.kibotu.android.deviceinfo;

import android.os.Environment;
import android.os.StatFs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum Storage {

    ROOT(Environment.getRootDirectory().getAbsolutePath()),
    DATA(Environment.getDataDirectory().getAbsolutePath()),
    EXTERNAL(Environment.getExternalStorageDirectory().getAbsolutePath());

    public long total;
    public long available;
    public long free;

    private final StatFs statFs;
    public final String absolutePath;

    private Storage(final String absolutePath) {
        this.absolutePath = absolutePath;
        statFs = new StatFs(absolutePath);
        update();
    }

    public void update() {
        final long blockSize = statFs.getBlockSize();
        total = statFs.getBlockCount() * blockSize;

        if (Device.getApiLevel() >= 18) {
            try {
                final Method method = StatFs.class.getMethod("getAvailableBlocksLong");
                available = (Long) method.invoke(statFs);
            } catch (final NoSuchMethodException e) {
                Logger.e("" + e.getMessage(), e);
            } catch (InvocationTargetException e) {
                Logger.e("" + e.getMessage(), e);
            } catch (IllegalAccessException e) {
                Logger.e("" + e.getMessage(), e);
            }
        } else
            available = statFs.getAvailableBlocks() * blockSize;
        free = statFs.getFreeBlocks() * blockSize;
    }
}