package net.kibotu.android.deviceinfo.ui.cpu;

import android.support.v7.widget.RecyclerView;
import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.library.cpu.Core;
import net.kibotu.android.deviceinfo.library.cpu.Cpu;
import net.kibotu.android.deviceinfo.library.cpu.CpuUpdateListener;
import net.kibotu.android.deviceinfo.library.cpu.CpuUsageReceiver;
import net.kibotu.android.deviceinfo.model.ListItem;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static net.kibotu.android.deviceinfo.ui.ViewHelper.*;

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

        cpuUsageReceiver = new CpuUsageReceiver();

        addFrequency();

        addCoreUtilization();

        addMisc();

        addCpuInfo();

        // updating adapter
        cpuUsageReceiver.addCpuUpdateListener(new CpuUpdateListener() {
            @Override
            protected void update(Cpu cpu) {
                notifyDataSetChanged();
            }
        }).registerReceiver();
    }

    private void addCpuInfo() {
        final ListItem item = new ListItem().setLabel("CPU Details").setDescription("Output /proc/cpuinfo.");

        for (Map.Entry<String, String> entry : CpuUsageReceiver.getCpuInfo().entrySet())
            item.addChild(new ListItem().setLabel(entry.getKey()).setValue(entry.getValue()));

        addSubListItem(item);
    }

    private void addFrequency() {
        final ListItem item = new ListItem().setLabel("Frequency").setDescription("Output cpuinfo_max_freq, cpuinfo_min_freq and scaling_cur_freq from /sys/devices/system/cpuUsageReceiver/cpu0/cpufreq/.");
        cpuUsageReceiver.addCpuUpdateListener(new CpuUpdateListener() {
            @Override
            protected void update(Cpu cpu) {
                item.clear()
                        .addChild(new ListItem().setLabel("Max").setValue(formatFrequency(CpuUsageReceiver.getMaxCpuFreq())))
                        .addChild(new ListItem().setLabel("Min").setValue(formatFrequency(CpuUsageReceiver.getMinCpuFreq())))
                        .addChild(new ListItem().setLabel("Current").setValue(formatFrequency(CpuUsageReceiver.getCurrentCpuFreq())));
            }
        });
        addSubListItem(item);
    }

    private void addCoreUtilization() {
        // create new cpu utilization card
        final ListItem item = new ListItem().setLabel("Core Utilization").setDescription("Output from /proc/stat.");
        addSubListItem(item);

        // register for cpu update events
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
            }
        });
    }

    private void addMisc() {
        // create new cpu utiliyation card
        final ListItem item = new ListItem().setLabel("Misc").setDescription("Output from /proc/stat.");
        addSubListItem(item);

        // register for cpu update events
        cpuUsageReceiver.addCpuUpdateListener(new CpuUpdateListener() {
            @Override
            protected void update(Cpu cpu) {

                item.clear()
                        .addChild(new ListItem().setLabel("Booted").setValue(getFormattedTimeDifference(cpu.getBtimeAsEpoch() * 1000L)))
                        .addChild(new ListItem().setLabel("Currently running processes").setValue(cpu.getProcs_running()))
                        .addChild(new ListItem().setLabel("Currently blocked processes").setValue(cpu.getProcs_blocked()))
                        .addChild(new ListItem().setLabel("Context switches across all cores").setValue(cpu.getCtxt()))
                        .addChild(new ListItem().setLabel("Processes and threads created").setValue(cpu.getProcesses()));
            }
        });
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.cpu;
    }
}
