package kumon2014.markcontroltool;


import kumon2014.kumondata.*;

public class QuestionControl {
	// / <summary>
	// / 教科ID
	// / </summary>
	public String KyokaID;

	// / <summary>
	// / 教材ID
	// / </summary>
	public String KyozaiID;

	// / <summary>
	// / プリントユニットID
	// / </summary>
	public String PrintUnitID;
	
	// / <summary>
	// / プリントセットID
	// / </summary>
	public String PrintSetID;

	// / <summary>
	// / テストID
	// / </summary>
	public String PrintID;

	// / <summary>
	// / テストNo
	// / </summary>
	public int PrintNo;
	
	// / <summary>
	// / 学習回数
	// / </summary>
	public int TrialCount;
	// / <summary>
	// / 最大学習回数
	// / </summary>
	public int MaxTrialCount;

	// / <summary>
	// / 採点方法
	// / </summary>
	public int QuestionGradingMethod;

	// / <summary>
	// / 圧縮された採点結果データのバイト配列
	// / </summary>
	public String GradingResultData;

	// / <summary>
	// / 圧縮されたインクデータのバイト配列
	// / </summary>
	public String InkData;
	//20150416 ADD-S InkData To Binary
	public byte[] InkBinary;
	//20150416 ADD-E InkData To Binary

	// / <summary>
	// / 採点されたかどうか
	// / </summary>
	public int GradingStatus;

	//状態
	public int Status;
	
	//点数
	public int Score;
	
	public int PrintType;
	
	public String RedComment;
	public String TagComment;
	public String TagText;

	public int	IsRegist;			//0=送信済み, 1=未送信
	public int 	IsLearned;			//学習したかどうか
	public int 	IsGreaded;			//採点したかどうか

	
    //20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
	public int mOrgCount = 0;		//テストの学習回数(加算前)。
    //20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用


	
	// / <summary>
	// / プリント情報の設定
	// / </summary>
	// / <param name="printData">プリントデータ</param>
	public void SetResultData(DResultData resultData) {

		if (resultData == null) {
			return;
		}

		// 教科ID
		KyokaID = resultData.mKyozaiID;
		// 教材ID
		KyozaiID = resultData.mKyozaiID;
		// プリントユニット
		PrintUnitID = resultData.mPrintUnitID;

		
		// プリントセットID
		if (resultData.mPrintSetID != null) {
			PrintSetID = resultData.mPrintSetID;
		}

		// プリントID
		PrintID = resultData.mPrintID;
		PrintNo = resultData.mPrintNo;

		// 学習回数
		TrialCount = resultData.mCount;
		MaxTrialCount = resultData.mLimitCount;
		
		// 採点方法
		QuestionGradingMethod = resultData.mGradingMethod;

		GradingStatus = resultData.mGradingStatus;
		Status = resultData.mStatus;
		Score  = resultData.mScore;

		GradingResultData = resultData.mGradingResultData;
		InkData = resultData.mInkData;
		//20150416 ADD-S InkData To Binary
		InkBinary = resultData.mInkBinary;
		//20150416 ADD-E InkData To Binary
		RedComment = resultData.mRedComment;
		TagComment = resultData.mTagComment;
		TagText = resultData.mTagText;

		IsRegist = resultData.mIsRegist;
		IsLearned = resultData.mIsLearned;
		IsGreaded = resultData.mIsGreaded;
		
		PrintType = resultData.mPrintType;
		
	    //20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
		mOrgCount = resultData.mOrgCount;		//テストの学習回数(加算前)。
	    //20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
		
	}
}
