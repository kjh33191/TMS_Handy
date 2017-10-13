package kumon2014.kumondata;

import java.io.Serializable;
import java.util.Date;

import kumon2014.database.master.MQuestion2;

public class DResultData  implements Serializable , Cloneable{
	private static final long serialVersionUID = 1L;
	
	//Key
	public String						mStudentID = "";
	public String						mKyokaID = "";
	public String						mKyokaName = "";
	public int							mKyokaOrderNo = 0;
	public String						mKyozaiID = "";
	public String						mKyozaiName = "";
	public int							mKyozaiOrderNo = 0;
	public String						mPrintUnitID = "";
	public String						mPrintSetID = "";	//プリントセットID
	public String						mPrintID;			//テストID
	public int							mPrintNo;			//テストNo

	public int							mStatus;			//  0:未学習　1:学習待機中  2:ダウンロード済み  3:学習中  4:採点待機中  5:採点中 10:完了テストの学習回数。
	public int							mCount;				//テストの学習回数。
	public int							mGradingMethod;		//テストの採点方法	0:自動採点, 1:本人採点, 2:指導者採点
	public int							mGradingStatus;		//0:未学習,　1:学習済み  2:採点済み,
	public int							mLearningPlace;		//学習場所 0=自宅, 1=教室

	public String						mScheduledDate;		//学習予定日
	public int							mScheduledIndex;	//学習Idx
	public int							mScheduledNum;		//学習予定枚数
	public int							mLimitCount;		//最大許容訂正回数

	public Date							mStartDate;			//テスト開始時間
	public Date							mEndDate;			//テスト終了時間
	public long							mAnswerTime;		//テスト時間
	public int							mScore;				//テストの点数。
	
	public String						mGradingResultData;	//採点データ
	public String						mInkData;			//インクデータ
	public String						mRedComment;		//赤字コメント
	public String						mTagComment;		//付箋コメント
	public String						mTagText;			//付箋テキスト
	public String						mStrokeNum;			//ストローク数
	public int							mPrintType;			//0:通常, 1=最終, 2=診断

	//20140731 ADD-S For 録音対応
	public byte[]						mSoundRecord;		//音声録音データ
	//20140731 ADD-E For 録音対応
	
    //20150121 ADD-S For 2015年度Ver. 教材更新
	public Date							mPrintUpdateTime;	//問題更新日
    //20150121 ADD-E For 2015年度Ver. 教材更新
	
    //20150210 DD-S For 2015年度Ver. 音声メモ
	public byte[]						mSoundComment;		//音声録音データ
    //20150210 ADD-E For 2015年度Ver. 音声メモ
	
    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
	public int							mSoundRecordStatus;//音声録音ステータス 0:適,1:不適
    //20150303 ADD-E For 2015年度Ver. 音声メモステータス
	
    //20150409 ADD-S For 2015年度Ver. 未読コメント
	public int							mCommentUnreadFlg;	//指導者コメント未読Flg 0:既読,1:未読
    //20150409 ADD-E For 2015年度Ver. 未読コメント
	
	//20150416 ADD-S InkData To Binary
	public byte[]						mInkBinary;			//インクバイナリデータ
	//20150416 ADD-E InkData To Binary
	
    //20150423 ADD-S For 2015年度Ver. 未読コメント
	public Date							mPrintSetDate;		//初回学習日(未読コメント表示する際のソート用)
	//20150423 ADD-E For 2015年度Ver. 未読コメント
	
	//送信用
	public int							mPenThickness;		//ペンの太さ
	public int							mGrade;				//最終テスト用
	
	//Work
	public MQuestion2 					mQuestion;	

	//DBにも記録（データ取得時はクリア、端末内でのみ使用する）
	public int							mIsRegist;			//0=送信済み, 1=未送信
	public int 							mIsLearned;			//学習したかどうか
	public int 							mIsGreaded;			//採点したかどうか
	//学習中かどうかのフラグ(DB記録せず)
	public int							mIsLearning;		//0=見るだけ, 1=学習中
	
	
    //Work
    /// 今のページを手動採点したかどうか
    public boolean[] ManualMarked = null;
    //Work
    /// 今のページに○を表示したかどうか
    public boolean[] DrawCircle = null;
	
    //20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
	public int mOrgCount = 0;		//テストの学習回数(加算前)。
    //20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
    
    
	public DResultData() {
		ClearAll();
	}
	
	public void ClearAll()
	{
		mStudentID = KumonDataCtrl.SF_GUID_NULL;
		mKyokaID = KumonDataCtrl.SF_GUID_NULL;
		mKyokaName = "";
		mKyokaOrderNo = 0;
		mKyozaiID = KumonDataCtrl.SF_GUID_NULL;
		mKyozaiName = "";
		mKyokaOrderNo = 0;
		mPrintUnitID = KumonDataCtrl.SF_GUID_NULL;
		mPrintSetID = KumonDataCtrl.SF_GUID_NULL;
		mPrintID = KumonDataCtrl.SF_GUID_NULL;
		mPrintNo = 0;
		
		mStatus = 0;
		mCount = 0;				//テストの学習回数。
		mGradingMethod = 0;
		mGradingStatus = 0;
		mLearningPlace = 0;
		mScheduledDate = "";
		mScheduledIndex = 0;
		mLimitCount = 0;
		
		mGradingResultData = "";
		mInkData = "";
		//20150416 ADD-S InkData To Binary
		mInkBinary = null;
		//20150416 ADD-E InkData To Binary
		
		mRedComment = "";
		mTagComment = "";
		mTagText = "";
		mStrokeNum = "";
		mPrintType = 0;
		
		mScore = -1;
		mPenThickness = 0;
		mGrade = 0;
		
		mQuestion = null;
		
		mIsRegist = 0;
		mIsLearned = 0;
		mIsGreaded = 0;
		mIsLearning = 0;
		
		//20140731 ADD-S For 録音対応
		mSoundRecord = null;
		//20140731 ADD-E For 録音対応
	    //20150121 ADD-S For 2015年度Ver. 教材更新
		mPrintUpdateTime = null;
 	    //20150121 ADD-E For 2015年度Ver. 教材更新
		
	    //20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
		mOrgCount = 0;		//テストの学習回数(加算前)。
	    //20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
		
        //20150210 ADD-S For 2015年度Ver. 音声メモ
		mSoundComment = null;
        //20150210 ADD-E For 2015年度Ver. 音声メモ
		
	    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
		mSoundRecordStatus = 0;
		//20150303 ADD-E For 2015年度Ver. 音声メモステータス
	    //20150409 ADD-S For 2015年度Ver. 未読コメント
		mCommentUnreadFlg = 0;
	    //20150409 ADD-E For 2015年度Ver. 未読コメント

	    //20150423 ADD-S For 2015年度Ver. 未読コメント
		mPrintSetDate = null;		//初回学習日
		//20150423 ADD-E For 2015年度Ver. 未読コメント
		
	}
	public void InitPageWork(int pageSide) {
		if(ManualMarked == null) {
			ManualMarked = new boolean[pageSide];
			DrawCircle = new boolean[pageSide];
			for(int i = 0; i < pageSide; i++) {
				ManualMarked[i] = false;
				DrawCircle[i] = false;
			}
		}
	}
	
	@Override
	public Object clone() {	//throwsを無くす
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	
}
