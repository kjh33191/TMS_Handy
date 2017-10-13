package kumon2014.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.AsyncTaskLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonCommon;
import kumon2014.common.KumonEnv;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.KumonLog;
import kumon2014.common.MyTimingLogger;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.data.TempDataDBIO;
import kumon2014.database.log.SLog;
import kumon2014.database.master.MQuestion2;
import kumon2014.database.master.MastDBIO;
import kumon2014.kumondata.DPrintSet;
import kumon2014.kumondata.DResultData;
import kumon2014.kumondata.DStudent;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.kumondata.WDownloadPrintSetIDList;
import kumon2014.kumondata.WDownloadResultData;
import kumon2014.kumondata.WQuestionDataList;
import kumon2014.message.KumonMessage;
import kumon2014.web.ModuleUpdate;
import kumon2014.webservice.CollectAndroidLogResponse;
import kumon2014.webservice.KumonSoap;
import kumon2014.webservice.RegistGradingResultResponse;

public class TopMenuActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;
	private TextView mTextviewVersion;
	private TextView mTextviewName;
	private ImageButton mImagebuttonReceive;
	private ImageButton mImagebuttonLearning;
	private ImageButton mImagebuttonSend;
	private ImageButton mImagebuttonStatus;
	private ImageButton mImagebuttonGraph;
	private ImageButton mImagebuttonChart;

	private Timer mTimer = null;
	private ControlVisibleTimerTask mTimerTask = null;
	private Handler mHandler = new Handler();

	private Timer mSuspendTimer = null;
	private SuspendTimerTask mSuspendTimerTask = null;
	private Handler mSuspendTimerHandler = new Handler();

	private int mCounter = 0;
