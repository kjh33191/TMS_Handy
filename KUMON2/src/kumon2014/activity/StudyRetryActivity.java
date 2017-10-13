package kumon2014.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.ScreenChange;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.KumonDataCtrl;

public class StudyRetryActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	private TextView					mTextviewName;
	private TextView					mTextviewKyozai;
	//private TextView					mTextviewMessage;

	private int 						mEntrance;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_studyretry);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");
			mEntrance = (int)intent.getIntExtra(ScreenChange.SF_ENTRANCE, ScreenChange.SF_NEXT);

	        mTextviewName = (TextView) findViewById(R.id.textview_Name);
	        mTextviewKyozai = (TextView) findViewById(R.id.textview_kyozai);
	        //mTextviewMessage = (TextView) findViewById(R.id.textview_Message);

	        mTextviewName.setText(mCurrentUser.mStudentName);
	        mTextviewKyozai.setText(mCurrentUser.mCurrentKyokaKyozaiName);

//	        String tmp = "í˘ê≥Ç™Ç†ÇËÇ‹Ç∑";
//	        mTextviewMessage.setText(tmp);
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

    public void onClickStart(View view){
		System.gc();
    	try {
    		int learningmode = KumonDataCtrl.SF_DATATYPE_RETRY;
    		switch(mEntrance) {
    			case(ScreenChange.SF_NEXT):
    				learningmode = KumonDataCtrl.SF_DATATYPE_RETRY;
    				break;
    			case(ScreenChange.SF_TODAY):
    				learningmode = KumonDataCtrl.SF_DATATYPE_TODAYRETRY;
    				break;
    			case(ScreenChange.SF_HOMEWORK):
    				learningmode = KumonDataCtrl.SF_DATATYPE_HOMEWORKRETRY;
    				break;
    		}
    		ScreenChange.doScreenChangeRetry(this, mCurrentUser, learningmode);
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    public void onClickBack(View view){
		System.gc();
    	try {
    		if(mEntrance == 0) {
    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDYRETRY, ScreenChange.SCNO_LIST, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
    		}
    		else {
    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDYRETRY, ScreenChange.SCNO_ENTRANCE, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
    		}
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }

}
