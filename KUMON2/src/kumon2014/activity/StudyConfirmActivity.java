package kumon2014.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.MyTimingLogger;
import kumon2014.common.ScreenChange;
import kumon2014.common.Utility;
import kumon2014.database.data.TblSoundCommentData;
import kumon2014.database.data.TblSoundRecordData;
import kumon2014.database.log.SLog;
import kumon2014.database.master.MQuestion2;
import kumon2014.database.master.MastDBIO;
import kumon2014.kumondata.DPrintSet;
import kumon2014.kumondata.DResultData;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.kumondata.WDownloadPrintSetData;
import kumon2014.markcontroltool.AndroidMarkControl;
import kumon2014.message.KumonMessage;
import kumon2014.view.MarkControlSurface;
import kumon2014.view.RecordCallback;
import kumon2014.webservice.KumonSoap;
import pothos.view.PenPlayBackCallback;
import pothos.view.ReplayTask;

public class StudyConfirmActivity extends BaseActivity implements PenPlayBackCallback, RecordCallback  {
	private AndroidMarkControl mAndroidMarkControl = null;

	CurrentUser mCurrentUser = null;

	private Handler mHandler = new Handler();
	private Timer m_Timer = null;
//	private ProgressDialog mProgressDialog;

	private int mLearningMode = 0;

	private TableRow 	mPageBar = null;

	private TableLayout mTablePenPlayBack = null;
//	private TextView 	mTextViewPenPlayBack = null;
	private boolean 	mPenPlayBack = false;
	private long		mPlayBackTime = 0;

	private ProgressBar mProgressBarPRProgress = null;
	private TextView 	mTextViewRPTime = null;
	private ImageView 	mImagebuttonRPBack = null;
	private ImageView 	mImagebuttonRPStop = null;
	private ImageView 	mImagebuttonRPPose = null;
	private ImageView 	mImagebuttonRPPlay1 = null;
	private ImageView 	mImagebuttonRPPlay2 = null;
	private ImageView 	mImagebuttonRPPlay4 = null;
	private ImageView 	mImagebuttonRPPlay8 = null;
	private ImageView 	mImagebuttonRPNext = null;
	//20140528 ADD-S For SoundStroke
	private ImageView 	mImagebuttonRPListen = null;
	private Timer 		mSoundStopTimer = null;
	//20140528 ADD-E For SoundStroke

	private ImageButton mBtnSkipBack = null;
	private ImageButton mBtnBack = null;
	private ImageButton mBtnNext = null;
	private ImageButton mBtnSkipNext = null;
	private boolean 	mClickable = true;


	private String mPrintSetID_WEB = "";
	private String mPrintUnitID_WEB = "";

	private RelativeLayout mRelativeLayout = null;

	//20140731 ADD-S For Memo
	private ImageButton mMemo = null;
	//20140731 ADD-E For Memo