//	private ProgressDialog mProgressDialog;
//	private ProgressDialog mProgressDialogCancel;

	// 20140916 ADD-S For 学習再開時は、メモリチェックをしない
	private boolean mLasttest = false;
	// 20140916 ADD-E For 学習再開時は、メモリチェックをしない

	// 20150409 ADD-S For 2015年度Ver. 未読コメント
	// private Context mContext;
	private boolean mDoUnreadCheck;

	// 20150409 ADD-E For 2015年度Ver. 未読コメント

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName()+"#onCreate");
		try {
			setContentView(R.layout.activity_topmenu);

			Intent intent = getIntent();
			mCurrentUser = (CurrentUser) intent
					.getSerializableExtra("CurrentUser");
			if (mCurrentUser == null) {
				ScreenChange.doScreenChange(this, ScreenChange.SCNO_TOP,
						ScreenChange.SCNO_SPLASH, true, null, 0,
						ScreenChange.SF_NEXT);
				return;
			}
			mTextviewVersion = (TextView) findViewById(R.id.textview_Version);
			mTextviewName = (TextView) findViewById(R.id.textview_Name);
			mImagebuttonReceive = (ImageButton) findViewById(R.id.imagebutton_receive);
			mImagebuttonLearning = (ImageButton) findViewById(R.id.imagebutton_learning);
			mImagebuttonSend = (ImageButton) findViewById(R.id.imagebutton_send);
			mImagebuttonStatus = (ImageButton) findViewById(R.id.imagebutton_status);
			mImagebuttonGraph = (ImageButton) findViewById(R.id.imagebutton_graph);
			mImagebuttonChart = (ImageButton) findViewById(R.id.imagebutton_chart);

			String version = "";
			PackageManager packageManager = getPackageManager();
			try {
				PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
				version = "Ver" + packageInfo.versionName;
			} catch (Exception e) {
			}
			logger.addSplit("PackageManager#getPackageInfo");
			mTextviewVersion.setText(version);
			mTextviewName.setText(mCurrentUser.mStudentName);

			// この画面まで来たので最低１度はログイン成功
			File startFile = StudentClientCommData.getStartFile();
			try {
				FileOutputStream fOS = new FileOutputStream(startFile);
				fOS.close();
			} catch (Exception e) {
			}
			logger.addSplit("write StartFile");

			// タイマーの初期化処理
			mSuspendTimerTask = new SuspendTimerTask();
			mSuspendTimer = new Timer(true);
			mSuspendTimer.schedule(mSuspendTimerTask, 10);
			logger.addSplit("schedule timer");

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		finally {
			logger.dumpToLog();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mCurrentUser != null) {
			mCurrentUser = null;
		}
		mCurrentUser = CurrentUser.readObject();
		if (mCurrentUser == null) {
			ScreenChange.doScreenChange(this, ScreenChange.SCNO_TOP,
					ScreenChange.SCNO_SPLASH, true, null, 0,
					ScreenChange.SF_NEXT);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 20140916 ADD-S For 学習再開時は、メモリチェックをしない
		if (mLasttest == false) {
			// 20140916 ADD-E For 学習再開時は、メモリチェックをしない

			// 20140611 ADD-S For LEAK
			long memory = Runtime.getRuntime().totalMemory();
			Log.d(getClass().getSimpleName()+"#onResume()", "totalMemory="+memory/1024+",freeMemory="+Runtime.getRuntime().freeMemory()/1024+",maxMemory="+Runtime.getRuntime().maxMemory()/1024);
			if (memory >= /*25000000*/ 200000000) {
				File leakFile = StudentClientCommData.getLeakFile();
				try {
					FileOutputStream fOS = new FileOutputStream(leakFile);
					fOS.close();
				} catch (Exception e) {
				}

				finish();
				DoOnDestroy();
				startActivity((new Intent(TopMenuActivity.this, SplashActivity.class)));
				System.exit(0);
			}
			// 20140611 ADD-E For LEAK

			// 20140916 ADD-S For 学習再開時は、メモリチェックをしない
		}
		// 20140916 ADD-E For 学習再開時は、メモリチェックをしない

		if (mTimerTask == null) {
			mTimerTask = new ControlVisibleTimerTask();
		}
		if (mTimer == null) {
			// 20150409 ADD-S For 2015年度Ver. 未読コメント
			mDoUnreadCheck = true;
			// 20150409 ADD-E For 2015年度Ver. 未読コメント
			mTimer = new Timer(true);
			mTimer.schedule(mTimerTask, 10, 60000);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask = null;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		DoOnDestroy();
	}

	private void DoOnDestroy() {
		Log.e(getString(R.string.app_name), "!!!!! TOPMENYU onDestroy()");

//		if (mProgressDialog != null) {
//			mProgressDialog.dismiss();
//			mProgressDialog = null;
//		}
		closeProgress();

		KumonDataCtrl.ClearKyozaiPrintSetList();

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask = null;
		}
		mHandler = null;

		Utility.cleanupView(findViewById(R.id.topmenu_topview));
		System.gc();
	}

	@Override
	public void onLowMemory() {
		Utility.onLowMemory(getApplicationContext());
		SLog log = new SLog();
		log.mSource = "TopMenuActivity";
		log.mMessage = "onLowMemory";
		SLog.DB_AddWarning(log);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public class AlertFragment extends DialogFragment {
		@SuppressLint("InflateParams")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// レイアウトの呼び出し
	        boolean isDebug = Utility.isDebugBuild(TopMenuActivity.this);

			LayoutInflater factory = LayoutInflater.from(TopMenuActivity.this);
			final View inputView = factory.inflate(
					R.layout.inputdialog_password, null);
			final EditText edittext = (EditText) inputView
					.findViewById(R.id.dialog_edittext);
			if (isDebug) {
				edittext.setText(KumonEnv.G_PASS);
			}
			else
				edittext.setText("");

			// ダイアログの作成(AlertDialog.Builder)
			return new AlertDialog.Builder(TopMenuActivity.this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("PASSWORD")
					.setView(inputView)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (edittext.getText().toString()
											.equalsIgnoreCase(KumonEnv.G_PASS)) {
										ScreenChange.doScreenChange(
												TopMenuActivity.this,
												ScreenChange.SCNO_START,
												ScreenChange.SCNO_SETTING,
												true, mCurrentUser, 0,
												ScreenChange.SF_NEXT);
									}
									edittext.setText("");
								}
							}).create();
		}
	}

	/**
	 * 「受け取る」クリック
	 * @param view
	 */
	public void onClickReceive(View view) {
		View.OnClickListener yesListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// タイマーは止めておかないと通信中にさらに定期通信する
				if (mTimer != null) {
					mTimer.cancel();
					mTimer = null;
				}
				if (mTimerTask != null) {
					mTimerTask = null;
				}

				showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

				ClickHandler<TopMenuActivity> handler = new ClickHandler<TopMenuActivity>(TopMenuActivity.this) {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						closeProgress();
						if (msg.what == 0) {
							mActivityRef.get().VersionCheck(false);
						}
					}
				};
				maintenanceCheck(handler);
				// 20150110 MOD-E For 2015年度Ver. メンテナンス中チェック
			}
		};
		showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No31, 0, yesListener, 0, null);

	}

	/**
	 * 「はじめる」クリック
	 * @param view
	 */
	public void onClickLearning(View view) {
		try {
			mImagebuttonLearning.setClickable(false);
			ScreenChange.doScreenChange(this, ScreenChange.SCNO_TOP,
					ScreenChange.SCNO_LIST, false, mCurrentUser, 0,
					ScreenChange.SF_NEXT);
			mImagebuttonLearning.setClickable(true);
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	/**
	 * 「送る」クリック
	 * @param view
	 */
	public void onClickSend(View view) {
		try {
			mImagebuttonSend.setClickable(false);
			showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);
			// 20140605 ADD-S For Bug
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			if (mTimerTask != null) {
				mTimerTask = null;
			}
			// 20140605 ADD-E For Bug

			// 20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
			Handler handler = new ClickHandler<TopMenuActivity>(TopMenuActivity.this) {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					closeProgress();
					mActivityRef.get().mImagebuttonSend.setClickable(true);

					if (msg.what == 1) {
					}
					else {
						mActivityRef.get().VersionCheck(true);
					}
				}

			};
			maintenanceCheck(handler);
			// 20150110 ADD-E For 2015年度Ver. メンテナンス中チェック

		} catch (Exception e) {
			SLog.DB_AddException(e);
			mImagebuttonSend.setClickable(true);
		}
	}

	/**
	 * 「採点箱を見る」クリック
	 * @param view
	 */
	public void onClickStatus(View view) {

		mImagebuttonStatus.setClickable(false);
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

		// 20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
		Handler handler = new ClickHandler<TopMenuActivity>(TopMenuActivity.this) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				closeProgress();
				mActivityRef.get().mImagebuttonStatus.setClickable(true);
				if (msg.what == 1) {
				}
				else {
					ScreenChange.doScreenChange(mActivityRef.get(), ScreenChange.SCNO_TOP,
							ScreenChange.SCNO_GRADESTATUS, false, mCurrentUser, 0,
							ScreenChange.SF_NEXT);
				}
			}

		};

		maintenanceCheck(handler);
		// 20150110 ADD-E For 2015年度Ver. メンテナンス中チェック
	}

	/**
	 * 「成績表」クリック
	 * @param view
	 */
	public void onClickGraph(View view) {
		try {
			mImagebuttonGraph.setClickable(false);
			showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

			// 20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
			ClickHandler<TopMenuActivity> handler = new ClickHandler<TopMenuActivity>(TopMenuActivity.this) {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					closeProgress();
					mActivityRef.get().mImagebuttonGraph.setClickable(true);
					if (msg.what == 1) {
					}
					else {
						ConnectivityManager cm = (ConnectivityManager) mActivityRef.get().getSystemService(CONNECTIVITY_SERVICE);
						if (StudentClientCommData.canConnect(cm) == true) {
							ScreenChange.doScreenChange(mActivityRef.get(), ScreenChange.SCNO_TOP,
									ScreenChange.SCNO_GRAPH, false, mCurrentUser, 0,
									ScreenChange.SF_NEXT);

						} else {
							mActivityRef.get().showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No4, 0, null);
						}
					}
				}

			};
			maintenanceCheck(handler);
			// 20150110 ADD-E For 2015年度Ver. メンテナンス中チェック

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	/**
	 * 「学習グラフ」クリック
	 * @param view
	 */
	public void onClickChart(View view) {
		try {
			mImagebuttonChart.setClickable(false);
			showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);
			// 20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
			ClickHandler<TopMenuActivity> handler = new ClickHandler<TopMenuActivity>(TopMenuActivity.this) {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					closeProgress();
					mActivityRef.get().mImagebuttonChart.setClickable(true);
					if (msg.what == 1) {
					}
					else {
						ConnectivityManager cm = (ConnectivityManager) mActivityRef.get().getSystemService(CONNECTIVITY_SERVICE);
						if (StudentClientCommData.canConnect(cm) == true) {
							ScreenChange.doScreenChange(mActivityRef.get(), ScreenChange.SCNO_TOP,
									ScreenChange.SCNO_CHART, false, mCurrentUser, 0,
									ScreenChange.SF_NEXT);

						} else {
							mActivityRef.get().showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No4, 0, null);
						}
					}
				}

			};

			maintenanceCheck(handler);
			// 20150110 ADD-E For 2015年度Ver. メンテナンス中チェック

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void onClickChange(View view) {
		// 20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);
		ClickHandler<TopMenuActivity> handler = new ClickHandler<TopMenuActivity>(TopMenuActivity.this) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// mImagebuttonChart.setClickable(true);
				closeProgress();
				if (msg.what == 1) {
				}
				else {
					ConnectivityManager cm = (ConnectivityManager) mActivityRef.get().getSystemService(CONNECTIVITY_SERVICE);
					if (StudentClientCommData.canConnect(cm) == true) {
						ScreenChange.doScreenChange(mActivityRef.get(), ScreenChange.SCNO_TOP,
								ScreenChange.SCNO_LOGIN, true, mCurrentUser, 0,
								ScreenChange.SF_NEXT);
					} else {
						mActivityRef.get().showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No10, 0, null);
					}
				}
			}

		};

		maintenanceCheck(handler);
		// 20150110 ADD-E For 2015年度Ver. メンテナンス中チェック
	}

	public void onClickSettingL(View view) {
		if (mCounter == 2) {
			// showDialog(0);
			FragmentManager fm = getFragmentManager();
			AlertFragment af = new AlertFragment();
			af.show(fm, "alert_dialog");
		}
		mCounter += 1;
	}

	public void onClickSettingR(View view) {
		mCounter += 2;
	}

	public void onClickMemory(View view) {
		System.gc();
		Utility.memory(getApplicationContext());
	}

	private void setControlVisible() {
		MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName() + "#setControlVisible");
		try {
		boolean learningExist = false;
		boolean todaylearningExist = false;
		boolean connect = false;
		boolean sendExist = false;
		//boolean entrance = false;

		// 学習可能データ存在チェック
		learningExist = KumonDataCtrl.ExistLearningData(mCurrentUser.mStudentID);
		logger.addSplit("KumonDataCtrl.ExistLearningData");
		todaylearningExist = KumonDataCtrl.TodayExistLearningData(mCurrentUser.mStudentID);
		logger.addSplit("KumonDataCtrl.TodayExistLearningData");

		// 通信状態チェック
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (StudentClientCommData.canConnect(cm) == true) {
			connect = true;
			// 20160217↓
			KumonLoaderCallbacks<Boolean> callback = new KumonLoaderCallbacks<Boolean>() {
				@Override
				public Loader<Boolean> onCreateLoader(int arg0, Bundle arg1) {
					AsyncTaskLoader<Boolean> loader = new AsyncTaskLoader<Boolean>(TopMenuActivity.this) {
						@Override
						public Boolean loadInBackground() {
							KumonSoap soap = new KumonSoap();

							return soap.SoapStudentEntrance(mCurrentUser.mStudentID);
						}
					};
					loader.forceLoad();
					return loader;
				}

				@Override
				public void onLoadFinished(Loader<Boolean> arg0, Boolean Result) {
					mImagebuttonStatus.setEnabled(Result);
					mImagebuttonStatus.setImageResource(Result ? R.drawable.btn_status : R.drawable.btn_status_off);
				}

			};
			KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_STUDENTENTRANCE, this, null, callback);
			// KumonSoap soap = new KumonSoap();
			// entrance = soap.SoapStudentEntrance(mCurrentUser.mStudentID);
			// 20160217↑
		} else {
			connect = false;
		}
		// 未送信データ存在チェック
		sendExist = KumonDataCtrl.IsExistSendData(mCurrentUser.mStudentID);
		logger.addSplit("KumonDataCtrl.IsExistSendData");

		// Receive
		if (connect && !sendExist) {
			mImagebuttonReceive.setEnabled(true);
			mImagebuttonReceive.setImageResource(R.drawable.btn_receive);
		} else {
			mImagebuttonReceive.setEnabled(false);
			mImagebuttonReceive.setImageResource(R.drawable.btn_receive_off);
		}
		// Study
		if (learningExist) {
			mImagebuttonLearning.setEnabled(true);
			if (todaylearningExist) {
				mImagebuttonLearning.setImageResource(R.drawable.btn_start_red);
			} else {
				mImagebuttonLearning.setImageResource(R.drawable.btn_start);
			}
		} else {
			mImagebuttonLearning.setEnabled(false);
			mImagebuttonLearning.setImageResource(R.drawable.btn_start_off);
		}
		// Send
		if (connect && sendExist) {
			mImagebuttonSend.setEnabled(true);
			mImagebuttonSend.setImageResource(R.drawable.btn_send1);
		} else {
			mImagebuttonSend.setEnabled(false);
			mImagebuttonSend.setImageResource(R.drawable.btn_send1_off);
		}
		// Status
		// オンラインのときは非同期処理でEnabledを制御する。
		// ここでEnabled=Falseにするのはオフラインのときだけ
		if (!connect) {
			mImagebuttonStatus.setEnabled(false);
			mImagebuttonStatus.setImageResource(R.drawable.btn_status_off);
		}
		// Graph
		if (connect) {
			mImagebuttonGraph.setEnabled(true);
			mImagebuttonGraph.setImageResource(R.drawable.btn_graph);
		} else {
			mImagebuttonGraph.setEnabled(false);
			mImagebuttonGraph.setImageResource(R.drawable.btn_graph_off);
		}
		// Chart
		if (connect) {
			mImagebuttonChart.setEnabled(true);
			mImagebuttonChart.setImageResource(R.drawable.btn_chart);
		} else {
			mImagebuttonChart.setEnabled(false);
			mImagebuttonChart.setImageResource(R.drawable.btn_chart_off);
		}
		logger.addSplit("set UI Visibility");
		}
		finally {
			logger.dumpToLog();
		}
	}

    static class DownloadHandler extends ClickHandler<TopMenuActivity>  {
    	boolean mModeSend;
		public DownloadHandler(TopMenuActivity act, boolean mode) {
			super(act);
			mModeSend = mode;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mActivityRef.get().closeProgress();
			if (msg.what == 1) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				// MIME type設定
				intent.setDataAndType(Uri.fromFile(new File(
						ModuleUpdate.SF_DOWNAPK)),
						"application/vnd.android.package-archive");
				// Intent発行
				mActivityRef.get().startActivity(intent);
				mActivityRef.get().finish();
			} else {
				if (mModeSend) {
					mActivityRef.get().StartRegistGradingResult();
				} else {
					// 20140618 MOD-S For 分割受信
					// StartGetResultData();
					mActivityRef.get().StartGetPrintSetList();
					// 20140618 MOD-E For 分割受信
				}
			}
		}

    };
	static class VersionCheckHandler extends ClickHandler<TopMenuActivity> {
		boolean mModeSend = false;
		public VersionCheckHandler(TopMenuActivity act, boolean send) {
			super(act);
			mModeSend = send;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				View.OnClickListener yesListener = new View.OnClickListener() {
					public void onClick(View v) {
						mActivityRef.get().showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No22);
						DownloadHandler handler = new DownloadHandler(mActivityRef.get(), mModeSend);
						ModuleUpdate.DownLoadApk(handler, mActivityRef.get());
					}
				};
				View.OnClickListener noListener = new View.OnClickListener() {
					public void onClick(View v) {
						if (mModeSend) {
							mActivityRef.get().StartRegistGradingResult();
						} else {
							// 20140618 MOD-S For 分割受信
							// StartGetResultData();
							mActivityRef.get().StartGetPrintSetList();
							// 20140618 MOD-E For 分割受信
						}
					}
				};
				mActivityRef.get().showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No80, 0, yesListener, 0, noListener);
