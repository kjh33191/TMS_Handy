package kumon2014.webservice;

import java.util.ArrayList;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import kumon2014.kumondata.DResultData;
public class PrintSetIDListResponse {
	
	public KumonSoapResult mRresult;
	public PrintSetIDListResponseData mResponsedata;
	
	public PrintSetIDListResponse() {
		mRresult = new KumonSoapResult();
		mResponsedata = new PrintSetIDListResponseData();
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
    	bret = true;
    	
		return bret;
	}
	public boolean Parser_ResponseData(SoapObject responsedataSoap) {
		boolean bret = false;
		
		for(int i=0; i< responsedataSoap.getPropertyCount(); i++){    
			PropertyInfo pi = new PropertyInfo();
    		responsedataSoap.getPropertyInfo(i, pi);

    		if(pi.getName().equals("PrintSetInfo")) {
    			Parser_PrintSetIDData((SoapObject)responsedataSoap.getProperty(i));
    		}
    	}        	
		
    	bret = true;
		return bret;
	}
	public boolean Parser_PrintSetIDData(SoapObject printdataListSoap) {
		boolean bret = false;
		
		int cnt = printdataListSoap.getPropertyCount();
   		for(int i=0; i < cnt; i++){    
   			SoapObject printdataSoap = (SoapObject)printdataListSoap.getProperty(i);
			DResultData resultdata = new DResultData();
   			
   			for(int j=0; j < printdataSoap.getPropertyCount(); j++){    
   				PropertyInfo pi = new PropertyInfo();
   				printdataSoap.getPropertyInfo(j, pi);
   				
    			if(pi.getName().equals("KyokaID")) {
    				resultdata.mKyokaID =  printdataSoap.getProperty("KyokaID").toString();
    			}
    			else if(pi.getName().equals("KyozaiID")) {
    				resultdata.mKyozaiID =  printdataSoap.getProperty("KyozaiID").toString();
    			}
    			else if(pi.getName().equals("PrintSetID")) {
    				resultdata.mPrintSetID = printdataSoap.getProperty("PrintSetID").toString();
    			}
    		}
			mResponsedata.mResultDataList.add(resultdata);
    	}        	
    	bret = true;
		return bret;
	}
}


class PrintSetIDListResponseData {
	public ArrayList<DResultData> 	mResultDataList = null;
    
	public PrintSetIDListResponseData() {
		mResultDataList = new ArrayList<DResultData>();
	}
}
