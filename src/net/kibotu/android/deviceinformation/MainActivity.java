package net.kibotu.android.deviceinformation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import net.kibotu.android.deviceid.R;


public class MainActivity extends FragmentActivity {


    public static MainActivity mActivity;
    public boolean updateThreadIsRunning = true;

    public void onCreate(Bundle savedInstanceState) {
        // enable opengl calls
        Device.enableHardwareAcceleration(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Log.i("Device", "starting");
        mActivity = this;

        //startWebViewWithFaq2();
        //ExpensionHelper.checkExpension();
        //startTextureView();

        // startDeviceInfoUpdates();

        setContentView(R.layout.main);
    }

    @Override
    public void onPause() {
        super.onPause();
        updateThreadIsRunning = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        final ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Fragment getItem(int position) {
            return new InfoFragment(Device.generateDeviceInfoList(MainActivity.this));
        }
    }


}