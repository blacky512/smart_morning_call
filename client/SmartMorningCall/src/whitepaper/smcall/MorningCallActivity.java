package whitepaper.smcall;

import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.db.SmcallDB;
import whitepaper.smcall.fragment.JoinFrag;
import whitepaper.smcall.fragment.ModifyFrag;
import whitepaper.smcall.remote.Utils;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MorningCallActivity extends FragmentActivity {
	FragmentActivity mActivity = this;
	boolean end = false;
	SmcallDB	 scDB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morningcall_activity);
		
		AlarmStr.private_ip = Utils.getPrivateIP(mActivity);
		
		scDB = new SmcallDB(getApplicationContext());
		scDB.open();
		
		Cursor c = scDB.getAlarm();
		if(c.getCount() > 0){
			c.moveToFirst();
			AlarmStr.time_hour		= Integer.valueOf(c.getString(0));
			AlarmStr.time_minute	= Integer.valueOf(c.getString(1));
			
			Log.i("TIME", c.getString(0));
			Log.i("TIME", c.getString(1));
			
			for(int i=2; i<9; i++){
				AlarmStr.repeat[i-2] = Boolean.valueOf(c.getString(i));
			}
			
			AlarmStr.type_sound = Boolean.valueOf(c.getString(9));
			
			AlarmStr.type_vibe	= Boolean.valueOf(c.getString(10));
			
			AlarmStr.alive		= Boolean.valueOf(c.getString(11));
		}
		
		c.close();
		scDB.close();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		menu.add(0, 0, 0, "회원정보수정").setIcon(android.R.drawable.ic_menu_edit);
		menu.add(0, 1, 0, "로그아웃").setIcon(android.R.drawable.ic_input_delete);
				
		return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
		DialogFragment newFragment;
		
		switch (item.getItemId()) {
		case 0:
			newFragment = new ModifyFrag();
			newFragment.show(fm, "modify");
			
			break;
		case 1:
			if(AlarmStr.alive){
				Toast.makeText(getApplicationContext(), "먼저 모닝콜을 해제해 주세요.", Toast.LENGTH_SHORT).show();
			}else{
				scDB.open();
				scDB.removeDB();
				
				try {
					finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();	
			}			
			
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.morningcall_activity, menu);
		return true;
	}
	*/
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		if(!end){
			end = true;
			Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
			
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					end = false;
					
				}
			});
			thread.start();
		}else{			
			Intent launchHome = new Intent(Intent.ACTION_MAIN);
			launchHome.addCategory(Intent.CATEGORY_DEFAULT);
			launchHome.addCategory(Intent.CATEGORY_HOME);
			startActivity(launchHome);				
		}	
	}

}
