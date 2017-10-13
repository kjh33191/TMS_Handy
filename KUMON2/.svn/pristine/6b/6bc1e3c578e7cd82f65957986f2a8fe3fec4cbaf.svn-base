package kumon2014.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.ksoap2.SoapEnvelope; 
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.support.annotation.WorkerThread;
import android.util.Base64;
import android.util.Log;

import kumon2014.common.*;
import kumon2014.database.master.*;
import kumon2014.kumondata.*;
import kumon2014.message.KumonMessage;



public class KumonSoap {
	static final String TAG = "KumonSoap";
	public static String ACTION_NAME = "WEBSERVICE_ACTION";

	private final String WSDL_TARGET_NAMESPACE = "http://kumon.netlearning.co.jp/";
	
	private final String OPERATION_NAME_StudentLogin = "Login"; 
	private final String SOAP_ACTION_StudentLogin = WSDL_TARGET_NAMESPACE + OPERATION_NAME_StudentLogin;
	private final String SOAP_ADDRESS_StudentLogin = KumonEnv.G_API_WEBSERVICEURL + "/StudentLogin.asmx";

	private  final String OPERATION_NAME_UserLogout = "Logout"; 
	private final String SOAP_ACTION_UserLogout = WSDL_TARGET_NAMESPACE + OPERATION_NAME_UserLogout;
	private  final String SOAP_ADDRESS_UserLogout = KumonEnv.G_API_WEBSERVICEURL + "/UserLogoutAndroid.asmx";

	private  final String OPERATION_NAME_StudentEntrance = "GetStudentEntryStatus"; 
	private final String SOAP_ACTION_StudentEntrance = WSDL_TARGET_NAMESPACE + OPERATION_NAME_StudentEntrance;
	private  final String SOAP_ADDRESS_StudentEntrance = KumonEnv.G_API_WEBSERVICEURL + "/StudentEntryStatus.asmx";
	

	private  final String OPERATION_NAME_AllRetryPrintSet = "GetAllRetryPrintSet"; 
	private final String SOAP_ACTION_AllRetryPrintSet = WSDL_TARGET_NAMESPACE + OPERATION_NAME_AllRetryPrintSet;
	private  final String SOAP_ADDRESS_AllRetryPrintSet = KumonEnv.G_API_WEBSERVICEURL + "/AllRetryPrintSet.asmx";
	
	private  final String OPERATION_NAME_Print = "GetPrint"; 
	private final String SOAP_ACTION_Print = WSDL_TARGET_NAMESPACE + OPERATION_NAME_Print;
	private  final String SOAP_ADDRESS_Print = KumonEnv.G_API_WEBSERVICEURL + "/Print2.asmx";

	private  final String OPERATION_NAME_PrintByPrintNo = "GetPrint"; 
	private final String SOAP_ACTION_PrintByPrintNo = WSDL_TARGET_NAMESPACE + OPERATION_NAME_PrintByPrintNo;
	private  final String SOAP_ADDRESS_PrintByPrintNo = KumonEnv.G_API_WEBSERVICEURL + "/PrintByPrintNo2.asmx";
	
	private  final String OPERATION_NAME_StudentGradingStatus = "GetStudentGradingStatus"; 
	private final String SOAP_ACTION_StudentGradingStatus = WSDL_TARGET_NAMESPACE + OPERATION_NAME_StudentGradingStatus;
	private  final String SOAP_ADDRESS_StudentGradingStatus = KumonEnv.G_API_WEBSERVICEURL + "/StudentGradingStatus.asmx";
	
	
	
	private  final String OPERATION_NAME_RegistGradingResult = "RegistGradingResultAndroid"; 
	private final String SOAP_ACTION_RegistGradingResult = WSDL_TARGET_NAMESPACE + OPERATION_NAME_RegistGradingResult;
	private  final String SOAP_ADDRESS_RegistGradingResult = KumonEnv.G_API_WEBSERVICEURL + "/GradingResultRegistAndroid.asmx";

	
	private  final String OPERATION_NAME_GradingResultDataAndroid = "GetGradingResultDataAndroid"; 
	private final String SOAP_ACTION_GradingResultDataAndroid = WSDL_TARGET_NAMESPACE + OPERATION_NAME_GradingResultDataAndroid;
    //20150121 MOD-S For 2015年度Ver. 教材更新
	//古いバージョンで動作する端末もあるので、Webサービスは変更ではなく、名称を変えて追加した為
	//private  final String SOAP_ADDRESS_GradingResultDataAndroid = KumonEnv.G_API_WEBSERVICEURL + "/GradingResultDataAndroid.asmx";
	private  final String SOAP_ADDRESS_GradingResultDataAndroid = KumonEnv.G_API_WEBSERVICEURL + "/GradingResultDataAndroidV10.asmx";
    //20150121 MOD-E For 2015年度Ver. 教材更新

	
	//20140618 ADD-S For 分割受信
	private  final String OPERATION_NAME_PrintSetIDList = "GetPrintSetIDList"; 
	private final String SOAP_ACTION_PrintSetIDList = WSDL_TARGET_NAMESPACE + OPERATION_NAME_PrintSetIDList;
	private  final String SOAP_ADDRESS_PrintSetIDList = KumonEnv.G_API_WEBSERVICEURL + "/PrintSetIDList.asmx";
	//20140618 ADD-E For 分割受信
	
