package voice.tUDP;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


import android.media.AudioRecord;
import android.util.Log;

/**
 * 녹음 및 전송을 수행
 * @author kyobum
 *
 */
public class Sender extends Thread{
	public AudioRecord				audioRecord;
	public int 						bufferSize;
	public byte[]					buffer;
	
	public DatagramPacket			packet;	
	public DatagramSocket			socket;
	
	public InetAddress				addr;
	
	
	public Sender(InetAddress addr, AudioRecord audioRecord, int bufferSize){
		//this.bos			= bos;
		
		this.addr			= addr;
		this.audioRecord	= audioRecord;
		this.bufferSize		= bufferSize;
		this.buffer			= new byte[bufferSize];
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try {
			socket			= new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		audioRecord.startRecording();		
		
		while (true) {			
			int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
			
			int i = 0;
			for(; i<bufferReadResult; i+=bufferReadResult/8){
				packet = new DatagramPacket(buffer, i, bufferReadResult/8, addr, Addr.port);				
				
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
			
			try {
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
		
	}
	
	

}
