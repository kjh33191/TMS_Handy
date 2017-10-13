package kumon2014.database.master;

import java.io.Serializable;
import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;


//教材マスターテーブル
public class MQuestionImage extends MastDBIO implements Serializable {
	private static final long serialVersionUID = 1L;

	// テーブル名 
	public static final String SF_TBLNAME = "M_QuestionImage";
	
	// カラム 
	public static final String SF_COL_PRINTID 		= "CPrintID";		//Text 
	public static final String SF_COL_KYOKAID 		= "CKyokaID";		//Text 
	public static final String SF_COL_KYOZAIID 		= "CKyozaiID";		//Text 
	public static final String SF_COL_PRINTNO 		= "CPrintNo";		//integer 
	public static final String SF_COL_PAGENO 		= "CPageNo";		//integer 
	public static final String SF_COL_IMAGE 		= "CImage";			//blob 
	//Index
	public static final int SF_IDX_PRINTID 			= 0;
	public static final int SF_IDX_KYOKAID 			= 1;
	public static final int SF_IDX_KYOZAIID 		= 2;
	public static final int SF_IDX_PRINTNO 			= 3;
	public static final int SF_IDX_PAGENO			= 4;
	public static final int SF_IDX_IMAGE			= 5;
	
//	return cursor.getBlob(0);

	public static final String SF_CREATE_TBL_SQL_QUESTION =
				"create table " + SF_TBLNAME + "( " 
						+ SF_COL_PRINTID 		+ " text not null, "
						+ SF_COL_KYOKAID 		+ " text, "
						+ SF_COL_KYOZAIID 		+ " text, "
						+ SF_COL_PRINTNO 		+ " integer, "
						+ SF_COL_PAGENO 		+ " integer, " 
						+ SF_COL_IMAGE 			+ " blob, " 
						+ " primary key( " + SF_COL_PRINTID + " , " + SF_COL_KYOKAID + " , " + SF_COL_KYOZAIID  + " , " + SF_COL_PAGENO  + " ) "
						+ " );";

	///////////////////////////////////////////////////////////////////////////////////
	public String	mPrintID = "";
	public String	mKyokaID = "";
	public String	mKyozaiID = "";
	public int		mPrintNo = 0;
	public int		mPageNo = 0;
	public byte[]	mImage = null;
	
	public MQuestionImage()
	{
		ClearAll();
	}
	public void ClearAll()
	{
		mPrintID = "";
		mKyokaID = "";
		mKyozaiID = "";
		mPrintNo = 0;
		mPageNo = 0;
		mImage = null;
	}
	
	//クリアALL
	protected static boolean DB_ClearAll(SQLiteDatabase writabledb) 
	{
		boolean ret = false;
		try {
			writabledb.delete(SF_TBLNAME, null, null);		
			ret = true;
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//Delete Print
	protected static boolean DB_DeleteByPrintID(SQLiteDatabase writabledb, String printid) 
	{
		boolean ret = false;
		try {
			if(writabledb.delete(SF_TBLNAME, 
								SF_COL_PRINTID + " = ?",
								new String[]{ printid }) >= 0) {
				ret = true;
			}
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//Add Print
	protected static boolean DB_AddPrintImage(SQLiteDatabase writabledb, String printID, String kyokaID, String kyozaiID, int printNo, ArrayList<MQuestionImage> imagelist)
	{
		boolean ret = false;
		
		try{
			for(int i = 0; i < imagelist.size(); i++) {
				ContentValues values = new ContentValues(); 
				MQuestionImage image = imagelist.get(i);
				values.put(SF_COL_PRINTID, printID); 
				values.put(SF_COL_KYOKAID, kyokaID); 
				values.put(SF_COL_KYOZAIID, kyozaiID); 
				values.put(SF_COL_PRINTNO, printNo); 
				values.put(SF_COL_PAGENO, image.mPageNo); 
				values.put(SF_COL_IMAGE, image.mImage); 
	
				if(writabledb.insert(SF_TBLNAME, null, values) >= 0) {
					ret = true;
				}                                 
				else {
					ret = false;
					break;
				}
				values = null;
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	//Get Print
	protected static ArrayList<MQuestionImage> DB_GetPrintImage(SQLiteDatabase readbledb, String printid) 
	{
		ArrayList<MQuestionImage> imagelist = new  ArrayList<MQuestionImage>();		
		Cursor cursor = null;
		try{
          cursor = readbledb.query( 
          		//Table名
        		  SF_TBLNAME, 
                  //項目名
                  null,		//全項目
          		//条件式
                  SF_COL_PRINTID + " = ?", 
          		//条件
                  new String[]{ printid },  
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
          imagelist =  DB_GetQuestionImage_readCursor( cursor );
      }
      catch(Exception e) {
      }
      finally{
          // Cursorを忘れずにcloseする
          if( cursor != null ){
              cursor.close();
          }
      }
      
      return imagelist;
	}
	public static ArrayList<MQuestionImage> DB_GetPrintImageByPrintNo(SQLiteDatabase readbledb, String kyoka, String kyozai, int printno)  {
		ArrayList<MQuestionImage> imagelist = new  ArrayList<MQuestionImage>();		
		Cursor cursor = null;
		try{
          cursor = readbledb.query( 
          		//Table名
        		  SF_TBLNAME, 
                  //項目名
                  null,		//全項目
          		//条件式
                  SF_COL_KYOKAID + " = ?" + 
                  " AND " + SF_COL_KYOZAIID + " = ?" +  
                  " AND " + SF_COL_PRINTNO + " = ?", 
          		//条件
                  new String[]{ kyoka, kyozai, Integer.toString(printno)},  
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
          imagelist =  DB_GetQuestionImage_readCursor( cursor );
		}
		catch(Exception e) {
		}
		finally{
			// Cursorを忘れずにcloseする
			if( cursor != null ){
				cursor.close();
			}
		}
      
		return imagelist;
	}
	
	private static ArrayList<MQuestionImage> DB_GetQuestionImage_readCursor(Cursor cursor)
	{
		ArrayList<MQuestionImage> imagelist = new  ArrayList<MQuestionImage>();		
		MQuestionImage image = null;
        while( cursor.moveToNext() ){
        	image = new MQuestionImage();
        	image.mPrintID = cursor.getString(SF_IDX_PRINTID);
        	image.mKyokaID = cursor.getString(SF_IDX_KYOKAID);
        	image.mKyozaiID = cursor.getString(SF_IDX_KYOZAIID);
        	image.mPrintNo = cursor.getInt(SF_IDX_PRINTNO);

        	image.mPageNo = cursor.getInt(SF_IDX_PAGENO);
        	image.mImage = cursor.getBlob(SF_IDX_IMAGE);
			
        	imagelist.add(image);
		}
		return imagelist;
	}
}
