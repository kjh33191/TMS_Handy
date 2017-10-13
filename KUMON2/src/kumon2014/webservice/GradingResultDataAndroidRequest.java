package kumon2014.webservice;

import kumon2014.webservice.Annotation.DataMember;

public class GradingResultDataAndroidRequest extends BaseSoapRequest {
	@DataMember
	public String LoginID; 
	@DataMember
	public String Password; 
	@DataMember
	public String KyokaID; 
	@DataMember
	public String KyozaiID; 
	@DataMember
	public String PrintSetID; 
	
	public GradingResultDataAndroidRequest() {
		super();
	}

}