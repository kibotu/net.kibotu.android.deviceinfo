package net.kibotu.android.deviceinfo.library.cpu;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class Core {

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

    /**
     * computed difference between previous and current core usage
     */
    public float usage;

    public int total() {
        return user + nice + system + idle + iowait + irq + softirq + steal + guest + guest_nice;
    }

    public int getUser() {
        return user;
    }

    public Core setUser(int user) {
        this.user = user;
        return this;
    }

    public int getNice() {
        return nice;
    }

    public Core setNice(int nice) {
        this.nice = nice;
        return this;
    }

    public int getSystem() {
        return system;
    }

    public Core setSystem(int system) {
        this.system = system;
        return this;
    }

    public int getIdle() {
        return idle;
    }

    public Core setIdle(int idle) {
        this.idle = idle;
        return this;
    }

    public int getIowait() {
        return iowait;
    }

    public Core setIowait(int iowait) {
        this.iowait = iowait;
        return this;
    }

    public int getIrq() {
        return irq;
    }

    public Core setIrq(int irq) {
        this.irq = irq;
        return this;
    }

    public int getSoftirq() {
        return softirq;
    }

    public Core setSoftirq(int softirq) {
        this.softirq = softirq;
        return this;
    }

    public int getSteal() {
        return steal;
    }

    public Core setSteal(int steal) {
        this.steal = steal;
        return this;
    }

    public int getGuest() {
        return guest;
    }

    public Core setGuest(int guest) {
        this.guest = guest;
        return this;
    }

    public int getGuest_nice() {
        return guest_nice;
    }

    public Core setGuest_nice(int guest_nice) {
        this.guest_nice = guest_nice;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Core core = (Core) o;

        if (user != core.user) return false;
        if (nice != core.nice) return false;
        if (system != core.system) return false;
        if (idle != core.idle) return false;
        if (iowait != core.iowait) return false;
        if (irq != core.irq) return false;
        if (softirq != core.softirq) return false;
        if (steal != core.steal) return false;
        if (guest != core.guest) return false;
        if (guest_nice != core.guest_nice) return false;
        return Float.compare(core.usage, usage) == 0;

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
        result = 31 * result + (usage != +0.0f ? Float.floatToIntBits(usage) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Core{" +
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
                ", usage=" + usage +
                '}';
    }
}
