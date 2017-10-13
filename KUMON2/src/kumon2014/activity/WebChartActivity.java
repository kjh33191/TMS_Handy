package kumon2014.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonEnv;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.database.log.SLog;
import kumon2014.message.KumonMessage;
import kumon2014.web.KumonJavascriptEventListenerInterface;
import kumon2014.web.KumonJavascriptInterface;

public class WebChartActivity extends BaseActivity {
	private static  String SF_CHART_PAGE = "/CandleGraph.aspx";

	private CurrentUser 					mCurrentUser = null;
    private	WebView 						mWebview;
    private KumonJavascriptInterface		mJavascriptinterface = null;

	private Timer 							m_Timer = null;
	private ControlbisibleTimerTask 		m_TimerTask = null;
	private	Handler 						mHandler = new Handler();
	private Context							mContext = null;

//	private ProgressDialog 					mProgressDialog;

    //@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
        	setContentView(R.layout.activity_webchart);

        	mContext = WebChartActivity.this;

			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

	//		mProgressDialog = new ProgressDialog(this);
//			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			mProgressDialog.setIndeterminate(false);
//			mProgressDialog.setCancelable(false);
//			KumonMessage.showProgress(mProgressDialog, this, KumonMessage.MSG_No21);
			showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

			//RedMine#2296 Android4.4の場合、同一SessionIDが振り出される不具合に対応
			android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
			cookieManager.removeAllCookie();
			//cookieManagerを使用してCookie内容をクリアする

			mWebview = (WebView) findViewById(R.id.WebViewMain);
			//mWebview = new WebView(this);
	        mWebview.setWebViewClient(new ViewClient()
	        {
	        	// Basei　認証
	            @Override
	            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
	            {
	                handler.proceed(KumonEnv.G_BASIC_ID, KumonEnv.G_BASIC_Password);
	            }
	        });

	        mWebview.getSettings().setJavaScriptEnabled(true);
	        mWebview.setWebChromeClient(new WebChromeClient(){});
	        mWebview.clearCache(true);
	        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

	        mJavascriptinterface =  new KumonJavascriptInterface();
	        mJavascriptinterface.setEventListener(mJSEventListener);
	        mWebview.addJavascriptInterface(mJavascriptinterface, "Android");
	        String url = KumonEnv.G_WEBPAGEURL_NORMAL;
	        url += SF_CHART_PAGE;
	    	url += "?login_id=" + mCurrentUser.mLoginID;
	    	url += "&student_id=" + mCurrentUser.mStudentID;

	        mWebview.loadUrl(url);

	        //タイマーの初期化処理
	        m_TimerTask = new ControlbisibleTimerTask();
			m_Timer = new Timer(true);
			m_Timer.schedule(m_TimerTask, 10, 10000);

        }
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    @Override
	protected void onRestart() {
    	super.onRestart();
    }
    @Override
    protected void onStart() {
    	super.onStart();
    }
    @Override
    protected void onResume() {
    	super.onResume();
    }
    @Override
    protected void onPause () {
        super.onPause ();
    }
    @Override
    protected void onStop() {
    	super.onStop();
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
		if(m_Timer != null) {
			m_Timer.cancel();
	    	m_Timer = null;
		}
		if(m_TimerTask != null) {
			m_TimerTask = null;
		}
		mHandler = null;

        mJavascriptinterface.setEventListener(null);
        mJavascriptinterface = null;

        mWebview.stopLoading();
        mWebview.setWebChromeClient(null);
        mWebview.setWebViewClient(null);
        unregisterForContextMenu(this.mWebview);
//        mWebview.freeMemory();
        mWebview.destroy();
        mWebview = null;

    	System.gc();
    }
    @Override
    public void onLowMemory() {
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }

    private void IsConnect()
    {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (StudentClientCommData.canConnect(cm) == false) {
			ScreenChange.doScreenChange(mContext, ScreenChange.SCNO_SPLASH, ScreenChange.SCNO_SPLASH, true, null, 0, ScreenChange.SF_NEXT);
		}
    }

    KumonJavascriptEventListenerInterface mJSEventListener = new KumonJavascriptEventListenerInterface() {
		//JavaScript Event
    	@Override
		public void onLogIn(String id, String pswd) {
		}
    	@Override
		public void onBack() {
	    	//ScreenChange.doScreenChange(this, ScreenChange.SCNO_GRAPH, ScreenChange.SCNO_TOP, true, mCurrentUser, 0);
			finish();
		}
    	@Override
		public void onDisplayPrint(String kyozaiId, String printSetId, String printId) {
		}
    };

    public void onClickBack(View view){
		finish();
	}

    //*************************************************************
    class ControlbisibleTimerTask extends TimerTask{
    	@Override
    	public void run() {
    		mHandler.post( new Runnable() {
    			public void run() {
    				IsConnect();
    			}
    		});
    	}
    }

    //WebViewClientを継承
    public class ViewClient extends WebViewClient {
    	@Override
    	public void onPageFinished(WebView view , String url){

    		//ロード完了時にやりたい事を書く
//    		if(mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//    		}
    		closeProgress();
    	}
    }
}
