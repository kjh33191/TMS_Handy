package kumon2014.markcontroltool;

import java.util.ArrayList;
import android.widget.ImageButton;
import android.widget.TextView;

import pothos.markcontroltool.MarkStuct.MdtTestMarkData;
import kumon2014.activity.R;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.DResultData;

public class StudyIndicator {
	public static final int SF_MODE_NONE = 0;
	public static final int SF_MODE_DISABLE = 1;
	public static final int SF_MODE_ENABLE = 2;
	
	
	private String mKyozaiName = "";
	
	private int mHeadPageNo = -1; 
	
	private int mLinenum = 1;
	private int mCurrentLine = 0;
	private int mStartPrintNo = 0;
	
	private int mCurrentPageNo = -1;
	private int mCurrentPageSide = -1;
	private int mMode = SF_MODE_NONE;
	
	ArrayList<IndicatorPage> mIndicatorList;
	
	private TextView mViewPage = null;
	private ImageButton mBtnBack = null;
	private ImageButton mBtnNext = null;
	private ArrayList<ImageButton> mBtnList = null;
	
	//20150126 ADD-S 2015年度Ver. 参照ページを増やす
	private int mRefPageNoFrom = -1; 
	private int mRefPageNoTo = -1;
	
	//20150126 ADD-E 2015年度Ver. 参照ページを増やす

	public StudyIndicator() 
	{
	}
	public void SetResultList(ArrayList<DResultData> resultlist) 
	{
		mLinenum = 1;
		mIndicatorList = new ArrayList<IndicatorPage>();

		
		int workStartPageNo = -1;
		int tempStartPageNo = -1;
		
		for(int i = 0; i < resultlist.size(); i++) {
			DResultData resultdata = resultlist.get(i);
			if( i == 0) {
				mStartPrintNo = resultdata.mPrintNo;
				mHeadPageNo = (int)Math.floor((resultdata.mPrintNo - 1) / 10) * 10 + 1;
				workStartPageNo = mHeadPageNo;
				//20150126 ADD-S 2015年度Ver. 参照ページを増やす
				mRefPageNoFrom = mHeadPageNo;
				mRefPageNoTo = resultdata.mPrintNo - 1;
				//20150126 ADD-E 2015年度Ver. 参照ページを増やす
			}
			
			tempStartPageNo = (int)Math.floor((resultdata.mPrintNo - 1) / 10) * 10 + 1;
			if(workStartPageNo != tempStartPageNo) {
				workStartPageNo = tempStartPageNo;
				mLinenum++;
			}
			
	        MdtTestMarkData testmark = null;
	        try
	        {
	            testmark = MdtTestMarkData.LoadFromJson(resultdata.mGradingResultData);
	        }
	        catch(Exception e)
	        {
	            testmark = null;
	        }

			for(int j = 0; j < 2; j++) {
				IndicatorPage indicatorPage = new IndicatorPage(resultdata.mPrintNo, j);
				//20150202 MOD-S For Bug リスタート時に不正
				//indicatorPage.SetStatus(resultdata.mCount, testmark);
				indicatorPage.SetStatus(resultdata.mOrgCount, testmark);
				//20150202 MOD-E For Bug
				mIndicatorList.add(indicatorPage);
			}
		}
		//20150126 ADD-S 2015年度Ver. 参照ページを増やす
		if( mRefPageNoFrom > mRefPageNoTo) {
			mRefPageNoFrom = - 1;
			mRefPageNoTo = - 1;
			//参照ページ無し
		}
		else {
			for(int i = mRefPageNoFrom; i <= mRefPageNoTo; i++) {
				for(int j = 0; j < 2; j++) {
					IndicatorPage indicatorPage = new IndicatorPage(i, j);
					indicatorPage.SetStatus(-1, null);
					mIndicatorList.add(indicatorPage);
				}
			}
		}
		//20150126 ADD-E 2015年度Ver. 参照ページを増やす
	}
	public void SetButtonCtrl(String kyozaiName, TextView viewpage, ImageButton btnback, ImageButton btnnext, ArrayList<ImageButton> btnlst) {
		mKyozaiName = kyozaiName;
		mViewPage = viewpage;
		mBtnBack = btnback;
		mBtnNext = btnnext;
		mBtnList = btnlst;
	}
	public void Init(int mode) {
		mMode = mode;
		mCurrentLine = 0;

		if(mMode == SF_MODE_NONE) {
			return;
		}
		if(mMode == SF_MODE_DISABLE) {
			mViewPage.setText("-1");
			mBtnBack.setImageResource(R.drawable.inbtn_back_off);
			mBtnBack.setEnabled(false);
			mBtnNext.setImageResource(R.drawable.inbtn_next_off);
			mBtnNext.setEnabled(false);
			
			for(int i = 0; i < mBtnList.size(); i++) {
				ImageButton btn = mBtnList.get(i);
				btn.setImageResource(R.drawable.inbtn_pw_off);
				btn.setEnabled(false);
			}
		}
	}
	//20150126 ADD-S 2015年度Ver. 参照ページを増やす
	public int GetRefPageNoFrom() {
		return mRefPageNoFrom;
	}
	public int GetRefPageNoTo() {
		return mRefPageNoTo;
	}
	public boolean IsRefPage(int page)
	{
		if(mRefPageNoFrom <= page && mRefPageNoTo >= page) {
			return true;
		}
		return false;
	}
	
