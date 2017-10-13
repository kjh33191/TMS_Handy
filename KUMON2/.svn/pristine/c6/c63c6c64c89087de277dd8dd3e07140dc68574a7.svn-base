package kumon2014.webservice;


import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class RegistGradingResultResponse {
	
	public KumonSoapResult mRresult;
	public RegistGradingResultResponseData mResponsedata;

	
	public RegistGradingResultResponse() {
		mRresult = new KumonSoapResult();
		mResponsedata = new RegistGradingResultResponseData();
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
    	    		
    	    		if(pi.getName().equals("PrintSetID")) {
    	    			mResponsedata.mPrintSetID = resultSoap.getProperty("PrintSetID").toString();
    	    		}
    	    	}
    		}
    		
    	}        	
    	bret = true;
    	
		return bret;
	}
	
	
}
class RegistGradingResultResponseData {
	public String		mPrintSetID = "";	//プリントセットID

}


