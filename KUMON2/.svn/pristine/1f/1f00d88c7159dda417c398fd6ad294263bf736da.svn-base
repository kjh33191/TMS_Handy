package kumon2014.database.data;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;

import kumon2014.kumondata.DStudent;
import android.content.ContentValues;
import android.database.Cursor;

//学習者テーブル
public class TBlStudent {
	// テーブル名 
	public static final String SF_TBLNAME = "D_Student";
	// カラム 
	//学習者代表ID
	public static final String SF_COL_STUDENTID			= "CStudentID";			//Text 
	//パスワード
	public static final String SF_COL_PASSWORD 			= "CPassWord";			//Text 
    //セッションID
	public static final String SF_COL_SESSIONID 		= "CSessionID";			//Text 
    //学習者管理ID
	public static final String SF_COL_STUDENTADMINID 	= "CStudentAdminID";	//Text 
	//名前
	public static final String SF_COL_NAME 				= "CName";				//Text 
	//名前カナ
	public static final String SF_COL_NAMEKANA 			= "CNameKana";			//Text 
	//えんぴつの太さ
	public static final String SF_COL_PENTHICKNESS 		= "CPenThickness";		//int 
	
	//Index
	public static final int SF_IDX_STUDENT 	= 0;
	public static final int SF_IDX_PASSWORD	= 1;
	public static final int SF_IDX_SESSIONID = 2;
	public static final int SF_IDX_STUDENTADMINID = 3;
	public static final int SF_IDX_NAME = 4;
	public static final int SF_IDX_NAMEKANA = 5;
	public static final int SF_IDX_PENTHICKNESS = 6;
	
	
	public static final String SF_CREATE_TBL_SQL_STUDENT =
				"create table " + SF_TBLNAME + "( " 
						+ SF_COL_STUDENTID + 		" text primary key not null, "
						+ SF_COL_PASSWORD + 		" text, "
						+ SF_COL_SESSIONID + 		" text, "
						+ SF_COL_STUDENTADMINID + 	" text, "
						+ SF_COL_NAME + 			" text, "
						+ SF_COL_NAMEKANA + 		" text, "
						+ SF_COL_PENTHICKNESS + 	" integer "
						+ " );";

