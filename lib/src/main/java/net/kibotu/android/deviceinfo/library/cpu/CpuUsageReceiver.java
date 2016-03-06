package net.kibotu.android.deviceinfo.library.cpu;

import android.app.Activity;
import android.util.Log;
import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.android.deviceinfo.library.misc.ShellUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class CpuUsageReceiver {

    private static final String TAG = CpuUsageReceiver.class.getSimpleName();
    private static int amountCoresCache = -1;
    private float[] cpuUsage;
    private ProcStat lastPs;

    private final CpuObservable cpuObservable;
    private Timer timer;

    public CpuUsageReceiver() {
        cpuObservable = new CpuObservable();
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
                ((Activity)Device.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.v(TAG, "update cpu");

                        // TODO: 06.03.2016 getting usage updates

                        cpuObservable.notifyObservers(new Cpu().setNumCores(getNumCores()));
                    }
                });
            }
        }, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

        return this;
    }

    public CpuUsageReceiver unregisterReceiver() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        return this;
    }

//    /**
//     * @credits to https://github.com/takke/cpustats
//     */
//    public synchronized static float[] getCpuUsage() {
//        if (cpuUsage == null) cpuUsage = new float[getNumCores()];
//
//        if (lastPs == null) {
//            lastPs = ProcStat.loadProcStat();
//            return cpuUsage;
//        }
//
//        final ProcStat ps = ProcStat.loadProcStat();
//
//        int usedCores = Math.min(ps.cpuUsageReceiver.size(), lastPs.cpuUsageReceiver.size());
//        cpuUsage = new float[usedCores + 1];
//        for (int i = 0; i < usedCores; ++i) {
//            cpuUsage[i] = 0;
//            final CpuUsageReceiver cpuUsageReceiverC = ps.cpuUsageReceiver.get(i);
//            final CpuUsageReceiver cpuUsageReceiverL = lastPs.cpuUsageReceiver.get(i);
//
//            int totalC = cpuUsageReceiverC.total();
//            int totalL = cpuUsageReceiverL.total();
//
//            final int totalDiff = totalC - totalL;
//            if (totalDiff > 0) {
//                final int idleDiff = cpuUsageReceiverC.idle - cpuUsageReceiverL.idle;
//
//                cpuUsage[i] = 100 - idleDiff * 100 / (float) totalDiff;
//            }
//        }
//        return cpuUsage;
//    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpuUsageReceiver"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        if (amountCoresCache != -1)
            return amountCoresCache;

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            final File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return amountCoresCache = files.length;
        } catch (final Exception e) {
            e.printStackTrace();
            //Default to return 1 core
            return 1;
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

    // endregion

    static Cpu parseCpu(final String cpuString) {
        final Cpu cpu = new Cpu();

        final Scanner s = new Scanner(cpuString);

        if (s.hasNext()) s.next();
        if (s.hasNextInt()) cpu.user = s.nextInt();
        if (s.hasNextInt()) cpu.nice = s.nextInt();
        if (s.hasNextInt()) cpu.system = s.nextInt();
        if (s.hasNextInt()) cpu.idle = s.nextInt();
        if (s.hasNextInt()) cpu.iowait = s.nextInt();
        if (s.hasNextInt()) cpu.irq = s.nextInt();
        if (s.hasNextInt()) cpu.softirq = s.nextInt();
        if (s.hasNextInt()) cpu.steal = s.nextInt();
        if (s.hasNextInt()) cpu.guest = s.nextInt();
        if (s.hasNextInt()) cpu.guest_nice = s.nextInt();

        return cpu;
    }
}