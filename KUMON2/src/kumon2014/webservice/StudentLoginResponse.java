package kumon2014.webservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;


public class StudentLoginResponse {
	
	public KumonSoapResult mResult;
	public StudentLoginResponseData mResponsedata;
	
	public StudentLoginResponse() {
		mResult = new KumonSoapResult();
		mResponsedata = new StudentLoginResponseData();
	}
	
	public boolean Parser(SoapObject response) {
		boolean bret = false;
		
    	for(int i=0; i< response.getPropertyCount();i++){    
    		PropertyInfo pinfo = new PropertyInfo();
    		response.getPropertyInfo(i, pinfo);
    		
    		if(pinfo.getName().equals("Result")) {
    			SoapObject resultSoap = (SoapObject)response.getProperty(i);
    	    	for(int j=0; j< resultSoap.getPropertyCount();j++){    
    	    		PropertyInfo pi = new PropertyInfo();
    	    		resultSoap.getPropertyInfo(j, pi);
    	    		
    	    		if(pi.getName().equals("Status")) {
    	    			mResult.setStatus(resultSoap.getProperty("Status").toString());
    	    		}
    	    		else if(pi.getName().equals("Error")) {
    	    			mResult.setError(resultSoap.getProperty("Error").toString());
    	    		}
    	    	}
    		}
    		else if(pinfo.getName().equals("ResponseData")) {
    			SoapObject responsedataSoap = (SoapObject)response.getProperty(i);
    	    	for(int j=0; j< responsedataSoap.getPropertyCount();j++){    
    	    		PropertyInfo pi = new PropertyInfo();
    	    		responsedataSoap.getPropertyInfo(j, pi);
    	    		
    	    		if(pi.getName().equals("SessionID")) {
    	    			mResponsedata.setSessionID(responsedataSoap.getProperty("SessionID").toString());
    	    		}
    	    		else if(pi.getName().equals("StudentAdminID")) {
    	    			mResponsedata.setStudentAdminID(responsedataSoap.getProperty("StudentAdminID").toString());
    	    		}
    	    		else if(pi.getName().equals("LastUpdate")) {
    	    			mResponsedata.setLastUpdate(responsedataSoap.getProperty("LastUpdate").toString());
    	    		}
    	    		else if(pi.getName().equals("StudentID")) {
    	    			mResponsedata.setStudentID(responsedataSoap.getProperty("StudentID").toString());
    	    		}
    	    		else if(pi.getName().equals("Name")) {
    	    			mResponsedata.setName(responsedataSoap.getProperty("Name").toString());
    	    		}
    	    		else if(pi.getName().equals("NameKana")) {
    	    			mResponsedata.setNameKana(responsedataSoap.getProperty("NameKana").toString());
    	    		}
    	    		else if(pi.getName().equals("StudentStatus")) {
    	    			mResponsedata.setStudentStatus(responsedataSoap.getProperty("StudentStatus").toString());
    	    		}
    	    		else if(pi.getName().equals("PenThickness")) {
    	    			mResponsedata.setPenThickness(responsedataSoap.getProperty("PenThickness").toString());
    	    		}
    	    		else if(pi.getName().equals("StudentKyokaInfo")) {
    	    			SoapObject studentkyokainfoListSoap = (SoapObject)responsedataSoap.getProperty(j);
    	    			int cnt = studentkyokainfoListSoap.getPropertyCount();
    	    			mResponsedata.addKyokaInfo(cnt);
	    	    		for(int k=0; k < cnt; k++){    
	    	    			SoapObject studentkyokainfoSoap = (SoapObject)studentkyokainfoListSoap.getProperty(k);

	    	    			for(int l=0; l < studentkyokainfoSoap.getPropertyCount(); l++){    
        	    			
    	    	    			PropertyInfo pk = new PropertyInfo();
    	    	    			studentkyokainfoSoap.getPropertyInfo(l, pk);
    	    	    		
    	    	    			if(pk.getName().equals("KyokaID")) {
    	    	    				mResponsedata.setKyokaInfo_KyokaID(k, studentkyokainfoSoap.getProperty("KyokaID").toString());
    	    	    			}
    	    	    			else if(pk.getName().equals("StudentKyokaID")) {
    	    	    				mResponsedata.setKyokaInfo_StudentKyokaID(k, studentkyokainfoSoap.getProperty("StudentKyokaID").toString());
    	    	    			}
    	    	    			else if(pk.getName().equals("LearningStatus")) {
    	    	    				mResponsedata.setKyokaInfo_LearningStatus(k, studentkyokainfoSoap.getProperty("LearningStatus").toString());
    	    	    			}
    	    	    			else if(pk.getName().equals("KyozaiProgressStatus")) {
    	    	    				mResponsedata.setKyokaInfo_KyozaiProgressStatus(k, studentkyokainfoSoap.getProperty("KyozaiProgressStatus").toString());
    	    	    			}
    	    	    		}
    	    	    	//
	    	    		}    		
    	    		}
    	    	}
    			
    		}
    	}        	
		
		return bret;
	}
}

