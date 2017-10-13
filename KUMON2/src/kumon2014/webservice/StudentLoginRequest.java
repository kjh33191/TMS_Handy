package kumon2014.webservice;

public class StudentLoginRequest extends BaseSoapRequest {
	@Annotation.DataMember
	public String LoginID;
	@Annotation.DataMember
	public String Password;
	
	public StudentLoginRequest() {
		super();
	}
}