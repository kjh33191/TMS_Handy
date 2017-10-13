package kumon2014.webservice;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CollectAndroidLogRequest extends BaseSoapRequest {
	@Annotation.DataMember
	public String 					LoginID;
	@Annotation.DataMember
	public String 					StudentAdminID;
	@Annotation.DataMember
	public AndroidLogList			LogList;	

	public CollectAndroidLogRequest() {
		super();
	}
}
class AndroidLogList extends Vector<AndroidLog> implements KvmSerializable{
	private static final long serialVersionUID = 1L;
	
	public Object getProperty(int arg0) {
		return this.get(arg0);	
	}

	public int getPropertyCount() {
		 return this.size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0)
	    {
		    case 0:
		    	arg2.type = new AndroidLog().getClass();
		    	arg2.name = "AndroidLog";
		        break;        
		    default:
		        break;
	    }
	}
	
	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((AndroidLog) arg1);		
	}
}


class AndroidLog extends BaseSoapRequest{
	@Annotation.DataMember
	public String 	Msg; 
	
	public AndroidLog() {
		super();
	}
}
