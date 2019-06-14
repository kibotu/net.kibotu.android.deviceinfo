package net.kibotu.android.deviceinfo.library.misc;

import androidx.annotation.NonNull;

import net.kibotu.android.deviceinfo.library.cpu.Core;
import net.kibotu.android.deviceinfo.library.cpu.Cpu;
import net.kibotu.android.deviceinfo.library.memory.Ram;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * @see <a href="https://www.kernel.org/doc/Documentation/filesystems/proc.txt">proc stat</a>
 */
public class Proc {

    // region /proc/stat

    public static Cpu getCpu() {

        final Cpu cpu = new Cpu();

        try {
            final RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");

            String line = reader.readLine();

            while (line != null) {

                if (line.startsWith("cpu "))
                    cpu.allCores = parseCore(line);
                else if (line.startsWith("cpu"))
                    cpu.cores.add(parseCore(line));

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

    private static Core parseCore(final String procStatString) {
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

    // endregion

    // region /proc/meminfo

    public static Ram getRam() {

        final Ram ram = new Ram();

        try {
            final RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");

            String line = reader.readLine();

            while (line != null) {

                if (line.startsWith("MemTotal"))
                    ram.MemTotal = parseNumber(line);

                if (line.startsWith("MemFree"))
                    ram.MemFree = parseNumber(line);

                if (line.startsWith("Buffers"))
                    ram.Buffers = parseNumber(line);

                if (line.startsWith("Cached"))
                    ram.Cached = parseNumber(line);

                if (line.startsWith("SwapCached"))
                    ram.SwapCached = parseNumber(line);

                if (line.startsWith("Active"))
                    ram.Active = parseNumber(line);

                if (line.startsWith("Inactive"))
                    ram.Inactive = parseNumber(line);

                if (line.startsWith("ActiveAnon"))
                    ram.ActiveAnon = parseNumber(line);

                if (line.startsWith("InactiveAnon"))
                    ram.InactiveAnon = parseNumber(line);

                if (line.startsWith("ActiveFile"))
                    ram.ActiveFile = parseNumber(line);

                if (line.startsWith("InactiveFile"))
                    ram.InactiveFile = parseNumber(line);

                if (line.startsWith("Unevictable"))
                    ram.Unevictable = parseNumber(line);

                if (line.startsWith("Mlocked"))
                    ram.Mlocked = parseNumber(line);

                if (line.startsWith("HighTotal"))
                    ram.HighTotal = parseNumber(line);

                if (line.startsWith("HighFree"))
                    ram.HighFree = parseNumber(line);

                if (line.startsWith("LowTotal"))
                    ram.LowTotal = parseNumber(line);

                if (line.startsWith("LowFree"))
                    ram.LowFree = parseNumber(line);

                if (line.startsWith("SwapTotal"))
                    ram.SwapTotal = parseNumber(line);

                if (line.startsWith("SwapFree"))
                    ram.SwapFree = parseNumber(line);

                if (line.startsWith("Dirty"))
                    ram.Dirty = parseNumber(line);

                if (line.startsWith("Writeback"))
                    ram.Writeback = parseNumber(line);

                if (line.startsWith("AnonPages"))
                    ram.AnonPages = parseNumber(line);

                if (line.startsWith("Mapped"))
                    ram.Mapped = parseNumber(line);

                if (line.startsWith("Shmem"))
                    ram.Shmem = parseNumber(line);

                if (line.startsWith("Slab"))
                    ram.Slab = parseNumber(line);

                if (line.startsWith("SReclaimable"))
                    ram.SReclaimable = parseNumber(line);

                if (line.startsWith("SUnreclaim"))
                    ram.SUnreclaim = parseNumber(line);

                if (line.startsWith("KernelStack"))
                    ram.KernelStack = parseNumber(line);

                if (line.startsWith("PageTables"))
                    ram.PageTables = parseNumber(line);

                if (line.startsWith("NFS_Unstable"))
                    ram.NFS_Unstable = parseNumber(line);

                if (line.startsWith("Bounce"))
                    ram.Bounce = parseNumber(line);

                if (line.startsWith("WritebackTmp"))
                    ram.WritebackTmp = parseNumber(line);

                if (line.startsWith("CommitLimit"))
                    ram.CommitLimit = parseNumber(line);

                if (line.startsWith("Committed_AS"))
                    ram.Committed_AS = parseNumber(line);

                if (line.startsWith("VmallocTotal"))
                    ram.VmallocTotal = parseNumber(line);

                if (line.startsWith("VmallocUsed"))
                    ram.VmallocUsed = parseNumber(line);

                if (line.startsWith("VmallocChunk"))
                    ram.VmallocChunk = parseNumber(line);

                line = reader.readLine();
            }

        } catch (final IOException e) {
            e.printStackTrace();
        }

        return ram;
    }

    private static long parseNumber(@NonNull final String line) {
        long value = 0;
        try {
            value = Long.valueOf(line.replaceAll("\\D+", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    // endregion

    // region /proc/mounts

    // TODO: 12.03.2016 https://www.centos.org/docs/5/html/5.1/Deployment_Guide/s2-proc-mounts.html

    // endregion

    // region /proc/partitions

    // TODO: 12.03.2016 https://www.centos.org/docs/5/html/5.1/Deployment_Guide/s2-proc-partitions.html

    // endregion

    // region /proc/uptime

    // TODO: 12.03.2016 https://www.centos.org/docs/5/html/5.1/Deployment_Guide/s2-proc-uptime.html

    // endregion

    // region /proc/version

    // TODO: 12.03.2016 https://www.centos.org/docs/5/html/5.1/Deployment_Guide/s2-proc-version.html

    // endregion

    // region /proc/filesystems

    // TODO: 12.03.2016 http://www.cyberciti.biz/tips/tell-what-filesystems-linux-kernel-can-handle.html

    // endregion
}