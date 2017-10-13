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

    public boolean						mDone = false;			//�w�K�ς�
    public boolean						mNext = false;			//�w�K�\��(�ʏ�)
    public boolean						mRetry = false;			//�w�K�\��(����)
    public boolean						mToday = false;			//�w�K�\��(������)
    public boolean						mTodayRetry = false;	//�w�K�\��(����������)
	//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
    //public boolean						mHomeWork = false;		//�w�K�\��(�h�蕪)
    //public boolean						mHomeWorkRetry = false;	//�w�K�\��(�h�蕪����)

    public boolean						mPast = false;			//�w�K�\��(�ߋ���)
    public boolean						mPastRetry = false;		//�w�K�\��(�ߋ�������)
    public boolean						mFuture = false;		//�w�K�\��(������)
    public boolean						mFutureRetry = false;	//�w�K�\��(����������)
    
	//20140917 MOD-E For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�

	//20150216 ADD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
    public boolean						mWait = false;	//(�̓_�҂�,�����M)
   	//20150216 ADD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
    
    public int							mPrintType = -1;		//�|�P�F���ݒ�G0�F�ʏ�;1=�ŏI,2=�f�f
      
	//20150110 ADD-S For 2015�N�xVer. �O���[�h�̍������ނ�I��������A�x��
    public int							mKyozaiOderNo;
	//20150110 ADD-E For 2015�N�xVer. �O���[�h�̍������ނ�I��������A�x��
    
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

		mDone = false;			//�w�K�ς�
	    mNext = false;			//�w�K�\��(�ʏ�)
	    mRetry = false;
	    mToday = false;			//�w�K�\��(������)
	    mTodayRetry = false;	//�w�K�\��(������)
		//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
	    //mHomeWork = false;		//�w�K�\��(�h�蕪)
	    //mHomeWorkRetry = false;	//�w�K�\��(�h�蕪)
	    
	    mPast = false;			//�w�K�\��(�ߋ���)
	    mPastRetry = false;		//�w�K�\��(�ߋ�������)
	    mFuture = false;		//�w�K�\��(������)
	    mFutureRetry = false;	//�w�K�\��(����������)
		//20140917 MOD-E For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
	    mPrintType = -1;
	    
		//20150110 ADD-S For 2015�N�xVer. �O���[�h�̍������ނ�I��������A�x��
	    mKyozaiOderNo = 0;
		//20150110 ADD-E For 2015�N�xVer. �O���[�h�̍������ނ�I��������A�x��
	    
		//20150216 ADD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
	    mWait = false;	//(�̓_�҂�,�����M)
	   	//20150216 ADD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
	}
	public void DataStatusCheck()
	{
		mDone = false;			//�w�K�ς�
	    mNext = false;			//�w�K�\��(�ʏ�)
	    mRetry = false;
	    mToday = false;			//�w�K�\��(������)
	    mTodayRetry = false;	//�w�K�\��(������)
		//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
	    //mHomeWork = false;		//�w�K�\��(�h�蕪)
	    //mHomeWorkRetry = false;	//�w�K�\��(�h�蕪)
	    
	    mPast = false;			//�w�K�\��(�ߋ���)
	    mPastRetry = false;		//�w�K�\��(�ߋ�������)
	    mFuture = false;		//�w�K�\��(������)
	    mFutureRetry = false;	//�w�K�\��(����������)
		//20140917 MOD-E For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
		//20150216 ADD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
	    mWait = false;	//(�̓_�҂�,�����M)
	   	//20150216 ADD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
	    
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
			
            //�w�K�\������قȂ�ꍇ�́A����PrintSet�ɂ͏o���Ȃ� ���́@�v�����g�Z�b�g���Ⴄ
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
			//20150216 ADD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
			case(KumonDataCtrl.SF_DATATYPE_WAIT):
				if(printset.mPrintType == KumonDataCtrl.SF_PRINTTYPE_NORMAL) {
					mWait = true;
					mDone = true;
				}
				break;
		   	//20150216 ADD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
				
			case(KumonDataCtrl.SF_DATATYPE_NEXT):
				mNext = true;
				//20140915 MOD-S For �w�K�\�肪�������͏h��w�K
				/***
				//20140822 ADD-S For �w�K�\�肪�������ł������Ŋw�K�\
				mToday = true;
				//20140822 ADD-E For �w�K�\�肪�������ł������Ŋw�K�\
				***/
				//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
				//mHomeWork = true;
				mFuture = true;
				//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
				
				//20140915 MOD-E For �w�K�\�肪�������͏h��w�K
			
				if(printset.mPrintSetID.equals(KumonDataCtrl.SF_GUID_NULL) == false) {
					mRetry = true;
					//20140915 MOD-S For �w�K�\�肪�������͏h��w�K
					/***
					//20140822 ADD-S For �w�K�\�肪�������ł������Ŋw�K�\
					mTodayRetry = true;
					//20140822 ADD-E For �w�K�\�肪�������ł������Ŋw�K�\
					***/
					//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
					//mHomeWorkRetry = true;
					mFutureRetry = true;
					//20140917 MOD-E For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
					//20140915 MOD-E For �w�K�\�肪�������͏h��w�K
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
				//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
				//mHomeWork = true;
				mPast = true;
				//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
				mNext = true;
				if(printset.mPrintSetID.equals(KumonDataCtrl.SF_GUID_NULL) == false) {
					//20140917 MOD-S For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
					//mHomeWorkRetry = true;
					mPastRetry = true;
					//20140917 MOD-E For �w�K�\�肪�������͏h��w�K�����ǁA�ԃ{�^���ł͂Ȃ�
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
				//20150216 ADD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
				case(KumonDataCtrl.SF_DATATYPE_WAIT):
					if(dataType == KumonDataCtrl.SF_DATATYPE_NONE || dataType == KumonDataCtrl.SF_DATATYPE_DONE) { 
						dataType = tmpdataType;
					}
					break;
			   	//20150216 ADD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
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
			//20150216 MOD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
			//dataType = KumonDataCtrl.SF_DATATYPE_DONE;
			dataType = KumonDataCtrl.SF_DATATYPE_WAIT;
			//20150216 MOD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
		}
		else if (result.mIsRegist == 1)
		{
			//�����M
			//20150216 MOD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
			//dataType = KumonDataCtrl.SF_DATATYPE_DONE;
			dataType = KumonDataCtrl.SF_DATATYPE_WAIT;
			//20150216 MOD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
		}
		else if (result.mScore >= 100)
		{
			//���_
			dataType = KumonDataCtrl.SF_DATATYPE_DONE;
		}
		else if (result.mCount >= result.mLimitCount)
		{
			//�����w�K�ς�
			dataType = KumonDataCtrl.SF_DATATYPE_DONE;
		}
		else if(result.mStatus == KumonDataCtrl.SF_STATUS_GRADEREADY) {
    		//�̓_�҂�
			//20150216 MOD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
			//dataType = KumonDataCtrl.SF_DATATYPE_DONE;
			dataType = KumonDataCtrl.SF_DATATYPE_WAIT;
			//20150216 MOD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
    	}
		else if(result.mStatus == KumonDataCtrl.SF_STATUS_GRADING) {
    		//�̓_��
			//20150216 MOD-S For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
			//dataType = KumonDataCtrl.SF_DATATYPE_DONE;
			dataType = KumonDataCtrl.SF_DATATYPE_WAIT;
			//20150216 MOD-E For �̓_�҂��ł��A�Œ�O���[�h�́A�Œ�O���[�h(�̓_�҂����ǂ���)
    	}
		else if(result.mStatus == KumonDataCtrl.SF_STATUS_END) {
    		//����
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
