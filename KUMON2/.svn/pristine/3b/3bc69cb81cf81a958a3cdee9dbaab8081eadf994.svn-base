package kumon2014.database.master;

import java.io.Serializable;
import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;


//教材マスターテーブル
public class MQuestionSound extends MastDBIO implements Serializable {
	private static final long serialVersionUID = 1L;

	// テーブル名 
	public static final String SF_TBLNAME = "M_QuestionSound";
	
	// カラム 
	public static final String SF_COL_PRINTID 		= "CPrintID";		//Text 
	public static final String SF_COL_KYOKAID 		= "CKyokaID";		//Text 
	public static final String SF_COL_KYOZAIID 		= "CKyozaiID";		//Text 
	public static final String SF_COL_PRINTNO 		= "CPrintNo";		//integer 
	public static final String SF_COL_PAGENO 		= "CPageNo";		//integer 
	public static final String SF_COL_SOUNDNO 		= "CSoundNo";			//integer 
	public static final String SF_COL_SOUND 		= "CSound";			//blob 
	//Index
	public static final int SF_IDX_PRINTID 			= 0;
	public static final int SF_IDX_KYOKAID 			= 1;
	public static final int SF_IDX_KYOZAIID 		= 2;
	public static final int SF_IDX_PRINTNO 			= 3;
	public static final int SF_IDX_PAGENO			= 4;
	public static final int SF_IDX_SOUNDNO			= 5;
	public static final int SF_IDX_SOUND			= 6;
	
//	return cursor.getBlob(0);

	public static final String SF_CREATE_TBL_SQL_QUESTION =
				"create table " + SF_TBLNAME + "( " 
						+ SF_COL_PRINTID 		+ " text not null, "
						+ SF_COL_KYOKAID 		+ " text, "
						+ SF_COL_KYOZAIID 		+ " text, "
						+ SF_COL_PRINTNO 		+ " integer, "
						+ SF_COL_PAGENO 		+ " integer, " 
						+ SF_COL_SOUNDNO 		+ " integer, " 
						+ SF_COL_SOUND 			+ " blob, " 
						+ " primary key( " + SF_COL_PRINTID + " , " + SF_COL_KYOKAID + " , " + SF_COL_KYOZAIID  + " , " + SF_COL_PAGENO  + " ) "
						+ " );";

	///////////////////////////////////////////////////////////////////////////////////
	public String	mPrintID = "";
	public String	mKyokaID = "";
	public String	mKyozaiID = "";
	public int		mPrintNo = 0;
	public int		mPageNo = 0;
	public int		mSoundNo = 0;
	public byte[]	mSound = null;
	
	public MQuestionSound()
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
		mSoundNo = 0;
		mSound = null;
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
	protected static boolean DB_AddPrintSound(SQLiteDatabase writabledb, String printID, String kyokaID, String kyozaiID, int printNo, ArrayList<MQuestionSound> soundlist) 
	{
		boolean ret = false;
		
		try{
			for(int i = 0; i < soundlist.size(); i++) {
				ContentValues values = new ContentValues(); 
				MQuestionSound sound = soundlist.get(i);
				values.put(SF_COL_PRINTID, printID); 
				values.put(SF_COL_KYOKAID, kyokaID); 
				values.put(SF_COL_KYOZAIID, kyozaiID); 
				values.put(SF_COL_PRINTNO, printNo); 
				values.put(SF_COL_PAGENO, sound.mPageNo); 
				values.put(SF_COL_SOUNDNO, sound.mSoundNo); 
				values.put(SF_COL_SOUND, sound.mSound); 
	
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
	protected static ArrayList<MQuestionSound> DB_GetPrintSound(SQLiteDatabase readbledb, String printid) 
	{
		ArrayList<MQuestionSound> soundlist = new  ArrayList<MQuestionSound>();		
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
          soundlist =  DB_GetQuestionSound_readCursor( cursor );
      }
      catch(Exception e) {
      }
      finally{
          // Cursorを忘れずにcloseする
          if( cursor != null ){
              cursor.close();
          }
      }
      
      return soundlist;
	}
	public static ArrayList<MQuestionSound> DB_GetPrintSoundByPrintNo(SQLiteDatabase readbledb, String kyoka, String kyozai, int printno)  {
		ArrayList<MQuestionSound> soundlist = new  ArrayList<MQuestionSound>();		
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
          soundlist =  DB_GetQuestionSound_readCursor( cursor );
		}
		catch(Exception e) {
		}
		finally{
			// Cursorを忘れずにcloseする
			if( cursor != null ){
				cursor.close();
			}
		}
      
		return soundlist;
	}
	
	private static ArrayList<MQuestionSound> DB_GetQuestionSound_readCursor(Cursor cursor)
	{
		ArrayList<MQuestionSound> soundlist = new  ArrayList<MQuestionSound>();		
		MQuestionSound sound = null;
        while( cursor.moveToNext() ){
        	sound = new MQuestionSound();
        	sound.mPrintID = cursor.getString(SF_IDX_PRINTID);
        	sound.mKyokaID = cursor.getString(SF_IDX_KYOKAID);
        	sound.mKyozaiID = cursor.getString(SF_IDX_KYOZAIID);
        	sound.mPrintNo = cursor.getInt(SF_IDX_PRINTNO);

        	sound.mPageNo = cursor.getInt(SF_IDX_PAGENO);
        	sound.mSoundNo = cursor.getInt(SF_IDX_SOUNDNO);
        	sound.mSound = cursor.getBlob(SF_IDX_SOUND);
			
			soundlist.add(sound);
		}
		return soundlist;
	}
	
}
