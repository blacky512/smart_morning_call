package whitepaper.smcall;

import whitepaper.smcall.db.SmcallDB;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public String id;
	public String pw;
	
	private Handler		h;
	private SmcallDB 	scDB;
	private Cursor		cs;
	
	private Class<?>	nextActivity;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main_activity);	
	    
	    h = new Handler();
	    h.postDelayed(irun, 1500);
	    
		////////////////오프닝 액티비티 만들어서 처리하도록///////////////////////
		//DB
		scDB	= new SmcallDB(MainActivity.this);
		scDB	.open();
		cs 		= scDB.getAccount();
		
				
		if(cs.getCount() == 1){ // 저장된 아이디 있음
			// 저장된 아이디로 로그인 후 알람 액티비티로 이동
			id = cs.getString(1);
			pw = cs.getString(2);
			
			//nextActivity = dd
		}else{
			Toast.makeText(getApplicationContext(), "계정없음", Toast.LENGTH_SHORT).show();
			nextActivity = LoginActivity.class;			
		}		
	}
	
	Runnable irun = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub		
			
			Intent i = new Intent(MainActivity.this, nextActivity);
			startActivity(i);
						
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
			
		}
	};
	
	
}
