package kumon2014.markcontroltool.control;

import java.io.File;

import kumon2014.database.data.TblSoundRecordData;
import kumon2014.common.KumonCommon;
import pothos.mdtcommon.DataStructs.RecordData;

public class RecordDataControl {

	public static void RecordDataDecompressed(File recordFolder) {

		File recordZipFile = new File(recordFolder, TblSoundRecordData.SF_SOUND_ZIP);
		File targetfolder = new File(recordFolder, TblSoundRecordData.SF_DATAFOLDER);
		if(targetfolder.exists() == false) {
			targetfolder.mkdirs();
			if(recordZipFile.exists()) {
				KumonCommon.DecompressRecordData(recordZipFile, targetfolder);		
			}
		}
	}
	public static String makeRecordFileName(File recordFolder, RecordData recordData, String loginID) {
		File filename;

		File targetfolder = new File(recordFolder, TblSoundRecordData.SF_DATAFOLDER);
		
		filename = new File(targetfolder, loginID + "_" + recordData.MdtDataID + "_" + recordData.PageNumber + "_" + recordData.QuestionNumber + "_" + recordData.RecordNumber + ".m4a");
		
		return filename.toString();
	}
}
