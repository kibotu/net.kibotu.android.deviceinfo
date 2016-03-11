package net.kibotu.android.deviceinfo.library.cpu;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class Cpu {

    Core allCores;

    ArrayList<Core> cores;

    /**
     * context switches across all CPUs.
     */
    int ctxt;

    /**
     * time at which the system booted in epoch unix time in seconds
     */
    int btime;

    /**
     * number of processes and threads created
     */
    int processes;

    /**
     * number of processes currently running on CPUs
     */
    int procs_running;

    /**
     * processes currently blocked
     */
    int procs_blocked;

    public Cpu() {
        cores = new ArrayList<>();
    }

    public ArrayList<Core> getCores() {
        return cores;
    }

    public int getCtxt() {
        return ctxt;
    }

    public int getBtimeAsEpoch() {
        return btime;
    }

    public int getProcesses() {
        return processes;
    }

    public int getProcs_running() {
        return procs_running;
    }

    public int getProcs_blocked() {
        return procs_blocked;
    }

    public Core getAllCores() {
        return allCores;
    }

    public void setAllCores(Core allCores) {
        this.allCores = allCores;
    }

    private static int amountCoresCache = -1;

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getAmountCores() {
        if (amountCoresCache != -1)
            return amountCoresCache;

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            final File[] files = dir.listFiles(new CpuFileFilter());
            //Return the number of cores (virtual CPU devices)
            return amountCoresCache = files.length;
        } catch (final Exception e) {
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cpu cpu = (Cpu) o;

        if (ctxt != cpu.ctxt) return false;
        if (btime != cpu.btime) return false;
        if (processes != cpu.processes) return false;
        if (procs_running != cpu.procs_running) return false;
        if (procs_blocked != cpu.procs_blocked) return false;
        if (allCores != null ? !allCores.equals(cpu.allCores) : cpu.allCores != null) return false;
        return cores != null ? cores.equals(cpu.cores) : cpu.cores == null;

    }

    @Override
    public int hashCode() {
        int result = allCores != null ? allCores.hashCode() : 0;
        result = 31 * result + (cores != null ? cores.hashCode() : 0);
        result = 31 * result + ctxt;
        result = 31 * result + btime;
        result = 31 * result + processes;
        result = 31 * result + procs_running;
        result = 31 * result + procs_blocked;
        return result;
    }

    @Override
    public String toString() {
        return "Cpu{" +
                "allCores=" + allCores +
                ", cores=" + cores +
                ", ctxt=" + ctxt +
                ", btime=" + btime +
                ", processes=" + processes +
                ", procs_running=" + procs_running +
                ", procs_blocked=" + procs_blocked +
                '}';
    }
}
