package Collaborhythm.Android.BluetoothTest;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final public class InputStreamThread extends Thread{
	
	private InputStream inpStream;
	private Handler inputReaderHandler;
	private static final int EVENT_SIZE  = 6;
	private static final int PKT_ACK = 7;
	private static final byte PKT_START_CHAR = '+';
	private static final byte SYNCH_REQ_CHAR = '#';

	public InputStreamThread(Handler handler, InputStream stream){
		Log.d("IRT", "starting input reader thread");
		inpStream = stream;
		inputReaderHandler = handler;
	}
	
	public void run(){
		Log.d("IRT", "reading char from input stream...");
			
		while(true){
			int b = getUnsignedByteFromStream();
			switch(b){
				case SYNCH_REQ_CHAR:
					handleSynchPkt();
					break;
				case PKT_START_CHAR:
					handleEventPkt();
					break;
				default:
					break;
			}
		}
	}
	
	
	private void handleSynchPkt() {
		handleMessage(-1,1,1);		
	}

	private void handleEventPkt() {
		int pktNumber = getUnsignedByteFromStream();
		int numOfEvents = getUnsignedByteFromStream();
		Log.d("IRT", "# events in pkt: "+numOfEvents);
		Log.d("IRT", "pkt num is: "+pktNumber);
		readEvents(numOfEvents);
		handleAckForPkt(pktNumber);
	}

	

	private void handleAckForPkt(int pktNumber){
		Bundle b = new Bundle();
		b.putInt("event", PKT_ACK);
		b.putInt("pktNum", pktNumber);
		Message message = new Message();
		message.setData(b);
		if(inputReaderHandler.sendMessage(message)){
			Log.d("IRT", "sent info to service");
		}else{
			Log.d("IRT", "couldn't send info to service");
		}
	}


	private void readEvents(int numOfEvents) {
		for(int i = 0; i<numOfEvents; i++){
			int boxNum = getUnsignedByteFromStream();
			//Log.d("IRT", "box num is: "+boxNum);
			int event = getUnsignedByteFromStream();
			//Log.d("IRT", "event is: "+event);
			int timestamp = getTimestamp();
			handleMessage(boxNum, event, timestamp);
		}		
	}

	/*
	 * Returns the timestamp, which is the number of seconds since January 1st, 1970.
	 */
	private int getTimestamp() {
		int timestamp = 0;
		for(int i=0; i<4; i++){
			int part = getUnsignedByteFromStream();
			timestamp += part << ((3-i)*8);			
		}
		return timestamp;
	}

	
	private void handleMessage(int boxNum, int event, int timestamp) {
		// TODO Auto-generated method stub
		Bundle b = new Bundle(3);
		b.putInt("boxNum", boxNum);
		Log.d("IRT", "the box num put into msg is "+boxNum);
		b.putInt("event", event);
		b.putInt("timestamp", timestamp);
		Message message = new Message();
		message.setData(b);
		if(inputReaderHandler.sendMessage(message)){
			Log.d("IRT", "sent info to service");
		}else{
			Log.d("IRT", "couldn't send info to service");
		}		
	}
	
	private int getUnsignedByteFromStream(){
		byte[] inp = new byte[1];
		try{
			inpStream.read(inp);
		}catch(IOException e){
			Log.d("IRT", "could not read update");
		}
		return inp[0]&0xFF;
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Log.d("IRT", "could not sleep");
		}				
	}
}
