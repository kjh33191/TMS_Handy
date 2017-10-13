package kumon2014.database.data;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;

import kumon2014.common.Utility;
import kumon2014.kumondata.DResultData;
import android.content.ContentValues;
import android.database.Cursor;

public class TblResultData {
	// テーブル名 
	public static final String SF_TBLNAME = "D_ResultData";

	// カラム 
	public static final String SF_COL_STUDENTID			= "CStudentID";			//Text 
	public static final String SF_COL_KYOKAID			= "CKyokaID";			//Text 
	public static final String SF_COL_KYOKANAME 		= "CKyokaName";			//Text 
	public static final String SF_COL_KYOKAORDERNO 		= "CKyokaOrderNumber";	//integer 
	public static final String SF_COL_KYOZAIID			= "CKyozaiID";			//Text 
	public static final String SF_COL_KYOZAINAME 		= "CKyozaiName";		//Text 
	public static final String SF_COL_KYOZAIORDERNO 	= "CKyozaiOrderNumber";	//integer 

	public static final String SF_COL_PRINTUNITID		= "CPrintUnitID";		//Text
	public static final String SF_COL_PRINTSETID		= "CPrintSetID";		//Text
	public static final String SF_COL_PRINTID			= "CPrintID";			//Text
	public static final String SF_COL_PRINTNO			= "CPrintNo";			//integer

	public static final String SF_COL_STATUS			= "CStatus";			//integer
	public static final String SF_COL_COUNT				= "CCount";				//integer
	public static final String SF_COL_GRADINGMETHOD 	= "CGradingMethod";		//integer
	public static final String SF_COL_GRADINGSTATUS 	= "CGradingStatus";		//integer
	public static final String SF_COL_LEARNINGPLACE 	= "CLearningPlace";		//integer
	
	public static final String SF_COL_SCHEDULEDDATE 	= "CScheduledDate";		//Text
	public static final String SF_COL_SCHEDULEDINDEX 	= "CScheduledIndex";	//integer
	public static final String SF_COL_SCHEDULEDNUM 		= "CScheduledNum";		//integer
	
	public static final String SF_COL_LIMITCOUNT 		= "CLimitCount";		//integer
	public static final String SF_COL_STARTDATE 		= "CStartDate";			//Text
	public static final String SF_COL_ENDDATE 			= "CEndDate";			//Text
	public static final String SF_COL_ANSWERTIME		= "CANSWERTIME";		//integer
	public static final String SF_COL_SCORE 			= "CScore";				//integer
	
	public static final String SF_COL_GRADINGRESULTDATA	= "CGradingResultData";	//Text
	public static final String SF_COL_INKDATA			= "CInkData";			//Text
	public static final String SF_COL_REDCOMMENT		= "CRedComment";		//Text
	public static final String SF_COL_TAGCOMMENT		= "CTagComment";		//Text
	public static final String SF_COL_TAGTEXT			= "CTagText";			//Text

	public static final String SF_COL_STROKENUM			= "CStrokeNum";			//Text
	
	public static final String SF_COL_PENTHICKNESS 		= "CPenThickness";		//integer
	public static final String SF_COL_GRADE 			= "CGrade";				//integer

	public static final String SF_COL_PRINTTYPE			= "CPrintType";			//integer
	
	//WORK
	public static final String SF_COL_ISREGIST 			= "CIsRegist";			//integer
	public static final String SF_COL_ISLEARNED 		= "CIsLearned";			//integer
	public static final String SF_COL_ISGREAD 			= "CIsGreaded";			//integer
	
	//20140521追加
	public static final String SF_COL_ANSWERTIME2 = "CANSWERTIME2"; // REAL
	
    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
	public static final String SF_COL_SOUNDRECORDSTATUS = "CSOUNDRECORDSTATUS"; // integer
    //20150303 ADD-E For 2015年度Ver. 音声メモステータス
	
    //20150409 ADD-S For 2015年度Ver. 未読コメント
	public static final String SF_COL_COMMENTUNREADFLG = "CCOMMENTUNREADFLG";	//integer
    //20150409 ADD-E For 2015年度Ver. 未読コメント
	
    //20150423 ADD-S For 2015年度Ver. 未読コメント
	public static final String SF_COL_PRINTSETDATE 		= "CPrintSetDate";		//Text
    //20150423 ADD-E For 2015年度Ver. 未読コメント
	
	//Index
	public static final int SF_IDX_STUDENT			= 0;
	public static final int SF_IDX_KYOKAID			= 1;
	public static final int SF_IDX_KYOKANAME 		= 2;
	public static final int SF_IDX_KYOKAORDERNO 	= 3;
	
	public static final int SF_IDX_KYOZAIID 		= 4;
	public static final int SF_IDX_KYOZAINAME 		= 5;
	public static final int SF_IDX_KYOZAIORDERNO 	= 6;
	
	public static final int SF_IDX_PRINTUNITID		= 7;
	public static final int SF_IDX_PRINTSETID		= 8;
	public static final int SF_IDX_PRINTID			= 9;
	public static final int SF_IDX_PRINTNO			= 10;

	public static final int SF_IDX_STATUS			= 11;
	public static final int SF_IDX_COUNT			= 12;
	public static final int SF_IDX_GRADINGMETHOD 	= 13;
	public static final int SF_IDX_GRADINGSTATUS	= 14;
	public static final int SF_IDX_LEARNINGPLACE	= 15;
	public static final int SF_IDX_SCHEDULEDDATE	= 16;
	public static final int SF_IDX_SCHEDULEDINDEX	= 17;
	public static final int SF_IDX_SCHEDULEDNUM		= 18;
	
	public static final int SF_IDX_LIMITCOUNT		= 19;
	public static final int SF_IDX_STARTDATE 		= 20;
	public static final int SF_IDX_ENDDATE 			= 21;
	public static final int SF_IDX_ANSWERTIME 		= 22;
	public static final int SF_IDX_SCORE 			= 23;
	
	public static final int SF_IDX_GRADINGRESULTDATA = 24;
	public static final int SF_IDX_INKDATA			= 25;
	public static final int SF_IDX_REDCOMMENT		= 26;
	public static final int SF_IDX_TAGCOMMENT		= 27;
	public static final int SF_IDX_TAGTEXT			= 28;
	public static final int SF_IDX_STROKENUM		= 29;
	
	public static final int SF_IDX_PENTHICKNESS		= 30;
	public static final int SF_IDX_GRADE 			= 31;
	public static final int SF_IDX_PRINTTYPE 		= 32;
	
	public static final int SF_IDX_ISREGIST 		= 33;
	public static final int SF_IDX_ISLEARNED 		= 34;
	public static final int SF_IDX_ISGREADED		= 35;

	//20140521追加
	public static final int SF_IDX_ANSWERTIME2 		= 36;
	
    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
	public static final int SF_IDX_SOUNDRECORDSTATUS = 37; 
	//20150303 ADD-E For 2015年度Ver. 音声メモステータス
	
    //20150409 ADD-S For 2015年度Ver. 未読コメント
	public static final int SF_IDX_COMMENTUNREADFLG = 38;
	//20150409 ADD-E For 2015年度Ver. 未読コメント
	
    //20150423 ADD-S For 2015年度Ver. 未読コメント
	public static final int SF_IDX_PRINTSETDATE 	= 39;
	//20150423 ADD-E For 2015年度Ver. 未読コメント
	
	public static final String SF_CREATE_TBL_SQL_PRINTDATA =
			"create table " + SF_TBLNAME + "( " 
					+ SF_COL_STUDENTID + 		" text not null, "
					+ SF_COL_KYOKAID + 			" text not null,  "
					+ SF_COL_KYOKANAME + 		" text,  "
					+ SF_COL_KYOKAORDERNO + 	" integer,  "
					+ SF_COL_KYOZAIID + 		" text not null, "
					+ SF_COL_KYOZAINAME + 		" text,  "
					+ SF_COL_KYOZAIORDERNO + 	" integer,  "
					+ SF_COL_PRINTUNITID + 		" text not null, "
					+ SF_COL_PRINTSETID + 		" text not null, "
					+ SF_COL_PRINTID + 			" text not null, "
					
					+ SF_COL_PRINTNO + 			" integer,  "
					+ SF_COL_STATUS + 			" integer,  "
					+ SF_COL_COUNT + 			" integer,  "
					+ SF_COL_GRADINGMETHOD + 	" integer,  "
					+ SF_COL_GRADINGSTATUS + 	" integer,  "
					+ SF_COL_LEARNINGPLACE + 	" integer,  "
					+ SF_COL_SCHEDULEDDATE + 	" text,  "
					+ SF_COL_SCHEDULEDINDEX + 	" integer,  "
					+ SF_COL_SCHEDULEDNUM + 	" integer,  "
					
