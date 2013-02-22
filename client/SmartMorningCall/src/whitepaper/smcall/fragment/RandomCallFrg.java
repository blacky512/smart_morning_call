package whitepaper.smcall.fragment;

import java.util.Calendar;

import whitepaper.smcall.AlarmReceiverActivity;
import whitepaper.smcall.R;
import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.remote.Jax;
import whitepaper.smcall.remote.Mjpage;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RandomCallFrg extends Fragment {

	private final String TAG = "RAN";
	private static final int DIALOG_TIME = 0;
		
	private Calendar	calendar;
	
	private Jax			jax;

	private View mView;

	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;
	
	
	private ImageButton[] btn_days = new ImageButton[7];	

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
		
		btn_days[0] = (ImageButton)mView.findViewById(R.id.btn_mon);
		btn_days[1] = (ImageButton)mView.findViewById(R.id.btn_tue);
		btn_days[2] = (ImageButton)mView.findViewById(R.id.btn_wed);
		btn_days[3] = (ImageButton)mView.findViewById(R.id.btn_thu);
		btn_days[4] = (ImageButton)mView.findViewById(R.id.btn_fri);
		btn_days[5] = (ImageButton)mView.findViewById(R.id.btn_sat);
		btn_days[6] = (ImageButton)mView.findViewById(R.id.btn_sun);
		
		for(int i=0; i<7; i++){
			btn_days[i].setOnClickListener(onClickListener);
		}
				

		// TODO Auto-generated method stub
		return mView;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		jax = new Jax();
		viewInit();
				
		calendar	= Calendar.getInstance();
		//@ 디비 오픈해서 저장된 알람 있다면 해당 정보로 alarmStr 값 입력 
		//값 바꾸면 모닝콜 활성 유무 오프시키고, 나중에 온 누르면 디비에 저장하도록
		//저장 안되었는데 페이지를 벗어나려고 하면 저장안할거냐 묻도록
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
		FragmentTransaction ft;
		DialogFragment newFragment;
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_settime:
				ft = getFragmentManager()
						.beginTransaction();
				newFragment = new TimePickerDialogFragment();
								
				newFragment.show(ft, "dialog");
				
				break;
				
			case R.id.onoff:
				//Toast.makeText(getActivity(), String.valueOf(onoff.isChecked()), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getActivity(), whitepaper.smcall.AlarmReceiverActivity.class);
				
		        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		        AlarmManager am = (AlarmManager) getActivity().getSystemService(Activity.ALARM_SERVICE);;
		        
				if(onoff.isChecked()){	// 사용
					// 알람등록
			        
			        Calendar cal = Calendar.getInstance();
			        cal.set(Calendar.HOUR_OF_DAY, AlarmStr.time_hour);
			        cal.set(Calendar.MINUTE, AlarmStr.time_minute);
			        //add(Calendar.SECOND, 5);
			        
			        // 서버에 알람 설정			        			        
			        String[] values = { "id", AlarmStr.id,
			        					"hour", String.valueOf(AlarmStr.time_hour),
			        				    "minute", String.valueOf(AlarmStr.time_minute)};			        
			        String ret = jax.sendJson(Mjpage.set_alarm, values);
			        
			        if(Boolean.valueOf(jax.getValue(ret, "result"))){
			        	Toast.makeText(getActivity(), "서버에 등록되었습니다.", Toast.LENGTH_SHORT).show();
			        	
			        	
			        	String[] values_ip = {"id", AlarmStr.id,
			        			  			  "ip_public", "000.000.000.000",
			        			  			  "ip_virtual", AlarmStr.private_ip};
			        	jax.sendJson(Mjpage.algo_update, values_ip);
			        	
			        }else{
			        	Toast.makeText(getActivity(), "등록 실패", Toast.LENGTH_SHORT).show();
			        	onoff.setChecked(false);
			        	break;
			        }
			        
			        // 단말기에 알람 설정
			        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
			        
				}else{					// 사용안함
					// 알람해제
					am.cancel(pendingIntent);
				}
				break;
				
				
			case R.id.btn_mon:
				//AlarmStr.repeat[0] = 
				//break;
			case R.id.btn_tue:
				//break;
			case R.id.btn_wed:
				//break;
			case R.id.btn_thu:
				//break;
			case R.id.btn_fri:
				//break;
			case R.id.btn_sat:
				//break;
			case R.id.btn_sun:
				ft = getFragmentManager()
				.beginTransaction();				
				newFragment = new DaySelect();
				//newFragment.setStyle(R.style.AppTheme, android.R.style.Theme_NoTitleBar_OverlayActionModes);
				//newFragment.setRetainInstance(true);
				newFragment.show(ft, "dialog");
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
			
			//@ 화면 업데이트 하는 메소드 작성해서 각종 리스너에 대해서 값 변할 때마다 작동하도록 함
			
			Toast.makeText(getActivity(), hourOfDay + "시" + minute + "분",
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
			return new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, onTimeSetListener, hour, minute, true);
			/*return new TimePickerDialog(getActivity(), STYLE_NORMAL,
					onTimeSetListener, hour, minute, false);*/			
		}
	}
}
