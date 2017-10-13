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

public class StudyFinishGradeMySelfActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	private TextView					mTextviewName;
	private TextView					mTextviewKyozai;
//	private ImageButton					mImageButtonSend;

	public void onClickMemory(View view) {
		System.gc();
		Utility.memory(getApplicationContext());
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_studyfinish_grade_myself);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

	        mTextviewName = (TextView) findViewById(R.id.textview_Name);
	        mTextviewKyozai = (TextView) findViewById(R.id.textview_Kyozai);
//	        mImageButtonSend = (ImageButton) findViewById(R.id.imagebutton_send);

	        mTextviewName.setText(mCurrentUser.mStudentName);
	        mTextviewKyozai.setText(mCurrentUser.mCurrentKyokaKyozaiName);
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
	protected void onDestroy() {
		super.onDestroy();
    	Utility.cleanupView(findViewById(R.id.studyfinish_topview));
    	System.gc();
	}
	@Override
	public void onLowMemory() {
		Utility.onLowMemory(getApplicationContext());
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
    public void onClickMarkStart(View view){
		System.gc();
    	try {
    		ScreenChange.doScreenChangeGradeMySelf(this, mCurrentUser, true);
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
}
