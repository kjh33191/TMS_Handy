package kumon2014.database.data;


import java.io.File;

import kumon2014.common.Utility;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

public class TempDataDBIO {
	private static TempDataSQLHelper   mTempDataSQLHelper = null;

	public static void Open(Context con)
	{
		mTempDataSQLHelper = new TempDataSQLHelper(con);

		// check for new password
		boolean isOldPass = false;
		File file = new File(TempDataSQLHelper.DATABASE_NAME);
		if (file.exists()) {
			try {
				SQLiteDatabase db = SQLiteDatabase.openDatabase(TempDataSQLHelper.DATABASE_NAME,
						mTempDataSQLHelper.GetKey256().toCharArray(), null, SQLiteDatabase.OPEN_READONLY);
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
				SQLiteDatabase db = SQLiteDatabase.openDatabase(TempDataSQLHelper.DATABASE_NAME,
						mTempDataSQLHelper.GetKey().toCharArray(), null, SQLiteDatabase.OPEN_READWRITE);
				db.changePassword(mTempDataSQLHelper.GetKey256());
				db.close();
			}
			catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}
	public static void Close()
	{
		mTempDataSQLHelper.close();
	}

	//All Clear
	public static boolean DB_AllClear()
	{
		SQLiteDatabase writable = mTempDataSQLHelper.getWritableDatabase(mTempDataSQLHelper.GetKey256());
		boolean ret = false;
		try {
			writable.beginTransaction();

			ret = TblResultData.DB_ClearAll(writable, false);
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
	//Copy From DataDB
	public static boolean DB_CopyFrom(String studentId)
	{
		SQLiteDatabase db = mTempDataSQLHelper.getWritableDatabase(mTempDataSQLHelper.GetKey256());
		try {

			String mainPath = DataSQLHelper.DATABASE_NAME;
			db.execSQL("ATTACH DATABASE '" + mainPath + "' AS MAIN_DB KEY '" + Utility.EscapeString(mTempDataSQLHelper.GetKey256()) + "'");

			// トランザクション開始
			db.beginTransaction();
			db.execSQL("INSERT INTO " + TblResultData.SF_TBLNAME + " SELECT * FROM MAIN_DB." + TblResultData.SF_TBLNAME + " WHERE " + TblResultData.SF_COL_STUDENTID + " = '" + studentId + "'; ");
			db.execSQL("INSERT INTO " + TblInkData.SF_TBLNAME + " SELECT * FROM MAIN_DB." + TblInkData.SF_TBLNAME + " WHERE " + TblInkData.SF_COL_STUDENTID + " = '" + studentId + "'; ");
			db.execSQL("INSERT INTO " + TblGradingResultData.SF_TBLNAME + " SELECT * FROM MAIN_DB." + TblGradingResultData.SF_TBLNAME + " WHERE " + TblGradingResultData.SF_COL_STUDENTID + " = '" + studentId + "'; ");


			// コミット
			db.setTransactionSuccessful();

		} catch (Exception e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}
	//Copy To DataDB
	public static boolean DB_CopyTo(String studentId)
	{
		SQLiteDatabase db = mTempDataSQLHelper.getWritableDatabase(mTempDataSQLHelper.GetKey256());
		try {
			String mainPath = DataSQLHelper.DATABASE_NAME;
			db.execSQL("AttAch database '" + mainPath + "' as MAIN_DB KEY '" + Utility.EscapeString(mTempDataSQLHelper.GetKey256()) + "'");

			// トランザクション開始
			db.beginTransaction();
			db.execSQL("INSERT INTO MAIN_DB." + TblResultData.SF_TBLNAME + " SELECT * FROM " + TblResultData.SF_TBLNAME + " WHERE " + TblResultData.SF_COL_STUDENTID + " = '" + studentId + "'; ");
			db.execSQL("INSERT INTO MAIN_DB." + TblInkData.SF_TBLNAME + " SELECT * FROM " + TblInkData.SF_TBLNAME + " WHERE " + TblInkData.SF_COL_STUDENTID + " = '" + studentId + "'; ");
			db.execSQL("INSERT INTO MAIN_DB." + TblGradingResultData.SF_TBLNAME + " SELECT * FROM " + TblGradingResultData.SF_TBLNAME + " WHERE " + TblGradingResultData.SF_COL_STUDENTID + " = '" + studentId + "'; ");

			// コミット
			db.setTransactionSuccessful();
		} catch (Exception e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}
}
