package kumon2014.activity;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.markcontroltool.AndroidMarkControl;
import kumon2014.message.KumonMessage;
import kumon2014.message.KumonMessage.KumonMessageDetail;
import kumon2014.view.MarkControlSurface;
import kumon2014.view.RecordCallback;
import pothos.markcontroltool.MdtMode;
import pothos.markcontroltool.InkControlTool.CInkMain;

/**
 * 学習画面 
 *
 */
public class StudyActivity extends BaseActivity implements RecordCallback {
	private static int REC_OFFSET_Time = 1500;	//ミリ秒

    private int 	mEntrance;
    private int 	mLearningmode;

	private AndroidMarkControl mAndroidMarkControl = null;


	private boolean bNormalEnd = false;
	CurrentUser mCurrentUser = null;
	private int mReStart = 0;

	private Handler mHandler = new Handler();
	private Timer m_Timer = null;
	private QuestionControlTimerTask m_TimerTask = null;
	private boolean mBRedPenMode = false;
	private boolean mIsSpecalTest = false;
	private Timer m_LimitTimer = null;
	private LimitTimerTask m_LimitTimerTask = null;
	private int mLimitTime = 0;
	// 暫定
	private int mPenKind = CInkMain.SF_PENKIND_BALLPEN;
	private int mPenWidth = 3;
//NEW_VER MOD-S
//	private EraserSize mEraserWidth = EraserSize.Normal;
	private float mEraserWidth = KumonDataCtrl.SF_Eraser_Normal;
//NEW_VER MOD-E

//NEW_VER DEL-S
//	private final int F_PEN_THIN = 2;
//	private final int F_PEN_NORMAL = 3;
//	private final int F_PEN_BOLD = 6;
//NEW_VER DEL-E

	private int mFromPage;
	private Context mContext;

	private TableRow mPageBar = null;
	private Button mRedPen = null;

	private RadioButton mPenS = null;
	private RadioButton mPenM = null;
	private RadioButton mPenL = null;
	private RadioButton mEraserS = null;
	private RadioButton mEraserM = null;
	private RadioButton mEraserL = null;

	private ImageButton mBtnFinish = null;
	private ImageButton mBtnRest = null;

	private ImageButton mBtnBack = null;
	private ImageButton mBtnNext = null;
	private boolean mClickable = true;

	private ImageButton mInBtnBack = null;
	private ImageButton mInBtnNext = null;

	private ImageButton mInBtn1A = null;
	private ImageButton mInBtn1B = null;
	private ImageButton mInBtn2A = null;
	private ImageButton mInBtn2B = null;
	private ImageButton mInBtn3A = null;
	private ImageButton mInBtn3B = null;
	private ImageButton mInBtn4A = null;
	private ImageButton mInBtn4B = null;
	private ImageButton mInBtn5A = null;
	private ImageButton mInBtn5B = null;
	private ImageButton mInBtn6A = null;
	private ImageButton mInBtn6B = null;
	private ImageButton mInBtn7A = null;
	private ImageButton mInBtn7B = null;
	private ImageButton mInBtn8A = null;
	private ImageButton mInBtn8B = null;
	private ImageButton mInBtn9A = null;
	private ImageButton mInBtn9B = null;
	private ImageButton mInBtn10A = null;
	private ImageButton mInBtn10B = null;
	ArrayList<ImageButton> mInBtnlist = new ArrayList<ImageButton>();

	private RelativeLayout mRelativeLayout = null;

	//20140626 ADD-S For Undo
	private ImageButton mUndo = null;
	//20140626 ADD-E For Undo

	//20140731 ADD-S For Memo
	private ImageButton mMemo = null;
	//20140731 ADD-E For Memo

	//20150115 ADD-S 再生時もダイアログ表示
	private boolean Recordable = false;
	//20150115 ADD-E 再生時もダイアログ表示

	//20150115 ADD-S 録音時間は外部ファイル
	long MaxRecordingTime = 0;
	//20150115 ADD-E 録音時間は外部ファイル

	String debugTag = "debug";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(debugTag, "onCreate");
		super.onCreate(savedInstanceState);

