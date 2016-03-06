package net.kibotu.android.deviceinfo.library.cpu;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class Cpu {

    private int numCores = -1;

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

    public int total() {
        return user + nice + system + idle + iowait + irq + softirq + steal + guest + guest_nice;
    }

    public int getNumCores() {
        return numCores;
    }

    public Cpu setNumCores(int numCores) {
        this.numCores = numCores;
        return this;
    }

    public int getUser() {
        return user;
    }

    public Cpu setUser(int user) {
        this.user = user;
        return this;
    }

    public int getNice() {
        return nice;
    }

    public Cpu setNice(int nice) {
        this.nice = nice;
        return this;
    }

    public int getSystem() {
        return system;
    }

    public Cpu setSystem(int system) {
        this.system = system;
        return this;
    }

    public int getIdle() {
        return idle;
    }

    public Cpu setIdle(int idle) {
        this.idle = idle;
        return this;
    }

    public int getIowait() {
        return iowait;
    }

    public Cpu setIowait(int iowait) {
        this.iowait = iowait;
        return this;
    }

    public int getIrq() {
        return irq;
    }

    public Cpu setIrq(int irq) {
        this.irq = irq;
        return this;
    }

    public int getSoftirq() {
        return softirq;
    }

    public Cpu setSoftirq(int softirq) {
        this.softirq = softirq;
        return this;
    }

    public int getSteal() {
        return steal;
    }

    public Cpu setSteal(int steal) {
        this.steal = steal;
        return this;
    }

    public int getGuest() {
        return guest;
    }

    public Cpu setGuest(int guest) {
        this.guest = guest;
        return this;
    }

    public int getGuest_nice() {
        return guest_nice;
    }

    public Cpu setGuest_nice(int guest_nice) {
        this.guest_nice = guest_nice;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cpu cpu = (Cpu) o;

        if (user != cpu.user) return false;
        if (nice != cpu.nice) return false;
        if (system != cpu.system) return false;
        if (idle != cpu.idle) return false;
        if (iowait != cpu.iowait) return false;
        if (irq != cpu.irq) return false;
        if (softirq != cpu.softirq) return false;
        if (steal != cpu.steal) return false;
        if (guest != cpu.guest) return false;
        return guest_nice == cpu.guest_nice;

    }

    @Override
    public int hashCode() {
        int result = user;
        result = 31 * result + nice;
        result = 31 * result + system;
        result = 31 * result + idle;
        result = 31 * result + iowait;
        result = 31 * result + irq;
        result = 31 * result + softirq;
        result = 31 * result + steal;
        result = 31 * result + guest;
        result = 31 * result + guest_nice;
        return result;
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
}
