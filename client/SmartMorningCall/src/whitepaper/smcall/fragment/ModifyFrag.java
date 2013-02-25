package whitepaper.smcall.fragment;

import whitepaper.smcall.R;
import whitepaper.smcall.remote.Mjpage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;


public class ModifyFrag extends DialogFragment{
	
	private View		mRoot;
	private WebView		webview;
	private ImageButton webviewCancel;
	
	public	Typeface			face;
	

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
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRoot = inflater.inflate(R.layout.join_frag, container, false);
		
		face = Typeface.createFromAsset(getActivity().getAssets(), "font/08SEOULNAMSANL.TTF");
		
		webview = (WebView) mRoot.findViewById(R.id.webView1);
				
		webview.getSettings().setJavaScriptEnabled(true); 
		webview.loadUrl(Mjpage.modify);				
		webview.setWebViewClient(new WebViewClient());			// WebViewClient ����
		
		webviewCancel = (ImageButton) mRoot.findViewById(R.id.wb_cancel);
		webviewCancel.setOnClickListener(onClickListener);
		
		return mRoot;
	}
	
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.wb_cancel:
				dismiss();
				break;

			default:
				break;
			}
		}
	};
}
