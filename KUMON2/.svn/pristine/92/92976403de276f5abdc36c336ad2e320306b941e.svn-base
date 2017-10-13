package kumon2014.webservice;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;


public class StudentEntranceResponse {
	
	public KumonSoapResult mResult;
	public StudentEntranceResponseData mResponsedata;
	
	public StudentEntranceResponse() {
		mResult = new KumonSoapResult();
		mResponsedata = new StudentEntranceResponseData();
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
    	    		
    	    		if(pi.getName().equals("EntryStatus")) {
    	    			mResponsedata.setEntryStatus(responsedataSoap.getProperty("EntryStatus").toString());
    	    		}
    	    	}
    			
    		}
    	}        	
		
		return bret;
	}
}

class StudentEntranceResponseData {
	public int			mEntryStatus;

	public StudentEntranceResponseData() {
		mEntryStatus = 0;
	}
	public void setEntryStatus(String entrystatus) {
		mEntryStatus = Integer.parseInt(entrystatus); 
	}
}
