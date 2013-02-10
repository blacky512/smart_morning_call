package voice.tUDP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.media.AudioRecord;
import android.media.AudioTrack;

/**
 * 클라이언트 모드로 선택시 실행되는 클라이언트 스레드.
 * 내부에서 Receiver(서버로부터 음성을 수신)스레드와 
 * Sender(서버로 음성을 송신) 스레드를 시작시킴.
 * @author kyobum
 *
 */

public class Cli extends Thread {
	public AudioTrack	audioTrack;
	public AudioRecord	audioRecord;	
	public int			bufferSize;
		
	public DatagramSocket	client	= null;
	public DatagramPacket	connPacket	= null;
	
	public Receiver		receiver	= null;
	public Sender		sender		= null;
	
	public byte[] buffer = null;
	
	public Cli(AudioTrack audioTrack, AudioRecord audioRecord, int bufferSize){
		this.audioTrack		= audioTrack;
		this.audioRecord	= audioRecord;
		this.bufferSize		= bufferSize;		
		
		this.buffer			= new byte[bufferSize];
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		InetSocketAddress isa = new InetSocketAddress(Addr.host, Addr.port);
		
		try {
	
			// Sender, Receiver 실행
			sender		= new Sender(isa.getAddress(), audioRecord, bufferSize);
			sender		.setDaemon(false);
			sender		.start();
			
			
			client		= new DatagramSocket(Addr.port);
			
			receiver	= new Receiver(client, audioTrack, bufferSize);
			receiver	.setDaemon(false);
			receiver	.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while( (Thread.State.TERMINATED != receiver.getState()) &&
				(Thread.State.TERMINATED != sender.getState())){
			
		}
	}
}
