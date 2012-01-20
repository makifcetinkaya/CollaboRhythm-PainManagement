package Collaborhythm.Android.BluetoothTest;

import java.io.OutputStream;
import java.util.Set;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BluetoothTestService extends Service{

	public static Set<BluetoothDevice> discoveredBTDevices;
	public static Set<BluetoothDeviceHook> deviceHooks;
	private BluetoothAdapter mBluetoothAdapter;
	
	private boolean mBluetoothSupported;
	
	public static String BROADCAST_ACTION = "Collaborhythm.Android.BluetoothTest.NEW_LOG";
	private static final String ARDUINO_BT_MAC= "00:07:80:90:8A:F5";
	
	private Intent mLogIntent;
	private OutputStream mOutputStream;
	private Thread mInputStreamThread;
	
	
//	public Handler mInputStreamHandler = new Handler(){
//		public void handleMessage(Message msg){
//			int event = (Integer) msg.getData().get("event");
//			Log.d("BT", "received message from IRT: "+ event);
//			switch(event){
//			case PKT_ACK:
//				int pktNum = (Integer) msg.getData().get("pktNum");
//				AckRunnable r = new AckRunnable(pktNum);
//				r.run();
//				break;
//			case SYNCH_REQ:
//				mSynchRunnable.run();
//				break;
//			default:
//				int boxNum = (Integer) msg.getData().get("boxNum");
//				int timestamp = (Integer) msg.getData().get("timestamp"); 
//				Log.d("BT", "timestamp from IRT: "+timestamp);
//				handleEvent(boxNum, event, timestamp);				
//			}	
//		}
//	};
//	
//	
//	public Handler mConnectionHandler = new Handler(){
//		public void handleMessage(Message msg){
//			//Log.d("BT", "incoming message!");
//			switch(msg.what){
//				case 1:
//					Log.d("BT", "arduino is connected now");
//					ClientConnectThread.mBluetoothSocket = (BluetoothSocket) msg.obj;
//					startInputStreamThread();
//			}
//		}
//	};
//	private Runnable mSynchRunnable = new Runnable(){
//		private static final int OUTGOING_MSG_LENGTH = 6;
//		private static final char PKT_START_CHAR = '+';
//		private static final int SYNCH=2;
//		@Override
//		public void run() {
//			
//			byte[] buffer = new byte[OUTGOING_MSG_LENGTH];
//			buffer[0] = PKT_START_CHAR;
//			buffer[1] = SYNCH;
//			setBufferTimestamp(buffer);
//			try {
//				mOutputStream.write(buffer);
//			} catch (IOException e) {
//				Log.d("BT", "problem writing to output stream");
//			}			
//		}
//	};
//	private ClientConnectThread mClientConnectThread;
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	

	@Override	
	public void onCreate(){
		mLogIntent = new Intent(BROADCAST_ACTION);
		//mInputStreamHandler.removeCallbacks();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            Log.d("BT", "bluetooth not supported");
            mBluetoothSupported = false;
        } else
        {
            mBluetoothSupported = true;
            //mReceiver = new DeviceFoundBroadcastReceiver();
        }
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		
		if(mBluetoothSupported){
			Log.d("BT","Bluetooth supported..");
			ServerConnectThread s = new ServerConnectThread();
			s.start();
//			BluetoothDevice dev = getArduinoBT();
//			if(dev != null){
//				mClientConnectThread = new ClientConnectThread(mConnectionHandler, dev);
//				mClientConnectThread.start();
//				try {
//					mOutputStream = ClientConnectThread.mBluetoothSocket.getOutputStream();
//				} catch (IOException e1) {
//					Log.d("BT", "problem getting outputstream");
//				}
//			}else{
			
//				Log.d("BT", "Arduino is not paired, bro!");
//			}
		}
		return startId;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void register(BluetoothDeviceHook bluetoothDeviceHook) {
		deviceHooks.add(bluetoothDeviceHook);		
	}

//	private BluetoothDevice getArduinoBT() {
//		BluetoothDevice arduinoBT = null;
//		for(BluetoothDevice dev: mBluetoothAdapter.getBondedDevices()){
//			if(dev.getAddress().endsWith(ARDUINO_BT_MAC)){
//				arduinoBT = dev;
//			}
//		}
//		return arduinoBT;
//	}

	
//	public void startInputStreamThread(){
//		InputStream s;
//		try {
//			s = ClientConnectThread.mBluetoothSocket.getInputStream();
//			mInputStreamThread = new InputStreamThread(mInputStreamHandler, s);
//			mInputStreamThread.start();
//		} catch (IOException e) {
//			Log.d("BT","problem getting input stream");			
//		}
//	}
//	
//
//	public void handleEvent(int boxNum, int event, double timestamp){
//		mLogIntent.putExtra("boxNum", boxNum);
//		mLogIntent.putExtra("event", event);
//		mLogIntent.putExtra("timestamp", timestamp);
//		this.sendBroadcast(mLogIntent);
//	}
//
//
//	
//	public void onDestroy(){
//		if(mClientConnectThread.isAlive()){
//			mClientConnectThread.interrupt();
//		}
//		if(mInputStreamThread.isAlive()){
//			mInputStreamThread.interrupt();
//		}
//	}
//	
//	class AckRunnable implements Runnable{
//
//		private byte pkt;
//		public AckRunnable(int pktNum){
//			pkt = (byte) pktNum;
//		}
//		@Override
//		public void run() {
//			Log.d("BT", "sending pkt ack msg..");
//			byte[] buffer = new byte[3];
//			buffer[0] = PKT_START_CHAR;
//			buffer[1] = PKT_ACK;
//			buffer[2] = pkt;
//			try {
//				mOutputStream.write(buffer);
//			} catch (IOException e) {
//				Log.d("BT", "problem writing to output stream");
//			}
//			
//		}
//		
//	}
//	
//
//	private void setBufferTimestamp(byte[] buffer){
//		int time = (int) ((long) System.currentTimeMillis()/1000);
//		Log.d("BT", "current secs: "+time);
//		int part1 = time >>> 24;
//		time = time << 8;
//		int part2 = time >>> 24;
//		time = time << 8;
//		int part3 = time >>> 24;
//		time = time << 8;
//		int part4 = time >>> 24;
//		Log.d("BT", "part1: "+part1);
//		Log.d("BT", "part2: "+part2);
//		Log.d("BT", "part3: "+part3);
//		Log.d("BT", "part4: "+part4);
//		buffer[2] = (byte) part1;
//		buffer[3] = (byte) part2; 
//		buffer[4] = (byte) part3;
//		buffer[5] = (byte) part4;
//	}
//	
	
}
