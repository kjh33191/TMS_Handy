package kumon2014.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
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
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.markcontroltool.AndroidMarkControl;
import kumon2014.message.KumonMessage;
import kumon2014.view.MarkControlSurface;
import kumon2014.view.RecordCallback;
import pothos.markcontroltool.MdtMode;
import pothos.markcontroltool.InkControlTool.CInkMain;
import pothos.view.PenPlayBackCallback;
import pothos.view.ReplayTask;

public class GradingActivity extends BaseActivity implements PenPlayBackCallback , RecordCallback  {
	//採点アイコンクリックイベント
	public View.OnClickListener mGradeIconListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	String work = v.getTag().toString();
        	String[] buff = work.split(":", 0);
        	try {
        		int idx = Integer.parseInt(buff[0]);
        		int pos = Integer.parseInt(buff[1]);
        		mAndroidMarkControl.GradeIconClick(idx, pos);
        	}
        	catch(Exception e) {

        	}
        }
    };


	private AndroidMarkControl mAndroidMarkControl = null;

	CurrentUser mCurrentUser = null;
	private int mReStart = 0;
	private int 	mEntrance;
	private int 	mLearningmode;

	private Handler mHandler = new Handler();
	private Timer m_Timer = null;
	private QuestionControlTimerTask m_TimerTask = null;
//	private ProgressDialog mProgressDialog;


	// 暫定
	private int mPenKind = CInkMain.SF_PENKIND_BALLPEN;
	private int mPenWidth = 3;
//NEW_VER MOD-S
//	private EraserSize mEraserWidth = EraserSize.Normal;
	private float mEraserWidth = KumonDataCtrl.SF_Eraser_Normal;
//NEW_VER MOD-E

//NEW_VER DEL-S
	/***
	private final int F_PEN_THIN = 2;
	private final int F_PEN_NORMAL = 3;
	private final int F_PEN_BOLD = 6;
	***/
//NEW_VER DEL-E

	private Context mContext;

	private TableRow 	mPageBar = null;

	private TableLayout mTablePenPlayBack = null;
