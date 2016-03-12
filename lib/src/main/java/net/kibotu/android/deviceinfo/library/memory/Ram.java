package net.kibotu.android.deviceinfo.library.memory;

/**
 * Created by Nyaruhodo on 11.03.2016.
 * <p/>
 * https://www.centos.org/docs/5/html/5.1/Deployment_Guide/s2-proc-meminfo.html
 * <p/>
 * http://zcentric.com/2012/05/29/mapping-procmeminfo-to-output-of-free-command/
 */
public class Ram {

    /**
     * Total amount of physical RAM, in kilobytes.
     */
    public long MemTotal;

    /**
     * The amount of physical RAM, in kilobytes, left unused by the system.
     */
    public long MemFree;

    /**
     * The amount of physical RAM, in kilobytes, used for file buffers.
     */
    public long Buffers;

    /**
     * The amount of physical RAM, in kilobytes, used as cache memory.
     */
    public long Cached;

    /**
     * The amount of swap, in kilobytes, used as cache memory.
     */
    public long SwapCached;

    /**
     * The total amount of buffer or page cache memory, in kilobytes, that is in active use.
     * This is memory that has been recently used and is usually not reclaimed for other purposes.
     */
    public long ActiveAnon;

    /**
     * The total amount of buffer or page cache memory, in kilobytes, that are free and available.
     * This is memory that has not been recently used and can be reclaimed for other purposes.
     */
    public long InactiveAnon;

    public long ActiveFile;
    public long InactiveFile;

    public long Active;

    public long Inactive;

    public long Unevictable;
    public long Mlocked;

    /**
     * The total and free amount of memory, in kilobytes, that is not directly mapped into kernel space.
     * The HighTotal value can vary based on the type of kernel used.
     */
    public long HighTotal;

    /**
     * The total and free amount of memory, in kilobytes, that is not directly mapped into kernel space.
     */
    public long HighFree;

    /**
     * The total and free amount of memory, in kilobytes, that is directly mapped into kernel space.
     * The LowTotal value can vary based on the type of kernel used.
     */
    public long LowTotal;

    /**
     * The total and free amount of memory, in kilobytes, that is directly mapped into kernel space.
     */
    public long LowFree;

    /**
     * The total amount of swap available, in kilobytes.
     */
    public long SwapTotal;

    /**
     * he total amount of swap free, in kilobytes.
     */
    public long SwapFree;

    /**
     * The total amount of memory, in kilobytes, waiting to be written back to the disk.
     */
    public long Dirty;

    /**
     * The total amount of memory, in kilobytes, actively being written back to the disk.
     */
    public long Writeback;
    public long AnonPages;

    /**
     * The total amount of memory, in kilobytes, which have been used to map devices, files, or libraries using the mmap command.
     */
    public long Mapped;
    public long Shmem;

    /**
     * The total amount of memory, in kilobytes, used by the kernel to cache data structures for its own use.
     */
    public long Slab;
    public long SReclaimable;
    public long SUnreclaim;
    public long KernelStack;

    /**
     * The total amount of memory, in kilobytes, dedicated to the lowest page table level.
     */
    public long PageTables;
    public long NFS_Unstable;
    public long Bounce;
    public long WritebackTmp;
    public long CommitLimit;


    /**
     * The total amount of memory, in kilobytes, estimated to complete the workload. This value represents the worst case scenario value, and also includes swap memory.
     */
    public long Committed_AS;

    /**
     * The total amount of memory, in kilobytes, of total allocated virtual address space.
     */
    public long VmallocTotal;

    /**
     * The total amount of memory, in kilobytes, of used virtual address space.
     */
    public long VmallocUsed;

    /**
     * The largest contiguous block of memory, in kilobytes, of available virtual address space.
     */
    public long VmallocChunk;


    /**
     * The total number of hugepages for the system. The number is derived by dividing Hugepagesize by the megabytes set aside for hugepages specified in /proc/sys/vm/hugetlb_pool.
     * This statistic only appears on the x86, Itanium, and AMD64 architectures.
     */
    public long HugePages_Total;

    /**
     * The total number of hugepages available for the system. This statistic only appears on the x86, Itanium, and AMD64 architectures.
     */
    public long HugePages_Free;

