package kumon2014.kumondata;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import pothos.markcontroltool.InkControlTool.CInkData;
import pothos.markcontroltool.InkControlTool.CInkIO;
import kumon2014.database.data.DataDBIO;

public class KumonDataCtrl {
	public static final String SF_GUID_NULL = "00000000-0000-0000-0000-000000000000";
	
	//NEW_VER ADD-S
	//Penの太さ
	public static final int SF_PEN_THIN = 2;
	public static final int SF_PEN_NORMAL = 3;
	public static final int SF_PEN_BOLD = 6;
	
	//消しゴムの太さ
    public static final float SF_Eraser_Thin = 4.0f;
    public static final float SF_Eraser_Normal = 20.0f;
    public static final float SF_Eraser_Boid = 30.0f;
	//NEW_VER ADD-E
	
	
	// ResultData Status
	public static final int SF_DATATYPE_NONE = -1; 
	/// 学習済み
	public static final int SF_DATATYPE_DONE = 0; 
	/// 学習予定(自宅・新規)
	public static final int SF_DATATYPE_NEXT = 1;
	/// 学習予定(自宅・訂正)
	public static final int SF_DATATYPE_RETRY = 2;
	/// 学習予定(当日・新規)
	public static final int SF_DATATYPE_TODAY = 3; 
	/// 学習予定(当日・訂正)
	public static final int SF_DATATYPE_TODAYRETRY = 4; 
	/// 学習予定(宿題・新規)
	public static final int SF_DATATYPE_HOMEWORK = 5; 
	/// 学習予定(宿題・訂正)
	public static final int SF_DATATYPE_HOMEWORKRETRY = 6; 
	/// 採点待ち(本人採点)
	public static final int SF_DATATYPE_GRADESELF = 7; 
	/// 採点 (指導者採点On学習者端末)
	public static final int SF_GRADEINSTRUCTORONCLIENT = 8; 
	/// 未送信
	public static final int SF_DATATYPE_NOTREGIST = 9; 
	/// WebView 
	public static final int SF_DATATYPE_WEBVIEW = 10; 
	/// 診断/終了TEST
	public static final int SF_DATATYPE_SPECIALTEST = 11; 

	//20150216 MOD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
	public static final int SF_DATATYPE_WAIT = 12; 
	//20150216 MOD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
	
    //20150409 ADD-S For 2015年度Ver. 未読コメント
	public static final int SF_DATATYPE_DONE_UNREAD = 13; 
    //20150409 ADD-E For 2015年度Ver. 未読コメント
	
	//採点方法
	public static final int SF_GradingMethod_Auto = 0;
	public static final int SF_GradingMethod_Self = 1;
	public static final int SF_GradingMethod_Instrucore = 2;
	public static final int SF_GradingMethod_InstrucoreOnClient = 3;

	//状態
	public static final int SF_STATUS_NO			= 0;	//未学習
	public static final int SF_STATUS_READY			= 1;	//学習待機中
	public static final int SF_STATUS_LEARNING 		= 3;	//学習中
	public static final int SF_STATUS_GRADEREADY 	= 4;	//採点待機中
	public static final int SF_STATUS_GRADING 		= 5;	//採点中
	public static final int SF_STATUS_END 			= 10;	//完了

	
	//GradingStatus
	public static final int SF_GREADINGSTATUS_NO		= 0;	//未学習
	public static final int SF_GREADINGSTATUS_LEARNED	= 1;	//学習済み
	public static final int SF_GREADINGSTATUS_GRADED	= 2;	//採点済み
	
	
	public static final int SF_LEARNING_NOCHECK = 0; 
	public static final int SF_LEARNING_NO = 1; 
	public static final int SF_LEARNING_YES = 2; 

	//GradingStatus
	public static final int SF_LEARNINGPLACE_HOME		= 0;	//教室
	public static final int SF_LEARNINGPLACE_SCHOOL		= 1;	//学習済み
	
	//PrintType
	public static final int SF_PRINTTYPE_NORMAL			= 0;	//通常テスト
	public static final int SF_PRINTTYPE_FINAL			= 1;	//最終テスト
	public static final int SF_PRINTTYPE_DIAGNOSTIC		= 2;	//診断テスト
	/*
	/// 保存期間終了
	public static final int SF_DATATYPE_KEEPOUT = 6; 
	/// WebView 
	public static final int SF_DATATYPE_WEBVIEW = 7; 
	/// 採点 (本人採点)
	public static final int SF_DATATYPE_GRADESELF = 8; 
	/// 採点 (本人採点)
	public static final int SF_DATATYPE_GRADEINSTRUCTOR = 9; 
	*/
	private static ArrayList<DKyozaiPrintSet> mKyozaiPrintSetList = null;
	private static String 					  mAccessStudentID = "";

	//20130508 ADD-S For No.105
	public static boolean bProgressStop = false;
	
