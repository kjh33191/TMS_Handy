package kumon2014.database.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;

import kumon2014.common.KumonCommon;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import kumon2014.kumondata.DResultData;

public class TblSoundRecordData {
	// SoundRecord Dataはサイズが大きくなりそうなので、ＳＤカード保存

	public static final String SF_SOUND_ZIP = "SoundRecord.zip";
	public static final String SF_DATAFOLDER = "DATA";
	public static final String SF_ZIP_ENTRY = "SoundRecordData";

	// クリアALL
	protected static boolean DB_ClearAll(SQLiteDatabase writableDb) {
		boolean ret = false;
		try {
			File recordfolder = StudentClientCommData.getRecordFolder(0);
			Utility.deleteDirectry(recordfolder);

			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By Studentid
	protected static boolean DB_DeleteByStudentID(SQLiteDatabase writableDb, String studentId) {
		boolean ret = false;
		try {
			File recordfolder = StudentClientCommData.getRecordFolder(studentId, 0);
			Utility.deleteDirectry(recordfolder);

			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By PrintUnit
	protected static boolean DB_DeleteByPrintUnit(SQLiteDatabase writableDb, String studentId, String kyoka, String kyozai, String printUnit) {
		boolean ret = false;
		try {
			File printunitFolder = StudentClientCommData.getRecordFolder(studentId, kyozai, printUnit, 0);
			Utility.deleteDirectry(printunitFolder);

			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By KyozaiID
	protected static boolean DB_DeleteByKyozaiID(SQLiteDatabase writableDb,	String studentId, String kyoka, String kyozai) {
		boolean ret = false;
		try {
			File recordfolder = StudentClientCommData.getRecordFolder(studentId, kyozai, 0);
			Utility.deleteDirectry(recordfolder);

			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Insert
	//20150310 MOD-S For Web音声データ展開
	//protected static boolean DB_InsertSoundDataList(SQLiteDatabase Writabledb, ArrayList<DResultData> resultdatalist) {
	public static boolean DB_InsertSoundDataList(ArrayList<DResultData> resultDataList, int webRef) {
	//20150310 MOD-E For Web音声データ展開
		boolean ret = false;
		try {
			for (DResultData resultData : resultDataList) {
				
				//20150310 MOD-S For Web音声データ展開
				//File printunitFolder = StudentClientCommData.getRecordFolder(resultData.mStudentID, resultData.mKyozaiID, resultData.mPrintUnitID);
				File printunitFolder = StudentClientCommData.getRecordFolder(resultData.mStudentID, resultData.mKyozaiID, resultData.mPrintUnitID, webRef);
				//20150310 MOD-E For Web音声データ展開
				Utility.deleteDirectry(printunitFolder);
				if (resultData.mSoundRecord != null) {
					printunitFolder.mkdirs();
					File sounddata = new File(printunitFolder, SF_SOUND_ZIP);

					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(sounddata));
					output.write(resultData.mSoundRecord);
					output.flush();
					output.close();
				}
			}
			ret = true;
		} catch (Exception e) {
//			String s = e.toString();
		}
		return ret;
	}

	//20150310 MOD-S For Web音声データ展開
	//protected static byte[] DB_GetSoundData(SQLiteDatabase readbledb, String studentid, String KyokaID, String KyozaiID, String PrintUnitID) {
	protected static byte[] DB_GetSoundData(SQLiteDatabase readableDb, String studentId, String kyokaId, String kyozaiId, String printUnitId, int webRef) {
	//20150310 MOD-E For Web音声データ展開
		try {
			//20150310 MOD-S For Web音声データ展開
			//File printunitFolder = StudentClientCommData.getRecordFolder(studentid, KyozaiID, PrintUnitID);
			File printUnitFolder = StudentClientCommData.getRecordFolder(studentId, kyozaiId, printUnitId, webRef);
			//20150310 MOD-E For Web音声データ展開
			return KumonCommon.CompressRecordData(printUnitFolder);
		} catch (Exception e) {
//			String s = e.getMessage();
		}
		return null;
	}
}