    //20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
	private  final String OPERATION_NAME_CollectAndroidLog = "PutAndroidLog"; 
	private final String SOAP_ACTION_CollectAndroidLog = WSDL_TARGET_NAMESPACE + OPERATION_NAME_CollectAndroidLog;
	private  final String SOAP_ADDRESS_CollectAndroidLog = KumonEnv.G_API_WEBSERVICEURL + "/CollectAndroidLog.asmx";
    //20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
	
	//20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
	private  final String OPERATION_NAME_MaintenanceCheck = "Check"; 
	private final String SOAP_ACTION_MaintenanceCheck = WSDL_TARGET_NAMESPACE + OPERATION_NAME_MaintenanceCheck;
	private  final String SOAP_ADDRESS_MaintenanceCheck = KumonEnv.G_API_WEBSERVICEURL + "/MaintenanceCheck.asmx";
	//20150110 ADD-E For 2015年度Ver. メンテナンス中チェック

	//20150413 ADD-S For 2015年度Ver. 未読対応
	private  final String OPERATION_NAME_CommentUnreadFlgUpdate = "SetUnreadFlg"; 
	private final String SOAP_ACTION_CommentUnreadFlgUpdate = WSDL_TARGET_NAMESPACE + OPERATION_NAME_CommentUnreadFlgUpdate;
	private  final String SOAP_ADDRESS_CommentUnreadFlgUpdate = KumonEnv.G_API_WEBSERVICEURL + "/CommentUnreadFlgUpdate.asmx";
	//20150413 ADD-E For 2015年度Ver. 未読対応
	
	public KumonSoap() {
	}
	
	@WorkerThread
	public DStudent SoapLogin(CurrentUser user)
	{
		DStudent student = SoapLoginOnly(user);
		
	    if(student.mSoapStatus == 0 && student.mSoapError.isEmpty()) {
			UserLogout(user.mLoginID);
	    }
		user.mLastSessionID = "";
		user.writeObject();
		
		return student;
		
	}

	@WorkerThread
	public DStudent SoapLoginOnly(CurrentUser user)
	{
		DStudent student = null;
		
		UserLogout(user.mLoginID);
    	
	    String id = user.mLoginID;
	    String pswd = user.mPassword;
	    
	    StudentLoginResponse res = StudentLogin(id, pswd);
	    student = KumonSoapToKumonData.cvtStudentLoginResponseToKumonData(res);
	    student.mStudentID = id;
	    student.mPassword = pswd;
		    
		return student;
	}

	@WorkerThread
	public void SoapLogout(CurrentUser user)
	{
		UserLogout(user.mLoginID);
	}

	@WorkerThread
	public boolean SoapStudentEntrance(String StudentID)
	{
		boolean entrance = false;
		
	    StudentEntranceResponse res = StudentEntrance(StudentID);
		    
	    if(res.mResponsedata.mEntryStatus == 1) {
	    	entrance = true;
	    }
		return entrance;
	}

	@WorkerThread
	public WDownloadResultData SoapGetRetryResultData(CurrentUser user) {
		WDownloadResultData wdownloadresultdata = new WDownloadResultData();
		
		UserLogout(user.mLoginID);
		
		ResultDataResponse res = GetAllRetryPrintSet(user.mLoginID, user.mPassword);
		wdownloadresultdata.mSoapStatus = res.mRresult.mStatus;
		wdownloadresultdata.mSoapError = res.mRresult.mError;
		if(res.mRresult.mStatus == 0 && res.mRresult.mError.isEmpty()) {
			for(int i = 0; i < res.mResponsedata.mResultDataList.size(); i++) {
				DResultData resultdata = res.mResponsedata.mResultDataList.get(i);
				resultdata.mStudentID = user.mStudentID;
				wdownloadresultdata.mDownLoadResultDataList.add(resultdata);
			}
		}
		else {
			UserLogout(user.mLoginID);
		}
		res = null;
		
		return wdownloadresultdata;
		
	}
	
	@WorkerThread
	public WQuestionDataList SoapGetQuestionData(CurrentUser user, MQuestion2 question)
	{
		WQuestionDataList questiondatalist = new WQuestionDataList();
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
		String Password = "ANDROID" + sdf.format(date) + "QUESTION";
		date  = null;
		
		PrintResponse res = GetPrint(Password, question.mKyokaID, question.mKyozaiID, question.mPrintID); 
		if(res.mRresult.mStatus == 0 && res.mRresult.mError.isEmpty()) {
			ArrayList<MQuestion2> downquestionlist = KumonSoapToKumonData.cvtPrintResponseToKumonData(res); 
			questiondatalist.mMQuestionDataList.addAll(downquestionlist);
			downquestionlist.clear();
			downquestionlist = null;
		}
		else {
			questiondatalist.mSoapStatus = res.mRresult.mStatus;
			questiondatalist.mSoapError = res.mRresult.mError;
		}
		res = null;
		
		return questiondatalist;
	}

