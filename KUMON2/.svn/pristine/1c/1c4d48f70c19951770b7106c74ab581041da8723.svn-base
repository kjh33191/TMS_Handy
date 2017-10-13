package kumon2014.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import kumon2014.common.CurrentUser;
import kumon2014.common.ScreenChange;
import kumon2014.common.Utility;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.log.SLog;
import kumon2014.database.master.MastDBIO;
import kumon2014.kumondata.DStudent;
import kumon2014.message.KumonMessage;
import net.sqlcipher.database.SQLiteDatabase;

public class SettingActivity extends BaseActivity {
	private CurrentUser mCurrentUser = null;

	private Spinner				mSpinnerStudent;
	private ArrayList<DStudent>	mStudentList;
	private String 				mClearStudentId = "";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.activity_setting);

			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

			mSpinnerStudent = (Spinner) findViewById(R.id.Spinner_Student);

			mStudentList = DataDBIO.DB_GetAllStudent();

	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        adapter.add("全学習者");
	        for(int i = 0; i < mStudentList.size(); i++)
	        {
	            adapter.add(mStudentList.get(i).mName);
	        }
	        mSpinnerStudent.setAdapter(adapter);
	        mSpinnerStudent.setSelection(0);

		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	protected void onRestart() {
		try {
			super.onRestart();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}

	@Override
	protected void onStart() {
		try {
		super.onStart();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}

	@Override
	protected void onResume() {
		try {
		super.onResume();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}

	@Override
	protected void onPause() {
		try {
		super.onPause();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}

	@Override
	protected void onStop() {
		try {
		super.onStop();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}

	@Override
	protected void onDestroy() {
		try {
		super.onDestroy();
    	Utility.cleanupView(findViewById(R.id.setting_topview));
    	System.gc();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
		
	}

	@Override
	public void onLowMemory() {
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
		super.onConfigurationChanged(newConfig);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}

	////////////////////////////////////////////////////////////////////////
	public void onClickEnv(View view) {
		try {
			ScreenChange.doScreenChange(this, ScreenChange.SCNO_SETTING, ScreenChange.SCNO_ENV, false, mCurrentUser, 0, ScreenChange.SF_NEXT);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void onClickClearAll(View view) {
		try {
			View.OnClickListener yesListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DataDBIO.DB_AllClear();
					MastDBIO.DB_AllClear();
					SLog.DB_ClearAll();
				}
			};
			showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No24, 0, yesListener, 0, null);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}

	}
	public void onClickClearStudent(View view) {
		try {
			mClearStudentId = "";
			int pos = mSpinnerStudent.getSelectedItemPosition();
			if(pos > 0) {
				mClearStudentId = mStudentList.get(pos-1).mStudentID;
			}

			View.OnClickListener yesListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mClearStudentId.isEmpty()) {
						DataDBIO.DB_AllClear();
					}
					else {
						DataDBIO.DB_ClearStudent(mClearStudentId);
					}
				}
			};
			showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No25, 0, yesListener, 0, null);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void onClickClearQuestion(View view) {
		try {
			View.OnClickListener yesListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MastDBIO.DB_ClearQuestion();
				}
			};
			showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No26, 0, yesListener, 0, null);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}

	}
	public void onClickReceiveQuestion(View view) {
		try {
			View.OnClickListener yesListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			};
			showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No27, 0, yesListener, 0, null);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void onClickLog(View view) {
		try {
			//20140917 DEL-S For　表示が辺だから出さない
			//ScreenChange.doScreenChange(this, ScreenChange.SCNO_SETTING, ScreenChange.SCNO_LOG, false, mCurrentUser, 0, ScreenChange.SF_NEXT);
			//20140917 DEL-E For　表示が辺だから出さない
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void onClickEnd(View view) {
		try {
			SQLiteDatabase.releaseMemory();
			finish();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
}
