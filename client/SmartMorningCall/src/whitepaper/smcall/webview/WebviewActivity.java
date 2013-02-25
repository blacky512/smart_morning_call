package whitepaper.smcall.webview;

import whitepaper.smcall.R;
import whitepaper.smcall.R.layout;
import whitepaper.smcall.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class WebviewActivity extends Activity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		
		setLayout();
		
		
		// ���信�� �ڹٽ�ũ��Ʈ���డ��
		mWebView.getSettings().setJavaScriptEnabled(true); 
		// Ȩ������ ����
	    // WebViewClient ����
		mWebView.setWebViewClient(new WebViewClientClass()); 
	    mWebView.setWebChromeClient(new WebChromeClientClass());  
	    mWebView.addJavascriptInterface(new JavaScriptInterface(this), "android");
	    mWebView.loadUrl("http://112.108.39.254/smart_morning_call/server/" +
	    		"join.html");
	    
	}
	
	public class JavaScriptInterface{
		Context mContext;
		JavaScriptInterface(Context c){
		mContext=c;		
		}
		public void webview_finish(){
			finish();
		}
	}

	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) { 
            mWebView.goBack(); 
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
    }
    
	//WebChromeClientClass는 상속받은 아이들만 사용가능하게 껍데기 클래스만 만듭니다.
    private class WebChromeClientClass extends WebChromeClient { }
    
    //WebViewClientClass는 WebViewClient에서 shoudOverrideUrlLoading함수를 오버라이딩합니다.
    private class WebViewClientClass extends WebViewClient {
    	@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
	        view.loadUrl(url); 
	        return false; 
	    }

    }
	
	/*
	 * Layout
	 */
	private void setLayout(){
		mWebView = (WebView) findViewById(R.id.webview);
	}
}