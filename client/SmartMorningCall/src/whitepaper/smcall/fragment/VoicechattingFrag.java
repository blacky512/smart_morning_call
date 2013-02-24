package whitepaper.smcall.fragment;

import java.util.Timer;
import java.util.TimerTask;

import whitepaper.smcall.AlarmReceiverActivity;
import whitepaper.smcall.R;
import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.remote.MatchInfo;
import whitepaper.smcall.voicechat.Voicechat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VoicechattingFrag extends DialogFragment {

	private static final String TAG = "VC_FRAG";
	private View 				mRoot;
	private Voicechat 			vc;
	private VoicechattingFrag 	me 			= this;	
	private HandlerThread 		handlerTh;	
	
	private Timer 				timer;
	
	private TextView			tv;
	private TextView			vc_info;
	private TextView			tv_chat;
	
	private TextView			tv_1;
	private TextView			tv_2;
	private TextView			tv_3;
	
	
	public Handler mainHandler;
	
	public final int DISCONNECTABLETIME		= 30;
	public final int LIMITTIME				= 60;
	
	private Button 				vc_cut;
	private Button 				vc_confirm;
	private Button				vc_cancel;
	
	private ImageView			iv_step1;
	private ImageView			iv_step2;
	private ImageView			iv_step3;
	
	public	Typeface			face;
	
	private RelativeLayout		layBtn1;
	private RelativeLayout		layBtn2;
	private RelativeLayout		layBtn3;
	
	

	public static DialogFragment newInstance() {
		return new VoicechattingFrag();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		setStyle(DialogFragment.STYLE_NO_FRAME,
				android.R.style.Theme_Holo_Light_Dialog);

		Log.i(TAG, "onAttach");
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setCancelable(false);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRoot = inflater.inflate(R.layout.voicechatting_frag, container, true);
		
		face = Typeface.createFromAsset(getActivity().getAssets(), "font/08SEOULNAMSANL.TTF");
		
		tv			= (TextView) mRoot.findViewById(R.id.tvTimer);
			tv.setTypeface(face);
		tv_chat		= (TextView) mRoot.findViewById(R.id.tv_chat);
			tv_chat.setTypeface(face);
		vc_info		= (TextView) mRoot.findViewById(R.id.vc_infobar);
			vc_info.setTypeface(face);
			
		tv_1		= (TextView) mRoot.findViewById(R.id.tv_1);
			tv_1.setTypeface(face);
		tv_2		= (TextView) mRoot.findViewById(R.id.tv_2);
			tv_2.setTypeface(face);
		tv_3		= (TextView) mRoot.findViewById(R.id.tv_3);
			tv_3.setTypeface(face);
			
		layBtn1		= (RelativeLayout) mRoot.findViewById(R.id.layBtn1);
		layBtn2		= (RelativeLayout) mRoot.findViewById(R.id.layBtn2);
		layBtn3		= (RelativeLayout) mRoot.findViewById(R.id.layBtn3);
		layBtn1.setOnClickListener(onClickListener);
		layBtn1.setEnabled(false);
		layBtn2.setOnClickListener(onClickListener);
		layBtn2.setEnabled(false);
		layBtn3.setOnClickListener(onClickListener);
		layBtn3.setEnabled(false);
		
		vc_cut		= (Button) mRoot.findViewById(R.id.voicechat_cut);
			//vc_cut.setOnClickListener(onClickListener);
			vc_cut.setEnabled(false);
		vc_confirm	= (Button) mRoot.findViewById(R.id.voicechat_confirm);
			//vc_confirm.setOnClickListener(onClickListener);
			vc_confirm.setEnabled(false);
		vc_cancel	= (Button) mRoot.findViewById(R.id.voicechat_cancel);
			vc_cancel.setEnabled(false);
				
		//iv_step1	= (ImageView) mRoot.findViewById(R.id.voicechat_step1);
		//iv_step2	= (ImageView) mRoot.findViewById(R.id.voicechat_step2);
		//iv_step3	= (ImageView) mRoot.findViewById(R.id.voicechat_step3);
				
		
		handlerTh = new HandlerThread("name");
		handlerTh.start();		
		mainHandler = new Handler(){
			
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				switch (msg.what) {
				case 0:	//@ ��ȭ �ð� �ʰ�
					//vc_cut.setEnabled(false);
					layBtn1.setEnabled(false);
					vc_cut.setBackgroundResource(R.drawable.jhk_cut_04);
					
					//vc_confirm.setEnabled(true);
					layBtn2.setEnabled(true);
					vc_confirm.setBackgroundResource(R.drawable.jhk_confirm_);
					Toast.makeText(getActivity(), "��ȭ�ð� �ʰ�", Toast.LENGTH_SHORT).show();
					
					
					timer.cancel();
					
					
					vc.RecvAudioSetListenable(false);
					
					vc.StopAudio();
					vc.StopMicAudio();
					break;
					
				case 1:	// ��ȭ�ð� ������Ʈ				
					tv.setText((String)msg.obj);					
					break;
					
				case 2:	// ��Ŷ ���ú� ���� (�������� ��ȭ����)
					vc.RecvAudioSetListenable(true);
					timer.schedule(new VoiceChatTimer(mainHandler), 0, 1000);
					break;
					
				case 3: // ��Ŷ ���� ���� (��뿡�� ���� ������)
					
					//vc_cut.setEnabled(false);
					layBtn1.setEnabled(false);
					vc_cut.setBackgroundResource(R.drawable.jhk_cut_04);
					
					//vc_confirm.setEnabled(true);
					layBtn2.setEnabled(true);
					vc_confirm.setBackgroundResource(R.drawable.jhk_confirm_);

					Toast.makeText(getActivity(), "��밡 ��ȭ�� �����߽��ϴ�.", Toast.LENGTH_SHORT).show();
					
					timer.cancel();
					vc.StopAudio();
					vc.StopMicAudio();
					
					break;
					
				case 4: // ���� ���� ������ �ð�
					
					//vc_cut.setEnabled(true);
					layBtn1.setEnabled(true);
					vc_cut.setBackgroundResource(R.drawable.jhk_cut_);
					
					break;
				default:
					break;
				}
				
			}
		};
		
		timer = new Timer(true);
		vc = new Voicechat(MatchInfo.match_private_Ip, MatchInfo.match_private_port, mainHandler);
		
		
		//timer.schedule(new VoiceChatTimer(mainHandler), 0, 1000);
		
		
		// vc = new Voicechat("112.108.39.212", 7771);		
		
		//vc.RecvAudio();
		//vc.SendMicAudio();
		
		if(MatchInfo.available){
			vc.RecvAudio();
			vc.SendMicAudio();
			
		}else{
			vc_info.setText("����� ��밡 �����ϴ�. �Ф�");
			layBtn3.setEnabled(true);
			vc_cancel.setBackgroundResource(R.drawable.jhk_cancel_);
			
		}
		
		return mRoot;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(savedInstanceState);
	}
	
	public View.OnClickListener onClickListener = new View.OnClickListener() {
		
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			//case R.id.voicechat_cut:
			case R.id.layBtn1:
				
				//vc_cut.setEnabled(false);
				layBtn1.setEnabled(false);
				vc_cut.setBackgroundResource(R.drawable.jhk_cut_04);
								
				//vc_confirm.setEnabled(true);
				layBtn2.setEnabled(true);
				vc_confirm.setBackgroundResource(R.drawable.jhk_confirm_);
				
				timer.cancel();
				vc.timer.cancel();
				vc.RecvAudioSetListenable(false);
				vc.StopMicAudio();
				vc.StopAudio();				
				
				
				
				break;
			//case R.id.voicechat_confirm:
			case R.id.layBtn2:
				
				dismiss();				
				((AlarmReceiverActivity) getActivity()).afterConfirm();
				
				
				vc.StopMicAudio();
				vc.StopAudio();
				break;
				
			case R.id.layBtn3:
				
				dismiss();
				((AlarmReceiverActivity) getActivity()).afterCancel();

			default:
				break;
			}
			
		}
	};
	

	public class VoiceChatTimer extends TimerTask {

		Handler mainHandler;


		int time_sec = 0;

		int tv_min = 0;
		int tv_sec = 0;
		
		Message retmsg;

		public VoiceChatTimer(Handler mainHandler) {
			this.mainHandler	= mainHandler;
		
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			tv_min = time_sec / 60;
			tv_sec = time_sec % 60;

			String ret = String.format("%02d", tv_min) + " : "
					   + String.format("%02d", tv_sec);
			
			retmsg = Message.obtain(mainHandler, 1, ret);
			mainHandler.sendMessage(retmsg);
			
			
			time_sec++;
			

			if (time_sec == DISCONNECTABLETIME+1){
				retmsg = Message.obtain(mainHandler, 4);
				mainHandler.sendMessage(retmsg);
			}

			if (time_sec > LIMITTIME) {
				// �ð� ���� �˸�
				retmsg = Message.obtain(mainHandler, 0);
				mainHandler.sendMessage(retmsg);

			}
		}
	}

}