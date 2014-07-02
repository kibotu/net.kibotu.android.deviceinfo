package net.kibotu.android.deviceinfo;

import com.wooga.sbs.error.tracking.android.SBSErrorTracking;
import com.wooga.sbs.error.tracking.android.intern.utils.CalenderService;
import com.wooga.sbs.error.tracking.android.intern.utils.JSONUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static com.wooga.sbs.error.tracking.android.Constants.*;
import static com.wooga.sbs.error.tracking.android.intern.SBSErrorTrackingCore.shared;
import static org.mockito.Mockito.*;

public class BlaTest extends ActivityInstrumentationImpl {

    private String metricPayload;
    private String device;
    private String app;
    private String sbs;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // api 19 tests fail without setting it @see http://stackoverflow.com/questions/12267572/mockito-dexmaker-on-android
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        // stub
        final CalenderService calenderService = mock(CalenderService.class);

        // when
        when(calenderService.getTime()).thenReturn(1404227980);

        SBSErrorTracking.InitErrorTracking(getActivity(), GAME_ID, USER_ID);
        SBSErrorTracking.SetSBSDeviceId(DEVICE_ID);
        //shared.getSbsInfo().trackingId = "12312";

        assertTrue(USER_ID.equals(SBSErrorTrackingCore.shared.getSbsInfo().userId));
        assertTrue(DEVICE_ID.equals(SBSErrorTrackingCore.shared.getSbsInfo().deviceId));

        setVars();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private void setVars() {
        device =  "\"device\":{\n" +
//                "        \"id\":\"e58d5c80-2775-4707-aa77-9106ba0f93a4\",\n" +
                "        \"model\":\"" + Device.getOsBuildModel() + "\",\n" +
                "        \"physicalRamSize\":" + Device.totalMemoryAvailable() + ",\n" +
                "        \"totalMemory\":" + Device.getTotalMemoryByEnvironment() + ",\n" +
                "        \"osVersion\":\"" + Device.getReleaseVersion() + "\",\n" +
                "        \"apiLevel\":" + Device.getApiLevel() + ",\n" +
                "        \"manufacturer\":\"" + Device.getManufacturer() + "\",\n" +
                "        \"jailbroken\":" + Device.checkIsRooted() + ",\n" +
                "        \"locale\":\"" + Device.getLocale() + "\",\n" +
                "        \"screenResolution\":\"" + Device.getResolution() + "\",\n" +
                "        \"osName\":\"" +  Device.getOsName() + "\",\n" +
                "        \"screenDensity\":" + Device.getScreenDensity() + ",\n" +
                "        \"isRetina\":false\n" +
                "    }";

        app = "\"app\": {" +
                // "        # mandatory. Marketing version. CFBundleShortVersionString on iOS\n" +
                "        \"version\" : \"" + Device.getAppversion() + "\",\n" +
                // "        # optional. CFBundleVersion on iOS\n" +
                "        \"technicalVersion\" : \"" + Device.getAppVersionCode() + "\",\n" +
                // "        # optional. CFBundleIdentifier on iOS\n" +
                "        \"bundleId\" : \"" + Device.getPackageName() + "\"\n" +
                "   }";

        sbs = "\"sbsInfo\": {" +
                "        \"gameId\": \"" + GAME_ID + "\",\n" +
                "        \"system\": \"" + SYSTEM  + "\",\n" +
                "        \"deviceId\": \"" + DEVICE_ID + "\",\n" +
                "        \"userId\": \"" + USER_ID +"\"\n" +
                "    }";

        // region metric payload

        // based on: https://github.com/wooga/sbs-error-tracking-api/blob/master/doc/metrics.json
        metricPayload = "{\n" +
                "    \"notifierVersion\": \"" + SBSErrorTrackingCore.NOTIFER_VERSION +  "\",\n" +
                sbs + ",\n" +
                // "    # in seconds since the first instant of 1 January 1970, GMT.\n" +
                "    \"createdAt\": " + CalenderService.getTimeInMillis() + ",\n" +
                device + ",\n" +
                app + ",\n" +
                "    \"userId\":\"" + USER_ID + "\"\n" +
                "}";
    }

