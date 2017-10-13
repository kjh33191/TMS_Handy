package kumon2014.webservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.ksoap2.serialization.SoapObject;

public class MaintenanceCheckResponse extends BaseSoapResponse {
	@Annotation.DataMember
	public MaintenanceCheckResponseResponseData ResponseData = new MaintenanceCheckResponseResponseData();

}

class MaintenanceCheckResponseResponseData extends Parsable {
	@Annotation.DataMember
	public int 			MaintenanceStatus;
	@Annotation.DataMember
	public Date  		MaintenanceDate;
    
	public MaintenanceCheckResponseResponseData() {
		MaintenanceStatus = 0;
		MaintenanceDate = null;
	}
	@Override
	public boolean Parse(SoapObject result) {
		try {
			Object status = result.getProperty("MaintenanceStatus");
			if (status != null) {
				MaintenanceStatus = Integer.parseInt(status.toString());
			}
		}
		catch (Exception unused) {
		}
		try {
			Object date = result.getProperty("MaintenanceDate");
			if (date != null) {
				setMaintenanceDate(date.toString());
			}
		}
		catch (Exception unused) {
		}
		return true;
	}
	void setMaintenanceDate(String maintenanceDate) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN);
		try {
			String temp = maintenanceDate.replace('T', ' ');
			MaintenanceDate = sdf1.parse(temp);
			Calendar cal = Calendar.getInstance();
			cal.setTime(MaintenanceDate);
			if (cal.get(Calendar.YEAR) < 2000) {
				MaintenanceDate = null;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			MaintenanceDate = null;
		}
	}
	
}

/*
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class MaintenanceCheckResponse {
	
	public KumonSoapResult mResult;
	public MaintenanceCheckResponseResponseData mResponsedata;

	public MaintenanceCheckResponse() {
		mResult = new KumonSoapResult();
		mResponsedata = new MaintenanceCheckResponseResponseData();
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
    	    		
    	    		if(pi.getName().equals("MaintenanceStatus")) {
    	    			mResponsedata.setMaintenanceStatus(responsedataSoap.getProperty("MaintenanceStatus").toString());
    	    		}
    	    		else if(pi.getName().equals("MaintenanceDate")) {
    	    			mResponsedata.setMaintenanceDate(responsedataSoap.getProperty("MaintenanceDate").toString());
    	    		}
    	    	}
    			
    		}
    	}        	
		
		return bret;
	}
}

class MaintenanceCheckResponseResponseData {
	public int 			mMaintenanceStatus;
	public Date  		mMaintenanceDate;
    
	public MaintenanceCheckResponseResponseData() {
		mMaintenanceStatus = 0;
		mMaintenanceDate = null;
	}
	public void setMaintenanceStatus(String maintenanceStatus) {
		mMaintenanceStatus = Integer.parseInt(maintenanceStatus); 
	}
	public void setMaintenanceDate(String maintenanceDate) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN);
		try {
			String temp = maintenanceDate.replace('T', ' ');
			mMaintenanceDate = sdf1.parse(temp);
			Calendar cal = Calendar.getInstance();
			cal.setTime(mMaintenanceDate);
			if (cal.get(Calendar.YEAR) < 2000) {
				mMaintenanceDate = null;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			mMaintenanceDate = null;
		}
	}
	
}

*/
