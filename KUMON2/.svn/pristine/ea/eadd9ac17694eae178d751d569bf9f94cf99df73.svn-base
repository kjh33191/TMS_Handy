package kumon2014.activity;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.Utility;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.log.SLog;
import kumon2014.database.master.MQuestion2;
import kumon2014.database.master.MastDBIO;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.kumondata.WDownloadResultData;
import kumon2014.kumondata.WQuestionDataList;
import kumon2014.message.KumonMessage;
import kumon2014.webservice.KumonSoap;
import kumon2014.webservice.StudentGradingStatusResponse;

public class GradeStatusActivity extends BaseActivity {
	private static final String SF_WAITING = "採点待ち：";
	CurrentUser mCurrentUser = null;

	private TextView					mTextviewName;
	private TextView					mTextviewGradeWait;
	private TextView					mTextviewGradeToday;
	private TextView					mTextviewGradeTodayLB;
	private TextView					mTextviewGradeHome;
	private TextView					mTextviewGradeHomeLB;
	private ImageView					mImageViewHana;

	private ImageButton					mImageButtonUpdate;

	private Timer 						m_Timer = null;
	private GradeStatusTimerTask 		m_TimerTask = null;
	private	Handler 					mHandler = new Handler();

	//private ProgressDialog 				mProgressDialog;

	public void onClickMemory(View view) {
		System.gc();
		Utility.memory(getApplicationContext());
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_gradestatus);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

	        mTextviewName = (TextView) findViewById(R.id.textview_Name);
	        mTextviewGradeWait = (TextView) findViewById(R.id.textview_gradewait);
	        mTextviewGradeTodayLB = (TextView) findViewById(R.id.textview_gradetodayLB);
	        mTextviewGradeHomeLB = (TextView) findViewById(R.id.textview_gradehomeLB);
	        mTextviewGradeToday = (TextView) findViewById(R.id.textview_gradetoday);
	        mTextviewGradeHome = (TextView) findViewById(R.id.textview_gradehome);
	        mImageViewHana = (ImageView) findViewById(R.id.imageview_hana);

	        mImageButtonUpdate = (ImageButton) findViewById(R.id.imagebutton_update);

			//名前
	        mTextviewName.setText(mCurrentUser.mStudentName);

	        //初期化
	        mTextviewGradeWait.setText("");
	        mTextviewGradeTodayLB.setText("");
	        mTextviewGradeHomeLB.setText("");
	        mTextviewGradeToday.setText("");
	        mTextviewGradeHome.setText("");
	        mImageViewHana.setVisibility(View.INVISIBLE);

