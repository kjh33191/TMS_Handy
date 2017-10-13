package kumon2014.kumondata;

import java.io.Serializable;
import java.util.ArrayList;

public class DPrintSet  implements Serializable , Cloneable{
	private static final long serialVersionUID = 1L;
	
	//Key
	public String						mStudentID = "";
	public String						mKyokaID = "";
	public String						mKyozaiID = "";
	public String						mPrintSetID = "";	//プリントセットID
	public int							mPrintType = KumonDataCtrl.SF_PRINTTYPE_NORMAL;	
	
	//送信用
	public int							mPenThickness;
	
    public ArrayList<DResultData> 		mResultList; 		//プリント情報。
    
	public DPrintSet() {
		mStudentID = "";
		mKyokaID = "";
		mKyozaiID = "";
		mPrintSetID = "";
		mPrintType = KumonDataCtrl.SF_PRINTTYPE_NORMAL;	
		
		mResultList = new ArrayList<DResultData>();
	}
	
	public void ClearAll()
	{
		for(int i = 0; i < mResultList.size(); i++)
		{
			DResultData result = mResultList.get(i);
			result.ClearAll();
			result = null;
		}
		mResultList.clear();
		mResultList = null;
	}
	public DPrintSet copy() 
	{
		DPrintSet printsetinfo = new DPrintSet();
		
		printsetinfo.mStudentID = this.mStudentID;
		printsetinfo.mKyokaID = this.mKyokaID;
		printsetinfo.mKyozaiID = this.mKyozaiID;
		printsetinfo.mPrintSetID = this.mPrintSetID;
		printsetinfo.mPrintType = this.mPrintType;
		
		for(int i = 0; i < this.mResultList.size(); i++) {
			printsetinfo.mResultList.add((DResultData)mResultList.get(i).clone());
		}
		
		return printsetinfo;
	}

	
}