	///////////////////////////////////////////////////////////////////////////////////
	//クリアALL
	protected static boolean DB_ClearAll(SQLiteDatabase writableDb) 
	{
		boolean ret = false;
		try {
			writableDb.delete(SF_TBLNAME, null, null);		
			ret = true;
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//Delete StudentID
	protected static boolean DB_DeleteByStudentID(SQLiteDatabase writableDb, String studentId) 
	{
		boolean ret = false;
		try {
			if(writableDb.delete(SF_TBLNAME, 
								SF_COL_STUDENTID + " = ?",
								new String[]{ studentId }) >= 0) {
				ret = true;
			}
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//Insert Student
	protected static boolean DB_InsertStudent(SQLiteDatabase writableDb, DStudent student) 
	{
		boolean ret = false;
		try{
			ContentValues values = new ContentValues(); 
	      	values.put(SF_COL_STUDENTID, student.mStudentID); 
	      	values.put(SF_COL_PASSWORD, student.mPassword); 
	      	values.put(SF_COL_SESSIONID, student.mSessionID); 
	      	values.put(SF_COL_STUDENTADMINID, student.mStudentAdminID); 
	      	values.put(SF_COL_NAME, student.mName); 
	      	values.put(SF_COL_NAMEKANA, student.mNameKana); 
	      	values.put(SF_COL_PENTHICKNESS, student.mPenThickness); 
	      	
	      	if(writableDb.insert(SF_TBLNAME, null, values) >= 0) {
				ret = true;
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	//Update StudentAll
	protected static boolean DB_UpdateStudentAll(SQLiteDatabase writableDb, DStudent student) 
	{
		boolean ret = false;
		try{
			ContentValues values = new ContentValues(); 
	      	values.put(SF_COL_STUDENTID, student.mStudentID); 
	      	values.put(SF_COL_PASSWORD, student.mPassword); 
	      	values.put(SF_COL_SESSIONID, student.mSessionID); 
	      	values.put(SF_COL_STUDENTADMINID, student.mStudentAdminID); 
	      	values.put(SF_COL_NAME, student.mName); 
	      	values.put(SF_COL_NAMEKANA, student.mNameKana); 
	      	values.put(SF_COL_PENTHICKNESS, student.mPenThickness); 
	      	
			if(writableDb.update(SF_TBLNAME,
									values,
									SF_COL_STUDENTID + " = ?",
									new String[]{ student.mStudentID }) >= 0) {
				ret = true;
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	//Get All Student
	protected static ArrayList<DStudent> DB_GetAllStudent(SQLiteDatabase readableDb) 
	{
		ArrayList<DStudent> studentlist = null;
		Cursor cursor = null;
		try{
			cursor = readableDb.query( 
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
					null,
					//limit
					null
					);

			// 検索結果をcursorから読み込んで返す
			studentlist =  DB_GetAllStudent_readCursor( cursor );
		}
		catch(Exception e) {
		}
		finally{
			// Cursorを忘れずにcloseする
			if( cursor != null ){
				cursor.close();
			}
		}
      
		return studentlist;
	}
	
	//Get Student
	protected static DStudent DB_GetStudent(SQLiteDatabase readableDb, String studentId) 
	{
		DStudent student = null;
		Cursor cursor = null;
		try{
			cursor = readableDb.query( 
					//Table名
					SF_TBLNAME, 
					//項目名
					null,		//全項目
					//条件式
					SF_COL_STUDENTID + " = ?", 
					//条件
					new String[]{ studentId },  
					//group by
					null,
					//Having
					null, 
					//order by
					null,
					//limit
					null
					);

			// 検索結果をcursorから読み込んで返す
			student =  DB_GetStudent_readCursor( cursor );
		}
		catch(Exception e) {
		}
		finally{
			// Cursorを忘れずにcloseする
			if( cursor != null ){
				cursor.close();
			}
		}
      
		return student;
	}
	private static ArrayList<DStudent> DB_GetAllStudent_readCursor(Cursor cursor)
	{
		ArrayList<DStudent> studentlist = new ArrayList<DStudent> ();
        while( cursor.moveToNext() ){
        	DStudent student = new DStudent();
			student.mStudentID = cursor.getString(SF_IDX_STUDENT);
			student.mPassword = cursor.getString(SF_IDX_PASSWORD);
			student.mSessionID = cursor.getString(SF_IDX_SESSIONID);
			student.mStudentAdminID = cursor.getString(SF_IDX_STUDENTADMINID);
			student.mName = cursor.getString(SF_IDX_NAME);
			student.mNameKana = cursor.getString(SF_IDX_NAMEKANA);
			student.mPenThickness = cursor.getInt(SF_IDX_PENTHICKNESS);
			
			studentlist.add(student);
		}
		return studentlist;
	}
	private static DStudent DB_GetStudent_readCursor(Cursor cursor)
	{
		DStudent student = null;
		
		if(cursor.moveToNext()){
			student = new DStudent();
			student.mStudentID = cursor.getString(SF_IDX_STUDENT);
			student.mPassword = cursor.getString(SF_IDX_PASSWORD);
			student.mSessionID = cursor.getString(SF_IDX_SESSIONID);
			student.mStudentAdminID = cursor.getString(SF_IDX_STUDENTADMINID);
			student.mName = cursor.getString(SF_IDX_NAME);
			student.mNameKana = cursor.getString(SF_IDX_NAMEKANA);
			student.mPenThickness = cursor.getInt(SF_IDX_PENTHICKNESS);
		}
		return student;
	}
	///////////////////////////////////////////////////////////////////////////////////////
	//Update Student SessionID
	protected static boolean DB_UpdateStudentSessionID(SQLiteDatabase writableDb, String studentId, String sessionId)  
	{
		boolean ret = false;
		try{
			ContentValues values = new ContentValues(); 
	      	values.put(SF_COL_SESSIONID, sessionId); 
	      	
			if(writableDb.update(SF_TBLNAME,
									values,
									SF_COL_STUDENTID + " = ?",
									new String[]{ studentId }) >=0 ) {
				ret = true;
			}
		}
		catch(Exception e) {
		}
		
		return ret;
	}
	//Get Student SessionID
	protected static String DB_GetStudentSessionID(SQLiteDatabase readableDb, String studentId) 
	{
		String sessionid = "";
		Cursor cursor = null;
		try{
          cursor = readableDb.query( 
        		  //Table名
        		  SF_TBLNAME, 
                  //項目名
        		  new String[]{ SF_COL_SESSIONID},	
        		  //条件式
                  SF_COL_STUDENTID + " = ?", 
                  //条件
                  new String[]{ studentId },  
                  //group by
                  null,
                  //Having
                  null, 
                  //order by
                  null,
                  //limit
                  null
                  );

          // 検索結果をcursorから読み込んで返す
          sessionid = DB_GetStudentSessionID_readCursor( cursor );
      }
      catch(Exception e) {
      }
      finally{
          // Cursorを忘れずにcloseする
          if( cursor != null ){
              cursor.close();
          }
      }
      
      return sessionid;
	}
	private static String DB_GetStudentSessionID_readCursor(Cursor cursor)
	{
		String sessionid = "";
		if(cursor.moveToNext()){
			sessionid = cursor.getString(SF_IDX_SESSIONID);
		}
		return sessionid;
	}

}

