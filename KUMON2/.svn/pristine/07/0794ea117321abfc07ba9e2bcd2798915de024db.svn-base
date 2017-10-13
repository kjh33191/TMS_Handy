package kumon2014.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.markcontroltool.control.KesAudioPlayer;
import kumon2014.view.RecordCallback;
import pothos.markcontroltool.InkControlTool.CInkMain;

public class MemoLandActivity extends BaseActivity implements RecordCallback {
	public static final String 	SF_MEMOINK = "MemoInk";
	public static final String 	SF_MEMOTEXT = "MemoText";
    //20150210 ADD-S For 2015年度Ver. 音声メモ
	public static final String 	SF_MEMOSOUND = "MemoSound";
    //20150210 ADD-E For 2015年度Ver. 音声メモ


	private ImageView 			mImageViewTitle;
	private ImageView 			mImageViewInk;
	private ImageView 			mImageViewMemo;
	private String 				mMemoink;
	private String 				mMemotext;
    //20150210 ADD-S For 2015年度Ver. 音声メモ
//	private Handler 			mHandler = new Handler();
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
			setContentView(R.layout.activity_memoland);

			Intent intent = getIntent();
			mMemoink = intent.getStringExtra(MemoLandActivity.SF_MEMOINK);
			mMemotext = intent.getStringExtra(MemoLandActivity.SF_MEMOTEXT);
	        //20150210 ADD-S For 2015年度Ver. 音声メモ
			mMemoSoundFileName = intent.getStringExtra(MemoLandActivity.SF_MEMOSOUND);
	        //20150210 ADD-E For 2015年度Ver. 音声メモ

			mImageViewTitle = (ImageView) findViewById(R.id.imageviewTitle);
			mImageViewInk = (ImageView) findViewById(R.id.imageviewInk);
			mImageViewMemo = (ImageView) findViewById(R.id.imageviewMemo);

	        //20150210 ADD-S For 2015年度Ver. 音声メモ
			mImageviewSound = (ImageView) findViewById(R.id.imageviewSound);
			if(mMemoSoundFileName == null || mMemoSoundFileName.length() == 0) {
				mImageviewSound.setImageResource(R.drawable.icon_speaker_on_land);
				mImageviewSound.setEnabled(false);
			}
			else {
				mImageviewSound.setImageResource(R.drawable.icon_speaker_off_land);
				mImageviewSound.setEnabled(true);
			}

			mTextViewRecord = (TextView) findViewById(R.id.textview_record);
			mImagebuttonClose = (ImageView) findViewById(R.id.imagebutton_RecordClose);
			mImagebuttonPause = (ImageView) findViewById(R.id.imagebutton_RecordPause);
			mImagebuttonPlay = (ImageView) findViewById(R.id.imagebutton_RecordPlay);
			mImagebuttonRecord = (ImageView) findViewById(R.id.imagebutton_Record);
			mImagebuttonStop = (ImageView) findViewById(R.id.imagebutton_RecordStop);

			SetPalyerDispMode(false);
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

