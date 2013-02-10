package voice.tUDP;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.media.AudioTrack;
import android.util.Log;

/**
 * 음성 수신 및 재생을 수행
 * @author kyobum
 *
 */
public class Receiver extends Thread{	
	public AudioTrack			audioTrack;
	public int					bufferSize;
	public byte[]				buffer;
	
	public DatagramSocket		socket;
	public DatagramPacket		connPacket;
		
	/*
	public Receiver(BufferedInputStream bis, AudioTrack audioTrack,
				int bufferSize){
		this.bis		= bis;
		this.audioTrack	= audioTrack;
		this.bufferSize = bufferSize;
		this.buffer		= new byte[bufferSize];		
	}
	*/
	
	public Receiver(DatagramSocket socket, AudioTrack audioTrack, int bufferSize){
	
	this.audioTrack	= audioTrack;
	this.bufferSize = bufferSize;
	this.buffer		= new byte[bufferSize];
		
	this.socket		= socket;
}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		DatagramPacket rcvPacket	= new DatagramPacket(buffer, bufferSize);
		
		audioTrack.play();
		try {
						
			while(true){
				socket.receive(rcvPacket);				
				Log.i("CHECK", "play");
				audioTrack.write(rcvPacket.getData(), 0, rcvPacket.getData().length);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
