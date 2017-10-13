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

//20150210 ADD-S For 2015年度Ver. 音声メモ
public class TblSoundCommentData {
	// SoundRecord Dataはサイズが大きくなりそうなので、ＳＤカード保存

	public static final String SF_SOUND_ZIP = "SoundComment.zip";
	public static final String SF_ZIP_ENTRY = "SoundCommentData";

	// クリアALL
	protected static boolean DB_ClearAll(SQLiteDatabase Writabledb) {
		boolean ret = false;
		try {
			File recordfolder = StudentClientCommData.getSoundCommentFolder(0);
			Utility.deleteDirectry(recordfolder);

			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By Studentid
	protected static boolean DB_DeleteByStudentID(SQLiteDatabase Writabledb, String studentid) {
		boolean ret = false;
		try {
			File recordfolder = StudentClientCommData.getSoundCommentFolder(studentid, 0);
			Utility.deleteDirectry(recordfolder);

			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By PrintUnit
	protected static boolean DB_DeleteByPrintUnit(SQLiteDatabase Writabledb, String studentid, String kyoka, String kyozai, String printunit) {
		boolean ret = false;
		try {
			File printunitFolder = StudentClientCommData.getSoundCommentFolder(studentid, kyozai, printunit, 0);
			Utility.deleteDirectry(printunitFolder);

			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}


	// Insert
	//20150310 MOD-S For Web音声データ展開
	//protected static boolean DB_InsertSoundCommentDataList(SQLiteDatabase Writabledb, ArrayList<DResultData> resultdatalist) {
	public static boolean DB_InsertSoundCommentDataList(ArrayList<DResultData> resultdatalist, int webref) {
	//20150310 MOD-E For Web音声データ展開
		boolean ret = false;
		try {
			for (int i = 0; i < resultdatalist.size(); i++) {
				DResultData resultData = resultdatalist.get(i);
				
				//20150310 MOD-S For Web音声データ展開
				//File printunitFolder = StudentClientCommData.getSoundCommentFolder(resultData.mStudentID, resultData.mKyozaiID, resultData.mPrintUnitID);
				File printunitFolder = StudentClientCommData.getSoundCommentFolder(resultData.mStudentID, resultData.mKyozaiID, resultData.mPrintUnitID, webref);
				//20150310 MOD-E For Web音声データ展開
				Utility.deleteDirectry(printunitFolder);
				if (resultData.mSoundComment != null) {
					printunitFolder.mkdirs();
					File sounddata = new File(printunitFolder, SF_SOUND_ZIP);

					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(sounddata));
					output.write(resultData.mSoundComment);
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
	//public static String DB_GetSoundCommentFileName(String studentid, String KyozaiID, String PrintUnitID) {
	public static String DB_GetSoundCommentFileName(String studentid, String KyozaiID, String PrintUnitID, int webref) {
	//20150310 MOD-E For Web音声データ展開
		String filename = null;
		
		//20150310 MOD-S For Web音声データ展開
		//File targetfolder = StudentClientCommData.getSoundCommentFolder(studentid, KyozaiID, PrintUnitID);
		File targetfolder = StudentClientCommData.getSoundCommentFolder(studentid, KyozaiID, PrintUnitID, webref);
		//20150310 MOD-E For Web音声データ展開
		File soundZipFile = new File(targetfolder, TblSoundCommentData.SF_SOUND_ZIP);
		if(targetfolder.exists()) {
			if(soundZipFile.exists()) {
				KumonCommon.DecompressRecordData(soundZipFile, targetfolder);		
				soundZipFile.delete();
			}
			File[] files = targetfolder.listFiles();
			for(int i = 0; i < files.length; i++){
				String extension = KumonCommon.GetExtension(files[i].getPath());
				if(extension.equalsIgnoreCase("MP3")) {
					filename = files[i].getPath();
					break;
				}
			}

		}
		return filename;
	}
	
}
//20150210 ADD-E For 2015年度Ver. 音声メモ
