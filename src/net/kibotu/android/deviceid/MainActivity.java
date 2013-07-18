package net.kibotu.android.deviceid;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static Activity mActivity;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActivity = this;
		final Button bt = (Button) findViewById(R.id.button1);
		final TextView idView = (TextView) findViewById(R.id.textView1);
		idView.setMovementMethod(new ScrollingMovementMethod());
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
										
					// http://developer.apple.com/library/ios/#documentation/3DDrawing/Conceptual/OpenGLES_ProgrammingGuide/DeterminingOpenGLESCapabilities/DeterminingOpenGLESCapabilities.html
					idView.append("GL_VERSION : " + getOpenGLVersion() + "\n"); 
					idView.append("getVersionFromPackageManager : " + getVersionFromPackageManager() + "\n"); 
					idView.append("supportsOpenGLES2 : " + supportsOpenGLES2() + "\n"); 
					idView.append("GL_MAX_TEXTURE_SIZE : " + glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE) + "\n"); 
					idView.append("GL_DEPTH_BITS : " + glGetIntegerv(GL10.GL_DEPTH_BITS) + "\n"); 
					idView.append("GL_STENCIL_BITS : " + glGetIntegerv(GL10.GL_STENCIL_BITS) + "\n"); 
					idView.append("GL_MAX_VERTEX_ATTRIBS : " + glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS) + "\n"); 
					idView.append("GL_MAX_VERTEX_UNIFORM_VECTORS : " + glGetIntegerv(GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS) + "\n"); 
					idView.append("GL_MAX_FRAGMENT_UNIFORM_VECTORS : " + glGetIntegerv(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS) + "\n"); 
					idView.append("GL_MAX_VARYING_VECTORS : " + glGetIntegerv(GLES20.GL_MAX_VARYING_VECTORS) + "\n"); 
					idView.append("GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS : " + glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS) + "\n"); 
					idView.append("GL_MAX_TEXTURE_IMAGE_UNITS : " + glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS) + "\n"); 
					idView.append("GL_MAX_TEXTURE_UNITS : " + glGetIntegerv(GLES10.GL_MAX_TEXTURE_UNITS) + "\n"); 
					idView.append("GL_EXTENSIONS : \n" + getExtensions() + "\n"); 
					//idView.append("GL_MAX_CLIP_PLANES : " + glGetIntegerv(GL10.GL_MAX_CLIP_PLANES) + "\n"); 
					
					
					idView.append("IMEI No : " + UIDeviceId.getDeviceIdFromTelephonyManager() + "\n");
					idView.append("IMSI No : " + UIDeviceId.getSubscriberIdFromTelephonyManager() + "\n");
					idView.append("hwID: " + UIDeviceId.getSerialNummer() + "\n");
					idView.append("AndroidID: " + UIDeviceId.getAndroidId() + "\n");
					idView.append("MAC Adress (wlan0): " + UIDeviceId.getMACAddress("wlan0") + "\n");
					idView.append("MAC Adress (eth0): " + UIDeviceId.getMACAddress("eth0") + "\n");
					idView.append("IP4 Adress: " + UIDeviceId.getIPAddress(true) + "\n");
					idView.append("IP6 Adress: " + UIDeviceId.getIPAddress(false) + "\n");
					idView.append("BOARD: " + android.os.Build.BOARD + "\n");
					idView.append("BOOTLOADER: " + android.os.Build.BOOTLOADER + "\n");
					idView.append("BRAND: " + android.os.Build.BRAND + "\n");
					idView.append("CPU_ABI: " + android.os.Build.CPU_ABI + "\n");
					idView.append("CPU_ABI2: " + android.os.Build.CPU_ABI2 + "\n");
					idView.append("DEVICE: " + android.os.Build.DEVICE + "\n");
					idView.append("DISPLAY: " + android.os.Build.DISPLAY + "\n");
					idView.append("FINGERPRINT: " + android.os.Build.FINGERPRINT + "\n");
					idView.append("HARDWARE: " + android.os.Build.HARDWARE + "\n");
					idView.append("HOST: " + android.os.Build.HOST + "\n");
					idView.append("ID: " + android.os.Build.ID + "\n");
					idView.append("MANUFACTURER: " + android.os.Build.MANUFACTURER + "\n");
					idView.append("MODEL: " + android.os.Build.MODEL + "\n");
					idView.append("PRODUCT: " + android.os.Build.PRODUCT + "\n");
					idView.append("RADIO: " + android.os.Build.RADIO + "\n");
					idView.append("SERIAL: " + android.os.Build.SERIAL + "\n");
					idView.append("TAGS: " + android.os.Build.TAGS + "\n");
					idView.append("TIME: " + android.os.Build.TIME + "\n");
					idView.append("TYPE: " + android.os.Build.TYPE + "\n");
					idView.append("UNKNOWN: " + android.os.Build.UNKNOWN + "\n");
					idView.append("USER: " + android.os.Build.USER + "\n");
	//				idView.append("getRadioVersion: " + android.os.Build.getRadioVersion() + "\n");
					idView.append("CODENAME: " + android.os.Build.VERSION.CODENAME + "\n");
					idView.append("INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL + "\n");
					idView.append("RELEASE: " + android.os.Build.VERSION.RELEASE + "\n");
					idView.append("SDK: " + android.os.Build.VERSION.SDK + "\n");
					idView.append("SDK_INT: " + android.os.Build.VERSION.SDK_INT + "\n"); 
				} catch(Exception e) {
					Log.e("Device", e.getMessage());
				}
			}
		});
	}
	
	static IntBuffer size =  IntBuffer.allocate(1);
	
	private static int glGetIntegerv(int value) {
		size =  IntBuffer.allocate(1);
		GLES10.glGetIntegerv(value,size);
		return size.get(0);
	}
	private static int getOpenGLVersion() {
		final ActivityManager activityManager = (ActivityManager)mActivity.getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		return configurationInfo.reqGlEsVersion;
	}
	
	private static boolean supportsOpenGLES2() {
		return getOpenGLVersion() >= 0x20000;
	}
	
	private static int getVersionFromPackageManager() {
	    PackageManager packageManager = mActivity.getPackageManager();
	    FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
	    if (featureInfos != null && featureInfos.length > 0) {
	        for (FeatureInfo featureInfo : featureInfos) {
	            // Null feature name means this feature is the open gl es version feature.
	            if (featureInfo.name == null) {
	                if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
	                    return getMajorVersion(featureInfo.reqGlEsVersion);
	                } else {
	                    return 1; // Lack of property means OpenGL ES version 1
	                }
	            }
	        }
	    }
	    return 1;
	}

	/** @see FeatureInfo#getGlEsVersion() */
	private static int getMajorVersion(int glEsVersion) {
	    return ((glEsVersion & 0xffff0000) >> 16);
	}
	
	public String getExtensions() {
		return GLES10.glGetString(GL10.GL_EXTENSIONS);
	}
}