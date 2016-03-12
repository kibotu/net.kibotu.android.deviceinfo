package net.kibotu.android.deviceinfo.library.cpu;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class Cpu {

    /**
     * Measures the number of jiffies (1/100 of a second for x86 systems) that the system has been in user mode,
     * user mode with low priority (nice), system mode, idle task, I/O wait, IRQ (hardirq), and softirq respectively.
     * The IRQ (hardirq) is the direct response to a hardware event. The IRQ takes minimal work for queuing the "heavy" work up for the softirq to execute.
     * The softirq runs at a lower priority than the IRQ and therefore may be interrupted more frequently.
     * The total for all CPUs is given at the top, while each individual CPU is listed below with its own statistics.
     * The following example is a 4-way Intel Pentium Xeon configuration with multi-threading enabled,
     * therefore showing four physical processors and four virtual processors totaling eight processors.
     */
    public Core allCores;

    private static int amountCoresCache = -1;

    public ArrayList<Core> cores;

    /**
     * context switches across all CPUs.
     */
    public int ctxt;

    /**
     * time at which the system booted in epoch unix time in seconds
     */
    public int btime;

    /**
     * number of processes and threads created
     */
    public int processes;

    /**
     * number of processes currently running on CPUs
     */
    public int procs_running;

    /**
     * processes currently blocked
     */
    public int procs_blocked;

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

    public void setCores(ArrayList<Core> cores) {
        this.cores = cores;
    }

    public void setCtxt(int ctxt) {
        this.ctxt = ctxt;
    }

    public void setBtime(int btime) {
        this.btime = btime;
    }

    public void setProcesses(int processes) {
        this.processes = processes;
    }

    public void setProcs_running(int procs_running) {
        this.procs_running = procs_running;
    }

    public void setProcs_blocked(int procs_blocked) {
        this.procs_blocked = procs_blocked;
    }

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
