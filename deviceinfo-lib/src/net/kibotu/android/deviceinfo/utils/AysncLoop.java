package net.kibotu.android.deviceinfo.utils;

import net.kibotu.android.error.tracking.Logger;

public abstract class AysncLoop implements Runnable {



    public abstract void loop();
}