					+ SF_COL_LIMITCOUNT + 		" integer,  "
					+ SF_COL_STARTDATE + 		" text,  "
					+ SF_COL_ENDDATE + 			" text,  "
					+ SF_COL_ANSWERTIME + 		" integer,  "
					+ SF_COL_SCORE + 			" integer,  "
					
					+ SF_COL_GRADINGRESULTDATA + " text,  "
					+ SF_COL_INKDATA + 			" text,  "
					+ SF_COL_REDCOMMENT + 		" text,  "
					+ SF_COL_TAGCOMMENT + 		" text,  "
					+ SF_COL_TAGTEXT + 			" text,  "
					
					+ SF_COL_STROKENUM + 		" text,  "
					+ SF_COL_PENTHICKNESS + 	" integer,  "
					+ SF_COL_GRADE + 			" integer,  "
					+ SF_COL_PRINTTYPE +		" integer,  "

					+ SF_COL_ISREGIST +			" integer,  "
					+ SF_COL_ISLEARNED + 		" integer,  "
					+ SF_COL_ISGREAD + 			" integer,  "
					
					+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID 
					+ " , " + SF_COL_KYOZAIID  + " , " + SF_COL_PRINTUNITID  + " )"
					+ " );";
	
	public static final String SF_CREATE_TBL_SQL_RESULTDARA_V2 = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_KYOKAID + " text not null,  " 
			+ SF_COL_KYOKANAME + " text,  " 
			+ SF_COL_KYOKAORDERNO + " integer,  "
			+ SF_COL_KYOZAIID + " text not null, " 
			+ SF_COL_KYOZAINAME	+ " text,  " 
			+ SF_COL_KYOZAIORDERNO + " integer,  "
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ SF_COL_PRINTSETID + " text not null, " 
			+ SF_COL_PRINTID + " text not null, "

			+ SF_COL_PRINTNO + " integer,  " 
			+ SF_COL_STATUS + " integer,  "
			+ SF_COL_COUNT + " integer,  " 
			+ SF_COL_GRADINGMETHOD + " integer,  " 
			+ SF_COL_GRADINGSTATUS + " integer,  "
			+ SF_COL_LEARNINGPLACE + " integer,  " 
			+ SF_COL_SCHEDULEDDATE + " text,  " 
			+ SF_COL_SCHEDULEDINDEX + " integer,  "
			+ SF_COL_SCHEDULEDNUM + " integer,  "

			+ SF_COL_LIMITCOUNT + " integer,  " 
			+ SF_COL_STARTDATE + " text,  "
			+ SF_COL_ENDDATE + " text,  " 
			+ SF_COL_ANSWERTIME + " integer,  "
			+ SF_COL_SCORE + " integer,  "

			+ SF_COL_GRADINGRESULTDATA + " text,  " 
			+ SF_COL_INKDATA + " text,  " 
			+ SF_COL_REDCOMMENT + " text,  " 
			+ SF_COL_TAGCOMMENT + " text,  " 
			+ SF_COL_TAGTEXT + " text,  "

			+ SF_COL_STROKENUM + " text,  " 
			+ SF_COL_PENTHICKNESS + " integer,  " 
			+ SF_COL_GRADE + " integer,  " 
			+ SF_COL_PRINTTYPE + " integer,  "

			+ SF_COL_ISREGIST + " integer,  " 
			+ SF_COL_ISLEARNED + " integer,  " 
			+ SF_COL_ISGREAD + " integer,  "

			+ SF_COL_ANSWERTIME2 + " REAL, "
			
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID
			+ " , " + SF_COL_KYOZAIID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";

    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
	public static final String SF_CREATE_TBL_SQL_RESULTDARA_V3 = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_KYOKAID + " text not null,  " 
			+ SF_COL_KYOKANAME + " text,  " 
			+ SF_COL_KYOKAORDERNO + " integer,  "
			+ SF_COL_KYOZAIID + " text not null, " 
			+ SF_COL_KYOZAINAME	+ " text,  " 
			+ SF_COL_KYOZAIORDERNO + " integer,  "
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ SF_COL_PRINTSETID + " text not null, " 
			+ SF_COL_PRINTID + " text not null, "

			+ SF_COL_PRINTNO + " integer,  " 
			+ SF_COL_STATUS + " integer,  "
			+ SF_COL_COUNT + " integer,  " 
			+ SF_COL_GRADINGMETHOD + " integer,  " 
			+ SF_COL_GRADINGSTATUS + " integer,  "
			+ SF_COL_LEARNINGPLACE + " integer,  " 
			+ SF_COL_SCHEDULEDDATE + " text,  " 
			+ SF_COL_SCHEDULEDINDEX + " integer,  "
			+ SF_COL_SCHEDULEDNUM + " integer,  "

			+ SF_COL_LIMITCOUNT + " integer,  " 
			+ SF_COL_STARTDATE + " text,  "
			+ SF_COL_ENDDATE + " text,  " 
			+ SF_COL_ANSWERTIME + " integer,  "
			+ SF_COL_SCORE + " integer,  "

			+ SF_COL_GRADINGRESULTDATA + " text,  " 
			+ SF_COL_INKDATA + " text,  " 
			+ SF_COL_REDCOMMENT + " text,  " 
			+ SF_COL_TAGCOMMENT + " text,  " 
			+ SF_COL_TAGTEXT + " text,  "

			+ SF_COL_STROKENUM + " text,  " 
			+ SF_COL_PENTHICKNESS + " integer,  " 
			+ SF_COL_GRADE + " integer,  " 
			+ SF_COL_PRINTTYPE + " integer,  "

			+ SF_COL_ISREGIST + " integer,  " 
			+ SF_COL_ISLEARNED + " integer,  " 
			+ SF_COL_ISGREAD + " integer,  "

			+ SF_COL_ANSWERTIME2 + " REAL, "
			
			+ SF_COL_SOUNDRECORDSTATUS + " integer, "
			
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID
			+ " , " + SF_COL_KYOZAIID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";
    //20150303 ADD-E For 2015年度Ver. 音声メモステータス
	
    //20150409 ADD-S For 2015年度Ver. 未読コメント
	public static final String SF_CREATE_TBL_SQL_RESULTDARA_V4 = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_KYOKAID + " text not null,  " 
			+ SF_COL_KYOKANAME + " text,  " 
			+ SF_COL_KYOKAORDERNO + " integer,  "
			+ SF_COL_KYOZAIID + " text not null, " 
			+ SF_COL_KYOZAINAME	+ " text,  " 
			+ SF_COL_KYOZAIORDERNO + " integer,  "
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ SF_COL_PRINTSETID + " text not null, " 
			+ SF_COL_PRINTID + " text not null, "

			+ SF_COL_PRINTNO + " integer,  " 
			+ SF_COL_STATUS + " integer,  "
			+ SF_COL_COUNT + " integer,  " 
			+ SF_COL_GRADINGMETHOD + " integer,  " 
			+ SF_COL_GRADINGSTATUS + " integer,  "
			+ SF_COL_LEARNINGPLACE + " integer,  " 
			+ SF_COL_SCHEDULEDDATE + " text,  " 
			+ SF_COL_SCHEDULEDINDEX + " integer,  "
			+ SF_COL_SCHEDULEDNUM + " integer,  "

			+ SF_COL_LIMITCOUNT + " integer,  " 
			+ SF_COL_STARTDATE + " text,  "
			+ SF_COL_ENDDATE + " text,  " 
			+ SF_COL_ANSWERTIME + " integer,  "
			+ SF_COL_SCORE + " integer,  "

			+ SF_COL_GRADINGRESULTDATA + " text,  " 
			+ SF_COL_INKDATA + " text,  " 
			+ SF_COL_REDCOMMENT + " text,  " 
			+ SF_COL_TAGCOMMENT + " text,  " 
			+ SF_COL_TAGTEXT + " text,  "

			+ SF_COL_STROKENUM + " text,  " 
			+ SF_COL_PENTHICKNESS + " integer,  " 
			+ SF_COL_GRADE + " integer,  " 
			+ SF_COL_PRINTTYPE + " integer,  "

