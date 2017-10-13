package kumon2014.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.support.annotation.WorkerThread;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonEnv;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.DebugToast;
import kumon2014.common.MyTimingLogger;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.data.TempDataDBIO;
import kumon2014.database.env.EnvDBIO;
import kumon2014.database.log.SLog;
import kumon2014.database.master.MastDBIO;
import kumon2014.kumondata.DStudent;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.message.KumonMessage;
import kumon2014.web.ModuleUpdate;
import kumon2014.webservice.KumonSoap;
import net.sqlcipher.database.SQLiteDatabase;

public class SplashActivity extends BaseActivity {
	private CurrentUser 				mUser;
	private InitTimerTask 				m_TimerTask = null;
	private Timer 						m_Timer = null;
	private InitTimerTask2 				m_TimerTask2 = null;
	private Timer 						m_Timer2 = null;

	//20140611 ADD-S For LEAK
	private LeakTimerTask 				m_LeakTimerTask = null;
	private Timer 						m_LeakTimer = null;
//	private ProgressDialog				m_ProgressDialog = null;
	//20140611 ADD-E For LEAK

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyTimingLogger logger = new MyTimingLogger("onCreate");
        try {
        	logger.addSplit("Start");
//	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_splash);
/*
	        // SQLCipherライブラリのイニシャルロード
	        SQLiteDatabase.loadLibs(getApplicationContext());
	        SLog.Open(getApplicationContext());
	        EnvDBIO.Open(getApplicationContext());
	        MastDBIO.Open(getApplicationContext());
	        DataDBIO.Open(getApplicationContext());
			//20140618 ADD-S For 分割受信
        	logger.addSplit("TempDataDBIO.Open");
	        TempDataDBIO.Open(getApplicationContext());
			//20140618 ADD-E For 分割受信
        	logger.addSplit("KumonEnv.Init");
	        KumonEnv.Init();


	    	// 20140722 ADD-S For InkDataが消える
        	logger.addSplit("StudentClientCommData.clearInkLog");
	        StudentClientCommData.clearInkLog();
	    	// 20140722 ADD-E For InkDataが消える
	        
	        //タイマーの初期化処理
	        m_TimerTask = new InitTimerTask();
			m_Timer = new Timer(true);
			m_Timer.schedule(m_TimerTask, 100);
*/
	        KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_INITIALTASK, this, null, mInitialTaskLoaderCallback);

