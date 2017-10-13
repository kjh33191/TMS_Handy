package kumon2014.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.ScreenChange;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.DKyozaiPrintSet;
import kumon2014.kumondata.KumonDataCtrl;

public class StudyEntranceActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	private ArrayList<DKyozaiPrintSet>	mkyozaiPrintSetList = null;

	private TextView					mTextviewName;
	private TextView					mTextviewKyozai;
	private TextView					mTextviewMessage;
	private ImageButton					mImageButtonToday;
	private ImageButton					mImageButtonHome;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_studyentrance);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

			mkyozaiPrintSetList = KumonDataCtrl.GetKyozaiDataExistList(mCurrentUser.mStudentID);

	        mTextviewName = (TextView) findViewById(R.id.textview_Name);
	        mTextviewKyozai = (TextView) findViewById(R.id.textview_kyozai);
	        mTextviewMessage = (TextView) findViewById(R.id.textview_Message);
	        mImageButtonToday = (ImageButton) findViewById(R.id.imagebutton_today);
	        mImageButtonHome = (ImageButton) findViewById(R.id.imagebutton_home);

	        mTextviewName.setText(mCurrentUser.mStudentName);
	        mTextviewKyozai.setText(mCurrentUser.mCurrentKyokaKyozaiName);

	        String tmp = "";
	        mTextviewMessage.setText(tmp);

    		if(IsTodayDataExist(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
    	        mImageButtonToday.setEnabled(true);
    	        mImageButtonToday.setImageResource(R.drawable.btn_today);
       		}
       		else {
       			mImageButtonToday.setEnabled(false);
       			mImageButtonToday.setImageResource(R.drawable.btn_today_off);
    		}

    		if(IsHomeDataExist(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
    			mImageButtonHome.setEnabled(true);
    			mImageButtonHome.setImageResource(R.drawable.btn_home);
       		}
       		else {
       			mImageButtonHome.setEnabled(false);
       			mImageButtonHome.setImageResource(R.drawable.btn_home_off);
    		}

        }
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
		System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Utility.cleanupView(findViewById(R.id.studyretry_topview));
    	System.gc();
    }
	@Override
	public void onLowMemory() {
		Utility.onLowMemory(getApplicationContext());
	}

    public void onClickToday(View view){
		System.gc();
    	try {
			if(IsNormalPrint(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
	    		if(IsTodayRetryDataExist(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
	    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_LIST, ScreenChange.SCNO_STUDYRETRY, true, mCurrentUser, 0, ScreenChange.SF_TODAY);
	    		}
	    		else {
	    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_LIST, ScreenChange.SCNO_STUDYSTART, true, mCurrentUser, 0, ScreenChange.SF_TODAY);
	    		}
			}
			else {
				if(IsTodayRetryDataExist(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
	    			ScreenChange.doScreenChangeSpecialStart(this, ScreenChange.SCNO_LIST, mCurrentUser, KumonDataCtrl.SF_DATATYPE_TODAYRETRY, ScreenChange.SF_TODAY);
	    		}
	    		else {
	    			ScreenChange.doScreenChangeSpecialStart(this, ScreenChange.SCNO_LIST, mCurrentUser, KumonDataCtrl.SF_DATATYPE_TODAY, ScreenChange.SF_TODAY);
	    		}
			}

    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    public void onClickHome(View view){
		System.gc();
		if(IsNormalPrint(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
			if(IsHomeRetryDataExist(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
				ScreenChange.doScreenChange(this, ScreenChange.SCNO_LIST, ScreenChange.SCNO_STUDYRETRY, true, mCurrentUser, 0, ScreenChange.SF_HOMEWORK);
			}
			else {
				ScreenChange.doScreenChange(this, ScreenChange.SCNO_LIST, ScreenChange.SCNO_STUDYSTART, true, mCurrentUser, 0, ScreenChange.SF_HOMEWORK);
			}
		}
		else {
			if(IsTodayRetryDataExist(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
    			ScreenChange.doScreenChangeSpecialStart(this, ScreenChange.SCNO_LIST, mCurrentUser, KumonDataCtrl.SF_DATATYPE_HOMEWORKRETRY, ScreenChange.SF_HOMEWORK);
    		}
    		else {
    			ScreenChange.doScreenChangeSpecialStart(this, ScreenChange.SCNO_LIST, mCurrentUser, KumonDataCtrl.SF_DATATYPE_HOMEWORK, ScreenChange.SF_HOMEWORK);
    		}
		}

    }
    public void onClickBack(View view){
		System.gc();
    	try {
    		ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDYRETRY, ScreenChange.SCNO_LIST, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }

	private boolean IsTodayDataExist(String kyokaID, String kyozaiID)
	{
		boolean exist = false;
		for(int i = 0; i < mkyozaiPrintSetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(i);
			if(kyozaiprintset.mKyokaID.equalsIgnoreCase(kyokaID) && kyozaiprintset.mKyozaiID.equalsIgnoreCase(kyozaiID)) {
				exist = kyozaiprintset.mToday;
				break;
			}
		}
		return exist;
	}
	private boolean IsHomeDataExist(String kyokaID, String kyozaiID)
	{
		boolean exist = false;
		for(int i = 0; i < mkyozaiPrintSetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(i);
			if(kyozaiprintset.mKyokaID.equalsIgnoreCase(kyokaID) && kyozaiprintset.mKyozaiID.equalsIgnoreCase(kyozaiID)) {
				//20140917 MOD-S For ŠwK—\’è‚ª–¢—ˆ“ú‚Íh‘èŠwK‚¾‚¯‚ÇAÔƒ{ƒ^ƒ“‚Å‚Í‚È‚¢
				//exist = kyozaiprintset.mHomeWork;
				exist = kyozaiprintset.mPast || kyozaiprintset.mFuture;
				//20140917 MOD-E For ŠwK—\’è‚ª–¢—ˆ“ú‚Íh‘èŠwK‚¾‚¯‚ÇAÔƒ{ƒ^ƒ“‚Å‚Í‚È‚¢
				break;
			}
		}
		return exist;
	}
	private boolean IsTodayRetryDataExist(String kyokaID, String kyozaiID)
	{
		boolean exist = false;
		for(int i = 0; i < mkyozaiPrintSetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(i);
			if(kyozaiprintset.mKyokaID.equalsIgnoreCase(kyokaID) && kyozaiprintset.mKyozaiID.equalsIgnoreCase(kyozaiID)) {
				exist = kyozaiprintset.mTodayRetry;
				break;
			}
		}
		return exist;
	}
	private boolean IsNormalPrint(String kyokaID, String kyozaiID)
	{
		boolean normal = true;
		for(int i = 0; i < mkyozaiPrintSetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(i);
			if(kyozaiprintset.mKyokaID.equalsIgnoreCase(kyokaID) && kyozaiprintset.mKyozaiID.equalsIgnoreCase(kyozaiID)) {
				if(kyozaiprintset.mPrintType > 0) {
					normal = false;
					break;
				}
			}
		}
		return normal;
	}

	private boolean IsHomeRetryDataExist(String kyokaID, String kyozaiID)
	{
		boolean exist = false;
		for(int i = 0; i < mkyozaiPrintSetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(i);
			if(kyozaiprintset.mKyokaID.equalsIgnoreCase(kyokaID) && kyozaiprintset.mKyozaiID.equalsIgnoreCase(kyozaiID)) {
				//20140917 MOD-S For ŠwK—\’è‚ª–¢—ˆ“ú‚Íh‘èŠwK‚¾‚¯‚ÇAÔƒ{ƒ^ƒ“‚Å‚Í‚È‚¢
				//exist = kyozaiprintset.mHomeWorkRetry;
				exist = kyozaiprintset.mPastRetry || kyozaiprintset.mFutureRetry;
				//20140917 MOD-E For ŠwK—\’è‚ª–¢—ˆ“ú‚Íh‘èŠwK‚¾‚¯‚ÇAÔƒ{ƒ^ƒ“‚Å‚Í‚È‚¢
				break;
			}
		}
		return exist;
	}


}