			+ SF_COL_ISREGIST + " integer,  " 
			+ SF_COL_ISLEARNED + " integer,  " 
			+ SF_COL_ISGREAD + " integer,  "

			+ SF_COL_ANSWERTIME2 + " REAL, "
			
			+ SF_COL_SOUNDRECORDSTATUS + " integer, "

			+ SF_COL_COMMENTUNREADFLG + " integer, "
			
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID
			+ " , " + SF_COL_KYOZAIID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";
    //20150409 ADD-E For 2015年度Ver. 未読コメント
    //20150423 ADD-S For 2015年度Ver. 未読コメント
	public static final String SF_CREATE_TBL_SQL_RESULTDARA_V5 = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_KYOKAID + " text not null,  " 
			+ SF_COL_KYOKANAME + " text,  " 
			+ SF_COL_KYOKAORDERNO + " integer,  "
			+ SF_COL_KYOZAIID + " text not null, " 
			+ SF_COL_KYOZAINAME	+ " text,  " 
			+ SF_COL_KYOZAIORDERNO + " integer,  "
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ SF_COL_PRINTSETID + " text not null, " 
			+ SF_COL_PRINTID + " text not null, "

			+ SF_COL_PRINTNO + " integer,  " 
			+ SF_COL_STATUS + " integer,  "
			+ SF_COL_COUNT + " integer,  " 
			+ SF_COL_GRADINGMETHOD + " integer,  " 
			+ SF_COL_GRADINGSTATUS + " integer,  "
			+ SF_COL_LEARNINGPLACE + " integer,  " 
			+ SF_COL_SCHEDULEDDATE + " text,  " 
			+ SF_COL_SCHEDULEDINDEX + " integer,  "
			+ SF_COL_SCHEDULEDNUM + " integer,  "

			+ SF_COL_LIMITCOUNT + " integer,  " 
			+ SF_COL_STARTDATE + " text,  "
			+ SF_COL_ENDDATE + " text,  " 
			+ SF_COL_ANSWERTIME + " integer,  "
			+ SF_COL_SCORE + " integer,  "

			+ SF_COL_GRADINGRESULTDATA + " text,  " 
			+ SF_COL_INKDATA + " text,  " 
			+ SF_COL_REDCOMMENT + " text,  " 
			+ SF_COL_TAGCOMMENT + " text,  " 
			+ SF_COL_TAGTEXT + " text,  "

			+ SF_COL_STROKENUM + " text,  " 
			+ SF_COL_PENTHICKNESS + " integer,  " 
			+ SF_COL_GRADE + " integer,  " 
			+ SF_COL_PRINTTYPE + " integer,  "

			+ SF_COL_ISREGIST + " integer,  " 
			+ SF_COL_ISLEARNED + " integer,  " 
			+ SF_COL_ISGREAD + " integer,  "

			+ SF_COL_ANSWERTIME2 + " REAL, "
			
			+ SF_COL_SOUNDRECORDSTATUS + " integer, "
			+ SF_COL_COMMENTUNREADFLG + " integer, "
			+ SF_COL_PRINTSETDATE + " text, "
			
			
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID
			+ " , " + SF_COL_KYOZAIID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";
    //20150423 ADD-E For 2015年度Ver. 未読コメント
	
	///////////////////////////////////////////////////////////////////////////////////
	//クリアALL
	protected static boolean DB_ClearAll(SQLiteDatabase Writabledb, boolean datafile) 
	{
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, null, null);		
			ret = true;
			
        	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			TblInkData.DB_ClearAll(Writabledb);
			TblGradingResultData.DB_ClearAll(Writabledb);
        	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			//20140731 ADD-S For 録音対応
			if(datafile) {
				TblSoundRecordData.DB_ClearAll(Writabledb);
			}
			//20140731 ADD-E For 録音対応
	        //20150210 MOD-S For 2015年度Ver. 音声メモ
			if(datafile) {
				TblSoundCommentData.DB_ClearAll(Writabledb);
			}
	        //20150210 MOD-E For 2015年度Ver. 音声メモ
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//Delete By Studentid
	protected static boolean DB_DeleteByStudentID(SQLiteDatabase Writabledb, String studentid ) 
	{
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, 
								SF_COL_STUDENTID + " = ? " ,
								new String[]{ studentid});
			ret = true;
			
        	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			TblInkData.DB_DeleteByStudentID(Writabledb, studentid);
			TblGradingResultData.DB_DeleteByStudentID(Writabledb, studentid);
        	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			//20140731 ADD-S For 録音対応
			TblSoundRecordData.DB_DeleteByStudentID(Writabledb, studentid);
			//20140731 ADD-E For 録音対応
	        //20150210 ADD-S For 2015年度Ver. 音声メモ
			TblSoundCommentData.DB_DeleteByStudentID(Writabledb, studentid);
	        //20150210 ADD-E For 2015年度Ver. 音声メモ
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//Delete By PrintUnit
	protected static boolean DB_DeleteByPrintUnit(SQLiteDatabase Writabledb, 
													String studentid, String kyoka, String kyozai, String printunit) 
	{
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, 
								SF_COL_STUDENTID + " = ? " 
				            		+ " AND " + SF_COL_KYOKAID + " = ? "
				            		+ " AND " + SF_COL_KYOZAIID + " = ? "
				            		+ " AND " + SF_COL_PRINTUNITID + " = ? ",
								new String[]{ studentid, kyoka, kyozai, printunit });
			ret = true;
			
        	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			TblInkData.DB_DeleteByPrintUnit(Writabledb, studentid, kyoka, kyozai, printunit);
			TblGradingResultData.DB_DeleteByPrintUnit(Writabledb, studentid, kyoka, kyozai, printunit);
        	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			//20140731 ADD-S For 録音対応
			TblSoundRecordData.DB_DeleteByPrintUnit(Writabledb, studentid, kyoka, kyozai, printunit);
			//20140731 ADD-E For 録音対応
	        //20150210 ADD-S For 2015年度Ver. 音声メモ
			TblSoundCommentData.DB_DeleteByPrintUnit(Writabledb, studentid, kyoka, kyozai, printunit);
	        //20150210 ADD-E For 2015年度Ver. 音声メモ

		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//Delete By KyozaiID
	protected static boolean DB_DeleteByKyozaiID(SQLiteDatabase Writabledb, String studentid, String kyoka, String kyozai) 
	{
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, 
								SF_COL_STUDENTID + " = ? " 
				            		+ " AND " + SF_COL_KYOKAID + " = ? "
				            		+ " AND " + SF_COL_KYOZAIID + " = ? " 
				            		+ " AND " + SF_COL_COUNT + " != 0 " ,
								new String[]{ studentid, kyoka, kyozai });
			ret = true;
        	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			TblInkData.DB_DeleteByKyozaiID(Writabledb, studentid, kyoka, kyozai);
			TblGradingResultData.DB_DeleteByKyozaiID(Writabledb, studentid, kyoka, kyozai);
        	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//20140618 ADD-S For 分割受信
	//Delete By PrintSetID
	protected static boolean DB_DeleteByPrintSetID(SQLiteDatabase Writabledb, String studentid, String kyoka, String kyozai, String printsetid ) 
	{
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, 
								SF_COL_STUDENTID + " = ? " 
				            		+ " AND " + SF_COL_KYOKAID + " = ? "
				            		+ " AND " + SF_COL_KYOZAIID + " = ? " 
				            		+ " AND " + SF_COL_PRINTSETID + " = ? " ,
								new String[]{ studentid, kyoka, kyozai, printsetid});
			ret = true;
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//20140618 ADD-S For 分割受信
	
