package whitepaper.smcall.fragment;

import whitepaper.smcall.R;
import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.remote.MatchInfo;
import whitepaper.smcall.voicechat.Voicechat;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VoicechattingFrag extends DialogFragment  {
	
	private static final String TAG = "VC_FRAG";
	private View mRoot;
	private	Voicechat vc;
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRoot = inflater.inflate(R.layout.voicechatting_frag, container, false);
		
		//return super.onCreateView(inflater, container, savedInstanceState);
		return mRoot;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		vc = new Voicechat(MatchInfo.match_private_Ip, 7771);
		//vc = new Voicechat("112.108.39.212", 7771);
		
		vc.RecvAudio();
		vc.SendMicAudio();	
						
	}	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		// Disable the back button
				OnKeyListener keyListener = new OnKeyListener() {
		 
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						Log.i(TAG, "key Event");
						if( keyCode == KeyEvent.KEYCODE_BACK){	
							Log.i(TAG, "back");
							vc.StopRecvAudio();
							vc.StopMicAudio();
							return true;
						}
						return false;
					}
		 
				
				};
		
		return super.onCreateDialog(savedInstanceState);
	}
	

}