//	private TextView 	mTextViewPenPlayBack = null;
	private boolean 	mPenPlayBack = false;
	private long		mPlayBackTime = 0;

	private ImageButton mImagebuttonPenPlayBack = null;
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

	private RadioButton mPenS = null;
	private RadioButton mPenM = null;
	private RadioButton mPenL = null;
	private RadioButton mEraserS = null;
	private RadioButton mEraserM = null;
	private RadioButton mEraserL = null;

	private ImageButton mBtnFinish = null;
	private ImageButton mBtnSkipBack = null;
	private ImageButton mBtnBack = null;
	private ImageButton mBtnNext = null;
	private ImageButton mBtnSkipNext = null;
	private boolean mClickable = true;

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

		try {
			System.gc();
			setContentView(R.layout.activity_grade);

			Intent intent = getIntent();
			mCurrentUser = (CurrentUser) intent.getSerializableExtra("CurrentUser");
//NEW_VER MOD-S
//			mPenWidth = F_PEN_NORMAL;
			mPenWidth =KumonDataCtrl.SF_PEN_NORMAL;
//NEW_VER MOD-S
			mLearningmode = intent.getIntExtra(ScreenChange.SF_LEARNINGMODE,	KumonDataCtrl.SF_DATATYPE_NONE);
			mEntrance = (int)intent.getIntExtra(ScreenChange.SF_ENTRANCE, ScreenChange.SF_NEXT);

			mAndroidMarkControl = new AndroidMarkControl(mCurrentUser, mLearningmode, 0);
			mAndroidMarkControl.setView((MarkControlSurface) findViewById(R.id.MarkControlSurfaceMain));

			mPageBar = (TableRow) findViewById(R.id.tablerow_pagebar);
			mAndroidMarkControl.setPageBar(mPageBar, null);

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

			mImagebuttonPenPlayBack = (ImageButton) findViewById(R.id.imagebutton_PenPlayBack);

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

			if(mLearningmode == KumonDataCtrl.SF_DATATYPE_GRADESELF) {
				//自己採点は、赤ペン不要
				mPenS.setVisibility(View.INVISIBLE);
				mPenM.setVisibility(View.INVISIBLE);
				mPenL.setVisibility(View.INVISIBLE);
				mEraserS.setVisibility(View.INVISIBLE);
				mEraserM.setVisibility(View.INVISIBLE);
				mEraserL.setVisibility(View.INVISIBLE);
				//再生不要
				mImagebuttonPenPlayBack.setVisibility(View.INVISIBLE);
			}
			//ポーズボタン不要
			ImageView btnRest = (ImageView) findViewById(R.id.imagebutton_Rest);
			btnRest.setVisibility(View.INVISIBLE);


			//スキップボタン不要
			mBtnSkipBack = (ImageButton) findViewById(R.id.imagebutton_skipback);
			mBtnSkipNext = (ImageButton) findViewById(R.id.imagebutton_skipnext);
			mBtnSkipBack.setVisibility(View.INVISIBLE);
			mBtnSkipNext.setVisibility(View.INVISIBLE);


			mBtnFinish = (ImageButton) findViewById(R.id.imagebutton_finish);
			mBtnBack = (ImageButton) findViewById(R.id.imagebutton_back);
			mBtnNext = (ImageButton) findViewById(R.id.imagebutton_next);

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

			mAndroidMarkControl.setRecordCallBack(GradingActivity.this, mCurrentUser.mLoginID);

			mProgressBarRCProgress = (ProgressBar) findViewById(R.id.progressBar_RCProgress);
			mTextViewRCTime = (TextView) findViewById(R.id.textview_RCTime);
			mTableRecord.setVisibility(View.GONE);
//			mTextViewRecord.setVisibility(View.GONE);
			//20140805 ADD-E For 録音

			InitializeQuestionControlTimer(false);

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
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
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

//		if (mProgressDialog != null) {
//			mProgressDialog = null;
//		}
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

		Utility.cleanupView(findViewById(R.id.grade_topview));
		System.gc();
	}

	@Override
	public void onLowMemory() {
		Utility.onLowMemory(getApplicationContext());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
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
		super.onActivityResult(requestCode, resultCode, intent);

	    if(requestCode == MemoActivity.SF_RESULTCODE) {
	    	int mode = intent.getIntExtra(MemoActivity.SF_RETURN, 0);
	    	if(mode == MemoActivity.SF_PAGEBACK) {
	    		//PageBack
	    	}
	    	else if(mode == MemoActivity.SF_PAGENEXT) {
	    		//PageNext
	    	}
	    	else {

	    	}
	    }
	    else {
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

	public void onClickFinish(View view) {
		ClosePlayBack();
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応
		try {
			// 最終ページ、且つB面での終了ボタン
			mContext = this;
			View.OnClickListener yesListener = new View.OnClickListener() {
				public void onClick(View v) {
					// ここまでの学習内容を保存
					mAndroidMarkControl.SaveCurrentPrintGradingData();
					SavePrintSetData();
				}
			};
			showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No40, 0, yesListener, 0, null);
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void onClickBack(View view) {
		ClosePlayBack();
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応
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
	}

	public void DoOnClickBack() {
		try {
			mBtnBack.setEnabled(false);
			mAndroidMarkControl.DoOnClickBackGrade(mRelativeLayout, this, mGradeIconListener);
			SetButtonProperties();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void onClickNext(View view) {
		ClosePlayBack();
		//20140731 ADD-S For 録音対応
		CloseRecordPanel();
		//20140731 ADD-E For 録音対応
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
	}

	public void DoOnClickNext() {

		try {
			mBtnNext.setEnabled(false);
			mAndroidMarkControl.DoOnClickNextGrade(mRelativeLayout, this, mGradeIconListener);
			SetButtonProperties();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}

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
		//終了ボタン
		if (mAndroidMarkControl.getGradeFinishStatus()) {
			mBtnFinish.setEnabled(true);
			mBtnFinish.setImageResource(R.drawable.bfinish);
		} else {
			mBtnFinish.setEnabled(false);
			mBtnFinish.setImageResource(R.drawable.bfinish_off);
		}

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


	}


	private boolean SavePrintSetData() {
		try {
			boolean ret = mAndroidMarkControl.SavePrintSetDataGrade();
			if (ret) {
				//保存成功
				if(mLearningmode == KumonDataCtrl.SF_DATATYPE_GRADESELF && mAndroidMarkControl.mGradingMethod_InstructerOnClient) {
					ScreenChange.doScreenChange(mContext, ScreenChange.SCNO_GRADE, ScreenChange.SCNO_STUDYFINISH_ONCLIENT, true, mCurrentUser, 0, mEntrance);
				}
				else {
					ScreenChange.doScreenChange(mContext, ScreenChange.SCNO_GRADE, ScreenChange.SCNO_STUDYFINISH, true, mCurrentUser, 0, mEntrance);
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
	public void loadData(boolean isback) {
		boolean ret = mAndroidMarkControl.loadDataFromDB(mReStart, false);
		if (ret == false) {
			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDY, ScreenChange.SCNO_SPLASH, true, null, 0, ScreenChange.SF_NEXT);
			return;
		}
		mAndroidMarkControl.InitializeQuestionControlGrade(0, true, MdtMode.SemiAutoMark, mRelativeLayout, this, mGradeIconListener);
		// ペンの選択状態
		SetPenRadioButtonCheckState();
		SetButtonProperties();
	}

	public void onClickDummy(View view) {
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

		mAndroidMarkControl.REPLAY_Start(GradingActivity.this);
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

	// ////////////////////////////////////////////////////////////////////////////////////
	private void InitializeQuestionControlTimer(boolean isback) {
//		if (mProgressDialog != null) {
//			mProgressDialog = null;
//		}
//		mProgressDialog = new ProgressDialog(this);
//		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		mProgressDialog.setIndeterminate(false);
//		mProgressDialog.setCancelable(false);
//		KumonMessage.showProgress(mProgressDialog, this, KumonMessage.MSG_No21);
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

		m_TimerTask = new QuestionControlTimerTask(isback);
		m_Timer = new Timer(true);
		m_Timer.schedule(m_TimerTask, 100);
	}



	private void setClickable(boolean mode) {
		mBtnSkipBack.setClickable(mode);
		mBtnBack.setClickable(mode);
		mBtnNext.setClickable(mode);
		mBtnSkipNext.setClickable(mode);
	}
	//20140731 ADD-S For Memo
	public void onClickMemo(View view) {
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
		if(MODE == RecordCallback.SF_RECORDICON_CLICK || MODE == RecordCallback.SF_SOUNDICON_CLICK) {
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

	// *************************************************************
	class QuestionControlTimerTask extends TimerTask {
		private boolean mIsTop;

		public QuestionControlTimerTask(boolean isTop) {
			mIsTop = isTop;
		}

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				public void run() {
					loadData(mIsTop);
//					mProgressDialog.dismiss();
					closeProgress();
				}
			});
		}
	}
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

}
