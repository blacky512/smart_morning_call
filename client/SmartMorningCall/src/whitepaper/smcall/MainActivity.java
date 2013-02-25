package whitepaper.smcall;

import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.db.SmcallDB;
import whitepaper.smcall.remote.Jax;
import whitepaper.smcall.remote.Mjpage;
import android.R.style;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Font.Style;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final String TAG = "MAIN";

	public Jax jax;

	public String id;
	public String pw;
	
	public TextView mainText;
	public Typeface	face;
	
	private Handler h;
	private SmcallDB scDB;
	private Cursor cs;

	private Class<?> nextActivity;

	// Load Dialog
	private ProgressDialog progressDialog = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		face = Typeface.createFromAsset(getAssets(), "font/08SEOULNAMSANL.TTF");

		h = new Handler();
		h.postDelayed(irun, 1500);	
		
		mainText = (TextView) findViewById(R.id.mainText);
		mainText.setTypeface(face);
	}


	Runnable irun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			jax = new Jax();
			// //////////////������ ��Ƽ��Ƽ ���� ó���ϵ���///////////////////////
			// DB
			scDB = new SmcallDB(MainActivity.this);
			scDB.open();
			cs = scDB.getAccount();
			cs.moveToFirst();

			if (cs.getCount() == 1) { // ����� ���̵� ����
				// ����� ���̵�� �α��� �� �˶� ��Ƽ��Ƽ�� �̵�
				
				mainText.setText("�α��� �� �Դϴ�.");
				
				Log.i(TAG, cs.toString());

				Log.i(TAG, id = cs.getString(0));

				Log.i(TAG, pw = cs.getString(1));

				Log.i("DB", id + " : " + pw + " ����� ����");
				
				AlarmStr.id = id;

				// TODO Auto-generated method stub
				String[] values = { "id", id, "pw", pw };
				String ret = jax.sendJson(Mjpage.login, values);

				if (Boolean.valueOf(jax.getValue(ret, "result"))) {
					Log.i(TAG, "�α��μ���");
					mainText.setText("�α��� ����");
					

					nextActivity = MorningCallActivity.class;
					
				} else { // �α��� ����
					Log.i(TAG, "�α��� ����");
					mainText.setText("�α��� ����");
					Toast.makeText(getApplicationContext(), "�α��� ����",
							Toast.LENGTH_SHORT).show();

					nextActivity = LoginActivity.class;
				}

			} else {
				//Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_SHORT).show();
				nextActivity = LoginActivity.class;
			}
			
			cs.close();
			scDB.close();
			
			
			Intent i = new Intent(MainActivity.this, nextActivity);
			startActivity(i);

			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			finish();
		}
	};

	@Override
	public void onBackPressed() {

	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			//progressDialog.setMessage("Connecting to Server");
			progressDialog.setCancelable(false);			
			//progressDialog.s
			
			return progressDialog;
		default:
			return null;
		}
	}

}
