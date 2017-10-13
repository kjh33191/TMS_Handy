package kumon2014.database.data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import android.content.Context;
import kumon2014.database.master.MQuestion2;
import kumon2014.database.master.MastDBIO;
import kumon2014.kumondata.DKyozaiPrintSet;
import kumon2014.kumondata.DResultData;
import kumon2014.kumondata.DStudent;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.kumondata.WDownloadResultData;

public class DataDBIO {
	private static DataSQLHelper   mDataSQLHelper = null;

	public static void Open(Context con) 
	{
		mDataSQLHelper = new DataSQLHelper(con); 
		
		// check for new password
		boolean isOldPass = false;
		File file = new File(DataSQLHelper.DATABASE_NAME);
		if (file.exists()) {
			try {
				SQLiteDatabase db = SQLiteDatabase.openDatabase(DataSQLHelper.DATABASE_NAME,
					mDataSQLHelper.GetKey256().toCharArray(), null, SQLiteDatabase.OPEN_READONLY);
				db.close();
			}
			catch (SQLiteException e) {
				isOldPass = true;
			}
		}
		if (isOldPass) {
			// change password
			try {
				@SuppressWarnings("deprecation")
				SQLiteDatabase db = SQLiteDatabase.openDatabase(DataSQLHelper.DATABASE_NAME,
					mDataSQLHelper.GetKey().toCharArray(), null, SQLiteDatabase.OPEN_READWRITE);
				db.changePassword(mDataSQLHelper.GetKey256());
				db.close();
			}
			catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}
	public static void Close() 
	{
		mDataSQLHelper.close();
	}

	//All Clear
	public static boolean DB_AllClear()
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		boolean ret = false;
		try {
			writable.beginTransaction();

			ret = TblResultData.DB_ClearAll(writable, true);
			ret = TBlStudent.DB_ClearAll(writable);
			
		    //20150409 ADD-S For 2015年度Ver. 未読コメント
			TblReadCommentData.DB_ClearAll(writable);
		    //20150409 ADD-E For 2015年度Ver. 未読コメント
			if(ret) {
				writable.setTransactionSuccessful();
			}
		}
		catch(Exception e) {}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
        }

