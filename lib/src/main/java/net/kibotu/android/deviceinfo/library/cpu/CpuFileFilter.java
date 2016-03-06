package net.kibotu.android.deviceinfo.library.cpu;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

class CpuFileFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        //Check if filename is "cpu", followed by a single digit number
        return Pattern.matches("cpu[0-9]+", pathname.getName());
    }
}