package kumon2014.kumondata;

import java.util.ArrayList;

import kumon2014.database.master.MQuestion2;

public class WQuestionDataList {
	public int 		mSoapStatus = 0;;
	public String 	mSoapError = "";

	public ArrayList<MQuestion2>			mMQuestionDataList = null;

	public WQuestionDataList()
	{
		mSoapStatus = 0;
		mSoapError = "";
		
		mMQuestionDataList = new ArrayList<MQuestion2>();
	}
	
	@SuppressWarnings("unchecked")
	public WQuestionDataList clone()
	{
		WQuestionDataList questiondatalist = new WQuestionDataList();
		
		questiondatalist.mSoapStatus = this.mSoapStatus;
		questiondatalist.mSoapError = this.mSoapError;

		questiondatalist.mMQuestionDataList = (ArrayList<MQuestion2>) this.mMQuestionDataList.clone();

		return questiondatalist;
	}
	
}