	//20150115 ADD-S 再生時もダイアログ表示
	private boolean Recordable = false;
	//20150115 ADD-E 再生時もダイアログ表示

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName()+"#onCreate");

		Log.d(getClass().getSimpleName()+"#onCreate", "thread=" + Thread.currentThread().getId());
		
		try {
			System.gc();
			logger.addSplit("System.gc End");
			setContentView(R.layout.activity_studyconfirm);

			Intent intent = getIntent();
			mCurrentUser = (CurrentUser) intent.getSerializableExtra("CurrentUser");
			mLearningMode = intent.getIntExtra(ScreenChange.SF_LEARNINGMODE, KumonDataCtrl.SF_DATATYPE_NONE);

			mAndroidMarkControl = new AndroidMarkControl(mCurrentUser, mLearningMode, 0);
			mAndroidMarkControl.setView((MarkControlSurface) findViewById(R.id.MarkControlSurfaceMain));

			mPageBar = (TableRow) findViewById(R.id.tablerow_pagebar);
			mAndroidMarkControl.setPageBar(mPageBar, null);

			mBtnSkipBack = (ImageButton) findViewById(R.id.imagebutton_skipback);
			mBtnBack = (ImageButton) findViewById(R.id.imagebutton_back);
			mBtnNext = (ImageButton) findViewById(R.id.imagebutton_next);
			mBtnSkipNext = (ImageButton) findViewById(R.id.imagebutton_skipnext);

			mTablePenPlayBack = (TableLayout) findViewById(R.id.tableLayout_penplayback);
//			mTextViewPenPlayBack = (TextView) findViewById(R.id.textview_penplayback);
			mTablePenPlayBack.setVisibility(View.GONE);
//			mTextViewPenPlayBack.setVisibility(View.GONE);

			mProgressBarPRProgress = (ProgressBar) findViewById(R.id.progressBar_PRProgress);
			mTextViewRPTime = (TextView) findViewById(R.id.textview_RPTime);
			mImagebuttonRPBack = (ImageView) findViewById(R.id.imagebutton_RPBack);
			mImagebuttonRPStop = (ImageView) findViewById(R.id.imagebutton_RPStop);
			mImagebuttonRPPose = (ImageView) findViewById(R.id.imagebutton_RPPose);
			mImagebuttonRPPlay1 = (ImageView) findViewById(R.id.imagebutton_RPPlay1);
			mImagebuttonRPPlay2 = (ImageView) findViewById(R.id.imagebutton_RPPlay2);
			mImagebuttonRPPlay4 = (ImageView) findViewById(R.id.imagebutton_RPPlay4);
			mImagebuttonRPPlay8 = (ImageView) findViewById(R.id.imagebutton_RPPlay8);
			mImagebuttonRPNext = (ImageView) findViewById(R.id.imagebutton_RPNext);
			//20140528 ADD-S For SoundStroke
			mImagebuttonRPListen = (ImageView) findViewById(R.id.imagebutton_RPListen);
			//20140528 ADD-E For SoundStroke

	        mRelativeLayout = (RelativeLayout) findViewById(R.id.MarkControlFrame);

			//20140731 ADD-S For Memo
			mMemo = (ImageButton) findViewById(R.id.imagebutton_memo);
			mAndroidMarkControl.SetMemoBtn(mMemo);
			//20140731 ADD-E For Memo

			//20140805 ADD-S For 録音
			mTableRecord = (TableLayout) findViewById(R.id.tableLayout_record);
//			mTextViewRecord = (TextView) findViewById(R.id.textview_record);
			mTableRecord.setVisibility(View.GONE);
//			mTextViewRecord.setVisibility(View.GONE);

			mImagebuttonStop = (ImageView) findViewById(R.id.imagebutton_RecordStop);
			mImagebuttonStart = (ImageView) findViewById(R.id.imagebutton_RecordStart);
			mImagebuttonPlay = (ImageView) findViewById(R.id.imagebutton_RecordPlay);
			//20150115 ADD-S 再生時もダイアログ表示
			mImagebuttonPause = (ImageView) findViewById(R.id.imagebutton_RecordPause);
			//20150115 ADD-E 再生時もダイアログ表示

			mAndroidMarkControl.setRecordCallBack(StudyConfirmActivity.this, mCurrentUser.mLoginID);

			mProgressBarRCProgress = (ProgressBar) findViewById(R.id.progressBar_RCProgress);
			mTextViewRCTime = (TextView) findViewById(R.id.textview_RCTime);
			mTableRecord.setVisibility(View.GONE);
//			mTextViewRecord.setVisibility(View.GONE);
			//20140805 ADD-E For 録音
			
			View rb = findViewById(R.id.radiobutton_pen_ll);
			rb.setVisibility(View.GONE);
			
			View btn = findViewById(R.id.imagebutton_Rest);
			btn.setVisibility(View.INVISIBLE);

			logger.addSplit("UI initialize end");
			
			if (mLearningMode == KumonDataCtrl.SF_DATATYPE_WEBVIEW) {

				String KyozaiID = intent.getStringExtra(ScreenChange.SF_KYOZAIID);
				mPrintSetID_WEB = intent.getStringExtra(ScreenChange.SF_PRINTSETID);
				mPrintUnitID_WEB = intent.getStringExtra(ScreenChange.SF_PRINTUNITID);

//				DPrintSet printset = new DPrintSet();
//				printset.mPrintSetID = mPrintSetID_WEB;
//				printset.mKyozaiID = KyozaiID;

				Bundle arg = new Bundle();
				arg.putString("PrintSetID", mPrintSetID_WEB);
				arg.putString("KyozaiID", KyozaiID);
				
				showProgress(R.layout.progress_msg_only, 0);
				
				KumonLoaderCallbacks<WDownloadPrintSetData> callback = new KumonLoaderCallbacks<WDownloadPrintSetData>() {
					@Override
					public Loader<WDownloadPrintSetData> onCreateLoader(
							int arg0, Bundle arg1) {
						DPrintSet printset = new DPrintSet();
						printset.mPrintSetID = arg1.getString("PrintSetID");
						printset.mKyozaiID = arg1.getString("KyozaiID");

						SoapReceiveGradingResultTaskLoader loader = new SoapReceiveGradingResultTaskLoader(StudyConfirmActivity.this, mCurrentUser, printset);
						loader.forceLoad();
						return loader;
					}

					@Override
					public void onLoadFinished(
							Loader<WDownloadPrintSetData> arg0,
							WDownloadPrintSetData arg1) {
						closeProgress();
						GetGradingResultEnd(arg1);
					}
				};
				KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_DOWNLOADPRINTSET, this, arg, callback);
//				SoapReceiveGradingResultTaskLoader task = new SoapReceiveGradingResultTaskLoader(mCurrentUser, StudyConfirmActivity.this);
//				task.execute(printset);
			} else {

				InitializeQuestionControlTimer();
				logger.addSplit("InitializeQuestionControlTimer end");
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		logger.dumpToLog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.DebugTimePass("onCreateOptionsMenu START");
		return true;
	}

	@Override
	protected void onRestart() {
		Utility.DebugTimePass("onRestart START");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		Utility.DebugTimePass("onResume START");
		super.onResume();
		
//		setClickable(false);
//		mClickable = false;
//		ProgressShow();
//		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(3000) ;
//				}
//				catch(Exception e) {
//				}
//				reFreshPage();
//			}
//		}).start();
		
	}

	@Override
	protected void onPause() {
		Utility.DebugTimePass("onPause START");
		super.onPause();
		
//		try {
// 			mHandler.post(new Runnable() {
// 				@Override
// 				public void run() {
// 					try {
// 						Thread.sleep(1000);
// 					} catch (InterruptedException e) {
// 						e.printStackTrace();
// 					}
// 				}
// 			});
// 		} catch (Exception e) {
// 			SLog.DB_AddException(e);
// 		}
		
		try {
 			mHandler.post(new Runnable() {
 				@Override
 				public void run() {
 					try {
 						Thread.sleep(1000);
 					} catch (InterruptedException e) {
 						e.printStackTrace();
 					}
 				}
 			});
 		} catch (Exception e) {
 			SLog.DB_AddException(e);
 		}
	}

	@Override
	protected void onStop() {
		Utility.DebugTimePass("onStop START");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Utility.DebugTimePass("onDestroy START");

//		if (mProgressDialog != null) {
//			mProgressDialog = null;
//		}
		closeProgress();
		if (m_Timer != null) {
			m_Timer.cancel();
			m_Timer = null;
		}
		//20140528 ADD-S For SoundStroke
		if (mSoundStopTimer != null) {
			mSoundStopTimer.cancel();
			mSoundStopTimer = null;
		}
		//20140528 ADD-E For SoundStroke

		//20140805 ADD-S For 録音
		if (m_RecTimer != null) {
			m_RecTimer.cancel();
			m_RecTimer = null;
		}
		//20140805 ADD-E For 録音

		Utility.cleanupView(findViewById(R.id.studyconfirm_topview));
		System.gc();
	}

	@Override
	public void onLowMemory() {
		Utility.DebugTimePass("onLowMemory START");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Utility.DebugTimePass("onConfigurationChanged START");
	}
	//20150413 ADD-S For 2015年度Ver. 未読対応
	// startActivityForResult で起動させたアクティビティが
	// finish() により破棄されたときにコールされる
	// requestCode : startActivityForResult の第二引数で指定した値が渡される
	// resultCode : 起動先のActivity.setResult の第一引数が渡される
	// Intent data : 起動先Activityから送られてくる Intent
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (intent == null)
			return;
	    if(requestCode == MemoActivity.SF_RESULTCODE) {
	    	
//			setClickable(false);
//			mClickable = false;
//			ProgressShow();
//			
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(3000) ;
//					}
//					catch(Exception e) {
//					}
//					reFreshPage();
//				}
//			}).start();
			
	    	int mode = intent.getIntExtra(MemoActivity.SF_RETURN, 0);
	    	if(mode == MemoActivity.SF_PAGEBACK) {
	    		//PageBack
	    		MemoPageBack();
	    	}
	    	else if(mode == MemoActivity.SF_PAGENEXT) {
	    		//PageNext
	    		MemoPageNext();
	    	}
	    	else {
	    		
	    	}
	    }
	    else {
	    	//メモ画面以外は何もしない
	    }
	}
	//20150413 ADD-E For 2015年度Ver. 未読対応

	public void onClickFinish(View view) {
		ClosePlayBack();
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応

		try {
			if (mLearningMode == KumonDataCtrl.SF_DATATYPE_WEBVIEW) {
				finish();
		    //20150409 ADD-S For 2015年度Ver. 未読コメント
			} else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_DONE_UNREAD) {
				finish();
		    //20150409 ADD-S For 2015年度Ver. 未読コメント
			} else {
				ScreenChange.doScreenChange(this, ScreenChange.SCNO_CONFIRM, ScreenChange.SCNO_LIST, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void onClickSkipBack(View view) {
		ClosePlayBack();
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応

		//20140618 MOD-S For 連打抑制
		/***
		setClickable(false);
		if (mClickable == true) {
			mClickable = false;
			new Thread(new Runnable() {
				@Override
				public void run() {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							DoOnClickSkipBack();
							mClickable = true;
							setClickable(true);
						}
					});
				}
			}).start();
		}
		***/
		if (mClickable == true) {
			setClickable(false);
			mClickable = false;
			ProgressShow();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(progressTime) ;
					}
					catch(Exception e) {
					}
					DoOnClickSkipBack();
				}
			}).start();
		}
		//20140618 MOD-E For 連打抑制
	}
	public void DoOnClickSkipBack() {
		//20140618 MOD-S For 連打抑制
		/***
		try {
			mBtnSkipBack.setEnabled(false);
			mAndroidMarkControl.DoOnClickSkipBackDone(mRelativeLayout, this);
			SetButtonProperties();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		***/
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// UI部品への操作;
					mBtnSkipBack.setEnabled(false);
					boolean pagechange = mAndroidMarkControl.DoOnClickSkipBackDone(mRelativeLayout, getApplicationContext());
					SetButtonProperties();

					mClickable = true;
					setClickable(true);
					ProgressHide();
					//20150413 ADD-S For 2015年度Ver. 未読対応
					if (mLearningMode != KumonDataCtrl.SF_DATATYPE_WEBVIEW && pagechange == true) {
						ShowUnreadMemo(false);
					}
					//20150413 ADD-E For 2015年度Ver. 未読対応
					return;
				}
			});

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		//20140618 MOD-E For 連打抑制
	}

	public void onClickBack(View view) {
		ClosePlayBack();
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応

		//20140618 MOD-S For 連打抑制
		/***
		setClickable(false);
		if(mClickable == true) {
			mClickable = false;
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	mHandler.post(new Runnable() {
			    		@Override
			    		public void run() {
			    			DoOnClickBack();
			    			mClickable = true;
			    			setClickable(true);
			    		}
			    	});
			    }
			}).start();
		}
		***/
		if (mClickable == true) {
			setClickable(false);
			mClickable = false;
			ProgressShow();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(progressTime) ;
					}
					catch(Exception e) {
					}
					DoOnClickBack();
				}
			}).start();
		}
		//20140618 MOD-E For 連打抑制
	}
	public void DoOnClickBack() {
		//20140618 MOD-S For 連打抑制
		/***
		try {
			mBtnSkipBack.setEnabled(false);
			mAndroidMarkControl.DoOnClickBackDone(mRelativeLayout, this);
			SetButtonProperties();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		***/
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// UI部品への操作;
					mBtnBack.setEnabled(false);
					boolean pagechange = mAndroidMarkControl.DoOnClickBackDone(mRelativeLayout, getApplicationContext());
					SetButtonProperties();

					mClickable = true;
					setClickable(true);
					ProgressHide();
					//20150413 ADD-S For 2015年度Ver. 未読対応
					if (mLearningMode != KumonDataCtrl.SF_DATATYPE_WEBVIEW && pagechange == true) {
						ShowUnreadMemo(false);
					}
					//20150413 ADD-E For 2015年度Ver. 未読対応
					return;
				}
			});

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		//20140618 MOD-E For 連打抑制
	}

	public void onClickNext(View view) {
		ClosePlayBack();
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応

		//20140618 MOD-S For 連打抑制
		/***
		setClickable(false);
		if(mClickable == true) {
			mClickable = false;
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	mHandler.post(new Runnable() {
			    		@Override
			    		public void run() {
			    			DoOnClickNext();
			    			mClickable = true;
			    			setClickable(true);
			    		}
			    	});
			    }
			  }).start();
		}
		***/
		if (mClickable == true) {
			setClickable(false);
			mClickable = false;
			ProgressShow();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(progressTime) ;
					}
					catch(Exception e) {
					}
					DoOnClickNext();
				}
			}).start();
		}
		//20140618 MOD-E For 連打抑制
	}
	public void DoOnClickNext() {
		//20140618 MOD-S For 連打抑制
		/***
		try {
			mBtnSkipBack.setEnabled(false);
			mAndroidMarkControl.DoOnClickNextDone(mRelativeLayout, this);
			SetButtonProperties();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		***/
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// UI部品への操作;
					mBtnNext.setEnabled(false);
					boolean pagechange = mAndroidMarkControl.DoOnClickNextDone(mRelativeLayout, getApplicationContext());
					SetButtonProperties();

					mClickable = true;
					setClickable(true);
					ProgressHide();
					//20150413 ADD-S For 2015年度Ver. 未読対応
					if (mLearningMode != KumonDataCtrl.SF_DATATYPE_WEBVIEW && pagechange == true) {
						ShowUnreadMemo(false);
					}
					//20150413 ADD-E For 2015年度Ver. 未読対応
					return;
				}
			});
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		//20140618 MOD-E For 連打抑制
	}

	public void onClickSkipNext(View view) {
		ClosePlayBack();
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応

		//20140618 MOD-S For 連打抑制
		/***
		setClickable(false);
		if(mClickable == true) {
			mClickable = false;
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	mHandler.post(new Runnable() {
			    		@Override
			    		public void run() {
			    			DoOnClickSkipNext();
			    			mClickable = true;
			    			setClickable(true);
			    		}
			    	});
			    }
			}).start();
		}
		***/
		if (mClickable == true) {
			setClickable(false);
			mClickable = false;
			ProgressShow();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(progressTime) ;
					}
					catch(Exception e) {
					}
					DoOnClickSkipNext();
				}
			}).start();
		}
		//20140618 MOD-E For 連打抑制
	}
	public void DoOnClickSkipNext() {
		//20140618 MOD-S For 連打抑制
		/***
		try {
			mBtnSkipBack.setEnabled(false);
			mAndroidMarkControl.DoOnClickSkipNextDone(mRelativeLayout, this);
			SetButtonProperties();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		***/
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// UI部品への操作;
					mBtnSkipNext.setEnabled(false);
					boolean pagechange = mAndroidMarkControl.DoOnClickSkipNextDone(mRelativeLayout, getApplicationContext());
					SetButtonProperties();

					mClickable = true;
					setClickable(true);
					ProgressHide();
					//20150413 ADD-S For 2015年度Ver. 未読対応
					if (mLearningMode != KumonDataCtrl.SF_DATATYPE_WEBVIEW && pagechange == true) {
						ShowUnreadMemo(false);
					}
					//20150413 ADD-E For 2015年度Ver. 未読対応
					return;
				}
			});
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}

		//20140618 MOD-E For 連打抑制
	}

	private void SetButtonProperties() {
		// 前へボタン
		if (mAndroidMarkControl.getBackStatus()) {
			mBtnBack.setEnabled(true);
			mBtnBack.setImageResource(R.drawable.bback);
		} else {
			mBtnBack.setEnabled(false);
			mBtnBack.setImageResource(R.drawable.bback_off);
		}
		// 次へボタン
		if (mAndroidMarkControl.getNextStatus()) {
			mBtnNext.setEnabled(true);
			mBtnNext.setImageResource(R.drawable.bnext);
		} else {
			mBtnNext.setEnabled(false);
			mBtnNext.setImageResource(R.drawable.bnext_off);
		}

		//SkipBack
		if (mAndroidMarkControl.getSkipBackStatusDone()) {
			mBtnSkipBack.setEnabled(true);
			mBtnSkipBack.setImageResource(R.drawable.bskipback);
		}
		else {
			mBtnSkipBack.setEnabled(false);
			mBtnSkipBack.setImageResource(R.drawable.bskipback_off);
		}

		//SkipNext
		if (mAndroidMarkControl.getSkipNextStatusDone()) {
			mBtnSkipNext.setEnabled(true);
			mBtnSkipNext.setImageResource(R.drawable.bskipnext);
		}
		else {
			mBtnSkipNext.setEnabled(false);
			mBtnSkipNext.setImageResource(R.drawable.bskipnext_off);
		}
	}
	public void onClickPenPlayBack(View view) {
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応
		if(mPenPlayBack == false) {
			OpenPlayBackPanel();
		}
		else {
			ClosePlayBack();
		}
	}
	public void onClickRPStop(View view) {
		PlayBackBtnImage();
		mImagebuttonRPStop.setImageResource(R.drawable.rp_stop_on);
		mAndroidMarkControl.REPLAY_Stop();
	}
	public void onClickRPPause(View view) {
		PlayBackBtnImage();
		mImagebuttonRPPose.setImageResource(R.drawable.rp_stopat_on);
		mAndroidMarkControl.REPLAY_Pause();
	}
	public void onClickRPPlay1(View view) {
		PlayBackBtnImage();
		mImagebuttonRPPlay1.setImageResource(R.drawable.rp_startx1_on);
		mAndroidMarkControl.REPLAY_Play(ReplayTask.SF_MODE_REPLAYX1);
	}
	public void onClickRPPlay2(View view) {
		PlayBackBtnImage();
		mImagebuttonRPPlay2.setImageResource(R.drawable.rp_startx2_on);
		mAndroidMarkControl.REPLAY_Play(ReplayTask.SF_MODE_REPLAYX2);
	}
	public void onClickRPPlay4(View view) {
		PlayBackBtnImage();
		mImagebuttonRPPlay4.setImageResource(R.drawable.rp_startx4_on);
		mAndroidMarkControl.REPLAY_Play(ReplayTask.SF_MODE_REPLAYX4);
	}
	public void onClickRPPlay8(View view) {
		PlayBackBtnImage();
		mImagebuttonRPPlay8.setImageResource(R.drawable.rp_startx8_on);
		mAndroidMarkControl.REPLAY_Play(ReplayTask.SF_MODE_REPLAYX8);
	}
	public void onClickRPBack(View view) {
		PlayBackBtnImage();
		mImagebuttonRPBack.setImageResource(R.drawable.rp_back_on);
		mAndroidMarkControl.REPLAY_Back();
	}
	public void onClickRPNext(View view) {
		PlayBackBtnImage();
		mImagebuttonRPNext.setImageResource(R.drawable.rp_next_on);
		mAndroidMarkControl.REPLAY_Next();
	}
	private void setRPTime(long pass, long all) {

		SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.JAPAN);
		Date date = new Date(pass); // 単位はミリ秒
		String strpass = format.format(date);
		date = new Date(all); // 単位はミリ秒
		String strall = format.format(date);

		mTextViewRPTime.setText(strpass + " / " + strall);

		mProgressBarPRProgress.setMax((int)all);
		mProgressBarPRProgress.setProgress((int)pass);

	}
	//Call Back
	@Override
	public void PlayBackCallback(int MODE, long PASSTIME) {
		if(	mPenPlayBack == false) return;

		if(mAndroidMarkControl.REPLAY_BackBtnStatus()) {
			mImagebuttonRPBack.setImageResource(R.drawable.rp_back_off);
		}
		else {
			mImagebuttonRPBack.setImageResource(R.drawable.rp_back_gray);
		}
		if(mAndroidMarkControl.REPLAY_NextBtnStatus()) {
			mImagebuttonRPNext.setImageResource(R.drawable.rp_next_off);
		}
		else {
			mImagebuttonRPNext.setImageResource(R.drawable.rp_next_gray);
		}

		if(MODE == PenPlayBackCallback.SF_PLAYBACK_PASS) {
			setRPTime(PASSTIME, mPlayBackTime);
		}
		else if(MODE == PenPlayBackCallback.SF_PLAYBACK_STOP) {
			PlayBackBtnImage();
			mImagebuttonRPStop.setImageResource(R.drawable.rp_stop_on);
			mImagebuttonRPListen.setImageResource(R.drawable.rp_listen_off);
		}
		else if(MODE == PenPlayBackCallback.SF_PLAYBACK_CLOSE) {
			ClosePlayBack();
		}
		//20140528 ADD-S For SoundStroke
		else if(MODE == PenPlayBackCallback.SF_PLAYBACK_SOUNDSTART) {
			mImagebuttonRPListen.setImageResource(R.drawable.rp_listen_on);
			SoundStopTimerTask task = new SoundStopTimerTask();
			if(mSoundStopTimer != null) {
				mSoundStopTimer.cancel();
				mSoundStopTimer = null;
			}
			mSoundStopTimer = new Timer();
			mSoundStopTimer.schedule(task, PASSTIME);
		}
		else if(MODE == PenPlayBackCallback.SF_PLAYBACK_SOUNDSTOP) {
			mImagebuttonRPListen.setImageResource(R.drawable.rp_listen_off);
		}
		//20140528 ADD-E For SoundStroke
	}
	private void OpenPlayBackPanel() {

		mPenPlayBack = true;

		mTablePenPlayBack.setVisibility(View.VISIBLE);
//		mTextViewPenPlayBack.setVisibility(View.VISIBLE);

		mAndroidMarkControl.REPLAY_Start(StudyConfirmActivity.this);
		PlayBackBtnImage();
		mPlayBackTime = mAndroidMarkControl.REPLAY_GetPlayBackTime();
		setRPTime(0, mPlayBackTime);


		mImagebuttonRPPlay1.setImageResource(R.drawable.rp_startx1_on);
		mAndroidMarkControl.REPLAY_Play(ReplayTask.SF_MODE_REPLAYX1);
	}
	private void ClosePlayBack() {
		mPenPlayBack = false;
		mAndroidMarkControl.REPLAY_End();
		mTablePenPlayBack.setVisibility(View.GONE);
//		mTextViewPenPlayBack.setVisibility(View.GONE);
	}
	private void PlayBackBtnImage() {
		mImagebuttonRPStop.setImageResource(R.drawable.rp_stop_off);
		mImagebuttonRPPose.setImageResource(R.drawable.rp_stopat_off);
		mImagebuttonRPPlay1.setImageResource(R.drawable.rp_startx1_off);
		mImagebuttonRPPlay2.setImageResource(R.drawable.rp_startx2_off);
		mImagebuttonRPPlay4.setImageResource(R.drawable.rp_startx4_off);
		mImagebuttonRPPlay8.setImageResource(R.drawable.rp_startx8_off);
		//20140528 ADD-S For SoundStroke
		mImagebuttonRPListen.setImageResource(R.drawable.rp_listen_off);
		if (mSoundStopTimer != null) {
			mSoundStopTimer.cancel();
			mSoundStopTimer = null;
		}
		//20140528 ADD-E For SoundStroke
	}
	//20140618 ADD-S For 連打抑制
