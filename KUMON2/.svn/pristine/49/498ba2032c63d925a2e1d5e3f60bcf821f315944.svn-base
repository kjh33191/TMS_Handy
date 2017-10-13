package kumon2014.webservice;


import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class CollectAndroidLogResponse {
	
	public KumonSoapResult mRresult;

	
	public CollectAndroidLogResponse() {
		mRresult = new KumonSoapResult();
	}
	
	@SuppressWarnings("unused")
	public boolean Parser(SoapObject response) {
		boolean bret = false;
		if (true) {
			SoapObject resultSoap = (SoapObject)response.getProperty("Result");
			if (resultSoap != null) {
				mRresult.setStatus(resultSoap.getProperty("Status").toString());
				mRresult.setError(resultSoap.getProperty("Error").toString());
			}
		}
		else {
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
		}
    	bret = true;
    	
		return bret;
	}
	
}


