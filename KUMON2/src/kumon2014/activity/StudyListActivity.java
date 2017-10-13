package kumon2014.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.ScreenChange;
import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;
import kumon2014.database.log.SLog;
import kumon2014.kumondata.DKyozaiPrintSet;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.message.KumonMessage;
import kumon2014.webservice.KumonSoap;

/**
 * 教材リスト画面
 * 
 */
public class StudyListActivity extends BaseActivity {
	CurrentUser mCurrentUser = null;

	private TextView					mTextviewName;
	private ArrayList<DKyozaiPrintSet>	mkyozaiPrintSetList = null;
	private boolean 					mEentrance = false;

	//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
	private ArrayList<DKyozaiPrintSet> 			mKyokaMinGradeList = null;		//教科別最低グレード
//	private Context 					mContext;
	private int							mSelectedPos = -1;
	//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告

	public void onClickMemory(View view) {
		Utility.memory(getApplicationContext());
	}

	static class InitData {
		public boolean studentEntrance;
		public ArrayList<DKyozaiPrintSet> templist;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
	        setContentView(R.layout.activity_studylist);
			Intent intent = getIntent();
			mCurrentUser = (CurrentUser)intent.getSerializableExtra("CurrentUser");

	        mTextviewName = (TextView) findViewById(R.id.textview_Name);

	        mTextviewName.setText(mCurrentUser.mStudentName);

	    	//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
	        mKyokaMinGradeList = new ArrayList<DKyozaiPrintSet>();
	        //20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告
        
	        showProgress(R.layout.progress_msg_only, KumonMessage.MSG_No21);

