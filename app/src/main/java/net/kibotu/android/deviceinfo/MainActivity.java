package net.kibotu.android.deviceinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import net.kibotu.android.deviceinfo.ui.menu.IMainMenu;
import net.kibotu.android.deviceinfo.ui.menu.IMenuProvider;
import net.kibotu.android.deviceinfo.ui.menu.MainMenu;

import static net.kibotu.android.deviceinfo.ui.FragmentProvider.showBuildConfigFragment;

public class MainActivity extends AppCompatActivity implements IMenuProvider {

    IMainMenu mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep the screen always on
        if (BuildConfig.DEBUG)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getMainMenu().setMenuItems(MainMenuFactory.createMenu());

        showBuildConfigFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMainMenu().prepareDrawers();
    }

    @Override
    public IMainMenu getMainMenu() {
        if (mainMenu == null)
            mainMenu = new MainMenu();
        return mainMenu;
    }
}