	@WorkerThread
	public WQuestionDataList SoapGetQuestionDataByPrintNo(CurrentUser user, MQuestion2 question)
	{
		WQuestionDataList questiondatalist = new WQuestionDataList();
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
		String Password = "ANDROID" + sdf.format(date) + "QUESTION";
		date  = null;
		
		Log.d(getClass().getSimpleName()+"#SoapGetQuestionDataByPrintNo", question.mPrintID + ", " + question.mKyokaID + ", "+question.mKyozaiID + ", " + question.mPrintNo);
		PrintResponse res = GetPrintByPrintNo(Password, question.mKyokaID, question.mKyozaiID, question.mPrintNo); 
		if(res.mRresult.mStatus == 0 && res.mRresult.mError.isEmpty()) {
			ArrayList<MQuestion2> downquestionlist = KumonSoapToKumonData.cvtPrintResponseToKumonData(res); 
			questiondatalist.mMQuestionDataList.addAll(downquestionlist);
			downquestionlist.clear();
			downquestionlist = null;
		}
		else {
			UserLogout(user.mLoginID);
			user.mLastSessionID = "";
			user.writeObject();
			
			questiondatalist.mSoapStatus = res.mRresult.mStatus;
			questiondatalist.mSoapError = res.mRresult.mError;
		}
		res = null;
		
		return questiondatalist;
	}

