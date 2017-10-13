package kumon2014.markcontroltool;

import android.widget.ImageButton;

import pothos.markcontroltool.MarkStuct.MdtPageMarkData;
import pothos.markcontroltool.MarkStuct.MdtTestMarkData;
import kumon2014.activity.R;

public class IndicatorPage {

	private static final int SF_STATUS_NONE = -1; 
	private static final int SF_STATUS_NEW = 0; 
	private static final int SF_STATUS_OK = 1; 
	private static final int SF_STATUS_NG = 2; 
	//20150126 ADD-S 2015年度Ver. 参照ページを増やす
	private static final int SF_STATUS_REF = 3; 
	//20150126 ADD-E 2015年度Ver. 参照ページを増やす

	public int 	mPrintNo = -1;
	public int 	mPageSideNo = -1;
	public int mStatus = SF_STATUS_NONE;
	
	public IndicatorPage(int printno, int pageside) 
	{
		mPrintNo = printno;
		mPageSideNo = pageside;
		mStatus = SF_STATUS_NONE;
	}
	public void SetStatus(int trial, MdtTestMarkData testmark) 
	{
		//20150126 MOD-S 2015年度Ver. 参照ページを増やす
		/***
		mStatus = SF_STATUS_NEW;
		if(testmark != null) {
			if(testmark.mPageMarks.size() >= 2) {
				mStatus = getPageStatus(trial-1, testmark, mPageSideNo);
			}
        }
        **/
		mStatus = SF_STATUS_NONE;
		if(trial >= 0) {
			if(testmark != null) {
				if(testmark.mPageMarks.size() >= 2) {
					mStatus = getPageStatus(trial-1, testmark, mPageSideNo);
				}
	        }
			else {
				mStatus = SF_STATUS_NEW;
			}
		}
		else {
			mStatus = SF_STATUS_REF;
		}
		//20150126 MOD-E 2015年度Ver. 参照ページを増やす
	}
	public void SetBtnImage(ImageButton btn, boolean current)
	{
		if(current) {
			//20150126 MOD-S 2015年度Ver. 参照ページを増やす
			//btn.setImageResource(R.drawable.inbtn_pw_cur);
			//btn.setEnabled(true);
			if(mStatus == SF_STATUS_REF) {
				btn.setImageResource(R.drawable.inbtn_pw_ref_cur);
				btn.setEnabled(true);
			}
			else {
				btn.setImageResource(R.drawable.inbtn_pw_cur);
				btn.setEnabled(true);
			}
			//20150126 MOD-E 2015年度Ver. 参照ページを増やす
		}
		else {
			switch(mStatus) {
				case(SF_STATUS_NEW):
					btn.setImageResource(R.drawable.inbtn_pw_new);
					btn.setEnabled(true);
					break;
				case(SF_STATUS_OK):
					btn.setImageResource(R.drawable.inbtn_pw_ok);
					btn.setEnabled(true);
					break;
				case(SF_STATUS_NG):
					btn.setImageResource(R.drawable.inbtn_pw_ng);
					btn.setEnabled(true);
					break;
				//20150126 ADD-S 2015年度Ver. 参照ページを増やす
				case(SF_STATUS_REF):
					btn.setImageResource(R.drawable.inbtn_pw_ref);
					btn.setEnabled(true);
					break;
				//20150126 ADD-E 2015年度Ver. 参照ページを増やす
			}
		}
		
	}
	//************************************************************************************************
	private int getPageStatus(int trial, MdtTestMarkData testmark, int pagesize) {
        MdtPageMarkData pmark = testmark.GetPageMarkData(pagesize);
        if (pmark == null) {
        	return SF_STATUS_NEW;
        }
        
        if(trial < 0) {
        	return SF_STATUS_NEW;
        }
        
        int pageScore = pmark.GetPageScore(trial);
        boolean right = (pageScore == pmark.getMdtAllScore()) ? true : false;
        if (right)
        {
        	return SF_STATUS_OK;
        }
    	return SF_STATUS_NG;
	}
}