	        //タイマーの初期化処理
	        m_TimerTask = new GradeStatusTimerTask();
			m_Timer = new Timer(true);
			m_Timer.schedule(m_TimerTask, 10);

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
    public void onClickUpdate(View view){
    	mImageButtonUpdate.setClickable(false);
    	StartGradeStatus();
//    	mImageButtonUpdate.setClickable(true);
    }
    public void onClickReceive(View view){
    	mImageButtonUpdate.setClickable(false);
    	if(KumonDataCtrl.IsExistSendData(mCurrentUser.mStudentID) == true) {
			showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No43, 0, null);
			mImageButtonUpdate.setClickable(true);
			return;
    	}
    	StartGetRetryResultData();
//    	mImageButtonUpdate.setClickable(true);
    }
    public void onClickTop(View view){
    	try {
    		this.finish();
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
	private void StartGradeStatus() {
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No41);

		KumonLoaderCallbacks<StudentGradingStatusResponse> callback = new KumonLoaderCallbacks<StudentGradingStatusResponse>() {

			@Override
			public Loader<StudentGradingStatusResponse> onCreateLoader(
					int arg0, Bundle arg1) {
				SoapStudentGradingStatusTaskLoader loader = new SoapStudentGradingStatusTaskLoader(GradeStatusActivity.this, mCurrentUser);
				loader.forceLoad();
				return loader;
			}

			@Override
			public void onLoadFinished(
					Loader<StudentGradingStatusResponse> arg0,
					StudentGradingStatusResponse response) {
				closeProgress();
				
	    		mImageButtonUpdate.setClickable(true);
	    		EndGradeStatus(response);
			}
		};
		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_STUDENTGRADINGSTATUS, this, null, callback);
//		SoapStudentGradingStatusTaskLoader task = new SoapStudentGradingStatusTaskLoader(mCurrentUser, GradeStatusActivity.this);
//        task.execute();
	}
	private void EndGradeStatus(StudentGradingStatusResponse response) {

		if(response == null || response.mRresult.mStatus != 0 || response.mRresult.mError.isEmpty() == false) {

			View.OnClickListener okListener = new View.OnClickListener() {
				public void onClick(View v) {
					finish();
		    		mHandler.post( new Runnable() {
		    			public void run() {
		    				activity_finish();
		    				return;
		    			}
		    		});
				}
			};

			if(response.mRresult.mStatus == 10) {
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No42, 0, okListener);
				return;
			}
			else {
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No8, 0, okListener);
				return;
			}
		}

		String msg = SF_WAITING + String.valueOf(response.GetGradingWaitNum()) + "人";
        mTextviewGradeWait.setText(msg);
        int gradetoday = response.GetTodayGrade();
        int gradehome = response.GetHomeworkGrade();
		String msgLB = "";

        if(gradetoday == -200) {
        	msg = "―";
        	msgLB = "";
        }
        else if(gradetoday == -100) {
        	msg = "◎";
        	msgLB = "";
        }
        else if(gradetoday == -1) {
        	msg = "採点待ち";
        	msgLB = "さいてんまち";
        }
        else if(gradetoday == 0) {
        	msg = "〇";
        	msgLB = "";
        }
        else {
        	msg = String.valueOf(gradetoday);
        	msgLB = "";
        }
        mTextviewGradeTodayLB.setText(msgLB);
        mTextviewGradeToday.setText(msg);

        if(gradehome == -200) {
        	msg = "―";
        	msgLB = "";
        }
        else if(gradehome == -100) {
        	msg = "◎";
        	msgLB = "";
        }
        else if(gradehome == -1) {
        	msg = "採点待ち";
        	msgLB = "さいてんまち";
        }
        else if(gradehome == 0) {
        	msg = "〇";
        	msgLB = "";
        }
        else {
        	msg = String.valueOf(gradehome);
        	msgLB = "";
        }
        mTextviewGradeHomeLB.setText(msgLB);
        mTextviewGradeHome.setText(msg);

        if((gradetoday == -200 || gradetoday == -100 || gradetoday == 0) && (gradehome == -200 || gradehome == -100 || gradehome == 0) && ((gradetoday == -200 && gradehome == -200) == false)) {
            mImageViewHana.setVisibility(View.VISIBLE);
        }
        else {
            mImageViewHana.setVisibility(View.INVISIBLE);
        }
	}
	//###########################################################################################
	private void StartGetRetryResultData() {
		// TODO: Progress count
		showProgress(R.layout.progress_msg_spinner, KumonMessage.MSG_No32);
		
		KumonLoaderCallbacks<WDownloadResultData> callback = new KumonLoaderCallbacks<WDownloadResultData>() {
			@Override
			public Loader<WDownloadResultData> onCreateLoader(int arg0,
					Bundle arg1) {
				SoapReceiveResultDataTaskLoader loader = new SoapReceiveResultDataTaskLoader(GradeStatusActivity.this, mCurrentUser);
				loader.forceLoad();
				return loader;
			}

			@Override
			public void onLoadFinished(Loader<WDownloadResultData> arg0, WDownloadResultData resultdata) {
				mImageButtonUpdate.setClickable(true);
				EndGetResultData(resultdata);
			}

		};
		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_GETRETRYRESULT, this, null, callback);
