package net.kibotu.android.deviceinfo.ui.cpu;

import com.common.android.utils.logging.Logger;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.cpu.Cpu;
import net.kibotu.android.deviceinfo.library.cpu.CpuUpdateListener;
import net.kibotu.android.deviceinfo.library.cpu.CpuUsageReceiver;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import static net.kibotu.android.deviceinfo.ui.ViewHelper.formatPercent;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class CpuFragment extends ListFragment {

    private CpuUsageReceiver cpuUsageReceiver;

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_cpu);
    }

    @Override
    public void onDestroyView() {
        cpuUsageReceiver.unregisterReceiver();
        super.onDestroyView();
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        // create new cpu card
        final ListItem item = new ListItem().setLabel("CPU Utilization").setDescription("Output from /proc/stat.");
        addSubListItem(item);

        // register for cpu update events
        cpuUsageReceiver = new CpuUsageReceiver();
        cpuUsageReceiver.addCpuUpdateListener(new CpuUpdateListener() {
            @Override
            protected void update(Cpu cpu) {

                item.clear()
                        .addChild(new ListItem().setLabel("Cores").setValue(cpu.getNumCores()))
                        .addChild(new ListItem().setLabel("Utilization All Cores").setValue(formatPercent(0)));

//                for (int i = 1; i < numCores + 1; ++i) {
//                    if (cpuUsages.length <= i) break;
//                    final float usage = cpuUsages[i];
//
//                    item.addChild(new ListItem().setLabel("Utilization Core").setValue(usage <= 0.01f
//                            ? "Idle"
//                            : formatPercent(usage)));


                notifyDataSetChanged();

            }
        }).registerReceiver();


//        final LinearLayout lFreq = (LinearLayout) LayoutInflater.from(context()).inflate(R.layout.tablewithtag, null);
//        CPU.threads.add(cachedList.addItem("Frequency", "Output cpuinfo_max_freq, cpuinfo_min_freq and scaling_cur_freq from /sys/devices/system/cpuUsageReceiver/cpu0/cpufreq/.", 1f, true, new DeviceInfoItemAsync(cores + 2) {
//            @Override
//            protected void async() {
//
//                customView = lFreq;
//                useHtml = true;
//
//                keys = "Max:" + BR;
//                value = formatFrequency(CpuUsageReceiver.getMaxCpuFreq()) + BR;
//                keys += "Min:" + BR;
//                value += formatFrequency(CpuUsageReceiver.getMinCpuFreq()) + BR;
//                keys += "Current:" + BR;
//                value += formatFrequency(CpuUsageReceiver.getCurrentCpuFreq());
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
//
//    private void updateWithCpu(Cpu cpu) {
//
//        ListItem item = new ListItem().setLabel("CPU Utilization").setDescription("Output from /proc/stat.")
//                .addChild(new ListItem().setLabel("Cores").setValue(cpu.getNumCores()))
//                .addChild(new ListItem().setLabel("Utilization All Cores").setValue(formatPercent(cpuUsages[0]) + ""));
//        for (int i = 1; i < numCores + 1; ++i) {
//            if (cpuUsages.length <= i) break;
//            final float usage = cpuUsages[i];
//
//            item.addChild(new ListItem().setLabel("Utilization Core").setValue(usage <= 0.01f
//                    ? "Idle"
//                    : formatPercent(usage)));
//        }
//    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.cpu;
    }
}
