package net.kibotu.android.deviceinfo.ui.java;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class JavaFragment extends ListFragment {

    @Override
    public String getTitle() {
        return getString(R.string.menu_item_java);
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.java;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final StringWriter strOut = new StringWriter();
        final PrintWriter out = new PrintWriter(strOut);

        final Properties props = System.getProperties();
        props.list(out);

        final String[] lines = strOut.toString().trim().split("\\r?\\n");

        for (final String line : lines) {
            final String[] split = line.split("=");
            final String key = split.length > 1 ? split[0] : "";
            final String value = split.length > 1 ? split[1] : "";
            addVerticallyCard(key, value, "");
        }
    }
}
