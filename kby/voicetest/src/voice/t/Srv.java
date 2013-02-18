package voice.t;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;

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
	
	public ServerSocket	server	= null; 
	public Socket 		client	= null;
	
	public Receiver		receiver	= null;
	public Sender		sender		= null;
	
	public BufferedInputStream		bis;
	public BufferedOutputStream		bos;
	
	public Handler		mainHandler	= null;
		
	public Srv(AudioTrack audioTrack, AudioRecord audioRecord, int bufferSize, Handler mainHandler){
		
		this.audioTrack		= audioTrack;
		this.audioRecord	= audioRecord;
		this.bufferSize		= bufferSize;
		this.mainHandler	= mainHandler;
			
		
		try {
			server = new ServerSocket(Addr.port);			
			sendMsgToMainActivity("서버 생성 성공 : "+Utils.getLocalIpAddress(1));
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		while(true){
			acceptClient();	// 클라이언트를 받도록
		}
	}
	
	private void acceptClient(){
		try {
			client = server.accept();
			sendMsgToMainActivity("클라이언트 Accept :"+client.getInetAddress());
			
			bis	= new BufferedInputStream(client.getInputStream());
			bos = new BufferedOutputStream(client.getOutputStream());
					
			// Sender, Receiver 실행
			sender		= new Sender(bos, audioRecord, bufferSize);
			sender		.setDaemon(true);
			sender		.start();
			
			receiver	= new Receiver(bis, audioTrack, bufferSize);
			receiver	.setDaemon(true);
			receiver	.start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendMsgToMainActivity(String msg){
		Message retmsg = Message.obtain(mainHandler, 0, msg);	
		mainHandler.sendMessage(retmsg);
	}
}
