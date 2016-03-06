package net.kibotu.android.deviceinfo.ui.cpu;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class CpuFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_cpu);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

//        final LinearLayout lCores = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);
//        CPU.threads.add(cachedList.addItem("CPU Utilization", "Output from /proc/stat.", 1f, true, new DeviceInfoItemAsync(0) {
//            @Override
//            protected void async() {
//
//                customView = lCores;
//                useHtml = true;
//
//                final float[] cpuUsages = Cpu.getCpuUsage();
//
//                keys = "Cores: " + BR;
//                value = cores + BR;
//                keys += "Utilization All Cores:" + BR;
//                value += formatPercent(cpuUsages[0]) + BR;
//
//                for (int i = 1; i < cores + 1; ++i) {
//                    if (cpuUsages.length <= i) break;
//                    final float usage = cpuUsages[i];
//                    keys += "Utilization Core " + i + BR;
//                    value += usage <= 0.01f ? "Idle" : Utils.formatPercent(usage) + BR;
//                }
//            }
//        }));
//
//        final LinearLayout lFreq = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);
//        CPU.threads.add(cachedList.addItem("Frequency", "Output cpuinfo_max_freq, cpuinfo_min_freq and scaling_cur_freq from /sys/devices/system/cpu/cpu0/cpufreq/.", 1f, true, new DeviceInfoItemAsync(cores + 2) {
//            @Override
//            protected void async() {
//
//                customView = lFreq;
//                useHtml = true;
//
//                keys = "Max:" + BR;
//                value = formatFrequency(Cpu.getMaxCpuFreq()) + BR;
//                keys += "Min:" + BR;
//                value += formatFrequency(Cpu.getMinCpuFreq()) + BR;
//                keys += "Current:" + BR;
//                value += formatFrequency(Cpu.getCurrentCpuFreq());
//            }
//        }));
//
//        cachedList.addItem("CPU Details", "Output /proc/cpuinfo.", new DeviceInfoItemAsync(cores + 5) {
//            @Override
//            protected void async() {
//                value = DeviceOld.getCpuInfo();
//            }
//        });
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.cpu;
    }
}