	@WorkerThread
	public RegistGradingResultResponse SoapRegistGradingResult(CurrentUser user, DPrintSet printset)
	{
		
		
		//20140718 MOD-S For Print単位送信
		/***
		RegistGradingResultRequest req = null;
		AnswerData answerdata = null;
		
		req = new RegistGradingResultRequest();
		req.setProperty(0, user.mLastSessionID);
		req.setProperty(1, user.mStudentID);
		req.setProperty(2, printset.mKyokaID);
		req.setProperty(3, printset.mPenThickness);
		answerdata = new AnswerData();

		String work;
		for(int i = 0; i < printset.mResultList.size(); i++) 
		{
			DResultData resultdata = printset.mResultList.get(i);
			
			AnswerDataInfo answerdatainfo = new AnswerDataInfo();
			answerdatainfo.setProperty(0, resultdata.mPrintUnitID);
			answerdatainfo.setProperty(1, resultdata.mKyozaiID);
			answerdatainfo.setProperty(2, printset.mPrintSetID);	//送信はPrintSetID単位(各DResultDataのPrintSetIDはAndroid内で識別用に振っている)
			answerdatainfo.setProperty(3, resultdata.mPrintID);
			
			answerdatainfo.setProperty(4, resultdata.mLearningPlace);
			answerdatainfo.setProperty(5, resultdata.mCount);
			answerdatainfo.setProperty(6, resultdata.mScore);
			answerdatainfo.setProperty(7, resultdata.mGradingMethod);
			answerdatainfo.setProperty(8, resultdata.mGradingStatus);
			answerdatainfo.setProperty(9, resultdata.mGrade);
			answerdatainfo.setProperty(10, resultdata.mGradingResultData);
			answerdatainfo.setProperty(11, resultdata.mInkData);
			answerdatainfo.setProperty(12, resultdata.mRedComment);
			answerdatainfo.setProperty(13, resultdata.mTagComment);
			answerdatainfo.setProperty(14, resultdata.mTagText);
			
			work = Utility.getSoapFormatDate(resultdata.mStartDate);
			answerdatainfo.setProperty(15, work);
			work = Utility.getSoapFormatDate(resultdata.mEndDate);
			answerdatainfo.setProperty(16, work);
			if(resultdata.mAnswerTime < 0) {
				answerdatainfo.setProperty(17, 0L);
			}
			else {
				answerdatainfo.setProperty(17, (long)resultdata.mAnswerTime);
			}
			work = Utility.getSoapFormatDateFromString(resultdata.mScheduledDate);
			answerdatainfo.setProperty(18, work);
			answerdatainfo.setProperty(19, resultdata.mScheduledIndex);

			answerdatainfo.setProperty(20, resultdata.mScheduledNum);
			answerdatainfo.setProperty(21, resultdata.mPrintNo);
			
			answerdata.add(answerdatainfo);
		}
		req.setProperty(4, answerdata);

		//送信
		RegistGradingResultResponse response = RegistGradingResult(req);
		if(response.mRresult.mStatus == 0 && response.mRresult.mError.isEmpty()) {
		}
		else {
			UserLogout(user.mStudentID);
			user.mLastSessionID = "";
			user.writeObject();
		}
		return response;
		***/
		
		RegistGradingResultRequest req = null;
		AnswerData answerdata = null;
		RegistGradingResultResponse response = null;
		
		String work;
		for(int i = 0; i < printset.mResultList.size(); i++) 
		{
			DResultData resultdata = printset.mResultList.get(i);
			
			req = new RegistGradingResultRequest();
			req.SessionID = user.mLastSessionID;
			req.StudentAdminID = user.mStudentID;
			req.KyokaID = resultdata.mKyokaID;
			req.PenThickness = resultdata.mPenThickness;
			
			answerdata = new AnswerData();

			AnswerDataInfo answerdatainfo = new AnswerDataInfo();
			answerdatainfo.PrintUnitID = resultdata.mPrintUnitID;
			answerdatainfo.KyozaiID = resultdata.mKyozaiID;
			answerdatainfo.PrintSetID = printset.mPrintSetID;	//送信はPrintSetID単位(各DResultDataのPrintSetIDはAndroid内で識別用に振っている)
			answerdatainfo.PrintID = resultdata.mPrintID;
			
			answerdatainfo.LearningPlace = resultdata.mLearningPlace;
			answerdatainfo.Count = resultdata.mCount;
			answerdatainfo.Score = resultdata.mScore;
			answerdatainfo.GradingMethod = resultdata.mGradingMethod;
			answerdatainfo.GradingStatus = resultdata.mGradingStatus;
			answerdatainfo.Grade = resultdata.mGrade;
			answerdatainfo.GradingResultData = resultdata.mGradingResultData;
			answerdatainfo.InkData = resultdata.mInkData;
			answerdatainfo.RedComment = resultdata.mRedComment;
			answerdatainfo.TagComment = resultdata.mTagComment;
			answerdatainfo.TagText = resultdata.mTagText;
			
			work = Utility.getSoapFormatDate(resultdata.mStartDate);
			answerdatainfo.StartDate = work;
			work = Utility.getSoapFormatDate(resultdata.mEndDate);
			answerdatainfo.EndDate = work;
			if(resultdata.mAnswerTime < 0) {
				answerdatainfo.AnswerTime = 0L;
			}
			else {
				answerdatainfo.AnswerTime = (long)resultdata.mAnswerTime;
			}
			work = Utility.getSoapFormatDateFromString(resultdata.mScheduledDate);
			answerdatainfo.ScheduledDate = work;
			answerdatainfo.ScheduledIndex = resultdata.mScheduledIndex;

			answerdatainfo.ScheduledNum = resultdata.mScheduledNum;
			answerdatainfo.PrintNo = resultdata.mPrintNo;

			//20140731 ADD-S For 録音対応
			if(resultdata.mSoundRecord == null) {
				answerdatainfo.SoundRecord = null;
			}
			else {
				answerdatainfo.SoundRecord = Base64.encodeToString(resultdata.mSoundRecord, Base64.NO_WRAP);
			}
			//20140731 ADD-E For 録音対応
			
		    //20150409 ADD-S For 2015年度Ver. 未読コメント
			answerdatainfo.CommentUnreadFlg = resultdata.mCommentUnreadFlg;
		    //20150409 ADD-E For 2015年度Ver. 未読コメント
			
		    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
			//answerdatainfo.setProperty(23, resultdata.mSoundRecordStatus);
			answerdatainfo.SoundRecordStatus = resultdata.mSoundRecordStatus;
		    //20150303 ADD-E For 2015年度Ver. 音声メモステータス
			
			answerdata.add(answerdatainfo);
			req.AnswerData = answerdata;

			//送信
			RegistGradingResultResponse responseX = RegistGradingResult(req);
			if(responseX.mRresult.mStatus == 0 && responseX.mRresult.mError.isEmpty()) {
				response = responseX;
				printset.mPrintSetID = responseX.mResponsedata.mPrintSetID;
    	    	// 未送信フラグクリア
				boolean stat = KumonDataCtrl.DB_ClearRegistPrintUnitData(user.mStudentID, resultdata.mKyokaID, resultdata.mKyozaiID, resultdata.mPrintUnitID);
				if(stat == false) {
    	    		response.mRresult.mStatus = 99;
    	    		response.mRresult.mError = KumonMessage.getKumonMessage(KumonMessage.MSG_No90);
    				break;
				}
			}
			else {
				UserLogout(user.mStudentID);
				user.mLastSessionID = "";
				user.writeObject();
				response = responseX;
				break;
			}
		}
		
		return response;
		//20140718 MOD-E For Print単位送信
		
	}
	
