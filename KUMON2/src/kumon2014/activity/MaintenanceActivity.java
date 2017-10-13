package kumon2014.activity;


import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;


public class MaintenanceActivity extends BaseActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
//	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_maintenance);
        }
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Utility.cleanupView(findViewById(R.id.maintenance_topview));
    	System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    public void onClickBack(View view) {
    	try {
    		this.finish();
    	}
    	catch(Exception e) {
    		SLog.DB_AddException(e);
    	}
    }
}