//				KumonMessage.showKumonMsgBoxYesNo(mActivityRef.get(),
//						KumonMessage.MSG_No80, "YES", yesListner, "NO",
//						noListner);
			} else {
				if (mModeSend) {
					mActivityRef.get().StartRegistGradingResult();
				} else {
					// 20140618 MOD-S For 分割受信
					// StartGetResultData();
					mActivityRef.get().StartGetPrintSetList();
					// 20140618 MOD-E For 分割受信
				}
			}
		}
	}
	private void VersionCheck(final boolean modeSend) {
		Handler handler = new VersionCheckHandler(this, modeSend);

		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), PackageManager.GET_ACTIVITIES);
			int versionCode = packageInfo.versionCode;
			ModuleUpdate.VersionCheck(versionCode, handler, this);
		} catch (Exception e) {
		}

	}

	// 20140618 ADD-S For 分割受信
	private void StartGetPrintSetList() {
		// TODO: Progress up
		showProgress(R.layout.progress_msg_only, 0);
		KumonLoaderCallbacks<WDownloadPrintSetIDList> callback = new KumonLoaderCallbacks<WDownloadPrintSetIDList>() {
			@Override
			public Loader<WDownloadPrintSetIDList> onCreateLoader(int arg0,
					Bundle arg1) {
				SoapReceivePrintSetIDListTaskLoader loader = new SoapReceivePrintSetIDListTaskLoader(TopMenuActivity.this, mCurrentUser);
				loader.forceLoad();
				return loader;
			}
			@Override
			public void onLoadFinished(Loader<WDownloadPrintSetIDList> arg0,
					WDownloadPrintSetIDList printSetIDList) {
				EndGetPrintSetList(printSetIDList);
			}
		};
		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_RECEIVEPRINTSETID, this, null, callback);
	}

	private void EndGetPrintSetList(WDownloadPrintSetIDList printSetIDList) {
		if (canceled == true || printSetIDList == null) {
			ReceiveDataRollBack();
			closeProgress();
			return;
		}
		if (printSetIDList.mSoapStatus == 0 && printSetIDList.mSoapError.isEmpty()) {
			StartGetPrintSetData(printSetIDList);
		} else {
			ReceiveDataRollBack();
			closeProgress();
			if (printSetIDList.mSoapError.isEmpty() == false)
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.SF_TTL_NOCONNECT, printSetIDList.mSoapError, 0, null);
			else
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No8, 0, null);
		}
	}

	private void StartGetPrintSetData(final WDownloadPrintSetIDList printSetIDList) {
		if (canceled == true) {
			ReceiveDataRollBack();
			closeProgress();
			return;
		}
		final int downcnt = printSetIDList.mDownLoadResultDataList.size();
		closeProgress();
//		if (mProgressDialog != null) {
//			mProgressDialog.dismiss();
//			mProgressDialog = null;
//		}

		if (downcnt > 0) {
			showProgress(R.layout.progress_msg_spinner, 0);
			KumonLoaderCallbacks<WDownloadPrintSetIDList> callback = new KumonLoaderCallbacks<WDownloadPrintSetIDList>() {
				@Override
				public Loader<WDownloadPrintSetIDList> onCreateLoader(int arg0, Bundle arg1) {
					SoapReceivePrintSetDataTaskLoader loader = new SoapReceivePrintSetDataTaskLoader(TopMenuActivity.this, mCurrentUser, downcnt, printSetIDList);
					loader.forceLoad();
					return loader;
				}

				@Override
				public void onLoadFinished(Loader<WDownloadPrintSetIDList> arg0, WDownloadPrintSetIDList response) {
					closeProgress();
//					if (mProgressDialogCancel != null) {
//						mProgressDialogCancel.dismiss();
//						mProgressDialogCancel = null;
//					}
					EndGetPrintSetData(response);
				}
			};
			KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_RECEIVEPRINTSET, this, null, callback);
//			SoapReceivePrintSetDataTaskLoader task = new SoapReceivePrintSetDataTaskLoader(
//					mCurrentUser, downcnt, TopMenuActivity.this);
//			task.execute(printSetIDList);
		} else {
			EndGetPrintSetData(printSetIDList);
		}
	}

	private void EndGetPrintSetData(WDownloadPrintSetIDList printSetIDList) {
		if (canceled == true || printSetIDList == null) {
			ReceiveDataRollBack();
			closeProgress();
			return;
		}

		KumonDataCtrl.ClearKyozaiPrintSetList();
		KumonDataCtrl.GetKyozaiDataExistList(mCurrentUser.mStudentID);

		if (printSetIDList.mSoapStatus == 0
				&& printSetIDList.mSoapError.isEmpty()) {
			// 確定
			TempDataDBIO.DB_AllClear();

//			if (mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//			}
			closeProgress();
			ScreenChange.doScreenChange(this, ScreenChange.SCNO_TOP,
					ScreenChange.SCNO_NOTICE, false, mCurrentUser,
					ScreenChange.SF_NOTICEMODE_RECEIVE, ScreenChange.SF_NEXT);
		} else {
			ReceiveDataRollBack();
			if (printSetIDList.mSoapError.isEmpty() == false) {
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//					mProgressDialog = null;
//				}
				closeProgress();
				showOkDialog(R.layout.progress_msg_ok,
						KumonMessage.SF_TTL_NOCONNECT,
						printSetIDList.mSoapError, 0, null);
			} else {
				closeProgress();
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//					mProgressDialog = null;
//				}
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No8, 0, null);
			}
		}
		printSetIDList.Clear();
		printSetIDList = null;
		System.gc();
	}

	private void ReceiveDataRollBack() {
		// 本番ＤＢの該当学習者データクリア
		DataDBIO.DB_ClearStudentData(mCurrentUser.mStudentID);
		// TempDbから元へ戻す
		TempDataDBIO.DB_CopyTo(mCurrentUser.mStudentID);
		TempDataDBIO.DB_AllClear();
	}

	// 20140618 ADD-E For 分割受信

	private void StartRegistGradingResult() {

		// 20140617 MOD-S For　メモリ不足対応
		/***
		 * ArrayList<DPrintSet> registprintdatalist =
		 * KumonDataCtrl.GetRegistDataList(mCurrentUser.mStudentID);
		 * SoapRegistGradingResultTask task = new
		 * SoapRegistGradingResultTask(mCurrentUser, registprintdatalist.size(),
		 * TopMenuActivity.this); task.execute(registprintdatalist);
		 ***/

		showProgress(R.layout.progress_msg_spinner, KumonMessage.MSG_No21);
		KumonLoaderCallbacks<RegistGradingResultResponse> callback = new KumonLoaderCallbacks<RegistGradingResultResponse>() {

			@Override
			public Loader<RegistGradingResultResponse> onCreateLoader(int arg0, Bundle arg1) {
				SoapRegistGradingResultTaskLoader loader = new SoapRegistGradingResultTaskLoader(TopMenuActivity.this, mCurrentUser);
				loader.forceLoad();
				return loader;
			}

			@Override
			public void onLoadFinished(Loader<RegistGradingResultResponse> arg0, RegistGradingResultResponse response) {
				closeProgress();
				EndRegistGradingResult(response);
			}
		};
		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_REGISTGRADINGRESULT, this, null, callback);