    /**
     * The size for each hugepages unit in kilobytes. By default, the value is 4096 KB on uniprocessor kernels for 32 bit architectures.
     * For SMP, hugemem kernels, and AMD64, the default is 2048 KB. For Itanium architectures, the default is 262144 KB.
     * This statistic only appears on the x86, Itanium, and AMD64 architectures.
     */
    public long Hugepagesize;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ram ram = (Ram) o;

        if (MemTotal != ram.MemTotal) return false;
        if (MemFree != ram.MemFree) return false;
        if (Buffers != ram.Buffers) return false;
        if (Cached != ram.Cached) return false;
        if (SwapCached != ram.SwapCached) return false;
        if (ActiveAnon != ram.ActiveAnon) return false;
        if (InactiveAnon != ram.InactiveAnon) return false;
        if (ActiveFile != ram.ActiveFile) return false;
        if (InactiveFile != ram.InactiveFile) return false;
        if (Active != ram.Active) return false;
        if (Inactive != ram.Inactive) return false;
        if (Unevictable != ram.Unevictable) return false;
        if (Mlocked != ram.Mlocked) return false;
        if (HighTotal != ram.HighTotal) return false;
        if (HighFree != ram.HighFree) return false;
        if (LowTotal != ram.LowTotal) return false;
        if (LowFree != ram.LowFree) return false;
        if (SwapTotal != ram.SwapTotal) return false;
        if (SwapFree != ram.SwapFree) return false;
        if (Dirty != ram.Dirty) return false;
        if (Writeback != ram.Writeback) return false;
        if (AnonPages != ram.AnonPages) return false;
        if (Mapped != ram.Mapped) return false;
        if (Shmem != ram.Shmem) return false;
        if (Slab != ram.Slab) return false;
        if (SReclaimable != ram.SReclaimable) return false;
        if (SUnreclaim != ram.SUnreclaim) return false;
        if (KernelStack != ram.KernelStack) return false;
        if (PageTables != ram.PageTables) return false;
        if (NFS_Unstable != ram.NFS_Unstable) return false;
        if (Bounce != ram.Bounce) return false;
        if (WritebackTmp != ram.WritebackTmp) return false;
        if (CommitLimit != ram.CommitLimit) return false;
        if (Committed_AS != ram.Committed_AS) return false;
        if (VmallocTotal != ram.VmallocTotal) return false;
        if (VmallocUsed != ram.VmallocUsed) return false;
        if (VmallocChunk != ram.VmallocChunk) return false;
        if (HugePages_Total != ram.HugePages_Total) return false;
        if (HugePages_Free != ram.HugePages_Free) return false;
        return Hugepagesize == ram.Hugepagesize;

    }

    @Override
    public int hashCode() {
        int result = (int) (MemTotal ^ (MemTotal >>> 32));
        result = 31 * result + (int) (MemFree ^ (MemFree >>> 32));
        result = 31 * result + (int) (Buffers ^ (Buffers >>> 32));
        result = 31 * result + (int) (Cached ^ (Cached >>> 32));
        result = 31 * result + (int) (SwapCached ^ (SwapCached >>> 32));
        result = 31 * result + (int) (ActiveAnon ^ (ActiveAnon >>> 32));
        result = 31 * result + (int) (InactiveAnon ^ (InactiveAnon >>> 32));
        result = 31 * result + (int) (ActiveFile ^ (ActiveFile >>> 32));
        result = 31 * result + (int) (InactiveFile ^ (InactiveFile >>> 32));
        result = 31 * result + (int) (Active ^ (Active >>> 32));
        result = 31 * result + (int) (Inactive ^ (Inactive >>> 32));
        result = 31 * result + (int) (Unevictable ^ (Unevictable >>> 32));
        result = 31 * result + (int) (Mlocked ^ (Mlocked >>> 32));
        result = 31 * result + (int) (HighTotal ^ (HighTotal >>> 32));
        result = 31 * result + (int) (HighFree ^ (HighFree >>> 32));
        result = 31 * result + (int) (LowTotal ^ (LowTotal >>> 32));
        result = 31 * result + (int) (LowFree ^ (LowFree >>> 32));
        result = 31 * result + (int) (SwapTotal ^ (SwapTotal >>> 32));
        result = 31 * result + (int) (SwapFree ^ (SwapFree >>> 32));
        result = 31 * result + (int) (Dirty ^ (Dirty >>> 32));
        result = 31 * result + (int) (Writeback ^ (Writeback >>> 32));
        result = 31 * result + (int) (AnonPages ^ (AnonPages >>> 32));
        result = 31 * result + (int) (Mapped ^ (Mapped >>> 32));
        result = 31 * result + (int) (Shmem ^ (Shmem >>> 32));
        result = 31 * result + (int) (Slab ^ (Slab >>> 32));
        result = 31 * result + (int) (SReclaimable ^ (SReclaimable >>> 32));
        result = 31 * result + (int) (SUnreclaim ^ (SUnreclaim >>> 32));
        result = 31 * result + (int) (KernelStack ^ (KernelStack >>> 32));
        result = 31 * result + (int) (PageTables ^ (PageTables >>> 32));
        result = 31 * result + (int) (NFS_Unstable ^ (NFS_Unstable >>> 32));
        result = 31 * result + (int) (Bounce ^ (Bounce >>> 32));
        result = 31 * result + (int) (WritebackTmp ^ (WritebackTmp >>> 32));
        result = 31 * result + (int) (CommitLimit ^ (CommitLimit >>> 32));
        result = 31 * result + (int) (Committed_AS ^ (Committed_AS >>> 32));
        result = 31 * result + (int) (VmallocTotal ^ (VmallocTotal >>> 32));
        result = 31 * result + (int) (VmallocUsed ^ (VmallocUsed >>> 32));
        result = 31 * result + (int) (VmallocChunk ^ (VmallocChunk >>> 32));
        result = 31 * result + (int) (HugePages_Total ^ (HugePages_Total >>> 32));
        result = 31 * result + (int) (HugePages_Free ^ (HugePages_Free >>> 32));
        result = 31 * result + (int) (Hugepagesize ^ (Hugepagesize >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Ram{" +
                "MemTotal=" + MemTotal +
                ", MemFree=" + MemFree +
                ", Buffers=" + Buffers +
                ", Cached=" + Cached +
                ", SwapCached=" + SwapCached +
                ", ActiveAnon=" + ActiveAnon +
                ", InactiveAnon=" + InactiveAnon +
                ", ActiveFile=" + ActiveFile +
                ", InactiveFile=" + InactiveFile +
                ", Active=" + Active +
                ", Inactive=" + Inactive +
                ", Unevictable=" + Unevictable +
                ", Mlocked=" + Mlocked +
                ", HighTotal=" + HighTotal +
                ", HighFree=" + HighFree +
                ", LowTotal=" + LowTotal +
                ", LowFree=" + LowFree +
                ", SwapTotal=" + SwapTotal +
                ", SwapFree=" + SwapFree +
                ", Dirty=" + Dirty +
                ", Writeback=" + Writeback +
                ", AnonPages=" + AnonPages +
                ", Mapped=" + Mapped +
                ", Shmem=" + Shmem +
                ", Slab=" + Slab +
                ", SReclaimable=" + SReclaimable +
                ", SUnreclaim=" + SUnreclaim +
                ", KernelStack=" + KernelStack +
                ", PageTables=" + PageTables +
                ", NFS_Unstable=" + NFS_Unstable +
                ", Bounce=" + Bounce +
                ", WritebackTmp=" + WritebackTmp +
                ", CommitLimit=" + CommitLimit +
                ", Committed_AS=" + Committed_AS +
                ", VmallocTotal=" + VmallocTotal +
                ", VmallocUsed=" + VmallocUsed +
                ", VmallocChunk=" + VmallocChunk +
                ", HugePages_Total=" + HugePages_Total +
                ", HugePages_Free=" + HugePages_Free +
                ", Hugepagesize=" + Hugepagesize +
                '}';
    }

    public long getAvailableInBytes() {
        return (MemFree + Cached) * 1024;
    }

    public long getFreeInBytes() {
        return (MemFree + Buffers + Cached + SwapFree) * 1024;
    }

    public long getUsedInBytes() {
        return (MemTotal - (Buffers + Cached + MemFree)) * 1024;
    }

    public long getTotalInBytes() {
        return MemTotal * 1024;
    }

    public long getUsedSwapInBytes() {
        return (SwapTotal - SwapFree) * 1024;
    }
}
