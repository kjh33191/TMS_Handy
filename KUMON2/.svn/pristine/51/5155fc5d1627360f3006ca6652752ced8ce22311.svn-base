package kumon2014.database.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.sqlcipher.database.SQLiteDatabase;

import kumon2014.common.Utility;

import android.content.ContentValues;
import android.database.Cursor;


//教材マスターテーブル
public class MQuestion2 extends MastDBIO implements Serializable {
	private static final long serialVersionUID = 1L;

	// テーブル名 
	public static final String SF_TBLNAME = "M_Question2";
	
	// カラム 
	public static final String SF_COL_PRINTID 		= "CPrintID";		//Text 
	public static final String SF_COL_KYOKAID 		= "CKyokaID";		//Text 
	public static final String SF_COL_KYOZAIID 		= "CKyozaiID";		//Text 
	public static final String SF_COL_PRINTNO 		= "CPrintNo";		//integer 
	public static final String SF_COL_MDTDATA 		= "CMDtData";		//blob 
    //20150121 ADD-S For 2015年度Ver. 教材更新
	public static final String SF_COL_UPDATETIME 	= "CUpdateTime";	//Text 
    //20150121 ADD-E For 2015年度Ver. 教材更新

	//Index
	public static final int SF_IDX_PRINTID 			= 0;
	public static final int SF_IDX_KYOKAID 			= 1;
	public static final int SF_IDX_KYOZAIID 		= 2;
	public static final int SF_IDX_PRINTNO 			= 3;
	public static final int SF_IDX_MDTDATA			= 4;
    //20150121 ADD-S For 2015年度Ver. 教材更新
	public static final int SF_IDX_UPDATETIME		= 5;
    //20150121 ADD-E For 2015年度Ver. 教材更新
	
//	return cursor.getBlob(0);

	public static final String SF_CREATE_TBL_SQL_QUESTION =
				"create table " + SF_TBLNAME + "( " 
						+ SF_COL_PRINTID 		+ " text not null, "
						+ SF_COL_KYOKAID 		+ " text, "
						+ SF_COL_KYOZAIID 		+ " text, "
						+ SF_COL_PRINTNO 		+ " integer, "
						+ SF_COL_MDTDATA 		+ " blob, " 
						+ " primary key( " + SF_COL_PRINTID + ")"
						+ " );";
	
	public static final String SF_CREATE_TBL_SQL_QUESTION_V2 =
			"create table " + SF_TBLNAME + "( " 
					+ SF_COL_PRINTID 		+ " text not null, "
					+ SF_COL_KYOKAID 		+ " text, "
					+ SF_COL_KYOZAIID 		+ " text, "
					+ SF_COL_PRINTNO 		+ " integer, "
					+ SF_COL_MDTDATA 		+ " blob, " 
					+ SF_COL_UPDATETIME		+ " text, "
					+ " primary key( " + SF_COL_PRINTID + ")"
					+ " );";

	///////////////////////////////////////////////////////////////////////////////////
	public String	mPrintID = "";
	public String	mKyokaID = "";
	public String	mKyozaiID = "";
	public int		mPrintNo = 0;
	public byte[]	mMDTData = null;
	public ArrayList<MQuestionImage> mImageList = null;
	public ArrayList<MQuestionSound> mSoundList = null;
	
	//20150121 ADD-S For 2015年度Ver. 教材更新
	public Date		mUpdateTime = null;
	//20150121 ADD-E For 2015年度Ver. 教材更新

	
	//Work
	public byte[]	mQuestionData = null;
	
