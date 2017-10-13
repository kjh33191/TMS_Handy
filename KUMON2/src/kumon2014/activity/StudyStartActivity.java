package kumon2014.activity;

import java.util.ArrayList;

import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.MyTimingLogger;
import kumon2014.common.ScreenChange;
import kumon2014.common.Utility;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.DResultData;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.message.KumonMessage;

public class StudyStartActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	private TextView mTextviewName;
	private TextView mTextviewKyozai;
	private TextView mTextviewStartPage;
	private Spinner mSpinnerPageNum;

	private int 	mEntrance;

	public void onClickMemory(View view) {
		Utility.memory(getApplicationContext());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName()+"#onCreate");
		try {
			setContentView(R.layout.activity_studystart);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser) intent.getSerializableExtra("CurrentUser");
			mEntrance = (int)intent.getIntExtra(ScreenChange.SF_ENTRANCE, ScreenChange.SF_NEXT);

			mTextviewName = (TextView) findViewById(R.id.textview_Name);
			mTextviewKyozai = (TextView) findViewById(R.id.textview_Kyozai);
			mTextviewStartPage = (TextView) findViewById(R.id.textview_StartPage);
			mSpinnerPageNum = (Spinner) findViewById(R.id.Spinner_PageNum);

			mTextviewName.setText(mCurrentUser.mStudentName);
			mTextviewKyozai.setText(mCurrentUser.mCurrentKyokaKyozaiName);

			logger.addSplit("Setup UI");
			KumonLoaderCallbacks<ArrayList<DResultData>> callback = new KumonLoaderCallbacks<ArrayList<DResultData>>() {
				@Override
				public Loader<ArrayList<DResultData>> onCreateLoader(int id, Bundle args) {
					AsyncTaskLoader<ArrayList<DResultData>> loader = new AsyncTaskLoader<ArrayList<DResultData>>(StudyStartActivity.this) {
						@Override
						public ArrayList<DResultData> loadInBackground() {
							logger.addSplit("AsyncTaskLoader Start");
							ArrayList<DResultData> resultdatalist = null;
							if(mEntrance == ScreenChange.SF_TODAY) {
								resultdatalist = DataDBIO.DB_GetTodayKyozaiPrintSet(
																		mCurrentUser.mStudentID,
																		mCurrentUser.mCurrentKyokaID,
																		mCurrentUser.mCurrentKyozaiID,
																		10);
								logger.addSplit("DataDBIO.DB_GetTodayKyozaiPrintSet");

							}
							else if(mEntrance == ScreenChange.SF_HOMEWORK) {
								resultdatalist = DataDBIO.DB_GetHomeKyozaiPrintSet(
																		mCurrentUser.mStudentID,
																		mCurrentUser.mCurrentKyokaID,
																		mCurrentUser.mCurrentKyozaiID,
																		10);

								logger.addSplit("DataDBIO.DB_GetHomeKyozaiPrintSet");
							}
							else {
								resultdatalist = DataDBIO.DB_GetNextKyozaiPrintSet(
																			mCurrentUser.mStudentID,
																			mCurrentUser.mCurrentKyokaID,
																			mCurrentUser.mCurrentKyozaiID,
																			10);
								logger.addSplit("DataDBIO.DB_GetNextKyozaiPrintSet");
							}
							logger.addSplit("AsyncTaskLoader End");
							return resultdatalist;
						}
						
					};
					loader.forceLoad();
					return loader;
				}

				@Override
				public void onLoadFinished(
						Loader<ArrayList<DResultData>> loader,
						ArrayList<DResultData> resultdatalist) {
					logger.addSplit("onLoadFinished start");
			
					closeProgress();
					int printno = 0;
					int schedulednum = 1;

					if (resultdatalist != null && resultdatalist.size() > 0) {
						printno = resultdatalist.get(0).mPrintNo;
						schedulednum = resultdatalist.get(0).mScheduledNum;
					}

					// 開始ページ
					mTextviewStartPage.setText(String.valueOf(printno) + "から");

					// ページ数
					int pos = -1;
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(StudyStartActivity.this, android.R.layout.simple_spinner_item);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					int page = resultdatalist.size();
					if (page > 10) {
						page = 10;
					}
					for (int i = 0; i < page; i++)
					{
						adapter.add(String.valueOf(i + 1));
						if (i + 1 == schedulednum) {
							pos = i;
						}
					}
					if (pos == -1) {
						pos = page - 1;
					}
					mSpinnerPageNum.setAdapter(adapter);
					mSpinnerPageNum.setSelection(pos);
					resultdatalist.clear();
					resultdatalist = null;

					logger.addSplit("onLoadFinished end");
					logger.dumpToLog();
				}
				
			};
			showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);
			KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_GETDBPRINTSET, this, null, callback);

		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
//		System.gc();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public void onDestroy() {
		super.onDestroy();
		Utility.cleanupView(findViewById(R.id.studystart_topview));
		System.gc();
	}

	@Override
	public void onLowMemory() {
		Utility.onLowMemory(getApplicationContext());
	}

	public void onClickStart(View view) {
		System.gc();
		try {
			String pagenum = mSpinnerPageNum.getSelectedItem().toString();
    		int learningmode = KumonDataCtrl.SF_DATATYPE_NEXT;
    		switch(mEntrance) {
    			case(ScreenChange.SF_NEXT):
    				learningmode = KumonDataCtrl.SF_DATATYPE_NEXT;
    				break;
    			case(ScreenChange.SF_TODAY):
    				learningmode = KumonDataCtrl.SF_DATATYPE_TODAY;
    				break;
    			case(ScreenChange.SF_HOMEWORK):
    				learningmode = KumonDataCtrl.SF_DATATYPE_HOMEWORK;
    				break;
    		}
			ScreenChange.doScreenChangeNext(this, mCurrentUser,	Utility.strToInt(pagenum), learningmode);
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void onClickBack(View view) {
		System.gc();
		try {
    		if(mEntrance == 0) {
    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDYSTART, ScreenChange.SCNO_LIST, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
    		}
    		else {
    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDYSTART, ScreenChange.SCNO_ENTRANCE, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
    		}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

}