	        KumonLoaderCallbacks<InitData> callback = new KumonLoaderCallbacks<InitData>() {
				@Override
				public Loader<InitData> onCreateLoader(int arg0, Bundle arg1) {
					AsyncTaskLoader<InitData> loader = new AsyncTaskLoader<InitData>(StudyListActivity.this) {
						@Override
						public InitData loadInBackground() {
							InitData data = new InitData();
					        data.templist = KumonDataCtrl.GetKyozaiDataExistList(mCurrentUser.mStudentID);
				    		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				    		if (StudentClientCommData.canConnect(cm) == true) {
		    					KumonSoap soap = new KumonSoap();
	
		    					data.studentEntrance = soap.SoapStudentEntrance(mCurrentUser.mStudentID);
				    		}
	    					return data;
						}
					};
					loader.forceLoad();
					return loader;
				}

				@Override
				public void onLoadFinished(Loader<InitData> arg0, InitData data) {
					try {
						closeProgress();
						
						ArrayList<DKyozaiPrintSet> templist = data.templist;
						mEentrance = data.studentEntrance;
						mkyozaiPrintSetList = MakeEntranceList(templist, mEentrance);

				        //TableRow row;
//				        int cnt = 0;
				        TableLayout table = (TableLayout) findViewById(R.id.tablelayout_learning);
				        TableRow.LayoutParams row_layout_params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				        for(int i = 0; i < mkyozaiPrintSetList.size(); i++){
				        	final int number = i;

				        	TableRow row = new TableRow(StudyListActivity.this);
				        	row.setLayoutParams(row_layout_params);
				        	row.setPadding(2, 2, 2, 2);

				        	if(IsDoneDataExist(i) == true || IsNextDataExist(i) == true) {
					        	TextView tv = new TextView(StudyListActivity.this);
					        	tv.setHeight(62);

					        	tv.setText(mkyozaiPrintSetList.get(i).mKyokaKyozaiName);
					        	tv.setBackgroundColor(Color.WHITE);
					        	tv.setGravity(Gravity.CENTER_VERTICAL);
					        	tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
					        	tv.setTextColor(Color.BLACK);
					        	row.addView(tv);

					        	ImageButton imgbtn1 = new ImageButton(StudyListActivity.this);
					        	if(IsNextDataExist(i) == true) {
					        		imgbtn1.setEnabled(true);
					        		if(IsTodayDataExist(i)) {
					        			imgbtn1.setImageResource(R.drawable.btn_start_min_red);
					        		}
					        		else {
					        			imgbtn1.setImageResource(R.drawable.btn_start_min);
					        		}
					        		imgbtn1.setOnClickListener(new OnClickListener() {
					        			public void onClick(View v) {
					        				onNextClick(v, number);
					        			}
					        		});
					        		//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告

					        		//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
					        		//20150216 MOD-S For 診断テストは対象外
					        		//SetKyozaiMinGrade(mkyozaiPrintSetList.get(i));
					        		if(IsDiagnosisPrint(i) == false) {
					        			SetKyozaiMinGrade(mkyozaiPrintSetList.get(i));
					        		}
					        		//20150216 MOD-E For 診断テストは対象外
					        		//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告
					        	}
					        	else {
					        		imgbtn1.setEnabled(false);
					        		imgbtn1.setImageResource(R.drawable.btn_start_min_off);

					        		//20150216 ADD-S For 採点待ちでも、最低グレードは、最低グレード
					        		if(IsDiagnosisPrint(i) == false) {
							        	if(IsWaitDataExist(i) == true) {
							        		SetKyozaiMinGrade(mkyozaiPrintSetList.get(i));
							        	}
					        		}
					        		//20150216 ADD-E For 採点待ちでも、最低グレードは、最低グレード
					        	}

					        	row.addView(imgbtn1);

					        	ImageButton imgbtn2 = new ImageButton(StudyListActivity.this);
					        	if(IsDoneDataExist(i) == true) {
					        		imgbtn2.setEnabled(true);
					            	imgbtn2.setImageResource(R.drawable.btn_result_min);
					        		imgbtn2.setOnClickListener(new OnClickListener() {
					        			public void onClick(View v) {
					        				onDoneClick(v, number);
					        			}
					        		});
					        	}
					        	else {
					        		imgbtn2.setEnabled(false);
					            	imgbtn2.setImageResource(R.drawable.btn_result_min_off);
					        	}
					        	row.addView(imgbtn2);

					        	table.addView(row);
				        	}
				        }
					}
					catch(Exception e) {
						SLog.DB_AddException(e);
					}
				}

	        };
	        KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_SETKYOZAILIST, this, null, callback);

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
    public void onDestroy() {
    	super.onDestroy();
    	mkyozaiPrintSetList = null;

    	Utility.cleanupView(findViewById(R.id.studylist_topview));
    	System.gc();
    }
    public void onNextClick(View v, int number){
		System.gc();
    	try {
	    	if(number < mkyozaiPrintSetList.size()) {
	    		DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(number);
	    		mSelectedPos = number;

				if(IsNormalPrint(kyozaiprintset.mKyokaID, kyozaiprintset.mKyozaiID)) {
		    		if(CheckMinGrade(kyozaiprintset) == true) {
		    			onDoNextClick(mSelectedPos);
		    		}
		    		else {
//		    			mContext = null;
//						mContext = this;
						View.OnClickListener yesListener = new View.OnClickListener() {
							public void onClick(View v) {
				    			onDoNextClick(mSelectedPos);
							}
						};
						showYesNoDialog(R.layout.progress_msg_yesno, KumonMessage.MSG_No51, R.string.text_positive, yesListener, R.string.text_negative, null);
		    		}
				}
				else {
	    			onDoNextClick(mSelectedPos);
				}
	    	}
    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }
    public void onDoNextClick(int number){
		System.gc();
    	try {
	    	if(number < mkyozaiPrintSetList.size()) {
	    		DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(number);

	    		mCurrentUser.mCurrentKyokaID = kyozaiprintset.mKyokaID;
	    		mCurrentUser.mCurrentKyozaiID = kyozaiprintset.mKyozaiID;
	    		mCurrentUser.mCurrentKyokaKyozaiName = kyozaiprintset.mKyokaKyozaiName;
	    		mCurrentUser.mCurrentKyokaName = kyozaiprintset.mKyokaName;
	    		mCurrentUser.mCurrentKyozaiName = kyozaiprintset.mKyozaiName;
	    		mCurrentUser.mCurrentPrintType = kyozaiprintset.mPrintType;

	    		if(mEentrance) {
	    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_LIST, ScreenChange.SCNO_ENTRANCE, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
	    		}
	    		else {
    				if(IsNormalPrint(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
	    				if(IsRetryDataExist(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
			    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_LIST, ScreenChange.SCNO_STUDYRETRY, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
			    		}
			    		else {
			    			ScreenChange.doScreenChange(this, ScreenChange.SCNO_LIST, ScreenChange.SCNO_STUDYSTART, true, mCurrentUser, 0, ScreenChange.SF_NEXT);
			    		}
    				}
    				else {
	    				if(IsRetryDataExist(mCurrentUser.mCurrentKyokaID, mCurrentUser.mCurrentKyozaiID)) {
			    			ScreenChange.doScreenChangeSpecialStart(this, ScreenChange.SCNO_LIST, mCurrentUser, KumonDataCtrl.SF_DATATYPE_RETRY, ScreenChange.SF_NEXT);
			    		}
			    		else {
			    			ScreenChange.doScreenChangeSpecialStart(this, ScreenChange.SCNO_LIST, mCurrentUser, KumonDataCtrl.SF_DATATYPE_NEXT, ScreenChange.SF_NEXT);
			    		}
    				}
	    		}
	    	}

    	}
		catch(Exception e) {
			SLog.DB_AddException(e);
		}
    }

    public void onDoneClick(View v, int number){
		System.gc();
    	try {
	    	if(number < mkyozaiPrintSetList.size()) {
	    		DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(number);
	    		mCurrentUser.mCurrentKyokaID = kyozaiprintset.mKyokaID;
	    		mCurrentUser.mCurrentKyozaiID = kyozaiprintset.mKyozaiID;
	    		mCurrentUser.mCurrentKyokaKyozaiName = kyozaiprintset.mKyokaKyozaiName;
	    		mCurrentUser.mCurrentKyokaName = kyozaiprintset.mKyokaName;
	    		mCurrentUser.mCurrentKyozaiName = kyozaiprintset.mKyozaiName;

	    		ScreenChange.doScreenChangeDone(this, mCurrentUser);
	    	}
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

    ///////////////////////////////////////////////////////////////////////////////////
	private boolean IsNextDataExist(int pos)
	{
		boolean exist = false;
		if(pos < mkyozaiPrintSetList.size()) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(pos);
			if(mEentrance == true) {
				//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				//exist = kyozaiprintset.mToday || kyozaiprintset.mHomeWork;
				//結局、全部
	   			exist = kyozaiprintset.mNext;
				//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
			}
			else {
	   			exist = kyozaiprintset.mNext;
			}
		}
		return exist;
	}
	private boolean IsTodayDataExist(int pos)
	{
		boolean exist = false;
		if(pos < mkyozaiPrintSetList.size()) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(pos);
//			if(kyozaiprintset.mPrintype == KumonDataCtrl.SF_PRINTTYPE_NORMAL) {
				//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				//if(kyozaiprintset.mToday || kyozaiprintset.mHomeWork) {
				if(kyozaiprintset.mToday || kyozaiprintset.mPast) {
				//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
					exist = true;
				}
//			}
		}
		return exist;
	}

	private boolean IsDoneDataExist(int pos)
	{
		boolean exist = false;
		if(pos < mkyozaiPrintSetList.size()) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(pos);
   			exist = kyozaiprintset.mDone;
		}
		return exist;
	}
	private boolean IsRetryDataExist(String kyokaID, String kyozaiID)
	{
		boolean exist = false;
		for(int i = 0; i < mkyozaiPrintSetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(i);
			if(kyozaiprintset.mKyokaID.equalsIgnoreCase(kyokaID) && kyozaiprintset.mKyozaiID.equalsIgnoreCase(kyozaiID)) {
				if(mEentrance == true) {
					//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
					//exist = kyozaiprintset.mTodayRetry || kyozaiprintset.mHomeWorkRetry;

					//結局全部
	    			exist = kyozaiprintset.mRetry;
					//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				}
				else {
	    			exist = kyozaiprintset.mRetry;
				}
				break;
			}
		}
		return exist;
	}
	private boolean IsNormalPrint(String kyokaID, String kyozaiID)
	{
		boolean normal = true;
		for(int i = 0; i < mkyozaiPrintSetList.size(); i++) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(i);
			if(kyozaiprintset.mKyokaID.equalsIgnoreCase(kyokaID) && kyozaiprintset.mKyozaiID.equalsIgnoreCase(kyozaiID)) {
				if(kyozaiprintset.mPrintType > 0) {
					normal = false;
					break;
				}
			}
		}
		return normal;
	}
	//20150216 ADD-S For 診断テストは対象外
	private boolean IsDiagnosisPrint(int pos)
	{
		boolean normal = false;
		if(pos < mkyozaiPrintSetList.size()) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(pos);
			if(kyozaiprintset.mPrintType == 2) {
				normal = true;
			}
		}
		return normal;
	}
	//20150216 ADD-E For 診断テストは対象外
	//20150216 ADD-S For 採点待ちでも、最低グレードは、最低グレード
	private boolean IsWaitDataExist(int pos)
	{
		boolean exist = false;
		if(pos < mkyozaiPrintSetList.size()) {
			DKyozaiPrintSet kyozaiprintset = mkyozaiPrintSetList.get(pos);
   			exist = kyozaiprintset.mWait;
		}
		return exist;
	}
	//20150216 ADD-e For 採点待ちでも、最低グレードは、最低グレード

	private ArrayList<DKyozaiPrintSet> MakeEntranceList(ArrayList<DKyozaiPrintSet> templist, boolean Eentrance)
	{
		ArrayList<DKyozaiPrintSet> list = new ArrayList<DKyozaiPrintSet>();
		if(templist != null) {
			for(int i = 0; i < templist.size(); i++) {
				DKyozaiPrintSet kyozaiprintset = templist.get(i);
				if(Eentrance == true) {
					//20140917 MOD-S For 学習予定が未来日は宿題学習だけど、赤ボタンではない
					/***
					if(kyozaiprintset.mToday || kyozaiprintset.mHomeWork || kyozaiprintset.mDone) {
						list.add(kyozaiprintset);
					}
					***/
					//結局全部
					list.add(kyozaiprintset);
					//20140917 MOD-E For 学習予定が未来日は宿題学習だけど、赤ボタンではない
				}
				else {
					list.add(kyozaiprintset);
				}
			}
		}

		return list;
	}
	//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
	private void SetKyozaiMinGrade(DKyozaiPrintSet kyozaiprintset) {

		if(mKyokaMinGradeList == null) {
			mKyokaMinGradeList = new ArrayList<DKyozaiPrintSet>();
		}
		boolean exist = false;
		for(int i = 0; i < mKyokaMinGradeList.size(); i++) {
			DKyozaiPrintSet minkyozaiprintset = mKyokaMinGradeList.get(i);
			if(minkyozaiprintset.mKyokaID.equalsIgnoreCase(kyozaiprintset.mKyokaID)) {
				if(minkyozaiprintset.mKyozaiOderNo < kyozaiprintset.mKyozaiOderNo) {
					minkyozaiprintset.mKyozaiOderNo = kyozaiprintset.mKyozaiOderNo;
				}
				exist = true;
				break;
			}
		}
		if(exist == false) {
			DKyozaiPrintSet minkyozaiprintset = new DKyozaiPrintSet(kyozaiprintset.mKyokaID, "", kyozaiprintset.mKyozaiID, "");
			minkyozaiprintset.mKyozaiOderNo = kyozaiprintset.mKyozaiOderNo;
			mKyokaMinGradeList.add(minkyozaiprintset);
		}
	}
	private boolean CheckMinGrade(DKyozaiPrintSet kyozaiprintset) {
		boolean stat = false;

		if(mKyokaMinGradeList == null) {
			return true;	//本当はここにこない
		}

		for(int i = 0; i < mKyokaMinGradeList.size(); i++) {
			DKyozaiPrintSet minkyozaiprintset = mKyokaMinGradeList.get(i);
			if(minkyozaiprintset.mKyokaID.equalsIgnoreCase(kyozaiprintset.mKyokaID)) {
				if(minkyozaiprintset.mKyozaiOderNo <= kyozaiprintset.mKyozaiOderNo) {
					stat = true;
				}
				break;
			}
		}

		return stat;
	}
	//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告

}
