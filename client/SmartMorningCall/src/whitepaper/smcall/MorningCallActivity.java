package whitepaper.smcall;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

public class MorningCallActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morningcall_activity);
		
		/*
		Intent i = new Intent("whitepaper.smcall");
		i.putExtra("serv", "serv");
		startService(i);
		*/		
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
		Toast.makeText(getApplicationContext(), "�ڷ� ��ư�� �ѹ� �� ������ ����˴ϴ�.(�̱���)", Toast.LENGTH_SHORT).show();
	}

}