		try {
			System.gc();
			setContentView(R.layout.activity_study);

			Intent intent = getIntent();
			mCurrentUser = (CurrentUser) intent.getSerializableExtra("CurrentUser");
			// 20130324 ADD-S For ペンの太さは前回使用の太さをキープ
			mPenWidth = mCurrentUser.mPenWidth;
			// 20130324 ADD-E
			mFromPage = intent.getIntExtra(ScreenChange.SF_FROMPAGE, ScreenChange.SCNO_STUDYSTART);
			mLearningmode = intent.getIntExtra(ScreenChange.SF_LEARNINGMODE,	KumonDataCtrl.SF_DATATYPE_NONE);
			mReStart = intent.getIntExtra(ScreenChange.SF_RESTARTY, 0);
			int nextpage = intent.getIntExtra(ScreenChange.SF_PAGECNT, 1);
			mEntrance = (int)intent.getIntExtra(ScreenChange.SF_ENTRANCE, ScreenChange.SF_NEXT);

			mIsSpecalTest = (boolean)intent.getBooleanExtra(ScreenChange.SF_SPECIALTEST, false);
			if(mIsSpecalTest) {
				mLimitTime = (int)intent.getIntExtra(ScreenChange.SF_LIMITTIME, 0);
			}

			mAndroidMarkControl = new AndroidMarkControl(mCurrentUser, mLearningmode, nextpage);

			mAndroidMarkControl.setView((MarkControlSurface) findViewById(R.id.MarkControlSurfaceMain));
			
			mPageBar = (TableRow) findViewById(R.id.tablerow_pagebar);
			mRedPen = (Button) findViewById(R.id.button_redpen);
			mAndroidMarkControl.setPageBar(mPageBar, mRedPen);

			mPenS = (RadioButton) findViewById(R.id.radiobutton_pen_ss);
			mPenM = (RadioButton) findViewById(R.id.radiobutton_pen_mm);
			mPenL = (RadioButton) findViewById(R.id.radiobutton_pen_ll);
			mEraserS = (RadioButton) findViewById(R.id.radiobutton_eraser_ss);
			mEraserM = (RadioButton) findViewById(R.id.radiobutton_eraser_mm);
			mEraserL = (RadioButton) findViewById(R.id.radiobutton_eraser_ll);

			RadioGroup groupPen = (RadioGroup) findViewById(R.id.radiogroup_pen);
			groupPen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					onCheckedChangedPen(group, checkedId);
				}
			});

			mBtnFinish = (ImageButton) findViewById(R.id.imagebutton_finish);
			mBtnRest = (ImageButton) findViewById(R.id.imagebutton_Rest);

			mBtnBack = (ImageButton) findViewById(R.id.imagebutton_back);
			mBtnNext = (ImageButton) findViewById(R.id.imagebutton_next);


			TextView viewpage = (TextView) findViewById(R.id.textview_Page);
			mInBtnBack = (ImageButton) findViewById(R.id.in_back);
			mInBtnNext = (ImageButton) findViewById(R.id.in_next);
			mInBtn1A = (ImageButton) findViewById(R.id.in_1A);
			mInBtnlist.add(mInBtn1A);
			mInBtn1B = (ImageButton) findViewById(R.id.in_1B);
			mInBtnlist.add(mInBtn1B);
			mInBtn2A = (ImageButton) findViewById(R.id.in_2A);
			mInBtnlist.add(mInBtn2A);
			mInBtn2B = (ImageButton) findViewById(R.id.in_2B);
			mInBtnlist.add(mInBtn2B);
			mInBtn3A = (ImageButton) findViewById(R.id.in_3A);
			mInBtnlist.add(mInBtn3A);
			mInBtn3B = (ImageButton) findViewById(R.id.in_3B);
			mInBtnlist.add(mInBtn3B);
			mInBtn4A = (ImageButton) findViewById(R.id.in_4A);
			mInBtnlist.add(mInBtn4A);
			mInBtn4B = (ImageButton) findViewById(R.id.in_4B);
			mInBtnlist.add(mInBtn4B);
			mInBtn5A = (ImageButton) findViewById(R.id.in_5A);
			mInBtnlist.add(mInBtn5A);
			mInBtn5B = (ImageButton) findViewById(R.id.in_5B);
			mInBtnlist.add(mInBtn5B);
			mInBtn6A = (ImageButton) findViewById(R.id.in_6A);
			mInBtnlist.add(mInBtn6A);
			mInBtn6B = (ImageButton) findViewById(R.id.in_6B);
			mInBtnlist.add(mInBtn6B);
			mInBtn7A = (ImageButton) findViewById(R.id.in_7A);
			mInBtnlist.add(mInBtn7A);
			mInBtn7B = (ImageButton) findViewById(R.id.in_7B);
			mInBtnlist.add(mInBtn7B);
			mInBtn8A = (ImageButton) findViewById(R.id.in_8A);
			mInBtnlist.add(mInBtn8A);
			mInBtn8B = (ImageButton) findViewById(R.id.in_8B);
			mInBtnlist.add(mInBtn8B);
			mInBtn9A = (ImageButton) findViewById(R.id.in_9A);
			mInBtnlist.add(mInBtn9A);
			mInBtn9B = (ImageButton) findViewById(R.id.in_9B);
			mInBtnlist.add(mInBtn9B);
			mInBtn10A = (ImageButton) findViewById(R.id.in_10A);
			mInBtnlist.add(mInBtn10A);
			mInBtn10B = (ImageButton) findViewById(R.id.in_10B);
			mInBtnlist.add(mInBtn10B);

			mAndroidMarkControl.SetButtonCtrl(mCurrentUser.mCurrentKyozaiName, viewpage, mInBtnBack, mInBtnNext, mInBtnlist);

	        mRelativeLayout = (RelativeLayout) findViewById(R.id.MarkControlFrame);

	        // 先頭、最後のボタンは非表示
	        View view = findViewById(R.id.imagebutton_skipback);
	        view.setVisibility(View.INVISIBLE);
	        view = findViewById(R.id.imagebutton_skipnext);
	        view.setVisibility(View.INVISIBLE);
	        
			//20140626 ADD-S For Undo
			int maxundocnt = 3;
			try{
				File propertyFile = StudentClientCommData.getPropertyFile();
				if(propertyFile.exists()) {
					FileInputStream is = new FileInputStream(propertyFile);
					Properties p = new Properties();
					p.load(is);
					String temp = p.getProperty("MaxUndoCnt");
					is.close();
		            maxundocnt = Integer.parseInt(temp);
				}
	        }catch(Exception e) {
	        	maxundocnt = 3;
	        }

			mUndo = (ImageButton) findViewById(R.id.imagebutton_undo);
			mAndroidMarkControl.SetUndoButton(mUndo, maxundocnt);
			//20140626 ADD-E For Undo

			//20140731 ADD-S For 録音対応
			mTableRecord = (TableLayout) findViewById(R.id.tableLayout_record);
			mTextViewRecord = (TextView) findViewById(R.id.textview_record);
			mTableRecord.setVisibility(View.GONE);
			mTextViewRecord.setVisibility(View.GONE);

			mProgressBarRCProgress = (ProgressBar) findViewById(R.id.progressBar_RCProgress);
			mTextViewRCTime = (TextView) findViewById(R.id.textview_RCTime);
			mTableRecord.setVisibility(View.GONE);
			mTextViewRecord.setVisibility(View.GONE);

			mImagebuttonStop = (ImageView) findViewById(R.id.imagebutton_RecordStop);
			mImagebuttonStart = (ImageView) findViewById(R.id.imagebutton_RecordStart);
			mImagebuttonPlay = (ImageView) findViewById(R.id.imagebutton_RecordPlay);
			//20150115 ADD-S 再生時もダイアログ表示
			mImagebuttonPause = (ImageView) findViewById(R.id.imagebutton_RecordPause);
			//20150115 ADD-E 再生時もダイアログ表示

			mAndroidMarkControl.setRecordCallBack(StudyActivity.this, mCurrentUser.mLoginID);
			//20140731 ADD-E For 録音対応

			//20140731 ADD-S For Memo
			mMemo = (ImageButton) findViewById(R.id.imagebutton_memo);
			mAndroidMarkControl.SetMemoBtn(mMemo);
			//20140731 ADD-E For Memo


			//20150115 ADD-S 録音時間は外部ファイル
			MaxRecordingTime = 30000L;
			try{
				File propertyFile = StudentClientCommData.getPropertyFile();
				if(propertyFile.exists()) {
					FileInputStream is = new FileInputStream(propertyFile);
					Properties p = new Properties();
					p.load(is);
					String temp = p.getProperty("MaxRecordingTime");
					is.close();
					MaxRecordingTime = Long.parseLong(temp);
				}
	        }catch(Exception e) {
	        	MaxRecordingTime = 30000L;
	        }
			//20150115 ADD-E 録音時間は外部ファイル

			InitializeQuestionControlTimer();

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(debugTag, "onCreateOptionsMenu");
		return true;
	}

	@Override
	protected void onRestart() {
		Log.d(debugTag, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		Log.d(debugTag, "onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d(debugTag, "onResume");
		super.onResume();
		
		setClickable(false);
		mClickable = false;
		ProgressShow();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000) ;
				}
				catch(Exception e) {
				}
				reFreshPage();
			}
		}).start();
		
		if (bNormalEnd == false) {
			// 休止中時間計測用のストップウォッチをスタート
			//mAndroidMarkControl.onClickStopRest();
		}
	}

	@Override
	protected void onPause() {
		Log.d(debugTag, "onPause");
		super.onPause();
		
		try {
 			mHandler.post(new Runnable() {
 				@Override
 				public void run() {
 					try {
 						Thread.sleep(1000);
 					} catch (InterruptedException e) {
 						e.printStackTrace();
 					}
 					
 					try {
	 					if (bNormalEnd == false) {
	 						//一応onPauseでSaveしておく
	 						mAndroidMarkControl.saveHalfwayTestData();
	 					}
	 				} catch (Exception e) {
	 					SLog.DB_AddException(e);
	 				}
 				}
 			});
 		} catch (Exception e) {
 			SLog.DB_AddException(e);
 		}
	}
	 
	@Override
	protected void onStop() {
		Log.d(debugTag, "onStop");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Log.d(debugTag, "onDestroy");
		super.onDestroy();
		try {
			if(bNormalEnd == true) {
				//正常終了(途中セーブテストのクリア)
				File lasttestFile = StudentClientCommData.getLastTestFile();
				if(lasttestFile.exists()) {
					lasttestFile.delete();
				}
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}

//		if (mProgressDialog != null) {
//			mProgressDialog = null;
//		}
		if (m_Timer != null) {
			m_Timer.cancel();
			m_Timer = null;
		}
		if (m_LimitTimer != null) {
			m_LimitTimer.cancel();
			m_LimitTimer = null;
		}

		//20140805 ADD-S For 録音
		if (m_RecTimer != null) {
			m_RecTimer.cancel();
			m_RecTimer = null;
		}
		//20140805 ADD-E For 録音

		Utility.cleanupView(findViewById(R.id.study_topview));
		System.gc();
	}

	@Override
	public void onLowMemory() {
		Log.d(debugTag, "onLowMemory");
		Utility.onLowMemory(getApplicationContext());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(debugTag, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	//20150413 ADD-S For 2015年度Ver. 未読対応
	// startActivityForResult で起動させたアクティビティが
	// finish() により破棄されたときにコールされる
	// requestCode : startActivityForResult の第二引数で指定した値が渡される
	// resultCode : 起動先のActivity.setResult の第一引数が渡される
	// Intent data : 起動先Activityから送られてくる Intent
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.d(debugTag, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, intent);

	    if(requestCode == MemoActivity.SF_RESULTCODE) {
	    	int mode = intent.getIntExtra(MemoActivity.SF_RETURN, 0);
	    	
			setDefaultPen();
			setClickable(false);
			mClickable = false;
			ProgressShow();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(3000) ;
					}
					catch(Exception e) {
					}
					reFreshPage();
				}
			}).start();
			
	    	if(mode == MemoActivity.SF_PAGEBACK) {
	    		//PageBack
	    	}
	    	else if(mode == MemoActivity.SF_PAGENEXT) {
	    		//PageNext
	    	}
	    	else {

	    	}
	    }else {
	    	//メモ画面以外は何もしない
	    }
	}
	//20150413 ADD-E For 2015年度Ver. 未読対応

	private void onCheckedChangedPen(RadioGroup group, int checkedId) {
		RadioButton radio = (RadioButton) findViewById(checkedId);
		if (radio.isChecked() == true) {
			switch (checkedId) {
			case (R.id.radiobutton_eraser_ss):
//NEW_VER MOD-S
				//mEraserWidth = EraserSize.Thin;
				mEraserWidth = KumonDataCtrl.SF_Eraser_Thin;
//NEW_VER MOD-E
				mPenKind = CInkMain.SF_PENKIND_NORMALERASER;
				break;
			case (R.id.radiobutton_eraser_mm):
//NEW_VER MOD-S
				//mEraserWidth = EraserSize.Normal;
				mEraserWidth = KumonDataCtrl.SF_Eraser_Normal;
//NEW_VER MOD-S
				mPenKind = CInkMain.SF_PENKIND_NORMALERASER;
				break;
			case (R.id.radiobutton_eraser_ll):
//NEW_VER MOD-S
				//mEraserWidth = EraserSize.Bold;
				mEraserWidth = KumonDataCtrl.SF_Eraser_Boid;
//NEW_VER MOD-S
				mPenKind = CInkMain.SF_PENKIND_NORMALERASER;
				break;

			case (R.id.radiobutton_pen_ss):
//NEW_VER MOD-S
				//mPenWidth = F_PEN_THIN;
				mPenWidth = KumonDataCtrl.SF_PEN_THIN;
//NEW_VER MOD-E
				mPenKind = CInkMain.SF_PENKIND_BALLPEN;
				break;
			case (R.id.radiobutton_pen_ll):
//NEW_VER MOD-S
				//mPenWidth = F_PEN_BOLD;
				mPenWidth = KumonDataCtrl.SF_PEN_BOLD;
//NEW_VER MOD-E
				mPenKind = CInkMain.SF_PENKIND_BALLPEN;
				break;
			default:
//NEW_VER MOD-S
				//mPenWidth = F_PEN_NORMAL;
				mPenWidth = KumonDataCtrl.SF_PEN_NORMAL;
//NEW_VER MOD-E
				mPenKind = CInkMain.SF_PENKIND_BALLPEN;
				break;
			}
			SetQuestionControlsPenState();
		}
	}

	/**
	 * 停止ボタン押下
	 * @param view
	 */
	public void onClickRest(View view) {
		try {
			//20140731 ADD-S For 録音対応
			CloseRecordPanel();
			//20140731 ADD-E For 録音対応

			mAndroidMarkControl.onClickRest();
			mAndroidMarkControl.setPenEnabled(false);
			
			View.OnClickListener okListener = new View.OnClickListener() {
				public void onClick(View v) {
					mAndroidMarkControl.setPenEnabled(true);
					mAndroidMarkControl.onClickStopRest();
				}
			};
			
			KumonMessageDetail detail = KumonMessage.getKumonMessageDetail(KumonMessage.MSG_No23);
			showOkDialog(R.layout.progress_msg_rest, detail.mTitle, detail.mMessage, 0, okListener);
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void onClickFinish(View view) {
		Log.d(debugTag, "onClickFinish");
		
		try {
			//20150212 ADD-S For 参照中は先頭ページへ遷移
			if(mAndroidMarkControl.mBShowTopQuestionData) {
				ProgressShow();
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(progressTime) ;
						}
						catch(Exception e) {
						}
						int pos = mAndroidMarkControl.getIndicatorStartPage();
						DoOnClickIndicator(pos);
					}
				}).start();
				
				return;
			}
			//20150212 ADD-E For 参照中は先頭ページへ遷移


			mAndroidMarkControl.StartStopwatch();
			//20140731 ADD-S For 録音対応
			CloseRecordPanel();
			//20140731 ADD-E For 録音対応

			if(mIsSpecalTest) {
				//診断・最終テスト
				if (mAndroidMarkControl.getSideIndex() == 0) {
					// 先頭ページでの終了ボタン
					mContext = this;
					View.OnClickListener yesListener = new View.OnClickListener() {
						public void onClick(View v) {
							mAndroidMarkControl.StopStopwatch();
							bNormalEnd = true;
							ScreenChange.doScreenChangeSpecialStart(mContext, ScreenChange.SCNO_STUDY, mCurrentUser, mLearningmode, mEntrance);
						}
					};
					View.OnClickListener noListener = new View.OnClickListener() {
						public void onClick(View v) {
							mAndroidMarkControl.StopStopwatch();
						}
					};
					showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No44, 0, yesListener, 0, noListener);
				} else  {
					// 先頭ページ以外
					mContext = this;
					View.OnClickListener yesListener = new View.OnClickListener() {
						public void onClick(View v) {
							// ここまでの学習内容を保存
							mAndroidMarkControl.StopStopwatch();
							mAndroidMarkControl.SaveCurrentPrintLearningData();
							SavePrintSetData(mAndroidMarkControl.getPageIndex());
						}
					};
					View.OnClickListener noListener = new View.OnClickListener() {
						public void onClick(View v) {
							mAndroidMarkControl.StopStopwatch();
						}
					};
					showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No45, 0, yesListener, 0, noListener);
				}


				return;
			}

			if ((mAndroidMarkControl.getSideIndex() == 0 &&  mAndroidMarkControl.getSideNumberMax() != 0) && mAndroidMarkControl.getPageIndex() == 0) {
				// 先頭ページでの終了ボタン
				mContext = this;
				View.OnClickListener yesListener = new View.OnClickListener() {
					public void onClick(View v) {
						mAndroidMarkControl.StopStopwatch();
						bNormalEnd = true;
						ScreenChange.doScreenChange(mContext, ScreenChange.SCNO_STUDY, mFromPage, true,	mCurrentUser, 0, mEntrance);
					}
				};
				View.OnClickListener noListener = new View.OnClickListener() {
					public void onClick(View v) {
						mAndroidMarkControl.StopStopwatch();
					}
				};
				showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No14, 0, yesListener, 0, noListener);
			} else if (mAndroidMarkControl.getPageIndex() > 0
					&& mAndroidMarkControl.getPageIndex() <= mAndroidMarkControl.getMaxPageIndex()
					&& (mAndroidMarkControl.getSideIndex() == 0 &&  mAndroidMarkControl.getSideNumberMax() != 0)){
				// 先頭ページ以外、且つＡ面での終了ボタン
				mContext = this;
				View.OnClickListener yesListener = new View.OnClickListener() {
					public void onClick(View v) {
						// 1枚前までの学習内容を保存
						mAndroidMarkControl.StopStopwatch();
						if (mAndroidMarkControl.getMode() == MdtMode.Test) {
							mAndroidMarkControl.SaveCurrentPrintLearningData();
						}
						SavePrintSetData(mAndroidMarkControl.getPageIndex() - 1);
					}
				};
				View.OnClickListener noListener = new View.OnClickListener() {
					public void onClick(View v) {
						mAndroidMarkControl.StopStopwatch();
					}
				};
				// 20130702 MOD-S For 学習終了時A面の場合、警告メッセージ追加
				// KumonMessage.showKumonMsgBoxYesNo(this,
				// KumonMessage.MSG_No16, "YES", yesListner, "NO", noListner);
				showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No16, 0, yesListener, 0, noListener);
				// 20130702 MOD-E For 学習終了時A面の場合、警告メッセージ追加
			} else if (mAndroidMarkControl.getPageIndex() < mAndroidMarkControl.getMaxPageIndex()
													&& mAndroidMarkControl.getSideIndex() == mAndroidMarkControl.getSideNumberMax()) {
				// 最終ページ以外、且つB面での終了ボタン
				mContext = this;
				View.OnClickListener yesListener = new View.OnClickListener() {
					public void onClick(View v) {
						// ここまでの学習内容を保存
						mAndroidMarkControl.StopStopwatch();
						if (mAndroidMarkControl.getMode() == MdtMode.Test) {
							mAndroidMarkControl.SaveCurrentPrintLearningData();
						}
						SavePrintSetData(mAndroidMarkControl.getPageIndex());
					}
				};
				View.OnClickListener noListener = new View.OnClickListener() {
					public void onClick(View v) {
						mAndroidMarkControl.StopStopwatch();
					}
				};
				showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No18, 0, yesListener, 0, noListener);
			} else {
				// 最終ページ、且つB面での終了ボタン
				mContext = this;
				View.OnClickListener yesListener = new View.OnClickListener() {
					public void onClick(View v) {
						// ここまでの学習内容を保存
						mAndroidMarkControl.StopStopwatch();
						if (mAndroidMarkControl.getMode() == MdtMode.Test) {
							mAndroidMarkControl.SaveCurrentPrintLearningData();
						}
						SavePrintSetData(mAndroidMarkControl.getPageIndex());
					}
				};
				View.OnClickListener noListener = new View.OnClickListener() {
					public void onClick(View v) {
						mAndroidMarkControl.StopStopwatch();
					}
				};
				showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No20, 0, yesListener, 0, noListener);
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}



	public void onClickBack(View view) {
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
							DoOnClickBack();
							mClickable = true;
							setClickable(true);
						}
					});
				}
			}).start();
		}
		***/
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応
		Log.d(debugTag, "onClickBack");
		if (mClickable == true) {
			setDefaultPen();
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
			mBtnBack.setEnabled(false);
			mAndroidMarkControl.DoOnClickBack(mRelativeLayout, this);
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
					boolean pagechange = mAndroidMarkControl.DoOnClickBack(mRelativeLayout, getApplicationContext());
					SetButtonProperties();

					mClickable = true;
					setClickable(true);
					ProgressHide();
					//20150413 ADD-S For 2015年度Ver. 未読対応
					if (pagechange == true) {
						ShowUnreadMemo();
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
							DoOnClickNext();
							mClickable = true;
							setClickable(true);
						}
					});
				}
			}).start();
		}
		***/
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応

		if (mClickable == true) {
			Utility.DebugTimePass("onClickNext START");
			setDefaultPen();
			setClickable(false);
			mClickable = false;
			ProgressShow();
			Log.d(debugTag, "onClickNext");
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
		else {
			Utility.DebugTimePass("onClickNext PASS");
		}
		//20140618 MOD-E For 連打抑制
	}

	public void DoOnClickNext() {
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
					Utility.DebugTimePass("DoOnClickNext START");

					mBtnNext.setEnabled(false);
					boolean pagechange = mAndroidMarkControl.DoOnClickNext(mRelativeLayout, getApplicationContext());
					SetButtonProperties();

					Utility.DebugTimePass("DoOnClickNext END");
					mClickable = true;
					setClickable(true);
					ProgressHide();
					//20150413 ADD-S For 2015年度Ver. 未読対応
					if (pagechange == true) {
						ShowUnreadMemo();
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
					Utility.DebugTimePass("DoOnClickNext START");

					mBtnNext.setEnabled(false);
					
					boolean pagechange = mAndroidMarkControl.refreshPage(mRelativeLayout, getApplicationContext());
					SetButtonProperties();

					Utility.DebugTimePass("DoOnClickNext END");
					mClickable = true;
					setClickable(true);
					ProgressHide();
					//mAndroidMarkControl.setPenEnabled(true);
					
					//20150413 ADD-S For 2015年度Ver. 未読対応
					if (pagechange == true) {
						ShowUnreadMemo();
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

	public void onClickInBack(View view) {
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
							DoOnClickInBack();
							mClickable = true;
							setClickable(true);
						}
					});
				}
			}).start();
		}
		***/

		if (mClickable == true) {
			setDefaultPen();
			setClickable(false);
			mClickable = false;
			ProgressShow();
			Log.d(debugTag, "onClickInBack");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(progressTime) ;
					}
					catch(Exception e) {
					}
					DoOnClickInBack();
				}
			}).start();
		}
		//20140618 MOD-E For 連打抑制
	}
	public void DoOnClickInBack() {
		//20140618 MOD-S For 連打抑制
		/***
		try {
			mInBtnBack.setEnabled(false);
			mAndroidMarkControl.DoOnClickInBack();
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
					mInBtnBack.setEnabled(false);
					mAndroidMarkControl.DoOnClickInBack();
					SetButtonProperties();

					mClickable = true;
					setClickable(true);
					ProgressHide();
					return;
				}
			});
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		//20140618 MOD-E For 連打抑制
	}
	public void onClickInNext(View view) {
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
							DoOnClickInNext();
							mClickable = true;
							setClickable(true);
						}
					});
				}
			}).start();
		}
		***/
		if (mClickable == true) {
			setDefaultPen();
			setClickable(false);
			mClickable = false;
			ProgressShow();
			Log.d(debugTag, "onClickInNext");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(progressTime) ;
					}
					catch(Exception e) {
					}
					DoOnClickInNext();
				}
			}).start();
		}
		//20140618 MOD-E For 連打抑制
	}
	public void DoOnClickInNext() {
		//20140618 MOD-S For 連打抑制
		/***
		try {
			mInBtnNext.setEnabled(false);
			mAndroidMarkControl.DoOnClickInNext();
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
					mInBtnNext.setEnabled(false);
					mAndroidMarkControl.DoOnClickInNext();
					SetButtonProperties();

					mClickable = true;
					setClickable(true);
					ProgressHide();
					return;
				}
			});
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}

		//20140618 MOD-E For 連打抑制
	}
	public void onClickIndicator(View view) {
		//20140618 MOD-S For 連打抑制
		/***
		setClickable(false);
		if (mClickable == true) {
			mClickable = false;

			int id = view.getId();
			int pos = -1;
			for(int i = 0; i < mInBtnlist.size(); i++) {
				if(mInBtnlist.get(i).getId() == id) {
					pos = i;
					break;
				}
			}
			final int finalpos = pos;

			new Thread(new Runnable() {
				@Override
				public void run() {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							DoOnClickIndicator(finalpos);
							mClickable = true;
							setClickable(true);
						}
					});
				}
			}).start();
		}
		***/
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応

		if (mClickable == true) {
			setClickable(false);
			mClickable = false;
			ProgressShow();

			int id = view.getId();
			int pos = -1;
			for(int i = 0; i < mInBtnlist.size(); i++) {
				if(mInBtnlist.get(i).getId() == id) {
					pos = i;
					break;
				}
			}
			final int finalpos = pos;
			Log.d(debugTag, "onClickIndicator");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(progressTime) ;
					}
					catch(Exception e) {
					}
					DoOnClickIndicator(finalpos);
				}
			}).start();
		}
		//20140618 MOD-E For 連打抑制

	}
	public void DoOnClickIndicator(int pos) {
		//20140618 MOD-S For 連打抑制
		/***
		try {
			mInBtnNext.setEnabled(false);
			mAndroidMarkControl.DoOnClickIndicator(pos, mRelativeLayout, this);
			SetButtonProperties();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		***/
		final int finalpos = pos;
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// UI部品への操作;
					boolean pagechange = mAndroidMarkControl.DoOnClickIndicator(finalpos, mRelativeLayout, getApplicationContext());
					SetButtonProperties();

					mClickable = true;
					setClickable(true);
					ProgressHide();
					//20150413 ADD-S For 2015年度Ver. 未読対応
					if (pagechange == true) {
						ShowUnreadMemo();
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


	public void onClickRedPen(View view) {
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応

		if(mBRedPenMode == true ) {
			RedPenModeStop();
		}
		else {
			RedPenModeStart();
		}
	}
	//20140626 ADD-S For Undo
	public void onClickUndo(View view) {
		mAndroidMarkControl.Undo();
	}
	//20140626 ADD-E For Undo

	public void RedPenModeStart() {
		mBRedPenMode = true;
		mRedPen.setText("赤ペン");
		mAndroidMarkControl.SetDrawingInk(false);
		mAndroidMarkControl.setPenColor(CInkMain.SF_CommentColor);
		mAndroidMarkControl.setPenAlpha(CInkMain.SF_CommentAlpha);
	}
	public void RedPenModeStop() {
		mRedPen.setText("   ");
		mBRedPenMode = false;
		mAndroidMarkControl.SetDrawingInk(true);
		mAndroidMarkControl.setPenColor(CInkMain.SF_InkColor);
		mAndroidMarkControl.setPenAlpha(CInkMain.SF_InkAlpha);
	}

	// For Pen
	private void SetPenRadioButtonCheckState() {
//NEW_VER MOD-S
		/***
		if (mPenKind == CInkStationery.SF_PENKIND_NORMALERASER) {
			switch (mEraserWidth) {
			case Thin:
				mEraserS.setChecked(true);
				break;
			case Bold:
				mEraserL.setChecked(true);
				break;
			default:
				mEraserM.setChecked(true);
				break;
			}
		} else {
			switch (mPenWidth) {
			case F_PEN_THIN:
				mPenS.setChecked(true);
				break;
			case F_PEN_BOLD:
				mPenL.setChecked(true);
				break;
			default:
				mPenM.setChecked(true);
				break;
			}
		}
		***/

		if (mPenKind == CInkMain.SF_PENKIND_NORMALERASER) {

			if(mEraserWidth == KumonDataCtrl.SF_Eraser_Thin) {
				mEraserS.setChecked(true);
			}
			else if(mEraserWidth == KumonDataCtrl.SF_Eraser_Boid)
				mEraserL.setChecked(true);
			else {
				mEraserM.setChecked(true);
			}
		} else {
			switch (mPenWidth) {
			case KumonDataCtrl.SF_PEN_THIN:
				mPenS.setChecked(true);
				break;
			case KumonDataCtrl.SF_PEN_BOLD:
				mPenL.setChecked(true);
				break;
			default:
				mPenM.setChecked(true);
				break;
			}
		}
//NEW_VER MOD-E
	}

	private void SetQuestionControlsPenState() {
		mAndroidMarkControl.setPenKind(mPenKind);

		if (mPenKind == CInkMain.SF_PENKIND_BALLPEN) {
			mAndroidMarkControl.setPenWidth(mPenWidth);

			// 20130324 ADD-S For ペンの太さは前回使用の太さをキープ
			mCurrentUser.mPenWidth = mPenWidth;
			mCurrentUser.writeObject();
			// 20130324 ADD-E
		} else {
			mAndroidMarkControl.setEraserWidth(mEraserWidth);
		}
		
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

		if(mAndroidMarkControl.mBShowTopQuestionData) {
			//20150212 MOD-S For 参照中は先頭ページへ遷移
			mBtnFinish.setVisibility(View.VISIBLE);
			//20150212 MOD-E For 参照中は先頭ページへ遷移
			mBtnRest.setVisibility(View.INVISIBLE);
		}
		else {
			mBtnFinish.setVisibility(View.VISIBLE);
			//20140825 MOD-S For 診断・終了テストでは休憩ボタンは不要
			//mBtnRest.setVisibility(View.VISIBLE);
			if(mIsSpecalTest) {
				mBtnRest.setVisibility(View.INVISIBLE);
			}
			else {
				mBtnRest.setVisibility(View.VISIBLE);
			}
			//20140825 MOD-E For 診断・終了テストでは休憩ボタンは不要
		}
	}


	private boolean SavePrintSetData(int pos) {
		try {
			mContext = this;
			boolean ret = mAndroidMarkControl.SavePrintSetData(pos);
			if (ret) {
				bNormalEnd = true;
				//保存成功
				if(mAndroidMarkControl.mGradingMethod_Self) {
					ScreenChange.doScreenChange(mContext, ScreenChange.SCNO_STUDY, ScreenChange.SCNO_STUDYFINISH_MYSELF, true, mCurrentUser, 0, mEntrance);
				}
				else if(mAndroidMarkControl.mGradingMethod_InstructerOnClient) {
					ScreenChange.doScreenChange(mContext, ScreenChange.SCNO_STUDY, ScreenChange.SCNO_STUDYFINISH_ONCLIENT, true, mCurrentUser, 0, mEntrance);
				}
				else {
					ScreenChange.doScreenChange(mContext, ScreenChange.SCNO_STUDY, ScreenChange.SCNO_STUDYFINISH, true, mCurrentUser, 0, mEntrance);
				}

			} else {
				//保存失敗
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No90, 0, null);
			}
			System.gc();
			return ret;
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		System.gc();
		return false;
	}
	public void loadData() {
		boolean ret = mAndroidMarkControl.loadDataFromDB(mReStart, mIsSpecalTest);
		if (ret == false) {
			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDY, ScreenChange.SCNO_SPLASH, true, null, 0, mEntrance);
			return;
		}
    	//20140915 MOD-S For Bug
		//mAndroidMarkControl.InitializeQuestionControl(0, true, false, mRelativeLayout, this);
		if(mReStart == 1) {
			if(mReStart == 1 && mAndroidMarkControl.mBShowTopQuestionData == true) {
				mAndroidMarkControl.InitializeQuestionControlTopQuestion(mAndroidMarkControl.mPageIndex, mAndroidMarkControl.mSideIndex);
			}
			else {
				mAndroidMarkControl.InitializeQuestionControl(mAndroidMarkControl.mPageIndex, true, false, mRelativeLayout, this, mReStart);
			}
		}
		else {
			mAndroidMarkControl.InitializeQuestionControl(0, true, false, mRelativeLayout, this, mReStart);
		}
    	//20140915 MOD-E For Bug
		// ペンの選択状態
		SetPenRadioButtonCheckState();
		SetButtonProperties();
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

		m_TimerTask = new QuestionControlTimerTask();
		m_Timer = new Timer(true);
		m_Timer.schedule(m_TimerTask, 100);
	}

	private void setClickable(boolean mode) {
		mBtnBack.setClickable(mode);
		mBtnNext.setClickable(mode);
	}
	
	private void setDefaultPen() { 
		if(mEraserS.isChecked() || mEraserM.isChecked() || mEraserL.isChecked()) {
			if(mPenL.isShown()) {
				mPenL.setChecked(true);
			}else if(mPenM.isShown()) {
				mPenM.setChecked(true);
			}else if(mPenS.isShown()) {
				mPenS.setChecked(true);
			}
		}
	}
	private void DoLimitTime() {
		View.OnClickListener okListener = new View.OnClickListener() {
			public void onClick(View v) {
				mAndroidMarkControl.SaveCurrentPrintLearningData();
				SavePrintSetData(mAndroidMarkControl.getMaxPageIndex());
			}
		};
		showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No46, 0, okListener);
//		KumonMessage.showKumonMsgBoxOk(this, KumonMessage.MSG_No46, "OK", okListner);
	}
	//20140618 ADD-S For 連打抑制
