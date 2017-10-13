package kumon2014.database.master;

import java.util.ArrayList;
import java.util.Comparator;

import net.sqlcipher.database.SQLiteDatabase;

import kumon2014.kumondata.KyozaiName;
import android.content.ContentValues;
import android.database.Cursor;


//教材マスターテーブル
public class MKyozai extends MastDBIO {
	// テーブル名 
	public static final String SF_TBLNAME = "M_Kyozai";
	// カラム 
	public static final String SF_COL_KYOZAIID 		= "CKyozaiID";				//Text 
	public static final String SF_COL_KYOZAINAME 	= "CKyozaiName";			//Text 
	public static final String SF_COL_KYOKAID 		= "CKyokaID";				//Text 
	public static final String SF_COL_KYOKANAME 	= "CKyokaName";				//Text 
	public static final String SF_COL_KYOKAORDERNO 	= "CKyokaOrderNumber";		//integer 
	public static final String SF_COL_KYOZAIORDERNO = "CKyozaiOrderNumber";		//integer 
	//Index
	public static final int SF_IDX_KYOZAIID 		= 0;
	public static final int SF_IDX_KYOZAINAME 		= 1;
	public static final int SF_IDX_KYOKAID 			= 2;
	public static final int SF_IDX_KYOKANAME 		= 3;
	public static final int SF_IDX_KYOKAORDERNO 	= 4;
	public static final int SF_IDX_KYOZAIORDERNO 	= 5;
	
	
	public static final String SF_CREATE_TBL_SQL_KYOZAI =
				"create table " + SF_TBLNAME + "( " 
						+ SF_COL_KYOZAIID + 		" text not null, "
						+ SF_COL_KYOZAINAME + 		" text, "
						+ SF_COL_KYOKAID + 			" text, "
						+ SF_COL_KYOKANAME + 		" text, "
						+ SF_COL_KYOKAORDERNO + 	" integer, "
						+ SF_COL_KYOZAIORDERNO + 	" integer, "
						+ " primary key( " + SF_COL_KYOZAIID  + " )"
						+ " );";

	///////////////////////////////////////////////////////////////////////////////////
	
	
	public String	mKyozaiID = "";
	public String	mKyozaiName = "";
	public String	mKyokaID = "";
	public String	mKyokaName = "";
	public int		mKyokaOrderNumber = 0;
	public int		mKyozaiOrderNumber = 0;
	