//		SoapRegistGradingResultTaskLoader task = new SoapRegistGradingResultTaskLoader(
//				mCurrentUser, TopMenuActivity.this);
//		task.execute();
		// 20140617 MOD-E For　メモリ不足対応
	}

	private void EndRegistGradingResult(RegistGradingResultResponse response) {
		if (response.mRresult.mStatus == 0
				&& response.mRresult.mError.isEmpty()) {
			// 成功
			// 20140620 MOD-S 未送信フラグのクリアは、PrintSet単に変更
			/***
			 * boolean stat =
			 * KumonDataCtrl.DB_ClearRegistData(mCurrentUser.mStudentID); if
			 * (stat == true) { ScreenChange.doScreenChange(this,
			 * ScreenChange.SCNO_TOP, ScreenChange.SCNO_NOTICE, false,
			 * mCurrentUser ,ScreenChange.SF_NOTICEMODE_SEND,
			 * ScreenChange.SF_NEXT); } else {
			 * KumonMessage.showKumonMsgBoxOk(this, KumonMessage.MSG_No90, "OK",
			 * null); }
			 ***/
			ScreenChange.doScreenChange(this, ScreenChange.SCNO_TOP,
					ScreenChange.SCNO_NOTICE, false, mCurrentUser,
					ScreenChange.SF_NOTICEMODE_SEND, ScreenChange.SF_NEXT);
			// 20140620 MOD-E 未送信フラグのクリアは、PrintSet単に変更

		} else if (response.mRresult.mError.isEmpty() == false) {
			showOkDialog(R.layout.progress_msg_ok, KumonMessage.SF_TTL_NOCONNECT,
					response.mRresult.mError, 0, null);
		} else {
			showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No8, 0, null);
		}
		System.gc();
	}
