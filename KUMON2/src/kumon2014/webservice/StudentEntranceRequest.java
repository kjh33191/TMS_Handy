package kumon2014.webservice;

public class StudentEntranceRequest extends BaseSoapRequest {
	@Annotation.DataMember
	public String StudentAdminID;
	
	public StudentEntranceRequest() {
		super();
	}
}