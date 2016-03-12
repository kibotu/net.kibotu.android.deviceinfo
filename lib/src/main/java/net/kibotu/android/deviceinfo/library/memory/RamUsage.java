package net.kibotu.android.deviceinfo.library.memory;

import net.kibotu.android.deviceinfo.library.misc.Proc;
import net.kibotu.android.deviceinfo.library.misc.UpdateTimer;

/**
 * Created by Nyaruhodo on 12.03.2016.
 */
public class RamUsage extends UpdateTimer<Ram> {

    @Override
    protected Ram getData() {
        return Proc.getRam();
    }
}
