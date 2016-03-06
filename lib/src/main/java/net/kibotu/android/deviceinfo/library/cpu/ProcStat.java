package net.kibotu.android.deviceinfo.library.cpu;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * @see <a href="https://www.kernel.org/doc/Documentation/filesystems/proc.txt">proc stat</a>
 */
public class ProcStat {

    public static Cpu load() {

        final Cpu cpu = new Cpu();

        try {
            final RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");

            String line = reader.readLine();

            while (line != null) {

                if (line.startsWith("cpu "))
                    cpu.allCores = parse(line);
                else if (line.startsWith("cpu"))
                    cpu.cores.add(parse(line));

                if (line.startsWith("ctxt"))
                    cpu.ctxt = Integer.parseInt(line.split(" ")[1]);

                if (line.startsWith("btime"))
                    cpu.btime = Integer.parseInt(line.split(" ")[1]);

                if (line.startsWith("processes"))
                    cpu.processes = Integer.parseInt(line.split(" ")[1]);

                if (line.startsWith("procs_running"))
                    cpu.procs_running = Integer.parseInt(line.split(" ")[1]);

                if (line.startsWith("procs_blocked"))
                    cpu.procs_blocked = Integer.parseInt(line.split(" ")[1]);

                line = reader.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return cpu;
    }

    private static Core parse(final String procStatString) {
        final Core core = new Core();

        final Scanner s = new Scanner(procStatString);

        if (s.hasNext()) s.next();
        if (s.hasNextInt()) core.user = s.nextInt();
        if (s.hasNextInt()) core.nice = s.nextInt();
        if (s.hasNextInt()) core.system = s.nextInt();
        if (s.hasNextInt()) core.idle = s.nextInt();
        if (s.hasNextInt()) core.iowait = s.nextInt();
        if (s.hasNextInt()) core.irq = s.nextInt();
        if (s.hasNextInt()) core.softirq = s.nextInt();
        if (s.hasNextInt()) core.steal = s.nextInt();
        if (s.hasNextInt()) core.guest = s.nextInt();
        if (s.hasNextInt()) core.guest_nice = s.nextInt();

        return core;
    }
}