/*
	void CloseProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		if (mProgressDialogCancel != null) {
			mProgressDialogCancel.dismiss();
			mProgressDialogCancel = null;
		}
	}
*/
	@UiThread
	private void CheckSuspend() {
		MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName()+"#CheckSuspend");
		try {
		mSuspendTimer.cancel();
		if (mSuspendTimer != null) {
			mSuspendTimer.cancel();
			mSuspendTimer = null;
		}
		mSuspendTimerTask = null;
		mSuspendTimerHandler = null;

		// Test中に中断
		File lasttestFile = StudentClientCommData.getLastTestFile();
		logger.addSplit("StudentClientCommData.getLastTestFile");
		if (lasttestFile.exists()) {
			// 20140916 ADD-S For 学習再開時は、メモリチェックをしない
			mLasttest = true;
			// 20140916 ADD-E For 学習再開時は、メモリチェックをしない
			ScreenChange.doScreenChangeRestart(this, mCurrentUser);
			logger.addSplit("ScreenChange.doScreenChangeRestart");
			return;
		}
		// 20140916 ADD-S For 学習再開時は、メモリチェックをしない
		else {
			mLasttest = false;
		}
		// 20140916 ADD-E For 学習再開時は、メモリチェックをしない

		// 採点中に中断
		ArrayList<DResultData> list = DataDBIO.DB_GetGradePrintSet(mCurrentUser.mStudentID);	// TODO: async
		logger.addSplit("DataDBIO.DB_GetGradePrintSet");
		if (list != null && list.size() > 0) {
			boolean self = false;
			boolean onClient = false;
			for (DResultData resultdata : list) {
				if (resultdata.mGradingMethod == KumonDataCtrl.SF_GradingMethod_Self) {
					self = true;
				} else if (resultdata.mGradingMethod == KumonDataCtrl.SF_GradingMethod_InstrucoreOnClient) {
					onClient = true;
				}
			}
			if (self) {
				ScreenChange.doScreenChangeGradeMySelf(this, mCurrentUser, false);
				return;
			} else {
				if (onClient) {
					KumonLoaderCallbacks<Boolean> callback = new KumonLoaderCallbacks<Boolean>() {
						@Override
						public Loader<Boolean> onCreateLoader(int arg0, Bundle arg1) {
							AsyncTaskLoader<Boolean> loader = new AsyncTaskLoader<Boolean>(TopMenuActivity.this) {
								@Override
								public Boolean loadInBackground() {
									KumonSoap soap = new KumonSoap();
									return soap.SoapStudentEntrance(mCurrentUser.mStudentID);
								}
							};
							loader.forceLoad();
							return loader;
						}
						@Override
						public void onLoadFinished(Loader<Boolean> arg0, Boolean entrance) {
							if (entrance) {
								ScreenChange.doScreenChangeGradeInstructorOnClient(TopMenuActivity.this, mCurrentUser, false);
								return;
							}
						}
					};
					KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_STUDENTENTRANCE, this, null, callback);
				}
			}
		}
		}
		finally {
			logger.dumpToLog();
		}
		/***
		 * m_TimerTask = new ControlbisibleTimerTask(); m_Timer = new
		 * Timer(true); m_Timer.schedule(m_TimerTask, 10, 60000);
		 ***/
	}


	// 20150110 ADD-E For 2015年度Ver. メンテナンス中チェック

	// 20150409 ADD-S For 2015年度Ver. 未読コメント
	@WorkerThread
	private boolean checkCommentUnreadPage() {
		MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName() + "#checkCommentUnreadPage");
		boolean stat = false;
		try {
			if (mDoUnreadCheck == true) {
				mDoUnreadCheck = false;
				// 未読コメントの有無をチェック
				boolean exist = KumonDataCtrl.ExistUnreadData(mCurrentUser.mStudentID);
				logger.addSplit("KumonDataCtrl.ExistUnreadData");

				if (exist == true) {
					// mContext = null;
					// mContext = this;
					View.OnClickListener yesListener = new View.OnClickListener() {
						public void onClick(View v) {
							// stat = true;
							showUnreadData();
						}
					};
					View.OnClickListener noListener = new View.OnClickListener() {
						public void onClick(View v) {
						}
					};
					showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No52,
							R.string.text_open, yesListener, R.string.text_later, noListener);
				}
			}
		}
		finally {
			logger.dumpToLog();
		}
		return stat;
	}

	@UiThread
	private void showUnreadData() {
		mCurrentUser.mCurrentKyokaID = KumonDataCtrl.SF_GUID_NULL;
		mCurrentUser.mCurrentKyozaiID = KumonDataCtrl.SF_GUID_NULL;
		mCurrentUser.mCurrentKyokaKyozaiName = "";
		mCurrentUser.mCurrentKyokaName = "";
		mCurrentUser.mCurrentKyozaiName = "";
		mCurrentUser.mCurrentPrintType = 0;
		ScreenChange.doScreenChangeDoneUnread(this, mCurrentUser);
	}

	// 20150409 ADD-E For 2015年度Ver. 未読コメント

	// *************************************************************
	// 20140618 ADD-S For 分割受信
	private static class SoapReceivePrintSetIDListTaskLoader extends
			AsyncTaskLoader</*CurrentUser, Integer, */WDownloadPrintSetIDList> {
//		private Activity mActivity = null;
		CurrentUser mUser = null;

		public SoapReceivePrintSetIDListTaskLoader(TopMenuActivity context, CurrentUser user) {
			super(context);
//			mActivity = context;
			mUser = user;
//			mProgressDialogCancel = null;
		}

		@Override
		@WorkerThread
		public WDownloadPrintSetIDList loadInBackground() {
			// 一旦TempDbへコピー（ロールバック用）
			MyTimingLogger logger = new MyTimingLogger(this.getClass().getSimpleName());
			TempDataDBIO.DB_AllClear();
			logger.addSplit("TempDataDBIO.DB_AllClear");
			TempDataDBIO.DB_CopyFrom(mUser.mStudentID);
			logger.addSplit("TempDataDBIO.DB_CopyFrom");
			// 本番ＤＢの該当学習者データクリア
			DataDBIO.DB_ClearStudentData(mUser.mStudentID);
			logger.addSplit("DataDBIO.DB_ClearStudentData");

			KumonSoap soap = new KumonSoap();
			// 20150413 ADD-S For 2015年度Ver. 未読対応
			// 既読フラグ送信
			try {
				ArrayList<String> list = DataDBIO.DB_GetReadCommentDataList(mUser.mStudentID);
				logger.addSplit("DataDBIO.DB_GetReadCommentDataList");
				soap.SoapSetUnreadFlg(mUser, list);
				DataDBIO.DB_ClearReadCommentDataList(mUser.mStudentID);
				logger.addSplit("DataDBIO.DB_ClearReadCommentDataList");
			} catch (Exception e) {
			}
			// 20150413 ADD-E For 2015年度Ver. 未読対応

			WDownloadPrintSetIDList printSetIDList = null;
			try {
				printSetIDList = soap.SoapGetPrintSetIDList(mUser);
				logger.addSplit("SoapGetPrintSetIDList");
			} catch (Exception e) {
				SLog.DB_AddException(e);
			}
			soap = null;
			logger.dumpToLog();
			return printSetIDList;
		}
	}

	private static class SoapReceivePrintSetDataTaskLoader extends AsyncTaskLoader</*WDownloadPrintSetIDList, Integer,*/ WDownloadPrintSetIDList> {
		private WeakReference<TopMenuActivity> mActivityRef = null;
		private CurrentUser mCurrentUser = null;
		WDownloadPrintSetIDList mPrintSetIDList = null;
		private int mDownloadCount = 0;

		public SoapReceivePrintSetDataTaskLoader(TopMenuActivity activity, CurrentUser user, int downloadCount, WDownloadPrintSetIDList printSetIdList) {
			super(activity);
			mCurrentUser = user;
			mDownloadCount = downloadCount;
			mActivityRef = new WeakReference<TopMenuActivity>(activity);
			mPrintSetIDList = printSetIdList;
//			mActivityRef.get().mProgressDialogCancel = null;
		}

		@Override
		@WorkerThread
		public WDownloadPrintSetIDList loadInBackground() {
			MyTimingLogger logger = new MyTimingLogger("SoapReceivePrintSetDataTaskLoader");
			Log.d(getClass().getSimpleName()+"#loadInBackground", "!!Start");
			KumonSoap soap = new KumonSoap();
//			boolean fTransactionSuccess = false;
			WDownloadPrintSetIDList response = new WDownloadPrintSetIDList();
			WDownloadResultData resultData = null;
//			WriteDBAccessor accessor = new MastDBIO.WriteDBAccessor();

			try {
				ArrayList<MQuestion2> saveQuestions = new ArrayList<MQuestion2>();
				if (KumonDataCtrl.bProgressStop == true) {
					mActivityRef.get().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mActivityRef.get().closeProgress();
						}
					});
					return null;
				}
				// //////////////////////////
				Integer totalCount = 0;

				ArrayList<DResultData> downloadList = mPrintSetIDList.mDownLoadResultDataList;
				int downCount = downloadList.size();

				for (int i = 0; i < downCount; i++) {
					if (KumonDataCtrl.bProgressStop == true) {
						return null;
					}
					int doneQuestionCount = 0;

					DResultData resultPrintSetData = downloadList.get(i);
					resultData = soap.SoapGetPrintSetData(mCurrentUser, resultPrintSetData);
					logger.addSplit("SoapGetPrintSetData#" + i);
					Log.d(getClass().getSimpleName()+"#loadInBackground", "SoapGetPrintSetData#" + i);
					if (resultData.mSoapStatus == 0 && resultData.mSoapError.isEmpty()) {
						resultData.CheckQuestionData();
						int questioncnt = resultData.mQuestionList.size() + resultData.mTopQuestionList.size();
						Log.d(getClass().getSimpleName()+"#loadInBackground", "count of " + i + " mQuestionList = " + resultData.mQuestionList.size() + ", mTopQuestionList = " + resultData.mTopQuestionList.size());
						for (int j = 0; j < resultData.mQuestionList.size(); j++) {
							if (mActivityRef.get() != null && mActivityRef.get().canceled)
								break;
							if (KumonDataCtrl.bProgressStop == true) {
								resultData.ClearDownLoadData();
								resultData = null;
								mActivityRef.get().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mActivityRef.get().closeProgress();
									}
								});
								return null;
							}
							MQuestion2 question = resultData.mQuestionList.get(j);
							WQuestionDataList questiondatalist = soap.SoapGetQuestionData(mCurrentUser, question);
							logger.addSplit("SoapGetQuestionData#" + j);
							Log.d(getClass().getSimpleName()+"#loadInBackground", "SoapGetQuestionData#" + j);
							if (questiondatalist.mSoapStatus == 0 && questiondatalist.mSoapError.isEmpty()) {
								for (MQuestion2 saveQuestion : questiondatalist.mMQuestionDataList) {
									MQuestion2 savequestion = KumonCommon.DecompressQuestion(saveQuestion);
									saveQuestions.add(savequestion);
									if (saveQuestions.size() >= 100) {
										MastDBIO.DB_InsertQuestionDataList(saveQuestions);
										saveQuestions.clear();
									}
//									MastDBIO.DB_InsertQuestionData(saveQuestion);
								}
							}
