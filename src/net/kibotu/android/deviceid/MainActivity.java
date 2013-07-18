package net.kibotu.android.deviceid;

import android.app.Activity;
import android.opengl.GLES20;
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
					idView.setText("");
					idView.append("GL_MAX_TEXTURE_SIZE : " + getTextureSize() + "\n"); 
					idView.append("IMEI No : " + UIDeviceId.getDeviceIdFromTelephonyManager() + "\n");
					idView.append("IMSI No : " + UIDeviceId.getSubscriberIdFromTelephonyManager() + "\n");
					idView.append("hwID: " + UIDeviceId.getSerialNummer() + "\n");
					idView.append("AndroidID: " + UIDeviceId.getAndroidId() + "\n");
	//				idView.append("MAC Adress: " + UIDeviceId.getMacAdress() + "\n");
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

			private int getTextureSize() {
				int [] size =  new int[1];
				GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE,size,0);
				return size[0];
			}
		});
	}
}