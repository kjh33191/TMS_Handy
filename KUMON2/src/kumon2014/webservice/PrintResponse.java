package kumon2014.webservice;

import java.util.ArrayList;
import java.util.Date;

import kumon2014.common.Utility;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import android.util.Base64;

public class PrintResponse {
	public KumonSoapResult mRresult;
	public PrintResponseData mResponsedata;
		
	public PrintResponse() {
		mRresult = new KumonSoapResult();
		mResponsedata = new PrintResponseData();
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
				Parser_ResponseData((SoapObject)response.getProperty(i));
			}
		}        	
			
		return bret;
	}
	public boolean Parser_ResponseData(SoapObject responsedataSoap) {
		boolean bret = false;
		
		for(int i=0; i< responsedataSoap.getPropertyCount(); i++){    
			PropertyInfo pi = new PropertyInfo();
			responsedataSoap.getPropertyInfo(i, pi);
	
			if(pi.getName().equals("Print")) {
				Parser_PrintInfo((SoapObject)responsedataSoap.getProperty(i));
			}
		}        	
		
		bret = true;
		return bret;
	}
	public boolean Parser_PrintInfo(SoapObject printinfoListSoap) {
		boolean bret = false;
		
		int cnt = printinfoListSoap.getPropertyCount();
   		for(int i=0; i < cnt; i++){    
   			SoapObject printsetSoap = (SoapObject)printinfoListSoap.getProperty(i);
   			PrintInfoResponseData printinforesponsedata = new PrintInfoResponseData();
   			for(int j=0; j < printsetSoap.getPropertyCount(); j++){
   				PropertyInfo pi = new PropertyInfo();
   				printsetSoap.getPropertyInfo(j, pi);
    			if(pi.getName().equals("PrintID")) {
    				printinforesponsedata.mPrintID = printsetSoap.getProperty("PrintID").toString();
    			}
    			else if(pi.getName().equals("KyokaID")) {
    				printinforesponsedata.mKyokaID = printsetSoap.getProperty("KyokaID").toString();
    	    	}
    			else if(pi.getName().equals("KyozaiID")) {
    				printinforesponsedata.mKyozaiID = printsetSoap.getProperty("KyozaiID").toString();
    	    	}
    			else if(pi.getName().equals("PrintNo")) {
    				printinforesponsedata.mPrintNo = Integer.parseInt(printsetSoap.getProperty("PrintNo").toString());
    	    	}
    			else if(pi.getName().equals("QuestionData")) {
    				String tmp = printsetSoap.getProperty("QuestionData").toString();
    				printinforesponsedata.mQuestionData = Base64.decode(tmp, Base64.NO_WRAP);
    	    	}
    		    //20150121 ADD-S For 2015年度Ver. 教材更新
    			else if(pi.getName().equals("UpdateTime")) {
    				String tmp = printsetSoap.getProperty("UpdateTime").toString();
    				if(tmp != null && tmp.length() > 0) {
    					printinforesponsedata.mUpdateTime = Utility.getDateSoapFromString(tmp);
    				}
    				else {
    					printinforesponsedata.mUpdateTime = null;
    				}
    	    	}
    		    //20150121 ADD-E For 2015年度Ver. 教材更新
    		}
   			mResponsedata.mPrintResponseDataList.add(printinforesponsedata);
    	}        	
		
    	bret = true;
		return bret;
	}
	
}
class PrintResponseData {
	public ArrayList<PrintInfoResponseData>  mPrintResponseDataList;
    
	public PrintResponseData() {
		mPrintResponseDataList = new ArrayList<PrintInfoResponseData>();
	}
}

class PrintInfoResponseData {
	public String 		mPrintID = "";
	public String 		mKyokaID = "";
	public String 		mKyozaiID = "";
	public int 			mPrintNo = 0;
	public byte[]		mQuestionData = null;
    //20150121 ADD-S For 2015年度Ver. 教材更新
	public Date			mUpdateTime = null;
    //20150121 ADD-E For 2015年度Ver. 教材更新

	public PrintInfoResponseData() {
	}
}

