package whitepaper.smcall;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.fragment.RandomCallFrg.TimePickerDialogFragment;
import whitepaper.smcall.fragment.VoicechattingFrag;
import whitepaper.smcall.remote.Jax;
import whitepaper.smcall.remote.MatchInfo;
import whitepaper.smcall.remote.Mjpage;
import whitepaper.smcall.remote.Utils;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 알람 리시버 액티비티. 알람을 받아서 모닝콜을 연결하도록 함
 * 
 * @author Administrator
 * 
 */

public class AlarmReceiverActivity extends FragmentActivity {
	
	public FragmentActivity me = this;
	
	private final String TAG = "JAX";
	private Activity mActivity = this;

	private PowerManager.WakeLock wl;
	private Jax	jax;
	
	private TextView tvTime;
	private Button stopAlarm;
	private Vibrator vibrator;
	private Timer	flowChecker;
		
	public Handler mainHandler;
	
	private DialogFragment diaFrag;

	public int flowTime = 0;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		
		setContentView(R.layout.alarmreceiver_activity);
		
		stopAlarm 	= (Button) findViewById(R.id.stopAlarm);
		stopAlarm	.setOnClickListener(onClickListener);
		
		tvTime		= (TextView) findViewById(R.id.flow);
		
		// 화면 OFF와 잠금을 뚫고 액티비티 띄우기
		wakeUp();	
		
		jax = new Jax();
		
		
		flowChecker = new Timer();
		flowChecker.schedule(new Counter(), 0, 1000);
		///////////////////////// 핸들러
						
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				switch (msg.what) {
				case 0:
					tvTime.setText((String)msg.obj);
					break;

				default:
					break;
				}
			}
		};
		//////////////////////////////////////// 핸들러 끝
		
		matchPolling();
		
			
		
		
		// 알람기능
		alarming();
				
				
		wl.release();
	}
	
	private void matchPolling()	{
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Looper.prepare();
				
				// TODO Auto-generated method stub
				String[] values = {"time", AlarmStr.getTime(),
						   			"id", AlarmStr.id};
				String ret;
				
				while(true){					
					ret = jax.sendJson(Mjpage.algo_array, values);
					if(Boolean.valueOf(jax.getValue(ret, "call"))){
						MatchInfo.match_private_Ip 	= jax.getValue(ret, "ip_virtual");
						MatchInfo.available 		= true;
						
						//@ 상대 아이디, 성별 받기
						/*
						 * MatchInfo.opposite_id	= jax.getValue(ret, "");
						 * MatchInfo.opposite_sex	= jax.getValue(ret, "");
						 * 
						 * @ voicecallfrag 성별 구분 업데이트
						 * @ pollfrag 상대 아이디 적어서 결과 전송
						 */
						
						// 연결처리
						Message retmsg = Message.obtain(mainHandler, 0);			
						mainHandler.sendMessage(retmsg);
						
						break;
					}else{						
						break;
					}
				}
				Looper.loop();				
			}
		});
		
		thread.start();
		
	}
	
	private void wakeUp(){
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
		wl.acquire();

		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		Toast.makeText(getApplicationContext(), "일어날 시간 입니다!", Toast.LENGTH_SHORT)
				.show();
	}
	
	private void alarming(){
		Thread alarmThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);				
				
				vibrator.vibrate( new long[]{ 0, 3000, 0}, 0 );	
			}
		});

		alarmThread.start();				
	}
		
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		FragmentManager fm = getSupportFragmentManager();
		//FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		DialogFragment newFragment;
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.stopAlarm:
			{
				
			}
				vibrator.cancel();	
				
				flowChecker.cancel();
				MatchInfo.response = String.valueOf(flowTime);
				
				stopAlarm.setVisibility(View.GONE);
				
				diaFrag = VoicechattingFrag.newInstance();				
				diaFrag.show(fm, TAG);			
				
				
				/*
				if(MatchInfo.available){
					VoicechattingFrag.newInstance().show(fm, TAG);
					//fm.beginTransaction().show(newFragment).commit();
				}
				*/
				break;
			}
		}
	};
	

	@Override
	protected void onStop() {		
		// TODO Auto-generated method stub
		super.onStop();	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.alarmreceiver_activity, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
	
	public class Counter extends TimerTask{
		
		int tv_min = 0;
		int tv_sec = 0;
		Message retmsg;
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			tv_min = flowTime / 60;
			tv_sec = flowTime % 60;

			String ret = "+ "+String.format("%02d", tv_min) + " : "
					   + String.format("%02d", tv_sec);
			
			retmsg = Message.obtain(mainHandler, 0, ret);
			mainHandler.sendMessage(retmsg);
			
			flowTime++;
		}
	}
	
	public void afterConfirm(){
		
		diaFrag.onDestroy();
		diaFrag.onDetach();
		
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
		//@ 상대 평가창 띄우기
		
	}
	
	public void afterCancel(){
		
		diaFrag.onDestroy();
		diaFrag.onDetach();
		
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}
	
	public void afterPoll(){
		
	}
		
	

}
