package net.kibotu.android.deviceinfo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @see https://www.kernel.org/doc/Documentation/filesystems/proc.txt
 */
public class ProcStat {

    public ArrayList<Cpu> cpu;

    /**
     * context switches across all CPUs.
     */
    int ctxt;

    /**
     * time at which the system booted
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

    public static ProcStat loadProcStat() {

        ProcStat procStat = new ProcStat();
        procStat.cpu = new ArrayList<Cpu>();

        try {
            final RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");

            String line = reader.readLine();

            while(line != null) {

                if(line.startsWith("cpu"))
                    procStat.cpu.add(Cpu.parseCpu(line));

                if(line.startsWith("ctxt"))
                    procStat.ctxt = Integer.parseInt(line.split(" ")[1]);

                if(line.startsWith("btime"))
                    procStat.btime = Integer.parseInt(line.split(" ")[1]);

                if(line.startsWith("processes"))
                    procStat.processes = Integer.parseInt(line.split(" ")[1]);

                if(line.startsWith("procs_running"))
                    procStat.procs_running = Integer.parseInt(line.split(" ")[1]);

                if(line.startsWith("procs_blocked"))
                    procStat.procs_blocked = Integer.parseInt(line.split(" ")[1]);

                line = reader.readLine();
            }
        }
        catch (final IOException e) {
            Logger.e(e);
        }

        return procStat;
    }

    @Override
    public String toString() {
        return "ProcStat{" +
                "cpu=" + cpu +
                ", ctxt=" + ctxt +
                ", btime=" + btime +
                ", processes=" + processes +
                ", procs_running=" + procs_running +
                ", procs_blocked=" + procs_blocked +
                '}';
    }
}