package voice.t;

import java.io.BufferedInputStream;
import java.io.IOException;

import android.media.AudioTrack;
import android.util.Log;

/**
 * 음성 수신 및 재생을 수행
 * @author kyobum
 *
 */
public class Receiver extends Thread{
	public BufferedInputStream 	bis;
	public AudioTrack			audioTrack;
	public int					bufferSize;
	public byte[]				buffer;
	
	public Receiver(BufferedInputStream bis, AudioTrack audioTrack,
				int bufferSize){
		this.bis		= bis;
		this.audioTrack	= audioTrack;
		this.bufferSize = bufferSize;
		this.buffer		= new byte[bufferSize];		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		audioTrack.play();
		try {
			while(bis.read(buffer)!=-1){
				Log.i("CHECK", "play");
				audioTrack.write(buffer, 0, bufferSize);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
