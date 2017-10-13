package kumon2014.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonEnv;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.message.KumonMessage;

/**
 * スタート画面
 *
 */
public class StartActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_start);

			 Intent intent = getIntent();
			 mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");
        }
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	@Override
    public void onDestroy() {
    	super.onDestroy();
    	Utility.cleanupView(findViewById(R.id.start_topview));
    	System.gc();
    }

    public void onClickEnter(View view){
    	try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			if(StudentClientCommData.canConnect(cm) == true) {
		    	ScreenChange.doScreenChange(this, ScreenChange.SCNO_START, ScreenChange.SCNO_LOGIN, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
			}
			else {
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No2, 0, null);
			}
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
	public void onClickSetting(View view){
		try {
			FragmentManager fm = this.getFragmentManager();
			AlertFragment af = new AlertFragment();
			af.show(fm, "alert_dialog");
			
//			showDialog(0);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }

	public class AlertFragment extends DialogFragment {
		@SuppressLint("InflateParams")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			//レイアウトの呼び出し
			PackageManager manager = StartActivity.this.getPackageManager();
	        ApplicationInfo info = null;
	        boolean isDebug = false;
	        try {
	            info = manager.getApplicationInfo(StartActivity.this.getPackageName(), 0);
	        } catch (NameNotFoundException e) {
	            isDebug = false;
	        }
	        if ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE) {
	            isDebug = true;
	        }

	        LayoutInflater factory = LayoutInflater.from(StartActivity.this);
			final View inputView = factory.inflate(R.layout.inputdialog_password, null);
			final EditText edittext = (EditText) inputView.findViewById(R.id.dialog_edittext);
			if (isDebug)
				edittext.setText(KumonEnv.G_PASS);
			else
				edittext.setText("");

			//ダイアログの作成(AlertDialog.Builder)
			return new AlertDialog.Builder(StartActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("PASSWORD")
				.setView(inputView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if(edittext.getText().toString().equalsIgnoreCase(KumonEnv.G_PASS)) {
							TimerTask tt = new TimerTask() {
								@Override
								public void run() {
							    	ScreenChange.doScreenChange(StartActivity.this, ScreenChange.SCNO_START, ScreenChange.SCNO_SETTING, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
								}
								
							};
							Timer timer = new Timer();
							timer.schedule(tt, 100);
						}
						edittext.setText("");
					}
				})
				.create();
		}
	}
	
}
