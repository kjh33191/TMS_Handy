package kumon2014.database.log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SLog {
	private static LogSQLHelper   mLogSQLHelper = null;
	
	public static final int SF_LOG_INFORMATION = 0;
	public static final int SF_LOG_WARNING = 1;
	public static final int SF_LOG_ERROR = 2;
	
	// テーブル名 
	public static final String SF_TBLNAME = "S_Log";
	// カラム 
	public static final String SF_COL_No 			= "CNo";					//integer (autoincrement) 
	public static final String SF_COL_Date 			= "CDate";					//Text 
	public static final String SF_COL_Level 		= "CLevel";					//integer 
	public static final String SF_COL_SOURCE 		= "CSource";				//Text 
	public static final String SF_COL_TRACE 		= "CTrace";					//Text 
	public static final String SF_COL_Message 		= "CMessager";				//Text 
	//Index
	public static final int SF_IDX_No 				= 0;
	public static final int SF_IDX_Date 			= 1;
	public static final int SF_IDX_Level 			= 2;
	public static final int SF_IDX_SOURCE 			= 3;
	public static final int SF_IDX_TRACE 			= 4;
	public static final int SF_IDX_Message 			= 5;
	
	
	public static final String SF_CREATE_TBL_SQL_KYOZAI =
				"create table " + SF_TBLNAME + "( " 
						+ SF_COL_No + 				" integer not null, "
						+ SF_COL_Date + 			" text, "
						+ SF_COL_Level + 			" integer, "
						+ SF_COL_SOURCE + 			" text, "
						+ SF_COL_TRACE + 			" text, "
						+ SF_COL_Message + 			" text, "
						+ " primary key( " + SF_COL_No  + " autoincrement )"
						+ " );";

	///////////////////////////////////////////////////////////////////////////////////
	public int		mNo = 0;
	public String	mDate = "";
	public int		mLevel = 0;
	public String	mSource = "";
	public String	mTrace = "";
	public String	mMessage = "";
	
	public SLog()
	{
	}
	
	public static void Open(Context con) 
	{
		mLogSQLHelper = new LogSQLHelper(con); 

		// check for new password
		boolean isOldPass = false;
		File file = new File(LogSQLHelper.DATABASE_NAME);
		if (file.exists()) {
			try {
				SQLiteDatabase db = SQLiteDatabase.openDatabase(LogSQLHelper.DATABASE_NAME,
						mLogSQLHelper.GetKey256().toCharArray(), null, SQLiteDatabase.OPEN_READONLY);
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
				SQLiteDatabase db = SQLiteDatabase.openDatabase(LogSQLHelper.DATABASE_NAME,
						mLogSQLHelper.GetKey().toCharArray(), null, SQLiteDatabase.OPEN_READWRITE);
				db.changePassword(mLogSQLHelper.GetKey256());
				db.close();
			}
			catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}
	public static void Close(Context con) 
	{
		mLogSQLHelper.close();
	}

	//クリアALL
	public static boolean DB_ClearAll() 
	{
		SQLiteDatabase writable = mLogSQLHelper.getWritableDatabase(mLogSQLHelper.GetKey256()); 
		
		boolean ret = false;
		try {
			writable.delete(SF_TBLNAME, null, null);		
			ret = true;
		}
		catch(Exception e)
		{
		}
		finally{
			writable.close();
			writable = null;
        }
		
		return ret;
	}
	
	//
	public static ArrayList<SLog> DB_GetLogList(int limit) 
	{
		SQLiteDatabase readable = mLogSQLHelper.getReadableDatabase(mLogSQLHelper.GetKey256()); 
		
		ArrayList<SLog>  loglist = new ArrayList<SLog>();
        Cursor cursor = null;
        try{
            cursor = readable.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
                    null,		//全項目
            		//条件式
                    null, 
            		//条件
                    null,  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    SF_COL_Date,
                    //limit
                    String.valueOf(limit)
                    );

            loglist =  DB_GetLogList_readCursor( cursor );
        }
        catch(Exception e) {
//			String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
            readable.close();
        }
        
        return loglist;
	}
	
	public static ArrayList<SLog> DB_GetLogList(int lebel, Date fromdate, Date todate) 
	{
		SQLiteDatabase readable = mLogSQLHelper.getReadableDatabase(mLogSQLHelper.GetKey256()); 
		
		ArrayList<SLog>  loglist = new ArrayList<SLog>();
		String fromstring = getFromDate(fromdate);
		String tostring = getToDate(fromdate);
		
		String[] cond = new String[]{ Integer.toString(lebel), fromstring, tostring };
		
		String where = " " + SF_COL_Level + " >= ? ";
		if(fromstring.isEmpty()) {
			where += " AND " + SF_COL_Date + " != ? ";
		}
		else {
			where += " AND " + SF_COL_Date + " >= ? ";
		}
		if(tostring.isEmpty()) {
			where += " AND " + SF_COL_Date + " != ? ";
		}
		else {
			where += " AND " + SF_COL_Date + " <= ? ";
		}
		
        Cursor cursor = null;
        try{
            cursor = readable.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
                    null,		//全項目
            		//条件式
                    where, 
            		//条件
                    cond,  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    SF_COL_No,
                    //limit
                    null
                    );

            loglist =  DB_GetLogList_readCursor( cursor );
        }
        catch(Exception e) {
//			String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
            readable.close();
            readable = null;
        }
        
        return loglist;
	}
	public static boolean DB_AddInfomationMessage(String message) 
	{
		SLog log = new SLog();
		log.mMessage = message;
		return DB_AddInfomation(log);
	}
	public static boolean DB_AddInfomation(SLog log) 
	{
		log.mLevel = SLog.SF_LOG_INFORMATION;
		log.mDate = getaddDate(new Date());
		return DB_AddLog(log);
	}
	public static boolean DB_AddWarning(SLog log) 
	{
		log.mLevel = SLog.SF_LOG_WARNING;
		log.mDate = getaddDate(new Date());
		return DB_AddLog(log);
	}
	public static boolean DB_AddError(SLog log) 
	{
		log.mLevel = SLog.SF_LOG_ERROR;
		log.mDate = getaddDate(new Date());
		return DB_AddLog(log);
	}
	public static boolean DB_AddException(Exception e) 
	{
		SLog log = new SLog();
		
		log.mLevel = SLog.SF_LOG_ERROR;
		log.mDate = getaddDate(new Date());
		log.mSource = e.getStackTrace()[0].getClassName() + "/" +e.getStackTrace()[0].getMethodName();
		log.mMessage = e.getMessage();
		
		return DB_AddLog(log);
	}
	
	private static boolean DB_AddLog(SLog log) 
	{

		SQLiteDatabase writable = mLogSQLHelper.getWritableDatabase(mLogSQLHelper.GetKey256()); 
		
		boolean ret = false;
        try{
        	ContentValues values = new ContentValues(); 
        	
        	values.put(SF_COL_Date, log.mDate); 
        	values.put(SF_COL_Level, log.mLevel); 
        	values.put(SF_COL_SOURCE, log.mSource); 
        	values.put(SF_COL_TRACE, log.mTrace); 
        	values.put(SF_COL_Message, log.mMessage); 
        		
        	if(writable.insert(SF_TBLNAME, null, values) >= 0) {
        		ret = true;
        	}
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
        	writable.close();
        	writable = null;
        }
        
        return ret;
	}
	private static ArrayList<SLog> DB_GetLogList_readCursor(Cursor cursor)
	{
		ArrayList<SLog>  loglist = new ArrayList<SLog>();
		
        while( cursor.moveToNext() ){
        	SLog log = new SLog();
        	log.mNo = cursor.getInt( SLog.SF_IDX_No );
        	log.mDate = cursor.getString( SLog.SF_IDX_Date );
        	log.mLevel = cursor.getInt( SLog.SF_IDX_Level );
        	log.mSource = cursor.getString( SLog.SF_IDX_SOURCE );
        	log.mTrace = cursor.getString( SLog.SF_IDX_TRACE );
        	log.mMessage = cursor.getString( SLog.SF_IDX_Message );
        	
        	loglist.add(log);
        }
        return loglist;
    }
	
	private static String getFromDate(Date date)
	{
		if(date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.JAPAN);
		return sdf.format(date);
	}
	private static String getToDate(Date date)
	{
		if(date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 99:99:99", Locale.JAPAN);
		return sdf.format(date);
	}
	private static String getaddDate(Date date)
	{
		if(date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPAN);
		return sdf.format(date);
	}

}
