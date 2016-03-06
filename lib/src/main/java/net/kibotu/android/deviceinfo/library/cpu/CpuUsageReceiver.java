package net.kibotu.android.deviceinfo.library.cpu;

import android.app.Activity;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.misc.ShellUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CpuUsageReceiver {

    private static final String TAG = CpuUsageReceiver.class.getSimpleName();
    private Cpu previousCpu;

    private final CpuObservable cpuObservable;
    private Timer timer;
    private long updateInterval;

    public CpuUsageReceiver() {
        cpuObservable = new CpuObservable();
        updateInterval = TimeUnit.SECONDS.toMillis(1);
    }

    public CpuUsageReceiver addCpuUpdateListener(CpuUpdateListener listener) {
        cpuObservable.addObserver(listener);
        return this;
    }

    public CpuUsageReceiver deleteObserver(CpuUpdateListener listener) {
        cpuObservable.deleteObserver(listener);
        return this;
    }

    public CpuUsageReceiver registerReceiver() {
        if (timer != null)
            return this;

        timer = new Timer();

        // scheduling cpu usage updates
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // doing ui updates on main thread
                ((Activity) Device.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cpuObservable.notifyObservers(getCpuUsage());
                    }
                });
            }
        }, 0, updateInterval);

        return this;
    }

    public CpuUsageReceiver unregisterReceiver() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        return this;
    }

    /**
     * @see <a href="https://github.com/takke/cpustats">cpustats</a>
     */
    public Cpu getCpuUsage() {
        if (previousCpu == null) {
            previousCpu = ProcStat.load();
            return previousCpu;
        }

        final Cpu cpu = ProcStat.load();

        // the amount of cores can be different since don't receive infos about idling cores
        final int usedCores = Math.min(cpu.cores.size(), previousCpu.cores.size());

        computeCoreUsage(cpu.allCores, previousCpu.allCores);

        for (int i = 0; i < usedCores; ++i) {
            final Core core = cpu.cores.get(i);
            final Core previousCore = previousCpu.cores.get(i);
            computeCoreUsage(core, previousCore);
        }

        return cpu;
    }

    private static void computeCoreUsage(Core core, Core previousCore) {
        // usuage can only be determined by the difference between previous and current total usage
        int total = core.total();
        int previousTotal = previousCore.total();

        final int totalDiff = total - previousTotal;
        if (totalDiff > 0) {
            final int idleDiff = core.idle - previousCore.idle;

            core.usage = 100 - idleDiff * 100 / (float) totalDiff;
        }
    }

    // endregion

    // region frequency

    public static int getCurrentCpuFreq() {
        return ShellUtils.readIntegerFile("/sys/devices/system/cpuUsageReceiver/cpu0/cpufreq/scaling_cur_freq");
    }

    public static int getMinCpuFreq() {
        return ShellUtils.readIntegerFile("/sys/devices/system/cpuUsageReceiver/cpu0/cpufreq/cpuinfo_min_freq");
    }

    public static int getMaxCpuFreq() {
        return ShellUtils.readIntegerFile("/sys/devices/system/cpuUsageReceiver/cpu0/cpufreq/cpuinfo_max_freq");
    }

    public CpuUsageReceiver setUpdateInterval(long timeInMillis) {
        updateInterval = timeInMillis;
        return this;
    }

    // endregion
}