	@WorkerThread
	public WDownloadPrintSetData SoapGetGradingResultForWeb(CurrentUser user, DPrintSet printset)
	{
		WDownloadPrintSetData downloadprintsetdata = new WDownloadPrintSetData();

		//回答データ取得
		ResultDataResponse response = GradingResultDataAndroid(user.mLoginID, user.mPassword, KumonDataCtrl.SF_GUID_NULL, printset.mKyozaiID, printset.mPrintSetID);
		downloadprintsetdata.mSoapStatus = response.mRresult.mStatus;
		downloadprintsetdata.mSoapError = response.mRresult.mError;
		if(response.mRresult.mStatus == 0 && response.mRresult.mError.isEmpty()) {
			downloadprintsetdata.mDowunLoadPrintSet = new DPrintSet();
			downloadprintsetdata.mDowunLoadPrintSet.mPrintSetID = printset.mPrintSetID;
//			ArrayList<MQuestion2> saveList = new ArrayList<MQuestion2>();
			ArrayList<String> printIds = new ArrayList<String>();
			for (DResultData res : response.mResponsedata.mResultDataList) {
				printIds.add(res.mPrintID);
			}
			HashMap<String, Boolean> existMap = MastDBIO.DB_IsExistQuestionData(printIds);
			for(int i = 0; i < response.mResponsedata.mResultDataList.size(); i++) {
				DResultData resultdata = response.mResponsedata.mResultDataList.get(i);
				resultdata.mStudentID = user.mStudentID;
				Boolean fExists = existMap.get(resultdata.mPrintID); 
				if (fExists == null || !fExists) {
//				if(MastDBIO.DB_IsExistQuestionData(resultdata.mPrintID) == false) {
					Log.d(getClass().getSimpleName(), "SoapGetQuestionData(#" + i + ", "+ resultdata.mPrintID + ")");
					MQuestion2 question = new MQuestion2();
					question.mKyokaID = resultdata.mKyokaID;
					question.mKyozaiID = resultdata.mKyozaiID;
					question.mPrintID = resultdata.mPrintID;
					WQuestionDataList questiondatalist = SoapGetQuestionData(user, question);
					if(questiondatalist.mSoapStatus == 0 && questiondatalist.mSoapError.isEmpty()) {
						if(questiondatalist.mMQuestionDataList.size() > 0) {
							MQuestion2 savequestion = questiondatalist.mMQuestionDataList.get(0);
							resultdata.mQuestion = savequestion;
//							savequestion = KumonCommon.DecompressQuestion(savequestion);
							
							MastDBIO.DB_InsertQuestionData(savequestion);
//							saveList.add(savequestion);
						}
					}
				}
				downloadprintsetdata.mDowunLoadPrintSet.mResultList.add(resultdata);
				
			}
//			if (saveList.size() > 0) {
//				MastDBIO.DB_InsertQuestionDataList(saveList);
//			}
		}
		
		UserLogout(user.mLoginID);
		user.mLastSessionID = "";
		user.writeObject();
	    return downloadprintsetdata;
	}

	@WorkerThread
	public StudentGradingStatusResponse SoapStudentGradingStatus(CurrentUser user)
	{
		StudentGradingStatusResponse res = StudentGradingStatus(user.mStudentID);
		
	    return res;
	}

	//20140618 ADD-S For 分割受信
	@WorkerThread
	public WDownloadPrintSetIDList SoapGetPrintSetIDList(CurrentUser user) {
		WDownloadPrintSetIDList wdownloadPrintSetIDList = new WDownloadPrintSetIDList();
		
		Date limitDate = StudentClientCommData.getKeepDate();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
		String limit = sdf2.format(limitDate);
		
		PrintSetIDListResponse res = GetPrintSetIDList(user.mLoginID, user.mPassword, limit);
		wdownloadPrintSetIDList.mSoapStatus = res.mRresult.mStatus;
		wdownloadPrintSetIDList.mSoapError = res.mRresult.mError;
		if(res.mRresult.mStatus == 0 && res.mRresult.mError.isEmpty()) {
			for(int i = 0; i < res.mResponsedata.mResultDataList.size(); i++) {
				DResultData resultdata = res.mResponsedata.mResultDataList.get(i);
				resultdata.mStudentID = user.mStudentID;
				wdownloadPrintSetIDList.mDownLoadResultDataList.add(resultdata);
			}
		}
		
		return wdownloadPrintSetIDList;
		
	}

	@WorkerThread
	public WDownloadResultData SoapGetPrintSetData(CurrentUser user, DResultData printsetresultdata) {
		WDownloadResultData wdownloadresultdata = new WDownloadResultData();

		ResultDataResponse response = GradingResultDataAndroid(user.mLoginID, user.mPassword, printsetresultdata.mKyokaID, printsetresultdata.mKyozaiID, printsetresultdata.mPrintSetID);
		wdownloadresultdata.mSoapStatus = response.mRresult.mStatus;
		wdownloadresultdata.mSoapError = response.mRresult.mError;
		if(response.mRresult.mStatus == 0 && response.mRresult.mError.isEmpty()) {
			for(int i = 0; i < response.mResponsedata.mResultDataList.size(); i++) {
				DResultData resultdata = response.mResponsedata.mResultDataList.get(i);
				resultdata.mStudentID = user.mStudentID;
				wdownloadresultdata.mDownLoadResultDataList.add(resultdata);
			}
		}
		response = null;
		
		return wdownloadresultdata;
	}
	
