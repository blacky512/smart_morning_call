package voice.t;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
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
		 
	public Socket 		client	= null;
	
	public Receiver		receiver	= null;
	public Sender		sender		= null;
	
	public BufferedInputStream		bis;
	public BufferedOutputStream		bos;
	
	public Cli(AudioTrack audioTrack, AudioRecord audioRecord, int bufferSize){
		this.audioTrack		= audioTrack;
		this.audioRecord	= audioRecord;
		this.bufferSize		= bufferSize;		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try {
			client	= new Socket(Addr.host, Addr.port);
			//@@@ 재시도하도록 수정해야함
			
			bis	= new BufferedInputStream(client.getInputStream());
			bos = new BufferedOutputStream(client.getOutputStream());
			
			// Sender, Receiver 실행
			sender		= new Sender(bos, audioRecord, bufferSize);
			sender		.setDaemon(false);
			sender		.start();
			
			receiver	= new Receiver(bis, audioTrack, bufferSize);
			receiver	.setDaemon(false);
			receiver	.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
