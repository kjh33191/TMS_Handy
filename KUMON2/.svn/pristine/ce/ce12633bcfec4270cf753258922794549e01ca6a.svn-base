package kumon2014.kumondata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DKyozaiPrintSet {
	//Key
	public String						mKyokaID = "";
	public String						mKyokaName = "";
	public String						mKyozaiID = "";
	public String						mKyozaiName = "";
	public String						mKyokaKyozaiName = "";
	
	public boolean						mLoaded = false;
	
    public ArrayList<DResultData>		mResultList;

    public boolean						mDone = false;			//学習済み
    public boolean						mNext = false;			//学習予定(通常)
    public boolean						mRetry = false;			//学習予定(訂正)
    public boolean						mToday = false;			//学習予定(当日分)
    public boolean						mTodayRetry = false;	//学習予定(当日分訂正)
	//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
    //public boolean						mHomeWork = false;		//学習予定(宿題分)
    //public boolean						mHomeWorkRetry = false;	//学習予定(宿題分訂正)

    public boolean						mPast = false;			//学習予定(過去日)
    public boolean						mPastRetry = false;		//学習予定(過去日訂正)
    public boolean						mFuture = false;		//学習予定(未来日)
    public boolean						mFutureRetry = false;	//学習予定(未来日訂正)
    
	//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない

	//20150216 ADD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
    public boolean						mWait = false;	//(採点待ち,未送信)
   	//20150216 ADD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
    
    public int							mPrintType = -1;		//－１：未設定；0：通常;1=最終,2=診断
      
	//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
    public int							mKyozaiOderNo;
	//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告
    
	public DKyozaiPrintSet(String kyoka, String kyokaName, String kyozai, String kyozaiName) {
		mKyokaID = kyoka;
		mKyokaName = kyokaName;
		mKyozaiID = kyozai;
		mKyozaiName = kyozaiName;
		mKyokaKyozaiName = mKyokaName + " " + mKyozaiName;
		mPrintType = -1;
		ClearAll();
	}
	
	public void ClearAll()
	{
		mLoaded = false;
		
		if(mResultList != null) {
			for(int i = 0; i < mResultList.size(); i++)
			{
				DResultData result = mResultList.get(i);
				result.ClearAll();
				result = null;
			}
			mResultList.clear();
			mResultList = null;
		}
		mResultList = new ArrayList<DResultData>();

		mDone = false;			//学習済み
	    mNext = false;			//学習予定(通常)
	    mRetry = false;
	    mToday = false;			//学習予定(当日分)
	    mTodayRetry = false;	//学習予定(当日分)
		//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
	    //mHomeWork = false;		//学習予定(宿題分)
	    //mHomeWorkRetry = false;	//学習予定(宿題分)
	    
	    mPast = false;			//学習予定(過去日)
	    mPastRetry = false;		//学習予定(過去日訂正)
	    mFuture = false;		//学習予定(未来日)
	    mFutureRetry = false;	//学習予定(未来日訂正)
		//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
	    mPrintType = -1;
	    
		//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
	    mKyozaiOderNo = 0;
		//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告
	    
		//20150216 ADD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
	    mWait = false;	//(採点待ち,未送信)
	   	//20150216 ADD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
	}
	public void DataStatusCheck()
	{
		mDone = false;			//学習済み
	    mNext = false;			//学習予定(通常)
	    mRetry = false;
	    mToday = false;			//学習予定(当日分)
	    mTodayRetry = false;	//学習予定(当日分)
		//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
	    //mHomeWork = false;		//学習予定(宿題分)
	    //mHomeWorkRetry = false;	//学習予定(宿題分)
	    
	    mPast = false;			//学習予定(過去日)
	    mPastRetry = false;		//学習予定(過去日訂正)
	    mFuture = false;		//学習予定(未来日)
	    mFutureRetry = false;	//学習予定(未来日訂正)
		//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
		//20150216 ADD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
	    mWait = false;	//(採点待ち,未送信)
	   	//20150216 ADD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
	    
	    mPrintType = -1;
		
		DPrintSet printset = null;
		DResultData result = null;
		String oldprintsetid = "";
		String oldscheduleddate = "";
		int oldprintno = -1;
		int oldprinttype = -1;
		for(int i =0; i < mResultList.size(); i++) 
		{
			result = mResultList.get(i);
			if(oldprintsetid.isEmpty()) {
				oldprintsetid = result.mPrintSetID;
				oldscheduleddate = result.mScheduledDate;
				oldprintno = result.mPrintNo - 1;
				oldprinttype = result.mPrintType;
				printset = new DPrintSet();
				printset.mPrintSetID = result.mPrintSetID;
				printset.mPrintType = result.mPrintType;
			}
			
            //学習予定日が異なる場合は、同一PrintSetには出来ない 又は　プリントセットが違う
            if ((oldscheduleddate.equals(result.mScheduledDate) == false) || (oldprintsetid.equals(result.mPrintSetID) == false)
            		|| (oldprintno + 1 != result.mPrintNo) ||(oldprinttype != result.mPrintType)) {
            	
            	setPrintSetList(printset);

				oldprintsetid = result.mPrintSetID;
				oldscheduleddate = result.mScheduledDate;
				oldprintno = result.mPrintNo;
				oldprinttype = result.mPrintType;
            	
				printset.ClearAll();
				printset = null;
				printset = new DPrintSet();
				printset.mPrintSetID = result.mPrintSetID;
				printset.mPrintType = result.mPrintType;
            }
            printset.mResultList.add(result);
            
			oldprintno = result.mPrintNo;
			oldprinttype = result.mPrintType;
		}
		if(printset != null) {
        	setPrintSetList(printset);
		}
		
		
		if(mResultList != null) {
			for(int i = 0; i < mResultList.size(); i++)
			{
				DResultData resultx = mResultList.get(i);
				resultx.ClearAll();
				resultx = null;
			}
			mResultList.clear();
			mResultList = null;
		}
		mResultList = new ArrayList<DResultData>();
		
	}

	private void setPrintSetList(DPrintSet printset)
	{
		int dataType = GetDataTypeByPrintSet(printset);
    	
		switch(dataType) {
			case(KumonDataCtrl.SF_DATATYPE_DONE):
				if(printset.mPrintType == KumonDataCtrl.SF_PRINTTYPE_NORMAL) {
					mDone = true;
				}
				break;
			//20150216 ADD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
			case(KumonDataCtrl.SF_DATATYPE_WAIT):
				if(printset.mPrintType == KumonDataCtrl.SF_PRINTTYPE_NORMAL) {
					mWait = true;
					mDone = true;
				}
				break;
		   	//20150216 ADD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
				
			case(KumonDataCtrl.SF_DATATYPE_NEXT):
				mNext = true;
				//20140915 MOD-S For 学習予定が未来日は宿題学習
				/***
				//20140822 ADD-S For 学習予定が未来日でも教室で学習可能
				mToday = true;
				//20140822 ADD-E For 学習予定が未来日でも教室で学習可能
				***/
				//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				//mHomeWork = true;
				mFuture = true;
				//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				
				//20140915 MOD-E For 学習予定が未来日は宿題学習
			
				if(printset.mPrintSetID.equals(KumonDataCtrl.SF_GUID_NULL) == false) {
					mRetry = true;
					//20140915 MOD-S For 学習予定が未来日は宿題学習
					/***
					//20140822 ADD-S For 学習予定が未来日でも教室で学習可能
					mTodayRetry = true;
					//20140822 ADD-E For 学習予定が未来日でも教室で学習可能
					***/
					//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
					//mHomeWorkRetry = true;
					mFutureRetry = true;
					//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
					//20140915 MOD-E For 学習予定が未来日は宿題学習
				}
				if(mPrintType == -1) {
					mPrintType = printset.mPrintType ;
				}
				break;
			case(KumonDataCtrl.SF_DATATYPE_TODAY):
				mToday = true;
				mNext = true;
				if(printset.mPrintSetID.equals(KumonDataCtrl.SF_GUID_NULL) == false) {
					mTodayRetry = true;
					mRetry = true;
				}
				if(mPrintType == -1) {
					mPrintType = printset.mPrintType;
				}
				break;
			case(KumonDataCtrl.SF_DATATYPE_HOMEWORK):
				//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				//mHomeWork = true;
				mPast = true;
				//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				mNext = true;
				if(printset.mPrintSetID.equals(KumonDataCtrl.SF_GUID_NULL) == false) {
					//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
					//mHomeWorkRetry = true;
					mPastRetry = true;
					//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
					mRetry = true;
				}
				if(mPrintType == -1) {
					mPrintType = printset.mPrintType;
				}
				break;
		}
	}

	private static int GetDataTypeByPrintSet(DPrintSet printset)
	{
		int dataType = KumonDataCtrl.SF_DATATYPE_NONE;
		
		for(int i = 0; i < printset.mResultList.size(); i++) {
			
			DResultData printinfo = printset.mResultList.get(i);
			int tmpdataType = GetDataTypeByResultData(printinfo);
			
			switch(tmpdataType)
			{
				case(KumonDataCtrl.SF_DATATYPE_DONE):
					if(dataType == KumonDataCtrl.SF_DATATYPE_NONE || dataType == KumonDataCtrl.SF_DATATYPE_DONE) { 
						dataType = tmpdataType;
					}
					break;
				//20150216 ADD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
				case(KumonDataCtrl.SF_DATATYPE_WAIT):
					if(dataType == KumonDataCtrl.SF_DATATYPE_NONE || dataType == KumonDataCtrl.SF_DATATYPE_DONE) { 
						dataType = tmpdataType;
					}
					break;
			   	//20150216 ADD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
				case(KumonDataCtrl.SF_DATATYPE_NEXT):
					dataType = tmpdataType;
					break;
				case(KumonDataCtrl.SF_DATATYPE_TODAY):
					dataType = tmpdataType;
					break;
				case(KumonDataCtrl.SF_DATATYPE_HOMEWORK):
					dataType = tmpdataType;
					break;
			}
		}
		return dataType;
	}
	private static int GetDataTypeByResultData(DResultData result)
	{
		
		int dataType = KumonDataCtrl.SF_DATATYPE_DONE;
		
		
		if (result.mIsLearned == 1)
		{
			//20150216 MOD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
			//dataType = KumonDataCtrl.SF_DATATYPE_DONE;
			dataType = KumonDataCtrl.SF_DATATYPE_WAIT;
			//20150216 MOD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
		}
		else if (result.mIsRegist == 1)
		{
			//未送信
			//20150216 MOD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
			//dataType = KumonDataCtrl.SF_DATATYPE_DONE;
			dataType = KumonDataCtrl.SF_DATATYPE_WAIT;
			//20150216 MOD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
		}
		else if (result.mScore >= 100)
		{
			//満点
			dataType = KumonDataCtrl.SF_DATATYPE_DONE;
		}
		else if (result.mCount >= result.mLimitCount)
		{
			//制限学習済み
			dataType = KumonDataCtrl.SF_DATATYPE_DONE;
		}
		else if(result.mStatus == KumonDataCtrl.SF_STATUS_GRADEREADY) {
    		//採点待ち
			//20150216 MOD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
			//dataType = KumonDataCtrl.SF_DATATYPE_DONE;
			dataType = KumonDataCtrl.SF_DATATYPE_WAIT;
			//20150216 MOD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
    	}
		else if(result.mStatus == KumonDataCtrl.SF_STATUS_GRADING) {
    		//採点中
			//20150216 MOD-S For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
			//dataType = KumonDataCtrl.SF_DATATYPE_DONE;
			dataType = KumonDataCtrl.SF_DATATYPE_WAIT;
			//20150216 MOD-E For 採点待ちでも、最低グレードは、最低グレード(採点待ちかどうか)
    	}
		else if(result.mStatus == KumonDataCtrl.SF_STATUS_END) {
    		//完了
			dataType = KumonDataCtrl.SF_DATATYPE_DONE;
    	}
		else
		{
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
			String today = sdf.format(date);
			int cmp = result.mScheduledDate.compareTo(today);
			if (cmp == 0)
			{
				dataType = KumonDataCtrl.SF_DATATYPE_TODAY;
			}
			else if (cmp < 0) {
				dataType = KumonDataCtrl.SF_DATATYPE_HOMEWORK;
			}
			else
			{
				dataType = KumonDataCtrl.SF_DATATYPE_NEXT;
			}
		}
		return dataType;
	}
}
