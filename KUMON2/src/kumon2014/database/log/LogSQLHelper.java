package kumon2014.database.log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;

public class LogSQLHelper  extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1; 

	// データベス名 
	public static final String DATABASE_NAME = StudentClientCommData.getLocalDBFolder() +  "/Log.db"; 
	// コンストラクタ
	public LogSQLHelper(Context context) 
	{ 
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	} 
	@Override 
	public void onCreate(SQLiteDatabase db) 
	{ 
		String sql;
		try {
			sql = SLog.SF_CREATE_TBL_SQL_KYOZAI;
			db.execSQL(sql); 
			
		}
		catch(Exception e) {
//			String s = e.getMessage();
		}
	} 
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{ 
		
	}  
	
	@Deprecated
	public String GetKey() 
	{
		return Utility.digest("KumonLog");
	}

	public String GetKey256() 
	{
		return "x'" + Utility.digest256("KumonLog") + "'";
	}
}
