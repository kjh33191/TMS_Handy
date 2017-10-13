package kumon2014.database.env;

import java.util.HashMap;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

public class SEnv {
	public static final String SF_KEY_ApiUrl = "ApiUrl";
	public static final String SF_KEY_AndroidWebPageUrl = "AndroidWebPageUrl";
	public static final String SF_KEY_BasicID = "BasicID";
	public static final String SF_KEY_BasicPassword = "BasicPassword";
	public static final String SF_KEY_Keepdays = "Keepdays";
	public static final String SF_KEY_Logkeepdays = "Logkeepdays";
	public static final String SF_KEY_UpdateUrl = "UpdateUrl";
	
	//20131101 ADD-S For TimeOut
	//KSoap2 TimeOut 
	public static final String SF_KEY_SoapTimeOut = "SoapTimeOut";
	//20131101 ADD-E For TimeOut
	
	// テーブル名 
	public static final String SF_TBLNAME = "S_Env";
	// カラム 
	public static final String SF_COL_Key 			= "CKey";					//Text 
	public static final String SF_COL_Value 		= "CValue";					//Text 
	//Index
	public static final int SF_IDX_Key 				= 0;
	public static final int SF_IDX_Value 			= 1;
	
	
	public static final String SF_CREATE_TBL_SQL_ENV =
				"create table " + SF_TBLNAME + "( " 
						+ SF_COL_Key + 				" text not null, "
						+ SF_COL_Value + 			" text, "
						+ " primary key( " + SF_COL_Key  + " )"
						+ " );";

	///////////////////////////////////////////////////////////////////////////////////
	public static String DB_GetValue(SQLiteDatabase readable, String key) 
	{
		String value = "";
        Cursor cursor = null;
        try{
            cursor = readable.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
                    null,		//全項目
            		//条件式
                    SF_COL_Key + " = ?", 
              		//条件
                      new String[]{ key },  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    null,
                    //limit
                    null
                    );

            value =  DB_GetValue_readCursor( cursor );
        }
        catch(Exception e) {
//			String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
        
        return value;
	}
	public static HashMap<String, String> DB_GetValues(SQLiteDatabase readable, String[] keys) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" IN (");
		for (int i = 0; i < keys.length; i++) {
			if (i == 0)
				sb.append("?");
			else
				sb.append(",?");
		}
		sb.append(")");
		HashMap<String, String> map = new HashMap<String, String>(); 
        Cursor cursor = null;
        try{
            cursor = readable.query( 
            		//Table名
            		SF_TBLNAME, 
                    //項目名
                    null,		//全項目
            		//条件式
                    SF_COL_Key + sb.toString(), 
              		//条件
                      keys,  
                    //group by
                    null,
                    //Having
                    null, 
                    //order by
                    null,
                    //limit
                    null
                    );
            if (cursor.moveToFirst()) {
	            do {
	            	String key = cursor.getString(SEnv.SF_IDX_Key);
	            	String value = cursor.getString(SF_IDX_Value);
	            	map.put(key, value);
	            } while (cursor.moveToNext());
            }
        }
        catch(Exception e) {
//			String s = e.getMessage();
        }
        finally{
            if( cursor != null ){
                cursor.close();
            }
        }
        
        return map;
	}
	public static boolean DB_InsertValue(SQLiteDatabase writable, String key, String value) 
	{
		boolean ret = false;
        try{
        	ContentValues values = new ContentValues(); 
        	
        	values.put(SF_COL_Key, key); 
        	values.put(SF_COL_Value, value); 
        		
        	if(writable.insert(SF_TBLNAME, null, values) >= 0) {
        		ret = true;
        	}
        }
        catch(Exception e) {
//        	String s = e.getMessage();
        }
        
        return ret;
	}
	protected static boolean DB_UpdateValue(SQLiteDatabase writable, String key, String value)
	{
		boolean ret = false;
		try{
			ContentValues values = new ContentValues(); 
			
			values.put(SF_COL_Value, value); 
			if(writable.update(SF_TBLNAME, 
									values, 
									SF_COL_Key + " = ? " ,
				                    new String[]{ key} 
									) >= 0) {
				ret = true;
			}
		}
		catch(Exception e) {
		}
		return ret;
	}
	
	protected static boolean DB_IsExist(SQLiteDatabase readbledb, String key)
	{
		boolean ret = false;
		Cursor cursor = null;
		try{
          cursor = readbledb.query( 
          		//Table名
        		  SF_TBLNAME, 
                  //項目名
                  new String[]{ SF_COL_Key },  
          		//条件式
                  SF_COL_Key + " = ?", 
          		//条件
                  new String[]{ key },  
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
	
	private static String DB_GetValue_readCursor(Cursor cursor)
	{
		String value = "";
		
        if(cursor.moveToNext() ){
        	value = cursor.getString( SEnv.SF_IDX_Value );
        }
        return value;
    }
}
