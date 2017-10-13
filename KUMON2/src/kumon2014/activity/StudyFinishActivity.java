package kumon2014.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.*;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.KumonLog;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.DPrintSet;
import kumon2014.kumondata.DResultData;
import kumon2014.kumondata.DStudent;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.message.KumonMessage;
import kumon2014.webservice.CollectAndroidLogResponse;
import kumon2014.webservice.KumonSoap;
import kumon2014.webservice.RegistGradingResultResponse;

public class StudyFinishActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	private TextView					mTextviewName;
	private TextView					mTextviewKyozai;
	private TextView					mTextviewMsg;
	private ImageButton					mImageButtonSend;

	public void onClickMemory(View view) {
		System.gc();
		Utility.memory(getApplicationContext());
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_studyfinish);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

	        mTextviewName = (TextView) findViewById(R.id.textview_Name);
	        mTextviewKyozai = (TextView) findViewById(R.id.textview_Kyozai);
	        mTextviewMsg = (TextView) findViewById(R.id.textview_Msg);
	        mImageButtonSend = (ImageButton) findViewById(R.id.imagebutton_send);

	        mTextviewName.setText(mCurrentUser.mStudentName);

	        String kyozainame = "";
	        kyozainame = mCurrentUser.mCurrentKyokaKyozaiName;
	        if(mCurrentUser.mCurrentPrintType == 2) {
	        	kyozainame += "�f�f�e�X�g ";
	        }
	        else if(mCurrentUser.mCurrentPrintType == 1) {
	        	kyozainame += "�I���e�X�g ";
	        }
	        mTextviewKyozai.setText(kyozainame);

	    	int mode = intent.getIntExtra(ScreenChange.SF_NOTICEMODE, 0);
	    	if(mode == 0) {
	    		mTextviewMsg.setText("�w�K���I���܂����B");
	    	}
	    	else {
	    		mTextviewMsg.setText("�̓_���I���܂����B");
	    	}


			ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			boolean sendExist = KumonDataCtrl.IsExistSendData(mCurrentUser.mStudentID);
			if(StudentClientCommData.canConnect(cm) == true && sendExist == true) {
				mImageButtonSend.setEnabled(true);
				mImageButtonSend.setImageResource(R.drawable.btn_send2);
			}
			else {
				mImageButtonSend.setEnabled(false);
				mImageButtonSend.setImageResource(R.drawable.btn_send2_off);
			}
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
		mImageButtonSend.setClickable(false);
		showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

    	try {
			//20150110 MOD-S For 2015�N�xVer. �����e�i���X���`�F�b�N
    		//StartRegistGradingResult();
    		ClickHandler<StudyFinishActivity> handler = new ClickHandler<StudyFinishActivity>(this) {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					closeProgress();
					if (msg.what == 0) {
						mActivityRef.get().StartRegistGradingResult();
					}
				}
    		};
    		
			maintenanceCheck(handler);
				return;
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
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

	private void StartRegistGradingResult() {

		//20140617 MOD-S For�@�������s���Ή�
		/***
		ArrayList<DPrintSet> registprintdatalist = KumonDataCtrl.GetRegistDataList(mCurrentUser.mStudentID);
		SoapRegistGradingResultTask task = new SoapRegistGradingResultTask(mCurrentUser, registprintdatalist.size(), StudyFinishActivity.this);
        task.execute(registprintdatalist);
		***/

		showProgress(R.layout.progress_msg_spinner, KumonMessage.MSG_No21);
		
		KumonLoaderCallbacks<RegistGradingResultResponse> callback = new KumonLoaderCallbacks<RegistGradingResultResponse>() {
			@Override
			public Loader<RegistGradingResultResponse> onCreateLoader(int arg0,
					Bundle arg1) {
				SoapRegistGradingResultTaskLoader loader = new SoapRegistGradingResultTaskLoader(StudyFinishActivity.this, mCurrentUser);
				loader.forceLoad();
				return loader;
			}

			@Override
			public void onLoadFinished(
					Loader<RegistGradingResultResponse> arg0,
					RegistGradingResultResponse response) {
	    		EndRegistGradingResult(response);
	    		closeProgress();
			}
		};
		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_REGISTGRADING, this, null, callback);
