package kumon2014.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kumon2014.kumondata.DResultData;

public class KumonLog {

	public static void AddLog(String title, String message) {
		String s = "";
		try {
			File logfilepath =  StudentClientCommData.getLogFolder();
	
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
			String text = sdf.format(date) + ".log";
			File logfile = new File(logfilepath, text);
			BufferedWriter bw = new BufferedWriter(
			            new OutputStreamWriter(
			            new FileOutputStream(logfile, true), "UTF-8"));

			//ì˙ït
			SimpleDateFormat sdf2 = new SimpleDateFormat("Åyyyyy/MM/dd hh:mm:ssÅz\r\n", Locale.JAPAN);
			s = sdf2.format(date);
		    bw.write(s);
		    //Title
		    s = "(" + title + ")\r\n";
		    bw.write(s);
		    //Message
		    bw.write(message + "\r\n");
		    bw.newLine();		    
		    bw.write("------------------------------------------------------------\r\n");
		    bw.flush();
		    bw.close();
		} catch (Exception e) {
		}					
		
	}
	public static void AddLog(String title, String message, DResultData resultdata) {
		String s = "";
		try {
			File logfilepath =  StudentClientCommData.getLogFolder();
	
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
			String text = sdf.format(date) + ".log";
			File logfile = new File(logfilepath, text);
			BufferedWriter bw = new BufferedWriter(
			            new OutputStreamWriter(
			            new FileOutputStream(logfile, true), "UTF-8"));

			//ì˙ït
			SimpleDateFormat sdf2 = new SimpleDateFormat("Åyyyyy/MM/dd hh:mm:ssÅz\r\n", Locale.JAPAN);
			s = sdf2.format(date);
		    bw.write(s);
		    //User
		    s = "StudentID=" + resultdata.mStudentID.toString() + " KyokaID=" + resultdata.mKyokaID.toString() + " KyozaiID=" + resultdata.mKyozaiID.toString() + " PrintUnitID=" + resultdata.mPrintUnitID.toString() + "\r\n"; 
		    bw.write(s);
		    //Title
		    s = "(" + title + ")\r\n";
		    bw.write(s);
		    //Message
		    bw.write(message + "\r\n");
		    bw.newLine();		    
		    bw.write("------------------------------------------------------------\r\n");
		    bw.flush();
		    bw.close();
		} catch (Exception e) {
		}					
		
	}
	
	
    //20141208 ADD-S For DebugLog èââÒäwèKéûÇ…ÅACountÇ™ÇQâÒÇ…Ç»Ç¡ÇƒÇµÇ‹Ç§å¥àˆí≤ç∏óp
	public static void AddAndroidLog(ArrayList<String> LogList, String printunit, int page, String msg) {
		if(LogList != null) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss", Locale.JAPAN);
			String logmsg = sdf.format(date) + "[PrintUnit = " + printunit + "] [Page = " + page + "] " + msg; 
			
			LogList.add(logmsg);
		}
	}
	public static void ClearAndroidLog(ArrayList<String> LogList) {
		if(LogList != null) {
			LogList.clear();
		}
	}
	public static void PrintoutAndroidLog(ArrayList<String> LogList) {
		try {
			if(LogList != null && LogList.size() > 0) {
				File logfilepath =  StudentClientCommData.getLogFolder();
				String text = StudentClientCommData.SF_Count0LogFileName;
				File logfile = new File(logfilepath, text);
				BufferedWriter bw = new BufferedWriter(
				            new OutputStreamWriter(
				            new FileOutputStream(logfile, true), "UTF-8"));
				int datacnt = LogList.size();
				for(int i = 0; i < datacnt; i++) {
					String s = LogList.get(i);
				    bw.write(s);
				    bw.newLine();		    
				}
			    bw.newLine();		    
			    bw.flush();
			    bw.close();
			}
			
		} catch (Exception e) {
		}					
	}
	public static void DeleteAndroidLogFile() {
		
		try {
			File logfilepath =  StudentClientCommData.getLogFolder();
			String text = StudentClientCommData.SF_Count0LogFileName;
			File logfile = new File(logfilepath, text);
			
			if(logfile.exists()) {
				logfile.delete();
			}
		} catch (Exception e) {
		}					
	}
	
	public static String[] GetAndroidLogList() {
		ArrayList<String> list = new ArrayList<String>();
		File logfilepath =  StudentClientCommData.getLogFolder();
		String text = StudentClientCommData.SF_Count0LogFileName;
		File logfile = new File(logfilepath, text);
		try{
			BufferedReader br = new BufferedReader(
			            new InputStreamReader(
			            new FileInputStream(logfile), "UTF-8"));
				
			String s;
			while ((s = br.readLine() ) != null){
				list.add(s);
			}
			br.close();
		}
		catch(Exception e){
		}
		
		if(list.size() > 0) {
			String s[] = list.toArray(new String[0]);
			return s;
		}
		else {
			return null;
		}
	}
    //20141208 ADD-E For DebugLog èââÒäwèKéûÇ…ÅACountÇ™ÇQâÒÇ…Ç»Ç¡ÇƒÇµÇ‹Ç§å¥àˆí≤ç∏óp
}
