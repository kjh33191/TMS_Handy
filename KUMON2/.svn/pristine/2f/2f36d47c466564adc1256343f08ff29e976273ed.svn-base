package kumon2014.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import kumon2014.activity.EnvActivity;
import kumon2014.activity.GradeStatusActivity;
import kumon2014.activity.GradingActivity;
import kumon2014.activity.LogActivity;
import kumon2014.activity.MaintenanceActivity;
import kumon2014.activity.SettingActivity;
import kumon2014.activity.SplashActivity;
import kumon2014.activity.StartActivity;
import kumon2014.activity.StudyActivity;
import kumon2014.activity.StudyConfirmActivity;
import kumon2014.activity.StudyFinishActivity;
import kumon2014.activity.StudyFinishGradeMySelfActivity;
import kumon2014.activity.StudyFinishGradeOnClientActivity;
import kumon2014.activity.StudyListActivity;
import kumon2014.activity.StudyEntranceActivity;
import kumon2014.activity.StudyRetryActivity;
import kumon2014.activity.StudySpecialTestStartActivity;
import kumon2014.activity.StudyStartActivity;
import kumon2014.activity.WebChartActivity;
import kumon2014.activity.WebGraphActivity;
import kumon2014.activity.TopMenuActivity;
import kumon2014.activity.WebLoginActivity;
import kumon2014.activity.WebNoticeActivity;
import kumon2014.kumondata.KumonDataCtrl;

public class ScreenChange {
	public static final String 	SF_FROMPAGE = "FROMPAGE";
	public static final String 	SF_NOTICEMODE = "NOTICEMODE";
	public static final int 	SF_NOTICEMODE_RECEIVE = 1;
	public static final int 	SF_NOTICEMODE_SEND = 2;
	public static final String 	SF_KYOZAIID = "KYOZAIID";
	public static final String 	SF_PRINTSETID = "PRINTSETID";
	public static final String 	SF_PRINTUNITID = "PRINTUNITID";
	public static final String 	SF_LEARNINGMODE = "LEARNINGMODE";
	public static final String 	SF_PAGECNT = "PAGECNT";
	public static final String 	SF_RESTARTY = "RESTARTY";
	public static final String 	SF_ENTRANCE = "ENTRANCE";
	public static final String 	SF_SPECIALTEST = "SPECIALTEST";
	public static final String 	SF_LIMITTIME = "LIMITTIME";
	
	public static final int SCNO_NO = -1;
	public static final int SCNO_SPLASH = 0;
	public static final int SCNO_START = 1;
	public static final int SCNO_LOGIN = 2;
	public static final int SCNO_TOP = 3;
	public static final int SCNO_LIST = 4;
	public static final int SCNO_STUDYSTART = 5;
	public static final int SCNO_STUDYRETRY = 6;
	public static final int SCNO_STUDY = 7;
	public static final int SCNO_STUDYFINISH = 8;
	public static final int SCNO_NOTICE = 9;
	public static final int SCNO_GRAPH = 10;
	public static final int SCNO_CONFIRM = 11;
	public static final int SCNO_SETTING = 12;
	public static final int SCNO_ENV = 13;
	public static final int SCNO_LOG = 14;
	public static final int SCNO_STUDYFINISH_MYSELF = 15;
	public static final int SCNO_STUDYFINISH_ONCLIENT = 16;
	public static final int SCNO_GRADE = 17;
	public static final int SCNO_ENTRANCE = 18;
	public static final int SCNO_GRADESTATUS = 19;
	public static final int SCNO_CHART = 20;
	public static final int SCNO_STUDYSPCECIALSTART = 21;
	//20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
	public static final int SCNO_MAINTENANCE = 22;
	//20150110 ADD-E For 2015年度Ver. メンテナンス中チェック
	
	public static final int SF_NEXT = 0;
	public static final int SF_TODAY = 1;
	public static final int SF_HOMEWORK = 2;
	
