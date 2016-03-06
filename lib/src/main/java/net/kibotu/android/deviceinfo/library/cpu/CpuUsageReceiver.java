package net.kibotu.android.deviceinfo.library.cpu;

import android.app.Activity;
import net.kibotu.android.deviceinfo.library.Device;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;
import static net.kibotu.android.deviceinfo.library.misc.ShellExtensions.readLineFrom;
import static net.kibotu.android.deviceinfo.library.misc.ShellExtensions.readRandomAcccessFileFrom;

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
        return parseInt(readLineFrom("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"));
    }

    public static int getMinCpuFreq() {
        return parseInt(readLineFrom("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"));
    }

    public static int getMaxCpuFreq() {
        return parseInt(readLineFrom("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
    }

    // endregion

    public CpuUsageReceiver setUpdateInterval(long timeInMillis) {
        updateInterval = timeInMillis;
        return this;
    }

    public static Map<String, String> getCpuInfo() {
        HashMap<String, String> map = new HashMap<>();

        String s = readRandomAcccessFileFrom("/proc/cpuinfo");

        Scanner scanner = new Scanner(s);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(":");
            if(split.length > 1)
                map.put(split[0].trim(), split[1].trim());
            else
                map.put(line, "");
        }

        return map;
    }
}