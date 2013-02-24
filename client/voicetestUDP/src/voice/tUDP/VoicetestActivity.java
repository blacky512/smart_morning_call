package voice.tUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import voice.tUDP.R;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 메인 액티비티
 * 
 * @author kyobum
 * 
 */
public class VoicetestActivity extends Activity {
	/** Called when the activity is first created. */

	// View
	RadioButton rBtnSrv; // 모드 선택 (서버와 클라이언트 중 하나 선택하도록)
	RadioButton rBtnCli;

	RadioGroup rGroup; // 라디오버튼 두개를 묶을 그룹

	EditText etAddr; // 서버 주소와 포트번호를 입력할 에딧텍스트
	EditText etPort;

	Button btnExe; // 실행버튼

	TextView txtView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	

		etAddr = (EditText) findViewById(R.id.editText1);

		btnExe = (Button) findViewById(R.id.button1);
		btnExe.setOnClickListener(onClickListener);

		txtView = (TextView) findViewById(R.id.info);

		String myIpAddr = Utils.getIpAddress(this);
		txtView.setText(myIpAddr);
	}

	public View.OnClickListener onClickListener = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.button1:
				
				Addr.host = etAddr.getText().toString();
								
				VoicechatSTUN vs = new VoicechatSTUN();
				vs.SendMicAudio();
				vs.RecvAudio();			
				
				btnExe.setEnabled(false);
				
				break;
			default:
				break;
			}
		}
	};

	// 타 스레드에서 본 액티비티(스레드)로 메시지를 보내면 받아 처리하는 핸들러
	// 여기서는 특별한 일을 수행하지 않음. 편의상 작성.
	public Handler mainHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case 0:
				Toast.makeText(getApplicationContext(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};


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
                    
                   
                    while(true)
                    {
                        DatagramPacket pack = new DatagramPacket(buf, BUF_SIZE);                        
                        sock.receive(pack);
                        
                     
                        
                        Log.d("REC", "recv pack: " + pack.getLength());
                        track.write(pack.getData(), 0, pack.getLength());
                        
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