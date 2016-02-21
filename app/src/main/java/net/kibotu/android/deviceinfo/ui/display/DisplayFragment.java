package net.kibotu.android.deviceinfo.ui.display;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

import java.util.List;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class DisplayFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_display);
    }
}