	public MQuestion2()
	{
		ClearAll();
	}
	public void ClearAll()
	{
		mMDTData = null;
		mImageList = null;
		mSoundList = null;
		mQuestionData = null;
		mUpdateTime = null;
	}
	@Override
	public boolean equals(Object obj) {
        // オブジェクトがnullでないこと
        if (obj == null) {
            return false;
        }
        // オブジェクトが同じ型であること
        if (!(obj instanceof MQuestion2)) {
            return false;
        }
        // 同値性を比較
        boolean stat = false;
        if(this.mPrintID == ((MQuestion2)obj).mPrintID
        		&& this.mKyokaID.equals(((MQuestion2)obj).mKyokaID)
                		&& this.mKyozaiID.equals(((MQuestion2)obj).mKyozaiID)
                        		&& this.mPrintNo == ((MQuestion2)obj).mPrintNo)
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
			MQuestionImage.DB_ClearAll(writabledb);
			MQuestionSound.DB_ClearAll(writabledb);
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
			if(ret == true) {
				MQuestionImage.DB_DeleteByPrintID(writabledb, printid);
				MQuestionSound.DB_DeleteByPrintID(writabledb, printid);
			}
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	//Add Print
	protected static boolean DB_AddPrint(SQLiteDatabase writabledb, MQuestion2 question) 
	{
		boolean ret = false;
		String work = null;
		
		try{
			ContentValues values = new ContentValues(); 
			values.put(SF_COL_PRINTID, question.mPrintID); 
			values.put(SF_COL_KYOKAID, question.mKyokaID); 
			values.put(SF_COL_KYOZAIID, question.mKyozaiID); 
			values.put(SF_COL_PRINTNO, question.mPrintNo); 
			values.put(SF_COL_MDTDATA, question.mMDTData); 
	        //20150121 ADD-S For 2015年度Ver. 教材更新
			work = Utility.getFormatDate(question.mUpdateTime);
	      	values.put(SF_COL_UPDATETIME, work); 
	        //20150121 ADD-E For 2015年度Ver. 教材更新

			if(writabledb.insert(SF_TBLNAME, null, values) >= 0) {
				ret = true;
			}                                 
			else {
				ret = false;
			}
			values = null;
			
			if(ret == true) {
				/*ret =*/ MQuestionImage.DB_AddPrintImage(writabledb, question.mPrintID, question.mKyokaID, question.mKyozaiID, question.mPrintNo, question.mImageList);
			}
			if(ret == true) {
				/*ret =*/ MQuestionSound.DB_AddPrintSound(writabledb, question.mPrintID, question.mKyokaID, question.mKyozaiID, question.mPrintNo, question.mSoundList);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	//Get Print
	protected static MQuestion2 DB_GetPrint(SQLiteDatabase readbledb, String printid) 
	{
		MQuestion2 question = null;
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
          if(question != null) {
        	  question.mImageList = MQuestionImage.DB_GetPrintImage(readbledb, printid);
        	  question.mSoundList = MQuestionSound.DB_GetPrintSound(readbledb, printid);
          }
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
	public static MQuestion2 DB_GetPrintByPrintNo(SQLiteDatabase readbledb, String kyoka, String kyozai, int printno)  {
		MQuestion2 question = null;
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
          
          if(question != null) {
        	  question.mImageList = MQuestionImage.DB_GetPrintImageByPrintNo(readbledb, kyoka, kyozai, printno);
        	  question.mSoundList = MQuestionSound.DB_GetPrintSoundByPrintNo(readbledb, kyoka, kyozai, printno);
          }
          
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
	
	private static MQuestion2 DB_GetQuestion_readCursor(Cursor cursor)
	{
		MQuestion2 question = null;
		String work = null;
        while( cursor.moveToNext() ){
        	if(question == null) {
        		question = new MQuestion2();
        	}
			question.mPrintID = cursor.getString(SF_IDX_PRINTID);
			question.mKyokaID = cursor.getString(SF_IDX_KYOKAID);
			question.mKyozaiID = cursor.getString(SF_IDX_KYOZAIID);
			question.mPrintNo = cursor.getInt(SF_IDX_PRINTNO);

			question.mMDTData = cursor.getBlob(SF_IDX_MDTDATA);
			
		    //20150121 ADD-S For 2015年度Ver. 教材更新
        	work = cursor.getString( SF_IDX_UPDATETIME );
        	question.mUpdateTime = Utility.getDateFromString(work);
		    //20150121 ADD-E For 2015年度Ver. 教材更新
		}
		return question;
	}
    //20150121 MOD-S For 2015年度Ver. 教材更新
	//protected static boolean DB_IsExistQuestion(SQLiteDatabase readbledb, String printid)
	protected static boolean DB_IsExistQuestion(SQLiteDatabase readbledb, String printid,  int leraningcount, Date printupdatetime)
    //20150121 MOD-E For 2015年度Ver. 教材更新
	{
		boolean ret = false;
		Cursor cursor = null;
		try{
          cursor = readbledb.query( 
          		//Table名
        		  SF_TBLNAME, 
                  //項目名
                  new String[]{ SF_COL_PRINTID, SF_COL_UPDATETIME },  
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
        	  //20150121 MOD-S For 2015年度Ver. 教材更新
        	  //ret = true;
        	  if(leraningcount == 0 && printupdatetime != null) {
            	  String work = cursor.getString(1);
            	  Date dt = Utility.getDateFromString(work);
            	  if(dt == null) {
                	  ret = false;
            	  }
            	  else {
            		  if(dt.compareTo(printupdatetime) < 0) {
                    	  ret = false;
            		  }
            		  else {
                    	  ret = true;
            		  }
            	  }
        	  }
        	  else {
            	  ret = true;
        	  }
        	  //20150121 MOD-E For 2015年度Ver. 教材更新
        	  
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
	
	protected static boolean DB_InserQuestionDataList(SQLiteDatabase writabledb, ArrayList<MQuestion2> questionlist) throws Exception 
	{
		boolean ret = true;
		try{

			for(int i = 0; i < questionlist.size(); i++) {
				MQuestion2 question = questionlist.get(i);
				
//				question = KumonCommon.DecompressQuestion(question);
				
				DB_DeleteByPrintID(writabledb, question.mPrintID) ;

				ret = DB_AddPrint(writabledb, question) ;
				if(ret == false) {
					break;
				}
			}
		}
		catch(Exception e) {
			throw e;
		}
		return ret;
	}
	protected static boolean DB_InserQuestionData(SQLiteDatabase writabledb, MQuestion2 question) 
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
