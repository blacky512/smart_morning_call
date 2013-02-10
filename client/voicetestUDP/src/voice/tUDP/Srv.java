package voice.tUDP;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.impl.conn.tsccm.WaitingThread;

import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 서버 모드 선택시 실행되는 서버 스레드.
 * 내부에서 Receiver(클라이언트로부터 음성을 수신)스레드와 
 * Sender(클라이언트로 음성을 송신) 스레드를 시작시킴.
 * @author kyobum
 *
 */
public class Srv extends Thread{
	public AudioTrack	audioTrack;
	public AudioRecord	audioRecord;	
	public int			bufferSize;
	
	public DatagramSocket	server		= null;
	//public DatagramSocket	client		= null;
	public DatagramPacket	connPacket	= null;
		
	public Receiver		receiver	= null;
	public Sender		sender		= null;
		
	public Handler		mainHandler	= null;
	
	
	///////UDP
	public byte[]	buffer	= null;
		
	public Srv(AudioTrack audioTrack, AudioRecord audioRecord, int bufferSize, Handler mainHandler){
		
		this.audioTrack		= audioTrack;
		this.audioRecord	= audioRecord;
		this.bufferSize		= bufferSize;
		this.mainHandler	= mainHandler;
		
		this.buffer			= new byte[bufferSize];
		this.connPacket		= new DatagramPacket(buffer, bufferSize);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		
		
		try {
			server = new DatagramSocket(Addr.port);
			//client = new DatagramSocket();
			sendMsgToMainActivity("서버 생성 성공");
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		chat();
	}
	
	private void chat(){
		
		boolean	conn = false;
		
		while(!conn){
			try {
				server.receive(connPacket);
				conn = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		receiver	= new Receiver(server, audioTrack, bufferSize);
		receiver	.setDaemon(true);
		receiver	.start();				
				
		sender		= new Sender(connPacket.getAddress(), audioRecord, bufferSize);
		sender		.setDaemon(true);
		sender		.start();
		
		
		while( (Thread.State.TERMINATED != receiver.getState()) &&
				(Thread.State.TERMINATED != sender.getState())){
			
		}
		
	}
	
	
	private void sendMsgToMainActivity(String msg){
		Message retmsg = Message.obtain(mainHandler, 0, msg);	
		mainHandler.sendMessage(retmsg);
	}
}