//							logger.addSplit("MastDBIO.DB_InserQuestionData#" + j + ", count = " + questiondatalist.mMQuestionDataList.size());
//							Log.d(getClass().getSimpleName()+"#loadInBackground", "MastDBIO.DB_InserQuestionData#" + j + ", count = " + questiondatalist.mMQuestionDataList.size());
							questiondatalist = null;
							doneQuestionCount++;

							{
								final int lQuestionCnt = questioncnt;
								final int lDoneQuestionCnt = doneQuestionCount;
								final int lTotalCnt = totalCount;
								mActivityRef.get().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mActivityRef.get().updateProgress((lDoneQuestionCnt * 100
												/ lQuestionCnt / mDownloadCount)
												+ (lTotalCnt * 100 / mDownloadCount));
									}
								});
							}

						}
						for (int j = 0; j < resultData.mTopQuestionList.size(); j++) {
							if (mActivityRef.get() != null && mActivityRef.get().canceled)
								break;
							if (KumonDataCtrl.bProgressStop == true) {
								resultData.ClearDownLoadData();
								resultData = null;
								mActivityRef.get().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mActivityRef.get().closeProgress();
									}
								});
								return null;
							}
							MQuestion2 question = resultData.mTopQuestionList.get(j);
							WQuestionDataList questionDataList = soap.SoapGetQuestionDataByPrintNo(mCurrentUser, question);
							logger.addSplit("SoapGetQuestionDataByPrintNo#" + j);
							Log.d(getClass().getSimpleName()+"#loadInBackground", "SoapGetQuestionDataByPrintNo#" + j);
							if (questionDataList.mSoapStatus == 0
									&& questionDataList.mSoapError.isEmpty()) {
								for (MQuestion2 saveQuestion : questionDataList.mMQuestionDataList) {
									MQuestion2 savequestion = KumonCommon.DecompressQuestion(saveQuestion);
									saveQuestions.add(savequestion);
									if (saveQuestions.size() >= 100) {
										MastDBIO.DB_InsertQuestionDataList(saveQuestions);
										saveQuestions.clear();
									}
//									MastDBIO.DB_InsertQuestionData(saveQuestion);
								}
							}