//	private ProgressDialog progressDialog = null;
	private int progressTime = 300;

	private void ProgressShow() {
//		if(progressDialog != null) {
//			progressDialog.dismiss();
//			progressDialog = null;
//		}
//	    progressDialog = new ProgressDialog(this);
//	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	    progressDialog.setCancelable(false);
//	    progressDialog.setTitle("処理中");
//	    progressDialog.setMessage("お待ちください。");
//	    progressDialog.show();
		showProgress(R.layout.progress_msg_only, 0);

	}
	private void ProgressHide() {
//		if(progressDialog != null) {
//			progressDialog.dismiss();
//			progressDialog = null;
//		}
		closeProgress();
	}
	//20140618 ADD-E For 連打抑制

	//20140731 ADD-S For 録音対応
	//****************************************************************************************************
	//*** 音声録音用
	//****************************************************************************************************
	private boolean mOpenRecordPanel = false;
	private TableLayout mTableRecord = null;
	private TextView 	mTextViewRecord = null;

	private ImageView 	mImagebuttonStop = null;
	private ImageView 	mImagebuttonStart = null;
	private ImageView 	mImagebuttonPlay = null;
	//20150115 ADD-S Pouse
	private ImageView 	mImagebuttonPause = null;
	//20150115 ADD-E Pouse

	private int mRecordingMode = RecordCallback.SF_MODE_HIDE;
	private int mCurrentIconPos = -1;

	//20140805 ADD-S For 録音
	private ProgressBar mProgressBarRCProgress = null;
	private TextView 	mTextViewRCTime = null;
	private Timer 				m_RecTimer = null;
	private RecordingTimerTask 	m_RecTimerTask = null;
	private long 				m_RecTime = 0;
	//20140805 ADD-E For 録音


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
		else if(MODE == RecordCallback.SF_RECORD_COMPLETION) {
		}
	}
	private void OpenRecordPanel(int MODE, int pos) {
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
		mTextViewRecord.setVisibility(View.VISIBLE);

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

//NEW_VER MOD-S
		//mAndroidMarkControl.setPenThreadMode(true);
		mAndroidMarkControl.setPenEnabled(true);
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
		mTextViewRecord.setVisibility(View.GONE);
	}
	private void RecordBtnImage() {

		//20150115 MOD-S 再生時もダイアログ表示
		/***
		boolean canrecord = mAndroidMarkControl.REC_Get_CanRecordStatus(mCurrentIconPos);
		boolean canplay = mAndroidMarkControl.REC_Get_CanPlay(mCurrentIconPos);

		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING)  {
			//停止
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_no);
			if(canplay) {
				mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off);
				mImagebuttonPlay.setEnabled(true);
			}
			else {
				mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_no);
				mImagebuttonPlay.setEnabled(false);
			}
			if(canrecord) {
				mImagebuttonStart.setEnabled(true);
				mImagebuttonStart.setImageResource(R.drawable.record_off);
			}
			else {
				mImagebuttonStart.setEnabled(false);
				mImagebuttonStart.setImageResource(R.drawable.record_no);
			}
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_RECORDING)  {
			//録音中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setEnabled(false);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_no);

			if(canrecord) {
				mImagebuttonStart.setEnabled(true);
				mImagebuttonStart.setImageResource(R.drawable.record_on);
			}
			else {
				mImagebuttonStart.setEnabled(false);
				mImagebuttonStart.setImageResource(R.drawable.record_no);
			}
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PLAYING)  {
			//再生中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_on);
			if(canrecord) {
				mImagebuttonStart.setEnabled(false);
				mImagebuttonStart.setImageResource(R.drawable.record_no);
			}
			else {
				mImagebuttonStart.setEnabled(false);
				mImagebuttonStart.setImageResource(R.drawable.record_no);
			}
		}
		***/
		boolean canrecord = false;
		boolean canplay = false;
		if(Recordable == true) {
			canrecord = mAndroidMarkControl.REC_Get_CanRecordStatus(mCurrentIconPos);
			canplay = mAndroidMarkControl.REC_Get_CanPlay(mCurrentIconPos);
		}
		else {
			canrecord = false;
			canplay = true;
		}
		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING)  {
			//停止
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_no);
			if(canplay) {
				mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off);
				mImagebuttonPlay.setEnabled(true);
			}
			else {
				mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_no);
				mImagebuttonPlay.setEnabled(false);
			}
			if(canrecord) {
				mImagebuttonStart.setEnabled(true);
				mImagebuttonStart.setImageResource(R.drawable.record_off);
			}
			else {
				mImagebuttonStart.setEnabled(false);
				mImagebuttonStart.setImageResource(R.drawable.record_no);
			}
			//PuaseButton
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_no);
			mImagebuttonPause.setEnabled(false);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_RECORDING)  {
			//録音中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setEnabled(false);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_no);

			if(canrecord) {
				mImagebuttonStart.setEnabled(true);
				mImagebuttonStart.setImageResource(R.drawable.record_on);
			}
			else {
				mImagebuttonStart.setEnabled(false);
				mImagebuttonStart.setImageResource(R.drawable.record_no);
			}
			//PuaseButton
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_no);
			mImagebuttonPause.setEnabled(false);

		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PLAYING)  {
			//再生中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_on);
			mImagebuttonStart.setEnabled(false);
			mImagebuttonStart.setImageResource(R.drawable.record_no);
			//PuaseButton
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_off);
			mImagebuttonPause.setEnabled(true);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PAUSE)  {
			//Pause中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off);
			mImagebuttonStart.setEnabled(false);
			mImagebuttonStart.setImageResource(R.drawable.record_no);
			//PuaseButton
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_on);
			mImagebuttonPause.setEnabled(true);
		}
		//20150115 MOD-E 再生時もダイアログ表示

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

