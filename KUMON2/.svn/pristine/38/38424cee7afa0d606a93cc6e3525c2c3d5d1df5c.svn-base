package kumon2014.webservice;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CommentUnreadFlgUpdateRequest extends BaseSoapRequest {
	@Annotation.DataMember
	public String 					SessionID; 
	@Annotation.DataMember
	public String 					StudentAdminID; 
	@Annotation.DataMember
	public UnreadFlgInfoList		UnreadFlgList;
	
	public CommentUnreadFlgUpdateRequest() {
		super();
	}	

}

class UnreadFlgInfoList extends Vector<UnreadFlgInfo> implements KvmSerializable{
	private static final long serialVersionUID = 1L;
	
	@Override
	public Object getProperty(int arg0) {
		return this.get(arg0);	
	}

	@Override
	public int getPropertyCount() {
		 return this.size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0)
	    {
		    case 0:
		    	arg2.type = new UnreadFlgInfo().getClass();
		    	arg2.name = "UnreadFlgInfo";
		        break;        
		    default:
		        break;
	    }
	}
	
	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((UnreadFlgInfo) arg1);		
	}
}


class UnreadFlgInfo extends BaseSoapRequest {
	@Annotation.DataMember
	public String 	PrintUnitID;
	@Annotation.DataMember
	public int 	CommentUnreadFlg; 
	
	public UnreadFlgInfo() {
		super();
	}

}
