package kumon2014.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import kumon2014.activity.R;
import kumon2014.common.KumonCommon;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.database.master.MQuestionSound;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.markcontroltool.RotateImage;
import kumon2014.markcontroltool.control.IconCheckPictureBox;
import kumon2014.markcontroltool.control.IconRecord;
import kumon2014.markcontroltool.control.IconSound;
import kumon2014.markcontroltool.control.RecordDataControl;
import pothos.markcontroltool.MdtMode;
import pothos.markcontroltool.InkControlTool.CInkMain;
import pothos.markcontroltool.MarkStuct.MdtPageMarkData;
import pothos.markcontroltool.MarkStuct.MdtQuestionMarkData;
import pothos.markcontroltool.MarkStuct.MdtTestMarkData;
import pothos.mdtcommon.DataStructs.MdtData;
import pothos.mdtcommon.DataStructs.PageData;
import pothos.mdtcommon.DataStructs.RecogInfo;
import pothos.mdtcommon.DataStructs.RecordData;
import pothos.mdtcommon.DataStructs.SoundData;
import pothos.view.KumonInkToolSurface;

public class MarkControlSurface extends KumonInkToolSurface implements OnPreparedListener, OnCompletionListener, MediaRecorder.OnInfoListener{
	private static float SF_QUESTIONIMAGE_OFFSETX = 0.0F;
	private static float SF_QUESTIONIMAGE_OFFSETY = 40.0F;//40
	private static float SF_DRAWTEXTOFFSET = 10.0F;	//DrawTexの基準はButtomの為

	//
	private ArrayList<byte[]> 	mBackGroundImageList = new ArrayList<byte[]>();
	private ArrayList<MQuestionSound> 	mSoundList = new ArrayList<MQuestionSound>();



	// SurfaveViewのサイズ
    private final int 			F_DefaultViewWidth = 600;
    private final int 			F_DefaultViewHeight = 870;
    private int 				mViewWidth = -1;
    private int 				mViewHeight = -1;


    private float 				m_MarkScaleX = 1.0f;
    private float 				m_MarkScaleY = 1.0f;
    private float 				m_MarkScaleOffsetX = 1.0f;
    private float 				m_MarkScaleOffsetY = 1.0f;
    private float				mPenScalew = 1.0f;
    private float				mPenScaleh = 1.0f;
    private float				mPenOffsetX = 0.0f;
    private float				mPenOffsetY = 0.0f;


	//For MDT(MDT)
    private MdtData 			mMDT_MdtData = null;		//MdtData For 正解表示
    private MdtData 			mMDT_MdtDataBack = null;	//MdtData For 正解表示
	private MdtTestMarkData 	mMDT_TestMark = null;
    public boolean[] 			mMDT_ManualMarked = null;

    private boolean				mMDT_TestCommon = true;		//"普通テスト(true)か、終了テスト(false)
    private boolean 			mMDT_IsGraded = false;		// 採点されたかどうか
    private int					mMDT_FullScore = 0;
    private MdtMode 			mMDT_Mode = MdtMode.None; 	//回答、採点モード
    private int					mMDT_TrialCount = 0;		//回答回数
    private int 				mDT_PageSide = 0;			//A面、Ｂ面
    private boolean 			mMDT_PageRight = false;		//該当ページが正解かどうか
    private int					mMDT_LearningMode = KumonDataCtrl.SF_DATATYPE_NONE;
    private int					mGradingMethod = -1;




    //For Draw(DW)
    private	Bitmap 				mDW_BackGroundImage = null;			//背景画
    private	Bitmap 				mDW_ForeGroundImage = null;			//前景画
    private	Bitmap 				mDW_MarkUnCheckBmp = null;			//CheckMarkIcon
    private Bitmap 				mDW_MarkNoRectCheckBmp = null;		//CheckMarkIcon

    //20140731 ADD-S For 三角対応
    private Bitmap 				mDW_MarkNoRectTriangleBmp = null;		//CheckMarkIcon
    //20140731 ADD-E For 三角対応

    //成績フォント(満点)
    private final float 		F_ScoreSize_Big = 160.0F;
    private final Typeface 		F_ScoreTypeFace_Big = Typeface.SERIF; //Typeface.DEFAULT_BOLD;
    private final int			F_ScoreColor_Big = Color.argb(128, 255 , 0, 0);
    //成績フォント(満点以外)
    private final float 		F_ScoreSize_Small = 60.0F;
    private final Typeface 		F_ScoreTypeFace_Small = Typeface.DEFAULT_BOLD;
    private final int			F_ScoreColor_Small = Color.argb(128, 255 , 0, 0);
    private final int			F_ScoreSmallSpaceWidth = 50;
    //成績フォント(矢印)
    private final float 		F_ScoreSize_Blow = 30.0F;
    private final Typeface 		F_ScoreTypeFace_Blow = Typeface.DEFAULT_BOLD;
    private final int			F_ScoreColor_Blow = Color.argb(128, 255 , 0, 0);
    //チェックマークの〇
    private final float 		F_CheckMarkCircleWidth = 3.0F;
    private final int 			F_CheckMarkCircleColor = Color.RED;
    private final int 			F_CheckMarkCircleRadius = 14;
    private final float 		F_CheckMarkCircleOffset = 16.0F;


    //正解
    private final float 		F_RightAnswerSize = 18.0F;
    private final Typeface 		F_RightAnswerTypeface = Typeface.SERIF;
    private final int 			F_RightAnswerColor = Color.argb(128, 255 , 0, 0);

    //認識結果
    private final int 			F_AutoRecogColor_ScoreBelow = Color.argb(128, 255, 0, 0);
    private final int 			F_AutoRecogColor = Color.argb(128, 0 , 0x40, 0xFF);
    private final float 		F_AutoRecogSize = 14.0F;
    private final Typeface 		F_AutoRecogTypeface = Typeface.SERIF;

    //認識尤度
    private double m_fLikeliThreshold = 90.0d;
    private boolean m_bShowLikelifood = true;
    private boolean m_bWarnLikelifood = true;

    //採点アイコン
    private ArrayList<IconCheckPictureBox[]> mlstIconCheckPic = new ArrayList<IconCheckPictureBox[]>();
    //20140521 ADD-S For Sound
    //再生アイコン
    private MediaPlayer mPlayer = null;
    private ArrayList<IconSound> mlstIconSound = new ArrayList<IconSound>();
    private IconSound soundPlaying = null;
    //20140521 ADD-E For Sound

    //20140704 ADD-S For 点数表示時の矢印
    private int mLimitCnt = 3;
    //20140704 ADD-E


	//************************************************************************************
	public MarkControlSurface(Context context) {
		super(context);
		Utility.DebugTimePass("MarkControlSurface1");
		Init();
    }
	public MarkControlSurface(Context context, AttributeSet attrs) {
	    super(context, attrs);
		Utility.DebugTimePass("MarkControlSurface2");
		Init();
	}

