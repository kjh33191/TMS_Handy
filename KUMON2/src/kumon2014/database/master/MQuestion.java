package kumon2014.database.master;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;


//教材マスターテーブル
public class MQuestion extends MastDBIO {
	// テーブル名 
	public static final String SF_TBLNAME = "M_Question";
	
	// カラム 
	public static final String SF_COL_PRINTID 		= "CPrintID";		//Text 
	public static final String SF_COL_KYOKAID 		= "CKyokaID";		//Text 
	public static final String SF_COL_KYOZAIID 		= "CKyozaiID";		//Text 
	public static final String SF_COL_PRINTNO 		= "CPrintNo";		//integer 
	public static final String SF_COL_MDTDATA 		= "CMDtData";		//blob 
	public static final String SF_COL_IMAGEA 		= "CImageA";		//blob 
	public static final String SF_COL_IMAGEB 		= "CImageB";		//blob 
	//Index
	public static final int SF_IDX_PRINTID 			= 0;
	public static final int SF_IDX_KYOKAID 			= 1;
	public static final int SF_IDX_KYOZAIID 		= 2;
	public static final int SF_IDX_PRINTNO 			= 3;
	public static final int SF_IDX_MDTDATA			= 4;
	public static final int SF_IDX_IMAGEA			= 5;
	public static final int SF_IDX_IMAGEB			= 6;
	
//	return cursor.getBlob(0);

	public static final String SF_CREATE_TBL_SQL_QUESTION =
				"create table " + SF_TBLNAME + "( " 
						+ SF_COL_PRINTID 		+ " text not null, "
						+ SF_COL_KYOKAID 		+ " text, "
						+ SF_COL_KYOZAIID 		+ " text, "
						+ SF_COL_PRINTNO 		+ " integer, "
						+ SF_COL_MDTDATA 		+ " blob, " 
						+ SF_COL_IMAGEA 		+ " blob, " 
						+ SF_COL_IMAGEB 		+ " blob, " 
						+ " primary key( " + SF_COL_PRINTID + ")"
						+ " );";

	///////////////////////////////////////////////////////////////////////////////////
	public String	mPrintID = "";
	public String	mKyokaID = "";
	public String	mKyozaiID = "";
	public int		mPrintNo = 0;
	public byte[]	mMDTData = null;
	public byte[]	mImageA = null;
	public byte[]	mImageB = null;
	
	public MQuestion()
	{
	}
	public void ClearAll()
	{
		mMDTData = null;
		mImageA = null;
		mImageB = null;
	}
	public boolean equals(Object obj) {
        // オブジェクトがnullでないこと
        if (obj == null) {
            return false;
        }
        // オブジェクトが同じ型であること
        if (!(obj instanceof MQuestion)) {
            return false;
        }
        // 同値性を比較
        boolean stat = false;
        if(this.mPrintID == ((MQuestion)obj).mPrintID
        		&& this.mKyokaID.equals(((MQuestion)obj).mKyokaID)
                		&& this.mKyozaiID.equals(((MQuestion)obj).mKyozaiID)
                        		&& this.mPrintNo == ((MQuestion)obj).mPrintNo)
        {
        	stat = true;
        }
        
        return stat;
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
	protected static boolean DB_AddPrint(SQLiteDatabase writabledb, MQuestion question) 
	{
		boolean ret = false;
		
		try{
			ContentValues values = new ContentValues(); 
			values.put(SF_COL_PRINTID, question.mPrintID); 
			values.put(SF_COL_KYOKAID, question.mKyokaID); 
			values.put(SF_COL_KYOZAIID, question.mKyozaiID); 
			values.put(SF_COL_PRINTNO, question.mPrintNo); 
			values.put(SF_COL_MDTDATA, question.mMDTData); 
			values.put(SF_COL_IMAGEA, question.mImageA); 
			values.put(SF_COL_IMAGEB, question.mImageB); 

			if(writabledb.insert(SF_TBLNAME, null, values) >= 0) {
				ret = true;
			}                                 
			else {
				ret = false;
			}
			values = null;
		      	
		}
		catch(Exception e) {
		}
		return ret;
	}
	//Get Print
	protected static MQuestion DB_GetPrint(SQLiteDatabase readbledb, String printid) 
	{
		MQuestion question = null;
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
          question =  DB_GetQuestion_readCursor( cursor );
      }
      catch(Exception e) {
      }
      finally{
          // Cursorを忘れずにcloseする
          if( cursor != null ){
              cursor.close();
          }
      }
      
      return question;
	}
	public static MQuestion DB_GetPrintByPrintNo(SQLiteDatabase readbledb, String kyoka, String kyozai, int printno)  {
		MQuestion question = null;
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
          question =  DB_GetQuestion_readCursor( cursor );
		}
		catch(Exception e) {
		}
		finally{
			// Cursorを忘れずにcloseする
			if( cursor != null ){
				cursor.close();
			}
		}
      
