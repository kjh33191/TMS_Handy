package kumon2014.common;

import java.io.File;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.spec.SecretKeySpec;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import kumon2014.kumondata.DResultData;

public class StudentClientCommData {

	private static final File SF_BaseFolder = new File(System.getenv("EXTERNAL_STORAGE"));
//	private static final File SF_BaseFolder = Environment
//			.getExternalStorageDirectory();

	private static final String SF_TopFolder = "/Kumon2/StudentClient";

	private static final String SF_DicFolder = "/KumonInkToolDemo/dic/";

	private static final String SF_LocalDBFolder = "/DB";
	private static final String SF_DownloadFolder = "/Download/";
	private static final String SF_SoundFolder = "/Sound/";
	private static final String SF_MemoFolder = "/Memo/";


	// 20140630 ADD-S For Ink Lost
	private static final String SF_InkFolder = "/InkData/";
	private static final String SF_LogFolder = "/Log/";
	// 20140630 ADD-E For Ink Lost
	// 20140722 ADD-S For InkData��������
	private static final String SF_InkLogFolder = "/InkLog/";
	// 20140722 ADD-E For InkData��������

	//20140731 ADD-S For �^���Ή�
	private static final String SF_RecordFolder = "/SoundRecord/";
	//20140731 ADD-E For �^���Ή�
	//20150310 ADD-S For Web�����f�[�^�W�J
	private static final String SF_RecordFolderWebRef = "/SoundRecordWebRef/";
	//20150310 ADD-E For Web�����f�[�^�W�J

    //20150210 ADD-S For 2015�N�xVer. ��������
	private static final String SF_SoundCommentFolder = "/SoundComment/";
    //20150210 ADD-E For 2015�N�xVer. ��������
	//20150310 ADD-S For Web�����f�[�^�W�J
	private static final String SF_SoundCommentFolderWebRef = "/SoundCommentWebRef/";
	//20150310 ADD-E For Web�����f�[�^�W�J

	private static final String SF_StartFileName = "start.dat";
	private static final String SF_LastUserFileName = "lastuser.dat";
	private static final String SF_LastTestFileName = "lastTest.dat";

	// 20140611 ADD-S For LEAK
	private static final String SF_LeakFileName = "LeakTest.dat";
	// 20140611 ADD-E For LEAK
	// 20140626 ADD-S For Undo
	public static final String SF_PropertyFileName = "kumon.properties";
	// 20140626 ADD-E For Undo

    //20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
	public static final String SF_Count0LogFileName = "Count0Log.txt";
    //20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

	// ���ʃt�H���_�A�t�@�C��
	public static File getTopFolder() {
		File folder = new File(SF_BaseFolder + SF_TopFolder);
		folder.mkdirs();

		return folder;
	}
	public static File getDictionaryFolder() {
		File folder = new File(SF_BaseFolder + SF_DicFolder);

		return folder;
	}

	public static File getLocalDBFolder() {
		File folder = new File(getTopFolder(), SF_LocalDBFolder);
		folder.mkdirs();

		return folder;
	}

	public static File getUpdateFolder() {
		File folder = new File(getTopFolder(), SF_DownloadFolder);
		folder.mkdirs();

		return folder;
	}

	public static File getSoundFolder() {
		File folder = new File(getTopFolder(), SF_SoundFolder);

		Utility.deleteDirectry(folder);
		folder.mkdirs();

		return folder;
	}
	public static File getMemoFolder() {

		File folder = new File(getSoundFolder(), SF_MemoFolder);
		Utility.deleteDirectry(folder);
		folder.mkdirs();

		return folder;
	}

	// 20140630 ADD-S For Ink Lost
	public static String getInkTextFile(DResultData resultdata) {
		File folder = new File(getTopFolder(), SF_InkFolder);
		folder.mkdirs();

		String inkfilepath = folder.toString() + "/" + resultdata.mStudentID
				+ "_" + resultdata.mKyokaID + "_" + resultdata.mKyozaiID + "_"
				+ resultdata.mPrintUnitID + ".ink";

		return inkfilepath;
	}

	public static String getInkTextFile(String studentID, String kyokaID,
			String kyozaiID, String printUnitID) {
		File folder = new File(getTopFolder(), SF_InkFolder);
		folder.mkdirs();

		String inkfilepath = folder.toString() + "/" + studentID + "_"
				+ kyokaID + "_" + kyozaiID + "_" + printUnitID + ".ink";

		return inkfilepath;
	}
	public static String getInkBinaryFile(DResultData resultdata) {
		File folder = new File(getTopFolder(), SF_InkFolder);
		folder.mkdirs();

		String inkfilepath = folder.toString() + "/" + resultdata.mStudentID
				+ "_" + resultdata.mKyokaID + "_" + resultdata.mKyozaiID + "_"
				+ resultdata.mPrintUnitID + ".inb";

		return inkfilepath;
	}
	public static String getInkBinaryFile(String studentID, String kyokaID,
			String kyozaiID, String printUnitID) {
		File folder = new File(getTopFolder(), SF_InkFolder);
		folder.mkdirs();

		String inkfilepath = folder.toString() + "/" + studentID + "_"
				+ kyokaID + "_" + kyozaiID + "_" + printUnitID + ".inb";

		return inkfilepath;
	}

