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
	//public BufferedInputStream 	bis;
	public AudioTrack			audioTrack;
	public int					bufferSize;
	public byte[]				buffer;
	
	public DatagramPacket		connPacket;
	public DatagramSocket		socket;
	
	/*
	public Receiver(BufferedInputStream bis, AudioTrack audioTrack,
				int bufferSize){
		this.bis		= bis;
		this.audioTrack	= audioTrack;
		this.bufferSize = bufferSize;
		this.buffer		= new byte[bufferSize];		
	}
	*/
	
	public Receiver(DatagramPacket connPacket, DatagramSocket socket, AudioTrack audioTrack,
			int bufferSize){
	//this.bis		= bis;
	this.audioTrack	= audioTrack;
	this.bufferSize = bufferSize;
	this.buffer		= new byte[bufferSize];
	
	this.connPacket	= connPacket;
	this.socket		= socket;
}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		DatagramPacket rcvPacket	= new DatagramPacket(buffer, bufferSize);
		
		audioTrack.play();
		try {
			//while(bis.read(buffer)!=-1){
			
			if(connPacket != null){
				audioTrack.write(connPacket.getData(), 0, bufferSize);
			}			
			
			while(true){
				socket.receive(rcvPacket);				
				Log.i("CHECK", "play");
				audioTrack.write(buffer, 0, bufferSize);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
