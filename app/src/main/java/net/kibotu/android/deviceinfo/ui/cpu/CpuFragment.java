package net.kibotu.android.deviceinfo.ui.cpu;

import android.support.v7.widget.RecyclerView;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.cpu.Core;
import net.kibotu.android.deviceinfo.library.cpu.Cpu;
import net.kibotu.android.deviceinfo.library.cpu.CpuUpdateListener;
import net.kibotu.android.deviceinfo.library.cpu.CpuUsageReceiver;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.ViewHelper;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.kibotu.android.deviceinfo.ui.ViewHelper.formatPercent;
import static net.kibotu.android.deviceinfo.ui.ViewHelper.getFormattedTimeDifference;

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

    @NotNull
    @Override
    protected RecyclerView.Adapter injectAdapterAnimation(RecyclerView.Adapter adapter) {
        // overriding default behaviour, so we don't inject an animation adapter
        return adapter;
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
                        .addChild(new ListItem().setLabel("Cores").setValue(Cpu.getAmountCores()))
                        .addChild(new ListItem().setLabel("Utilization All Cores").setValue(formatPercent(cpu.getAllCores().usage / 100)));

                final ArrayList<Core> cores = cpu.getCores();
                for (int i = 0, coresSize = cores.size(); i < coresSize; i++) {
                    Core core = cores.get(i);
                    item.addChild(new ListItem().setLabel("Utilization Core " + (i + 1)).setValue(core.usage <= 0.01f
                            ? "Idle"
                            : formatPercent(core.usage / 100)));
                }

                item
                        .addChild(new ListItem().setLabel("Currently Running Processes").setValue(cpu.getProcs_running()))
                        .addChild(new ListItem().setLabel("Currently blocked Processes").setValue(cpu.getProcs_blocked()))
                        .addChild(new ListItem().setLabel("Booted").setValue(getFormattedTimeDifference(cpu.getBtimeAsEpoch() * 1000L)))
                        .addChild(new ListItem().setLabel("Context switches across all CPUs").setValue(cpu.getCtxt()))
                        .addChild(new ListItem().setLabel("Processes and threads created").setValue(cpu.getProcesses()));

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
