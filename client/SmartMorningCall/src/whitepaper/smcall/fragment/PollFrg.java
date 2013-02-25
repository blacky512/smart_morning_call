package whitepaper.smcall.fragment;

import whitepaper.smcall.R;
import whitepaper.smcall.alarm.AlarmStr;
import whitepaper.smcall.remote.Jax;
import whitepaper.smcall.remote.MatchInfo;
import whitepaper.smcall.remote.Mjpage;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class PollFrg extends DialogFragment{
	
	private View mRoot;
	
	private Jax jax; 
	
	private RelativeLayout laybtnLike;
	private RelativeLayout laybtnDislike;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		setStyle(DialogFragment.STYLE_NO_FRAME, 
				android.R.style.Theme_Holo_Light_Dialog);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setCancelable(false);
		
		jax = new Jax();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRoot = inflater.inflate(R.layout.poll_frag, container, false);
		//return super.onCreateView(inflater, container, savedInstanceState);
		
		laybtnLike		= (RelativeLayout) mRoot.findViewById(R.id.laybtn_like);
		laybtnDislike	= (RelativeLayout) mRoot.findViewById(R.id.laybtn_dislike);
		
		laybtnLike.setOnClickListener(onClickListener);
		laybtnDislike.setOnClickListener(onClickListener);
		
		return mRoot;
	}
	
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.laybtn_like:
				sendResult(true);
				
				break;
				
			case R.id.laybtn_dislike:
				sendResult(false);				
				
				break;

			default:
				break;
			}
			
		}
	};
	
	public boolean sendResult(boolean like){
		
		String ret_like;
		
		if(like){
			ret_like = "1";
		}else{
			ret_like = "-1";
		}
		
		
		String[] values= {
				"id", AlarmStr.id,						// ID
				"opposite_id", MatchInfo.opposite_id,	// 상대 아이디
				"like", ret_like, 						// Like or Dislike (-1 ~ 1)
				"stamp", MatchInfo.stamp,				// 30초 이상 대화는 1, 미만은 0
				"x", MatchInfo.response					// 응답시간 (몇초만에 일어났나?)
		};
		jax.sendJson(Mjpage.poll, values);
		
		return true;
	}
	
}
