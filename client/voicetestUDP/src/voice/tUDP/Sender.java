package voice.tUDP;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


import android.media.AudioRecord;
import android.util.Log;

/**
 * 녹음 및 전송을 수행
 * @author kyobum
 *
 */
public class Sender extends Thread{
	//public BufferedOutputStream		bos;
	public AudioRecord				audioRecord;
	public int 						bufferSize;
	public byte[]					buffer;
	
	public DatagramPacket			connPacket;
	
	public DatagramSocket			dSocket;
	/*
	public Sender(BufferedOutputStream bos,AudioRecord audioRecord,	int bufferSize){
		this.bos			= bos;
		this.audioRecord	= audioRecord;
		this.bufferSize		= bufferSize;
		this.buffer			= new byte[bufferSize];
	}
	*/
	
	public Sender(DatagramPacket connPacket, DatagramSocket dSocket, AudioRecord audioRecord, int bufferSize){
		//this.bos			= bos;
		this.audioRecord	= audioRecord;
		this.bufferSize		= bufferSize;
		this.buffer			= new byte[bufferSize];
		
		this.connPacket		= connPacket;
		this.dSocket		= dSocket;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		DatagramPacket	sdPacket	= null;
		
		
		audioRecord.startRecording();
		
		if(connPacket.getAddress().toString() == "/"+Addr.host){ // 클라이언트인 경우
			audioRecord.read(buffer, 0, bufferSize);
			sdPacket = new DatagramPacket(buffer, bufferSize, connPacket.getAddress(), connPacket.getPort());
			try {
				dSocket.send(sdPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		
		}
		
		try {
			dSocket = new DatagramSocket();
			
			while (true) {			
				audioRecord.read(buffer, 0, bufferSize);
				sdPacket = new DatagramPacket(buffer, bufferSize, connPacket.getAddress(), connPacket.getPort());
				
				Log.i("CHECK", connPacket.getAddress()+":"+connPacket.getPort()+"..send");
				
				try {
					dSocket.send(sdPacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
	}
	
	

}
