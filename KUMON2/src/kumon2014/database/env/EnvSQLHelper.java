package kumon2014.database.env;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;

public class EnvSQLHelper  extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1; 

	// �f�[�^�x�X�� 
	public static final String DATABASE_NAME = StudentClientCommData.getLocalDBFolder()
														+  "/Env.db"; 
	// �R���X�g���N�^
	public EnvSQLHelper(Context context) 
	{ 
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	} 
	@Override 
	public void onCreate(SQLiteDatabase db) 
	{ 
		String sql;
		try {
			sql = SEnv.SF_CREATE_TBL_SQL_ENV;
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
	
	/**
	 * @deprecated 64�o�C�gRaw Key Data���g���̂�{@link GetKey256}���g�p���邱��
	 * @return
	 */
	@Deprecated public String GetKey() 
	{
		return Utility.digest("KumonEnv");
	}

	public String GetKey256() 
	{
		return "x'" + Utility.digest256("KumonEnv") + "'";
	}
}
