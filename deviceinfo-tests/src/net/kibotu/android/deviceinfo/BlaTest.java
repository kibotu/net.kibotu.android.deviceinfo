package net.kibotu.android.deviceinfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlaTest extends ActivityInstrumentationImpl {

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // api 19 tests fail without setting it @see http://stackoverflow.com/questions/12267572/mockito-dexmaker-on-android
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

//        Logger.init(new LogcatLogger(getActivity()));
//        Logger.v("test");
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
//
    @Test
    public void testBuildPayload() throws Exception {

//        assertTrue(d.x == 100);
    }
}