//		SoapReceiveResultDataTaskLoader task = new SoapReceiveResultDataTaskLoader(GradeStatusActivity.this);
//		task.execute(mCurrentUser);
	}
	private void EndGetResultData(WDownloadResultData resultdata) {
		if(resultdata == null) {
			closeProgress();
			return;
		}

		if (resultdata.mSoapStatus == 0 && resultdata.mSoapError.isEmpty()) {
			if(KumonDataCtrl.bProgressStop == true) {
				closeProgress();
				return;
			}

			boolean stat = true;
			if(resultdata.mDownLoadResultDataList.size() > 0) {
				stat = DataDBIO.DB_SaveDownLoadDataAllRetry(mCurrentUser.mStudentID, resultdata);
			}
			//MastDBIO.DB_InserQuestionDataList(resultdata.mDownLoadQuestionList);
			resultdata.ClearDownLoadData();
			resultdata = null;

			KumonDataCtrl.ClearKyozaiPrintSetList();
			KumonDataCtrl.GetKyozaiDataExistList(mCurrentUser.mStudentID);

			System.gc();
//			if(mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//			}
			closeProgress();

			if (stat == true) {
				this.finish();
			} else {
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No90, 0, null);
			}
		} else if (resultdata.mSoapError.isEmpty() == false) {
//			if(mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//			}
			closeProgress();
			showOkDialog(R.layout.progress_msg_ok, KumonMessage.SF_TTL_NOCONNECT, resultdata.mSoapError, 0, null);
		} else {
//			if(mProgressDialog != null) {
//				mProgressDialog.dismiss();
//				mProgressDialog = null;
//			}
			showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No8, 0, null);
		}
		System.gc();
	}

	private void activity_finish()
	{
		this.finish();
	}
	/*
	void CloseProgress()
	{
		if(mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		if(mProgressDialogCancel != null) {
			mProgressDialogCancel.dismiss();
			mProgressDialogCancel = null;
		}
	}
*/
	/////////////////////////////////////////////////////////////////////////////////////////
    private static class SoapStudentGradingStatusTaskLoader extends AsyncTaskLoader</*Void, Void,*/ StudentGradingStatusResponse> {
//    	private WeakReference<GradeStatusActivity> 		mActivityRef = null;
    	private CurrentUser		mUser = null;
//    	private ProgressDialog 	mProgressDialog;

    	 public SoapStudentGradingStatusTaskLoader(GradeStatusActivity activity, CurrentUser user) {
    		 super(activity);
    		 mUser = user;
//    		 mActivityRef = new WeakReference<GradeStatusActivity>(activity);
    	 }
/*
    	protected void onPreExecute() {
    		mProgressDialog = new ProgressDialog(mActivity);
    		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgressDialog.setIndeterminate(false);
    		mProgressDialog.setCancelable(false);
    		KumonMessage.showProgress(mProgressDialog, GradeStatusActivity.this, KumonMessage.MSG_No41);
    	}
*/
    	// UI スレッドではない
    	// バックグラウンドでダウンロード処理を行う
    	@Override
    	public StudentGradingStatusResponse loadInBackground() {
    		KumonSoap soap = new KumonSoap();
    		StudentGradingStatusResponse response = soap.SoapStudentGradingStatus(mUser);
    		return response;
    	}
/*
    	// UI スレッド
    	// プログレスバーの表示を更新する
    	@Override
    	protected void onProgressUpdate(Void... values) {
    	}

    	// UI スレッド
    	// ダイアログを表示する
    	@Override
    	protected void onPostExecute(StudentGradingStatusResponse response) {
    		mProgressDialog.dismiss();
    		mImageButtonUpdate.setClickable(true);
    		EndGradeStatus(response);
    	}
    	*/
    }

	/////////////////////////////////////////////////////////////////////////////////////////
    private static class SoapReceiveResultDataTaskLoader extends AsyncTaskLoader</*CurrentUser, Integer,*/ WDownloadResultData> {
    	private WeakReference<GradeStatusActivity> 		mActivityRef = null;
    	CurrentUser mUser = null;

    	 public SoapReceiveResultDataTaskLoader(GradeStatusActivity activity, CurrentUser user) {
    		 super(activity);
    		 mActivityRef = new WeakReference<GradeStatusActivity>(activity);
    		 mUser = user;
//    		 mProgressDialogCancel = null;
    	 }
/*
    	protected void onPreExecute() {
    		mProgressDialog = new ProgressDialog(mActivity);
    		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		mProgressDialog.setIndeterminate(false);
    		mProgressDialog.setCancelable(false);
			mProgressDialog.setMax(100);
    		mProgressDialog.incrementProgressBy(0);
    		mProgressDialog.setButton(
    			DialogInterface.BUTTON_NEGATIVE,
    			"Cancel",
    			new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					KumonDataCtrl.bProgressStop = true;

    					mProgressDialogCancel = new ProgressDialog(mActivity);
    					mProgressDialogCancel.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    					mProgressDialogCancel.setIndeterminate(false);
    					mProgressDialogCancel.setCancelable(false);
    		    		KumonMessage.showProgress(mProgressDialogCancel, mActivity, KumonMessage.MSG_No32);
    				}
    			}
    		);
    		//20130508 ADD-E No.105
    		KumonMessage.showProgress(mProgressDialog, mActivity, KumonMessage.MSG_No6);
    	}
*/
    	// UI スレッドではない
    	// バックグラウンドでダウンロード処理を行う
    	@Override
    	public WDownloadResultData loadInBackground() {
    		KumonSoap soap = new KumonSoap();
    		WDownloadResultData resultdata = null;
    		try {
    			if(KumonDataCtrl.bProgressStop == true) {
    				mActivityRef.get().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mActivityRef.get().closeProgress();
						}
    				});
    				return null;
    			}

    			int maincnt = 30;
    			resultdata = soap.SoapGetRetryResultData(mUser);
    			resultdata.CheckQuestionData();
    			{
    				final int lMainCnt = maincnt;
    				mActivityRef.get().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mActivityRef.get().updateProgress(lMainCnt);
						}
    				});
    			}
    			int questioncnt =resultdata.mQuestionList.size() + resultdata.mTopQuestionList.size();
    			int donecnt = 0;
    			for(int i= 0; i <resultdata.mQuestionList.size(); i++) {
        			if(KumonDataCtrl.bProgressStop == true) {
        				mActivityRef.get().runOnUiThread(new Runnable() {
    						@Override
    						public void run() {
    							mActivityRef.get().closeProgress();
    						}
        				});
        				return null;
        			}
					MQuestion2 question = resultdata.mQuestionList.get(i);
					WQuestionDataList questiondatalist = soap.SoapGetQuestionData(mUser, question);
					if(questiondatalist.mSoapStatus == 0 && questiondatalist.mSoapError.isEmpty()) {
						MastDBIO.DB_InsertQuestionDataList(questiondatalist.mMQuestionDataList);
						/*
						for(int j = 0; j < questiondatalist.mMQuestionDataList.size(); j++) {
							MQuestion2 savequestion = questiondatalist.mMQuestionDataList.get(j);
							MastDBIO.DB_InsertQuestionData(savequestion);
						}
						*/
					}
					donecnt++;
	    			{
	    				final int lDoneCnt = donecnt;
	    				final int lQuestionCnt = questioncnt;
	    				mActivityRef.get().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mActivityRef.get().updateProgress(30 + ( lDoneCnt * 100  / lQuestionCnt) );
							}
	    				});
	    			}
    			}
    			for(int i= 0; i <resultdata.mTopQuestionList.size(); i++) {
        			if(KumonDataCtrl.bProgressStop == true) {
        				mActivityRef.get().runOnUiThread(new Runnable() {
    						@Override
    						public void run() {
    							mActivityRef.get().closeProgress();
    						}
        				});
        				return null;
        			}
					MQuestion2 question = resultdata.mTopQuestionList.get(i);
					WQuestionDataList questiondatalist = soap.SoapGetQuestionDataByPrintNo(mUser, question);
					if(questiondatalist.mSoapStatus == 0 && questiondatalist.mSoapError.isEmpty()) {
						for(int j = 0; j < questiondatalist.mMQuestionDataList.size(); j++) {
							MQuestion2 savequestion = questiondatalist.mMQuestionDataList.get(j);
							MastDBIO.DB_InsertQuestionData(savequestion);
						}
					}
					donecnt++;
	    			{
	    				final int lDoneCnt = donecnt;
	    				final int lQuestionCnt = questioncnt;
	    				mActivityRef.get().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mActivityRef.get().updateProgress(30 + ( lDoneCnt * 100  / lQuestionCnt) );
							}
	    				});
	    			}
    			}

				mActivityRef.get().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mActivityRef.get().updateProgress(100);
					}
				});
    		}
    		catch(Exception e){
    			SLog.DB_AddException(e);
    		}
    		soap = null;
    		return resultdata;
    	}
/*
    	// UI スレッド
    	// プログレスバーの表示を更新する
    	@Override
    	protected void onProgressUpdate(Integer... progress) {
    		mProgressDialog.setProgress(progress[0]);
    	}
*/
    	/*
    	// UI スレッド
    	// ダイアログを表示する
    	@Override
    	protected void onPostExecute(WDownloadResultData resultdata) {
			if(mProgressDialogCancel != null) {
				mProgressDialogCancel.dismiss();
				mProgressDialogCancel = null;
			}
			mImageButtonUpdate.setClickable(true);
			EndGetResultData(resultdata);
    	}
    	*/
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    class GradeStatusTimerTask extends TimerTask{
    	@Override
    	public void run() {
    		mHandler.post( new Runnable() {
    			public void run() {
    				StartGradeStatus();
    			}
    		});
    	}
    }

}