//	private ProgressDialog progressDialog = null;
	private int progressTime = /*300*/ 100;

	private void ProgressShow() {
//		if(progressDialog != null) {
//			progressDialog.dismiss();
//			progressDialog = null;
//		}
//	    progressDialog = new ProgressDialog(this);
//	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	    progressDialog.setCancelable(false);
//		KumonMessage.showProgress(progressDialog, this, KumonMessage.MSG_No53);
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

/*
	    progressDialog.setTitle("処理中");
	    progressDialog.setMessage("お待ちください。");
	    progressDialog.show();

	    progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
	    progressDialog.getWindow().getDecorView().setSystemUiVisibility(visibility);
 */	    
	}
	private void ProgressHide() {
		closeProgress();
//		if(progressDialog != null) {
//			progressDialog.dismiss();
//			progressDialog = null;
//		}
	}
	//20140618 ADD-E For 連打抑制

	//20150413 ADD-E For 2015年度Ver. 未読対応
	private void MemoPageBack() {
		ProgressShow();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(progressTime) ;
				}
				catch(Exception e) {
				}
				DoMemoPageBack();
			}
		}).start();
	}
	public void DoMemoPageBack() {
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mAndroidMarkControl.DoOnClickMemoPageBack(mRelativeLayout, getApplicationContext());
					SetButtonProperties();
					ProgressHide();
					DoOnClickMemo();
					return;
				}
			});
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	private void MemoPageNext() {
		ProgressShow();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(progressTime) ;
				}
				catch(Exception e) {
				}
				DoMemoPageNext();
			}
		}).start();
	}
	public void DoMemoPageNext() {
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mAndroidMarkControl.DoOnClickMemoPageNext(mRelativeLayout, getApplicationContext());
					SetButtonProperties();
					ProgressHide();
					DoOnClickMemo();
					return;
				}
			});
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	//20150413 ADD-S For 2015年度Ver. 未読対応

	// ///////////////////////////////////////////////////////////////////////////////
	private void GetGradingResultEnd(WDownloadPrintSetData downloadprintsetdata) {
		View.OnClickListener okListener = new View.OnClickListener() {
			public void onClick(View v) {
				finish();
	    		mHandler.post( new Runnable() {
	    			public void run() {
	    				activity_finish();
	    			}
	    		});
			}
		};

		if (downloadprintsetdata.mSoapStatus == 0 && downloadprintsetdata.mSoapError.isEmpty()) {
			//20150310 ADD-S For Web音声データ展開
			TblSoundRecordData.DB_InsertSoundDataList(downloadprintsetdata.mDowunLoadPrintSet.mResultList, 1);
			TblSoundCommentData.DB_InsertSoundCommentDataList(downloadprintsetdata.mDowunLoadPrintSet.mResultList, 1);
			//20150310 ADD-E For Web音声データ展開

			int page = 0;
			mAndroidMarkControl.loadDataFromWeb(downloadprintsetdata.mDowunLoadPrintSet);
			for(int i = 0; i < downloadprintsetdata.mDowunLoadPrintSet.mResultList.size(); i++) {
				if(mPrintUnitID_WEB.equals(downloadprintsetdata.mDowunLoadPrintSet.mResultList.get(i).mPrintUnitID)) {
					page = i;
				}
			}
			//20140915 MOD-S For Bug
			//mAndroidMarkControl.InitializeQuestionControl(page, true, true, mRelativeLayout, this);
			mAndroidMarkControl.InitializeQuestionControl(page, true, true, mRelativeLayout, this, 0);
			//20140915 MOD-E For Bug
			SetButtonProperties();
		} else {
			if (downloadprintsetdata.mSoapError.isEmpty()) {
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No8, 0, okListener);
			} else {
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.SF_TTL_NOCONNECT, downloadprintsetdata.mSoapError, 0, okListener);
			}
		}
	}
	private void activity_finish()
	{
		finish();
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private static class SoapReceiveGradingResultTaskLoader extends AsyncTaskLoader</*DPrintSet, Integer, */WDownloadPrintSetData> {
		private CurrentUser mUser = null;
		DPrintSet mPrintset = null;
//		private ProgressDialog mProgressDialog;

		public SoapReceiveGradingResultTaskLoader(Context context, CurrentUser user, DPrintSet printset) {
			super(context);
			mUser = user;
			mPrintset = printset;
		}
/*
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(mActivity);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setCancelable(false);
			KumonMessage.showProgress(mProgressDialog, mActivity, KumonMessage.MSG_No21);
		}

		// UI スレッドではない
		// バックグラウンドでダウンロード処理を行う
		@Override
		protected WDownloadPrintSetData doInBackground(DPrintSet... printset) {
			KumonSoap soap = new KumonSoap();
			WDownloadPrintSetData downloadprintsetdata = null;

			downloadprintsetdata = soap.SaopGetGradingResultForWeb(mUser, printset[0]);
			if (downloadprintsetdata.mSoapStatus == 0
					&& downloadprintsetdata.mSoapError.isEmpty()
					&& downloadprintsetdata.mDowunLoadPrintSet.mResultList.size() > 0) {

				for(int i = 0; i < downloadprintsetdata.mDowunLoadPrintSet.mResultList.size(); i++) {
					DResultData resultdata = downloadprintsetdata.mDowunLoadPrintSet.mResultList.get(i);
					MQuestion2 question = MastDBIO.DB_GetPrint(resultdata.mPrintID);
					resultdata.mQuestion = question;
				}
			}
			return downloadprintsetdata;
		}

		// UI スレッド
		// プログレスバーの表示を更新する
		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		// UI スレッド
		// ダイアログを表示する
		@Override
		protected void onPostExecute(final WDownloadPrintSetData downloadprintsetdata) {
			mProgressDialog.dismiss();

			//20130508 MOD-S
			//GetGradingResultEnd(wprintdatalist);
			mHandler.post( new Runnable() {
				public void run() {
					GetGradingResultEnd(downloadprintsetdata);
				}
			});
			//20130508 MOD-E

		}
*/
		@Override
		public WDownloadPrintSetData loadInBackground() {
			KumonSoap soap = new KumonSoap();
			WDownloadPrintSetData downloadprintsetdata = null;

			downloadprintsetdata = soap.SoapGetGradingResultForWeb(mUser, mPrintset);
			if (downloadprintsetdata.mSoapStatus == 0
					&& downloadprintsetdata.mSoapError.isEmpty()
					&& downloadprintsetdata.mDowunLoadPrintSet.mResultList.size() > 0) {

				ArrayList<String> printIds = new ArrayList<String>();
				for(DResultData resultData : downloadprintsetdata.mDowunLoadPrintSet.mResultList) {
					printIds.add(resultData.mPrintID);
				}
				HashMap<String, MQuestion2> questions = MastDBIO.DB_GetPrints(printIds);
				for(DResultData resultData : downloadprintsetdata.mDowunLoadPrintSet.mResultList) {
					MQuestion2 question = questions.get(resultData.mPrintID);
					resultData.mQuestion = question;
				}
/*
				for(int i = 0; i < downloadprintsetdata.mDowunLoadPrintSet.mResultList.size(); i++) {
					DResultData resultdata = downloadprintsetdata.mDowunLoadPrintSet.mResultList.get(i);
					MQuestion2 question = MastDBIO.DB_GetPrint(resultdata.mPrintID);
					resultdata.mQuestion = question;
				}
*/
			}
			return downloadprintsetdata;
		}
	}

	private void InitializeQuestionControlTimer() {
//		if (mProgressDialog != null) {
//			mProgressDialog = null;
//		}
//		mProgressDialog = new ProgressDialog(this);
//		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		mProgressDialog.setIndeterminate(false);
//		mProgressDialog.setCancelable(false);
//		KumonMessage.showProgress(mProgressDialog, this, KumonMessage.MSG_No21);
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_INITIALIZEQUESTION, this, null, mControlLoaderCallback);
	}

	private boolean loadData() {
		MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName()+"#loadData");
		
		boolean ret = mAndroidMarkControl.loadDataFromDB(0, false);
		logger.addSplit("loadDataFromDB");
		if (ret == false) {
/* Can't work in BG
 			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDY, ScreenChange.SCNO_SPLASH, true, null, 0, ScreenChange.SF_NEXT);
 */
			return false;
		}
		//20140915 MOD-S For Bug
		//mAndroidMarkControl.InitializeQuestionControl(0, true, true, mRelativeLayout, this);

