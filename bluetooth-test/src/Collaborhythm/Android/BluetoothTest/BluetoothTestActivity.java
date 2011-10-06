package Collaborhythm.Android.BluetoothTest;

import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BluetoothTestActivity extends Activity {
	
	public static TextView log;
    public static String BROADCAST_ACTION = "Collaborhythm.Android.BluetoothTest.NEW_LOG";
	private IntentFilter intentFilter;
	private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			updateUI(intent);
		}
	};
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	intentFilter = new IntentFilter(BROADCAST_ACTION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.registerReceiver(mUpdateReceiver, intentFilter);
		log = (TextView) findViewById(R.id.logView);
        
        Intent startTestIntent = new Intent(this, BluetoothTestService.class);
        this.startService(startTestIntent);
    }
    

	private void updateUI(Intent intent) {
		Log.d("BT", "updating UI");
		int boxNum = intent.getIntExtra("boxNum", 0);
		int event = intent.getIntExtra("event", 8);
		double timestamp = intent.getDoubleExtra("timestamp", 0);
		Date d = new Date((long) (timestamp*1000));
		
		StringBuilder u = new StringBuilder();
		u.append("b#:" + Integer.toString(boxNum)+ 
				" ,e#:"+Integer.toString(event) +" at "+ d.toString());
		log.append(u.toString()+"\n");
		
	}
    
	public void onDestroy(){
		this.unregisterReceiver(mUpdateReceiver);
	}
    
} 