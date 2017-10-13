package kumon2014.common;

import java.util.HashMap;

import kumon2014.database.env.EnvDBIO;
import kumon2014.database.env.SEnv;

public class KumonEnv {
	private static String G_WEB_URL_CONTENTS = "/Contents";
	private static String G_WEB_URL_ACCOUNT = "/Account";
	private static String G_UPDATE_CHECKFILENAME = "/Update.txt";
	private static String G_UPDATE_APKFILENAME = "/Kumon2.Apk";
	// 20140626 ADD-S For Undo
	private static String G_UPDATE_PROPERTYFILENAME = "/Kumon.txt";
	// 20140626 ADD-E For Undo

	/*
	 * //KumonSoap Url public static String G_API_URL =
	 * "http://192.168.0.160/testkumon/kumon"; //KumonAndroidSoap Url public
	 * static String G_ANDROIDAPI_URL =
	 * "http://192.168.0.160/testkumon/Android/kumonadnroid"; //WebPage Url
	 * public static String G_WEBPAGE_URL =
	 * "http://192.168.0.160/testkumon/Android/WebKES";
	 */

	public static String G_API_WEBSERVICEURL = "http://192.168.0.5/KumonWebService";
	// WebPage Url
	public static String G_WEBPAGE_URL = "http://192.168.0.5/AndroidWeb";
	// ログイン用
	public static String G_WEBPAGEURL_LOGIN = G_WEBPAGE_URL + G_WEB_URL_ACCOUNT;
	// その他用
	public static String G_WEBPAGEURL_NORMAL = G_WEBPAGE_URL
			+ G_WEB_URL_CONTENTS;

	// 回答済みデータ保存日数
	public static int G_KEEP_DAYS = 14;

	// ログ保存日数
	public static int G_LOGKEEP_DAYS = 14;

	// Update Url
	public static String G_UPDATE_URL = "";
	public static String G_UPDATE_CHECKFILE = "";
	public static String G_UPDATE_APKFILE = "";

	public static String G_PASS = "KUMON2014";

	// 20131101 ADD-S For TimeOut
	// KSoap2 TimeOut ミリ秒
	public static int G_SOAP_TIMEOUT = 120000; // 120秒
	// 20131101 ADD-E For TimeOut

	public static String G_BASIC_ID = "";
	public static String G_BASIC_Password = "";

	// 20140626 ADD-S For Undo
	public static String G_UPDATE_PROPERTYFILE = "";

	// 20140626 ADD-E For Undo

	public static void Init() {
		String[] keys = { SEnv.SF_KEY_ApiUrl, SEnv.SF_KEY_AndroidWebPageUrl,
				SEnv.SF_KEY_Keepdays, SEnv.SF_KEY_Logkeepdays,
				SEnv.SF_KEY_UpdateUrl, SEnv.SF_KEY_SoapTimeOut };
        MyTimingLogger logger = new MyTimingLogger("Init");

		HashMap<String, String> map = EnvDBIO.DB_GetValues(keys);
		
		logger.addSplit("DB_GetValues");
		
		String temp;
		temp = map.get(SEnv.SF_KEY_ApiUrl);
		if (temp != null && temp.length() > 0) {
			temp = temp.trim();
			if (temp != null && temp.length() > 0) {
				KumonEnv.G_API_WEBSERVICEURL = temp;
			}
		}
		temp = map.get(SEnv.SF_KEY_AndroidWebPageUrl);
		if (temp != null && temp.length() > 0) {
			temp = temp.trim();
			if (temp != null && temp.length() > 0) {
				KumonEnv.G_WEBPAGE_URL = temp;
			}
		}
		temp = map.get(SEnv.SF_KEY_Keepdays);
		if (temp != null && temp.length() > 0) {
			temp = temp.trim();
			if (temp != null && temp.length() > 0) {
			KumonEnv.G_KEEP_DAYS = Utility.strToInt(temp);
			}
		}
		temp = map.get(SEnv.SF_KEY_Logkeepdays);
		if (temp != null && temp.length() > 0) {
			temp = temp.trim();
			if (temp != null && temp.length() > 0) {
				KumonEnv.G_LOGKEEP_DAYS = Utility.strToInt(temp);
			}
		}

		KumonEnv.G_WEBPAGEURL_LOGIN = KumonEnv.G_WEBPAGE_URL + KumonEnv.G_WEB_URL_ACCOUNT;
		KumonEnv.G_WEBPAGEURL_NORMAL = KumonEnv.G_WEBPAGE_URL + KumonEnv.G_WEB_URL_CONTENTS;

		temp = map.get(SEnv.SF_KEY_UpdateUrl);
		if (temp != null && temp.length() > 0) {
			temp = temp.trim();
			if (temp != null && temp.length() > 0) {
				KumonEnv.G_UPDATE_URL = temp;
			}
		}
		KumonEnv.G_UPDATE_CHECKFILE = KumonEnv.G_UPDATE_URL + KumonEnv.G_UPDATE_CHECKFILENAME;
		KumonEnv.G_UPDATE_APKFILE = KumonEnv.G_UPDATE_URL + KumonEnv.G_UPDATE_APKFILENAME;

		// 20140626 ADD-S For Undo
		KumonEnv.G_UPDATE_PROPERTYFILE = KumonEnv.G_UPDATE_URL + "/" + KumonEnv.G_UPDATE_PROPERTYFILENAME;
		// 20140626 ADD-E For Undo

		temp = map.get(SEnv.SF_KEY_SoapTimeOut);
		if (temp != null && temp.length() > 0) {
			temp = temp.trim();
			if (temp != null && temp.length() > 0) {
				KumonEnv.G_SOAP_TIMEOUT = Utility.strToInt(temp);
			}
		}
		logger.dumpToLog();
	}
}
