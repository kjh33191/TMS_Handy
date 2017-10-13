package kumon2014.database.master;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import kumon2014.common.KumonCommon;

import kumon2014.kumondata.DResultData;
import kumon2014.kumondata.KyozaiName;

import android.content.Context;

public class MastDBIO {
	private static MastSQLHelper   mMastSQLHelper = null;

	public static void Open(Context con) 
	{
		mMastSQLHelper = new MastSQLHelper(con); 

		// check for new password
		boolean isOldPass = false;
		File file = new File(MastSQLHelper.DATABASE_NAME);
		if (file.exists()) {
			try {
				SQLiteDatabase db = SQLiteDatabase.openDatabase(MastSQLHelper.DATABASE_NAME,
						mMastSQLHelper.GetKey256().toCharArray(), null, SQLiteDatabase.OPEN_READONLY);
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
				SQLiteDatabase db = SQLiteDatabase.openDatabase(MastSQLHelper.DATABASE_NAME,
						mMastSQLHelper.GetKey().toCharArray(), null, SQLiteDatabase.OPEN_READWRITE);
				db.changePassword(mMastSQLHelper.GetKey256());
				db.close();
			}
			catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}
	public static void Close() 
	{
		mMastSQLHelper.close();
	}
	
	//All Clear
	public static boolean DB_AllClear()
	{
		SQLiteDatabase writable = mMastSQLHelper.getWritableDatabase(mMastSQLHelper.GetKey256()); 
		boolean ret = false;
		try {
			writable.beginTransaction();

			ret = MKyozai.DB_ClearAll(writable);
			ret = MQuestion2.DB_ClearAll(writable);
			ret = MQuestionImage.DB_ClearAll(writable);
			ret = MQuestionSound.DB_ClearAll(writable);
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
	//Clear Question
	public static boolean DB_ClearQuestion()
	{
		SQLiteDatabase writable = mMastSQLHelper.getWritableDatabase(mMastSQLHelper.GetKey256()); 
		boolean ret = false;
		try {
			writable.beginTransaction();
			ret = MQuestion2.DB_ClearAll(writable);
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

	/**
	 * ���̎擾
	 * @param printid �v�����gID
	 * @return ���
	 */
	public static MQuestion2 DB_GetPrint(String printid) 
	{
		SQLiteDatabase readable = mMastSQLHelper.getReadableDatabase(mMastSQLHelper.GetKey256()); 
    	MQuestion2 question = null;
    	try {
    		question = MQuestion2.DB_GetPrint(readable, printid); 
    	}
    	catch(Exception unused) {}
		finally{
			readable.close();
        }
    	return question;
	}
	/**
	 * ���ꗗ�̎擾
	 * @param printids �v�����gID�ꗗ
	 * @return ���ꗗ
	 */
	public static HashMap<String, MQuestion2> DB_GetPrints(ArrayList<String> printids) 
	{
		SQLiteDatabase readable = mMastSQLHelper.getReadableDatabase(mMastSQLHelper.GetKey256());
		HashMap<String, MQuestion2> questions = new HashMap<String, MQuestion2>();
    	try {
    		for (String printid : printids) {
    			MQuestion2 question = MQuestion2.DB_GetPrint(readable, printid);
    			questions.put(printid, question);
    		}
    	}
    	catch(Exception unused) {}
		finally{
			readable.close();
        }
    	return questions;
	}
	public static DResultData DB_GetPrintByPrintNo(String kyoka, String kyozai, int printno) 
	{
		SQLiteDatabase readable = mMastSQLHelper.getReadableDatabase(mMastSQLHelper.GetKey256()); 
    	MQuestion2 question = null;
    	try {
    		question = MQuestion2.DB_GetPrintByPrintNo(readable, kyoka, kyozai, printno); 
    	}
    	catch(Exception unused) {
    		question = null;
    	}
		finally{
			readable.close();
        }
    	if(question == null) {
    		return null;
    	}
    	DResultData resultdata = new DResultData();
    	resultdata.mQuestion = question;
    	
    	return resultdata;
	}
	public static boolean DB_InsertQuestionDataList(SQLiteDatabase writable, ArrayList<MQuestion2> questionlist) throws Exception
	{
		boolean ret = false;
		try {
			ret = MQuestion2.DB_InserQuestionDataList(writable, questionlist);
		}
		catch(Exception e) 
		{
			throw e; 
		}
		
		return ret;
	}

	public static boolean DB_InsertQuestionDataList(ArrayList<MQuestion2> questionlist) 
	{
		boolean ret = false;
		SQLiteDatabase writable = mMastSQLHelper.getWritableDatabase(mMastSQLHelper.GetKey256());
		writable.beginTransaction();
		try {
			ret = DB_InsertQuestionDataList(writable, questionlist);
        	writable.setTransactionSuccessful();
		}
		catch(Exception e) 
		{
		}
        finally{
        	writable.endTransaction();
        	writable.close();
        	writable = null;
        }
		
		return ret;
	}

	public static boolean DB_InsertQuestionData(SQLiteDatabase writable, MQuestion2 question) 
	{
		
		boolean ret = false;
		try {
			question = KumonCommon.DecompressQuestion(question);
			ret = MQuestion2.DB_InserQuestionData(writable, question);
		}
		catch(Exception unused){}
        finally{
        }
		return ret;
	}
	public static boolean DB_InsertQuestionData(MQuestion2 question) 
	{
		
		boolean ret = false;
		SQLiteDatabase writable = mMastSQLHelper.getWritableDatabase(mMastSQLHelper.GetKey256()); 
		try {
			ret = DB_InsertQuestionData(writable, question);
		}
		catch(Exception unused) {}
        finally{
        	writable.close();
        	writable = null;
        }
		return ret;
	}
	
	public static SQLiteDatabase DB_GetReadable()
	{
		return mMastSQLHelper.getReadableDatabase(mMastSQLHelper.GetKey256()); 
	}
	
	/**
	 * ��肪�ۑ��ς݂�����
	 * @param printid ���ID
	 * @return ����
	 */
	public static boolean DB_IsExistQuestionData(String printid)
	{
		boolean ret = false;
		SQLiteDatabase readable = mMastSQLHelper.getReadableDatabase(mMastSQLHelper.GetKey256()); 
		try {
	        //20150121 MOD-S For 2015�N�xVer. ���ލX�V
			//ret = MQuestion2.DB_IsExistQuestion(readable, printid);
			//Web�Q�Ǝ���,�����̖��f�[�^������΂������D��
			ret = MQuestion2.DB_IsExistQuestion(readable, printid, -1, null);
	        //20150121 MOD-E For 2015�N�xVer. ���ލX�V
		}
		catch(Exception unused) {}
        finally{
        	readable.close();
        	readable = null;
        }
		return ret;
	}

	/**
	 * ���Q�����݂��邩����
	 * @param ids printid�̔z��
	 * @return printid�Ƒ��ۂ̃}�b�v
	 */
	public static HashMap<String, Boolean> DB_IsExistQuestionData(ArrayList<String> ids) {
		HashMap<String, Boolean> retMap = new HashMap<String, Boolean>();
		SQLiteDatabase readable = mMastSQLHelper.getReadableDatabase(mMastSQLHelper.GetKey256());
		try {
			for (String printid : ids) {
				boolean ret = MQuestion2.DB_IsExistQuestion(readable, printid, -1, null);
				retMap.put(printid, ret);
			}
		}
		catch(Exception unused) {}
		finally {
			readable.close();
		}
		return retMap;
	}
	//20130403 ADD-S For SpeedUp
    //20150121 MOD-S For 2015�N�xVer. ���ލX�V
	//public static boolean DB_IsExistQuestionData(SQLiteDatabase readable, String printid)
	public static boolean DB_IsExistQuestionData(SQLiteDatabase readable, String printid, int leraningcount, Date printupdatetime)
    //20150121 MOD-E For 2015�N�xVer. ���ލX�V
	{
		boolean ret = false;
		try {
		    //20150121 MOD-S For 2015�N�xVer. ���ލX�V
			ret = MQuestion2.DB_IsExistQuestion(readable, printid, leraningcount, printupdatetime);
		    //20150121 MOD-E For 2015�N�xVer. ���ލX�V
		}
		catch(Exception unused) {}
		return ret;
	}
	//20130403 ADD-E
	public static boolean DB_IsExistQuestionDataByNo(SQLiteDatabase readable, String kyokaid, String kyozaiid, int printno )
	{
		boolean ret = false;
		try {
			ret = MQuestion2.DB_IsExistQuestionByNo(readable, kyokaid, kyozaiid, printno);
		}
		catch(Exception unused) {}
		return ret;
	}
	public static ArrayList<KyozaiName> GetKyozaiList(ArrayList<KyozaiName> namelist) 
	{
		ArrayList<KyozaiName> list = null;
		SQLiteDatabase readable = mMastSQLHelper.getReadableDatabase(mMastSQLHelper.GetKey256()); 
		try {
			list = MKyozai.GetKyozaiList(readable, namelist);
		}
		catch(Exception unused) {}
        finally{
        	readable.close();
        	readable = null;
        }
		return list;
	}
	public static boolean AddKyozaiList(ArrayList<MKyozai> kyozailist) 
	{
		SQLiteDatabase writable = mMastSQLHelper.getWritableDatabase(mMastSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			writable.beginTransaction();

			MKyozai.DB_ClearAll(writable);
			ret = MKyozai.DB_AddKyozai(writable, kyozailist);
			
			if(ret) {
				writable.setTransactionSuccessful();
			}
		}
		catch(Exception unused) {}
		finally{
			writable.endTransaction();
			writable.close();
			writable = null;
        }

		return ret;
	}
	
	//20150126 ADD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
	public static ArrayList<DResultData> DB_GetRefPrint(String kyoka, String kyozai, int reffrom, int refto) 
	{
		ArrayList<DResultData> resultlist = new ArrayList<DResultData>();
		SQLiteDatabase readable = mMastSQLHelper.getReadableDatabase(mMastSQLHelper.GetKey256()); 
    	MQuestion2 question = null;
    	try {
    		for(int i = reffrom; i <= refto; i++) {
        		question = MQuestion2.DB_GetPrintByPrintNo(readable, kyoka, kyozai, i); 
            	if(question == null) {
            		resultlist = null;
            		break;
            	}
            	else {
                	DResultData resultdata = new DResultData();
                	resultdata.mQuestion = question;
                	resultdata.mPrintNo = i;
                	resultlist.add(resultdata);
            	}
    		}
    	}
    	catch(Exception e) {
    		question = null;
    	}
		finally{
			readable.close();
			readable = null;
        }
    	
    	return resultlist;
	}
	//20150126 ADD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷

	public static class WriteDBAccessor {
		
		SQLiteDatabase mWritable;
		public WriteDBAccessor() {
			mWritable = mMastSQLHelper.getWritableDatabase(mMastSQLHelper.GetKey256());
			mWritable.beginTransaction();
		}
		public SQLiteDatabase getWritable() {
			return mWritable;
		}
		public void Close(boolean fSuccess) {
			if (fSuccess) {
				mWritable.setTransactionSuccessful();
			}
			mWritable.endTransaction();
			mWritable.close();
			mWritable = null;
		}
	}
}
