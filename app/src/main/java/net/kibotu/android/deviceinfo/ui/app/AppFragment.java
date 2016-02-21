package net.kibotu.android.deviceinfo.ui.app;

import net.kibotu.android.deviceinfo.R;
import net.kibotu.android.deviceinfo.ui.list.ListFragment;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class AppFragment extends ListFragment {

    @Override
    protected String getTitle() {
        return getString(R.string.menu_item_app);
    }
}
