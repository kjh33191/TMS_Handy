package kumon2014.webservice;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;


public class StudentGradingStatusResponse {
	public KumonSoapResult mRresult;
	public ResponseData mResponsedata;
		
	
	public StudentGradingStatusResponse() {
		mRresult = new KumonSoapResult();
		mResponsedata = new ResponseData();
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
						mRresult.setStatus(resultSoap.getProperty("Status").toString());
					}
					else if(pi.getName().equals("Error")) {
						mRresult.setError(resultSoap.getProperty("Error").toString());
					}
				}
			}
			else if(pinfo.getName().equals("ResponseData")) {
				SoapObject resultSoap = (SoapObject)response.getProperty(i);
				for(int j=0; j< resultSoap.getPropertyCount();j++){    
					PropertyInfo pi = new PropertyInfo();
					resultSoap.getPropertyInfo(j, pi);
	    	    		
					String work;
					if(pi.getName().equals("GradingWaitNum")) {
						work = resultSoap.getProperty("GradingWaitNum").toString();
						mResponsedata.mGradingWaitNum = Integer.valueOf(work);
					}
					else if(pi.getName().equals("TodayGrade")) {
						work = resultSoap.getProperty("TodayGrade").toString();
						mResponsedata.mTodayGrade = Integer.valueOf(work);
					}
					else if(pi.getName().equals("HomeworkGrade")) {
						work = resultSoap.getProperty("HomeworkGrade").toString();
						mResponsedata.mHomeworkGrade = Integer.valueOf(work);
					}
				}
			}
		}        	
		return bret;
	}
	
	public int GetGradingWaitNum() {
		return mResponsedata.mGradingWaitNum;
	}
	public int GetTodayGrade() {
		return mResponsedata.mTodayGrade;
	}
	public int GetHomeworkGrade() {
		return mResponsedata.mHomeworkGrade;
	}
}

class ResponseData {
	public int 		mGradingWaitNum = 0;
	public int 		mTodayGrade = 0;
	public int 		mHomeworkGrade = 0;
	
	public ResponseData() {
	}
}
