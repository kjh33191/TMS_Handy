package kumon2014.kumondata;

import java.util.ArrayList;
//New Version MOD-S
//import info.guardianproject.database.sqlcipher.SQLiteDatabase;
//New Version MOD-E

public class WDownloadPrintSetIDList {
	
	public int 		mSoapStatus = 0;;
	public String 	mSoapError = "";
	
	public ArrayList<DResultData>			mDownLoadResultDataList = null;

	public WDownloadPrintSetIDList()
	{
		mSoapStatus = 0;
		mSoapError = "";
		
		mDownLoadResultDataList = new ArrayList<DResultData>();
	}
	public void Clear()
	{
		if(mDownLoadResultDataList != null) {
			for(int i= 0; i < mDownLoadResultDataList.size(); i++)
			{
				DResultData resultdata = mDownLoadResultDataList.get(i);
				resultdata.ClearAll();
				resultdata = null;
			}
			mDownLoadResultDataList.clear();
			mDownLoadResultDataList = null;
		}
	}
}
