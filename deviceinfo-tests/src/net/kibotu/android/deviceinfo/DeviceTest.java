package net.kibotu.android.deviceinfo;

import net.kibotu.android.error.tracking.LogcatLogger;
import net.kibotu.android.error.tracking.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeviceTest extends ActivityInstrumentationImpl {

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // api 19 tests fail without setting it @see http://stackoverflow.com/questions/12267572/mockito-dexmaker-on-android
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        Logger.init(new LogcatLogger(getActivity()));
        Device.init(getActivity());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testInit() throws Exception {
        try {
            Device.context();
            assertTrue(true);
        } catch (final Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testSomething() throws Exception {

    }
}
