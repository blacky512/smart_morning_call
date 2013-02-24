package voice.tUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

public class VoicechatSTUN {
	 static final String LOG_TAG = "UdpStream";    
	    static final int AUDIO_PORT = 7771;
	    static final int SAMPLE_RATE = 8000;
	    static final int SAMPLE_INTERVAL = 20; // milliseconds
	    static final int SAMPLE_SIZE = 2; // bytes per sample
	    static final int BUF_SIZE = SAMPLE_INTERVAL*SAMPLE_INTERVAL*SAMPLE_SIZE*2;
		public void SendMicAudio() {
			Thread thrd = new Thread(new Runnable() {
				public void run() {
					
					Log.e(LOG_TAG, "start SendMicAudio thread, thread id: "
							+ Thread.currentThread().getId());
							
					AudioRecord audio_recorder = new AudioRecord(
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
						InetAddress addr = InetAddress.getByName(Addr.host);
						DatagramSocket sock = new DatagramSocket();					
					
						audio_recorder.startRecording();
						
						
						while (true) {							
							bytes_read = audio_recorder.read(buf, 0, BUF_SIZE);
							
							DatagramPacket pack = new DatagramPacket(buf,
									bytes_read, addr, AUDIO_PORT);
							
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
				} // end run
			});
			thrd.start();
		}
		
	    public void RecvAudio()
	    {
	        Thread thrd = new Thread(new Runnable() {
	            
	            public void run() 
	            {
	                Log.e(LOG_TAG, "start recv thread, thread id: "
	                    + Thread.currentThread().getId());
	                AudioTrack track = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 
	                        SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, 
	                        AudioFormat.ENCODING_PCM_16BIT, BUF_SIZE, 
	                        AudioTrack.MODE_STREAM);
	                track.play();
	                try
	                {
	                    DatagramSocket sock = new DatagramSocket(AUDIO_PORT);
	                    byte[] buf = new byte[BUF_SIZE];
	                    
	                    
	                    
	                    Log.d("REC", "started");
	                    
	                    // new //
	                    
	                    int half=0;
	                    int length;
	                    
	                    /////////
	                    while(true)
	                    {
	                        
	                        if(half%4 == 0)
	                        {	
								byte[] addrBuf;
								InetAddress addr = InetAddress.getByName(Addr.host);
								////
								addrBuf = "serveraddress".getBytes();
								//Arrays.fill(addrBuf, 13, addrBuf.l, (Byte) null);
								DatagramPacket addrPack = new DatagramPacket(addrBuf, 0, addrBuf.length, addr, AUDIO_PORT);
								
								sock.send(addrPack);	
							
							}
	                        half++;
	                        
	                        DatagramPacket pack = new DatagramPacket(buf, BUF_SIZE);                        
	                        sock.receive(pack);
	                        
	                        						
	                        
	                        length = pack.getLength();
	                        
	                        if(length < 1000){
	                        	Log.d("REC", new String(pack.getData(), 0, length));                        	
	                        }else{
	                        	Log.d("REC", "recv pack: " + length);
		                        track.write(pack.getData(), 0, length);
	                        }
	                        /////////
	                        
	                        /*
	                        Log.d("REC", "recv pack: " + pack.getLength());
	                        track.write(pack.getData(), 0, pack.getLength());
	                        */
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
	            } // end run
	        });
	        thrd.start();
	    }
}