//		SoapRegistGradingResultTaskLoader task = new SoapRegistGradingResultTaskLoader(mCurrentUser, StudyFinishActivity.this);
//        task.execute();
    	//20140617 MOD-E For�@�������s���Ή�
	}

	@UiThread
	private void EndRegistGradingResult(RegistGradingResultResponse response) {
		if (response.mRresult.mStatus == 0 && response.mRresult.mError.isEmpty()) {
			// ����
			boolean stat = KumonDataCtrl.DB_ClearRegistData(mCurrentUser.mStudentID);

    		KumonDataCtrl.ClearKyozaiPrintSetList();
			KumonDataCtrl.GetKyozaiDataExistList(mCurrentUser.mStudentID);

			if (stat == true) {
				ScreenChange.doScreenChange(this, ScreenChange.SCNO_STUDYFINISH, ScreenChange.SCNO_NOTICE, true, mCurrentUser ,ScreenChange.SF_NOTICEMODE_SEND, ScreenChange.SF_NEXT);
				return;
			} else {
				showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No90, 0, null);
				mImageButtonSend.setClickable(true);
			}
		} else if (response.mRresult.mError.isEmpty() == false) {
			showOkDialog(R.layout.progress_msg_ok, KumonMessage.SF_TTL_NOCONNECT, response.mRresult.mError, 0, null);
			mImageButtonSend.setClickable(true);
		} else {
			showOkDialog(R.layout.progress_msg_ok, KumonMessage.MSG_No8, 0, null);
			mImageButtonSend.setClickable(true);
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////
   	//20140617 MOD-S For�@�������s���Ή�
    //private class SoapRegistGradingResultTask extends AsyncTask<ArrayList<DPrintSet>, Integer, RegistGradingResultResponse> {
    private static class SoapRegistGradingResultTaskLoader extends AsyncTaskLoader</*Void, Integer,*/ RegistGradingResultResponse> {
  	//20140617 MOD-E For�@�������s���Ή�
    	private WeakReference<StudyFinishActivity> 		mActivityRef = null;
    	private CurrentUser		mUser = null;
    	private int				mUploadCnt = 0;

    	//20140617 MOD-S For�@�������s���Ή�
    	//public SoapRegistGradingResultTask(CurrentUser user, int uploadcnt, Activity activity) {
    	public SoapRegistGradingResultTaskLoader(StudyFinishActivity activity, CurrentUser user) {
    		super(activity);
		//20140617 MOD-E For�@�������s���Ή�
   	 		 this.mUser = user;
   	    	//20140617 DEL-S For�@�������s���Ή�
     		//this.mUploadCnt = uploadcnt;
     	    //20140617 DEL-E For�@�������s���Ή�
    		 mActivityRef = new WeakReference<StudyFinishActivity>(activity);
    	 }
/*
    	protected void onPreExecute() {
    		mProgressDialog = new ProgressDialog(mActivity);
    		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		mProgressDialog.setIndeterminate(false);
    		mProgressDialog.setCancelable(false);
			mProgressDialog.setMax(100);
    		mProgressDialog.incrementProgressBy(0);
    		KumonMessage.showProgress(mProgressDialog, mActivity, KumonMessage.MSG_No7);
    	}
*/
    	// UI �X���b�h�ł͂Ȃ�
    	// �o�b�N�O���E���h�Ń_�E�����[�h�������s��
		//20140617 MOD-S For�@�������s���Ή�
    	//protected RegistGradingResultResponse doInBackground(ArrayList<DPrintSet>... registprintdatalist) {
    	@Override
    	@WorkerThread
		public RegistGradingResultResponse loadInBackground() {
   		//20140617 MOD-E For�@�������s���Ή�
    		KumonSoap soap = new KumonSoap();

    	    //20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
    		String[] msg = KumonLog.GetAndroidLogList();
    		if(msg != null && msg.length > 0) {
	    		CollectAndroidLogResponse res = soap.SoapSendAndroidLog(mUser, msg);
	    		if(res.mRresult.mStatus == 0) {
	    			KumonLog.DeleteAndroidLogFile();
	    		}
    		}
    	    //20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
	    	//20150413 ADD-S For 2015�N�xVer. ���ǑΉ�
	        //���ǃt���O���M
			try {
				ArrayList<String> list = DataDBIO.DB_GetReadCommentDataList(mUser.mStudentID);
				soap.SoapSetUnreadFlg(mUser, list);
				DataDBIO.DB_ClearReadCommentDataList(mUser.mStudentID);
			} catch (Exception e) {
			}
	    	//20150413 ADD-E For 2015�N�xVer. ���ǑΉ�


    		Integer totalcnt = 1;
    		RegistGradingResultResponse response = new RegistGradingResultResponse();

    		DStudent student = soap.SoapLoginOnly(mUser);
    		if(student == null) {
    			response.mRresult.mStatus = 99;
    			response.mRresult.mError = "";
    			return response;
    		}
	    	if(student.mSoapStatus != 0 || student.mSoapError.isEmpty() == false) {
	    		response.mRresult.mStatus = student.mSoapStatus;
	    		response.mRresult.mError = student.mSoapError;
	    		return response;
	    	}
    		mUser.mLastSessionID = student.mSessionID;

       		//20140617 MOD-S For�@�������s���Ή�
    		//ArrayList<DPrintSet> uploadlist = registprintdatalist[0];
         	ArrayList<DResultData> uploadlist = KumonDataCtrl.GetRegistPrintSetList(mUser.mStudentID);
    		mUploadCnt = uploadlist.size();
       		//20140617 MOD-E For�@�������s���Ή�

    		int upcnt = uploadlist.size();
    	    for(int i = 0; i < upcnt; i++) {
    	    	{
    	    		final int lTotalCnt = totalcnt;
					mActivityRef.get().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mActivityRef.get().updateProgress(lTotalCnt * 100 / mUploadCnt);
						}
					});
    	    	}
    			//20130324 MOD-E

           		//20140617 MOD-S For�@�������s���Ή�
    	    	//DPrintSet registprintset = uploadlist.get(i);

    	    	DResultData resultdata = uploadlist.get(i);
    			DPrintSet printSet = KumonDataCtrl.GetRejistPrintSet(mUser.mStudentID, resultdata.mKyokaID, resultdata.mKyozaiID, resultdata.mPrintSetID);
           		//20140617 MOD-S For�@�������s���Ή�

    	    	response = soap.SoapRegistGradingResult(mUser, printSet);
    			printSet.ClearAll();
    			printSet = null;

    	    	if(response.mRresult.mStatus == 0 && response.mRresult.mError.isEmpty()) {
    	    		//20140718 DEL-S For Print�P�ʑ��M
    	    		/***
        	    	//20140620 ADD-S �����M�t���O�N���A
    				boolean stat = KumonDataCtrl.DB_ClearRegistPrintSetData(mCurrentUser.mStudentID, resultdata.mKyokaID, resultdata.mKyozaiID, resultdata.mPrintSetID);
    				if(stat == false) {
        	    		uploadlist.clear();
        	    		uploadlist = null;
        	    		response.mRresult.mStatus = 99;
        	    		response.mRresult.mError = KumonMessage.getKumonMessage(KumonMessage.MSG_No90);
    				}
        	    	//20140620 ADD-E �����M�t���O�N���A
        	    	***/
    	    		//20140718 DEL-E For Print�P�ʑ��M
    	    	}
    	    	else {
    	    		uploadlist.clear();
    	    		uploadlist = null;
    	    		return response;
    	    	}
    	    	totalcnt++;
    	    }
	    	soap.SoapLogout(mUser);

    		uploadlist.clear();
    		uploadlist = null;
    		return response;

    	}
/*
    	// UI �X���b�h
    	// �v���O���X�o�[�̕\�����X�V����
    	@Override
    	protected void onProgressUpdate(Integer... progress) {
    		mProgressDialog.setProgress(progress[0]);
    	}

    	// UI �X���b�h
    	// �_�C�A���O��\������
    	@Override
    	protected void onPostExecute(RegistGradingResultResponse response) {
    		mProgressDialog.dismiss();

    		EndRegistGradingResult(response);
    	}
*/
    }

}
