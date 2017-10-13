package kumon2014.webservice;

public class ResultDataRequest extends BaseSoapRequest {
	@Annotation.DataMember
	public String LoginID;
	@Annotation.DataMember
	public String Password;
	@Annotation.DataMember
	public String LimitDate;
	
	public ResultDataRequest() {
		super();
	}

}