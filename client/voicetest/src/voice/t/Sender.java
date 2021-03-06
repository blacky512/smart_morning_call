package voice.t;

import java.io.BufferedOutputStream;
import java.io.IOException;


import android.media.AudioRecord;
import android.util.Log;

/**
 * 녹음 및 전송을 수행
 * @author kyobum
 *
 */
public class Sender extends Thread{
	public BufferedOutputStream		bos;
	public AudioRecord				audioRecord;
	public int 						bufferSize;
	public byte[]					buffer;
	
	public Sender(BufferedOutputStream bos,AudioRecord audioRecord,	int bufferSize){
		this.bos			= bos;
		this.audioRecord	= audioRecord;
		this.bufferSize		= bufferSize;
		this.buffer			= new byte[bufferSize];
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		audioRecord.startRecording();
		while (true) {
			int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
			try {
				sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < bufferReadResult; i++)
				try {					
					bos.write(buffer[i]);
					//Log.i("CHECK","보냈음");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
	

}
