package kumon2014.activity;



import java.util.HashMap;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import kumon2014.common.KumonEnv;
import kumon2014.common.Utility;
import kumon2014.database.env.EnvDBIO;
import kumon2014.database.env.SEnv;
import kumon2014.database.log.SLog;

public class EnvActivity extends BaseActivity {
	private EditText mEditText_WebServiceUrl = null;
	private EditText mEditText_WebPageUrl = null;
	private EditText mEditText_UpdateUrl = null;
	private EditText mEditText_Keepdays = null;
	private EditText mEditText_Logkeepdays = null;

	//20131101 ADD-S For TimeOut
	private EditText mEditText_SoapTimeOut = null;
	//20131101 ADD-E For TimeOut

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_env);

		try{
			mEditText_WebServiceUrl = (EditText) findViewById(R.id.editText_WebServiceUrl);
			mEditText_WebPageUrl = (EditText) findViewById(R.id.editText_WebPageUrl);
			mEditText_UpdateUrl = (EditText) findViewById(R.id.editText_UpdateUrl);
			mEditText_Keepdays = (EditText) findViewById(R.id.editText_keepdays);
			mEditText_Logkeepdays = (EditText) findViewById(R.id.editText_logkeepdays);
			mEditText_SoapTimeOut = (EditText) findViewById(R.id.editText_soaptimeout);


			mEditText_WebServiceUrl.setText(KumonEnv.G_API_WEBSERVICEURL);
			mEditText_WebPageUrl.setText(KumonEnv.G_WEBPAGE_URL);

			mEditText_UpdateUrl.setText(KumonEnv.G_UPDATE_URL);
			mEditText_Keepdays.setText(Integer.toString(KumonEnv.G_KEEP_DAYS));
			mEditText_Logkeepdays.setText(Integer.toString(KumonEnv.G_LOGKEEP_DAYS));
			mEditText_SoapTimeOut.setText(Integer.toString(KumonEnv.G_SOAP_TIMEOUT));
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
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

    	Utility.cleanupView(findViewById(R.id.env_topview));
    	System.gc();
	}

	@Override
	public void onLowMemory() {
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	////////////////////////////////////////////////////////////////////////
	public void onClickOk(View view) {
		try {
			String webServiceUrl = mEditText_WebServiceUrl.getText().toString();
			String WebPageUrl = mEditText_WebPageUrl.getText().toString();
			String updateUrl = mEditText_UpdateUrl.getText().toString();
			String keepdays = mEditText_Keepdays.getText().toString();
			String logkeepdays = mEditText_Logkeepdays.getText().toString();
			String soaptimeout = mEditText_SoapTimeOut.getText().toString();

			webServiceUrl = CheckLastSlash(webServiceUrl);
			WebPageUrl = CheckLastSlash(WebPageUrl);
			updateUrl = CheckLastSlash(updateUrl);

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(SEnv.SF_KEY_ApiUrl, webServiceUrl);
			map.put(SEnv.SF_KEY_AndroidWebPageUrl, WebPageUrl);
			map.put(SEnv.SF_KEY_UpdateUrl, updateUrl);
			map.put(SEnv.SF_KEY_Keepdays, keepdays);
			map.put(SEnv.SF_KEY_Logkeepdays, logkeepdays);
			map.put(SEnv.SF_KEY_SoapTimeOut, soaptimeout);
			EnvDBIO.DB_SetValues(map);

			KumonEnv.G_API_WEBSERVICEURL = webServiceUrl;
			KumonEnv.G_WEBPAGE_URL = WebPageUrl;
			KumonEnv.G_UPDATE_URL = updateUrl;
			KumonEnv.G_KEEP_DAYS = Utility.strToInt(keepdays);
			KumonEnv.G_LOGKEEP_DAYS = Utility.strToInt(logkeepdays);
			KumonEnv.G_SOAP_TIMEOUT = Utility.strToInt(soaptimeout);

			finish();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void onClickCancel(View view) {
		try {
			finish();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
	/////////////////////////////////////////////////////////////////////////

	private String CheckLastSlash(String url) {
		String value = "";
		boolean hitSlash = false;
		int pos = 0;
		if(url.length() > 0) {
			if(url.substring(url.length() - 1, url.length()).equals("/")) {
				hitSlash = true;
				for(int i = url.length()-1 ; i >= 0; i--) {
					if(url.substring(i, i+1).equals("/")) {
						pos = i;
					}
					else {
						value += url.substring(0, pos);
						break;
					}
				}
			}
		}
		if(hitSlash == false) {
			value = url;
		}
		return value;
	}
}