	//20150126 ADD-E 2015年度Ver. 参照ページを増やす
	public void SetCurrent(int printno, int pageside) {

		if(mMode == SF_MODE_ENABLE) {
			mCurrentPageNo = printno;
			mCurrentPageSide = pageside;
			for(int i = 0; i < mLinenum; i++) {
				int statPageNo = mHeadPageNo + (i * 10);
				if(statPageNo <= printno && statPageNo + 10 >  printno) {
					mCurrentLine = i;
					break;
				}
			}
			MoveLine(mCurrentLine);
		}
	}
	public void MovePageSide(int pageside) {
		mCurrentPageSide = pageside;
		if(mMode == SF_MODE_ENABLE) {
			MoveLine(mCurrentLine);
		}
	}
	public int getPageIndex(int pos) {
		return ((pos / 2) + mCurrentLine * 10) + mHeadPageNo - mStartPrintNo;
	}
	//20150126 ADD-S 2015年度Ver. 参照ページを増やす
	public int getRefPageIndex(int pos) {
		return (int) (Math.floor(pos / 2));
	}
	//20150126 ADD-E 2015年度Ver. 参照ページを増やす
	public int getSideIndex(int pos) {
		return (pos % 2);
	}
	
	private void MoveLine(int lineno) {

		mCurrentLine = lineno;
		int statPageNo = mHeadPageNo + (mCurrentLine * 10);
				
		mViewPage.setText(mKyozaiName + Integer.toString(statPageNo));
		
		if(mCurrentLine > 0) {
			mBtnBack.setImageResource(R.drawable.inbtn_back);
			mBtnBack.setEnabled(true);
		}
		else {
			mBtnBack.setImageResource(R.drawable.inbtn_back_off);
			mBtnBack.setEnabled(false);
		}
		
		if(mLinenum > mCurrentLine + 1) {
			mBtnNext.setImageResource(R.drawable.inbtn_next);
			mBtnNext.setEnabled(true);
		}
		else {
			mBtnNext.setImageResource(R.drawable.inbtn_next_off);
			mBtnNext.setEnabled(false);
		}
			
		for(int i = 0; i < mBtnList.size(); i++) {
			ImageButton btn = mBtnList.get(i);
			IndicatorPage indicator = getIndicatorPage(statPageNo + (i / 2), (i % 2));
			if(indicator == null) {
				btn.setImageResource(R.drawable.inbtn_pw_off);
				btn.setEnabled(false);
				//20150126 DEL-S 2015年度Ver. 参照ページを増やす
				/***
				if(i == 0) {
					//一番左はいつも押せる
					btn.setImageResource(R.drawable.inbtn_pw_ref);
					btn.setEnabled(true);
				}
				***/
				//20150126 DEL-E 2015年度Ver. 参照ページを増やす
			}
			else {
				boolean isCurrent = false;
				if(indicator.mPrintNo == mCurrentPageNo && indicator.mPageSideNo == mCurrentPageSide) {
					isCurrent = true;
				}
				indicator.SetBtnImage(btn, isCurrent);
			}
		}
		
	}
	
	public void DoOnClickInBack() {
		try {
			if(mCurrentLine > 0) {
				MoveLine(mCurrentLine-1);
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void DoOnClickInNext() {
		try {
			if(mCurrentLine + 1 < mLinenum ) {
				MoveLine(mCurrentLine+1);
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		try {
			super.finalize();
	    } finally {
	    	destruction();
	    }
	}
	private void destruction() {
		if(mIndicatorList != null) {
//			for(int i = 0; i < mIndicatorList.size(); i++ ) {
//				IndicatorPage indicator = mIndicatorList.get(i);
//				indicator = null;
//			}
			mIndicatorList.clear();
			mIndicatorList = null;
		}
		if(mBtnList != null) {
//			for(int i = 0; i < mBtnList.size(); i++ ) {
//				ImageButton btn = mBtnList.get(i);
//				btn = null;
//			}
			mBtnList.clear();
			mBtnList = null;
		}
	}
	private IndicatorPage getIndicatorPage(int printno, int pageside) {
		for(int i = 0; i < mIndicatorList.size(); i++) {
			IndicatorPage indicator = mIndicatorList.get(i);
			if(indicator.mPrintNo == printno && indicator.mPageSideNo == pageside) {
				return indicator;
			}
		}
		return null;
	}
	public int getIndicatorStartPage() {
		int pos = 0;
		
		if(mCurrentLine == 0) {
			pos = ((mStartPrintNo - 1) % 10) * 2;
		}
		else {
			pos = 0;
		}
		return pos;
	}
	
}