			logger.dumpToLog();
        }
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
	
	static class InitialTask extends AsyncTaskLoader<Boolean>
	{
		public InitialTask(Context context) {
			super(context);
		}
		@Override
		public Boolean loadInBackground() {
			MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName());
			
	        // SQLCipherライブラリのイニシャルロード
	        SQLiteDatabase.loadLibs(getContext().getApplicationContext());
	        SLog.Open(getContext().getApplicationContext());
	        EnvDBIO.Open(getContext().getApplicationContext());
	        MastDBIO.Open(getContext().getApplicationContext());
	        DataDBIO.Open(getContext().getApplicationContext());
			//20140618 ADD-S For 分割受信
	        TempDataDBIO.Open(getContext().getApplicationContext());
			//20140618 ADD-E For 分割受信
	        logger.addSplit("DB Open");
	        KumonEnv.Init();
	        logger.addSplit("KumonEnv.init");

	    	// 20140722 ADD-S For InkDataが消える
	        StudentClientCommData.clearInkLog();
	        logger.addSplit("clearInkLog");
	    	// 20140722 ADD-E For InkDataが消える
	        logger.dumpToLog();
			return true;
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
		if(m_Timer2 != null) {
			m_Timer2.cancel();
	    	m_Timer2 = null;
		}
		if(m_TimerTask2 != null) {
			m_TimerTask2 = null;
		}

		if (mInitialTaskLoaderCallback != null) {
			mInitialTaskLoaderCallback = null;
		}
    	Utility.cleanupView(findViewById(R.id.splash_topview));
    	System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    static class DownloadHandler extends ClickHandler<SplashActivity>  {
		public DownloadHandler(SplashActivity act) {
			super(act);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				// MIME type設定
				intent.setDataAndType(Uri.fromFile(new File(ModuleUpdate.SF_DOWNAPK)), "application/vnd.android.package-archive");
				// Intent発行
				mActivityRef.get().startActivity(intent);
				mActivityRef.get().finish();
			}
			else {
				mActivityRef.get().SystemInit();
			}
		}
    	
    };
    static class VersionCheckHandler extends ClickHandler<SplashActivity> {
    	VersionCheckHandler(SplashActivity activity) {
    		super(activity); 
    	}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(msg.what == 1 /*true*/) {
				View.OnClickListener yesListener = new View.OnClickListener() {
					public void onClick(View v) {
						DownloadHandler handler = new DownloadHandler(mActivityRef.get());
						ModuleUpdate.DownLoadApk(handler, mActivityRef.get());
					}
				};
				View.OnClickListener noListener = new View.OnClickListener() {
					public void onClick(View v) {
						mActivityRef.get().m_TimerTask2 = mActivityRef.get().new InitTimerTask2();
						mActivityRef.get().m_Timer2 = new Timer(true);
						mActivityRef.get().m_Timer2.schedule(mActivityRef.get().m_TimerTask2, 100);
					}
				};
				mActivityRef.get().showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No80, 0, yesListener, 0, noListener);
			}
			else {
				mActivityRef.get().SystemInit();
			}
			
		}
    	
    }
	private void VersionCheck()
	{
		//20140611 ADD-S For LEAK
		File leakFile = StudentClientCommData.getLeakFile();
		if(leakFile.exists()) {
			leakFile.delete();
//			m_ProgressDialog = new ProgressDialog(this);
//			m_ProgressDialog.setTitle("Clean Up");
	        // プログレスダイアログのメッセージを設定します
//			m_ProgressDialog.setMessage("お待ちください…");
	        // プログレスダイアログの確定（false）／不確定（true）を設定します
//			m_ProgressDialog.setIndeterminate(true);
	        // プログレスダイアログのスタイルを円スタイルに設定します
//			m_ProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        // プログレスダイアログのキャンセルが可能かどうかを設定します
//			m_ProgressDialog.setCancelable(false);
	        // プログレスダイアログを表示します
//			m_ProgressDialog.show();
			showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No79);

			m_LeakTimerTask = new LeakTimerTask();
			m_LeakTimer = new Timer(true);
			m_LeakTimer.schedule(m_LeakTimerTask, 100);
			return;
		}
		//20140611 ADD-E For LEAK

		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		
		VersionCheckHandler handler = new VersionCheckHandler(this);
		
		if(StudentClientCommData.canConnect(cm) == true) {
			PackageManager packageManager = this.getPackageManager();
			try {
				PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
				int versionCode = packageInfo.versionCode;
				ModuleUpdate.VersionCheck(versionCode, handler, this);
			} catch (Exception e) {
			}
		}
		else {
			m_TimerTask2 = new InitTimerTask2();
			m_Timer2 = new Timer(true);
			m_Timer2.schedule(m_TimerTask2, 100);
		}
	}
	private void SystemInit()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);


		int nextActivity = ScreenChange.SCNO_NO;
		File startFile = StudentClientCommData.getStartFile();

		if(startFile.exists() && KumonEnv.G_API_WEBSERVICEURL.isEmpty() == false) {
			mUser = CurrentUser.readObject();
			if(mUser != null && mUser.mStudentID != null && mUser.mStudentID.isEmpty() == false) {
	    		KumonDataCtrl.ClearKyozaiPrintSetList();
				KumonDataCtrl.GetKyozaiDataExistList(mUser.mStudentID);

				//１回以上起動した事がある
				if(StudentClientCommData.canConnect(cm) == true) {
					//20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
					/***
					//ログイン確認後遷移
		    		KumonSoap soap = new KumonSoap();
		    		DStudent student = soap.SoapLogin(mUser);
		    		LoginEnd(student);
					return;
					***/
				    KumonLoaderCallbacks<LoginCheck> maintenanceCallback = new KumonLoaderCallbacks<LoginCheck>() {    
				  	   
						@Override
						public Loader<LoginCheck> onCreateLoader(int arg0, Bundle arg1) {
							Loader<LoginCheck> loader = new AsyncTaskLoader<LoginCheck>(SplashActivity.this) {

								@Override
								public LoginCheck loadInBackground() {
									MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName());
									LoginCheck lc = new LoginCheck();
									lc.isMaintenance = maintenanceCheck();
									logger.addSplit("maintenanceCheck");
									if (!lc.isMaintenance) {
										KumonSoap soap = new KumonSoap();
										lc.student = soap.SoapLogin(mUser);
										logger.addSplit("SoapLogin");
									}
									logger.dumpToLog();
									return lc;
								}
								
							};
							loader.forceLoad();
							return loader;
						}
					
						@Override
						public void onLoadFinished(Loader<LoginCheck> arg0, LoginCheck arg1) {
							if(arg1.isMaintenance) {
								//メンテナンス中の為、ログイン不要
								DebugToast.makeText(SplashActivity.this, "Maintenancing", Toast.LENGTH_SHORT);
								ScreenChange.doScreenChange(SplashActivity.this, ScreenChange.SCNO_START, ScreenChange.SCNO_TOP, true, mUser, 0, ScreenChange.SF_NEXT);
								return;
							}
							else {
								LoginEnd(arg1.student);
								return;
							}
						}
					
				    };
					
			        KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_MAINTENANCECHECK, SplashActivity.this, null, maintenanceCallback);
			        return;
				}
				else {
					ScreenChange.doScreenChange(this, ScreenChange.SCNO_START, ScreenChange.SCNO_TOP, true, mUser, 0, ScreenChange.SF_NEXT);
					return;
				}
			}
			else {
				//前回ログインなし
				nextActivity = ScreenChange.SCNO_START;
			}
		}
		else {
			//初回起動
			nextActivity = ScreenChange.SCNO_START;
		}
		ScreenChange.doScreenChange(this, ScreenChange.SCNO_START, nextActivity, true, null, 0, ScreenChange.SF_NEXT);
	}

	static class LoginCheck {
		public boolean isMaintenance;
		public DStudent student; 
	}
	
	//20140611 ADD-S For LEAK
	private void LeakInit() {
		mUser = CurrentUser.readObject();
		KumonDataCtrl.ClearKyozaiPrintSetList();
		KumonDataCtrl.GetKyozaiDataExistList(mUser.mStudentID);
		closeProgress();
//		if(m_ProgressDialog != null) {
//			m_ProgressDialog.dismiss();
//			m_ProgressDialog = null;
//		}
		ScreenChange.doScreenChange(this, ScreenChange.SCNO_START, ScreenChange.SCNO_TOP, true, mUser, 0, ScreenChange.SF_NEXT);
	}
	//20140611 ADD-E For LEAK

	private void LoginEnd(DStudent student)
	{
		MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName()+"#LoginEnd");
		int nextActivity = ScreenChange.SCNO_NO;
		if(student.mSoapStatus == 0 && student.mSoapError.isEmpty()) {
			DataDBIO.DB_SaveStudent(student);
			nextActivity = ScreenChange.SCNO_TOP;
			logger.addSplit("DataDBIO.DB_SaveStudent");
		}
		else {
			//Login
			nextActivity = ScreenChange.SCNO_LOGIN;
		}
		ScreenChange.doScreenChange(this, ScreenChange.SCNO_START, nextActivity, true, mUser, 0, ScreenChange.SF_NEXT);
		logger.addSplit("doScreenChange");
		logger.dumpToLog();
	}
	
	/**
	 * メンテナンス中確認
	 * UIスレッドからは呼ばないこと
	 * @return
	 */
	@WorkerThread
	private boolean maintenanceCheck() {
		boolean stat = false;

		KumonSoap soap = new KumonSoap();
		stat = soap.SoapMaintenanceCheck();
		return stat;
	}

    //*************************************************************
    class InitTimerTask extends TimerTask{
    	@Override
    	public void run() {
    		runOnUiThread(new Runnable() {
    			public void run() {
    	    		VersionCheck();
    			}
    		});
    	}
    }
    class InitTimerTask2 extends TimerTask{
    	@Override
    	public void run() {
    		runOnUiThread(new Runnable() {
    			public void run() {
    				SystemInit();
    			}
    		});
    	}
    }

	//20140611 ADD-S For LEAK
    class LeakTimerTask extends TimerTask{
    	@Override
    	public void run() {
    		runOnUiThread(new Runnable() {
    			public void run() {
    				LeakInit();
    			}
    		});
    	}
    }
	//20140611 ADD-E For LEAK

    /**
     * InitialTaskのコールバック
     */
    KumonLoaderCallbacks<Boolean> mInitialTaskLoaderCallback = new KumonLoaderCallbacks<Boolean>() {    
		@Override
		public Loader<Boolean> onCreateLoader(int arg0, Bundle arg1) {
			Loader<Boolean> loader = new InitialTask(SplashActivity.this);
			loader.forceLoad();
			return loader;
		}
	
		@Override
		public void onLoadFinished(Loader<Boolean> arg0, Boolean arg1) {
	        //タイマーの初期化処理
	        m_TimerTask = new InitTimerTask();
			m_Timer = new Timer(true);
			m_Timer.schedule(m_TimerTask, 100);
		}
    };

}