	//Insert 
	protected static boolean DB_InsertResultDataList(SQLiteDatabase Writabledb, ArrayList<DResultData> resultdatalist) 
	{
		boolean ret = false;
		long cnt = 0;
		try{
			String work;
			for(int i = 0; i < resultdatalist.size(); i++) {
				ContentValues values = new ContentValues(); 
		      	values.put(SF_COL_STUDENTID, resultdatalist.get(i).mStudentID); 
		      	values.put(SF_COL_KYOKAID, resultdatalist.get(i).mKyokaID); 
		      	values.put(SF_COL_KYOKANAME, resultdatalist.get(i).mKyokaName); 
		      	values.put(SF_COL_KYOKAORDERNO, resultdatalist.get(i).mKyokaOrderNo); 
		      	values.put(SF_COL_KYOZAIID, resultdatalist.get(i).mKyozaiID); 
		      	values.put(SF_COL_KYOZAINAME, resultdatalist.get(i).mKyozaiName); 
		      	values.put(SF_COL_KYOZAIORDERNO, resultdatalist.get(i).mKyozaiOrderNo); 
		      	values.put(SF_COL_PRINTUNITID, resultdatalist.get(i).mPrintUnitID); 
		      	values.put(SF_COL_PRINTSETID, resultdatalist.get(i).mPrintSetID); 
		      	values.put(SF_COL_PRINTID, resultdatalist.get(i).mPrintID); 
		      	values.put(SF_COL_PRINTNO, resultdatalist.get(i).mPrintNo); 
		      	values.put(SF_COL_STATUS, resultdatalist.get(i).mStatus); 
		      	values.put(SF_COL_COUNT, resultdatalist.get(i).mCount); 
		      	values.put(SF_COL_GRADINGMETHOD, resultdatalist.get(i).mGradingMethod); 
		      	values.put(SF_COL_GRADINGSTATUS, resultdatalist.get(i).mGradingStatus); 
		      	values.put(SF_COL_LEARNINGPLACE, resultdatalist.get(i).mLearningPlace); 

		      	values.put(SF_COL_SCHEDULEDDATE, resultdatalist.get(i).mScheduledDate); 
		      	values.put(SF_COL_SCHEDULEDINDEX, resultdatalist.get(i).mScheduledIndex); 
		      	values.put(SF_COL_SCHEDULEDNUM, resultdatalist.get(i).mScheduledNum); 
		      	
		      	values.put(SF_COL_LIMITCOUNT, resultdatalist.get(i).mLimitCount); 
    			work = Utility.getFormatDate(resultdatalist.get(i).mStartDate);
		      	values.put(SF_COL_STARTDATE, work); 
    			work = Utility.getFormatDate(resultdatalist.get(i).mEndDate);
		      	values.put(SF_COL_ENDDATE, work); 
				//20140521 DEL-S
				//values.put(SF_COL_ANSWERTIME, resultdatalist.get(i).mAnswerTime);
				//20140521 DEL-E
		      	
		      	values.put(SF_COL_SCORE, resultdatalist.get(i).mScore); 
		      	
	        	//20140605 DEL-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		      	//values.put(SF_COL_GRADINGRESULTDATA, resultdatalist.get(i).mGradingResultData); 
		      	//values.put(SF_COL_INKDATA, resultdatalist.get(i).mInkData); 
	        	//20140605 DEL-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		      	values.put(SF_COL_REDCOMMENT, resultdatalist.get(i).mRedComment); 
		      	values.put(SF_COL_TAGCOMMENT, resultdatalist.get(i).mTagComment); 
		      	values.put(SF_COL_TAGTEXT, resultdatalist.get(i).mTagText); 
		      	
		      	values.put(SF_COL_STROKENUM, resultdatalist.get(i).mStrokeNum); 

		      	values.put(SF_COL_PENTHICKNESS, resultdatalist.get(i).mPenThickness); 
		      	values.put(SF_COL_GRADE, resultdatalist.get(i).mGrade);
		      	
		      	values.put(SF_COL_PRINTTYPE, resultdatalist.get(i).mPrintType); 

		      	values.put(SF_COL_ISREGIST, 0); 
		      	values.put(SF_COL_ISLEARNED, 0); 
		      	values.put(SF_COL_ISGREAD, 0); 
		      	
				//20140521 ADD-S
				values.put(SF_COL_ANSWERTIME2, resultdatalist.get(i).mAnswerTime);
				//20140521 ADD-E
		      	

			    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
		      	values.put(SF_COL_SOUNDRECORDSTATUS, resultdatalist.get(i).mSoundRecordStatus); 
			    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
		        //20150409 ADD-S For 2015年度Ver. 未読コメント
		      	values.put(SF_COL_COMMENTUNREADFLG, resultdatalist.get(i).mCommentUnreadFlg); 
		        //20150409 ADD-E For 2015年度Ver. 未読コメント
		        //20150423 ADD-S For 2015年度Ver. 未読コメント
    			work = Utility.getFormatDate(resultdatalist.get(i).mPrintSetDate);
		      	values.put(SF_COL_PRINTSETDATE, work); 
		        //20150423 ADD-E For 2015年度Ver. 未読コメント

		      	long rets = Writabledb.insert(SF_TBLNAME, null, values);
		      	if( rets >= 0) {
		      		cnt++;
		      	}
		      	else {
		      		break;
		      	}
			}
			if(cnt == resultdatalist.size()) {
				ret = true;
	        	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
				ret = TblInkData.DB_InsertInkDataList(Writabledb, resultdatalist);
				if(ret) {
					ret = TblGradingResultData.DB_InsertGradingResultDataList(Writabledb, resultdatalist);
				}
	        	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
				//20140731 ADD-S For 録音対応
				if(ret) {
					//20150310 MOD-S For Web音声データ展開
					//ret = TblSoundRecordData.DB_InsertSoundDataList(Writabledb, resultdatalist);
					ret = TblSoundRecordData.DB_InsertSoundDataList(resultdatalist, 0);
					//20150310 MOD-E For Web音声データ展開
				}
				//20140731 ADD-E For 録音対応
		        //20150210 MOD-S For 2015年度Ver. 音声メモ
				if(ret) {
					//20150310 MOD-S For Web音声データ展開
					//ret = TblSoundCommentData.DB_InsertSoundCommentDataList(Writabledb, resultdatalist);
					ret = TblSoundCommentData.DB_InsertSoundCommentDataList(resultdatalist, 0);
					//20150310 MOD-E For Web音声データ展開
				}
		        //20150210 MOD-E For 2015年度Ver. 音声メモ
		}
		}
		catch(Exception e) {
//			String s = e.toString();
		}
		return ret;
	}
	protected static ArrayList<DResultData> DB_GetResultList(SQLiteDatabase readbledb, String studentid, String KyokaID,  String KyozaiID) 
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
    		String cond = "";
    		String[] where = null;

