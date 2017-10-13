package kumon2014.webservice;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class CommentUnreadFlgUpdateResponse {
	
	public KumonSoapResult mRresult;

	
	public CommentUnreadFlgUpdateResponse() {
		mRresult = new KumonSoapResult();
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
    	}        	
    	bret = true;
    	
		return bret;
	}
	
}