class StudentLoginResponseData {
	public String 		mSessionID;
	public String 		mStudentAdminID;
	public Date  		mLastUpdate;
	public String		mStudentID;
	public String		mName;
	public String		mNameKana;
	public int			mStudentStatus;
	public int			mPenThickness;

    StudentKyokaInfoResponse[]  mKyokaInfo;
    
	public StudentLoginResponseData() {
		mSessionID = "";
		mStudentAdminID = "";
		mLastUpdate = null;
		mStudentID = "";
		mName = "";
		mNameKana = "";
		mStudentStatus = 0;
		mPenThickness = 0;
		
		mKyokaInfo = null;
	}
	public void addKyokaInfo(int cnt) {
		if(cnt > 0) {
			mKyokaInfo = new StudentKyokaInfoResponse[cnt]; 
			for(int i = 0; i < cnt; i++) {
				mKyokaInfo[i] = new StudentKyokaInfoResponse(); 
			}
		}
		else {
			mKyokaInfo = null;
		}
	}
    
	public void setSessionID(String sessionid) {
		mSessionID = sessionid; 
	}
	public void setStudentAdminID(String studentadminid) {
		mStudentAdminID = studentadminid; 
	}
	public void setLastUpdate(String lastupdate) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.JAPAN);
		
		try {
			String temp = lastupdate.replace('T', ' ');
			mLastUpdate = sdf1.parse(temp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			mLastUpdate = null;
		}
	}
	public void setStudentID(String studentid) {
		mStudentID = studentid; 
	}
	public void setName(String name) {
		mName = name; 
	}
	public void setNameKana(String namekana) {
		mNameKana = namekana; 
	}
	public void setStudentStatus(String studentstatus) {
		mStudentStatus = Integer.parseInt(studentstatus); 
	}
	public void setPenThickness(String penthickness) {
		mPenThickness = Integer.parseInt(penthickness); 
	}

	public void setKyokaInfo_KyokaID(int pos , String kyokaid) {
		if(mKyokaInfo != null & (pos >= 0 && pos < mKyokaInfo.length)) {
			mKyokaInfo[pos].setKyokaID(kyokaid);
		}
	}
	public void setKyokaInfo_StudentKyokaID(int pos , String studentkyokaid) {
		if(mKyokaInfo != null & (pos >= 0 && pos < mKyokaInfo.length)) {
			mKyokaInfo[pos].setStudentKyokaID(studentkyokaid);
		}
	}
	public void setKyokaInfo_LearningStatus(int pos , String learningstatus) {
		if(mKyokaInfo != null &(pos >= 0 && pos < mKyokaInfo.length)) {
			mKyokaInfo[pos].setLearningStatus(learningstatus);
		}
	}
	public void setKyokaInfo_KyozaiProgressStatus(int pos , String kyozaiprogressstatus) {
		if(mKyokaInfo != null &(pos >= 0 && pos < mKyokaInfo.length)) {
			mKyokaInfo[pos].setKyozaiProgressStatus(kyozaiprogressstatus);
		}
	}
	
}
class StudentKyokaInfoResponse {
	public String 		mKyokaID;
	public String 		mStudentKyokaID;
	public int 			mLearningStatus;
	public int 			mKyozaiProgressStatus;
	
	public StudentKyokaInfoResponse() {
		mKyokaID = "";
		mStudentKyokaID = "";
		mLearningStatus = 0;
		mKyozaiProgressStatus = 0;
	}
	public void setKyokaID(String kyokaid) {
		mKyokaID = kyokaid; 
	}
	public void setStudentKyokaID(String studentkyokaid) {
		mStudentKyokaID = studentkyokaid; 
	}
	public void setLearningStatus(String learningstatus) {
		mLearningStatus = Integer.parseInt(learningstatus); 
	}
	public void setKyozaiProgressStatus(String kyozaiprogressstatus) {
		mKyozaiProgressStatus = Integer.parseInt(kyozaiprogressstatus); 
	}
	
}
