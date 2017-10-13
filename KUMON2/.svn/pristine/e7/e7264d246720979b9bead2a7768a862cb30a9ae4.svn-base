package kumon2014.database.data;


import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import android.content.Context; 

// SQLiteOpenHelper 
public class TempDataSQLHelper extends SQLiteOpenHelper { 
	//本ＤＢは、データ受信時のロールバック用です
	//各テーブル構造は、DataSQLHelperと全くの同じにしてください
	//用途
	//受信前に該当する学習者のデータをTempDataSQLHelperへコピー
	//DataSQLHelperの該当する学習者のデータを削除
	//受信処理
	//正常時は、TempDataSQLHelperのデータをクリア
	//エラー時・キャンセル時は、再度DataSQLHelperの該当する学習者のデータを削除し
	//TempDataSQLHelperのデータをDataSQLHelperへコピー後、TempDataSQLHelperをクリア
	
	//private static final int DATABASE_VERSION = 4; 
	//V10.1以降
	//private static final int DATABASE_VERSION = 5; 
	//V10.3以降 未読コメントFlg追加
	//private static final int DATABASE_VERSION = 6; 
	//V10.3以降 PrintSetDate追加
	private static final int DATABASE_VERSION = 7; 

	// データベス名 
	public static final String DATABASE_NAME = StudentClientCommData.getLocalDBFolder() +  "/TempDATA.db"; 
	// コンストラクタ
	public TempDataSQLHelper(Context context) 
	{ 
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	} 
	@Override 
	public void onCreate(SQLiteDatabase db) 
	{ 
		//下記は、DataSQLHelperと同じにする事
		String sql;
		try {
			//Result Table
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
			db.execSQL(sql); 

			//Ink Table
			//20150416 MOD-S InkData To Binary
  			//sql = TblInkData.SF_CREATE_TBL_SQL_INKDATA_V4;		//V4
			sql = TblInkData.SF_CREATE_TBL_SQL_INKDATA_V6;		//V6
			//20150416 MOD-E InkData To Binary
			db.execSQL(sql); 

			//Grading Result Table
			sql = TblGradingResultData.SF_CREATE_TBL_SQL_GRADINGRESULT;
			db.execSQL(sql); 
			
		}
		catch(Exception e) {
//			String s = e.getMessage();
		}
	} 
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{ 
		//下記は、DataSQLHelperと同じにする事
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