		return question;
	}
	
	private static MQuestion DB_GetQuestion_readCursor(Cursor cursor)
	{
		MQuestion question = null;
//		byte[] buff;
//		byte[] mQuestionData = null;
        while( cursor.moveToNext() ){
        	if(question == null) {
        		question = new MQuestion();
        	}
			question.mPrintID = cursor.getString(SF_IDX_PRINTID);
			question.mKyokaID = cursor.getString(SF_IDX_KYOKAID);
			question.mKyozaiID = cursor.getString(SF_IDX_KYOZAIID);
			question.mPrintNo = cursor.getInt(SF_IDX_PRINTNO);

			question.mMDTData = cursor.getBlob(SF_IDX_MDTDATA);
			question.mImageA = cursor.getBlob(SF_IDX_IMAGEA);
			question.mImageB = cursor.getBlob(SF_IDX_IMAGEB);
			
		}
//        buff = null;
//        mQuestionData = null;
		return question;
	}
	protected static boolean DB_IsExistQuestion(SQLiteDatabase readbledb, String printid)
	{
		boolean ret = false;
		Cursor cursor = null;
		try{
          cursor = readbledb.query( 
          		//Table名
        		  SF_TBLNAME, 
                  //項目名
                  new String[]{ SF_COL_PRINTID },  
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

          if( cursor.moveToNext() ){
          	ret = true;
          }
      }
      catch(Exception e) {
//    	  String s = e.getMessage();
      }
      finally{
          // Cursorを忘れずにcloseする
          if( cursor != null ){
              cursor.close();
          }
      }
      
      return ret;
		
	}
	protected static boolean DB_IsExistQuestionByNo(SQLiteDatabase readbledb, String kyokaid, String kyozaiid, int printno)
	{
        String where = SF_COL_KYOKAID + " = ? " 
            		+ " AND " + SF_COL_KYOZAIID + " = ? "
            		+ " AND " + SF_COL_PRINTNO + " = ? ";
		boolean ret = false;
		Cursor cursor = null;
		try{
          cursor = readbledb.query( 
          		//Table名
        		  SF_TBLNAME, 
                  //項目名
                  new String[]{ SF_COL_PRINTID },  
          		//条件式
                  where, 
          		//条件
                  new String[]{ kyokaid, kyozaiid, Integer.toString(printno) },  
                  //group by
                  null,
                  //Having
                  null, 
                  //order by
                  null,
                  //limit
                  null
                  );

          if( cursor.moveToNext() ){
          	ret = true;
          }
      }
      catch(Exception e) {
//    	  String s = e.getMessage();
      }
      finally{
          // Cursorを忘れずにcloseする
          if( cursor != null ){
              cursor.close();
          }
      }
      
      return ret;
		
	}
	
	protected static boolean DB_InserQuestionDataList(SQLiteDatabase writabledb, ArrayList<MQuestion> questionlist) 
	{
		boolean ret = true;
		try{

			for(int i = 0; i < questionlist.size(); i++) {
				MQuestion question = questionlist.get(i);
				
				DB_DeleteByPrintID(writabledb, question.mPrintID) ;

				ret = DB_AddPrint(writabledb, question) ;
				if(ret == false) {
					break;
				}
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	protected static boolean DB_InserQuestionData(SQLiteDatabase writabledb, MQuestion question) 
	{
		boolean ret = false;
		try{
			DB_DeleteByPrintID(writabledb, question.mPrintID) ;
			
			ret = DB_AddPrint(writabledb, question) ;
		}
		catch(Exception e) {
		}
		return ret;
	}

	
}