	public MKyozai()
	{
	}
	protected static ArrayList<KyozaiName> GetKyozaiList(SQLiteDatabase readbledb, ArrayList<KyozaiName> namelist) 
	{
		ArrayList<KyozaiName> kyozailist = new ArrayList<KyozaiName>();
		
		MKyozai[] kyozaiList = DB_GetKyozaiList(readbledb);
		if(kyozaiList == null) {
			kyozaiList = DEBUG_XXX(readbledb);
		}

		for(int i = 0; i < kyozaiList.length; i++) {
			MKyozai kyozai = kyozaiList[i];
			for(int j = 0; j < namelist.size(); j++) {
				KyozaiName kyozainame = namelist.get(j);
				if(kyozai.mKyozaiID.equalsIgnoreCase(kyozainame.mKyozaiID)) {
					kyozainame.mKyokaName = kyozai.mKyokaName;
					kyozainame.mKyozaiName = kyozai.mKyozaiName;
					kyozainame.mKyokaKyozaiName = kyozai.mKyokaName + " " + kyozai.mKyozaiName;
					kyozainame.mOrderNo = kyozai.mKyozaiOrderNumber;
					kyozailist.add(kyozainame);
					break;
				}
			}
		}
		
		return kyozailist;
	}
	protected static MKyozai[] DEBUG_XXX(SQLiteDatabase readbledb) 
	{
		MKyozai[] kyozai = new MKyozai[11];
		for(int i = 0; i < kyozai.length; i++)
		{
			kyozai[i] = new MKyozai();
		}
		kyozai[0].mKyozaiID = "4ec9e17f-ba54-43f0-acc1-9fb8dd57fca9";
		kyozai[0].mKyozaiName = "3A";
		kyozai[0].mKyokaName = "算数・数学";
		
		kyozai[1].mKyozaiID = "cfaea9e7-d2ae-49e9-9e30-d7d11a7090bf";
		kyozai[1].mKyozaiName = "2A";
		kyozai[1].mKyokaName = "算数・数学";
		
		kyozai[2].mKyozaiID = "2779efdf-e823-4dc0-9d2f-ea9eed6109d9";
		kyozai[2].mKyozaiName = "A";
		kyozai[2].mKyokaName = "算数・数学";
		
		kyozai[3].mKyozaiID = "81e1eb97-e823-4cc1-8a88-d4565562502a";
		kyozai[3].mKyozaiName = "B";
		kyozai[3].mKyokaName = "算数・数学";
		
		kyozai[4].mKyozaiID = "c993e09f-6ba2-4029-854b-a2e208089f15";
		kyozai[4].mKyozaiName = "C";
		kyozai[4].mKyokaName = "算数・数学";
		
		kyozai[5].mKyozaiID = "55d41f67-81f7-4c0b-8d32-a313aea92697";
		kyozai[5].mKyozaiName = "4A";
		kyozai[5].mKyokaName = "国語";
		
		kyozai[6].mKyozaiID = "f58d1abe-eaf7-433c-876a-23e1f691c2ab";
		kyozai[6].mKyozaiName = "3A";
		kyozai[6].mKyokaName = "国語";
		
		kyozai[7].mKyozaiID = "1ca2ebd5-359d-424b-bf82-3bdb887eda7b";
		kyozai[7].mKyozaiName = "2A";
		kyozai[7].mKyokaName = "国語";
		
		kyozai[8].mKyozaiID = "0b74280f-2103-49dd-8007-458783fd284d";
		kyozai[8].mKyozaiName = "A";
		kyozai[8].mKyokaName = "国語";

		kyozai[9].mKyozaiID = "425ac506-16d4-46af-b764-dcb36ba6fa3a";
		kyozai[9].mKyozaiName = "B";
		kyozai[9].mKyokaName = "国語";
		
		kyozai[10].mKyozaiID = "42bd67f5-e335-4545-ba96-8ef0188622a5";
		kyozai[10].mKyozaiName = "C";
		kyozai[10].mKyokaName = "国語";

		return kyozai;
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
	//教材マスタ取得
	protected static MKyozai[] DB_GetKyozaiList(SQLiteDatabase readbledb) 
	{
		MKyozai[] list = null;
		
        Cursor cursor = null;
        try{
            cursor = readbledb.query( 
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
                    SF_COL_KYOKAORDERNO + " , " + SF_COL_KYOZAIORDERNO + " DESC",
                    //limit
                    null
                    );

            list =  DB_GetKyozaiList_readCursor( cursor );
        }
        catch(Exception e) {
//			String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
        
        return list;
	}
	//教材マスタ登録
	protected static boolean DB_AddKyozai(SQLiteDatabase writabledb, ArrayList<MKyozai> kyozailist) 
	{
		boolean ret = false;
		long cnt = 0;
        try{
        	ContentValues values = new ContentValues(); 
        	for(int i = 0; i < kyozailist.size(); i++) {
        		MKyozai kyozai = kyozailist.get(i);
        		values.put(SF_COL_KYOZAIID, kyozai.mKyozaiID); 
        		values.put(SF_COL_KYOZAINAME, kyozai.mKyozaiName); 
        		values.put(SF_COL_KYOKAID, kyozai.mKyokaID); 
        		values.put(SF_COL_KYOKANAME, kyozai.mKyokaName); 
        		values.put(SF_COL_KYOKAORDERNO, kyozai.mKyokaOrderNumber); 
        		values.put(SF_COL_KYOZAIORDERNO, kyozai.mKyozaiOrderNumber); 
        		
        		if(writabledb.insert(SF_TBLNAME, null, values) >= 0) {
        			cnt++;
        		}
        		else {
        			break;
        		}
        	}
        	if(cnt == kyozailist.size()) {
        		ret = true;
        	}
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        return ret;
	}
	private static MKyozai[] DB_GetKyozaiList_readCursor(Cursor cursor)
	{
		MKyozai[] list = null;
		if(cursor.getCount() == 0) {
			return null;
		}
		list = new MKyozai[cursor.getCount()];
		
        // ↓のようにすると、検索結果の件数分だけ繰り返される
		int pos = 0;
        while( cursor.moveToNext() ){
        	list[pos] = new MKyozai();
        	list[pos].mKyozaiID = cursor.getString( MKyozai.SF_IDX_KYOZAIID );
        	list[pos].mKyozaiName = cursor.getString( MKyozai.SF_IDX_KYOZAINAME );
        	list[pos].mKyokaID = cursor.getString( MKyozai.SF_IDX_KYOKAID );
        	list[pos].mKyokaName = cursor.getString( MKyozai.SF_IDX_KYOKANAME );
        	list[pos].mKyokaOrderNumber = cursor.getInt( MKyozai.SF_IDX_KYOKAORDERNO );
        	list[pos].mKyozaiOrderNumber = cursor.getInt( MKyozai.SF_IDX_KYOZAIORDERNO );
    		pos++;
        }
        return list;
    }
	
}
class KyozaiNameComparator 		implements Comparator<KyozaiName> {
	public int compare(KyozaiName s, KyozaiName t) {
		//               + (x > y)
		// compare x y = 0 (x = y)
		//               - (x < y)
		return s.mOrderNo - t.mOrderNo;
	}
}

