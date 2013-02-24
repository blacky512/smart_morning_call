package whitepaper.smcall;

import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.db.SmcallDB;
import whitepaper.smcall.remote.Jax;
import whitepaper.smcall.remote.Mjpage;
import whitepaper.smcall.voicechat.Voicechat;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
		
	private EditText	editText_id;
	private EditText	editText_pw;
	private Button		btn_login;
	
	private SmcallDB 	scDB;
	private Cursor		cs;	
	private Jax			jax;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.login_activity);
		
		view_init();
		init();		
	}
	
	private void view_init(){
		editText_id	= (EditText)findViewById(R.id.etId);
		editText_pw	= (EditText)findViewById(R.id.etPw);
		btn_login	= (Button)findViewById(R.id.btnLg);
			btn_login.setOnClickListener(onClickListener);
	}
	
	private void init(){
		scDB	= new SmcallDB(LoginActivity.this);
		scDB	.open();
		
		jax		= new Jax();
	}
	
	public View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnLg:
				/*
				Intent i1 = new Intent(LoginActivity.this, MorningCallActivity.class);
				startActivity(i1);*/
				
				// 입력에 대한 예외처리
				if((editText_id.getText().length() == 0 && editText_pw.getText().length() == 0)){
					Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
					break;
				}else if(editText_id.getText().length() == 0){
					Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
					break;
				}else if(editText_pw.getText().length() == 0){
					Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
					break;
				}
				
				String id = editText_id.getText().toString(); AlarmStr.id = id;
				String pw = editText_pw.getText().toString();
				
				String[]	values 	= {"id", id, "pw", pw};
				String		ret 	= jax.sendJson(Mjpage.login, values);
				
				if(Boolean.valueOf(jax.getValue(ret, "result"))){
					Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_SHORT).show();			
					
					scDB.initAccount();
					scDB.insertAccount(id, pw);
					AlarmStr.id = id;
					
					Intent i = new Intent(LoginActivity.this, MorningCallActivity.class);
					startActivity(i);					
				}else{
					Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
				}
				
				
				break;
				
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		// 옵션메뉴 없음
		return true;
	}
	
	private boolean end = false;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		scDB.close();
	}

}
