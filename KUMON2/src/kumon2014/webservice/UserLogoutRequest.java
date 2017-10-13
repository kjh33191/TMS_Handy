package kumon2014.webservice;

public class UserLogoutRequest extends BaseSoapRequest {
	//20130702 MOD-S For 強制ログアウト
	//String SessionID;
	@Annotation.DataMember
	public String LoginID; 
	//20130702 MOD-E For 強制ログアウト

	public UserLogoutRequest() {
		super();
	}

}