//							logger.addSplit("MastDBIO.DB_InserQuestionData#" + j);
//							Log.d(getClass().getSimpleName()+"#loadInBackground", "MastDBIO.DB_InserQuestionData#" + j);
							questionDataList = null;
							doneQuestionCount++;
							{
								final int lQuestionCount = questioncnt;
								final int lDoneQuestionCount = doneQuestionCount;
								final int lTotalCount = totalCount;
								mActivityRef.get().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mActivityRef.get().updateProgress((lDoneQuestionCount * 100
												/ lQuestionCount / mDownloadCount)
												+ (lTotalCount * 100 / mDownloadCount));
									}
								});
							}

						}
						if (mActivityRef.get() != null && !mActivityRef.get().canceled) {
							if (KumonDataCtrl.bProgressStop == true) {
								resultData.ClearDownLoadData();
								resultData = null;
								mActivityRef.get().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mActivityRef.get().closeProgress();
									}
								});
								return null;
							}
							// Save
							boolean stat = DataDBIO.DB_SaveDownLoadPrintSetData(mCurrentUser.mStudentID, resultData);
							logger.addSplit("DataDBIO.DB_SaveDownLoadPrintSetData");
							Log.d(getClass().getSimpleName()+"#loadInBackground", "DataDBIO.DB_SaveDownLoadPrintSetData");
							resultData.ClearDownLoadData();
							resultData = null;
	//						System.gc();
							if (stat == false) {
								response.mSoapStatus = 0;
								response.mSoapError = KumonMessage.getKumonMessage(KumonMessage.MSG_No90);
								return response;
							}
						}
					} else {
						resultData.ClearDownLoadData();
						response.mSoapStatus = resultData.mSoapStatus;
						response.mSoapError = resultData.mSoapError;
						resultData = null;
						return response;
					}
					totalCount++;
					{
						final int lTotalCnt = totalCount;
						mActivityRef.get().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mActivityRef.get().updateProgress(lTotalCnt * 100 / mDownloadCount);
							}
						});
					}
					logger.addSplit("End of Loop");
					Log.d(getClass().getSimpleName()+"#loadInBackground", "End of Loop");

					if (mActivityRef.get() != null && mActivityRef.get().canceled)
						break;
				}
				if (saveQuestions.size() > 0) {
					MastDBIO.DB_InsertQuestionDataList(saveQuestions);
					logger.addSplit("MastDBIO.DB_InserQuestionData");
					Log.d(getClass().getSimpleName()+"#loadInBackground", "MastDBIO.DB_InserQuestionData");
				}

				mActivityRef.get().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mActivityRef.get().updateProgress(100);
					}
				});
			} catch (Exception e) {
				SLog.DB_AddException(e);
			}
			finally {
				logger.dumpToLog();
			}
			soap = null;
			return response;
		}
	}

	// 20140618 ADD-E For 分割受信

	// 20140617 MOD-S For　メモリ不足対応
	// private class SoapRegistGradingResultTask extends
	// AsyncTask<ArrayList<DPrintSet>, Integer, RegistGradingResultResponse> {
	private static class SoapRegistGradingResultTaskLoader extends AsyncTaskLoader</*Void, Integer, */RegistGradingResultResponse> {
		// 20140617 MOD-E For　メモリ不足対応

		private WeakReference<TopMenuActivity> mActivityRef = null;
		private CurrentUser mUser = null;
		private int mUploadCnt = 0;

		// 20140617 MOD-S For　メモリ不足対応
		// public SoapRegistGradingResultTask(CurrentUser user, int uploadcnt,
		// Activity activity) {
		public SoapRegistGradingResultTaskLoader(TopMenuActivity activity, CurrentUser user) {
			super(activity);
			// 20140617 MOD-E For　メモリ不足対応
			mUser = user;
			// 20140617 DEL-S For　メモリ不足対応
			// this.mUploadCnt = uploadcnt;
			// 20140617 DEL-E For　メモリ不足対応
			mActivityRef = new WeakReference<TopMenuActivity>(activity);
		}
/*
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(mActivity);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setMax(100);
			mProgressDialog.incrementProgressBy(0);
			KumonMessage.showProgress(mProgressDialog, mActivity, KumonMessage.MSG_No7);
		}
*/
		// UI スレッドではない
		// バックグラウンドでダウンロード処理を行う
		@Override
		// 20140617 MOD-S For　メモリ不足対応
		// protected RegistGradingResultResponse
		// doInBackground(ArrayList<DPrintSet>... registprintdatalist) {
		public RegistGradingResultResponse loadInBackground() {
			// 20140617 MOD-E For　メモリ不足対応
			KumonSoap soap = new KumonSoap();

			// 20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			String[] msg = KumonLog.GetAndroidLogList();
			if (msg != null && msg.length > 0) {
				CollectAndroidLogResponse res = soap.SoapSendAndroidLog(mUser,
						msg);
				if (res.mRresult.mStatus == 0) {
					KumonLog.DeleteAndroidLogFile();
				}
			}
			// 20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

			// 20150413 ADD-S For 2015年度Ver. 未読対応
			// 既読フラグ送信
			try {
				ArrayList<String> list = DataDBIO
						.DB_GetReadCommentDataList(mUser.mStudentID);
				soap.SoapSetUnreadFlg(mUser, list);
				DataDBIO.DB_ClearReadCommentDataList(mUser.mStudentID);
			} catch (Exception e) {
			}
			// 20150413 ADD-E For 2015年度Ver. 未読対応

			int totalCount = 1;
			RegistGradingResultResponse response = new RegistGradingResultResponse();

			DStudent student = soap.SoapLoginOnly(mUser);
			if (student == null) {
				response.mRresult.mStatus = 99;
				response.mRresult.mError = "";
				return response;
			}
			if (student.mSoapStatus != 0 || student.mSoapError.isEmpty() == false) {
				response.mRresult.mStatus = student.mSoapStatus;
				response.mRresult.mError = student.mSoapError;
				return response;
			}
			mUser.mLastSessionID = student.mSessionID;
			// 20140617 MOD-S For　メモリ不足対応
			// ArrayList<DPrintSet> uploadlist = registprintdatalist[0];
			ArrayList<DResultData> uploadList = KumonDataCtrl.GetRegistPrintSetList(mUser.mStudentID);
			mUploadCnt = uploadList.size();
			// 20140617 MOD-E For　メモリ不足対応

			for (DResultData resultData : uploadList) {
				{
					final int lTotalCount = totalCount;
					mActivityRef.get().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mActivityRef.get().updateProgress(lTotalCount * 100 / mUploadCnt);
						}
					});
				}

				// 20140617 MOD-S For　メモリ不足対応

				DPrintSet printSet = KumonDataCtrl.GetRejistPrintSet(
						mUser.mStudentID, resultData.mKyokaID,
						resultData.mKyozaiID, resultData.mPrintSetID);
				// 20140617 MOD-S For　メモリ不足対応
				response = soap.SoapRegistGradingResult(mUser, printSet);

				printSet.ClearAll();
				printSet = null;
				if (response.mRresult.mStatus == 0 && response.mRresult.mError.isEmpty()) {
					// 20140718 DEL-S For Print単位送信
					/***
					 * //20140620 ADD-S 未送信フラグクリア boolean stat =
					 * KumonDataCtrl.DB_ClearRegistPrintSetData
					 * (mCurrentUser.mStudentID, resultdata.mKyokaID,
					 * resultdata.mKyozaiID, resultdata.mPrintSetID); if(stat ==
					 * false) { uploadlist.clear(); uploadlist = null;
					 * response.mRresult.mStatus = 99; response.mRresult.mError
					 * = KumonMessage.getKumonMessage(KumonMessage.MSG_No90); }
					 * //20140620 ADD-E 未送信フラグクリア
					 ***/
					// 20140718 DEL-E For Print単位送信
				} else {
					uploadList.clear();
					uploadList = null;
					return response;
				}

				totalCount++;
			}
			soap.SoapLogout(mUser);

			uploadList.clear();
			uploadList = null;
			return response;
		}

		/*
		// UI スレッド
		// プログレスバーの表示を更新する
		@Override
		protected void onProgressUpdate(Integer... progress) {
			mProgressDialog.setProgress(progress[0]);
		}
		*/

		/*
		// UI スレッド
		// ダイアログを表示する
		@Override
		protected void onPostExecute(RegistGradingResultResponse response) {
			mProgressDialog.dismiss();

			EndRegistGradingResult(response);
		}
		 */
	}

	// *************************************************************
	class ControlVisibleTimerTask extends TimerTask {
		@Override
		public void run() {
			mHandler.post(new Runnable() {
				public void run() {
					// 20150409 MOD-S For 2015年度Ver. 未読コメント
					// 未読コメントが有る場合は、メッセージボックスを表示して、未読教材を表示する
					// setControlbisible();
					if (checkCommentUnreadPage() == false) {
						setControlVisible();
					}
					// 20150409 MOD-E For 2015年度Ver. 未読コメント
				}
			});
		}
	}

	class SuspendTimerTask extends TimerTask {
		@Override
		public void run() {
			mSuspendTimerHandler.post(new Runnable() {
				public void run() {
					CheckSuspend();
				}
			});
		}
	}

}
