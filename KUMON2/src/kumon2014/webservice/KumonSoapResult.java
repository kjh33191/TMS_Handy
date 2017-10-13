package kumon2014.webservice;

import java.io.Serializable;

public class KumonSoapResult implements Serializable {
	private static final long serialVersionUID = 1L;
	@Annotation.DataMember
	public int 			mStatus;
	@Annotation.DataMember
	public String 		mError;

	public KumonSoapResult() {
		mStatus = 0;
		mError = "";
	}
	
	public void setStatus(String status) {
		mStatus = Integer.parseInt(status); 
	}
	public void setError(String error) {
		if(error != null) {
			mError = error; 
		}
	}
}
