package kumon2014.activity;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.ScreenChange;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.database.master.MQuestion2;
import kumon2014.kumondata.DResultData;
import kumon2014.kumondata.KumonDataCtrl;
import pothos.mdtcommon.IO;
import pothos.mdtcommon.DataStructs.MdtData;

public class StudySpecialTestStartActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	private TextView					mTextviewName;
	private TextView					mTextviewKyozai;
	private TextView					mTextviewStandard;
	private TextView					mTextviewLimit;

	private int 						mEntrance;
	private int							mLearningmode;
	private int 						mLimitTime = -1;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_studyspecialteststart);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");
			mEntrance = (int)intent.getIntExtra(ScreenChange.SF_ENTRANCE, ScreenChange.SF_NEXT);
			mLearningmode = (int)intent.getIntExtra(ScreenChange.SF_LEARNINGMODE, KumonDataCtrl.SF_DATATYPE_NEXT);

	        mTextviewName = (TextView) findViewById(R.id.textview_Name);
	        mTextviewKyozai = (TextView) findViewById(R.id.textview_kyozai);
	        mTextviewStandard = (TextView) findViewById(R.id.textview_standard);
	        mTextviewLimit = (TextView) findViewById(R.id.textview_limit);

	        mTextviewName.setText(mCurrentUser.mStudentName);

	        String kyozainame = mCurrentUser.mCurrentKyokaName ;
	        kyozainame += mCurrentUser.mCurrentKyozaiName;
	        if(mCurrentUser.mCurrentPrintType == 2) {
	        	kyozainame += " 診断テスト ";
	        }
	        else {
	        	kyozainame += " 終了テスト ";
	        }
	        mTextviewKyozai.setText(kyozainame);

	        String standardString = "";
	        String limitString = "";
	        DResultData resultsata = MQuestion2.DB_GetPrintByPrintNo(mCurrentUser.mCurrentKyokaID,mCurrentUser.mCurrentKyozaiID, 0);
	        if(resultsata != null) {
	    		MdtData mdtData = null;
	    		try {
	    			mdtData = IO.JsonToMdtData(new String(resultsata.mQuestion.mMDTData, "UTF-8"));
	    		} catch (Exception e) {
	    			mdtData = null;
	    		}
	    		if(mdtData != null) {
	    			standardString = String.format(Locale.JAPAN, "%d分", mdtData.StandardTime / 60);
	    			limitString = String.format(Locale.JAPAN, "%d分", mdtData.LimitTime / 60);
	    			mLimitTime = mdtData.LimitTime / 60;
	    		}
	        }
	        mTextviewStandard.setText(standardString);
	        mTextviewLimit.setText(limitString);
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
    		ScreenChange.doScreenChangeSpcecialTest(this, mCurrentUser, mLearningmode, mLimitTime, mEntrance);
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    public void onClickBack(View view){
		System.gc();
    	try {
    		if(mEntrance == 0) {
    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDYSPCECIALSTART, ScreenChange.SCNO_LIST, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
    		}
    		else {
    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDYSPCECIALSTART, ScreenChange.SCNO_ENTRANCE, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
    		}
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }

}
