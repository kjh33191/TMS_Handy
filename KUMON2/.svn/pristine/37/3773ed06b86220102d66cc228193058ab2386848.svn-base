package kumon2014.database.data;


import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import android.content.Context; 

// SQLiteOpenHelper 
public class DataSQLHelper extends SQLiteOpenHelper { 
	
	//1=V1.9まで
	//private static final int DATABASE_VERSION = 1; 
	//20140521 DB変更 AnswertimeをREAL型にする為、CANSWERTIME2を追加し
	//データ移行。CANSWERTIMEは使用しない(V2.0～V2.1)
	//private static final int DATABASE_VERSION = 2; 
	//20140605  大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
	//V2.2,V2.3
	//private static final int DATABASE_VERSION = 3; 
	//V2.4以降
	//private static final int DATABASE_VERSION = 4; 
	//V10.1以降
	//private static final int DATABASE_VERSION = 5; 
	//V10.3以降 未読コメントFlg追加
	//private static final int DATABASE_VERSION = 6; 
	//V10.3以降 PrintSetDate追加
	private static final int DATABASE_VERSION = 7; 

	// データベス名 
	public static final String DATABASE_NAME = StudentClientCommData.getLocalDBFolder() +  "/DATA.db"; 
	// コンストラクタ
	public DataSQLHelper(Context context) 
	{ 
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	} 
	@Override 
	public void onCreate(SQLiteDatabase db) 
	{ 
		String sql;
		try {
			//Student
			sql = TBlStudent.SF_CREATE_TBL_SQL_STUDENT;
			db.execSQL(sql); 
			
			//Result Table
			//20140521 MOD-S 
			//sql = TblResultData.SF_CREATE_TBL_SQL_PRINTDATA;		//V1
		    //20150303 MOD-S For 2015年度Ver. 音声メモステータス
			//sql = TblResultData.SF_CREATE_TBL_SQL_RESULTDARA_V2;	//V2
		    //20150409 MOD-S For 2015年度Ver. 未読コメント
			//sql = TblResultData.SF_CREATE_TBL_SQL_RESULTDARA_V3;	//V3
	        //20150423 ADD-S For 2015年度Ver. 未読コメント
			//sql = TblResultData.SF_CREATE_TBL_SQL_RESULTDARA_V4;	//V4
			sql = TblResultData.SF_CREATE_TBL_SQL_RESULTDARA_V5;	//V4
	        //20150423 ADD-E For 2015年度Ver. 未読コメント
		    //20150409 MOD-E For 2015年度Ver. 未読コメント
		    //20150303 MOD-E For 2015年度Ver. 音声メモステータス
			//20140521 MOD-E 
			db.execSQL(sql); 
			
        	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			//InkData Table
			//20140607 MOD-S 
			//sql = TblInkData.SF_CREATE_TBL_SQL_INKDATA; 	//V2
			//20150416 MOD-S InkData To Binary
			//sql = TblInkData.SF_CREATE_TBL_SQL_INKDATA_V4;		//V4
			sql = TblInkData.SF_CREATE_TBL_SQL_INKDATA_V6;		//V6
			//20150416 MOD-E InkData To Binary
			//20140607 MOD-E 
			db.execSQL(sql); 

			//GradingResultData Table
			sql = TblGradingResultData.SF_CREATE_TBL_SQL_GRADINGRESULT;
			db.execSQL(sql); 
        	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		
		    //20150409 ADD-S For 2015年度Ver. 未読コメント
			sql = TblReadCommentData.SF_CREATE_TBL_SQL_REDCOMMENTDATA;
			db.execSQL(sql); 
		    //20150409 ADD-E For 2015年度Ver. 未読コメント
			
		}
		catch(Exception e) {
//			String s = e.getMessage();
		}
	} 
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{ 
		String sql;
		
		if( oldVersion == 1 && newVersion == 4 ){
			//CANSWERTIME2を追加
			db.execSQL(
					"alter table " + TblResultData.SF_TBLNAME +
					" add " + TblResultData.SF_COL_ANSWERTIME2 + " REAL default 0 ");
			
			//CANSWERTIME -> CANSWERTIME2
			db.execSQL(
					"UPDATE " + TblResultData.SF_TBLNAME +
					" SET " + TblResultData.SF_COL_ANSWERTIME2 + " = " + TblResultData.SF_COL_ANSWERTIME);
			
		}
		if((oldVersion == 1 || oldVersion == 2) && newVersion == 4) {
			//InkData Table
			sql = TblInkData.SF_CREATE_TBL_SQL_INKDATA_V4;
			db.execSQL(sql); 

			//GradingResultData Table
			sql = TblGradingResultData.SF_CREATE_TBL_SQL_GRADINGRESULT;
			db.execSQL(sql); 
			
			db.execSQL("INSERT INTO " + TblInkData.SF_TBLNAME + "( "
					+ TblInkData.SF_COL_STUDENTID + " , " 
					+ TblInkData.SF_COL_KYOKAID + " , "
					+ TblInkData.SF_COL_KYOZAIID + " , "
					+ TblInkData.SF_COL_PRINTUNITID + " , "
					+ TblInkData.SF_COL_COUNT + " , " 
					+TblInkData.SF_COL_INKDATA + " )" 
					+ " SELECT "  
							+ TblResultData.SF_COL_STUDENTID + " , "
							+ TblResultData.SF_COL_KYOKAID + " , "
							+ TblResultData.SF_COL_KYOZAIID + " , "
							+ TblResultData.SF_COL_PRINTUNITID + " , "
							+ TblResultData.SF_COL_COUNT + " , "
							+ TblResultData.SF_COL_INKDATA +  "  "
							+ " FROM " + TblResultData.SF_TBLNAME);

			db.execSQL("INSERT INTO " + TblGradingResultData.SF_TBLNAME + "( "
					+ TblGradingResultData.SF_COL_STUDENTID + " , " 
					+ TblGradingResultData.SF_COL_KYOKAID + " , "
					+ TblGradingResultData.SF_COL_KYOZAIID + " , "
					+ TblGradingResultData.SF_COL_PRINTUNITID + " , "
					+ TblGradingResultData.SF_COL_COUNT + " , " 
					+ TblGradingResultData.SF_COL_GRADINGRESULTDATA + " )" 
					+ " SELECT "  
							+ TblResultData.SF_COL_STUDENTID + " , "
							+ TblResultData.SF_COL_KYOKAID + " , "
							+ TblResultData.SF_COL_KYOZAIID + " , "
							+ TblResultData.SF_COL_PRINTUNITID + " , "
							+ TblResultData.SF_COL_COUNT + " , "
							+ TblResultData.SF_COL_GRADINGRESULTDATA + 
							" FROM " + TblResultData.SF_TBLNAME);
			db.execSQL(
					"UPDATE " + TblResultData.SF_TBLNAME +
					" SET " + TblResultData.SF_COL_INKDATA + " = null " + " , " +
							TblResultData.SF_COL_GRADINGRESULTDATA + " = null " );
		}
		if(oldVersion == 3 && newVersion == 4) {
			//SF_COL_INKDATAZIPを追加
			db.execSQL(
					"alter table " + TblInkData.SF_TBLNAME +
					" add " + TblInkData.SF_COL_INKDATAZIP + " blob default null ");
		}
		
	    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
		if( oldVersion < 5 ){
			//CSOUNDRECORDSTATUSを追加
			db.execSQL(
					"alter table " + TblResultData.SF_TBLNAME +
					" add " + TblResultData.SF_COL_SOUNDRECORDSTATUS + " integer default 0 ");
			
		}
	    //20150303 ADD-E For 2015年度Ver. 音声メモステータス
		
	    //20150409 ADD-S For 2015年度Ver. 未読コメント
		if( oldVersion < 6 ){
			//CCOMMENTUNREADFLGを追加
			db.execSQL(
					"alter table " + TblResultData.SF_TBLNAME +
					" add " + TblResultData.SF_COL_COMMENTUNREADFLG + " integer default 0 ");

			db.execSQL(
					"alter table " + TblInkData.SF_TBLNAME +
					" add " + TblInkData.SF_COL_INKBINARY + " blob default null ");
			
			sql = TblReadCommentData.SF_CREATE_TBL_SQL_REDCOMMENTDATA;
			db.execSQL(sql); 
		}
	    //20150409 ADD-E For 2015年度Ver. 未読コメント

        //20150423 ADD-S For 2015年度Ver. 未読コメント
		if( oldVersion < 7 ){
			//CPrintSetDateを追加
			db.execSQL(
					"alter table " + TblResultData.SF_TBLNAME +
					" add " + TblResultData.SF_COL_PRINTSETDATE + " text default null ");
		}
        //20150423 ADD-E For 2015年度Ver. 未読コメント
		
	}  

	/**
	 * @deprecated 64バイトRaw Key Dataを使うので{@link GetKey256}を使用すること
	 * @return
	 */
	@Deprecated public String GetKey() 
	{
		return Utility.digest("KumonLokal");
	}
	public String GetKey256() 
	{
		return "x'" + Utility.digest256("KumonLokal") + "'";
	}
}