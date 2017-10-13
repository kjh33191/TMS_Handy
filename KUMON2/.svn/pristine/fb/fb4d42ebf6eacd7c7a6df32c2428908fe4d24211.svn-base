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
	// 20140722 ADD-S For InkDataが消える
	private static final String SF_InkLogFolder = "/InkLog/";
	// 20140722 ADD-E For InkDataが消える

	//20140731 ADD-S For 録音対応
	private static final String SF_RecordFolder = "/SoundRecord/";
	//20140731 ADD-E For 録音対応
	//20150310 ADD-S For Web音声データ展開
	private static final String SF_RecordFolderWebRef = "/SoundRecordWebRef/";
	//20150310 ADD-E For Web音声データ展開

    //20150210 ADD-S For 2015年度Ver. 音声メモ
	private static final String SF_SoundCommentFolder = "/SoundComment/";
    //20150210 ADD-E For 2015年度Ver. 音声メモ
	//20150310 ADD-S For Web音声データ展開
	private static final String SF_SoundCommentFolderWebRef = "/SoundCommentWebRef/";
	//20150310 ADD-E For Web音声データ展開

	private static final String SF_StartFileName = "start.dat";
	private static final String SF_LastUserFileName = "lastuser.dat";
	private static final String SF_LastTestFileName = "lastTest.dat";

	// 20140611 ADD-S For LEAK
	private static final String SF_LeakFileName = "LeakTest.dat";
	// 20140611 ADD-E For LEAK
	// 20140626 ADD-S For Undo
	public static final String SF_PropertyFileName = "kumon.properties";
	// 20140626 ADD-E For Undo

    //20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
	public static final String SF_Count0LogFileName = "Count0Log.txt";
    //20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

	// 共通フォルダ、ファイル
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
	// 20140722 ADD-S For InkDataが消える
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

	// 20140722 ADD-S For InkDataが消える
	//20140731 ADD-S For 録音対応
	//20150310 MOD-S For Web音声データ展開
	//public static File getRecordFolder(i) {
	public static File getRecordFolder(int webref ) {
	//20150310 MOD-E For Web音声データ展開

		//20150310 MOD-S For Web音声データ展開
		//File folder = new File(getTopFolder(), SF_RecordFolder);
		String foldername ;
		if(webref == 0) {
			foldername = SF_RecordFolder;
		}
		else {
			foldername = SF_RecordFolderWebRef;
		}
		File folder = new File(getTopFolder(), foldername);
		//20150310 MOD-E For Web音声データ展開

		folder.mkdirs();

		return folder;
	}
	//20150310 MOD-S For Web音声データ展開
	//public static File getRecordFolder(String studentid) {
	public static File getRecordFolder(String studentid, int webref ) {
	//20150310 MOD-E For Web音声データ展開
		//20150310 MOD-S For Web音声データ展開
		//File folder = getRecordFolder();
		File folder = getRecordFolder(webref);
		//20150310 MOD-E For Web音声データ展開
		File studentfolder = new File(folder, studentid);
		//studentfolder.mkdirs();

		return studentfolder;
	}
	//20150310 MOD-S For Web音声データ展開
	//public static File getRecordFolder(String studentid, String kyozai) {
	public static File getRecordFolder(String studentid, String kyozai, int webref ) {
	//20150310 MOD-E For Web音声データ展開
		//20150310 MOD-S For Web音声データ展開
		//File studentfolder = getRecordFolder(studentid);
		File studentfolder = getRecordFolder(studentid, webref);
		//20150310 MOD-E For Web音声データ展開
		File kyozaifolder = new File(studentfolder, kyozai);
		//kyozaifolder.mkdirs();

		return kyozaifolder;
	}
	//20150310 MOD-S For Web音声データ展開
	//public static File getRecordFolder(String studentid, String kyozai, String printinit ) {
	public static File getRecordFolder(String studentid, String kyozai, String printinit, int webref ) {
	//20150310 MOD-E For Web音声データ展開
		//20150310 MOD-S For Web音声データ展開
		//File kyozaifolder = getRecordFolder(studentid, kyozai);
		File kyozaifolder = getRecordFolder(studentid, kyozai, webref);
		//20150310 MOD-E For Web音声データ展開
		File printunitfolder = new File(kyozaifolder, printinit);
		//printunitfolder.mkdirs();

		return printunitfolder;
	}
	//20140731 ADD-E For 録音対応

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
	 * 通信状態チェック
	 * @param cm
	 * @return 接続可ならtrue
	 */
	public static boolean canConnect(ConnectivityManager cm) {
		NetworkInfo nInfo = cm.getActiveNetworkInfo();
		if (nInfo == null) {
			// 接続不可
		} else {
			if (nInfo.isConnected()) {
				// 接続可
				return true;
			} else {
				// 接続不可
			}
		}
		return false;
	}
    //20150210 ADD-S For 2015年度Ver. 音声メモ
	//20150310 MOD-S For Web音声データ展開
	//public static File getSoundCommentFolder() {
	public static File getSoundCommentFolder(int webref) {
	//20150310 MOD-E For Web音声データ展開

		//20150310 MOD-S For Web音声データ展開
		//File folder = new File(getTopFolder(), SF_SoundCommentFolder);
		String foldername ;
		if(webref == 0) {
			foldername = SF_SoundCommentFolder;
		}
		else {
			foldername = SF_SoundCommentFolderWebRef;
		}
		File folder = new File(getTopFolder(), foldername);
		//20150310 MOD-E For Web音声データ展開
		folder.mkdirs();

		return folder;
	}
	//20150310 MOD-S For Web音声データ展開
	//public static File getSoundCommentFolder(String studentid) {
	public static File getSoundCommentFolder(String studentid, int webref) {
	//20150310 MOD-E For Web音声データ展開
		//20150310 MOD-S For Web音声データ展開
		//File folder = getSoundCommentFolder();
		File folder = getSoundCommentFolder(webref);
		//20150310 MOD-E For Web音声データ展開
		File studentfolder = new File(folder, studentid);
		//studentfolder.mkdirs();

		return studentfolder;
	}
	//20150310 MOD-S For Web音声データ展開
	//public static File getSoundCommentFolder(String studentid, String kyozai) {
	public static File getSoundCommentFolder(String studentid, String kyozai, int webref) {
	//20150310 MOD-E For Web音声データ展開
		//20150310 MOD-S For Web音声データ展開
		//File studentfolder = getSoundCommentFolder(studentid);
		File studentfolder = getSoundCommentFolder(studentid, webref);
		//20150310 MOD-E For Web音声データ展開
		File kyozaifolder = new File(studentfolder, kyozai);
		//kyozaifolder.mkdirs();

		return kyozaifolder;
	}
	//20150310 MOD-S For Web音声データ展開
	//public static File getSoundCommentFolder(String studentid, String kyozai, String printinit) {
	public static File getSoundCommentFolder(String studentid, String kyozai, String printinit, int webref) {
	//20150310 MOD-E For Web音声データ展開
		//20150310 MOD-S For Web音声データ展開
		//File kyozaifolder = getSoundCommentFolder(studentid, kyozai);
		File kyozaifolder = getSoundCommentFolder(studentid, kyozai, webref);
		//20150310 MOD-E For Web音声データ展開
		File printunitfolder = new File(kyozaifolder, printinit);
		//printunitfolder.mkdirs();

		return printunitfolder;
	}
    //20150210 ADD-E For 2015年度Ver. 音声メモ

	/**
	 * 秘密鍵をバイト列から生成する
	 *
	 * @param key_bits
	 *            鍵の長さ（ビット単位）
	 */
	public static Key GetKey() {
		// バイト列
		int key_bits = 128;
		byte[] key = new byte[key_bits / 8];

		// バイト列の内容（秘密鍵の値）はプログラマーが決める
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
