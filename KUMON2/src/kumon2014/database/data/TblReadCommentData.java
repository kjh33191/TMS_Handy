package kumon2014.database.data;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;

import kumon2014.kumondata.DResultData;
import android.content.ContentValues;
import android.database.Cursor;

public class TblReadCommentData {
	// テーブル名
	public static final String SF_TBLNAME = "D_ReadCommentData";

	// カラム
	public static final String SF_COL_STUDENTID = "CStudentID"; // Text
	public static final String SF_COL_PRINTUNITID = "CPrintUnitID"; // Text

	public static final String SF_CREATE_TBL_SQL_REDCOMMENTDATA = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";
	
	// クリアALL
	protected static boolean DB_ClearAll(SQLiteDatabase Writabledb) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, null, null);
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By Studentid
	protected static boolean DB_DeleteByStudentID(SQLiteDatabase Writabledb, String studentid) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, SF_COL_STUDENTID + " = ? ",
					new String[] { studentid });
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By PrintUnit
	protected static boolean DB_DeleteByPrintUnit(SQLiteDatabase Writabledb, String studentid, String printunit) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, SF_COL_STUDENTID + " = ? " + " AND "
					 + SF_COL_PRINTUNITID + " = ? ",
					new String[] { studentid, printunit });
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Insert
	protected static boolean DB_Insert(SQLiteDatabase Writabledb, DResultData resultdata) {
		boolean ret = false;
		try {
			ContentValues values = new ContentValues(); 
				
			values.put(SF_COL_STUDENTID, resultdata.mStudentID);
			values.put(SF_COL_PRINTUNITID,	resultdata.mPrintUnitID);
			
			long rets = Writabledb.insert(SF_TBLNAME, null, values);
			if (rets >= 0) {
				ret = true;
			}
		} catch (Exception e) {
//			String s = e.toString();
		}
		return ret;
	}

	protected static ArrayList<String> DB_GetReadCommentDataList(SQLiteDatabase readbledb, String studentid) {
		ArrayList<String> printunitlist = new  ArrayList<String>();
		
		Cursor cursor = null;
		try {
			String cond = "";
			String[] where = null;

			cond = SF_COL_STUDENTID + " = ? ";
			where = new String[] { studentid };

			String[] columns = {SF_COL_STUDENTID, SF_COL_PRINTUNITID };
			
			cursor = readbledb.query(
			// Table名
					SF_TBLNAME,
					// 項目名
					columns, 
					// 条件式
					cond,
					// 条件
					where,
					// group by
					null,
					// Having
					null,
					// order by
					null, 
					// limit
					null);

			// 検索結果をcursorから読み込んで返す
			printunitlist = DB_ReadComment_readCursor(cursor) ;

		} catch (Exception e) {
//			String s = e.getMessage();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return printunitlist;
	}


	private static ArrayList<String> DB_ReadComment_readCursor(Cursor cursor) {
		ArrayList<String> printunitlist = new  ArrayList<String>();
		
        while( cursor.moveToNext() ){
        	String printunitid = cursor.getString(1);
        	printunitlist.add(printunitid);
        }
        return printunitlist;
		
	}
	
}