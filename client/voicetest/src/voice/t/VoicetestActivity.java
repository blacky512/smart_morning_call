package voice.t;


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
 * @author kyobum
 *
 */
public class VoicetestActivity extends Activity {
    /** Called when the activity is first created. */
	
	// View
	RadioButton rBtnSrv;	// 모드 선택 (서버와 클라이언트 중 하나 선택하도록)
	RadioButton rBtnCli;
	
	RadioGroup	rGroup;		// 라디오버튼 두개를 묶을 그룹
	
	EditText	etAddr;		// 서버 주소와 포트번호를 입력할 에딧텍스트
	EditText	etPort;
	
	Button		btnExe;		// 실행버튼
	
	TextView	txtView;	
	
	
	// Audio Variables
	
	// RECORD
	int frequency				= 11025;		
	int channelConfiguration	= AudioFormat.CHANNEL_CONFIGURATION_MONO;
	int audioEncoding			= AudioFormat.ENCODING_PCM_16BIT;
	
	public int			bufferSize	= AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
	public byte[] 		buffer		= new byte[bufferSize];
	public AudioRecord	audioRecord;
	
	
	// PLAY
	AudioTrack 			audioTrack;
	
	// Server, Client Thread....
	public Srv		srvThread;
	public Cli		cliThread;
	public String	myIpAddr;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // View 초기화
        rBtnSrv = (RadioButton)findViewById(R.id.radioButton1);
        rBtnSrv.setOnClickListener(onClickListener);        
        
        rBtnCli	= (RadioButton)findViewById(R.id.radioButton2);
        rBtnCli.setOnClickListener(onClickListener);
        
        rGroup	= (RadioGroup)findViewById(R.id.radioGroup1);
              
        etAddr	= (EditText)findViewById(R.id.editText1);        
        etPort	= (EditText)findViewById(R.id.editText2);
        etAddr	.setEnabled(false);
        etPort	.setEnabled(false);
        
        btnExe	= (Button)findViewById(R.id.button1);
        btnExe	.setOnClickListener(onClickListener);
        
        txtView	= (TextView)findViewById(R.id.info);
        
        
        audioRecord	= new AudioRecord(MediaRecorder.AudioSource.MIC,
				frequency, 
				channelConfiguration,
				audioEncoding,
				bufferSize);
        //audioRecord.release();
        
        audioTrack	= new AudioTrack(AudioManager.STREAM_VOICE_CALL, 
        		frequency, 
        		channelConfiguration,
        		audioEncoding, 
    			bufferSize*2, 
    			AudioTrack.MODE_STREAM);        
        
        myIpAddr	= Utils.getIpAddress(this);
        
        Toast.makeText(getApplicationContext(), String.valueOf(bufferSize), Toast.LENGTH_SHORT).show();        
    }
    
    @Override
    public void onDestroy() { 
        super.onDestroy();        
        audioRecord.release();
    }
        
    public View.OnClickListener onClickListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.radioButton1:
				etAddr	.setEnabled(false);
		        etPort	.setEnabled(false);
		        ///Toast.makeText(getApplicationContext(), String.valueOf(rGroup.getCheckedRadioButtonId()), Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.radioButton2:
				etAddr	.setEnabled(true);
		        etPort	.setEnabled(true);
		        //Toast.makeText(getApplicationContext(), rGroup.getCheckedRadioButtonId(), Toast.LENGTH_SHORT).show();		        
				break;
				
			case R.id.button1:
				if(rGroup.getCheckedRadioButtonId() == rBtnSrv.getId()){			// 서버모드
					srvThread = new Srv(audioTrack, audioRecord, bufferSize, mainHandler);		// 서버스레드 실행
					srvThread.setDaemon(true);
					srvThread.start();
					
					txtView.setText(myIpAddr);
					btnExe.setEnabled(false);
					
				}else if(rGroup.getCheckedRadioButtonId() == rBtnCli.getId()){	// 클라이언트모드
					Addr.host = etAddr.getText().toString();
					Addr.port = Integer.parseInt(etPort.getText().toString());
					
					Log.i("CHECK", Addr.host);
					
					cliThread	= new Cli(audioTrack, audioRecord, bufferSize);					// 클라이언트스레드 실행
					cliThread	.setDaemon(true);
					cliThread	.start();
					
					btnExe.setEnabled(false);
				}
				break;
			default:
				break;
			}
		}
	};
	
	// 타 스레드에서 본 액티비티(스레드)로 메시지를 보내면 받아 처리하는 핸들러
	// 여기서는 특별한 일을 수행하지 않음. 편의상 작성.
	public Handler mainHandler	= new Handler(){
	    	public void handleMessage(android.os.Message msg){
	    		switch (msg.what){
	    		
	    		case 0:
	    			Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
	    			break;
	    		}
	    	}
	 };
	
}