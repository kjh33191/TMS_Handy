package kumon2014.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.markcontroltool.control.KesAudioPlayer;
import kumon2014.view.RecordCallback;
import pothos.markcontroltool.InkControlTool.CInkMain;

public class MemoActivity extends BaseActivity  implements RecordCallback {
	public static final String 	SF_MEMOINK = "MemoInk";
	public static final String 	SF_MEMOTEXT = "MemoText";
    //20150210 ADD-S For 2015年度Ver. 音声メモ
	public static final String 	SF_MEMOSOUND = "MemoSound";
    //20150210 ADD-E For 2015年度Ver. 音声メモ
	//20150413 ADD-S For 2015年度Ver. 未読対応
	public static final String 	SF_SHOWPAGEBUTTON = "ShowPagebutton";
	public static final String 	SF_BACKENABLE = "BackEnable";
	public static final String 	SF_NEXTENABLE = "NextEnable";
	public static final String 	SF_RETURN = "Return";
	public static final int 	SF_RESULTCODE = 555;

	public static final int 	SF_PAGEEND = 0;
	public static final int 	SF_PAGEBACK = 1;
	public static final int 	SF_PAGENEXT = 2;

	//20150413 ADD-E For 2015年度Ver. 未読対応


	private ImageView 			mImageViewInk;
	private TextView 			mTextviewMemo;
	private String 				mMemoink;
	private String 				mMemotext;
    //20150210 ADD-S For 2015年度Ver. 音声メモ
	private Handler 			mHandler = new Handler();
	private String				mMemoSoundFileName = null;
    //20150210 ADD-E For 2015年度Ver. 音声メモ
	//20150413 ADD-S For 2015年度Ver. 未読対応
	private ImageView 			mImageViewPageBack;
	private ImageView 			mImageViewPageNext;
	//20150413 ADD-E For 2015年度Ver. 未読対応

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.activity_memo);

			Intent intent = getIntent();
			mMemoink = intent.getStringExtra(MemoActivity.SF_MEMOINK);
			mMemotext = intent.getStringExtra(MemoActivity.SF_MEMOTEXT);
	        //20150210 ADD-S For 2015年度Ver. 音声メモ
			mMemoSoundFileName = intent.getStringExtra(MemoActivity.SF_MEMOSOUND);
	        //20150210 ADD-E For 2015年度Ver. 音声メモ


			mImageViewInk = (ImageView) findViewById(R.id.imageviewink);
			mTextviewMemo = (TextView) findViewById(R.id.textviewMemo);

	        //20150210 ADD-S For 2015年度Ver. 音声メモ
			mImageviewSound = (ImageView) findViewById(R.id.imageviewSound);
			if(mMemoSoundFileName == null || mMemoSoundFileName.length() == 0) {
				mImageviewSound.setImageResource(R.drawable.icon_speaker_on);
				mImageviewSound.setEnabled(false);
			}
			else {
				mImageviewSound.setImageResource(R.drawable.icon_speaker_off);
				mImageviewSound.setEnabled(true);
			}

			mTableRecord = (TableLayout) findViewById(R.id.tableLayout_record);
			mTextViewRecord = (TextView) findViewById(R.id.textview_record);
			mTableRecord.setVisibility(View.INVISIBLE);
			mTextViewRecord.setVisibility(View.INVISIBLE);

			mImagebuttonStop = (ImageView) findViewById(R.id.imagebutton_RecordStop);
			mImagebuttonPlay = (ImageView) findViewById(R.id.imagebutton_RecordPlay);
			mImagebuttonPause = (ImageView) findViewById(R.id.imagebutton_RecordPause);
	        //20150210 ADD-E For 2015年度Ver. 音声メモ

			//20150413 ADD-S For 2015年度Ver. 未読対応
			mImageViewPageBack = (ImageView) findViewById(R.id.pageback);
			mImageViewPageNext = (ImageView) findViewById(R.id.pagenext);

			boolean bShow = intent.getBooleanExtra(MemoActivity.SF_SHOWPAGEBUTTON, false);
			if(bShow) {
				mImageViewPageBack.setVisibility(View.VISIBLE);
				mImageViewPageNext.setVisibility(View.VISIBLE);
				boolean bBack = intent.getBooleanExtra(MemoActivity.SF_BACKENABLE, false);
				boolean bNext = intent.getBooleanExtra(MemoActivity.SF_NEXTENABLE, false);
				if(bBack) {
					mImageViewPageBack.setEnabled(true);
					mImageViewPageBack.setImageResource(R.drawable.bback);
				}
				else {
					mImageViewPageBack.setEnabled(false);
					mImageViewPageBack.setImageResource(R.drawable.bback_off);
				}
				if(bNext) {
					mImageViewPageNext.setEnabled(true);
					mImageViewPageNext.setImageResource(R.drawable.bnext);
				}
				else {
					mImageViewPageNext.setEnabled(false);
					mImageViewPageNext.setImageResource(R.drawable.bnext_off);
				}

			}
			else {
				mImageViewPageBack.setVisibility(View.INVISIBLE);
				mImageViewPageNext.setVisibility(View.INVISIBLE);
			}
			//20150413 ADD-E For 2015年度Ver. 未読対応

		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}


	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

		// Viewのサイズを取得
		CInkMain inkMain = new CInkMain();
		Bitmap bmp = inkMain.makeStrokeBmp(mMemoink, mImageViewInk.getWidth(), mImageViewInk.getHeight());

		mImageViewInk.setImageBitmap(bmp);
		mTextviewMemo.setText(mMemotext);
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
	protected void onDestroy() {
		super.onDestroy();
		mImageViewInk.setImageBitmap(null);

		if (m_PlayingTimer != null) {
			m_PlayingTimer.cancel();
			m_PlayingTimer = null;
		}

    	Utility.cleanupView(findViewById(R.id.memo_topview));
    	System.gc();
	}

	@Override
	public void onLowMemory() {
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	////////////////////////////////////////////////////////////////////////
	public void onClickBack(View view) {
		try {
			//20150206 ADD-S For 2015Ver. 指導者音声メモ
			CloseRecordPanel();
			//20150206 ADD-E For 2015Ver. 指導者音声メモ
			//20150413 MOD-S For 2015年度Ver. 未読対応
			//this.finish();
			CloseActivity(MemoActivity.SF_PAGEEND);
			//20150413 MOD-E For 2015年度Ver. 未読対応
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
	//20150413 ADD-S For 2015年度Ver. 未読対応
	public void onClickPageBack(View view) {
		CloseRecordPanel();
		CloseActivity(MemoActivity.SF_PAGEBACK);
	}
	public void onClickPageNext(View view) {
		CloseRecordPanel();
		CloseActivity(MemoActivity.SF_PAGENEXT);
	}

	private void CloseActivity(int mode) {
		Intent data = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt(MemoActivity.SF_RETURN, mode);
		data.putExtras(bundle);
		setResult(RESULT_OK, data);
		finish();
	}
	//20150413 ADD-E For 2015年度Ver. 未読対応

	//20150206 ADD-S For 2015Ver. 指導者音声メモ
	//****************************************************************************************************
	//*** 音声録音用
	//****************************************************************************************************
	private KesAudioPlayer		mKesAudioPlayer = null;
	private boolean 			mOpenRecordPanel = false;
	private TableLayout 		mTableRecord = null;
	private TextView 			mTextViewRecord = null;

	private ImageView 			mImagebuttonStop = null;
	private ImageView 			mImagebuttonPlay = null;
	private ImageView 			mImagebuttonPause = null;

	private ImageView 			mImageviewSound = null;

	private int 				mRecordingMode = RecordCallback.SF_MODE_HIDE;

	private ProgressBar 		mProgressBarRCProgress = null;
	private TextView 			mTextViewRCTime = null;
	private Timer 				m_PlayingTimer = null;
	private PlayingTimerTask 	m_PlayingTimerTask = null;
	private long 				m_PlayingTime = 0;


	//Call Back
	@Override
	public void recordCallback(int MODE, int pos) {
		if(MODE == RecordCallback.SF_RECORD_STOP) {
			mRecordingMode = RecordCallback.SF_MODE_STOPPING;
			if (m_PlayingTimer != null) {
				m_PlayingTimer.cancel();
				m_PlayingTimerTask = null;
			}
			RecordBtnImage();
			mProgressBarRCProgress.setProgress(0);
		}
		else if(MODE == RecordCallback.SF_RECORD_COMPLETION) {
		}
	}
	private void OpenRecordPanel() {
		if(mOpenRecordPanel) {
			return;
		}
		if(mKesAudioPlayer != null) {
			mKesAudioPlayer = null;
		}
		mKesAudioPlayer = new KesAudioPlayer();
		mKesAudioPlayer.SetSound(mMemoSoundFileName);
		mKesAudioPlayer.setRecordCallBack(MemoActivity.this);

		mOpenRecordPanel = true;
		mRecordingMode = RecordCallback.SF_MODE_STOPPING;
		mTableRecord.setVisibility(View.VISIBLE);
		mTextViewRecord.setVisibility(View.VISIBLE);

		mProgressBarRCProgress = (ProgressBar) findViewById(R.id.progressBar_RCProgress);
		mTextViewRCTime = (TextView) findViewById(R.id.textview_RCTime);
		long rectime = mKesAudioPlayer.Get_Duration();
		rectime += 999;
		SimpleDateFormat format = new SimpleDateFormat("ss秒", Locale.JAPAN);
		Date date = new Date(rectime);
		String strpass = format.format(date);
		mTextViewRCTime.setText(strpass);
		RecordBtnImage();
	}
	private void CloseRecordPanel() {
		if(mOpenRecordPanel == false) {
			return;
		}
		if (m_PlayingTimer != null) {
			m_PlayingTimer.cancel();
			m_PlayingTimer = null;
		}
		if(mRecordingMode == RecordCallback.SF_RECORD_PLAY) {
			mKesAudioPlayer.Play_Stop();
		}
		mRecordingMode = RecordCallback.SF_MODE_HIDE;

		mOpenRecordPanel = false;
		mTableRecord.setVisibility(View.INVISIBLE);
		mTextViewRecord.setVisibility(View.INVISIBLE);
		mKesAudioPlayer = null;
	}
	private void RecordBtnImage() {
		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING)  {
			//停止
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_no);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off);
			mImagebuttonPlay.setEnabled(true);
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_no);
			mImagebuttonPause.setEnabled(false);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PLAYING)  {
			//再生中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_on);
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_off);
			mImagebuttonPause.setEnabled(true);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PAUSE)  {
			//Pause中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off);
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_on);
			mImagebuttonPause.setEnabled(true);
		}
	}

	public void onClickDummy(View view) {
	}
	public void onClickSoundMemo(View view) {
		OpenRecordPanel();
	}
	public void onClickRecordClose(View view) {
		CloseRecordPanel();
	}
	public void onClickRecordStop(View view) {
		if (m_PlayingTimer != null) {
			m_PlayingTimer.cancel();
			m_PlayingTimer = null;
		}
		mKesAudioPlayer.Play_Stop();
		mRecordingMode = RecordCallback.SF_MODE_STOPPING;
		RecordBtnImage();
		mProgressBarRCProgress.setProgress(0);
	}
	public void onClickRecordPlay(View view) {
		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING) {
			long rectime = mKesAudioPlayer.Get_Duration();
			SimpleDateFormat format = new SimpleDateFormat("ss秒", Locale.JAPAN);
			Date date = new Date(rectime + 999);
			String strpass = format.format(date);
			mTextViewRCTime.setText(strpass);
			mProgressBarRCProgress.setMax((int)rectime);

			m_PlayingTime = 0;
			m_PlayingTimerTask = new PlayingTimerTask();
			m_PlayingTimer = new Timer(true);
			m_PlayingTimer.schedule(m_PlayingTimerTask, 0, 1000);

			mKesAudioPlayer.Play_Start();
			mRecordingMode= RecordCallback.SF_MODE_PLAYING;
			RecordBtnImage();
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PAUSE) {
			m_PlayingTimerTask = new PlayingTimerTask();
			m_PlayingTimer = new Timer(true);
			m_PlayingTimer.schedule(m_PlayingTimerTask, 0, 1000);

			mKesAudioPlayer.Play_ReStart();
			mRecordingMode= RecordCallback.SF_MODE_PLAYING;
			RecordBtnImage();
		}

	}
	public void onClickRecordPause(View view) {
		if(mRecordingMode == RecordCallback.SF_MODE_PLAYING) {
			if (m_PlayingTimer != null) {
				m_PlayingTimer.cancel();
				m_PlayingTimer = null;
			}
			mKesAudioPlayer.Play_Pause();
			mRecordingMode= RecordCallback.SF_MODE_PAUSE;
			RecordBtnImage();
		}
	}
	private void setRCTime(boolean recording, long pass) {

		if(mRecordingMode == RecordCallback.SF_RECORD_PLAY) {
			int Progress = (int)(pass);
			mProgressBarRCProgress.setProgress(Progress);
		}
	}


	class PlayingTimerTask extends TimerTask {
		public PlayingTimerTask() {
		}

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				public void run() {
					setRCTime(true, m_PlayingTime);
					m_PlayingTime += 1000L;
				}
			});
		}
	}

}
