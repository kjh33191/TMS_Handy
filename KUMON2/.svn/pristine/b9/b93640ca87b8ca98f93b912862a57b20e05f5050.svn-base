package kumon2014.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.message.KumonMessage;

public class LogActivity extends BaseActivity  {
	private ListView mListView_main;
	private LogListAdapter mLogListAdapter;
	private ArrayList<SLog> mLogList = new ArrayList<SLog>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.activity_log);
			mListView_main = (ListView)findViewById(R.id.listView_main);

			mLogList = SLog.DB_GetLogList(50);
			mLogListAdapter = new LogListAdapter();
			mListView_main.setAdapter(mLogListAdapter);

			mLogListAdapter.notifyDataSetChanged();

			mListView_main.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					ListView listView = (ListView) parent;
					// クリックされたアイテムを取得します
					SLog log = (SLog) listView.getItemAtPosition(position);
					Toast.makeText(LogActivity.this, log.mDate + "/n" + log.mSource + "/n" + log.mMessage, Toast.LENGTH_LONG).show();
				}
			});
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

    	Utility.cleanupView(findViewById(R.id.log_topview));
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
	public void onClickClear(View view) {
		try {
			View.OnClickListener yesListener = new View.OnClickListener() {
				public void onClick(View v) {
					SLog.DB_ClearAll();
					mLogList = SLog.DB_GetLogList(50);
					mLogListAdapter.notifyDataSetChanged();
				}
			};
			showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No28, 0, yesListener, 0, null);
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
	public void onClickBack(View view) {
		try {
			finish();
		}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
	}
	/////////////////////////////////////////////////////////////////////////


	private class LogListAdapter extends BaseAdapter {

	    @Override
	    public int getCount() {
	    	return mLogList.size();
	    }

	    @Override
	    public Object getItem(int position) {
	    	return mLogList.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	    	return position;
	    }

	    @SuppressLint("InflateParams")
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	TextView dateTextView;
	    	TextView sourceTextView;
	    	TextView messageTextView;
	    	View v = convertView;
	    	if(v==null){
	    		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		v = inflater.inflate(R.layout.logrow, null);
	    	}
	    	SLog log = (SLog)getItem(position);
	    	if(log != null){
	    		dateTextView = (TextView)v.findViewById(R.id.dateTextView);
	    		sourceTextView = (TextView)v.findViewById(R.id.sorceTextView);
	    		messageTextView = (TextView)v.findViewById(R.id.messageTextView);

	    		dateTextView.setText(log.mDate);
	    		sourceTextView.setText(log.mSource);
	    		messageTextView.setText(log.mMessage);
	    	}
	    	return v;
	    }
	}
}
