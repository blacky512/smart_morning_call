package whitepaper.smcall.fragment;

import java.util.Calendar;

import whitepaper.smcall.AlarmReceiverActivity;
import whitepaper.smcall.R;
import whitepaper.smcall.alarm.AlarmStr;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RandomCallFrg extends Fragment {

	private static final int DIALOG_TIME = 0;
		
	private Calendar	calendar;

	private View mView;

	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;

	private Button btn_setTime;
	
	private	ToggleButton onoff;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.randomcall_frag, null);

		// TODO Auto-generated method stub
		return mView;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		viewInit();
				
		calendar	= Calendar.getInstance();
		//@ ��� �����ؼ� ����� �˶� �ִٸ� �ش� ������ alarmStr �� �Է� 
		//�� �ٲٸ� ����� Ȱ�� ���� ������Ű��, ���߿� �� ������ ��� �����ϵ���
		//���� �ȵǾ��µ� �������� ������� �ϸ� ������Ұų� ������
	}
	
	private void viewInit(){
		
		tv1 = (TextView) mView.findViewById(R.id.textView1);
		tv2 = (TextView) mView.findViewById(R.id.textView2);
		tv3 = (TextView) mView.findViewById(R.id.textView3);
		tv4 = (TextView) mView.findViewById(R.id.textView4);
		tv5 = (TextView) mView.findViewById(R.id.textView5);

		btn_setTime	= (Button) mView.findViewById(R.id.btn_settime);
		btn_setTime	.setOnClickListener(onClickListener);
		
		onoff	= (ToggleButton) mView.findViewById(R.id.onoff);
		onoff	.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_settime:
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				DialogFragment newFragment = new TimePickerDialogFragment();
				newFragment.show(ft, "dialog");
				
				break;
				
			case R.id.onoff:
				//Toast.makeText(getActivity(), String.valueOf(onoff.isChecked()), Toast.LENGTH_SHORT).show();
				if(onoff.isChecked()){	// ���
					// �˶����
					Intent intent = new Intent(getActivity(), whitepaper.smcall.AlarmReceiverActivity.class);
			        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),
			            12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			        
			        Calendar cal = Calendar.getInstance();
			        cal.set(Calendar.HOUR_OF_DAY, AlarmStr.time_hour);
			        cal.set(Calendar.MINUTE, AlarmStr.time_minute);
			        //add(Calendar.SECOND, 5);
			        
			        AlarmManager am = 
			            (AlarmManager) getActivity().getSystemService(Activity.ALARM_SERVICE);
			        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
			        
				}else{					// ������
					// �˶�����
				}
				break;

			default:
				break;
			}
		}
	};
	

	public OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			AlarmStr.time_hour		= hourOfDay;
			AlarmStr.time_minute	= minute;
			
			//@ ȭ�� ������Ʈ �ϴ� �޼ҵ� �ۼ��ؼ� ���� �����ʿ� ���ؼ� �� ���� ������ �۵��ϵ��� ��
			
			Toast.makeText(getActivity(), hourOfDay + "��" + minute + "��",
					Toast.LENGTH_SHORT).show();
		}
	};

	public class TimePickerDialogFragment extends DialogFragment {
		/*
		 * private Fragment mFragment;
		 * 
		 * public TimePickerDialogFragment(Fragment callback) { mFragment =
		 * callback; }
		 */
		
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			int hour	= calendar.get(Calendar.HOUR_OF_DAY);
			int minute	= (calendar.get(Calendar.MINUTE)+1) % 60;
			
			return new TimePickerDialog(getActivity(), STYLE_NORMAL,
					onTimeSetListener, hour, minute, false);			
		}
	}
}