	public MarkControlSurface(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
		Utility.DebugTimePass("MarkControlSurface3");
		Init();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Utility.DebugTimePass("surfaceChanged W=" +  width + " H=" + height );
		super.surfaceChanged(holder, format, width, height);

	    mViewWidth = width;
	    mViewHeight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Utility.DebugTimePass("surfaceCreated");
		super.surfaceCreated(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Utility.DebugTimePass("surfaceDestroyed");
		super.surfaceDestroyed(holder);
		ReleaseMediaPlayer();
		//20140731 ADD-S For 録音対応
		ReleaseMediaRecorder();
		//20140731 ADD-E For 録音対応
	}
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
	{
		try {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override // タッチされた位置を取得
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret = super.onTouchEvent(event);

		try {
		if(super.getPenEnabledX()) {
			if(event.getAction() == MotionEvent.ACTION_UP) {
				SetUndoButtonProperties(true);
			}
		}
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}

		return ret;
	}
	public void Invalidate() {
		try {
		super.InvalidateX();
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	//
	protected void Init()
	{
		try {
			super.Init();
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}

		setFocusable(true);

	    mViewWidth = F_DefaultViewWidth;
	    mViewHeight = F_DefaultViewHeight;

	    Resources res = this.getContext().getResources();
	    Bitmap orgBitmap = BitmapFactory.decodeResource(res, R.drawable.uncheck);
	    mDW_MarkUnCheckBmp =  KumonCommon.makeTransparentBitmap(orgBitmap, Color.argb(0xFF, 0xFF, 0xFF, 0xFF));

	    orgBitmap = BitmapFactory.decodeResource(res, R.drawable.norectcheck);
	    mDW_MarkNoRectCheckBmp =  KumonCommon.makeTransparentBitmap(orgBitmap, Color.argb(0xFF, 0xFF, 0xFF, 0xFF));

	    orgBitmap = BitmapFactory.decodeResource(res, R.drawable.norecttriangle);
	    mDW_MarkNoRectTriangleBmp =  KumonCommon.makeTransparentBitmap(orgBitmap, Color.argb(0xFF, 0xFF, 0xFF, 0xFF));

	}

	//Ready
	//問題データ
	public void setMdtData(MdtData data)
	{
		mMDT_MdtData = (MdtData)data.clone();
		mMDT_MdtDataBack = (MdtData)data.clone();
		mMDT_FullScore = data.FullScore;
		SetMdtSize();

		//座標を端末用に再計算したMdtDatを設定する
		super.setMdtDataX(mMDT_MdtData);
	}
	//問題画像の設定
	public void setMdtDataImage(byte[][] image)
	{
		mBackGroundImageList.clear();

		for (int i = 0; i < image.length; i++) {
			mBackGroundImageList.add(image[i]);
		}
	}
	@SuppressWarnings("unchecked")
	public void setMdtDataSound(ArrayList<MQuestionSound> soundlist)
	{
		mSoundList.clear();
		if(soundlist != null) {
			mSoundList = (ArrayList<MQuestionSound>)soundlist.clone();
		}
	}

	//インクデータ
	public void SetInkData(String inkjson)
	{
		try {
		super.SetInkDataX(inkjson);
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	//インクデータ
	//20150416 ADD-S InkData To Binary
	public void SetInkBinaryData(byte[] inkdata)
	{
		try {
		super.SetInkBinaryDataX(inkdata);
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	//20150416 ADD-E InkData To Binary
	//赤字コメント
	public void SetRedComment(String inkjson)
	{
		try {
		super.SetRedCommentX(inkjson);
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	//今描画しているのはインクかRedComenntか
	public void SetDrawingInk(Boolean drawingInk) {
		try {
		super.SetDrawingInkX(drawingInk);
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
    //20140704 ADD-S For 点数表示時の矢印
	public void setlimitCnt(int cnt) {
		mLimitCnt = cnt;
	}
    //20140704 ADD-E


    public void clearInkAll()
    {
    	try {
		super.clearInkAllX();
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
    }
    public String SaveInkToJson()
    {
    	try {
    		return super.SaveInkToJsonX();
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return null;
		}
    }
	//20150416 ADD-S InkData To Binary
	public byte[] SaveInkBinary() {
		try {
			return super.SaveInkBinaryX();
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return null;
		}
	}
	//20150416 ADD-E InkData To Binary

    public String SaveRedCommentToJson()
    {
    	try {
    		return super.SaveRedCommentToJsonX();
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return null;
		}
    }

	/// <summary>
	/// 問題データの読み込み
	/// </summary>
	/// <param name="questionData">問題データ</param>

	public void clearBackgroundImage() {
		mDW_BackGroundImage = null;
		mDW_ForeGroundImage = null;
		try {
		super.SetBackImageX(null, Color.WHITE);
		super.SetForeImageX(null, Color.WHITE);
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void setGradeIcon(int pageSide, RelativeLayout layout, Context context, OnClickListener listener) {
		//20140731 ADD-S For アイコン回転
		RotateImage.makeRotateImageGrade(mMDT_MdtData.LandscapeOrientation, this.getResources());
		//20140731 ADD-E For アイコン回転

		if(mlstIconCheckPic.size() > 0) {
            for (int i = 0; i < mlstIconCheckPic.size(); i++) {
            	IconCheckPictureBox[] arrayIconCheckPictureBox = mlstIconCheckPic.get(i);
                for (int j = 0; j < 2; j++) {
                	layout.removeView(arrayIconCheckPictureBox[j].ImageButton);
                	arrayIconCheckPictureBox[j].ImageButton = null;
                }
            }
		}
		mlstIconCheckPic.clear();
		if(mMDT_LearningMode == KumonDataCtrl.SF_DATATYPE_GRADESELF) {
    		if(mGradingMethod != KumonDataCtrl.SF_GradingMethod_Self) {
    			return;
    		}
		}

        PageData data = null;
        int page = mMDT_MdtData.PageDatas.size();
        for (int i = 0; i < page; i++)
        {
            if (pageSide == mMDT_MdtData.PageDatas.get(i).PageNumber)
            {
                data = mMDT_MdtData.PageDatas.get(i);
                break;
            }
        }
        if(data == null) {
            return;
        }


        //ページデータを取得
        MdtPageMarkData pMark = mMDT_TestMark.GetPageMarkData(pageSide);
        if (pMark == null) {
            return;
        }

        int len = data.QuestionDatas.size();

        //初期化
        for (int i = 0; i < len; i++)
        {
        	//イメージボタン1
	        ImageView button1 = new ImageView(context);
   			//20140731 MOD-S For アイコン回転
	        //button1.setImageResource(R.drawable.gd_wrong_white);
	        button1.setImageBitmap(RotateImage.mgd_wrong_white);
   			//20140731 MOD-E For アイコン回転

	        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	        PointF ptf = cvtScalePointCheckIcon(data.QuestionDatas.get(i).CheckIconLocation1, button1.getHeight());
	        lp1.setMargins((int)ptf.x, (int)ptf.y, 0, 0);
	        button1.setLayoutParams(lp1);
	        button1.setTag(Integer.toString(i) + ":0");
	        button1.setOnClickListener(listener);
	        layout.addView(button1);

	        //イメージボタンクラス1
            IconCheckPictureBox icon1 = new IconCheckPictureBox();
            icon1.MdtMarkNumber = IconCheckPictureBox.IconMarkNumber.SecondCheck;
            icon1.MdtPageNumber = data.QuestionDatas.get(i).PageNumber;
            icon1.MdtQuestionNumber = data.QuestionDatas.get(i).QuestionNumber;
            icon1.ImageButton = button1;
            icon1.ImageButton = button1;
            //20140811 ADD-S
            icon1.mLearningMode = mMDT_LearningMode ;
            //20140811 ADD-S
            icon1.SetVisible(false);


        	//イメージボタン2
	        ImageView button2 = new ImageView(context);
   			//20140731 MOD-S For アイコン回転
	        //button1.setImageResource(R.drawable.gd_wrong_white);
	        button1.setImageBitmap(RotateImage.mgd_wrong_white);
   			//20140731 MOD-E For アイコン回転

	        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	        ptf = cvtScalePointCheckIcon(new Point(data.QuestionDatas.get(i).CheckIconLocation1.x + 34, data.QuestionDatas.get(i).CheckIconLocation1.y), button1.getHeight());
	        lp2.setMargins((int)ptf.x, (int)ptf.y, 0, 0);
	        button2.setLayoutParams(lp2);
	        button2.setTag(Integer.toString(i) + ":1");
	        button2.setOnClickListener(listener);
	        layout.addView(button2);

	        //イメージボタンクラス2
            IconCheckPictureBox icon2 = new IconCheckPictureBox();
            icon2.MdtMarkNumber = IconCheckPictureBox.IconMarkNumber.FirstCheck;
            icon2.MdtPageNumber = data.QuestionDatas.get(i).PageNumber;
            icon2.MdtQuestionNumber = data.QuestionDatas.get(i).QuestionNumber;
            icon2.ImageButton = button2;
            //20140811 ADD-S
            icon2.mLearningMode = mMDT_LearningMode ;
            //20140811 ADD-S
            icon2.SetVisible(false);

            IconCheckPictureBox[] arrayIconCheckPictureBox = new IconCheckPictureBox[2];
            arrayIconCheckPictureBox[0] = icon1;
            arrayIconCheckPictureBox[1] = icon2;
            mlstIconCheckPic.add(arrayIconCheckPictureBox);
        }

        //このページの設問データを取得
        ArrayList<MdtQuestionMarkData> questionMarkDatas = pMark.QuestionMarks;
        len = questionMarkDatas.size();
        int amendCount;
        //20140731 MOD-S For 三角対応
        //boolean right;
        int firstright;
        int secondright;
        int lastright;
        //20140731 MOD-E For 三角対応

        //各問題を設定する
        for (int i = 0; i < len; i++)
        {
        	MdtQuestionMarkData mqmd = questionMarkDatas.get(i);
            //修正の回数
            amendCount = mqmd.GetAmendCount(mMDT_TrialCount, mMDT_Mode);
            //Testする時に　二回目から不正解だった問題にチェックを表示する
                //テスト以外, 現在の成績を取得して表示
                //手動の時　最後と手動と回目のデフォルトの値が違う
                //手動 0 全部right,1 default right (manualMarkは全部 right、 最後は0回目の通り),2 1目の通り;
                //fixCircleの時はmanualMarkの通り
            lastright = mqmd.GetRight(mMDT_TrialCount);
            //20140731 MOD-S For 三角対応
            firstright = mqmd.GetRight(0);
           	if(mMDT_TrialCount > 0) {
           		secondright = mqmd.GetRight(1);
           	}
           	else {
           		secondright = firstright;
           	}
            //20140731 MOD-E For 三角対応

            //20140731 MOD-S For 三角対応
            //if (amendCount == 0 && right)
            if (amendCount == 0 && lastright == MdtTestMarkData.SF_AnswerResultCheck_Right)
            //20140731 MOD-E For 三角対応
            {
            	if (mMDT_TrialCount > 0) {
            		continue;
                }
            }

            for (int j = 0; j < mlstIconCheckPic.size(); j++)
            {
                IconCheckPictureBox[] arrayIconCheckPictureBox = mlstIconCheckPic.get(j);
                if (arrayIconCheckPictureBox[0].MdtPageNumber == mqmd.MdtQuestionData.PageNumber && arrayIconCheckPictureBox[0].MdtQuestionNumber == mqmd.MdtQuestionData.QuestionNumber) {

                	for(int k = 0; k < 2; k++) {
                		arrayIconCheckPictureBox[k].MdtAmendCount = amendCount;
                		arrayIconCheckPictureBox[k].MdtTrialNumber = mMDT_TrialCount;
                		arrayIconCheckPictureBox[k].LastMdtRightX = lastright;
                        //20140731 ADD-S For 三角対応
                		arrayIconCheckPictureBox[k].FirstMdtRightX = firstright;
                		arrayIconCheckPictureBox[k].SecondMdtRightX = secondright;
                        //20140731 ADD-E For 三角対応
                		arrayIconCheckPictureBox[k].LastOrgMdtRightX = lastright;
                        //20140731 MOD-S For 三角対応
                        //boolean autoRight = mqmd.GetAutoRight(mMDT_TrialCount);
                		boolean autoRight = false;
                        int autoRightInt = mqmd.GetAutoRight(mMDT_TrialCount);
                        if(autoRightInt == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                        	autoRight = true;
                        }
                        //20140731 ADD-E For 三角対応
                        double lowScore = mqmd.GetLowScore(mMDT_TrialCount);
                        boolean isEmpty = mqmd.IsRectEmpty();
                        boolean isLow = (lowScore < m_fLikeliThreshold) ? true : false;
                        arrayIconCheckPictureBox[k].SemiAttention = (!autoRight) || isLow || isEmpty;
                		arrayIconCheckPictureBox[k].SetStatus();
                	}
                }
            }
        }
	}
    //20140521 ADD-S For Sound
	public void setSoundIcon( int pageSide, RelativeLayout layout, Context context) {

		//20140731 ADD-S For 録音対応
		setRecordIcon(pageSide, layout, context) ;
		//20140731 ADD-E For 録音対応

		if(mlstIconSound.size() > 0) {
            for (int i = 0; i < mlstIconSound.size(); i++) {
            	IconSound iconsound = mlstIconSound.get(i);
               	layout.removeView(iconsound.ImageButton);
               	iconsound.ImageButton = null;
            }
		}
		mlstIconSound.clear();

        PageData data = null;
        int page = mMDT_MdtData.PageDatas.size();
        for (int i = 0; i < page; i++)
        {
            if (pageSide == mMDT_MdtData.PageDatas.get(i).PageNumber)
            {
                data = mMDT_MdtData.PageDatas.get(i);
                break;
            }
        }
        if(data == null) {
            return;
        }

        int len = data.SoundDatas.size();

        //初期化
        for (int i = 0; i < len; i++)
        {
    		byte[] buff = null;
        	SoundData sound =  data.SoundDatas.get(i);
    		for(int j =0; j < mSoundList.size(); j++) {
    			MQuestionSound questionSound = mSoundList.get(j);
    			if(sound.PageNumber ==  questionSound.mPageNo && sound.SoundNumber ==  questionSound.mSoundNo) {
    				buff = questionSound.mSound;
    				break;
    			}
    		}
    		if(buff != null) {
    			//イメージボタン1
    			ImageView button1 = new ImageView(context);
       			//20140731 MOD-S For アイコン回転
    			//button1.setImageResource(R.drawable.icon_speaker_off);
    	        button1.setImageBitmap(RotateImage.micon_speaker_off);
       			//20140731 MOD-E For アイコン回転

    			RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    			PointF ptf = cvtScalePointCheckIcon(new Point(sound.SoundIconArea.left,  sound.SoundIconArea.top), button1.getHeight());
    			lp1.setMargins((int)ptf.x, (int)ptf.y, 0, 0);
    			button1.setLayoutParams(lp1);
    			button1.setTag(Integer.toString(i));
    			button1.setOnClickListener(mSoundIconListener);
    			layout.addView(button1);

    			//イメージボタンクラス1
    			IconSound icon1 = new IconSound();
    			icon1.mSoundData = sound;
    			icon1.ImageButton = button1;
    			icon1.mSound = buff;
    			mlstIconSound.add(icon1);
    		}
        }
	}
    //20140521 ADD-E For Sound

	public void SaveManualMark(int pageside) {
		mMDT_ManualMarked[pageside] = true;
    	mMDT_TestMark.mPageMarks.get(pageside).SaveManualMark(mMDT_TrialCount);
	}
	public boolean[] GetManualMark() {
		return mMDT_ManualMarked;
	}
	public void setManualMark(boolean[] value) {
		mMDT_ManualMarked = value;
	}

	public void GradeIconClick(int idx, int pos) {
		if(idx >= 0 && idx < mlstIconCheckPic.size()) {
            IconCheckPictureBox[] arrayIconCheckPictureBox = mlstIconCheckPic.get(idx);

            IconCheckPictureBox icon ;
            IconCheckPictureBox iconCoope ;
            if(pos == 0) {
            	icon = arrayIconCheckPictureBox[0];
            	iconCoope = arrayIconCheckPictureBox[1];
            }
            else {
            	icon = arrayIconCheckPictureBox[1];
            	iconCoope = arrayIconCheckPictureBox[0];
            }
            icon.OnClick(iconCoope);

            int page = icon.MdtPageNumber;
            int question = icon.MdtQuestionNumber;

            //20140731 MOD-S For 三角対応
            //boolean right;
            int rightX;
            //20140731 MOD-S For 三角対応
            if (mMDT_TrialCount != 1)
            {
            	rightX = icon.LastMdtRightX;
            }
            else
            {
                if (icon.MdtMarkNumber == IconCheckPictureBox.IconMarkNumber.FirstCheck)
                {
                	rightX = iconCoope.LastMdtRightX;
                }
                else
                {
                	rightX = icon.LastMdtRightX;
                }
            }
            // CE K.Iwakura 2011/2/21

            MdtQuestionMarkData qmark = mMDT_TestMark.GetQuestionMarkData(page, question);
            if (qmark != null)
            {
            	int rightint = 1;
                //20140731 MOD-S For 三角対応
            	/***
            	if(right) {
            		rightint = 2;
            	}
            	***/
            	rightint = rightX;
                //20140731 MOD-E For 三角対応
            	qmark.SetManualRight(mMDT_TrialCount, rightint);
            	qmark.SetMarked(true);
            }
            //20110218 TK AS
            for (int k = 0; k < mMDT_TestMark.getPageCount(); k++)
            {
            	if (mMDT_TestMark.mPageMarks.get(k).getMdtPageNumber() == page)
            	{
            		mMDT_ManualMarked[k] = true;
            	}
            }
            MakeForeImage(mDT_PageSide);
    		if(mDW_ForeGroundImage != null) {
    			try {
		    		super.SetForeImageX(mDW_ForeGroundImage, Color.WHITE);
	    		}
	    		catch (Exception e) {
	    			SLog.DB_AddException(e);
	    		}
    		}
        	Invalidate();
        }
	}
	public void setGradingMethod(int gradingMethod) {
		mGradingMethod = gradingMethod;
	}

	public void showPage(int pageSide, MdtMode mode, int learningMode, MdtTestMarkData testmarkdata, int trial, boolean isgreaded, boolean iscommontest) {

		//ペン受付停止
		setPenEnabled(false);

		mMDT_TestMark = testmarkdata;
	    mMDT_TestCommon = iscommontest;
	    mMDT_IsGraded = isgreaded;
	    mMDT_Mode = mode;
	    mMDT_LearningMode = learningMode;
	    mMDT_TrialCount = trial - 1;
	    try {
        super.setTrialNumberX(mMDT_TrialCount);
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
        boolean showredcomment = true;
    	if(mMDT_Mode == MdtMode.SemiAutoMark) {
    		if(mMDT_LearningMode == KumonDataCtrl.SF_DATATYPE_GRADESELF) {
        		if(mGradingMethod != KumonDataCtrl.SF_GradingMethod_Self) {
        			showredcomment = false;
        		}
    		}
    	}
    	else {
    		//未採点
    		if(mMDT_IsGraded == false) {
    			showredcomment = false;
    		}
    	}
    	try {
    	super.setShowRedCommentX(showredcomment);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}

    	boolean gradeinstructorinclient = false;
		if(mMDT_LearningMode == KumonDataCtrl.SF_GRADEINSTRUCTORONCLIENT) {
	    	gradeinstructorinclient = true;
		}
		try {
			super.GradeInstructorOnClientX(gradeinstructorinclient) ;
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}

	    movePage(pageSide);
	}
	public void movePage(int pageSide) {
		mDT_PageSide = pageSide;
		try {
		super.setPageNumberX(pageSide);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}

		//20140626 ADD-S For Undo
	 	InitUndoStatus();
	 	SetUndoButtonProperties(false);
		//20140626 ADD-E For Undo

		//ペン受付停止
	 	setPenEnabled(false);

		MakeBackImage(pageSide);
		MakeForeImage(pageSide);

		try {
			super.SetLandscapeOrientationX(mMDT_MdtData.LandscapeOrientation);
	
			RotateImage.makeRotateImageComm(mMDT_MdtData.LandscapeOrientation, this.getResources());
	
			super.SetBackImageX(mDW_BackGroundImage, Color.WHITE);
			super.SetForeImageX(mDW_ForeGroundImage, Color.WHITE);
		}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}


		if(mMDT_Mode == MdtMode.Test) {
	        if (mMDT_PageRight) {
	        	setPenEnabled(false);
	        }
	        else {
				setPenEnabled(true);
	        }
		}
		else if(mMDT_Mode == MdtMode.SemiAutoMark) {
    		if(mMDT_LearningMode == KumonDataCtrl.SF_GRADEINSTRUCTORONCLIENT) {
    			super.setPenColorX(CInkMain.SF_CommentColor);
    			super.setPenColorX(CInkMain.SF_CommentColor);
    			super.setPenAlphaX(CInkMain.SF_CommentAlpha);
    			super.SetDrawingInkX(false);
    			setPenEnabled(true);
    		}
    		else {
    			setPenEnabled(false);
    		}
		}
		else {
        	setPenEnabled(false);
		}

    	Invalidate();

	}

	//背景画
	private boolean MakeBackImage(int pageSide)
	{

		if(pageSide < 0 || pageSide >= mBackGroundImageList.size()) {
			//20140626 ADD-S
			mDW_BackGroundImage = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(mDW_BackGroundImage);
	        canvas.drawColor(Color.WHITE);

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setTextSize(32);
			paint.setTypeface(F_ScoreTypeFace_Big);
			paint.setColor(Color.BLACK);
			canvas.drawText("現在、このページは表示できません。", 20, 80, paint);
			canvas.drawText("先生にこの画面を見せてください。", 20, 120, paint);

			canvas.drawText("先生がいない場合は、", 20, 200, paint);
			canvas.drawText("とばして学習してください。", 20, 240, paint);
	        canvas = null;
	        paint = null;
			//20140626 ADD-E
			return false;
		}

		Bitmap image = null;
		byte[] buff = mBackGroundImageList.get(pageSide);
		Bitmap work = BitmapFactory.decodeByteArray(buff, 0, buff.length);

		/***
		if(mMDT_MdtData.LandscapeOrientation == true) {
			Bitmap temp = BitmapFactory.decodeByteArray(buff, 0, buff.length);
			Matrix mat = new Matrix();
			mat.postRotate(90, temp.getHeight() / 2, temp.getWidth() / 2);
			work = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), mat, true);
		}
		else {
		***/
		if(mMDT_MdtData.LandscapeOrientation == true) {
			//MOD-S 問題画像は左50ピクセルをカット
			image = Bitmap.createBitmap(work, (int)SF_QUESTIONIMAGE_OFFSETY,
													(int)SF_QUESTIONIMAGE_OFFSETX,
			    									(int)(work.getWidth() - SF_QUESTIONIMAGE_OFFSETY),
			    									(int)(work.getHeight() - SF_QUESTIONIMAGE_OFFSETX));
			//MOD-E 問題画像は上位50ピクセルをカット
			image.getWidth();
			image.getHeight();
			if(SF_QUESTIONIMAGE_OFFSETX > 0.0F || SF_QUESTIONIMAGE_OFFSETY > 0.0F) {
				work.recycle();
				work = null;
			}

			try {
				float scw = (float)(mViewHeight / (mMDT_MdtData.BackgroundImageSizeWidth - SF_QUESTIONIMAGE_OFFSETY));
				float sch = (float)(mViewWidth / (mMDT_MdtData.BackgroundImageSizeHeight - SF_QUESTIONIMAGE_OFFSETX));

				Matrix matrix = new Matrix();
				matrix.postScale(scw, sch, 0, 0);

				mDW_BackGroundImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
			}
			catch(Exception e) {
				image = null;
				mDW_BackGroundImage = null;
				return false;
			}
			image = null;
		}
		else {
			//MOD-S 問題画像は上位50ピクセルをカット
			image = Bitmap.createBitmap(work, (int)SF_QUESTIONIMAGE_OFFSETX,
													(int)SF_QUESTIONIMAGE_OFFSETY,
			    									(int)(work.getWidth() - SF_QUESTIONIMAGE_OFFSETX),
			    									(int)(work.getHeight() - SF_QUESTIONIMAGE_OFFSETY));
			//MOD-E 問題画像は上位50ピクセルをカット
			image.getWidth();
			image.getHeight();

			if(SF_QUESTIONIMAGE_OFFSETX > 0.0F || SF_QUESTIONIMAGE_OFFSETY > 0.0F) {
				work.recycle();
				work = null;
			}

			try {
				//20140514 MOD-S 横向き対応
				//float scw = (float)mViewSize.Width / (F_DSCViewSize.Width - SF_QUESTIONIMAGE_OFFSETX);
				//float sch = (float)mViewSize.Height / (F_DSCViewSize.Height - SF_QUESTIONIMAGE_OFFSETY);

				int width = 0;
				int height = 0;
				if(mMDT_MdtData.LandscapeOrientation) {
					width = mMDT_MdtData.BackgroundImageSizeHeight;
					height = mMDT_MdtData.BackgroundImageSizeWidth;
				}
				else {
					width = mMDT_MdtData.BackgroundImageSizeWidth;
					height = mMDT_MdtData.BackgroundImageSizeHeight;
				}

				float scw = mViewWidth / (width - SF_QUESTIONIMAGE_OFFSETX);
				float sch = mViewHeight / (height - SF_QUESTIONIMAGE_OFFSETY);
				//20140514 MOD-E 横向き対応

				Matrix matrix = new Matrix();
				matrix.postScale(scw, sch, 0, 0);

				mDW_BackGroundImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
			}
			catch(Exception e) {
				image = null;
				mDW_BackGroundImage = null;
				return false;
			}
			image = null;
		}

		return true;
	}
	private boolean MakeForeImage(int pageSide)
	{
        //20140514 ADD-S 横向き対応
		if(mMDT_MdtData.LandscapeOrientation == true) {
			mDW_ForeGroundImage = Bitmap.createBitmap(mViewHeight, mViewWidth, Bitmap.Config.ARGB_8888);
		}
		else {
	    	mDW_ForeGroundImage = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
		}
		Canvas canvas = new Canvas(mDW_ForeGroundImage);
		canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
		try {
   			DrawTopImage(canvas, pageSide);
   		}
   		catch(Exception e) {
   			mDW_ForeGroundImage = null;
   			return false;
   		}
		return true;
	}

    //前景描画
    private void DrawTopImage(Canvas canvas, int pageSide)
    {
    	if(mMDT_Mode == MdtMode.TopQuestion) {
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setTextSize(32);
			paint.setTypeface(F_ScoreTypeFace_Big);
			paint.setColor(Color.argb(128, 0, 255, 0));
			canvas.drawText("参考問題ページ", 20, 80, paint);
			return;
    	}
    	if(mMDT_Mode == MdtMode.SemiAutoMark) {
    		if(mMDT_LearningMode == KumonDataCtrl.SF_DATATYPE_GRADESELF) {
        		if(mGradingMethod != KumonDataCtrl.SF_GradingMethod_Self) {
        			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        			paint.setTextSize(32);
        			paint.setTypeface(F_ScoreTypeFace_Big);
        			paint.setColor(Color.argb(128, 255, 0, 0));
        			canvas.drawText("このページは指導者が採点します。", 20, 80, paint);
        			return;
        		}
    		}
    	}
    	else {
    		//未採点
    		if(mMDT_IsGraded == false) {
    		    //20150402 ADD-S For 未採点でも録音アイコンの指摘時間は表示
    			DrawRecordingTime(canvas, pageSide);
    		    //20150402 ADD-Ｅ For 未採点でも録音アイコンの指摘時間は表示
    			DrawWaitGraded(canvas);
    			return;
    		}

    	}

    	if(mMDT_Mode == MdtMode.DisplayMark) {
        	//テスト結果表示
    		if (mMDT_TestCommon) {
    			//点数
    			DrawDisplayResult(canvas, pageSide, mMDT_TrialCount);
    	        //チェックマーク描画
    	        DrawMarkAll(canvas, pageSide);
    	        //大きい円
    	        DrawCircle(canvas, pageSide);
    		}
    	}
    	else if(mMDT_Mode == MdtMode.Test) {
           	//テスト中
	        if (mMDT_TrialCount > 0)
	        {
	            if (mMDT_TestCommon)
	            {
        			//点数
	    			DrawDisplayResult(canvas, pageSide, mMDT_TrialCount - 1);
	    	        //チェックマーク描画
	    	        DrawMarkAll(canvas, pageSide);
	    	        //大きい円
	    	        DrawCircle(canvas, pageSide);
	            }
	        }
    	}
    	else if(mMDT_Mode == MdtMode.SemiAutoMark) {
    		//正解
    		DrawRightAnswer(canvas, pageSide);
    		//認識結果
    		DrawRecogString(canvas ,pageSide, mMDT_TrialCount);

    		boolean bDraw = true;
    		for (int i = 0; i < mMDT_TestMark.getPageCount(); i++)
    		{
    			if (mMDT_TestMark.mPageMarks.get(i).getMdtPageNumber() == pageSide)
    			{
    				if (mMDT_ManualMarked[i] == false)
    				{
    					if (mMDT_TrialCount == 0) //手動最初び○が書かない
    					{
    						//mMDT_TestMark.mPageMarks.get(i).DrawCircle = false; // ADD K.Iwakura 2011/2/21
    						bDraw = false;
    						break;
    					}
                    }
                }
            }
    		if(bDraw) {
    	        //大きい円
    	        DrawCircle(canvas, pageSide);
    		}

    	}
    	else if(mMDT_Mode == MdtMode.ManualMark) {
    		//正解
    		DrawRightAnswer(canvas, pageSide);
    		//認識結果
    		DrawRecogString(canvas ,pageSide, mMDT_TrialCount - 1);
	        //大きい円
	        DrawCircle(canvas, pageSide);
    	}

	    //20150302 ADD-S For 最適録音時間
		DrawRecordingTime(canvas, pageSide);
	    //20150302 ADD-E For 最適録音時間
    }

    //点数表示
    private void DrawDisplayResult(Canvas canvas, int pageSide, int trial)
    {
    	MdtPageMarkData pmark = mMDT_TestMark.GetPageMarkData(pageSide);
    	if (pmark == null) {
    		return;
    	}
    	//出力関連エリアの取得
    	Rect rect = GetRect();

    	int viewHeight = mViewHeight;
    	int viewWidth = mViewWidth;
        if(mMDT_MdtData.LandscapeOrientation == true) {
        	viewHeight = mViewWidth;
        	viewWidth = mViewHeight;
        }

    	//成績の取得
    	int allscore = mMDT_TestMark.GetScore(trial);

    	//テストの回目を取得する
    	ArrayList<Integer> trials = mMDT_TestMark.GetTestTrialsByResult();
    	if (pageSide == 0)
    	{
    		//満点以外は大きい点数を表示しない   大きい点数は不透明
    		float r = rect.width() / 2;

    		if (allscore == mMDT_FullScore)
    		{
    			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    			paint.setTextSize(F_ScoreSize_Big);
    			paint.setTypeface(F_ScoreTypeFace_Big);
    			paint.setColor(F_ScoreColor_Big);
				if(mMDT_MdtData.LandscapeOrientation == true) {
					canvas.drawText(Integer.toString(allscore), viewWidth / 2 - (int)(F_ScoreSize_Big * 0.8f), 150 , paint);
				}
				else {
					canvas.drawText(Integer.toString(allscore), viewWidth / 2 - (int)(F_ScoreSize_Big * 0.8f), viewHeight / 2 - r + (F_ScoreSize_Big * 0.3f) - 80 , paint);
				}
    		}
    		//各成績を描画する　小さい点数は透明
    		Paint paintstr = new Paint(Paint.ANTI_ALIAS_FLAG);
    		paintstr.setColor(F_ScoreColor_Small);
    		paintstr.setTextSize(F_ScoreSize_Small);
    		paintstr.setTypeface(F_ScoreTypeFace_Small);

    		Paint paintblow = new Paint(Paint.ANTI_ALIAS_FLAG);
    		paintblow.setColor(F_ScoreColor_Blow);
    		paintblow.setTextSize(F_ScoreSize_Blow);
    		paintblow.setTypeface(F_ScoreTypeFace_Blow);

    		//文字のXの位置
    		int xResult = (int)(viewWidth - (F_ScoreSize_Blow * 2) - F_ScoreSmallSpaceWidth);
    		int xDown = (int)(viewWidth - (F_ScoreSize_Blow * 1) - F_ScoreSmallSpaceWidth - 5);
    		int x = xResult;
    		int y = 100;

    		for (int i = 0; i < trials.size(); i++)
    		{
    			if (trials.get(i) > trial) {
    				break;
    			}
    			//20140528 ADD-S For Bug
    			if (trials.get(i) >= 6 && i != trials.size() - 1) {
    				continue;
    			}
    			//20140528 ADD-E For Bug
    			if (i == trial && (allscore == mMDT_FullScore)) {
    				continue;
    			}

    			int result = mMDT_TestMark.GetScore(trials.get(i));
    			if (result >= 70)
    			{
    				x = xResult;
    				if(mMDT_MdtData.LandscapeOrientation == true && trials.get(i) > 3) {
        				x -= 100;
    				}

    				String resstr = Integer.toString(result);
    				canvas.drawText(resstr, x, y, paintstr);
    			}
    			//20140528 DEL-S For Bug
    			//if (i == 2) {
    			//	continue;
    			//}
    			//20140528 DEL-E For Bug

				x = xDown;
				if(mMDT_MdtData.LandscapeOrientation == true && trials.get(i) > 3) {
    				x -= 100;
				}

    			y += F_ScoreSize_Blow;

    		    //20140704 MOD-S For 点数表示時の矢印（制限数に達した場合は、矢印を表示しない）
    			//canvas.drawText("↓", x, y, paintblow);
    			if(trials.get(i) + 1 < mLimitCnt && trials.get(i) < 6) {
    				canvas.drawText("↓", x, y, paintblow);
    			}
    		    //20140704 MOD-E For
    			y += F_ScoreSize_Small - 10;
				if(mMDT_MdtData.LandscapeOrientation == true && trials.get(i) == 3) {
    				y = 180;
				}
    		}
			//20140528 ADD-S For Bug
    	}
    }
    private void DrawCircle(Canvas canvas, int pageSide)
    {
        MdtPageMarkData pmark = mMDT_TestMark.GetPageMarkData(pageSide);
        if (pmark == null) {
            return ;
        }
        int clr_draw = Color.argb(128, 255, 0, 0);
        //m_uDisplayTrial指定回目のスコアを取得する
        int trial = mMDT_TrialCount;
        if (mMDT_Mode == MdtMode.Test)
        {
            trial--;
            if(trial == -1) {
                return ;
            }
        }

        //20140528 ADD-S For 未テストで問題設定が無いと丸になってします
        if(trial < 0) {
            return ;
        }
        //20140528 ADD-E For 未テストで問題設定が無いと丸になってします

        //
        int pageScore = pmark.GetPageScore((int)trial);
        boolean right = (pageScore == pmark.getMdtAllScore()) ? true : false;
        mMDT_PageRight = right;

        if (right)
        {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setColor(clr_draw);
            paint.setStrokeWidth(8);

            if(mMDT_MdtData.LandscapeOrientation == true) {
            	canvas.drawCircle(mViewHeight / 2, mViewWidth / 2, (mViewWidth / 2) - 10, paint);
            }
            else {
            	canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, (mViewWidth / 2) - 10, paint);
            }
        }
        return ;
    }

    private void DrawMarkAll(Canvas canvas, int pageSide)
    {
    	//Testの最初の回答、チェックがいらない
    	if (mMDT_Mode == MdtMode.Test && mMDT_TrialCount == 0) {
    		return;
    	}
    	//ページデータを取得
    	MdtPageMarkData pMark = mMDT_TestMark.GetPageMarkData(pageSide);
    	if (pMark == null) {
    		return;
    	}
    	//このページの設問データを取得
    	ArrayList<MdtQuestionMarkData> questionMarkDatas = pMark.QuestionMarks;
    	int len = questionMarkDatas.size();
    	int amendCount;
        //20140731 MOD-S For 三角対応
    	//boolean right;
        int firstright;
        int secondright;
        int lastright;
        //20140731 MOD-E For 三角対応

    	//各問題を設定する
    	for (int i = 0; i < len; i++)
    	{
    		MdtQuestionMarkData markdata = questionMarkDatas.get(i);
    		//修正の回数
    		amendCount = markdata.GetAmendCount(mMDT_TrialCount, mMDT_Mode);
    		//Testする時に　二回目から不正解だった問題にチェックを表示する
    		if (mMDT_Mode != MdtMode.Test)
    		{
    			//テスト以外, 現在の成績を取得して表示
    			//手動の時　最後と手動と回目のデフォルトの値が違う
    			//手動 0 全部right,1 default right (manualMarkは全部 right、 最後は0回目の通り),2 1目の通り;
    			//fixCircleの時はmanualMarkの通り
            	if (mMDT_Mode == MdtMode.ManualMark)
            	{
                   	if (mMDT_TrialCount == 0)
                   	{
                   	    //20140731 MOD-S For 三角対応
                   		//right = markdata.GetManualRight(mMDT_TrialCount);

                   		lastright = markdata.GetManualRight(mMDT_TrialCount);
                   		firstright = lastright;
                   		secondright = lastright;
                   	    //20140731 MOD-S For 三角対応
                   	}
                   	else
                   	{
                   	    //20140731 MOD-S For 三角対応
                   		//right = markdata.GetRight(mMDT_TrialCount);
                   		lastright = markdata.GetRight(mMDT_TrialCount);
                   		firstright = markdata.GetRight(0);
                   		if(mMDT_TrialCount > 1) {
                   			secondright = markdata.GetRight(1);
                   		}
                   		else {
                   			secondright = firstright;
                   		}
                   	    //20140731 MOD-E For 三角対応
                   	}
            	}
            	else
            	{
               	    //20140731 MOD-S For 三角対応
            		//right = markdata.GetRight(mMDT_TrialCount);
               		lastright = markdata.GetRight(mMDT_TrialCount);
               		firstright = markdata.GetRight(0);
               		if(mMDT_TrialCount > 1) {
               			secondright = markdata.GetRight(1);
               		}
               		else {
               			secondright = firstright;
               		}
               	    //20140731 MOD-S For 三角対応
            	}
    		}
    		else
    		{
    			if (mMDT_TrialCount > 0)
    			{
    				//テストの場合は前回の成績を取得して　表示
               	    //20140731 MOD-S For 三角対応
    				//right = questionMarkDatas.get(i).GetRight(mMDT_TrialCount - 1);
    				lastright = questionMarkDatas.get(i).GetRight(mMDT_TrialCount - 1);
    				firstright = questionMarkDatas.get(i).GetRight(0);
               		if(mMDT_TrialCount > 1) {
               			secondright = questionMarkDatas.get(i).GetRight(1);
               		}
               		else {
               			secondright = firstright;
               		}
               	    //20140731 MOD-E For 三角対応
    				amendCount = questionMarkDatas.get(i).GetAmendCount(mMDT_TrialCount - 1, mMDT_Mode);
    			}
    			else {
    				continue; //testmode 最初回答の場合は　全部表示しない。
    			}
    		}
            //20140731 MOD-S For 三角対応
    		//if (amendCount == 0 && right)
       		if (amendCount == 0 && lastright == MdtTestMarkData.SF_AnswerResultCheck_Right)
  	        //20140731 MOD-E For 三角対応
    		{
    			if (mMDT_Mode != MdtMode.ManualMark) {
    				continue;
    			}
    			else
    			{
    				if (mMDT_TrialCount > 0) {
    					continue;
    				}
    			}
    		}

            //20140731 MOD-S For 三角対応
    		DrawMark(canvas, markdata, amendCount, lastright, firstright, secondright);
            //20140731 MOD-E For 三角対応

    	}
    }
    //20140731 MOD-S For 三角対応
	//private void DrawMark(Canvas canvas, MdtQuestionMarkData markdata, int amendCount, boolean right)
	private void DrawMark(Canvas canvas, MdtQuestionMarkData markdata, int amendCount, int LastMdtRightX, int FirstMdtRightX, int SecondMdtRightX)

    //20140731 MOD-E For 三角対応
	{

		//20130409 ADD-S For markdata.MdtQuestionDataがnullの時がある
		if(markdata.MdtQuestionData == null) return;
		//20130409 ADD-E

		Point pt2 = markdata.MdtQuestionData.CheckIconLocation1;
		Point pt1 = new Point(pt2.x + 34, pt2.y);

		Bitmap mark1 = null;
		Bitmap mark2 = null;
		boolean addcircle1 = false;
		boolean addcircle2 = false;

        switch (mMDT_TrialCount)
        {
            case 0:
                //20140731 MOD-S For 三角対応
            	/***
            	if (right) {
            		mark1 = mDW_MarkUnCheckBmp;
            	}
            	else {
            		mark1 = mDW_MarkNoRectCheckBmp;
                }
            	mark2 = null;
            	***/
                switch (LastMdtRightX) {
                	case(MdtTestMarkData.SF_AnswerResultCheck_Right):
                		//正解
                		mark1 = mDW_MarkUnCheckBmp;
                		break;
                	case(MdtTestMarkData.SF_AnswerResultCheck_Wrong):
                		//不正解
                		mark1 = mDW_MarkNoRectCheckBmp;
                		break;
                	case(MdtTestMarkData.SF_AnswerResultCheck_Unright):
                		//三角
                		mark1 = mDW_MarkNoRectTriangleBmp;
                		break;
                }
            	mark2 = null;
                //20140731 MOD-E For 三角対応
                break;
            case 1:
                //20140731 MOD-S For 三角対応
            	/***
            	if (right) {
            		mark1 = mDW_MarkNoRectCheckBmp;
            		addcircle1 = true;
            		mark2 = mDW_MarkUnCheckBmp;
            	}
            	else {
            		mark1 = mDW_MarkNoRectCheckBmp;
                    if (amendCount <= 1)
                    {
                    	mark2 = mDW_MarkUnCheckBmp;
                    }
                    else {
                    	mark2 = mDW_MarkNoRectCheckBmp;
                    }
            	}
            	***/
                switch (LastMdtRightX) {
	            	case(MdtTestMarkData.SF_AnswerResultCheck_Right):
	            		//正解
	            		if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
	            			mark1 = mDW_MarkNoRectTriangleBmp;
	            		}
	            		else {
	            			mark1 = mDW_MarkNoRectCheckBmp;
	            		}
	            		addcircle1 = true;
	            		mark2 = mDW_MarkUnCheckBmp;
	            		break;
	            	case(MdtTestMarkData.SF_AnswerResultCheck_Wrong):
	            		//不正解
	            		if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
	            			mark1 = mDW_MarkNoRectTriangleBmp;
	            		}
	            		else {
	            			mark1 = mDW_MarkNoRectCheckBmp;
	            		}

	                    if (amendCount <= 1)
	                    {
	                    	mark2 = mDW_MarkUnCheckBmp;
	                    }
	                    else {
	            			mark2 = mDW_MarkNoRectCheckBmp;
	                    }
	            		break;
	            	case(MdtTestMarkData.SF_AnswerResultCheck_Unright):
	            		//三角
	            		if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
	            			mark1 = mDW_MarkNoRectTriangleBmp;
	            		}
	            		else {
	            			mark1 = mDW_MarkNoRectCheckBmp;
	            		}

	                    if (amendCount <= 1)
	                    {
	                    	mark2 = mDW_MarkUnCheckBmp;
	                    }
	                    else {
	            			mark2 = mDW_MarkNoRectTriangleBmp;
	                    }
	            		break;
	            }
                //20140731 MOD-E For 三角対応
                break;
            //20140528 MOD-S For 訂正回数３回以上対応
            //case 2:
            default:
            //20140528 MOD-E For 訂正回数３回以上対応

                //20140731 MOD-S For 三角対応
            	/***
            	if (right)
            	{
            		if (amendCount > 1)
            		{
            			mark1 = mDW_MarkNoRectCheckBmp;
            			mark2 = mDW_MarkNoRectCheckBmp;
                		addcircle2 = true;
            		}
            		else
            		{
            			mark1 = mDW_MarkNoRectCheckBmp;
                		addcircle1 = true;
            			mark2 = null;
                    }
            	}
            	else
            	{
            		mark1 = mDW_MarkNoRectCheckBmp;
            		mark2 = mDW_MarkNoRectCheckBmp;
            	}
            	***/
                switch (LastMdtRightX) {
	            	case(MdtTestMarkData.SF_AnswerResultCheck_Right):
	            		//正解
	            		if (amendCount > 1)
	            		{
		            		if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
		            			mark1 = mDW_MarkNoRectTriangleBmp;
		            		}
		            		else {
		            			mark1 = mDW_MarkNoRectCheckBmp;
		            		}
		            		if(SecondMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
		            			mark2 = mDW_MarkNoRectTriangleBmp;
		            		}
		            		else {
		            			mark2 = mDW_MarkNoRectCheckBmp;
		            		}
	                		addcircle2 = true;
	            		}
	            		else
	            		{
		            		if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
		            			mark1 = mDW_MarkNoRectTriangleBmp;
		            		}
		            		else {
		            			mark1 = mDW_MarkNoRectCheckBmp;
		            		}
	                		addcircle1 = true;
	            			mark2 = null;
	                    }
	            		break;
	            	case(MdtTestMarkData.SF_AnswerResultCheck_Wrong):
	            		//不正解
	            		if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
	            			mark1 = mDW_MarkNoRectTriangleBmp;
	            		}
	            		else {
	            			mark1 = mDW_MarkNoRectCheckBmp;
	            		}
            			mark2 = mDW_MarkNoRectCheckBmp;
	            		break;
	            	case(MdtTestMarkData.SF_AnswerResultCheck_Unright):
	            		//三角
	            		if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
	            			mark1 = mDW_MarkNoRectTriangleBmp;
	            		}
	            		else {
	            			mark1 = mDW_MarkNoRectCheckBmp;
	            		}
            			mark2 = mDW_MarkNoRectTriangleBmp;
	            		break;
	            }
                break;
                //20140731 MOD-E For 三角対応
        }

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(F_CheckMarkCircleColor);
        paint.setStrokeWidth(F_CheckMarkCircleWidth);

        PointF ptf1 = cvtScalePoint(pt1);
        PointF ptf2 = cvtScalePoint(pt2);
        if(mark1 != null) {
        	canvas.drawBitmap(mark1, ptf1.x, ptf1.y, null);
        }
        if(mark2 != null) {
        	canvas.drawBitmap(mark2, ptf2.x, ptf2.y, null);
        }
        if(addcircle1 == true) {
        	canvas.drawCircle(ptf1.x + F_CheckMarkCircleOffset , ptf1.y + F_CheckMarkCircleOffset , F_CheckMarkCircleRadius, paint);
        }
        if(addcircle2 == true) {
        	canvas.drawCircle(ptf2.x + F_CheckMarkCircleOffset , ptf2.y + F_CheckMarkCircleOffset , F_CheckMarkCircleRadius, paint);
        }
	}
	private void DrawWaitGraded(Canvas canvas)
	{
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(50);
		paint.setTypeface(F_ScoreTypeFace_Big);
		paint.setColor(Color.argb(128, 255, 0, 0));
		canvas.drawText("まだ採点されていません。", 20, 80, paint);
	}
    /// 正解を表示する
    private void DrawRightAnswer(Canvas canvas, int pageSide)
    {
        if (mMDT_MdtData == null) {
            return;
        }

        //CS TK 20110311　データを取得する時に、
        //採点データからパタンデータへ移動
        PageData pd = null;
        for (int i = 0; i < mMDT_MdtData.PageDatas.size(); i++)
        {
            if (mMDT_MdtData.PageDatas.get(i).PageNumber == pageSide)
            {
                pd = mMDT_MdtData.PageDatas.get(i);
                break;
            }
        }
        if (pd == null) {
            return;
        }

        //20140529 ADD-S 手書き正解
        if(pd.HandwritingRightAnswer != null) {
            float MarkScaleX = 1.0F;
            float MarkScaleY = 1.0F;
            float PenOffsetX = 1.0F;
            float PenOffsetY = 1.0F;

    		if(mMDT_MdtData.LandscapeOrientation == true) {
    	        PenOffsetX = (SF_QUESTIONIMAGE_OFFSETY *  (float)pd.HandwritingRightAnswer.TabletWidth / (float)mMDT_MdtData.BackgroundImageSizeWidth);
    	        PenOffsetY = (SF_QUESTIONIMAGE_OFFSETX * (float)pd.HandwritingRightAnswer.TabletHeight / (float)mMDT_MdtData.BackgroundImageSizeHeight);

    	        MarkScaleX = mViewHeight / ((float)pd.HandwritingRightAnswer.TabletWidth - PenOffsetX);
    	        MarkScaleY = mViewWidth / ((float)pd.HandwritingRightAnswer.TabletHeight - PenOffsetY);

    		}
    		else {
    	        PenOffsetX = (SF_QUESTIONIMAGE_OFFSETX *  (float)pd.HandwritingRightAnswer.TabletWidth / (float)mMDT_MdtData.BackgroundImageSizeWidth);
    	        PenOffsetY = (SF_QUESTIONIMAGE_OFFSETY * (float)pd.HandwritingRightAnswer.TabletHeight / (float)mMDT_MdtData.BackgroundImageSizeHeight);

    	        MarkScaleX = mViewWidth / ((float)pd.HandwritingRightAnswer.TabletWidth - PenOffsetX);
    	        MarkScaleY = mViewHeight / ((float)pd.HandwritingRightAnswer.TabletHeight - PenOffsetY);
    		}

    		try {
    		super.drawHandwritingRightAnswerX(canvas, pd.HandwritingRightAnswer, MarkScaleX, MarkScaleY, PenOffsetX, PenOffsetY);
        	}
    		catch (Exception e) {
    			SLog.DB_AddException(e);
    		}
        }
        //20140529 ADD-E 手書き正解



		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(F_RightAnswerSize);
		paint.setTypeface(F_RightAnswerTypeface);
		paint.setColor(F_RightAnswerColor);

        for (int i = 0; i < pd.QuestionDatas.size(); i++)
        {
        	try {
        	String str = super.getAnserCommentX(pd, pageSide, i);

            PointF ptf = cvtScalePoint(new Point(pd.QuestionDatas.get(i).CheckIconLocation2.x, pd.QuestionDatas.get(i).CheckIconLocation2.y));
    		canvas.drawText(str, ptf.x + 25, ptf.y + SF_DRAWTEXTOFFSET, paint);
        	}
    		catch (Exception e) {
    			SLog.DB_AddException(e);
    		}
        }
		paint = null;
    }
    /// <summary>
    /// 認識結果を表示する
    /// </summary>
    private void DrawRecogString(Canvas canvas , int pageSide, int trial)
    {
		Paint brush_Normal = new Paint(Paint.ANTI_ALIAS_FLAG);
		brush_Normal.setColor(F_AutoRecogColor);
		brush_Normal.setTextSize(F_AutoRecogSize);
		brush_Normal.setTypeface(F_AutoRecogTypeface);

		Paint brush_Score = new Paint(Paint.ANTI_ALIAS_FLAG);
        brush_Score.setColor(F_AutoRecogColor_ScoreBelow);
        brush_Score.setTextSize(F_AutoRecogSize);
        brush_Score.setTypeface(F_AutoRecogTypeface);

		Paint px = new Paint(Paint.ANTI_ALIAS_FLAG);
		px.setColor(Color.argb(255, 0 , 255, 0));
		px.setTextSize(F_AutoRecogSize);
		px.setTypeface(F_AutoRecogTypeface);

        if (mMDT_TestMark == null) {
            return;
        }
        //page
        int pageNum = mMDT_TestMark.getPageCount();
        for (int i = 0; i < pageNum; i++)
        {
            int page = mMDT_TestMark.getPageNumber(i);
            if (page != pageSide) {
                continue;
            }
            //question
            int questionMarksNum = mMDT_TestMark.getQuestionMarksCount(i);
            for (int j = 0; j < questionMarksNum; j++)
            {
                //area
                int answerRectMarkNum = mMDT_TestMark.getAnswerRectMarkCount(i, j);
                for (int k = 0; k < answerRectMarkNum; k++)
                {
                	try {
                	RecogInfo recogInfo = super.getRecogInfoX(mMDT_TestMark, mMDT_MdtData, i, j, k, trial);
                    if(recogInfo == null) {
                    	continue;
                	}
                    // CS K.Iwakura 2011/2/21
                    Paint brush;
                    if (m_bWarnLikelifood)
                    {
                        if (recogInfo.recogScore == 0 || recogInfo.recogScore > m_fLikeliThreshold)
                        {
                            brush = brush_Normal;
                        }
                        else
                        {
                            brush = brush_Score;
                        }
                    }
                    else
                    {
                        brush = brush_Normal;
                    }
                    if (m_bShowLikelifood && (recogInfo.recogString.length() > 0)) {
                    	recogInfo.recogString = recogInfo.recogString + String.format("(%d)", (int)recogInfo.recogScore);
                    }
                    Rect r = cvtPenScaleRect(recogInfo.answerAreaRect);
            		canvas.drawText(recogInfo.recogString, r.left, r.top + SF_DRAWTEXTOFFSET, brush);
                	}
            		catch (Exception e) {
            			SLog.DB_AddException(e);
            			continue;
            		}
                }
            }
        }
    }
    //20150302 ADD-S For 最適録音時間
	private void DrawRecordingTime(Canvas canvas , int pageSide)
	{
	    PageData data = null;
	    int page = mMDT_MdtData.PageDatas.size();
	    for (int i = 0; i < page; i++)
	    {
	        if (pageSide == mMDT_MdtData.PageDatas.get(i).PageNumber)
	        {
	            data = mMDT_MdtData.PageDatas.get(i);
	            break;
	        }
	    }
	    if(data == null) {
	        return;
	    }

	    int len = data.RecordDatas.size();
	    for (int i = 0; i < len; i++)
	    {
			RecordData record =  data.RecordDatas.get(i);
			if(record.ProperRangeMin > 0 || record.ProperRangeMax > 0) {
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setTextSize(10);
				paint.setTypeface(F_ScoreTypeFace_Big);
				paint.setColor(Color.argb(255, 0, 0, 0));

	    		String time;
	    		if(record.ProperRangeMax > 0) {
	    			time = String.format(Locale.JAPAN, "%d-%d", record.ProperRangeMin, record.ProperRangeMax);
	    		}
	    		else {
	    			time = String.format(Locale.JAPAN, "%d-", record.ProperRangeMin);
	    		}

	    		PointF ptf2 = cvtScalePoint(new Point(record.RecordIconArea.left,  record.RecordIconArea.top));
	    		if(mMDT_MdtData.LandscapeOrientation == true) {
		    		ptf2.x -= 4;
	    		}
	    		else {
		    		ptf2.x += 2;
	    		}
	    		ptf2.y += 42;
				canvas.drawText(time, ptf2.x, ptf2.y, paint);
			}
	    }
    }
    //20150302 ADD-E For 最適録音時間


    private void SetMdtSize()
    {
        if (mMDT_MdtData == null || mMDT_MdtDataBack == null) {
            return;
        }

        //20140514 MOD-S 横向き対応
        //float scaleAX = (float)((float)F_DSCViewSize.Width / (float)mMDT_MdtDataBack.ScreenSize.Width) ;
        //float scaleAY = (float)((float)F_DSCViewSize.Height / (float)mMDT_MdtDataBack.ScreenSize.Height);
        //
        //float scaleBX = (float)((float)mViewSize.Width / (float)(F_DSCViewSize.Width - SF_QUESTIONIMAGE_OFFSETX)) ;
        //float scaleBY = (float)((float)mViewSize.Height / (float)(F_DSCViewSize.Height - SF_QUESTIONIMAGE_OFFSETY));

        float scaleAX = 1.0F;
        float scaleAY = 1.0F;

        float scaleBX = 1.0F;
        float scaleBY = 1.0F;
		if(mMDT_MdtData.LandscapeOrientation == true) {
	        scaleAX = (float)((float)mMDT_MdtData.BackgroundImageSizeWidth / (float)mMDT_MdtData.ScreenSizeWidth) ;
	        scaleAY = (float)((float)mMDT_MdtData.BackgroundImageSizeHeight / (float)mMDT_MdtData.ScreenSizeHeight);

	        scaleBX = mViewHeight / (float)(mMDT_MdtData.BackgroundImageSizeWidth - SF_QUESTIONIMAGE_OFFSETY) ;
	        scaleBY = mViewWidth / (float)(mMDT_MdtData.BackgroundImageSizeHeight - SF_QUESTIONIMAGE_OFFSETX);

	        m_MarkScaleX = scaleAX * scaleBX;
		    m_MarkScaleY = scaleAY * scaleBY;

		    m_MarkScaleOffsetX = SF_QUESTIONIMAGE_OFFSETY / scaleAX;
			m_MarkScaleOffsetY = SF_QUESTIONIMAGE_OFFSETX / scaleAY;
		}
		else {
	        scaleAX = (float)((float)mMDT_MdtData.BackgroundImageSizeWidth / (float)mMDT_MdtData.ScreenSizeWidth) ;
	        scaleAY = (float)((float)mMDT_MdtData.BackgroundImageSizeHeight / (float)mMDT_MdtData.ScreenSizeHeight);

	        scaleBX = mViewWidth / (float)(mMDT_MdtData.BackgroundImageSizeWidth - SF_QUESTIONIMAGE_OFFSETX) ;
	        scaleBY = mViewHeight / (float)(mMDT_MdtData.BackgroundImageSizeHeight - SF_QUESTIONIMAGE_OFFSETY);

	        m_MarkScaleX = scaleAX * scaleBX;
		    m_MarkScaleY = scaleAY * scaleBY;

		    m_MarkScaleOffsetX = SF_QUESTIONIMAGE_OFFSETX / scaleAX;
			m_MarkScaleOffsetY = SF_QUESTIONIMAGE_OFFSETY / scaleAY;
		}
        //20140514 MOD-E 横向き対応


        //20140514 MOD-S 横向き対応
	    //mPenScalew = (float)mViewSize.Width/(F_DSCViewSize.Width - SF_QUESTIONIMAGE_OFFSETX);
		//mPenScaleh = (float)mViewSize.Height/(F_DSCViewSize.Height - SF_QUESTIONIMAGE_OFFSETY);

		if(mMDT_MdtData.LandscapeOrientation == true) {
		    mPenScalew = mViewHeight / (mMDT_MdtData.BackgroundImageSizeWidth - SF_QUESTIONIMAGE_OFFSETY);
			mPenScaleh = mViewWidth / (mMDT_MdtData.BackgroundImageSizeHeight - SF_QUESTIONIMAGE_OFFSETX);


			mPenOffsetX = SF_QUESTIONIMAGE_OFFSETY * mPenScalew;
			mPenOffsetY = SF_QUESTIONIMAGE_OFFSETX * mPenScaleh;
		}
		else {
		    mPenScalew = mViewWidth / (mMDT_MdtData.BackgroundImageSizeWidth - SF_QUESTIONIMAGE_OFFSETX);
			mPenScaleh = mViewHeight / (mMDT_MdtData.BackgroundImageSizeHeight - SF_QUESTIONIMAGE_OFFSETY);

			mPenOffsetX = SF_QUESTIONIMAGE_OFFSETX * mPenScalew;
			mPenOffsetY = SF_QUESTIONIMAGE_OFFSETY * mPenScaleh;
		}
        //20140514 MOD-E 横向き対応

		try {
			super.SetPenScaleX(mPenScalew, mPenScaleh, mPenOffsetX, mPenOffsetY);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}

	    Rect rect;
		int pagecnt = mMDT_MdtDataBack.getPageCnt();
		for (int i = 0; i < pagecnt; i++)
	    {
	    	//回答エリア数取得
		    int anserareacnt = mMDT_MdtDataBack.getAnserAreaCount(i);
	    	for (int j = 0; j < anserareacnt; j++)
	    	{
	    		rect = mMDT_MdtDataBack.getAnswerAreaRect(i, j);
	    		Rect newRect = new Rect((int)(rect.left * scaleAX),	(int)(rect.top * scaleAY), (int)(rect.right * scaleAX), (int)(rect.bottom * scaleAY));
	    		mMDT_MdtData.setAnswerAreaRect(i, j, newRect);
	    	}
        }
    }

    private Rect GetRect()
    {
        //出力エリアを確定する
        int x, y, r;
        if (mViewHeight >= mViewWidth)
        {
            if (mViewWidth > 50)
            {
                x = 25;
                r = (int) ((mViewWidth - 50) / 2);
                y = (int) (mViewHeight / 2 - r);
            }
            else
            {
                x = 1;
                r = (int) (mViewWidth / 2);
                y = (int) (mViewHeight / 2 - r);
            }
        }
        else
        {
            if (mViewHeight > 50)
            {
                x = (int) (mViewWidth / 2 - 25);
                r = (int) ((mViewHeight - 50) / 2);
                y = 25;
            }
            else
            {
                y = 1;
                r = (int) (mViewHeight / 2);
                x = (int) (mViewWidth / 2 - r);
            }
        }
        return new Rect(x, y, (2 * r) + x, (2 * r) + y);
    }

    public PointF cvtScalePoint(Point pt) {
    	PointF newpoint = new PointF();
    	newpoint.x = (pt.x - m_MarkScaleOffsetX) * m_MarkScaleX ;
    	newpoint.y = (pt.y - m_MarkScaleOffsetY) * m_MarkScaleY ;

    	return newpoint;
    }
    public PointF cvtScalePointCheckIcon(Point pt, int width) {

    	PointF newpoint = cvtScalePoint(pt);
    	if(mMDT_MdtData.LandscapeOrientation == true) {
    		float x = newpoint.y;
    		float y = mViewHeight - newpoint.x - width - m_MarkScaleOffsetX;
    		newpoint.x = x;
    		newpoint.y = y;
    	}

    	return newpoint;
    }
    public Rect cvtPenScaleRect(Rect rect) {
		int left = (int)(rect.left * mPenScalew - mPenOffsetX);
		int right = (int)(rect.right * mPenScalew - mPenOffsetX);
		int top = (int)(rect.top * mPenScaleh - mPenOffsetY);
		int bottom = (int)(rect.bottom * mPenScaleh - mPenOffsetY);

		/***
		mMDT_MdtData.PageDatas.get(i).AnswerAreas.get(j).AnswerArea =
				new Rect((int)(rect.left * scaleAX),
				(int)(rect.top * scaleAY),
				(int)(rect.right * scaleAX),
				(int)(rect.bottom * scaleAY));


		int left = rect.left;
		int right = rect.right;
		int top = rect.top;
		int bottom = rect.bottom;
    	***/

    	Rect newrect = new Rect(left, top, right, bottom);
    	return newrect;
    }


    //For Pen Width
    public void setPenKind(int value)
    {
    	try {
    	super.setPenKindX(value);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
    }
    public int getPenKind()
    {
    	try {
    	return super.getPenKindX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return -1;
		}
    }
    public void setPenWidth(int value)
    {
    	try {
    	super.setPenWidthX(value);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
    }
    public int getPenWidth()
    {
    	try {
    	return super.getPenWidthX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return -1;
		}
    }
    public float getEraserWidth()
    {
    	try {
    	return super.getEraserWidthX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return 0;
		}
    }
    public void setEraserWidth(float value)
    {
    	try {
    	super.setEraserWidthX(value);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
    }
	public void setPenColor(int color) {
		try {
		super.setPenColorX(color);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void setPenAlpha(int alpha) {
		try {
		super.setPenAlphaX(alpha);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void setPenEnabled(boolean mode) {
		try {
		super.setPenEnabledX(mode);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public boolean getPenEnabled() {
		try {
		return super.getPenEnabledX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return false;
		}
	}



	//****************************************************************************************************
	//*** インク再生用
	//****************************************************************************************************
	public void REPLAY_Start(final pothos.view.PenPlayBackCallback callback) {
		//OK
		try {
		super.REPLAY_StartX((pothos.view.PenPlayBackCallback) callback);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void REPLAY_End() {
		try {
		super.REPLAY_EndX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public long GetPlayBackTime() {
		try {
		return super.GetPlayBackTimeX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return 0;
		}
	}

	public void REPLAY_TouchEnd() {
		try {
		super.REPLAY_TouchEndX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void REPLAY_Clear() {
		try {
		super.REPLAY_ClearX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void REPLAY_Play(int mode) {
		try {
		super.REPLAY_PlayX(mode);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void REPLAY_Back() {
		try {
		super.REPLAY_BackX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void REPLAY_Next() {
		try {
		super.REPLAY_NextX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void REPLAY_Stop() {
		try {
		super.REPLAY_StopX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void REPLAY_Pause() {
		try {
		super.REPLAY_PauseX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public boolean REPLAY_BackBtnStatus() {
		try {
		return super.REPLAY_BackBtnStatusX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return false;
		}
	}
	public boolean REPLAY_NextBtnStatus() {
		try {
		return super.REPLAY_NextBtnStatusX();
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
			return false;
		}
	}
	public void OnPlaBackEvent(int mode, long passtime, int strokepos)
	{
		try {
		super.OnPlayBackEventX(mode, passtime, strokepos);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}


	//Sound再生イベント
	public View.OnClickListener mSoundIconListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	//20150115 MOD-S 再生時もダイアログ表示
        	/****
        	String work = v.getTag().toString();
        	try {
        		int pos = Integer.parseInt(work);
        		if( 0 <= pos && pos < mlstIconSound.size()) {
        			IconSound sound = mlstIconSound.get(pos);
        			if(sound.IsPalying) {
        				SoundStop(sound);
        			}
        			else {
        				SoundPlay(sound);
        			}
        		}
        	}
        	catch(Exception e) {
        	}
        	***/

        	String work = v.getTag().toString();
        	try {
        		int pos = Integer.parseInt(work);
        		if( 0 <= pos && pos < mlstIconSound.size()) {
//        			IconSound sound = mlstIconSound.get(pos);
       				if(mRecordCallback != null) {
       					mRecordCallback.recordCallback(RecordCallback.SF_SOUNDICON_CLICK, pos);
       				}
        		}
        	}
        	catch(Exception e) {
        	}
        	//20150115 MOD-E 再生時もダイアログ表示

        }
    };

	@Override
	public void onPrepared(MediaPlayer arg0) {
		mPlayer.start();

		if(soundPlaying != null) {
			try {
			super.StartSoundStrokeX(soundPlaying.mSoundData.QuestionNumber, soundPlaying.mSoundData.SoundNumber);
	    	}
			catch (Exception e) {
				SLog.DB_AddException(e);
			}
		}
	}
	@Override
	public void onCompletion(MediaPlayer arg0) {
		if(mRecordsoundPlaying != null) {
			REC_Play_Stop();
			if(mRecordCallback != null) {
				mRecordCallback.recordCallback(RecordCallback.SF_RECORD_STOP, 0);
			}
			mRecordsoundPlaying = null;
		}
		else {
			SoundStop(soundPlaying);
			mRecordCallback.recordCallback(RecordCallback.SF_RECORD_STOP, 0);
		}
	}

	private void SoundStop(IconSound sound) {
		if(mPlayer != null) {
			ReleaseMediaPlayer();
			if(sound != null) {
				try {
				super.EndSoundStrokeX(sound.mSoundData.QuestionNumber, sound.mSoundData.SoundNumber);
		    	}
				catch (Exception e) {
					SLog.DB_AddException(e);
				}
			}
		}
		sound.Stop();
		soundPlaying = null;
	}
	//20140626 ADD-S For Undo
	private ImageButton mBtnUndo = null;
	private int mMaxUndoCnt = 0;
	private int mUndoCnt = 0;
	private boolean mDoUndo = false;
	public void SetUndoButton(ImageButton btnundo, int maxundocnt)
	{
		mBtnUndo = btnundo;
		mMaxUndoCnt = maxundocnt;
		InitUndoStatus();
	}
	public void Undo()
	{
		if(mUndoCnt > 0) {
			try {
			super.UndoStrokeX();
	    	}
			catch (Exception e) {
				SLog.DB_AddException(e);
			}
	    	Invalidate();
			mUndoCnt--;
	    	SetUndoButtonProperties(false);
	    	mDoUndo = true;
		}
	}
	public void SetUndoButtonProperties(boolean addcnt)
	{
		if(mBtnUndo != null)  {
			try {
			boolean ret = super.getUndoStatusX();
			if(addcnt) {
				if(mDoUndo) {
					InitUndoStatus();
				}
				mUndoCnt++;
				if(mUndoCnt > mMaxUndoCnt) {
					mUndoCnt = mMaxUndoCnt;
				}
			}

			if(ret && mUndoCnt > 0) {
				mBtnUndo.setEnabled(true);
				mBtnUndo.setImageResource(R.drawable.btn_undo_on);
			}
			else {
				mBtnUndo.setEnabled(false);
				mBtnUndo.setImageResource(R.drawable.btn_undo_off);
				mUndoCnt = 0;
			}
	    	}
			catch (Exception e) {
				SLog.DB_AddException(e);
			}
		}
	}
	private void InitUndoStatus() {
		mUndoCnt = 0;
    	mDoUndo = false;
	}
	//20140626 ADD-E For Undo

	//20140731 ADD-S For 録音対応
	//****************************************************************************************************
	//*** 音声録音用
	//****************************************************************************************************

	private RecordCallback mRecordCallback = null;
	private MediaRecorder mRecorder = null;
    private ArrayList<IconRecord> mlstIconRecord = new ArrayList<IconRecord>();
    private int mRecordingMode = RecordCallback.SF_MODE_HIDE;
    private int mCurrentIconPos = -1;
    private String mLoginID = "";
    private File mRecordFolder = null;
    private IconRecord mRecordsoundPlaying = null;

	public void setRecordCallBack(final RecordCallback callback, String loginID) {
		mRecordCallback = callback;
		mLoginID = loginID;
	}
	public void setRecordFolder(File recordolder) {
		mRecordFolder = recordolder;
	}

	public void setRecordIcon( int pageSide, RelativeLayout layout, Context context) {
		ReleaseMediaRecorder();
		if(mlstIconRecord.size() > 0) {
            for (int i = 0; i < mlstIconRecord.size(); i++) {
            	IconRecord iconRecord = mlstIconRecord.get(i);
               	layout.removeView(iconRecord.ImageButton);
               	iconRecord.ImageButton = null;
            }
		}
		mlstIconRecord.clear();

        PageData data = null;
        int page = mMDT_MdtData.PageDatas.size();
        for (int i = 0; i < page; i++)
        {
            if (pageSide == mMDT_MdtData.PageDatas.get(i).PageNumber)
            {
                data = mMDT_MdtData.PageDatas.get(i);
                break;
            }
        }
        if(data == null) {
            return;
        }

        int len = data.RecordDatas.size();
        if(mRecordFolder != null && len > 0) {
           	RecordDataControl.RecordDataDecompressed(mRecordFolder);
        }

		boolean canRecord = (mMDT_Mode == MdtMode.Test && mMDT_PageRight == false);
        //初期化
        for (int i = 0; i < len; i++)
        {
    		RecordData record =  data.RecordDatas.get(i);
    		record.RecordFileName = RecordDataControl.makeRecordFileName(mRecordFolder, record, mLoginID);

    		//イメージボタン1
    		ImageView button1 = new ImageView(context);
    		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    		PointF ptf = cvtScalePointCheckIcon(new Point(record.RecordIconArea.left,  record.RecordIconArea.top), button1.getHeight());
    		lp1.setMargins((int)ptf.x, (int)ptf.y, 0, 0);
    		button1.setLayoutParams(lp1);
    		button1.setTag(Integer.toString(i));
    		button1.setOnClickListener(mRecordIconListener);
    		layout.addView(button1);

    		//イメージボタンクラス1
    		IconRecord icon1 = new IconRecord(canRecord);
    		icon1.mRecordData = record;
    		icon1.ImageButton = button1;
    		icon1.setImage();
    		mlstIconRecord.add(icon1);
        }
	}

	//録音アイコンクリックイベント
	public View.OnClickListener mRecordIconListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	String work = v.getTag().toString();
        	try {
        		int pos = Integer.parseInt(work);
        		if( 0 <= pos && pos < mlstIconRecord.size()) {
        			IconRecord record = mlstIconRecord.get(pos);
        			if(record.IsRecording) {
        			}
        			else {
        				if(mRecordCallback != null) {
        					if(record.mCanShowPlayer) {
        						mRecordCallback.recordCallback(RecordCallback.SF_RECORDICON_CLICK, pos);
        					}
        				}
        				else {
        					if(mRecordingMode == RecordCallback.SF_RECORD_PLAY ) {
				  				REC_Play_Stop();
				  			}
				  			else {
				  				REC_Play_Start(true, pos);
				  			}
        				}
        			}
        		}
        	}
        	catch(Exception e) {
        	}
        }
    };
    public void REC_Recording_Start(int pos) {
		if( 0 <= pos && pos < mlstIconRecord.size()) {


			mCurrentIconPos = pos;
			IconRecord record = mlstIconRecord.get(pos);
			record.RecordStart();
			//保存先
			//削除しないと、前回以上の録音ができない
			File recfile = new File(record.mRecordData.RecordFileName);
			if(recfile.exists()) {
				recfile.delete();
			}
			ReleaseMediaRecorder();
			mRecorder = new MediaRecorder();

			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//			recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
			mRecorder.setAudioSamplingRate(16000);//デフォルトの8000だと途中で録音が止まる！！
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//			recorder.setMaxDuration(40000);
//			recorder.setMaxFileSize(5000000L);

			mRecorder.setOnInfoListener(this);
			//保存先
			mRecorder.setOutputFile(record.mRecordData.RecordFileName);
	        //録音準備＆録音開始
	        try {
	        	mRecorder.prepare();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        mRecorder.start();   //録音開始
		}
    }

    public void REC_Recording_Stop() {
		if( 0 <= mCurrentIconPos && mCurrentIconPos < mlstIconRecord.size()) {
			IconRecord record = mlstIconRecord.get(mCurrentIconPos);
			record.RecordStop();
		}

		if(mRecorder != null) {
			mRecorder.stop();
			//20140818 DEl-S ここでResetすると、次回録音時に影響有り
			//resetせずに、onInfoの802を待つ
			/***
		    mRecorder.reset();
		    mRecorder.release();
		    mRecorder = null;
		    ***/
			//20140818 DEl-S ここでResetすると、次回録音時に影響有り
		}
    }
    public void REC_Play_Start(boolean recordable, int pos) {
    	soundPlaying = null;
    	if(recordable == true) {
			if( 0 <= pos && pos < mlstIconRecord.size()) {
				mCurrentIconPos = pos;
				IconRecord record = mlstIconRecord.get(pos);
				File soundfile = new File(record.mRecordData.RecordFileName);
				if(soundfile.exists()) {
					try {
						ReleaseMediaPlayer();
						mPlayer = new MediaPlayer();
						mRecordsoundPlaying = record;
						mPlayer.setDataSource(soundfile.getAbsolutePath());
						mPlayer.prepareAsync();
						mPlayer.setOnPreparedListener(this);
						mPlayer.setOnCompletionListener(this);
						//record.Play();
						mRecordingMode = RecordCallback.SF_RECORD_PLAY;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
    	}
    	else {
			if( 0 <= pos && pos < mlstIconSound.size()) {
				IconSound sound = mlstIconSound.get(pos);
				soundPlaying = sound;
				sound.Save();
				if(sound.mSoundData.SoundFilePath != "") {
					try {
						ReleaseMediaPlayer();
						mPlayer = new MediaPlayer();
						mPlayer.setDataSource(sound.mSoundData.SoundFilePath);
						mPlayer.prepareAsync();
						mPlayer.setOnPreparedListener(this);
						mPlayer.setOnCompletionListener(this);
						mRecordingMode = RecordCallback.SF_RECORD_PLAY;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
    	}
    }
    public void REC_Play_Stop() {
		mRecordingMode = RecordCallback.SF_RECORD_STOP;
		ReleaseMediaPlayer();
    }
    public void REC_Play_ReStart() {
		mRecordingMode = RecordCallback.SF_RECORD_PLAY;
		if(mPlayer != null) {
			mPlayer.start();
		}
    }
    public void REC_Play_Pause() {
		mRecordingMode = RecordCallback.SF_RECORD_PAUSE;
		if(mPlayer != null) {
			mPlayer.pause();
		}
    }

	//20150115 MOD-S 再生時もダイアログ表示
	public int REC_Get_Duration(boolean recordable, int pos) {
		int rectime = 0;
		if(recordable) {
			if( 0 <= pos && pos < mlstIconRecord.size()) {
				IconRecord record = mlstIconRecord.get(pos);
				File soundfile = new File(record.mRecordData.RecordFileName);
				if(soundfile.exists()) {
	    		    MediaPlayer Player = null;
	    		    try {
		    		    Player = new MediaPlayer();
		    		    Player.setDataSource(soundfile.getAbsolutePath());
		    		    Player.prepare();
						rectime = Player.getDuration();
					} catch (Exception e) {
						e.printStackTrace();
					}
	    	        if(Player != null) {
    	        		Player.reset();
		    	        Player.release();
		    	        Player = null;
	    	        }
				}
			}
		}
		else {
			//20150217 DEL-S 再生時間が正しく取得できない　ビットレートの関係か？
			/***
			if( 0 <= pos && pos < mlstIconSound.size()) {
				IconSound sound = mlstIconSound.get(pos);
				sound.Save();
				if(sound.mSoundData.SoundFilePath != "") {
					try {
						if(player == null) {
							player = new MediaPlayer();
						}
						else {
							if(player.isPlaying()) {
								player.stop();
								try {
									player.prepare();
								} catch (IOException e){}
							}
							player.reset();
						}
						player.setDataSource(sound.mSoundData.SoundFilePath);
						player.prepare();
						rectime = player.getDuration();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(player != null) {
					player.release();
					player = null;
				}
			}
			***/
			//20150217 DEL-E 再生時間が正しく取得できない　ビットレートの関係か？

		}

		//20150115 MOD-E 再生時もダイアログ表示

		return rectime;
	}
	public boolean REC_Get_CanRecordStatus(int pos) {
		boolean status = false;
		if( 0 <= pos && pos < mlstIconRecord.size()) {
			IconRecord record = mlstIconRecord.get(pos);
			status = record.mCanRecord;
		}
		return status;
	}
	public boolean REC_Get_CanPlay(int pos) {
		boolean status = false;
		if( 0 <= pos && pos < mlstIconRecord.size()) {
			IconRecord record = mlstIconRecord.get(pos);
			status = record.IsFileExist();
		}
		return status;
	}
	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		Log.d("onInfo","what = " +  what + " extra=" +  extra);
		if(what == 802) {//MEDIA_RECORDER_INFO_COMPLETION_STATUS) {//COMPLETION_STATUS
			mRecordCallback.recordCallback(RecordCallback.SF_RECORD_COMPLETION, 0);
			mRecorder.reset();
			mRecorder.release();
			mRecorder = null;
		}

	}
	//20140805 ADD-E For 録音

	//20150219 ADD-S
	private void ReleaseMediaPlayer()
	{
		if(mPlayer != null) {
			if(mPlayer.isPlaying()) {
				mPlayer.stop();
			}
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
	}
	private void ReleaseMediaRecorder()
	{
		if(mRecorder != null) {
			mRecorder.reset();
			mRecorder.release();
			mRecorder = null;
		}
	}
	//20150219 ADD-E

    public void ConnectMdtDataAndMarkData(MdtData mdtData, MdtTestMarkData testmark) {
    	try {
    	super.ConnectMdtDataAndMarkDataX(mdtData, testmark);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
    }
	@Override
	public void setVisibility(int visibility) {
		Utility.DebugTimePass(getClass().getSimpleName()+"#setVisibility, visibility=" + visibility);
		try {
		super.setVisibility(visibility);
    	}
		catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

}
