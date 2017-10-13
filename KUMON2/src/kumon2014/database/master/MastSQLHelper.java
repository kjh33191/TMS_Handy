package kumon2014.database.master;


import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import android.content.Context; 

// SQLiteOpenHelper 
public class MastSQLHelper extends SQLiteOpenHelper { 
	//private static final int DATABASE_VERSION = 1; 
	//V10.0�ȍ~
	private static final int DATABASE_VERSION = 2; 

	// �f�[�^�x�X�� 
	public static final String DATABASE_NAME = StudentClientCommData.getLocalDBFolder() + "/MAST.db"; 
	// �R���X�g���N�^
	public MastSQLHelper(Context context) 
	{ 
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	} 
	@Override 
	public void onCreate(SQLiteDatabase db) 
	{ 
		String sql;
		try {
			//���ރ}�X�^
			sql = MKyozai.SF_CREATE_TBL_SQL_KYOZAI;
			db.execSQL(sql); 
			
			//���f�[�^�}�X�^
	        //20150121 MOD-S For 2015�N�xVer. ���ލX�V
			//sql = MQuestion2.SF_CREATE_TBL_SQL_QUESTION;
			sql = MQuestion2.SF_CREATE_TBL_SQL_QUESTION_V2;
	        //20150121 MOD-E For 2015�N�xVer. ���ލX�V
			db.execSQL(sql); 
			
			//���Image�f�[�^�}�X�^
			sql = MQuestionImage.SF_CREATE_TBL_SQL_QUESTION;
			db.execSQL(sql); 
			
			//���Sound�f�[�^�}�X�^
			sql = MQuestionSound.SF_CREATE_TBL_SQL_QUESTION;
			db.execSQL(sql); 
		}
		catch(Exception e) {
//			String s = e.getMessage();
		}
			
	} 
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{ 
		if( oldVersion < 2 && newVersion == 2 ){
			//CUpdateTime��ǉ�
			db.execSQL(
					"alter table " + MQuestion2.SF_TBLNAME +
					" add " + MQuestion2.SF_COL_UPDATETIME + " text default null ");
		}
	}
	
	/**
	 * @deprecated 64�o�C�gRaw Key Data���g���̂�{@link GetKey256}���g�p���邱��
	 * @return
	 */
	@Deprecated public String GetKey() 
	{
		return Utility.digest("KumonMaster");
	}
	
	public String GetKey256() 
	{
		return "x'" + Utility.digest256("KumonMaster") + "'";
	}
}