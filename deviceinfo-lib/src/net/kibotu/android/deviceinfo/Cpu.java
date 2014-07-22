package net.kibotu.android.deviceinfo;

import net.kibotu.android.error.tracking.Logger;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Cpu {

    /**
     * normal processes executing in user mode
     */
    public int user;
    /**
     * niced processes executing in user mode
     */
    public int nice;
    /**
     * processes executing in kernel mode
     */
    public int system;
    /**
     * twiddling thumbs
     */
    public int idle;
    /**
     * waiting for I/O to complete
     */
    public int iowait;
    /**
     * servicing public interrupts
     */
    public int irq;
    /**
     * servicing softirqs
     */
    public int softirq;
    /**
     * involuntary wait
     */
    public int steal;
    /**
     * running a normal guest
     */
    public int guest;
    /**
     * running a niced guest
     */
    public int guest_nice;

    public static Cpu parseCpu(final String cpuString) {
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

    @Override
    public String toString() {
        return "Cpu{" +
                "user=" + user +
                ", nice=" + nice +
                ", system=" + system +
                ", idle=" + idle +
                ", iowait=" + iowait +
                ", irq=" + irq +
                ", softirq=" + softirq +
                ", steal=" + steal +
                ", guest=" + guest +
                ", guest_nice=" + guest_nice +
                '}';
    }

    public int total() {
        return user + nice + system + idle + iowait + irq + softirq + steal + guest + guest_nice;
    }

    // region usage
    private static float[] cpuUsage;
    private static ProcStat lastPs;

    /**
     * @credits to https://github.com/takke/cpustats
     */
    public synchronized static float[] getCpuUsage() {
        if (cpuUsage == null) cpuUsage = new float[getNumCores()];

        if (lastPs == null) {
            lastPs = ProcStat.loadProcStat();
            return cpuUsage;
        }

        final ProcStat ps = ProcStat.loadProcStat();

        int usedCores = Math.min(ps.cpu.size(), lastPs.cpu.size());
        cpuUsage = new float[usedCores + 1];
        for (int i = 0; i < usedCores; ++i) {
            cpuUsage[i] = 0;
            final Cpu cpuC = ps.cpu.get(i);
            final Cpu cpuL = lastPs.cpu.get(i);

            int totalC = cpuC.total();
            int totalL = cpuL.total();

            final int totalDiff = totalC - totalL;
            if (totalDiff > 0) {
                final int idleDiff = cpuC.idle - cpuL.idle;

                cpuUsage[i] = 100 - idleDiff * 100 / (float) totalDiff;
            }
        }
        return cpuUsage;
    }

    // endregion

    // region amount cores

    private static int numCores = -1;

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {

        if (numCores != -1)
            return numCores;

        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return numCores = files.length;
        } catch (final Exception e) {
            //Default to return 1 core
            return numCores = 1;
        }
    }

    // endregion

    // region frequency

    public static int getCurrentCpuFreq() {
        return readIntegerFile("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
    }

    public static int getMinCpuFreq() {
        return readIntegerFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
    }

    public static int getMaxCpuFreq() {
        return readIntegerFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
    }

    private static int readIntegerFile(final String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            final String line = reader.readLine();
            return Integer.parseInt(line);
        } catch (final Exception e) {
            try {
                Thread.currentThread().join();
            } catch (final InterruptedException ex) {
                Logger.e(ex);
            }
            Logger.e(e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (final IOException e) {
                Logger.e(e);
            }
        }
        return 0;
    }

    // endregion
}