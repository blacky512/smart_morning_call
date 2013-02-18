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
	    
		////////////////������ ��Ƽ��Ƽ ���� ó���ϵ���///////////////////////
		//DB
		scDB	= new SmcallDB(MainActivity.this);
		scDB	.open();
		cs 		= scDB.getAccount();
		
				
		if(cs.getCount() == 1){ // ����� ���̵� ����
			// ����� ���̵�� �α��� �� �˶� ��Ƽ��Ƽ�� �̵�
			id = cs.getString(1);
			pw = cs.getString(2);
			
			//nextActivity = dd
		}else{
			Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_SHORT).show();
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