	//20140618 ADD-E For 分割受信
	
	/////////////////////////////////////////////////////////////////////////////////////
	@WorkerThread
	public StudentLoginResponse StudentLogin(String id, String pswd)
	{
		StudentLoginResponse studentloginresponse = new StudentLoginResponse();
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_StudentLogin);

		StudentLoginRequest req = new StudentLoginRequest();
		req.LoginID = id;
		req.Password = Utility.digest(pswd);
		
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new StudentLoginRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_StudentLogin, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.call(SOAP_ACTION_StudentLogin, envelope);
        	response = (SoapObject) envelope.getResponse();
        	studentloginresponse.Parser(response);
        }
        catch (Exception exception)
        {
        	studentloginresponse.mResult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        
        return studentloginresponse;
	}
	
	@WorkerThread
	public KumonSoapResult UserLogout(String id)
	{
		KumonSoapResult kumonsoapresult = new KumonSoapResult();
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_UserLogout);

		UserLogoutRequest req = new UserLogoutRequest();
		req.LoginID = id;
		
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new UserLogoutRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_UserLogout, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.call(SOAP_ACTION_UserLogout, envelope);
        	response = (SoapObject) envelope.getResponse();
        	
        	UserLogoutResponse userlogoutresponse = new UserLogoutResponse();
        	userlogoutresponse.Parser(response);
        	
        	kumonsoapresult = userlogoutresponse.mRresult;
        }
        catch (Exception exception)
        {
        	kumonsoapresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return kumonsoapresult;
	}

	@WorkerThread
	public StudentEntranceResponse StudentEntrance(String StudentID)
	{
		StudentEntranceResponse studententranceresponse = new StudentEntranceResponse();
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_StudentEntrance);

		StudentEntranceRequest req = new StudentEntranceRequest();
		req.StudentAdminID = StudentID;
		
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new StudentEntranceRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_StudentEntrance, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.call(SOAP_ACTION_StudentEntrance, envelope);
        	response = (SoapObject) envelope.getResponse();
        	studententranceresponse.Parser(response);
        }
        catch (Exception exception)
        {
        	studententranceresponse.mResult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return studententranceresponse;
	}
	
	@WorkerThread
	public ResultDataResponse GetAllRetryPrintSet(String loginid, String Password) {
		ResultDataResponse resultdataresponse = new ResultDataResponse();
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_AllRetryPrintSet);

		RetryResultDataRequest req = new RetryResultDataRequest();
		req.LoginID = loginid;
		req.Password = Utility.digest(Password);
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new RetryResultDataRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_AllRetryPrintSet, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.call(SOAP_ACTION_AllRetryPrintSet, envelope);
        	response = (SoapObject) envelope.getResponse();
        	resultdataresponse.Parser(response);
        }
        catch (Exception exception)
        {
        	resultdataresponse.mRresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return resultdataresponse;
		
	}

	@WorkerThread
	public ResultDataResponse GradingResultDataAndroid(String loginid, String Password, String kyoka, String kyozai, String PrintSetID)
	{
		ResultDataResponse resultdataresponse = new ResultDataResponse();
    	
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_GradingResultDataAndroid);

		GradingResultDataAndroidRequest req = new GradingResultDataAndroidRequest();
		req.LoginID = loginid;
		req.Password = Utility.digest(Password);
		req.KyokaID = kyoka;
		req.KyozaiID = kyozai;
		req.PrintSetID = PrintSetID;
		
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new GradingResultDataAndroidRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_GradingResultDataAndroid, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.call(SOAP_ACTION_GradingResultDataAndroid, envelope);
        	response = (SoapObject) envelope.getResponse();
        	resultdataresponse.Parser(response);
        }
        catch (Exception exception)
        {
        	resultdataresponse.mRresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return resultdataresponse;
	}

	@WorkerThread
	public PrintResponse GetPrint(String Password, String KyokaID, String KyozaiID, String PrintID)
	{
		PrintResponse printresponse = new PrintResponse();
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_Print);

		PrintRequest req = new PrintRequest();
		req.Password = Password;
		req.KyokaID = KyokaID;
		req.KyozaiID = KyozaiID;
		req.PrintID = PrintID;
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new PrintRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Print, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.debug = true;
        	httpTransport.call(SOAP_ACTION_Print, envelope);
        	response = (SoapObject) envelope.getResponse();
        	printresponse.Parser(response);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	printresponse.mRresult.mError = e.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return printresponse;
	}

	@WorkerThread
	public PrintResponse GetPrintByPrintNo(String Password, String KyokaID, String KyozaiID, int PrintNo)
	{
		PrintResponse printresponse = new PrintResponse();
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_PrintByPrintNo);

		PrintByPrintNoRequest req = new PrintByPrintNoRequest();
		req.setProperty(0, Password);
		req.setProperty(1, KyokaID);
		req.setProperty(2, KyozaiID);
		req.setProperty(3, PrintNo);
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new PrintByPrintNoRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_PrintByPrintNo, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.call(SOAP_ACTION_PrintByPrintNo, envelope);
        	response = (SoapObject) envelope.getResponse();
        	printresponse.Parser(response);
        }
        catch (Exception exception)
        {
        	printresponse.mRresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return printresponse;
	}
	
	@WorkerThread
	public RegistGradingResultResponse RegistGradingResult(RegistGradingResultRequest registrequest)
	{
		RegistGradingResultResponse registresponse = new RegistGradingResultResponse();
    	
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_RegistGradingResult);
		
		
        request.addProperty("Request", registrequest);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new RegistGradingResultRequest().getClass());
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "AnswerData", new AnswerData().getClass());
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "AnswerDataInfo", new AnswerDataInfo().getClass());
    	SoapObject response = null;
        
        
    	//20131101 MOD-S For TimeOut
        //HttpTransportSE httpTransport = new HttpTransportSE();
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_RegistGradingResult, KumonEnv.G_SOAP_TIMEOUT);
    	//20131101 MOD-E For TimeOut
        httpTransport.debug = true; 
        try
        {
        	httpTransport.call(SOAP_ACTION_RegistGradingResult, envelope);
//        	Log.d("dump Request: " ,httpTransport.requestDump);
        	response = (SoapObject) envelope.getResponse();
        	registresponse.Parser(response);
        }
        catch (Exception exception)
        {
        	registresponse.mRresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return registresponse;
	}

	@WorkerThread
	public StudentGradingStatusResponse StudentGradingStatus(String studentID)
	{
		StudentGradingStatusResponse studentGradingStatusResponse = new StudentGradingStatusResponse();
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_StudentGradingStatus);

		StudentGradingStatusRequest req = new StudentGradingStatusRequest();
		req.SessionID = KumonDataCtrl.SF_GUID_NULL;
		req.StudentAdminID = studentID;
		
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new StudentGradingStatusRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_StudentGradingStatus, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.call(SOAP_ACTION_StudentGradingStatus, envelope);
        	response = (SoapObject) envelope.getResponse();
        	studentGradingStatusResponse.Parser(response);
        }
        catch (Exception exception)
        {
        	studentGradingStatusResponse.mRresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return studentGradingStatusResponse;
	}
	//20140618 ADD-S For 分割受信
	@WorkerThread
	public PrintSetIDListResponse GetPrintSetIDList(String loginid, String Password, String limitdate) {
		PrintSetIDListResponse printsetIDListResponse = new PrintSetIDListResponse();
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_PrintSetIDList);

		PrintSetIDListRequest req = new PrintSetIDListRequest();
		req.LoginID = loginid;
		req.Password = Utility.digest(Password);
		req.LimitDate = limitdate;
        request.addProperty("Request", req);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new PrintSetIDListRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_PrintSetIDList, KumonEnv.G_SOAP_TIMEOUT);
        try
        {
        	httpTransport.call(SOAP_ACTION_PrintSetIDList, envelope);
        	response = (SoapObject) envelope.getResponse();
        	printsetIDListResponse.Parser(response);
        }
        catch (Exception exception)
        {
        	printsetIDListResponse.mRresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return printsetIDListResponse;
		
	}
	
	//20140618 ADD-E For 分割受信
	//20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
	@WorkerThread
	public boolean SoapMaintenanceCheck()
	{
		boolean stat = false;
		MaintenanceCheckRequest req = new MaintenanceCheckRequest();
		MaintenanceCheckResponse response = MaintenanceCheck(req);
		if(response.Result.mStatus == 0 && response.Result.mError.isEmpty()) {
			if(response.ResponseData.MaintenanceStatus != 0) {
				stat = true;
			}
		}
		return stat;
	}
	
	@WorkerThread
	public MaintenanceCheckResponse MaintenanceCheck(MaintenanceCheckRequest checkRequest)
	{
		MaintenanceCheckResponse checkResponse = new MaintenanceCheckResponse();
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_MaintenanceCheck);
        request.addProperty("Request", checkRequest);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new MaintenanceCheckRequest().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_MaintenanceCheck, KumonEnv.G_SOAP_TIMEOUT);
        httpTransport.debug = true; 
        try
        {
        	httpTransport.call(SOAP_ACTION_MaintenanceCheck, envelope);
        	response = (SoapObject) envelope.getResponse();
        	checkResponse.Parse(response);
        }
        catch (Exception exception)
        {
        	checkResponse.Result.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return checkResponse;
	}
	//20150110 ADD-E For 2015年度Ver. メンテナンス中チェック

    //20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
	@WorkerThread
	public CollectAndroidLogResponse SoapSendAndroidLog(CurrentUser user, String[] msg)
	{
		if(msg.length > 0) {
			CollectAndroidLogRequest req = null;
			AndroidLogList loglist = null;
		
			req = new CollectAndroidLogRequest();
			req.LoginID = user.mLoginID;
			req.StudentAdminID = user.mStudentID;
			loglist = new AndroidLogList();

			for(int i = 0; i < msg.length; i++) 
			{
				AndroidLog log = new AndroidLog();
				log.Msg = msg[i];
				loglist.add(log);
			}
			req.LogList = loglist;

			//送信
			CollectAndroidLogResponse response = SendAndroidLog(req);
			if(response.mRresult.mStatus == 0 && response.mRresult.mError.isEmpty()) {
			}
			else {
				UserLogout(user.mStudentID);
				user.mLastSessionID = "";
				user.writeObject();
			}
			return response;
		}
		else {
			CollectAndroidLogResponse response = new CollectAndroidLogResponse();
			response.mRresult.mStatus = 0;
			response.mRresult.mError = "";
			return response;
		}
		
	}
	@WorkerThread
	public CollectAndroidLogResponse SendAndroidLog(CollectAndroidLogRequest logrequest)
	{
		CollectAndroidLogResponse logresponse = new CollectAndroidLogResponse();
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_CollectAndroidLog);
        request.addProperty("Request", logrequest);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new CollectAndroidLogRequest().getClass());
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "LogList", new AndroidLogList().getClass());
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "AndroidLog", new AndroidLog().getClass());
    	SoapObject response = null;
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_CollectAndroidLog, KumonEnv.G_SOAP_TIMEOUT);
        httpTransport.debug = true; 
        try
        {
        	httpTransport.call(SOAP_ACTION_CollectAndroidLog, envelope);
        	response = (SoapObject) envelope.getResponse();
        	logresponse.Parser(response);
        }
        catch (Exception exception)
        {
        	logresponse.mRresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return logresponse;
	}
	//20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
	
	//20150413 ADD-S For 2015年度Ver. 未読対応
	//private  final String OPERATION_NAME_CommentUnreadFlgUpdate = "SetUnreadFlg"; 
	//private final String SOAP_ACTION_CommentUnreadFlgUpdate = WSDL_TARGET_NAMESPACE + OPERATION_NAME_CommentUnreadFlgUpdate;
	//private  final String SOAP_ADDRESS_CommentUnreadFlgUpdate = KumonEnv.G_API_WEBSERVICEURL + "/CommentUnreadFlgUpdate.asmx";
	@WorkerThread
	public CommentUnreadFlgUpdateResponse SoapSetUnreadFlg(CurrentUser user, ArrayList<String> printunitlist)
	{
		if(printunitlist.size() > 0) {
			CommentUnreadFlgUpdateRequest req = null;
			UnreadFlgInfoList list = null;
		
			req = new CommentUnreadFlgUpdateRequest();
			req.SessionID = KumonDataCtrl.SF_GUID_NULL;
			req.StudentAdminID = user.mStudentID;
			list = new UnreadFlgInfoList();

			for(int i = 0; i < printunitlist.size(); i++) 
			{
				UnreadFlgInfo unreadflginfo = new UnreadFlgInfo();
				unreadflginfo.PrintUnitID = printunitlist.get(i);
				unreadflginfo.CommentUnreadFlg = 0;
				list.add(unreadflginfo);
			}
			req.UnreadFlgList = list;

			//送信
			CommentUnreadFlgUpdateResponse response = SetUnreadFlg(req);
			if(response.mRresult.mStatus == 0 && response.mRresult.mError.isEmpty()) {
			}
			else {
			}
			return response;
		}
		else {
			CommentUnreadFlgUpdateResponse response = new CommentUnreadFlgUpdateResponse();
			response.mRresult.mStatus = 0;
			response.mRresult.mError = "";
			return response;
		}
		
	}

	@WorkerThread
	public CommentUnreadFlgUpdateResponse SetUnreadFlg(CommentUnreadFlgUpdateRequest unreadrequest)
	{
		CommentUnreadFlgUpdateResponse unreadresponse = new CommentUnreadFlgUpdateResponse();
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_CommentUnreadFlgUpdate);
        request.addProperty("Request", unreadrequest);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "Request", new CommentUnreadFlgUpdateRequest().getClass());
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "UnreadFlgList", new UnreadFlgInfoList().getClass());
        envelope.addMapping(WSDL_TARGET_NAMESPACE, "UnreadFlgInfo", new UnreadFlgInfo().getClass());
    	SoapObject response = null;
        
        
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_CommentUnreadFlgUpdate, KumonEnv.G_SOAP_TIMEOUT);
        httpTransport.debug = true; 
        try
        {
        	httpTransport.call(SOAP_ACTION_CommentUnreadFlgUpdate, envelope);
        	response = (SoapObject) envelope.getResponse();
        	unreadresponse.Parser(response);
        }
        catch (Exception exception)
        {
        	unreadresponse.mRresult.mError = exception.getMessage();
        }
        finally{
        	request = null;
        	envelope = null;
        	httpTransport = null;
        	response = null;
//    		System.gc();
       	}
        return unreadresponse;
	}
	
	//20150413 ADD-E For 2015年度Ver. 未読対応

}

