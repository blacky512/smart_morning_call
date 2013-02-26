package whitepaper.smcall.voicechat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Voicechat {
	
	
    static final String LOG_TAG = "UdpStream";
   // static String	HOST = "";
    //static int AUDIO_PORT = 7771;
    static final int SAMPLE_RATE = 8000;
    static final int SAMPLE_INTERVAL = 20; 	// milliseconds
    static final int SAMPLE_SIZE = 2; 		// bytes per sample
    static final int BUF_SIZE = SAMPLE_INTERVAL*SAMPLE_INTERVAL*SAMPLE_SIZE*2;
    
    private Handler mainHandler;
    
    public Voicechat(String Host, int port, Handler mainHandler){
    	//HOST = Host;
    	//AUDIO_PORT = port;
    	this.mainHandler = mainHandler;
    }
    
    public SendThread sendThread;
    
    public void StopMicAudio() {
    	if(sendThread != null){
    		sendThread.kill();
    	}
    }
	public void SendMicAudio(InetAddress ia, int port) {
		sendThread = new SendThread(ia, port);
		sendThread.setDaemon(true);
		sendThread.start();	
	}
	public class SendThread extends Thread{
    	boolean available = true;
    	AudioRecord audio_recorder;
    	
    	InetAddress m_ia;
    	int			m_port;
    	
    	public SendThread(InetAddress ia, int port){
    		m_ia = ia;
    		m_port = port;
    	}
    	
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
    		super.run();
    		
    		Log.e(LOG_TAG, "start SendMicAudio thread, thread id: "
					+ Thread.currentThread().getId());
					
			audio_recorder = new AudioRecord(
					MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT,
					AudioRecord.getMinBufferSize(SAMPLE_RATE,
							AudioFormat.CHANNEL_CONFIGURATION_MONO,
							AudioFormat.ENCODING_PCM_16BIT) * 10);
			int bytes_read = 0;
			int bytes_count = 0;
			byte[] buf = new byte[BUF_SIZE];
			try {					
				//InetAddress addr = InetAddress.getByName(HOST);
				DatagramSocket sock = new DatagramSocket(7772);					
			
				audio_recorder.startRecording();
				while (available) {
					bytes_read = audio_recorder.read(buf, 0, BUF_SIZE);
					DatagramPacket pack = new DatagramPacket(buf,
							bytes_read, m_ia, m_port);
					
					sock.send(pack);
					bytes_count += bytes_read;
					Log.d(LOG_TAG, "bytes_count : " + bytes_count);
					Thread.sleep(SAMPLE_INTERVAL, 0);
				}
			} catch (InterruptedException ie) {
				Log.e(LOG_TAG, "InterruptedException");
			}
			// catch (FileNotFoundException fnfe)
			// {
			// Log.e(LOG_TAG, "FileNotFoundException");
			// }
			catch (SocketException se) {
				Log.e(LOG_TAG, "SocketException");
			} catch (UnknownHostException uhe) {
				Log.e(LOG_TAG, "UnknownHostException");
			} catch (IOException ie) {
				Log.e(LOG_TAG, "IOException");
			}
    	}
    	
    	private void kill(){    		
    		available = false;
    		
    		if(audio_recorder.getState() == AudioRecord.STATE_INITIALIZED){
    			audio_recorder.stop();
    			audio_recorder.release();
    		}
    	}
    }

	
	public RecvThread 	recvThread;	
	public Timer		timer;
	
	public void StopAudio(){
		if(recvThread != null){
			recvThread.kill();
		}
	}
    public void RecvAudio()
    {
    	recvThread = new RecvThread();
    	recvThread.setDaemon(true);
    	recvThread.start();    	
    }
    public void RecvAudioSetListenable(boolean able){
    	recvThread.setListenable(able);
    }
    
    public Boolean isRecvConnected = false;
    public class RecvThread extends Thread{
		boolean available = true;
		boolean listenable = false;
		DatagramSocket sock;
		AudioTrack track;
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			
			
			
			
			Log.e(LOG_TAG, "start recv thread, thread id: "
                    + Thread.currentThread().getId());
                track = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 
                        SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, 
                        AudioFormat.ENCODING_PCM_16BIT, BUF_SIZE, 
                        AudioTrack.MODE_STREAM);
                track.play();
                try
                {
                    sock = new DatagramSocket(7771);
                    byte[] buf = new byte[BUF_SIZE];

                    boolean firstRecv = true;
                    while(available)
                    {
                        DatagramPacket pack = new DatagramPacket(buf, BUF_SIZE);
                        sock.receive(pack);
                        isRecvConnected = true;
                        
                        Log.d("REC", "recv pack: " + pack.getLength());
                        
                        if(firstRecv){
                        	
                        	Message msg = Message.obtain(mainHandler, 2);
                        	mainHandler.sendMessage(msg);
                        	
                        	firstRecv = false;
                        	
                        	timer = new Timer();
                        	RecvStateChecker rsc = new RecvStateChecker();
                        	
                        	timer.schedule(rsc, 0, 1000);
                        	
                        }                        
                        
                        if(listenable){
                        	track.write(pack.getData(), 0, pack.getLength());
                        }                        
                    }
                }
                catch (SocketException se)
                {
                    Log.e(LOG_TAG, "SocketException: " + se.toString());
                }
                catch (IOException ie)
                {
                    Log.e(LOG_TAG, "IOException" + ie.toString());
                }
			
		}
		
		private void kill(){			
			available = false;
			sock.close();
			
			if( track.getState() == AudioTrack.STATE_INITIALIZED ){
				track.stop();
				track.release();
			}			
		}
		
		private void setListenable(boolean able){
			listenable = able;
		}
		
		public void disconnectionHandling(){
			timer.cancel();
			Message msg = Message.obtain(mainHandler, 3); // ���� ���� �˸�
			mainHandler.sendMessage(msg);
		}
	}
    
    public class RecvStateChecker extends TimerTask{
    	
    	int cnt = 0;
    	
    	public RecvStateChecker(){
    		
    	}
    	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			if(!isRecvConnected){
				cnt++;
			}else{
				cnt = 0;
			}
			isRecvConnected = false;
			
			if(cnt > 1){
				recvThread.disconnectionHandling();
			}
		}
    	
    }
}