	public static ArrayList<DKyozaiPrintSet> GetKyozaiDataExistList(String studentid) 
	{
		if(studentid == "") {
			ClearKyozaiPrintSetList();
		}
		else {
			if(mKyozaiPrintSetList == null) {
				mKyozaiPrintSetList = DataDBIO.DB_GetKyozaiDataExistList(studentid);
				mAccessStudentID = studentid;
			}
			else {
				if(mAccessStudentID.equalsIgnoreCase(studentid) == false) {
					mKyozaiPrintSetList.clear();
					mKyozaiPrintSetList = null;
					mKyozaiPrintSetList = DataDBIO.DB_GetKyozaiDataExistList(studentid);
					mAccessStudentID = studentid;
				}
			}
		}
		return mKyozaiPrintSetList;
	}

	public static void KyozaiDataExistList_Kyozai(String studentid, String KyokaID,  String KyozaiID)
	{
		if(mKyozaiPrintSetList == null) {
			mKyozaiPrintSetList = DataDBIO.DB_GetKyozaiDataExistList(studentid);
			mAccessStudentID = studentid;
		}
		else {
			if(mAccessStudentID.equalsIgnoreCase(studentid) == false) {
				mKyozaiPrintSetList.clear();
				mKyozaiPrintSetList = null;
				mKyozaiPrintSetList = DataDBIO.DB_GetKyozaiDataExistList(studentid);
				mAccessStudentID = studentid;
			}
			else {
				/***
				for(int i = 0; i < mKyozaiPrintSetList.size(); i++) {
					DKyozaiPrintSet kyozaiPrintSet = mKyozaiPrintSetList.get(i);
					if(kyozaiPrintSet.mKyokaID.equals(KyokaID) && kyozaiPrintSet.mKyozaiID.equals(KyozaiID)) {
						kyozaiPrintSet.ClearAll();
						kyozaiPrintSet = DataDBIO.DB_GetKyozaiDataExistList_Kyozai(studentid, KyokaID,  KyozaiID);
						mKyozaiPrintSetList.set(i, kyozaiPrintSet);
						break;
					}
				}
				***/
				mKyozaiPrintSetList.clear();
				mKyozaiPrintSetList = null;
				mKyozaiPrintSetList = DataDBIO.DB_GetKyozaiDataExistList(studentid);
				mAccessStudentID = studentid;

			}
		}
	}
	
	public static void ClearKyozaiPrintSetList() 
	{
		if(mKyozaiPrintSetList != null) {
			for(int i = 0; i < mKyozaiPrintSetList.size(); i++) {
				DKyozaiPrintSet kyozaiPrintSet = mKyozaiPrintSetList.get(i);
				if(kyozaiPrintSet != null) {
					kyozaiPrintSet.ClearAll();
					kyozaiPrintSet = null;
				}
			}
			mKyozaiPrintSetList.clear();
			mKyozaiPrintSetList = null;
		}
		mAccessStudentID = "";
	}
	
    public static boolean ExistLearningData(String studentid)
    {
    	ArrayList<DKyozaiPrintSet> kyozaiprintsetList =	GetKyozaiDataExistList(studentid) ;
		for(int i = 0; i < kyozaiprintsetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = kyozaiprintsetList.get(i);
			if(kyozaiprintset.mDone == true || kyozaiprintset.mNext == true) {
				return true;
			}
		}
		return false;
    }
    public static boolean TodayExistLearningData(String studentid)
    {
    	ArrayList<DKyozaiPrintSet> kyozaiprintsetList =	GetKyozaiDataExistList(studentid) ;
		for(int i = 0; i < kyozaiprintsetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = kyozaiprintsetList.get(i);
			//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
			//if(kyozaiprintset.mToday == true || kyozaiprintset.mHomeWork == true) {
			if(kyozaiprintset.mToday == true || kyozaiprintset.mPast == true) {
			//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				return true;
			}
		}
		return false;
    }
	public static boolean IsExistSendData(String studentid)
	{
    	//20140605 MOD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
    	//ArrayList<DResultData> resultdatalist = DataDBIO.DB_GetRegistDataList(studentid, false);
    	ArrayList<DResultData> resultdatalist = DataDBIO.DB_GetRegistDataList(studentid, false);
    	//20140605 MOD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
    	if(resultdatalist != null && resultdatalist.size() > 0) {
    		return true;
    	}
		return false;
	}
	
