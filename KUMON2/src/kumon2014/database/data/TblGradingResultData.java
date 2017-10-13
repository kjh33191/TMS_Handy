package kumon2014.database.data;


import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;

import kumon2014.kumondata.DResultData;
import android.content.ContentValues;
import android.database.Cursor;

public class TblGradingResultData {
	// テーブル名
	public static final String SF_TBLNAME = "D_GradingResultData";

	// カラム
	public static final String SF_COL_STUDENTID = "CStudentID"; // Text
	public static final String SF_COL_KYOKAID = "CKyokaID"; // Text
	public static final String SF_COL_KYOZAIID = "CKyozaiID"; // Text
	public static final String SF_COL_PRINTUNITID = "CPrintUnitID"; // Text
	
	public static final String SF_COL_COUNT = "CCount"; // integer
	public static final String SF_COL_GRADINGRESULTDATA = "CGradingResultData"; // Text


	public static final String SF_CREATE_TBL_SQL_GRADINGRESULT = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_KYOKAID + " text not null,  " 
			+ SF_COL_KYOZAIID + " text not null, " 
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ SF_COL_COUNT + " integer,  " 
			+ SF_COL_GRADINGRESULTDATA + " text,  " 
			
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID
			+ " , " + SF_COL_KYOZAIID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";

	
	// /////////////////////////////////////////////////////////////////////////////////
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
	protected static boolean DB_DeleteByPrintUnit(SQLiteDatabase Writabledb, String studentid, String kyoka, String kyozai, String printunit) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, SF_COL_STUDENTID + " = ? " + " AND "
					+ SF_COL_KYOKAID + " = ? " + " AND " + SF_COL_KYOZAIID
					+ " = ? " + " AND " + SF_COL_PRINTUNITID + " = ? ",
					new String[] { studentid, kyoka, kyozai, printunit });
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By KyozaiID
	protected static boolean DB_DeleteByKyozaiID(SQLiteDatabase Writabledb,	String studentid, String kyoka, String kyozai) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, SF_COL_STUDENTID + " = ? " + " AND "
					+ SF_COL_KYOKAID + " = ? " + " AND " + SF_COL_KYOZAIID
					+ " = ? " + " AND " + SF_COL_COUNT + " != 0 ",
					new String[] { studentid, kyoka, kyozai });
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Insert
	protected static boolean DB_InsertGradingResultDataList(SQLiteDatabase Writabledb, ArrayList<DResultData> resultdatalist) {
		boolean ret = false;
		long cnt = 0;
		try {
			for (int i = 0; i < resultdatalist.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(SF_COL_STUDENTID, resultdatalist.get(i).mStudentID);
				values.put(SF_COL_KYOKAID, resultdatalist.get(i).mKyokaID);
				values.put(SF_COL_KYOZAIID, resultdatalist.get(i).mKyozaiID);
				values.put(SF_COL_PRINTUNITID,	resultdatalist.get(i).mPrintUnitID);
				values.put(SF_COL_COUNT, resultdatalist.get(i).mCount);
				values.put(SF_COL_GRADINGRESULTDATA, resultdatalist.get(i).mGradingResultData);
			
				long rets = Writabledb.insert(SF_TBLNAME, null, values);
				if (rets >= 0) {
					cnt++;
				} else {
					break;
				}
			}
			if (cnt == resultdatalist.size()) {
				ret = true;
			}
		} catch (Exception e) {
//			String s = e.toString();
		}
		return ret;
	}

	protected static String DB_GetGradingResultData(SQLiteDatabase readbledb, String studentid, String KyokaID, String KyozaiID, String PrintUnitID) {
		String inkdate = "";
		Cursor cursor = null;
		try {
			String cond = "";
			String[] where = null;

			cond = SF_COL_STUDENTID + " = ? " + " AND "
					+ SF_COL_KYOKAID + " = ? " + " AND "
					+ SF_COL_KYOZAIID + " = ? " + " AND "
					+ SF_COL_PRINTUNITID + " = ? ";
			
			where = new String[] { studentid, KyokaID, KyozaiID, PrintUnitID };

			String[] columns = {SF_COL_GRADINGRESULTDATA };
			
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
			inkdate = DB_GradingResultData_readCursor(cursor, false);

		} catch (Exception e) {
//			String s = e.getMessage();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return inkdate;
	}


	private static String DB_GradingResultData_readCursor(Cursor cursor, boolean alldata) {
		
		String gradingresultdata = "";
		while (cursor.moveToNext()) {
			gradingresultdata = cursor.getString(0);
		}
		return gradingresultdata;
	}
	protected static boolean DB_UpdateGradingResultData(SQLiteDatabase writable, DResultData resultdata) {
		boolean ret = false;
		try {
			ContentValues values = new ContentValues();
			values.put(SF_COL_COUNT, resultdata.mCount);
			values.put(SF_COL_GRADINGRESULTDATA, resultdata.mGradingResultData);

			if (writable.update(SF_TBLNAME, values, SF_COL_STUDENTID + " = ? "
					+ " AND " + SF_COL_KYOKAID + " = ? " + " AND "
					+ SF_COL_KYOZAIID + " = ? " + " AND " 
					+ SF_COL_PRINTUNITID + " = ? "
					, new String[] { resultdata.mStudentID,
					resultdata.mKyokaID, resultdata.mKyozaiID,
					resultdata.mPrintUnitID }) == 1) {
				ret = true;
			}
		} catch (Exception e) {
		}
		return ret;
	}
	
}
