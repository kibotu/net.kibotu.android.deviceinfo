package net.kibotu.android.deviceinfo;

import android.annotation.TargetApi;
import android.os.Build;
import com.jayway.android.robotium.solo.Solo;
import org.junit.Test;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class ABTest extends ActivityInstrumentationImpl {

    private Solo solo;

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Test
    public void testAB() throws Exception {
        assertTrue(true);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