/* Can't work in BG
		mAndroidMarkControl.InitializeQuestionControl(0, true, true, mRelativeLayout, this, 0);
 */
		logger.addSplit("InitializeQuestionControl");
		//20140915 MOD-E For Bug
		// ペンの選択状態
/* Can't work in BG
		SetButtonProperties();
 */
		logger.addSplit("SetButtonProperties");
		logger.dumpToLog();
		return true;
	}

	private void setClickable(boolean mode) {
		mBtnSkipBack.setClickable(mode);
		mBtnBack.setClickable(mode);
		mBtnNext.setClickable(mode);
		mBtnSkipNext.setClickable(mode);
	}
	//20140731 ADD-S For Memo
	public void onClickMemo(View view) {
		DoOnClickMemo();
	}
	public void DoOnClickMemo() {

		mAndroidMarkControl.UpdateUnreadFlg();

		boolean land = mAndroidMarkControl.getOrientation();
		String tagcmt = mAndroidMarkControl.getTagComment();
		String tagtext = mAndroidMarkControl.getTagText();
        //20150210 ADD-S For 2015年度Ver. 音声メモ
		String soundcomment = mAndroidMarkControl.GetSoundCommentFileName();
        //20150210 ADD-E For 2015年度Ver. 音声メモ

		Intent intent = null;
		if(land) {
			//横
			intent = new Intent(this, MemoLandActivity.class);
		}
		else {
			//縦
			intent = new Intent(this, MemoActivity.class);
		}
		intent.putExtra(MemoActivity.SF_MEMOINK, tagcmt);
        intent.putExtra(MemoActivity.SF_MEMOTEXT, tagtext);
        //20150210 ADD-S For 2015年度Ver. 音声メモ
        intent.putExtra(MemoActivity.SF_MEMOSOUND, soundcomment);
        //20150210 ADD-E For 2015年度Ver. 音声メモ

		//20150413 ADD-S For 2015年度Ver. 未読対応
        intent.putExtra(MemoActivity.SF_SHOWPAGEBUTTON, true);
        boolean bBack = false;
        boolean bNext = false;
        int idx = mAndroidMarkControl.getMemoPageBackIndex();
        if(idx >= 0) {
            bBack = true;
        }
        idx = mAndroidMarkControl.getMemoPageNextIndex();
        if(idx >= 0) {
        	bNext = true;
        }
        intent.putExtra(MemoActivity.SF_BACKENABLE, bBack);
        intent.putExtra(MemoActivity.SF_NEXTENABLE, bNext);
		//20150413 ADD-E For 2015年度Ver. 未読対応

		//20150413 MOD-S For 2015年度Ver. 未読対応
		//intent.setAction(Intent.ACTION_VIEW);
		//startActivity(intent);

		startActivityForResult(intent, MemoActivity.SF_RESULTCODE);
		//20150413 MOD-E For 2015年度Ver. 未読対応
	}
	//20150413 ADD-S For 2015年度Ver. 未読対応
	private int ShowUnreadMemo(boolean doBackground) {
		int unread = mAndroidMarkControl.getUnreadStatus();
		if(unread > 0 && !doBackground) {
			DoOnClickMemo();
		}
		return unread;
	}
	//20150413 ADD-E For 2015年度Ver. 未読対応

	//20140731 ADD-E For Memo
	//20140731 ADD-S For 録音対応
	//****************************************************************************************************
	//*** 音声録音用
	//****************************************************************************************************
	private boolean mOpenRecordPanel = false;
	private TableLayout mTableRecord = null;
