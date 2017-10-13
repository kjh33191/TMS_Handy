package kumon2014.webservice;

import java.util.ArrayList;

import kumon2014.database.master.*;
import kumon2014.kumondata.*;


public final class KumonSoapToKumonData {

	public static DStudent cvtStudentLoginResponseToKumonData(StudentLoginResponse studentloginresponse) {
		DStudent student = new DStudent();
		
		student.mSoapStatus = studentloginresponse.mResult.mStatus;
		student.mSoapError = studentloginresponse.mResult.mError;
		
		if(studentloginresponse.mResponsedata != null) {
			student.mSessionID = studentloginresponse.mResponsedata.mSessionID;
			student.mStudentAdminID = studentloginresponse.mResponsedata.mStudentAdminID;
			student.mStudentID = studentloginresponse.mResponsedata.mStudentID;
			student.mName = studentloginresponse.mResponsedata.mName;
			student.mNameKana = studentloginresponse.mResponsedata.mNameKana;
			student.mPenThickness = studentloginresponse.mResponsedata.mPenThickness;
		}
		return student;
	}
	
	public static String getSessionID(StudentLoginResponse studentloginresponse) {
		String sessionid = "";
		
		if(studentloginresponse.mResult.mStatus == 0 && studentloginresponse.mResult.mError.isEmpty() 
				&& studentloginresponse.mResponsedata != null) {
			
			sessionid = studentloginresponse.mResponsedata.mSessionID;
		}
		return sessionid;
	}
	public static String getStudentAdminID(StudentLoginResponse studentloginresponse) {
		String studentadminid = "";
		
		if(studentloginresponse.mResult.mStatus == 0 && studentloginresponse.mResult.mError.isEmpty() 
				&& studentloginresponse.mResponsedata != null) {
			
			studentadminid = studentloginresponse.mResponsedata.mStudentAdminID;
		}
		return studentadminid;
	}
	

	public static ArrayList<MQuestion2> cvtPrintResponseToKumonData(PrintResponse printresponse) 
	{
		ArrayList<MQuestion2> questiondatalist = new ArrayList<MQuestion2>();
		
		if(printresponse.mRresult.mStatus == 0 && printresponse.mRresult.mError.isEmpty() 
													&& printresponse.mResponsedata != null) {
			if(printresponse.mResponsedata.mPrintResponseDataList != null) {
				
				int questiondatacnt = printresponse.mResponsedata.mPrintResponseDataList.size();
				for(int i = 0; i < questiondatacnt; i++) {
					PrintInfoResponseData printinforesponsedata = printresponse.mResponsedata.mPrintResponseDataList.get(i);
					MQuestion2 question = new MQuestion2();

					question.mKyokaID = printinforesponsedata.mKyokaID;
					question.mKyozaiID = printinforesponsedata.mKyozaiID;
					question.mPrintID = printinforesponsedata.mPrintID;
					question.mPrintNo = printinforesponsedata.mPrintNo;
					question.mQuestionData = printinforesponsedata.mQuestionData;
					//20150121 ADD-S For 2015年度Ver. 教材更新
					question.mUpdateTime = printinforesponsedata.mUpdateTime;
		        	//20150121 ADD-E For 2015年度Ver. 教材更新
					
					questiondatalist.add(question);
				}
			}
		}
		return questiondatalist;
	}
	
}
