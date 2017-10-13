package kumon2014.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.ScreenChange;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;

public class StudyFinishGradeOnClientActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	private TextView					mTextviewName;
	private TextView					mTextviewKyozai;

	public void onClickMemory(View view) {
		System.gc();
		Utility.memory(getApplicationContext());
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_studyfinish_grade_onclient);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

	        mTextviewName = (TextView) findViewById(R.id.textview_Name);
	        mTextviewKyozai = (TextView) findViewById(R.id.textview_Kyozai);

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
    public void goGrade(){

		System.gc();
    	try {
    		ScreenChange.doScreenChangeGradeInstructorOnClient(this, mCurrentUser, true);
			return;
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    public void onClicklogo(View view){

		System.gc();
    	try {
			FragmentManager fm = getFragmentManager();
			AlertFragment af = new AlertFragment();
			af.show(fm, "alert_dialog");
//        	showDialog(0);
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

	public class AlertFragment extends DialogFragment {
		@SuppressLint("InflateParams")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			//レイアウトの呼び出し
			LayoutInflater factory = LayoutInflater.from(StudyFinishGradeOnClientActivity.this);
			final View inputView = factory.inflate(R.layout.inputdialog_password, null);
			final EditText edittext = (EditText) inputView.findViewById(R.id.dialog_edittext);
			edittext.setText("");

			//ダイアログの作成(AlertDialog.Builder)
			return new AlertDialog.Builder(StudyFinishGradeOnClientActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("PASSWORD")
				.setView(inputView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("MMdd", Locale.JAPAN);
						String snswer = sdf.format(date);
						snswer = snswer.substring(3, 4) + snswer.substring(2, 3) + snswer.substring(1, 2) + snswer.substring(0, 1);
						if(edittext.getText().toString().equalsIgnoreCase(snswer)) {
							goGrade();
						}
						edittext.setText("");
					}
				})
				.create();
		}
	}
    
}