		return ret;
		
	}
	//All Student
	public static boolean DB_ClearStudent(String studentId)
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		boolean ret = false;
		try {
			writable.beginTransaction();

			ret = TblResultData.DB_DeleteByStudentID(writable, studentId);
			ret = TBlStudent.DB_DeleteByStudentID(writable, studentId);
			if(ret) {
				writable.setTransactionSuccessful();
			}
		}
		catch(Exception e) {}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
        }

		return ret;
		
	}
	//Get All Student
	public static ArrayList<DStudent> DB_GetAllStudent()
	{
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
		
		ArrayList<DStudent> studentlist = new ArrayList<DStudent>();
		try {
			studentlist = TBlStudent.DB_GetAllStudent(readable) ;
		}
		catch(Exception e) 
		{
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return studentlist;
	}

	//Get Student
	public static DStudent DB_GetStudent(String studentId) 
	{
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
		
		DStudent student = new DStudent();
		try {
			student = TBlStudent.DB_GetStudent(readable, studentId) ;
		}
		catch(Exception e) 
		{
			student = null;
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return student;
	}

	//Save Student(Delete & Insert)
	public static boolean DB_SaveStudent(DStudent student) 
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			writable.beginTransaction();

			ret = TBlStudent.DB_DeleteByStudentID(writable, student.mStudentID);
			ret = TBlStudent.DB_InsertStudent(writable, student);
			
			if(ret) {
				writable.setTransactionSuccessful();
			}
		}
		catch(Exception e) {}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
        }

		return ret;
	}
	public static boolean DB_SaveDownLoadDataAll(String studentId, WDownloadResultData downloadResultData)
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			writable.beginTransaction();

			//既存のデータを削除
			ret = TblResultData.DB_DeleteByStudentID(writable, studentId);
			if(ret == true) {
				//新規データ追加
				ret = TblResultData.DB_InsertResultDataList(writable, downloadResultData.mDownLoadResultDataList);
			}
			if(ret) {
				writable.setTransactionSuccessful();
			}
		}
		catch(Exception e) {
//			String s = e.getMessage();
		}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
        }
		return ret;
	}
	//20140618 ADD-S For 分割受信
	public static boolean DB_SaveDownLoadPrintSetData(String studentId, WDownloadResultData downloadResultData)
	{
		boolean ret = false;
		if(downloadResultData.mDownLoadResultDataList != null && downloadResultData.mDownLoadResultDataList.size() > 0) {
//			DResultData resultData = wdownloadresultdata.mDownLoadResultDataList.get(0);
			
			SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
			try {
				writable.beginTransaction();
				//20140620 DEL-S 受信前に全データ削除を行っているので、削除は不要
				//既存のデータを削除
				/***
				ret = TblResultData.DB_DeleteByPrintSetID(writable, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintSetID);
				int len = wdownloadresultdata.mDownLoadResultDataList.size();
				for(int i = 0; i < len; i++) {
					DResultData resultData2 = wdownloadresultdata.mDownLoadResultDataList.get(i);
					ret = TblInkData.DB_DeleteByPrintUnit(writable, studentid, resultData2.mKyokaID, resultData2.mKyozaiID, resultData2.mPrintUnitID);
					ret = TblResultData.DB_DeleteByPrintUnit(writable, studentid, resultData2.mKyokaID, resultData2.mKyozaiID, resultData2.mPrintUnitID);
				}
				***/
				//20140620 DEL-E
				ret = true;
				if(ret == true) {
					//新規データ追加
					ret = TblResultData.DB_InsertResultDataList(writable, downloadResultData.mDownLoadResultDataList);
				}
				if(ret) {
					writable.setTransactionSuccessful();
				}
			}
			catch(Exception e) {
//				String s = e.getMessage();
			}
			finally{
				writable.endTransaction();
				writable.close();
				writable = null;
	        }
		}
		else {
			ret = true;
		}
		
		return ret;
	}
	//20140618 ADD-E For 分割受信
	
	public static boolean DB_SaveDownLoadDataAllRetry(String studentId, WDownloadResultData downloadResultData)
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			writable.beginTransaction();

			//既存のデータを削除
			for (DResultData resultData : downloadResultData.mDownLoadResultDataList) {
				//PrintUnitIDが変更されている可能性があるので、教材単位で削除
				//ret = TblResultData.DB_DeleteByPrintUnit(writable, DResultData.mStudentID, DResultData.mKyokaID, DResultData.mKyozaiID, DResultData.mPrintUnitID);
				ret = TblResultData.DB_DeleteByKyozaiID(writable, resultData.mStudentID, resultData.mKyokaID, resultData.mKyozaiID);
				if(ret == false) {
					break;
				}
			}
			if(ret == true) {
				//新規データ追加
				ret = TblResultData.DB_InsertResultDataList(writable, downloadResultData.mDownLoadResultDataList);
			}
			if(ret) {
				writable.setTransactionSuccessful();
			}
		}
		catch(Exception e) {
//			String s = e.getMessage();
		}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
        }
		return ret;
	}
	
	//通常・新規PrintSet取得
	public static ArrayList<DResultData> DB_GetNextKyozaiPrintSet(String studentId, String kyokaId, String kyozaiId, int num) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> resultList = new ArrayList<DResultData>();
		try {
			ArrayList<DResultData> tempList = TblResultData.DB_GetNextResultData(readable, studentId, kyokaId, kyozaiId);
			
			String oldPrintSetId = "";
			String oldScheduledDate = "";
			int oldPrintNo = -1;
			int oldPrintType = -1;
			int pageCount = 0;
			
			for (DResultData result : tempList) {
				if (oldPrintSetId.isEmpty()) {
					oldPrintSetId = result.mPrintSetID;
					oldScheduledDate = result.mScheduledDate;
					oldPrintNo = result.mPrintNo;
					oldPrintType = result.mPrintType;
				}
	            //学習予定日が異なる場合は、同一PrintSetには出来ない 又は　プリントセットが違う
	            if ((oldScheduledDate.equals(result.mScheduledDate) == false) || (oldPrintSetId.equals(result.mPrintSetID) == false)) {
					break;
				}
	            //ページ番号が連番でない場合も不可
	            if (oldPrintNo != result.mPrintNo) {
					break;
				}
	            if (oldPrintType != result.mPrintType) {
					break;
				}
	            
	            oldPrintNo = result.mPrintNo + 1;
				oldPrintType = result.mPrintType;
				
				resultList.add((DResultData) result.clone());
				pageCount++;
				
				if(pageCount >= num) {
					break;
				}
			}
	    	//問題データ取得
	    	resultList = DB_GetPrint(resultList);
			return resultList;
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return resultList;
		
	}
	//通常・訂正PrintSet取得
	public static ArrayList<DResultData> DB_GetRetryKyozaiPrintSet(String studentId, String kyokaId, String kyozaiId) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> resultList = null;
		try {
	    	resultList = TblResultData.DB_GetRetryResultData(readable, studentId, kyokaId, kyozaiId);
	    	String printSetId = "";
	    	if(resultList.size() > 0) {
	    		printSetId = resultList.get(0).mPrintSetID;
	    		resultList.clear();
	    	}
	    	if(!printSetId.equals("")) {
		    	resultList = TblResultData.DB_GetPrintSet(readable, studentId, kyokaId, kyozaiId, printSetId, true);
		    	//問題データ取得
		    	resultList = DB_GetPrint(resultList);
	    	}
			return resultList;
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return resultList;
	}
	//当日・新規PrintSet取得
	public static ArrayList<DResultData> DB_GetTodayKyozaiPrintSet(String studentId, String kyokaId, String kyozaiId, int num) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> resultList = new ArrayList<DResultData>();
		try {
			ArrayList<DResultData> tempList = TblResultData.DB_GetNextResultData(readable, studentId, kyokaId, kyozaiId);
			String oldPrintSetId = "";
			String oldScheduledDate = "";
			int oldPrintNo = -1;
			int pageCount = 0;
			for(DResultData result : tempList) {
				//学習予定日が今日のものだけ
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
				String today = sdf.format(date);
				int cmp = result.mScheduledDate.compareTo(today);
				if (cmp == 0)
				{
					//当日
					//OK
				}
				else if (cmp < 0) {
					//過去日
					continue;
				}
				else
				{
					//未来日
					//20140822 DEL-S For 学習予定が未来日でも教室で学習可能
					/**
					//先の日付は対象外
					break;
					**/
					//OK
					//20140822 DEL-E For 学習予定が未来日でも教室で学習可能
					
					//20140915 ADD-S For 学習予定が未来日は宿題学習
					break;
					//20140915 ADD-S For 学習予定が未来日は宿題学習
				}
				if(oldPrintSetId.isEmpty()) {
					oldPrintSetId = result.mPrintSetID;
					oldScheduledDate = result.mScheduledDate;
					oldPrintNo = result.mPrintNo;
				}
					
	            //学習予定日が異なる場合は、同一PrintSetには出来ない 又は　プリントセットが違う
	            if ((oldScheduledDate.equals(result.mScheduledDate) == false) || (oldPrintSetId.equals(result.mPrintSetID) == false)) {
					break;
				}
	            //ページ番号が連番でない場合も不可
	            if (oldPrintNo != result.mPrintNo) {
					break;
				}
	            oldPrintNo = result.mPrintNo + 1;
				
				resultList.add((DResultData) result.clone());
				pageCount++;
				
				if(pageCount >= num) {
					break;
				}
			}
	    	//問題データ取得
	    	resultList = DB_GetPrint(resultList);
			return resultList;
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return resultList;
		
	}
	//当日・訂正PrintSet取得
	public static ArrayList<DResultData> DB_GetTodayRetryKyozaiPrintSet(String studentId, String kyokaId, String kyozaiId) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> resultList = null;
		try {
	    	resultList = TblResultData.DB_GetRetryResultData(readable, studentId, kyokaId, kyozaiId);
	    	String printSetId = "";
			for (DResultData result : resultList) {
				//学習予定日が今日のものだけ
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
				String today = sdf.format(date);
				int cmp = result.mScheduledDate.compareTo(today);
				if (cmp == 0)
				{
					//OK
				}
				else if (cmp < 0) {
					continue;
				}
				else
				{
					//20140822 DEL-S For 学習予定が未来日でも教室で学習可能
					/***
					//先の日付は対象外
					break;
					***/
					//OK
					//20140822 DEL-E For 学習予定が未来日でも教室で学習可能
					
					//20140915 ADD-S For 学習予定が未来日は宿題学習
					break;
					//20140915 ADD-S For 学習予定が未来日は宿題学習
				}
				
				printSetId = result.mPrintSetID;
				break;
			}
	    	
	    	if(printSetId.equals("") == false) {
		    	resultList = TblResultData.DB_GetPrintSet(readable, studentId, kyokaId, kyozaiId, printSetId, true);
		    	//問題データ取得
		    	resultList = DB_GetPrint(resultList);
	    	}
			return resultList;
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return resultList;
	}
	//宿題・新規PrintSet取得
	public static ArrayList<DResultData> DB_GetHomeKyozaiPrintSet(String studentId, String kyokaId, String kyozaiId, int num) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> tempList = null;
    	ArrayList<DResultData> resultList = new ArrayList<DResultData>();
		try {
			tempList = TblResultData.DB_GetNextResultData(readable, studentId, kyokaId, kyozaiId);
			String oldPrintSetId = "";
			String oldScheduledDate = "";
			int oldPrintNo = -1;
			int pageCount = 0;
			for(DResultData result : tempList) {
				//学習予定日が今日より前の
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
				String today = sdf.format(date);
				int cmp = result.mScheduledDate.compareTo(today);
				if (cmp == 0)
				{
					//今日の日付は対象外
					//20140915 MOD-S For 学習予定が未来日は宿題学習
					//break;
					continue;
					//20140915 MOD-E For 学習予定が未来日は宿題学習
					
				}
				else if (cmp < 0) {
					//OK
				}
				else
				{
					//20140915 DEL-S For 学習予定が未来日は宿題学習
					//先の日付は対象外
					//break;
					//20140915 DEL-E For 学習予定が未来日は宿題学習
					
				}
				if(oldPrintSetId.isEmpty()) {
					oldPrintSetId = result.mPrintSetID;
					oldScheduledDate = result.mScheduledDate;
					oldPrintNo = result.mPrintNo;
				}
					
	            //学習予定日が異なる場合は、同一PrintSetには出来ない 又は　プリントセットが違う
	            if ((oldScheduledDate.equals(result.mScheduledDate) == false) || (oldPrintSetId.equals(result.mPrintSetID) == false)) {
					break;
				}
	            //ページ番号が連番でない場合も不可
	            if (oldPrintNo != result.mPrintNo) {
					break;
				}
	            oldPrintNo = result.mPrintNo + 1;
				
				resultList.add((DResultData) result.clone());
				pageCount++;
				
				if(pageCount >= num) {
					break;
				}
			}
	    	//問題データ取得
	    	resultList = DB_GetPrint(resultList);
			return resultList;
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return resultList;
		
	}
	//宿題・訂正PrintSet取得
	public static ArrayList<DResultData> DB_GetHomeRetryKyozaiPrintSet(String studentId, String kyokaId, String kyozaiId) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> workResultList = null;
    	ArrayList<DResultData> resultList = null;
		try {
			workResultList = TblResultData.DB_GetRetryResultData(readable, studentId, kyokaId, kyozaiId);
	    	String printSetID = "";
			for (DResultData resultData : workResultList) {
				//学習予定日が今日より前の
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
				String today = sdf.format(date);
				int cmp = resultData.mScheduledDate.compareTo(today);
				if (cmp == 0)
				{
					//今日の日付は対象外
					//20140915 MOD-S For 学習予定が未来日は宿題学習
					//break;
					continue;
					//20140915 MOD-E For 学習予定が未来日は宿題学習
				}
				else if (cmp < 0) {
					//OK
				}
				else
				{
					//20140915 DEL-S For 学習予定が未来日は宿題学習
					//先の日付は対象外
					//break;
					//20140915 DEL-E For 学習予定が未来日は宿題学習
				}
				
				printSetID = resultData.mPrintSetID;
				break;
			}
	    	
	    	if(printSetID.equals("") == false) {
	    		resultList = TblResultData.DB_GetPrintSet(readable, studentId, kyokaId, kyozaiId, printSetID, true);
		    	//問題データ取得
	    		resultList = DB_GetPrint(resultList);
	    	}
			return resultList;
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return resultList;
	}
	//学習済みPrintSet取得
	public static ArrayList<DResultData> DB_GetDoneKyozaiPrintSet(String studentId, String kyokaId, String kyozaiId) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> resultList = new ArrayList<DResultData>();
    	ArrayList<String> printSetIdList = null;
    	ArrayList<DResultData> tempResultList = null;
		try {
			printSetIdList = TblResultData.DB_GetPrintSetIDList(readable, studentId, kyokaId, kyozaiId);
			for(String printSetId : printSetIdList) {
				tempResultList = TblResultData.DB_GetPrintSet(readable, studentId, kyokaId, kyozaiId, printSetId, false);
				if(IsDone(tempResultList)) {
					for(int j = 0; j < tempResultList.size(); j++) {
						resultList.add((DResultData) tempResultList.get(j).clone());
					}
				}
			}
	    	//問題データ取得
			//20140731 DEL-S For 学習済み教材の表示は,PrintSet単位でＤＢ参照
			//問題データは後から取得
	    	//resultlist = DB_GetPrint(resultlist);
	    	//20140731 DEL-E For 学習済み教材の表示は,PrintSet単位でＤＢ参照
			return resultList;
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return resultList;
	}
	
	//採点用PrintSet取得
	public static ArrayList<DResultData> DB_GetGradePrintSet(String studentId)
	{
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> resultList = null;
		try {
	    	//20140731 MOD-S For Bug
	    	//採点はあくまでもSF_COL_GRADINGMETHOD = 1 OR ３のみ
	    	/***
	    	resultlist = TblResultData.DB_GetGradeResultData(readable, studentID);
	    	String PrintSetID = "";
	    	String kyokaID = "";
	    	String kyozaiID = "";
	    	if(resultlist.size() > 0) {
	    		PrintSetID = resultlist.get(0).mPrintSetID;
	    		kyokaID = resultlist.get(0).mKyokaID;
	    		kyozaiID = resultlist.get(0).mKyozaiID;
	    		resultlist.clear();
	    	}
	    	if(PrintSetID.equals("") == false) {
		    	resultlist = TblResultData.DB_GetPrintSet(readable, studentID, kyokaID, kyozaiID, PrintSetID, true);
		    	//問題データ取得
		    	resultlist = DB_GetPrint(resultlist);
	    	}
	    	***/
	    	resultList = TblResultData.DB_GetGradeResultData(readable, studentId);
	    	//問題データ取得
	    	resultList = DB_GetPrint(resultList);
	    	
	    	//20140731 MOD-E For Bug 
			return resultList;
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return resultList;
	}
	
	//問題データ取得
	public static ArrayList<DResultData> DB_GetPrint(ArrayList<DResultData> resultList)
	{
		ArrayList<String> printids = new ArrayList<String>();
		for (DResultData resultData : resultList) {
			printids.add(resultData.mPrintID);
		}
		HashMap <String,MQuestion2> questions = MastDBIO.DB_GetPrints(printids);
		for (DResultData resultData : resultList) {
			MQuestion2 q = questions.get(resultData.mPrintID);
			if (q != null)
				resultData.mQuestion = q;
		}
		return resultList;
		
	/*
	    for(int i = 0; i < resultlist.size(); i++) {
	    	DResultData resultdata = resultlist.get(i);
	    	MQuestion2 question ;
	    	question = MastDBIO.DB_GetPrint(resultdata.mPrintID); 
	    	if(question != null) {
	    		resultdata.mQuestion = question;
	    	}
	    }
		return resultlist;
		*/
	}
	public static boolean IsDone(ArrayList<DResultData> resultList) {
		for (DResultData resultdata : resultList) {
	    	if(resultdata.mPrintType != 0) {
	    		//診断・最終テストは参照不可
	    		return false;
	    	}
	    	if(resultdata.mIsRegist == 1) {
	    		continue;
	    	}
	    	if(resultdata.mIsLearned == 1) {
	    		continue;
	    	}
	    	if(resultdata.mScore >= 100) {
	    		continue;
	    	}
	    	if(resultdata.mCount >=  resultdata.mLimitCount) {
	    		continue;
	    	}
	    	if(resultdata.mStatus == KumonDataCtrl.SF_STATUS_GRADEREADY) {
	    		//採点待ち
	    		continue;
	    	}
	    	if(resultdata.mStatus == KumonDataCtrl.SF_STATUS_GRADING) {
	    		//採点中
	    		continue;
	    	}
	    	if(resultdata.mStatus == KumonDataCtrl.SF_STATUS_END) {
	    		//完了
	    		continue;
	    	}
    		return false;
		}
		return true;
	}
	
	public static boolean DB_UpdateResultData(String studentId, ArrayList<DResultData> updateList)
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			writable.beginTransaction();
			for (DResultData resultData : updateList) {
				ret = TblResultData.DB_UpdateResultData(writable, resultData);
				if(ret == false) {
					break;
				}
			}
			
			if(ret) {
				writable.setTransactionSuccessful();
			}
		}
		catch(Exception e) {}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