    @Test
    public void testBuildMetricsPaylaod() throws Exception {

        // build payload
        final JSONObject json = PayloadBuilder.buildPayloadForStartRequest();

        // then
        assertNotNull(metricPayload);
        assertNotNull(json);
        JSONAssert.assertEquals(metricPayload, json, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    public void testBuildPayload() throws Exception {

        // stub appState
        final AppState appStateMock = mock(AppState.class);

        // when
        when(appStateMock.getDuration()).thenReturn(495L);
        when(appStateMock.getMemoryUsage()).thenReturn(18935512L);
        when(appStateMock.getInForeground()).thenReturn(false);
        when(appStateMock.getLowMemory()).thenReturn(false);
        PayloadBuilder.appState = appStateMock;

        // stub error
        final Error errorMock = mock(Error.class);
        final NoSuchMethodError exceptionMock = new NoSuchMethodError("Unable to connect to database.");
        final StackTraceElement stackTraceElement = new StackTraceElement(NoSuchMethodError.class.getCanonicalName(),"create",NoSuchMethodError.class.getSimpleName(),1234);
        exceptionMock.setStackTrace(new StackTraceElement[] { stackTraceElement });

        errorMock.tag = "error";
        errorMock.exception = exceptionMock;

        //region error call

        // based on https://github.com/wooga/sbs-error-tracking-api/blob/master/doc/api.json
        final String errorPayload = "{\n" +
                "    \"notifierVersion\": \"" + shared.NOTIFER_VERSION + "\",\n" +
                sbs + ",\n" +
                "    \"events\": [{\n" +
////                "        # in seconds since the first instant of 1 January 1970, GMT.\n" +
                "    \"createdAt\": " + CalenderService.getTimeInMillis() + ",\n" +
//                "\n" +
////                "        # optional, used to pair error that happens in different systems (eg FE/BE).\n" +
//                "        \"trackingId\": \"12312\",\n" + // deprecated
//                "\n" +
////                "        # mandatory, should be \"fatal\" (crashes), \"error\" (assertions) or \"warning\"\n" +
                "        \"severity\": \"error\",\n" +
//                "\n" +
                "        \"userId\": \"" + USER_ID + "\",\n" +
                app + ",\n" +
                "        \"errorType\": \"java.lang.NoSuchMethodError\",\n" +
                "        \"message\": \"Unable to connect to database.\",\n" +
                "        \"stacktrace\": [{\n" +
//                "\n" +
////                "            # optional make sure this is relative path in the project.\n" +
////                "            # We don't want different files from different devices/releases.\n" +
                "            \"file\": \"" + NoSuchMethodError.class.getSimpleName() + "\",\n" +
//                "\n" +
////                "            # optional\n" +
                "            \"lineNumber\": 1234,\n" +
                "            \"method\": \"java.lang.NoSuchMethodError.create\"\n" +
                "        }],\n" +
//                "\n" +
////                "        # This should be sent by ios if the stacktrace can't be parsed on the client\n" +
////                "        # Should not be used by anyone else\n" +
//                "        \"stacktraceRaw\": \"89213u-r58234tu=348u\",\n" +
//                "\n" +
////                "        # optional\n" +
////                "        # in descending order (last breadcrumb is on top)\n" +
////                "        # default buffer size is 20\n" +
                "        \"breadcrumbs\": [\n" +
                "            \"string\",\n" +
                "            \"string2\"\n" +
                "        ],\n" +
////                "        # The value of metaData can be anything and metadata is optional\n" +
                "        \"metaData\": {\n" +
                "            \"someData\": {\n" +
                "                \"key\": \"value\",\n" +
                "                \"setOfKeys\": {\n" +
                "                    \"key\": \"value\",\n" +
                "                    \"key2\": \"value\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
////                "        # This is optional, BE doesn't look at any specific field.\n" +
////                "        # It is recommended to use these field name if the data is available\n" +
                "        \"appState\":{\n" +
                "           \"memoryUsage\":18935512,\n" +
                "           \"duration\":495,\n" +
                "           \"lowMemory\":false,\n" +
                "           \"inForeground\":false\n" +
                "        },\n" +
//                "        # This is optional, BE doesn't look at any specific field.\n" +
//                "        # It is recomended to use these field name if the data is available\n" +
                device + "\n" +
                "    }]\n" +
                "}";
        //endregion

        // build payload
        SBSErrorTrackingCore.addBreadcrumb("string2");
        SBSErrorTrackingCore.addBreadcrumb("string");

        JSONObject metaData = JSONUtils.saveStringToJSONObject("{\n" +
                "        \"someData\": {\n" +
                "          \"key\": \"value\",\n" +
                "          \"setOfKeys\": {\n" +
                "            \"key\": \"value\",\n" +
                "            \"key2\": \"value\"\n" +
                "          }\n" +
                "        }\n" +
                "      }");
        errorMock.metaData = metaData;

        final JSONObject json = PayloadBuilder.buildPayload(errorMock);

        // then
        assertNotNull(errorPayload);
        assertNotNull(json);
        JSONAssert.assertEquals(errorPayload, json, JSONCompareMode.NON_EXTENSIBLE);
    }
}
