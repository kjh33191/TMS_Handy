package kumon2014.database.env;

import java.io.File;
import java.util.HashMap;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import android.content.Context;

/**
 * 環境変数収容DBアクセス
 *
 */
public class EnvDBIO {
	private static EnvSQLHelper   mEnvSQLHelper = null;

	public static void Open(Context con) 
	{
		mEnvSQLHelper = new EnvSQLHelper(con); 

		// check for new password
		boolean isOldPass = false;
		File file = new File(EnvSQLHelper.DATABASE_NAME);
		if (file.exists()) {
			try {
				SQLiteDatabase db = SQLiteDatabase.openDatabase(EnvSQLHelper.DATABASE_NAME,
						mEnvSQLHelper.GetKey256().toCharArray(), null, SQLiteDatabase.OPEN_READONLY);
				db.close();
			}
			catch (SQLiteException e) {
				isOldPass = true;
			}
		}
		if (isOldPass) {
			// change password
			try {
				@SuppressWarnings("deprecation")
				SQLiteDatabase db = SQLiteDatabase.openDatabase(EnvSQLHelper.DATABASE_NAME,
						mEnvSQLHelper.GetKey().toCharArray(), null, SQLiteDatabase.OPEN_READWRITE);
				db.changePassword(mEnvSQLHelper.GetKey256());
				db.close();
			}
			catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}
	public static void Close(Context con) 
	{
		mEnvSQLHelper.close();
	}
	
	public static String DB_GetValue(String key) 
	{
		SQLiteDatabase readable = mEnvSQLHelper.getReadableDatabase(mEnvSQLHelper.GetKey256()); 
		
		String value = "";
		try {
			value = SEnv.DB_GetValue(readable, key); 
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
    	return value;
	}
	
	/**
	 * 値のまとめ取得
	 * @param keys
	 * @return
	 */
	public static HashMap<String, String> DB_GetValues(String[] keys)
	{
			SQLiteDatabase readable = mEnvSQLHelper.getReadableDatabase(mEnvSQLHelper.GetKey256());
			HashMap<String, String> list = new HashMap<String, String>();
			try {
				return SEnv.DB_GetValues(readable, keys);
/*				for (String key : keys) {
					String value = SEnv.DB_GetValue(readable, key); 
					list.put(key, value);
				}*/
			}
			catch(Exception e) {
			}
			finally{
				readable.close();
				readable = null;
	        }
			return list;
	}
	
	public static boolean DB_SetValue(String key, String value) 
	{
		SQLiteDatabase readable = mEnvSQLHelper.getReadableDatabase(mEnvSQLHelper.GetKey256()); 
		
		boolean stat = false;
		try {
			boolean exist = SEnv.DB_IsExist(readable, key);
			
			if(exist == true) {
				stat = SEnv.DB_UpdateValue(readable, key, value);
			}
			else {
				stat = SEnv.DB_InsertValue(readable, key, value);
			}
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return stat;
	}

	/**
	 * 値のまとめ設定
	 * @param keys
	 * @return
	 */
	public static boolean DB_SetValues(HashMap<String, String> keys) 
	{
		SQLiteDatabase readable = mEnvSQLHelper.getReadableDatabase(mEnvSQLHelper.GetKey256()); 
		
		boolean stat = false;
		try {
			for (String key : keys.keySet()) {
				boolean exist = SEnv.DB_IsExist(readable, key);
				String value = keys.get(key);
				
				if(exist == true) {
					stat = SEnv.DB_UpdateValue(readable, key, value);
				}
				else {
					stat = SEnv.DB_InsertValue(readable, key, value);
				}
			}
		}
		catch(Exception e) {
		}
		finally{
			readable.close();
			readable = null;
        }
		
		return stat;
	}
}