//		if (mProgressDialog != null) {
//			mProgressDialog = null;
//		}

		if(mRecordingMode != RecordCallback.SF_MODE_RECORDING) {
			mAndroidMarkControl.REC_Play_Stop();
			mRecordingMode = RecordCallback.SF_MODE_STOPPING;
			RecordBtnImage();
			setRecDuration(mCurrentIconPos);
			return;
		}

//		mProgressDialog = new ProgressDialog(this);
//		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		mProgressDialog.setIndeterminate(false);
//		mProgressDialog.setCancelable(false);
//		KumonMessage.showProgress(mProgressDialog, this, KumonMessage.MSG_No49);
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No49);
		Log.d(debugTag, "onClickRecordStop");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(REC_OFFSET_Time) ;
				}
				catch(Exception e) {
				}
				DoonClickRecordStop();
			}
		}).start();
	}
	public void DoonClickRecordStop() {
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if(mRecordingMode == RecordCallback.SF_MODE_RECORDING) {
						mAndroidMarkControl.REC_Recording_Stop();
					}
//					mProgressDialog.dismiss();
					closeProgress();
					mRecordingMode = RecordCallback.SF_MODE_STOPPING;
					RecordBtnImage();
					setRecDuration(mCurrentIconPos);
					return;
				}
			});

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
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
			Log.d(debugTag, "onClickRecordStart");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(100) ;
					}
					catch(Exception e) {
					}
					DoonClickRecordStart();
				}
			}).start();
		}
	}
	public void DoonClickRecordStart() {
		try {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mAndroidMarkControl.REC_Recording_Start(mCurrentIconPos);
					closeProgress();
//					mProgressDialog.dismiss();

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
			SimpleDateFormat format = new SimpleDateFormat("ss秒", Locale.JAPAN);
			Date date = new Date(rectime);
			String strpass = format.format(date);
			mTextViewRCTime.setText(strpass);
			mProgressBarRCProgress.setMax((int)rectime);

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
			//20140805 ADD-E For 録音
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
	private void setRCTime(boolean recording, long pass) {

		if(mRecordingMode == RecordCallback.SF_RECORD_PLAY) {
			int Progress = (int)(pass);
			mProgressBarRCProgress.setProgress(Progress);
		}
		else {
			if(recording) {
				//20140818 MOD-S 録音時間は30秒
				String strpass = String.valueOf(Math.round(pass / 1000)) + "/" + String.valueOf(Math.round(MaxRecordingTime / 1000)) ;
				mTextViewRCTime.setText(strpass);
				//20150115 MOD-S 録音時間は外部ファイル
				//mProgressBarRCProgress.setMax(30);
				mProgressBarRCProgress.setMax((int) MaxRecordingTime);
				//20150115 MOD-E 録音時間は外部ファイル
				mProgressBarRCProgress.setProgress((int)pass);
				//20140818 MOD-E 録音時間は30秒
			}
			else {
				String strpass = String.valueOf(Math.round(pass / 1000)) + "秒";
				mTextViewRCTime.setText(strpass);

				mProgressBarRCProgress.setMax(1000);
				mProgressBarRCProgress.setProgress(0);
			}
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
	private void checkRecTime(long rectime) {
		//20150115 MOD-S 録音時間は外部ファイル
		//if(rectime > 30000) {	//録音は30秒まで
		if(rectime > MaxRecordingTime) {	//録音は30秒まで
		//20150115 MOD-E 録音時間は外部ファイル
			if (m_RecTimer != null) {
				m_RecTimer.cancel();
				m_RecTimer = null;
			}
//			if (mProgressDialog != null) {
//				mProgressDialog = null;
//			}
//			mProgressDialog = new ProgressDialog(this);
//			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			mProgressDialog.setIndeterminate(false);
//			mProgressDialog.setCancelable(false);
//			KumonMessage.showProgress(mProgressDialog, this, KumonMessage.MSG_No50);
			showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No50);
			Log.d(debugTag, "checkRecTime");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(REC_OFFSET_Time) ;
					}
					catch(Exception e) {
					}
					DoonClickRecordStop();
				}
			}).start();
		}
	}
	//20140805 ADD-E For 録音

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
		//String soundcomment = TblSoundCommentData.DB_GetSoundCommentFileName(mCurrentUser.mStudentID, mCurrentUser.mCurrentKyozaiID, mAndroidMarkControl.getPrintUnitID(), 0);
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
        intent.putExtra(MemoActivity.SF_SHOWPAGEBUTTON, false);
        boolean bBack = false;
        boolean bNext = false;
        intent.putExtra(MemoActivity.SF_BACKENABLE, bBack);
        intent.putExtra(MemoActivity.SF_NEXTENABLE, bNext);
		//20150413 ADD-E For 2015年度Ver. 未読対応

		//20150413 MOD-S For 2015年度Ver. 未読対応
        //intent.setAction(Intent.ACTION_VIEW);
		//startActivity(intent);

		startActivityForResult(intent, MemoActivity.SF_RESULTCODE);
		//20150413 MOD-E For 2015年度Ver. 未読対応
	}
	//20140731 ADD-E For Memo
	//20150413 ADD-S For 2015年度Ver. 未読対応
	private void ShowUnreadMemo() {
		int unread = mAndroidMarkControl.getUnreadStatus();
		if(unread > 0) {
			DoOnClickMemo();
		}
	}
	//20150413 ADD-E For 2015年度Ver. 未読対応

	// *************************************************************
	class QuestionControlTimerTask extends TimerTask {

		public QuestionControlTimerTask() {
		}

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				public void run() {
					loadData();

					if(mIsSpecalTest) {
						//診断・最終テストでは制限時間あり
						if(mLimitTime > 0) {
							m_LimitTimerTask = new LimitTimerTask();
							m_LimitTimer = new Timer(true);
							m_LimitTimer.schedule(m_LimitTimerTask, mLimitTime * 60 * 1000);
						}
					}
					closeProgress();
//					mProgressDialog.dismiss();
					//20150413 ADD-S For 2015年度Ver. 未読対応
					ShowUnreadMemo();
					//20150413 ADD-E For 2015年度Ver. 未読対応
				}
			});
		}
	}
	// *************************************************************
	class LimitTimerTask extends TimerTask {

		public LimitTimerTask() {
		}

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				public void run() {
					if (m_Timer != null) {
						m_Timer.cancel();
						m_Timer = null;
					}
					if (m_LimitTimer != null) {
						m_LimitTimer.cancel();
						m_LimitTimer = null;
					}

					DoLimitTime();
				}
			});
		}
	}
	//20140805 ADD-S For 録音
	class RecordingTimerTask extends TimerTask {
		public RecordingTimerTask() {
		}

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				public void run() {
					setRCTime(true, m_RecTime);
					if(mRecordingMode == RecordCallback.SF_MODE_RECORDING) {
						checkRecTime(m_RecTime);
					}
					m_RecTime += 1000L;
				}
			});
		}
	}
	//20140805 ADD-E For 録音

}