    		//診断・最終テストは、参照不可
            //cond = SF_COL_STUDENTID + " = ? ";
            cond = "( " + SF_COL_PRINTTYPE + " = 0 OR (" + SF_COL_PRINTTYPE + " != 0 AND (" + SF_COL_STATUS + " = 0 OR " + SF_COL_STATUS + " = 1 ))) AND " +  SF_COL_STUDENTID + " = ? ";
   			where = new String[]{ studentid};
        	if(KyokaID.length() > 0) {
        		cond += " AND " + SF_COL_KYOKAID + " = ? " ;
       			where = new String[]{ studentid, KyokaID};
            	if(KyozaiID.length() > 0) {
            		cond += " AND " + SF_COL_KYOZAIID + " = ? " ;
           			where = new String[]{ studentid, KyokaID, KyozaiID};
            	}
        	}
        	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
            //20150303 MOD-S For 2015年度Ver. 音声メモステータス
        	/***
			String[] columns = {SF_COL_STUDENTID, SF_COL_KYOKAID, SF_COL_KYOKANAME, SF_COL_KYOKAORDERNO, SF_COL_KYOZAIID, SF_COL_KYOZAINAME, SF_COL_KYOZAIORDERNO,
					SF_COL_PRINTUNITID, SF_COL_PRINTSETID, SF_COL_PRINTID, SF_COL_PRINTNO, SF_COL_STATUS, SF_COL_COUNT, SF_COL_GRADINGMETHOD, SF_COL_GRADINGSTATUS, 
					SF_COL_LEARNINGPLACE, SF_COL_SCHEDULEDDATE, SF_COL_SCHEDULEDINDEX, SF_COL_SCHEDULEDNUM, SF_COL_LIMITCOUNT, SF_COL_STARTDATE, SF_COL_ENDDATE,
					SF_COL_ANSWERTIME, SF_COL_SCORE, SF_COL_GRADE, SF_COL_PRINTTYPE, SF_COL_ISREGIST, SF_COL_ISLEARNED, SF_COL_ISGREAD, SF_COL_ANSWERTIME2};
			***/
            //20150409 MOD-S For 2015年度Ver. 未読コメント
        	/***
			String[] columns = {SF_COL_STUDENTID, SF_COL_KYOKAID, SF_COL_KYOKANAME, SF_COL_KYOKAORDERNO, SF_COL_KYOZAIID, SF_COL_KYOZAINAME, SF_COL_KYOZAIORDERNO,
					SF_COL_PRINTUNITID, SF_COL_PRINTSETID, SF_COL_PRINTID, SF_COL_PRINTNO, SF_COL_STATUS, SF_COL_COUNT, SF_COL_GRADINGMETHOD, SF_COL_GRADINGSTATUS, 
					SF_COL_LEARNINGPLACE, SF_COL_SCHEDULEDDATE, SF_COL_SCHEDULEDINDEX, SF_COL_SCHEDULEDNUM, SF_COL_LIMITCOUNT, SF_COL_STARTDATE, SF_COL_ENDDATE,
					SF_COL_ANSWERTIME, SF_COL_SCORE, SF_COL_GRADE, SF_COL_PRINTTYPE, SF_COL_ISREGIST, SF_COL_ISLEARNED, SF_COL_ISGREAD, SF_COL_ANSWERTIME2, SF_COL_SOUNDRECORDSTATUS};
			***/
			String[] columns = {SF_COL_STUDENTID, SF_COL_KYOKAID, SF_COL_KYOKANAME, SF_COL_KYOKAORDERNO, SF_COL_KYOZAIID, SF_COL_KYOZAINAME, SF_COL_KYOZAIORDERNO,
					SF_COL_PRINTUNITID, SF_COL_PRINTSETID, SF_COL_PRINTID, SF_COL_PRINTNO, SF_COL_STATUS, SF_COL_COUNT, SF_COL_GRADINGMETHOD, SF_COL_GRADINGSTATUS, 
					SF_COL_LEARNINGPLACE, SF_COL_SCHEDULEDDATE, SF_COL_SCHEDULEDINDEX, SF_COL_SCHEDULEDNUM, SF_COL_LIMITCOUNT, SF_COL_STARTDATE, SF_COL_ENDDATE,
					SF_COL_ANSWERTIME, SF_COL_SCORE, SF_COL_GRADE, SF_COL_PRINTTYPE, SF_COL_ISREGIST, SF_COL_ISLEARNED, SF_COL_ISGREAD, SF_COL_ANSWERTIME2, SF_COL_SOUNDRECORDSTATUS, 
					SF_COL_COMMENTUNREADFLG};
		    //20150409 MOD-E For 2015年度Ver. 未読コメント
            //20150303 MOD-E For 2015年度Ver. 音声メモステータス
        	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）

            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
                	//20140605 MOD-S For　大量データ対応（Ｉｎｋとテスト結果を別レテーブルに）
            		//null,		//全項目
            		columns,	
            		//20140605 MOD-E For　大量データ対応（Ｉｎｋとテスト結果を別レテーブルに）
            		//条件式
                    cond ,
					//条件
                    where,  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                	//20140707 MOD-S For　同じSF_COL_KYOZAIORDERNOが別教材で存在する
                    //SF_COL_KYOKAORDERNO + " ASC , " + SF_COL_KYOZAIORDERNO + " Desc , "
                    //		+ SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                    SF_COL_KYOKAORDERNO + " ASC , " + SF_COL_KYOZAIORDERNO + " Desc , " + SF_COL_KYOZAINAME + " ASC , "
                            		+ SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                   	//20140707 MOD-S For　同じSF_COL_KYOZAIORDERNOが別教材で存在する
                    //limit
                    null 
                    );
            
            // 検索結果をcursorから読み込んで返す
        	//20140605 MOD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
            //resultdatalist =  DB_GetResultData_readCursor( cursor, false);
            resultdatalist =  DB_GetResultData_readCursor2( cursor, false);
        	//20140605 MOD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
        
        return resultdatalist;
	}
	
	protected static ArrayList<DResultData> DB_GetNextResultData(SQLiteDatabase readbledb, String studentid, String kyokaID, String kyozaiID) 
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
        	
            String where = SF_COL_STUDENTID + " = ? "
            	    + " AND " + SF_COL_KYOKAID + " = ? " 
            		+ " AND " + SF_COL_KYOZAIID + " = ? "
            		+ " AND " + SF_COL_COUNT + " = 0 " ;
            
            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
            		null,		//全項目
            		//条件式
                    where ,
					//条件
					new String[]{ studentid, kyokaID, kyozaiID},  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                    //limit
                    null 
                    );
            
            resultdatalist =  DB_GetResultData_readCursor( cursor, false);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
    	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		if(resultdatalist != null) {
			for(int i = 0; i < resultdatalist.size(); i++) {
				DResultData resultData = resultdatalist.get(i);
				DResultData resultInkData = TblInkData.DB_GetInkData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				resultData.mInkData = resultInkData.mInkData;
				resultData.mInkBinary = resultInkData.mInkBinary;
				resultData.mGradingResultData = TblGradingResultData.DB_GetGradingResultData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
			}
		}
    	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
        
        return resultdatalist;
	}
	protected static ArrayList<DResultData> DB_GetRetryResultData(SQLiteDatabase readbledb, String studentid, String kyokaID, String kyozaiID) 
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
            String where = SF_COL_STUDENTID + " = ? "
            	    + " AND " + SF_COL_KYOKAID + " = ? " 
            		+ " AND " + SF_COL_KYOZAIID + " = ? "
            		+ " AND " + SF_COL_COUNT + " > 0 "
            		+ " AND " + SF_COL_STATUS + " = 1 "
            		//20140822 ADD-S For 本人採点、端末での指導者採点は、未送信がヒットしてしまう
            		+ " AND " + SF_COL_ISLEARNED + " != 1 ";
            		//20140822 ADD-E For 本人採点、端末での指導者採点は、未送信がヒットしてしまう

            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
            		null,		//全項目
            		//条件式
                    where ,
					//条件
					new String[]{ studentid, kyokaID, kyozaiID},  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                    //limit
                    null 
                    );
            
            // 検索結果をcursorから読み込んで返す
            resultdatalist =  DB_GetResultData_readCursor( cursor, false);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
    	//20140618 DEL-S For　この時点で全データは不要
        /***
        //20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		if(resultdatalist != null) {
			for(int i = 0; i < resultdatalist.size(); i++) {
				DResultData resultData = resultdatalist.get(i);
				resultData.mInkData = TblInkData.DB_GetInkData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				resultData.mGradingResultData = TblGradingResultData.DB_GetGradingResultData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
			}
		}
    	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
    	***/
		//20140618 DEL-E For　この時点で全データは不要
        
        return resultdatalist;
	}
	//20140605 MOD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
	//protected static ArrayList<DResultData> DB_GetRegistDataList(SQLiteDatabase readbledb, String studentid) 
	protected static ArrayList<DResultData> DB_GetRegistDataList(SQLiteDatabase readbledb, String studentid, boolean withink) 
	//20140605 MOD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
            String where = SF_COL_STUDENTID + " = ? "
            		+ " AND " + SF_COL_ISREGIST + " = 1 ";
        	
            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
            		null,		//全項目
            		//条件式
                    where ,
					//条件
					new String[]{ studentid },  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    SF_COL_KYOKAORDERNO + " Desc , " + SF_COL_KYOZAIORDERNO + " Desc , " + 
            		SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                    //limit
                    null 
            		//20140617 MOD-E For サイズが大きくなりすぎるので分割送信
                    );
            
            resultdatalist =  DB_GetResultData_readCursor( cursor, false);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
    	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		if(resultdatalist != null && withink == true) {
			for(int i = 0; i < resultdatalist.size(); i++) {
				DResultData resultData = resultdatalist.get(i);
				DResultData resultInkData = TblInkData.DB_GetInkData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				resultData.mInkData = resultInkData.mInkData;
				resultData.mInkBinary = resultInkData.mInkBinary;
				resultData.mGradingResultData = TblGradingResultData.DB_GetGradingResultData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
			}
		}
    	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
        
        return resultdatalist;
	}
	
	
	protected static ArrayList<DResultData> DB_GetGradeResultData(SQLiteDatabase readbledb, String studentid) 
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
            String where = SF_COL_STUDENTID + " = ? "
            		+ " AND (" + SF_COL_GRADINGMETHOD + " = 1 OR " + SF_COL_GRADINGMETHOD + " = 3 ) "
            		+ " AND " + SF_COL_STATUS + " = 4 ";

            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
            		null,		//全項目
            		//条件式
                    where ,
					//条件
					new String[]{ studentid},  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                    //limit
                    null 
                    );
            
            // 検索結果をcursorから読み込んで返す
            resultdatalist =  DB_GetResultData_readCursor( cursor, false);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
    	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		if(resultdatalist != null) {
			for(int i = 0; i < resultdatalist.size(); i++) {
				DResultData resultData = resultdatalist.get(i);
				DResultData resultInkData = TblInkData.DB_GetInkData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				resultData.mInkData = resultInkData.mInkData;
				resultData.mInkBinary = resultInkData.mInkBinary;
				resultData.mGradingResultData = TblGradingResultData.DB_GetGradingResultData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
			}
		}
    	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
        
        return resultdatalist;
	}
	
	//20140731 MOD-S For 学習済み教材の表示は,PrintSet単位でＤＢ参照
	//protected static ArrayList<DResultData> DB_GetPrintSet(SQLiteDatabase readbledb, String studentid, String kyokaID, String kyozaiID,  String printsetID) 
	protected static ArrayList<DResultData> DB_GetPrintSet(SQLiteDatabase readbledb, String studentid, String kyokaID, String kyozaiID,  String printsetID, boolean withinkdata) 
	//20140731 MOD-E For 学習済み教材の表示は,PrintSet単位でＤＢ参照
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
            String where = SF_COL_STUDENTID + " = ? "
            	    + " AND " + SF_COL_KYOKAID + " = ? " 
            		+ " AND " + SF_COL_KYOZAIID + " = ? "
            		+ " AND " + SF_COL_PRINTSETID + " = ? ";
            
            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
                	null,		//全項目
            		//条件式
                    where ,
					//条件
					new String[]{ studentid, kyokaID, kyozaiID, printsetID},  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                    //limit
                    null 
                    );
            
            // 検索結果をcursorから読み込んで返す
            resultdatalist =  DB_GetResultData_readCursor( cursor, false);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
        
    	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		if(resultdatalist != null) {
			for(int i = 0; i < resultdatalist.size(); i++) {
				DResultData resultData = resultdatalist.get(i);
				//20140731 MOD-S For 学習済み教材の表示は,PrintSet単位でＤＢ参照
				//resultData.mInkData = TblInkData.DB_GetInkData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				if(withinkdata) {
					DResultData resultInkData = TblInkData.DB_GetInkData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
					resultData.mInkData = resultInkData.mInkData;
					resultData.mInkBinary = resultInkData.mInkBinary;
				}
				//20140731 MOD-E For 学習済み教材の表示は,PrintSet単位でＤＢ参照
				resultData.mGradingResultData = TblGradingResultData.DB_GetGradingResultData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
			}
		}
    	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
        
        return resultdatalist;
	}

	
	private static ArrayList<DResultData> DB_GetResultData_readCursor(Cursor cursor, boolean alldata)
	{
		ArrayList<DResultData> resultdatalist = new ArrayList<DResultData>();
		DResultData resultdata = null;
		
		String work;
        while( cursor.moveToNext() ){
        	resultdata = new DResultData();
        	resultdata.mStudentID = cursor.getString( SF_IDX_STUDENT );
        	resultdata.mKyokaID = cursor.getString( SF_IDX_KYOKAID );
        	resultdata.mKyokaName = cursor.getString( SF_IDX_KYOKANAME );
        	resultdata.mKyokaOrderNo = cursor.getInt( SF_IDX_KYOKAORDERNO );
        	resultdata.mKyokaID = cursor.getString( SF_IDX_KYOKAID );
        	resultdata.mKyozaiName = cursor.getString( SF_IDX_KYOZAINAME );
        	resultdata.mKyozaiID = cursor.getString( SF_IDX_KYOZAIID );
        	resultdata.mKyozaiOrderNo = cursor.getInt( SF_IDX_KYOZAIORDERNO );
        	
        	resultdata.mPrintUnitID = cursor.getString( SF_IDX_PRINTUNITID );
        	resultdata.mPrintSetID = cursor.getString( SF_IDX_PRINTSETID );
        	resultdata.mPrintID = cursor.getString( SF_IDX_PRINTID );
        	resultdata.mPrintNo = cursor.getInt( SF_IDX_PRINTNO );

        	resultdata.mStatus = cursor.getInt( SF_IDX_STATUS );
        	resultdata.mCount = cursor.getInt( SF_IDX_COUNT );
        	resultdata.mGradingMethod = cursor.getInt( SF_IDX_GRADINGMETHOD );
        	resultdata.mGradingStatus = cursor.getInt( SF_IDX_GRADINGSTATUS );
        	resultdata.mLearningPlace = cursor.getInt( SF_IDX_LEARNINGPLACE );

        	resultdata.mScheduledDate = cursor.getString( SF_IDX_SCHEDULEDDATE );
        	resultdata.mScheduledIndex = cursor.getInt( SF_IDX_SCHEDULEDINDEX );
        	resultdata.mScheduledNum = cursor.getInt( SF_IDX_SCHEDULEDNUM );
        	
        	resultdata.mLimitCount = cursor.getInt( SF_IDX_LIMITCOUNT );
        	work = cursor.getString( SF_IDX_STARTDATE );
        	resultdata.mStartDate = Utility.getDateFromString(work);
        	work = cursor.getString( SF_IDX_ENDDATE );
        	resultdata.mEndDate = Utility.getDateFromString(work);
			//20140521 DEL-S
        	//resultdata.mAnswerTime = cursor.getInt( SF_IDX_ANSWERTIME );
			//20140521 DEL-E
        	resultdata.mScore = cursor.getInt( SF_IDX_SCORE );

        	resultdata.mGradingResultData = cursor.getString( SF_IDX_GRADINGRESULTDATA );
        	resultdata.mInkData = cursor.getString( SF_IDX_INKDATA );
        	resultdata.mRedComment = cursor.getString( SF_IDX_REDCOMMENT );
        	resultdata.mTagComment = cursor.getString( SF_IDX_TAGCOMMENT );
        	resultdata.mTagText = cursor.getString( SF_IDX_TAGTEXT );
	      	//20140905 ADD-S mTagComment,mTagTextにごみが入っている時がある
	      	if(resultdata.mTagComment.equalsIgnoreCase("anyType{}")) {
	      		resultdata.mTagComment = ""; 
	      	}
	      	if(resultdata.mTagText.equalsIgnoreCase("anyType{}")) {
	      		resultdata.mTagText = ""; 
	      	}
	      	//20140905 MOD-E
        	

        	resultdata.mStrokeNum = cursor.getString( SF_IDX_STROKENUM );
        	resultdata.mPenThickness = cursor.getInt( SF_IDX_PENTHICKNESS );
        	
        	resultdata.mGrade = cursor.getInt( SF_IDX_GRADE );
        	resultdata.mPrintType = cursor.getInt( SF_IDX_PRINTTYPE );

        	resultdata.mIsRegist = cursor.getInt( SF_IDX_ISREGIST );
        	resultdata.mIsLearned = cursor.getInt( SF_IDX_ISLEARNED );
        	resultdata.mIsGreaded = cursor.getInt( SF_IDX_ISGREADED );
			//20140521 ADD-S
			resultdata.mAnswerTime = cursor.getLong(SF_IDX_ANSWERTIME2);
			//20140521 ADD-E
        	
		    //20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			resultdata.mOrgCount = resultdata.mCount;
		    //20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			
		    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
			resultdata.mSoundRecordStatus = cursor.getInt(SF_IDX_SOUNDRECORDSTATUS);
		    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
			
		    //20150409 ADD-S For 2015年度Ver. 未読コメント
			resultdata.mCommentUnreadFlg = cursor.getInt(SF_IDX_COMMENTUNREADFLG);
		    //20150409 ADD-E For 2015年度Ver. 未読コメント
			
	        //20150423 ADD-S For 2015年度Ver. 未読コメント
        	work = cursor.getString( SF_IDX_PRINTSETDATE );
        	resultdata.mPrintSetDate = Utility.getDateFromString(work);
	        //20150423 ADD-E For 2015年度Ver. 未読コメント
			
        	resultdatalist.add(resultdata);
        }
        return resultdatalist;
    }
	private static ArrayList<DResultData> DB_GetResultData_readCursor2(Cursor cursor, boolean alldata) {
		ArrayList<DResultData> resultdatalist = new ArrayList<DResultData>();
		DResultData resultdata = null;

		String work;
		while (cursor.moveToNext()) {
			resultdata = new DResultData();
			resultdata.mStudentID = cursor.getString(SF_IDX_STUDENT);
			resultdata.mKyokaID = cursor.getString(SF_IDX_KYOKAID);
			resultdata.mKyokaName = cursor.getString(SF_IDX_KYOKANAME);
			resultdata.mKyokaOrderNo = cursor.getInt(SF_IDX_KYOKAORDERNO);
			resultdata.mKyokaID = cursor.getString(SF_IDX_KYOKAID);
			resultdata.mKyozaiName = cursor.getString(SF_IDX_KYOZAINAME);
			resultdata.mKyozaiID = cursor.getString(SF_IDX_KYOZAIID);
			resultdata.mKyozaiOrderNo = cursor.getInt(SF_IDX_KYOZAIORDERNO);

			resultdata.mPrintUnitID = cursor.getString(SF_IDX_PRINTUNITID);
			resultdata.mPrintSetID = cursor.getString(SF_IDX_PRINTSETID);
			resultdata.mPrintID = cursor.getString(SF_IDX_PRINTID);
			resultdata.mPrintNo = cursor.getInt(SF_IDX_PRINTNO);

			resultdata.mStatus = cursor.getInt(SF_IDX_STATUS);
			resultdata.mCount = cursor.getInt(SF_IDX_COUNT);
			resultdata.mGradingMethod = cursor.getInt(SF_IDX_GRADINGMETHOD);
			resultdata.mGradingStatus = cursor.getInt(SF_IDX_GRADINGSTATUS);
			resultdata.mLearningPlace = cursor.getInt(SF_IDX_LEARNINGPLACE);

			resultdata.mScheduledDate = cursor.getString(SF_IDX_SCHEDULEDDATE);
			resultdata.mScheduledIndex = cursor.getInt(SF_IDX_SCHEDULEDINDEX);
			resultdata.mScheduledNum = cursor.getInt(SF_IDX_SCHEDULEDNUM);

			resultdata.mLimitCount = cursor.getInt(SF_IDX_LIMITCOUNT);
			work = cursor.getString(SF_IDX_STARTDATE);
			resultdata.mStartDate = Utility.getDateFromString(work);
			work = cursor.getString(SF_IDX_ENDDATE);
			resultdata.mEndDate = Utility.getDateFromString(work);
			//20140521 DEL-S
			//resultdata.mAnswerTime = cursor.getInt(SF_IDX_ANSWERTIME);
			//20140521 DEL-E
			resultdata.mScore = cursor.getInt(SF_IDX_SCORE);

			resultdata.mGrade = cursor.getInt(24);
			resultdata.mPrintType = cursor.getInt(25);

			resultdata.mIsRegist = cursor.getInt(26);
			resultdata.mIsLearned = cursor.getInt(27);
			resultdata.mIsGreaded = cursor.getInt(28);
			//20140521 ADD-S
			resultdata.mAnswerTime = cursor.getLong(29);
			//20140521 ADD-E

		    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
			resultdata.mSoundRecordStatus = cursor.getInt(30);
		    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
			
		    //20150409 ADD-S For 2015年度Ver. 未読コメント
			resultdata.mCommentUnreadFlg = cursor.getInt(31);
		    //20150409 ADD-E For 2015年度Ver. 未読コメント
			
			resultdatalist.add(resultdata);
		}
		return resultdatalist;
	}
	
	protected static ArrayList<String> DB_GetPrintSetIDList(SQLiteDatabase readbledb, String studentid, String kyokaID, String kyozaiID) 
	{
		ArrayList<String> printsetlist = null;
        Cursor cursor = null;
        try{
        	
            String where = SF_COL_STUDENTID + " = ? "
            	    + " AND " + SF_COL_KYOKAID + " = ? " 
            		+ " AND " + SF_COL_KYOZAIID + " = ? " ;
        	
            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
					new String[]{ SF_COL_PRINTSETID },  
            		//条件式
                    where ,
					//条件
					new String[]{ studentid, kyokaID, kyozaiID},  
                    //group by
					SF_COL_PRINTSETID ,
                    //Having
                    null, 
                    //order by
                    SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                    //limit
                    null 
                    );
            
            // 検索結果をcursorから読み込んで返す
            printsetlist =  DB_GetPrintSetIDList_readCursor( cursor);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
        
        return printsetlist;
	}
	
	private static ArrayList<String> DB_GetPrintSetIDList_readCursor(Cursor cursor)
	{
		ArrayList<String> stringlist = new ArrayList<String>();

		
        while( cursor.moveToNext() ){
        	String work = cursor.getString( 0 );
        	stringlist.add(work);
        }
        return stringlist;
    }

	
	
	protected static boolean DB_UpdateResultData(SQLiteDatabase writable, DResultData resultdata)
	{
		boolean ret = false;
		String work;
		try{
			ContentValues values = new ContentValues(); 
	      	values.put(SF_COL_PRINTSETID, resultdata.mPrintSetID); 
	      	values.put(SF_COL_STATUS, resultdata.mStatus); 
	      	values.put(SF_COL_COUNT, resultdata.mCount); 
	      	values.put(SF_COL_GRADINGMETHOD, resultdata.mGradingMethod); 
	      	values.put(SF_COL_GRADINGSTATUS, resultdata.mGradingStatus); 
	      	values.put(SF_COL_LEARNINGPLACE, resultdata.mLearningPlace); 

        	//20140605 DEL-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
	      	//values.put(SF_COL_GRADINGRESULTDATA, resultdata.mGradingResultData); 
	      	//values.put(SF_COL_INKDATA, resultdata.mInkData); 
        	//20140605 DEL-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
	      	values.put(SF_COL_REDCOMMENT, resultdata.mRedComment); 
	      	values.put(SF_COL_TAGCOMMENT, resultdata.mTagComment); 
	      	values.put(SF_COL_TAGTEXT, resultdata.mTagText); 
	      	values.put(SF_COL_PENTHICKNESS, resultdata.mPenThickness); 
	      	values.put(SF_COL_GRADE, resultdata.mGrade); 
	      	values.put(SF_COL_SCORE, resultdata.mScore); 
			
			work = Utility.getFormatDate(resultdata.mStartDate);
	      	values.put(SF_COL_STARTDATE, work); 
			work = Utility.getFormatDate(resultdata.mEndDate);
	      	values.put(SF_COL_ENDDATE, work); 
	      	//20140521 DEL-S
			//values.put(SF_COL_ANSWERTIME, resultdata.mAnswerTime);
			//20140521 DEL-E
	      	
	      	values.put(SF_COL_ISREGIST, 1); 
	      	values.put(SF_COL_ISLEARNED, resultdata.mIsLearned); 
	      	values.put(SF_COL_ISGREAD, resultdata.mIsGreaded); 
	      	
			//20140521 ADD-S
			values.put(SF_COL_ANSWERTIME2, resultdata.mAnswerTime);
			//20140521 ADD-E
	      	
		    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
	      	values.put(SF_COL_SOUNDRECORDSTATUS, resultdata.mSoundRecordStatus); 
		    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
			
			if(writable.update(SF_TBLNAME, 
									values, 
									SF_COL_STUDENTID + " = ? " 
						            		+ " AND " + SF_COL_KYOKAID + " = ? "
						            		+ " AND " + SF_COL_KYOZAIID + " = ? "
						            		+ " AND " + SF_COL_PRINTUNITID + " = ? ", 
				                    new String[]{ resultdata.mStudentID, resultdata.mKyokaID, resultdata.mKyozaiID,	resultdata.mPrintUnitID} 
									) == 1) 
			{
				ret = true;
	        	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
				ret = TblInkData.DB_UpdateInkData(writable, resultdata);
				if(ret) {
					ret = TblGradingResultData.DB_UpdateGradingResultData(writable, resultdata);
				}
	        	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	protected static boolean DB_ClearRegistFlg(SQLiteDatabase writable, String studentid)
	{
		boolean ret = false;
		try{
			ContentValues values = new ContentValues(); 
			
	      	values.put(SF_COL_ISREGIST, 0); 
			
			if(writable.update(SF_TBLNAME, 
									values, 
									SF_COL_STUDENTID + " = ? " ,
				                    new String[]{ studentid } 
									) >= 0) {
				ret = true;
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	//20140617 ADD-S For　メモリ不足対応
	protected static boolean DB_ClearRegistFlgPrintSet(SQLiteDatabase writable, String studentid, String kyokaID, String kyozaiID, String printsetID)
	{
		boolean ret = false;
		try{
			ContentValues values = new ContentValues(); 
			
	      	values.put(SF_COL_ISREGIST, 0); 
			
	        String where = SF_COL_STUDENTID + " = ? "
	        	    + " AND " + SF_COL_KYOKAID + " = ? " 
	        		+ " AND " + SF_COL_KYOZAIID + " = ? "
	        		+ " AND " + SF_COL_PRINTSETID + " = ? "
	        		+ " AND " + SF_COL_ISREGIST + " = 1 ";
	        
			if(writable.update(SF_TBLNAME, 
									values, 
									where ,
									new String[]{ studentid, kyokaID, kyozaiID, printsetID}  
									) >= 0) {
				ret = true;
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	//20140617 ADD-E For　メモリ不足対応
	//20140718 ADD-S For Print単位送信
	protected static boolean DB_ClearRegistFlgPrintUnit(SQLiteDatabase writable, String studentid, String kyokaID, String kyozaiID, String printUnitID)
	{
		boolean ret = false;
		try{
			ContentValues values = new ContentValues(); 
			
	      	values.put(SF_COL_ISREGIST, 0); 
			
	        String where = SF_COL_STUDENTID + " = ? "
	        	    + " AND " + SF_COL_KYOKAID + " = ? " 
	        		+ " AND " + SF_COL_KYOZAIID + " = ? "
	        		+ " AND " + SF_COL_PRINTUNITID + " = ? "
	        		+ " AND " + SF_COL_ISREGIST + " = 1 ";
	        
			if(writable.update(SF_TBLNAME, 
									values, 
									where ,
									new String[]{ studentid, kyokaID, kyozaiID, printUnitID}  
									) >= 0) {
				ret = true;
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	//20140718 ADD-E For Print単位送信

	//20140617 ADD-S For　メモリ不足対応
	protected static ArrayList<DResultData> DB_GetPrintSetIDListForRegist(SQLiteDatabase readbledb, String studentid) 
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
        	String where = SF_COL_STUDENTID + " = ? "
                		+ " AND " + SF_COL_ISREGIST + " = 1 ";
                
        	cursor = readbledb.query( 
                		//Table名
                		SF_TBLNAME, 
                        //項目名
    					new String[]{ SF_COL_KYOKAID, SF_COL_KYOZAIID, SF_COL_PRINTSETID},  
                		//条件式
                        where ,
    					//条件
    					new String[]{ studentid },  
                        //group by
    					SF_COL_KYOKAID + " , " + SF_COL_KYOZAIID + " , " +  SF_COL_PRINTSETID,  
                        //Having
                        null, 
                        //order by
                        SF_COL_KYOKAORDERNO + " Desc , " + SF_COL_KYOZAIORDERNO + " Desc , " + 
                		SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                        //limit
                        null 
                        );
                
        	resultdatalist =  DB_GetPrintSetIDListForRegist_readCursor(cursor);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
        
        return resultdatalist;
	}
	
	private static ArrayList<DResultData> DB_GetPrintSetIDListForRegist_readCursor(Cursor cursor)
	{
		ArrayList<DResultData> resultdatalist = new ArrayList<DResultData>();
		
        while( cursor.moveToNext() ){
        	DResultData resultData = new DResultData();
        	resultData.mKyokaID = cursor.getString( 0 );
        	resultData.mKyozaiID = cursor.getString( 1 );
        	resultData.mPrintSetID = cursor.getString( 2 );
        	
        	resultdatalist.add(resultData);
        }
        return resultdatalist;
    }
	protected static ArrayList<DResultData> DB_GetRejistPrintSet(SQLiteDatabase readbledb, String studentid, String kyokaID, String kyozaiID,  String printsetID ) 
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
            String where = SF_COL_STUDENTID + " = ? "
            	    + " AND " + SF_COL_KYOKAID + " = ? " 
            		+ " AND " + SF_COL_KYOZAIID + " = ? "
            		+ " AND " + SF_COL_PRINTSETID + " = ? "
            		+ " AND " + SF_COL_ISREGIST + " = 1 ";
            
            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
                	null,		//全項目
            		//条件式
                    where ,
					//条件
					new String[]{ studentid, kyokaID, kyozaiID, printsetID},  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    SF_COL_SCHEDULEDDATE + " ASC , " + SF_COL_SCHEDULEDINDEX + " ASC " ,
                    //limit
                    null 
                    );
            
            // 検索結果をcursorから読み込んで返す
            resultdatalist =  DB_GetResultData_readCursor( cursor, false);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
        
    	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		if(resultdatalist != null) {
			for(int i = 0; i < resultdatalist.size(); i++) {
				DResultData resultData = resultdatalist.get(i);
				DResultData resultInkData = TblInkData.DB_GetInkData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				resultData.mInkData = resultInkData.mInkData;
				resultData.mInkBinary = resultInkData.mInkBinary;
				
				resultData.mGradingResultData = TblGradingResultData.DB_GetGradingResultData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				//20140731 ADD-S For 録音対応
				resultData.mSoundRecord = TblSoundRecordData.DB_GetSoundData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID, 0);
				//20140731 ADD-E For 録音対応
			}
		}
    	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
        
        return resultdatalist;
	}
	
	//20140617 ADD-E For　メモリ不足対応
    //20150409 ADD-S For 2015年度Ver. 未読コメント
    public static boolean ExistUnreadData(SQLiteDatabase readbledb, String studentid)
    {
    	boolean exist = false;
    	//条件　学習終了後且つ未読
    	String where = String.format( " %s = 10 AND %s = 1 AND %s = 0", TblResultData.SF_COL_STATUS, TblResultData.SF_COL_COMMENTUNREADFLG, SF_COL_PRINTTYPE);
    	Cursor cursor = readbledb.rawQuery(
    	        String.format("SELECT COUNT(*) FROM %s WHERE %s", TblResultData.SF_TBLNAME, where), null);
        int count = 0;
   	    if(cursor.moveToNext()){
   	        count = cursor.getInt(0);
   	    }
   	    cursor.close();

   	    if(count > 0) {
   	    	exist = true;
   	    }
		return exist;
    }
	protected static ArrayList<DResultData> DB_GetUnreadData(SQLiteDatabase readbledb, String studentid) 
	{
		ArrayList<DResultData> resultdatalist = null;
        Cursor cursor = null;
        try{
        	
            String where = SF_COL_STUDENTID + " = ? "
            		+ " AND " + SF_COL_STATUS + " = 10 " 
            		+ " AND " + SF_COL_COMMENTUNREADFLG + " <> 0 "
    				+ " AND " + SF_COL_PRINTTYPE + " = 0 " ;
            
            cursor = readbledb.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
            		null,		//全項目
            		//条件式
                    where ,
					//条件
					new String[]{ studentid },  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    //SF_COL_KYOKAORDERNO + " ASC , " + SF_COL_KYOZAIORDERNO + " DESC , "  + SF_COL_STARTDATE + " ASC , "+ SF_COL_PRINTNO + " ASC ",
                    SF_COL_PRINTSETDATE + " ASC , "+ SF_COL_PRINTNO + " ASC ",
                    //limit
                    null 
                    );
            
            resultdatalist =  DB_GetResultData_readCursor( cursor, false);
            
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
    	//20140605 ADD-S For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
		if(resultdatalist != null) {
			for(int i = 0; i < resultdatalist.size(); i++) {
				DResultData resultData = resultdatalist.get(i);
				DResultData resultInkData = TblInkData.DB_GetInkData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
				resultData.mInkData = resultInkData.mInkData;
				resultData.mInkBinary = resultInkData.mInkBinary;
				
				resultData.mGradingResultData = TblGradingResultData.DB_GetGradingResultData(readbledb, studentid, resultData.mKyokaID, resultData.mKyozaiID, resultData.mPrintUnitID);
			}
		}
    	//20140605 ADD-E For　大量データ対応（Ｉｎｋとテスト結果を別テーブルに）
        
        return resultdatalist;
	}

    //20150409 ADD-S For 2015年度Ver. 未読コメント
	protected static boolean DB_UpdateunreadCommentFlg(SQLiteDatabase writable, DResultData resultdata)
	{
		boolean ret = false;
		try{
			ContentValues values = new ContentValues(); 
			
	      	values.put(SF_COL_COMMENTUNREADFLG, 0); 
			
			if(writable.update(SF_TBLNAME, 
					values, 
					SF_COL_STUDENTID + " = ? " 
		            		+ " AND " + SF_COL_KYOKAID + " = ? "
		            		+ " AND " + SF_COL_KYOZAIID + " = ? "
		            		+ " AND " + SF_COL_PRINTUNITID + " = ? ", 
                    new String[]{ resultdata.mStudentID, resultdata.mKyokaID, resultdata.mKyozaiID,	resultdata.mPrintUnitID} 
					) == 1) 
			{
				ret = true;
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
    //20150409 ADD-E For 2015年度Ver. 未読コメント
	
}
