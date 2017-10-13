package kumon2014.database.data;


import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import android.content.Context; 

// SQLiteOpenHelper 
public class TempDataSQLHelper extends SQLiteOpenHelper { 
	//�{�c�a�́A�f�[�^��M���̃��[���o�b�N�p�ł�
	//�e�e�[�u���\���́ADataSQLHelper�ƑS���̓����ɂ��Ă�������
	//�p�r
	//��M�O�ɊY������w�K�҂̃f�[�^��TempDataSQLHelper�փR�s�[
	//DataSQLHelper�̊Y������w�K�҂̃f�[�^���폜
	//��M����
	//���펞�́ATempDataSQLHelper�̃f�[�^���N���A
	//�G���[���E�L�����Z�����́A�ēxDataSQLHelper�̊Y������w�K�҂̃f�[�^���폜��
	//TempDataSQLHelper�̃f�[�^��DataSQLHelper�փR�s�[��ATempDataSQLHelper���N���A
	
	//private static final int DATABASE_VERSION = 4; 
	//V10.1�ȍ~
	//private static final int DATABASE_VERSION = 5; 
	//V10.3�ȍ~ ���ǃR�����gFlg�ǉ�
	//private static final int DATABASE_VERSION = 6; 
	//V10.3�ȍ~ PrintSetDate�ǉ�
	private static final int DATABASE_VERSION = 7; 

	// �f�[�^�x�X�� 
	public static final String DATABASE_NAME = StudentClientCommData.getLocalDBFolder() +  "/TempDATA.db"; 
	// �R���X�g���N�^
	public TempDataSQLHelper(Context context) 
	{ 
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	} 
	@Override 
	public void onCreate(SQLiteDatabase db) 
	{ 
		//���L�́ADataSQLHelper�Ɠ����ɂ��鎖
		String sql;
		try {
			//Result Table
		    //20150303 MOD-S For 2015�N�xVer. ���������X�e�[�^�X
			//sql = TblResultData.SF_CREATE_TBL_SQL_RESULTDARA_V2;	//V2
		    //20150409 MOD-S For 2015�N�xVer. ���ǃR�����g
			//sql = TblResultData.SF_CREATE_TBL_SQL_RESULTDARA_V3;	//V3
	        //20150423 ADD-S For 2015�N�xVer. ���ǃR�����g
			//sql = TblResultData.SF_CREATE_TBL_SQL_RESULTDARA_V4;	//V4
			sql = TblResultData.SF_CREATE_TBL_SQL_RESULTDARA_V5;	//V4
	        //20150423 ADD-E For 2015�N�xVer. ���ǃR�����g
		    //20150409 MOD-E For 2015�N�xVer. ���ǃR�����g
		    //20150303 MOD-E For 2015�N�xVer. ���������X�e�[�^�X
			db.execSQL(sql); 

			//Ink Table
			//20150416 MOD-S InkData To Binary
  			//sql = TblInkData.SF_CREATE_TBL_SQL_INKDATA_V4;		//V4
			sql = TblInkData.SF_CREATE_TBL_SQL_INKDATA_V6;		//V6
			//20150416 MOD-E InkData To Binary
			db.execSQL(sql); 

			//Grading Result Table
			sql = TblGradingResultData.SF_CREATE_TBL_SQL_GRADINGRESULT;
			db.execSQL(sql); 
			
		}
		catch(Exception e) {
//			String s = e.getMessage();
		}
	} 
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{ 
		//���L�́ADataSQLHelper�Ɠ����ɂ��鎖
	    //20150303 ADD-S For 2015�N�xVer. ���������X�e�[�^�X
		if( oldVersion < 5 ){
			//CSOUNDRECORDSTATUS��ǉ�
			db.execSQL(
					"alter table " + TblResultData.SF_TBLNAME +
					" add " + TblResultData.SF_COL_SOUNDRECORDSTATUS + " integer default 0 ");
			
		}
	    //20150303 ADD-E For 2015�N�xVer. ���������X�e�[�^�X
		
	    //20150409 ADD-S For 2015�N�xVer. ���ǃR�����g
		if( oldVersion < 6 ){
			//CCOMMENTUNREADFLG��ǉ�
			db.execSQL(
					"alter table " + TblResultData.SF_TBLNAME +
					" add " + TblResultData.SF_COL_COMMENTUNREADFLG + " integer default 0 ");
			
			db.execSQL(
					"alter table " + TblInkData.SF_TBLNAME +
					" add " + TblInkData.SF_COL_INKBINARY + " blob default null ");
			
		}
	    //20150409 ADD-E For 2015�N�xVer. ���ǃR�����g
		
        //20150423 ADD-S For 2015�N�xVer. ���ǃR�����g
		if( oldVersion < 7 ){
			//CPrintSetDate��ǉ�
			db.execSQL(
					"alter table " + TblResultData.SF_TBLNAME +
					" add " + TblResultData.SF_COL_PRINTSETDATE + " text default null ");
		}
        //20150423 ADD-E For 2015�N�xVer. ���ǃR�����g
		
	}  
	
	/**
	 * @deprecated 64�o�C�gRaw Key Data���g���̂�{@link GetKey256}���g�p���邱��
	 * @return
	 */
	@Deprecated public String GetKey() 
	{
		return Utility.digest("KumonLokal");
	}
	public String GetKey256() 
	{
		return "x'" + Utility.digest256("KumonLokal") + "'";
	}
}