	public static void doScreenChange(Context con, int fromscreen, int toscreen, boolean bfinish, CurrentUser user, int noticemode, int entrance)
	{
		Class<?> t = getScreen(toscreen);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, fromscreen);
			intent.putExtra(ScreenChange.SF_NOTICEMODE, noticemode);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_RESTARTY, 0);
	        intent.putExtra(SF_ENTRANCE, entrance);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
			if(bfinish) {
				((Activity) con).finish();
			}
		}
		System.gc();
	}
	public static void doScreenChangeSpecialStart(Context con, int fromscreen, CurrentUser user, int learningMode, int entrance)
	{
		Class<?> t = getScreen(SCNO_STUDYSPCECIALSTART);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, fromscreen);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_LEARNINGMODE, learningMode);
	        intent.putExtra(SF_RESTARTY, 0);
	        intent.putExtra(SF_ENTRANCE, entrance);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
			((Activity) con).finish();
		}
		System.gc();
	}
	
	public static void doScreenChangeDone(Context con, CurrentUser user)
	{
		Class<?> t = getScreen(SCNO_CONFIRM);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_LIST);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_LEARNINGMODE, KumonDataCtrl.SF_DATATYPE_DONE);
	        intent.putExtra(SF_RESTARTY, 0);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
			((Activity) con).finish();
		}
		System.gc();
	}
	public static void doScreenChangeRetry(Context con, CurrentUser user, int learningmode)
	{
		Class<?> t = getScreen(SCNO_STUDY);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_STUDYRETRY);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_LEARNINGMODE, learningmode);
	        intent.putExtra(SF_RESTARTY, 0);
	        intent.putExtra(SF_SPECIALTEST, false);
	        
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
			((Activity) con).finish();
		}
		System.gc();
	}
	public static void doScreenChangeNext(Context con, CurrentUser user, int page, int learningmode)
	{
		Class<?> t = getScreen(SCNO_STUDY);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_STUDYSTART);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_LEARNINGMODE, learningmode);
	        intent.putExtra(SF_PAGECNT, page);
	        intent.putExtra(SF_RESTARTY, 0);
	        intent.putExtra(SF_SPECIALTEST, false);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
			((Activity) con).finish();
		}
		System.gc();
	}
	public static void doScreenChangeSpcecialTest(Context con, CurrentUser user, int learningmode, int limitTime, int entrance)
	{
		Class<?> t = getScreen(SCNO_STUDY);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_STUDYSPCECIALSTART);
			intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_LEARNINGMODE, learningmode);
	        intent.putExtra(SF_PAGECNT, 0);
	        intent.putExtra(SF_RESTARTY, 0);
	        intent.putExtra(SF_SPECIALTEST, true);
	        intent.putExtra(SF_LIMITTIME, limitTime);
	        intent.putExtra(SF_ENTRANCE, entrance);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
			((Activity) con).finish();
		}
		System.gc();
	}
	
	public static void doScreenChangeGradeMySelf(Context con, CurrentUser user, boolean bfinish)
	{
		Class<?> t = getScreen(SCNO_GRADE);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_STUDYFINISH_MYSELF);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_LEARNINGMODE, KumonDataCtrl.SF_DATATYPE_GRADESELF);
	        intent.putExtra(SF_RESTARTY, 0);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
			if(bfinish) {
				((Activity) con).finish();
			}
		}
		System.gc();
	}
	public static void doScreenChangeGradeInstructorOnClient(Context con, CurrentUser user, boolean bfinish)
	{
		Class<?> t = getScreen(SCNO_GRADE);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_STUDYFINISH_ONCLIENT);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_LEARNINGMODE, KumonDataCtrl.SF_GRADEINSTRUCTORONCLIENT);
	        intent.putExtra(SF_RESTARTY, 0);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
			if(bfinish) {
				((Activity) con).finish();
			}
		}
		System.gc();
	}

	public static void doScreenChangeGraph(Context con, CurrentUser user, String kyozaiId, String printsetid, String PrintUnitid)
	{
		Class<?> t = getScreen(SCNO_CONFIRM);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_GRAPH);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_KYOZAIID, kyozaiId);
	        intent.putExtra(SF_PRINTSETID, printsetid);
	        intent.putExtra(SF_PRINTUNITID, PrintUnitid);
	        intent.putExtra(SF_LEARNINGMODE, KumonDataCtrl.SF_DATATYPE_WEBVIEW);
	        intent.putExtra(SF_RESTARTY, 0);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
		}
		System.gc();
	}
	public static void doScreenChangeRestart(Context con, CurrentUser user)
	{
		Class<?> t = getScreen(SCNO_STUDY);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_SPLASH);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_RESTARTY, 1);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
		}
		System.gc();
	}
    //20150409 ADD-S For 2015年度Ver. 未読コメント
	public static void doScreenChangeDoneUnread(Context con, CurrentUser user)
	{
		Class<?> t = getScreen(SCNO_CONFIRM);
		if(t != null) {
			Intent intent = new Intent(con, t);
			intent.putExtra(ScreenChange.SF_FROMPAGE, SCNO_TOP);
	        intent.putExtra("CurrentUser", user);
	        intent.putExtra(SF_LEARNINGMODE, KumonDataCtrl.SF_DATATYPE_DONE_UNREAD);
	        intent.putExtra(SF_RESTARTY, 0);
	        
	        intent.setAction(Intent.ACTION_VIEW);
			con.startActivity(intent); 
		}
		System.gc();
	}
    //20150409 ADD-E For 2015年度Ver. 未読コメント

	private static Class<?> getScreen(int scno)
	{
		switch(scno)
		{
			case(SCNO_SPLASH):
				return SplashActivity.class;
			case(SCNO_TOP):
				return TopMenuActivity.class;
			case(SCNO_START):
				return StartActivity.class;
			case(SCNO_LOGIN):
				return WebLoginActivity.class;
			case(SCNO_LIST):
				return StudyListActivity.class;
			case(SCNO_STUDYSTART):
				return StudyStartActivity.class;
			case(SCNO_STUDYRETRY):
				return StudyRetryActivity.class;
			case(SCNO_STUDY):
				return StudyActivity.class;
			case(SCNO_STUDYFINISH):
				return StudyFinishActivity.class;
			case(SCNO_NOTICE):
				return WebNoticeActivity.class;
			case(SCNO_GRAPH):
				return WebGraphActivity.class;
			case(SCNO_CONFIRM):
				return StudyConfirmActivity.class;
			case(SCNO_SETTING):
				return SettingActivity.class;
			case(SCNO_ENV):
				return EnvActivity.class;
			case(SCNO_LOG):
				return LogActivity.class;
			case(SCNO_STUDYFINISH_MYSELF):
				return StudyFinishGradeMySelfActivity.class;
			case(SCNO_STUDYFINISH_ONCLIENT):
				return StudyFinishGradeOnClientActivity.class;
			case(SCNO_GRADE):
				return GradingActivity.class;
			case(SCNO_ENTRANCE):
				return StudyEntranceActivity.class;
			case(SCNO_GRADESTATUS):
				return GradeStatusActivity.class;
			case(SCNO_CHART):
				return WebChartActivity.class;
			case(SCNO_STUDYSPCECIALSTART):
				return StudySpecialTestStartActivity.class;
			//20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
			case(SCNO_MAINTENANCE):
				return MaintenanceActivity.class;
			//20150110 ADD-E For 2015年度Ver. メンテナンス中チェック
		}
		
		return null;
	}
}
