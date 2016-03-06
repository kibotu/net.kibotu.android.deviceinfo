package net.kibotu.android.deviceinfo.ui.menu;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.v4.widget.DrawerLayout.*;

@IntDef({LOCK_MODE_UNLOCKED, LOCK_MODE_LOCKED_CLOSED, LOCK_MODE_LOCKED_OPEN})
@Retention(RetentionPolicy.SOURCE)
public @interface LockMode {
}