	public static ArrayList<DPrintSet> GetRegistDataList(String studentid)
	{
		ArrayList<DPrintSet> printsetlist = new ArrayList<DPrintSet>();
		
		//20140605 MOD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
    	//ArrayList<DResultData> resultdatalist = DataDBIO.DB_GetRegistDataList(studentid);
    	ArrayList<DResultData> resultdatalist = DataDBIO.DB_GetRegistDataList(studentid, true);
    	//20140605 MOD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
    	
    	if(resultdatalist != null) {
    		String Oldprintsetid = "";
    		DPrintSet printset = null;
    		for(int i = 0; i < resultdatalist.size(); i++)
			{
    			DResultData resultdata = resultdatalist.get(i);
				if(resultdata.mPrintSetID.equals(Oldprintsetid) == false ) {
					if(printset != null) {
						printsetlist.add(printset);
						printset = null;
					}
					printset = new DPrintSet();
					printset.mStudentID = resultdata.mStudentID;
					printset.mKyokaID = resultdata.mKyokaID;
					printset.mKyozaiID = resultdata.mKyozaiID;
					if(resultdata.mCount ==1) {
						//初回学習時のPrintSetIDはAndroid内で勝手に振ったもの
						printset.mPrintSetID = KumonDataCtrl.SF_GUID_NULL;
					}
					else {
						printset.mPrintSetID = resultdata.mPrintSetID;
					}
					printset.mPenThickness = resultdata.mPenThickness;
					
					Oldprintsetid = resultdata.mPrintSetID; 
				}
				printset.mResultList.add(resultdata);
			}
			if(printset != null) {
				printsetlist.add(printset);
			}
    	}
		return printsetlist;
	}
	//20140617 ADD-S For　メモリ不足対応
	public static ArrayList<DResultData> GetRegistPrintSetList(String studentid)
	{
		ArrayList<DResultData> resultdatalist = DataDBIO.DB_GetPrintSetIDListForRegist(studentid);
		return resultdatalist;
	}
	public static DPrintSet GetRejistPrintSet(String studentid, String KyokaID, String KyozaiID, String PrintSetID) {
		ArrayList<DResultData> resultdatalist = DataDBIO.DB_GetRejistPrintSet(studentid, KyokaID, KyozaiID, PrintSetID);
		
		DPrintSet printset = new DPrintSet();
    	
    	if(resultdatalist != null) {
    		for(int i = 0; i < resultdatalist.size(); i++)
			{
    			DResultData resultdata = resultdatalist.get(i);

    			if(i == 0) {
	    			printset.mStudentID = resultdata.mStudentID;
					printset.mKyokaID = resultdata.mKyokaID;
					printset.mKyozaiID = resultdata.mKyozaiID;
					if(resultdata.mCount ==1) {
						//初回学習時のPrintSetIDはAndroid内で勝手に振ったもの
						printset.mPrintSetID = KumonDataCtrl.SF_GUID_NULL;
					}
					else {
						printset.mPrintSetID = resultdata.mPrintSetID;
					}
					printset.mPenThickness = resultdata.mPenThickness;
    			}
    			
				//InkDataをBinaryからJsonへ変換
    	        CInkData data = null;
    	        if(resultdata.mInkBinary != null && resultdata.mInkBinary.length > 0) {
    		    	try {
    		    		ByteArrayInputStream ms = new ByteArrayInputStream(resultdata.mInkBinary);
    		    		CInkIO inkIO = new CInkIO();
    		    		data = inkIO.LoadInk(ms);
    		    		ms.close();
    		    		ms = null;
    		    		resultdata.mInkData = inkIO.SaveInkToJson(data);
    		    		resultdata.mInkBinary = null;
    		    		inkIO = null;
    		    		data.Clear();
    		    		data = null;
    		    	}
    		    	catch(Exception e) {
        	        	resultdata.mInkData = "";
        	        	if(data != null) {
        	        		data.Clear();
        	        	}
    		    		data = null;
    		    	}
    	        }

    			
				printset.mResultList.add(resultdata);
			}
    	}
		return printset;
	}
	
	//20140617 ADD-E For　メモリ不足対応
	public static boolean DB_ClearRegistData(String studentid) {
    	return DataDBIO.DB_ClearRegistData(studentid);
	}
	//20140617 ADD-S For　メモリ不足対応
	public static boolean DB_ClearRegistPrintSetData(String studentid, String KyokaID, String KyozaiID, String PrintSetID) {
    	return DataDBIO.DB_ClearRegistPrintSetData(studentid, KyokaID, KyozaiID, PrintSetID);
	}
	//20140617 ADD-E For　メモリ不足対応
	//20140718 ADD-S For Print単位送信
	public static boolean DB_ClearRegistPrintUnitData(String studentid, String KyokaID, String KyozaiID, String PrintUnitID) {
    	return DataDBIO.DB_ClearRegistPrintUnitData(studentid, KyokaID, KyozaiID, PrintUnitID);
	}
	//20140718 ADD-E For Print単位送信

    //20150409 ADD-S For 2015年度Ver. 未読コメント
    public static boolean ExistUnreadData(String studentid)
    {
    	boolean exist =	DataDBIO.ExistUnreadData(studentid) ;
		return exist;
    }
    //20150409 ADD-E For 2015年度Ver. 未読コメント
	
	
}