		if(hasFocus) {
		//Title
        Bitmap bmpTitle = Bitmap.createBitmap(mImageViewTitle.getHeight(), mImageViewTitle.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canTitle = new Canvas(bmpTitle);
        canTitle.drawColor(Color.parseColor("#FFE8F5FD"));
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(22);
		paint.setTypeface(Typeface.SERIF);
		paint.setColor(Color.BLACK);
        canTitle.drawText(getString(R.string.text_teacher_memo), 20, 30, paint);

		Matrix mtTitle = new Matrix();
		mtTitle.postRotate(270);
		mtTitle.postTranslate(0, mImageViewTitle.getHeight());
        Bitmap bmpRotateTitle = Bitmap.createBitmap(bmpTitle.getHeight(), bmpTitle.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canTitle2 = new Canvas(bmpRotateTitle);
        canTitle2.setMatrix(mtTitle);
        canTitle2.drawBitmap(bmpTitle, 0, 0, null);
		mImageViewTitle.setImageBitmap(bmpRotateTitle);


		// Ink
		CInkMain inkMain = new CInkMain();
		Bitmap bmpInk = inkMain.makeStrokeBmp(mMemoink, mImageViewInk.getHeight(), mImageViewInk.getWidth());
		Matrix mtInk = new Matrix();
		mtInk.postRotate(270);
		mtInk.postTranslate(0, bmpInk.getWidth());
        Bitmap bmpRotateInk = Bitmap.createBitmap(bmpInk.getHeight(), bmpInk.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canInk = new Canvas(bmpRotateInk);
        canInk.setMatrix(mtInk);
        canInk.drawBitmap(bmpInk, 0, 0, null);
        mImageViewInk.setImageBitmap(bmpRotateInk);


		// Memo
        Bitmap bmpMemo = Bitmap.createBitmap(mImageViewMemo.getHeight(), mImageViewMemo.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canMemo = new Canvas(bmpMemo);
        canMemo.drawColor(Color.parseColor("#FFDDDDDD"));
		Paint paintMemo = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintMemo.setTextSize(20);
//		paint.setTypeface(Typeface.SERIF);
		paintMemo.setColor(Color.BLACK);

		String[] lines = mMemotext.split("\n");
		for(int i = 0; i < lines.length; i++) {
			canMemo.drawText(lines[i], 30, 18*( i +1), paintMemo);
		}


		Matrix mtMemo = new Matrix();
		mtMemo.postRotate(270);
		mtMemo.postTranslate(0, mImageViewMemo.getWidth());
        Bitmap bmpRotateMemo = Bitmap.createBitmap(bmpMemo.getHeight(), bmpMemo.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canMemo2 = new Canvas(bmpRotateMemo);
        canMemo2.setMatrix(mtMemo);
        canMemo2.drawBitmap(bmpMemo, 0, 0, null);
        mImageViewMemo.setImageBitmap(bmpRotateMemo);
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
	protected void onDestroy() {
		super.onDestroy();
		mImageViewTitle.setImageBitmap(null);
		mImageViewInk.setImageBitmap(null);
		mImageViewMemo.setImageBitmap(null);

    	Utility.cleanupView(findViewById(R.id.memoland_topview));
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
	private TextView 			mTextViewRecord = null;

	private ImageView 			mImagebuttonStop = null;
	private ImageView 			mImagebuttonPlay = null;
	private ImageView 			mImagebuttonPause = null;

	private ImageView 			mImageviewSound = null;

	private int 				mRecordingMode = RecordCallback.SF_MODE_HIDE;

	private ImageView 			mImagebuttonRecord = null;
	private ImageView 			mImagebuttonClose = null;




	//Call Back
	@Override
	public void recordCallback(int MODE, int pos) {
		if(MODE == RecordCallback.SF_RECORD_STOP) {
			mRecordingMode = RecordCallback.SF_MODE_STOPPING;
			RecordBtnImage();
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
		mKesAudioPlayer.setRecordCallBack(MemoLandActivity.this);

		mOpenRecordPanel = true;
		mRecordingMode = RecordCallback.SF_MODE_STOPPING;
		SetPalyerDispMode(true);

		RecordBtnImage();
	}
	private void CloseRecordPanel() {
		if(mOpenRecordPanel == false) {
			return;
		}
		if(mRecordingMode == RecordCallback.SF_RECORD_PLAY) {
			mKesAudioPlayer.Play_Stop();
		}
		mRecordingMode = RecordCallback.SF_MODE_HIDE;

		mOpenRecordPanel = false;
		SetPalyerDispMode(false);
		mKesAudioPlayer = null;
	}
	private void RecordBtnImage() {
		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING)  {
			//停止
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_no);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off_land);
			mImagebuttonPlay.setEnabled(true);
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_no_land);
			mImagebuttonPause.setEnabled(false);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PLAYING)  {
			//再生中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_on_land);
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_off_land);
			mImagebuttonPause.setEnabled(true);
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PAUSE)  {
			//Pause中
			mImagebuttonStop.setImageResource(R.drawable.rp_stop_off);
			mImagebuttonPlay.setImageResource(R.drawable.rp_startx1_off_land);
			mImagebuttonPause.setImageResource(R.drawable.rp_pause_on_land);
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
		mKesAudioPlayer.Play_Stop();
		mRecordingMode = RecordCallback.SF_MODE_STOPPING;
		RecordBtnImage();
	}
	public void onClickRecordPlay(View view) {
		if(mRecordingMode == RecordCallback.SF_MODE_STOPPING) {
			mKesAudioPlayer.Play_Start();
			mRecordingMode= RecordCallback.SF_MODE_PLAYING;
			RecordBtnImage();
		}
		else if(mRecordingMode == RecordCallback.SF_MODE_PAUSE) {
			mKesAudioPlayer.Play_ReStart();
			mRecordingMode= RecordCallback.SF_MODE_PLAYING;
			RecordBtnImage();
		}

	}
	public void onClickRecordPause(View view) {
		if(mRecordingMode == RecordCallback.SF_MODE_PLAYING) {
			mKesAudioPlayer.Play_Pause();
			mRecordingMode= RecordCallback.SF_MODE_PAUSE;
			RecordBtnImage();
		}
	}
	private void SetPalyerDispMode(boolean mode) {

		if(mode) {
			mImagebuttonClose.setVisibility(View.VISIBLE);
			mImagebuttonPause.setVisibility(View.VISIBLE);
			mImagebuttonPlay.setVisibility(View.VISIBLE);
			mImagebuttonRecord.setVisibility(View.VISIBLE);
			mImagebuttonStop.setVisibility(View.VISIBLE);
		}
		else {
			mTextViewRecord.setVisibility(View.INVISIBLE);

			mImagebuttonClose.setVisibility(View.INVISIBLE);
			mImagebuttonPause.setVisibility(View.INVISIBLE);
			mImagebuttonPlay.setVisibility(View.INVISIBLE);
			mImagebuttonRecord.setVisibility(View.INVISIBLE);
			mImagebuttonStop.setVisibility(View.INVISIBLE);
		}
	}


}
