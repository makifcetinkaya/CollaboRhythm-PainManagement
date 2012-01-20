package Collaborhythm.Android.BluetoothTest;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ClientConnectThread extends Thread {
	
	public static BluetoothSocket mBluetoothSocket;
	private Handler mConnectionHandler;
	private BluetoothDevice mBluetoothDevice;
	private static final UUID uuid =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public ClientConnectThread(Handler handler, BluetoothDevice dev){
		mBluetoothDevice = dev;
		mConnectionHandler = handler;

		try {
			mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("BT", "could not create bluetooth socket..");
		}
	}
	
	public void run(){
		int trial = 0;
		while(trial < 3){
			try{			
				mBluetoothSocket.connect();
				Log.d("BT", "socket connected..");
				trial = 5;
				handleConnectedSocket();
				break;
			}catch(IOException e){
				Log.d("BT", "client socket cannot connect, closing socket");
				trial++;
				try {
					mBluetoothSocket.close();
				} catch (IOException e1) {
					Log.d("BT", "problem closing socket");
					break;
				}			
			}
		}
	}

	private void handleConnectedSocket() {
		String MAC = mBluetoothSocket.getRemoteDevice().getAddress();
		Message msg = new Message();
		msg.obj = mBluetoothSocket;
		Log.d("BT","sending new dev msg");
		if (MAC.endsWith(ARDUINO_BT_MAC)){
			msg.what = 1;
			mConnectionHandler.sendMessage(msg);
		}else {
			Log.d("BT","Arduino could not be found");
		}
	}
}
