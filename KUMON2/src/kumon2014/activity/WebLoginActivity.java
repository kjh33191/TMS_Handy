package kumon2014.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonEnv;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.DStudent;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.message.KumonMessage;
import kumon2014.web.KumonJavascriptEventListenerInterface;
import kumon2014.web.KumonJavascriptInterface;
import kumon2014.webservice.KumonSoap;

public class WebLoginActivity extends BaseActivity {
	private static  String SF_LOGIN_PAGE = "/Login.aspx";

	private CurrentUser 					mCurrentUser = null;
	private int 							mFromPage;
    private	WebView 						mWebview;

    //20130324 ADD-S For ネットワーク接続確認
	private Timer 							m_Timer = null;
	private ControlbisibleTimerTask 		m_TimerTask = null;
	private	Handler 						mHandler = new Handler();
    //20130324 ADD-E

    //@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getClass().getSimpleName()+"#onCreate", "thread = " + Thread.currentThread().getId());
        try {
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");
			if(mCurrentUser == null) {
				mCurrentUser = new CurrentUser();
			}
	        mFromPage = intent.getIntExtra(ScreenChange.SF_FROMPAGE, ScreenChange.SCNO_TOP);

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

	        KumonJavascriptInterface javascriptinterface = new KumonJavascriptInterface();
	        javascriptinterface.setEventListener(mJSEventListener);
	        mWebview.addJavascriptInterface(javascriptinterface, "Android");
	        String url = KumonEnv.G_WEBPAGEURL_LOGIN;
	        url += SF_LOGIN_PAGE;
	        mWebview.loadUrl(url);

	        setContentView(mWebview);

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
    public void onDestroy() {
    	super.onDestroy();

		if(m_Timer != null) {
			m_Timer.cancel();
	    	m_Timer = null;
		}
		if(m_TimerTask != null) {
			m_TimerTask = null;
		}
		mHandler = null;
    	System.gc();
    }

    private void IsConnect()
    {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (StudentClientCommData.canConnect(cm) == false) {
			ScreenChange.doScreenChange(this, ScreenChange.SCNO_SPLASH, ScreenChange.SCNO_SPLASH, true, null, 0, ScreenChange.SF_NEXT);
		}
    }

    @UiThread
	private void LoginStart(String id, String pswd)
	{
//		mProgressDialog = new ProgressDialog(this);
//		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		mProgressDialog.setIndeterminate(false);
//		mProgressDialog.setCancelable(false);
        Log.d(this.getClass().getSimpleName()+"#LoginStart", "thread = " + Thread.currentThread().getId());
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No29);

//		CurrentUser user = new CurrentUser();
//		user.mLoginID = id;
//		user.mPassword = pswd;
		Bundle arg = new Bundle();
		arg.putString("LoginID", id);
		arg.putString("Password", pswd);
//		(new SoapTaskLoader()).execute(user);

		KumonLoaderCallbacks<DStudent> callback = new KumonLoaderCallbacks<DStudent>() {

			@Override
			public Loader<DStudent> onCreateLoader(int arg0, Bundle arg1) {
				CurrentUser user = new CurrentUser();
				user.mLoginID = arg1.getString("LoginID");
				user.mPassword = arg1.getString("Password");
				Loader<DStudent> loader = new SoapTaskLoader(WebLoginActivity.this, user);
				loader.forceLoad();
				return loader;
			}

			@Override
			public void onLoadFinished(Loader<DStudent> arg0, DStudent student) {
	    		//20130415 MOD-S Bug Ui スレッド
	    		//LoginEnd(student);

	    		final DStudent studentx = student;
	    		LoginEnd(studentx);
	    		//20130415 MOD-E
			}

		};
		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_LOGIN, this, arg, callback);
	}
	private void LoginEnd(DStudent student)
	{
		if(student.mSoapStatus == 0) {
			mCurrentUser.mStudentName = student.mName;
			mCurrentUser.mLoginID = student.mStudentID;
			mCurrentUser.mPassword = student.mPassword;
			mCurrentUser.mStudentID = student.mStudentAdminID;

			//20130324 ADD-S For ペンの太さは前回使用の太さをキープ
			mCurrentUser.mPenWidth = student.mPenThickness;
			//20130324 ADD-E
			mCurrentUser.writeObject();

	    	DataDBIO.DB_SaveStudent(student);

	    	//20130403 ADD-S No.94対応
			//KumonDataCtrl.GetKyozaiPrintSetList(mCurrentUser.mCurrentStudentID);
    		KumonDataCtrl.ClearKyozaiPrintSetList();
			KumonDataCtrl.GetKyozaiDataExistList(mCurrentUser.mStudentID);
	    	//20130403 ADD-E

//			if(mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//			}
			closeProgress();

			//TopMenu
	    	ScreenChange.doScreenChange(this, ScreenChange.SCNO_START, ScreenChange.SCNO_TOP, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
			return;
		}
		else if(student.mSoapError.isEmpty() ) {
//			if(mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//			}
			closeProgress();

			String msg2 = KumonMessage.getKumonMessage(KumonMessage.MSG_No3);
			if(msg2 == "") {
				msg2 = "IDかパスワードが違います。";
			}
			setErrMsg(msg2);
		}
		else {
//			if(mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//			}
			closeProgress();
			setErrMsg(student.mSoapError);
		}
	}
	private void setErrMsg(String msg) {
		mWebview.loadUrl("javascript:F_SetFailure('" + msg + "')" );
	}

	KumonJavascriptEventListenerInterface mJSEventListener = new KumonJavascriptEventListenerInterface() {
		//JavaScript Event
		@Override
		public void onLogIn(final String id, final String pswd) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					LoginStart(id, pswd);
				}

			});
		}

		@Override
		public void onBack() {
			if(m_Timer != null) {
				m_Timer.cancel();
		    	m_Timer = null;
			}
			if(m_TimerTask != null) {
				m_TimerTask = null;
			}
			mHandler = null;
			ScreenChange.doScreenChange(WebLoginActivity.this, ScreenChange.SCNO_LOGIN, mFromPage, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
		}

		@Override
		public void onDisplayPrint(String kyozaiId, String printSetId, String printId) {
		}
	};

    private static class SoapTaskLoader extends AsyncTaskLoader</*CurrentUser, Integer, */DStudent> {

    	CurrentUser mUser;
    	public SoapTaskLoader(Context context, CurrentUser user) {
    		super(context);
    		mUser = user;
    	}
/*
    	// UI スレッドではない
    	// バックグラウンドでダウンロード処理を行う
    	@Override
    	protected DStudent doInBackground(CurrentUser... user) {
    		KumonSoap soap = new KumonSoap();
    		DStudent student = soap.SoapLogin(user[0]);

    		return student;
    	}
    	// UI スレッド
    	// プログレスバーの表示を更新する
    	@Override
    	protected void onProgressUpdate(Integer... progress) {
    	}

    	// UI スレッド
    	// ダイアログを表示する
    	@Override
    	protected void onPostExecute(DStudent student) {
    		//20130415 MOD-S Bug Ui スレッド
    		//LoginEnd(student);

    		final DStudent studentx = student;
    		mHandler.post( new Runnable() {
    			public void run() {
    				LoginEnd(studentx);
    			}
    		});
    		//20130415 MOD-E

    	}
    	*/
		@Override
		public DStudent loadInBackground() {
    		KumonSoap soap = new KumonSoap();
    		DStudent student = soap.SoapLogin(mUser);

    		return student;
		}
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
}
