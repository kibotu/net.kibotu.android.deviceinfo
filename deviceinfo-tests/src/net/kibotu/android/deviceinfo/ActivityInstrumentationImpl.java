package net.kibotu.android.deviceinfo;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class ActivityInstrumentationImpl extends ActivityInstrumentationTestCase2<UnitTestActivity> {

    @TargetApi(Build.VERSION_CODES.FROYO)
    public ActivityInstrumentationImpl() {
        super(UnitTestActivity.class.getCanonicalName(), UnitTestActivity.class);
    }
}
