package kumon2014.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
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
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.message.KumonMessage;
import kumon2014.web.KumonJavascriptEventListenerInterface;
import kumon2014.web.KumonJavascriptInterface;
import kumon2014.webservice.KumonSoap;

public class WebGraphActivity extends BaseActivity {
	private static  String SF_GRAPH_PAGE = "/RecordTable.aspx";
	public static final int 	SF_RESULTCODE = 555;

	private CurrentUser 					mCurrentUser = null;
    private	WebView 						mWebview;
    private KumonJavascriptInterface				mJavascriptinterface = null;

    //20130324 ADD-S For ネットワーク接続確認
	private Timer 							m_Timer = null;
	private ControlbisibleTimerTask 		m_TimerTask = null;
	private	Handler 						mHandler = new Handler();
	private Context							mContext = null;
    //20130324 ADD-E
//	private ProgressDialog 					mProgressDialog;

    //@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
        	setContentView(R.layout.activity_webgraph);

    		//20150310 ADD-S For Web音声データ展開
    		File recfolder = StudentClientCommData.getRecordFolder(1);
    		Utility.deleteDirectry(recfolder);
    		File cmtfolder = StudentClientCommData.getSoundCommentFolder(1);
    		Utility.deleteDirectry(cmtfolder);
    		//20150310 ADD-E For Web音声データ展開

        	mContext = WebGraphActivity.this;

			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

//			mProgressDialog = new ProgressDialog(this);
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
	        url += SF_GRAPH_PAGE;
	    	url += "?login_id=" + mCurrentUser.mLoginID;

	        mWebview.loadUrl(url);

//	        setContentView(mWebview);



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
      //InitializeQuestionControl(false);
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

		//20150310 ADD-S For Web音声データ展開
		File recfolder = StudentClientCommData.getRecordFolder(1);
		Utility.deleteDirectry(recfolder);
		File cmtfolder = StudentClientCommData.getSoundCommentFolder(1);
		Utility.deleteDirectry(cmtfolder);
		//20150310 ADD-E For Web音声データ展開



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
	//20150413 ADD-S For 2015年度Ver. 未読対応
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

	    if(requestCode == WebGraphActivity.SF_RESULTCODE) {
	    	//20150413 ADD-S For 2015年度Ver. 未読対応
	    	KumonLoaderCallbacks<Boolean> callback = new KumonLoaderCallbacks<Boolean>() {
				@Override
				public Loader<Boolean> onCreateLoader(int arg0, Bundle arg1) {
					AsyncTaskLoader<Boolean> loader = new AsyncTaskLoader<Boolean>(WebGraphActivity.this) {
						@Override
						public Boolean loadInBackground() {
							KumonSoap soap = new KumonSoap();
					        //既読フラグ送信
							try {
								ArrayList<String> list = DataDBIO.DB_GetReadCommentDataList(mCurrentUser.mStudentID);
								soap.SoapSetUnreadFlg(mCurrentUser, list);
								DataDBIO.DB_ClearReadCommentDataList(mCurrentUser.mStudentID);
							} catch (Exception e) {
							}
							return true;
						}
					};
					loader.forceLoad();
					return loader;
				}
				@Override
				public void onLoadFinished(Loader<Boolean> arg0, Boolean arg1) {
					mWebview.reload();
				}
	    	};
	    	KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_GETREADCOMMENT, this, null, callback);
			//20150413 ADD-E For 2015年度Ver. 未読対応
	    }
	    else {
	    	//メモ画面以外は何もしない
	    }
	}
	//20150413 ADD-E For 2015年度Ver. 未読対応

    private void IsConnect()
    {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (StudentClientCommData.canConnect(cm) == false) {
			ScreenChange.doScreenChange(mContext, ScreenChange.SCNO_SPLASH, ScreenChange.SCNO_SPLASH, true, null, 0, ScreenChange.SF_NEXT);
		}
    }


	private void ShowStudyConfirm(String kyozaiId, String printsetid, String PrintUnitid)
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (StudentClientCommData.canConnect(cm) == true) {
			mCurrentUser.mCurrentKyozaiID = kyozaiId;

			//20150413 MOD-S For 2015年度Ver. 未読対応
			//学習結果画面からの戻りイベントを取得する為
			//ScreenChange.doScreenChangeGraph(mContext, mCurrentUser, kyozaiId, printsetid, PrintUnitid);

			Intent intent = new Intent(mContext, StudyConfirmActivity.class);
			intent.putExtra(ScreenChange.SF_FROMPAGE, ScreenChange.SCNO_GRAPH);
	        intent.putExtra("CurrentUser", mCurrentUser);
	        intent.putExtra(ScreenChange.SF_KYOZAIID, kyozaiId);
	        intent.putExtra(ScreenChange.SF_PRINTSETID, printsetid);
	        intent.putExtra(ScreenChange.SF_PRINTUNITID, PrintUnitid);
	        intent.putExtra(ScreenChange.SF_LEARNINGMODE, KumonDataCtrl.SF_DATATYPE_WEBVIEW);
	        intent.putExtra(ScreenChange.SF_RESTARTY, 0);

			startActivityForResult(intent, WebGraphActivity.SF_RESULTCODE);
			//20150413 MOD-E For 2015年度Ver. 未読対応

		}
		else {
			View.OnClickListener okListener = new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			};

			showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No10, 0, okListener);
		}
	}

	KumonJavascriptEventListenerInterface mJSEventListener = new KumonJavascriptEventListenerInterface() {
		// JavaScript Event
		@Override
		public void onLogIn(String id, String pswd) {
		}

		@Override
		public void onBack() {
			// ScreenChange.doScreenChange(this, ScreenChange.SCNO_GRAPH,
			// ScreenChange.SCNO_TOP, true, mCurrentUser, 0);
			finish();
		}

		@Override
		public void onDisplayPrint(String kyozaiId, String printSetId,
				String printUnitId) {
			if (m_Timer != null) {
				m_Timer.cancel();
				m_Timer = null;
			}
			if (m_TimerTask != null) {
				m_TimerTask = null;
			}
			// 20130415 MOD-S Bug Ui スレッド
			// ShowStudyConfirm(printSetId, printId);

			final String OnKyozaiid = kyozaiId;
			final String OnPrintsetid = printSetId;
			final String OnPrintUnitid = printUnitId;
			mHandler.post(new Runnable() {
				public void run() {
					ShowStudyConfirm(OnKyozaiid, OnPrintsetid, OnPrintUnitid);
				}
			});
			// 20130415 MOD-E
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
    		closeProgress();
//    		if(mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//    		}
    	}
    }
}