//			SQLiteDatabase.releaseMemory();
        }
		
		return ret;
	}
	//20140605 MOD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
    //public static ArrayList<DResultData> DB_GetRegistDataList(String studentid)
    public static ArrayList<DResultData> DB_GetRegistDataList(String studentId, boolean withInk)
	//20140605 MOD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
	{
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
		ArrayList<DResultData> registDataList = null;
		try {
			//20140605 MOD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			//registdatalist = TblResultData.DB_GetRegistDataList(readable, studentid);
			registDataList = TblResultData.DB_GetRegistDataList(readable, studentId, withInk);
			//20140605 MOD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		return registDataList;
	}
	public static boolean DB_ClearRegistData(String studentId)
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			ret = TblResultData.DB_ClearRegistFlg(writable, studentId);
		}
		catch(Exception e) {
		}
		finally{
			writable.close();
			writable = null;
        }
		return ret;
	}
	//20140617 ADD-S For　メモリ不足対応
	public static boolean DB_ClearRegistPrintSetData(String studentId, String kyokaId, String kyozaiId, String printSetId)
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			ret = TblResultData.DB_ClearRegistFlgPrintSet(writable, studentId, kyokaId, kyozaiId, printSetId);
		}
		catch(Exception e) {
		}
		finally{
			writable.close();
			writable = null;
        }
		return ret;
	}
	//20140617 ADD-E For　メモリ不足対応
	//20140718 ADD-S For Print単位送信
	public static boolean DB_ClearRegistPrintUnitData(String studentId, String kyokaId, String kyozaiId, String printUnitId)
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			ret = TblResultData.DB_ClearRegistFlgPrintUnit(writable, studentId, kyokaId, kyozaiId, printUnitId);
		}
		catch(Exception e) {
		}
		finally{
			writable.close();
			writable = null;
        }
		return ret;
	}
	//20140718 ADD-E For Print単位送信
	public static ArrayList<DKyozaiPrintSet> DB_GetKyozaiDataExistList(String studentId)
	{
		ArrayList<DKyozaiPrintSet>	kyozaiPrintSetList = new ArrayList<DKyozaiPrintSet>();
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
		try {
	    	ArrayList<DResultData> resultList = TblResultData.DB_GetResultList(readable, studentId, "", "");
			
    		DKyozaiPrintSet kyozaiPrintSet = null;
    		String oldKyozaiID = ""; 
	    	for (DResultData resultData : resultList)
	    	{
	    		if(oldKyozaiID.isEmpty()) {
	    			oldKyozaiID = resultData.mKyozaiID;
	    			kyozaiPrintSet = new DKyozaiPrintSet(resultData.mKyokaID, resultData.mKyokaName, resultData.mKyozaiID, resultData.mKyozaiName);
		    		//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
		    		kyozaiPrintSet.mKyozaiOderNo = resultData.mKyozaiOrderNo;
		    		//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告
	    		}
	    		if(oldKyozaiID.equals(resultData.mKyozaiID) == false ) {
	    			kyozaiPrintSet.DataStatusCheck();
	    			kyozaiPrintSetList.add(kyozaiPrintSet);
	    			
	    			oldKyozaiID = resultData.mKyozaiID;
	    			kyozaiPrintSet = new DKyozaiPrintSet(resultData.mKyokaID, resultData.mKyokaName, resultData.mKyozaiID, resultData.mKyozaiName);
		    		//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
		    		kyozaiPrintSet.mKyozaiOderNo = resultData.mKyozaiOrderNo;
		    		//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告
	    		}
	    		kyozaiPrintSet.mResultList.add(resultData);
	    	}
	    	if(kyozaiPrintSet != null) {
    			kyozaiPrintSet.DataStatusCheck();
	    		kyozaiPrintSetList.add(kyozaiPrintSet);
	    	}
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return kyozaiPrintSetList;
	}
	public static DKyozaiPrintSet DB_GetKyozaiDataExistList_Kyozai(String studentId, String kyokaId,  String kyozaiId)
	{
		DKyozaiPrintSet kyozaiPrintSet = null;
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
		try {
	    	ArrayList<DResultData> resultList = TblResultData.DB_GetResultList(readable, studentId, kyokaId, kyozaiId);
	    	if(resultList.size() > 0) {
	    		kyozaiPrintSet = new DKyozaiPrintSet(kyokaId, resultList.get(0).mKyokaName, kyozaiId, resultList.get(0).mKyozaiName);
	    		//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
	    		kyozaiPrintSet.mKyozaiOderNo = resultList.get(0).mKyozaiOrderNo;
	    		//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告
		    	for (DResultData resultData : resultList) {
		    		kyozaiPrintSet.mResultList.add(resultData);
		    	}
    			kyozaiPrintSet.DataStatusCheck();
	    	}
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		return kyozaiPrintSet;
	}
	
	//20140617 ADD-S For　メモリ不足対応
    public static ArrayList<DResultData> DB_GetPrintSetIDListForRegist(String studentId)
    {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
		ArrayList<DResultData> registDataList = null;
		try {
			registDataList = TblResultData.DB_GetPrintSetIDListForRegist(readable, studentId);
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		return registDataList;
	}
	public static ArrayList<DResultData> DB_GetRejistPrintSet(String studentId, String kyokaId, String kyozaiId, String printSetId) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> resultList = null;
		try {
	    	resultList = TblResultData.DB_GetRejistPrintSet(readable, studentId, kyokaId, kyozaiId, printSetId);
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		return resultList;
	}
	public static boolean DB_ClearStudentData(String studentId)
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		boolean ret = false;
		try {
			writable.beginTransaction();

			ret = TblResultData.DB_DeleteByStudentID(writable, studentId);
			if(ret) {
				writable.setTransactionSuccessful();
			}
		}
		catch(Exception e) {}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
        }

		return ret;
		
	}
	//20140617 ADD-E For　メモリ不足対応
	
	//20140731 ADD-S For 学習済み教材の表示は,PrintSet単位でＤＢ参照
	public static ArrayList<DResultData> DB_GetInkDataByPrintSetID(ArrayList<DResultData> resultDataList, String printSetId){
		
		SQLiteDatabase readableDb = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
		
		for (DResultData resultData : resultDataList) {
			if(resultData.mPrintSetID.equalsIgnoreCase(printSetId)) {
				//インクデータ取得
				DResultData resultInkData = TblInkData.DB_GetInkData(readableDb, resultData.mStudentID, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				resultData.mInkData = resultInkData.mInkData;
				resultData.mInkBinary = resultInkData.mInkBinary;
				
		    	//問題データ取得
				resultData.mQuestion = DB_GetPrint(resultData.mPrintID);
			}
		}
		return resultDataList;
	}
	//問題データ取得
	public static MQuestion2 DB_GetPrint(String printId)
	{
    	MQuestion2 question = null;
    	question = MastDBIO.DB_GetPrint(printId); 
		return question;
	}

	//20140731 ADD-E For 学習済み教材の表示は,PrintSet単位でＤＢ参照
    //20150409 ADD-S For 2015年度Ver. 未読コメント
    public static boolean ExistUnreadData(String studentId)
    {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	boolean exist =	TblResultData.ExistUnreadData(readable, studentId) ;
		return exist;
    }
    
	public static ArrayList<DResultData> DB_GetUnreadData(String studentId) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<DResultData> resultlist = new ArrayList<DResultData>();
		try {
			resultlist = TblResultData.DB_GetUnreadData(readable, studentId);
	    	//問題データ取得
	    	resultlist = DB_GetPrint(resultlist);
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		return resultlist;
	}
	public static boolean DB_UpdateReadCommentData(DResultData resultData) 
	{
		SQLiteDatabase writable = mDataSQLHelper.getWritableDatabase(mDataSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			writable.beginTransaction();
			ret = TblResultData.DB_UpdateunreadCommentFlg(writable, resultData);
			TblReadCommentData.DB_Insert(writable, resultData);
			
			//20150422 MOD-S Web参照時はDB_UpdateunreadCommentFlgでFalseになるが、
			//DB_Insertを実行する為、必ずCommitする
			/***
			if(ret) {
				writable.setTransactionSuccessful();
			***/
			writable.setTransactionSuccessful();
			//20150422 MOD-E
			
		}
		catch(Exception e) {}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
        }

		return ret;
	}
	public static ArrayList<String> DB_GetReadCommentDataList(String studentId) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	ArrayList<String> list = new ArrayList<String>();
		try {
			list = TblReadCommentData.DB_GetReadCommentDataList(readable, studentId);
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		return list;
	}
	public static boolean DB_ClearReadCommentDataList(String studentId) {
		SQLiteDatabase readable = mDataSQLHelper.getReadableDatabase(mDataSQLHelper.GetKey256()); 
    	boolean stat = false;
		try {
			stat = TblReadCommentData.DB_DeleteByStudentID(readable, studentId);
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		return stat;
	}
	
    //20150409 ADD-E For 2015年度Ver. 未読コメント
	

}
