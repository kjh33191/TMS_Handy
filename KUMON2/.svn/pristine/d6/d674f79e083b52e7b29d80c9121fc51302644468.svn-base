package kumon2014.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonEnv;
import kumon2014.common.ScreenChange;
import kumon2014.database.log.SLog;
import kumon2014.web.KumonJavascriptEventListenerInterface;
import kumon2014.web.KumonJavascriptInterface;

public class WebNoticeActivity extends BaseActivity {
	private static  String SF_NOTICE_PAGE = "/Notice.aspx";

	private CurrentUser mCurrentUser = null;
	private	WebView 		mWebview;

    //@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");
	    	int mode = intent.getIntExtra(ScreenChange.SF_NOTICEMODE, ScreenChange.SF_NOTICEMODE_RECEIVE);

			//RedMine#2296 Android4.4の場合、同一SessionIDが振り出される不具合に対応
			android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
			cookieManager.removeAllCookie();
			//cookieManagerを使用してCookie内容をクリアする

	        mWebview = new WebView(this);
	        mWebview.setWebViewClient(new WebViewClient()
	        {
	            @Override
	            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
	            {
	                handler.proceed(KumonEnv.G_BASIC_ID, KumonEnv.G_BASIC_Password);
	            }
	        });

	        mWebview.getSettings().setJavaScriptEnabled(true);
	        mWebview.setWebChromeClient(new WebChromeClient(){});
	        mWebview.clearCache(true);

	        KumonJavascriptInterface javascriptinterface =  new KumonJavascriptInterface();
	        javascriptinterface.setEventListener(mJSEventListener);
	        mWebview.addJavascriptInterface(javascriptinterface, "Android");

	    	String url = KumonEnv.G_WEBPAGEURL_NORMAL;
	    	url += SF_NOTICE_PAGE;

	    	if(mode == ScreenChange.SF_NOTICEMODE_SEND) {
	    		url += "?page=Grade";
	    	}
	    	else {
	    		url += "?page=Kyozai";
	    	}
	    	url += "&login_id=" + mCurrentUser.mLoginID;
	    	mWebview.loadUrl(url);

	        setContentView(mWebview);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	System.gc();
    }

    KumonJavascriptEventListenerInterface mJSEventListener = new KumonJavascriptEventListenerInterface() {
		//JavaScript Event
    	@Override
		public void onBack() {
	    	//ScreenChange.doScreenChange(this, ScreenChange.SCNO_NOTICE, ScreenChange.SCNO_TOP, true, mCurrentUser, 0);
			finish();
		}
    	@Override
		public void onLogIn(String id, String pswd) {
		}
    	@Override
		public void onDisplayPrint(String kyozaiId, String printSetId, String printId) {
		}
    };

}
