package whitepaper.smcall;

import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.remote.Jax;
import whitepaper.smcall.remote.Mjpage;
import whitepaper.smcall.remote.Utils;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * 알람 리시버 액티비티. 알람을 받아서 모닝콜을 연결하도록 함
 * 
 * @author Administrator
 * 
 */

public class AlarmReceiverActivity extends Activity {
	private final String tag = "JAX";
	private Activity mActivity = this;

	private PowerManager.WakeLock wl;
	private Jax	jax;
	
	private Button stopAlarm;	
	
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmreceiver_activity);
		
		stopAlarm = (Button) findViewById(R.id.stopAlarm);
		stopAlarm.setOnClickListener(onClickListener);
		
		
		jax = new Jax();
		
		sendInfo();
		
		// 화면 OFF와 잠금을 뚫고 액티비티 띄우기
		wakeUp();
		
		// 알람기능
		alarming();
				
				
		wl.release();
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

		Toast.makeText(getApplicationContext(), "알람입니다", Toast.LENGTH_LONG)
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
	
	private void sendInfo(){
		Thread sendThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String[] value = {	"time",			String.format("%02d", AlarmStr.time_hour)+":"+String.format("%02d", AlarmStr.time_minute)+":"+"00", 
					  	"id",			AlarmStr.id,
					  	"ip_public" ,	"000.000.000.000",
					  	"ip_private",	Utils.getPrivateIP(mActivity)};
			
				String ret = jax.sendJson(Mjpage.algo_update, value);
				Log.i(tag, ret);				
			}
		});
		
		sendThread.start();
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.stopAlarm:				
				vibrator.cancel();
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

}
