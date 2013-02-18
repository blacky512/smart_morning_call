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
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 알람 리시버 액티비티. 알람을 받아서 모닝콜을 연결하도록 함
 * 
 * @author Administrator
 * 
 */

public class AlarmReceiverActivity extends Activity {
	private final String tag = "JAX";

	private PowerManager.WakeLock wl;
	private Jax	jax;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmreceiver_activity);
		
		jax = new Jax();
		
		// 화면 OFF와 잠금을 뚫고 액티비티 띄우기
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
		
		
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
		
		String[] value = {	"time",			String.format("%02d", AlarmStr.time_hour)+":"+String.format("%02d", AlarmStr.time_minute)+":"+"00", 
						  	"id",			AlarmStr.id,
						  	"ip_public" ,	Utils.getPublicIP(),
						  	"ip_private",	Utils.getPrivateIP(this)};
				
		String ret = jax.sendJson(Mjpage.algo_update, value);
		Log.i(tag, ret);
				
		wl.release();
	}

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

}