	public static File getLogFolder() {
		File folder = new File(getTopFolder(), SF_LogFolder);
		folder.mkdirs();

		return folder;
	}

	// 20140630 ADD-E For Ink Lost
	// 20140722 ADD-S For InkData��������
	public static String getInkLogFile(DResultData resultdata)
	{
		File folder = new File(getTopFolder(), SF_InkLogFolder);
		folder.mkdirs();

		Date date=new Date();
		SimpleDateFormat sdfFmt= new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
		String yymm = (sdfFmt.format(date));

		File yymmfolder = new File(folder, yymm);
		yymmfolder.mkdirs();

		String inkfilepath =  yymmfolder.toString() + "/"
				+ resultdata.mPrintUnitID + "_"
				+ resultdata.mCount + ".ink";
		return inkfilepath;
	}
	public static void clearInkLog()
	{
		File logfolder = new File(getTopFolder(), SF_InkLogFolder);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH,-1);
		String lastymd = (String) DateFormat.format("yyyyMMdd", cal);
		File lastfolder = new File(logfolder, lastymd);

		final File[] files = logfolder.listFiles();
		if(null != files){
		    for(int i = 0; i < files.length; ++i) {
		    	if(files[i].toString().compareToIgnoreCase(lastfolder.toString()) < 0) {
		    		Utility.deleteDirectry(files[i]);
		    	}
		    }
		}
	}

	// 20140722 ADD-S For InkData��������
	//20140731 ADD-S For �^���Ή�
	//20150310 MOD-S For Web�����f�[�^�W�J
	//public static File getRecordFolder(i) {
	public static File getRecordFolder(int webref ) {
	//20150310 MOD-E For Web�����f�[�^�W�J

		//20150310 MOD-S For Web�����f�[�^�W�J
		//File folder = new File(getTopFolder(), SF_RecordFolder);
		String foldername ;
		if(webref == 0) {
			foldername = SF_RecordFolder;
		}
		else {
			foldername = SF_RecordFolderWebRef;
		}
		File folder = new File(getTopFolder(), foldername);
		//20150310 MOD-E For Web�����f�[�^�W�J

		folder.mkdirs();

		return folder;
	}
	//20150310 MOD-S For Web�����f�[�^�W�J
	//public static File getRecordFolder(String studentid) {
	public static File getRecordFolder(String studentid, int webref ) {
	//20150310 MOD-E For Web�����f�[�^�W�J
		//20150310 MOD-S For Web�����f�[�^�W�J
		//File folder = getRecordFolder();
		File folder = getRecordFolder(webref);
		//20150310 MOD-E For Web�����f�[�^�W�J
		File studentfolder = new File(folder, studentid);
		//studentfolder.mkdirs();

		return studentfolder;
	}
	//20150310 MOD-S For Web�����f�[�^�W�J
	//public static File getRecordFolder(String studentid, String kyozai) {
	public static File getRecordFolder(String studentid, String kyozai, int webref ) {
	//20150310 MOD-E For Web�����f�[�^�W�J
		//20150310 MOD-S For Web�����f�[�^�W�J
		//File studentfolder = getRecordFolder(studentid);
		File studentfolder = getRecordFolder(studentid, webref);
		//20150310 MOD-E For Web�����f�[�^�W�J
		File kyozaifolder = new File(studentfolder, kyozai);
		//kyozaifolder.mkdirs();

		return kyozaifolder;
	}
	//20150310 MOD-S For Web�����f�[�^�W�J
	//public static File getRecordFolder(String studentid, String kyozai, String printinit ) {
	public static File getRecordFolder(String studentid, String kyozai, String printinit, int webref ) {
	//20150310 MOD-E For Web�����f�[�^�W�J
		//20150310 MOD-S For Web�����f�[�^�W�J
		//File kyozaifolder = getRecordFolder(studentid, kyozai);
		File kyozaifolder = getRecordFolder(studentid, kyozai, webref);
		//20150310 MOD-E For Web�����f�[�^�W�J
		File printunitfolder = new File(kyozaifolder, printinit);
		//printunitfolder.mkdirs();

		return printunitfolder;
	}
	//20140731 ADD-E For �^���Ή�

	public static File getStartFile() {
		File start = new File(getTopFolder(), SF_StartFileName);
		return start;
	}

	public static File getLastUserFile() {
		File lastuser = new File(getTopFolder(), SF_LastUserFileName);
		return lastuser;
	}

	public static File getLastTestFile() {
		File lasttest = new File(getTopFolder(), SF_LastTestFileName);
		return lasttest;
	}

	// 20140611 ADD-S For LEAK
	public static File getLeakFile() {
		File start = new File(getTopFolder(), SF_LeakFileName);
		return start;
	}

	// 20140611 ADD-E For LEAK
	// 20140626 ADD-S For Undo
	public static File getPropertyFile() {
		File start = new File(getTopFolder(), SF_PropertyFileName);
		return start;
	}

	// 20140626 ADD-E For Undo

	/**
	 * �ʐM��ԃ`�F�b�N
	 * @param cm
	 * @return �ڑ��Ȃ�true
	 */
	public static boolean canConnect(ConnectivityManager cm) {
		NetworkInfo nInfo = cm.getActiveNetworkInfo();
		if (nInfo == null) {
			// �ڑ��s��
		} else {
			if (nInfo.isConnected()) {
				// �ڑ���
				return true;
			} else {
				// �ڑ��s��
			}
		}
		return false;
	}
    //20150210 ADD-S For 2015�N�xVer. ��������
	//20150310 MOD-S For Web�����f�[�^�W�J
	//public static File getSoundCommentFolder() {
	public static File getSoundCommentFolder(int webref) {
	//20150310 MOD-E For Web�����f�[�^�W�J

		//20150310 MOD-S For Web�����f�[�^�W�J
		//File folder = new File(getTopFolder(), SF_SoundCommentFolder);
		String foldername ;
		if(webref == 0) {
			foldername = SF_SoundCommentFolder;
		}
		else {
			foldername = SF_SoundCommentFolderWebRef;
		}
		File folder = new File(getTopFolder(), foldername);
		//20150310 MOD-E For Web�����f�[�^�W�J
		folder.mkdirs();

		return folder;
	}
	//20150310 MOD-S For Web�����f�[�^�W�J
	//public static File getSoundCommentFolder(String studentid) {
	public static File getSoundCommentFolder(String studentid, int webref) {
	//20150310 MOD-E For Web�����f�[�^�W�J
		//20150310 MOD-S For Web�����f�[�^�W�J
		//File folder = getSoundCommentFolder();
		File folder = getSoundCommentFolder(webref);
		//20150310 MOD-E For Web�����f�[�^�W�J
		File studentfolder = new File(folder, studentid);
		//studentfolder.mkdirs();

		return studentfolder;
	}
	//20150310 MOD-S For Web�����f�[�^�W�J
	//public static File getSoundCommentFolder(String studentid, String kyozai) {
	public static File getSoundCommentFolder(String studentid, String kyozai, int webref) {
	//20150310 MOD-E For Web�����f�[�^�W�J
		//20150310 MOD-S For Web�����f�[�^�W�J
		//File studentfolder = getSoundCommentFolder(studentid);
		File studentfolder = getSoundCommentFolder(studentid, webref);
		//20150310 MOD-E For Web�����f�[�^�W�J
		File kyozaifolder = new File(studentfolder, kyozai);
		//kyozaifolder.mkdirs();

		return kyozaifolder;
	}
	//20150310 MOD-S For Web�����f�[�^�W�J
	//public static File getSoundCommentFolder(String studentid, String kyozai, String printinit) {
	public static File getSoundCommentFolder(String studentid, String kyozai, String printinit, int webref) {
	//20150310 MOD-E For Web�����f�[�^�W�J
		//20150310 MOD-S For Web�����f�[�^�W�J
		//File kyozaifolder = getSoundCommentFolder(studentid, kyozai);
		File kyozaifolder = getSoundCommentFolder(studentid, kyozai, webref);
		//20150310 MOD-E For Web�����f�[�^�W�J
		File printunitfolder = new File(kyozaifolder, printinit);
		//printunitfolder.mkdirs();

		return printunitfolder;
	}
    //20150210 ADD-E For 2015�N�xVer. ��������

	/**
	 * �閧�����o�C�g�񂩂琶������
	 *
	 * @param key_bits
	 *            ���̒����i�r�b�g�P�ʁj
	 */
	public static Key GetKey() {
		// �o�C�g��
		int key_bits = 128;
		byte[] key = new byte[key_bits / 8];

		// �o�C�g��̓��e�i�閧���̒l�j�̓v���O���}�[�����߂�
		for (int i = 0; i < key.length; i++) {
			key[i] = (byte) (i + 1);
		}

		return new SecretKeySpec(key, "AES");
	}

	public static Date getKeepDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -KumonEnv.G_KEEP_DAYS);

		return calendar.getTime();
	}

}