//	private TextView 	mTextViewRecord = null;

	private ImageView 	mImagebuttonStop = null;
	private ImageView 	mImagebuttonStart = null;
	private ImageView 	mImagebuttonPlay = null;
	//20150115 ADD-S Pouse
	private ImageView 	mImagebuttonPause = null;
	//20150115 ADD-E Pouse

	private int mRecordingMode = RecordCallback.SF_MODE_HIDE;
	private int mCurrentIconPos = -1;

	private ProgressBar mProgressBarRCProgress = null;
	private TextView 	mTextViewRCTime = null;
	private Timer 				m_RecTimer = null;
	private RecordingTimerTask 	m_RecTimerTask = null;
	private long 				m_RecTime = 0;


	//Call Back
	@Override
	public void recordCallback(int MODE, int pos) {
		//20150115 MOD-S 再生時もダイアログ表示
		//if(MODE == pothos.view.RecordCallback.SF_RECORDICON_CLICK) {
		if(MODE == kumon2014.view.RecordCallback.SF_RECORDICON_CLICK || MODE == kumon2014.view.RecordCallback.SF_SOUNDICON_CLICK) {
		//20150115 MOD-E 再生時もダイアログ表示
			OpenRecordPanel(MODE, pos);
		}
		else if(MODE == RecordCallback.SF_RECORD_START) {
		}
		else if(MODE == RecordCallback.SF_RECORD_STOP) {
			mRecordingMode = RecordCallback.SF_MODE_STOPPING;
			RecordBtnImage();

			//20140805 ADD-S For 録音
			if (m_RecTimer != null) {
				m_RecTimer.cancel();
				m_RecTimer = null;
			}
			setRecDuration(mCurrentIconPos);
			//20140805 ADD-E For 録音
		}
	}
	private void OpenRecordPanel(int MODE, int pos) {
		ClosePlayBack();

		if(mOpenRecordPanel) {
			return;
		}
		//20150115 MOD-S 再生時もダイアログ表示
		if(MODE == kumon2014.view.RecordCallback.SF_RECORDICON_CLICK) {
			Recordable = true;
		}
		else {
			Recordable = false;
		}
		//20150115 MOD-E 再生時もダイアログ表示

		mCurrentIconPos = pos;
		mOpenRecordPanel = true;
		mRecordingMode = RecordCallback.SF_MODE_STOPPING;

		mTableRecord.setVisibility(View.VISIBLE);
//		mTextViewRecord.setVisibility(View.VISIBLE);

		if(Recordable == true) {
			mProgressBarRCProgress.setVisibility(View.VISIBLE);
			mTextViewRCTime.setVisibility(View.VISIBLE);;
			setRecDuration(mCurrentIconPos);
		}
		else {
			mProgressBarRCProgress.setVisibility(View.INVISIBLE);
			mTextViewRCTime.setVisibility(View.INVISIBLE);;
		}

		RecordBtnImage();
//NEW_VER MOD-S
//		mAndroidMarkControl.setPenThreadMode(false);
		mAndroidMarkControl.setPenEnabled(false);
//NEW_VER MOD-E
	}
	private void CloseRecordPanel() {
		//20140805 ADD-S For 録音
		if (m_RecTimer != null) {
			m_RecTimer.cancel();
			m_RecTimer = null;
		}
		//20140805 ADD-E For 録音

//NEW_VER MOD-S
		//mAndroidMarkControl.setPenThreadMode(true);
// この画面でペン入力を受け取ることはあるのだろうか?
		//mAndroidMarkControl.setPenEnabled(true);
		mAndroidMarkControl.setPenEnabled(false);
//NEW_VER MOD-E
		if(mRecordingMode == RecordCallback.SF_MODE_RECORDING) {
			mAndroidMarkControl.REC_Recording_Stop();
		}
		else if(mRecordingMode == RecordCallback.SF_RECORD_PLAY) {
			mAndroidMarkControl.REC_Play_Stop();
		}
		mCurrentIconPos = -1;
		mRecordingMode = RecordCallback.SF_MODE_HIDE;

		mOpenRecordPanel = false;

		mTableRecord.setVisibility(View.GONE);
//		mTextViewRecord.setVisibility(View.GONE);
	}
	private void RecordBtnImage() {
		mImagebuttonStart.setEnabled(false);
		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING)  {
			//停止
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_no);
			mImagebuttonStart.setImageResource(R.drawable.record_no);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off);
			//PuaseButton
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_no);
			mImagebuttonPause.setEnabled(false);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_RECORDING)  {
			//録音中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonStart.setImageResource(R.drawable.record_no);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off);
			//PuaseButton
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_no);
			mImagebuttonPause.setEnabled(false);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PLAYING)  {
			//再生中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonStart.setImageResource(R.drawable.record_no);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_on);
			//PuaseButton
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_off);
			mImagebuttonPause.setEnabled(true);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PAUSE)  {
			//Pause中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off);
			mImagebuttonStart.setImageResource(R.drawable.record_no);
			//PuaseButton
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_on);
			mImagebuttonPause.setEnabled(true);
		}
	}
	public void onClickDummy(View view) {
	}
	public void onClickRecordStop(View view) {
		//20140805 ADD-S For 録音
		if (m_RecTimer != null) {
			m_RecTimer.cancel();
			m_RecTimer = null;
		}
		//20140805 ADD-E For 録音

		if(mRecordingMode == RecordCallback.SF_MODE_RECORDING) {
			mAndroidMarkControl.REC_Recording_Stop();
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PLAYING) {
			mAndroidMarkControl.REC_Play_Stop();
		}
		mRecordingMode = RecordCallback.SF_MODE_STOPPING;
		RecordBtnImage();
		setRecDuration(mCurrentIconPos);
	}
	public void onClickRecordStart(View view) {
		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING) {

//			if (mProgressDialog != null) {
//				mProgressDialog = null;
//			}
//			mProgressDialog = new ProgressDialog(this);
//			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			mProgressDialog.setIndeterminate(false);
//			mProgressDialog.setCancelable(false);
//			KumonMessage.showProgress(mProgressDialog, this, KumonMessage.MSG_No47);
			
			showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No47);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(100) ;
					}
					catch(Exception e) {
					}
					DoOnClickRecordStart();
				}
			}).start();
		}
	}
	public void DoOnClickRecordStart() {
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mAndroidMarkControl.REC_Recording_Start(mCurrentIconPos);
//					mProgressDialog.dismiss();
					closeProgress();

					//20140805 ADD-S For 録音
					m_RecTime = 0;
					m_RecTimerTask = new RecordingTimerTask();
					m_RecTimer = new Timer(true);
					m_RecTimer.schedule(m_RecTimerTask, 0, 1000);
					//20140805 ADD-E For 録音

					mRecordingMode= RecordCallback.SF_MODE_RECORDING;
					RecordBtnImage();
					return;
				}
			});

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void onClickRecordPlay(View view) {
		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING) {
			//20140805 ADD-S For 録音
			long rectime = mAndroidMarkControl.REC_Get_Duration(Recordable, mCurrentIconPos);
			//rectime += 999;
			SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.JAPAN);
			Date date = new Date(rectime);
			String strpass = format.format(date);
			mTextViewRCTime.setText(strpass);
			mProgressBarRCProgress.setMax((int) rectime);

			m_RecTime = 0;
			m_RecTimerTask = new RecordingTimerTask();
			m_RecTimer = new Timer(true);
			m_RecTimer.schedule(m_RecTimerTask, 0, 1000);

			mAndroidMarkControl.REC_Play_Start(Recordable, mCurrentIconPos);
			mRecordingMode= RecordCallback.SF_MODE_PLAYING;
			RecordBtnImage();
			//20140805 ADD-E For 録音
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PAUSE) {
			m_RecTimerTask = new RecordingTimerTask();
			m_RecTimer = new Timer(true);
			m_RecTimer.schedule(m_RecTimerTask, 0, 1000);

			mAndroidMarkControl.REC_Play_ReStart();
			mRecordingMode= RecordCallback.SF_MODE_PLAYING;
			RecordBtnImage();
		}
	}
	public void onClickRecordClose(View view) {
		CloseRecordPanel();
	}
	public void onClickRecordPause(View view) {
		if(mRecordingMode == RecordCallback.SF_MODE_PLAYING) {

			if (m_RecTimer != null) {
				m_RecTimer.cancel();
				m_RecTimer = null;
			}
			mAndroidMarkControl.REC_Play_Pause();
			mRecordingMode= RecordCallback.SF_MODE_PAUSE;
			RecordBtnImage();
		}
	}

	//20140805 ADD-S For 録音
	private void setRCTime(boolean recording, long pass) {

		if(mRecordingMode == RecordCallback.SF_RECORD_PLAY) {
			int Progress = (int)(pass);
			mProgressBarRCProgress.setProgress(Progress);
		}
		else {
			String strpass = String.valueOf(Math.round(pass / 1000)) + "秒";
			mTextViewRCTime.setText(strpass);

			mProgressBarRCProgress.setMax(1000);
			mProgressBarRCProgress.setProgress(0);
		}
	}
	private void setRecDuration(int pos) {
		if(Recordable) {
			//20150115 MOD-S 再生時もダイアログ表示
			//int rectime = mAndroidMarkControl.REC_Get_Duration(pos);
			long rectime = mAndroidMarkControl.REC_Get_Duration(Recordable, pos);
			//rectime += 999;	//秒以下は切り上げ
			//20150115 MOD-E 再生時もダイアログ表示
			setRCTime(false, rectime);
		}
	}
	//20140805 ADD-E For 録音

	public void setFixedOrientation(Boolean flg){
	    if(flg){
	        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	        Configuration config = getResources().getConfiguration();
	        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
	            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        }else if (config.orientation == Configuration.ORIENTATION_PORTRAIT){
	            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        }
	    }else{
	        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	    }
	}
    @SuppressWarnings("unused")
	private void lockScreenOrientation(Activity activity, Boolean flg){
        if(flg){
//	        Configuration config = getResources().getConfiguration();
            switch (((WindowManager) activity.getSystemService(Activity.WINDOW_SERVICE))
                    .getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_90:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case Surface.ROTATION_180:
                activity.setRequestedOrientation(9/* reversePortait */);
                break;
            case Surface.ROTATION_270:
                activity.setRequestedOrientation(8/* reverseLandscape */);
                break;
            default :
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }else{
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }


	// *************************************************************
    /**
     * 問題初期化処理用TaskLoader
     * @author shinm
     *
     */
    static class QuetsionTaskResult {
    	public boolean fLoadData = false;
    	public int unreadCount = 0;
    }
    static class QuestionControlTask extends AsyncTaskLoader<QuetsionTaskResult> {
    	StudyConfirmActivity activity;
		public QuestionControlTask(StudyConfirmActivity context) {
			super(context);
			activity = context;
		}

		@Override
		public QuetsionTaskResult loadInBackground() {
			Log.d(getClass() + "#loadInBackground", "Start");
			QuetsionTaskResult ret = new QuetsionTaskResult();
			ret.fLoadData = activity.loadData();
			
			//20150413 ADD-S For 2015年度Ver. 未読対応
			ret.unreadCount = activity.ShowUnreadMemo(true);
			//20150413 ADD-E For 2015年度Ver. 未読対応
			Log.d(getClass() + "#loadInBackground", "End");
			return ret;
		}
    }
    KumonLoaderCallbacks<QuetsionTaskResult> mControlLoaderCallback = new KumonLoaderCallbacks<QuetsionTaskResult>() {

		@Override
		public Loader<QuetsionTaskResult> onCreateLoader(int arg0, Bundle arg1) {
			Loader<QuetsionTaskResult> loader = new QuestionControlTask(StudyConfirmActivity.this);
			loader.forceLoad();
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<QuetsionTaskResult> arg0,
				QuetsionTaskResult result) {
			if (!result.fLoadData) {
				closeProgress();
//				mProgressDialog.dismiss();
				ScreenChange.doScreenChange(StudyConfirmActivity.this, ScreenChange.SCNO_STUDY, ScreenChange.SCNO_SPLASH, true, null, 0, ScreenChange.SF_NEXT);
			}
			else {
				mAndroidMarkControl.InitializeQuestionControl(0, true, true, mRelativeLayout, StudyConfirmActivity.this, 0);
				closeProgress();
//				mProgressDialog.dismiss();
				if (result.unreadCount > 0) {
					DoOnClickMemo();
				}
			}
		}

    };
    
	//20140528 ADD-S For SoundStroke
    class SoundStopTimerTask extends TimerTask{
    	@Override
    	public void run() {
    		mSoundStopTimer = null;
    		mHandler.post( new Runnable() {
    			public void run() {
    				PlayBackCallback(PenPlayBackCallback.SF_PLAYBACK_SOUNDSTOP, 0);
    			}
    		});
    	}
    }
	//20140528 ADD-E For SoundStroke
	//20140805 ADD-S For 録音
	class RecordingTimerTask extends TimerTask {
		public RecordingTimerTask() {
		}

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				public void run() {
					setRCTime(true, m_RecTime);
					m_RecTime += 1000L;
				}
			});
		}
	}
	//20140805 ADD-E For 録音

	public void reFreshPage() {
		//20140618 MOD-S For 連打抑制
		/***
		try {
			mBtnNext.setEnabled(false);
			mAndroidMarkControl.DoOnClickNext(mRelativeLayout, this);
			SetButtonProperties();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		***/
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// UI部品への操作;
					mBtnNext.setEnabled(false);
					
					boolean pagechange = mAndroidMarkControl.refreshPage(mRelativeLayout, getApplicationContext());
					SetButtonProperties();
					mClickable = true;
					setClickable(true);
					ProgressHide();
					
					if (pagechange == true) {
						ShowUnreadMemo(false);
					}
					return;
				}
			});

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		//20140618 MOD-E For 連打抑制

	}
	
}
