package net.kibotu.android.deviceinfo;

import java.util.Scanner;

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

    public static Cpu parseCpu(String cpuString){
        Cpu cpu = new Cpu();

        final Scanner s = new Scanner(cpuString);

        if(s.hasNext()) s.next();
        if(s.hasNextInt()) cpu.user = s.nextInt();
        if(s.hasNextInt()) cpu.nice = s.nextInt();
        if(s.hasNextInt()) cpu.system = s.nextInt();
        if(s.hasNextInt()) cpu.idle = s.nextInt();
        if(s.hasNextInt()) cpu.iowait = s.nextInt();
        if(s.hasNextInt()) cpu.irq = s.nextInt();
        if(s.hasNextInt()) cpu.softirq = s.nextInt();
        if(s.hasNextInt()) cpu.steal = s.nextInt();
        if(s.hasNextInt()) cpu.guest = s.nextInt();
        if(s.hasNextInt()) cpu.guest_nice = s.nextInt();

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
}