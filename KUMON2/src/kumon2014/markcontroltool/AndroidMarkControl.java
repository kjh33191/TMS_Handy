package kumon2014.markcontroltool;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import kumon2014.activity.R;
import kumon2014.common.CurrentUser;
import kumon2014.common.KumonLog;
import kumon2014.common.MyTimingLogger;
import kumon2014.common.StudentClientCommData;
import kumon2014.database.data.DataDBIO;
import kumon2014.database.data.TblSoundCommentData;
import kumon2014.database.log.SLog;
import kumon2014.database.master.MastDBIO;
import kumon2014.kumondata.DPrintSet;
import kumon2014.kumondata.DResultData;
import kumon2014.kumondata.KumonDataCtrl;
import kumon2014.kumondata.STestSaveData;
import kumon2014.markcontroltool.control.RecordDataControl;
import kumon2014.view.MarkControlSurface;
//import kumon2014.view.PenPlayBackCallback;
import kumon2014.view.RecordCallback;
import pothos.markcontroltool.MdtMode;
import pothos.markcontroltool.InkControlTool.CInkMain;
import pothos.markcontroltool.MarkStuct.MdtTestMarkData;
import pothos.markcontroltool.MarkStuct.TestTime;
import pothos.mdtcommon.IO;
import pothos.mdtcommon.DataStructs.MdtData;
import pothos.mdtcommon.DataStructs.PageData;
import pothos.mdtcommon.DataStructs.RecordData;
import pothos.recognizer.KRecognizer;
import pothos.view.PenPlayBackCallback;

public class AndroidMarkControl {
	private MarkControlSurface mMarkControl = null;
	private QuestionControl mQuestionControl = null;
	private StudyIndicator mStudyIndicator = null;

	private MdtTestMarkData mMDT_TestMark = null;
	private MdtData mMDT_MdtData = null;

	private ArrayList<DResultData> mResultDataList = null;
	// 20140731 ADD-S For 学習済み教材の表示は,PrintSet単位でＤＢ参照
	// 初回、インクデータは読み込まず
	// PrintSetIDが変わったら、インクデータのみ再読み込み
	private String LastPrintSetID = "";
	// 20140731 ADD-E For 学習済み教材の表示は,PrintSet単位でＤＢ参照

	// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
	// private DResultData mTopQuestionData = null;
	private ArrayList<DResultData> mTopQuestionDataList = null;
	// 20150126 MOD-E 2015年度Ver. 参照ページを増やす

	public boolean mBShowTopQuestionData = false;

	// / 問題コントロール配列内インデックス
	public int mPageIndex = 0;
	public int mSideIndex = 0;

	private CurrentUser mCurrentUser = null;
	private int mLearningMode = KumonDataCtrl.SF_DATATYPE_NONE;
	private int mNextPage = 0;
	// 20140915 DEL-S For Bug
	// private int mReStart = 0;
	// 20140915 DEL-E For Bug
	private MdtMode mMDT_Mode = MdtMode.None; // 回答、採点モード

	public boolean mGradingMethod_Instructer = false;
	public boolean mGradingMethod_Self = false;
	public boolean mGradingMethod_InstructerOnClient = false;

	private TableRow mPagebar = null;
	private Button mRredpen = null;

	private static final int SF_BAR_BLUE = Color.parseColor("#FF7DCEF4");
	private static final int SF_BAR_RED = Color.parseColor("#FFFFC0CB");

	public TestTime[] mTestTimes = null;
	private Stopwatch[] mSuspendLearningStopWatch = null;

	// For 文字認識
	// private boolean m_bSemiAutoChangeManual = false;

	// 20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
	private ArrayList<String> mLogList = null;

	// 20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

	public AndroidMarkControl(CurrentUser currentUser, int learningMode,
			int nextpage) {
		mQuestionControl = new QuestionControl();
		mCurrentUser = currentUser;
		mLearningMode = learningMode;
		mNextPage = nextpage;

		mStudyIndicator = new StudyIndicator();

	}

	@Override
	protected void finalize() throws Throwable {
		Log.d("debug", "finalize");
		try {
			super.finalize();
		} finally {
			Log.d("debug", "destruction");
			destruction();
		}
	}

	private void destruction() {
		// 解放とか、破棄とか
		mMarkControl = null;
		mQuestionControl = null;
	}

	// 初期化
	public void setView(MarkControlSurface view) {
		mMarkControl = view;
	}

	public MarkControlSurface getView() {
		return mMarkControl;
	}
	
	public void SetButtonCtrl(String kyozaiName, TextView viewpage,
			ImageButton btnback, ImageButton btnnext,
			ArrayList<ImageButton> btnlist) {
		mStudyIndicator.SetButtonCtrl(kyozaiName, viewpage, btnback, btnnext,
				btnlist);
	}

	public void setPageBar(TableRow pagebar, Button redpen) {
		mPagebar = pagebar;
		mRredpen = redpen;
	}

	// 20140626 ADD-S For Undo
	public void SetUndoButton(ImageButton btnundo, int maxundocnt) {
		mMarkControl.SetUndoButton(btnundo, maxundocnt);
	}

	public void Undo() {
		mMarkControl.Undo();
	}

	// 20140626 ADD-E For Undo

	// Load
	public boolean loadDataFromDB(int restart, boolean isSpecaltest) {
		// 20140915 DEL-S For Bug
		// mReStart = restart;
		// 20140915 DEL-E For Bug

		MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName()
				+ "#loadDataFromDB");
		try {
			int bShowStudyIndicator = StudyIndicator.SF_MODE_ENABLE;

			if (restart != 0) {
				STestSaveData testsavedata = STestSaveData.readObject();
				logger.addSplit("!restart STestSaveData.readObject");
				if (testsavedata == null) {
					return false;
				} else {
					mResultDataList = testsavedata.mResultDataList;
					mPageIndex = testsavedata.mQuestionControlIndex;
					mTestTimes = testsavedata.mTestTimes;
					mSuspendLearningStopWatch = testsavedata.mArySuspendLearningStopWatch;
					// 20140915 ADD-S For Bug
					mLearningMode = testsavedata.mLearningMode;
					if (mLearningMode <= KumonDataCtrl.SF_DATATYPE_DONE) {
						mLearningMode = KumonDataCtrl.SF_DATATYPE_NEXT;
					}
					// 20140915 ADD-S For Bug
					// 20150126 ADD-S 2015年度Ver. 参照ページを増やす
					mBShowTopQuestionData = testsavedata.mBShowTopQuestionData;
					mSideIndex = testsavedata.mSideIndex;
					// 20150126 ADD-E 2015年度Ver. 参照ページを増やす
				}
				// 20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
				if (mLogList == null) {
					mLogList = new ArrayList<String>();
				}
				mLogList.clear();
				KumonLog.AddAndroidLog(mLogList,
						mResultDataList.get(mPageIndex).mPrintUnitID,
						mPageIndex, "START Restart ");

				logger.addSplit("!restart KumonLog.AddAndroidLog");
				// 20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

			} else {
				if (mLearningMode == KumonDataCtrl.SF_DATATYPE_GRADESELF
						|| mLearningMode == KumonDataCtrl.SF_GRADEINSTRUCTORONCLIENT) {
					mResultDataList = DataDBIO
							.DB_GetGradePrintSet(mCurrentUser.mStudentID);
					logger.addSplit("restart DataDBIO.DB_GetGradePrintSet");
					bShowStudyIndicator = StudyIndicator.SF_MODE_NONE;
				} else {
					// 20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
					if (mLogList == null) {
						mLogList = new ArrayList<String>();
					}
					mLogList.clear();
					KumonLog.AddAndroidLog(mLogList, "XXXXXXXX", 0, "START ");
					// 20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

					if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT) {
						mResultDataList = DataDBIO.DB_GetNextKyozaiPrintSet(
								mCurrentUser.mStudentID,
								mCurrentUser.mCurrentKyokaID,
								mCurrentUser.mCurrentKyozaiID, mNextPage);
						logger.addSplit("restart DataDBIO.DB_GetNextKyozaiPrintSet");
					} else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY) {
						mResultDataList = DataDBIO.DB_GetRetryKyozaiPrintSet(
								mCurrentUser.mStudentID,
								mCurrentUser.mCurrentKyokaID,
								mCurrentUser.mCurrentKyozaiID);
						logger.addSplit("restart DataDBIO.DB_GetRetryKyozaiPrintSet");
					} else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_TODAY) {
						mResultDataList = DataDBIO.DB_GetTodayKyozaiPrintSet(
								mCurrentUser.mStudentID,
								mCurrentUser.mCurrentKyokaID,
								mCurrentUser.mCurrentKyozaiID, mNextPage);
						logger.addSplit("restart DataDBIO.DB_GetTodayKyozaiPrintSet");
					} else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_TODAYRETRY) {
						mResultDataList = DataDBIO
								.DB_GetTodayRetryKyozaiPrintSet(
										mCurrentUser.mStudentID,
										mCurrentUser.mCurrentKyokaID,
										mCurrentUser.mCurrentKyozaiID);
						logger.addSplit("restart DataDBIO.DB_GetTodayRetryKyozaiPrintSet");
					} else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_HOMEWORK) {
						mResultDataList = DataDBIO.DB_GetHomeKyozaiPrintSet(
								mCurrentUser.mStudentID,
								mCurrentUser.mCurrentKyokaID,
								mCurrentUser.mCurrentKyozaiID, mNextPage);
						logger.addSplit("restart DataDBIO.DB_GetHomeKyozaiPrintSet");
					} else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_HOMEWORKRETRY) {
						mResultDataList = DataDBIO
								.DB_GetHomeRetryKyozaiPrintSet(
										mCurrentUser.mStudentID,
										mCurrentUser.mCurrentKyokaID,
										mCurrentUser.mCurrentKyozaiID);
						logger.addSplit("restart DataDBIO.DB_GetHomeRetryKyozaiPrintSet");
					} else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_DONE) {
						mResultDataList = DataDBIO.DB_GetDoneKyozaiPrintSet(
								mCurrentUser.mStudentID,
								mCurrentUser.mCurrentKyokaID,
								mCurrentUser.mCurrentKyozaiID);
						logger.addSplit("restart DataDBIO.DB_GetDoneKyozaiPrintSet");
						bShowStudyIndicator = StudyIndicator.SF_MODE_NONE;
					}
					// 20150409 ADD-S For 2015年度Ver. 未読コメント
					else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_DONE_UNREAD) {
						mResultDataList = DataDBIO
								.DB_GetUnreadData(mCurrentUser.mStudentID);
						logger.addSplit("restart DataDBIO.DB_GetUnreadData");
						bShowStudyIndicator = StudyIndicator.SF_MODE_NONE;
					}
					// 20150409 ADD-S For 2015年度Ver. 未読コメント
					else {
						return false;
					}
				}

				if (mResultDataList == null || mResultDataList.size() == 0) {
					return false;
				} else {
					mTestTimes = new TestTime[mResultDataList.size()];
					mSuspendLearningStopWatch = new Stopwatch[mResultDataList
							.size()];
				}
			}
			if (isSpecaltest) {
				bShowStudyIndicator = StudyIndicator.SF_MODE_DISABLE;
			}
			mStudyIndicator.SetResultList(mResultDataList);
			logger.addSplit("restart mStudyIndicator.SetResultList");
			mStudyIndicator.Init(bShowStudyIndicator);
			logger.addSplit("restart mStudyIndicator.Init");
			if (bShowStudyIndicator == StudyIndicator.SF_MODE_ENABLE) {
				// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
				// int topprintNo = mStudyIndicator.GetHeadPageNo();
				// mTopQuestionData =
				// MastDBIO.DB_GetPrintByPrintNo(mCurrentUser.mCurrentKyokaID,
				// mCurrentUser.mCurrentKyozaiID, topprintNo);
				if (mResultDataList != null && mResultDataList.size() > 0) {
					int reffrom = mStudyIndicator.GetRefPageNoFrom();
					int refto = mStudyIndicator.GetRefPageNoTo();
					mTopQuestionDataList = MastDBIO.DB_GetRefPrint(
							mCurrentUser.mCurrentKyokaID,
							mCurrentUser.mCurrentKyozaiID, reffrom, refto);
					logger.addSplit("restart MastDBIO.DB_GetRefPrint");
				}
				// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
			} else {
				// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
				// mTopQuestionData = null;
				mTopQuestionDataList = null;
				// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
			}
			return true;
		} finally {
			logger.dumpToLog();
		}
	}

	public boolean loadDataFromWeb(DPrintSet printset) {
		// 20140915 DEL-S For Bug
		// mReStart = 0;
		// 20140915 DEL-E For Bug

		mResultDataList = printset.mResultList;
		mStudyIndicator.Init(StudyIndicator.SF_MODE_NONE);

		return true;
	}

	// Save
	public boolean SavePrintSetData(int pos) {
		boolean ret = false;

		mGradingMethod_Instructer = false;
		mGradingMethod_Self = false;
		mGradingMethod_InstructerOnClient = false;

		// スキップされたページは最後の時間をセット
		Date endTime = new Date();
		if (pos >= 0 && pos < mResultDataList.size()) {
			endTime = mResultDataList.get(pos).mStartDate;
		}
		try {
			ArrayList<DResultData> saveDatalist = new ArrayList<DResultData>();
			String newPrintSetID = UUID.randomUUID().toString();
			for (int i = 0; i <= pos; i++) {
				DResultData resultdata = (DResultData) mResultDataList.get(i)
						.clone();
				if (resultdata.mIsLearning == KumonDataCtrl.SF_LEARNING_NOCHECK) {
					if (IsLearnAble(resultdata)) {
						// 20141208 MOD-S For DebugLog
						// 初回学習時に、Countが２回になってしまう原因調査用
						/***
						 * resultdata.mCount ++;
						 * 
						 * MdtData mdt =
						 * LoadMdtData(resultdata.mQuestion.mMDTData);
						 * MdtTestMarkData testmark = LoadTestDatas(mdt,
						 * resultdata); resultdata.mGradingResultData =
						 * MdtTestMarkData.SaveToJson(testmark);
						 * resultdata.mStartDate = endTime; resultdata.mEndDate
						 * = endTime; resultdata.mAnswerTime = 0;
						 * resultdata.mIsLearning =
						 * KumonDataCtrl.SF_LEARNING_YES;
						 ***/

						if (resultdata.mCount == resultdata.mOrgCount) {
							// 正常
							resultdata.mCount++;

							MdtData mdt = LoadMdtData(resultdata.mQuestion.mMDTData);
							MdtTestMarkData testmark = LoadTestDatas(mdt,
									resultdata);
							resultdata.mGradingResultData = MdtTestMarkData
									.SaveToJson(testmark);
							resultdata.mStartDate = endTime;
							resultdata.mEndDate = endTime;
							resultdata.mAnswerTime = 0;
							resultdata.mIsLearning = KumonDataCtrl.SF_LEARNING_YES;

							String msg = "[SavePrintSetData] (Add Count OK)  (mCount = "
									+ resultdata.mCount
									+ ") (mOrgCount = "
									+ resultdata.mOrgCount
									+ "(mIsLearning = 0) (IsLearnAble() == TRUE)";
							KumonLog.AddAndroidLog(mLogList,
									resultdata.mPrintUnitID, i, msg);

						} else {
							// 異常
							String msg = "!!! [SavePrintSetData] (Add Count Error) (mCount = "
									+ resultdata.mCount
									+ ") (mOrgCount = "
									+ resultdata.mOrgCount
									+ "(mIsLearning = 0) (IsLearnAble() == TRUE)";
							KumonLog.AddAndroidLog(mLogList,
									resultdata.mPrintUnitID, i, msg);

							if (resultdata.mCount != resultdata.mOrgCount + 1) {
								resultdata.mCount++;
							}
							MdtData mdt = LoadMdtData(resultdata.mQuestion.mMDTData);
							MdtTestMarkData testmark = LoadTestDatas(mdt,
									resultdata);
							resultdata.mGradingResultData = MdtTestMarkData
									.SaveToJson(testmark);
							if (resultdata.mStartDate == null) {
								resultdata.mStartDate = endTime;
							}
							if (resultdata.mEndDate == null) {
								resultdata.mEndDate = endTime;
							}
							if (resultdata.mEndDate == null) {
								resultdata.mEndDate = endTime;
							}
							if (resultdata.mAnswerTime < 0) {
								resultdata.mAnswerTime = 0;
							}
							resultdata.mIsLearning = KumonDataCtrl.SF_LEARNING_YES;
						}
						// 20141208 MOD-E For DebugLog
						// 初回学習時に、Countが２回になってしまう原因調査用
					} else {
						resultdata.mIsLearning = KumonDataCtrl.SF_LEARNING_NO;
					}
				}
				if (resultdata.mIsLearning == KumonDataCtrl.SF_LEARNING_YES) { // 学習　送信対象
					resultdata.mPenThickness = mMarkControl.getPenWidth();
					// resultdata.mCount++;
					// 初回学習
					if (resultdata.mCount == 1) {
						resultdata.mPrintSetID = newPrintSetID;
						// 学習場所
						if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT
								|| mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY) {
							resultdata.mLearningPlace = KumonDataCtrl.SF_LEARNINGPLACE_HOME;
						} else {
							resultdata.mLearningPlace = KumonDataCtrl.SF_LEARNINGPLACE_SCHOOL;
						}
					}
					resultdata.mScore = -1;

					if (resultdata.mGradingMethod == KumonDataCtrl.SF_GradingMethod_Auto) {
						// 自動採点は無い
						resultdata.mGradingMethod = KumonDataCtrl.SF_GradingMethod_Instrucore;
					}

					if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT
							|| mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY) {
						if (resultdata.mGradingMethod == 3) {
							// 自宅で 3:指導者採点（生徒端末）は不可
							resultdata.mGradingMethod = 2;
						}
					}

					resultdata.mStatus = KumonDataCtrl.SF_STATUS_GRADEREADY; // 採点待ち
					resultdata.mGradingStatus = KumonDataCtrl.SF_GREADINGSTATUS_LEARNED; // 学習済み
					resultdata.mGrade = 0; // 最終テスト用
					resultdata.mIsLearned = 1; // 学習済み

					//
					switch (resultdata.mGradingMethod) {
					case (KumonDataCtrl.SF_GradingMethod_Self):
						mGradingMethod_Self = true;
						break;
					case (KumonDataCtrl.SF_GradingMethod_Instrucore):
						mGradingMethod_Instructer = true;
						break;
					case (KumonDataCtrl.SF_GradingMethod_InstrucoreOnClient):
						mGradingMethod_InstructerOnClient = true;
						break;
					}

					saveDatalist.add(resultdata);
				}
				resultdata = null;
			}
			if (saveDatalist.size() > 0) {
				ret = DataDBIO.DB_UpdateResultData(mCurrentUser.mStudentID,
						saveDatalist);
				KumonDataCtrl.KyozaiDataExistList_Kyozai(
						mCurrentUser.mStudentID, mCurrentUser.mCurrentKyokaID,
						mCurrentUser.mCurrentKyozaiID);

				// 20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
				boolean stat = true;
				for (int i = 0; i < saveDatalist.size(); i++) {
					DResultData resultdata = (DResultData) saveDatalist.get(i);
					if (resultdata.mOrgCount + 1 != resultdata.mCount) {
						stat = false;
						String msg = "### [SavePrintSetData]  (Count Error) (mCount = "
								+ mQuestionControl.TrialCount
								+ ") (mOrgCount = "
								+ mQuestionControl.mOrgCount + ")";
						KumonLog.AddAndroidLog(mLogList,
								resultdata.mPrintUnitID, -1, msg);
					}
				}
				if (stat == true) {
					KumonLog.ClearAndroidLog(mLogList);
				}
				// 20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			} else {
				ret = true;
				// 20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
				KumonLog.ClearAndroidLog(mLogList);
				// 20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			}
			saveDatalist.clear();
			saveDatalist = null;

			// 20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			KumonLog.PrintoutAndroidLog(mLogList);
			// 20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

		} catch (Exception e) {
			SLog.DB_AddException(e);
			return false;
		}
		return ret;
	}

	public boolean SavePrintSetDataGrade() {
		mGradingMethod_Instructer = false;
		mGradingMethod_Self = false;
		mGradingMethod_InstructerOnClient = false;

		boolean ret = false;
		try {
			ArrayList<DResultData> saveDatalist = new ArrayList<DResultData>();
			for (int i = 0; i < mResultDataList.size(); i++) {
				DResultData resultdata = (DResultData) mResultDataList.get(i)
						.clone();

				switch (resultdata.mGradingMethod) {
				case (KumonDataCtrl.SF_GradingMethod_Self):
					mGradingMethod_Self = true;
					break;
				case (KumonDataCtrl.SF_GradingMethod_Instrucore):
					mGradingMethod_Instructer = true;
					break;
				case (KumonDataCtrl.SF_GradingMethod_InstrucoreOnClient):
					mGradingMethod_InstructerOnClient = true;
					break;
				}

				if (mLearningMode == KumonDataCtrl.SF_DATATYPE_GRADESELF) {
					// 自己採点
					if (resultdata.mGradingMethod != 1) {
						continue;
					}
				} else if (mLearningMode == KumonDataCtrl.SF_GRADEINSTRUCTORONCLIENT) {
					/***
					 * DEL-S 指導者採点（生徒端末）は全ての採点画が可能 //指導者採点（生徒端末）
					 * if(resultdata.mGradingMethod != 3) { continue; } DEL-E
					 ***/
				} else {
					continue;
				}

				// resultdata.mScore = 取得済み;
				resultdata.mGradingStatus = KumonDataCtrl.SF_GREADINGSTATUS_GRADED; // 採点済み
				resultdata.mGrade = 0; // 最終テスト用

				if (resultdata.mScore == 100
						|| resultdata.mCount >= resultdata.mLimitCount) {
					resultdata.mStatus = 10;
				} else {
					resultdata.mStatus = 1;
				}

				saveDatalist.add(resultdata);
				resultdata = null;
			}
			if (saveDatalist.size() > 0) {
				ret = DataDBIO.DB_UpdateResultData(mCurrentUser.mStudentID,
						saveDatalist);
				KumonDataCtrl.KyozaiDataExistList_Kyozai(
						mCurrentUser.mStudentID, mCurrentUser.mCurrentKyokaID,
						mCurrentUser.mCurrentKyozaiID);
			} else {
				ret = true;
			}
			saveDatalist.clear();
			saveDatalist = null;
		} catch (Exception e) {
			SLog.DB_AddException(e);
			return false;
		}
		return ret;
	}

	// テスト中データのSave
	public void saveHalfwayTestData() throws Exception {
		// 20150126 ADD-S 2015年度Ver. 参照ページを増やす
		if (mBShowTopQuestionData == false) {
			// 20150126 ADD-E 2015年度Ver. 参照ページを増やす
			if (mSuspendLearningStopWatch[mPageIndex] == null) {
				mSuspendLearningStopWatch[mPageIndex] = new Stopwatch();
			}
			if (mMDT_Mode != MdtMode.TestSleep) {
				mSuspendLearningStopWatch[mPageIndex].Start();
			}

			// 20150416 MOD-S InkData To Binary
			// mResultDataList.get(mPageIndex).mInkData = GetInkData();
			mResultDataList.get(mPageIndex).mInkData = "";
			mResultDataList.get(mPageIndex).mInkBinary = GetInkBinaryData();
			// 20150416 MOD-E InkData To Binary

			mResultDataList.get(mPageIndex).mGradingResultData = GetMarkData();
			mResultDataList.get(mPageIndex).mRedComment = GetRedComment();
			mResultDataList.get(mPageIndex).mCount = mQuestionControl.TrialCount;
			// 20150126 ADD-S 2015年度Ver. 参照ページを増やす
		}
		// 20150126 ADD-E 2015年度Ver. 参照ページを増やす

		STestSaveData testsavedata = new STestSaveData();
		testsavedata.mResultDataList = mResultDataList;
		testsavedata.mQuestionControlIndex = mPageIndex;
		testsavedata.mTestTimes = mTestTimes;
		testsavedata.mArySuspendLearningStopWatch = mSuspendLearningStopWatch;
		// 20140915 ADD-S For Bug
		testsavedata.mLearningMode = mLearningMode;
		// 20140915 ADD-S For Bug

		// 20150126 ADD-S 2015年度Ver. 参照ページを増やす
		testsavedata.mBShowTopQuestionData = mBShowTopQuestionData;
		testsavedata.mSideIndex = mSideIndex;
		// 20150126 ADD-E 2015年度Ver. 参照ページを増やす

		testsavedata.writeObject();
	}

	// / <summary>
	// / 現在表示しているプリントの学習結果の保存
	// / </summary>
	public void SaveCurrentPrintLearningData() {
		try {

			EndTest();
			// 学習時間の設定と学習時間の算出
			Date startTime = new Date();
			Date endTime = new Date();
			long testTimeSpan;

			if (mTestTimes[mPageIndex] == null) {
				mTestTimes[mPageIndex] = new TestTime();
			}
			startTime = mTestTimes[mPageIndex].m_dtStart;

			if (mSuspendLearningStopWatch[mPageIndex] != null) {
				testTimeSpan = ((endTime.getTime() - startTime.getTime()) - mSuspendLearningStopWatch[mPageIndex]
						.getTime()) * 10000; // 100ナノ秒単位
			} else {
				testTimeSpan = (endTime.getTime() - startTime.getTime()) * 10000; // 100ナノ秒単位
			}

			mTestTimes[mPageIndex].m_dtEnd = endTime;
			mTestTimes[mPageIndex].m_tsSpan = testTimeSpan;

			mResultDataList.get(mPageIndex).mCount = mQuestionControl.TrialCount;
			// 20150416 MOD-S InkData To Binary
			// mResultDataList.get(mPageIndex).mInkData = GetInkData();
			mResultDataList.get(mPageIndex).mInkData = "";
			mResultDataList.get(mPageIndex).mInkBinary = GetInkBinaryData();
			// 20150416 MOD-E InkData To Binary

			mResultDataList.get(mPageIndex).mGradingResultData = GetMarkData();
			mResultDataList.get(mPageIndex).mRedComment = GetRedComment();
			// mResultDataList.get(mPageIndex).mTagComment = GetTagComment();
			// mResultDataList.get(mPageIndex).mTagText = GetTagText();

			mResultDataList.get(mPageIndex).mStartDate = startTime;
			mResultDataList.get(mPageIndex).mEndDate = endTime;
			mResultDataList.get(mPageIndex).mAnswerTime = testTimeSpan;
			// 20141208 ADD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			// mResultDataList.get(mPageIndex).mOrgCount =
			// mQuestionControl.mOrgCount;
			// 20141208 ADD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

			// 20150303 ADD-S For 2015年度Ver. 音声メモステータス
			mResultDataList.get(mPageIndex).mSoundRecordStatus = checkSoundRecordStatus();
			// 20150303 ADD-E For 2015年度Ver. 音声メモステータス
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void SaveCurrentPrintGradingData() {
		try {
			mMarkControl.REPLAY_TouchEnd();

			// 前ページを採点後の状態にする
			for (int i = 0; i < mMDT_TestMark.getPageCount(); i++) {
				if (mMDT_TestMark.mPageMarks.get(i).getMdtPageNumber() == mSideIndex) {
					mMarkControl.SaveManualMark(mSideIndex);
				}
			}
			mResultDataList.get(mPageIndex).mGradingResultData = GetMarkData();
			mResultDataList.get(mPageIndex).mScore = GetScore();
			mResultDataList.get(mPageIndex).mRedComment = GetRedComment();
			mResultDataList.get(mPageIndex).ManualMarked = mMarkControl
					.GetManualMark();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	// 20140915 MOD-S For Bug
	// public boolean InitializeQuestionControl(int page, boolean isTop, boolean
	// isConfrim, RelativeLayout layout, Context context) {
	public boolean InitializeQuestionControl(int page, boolean isTop,
			boolean isConfrim, RelativeLayout layout, Context context,
			int restart) {
		// 20140915 MOD-E For Bug
		
		MyTimingLogger logger = new MyTimingLogger("InitializeQuestionControl");
		try {
			mBShowTopQuestionData = false;

			mPageIndex = page;
			DResultData resultData = mResultDataList.get(mPageIndex);
			
			logger.addSplit("mResultDataList.get");
			
			// 20140731 ADD-S For 学習済み教材の表示は,PrintSet単位でＤＢ参照
			if (mLearningMode == KumonDataCtrl.SF_DATATYPE_DONE) {
				if (LastPrintSetID.equalsIgnoreCase(resultData.mPrintSetID) == false) {
					// PrintSetが違うから、前回のPrintSetIDのインクデータとPrintDataをクリアし、
					// 今回対象のPrintSetIDのインクデータとPrintDataをDBから取得する
					if (LastPrintSetID.length() > 0) {
						ClearLastPrintSetData(mResultDataList, LastPrintSetID);
					}
					mResultDataList = DataDBIO.DB_GetInkDataByPrintSetID(
							mResultDataList, resultData.mPrintSetID);
					logger.addSplit("DataDBIO.DB_GetInkDataByPrintSetID");
				}
			}
			LastPrintSetID = resultData.mPrintSetID;
			// 20140731 ADD-E For 学習済み教材の表示は,PrintSet単位でＤＢ参照

			if (mQuestionControl == null) {
				mQuestionControl = new QuestionControl();
			}
			mQuestionControl.SetResultData(resultData);
			logger.addSplit("mQuestionControl.SetResultData");

			// ページバーの色設定
			setPageBarColor(resultData);
			logger.addSplit("setPageBarColor");

			// SetUp
			loadDataFromPrintData(resultData);
			logger.addSplit("loadDataFromPrintData");
			
			setDataForView(resultData);
			logger.addSplit("setDataForView");

			resultData.InitPageWork(mMDT_TestMark.getPageCount());
			logger.addSplit("resultData.InitPageWork");

			mMarkControl.setManualMark(resultData.ManualMarked);

			mSideIndex = isTop ? 0 : (int) getSideNumberMax();

			mStudyIndicator.SetCurrent(resultData.mPrintNo, mSideIndex);

			mMDT_Mode = MdtMode.Test;

			// 20141208 MOD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			String msg = "[InitializeQuestionControl] (START)";
			KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
					mPageIndex, msg);
			// 20141208 MOD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

			// 満点、指導者採点待ち、3回目の学習が終了している場合は参照のみ可能とする
			if (isConfrim == true) {
				boolean greaded = true;
				if (mQuestionControl.Status == KumonDataCtrl.SF_STATUS_GRADEREADY
						|| mQuestionControl.Status == KumonDataCtrl.SF_STATUS_GRADING) {
					greaded = false;
				}
				mMDT_Mode = MdtMode.DisplayMark;
				mMarkControl.showPage(mSideIndex, mMDT_Mode, mLearningMode,
						mMDT_TestMark, mQuestionControl.TrialCount, greaded,
						true);
				logger.addSplit("mMarkControl.showPage");
			} else {
				// 20140622 MOD-S For Bug
				// if (IsLearnAble(mQuestionControl)) {
				boolean bIsLearnAble = false;
				if (mResultDataList.get(mPageIndex).mIsLearning == KumonDataCtrl.SF_LEARNING_NOCHECK) {
					bIsLearnAble = IsLearnAble(mQuestionControl);
				} else if (mResultDataList.get(mPageIndex).mIsLearning == KumonDataCtrl.SF_LEARNING_YES) {
					bIsLearnAble = true;
				} else {
					bIsLearnAble = false;
				}

				if (bIsLearnAble) {
					// 20140622 MOD-E For Bug
					mMDT_Mode = MdtMode.Test;
					// 20140915 MOD-S For Bug
					// if(mReStart == 0 &&
					// mResultDataList.get(mPageIndex).mIsLearning !=
					// KumonDataCtrl.SF_LEARNING_YES) {
					if (restart == 0
							&& mResultDataList.get(mPageIndex).mIsLearning != KumonDataCtrl.SF_LEARNING_YES) {
						// 20140915 MOD-E For Bug
						// 20141208 MOD-S For DebugLog
						// 初回学習時に、Countが２回になってしまう原因調査用
						// mQuestionControl.TrialCount++;
						if (mQuestionControl.TrialCount == mQuestionControl.mOrgCount) {
							mQuestionControl.TrialCount++;
							String msg2 = "[InitializeQuestionControl]  (Add Count OK) (Count = "
									+ mQuestionControl.mOrgCount
									+ " -> To "
									+ mQuestionControl.TrialCount
									+ ") (Add Count Normal)";
							KumonLog.AddAndroidLog(mLogList,
									mQuestionControl.PrintUnitID, mPageIndex,
									msg2);
						} else {
							String msg2 = "!!! [InitializeQuestionControl]  (Add Count Error) (Page = "
									+ mPageIndex
									+ ") (mCount = "
									+ mQuestionControl.TrialCount
									+ ") (mOrgCount = "
									+ mQuestionControl.mOrgCount
									+ "(mIsLearning = "
									+ mResultDataList.get(mPageIndex).mIsLearning
									+ ")";
							KumonLog.AddAndroidLog(mLogList,
									mQuestionControl.PrintUnitID, mPageIndex,
									msg2);
						}
						// 20141208 MOD-E For DebugLog
						// 初回学習時に、Countが２回になってしまう原因調査用
					}
					mMarkControl.showPage(mSideIndex, mMDT_Mode, mLearningMode,
							mMDT_TestMark, mQuestionControl.TrialCount, true,
							true);
					// 20140915 MOD-S For Bug
					// if(mReStart != 0) {
					if (restart != 0) {
						// 20140915 MOD-S For Bug
						if (mSuspendLearningStopWatch[mPageIndex] == null) {
							mSuspendLearningStopWatch[mPageIndex] = new Stopwatch();
						}
						mSuspendLearningStopWatch[mPageIndex].Stop();
					} else {
						if (mResultDataList.get(mPageIndex).mIsLearning == KumonDataCtrl.SF_LEARNING_YES) {
							// 休止中時間計測用のストップウォッチを停止
							if (mSuspendLearningStopWatch[mPageIndex] == null) {
								mSuspendLearningStopWatch[mPageIndex] = new Stopwatch();
							}
							mSuspendLearningStopWatch[mPageIndex].Stop();
						} else {
							StartTest(mPageIndex);
							mResultDataList.get(mPageIndex).mCount = (int) mQuestionControl.TrialCount;
						}
					}
					mResultDataList.get(mPageIndex).mIsLearning = KumonDataCtrl.SF_LEARNING_YES;
				} else {
					if (mQuestionControl.Status == KumonDataCtrl.SF_STATUS_GRADEREADY
							|| mQuestionControl.Status == KumonDataCtrl.SF_STATUS_GRADING) {

						// 採点待ち
						mMDT_Mode = MdtMode.DisplayMark;
						mMarkControl.showPage(mSideIndex, mMDT_Mode,
								mLearningMode, mMDT_TestMark,
								mQuestionControl.TrialCount, false, true);
					} else {
						mMDT_Mode = MdtMode.DisplayMark;
						mMarkControl.showPage(mSideIndex, mMDT_Mode,
								mLearningMode, mMDT_TestMark,
								mQuestionControl.TrialCount, true, true);
					}
					mResultDataList.get(mPageIndex).mIsLearning = KumonDataCtrl.SF_LEARNING_NO;
				}
			}
			mMarkControl.setSoundIcon(mSideIndex, layout, context);

		} catch (Exception e) {
			SLog.DB_AddException(e);
			return false;
		}
		finally {
			logger.dumpToLog();
		}
		return true;
	}

	public boolean InitializeQuestionControlGrade(int page, boolean isTop,
			MdtMode mode, RelativeLayout layout, Context context,
			OnClickListener listener) {
		try {
			mBShowTopQuestionData = false;

			mPageIndex = page;
			DResultData resultData = mResultDataList.get(mPageIndex);
			if (mQuestionControl == null) {
				mQuestionControl = new QuestionControl();
			}
			mQuestionControl.SetResultData(resultData);

			// ページバーの色設定
			setPageBarColor(resultData);

			// SetUp
			loadDataFromPrintData(resultData);
			setDataForView(resultData);
			resultData.InitPageWork(mMDT_TestMark.getPageCount());
			mMarkControl.setManualMark(resultData.ManualMarked);

			mSideIndex = isTop ? 0 : (int) getSideNumberMax();

			// 対応方法：採点データがあれば、自動採点を止めて、
			boolean bCancle = false;
			ArrayList<Integer> trials = mMDT_TestMark.GetTestTrialsByResult();
			for (int i = 0; i < trials.size(); i++) {
				if (trials.get(i) == mQuestionControl.TrialCount - 1) {
					bCancle = true;
				}
			}
			if (!bCancle) {
				// 主導採点の前に自動採点
				// なければ　データを追加する　０回は全部正しい
				// Android4.4対応
				// DoSemiAutoMark(mQuestionControl.TrialCount-1);
				KRecognizer recognizer = new KRecognizer();
				recognizer.SetDictionaryPath(StudentClientCommData
						.getDictionaryFolder().toString());
				recognizer.DoSemiAutoMark(mMDT_TestMark, mMarkControl,
						mQuestionControl.TrialCount - 1);
				mMDT_TestMark
						.AddQuesesMarkData(mQuestionControl.TrialCount - 1);
			}
			// 採点前の状態に戻る
			for (int i = 0; i < mMDT_TestMark.getPageCount(); i++) {
				// ページ変更前に　ManualMarkedをtrueにする
				// mMDT_TestMark.mPageMarks.get(i).ManualMarked = false;
				mMDT_TestMark.AutoMarked = false;
			}

			mMDT_Mode = mode;
			mMarkControl
					.setGradingMethod(mQuestionControl.QuestionGradingMethod);
			mMarkControl.showPage(mSideIndex, mMDT_Mode, mLearningMode,
					mMDT_TestMark, mQuestionControl.TrialCount, true, true);
			mMarkControl.setSoundIcon(mSideIndex, layout, context);
			mMarkControl.setGradeIcon(mSideIndex, layout, context, listener);

		} catch (Exception e) {
			SLog.DB_AddException(e);
			return false;
		}
		return true;
	}

	// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
	// public boolean InitializeQuestionControlTopQuestion(int sideIndex) {
	public boolean InitializeQuestionControlTopQuestion(int page, int sideIndex) {
		// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
		// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
		// if(mTopQuestionData == null) return false;
		if (mTopQuestionDataList == null || mTopQuestionDataList.size() < page)
			return false;
		// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
		try {
			mBShowTopQuestionData = true;
			// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
			// mPageIndex = -1;
			// DResultData resultData = mTopQuestionData;
			mPageIndex = page;
			DResultData resultData = mTopQuestionDataList.get(page);
			mStudyIndicator.SetCurrent(resultData.mPrintNo, sideIndex);
			// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
			resultData.mCount = -1;
			resultData.mLearningPlace = KumonDataCtrl.SF_LEARNINGPLACE_HOME;
			// ページバーの色設定
			setPageBarColor(resultData);
			// SetUp
			// MDTData
			loadDataFromPrintData(resultData);
			mMarkControl.setMdtData(mMDT_MdtData);
			// BackImageData
			int len = 0;
			if (resultData.mQuestion.mImageList != null) {
				len = resultData.mQuestion.mImageList.size();
			}
			byte[][] image = new byte[len][];
			for (int i = 0; i < len; i++) {
				image[i] = resultData.mQuestion.mImageList.get(i).mImage;
			}
			mMarkControl.setMdtDataImage(image);

			mSideIndex = sideIndex;
			mMDT_Mode = MdtMode.TopQuestion;
			mMarkControl.showPage(mSideIndex, mMDT_Mode, -1, null, -1, false,
					false);
		} catch (Exception e) {
			SLog.DB_AddException(e);
			return false;
		}
		return true;
	}

	// / <summary>
	// / 学習対象の問題かどうかを返す
	// / </summary>
	// / <param name="questionControl">問題コントロール</param>
	// / <returns>学習対象かどうか</returns>
	private boolean IsLearnAble(QuestionControl questionControl) {
		// 100点
		// 制限回数Over
		// 採点待ち
		// 未送信
		if (questionControl.Score >= 100 // 満点
				|| questionControl.TrialCount >= questionControl.MaxTrialCount // 3回目の学習が終了済み
				|| questionControl.Status == KumonDataCtrl.SF_STATUS_GRADEREADY // 採点待ち
				|| questionControl.Status == KumonDataCtrl.SF_STATUS_GRADING // 採点中
				|| questionControl.Status == KumonDataCtrl.SF_STATUS_END // 完了
				|| questionControl.IsRegist == 1 // 採点待ち
				|| questionControl.IsLearned == 1 // 学習済み
		) {
			return false;
		}
		return true;
	}

	private boolean IsLearnAble(DResultData resultData) {

		// 20140704 MOD-S For Bug
		/***
		 * if (resultData.mScore >= 100 // 満点 || resultData.mCount >=
		 * resultData.mLimitCount // 3回目の学習が終了済み || resultData.mStatus ==
		 * KumonDataCtrl.SF_STATUS_GRADEREADY //採点待ち || resultData.mStatus ==
		 * KumonDataCtrl.SF_STATUS_GRADING //採点中 || resultData.mStatus ==
		 * KumonDataCtrl.SF_STATUS_END //完了 || resultData.mIsRegist == 1 //採点待ち
		 * || resultData.mIsLearned == 1 //学習済み ) { return false; } return true;
		 ***/
		if (resultData.mIsLearning == KumonDataCtrl.SF_LEARNING_NOCHECK) {
			if (resultData.mScore >= 100 // 満点
					|| resultData.mCount >= resultData.mLimitCount // 3回目の学習が終了済み
					|| resultData.mStatus == KumonDataCtrl.SF_STATUS_GRADEREADY // 採点待ち
					|| resultData.mStatus == KumonDataCtrl.SF_STATUS_GRADING // 採点中
					|| resultData.mStatus == KumonDataCtrl.SF_STATUS_END // 完了
					|| resultData.mIsRegist == 1 // 採点待ち
					|| resultData.mIsLearned == 1 // 学習済み
			) {
				return false;
			}
			return true;
		} else if (resultData.mIsLearning == KumonDataCtrl.SF_LEARNING_YES) {
			return true;
		} else {
			return false;
		}

		// 20140704 MOD-E For Bug
	}

	// StopWatch
	public void StartStopwatch() {
		// 休止中時間計測用のストップウォッチをスタート
		if (mSuspendLearningStopWatch[mPageIndex] == null) {
			mSuspendLearningStopWatch[mPageIndex] = new Stopwatch();
		}
		mSuspendLearningStopWatch[mPageIndex].Start();

		if (mMDT_Mode == MdtMode.Test) {
			if (mSuspendLearningStopWatch[mPageIndex] != null
					&& mSuspendLearningStopWatch[mPageIndex].IsRunning) {
				mSuspendLearningStopWatch[mPageIndex].Stop();
			}
		}
	}

	public void StopStopwatch() {
		if (mSuspendLearningStopWatch[mPageIndex] == null) {
			mSuspendLearningStopWatch[mPageIndex] = new Stopwatch();
		}
		mSuspendLearningStopWatch[mPageIndex].Stop();
	}

	public void StartRunningStopwatch() {
		// 休止中時間計測用のストップウォッチをスタート
		if (mSuspendLearningStopWatch[mPageIndex] == null) {
			mSuspendLearningStopWatch[mPageIndex] = new Stopwatch();
		}
		mSuspendLearningStopWatch[mPageIndex].Start();
	}

	public void StopRunningStopwatch() {
		if (mSuspendLearningStopWatch[mPageIndex] != null
				&& mSuspendLearningStopWatch[mPageIndex].IsRunning) {
			mSuspendLearningStopWatch[mPageIndex].Stop();
		}
	}

	// Property
	public int getSideIndex() {
		return mSideIndex;
	}

	public int getSideNumberMax() {
		return mMDT_TestMark.getPageCount() - 1;
	}

	public int getPageIndex() {
		return mPageIndex;
	}

	public int getMaxPageIndex() {
		return mResultDataList.size() - 1;
	}

	public int getLearningMode() {
		return mLearningMode;
	}

	public MdtMode getMode() {
		return mMDT_Mode;
	}

	// ForPen
	public void setPenKind(int value) {
		mMarkControl.setPenKind(value);
	}

	public int getPenWidth() {
		return mMarkControl.getPenWidth();
	}

	public void setPenWidth(int value) {
		mMarkControl.setPenWidth(value);
	}

	public float getEraserWidth() {
		return mMarkControl.getEraserWidth();
	}

	public void setEraserWidth(float value) {
		mMarkControl.setEraserWidth(value);
	}

	// Button Event
	public void onClickRest() {

		SleepTest();

		// 休止中時間計測用のストップウォッチをスタート
		if (mSuspendLearningStopWatch != null) {
			if (mSuspendLearningStopWatch[mPageIndex] == null) {
				mSuspendLearningStopWatch[mPageIndex] = new Stopwatch();
			}
			mSuspendLearningStopWatch[mPageIndex].Start();
		}
	}

	public void onClickStopRest() {
		if (mBShowTopQuestionData == false) {
			mMDT_Mode = MdtMode.Test;
			if (mSuspendLearningStopWatch != null) {
				if (mSuspendLearningStopWatch[mPageIndex] == null) {
					mSuspendLearningStopWatch[mPageIndex] = new Stopwatch();
				}
				mSuspendLearningStopWatch[mPageIndex].Stop();
			}
		}
	}

	public boolean DoOnClickSkipBackDone(RelativeLayout layout, Context context) {
		boolean pagechange = false;
		mMarkControl.REPLAY_TouchEnd();
		if (mPageIndex == 0) {
			ShowFirstPage();
			mMarkControl.setSoundIcon(mSideIndex, layout, context);
		} else {
			// 20140915 MOD-S For Bug
			// InitializeQuestionControl(0, true, true, layout, context);
			InitializeQuestionControl(0, true, true, layout, context, 0);
			// 20140915 MOD-E For Bug
			pagechange = true;
		}
		return pagechange;
	}

	public boolean DoOnClickBack(RelativeLayout layout, Context context) {
		boolean pagechange = false;
		// 20141208 MOD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
		String msg = "[DoOnClickBack] (START) (mSideIndex = " + mSideIndex
				+ ")";
		KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
				mPageIndex, msg);
		// 20141208 MOD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

		mMarkControl.REPLAY_TouchEnd();
		try {
			if (mBShowTopQuestionData) {
				// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
				/***
				 * if(mSideIndex > 0) { mSideIndex = 0;
				 * mMarkControl.movePage(mSideIndex); }
				 ***/

				if (mSideIndex > 0) {
					PageBack();
				} else {
					if (mPageIndex > 0) {
						InitializeQuestionControlTopQuestion(mPageIndex - 1, 1);
					}
				}
				// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
				return false;
			}

			if (mSideIndex > 0) {
				PageBack();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
			} else {
				// 現在表示している問題のデータの保存
				if (mMDT_Mode == MdtMode.Test) {
					StopRunningStopwatch();
					// プリントを切り替える度にテスト終了(自動採点は採点も行う)した結果を保持する。
					// ※画面遷移時にまとめて終了状態にすると、問題データ読み込みを学習枚数分行うことになり、処理時間がかかりすぎるため
					SaveCurrentPrintLearningData();

					// 休止中時間計測用のストップウォッチをスタート
					StartRunningStopwatch();
				}

				//
				// 前の問題コントロールを表示
				//
				if (mPageIndex > 0) {
					// 20140915 MOD-S For Bug
					// InitializeQuestionControl(mPageIndex - 1, false, false,
					// layout, context);
					InitializeQuestionControl(mPageIndex - 1, false, false,
							layout, context, 0);
					// 20140915 MOD-E For Bug
					pagechange = true;
				}
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		return pagechange;
	}

	public boolean DoOnClickBackDone(RelativeLayout layout, Context context) {
		boolean pagechange = false;
		mMarkControl.REPLAY_TouchEnd();
		try {
			if (mSideIndex > 0) {
				PageBack();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
			} else {
				if (mPageIndex > 0) {
					// 20140915 MOD-S For Bug
					// InitializeQuestionControl(mPageIndex - 1, false, true,
					// layout, context);
					InitializeQuestionControl(mPageIndex - 1, false, true,
							layout, context, 0);
					// 20140915 MOD-E For Bug
					pagechange = true;
				}
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		return pagechange;
	}

	public void DoOnClickBackGrade(RelativeLayout layout, Context context,
			OnClickListener listener) {
		mMarkControl.REPLAY_TouchEnd();
		try {
			// 前ページを採点後の状態にする
			for (int i = 0; i < mMDT_TestMark.getPageCount(); i++) {
				if (mMDT_TestMark.mPageMarks.get(i).getMdtPageNumber() == mSideIndex) {
					mMarkControl.SaveManualMark(mSideIndex);
				}
			}
			if (mSideIndex > 0) {
				PageBack();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
				mMarkControl
						.setGradeIcon(mSideIndex, layout, context, listener);
			} else {
				SaveCurrentPrintGradingData();
				if (mPageIndex > 0) {
					InitializeQuestionControlGrade(mPageIndex - 1, false,
							mMDT_Mode, layout, context, listener);
				}
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public boolean refreshPage(RelativeLayout layout, Context context) {
		boolean pagechange = false;
		mMarkControl.REPLAY_TouchEnd();

		// 20141208 MOD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
		String msg = "[refreshPage] (START) (mSideIndex = " + mSideIndex
				+ ")";
		KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
				mPageIndex, msg);
		// 20141208 MOD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

		try {
			
			if(mSideIndex == 0) {
				InitializeQuestionControl(mPageIndex, true, false, layout, context, 0);
			}else {
				InitializeQuestionControl(mPageIndex, false, false, layout, context, 0);
			}
			
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		return pagechange;

	}
	
	public boolean DoOnClickNext(RelativeLayout layout, Context context) {
		boolean pagechange = false;
		mMarkControl.REPLAY_TouchEnd();

		// 20141208 MOD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
		String msg = "[DoOnClickNext] (START) (mSideIndex = " + mSideIndex
				+ ")";
		KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
				mPageIndex, msg);
		// 20141208 MOD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

		try {
			if (mBShowTopQuestionData) {
				// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
				/***
				 * if(mSideIndex == 0) { mSideIndex++;
				 * mMarkControl.movePage(mSideIndex); }
				 ***/
				if (mSideIndex == 0) {
					PageNext();
				} else {
					InitializeQuestionControlTopQuestion(mPageIndex + 1, 0);
				}
				// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
				return false;
			}

			if (getSideNumberMax() > mSideIndex) {
				PageNext();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
			} else {
				// 現在表示しているプリントのインクデータ・採点結果データの保存
				if (mMDT_Mode == MdtMode.Test) {
					StopRunningStopwatch();

					// プリントを切り替える度にテスト終了(自動採点は採点も行う)した結果を保持する。
					// ※画面遷移時にまとめて終了状態にすると、問題データ読み込みを学習枚数分行うことになり、処理時間がかかりすぎるため
					SaveCurrentPrintLearningData();

					// 休止中時間計測用のストップウォッチをスタート
					StartRunningStopwatch();
				}

				//
				// 次の問題コントロール
				//
				if (mPageIndex + 1 <= getMaxPageIndex()) {
					// 20140915 MOD-E For Bug
					// InitializeQuestionControl(mPageIndex + 1, true, false,
					// layout, context);
					InitializeQuestionControl(mPageIndex + 1, true, false,
							layout, context, 0);
					// 20140915 MOD-E For Bug
					pagechange = true;
				}
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		return pagechange;

	}

	public boolean DoOnClickNextDone(RelativeLayout layout, Context context) {
		boolean pagechange = false;
		mMarkControl.REPLAY_TouchEnd();
		try {
			if (getSideNumberMax() > mSideIndex) {
				PageNext();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
			} else {
				if (mPageIndex + 1 <= getMaxPageIndex()) {
					// 20140915 MOD-S For Bug
					// InitializeQuestionControl(mPageIndex + 1, true, true,
					// layout, context);
					InitializeQuestionControl(mPageIndex + 1, true, true,
							layout, context, 0);
					// 20140915 MOD-E For Bug
					pagechange = true;
				}
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		return pagechange;

	}

	public void DoOnClickNextGrade(RelativeLayout layout, Context context,
			OnClickListener listener) {
		mMarkControl.REPLAY_TouchEnd();

		try {
			// 前ページを採点後の状態にする
			for (int i = 0; i < mMDT_TestMark.getPageCount(); i++) {
				if (mMDT_TestMark.mPageMarks.get(i).getMdtPageNumber() == mSideIndex) {
					mMarkControl.SaveManualMark(mSideIndex);
				}
			}

			if (getSideNumberMax() > mSideIndex) {
				PageNext();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
				mMarkControl
						.setGradeIcon(mSideIndex, layout, context, listener);
			} else {
				SaveCurrentPrintGradingData();
				if (mPageIndex + 1 <= getMaxPageIndex()) {
					InitializeQuestionControlGrade(mPageIndex + 1, true,
							mMDT_Mode, layout, context, listener);
				}
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}

	}

	public boolean DoOnClickSkipNextDone(RelativeLayout layout, Context context) {
		boolean pagechange = false;
		mMarkControl.REPLAY_TouchEnd();
		try {
			if (mPageIndex >= getMaxPageIndex()) {
				ShowLastPage();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
			} else {
				// 20140915 MOD-S For Bug
				// InitializeQuestionControl(getMaxPageIndex(), false, true,
				// layout, context);
				InitializeQuestionControl(getMaxPageIndex(), false, true,
						layout, context, 0);
				// 20140915 MOD-E For Bug
				pagechange = true;
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		return pagechange;
	}

	public void DoOnClickInBack() {
		mMarkControl.REPLAY_TouchEnd();
		try {
			mStudyIndicator.DoOnClickInBack();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void DoOnClickInNext() {
		mMarkControl.REPLAY_TouchEnd();
		try {
			mStudyIndicator.DoOnClickInNext();
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public boolean DoOnClickIndicator(int pos, RelativeLayout layout,
			Context context) {
		boolean pagechange = false;
		mMarkControl.REPLAY_TouchEnd();
		try {
			int pageidx = mStudyIndicator.getPageIndex(pos);
			int sideidx = mStudyIndicator.getSideIndex(pos);

			// 20141208 MOD-S For DebugLog 初回学習時に、Countが２回になってしまう原因調査用
			String msg = "[DoOnClickIndicator] (START) (sideidx = " + sideidx
					+ ")";
			KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
					pageidx, msg);
			// 20141208 MOD-E For DebugLog 初回学習時に、Countが２回になってしまう原因調査用

			if (pageidx < 0) {
				// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
				// InitializeQuestionControlTopQuestion(0);

				pageidx = mStudyIndicator.getRefPageIndex(pos);
				if (mBShowTopQuestionData == true) {
					// 参照問題表示中
					if (mPageIndex == pageidx) {
						if (mSideIndex == sideidx) {
							// 現在ページ
						} else {
							if (sideidx == 0) {
								PageBack();
							} else {
								PageNext();
							}
						}
					} else {
						InitializeQuestionControlTopQuestion(pageidx, sideidx);
					}
				} else {
					// 通常テストからの遷移
					if (mMDT_Mode == MdtMode.Test) {
						StopRunningStopwatch();
						SaveCurrentPrintLearningData();
					}
					InitializeQuestionControlTopQuestion(pageidx, sideidx);
				}
				// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
				return false;
			}
			// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
			// if(mPageIndex == pageidx) {
			if (mBShowTopQuestionData == false && mPageIndex == pageidx) {
				// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
				if (mSideIndex == sideidx) {
					// 現在ページ
				} else {
					if (sideidx == 0) {
						PageBack();
						mMarkControl.setSoundIcon(mSideIndex, layout, context);
					} else {
						PageNext();
						mMarkControl.setSoundIcon(mSideIndex, layout, context);
					}
				}
			} else {
				if (pageidx >= 0 && pageidx <= getMaxPageIndex()) {
					// 現在表示しているプリントのインクデータ・採点結果データの保存
					if (mMDT_Mode == MdtMode.Test) {
						StopRunningStopwatch();

						// プリントを切り替える度にテスト終了(自動採点は採点も行う)した結果を保持する。
						// ※画面遷移時にまとめて終了状態にすると、問題データ読み込みを学習枚数分行うことになり、処理時間がかかりすぎるため
						SaveCurrentPrintLearningData();

						// 休止中時間計測用のストップウォッチをスタート
						StartRunningStopwatch();
					}
					boolean istop = false;
					if (sideidx == 0) {
						istop = true;
					}
					// 20140915 MOD-S For Bug
					// InitializeQuestionControl(pageidx, istop, false, layout,
					// context);
					InitializeQuestionControl(pageidx, istop, false, layout,
							context, 0);
					// 20140915 MOD-E For Bug
					pagechange = true;
				}
			}
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
		return pagechange;
	}

	// 20150413 ADD-E For 2015年度Ver. 未読対応
	public void DoOnClickMemoPageBack(RelativeLayout layout, Context context) {
		mMarkControl.REPLAY_TouchEnd();
		int index = getMemoPageBackIndex();
		if (index >= 0) {
			InitializeQuestionControl(index, true, true, layout, context, 0);
		}
	}

	public void DoOnClickMemoPageNext(RelativeLayout layout, Context context) {
		mMarkControl.REPLAY_TouchEnd();
		int index = getMemoPageNextIndex();
		if (index >= 0) {
			InitializeQuestionControl(index, true, true, layout, context, 0);
		}
	}

	// 20150413 ADD-E For 2015年度Ver. 未読対応

	public boolean getGradeFinishStatus() {
		// 最終ページ B面のみ
		boolean ret = false;
		if (mPageIndex >= getMaxPageIndex()) {
			// 最終ページの場合
			if (mSideIndex >= getSideNumberMax()) {
				ret = true;
			}
		}
		return ret;
	}

	public boolean getBackStatus() {
		boolean ret = true;
		if (mBShowTopQuestionData) {
			// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
			/***
			 * if(mSideIndex == 0) { ret = false; }
			 ***/
			if (mSideIndex <= 0 && mPageIndex <= 0) {
				ret = false;
			}
			// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
		} else {
			if (mSideIndex <= 0 && mPageIndex <= 0) {
				ret = false;
			}
		}
		return ret;
	}

	public boolean getNextStatus() {
		boolean ret = true;
		if (mBShowTopQuestionData) {
			// 20150126 MOD-S 2015年度Ver. 参照ページを増やす
			/***
			 * if(mSideIndex > 0) { ret = false; }
			 ***/
			if (mSideIndex >= 1
					&& mPageIndex >= mTopQuestionDataList.size() - 1) {
				ret = false;
			}
			// 20150126 MOD-E 2015年度Ver. 参照ページを増やす
		} else {
			if (mSideIndex >= getSideNumberMax()
					&& mPageIndex >= getMaxPageIndex()) {
				ret = false;
			}
		}
		return ret;
	}

	public boolean getSkipBackStatusDone() {
		boolean existsSkipTarget = false;

		// スキップ(前へ)ボタン
		if (mPageIndex == 0 && mSideIndex == 0) {
			existsSkipTarget = false;
		} else {
			existsSkipTarget = true;
		}
		return existsSkipTarget;
	}

	public boolean getSkipNextStatusDone() {

		boolean existsSkipTarget = false;
		if (mPageIndex >= getMaxPageIndex() && mSideIndex >= getSideNumberMax()) {
			existsSkipTarget = false;
		} else {
			existsSkipTarget = true;
		}
		return existsSkipTarget;
	}

	// 20150413 ADD-S For 2015年度Ver. 未読対応
	public int getMemoPageBackIndex() {
		int exist = -1;
		for (int i = mPageIndex - 1; i >= 0; i--) {

			DResultData result = mResultDataList.get(i);

			String soundcomment;
			if (mLearningMode == KumonDataCtrl.SF_DATATYPE_WEBVIEW) {
				soundcomment = TblSoundCommentData.DB_GetSoundCommentFileName(
						mCurrentUser.mStudentID, result.mKyozaiID,
						result.mPrintUnitID, 1);
			} else {
				soundcomment = TblSoundCommentData.DB_GetSoundCommentFileName(
						mCurrentUser.mStudentID, result.mKyozaiID,
						result.mPrintUnitID, 0);
			}
			if (CInkMain.HaveMemoStroke(result.mTagComment)
					|| TextUtils.isEmpty(result.mTagText) == false
					|| (soundcomment != null && soundcomment.length() > 0)) {
				exist = i;
				break;
			}
		}
		return exist;
	}

	public int getMemoPageNextIndex() {
		int exist = -1;
		for (int i = mPageIndex + 1; i < mResultDataList.size(); i++) {

			DResultData result = mResultDataList.get(i);

			String soundcomment;
			if (mLearningMode == KumonDataCtrl.SF_DATATYPE_WEBVIEW) {
				soundcomment = TblSoundCommentData.DB_GetSoundCommentFileName(
						mCurrentUser.mStudentID, result.mKyozaiID,
						result.mPrintUnitID, 1);
			} else {
				soundcomment = TblSoundCommentData.DB_GetSoundCommentFileName(
						mCurrentUser.mStudentID, result.mKyozaiID,
						result.mPrintUnitID, 0);
			}
			if (CInkMain.HaveMemoStroke(result.mTagComment)
					|| TextUtils.isEmpty(result.mTagText) == false
					|| (soundcomment != null && soundcomment.length() > 0)) {
				exist = i;
				break;
			}
		}
		return exist;

	}

	public int getUnreadStatus() {
		int unread = 0;
		if (mPageIndex >= 0 && mPageIndex < mResultDataList.size()) {
			DResultData result = mResultDataList.get(mPageIndex);
			unread = result.mCommentUnreadFlg;
		}
		return unread;
	}

	public void UpdateUnreadFlg() {
		mResultDataList.get(mPageIndex).mCommentUnreadFlg = 0;
		DResultData resultdata = mResultDataList.get(mPageIndex);
		DataDBIO.DB_UpdateReadCommentData(resultdata);
	}

	// 20150413 ADD-E For 2015年度Ver. 未読対応

	// *********************************************************************************************************
	private void loadDataFromPrintData(DResultData resultData) {
		if (mMDT_MdtData != null) {
			mMDT_MdtData = null;
		}
		mMDT_MdtData = LoadMdtData(resultData.mQuestion.mMDTData);
		if (mMDT_TestMark != null) {
			mMDT_TestMark = null;
		}
		mMDT_TestMark = LoadTestDatas(mMDT_MdtData, resultData);

		mQuestionControl.GradingResultData = resultData.mGradingResultData;
		mQuestionControl.InkData = resultData.mInkData;
		// 20150416 ADD-S InkData To Binary
		mQuestionControl.InkBinary = resultData.mInkBinary;
		// 20150416 ADD-E InkData To Binary
		mQuestionControl.RedComment = resultData.mRedComment;
		mQuestionControl.TagComment = resultData.mTagComment;
		mQuestionControl.TagText = resultData.mTagText;
	}

	private void setDataForView(DResultData resultData) {

		// MDTData
		mMarkControl.setMdtData(mMDT_MdtData);

		// InkData
		// 20150416 MOD-S InkData To Binary
		// mMarkControl.SetInkData(resultData.mInkData);
		if (resultData.mInkBinary != null && resultData.mInkBinary.length > 0) {
			mMarkControl.SetInkBinaryData(resultData.mInkBinary);
		} else {
			mMarkControl.SetInkData(resultData.mInkData);
		}
		// 20150416 MOD-E InkData To Binary
		mMarkControl.SetRedComment(resultData.mRedComment);

		// BackImageData
		int len = 0;
		if (resultData.mQuestion.mImageList != null) {
			len = resultData.mQuestion.mImageList.size();
		}
		byte[][] image = new byte[len][];
		for (int i = 0; i < len; i++) {
			image[i] = resultData.mQuestion.mImageList.get(i).mImage;
		}
		mMarkControl.setMdtDataImage(image);

		mMarkControl.setMdtDataSound(resultData.mQuestion.mSoundList);

		// 20140704 ADD-S For 点数表示時の矢印
		mMarkControl.setlimitCnt(resultData.mLimitCount);
		// 20140704 ADD-E

		// 20140731 ADD-S For 録音対応
		// 20150310 MOD-S For Web音声データ展開
		File recordolder;
		if (mLearningMode == KumonDataCtrl.SF_DATATYPE_WEBVIEW) {
			recordolder = StudentClientCommData.getRecordFolder(
					resultData.mStudentID, resultData.mKyozaiID,
					resultData.mPrintUnitID, 1);
		} else {
			recordolder = StudentClientCommData.getRecordFolder(
					resultData.mStudentID, resultData.mKyozaiID,
					resultData.mPrintUnitID, 0);
		}
		// 20150310 MOD-E For Web音声データ展開
		mMarkControl.setRecordFolder(recordolder);
		// 20140731 ADD-E For 録音対応

		// 20140731 ADD-S For Memo
		SetMemoBtnStaus(resultData);
		// 20140731 ADD_E For Memo
	}

	// / <summary>
	// / 問題データの読み込み
	// / </summary>
	// / <param name="questionData">問題データ</param>
	private MdtData LoadMdtData(byte[] mdtdata) {
		MdtData mdt_MdtData = null;
		try {
			mdt_MdtData = IO.JsonToMdtData(new String(mdtdata, "UTF-8"));
		} catch (Exception e) {
			mdt_MdtData = null;
		}
		return mdt_MdtData;
	}

	public MdtTestMarkData LoadTestDatas(MdtData mdtData, DResultData resultData) {
		MdtTestMarkData testmark = null;
		;

		if (resultData.mGradingResultData != null
				&& resultData.mGradingResultData.isEmpty() == false) {
			try {
				testmark = MdtTestMarkData
						.LoadFromJson(resultData.mGradingResultData);
			} catch (Exception e) {
				testmark = null;
			}
			if (testmark == null) {
			} else {
				mMarkControl.ConnectMdtDataAndMarkData(mdtData, testmark);
			}
		} else {
			testmark = new MdtTestMarkData(mdtData);
		}
		return testmark;
	}

	public void SetDrawingInk(Boolean drawingInk) {
		mMarkControl.SetDrawingInk(drawingInk);
	}

	public void setPenColor(int color) {
		mMarkControl.setPenColor(color);
	}

	public void setPenAlpha(int alpha) {
		mMarkControl.setPenAlpha(alpha);
	}

	// =======================================================================================================
	public void StartTest(int pageNumber) {

		if (mTestTimes[pageNumber] == null) {
			mTestTimes[pageNumber] = new TestTime();
		}
	}

	// / <summary>
	// / 問題の解答を一時中断します。
	// / </summary>
	public void SleepTest() {
		if (mMDT_Mode == MdtMode.TestSleep) {
			return;
		}
		mMDT_Mode = MdtMode.TestSleep;
	}

	// / <summary>
	// / 問題の解答を終了します。
	// / </summary>
	public void EndTest() {
		mMDT_Mode = MdtMode.TestEnd;
	}

	// / <summary>
	// / インクデータのBase64文字列を圧縮したbyte配列の取得
	// / </summary>
	// / <returns>インクデータ文字列を圧縮したバイト配列</returns>
	public String GetInkData() {
		return mMarkControl.SaveInkToJson();
	}

	// 20150416 ADD-S InkData To Binary
	public byte[] GetInkBinaryData() {
		return mMarkControl.SaveInkBinary();
	}

	// 20150416 ADD-E InkData To Binary
	public String GetRedComment() {
		return mMarkControl.SaveRedCommentToJson();
	}

	// / <summary>
	// / 採点結果データの文字列を取得
	// / </summary>
	// / <returns>採点結果データの文字列を圧縮したバイト配列</returns>
	public String GetMarkData() {
		String ret = "";
		try {
			ret = MdtTestMarkData.SaveToJson(mMDT_TestMark);
		} catch (Exception e) {
		}
		return ret;
	}

	// / <summary>
	// / 点数の取得
	// / </summary>
	// / <returns>点数</returns>
	public int GetScore() {
		int ret = 0;

		ret = mMDT_TestMark.GetScore(mQuestionControl.TrialCount - 1);

		// 未採点の場合は0点で登録
		// ※0点は画面上表示しないため、採点の結果0点になった場合と区別できる必要はない
		return (ret < 0) ? 0 : ret;
	}

	// / <summary>
	// / 音声再生停止情報のインクデータへの埋め込み
	// / </summary>
	public void SoundFinished() {
		// KYON _uctInkMarking.soundFinished();
	}

	// / <summary>
	// / 次のページ表示
	// / </summary>
	public void PageNext() {
		int pageNo = mSideIndex + 1;

		if (pageNo <= getSideNumberMax()) {
			mSideIndex++;
			mMarkControl.movePage(mSideIndex);
			mStudyIndicator.MovePageSide(mSideIndex);
		}
	}

	// / <summary>
	// / 前のページ
	// / </summary>
	public void PageBack() {
		int pageNo = mSideIndex - 1;

		if (pageNo >= 0) {
			mSideIndex--;
			mMarkControl.movePage(mSideIndex);
			mStudyIndicator.MovePageSide(mSideIndex);
		}
	}

	// / <summary>
	// / 最初のページ
	// / </summary>
	public void ShowFirstPage() {
		mSideIndex = 0;
		mMarkControl.movePage(mSideIndex);
		mStudyIndicator.MovePageSide(mSideIndex);
	}

	// / <summary>
	// / 最後のページ
	// / </summary>
	public void ShowLastPage() {
		mSideIndex = getSideNumberMax();
		mMarkControl.movePage(mSideIndex);
		mStudyIndicator.MovePageSide(mSideIndex);
	}

	public void GradeIconClick(int idx, int pos) {
		mMarkControl.GradeIconClick(idx, pos);
	}

	private void setPageBarColor(DResultData resultdata) {
		boolean shcool = false;
		if (resultdata.mCount == 0) {
			// 20140917 MOD-S For 未学習が赤くなる
			// if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT ||
			// mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY) {
			if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT
					|| mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY
					|| mLearningMode == KumonDataCtrl.SF_DATATYPE_WEBVIEW) {
				// 20140917 MOD-E For 未学習が赤くなる
			} else {
				shcool = true;
			}
		} else {
			if (resultdata.mLearningPlace == KumonDataCtrl.SF_LEARNINGPLACE_SCHOOL) {
				shcool = true;
			}
		}
		if (shcool) {
			if (mPagebar != null) {
				mPagebar.setBackgroundColor(SF_BAR_RED);
			}
			if (mRredpen != null) {
				mRredpen.setBackgroundColor(SF_BAR_RED);
			}
		} else {
			if (mPagebar != null) {
				mPagebar.setBackgroundColor(SF_BAR_BLUE);
			}
			if (mRredpen != null) {
				mRredpen.setBackgroundColor(SF_BAR_BLUE);
			}
		}
	}

	// NEW_VER MOD-S
	/***
	 * public void setPenThreadMode(boolean mode) {
	 * mMarkControl.setPenThreadMode(mode); }
	 ***/
	public void setPenEnabled(boolean mode) {
		mMarkControl.setPenEnabled(mode);
	}

	// NEW_VER MOD-E

	// ****************************************************************************************************
	// *** インク再生用
	// ****************************************************************************************************
	public void REPLAY_Start(final PenPlayBackCallback callback) {
		mMarkControl.REPLAY_Start(callback);
	}

	public void REPLAY_End() {
		mMarkControl.REPLAY_End();
	}

	public long REPLAY_GetPlayBackTime() {
		return mMarkControl.GetPlayBackTime();
	}

	public void REPLAY_Stop() {
		mMarkControl.REPLAY_Stop();
	}

	public void REPLAY_Pause() {
		mMarkControl.REPLAY_Pause();
	}

	public void REPLAY_Play(int mode) {
		mMarkControl.REPLAY_Play(mode);
	}

	public void REPLAY_Back() {
		mMarkControl.REPLAY_Back();
	}

	public void REPLAY_Next() {
		mMarkControl.REPLAY_Next();
	}

	public boolean REPLAY_BackBtnStatus() {
		return mMarkControl.REPLAY_BackBtnStatus();
	}

	public boolean REPLAY_NextBtnStatus() {
		return mMarkControl.REPLAY_NextBtnStatus();
	}

	// 20140731 ADD-S For 録音対応
	// ****************************************************************************************************
	// *** 音声録音用
	// ****************************************************************************************************
	public void setRecordCallBack(final RecordCallback callback, String loginID) {
		mMarkControl.setRecordCallBack(callback, loginID);
	}

	public void REC_Recording_Start(int pos) {
		mMarkControl.REC_Recording_Start(pos);
	}

	public void REC_Recording_Stop() {
		mMarkControl.REC_Recording_Stop();
	}

	public void REC_Play_Start(boolean recordable, int pos) {
		mMarkControl.REC_Play_Start(recordable, pos);
	}

	public void REC_Play_Stop() {
		mMarkControl.REC_Play_Stop();
	}

	public void REC_Play_ReStart() {
		mMarkControl.REC_Play_ReStart();
	}

	public void REC_Play_Pause() {
		mMarkControl.REC_Play_Pause();
	}

	// 20140805 ADD-S For 録音
	// 20150115 MOD-S 再生時もダイアログ表示
	/***
	 * public int REC_Get_Duration(int pos) { return
	 * mMarkControl.REC_Get_Duration(pos); }
	 ***/
	public int REC_Get_Duration(boolean recordable, int pos) {
		return mMarkControl.REC_Get_Duration(recordable, pos);
	}

	// 20150115 MOD-E 再生時もダイアログ表示
	public boolean REC_Get_CanRecordStatus(int pos) {
		return mMarkControl.REC_Get_CanRecordStatus(pos);
	}

	public boolean REC_Get_CanPlay(int pos) {
		return mMarkControl.REC_Get_CanPlay(pos);
	}

	// 20140805 ADD-E For 録音

	// 20140731 ADD-S For Memo
	// ****************************************************************************************************
	// *** 指導者メモ用
	// ****************************************************************************************************
	private ImageButton mImageBtnMemo = null;

	public void SetMemoBtn(ImageButton btn) {
		mImageBtnMemo = btn;
	}

	public void SetMemoBtnStaus(DResultData resultData) {
		if (mImageBtnMemo != null) {

			// 20150210 MOD-S For 2015年度Ver. 音声メモ
			// if(CInkMain.HaveMemoStroke(resultData.mTagComment) ||
			// TextUtils.isEmpty(resultData.mTagText) == false) {

			// 20150310 MOD-S For Web音声データ展開
			// String soundcomment =
			// TblSoundCommentData.DB_GetSoundCommentFileName(mCurrentUser.mStudentID,
			// mCurrentUser.mCurrentKyozaiID, mQuestionControl.PrintUnitID);

			String soundcomment = GetSoundCommentFileName();
			// 20150310 MOD-E For Web音声データ展開
			if (CInkMain.HaveMemoStroke(resultData.mTagComment)
					|| TextUtils.isEmpty(resultData.mTagText) == false
					|| (soundcomment != null && soundcomment.length() > 0)) {
				// 20150210 MOD-E For 2015年度Ver. 音声メモ
				mImageBtnMemo.setImageResource(R.drawable.post_it_icon);
				mImageBtnMemo.setEnabled(true);
			} else {
				mImageBtnMemo.setImageResource(R.drawable.post_it_icon_off);
				mImageBtnMemo.setEnabled(false);
			}
		}
	}

	public String getTagComment() {
		if (mQuestionControl != null) {
			return mQuestionControl.TagComment;
		}
		return null;
	}

	public String getTagText() {
		if (mQuestionControl != null) {
			return mQuestionControl.TagText;
		}
		return null;
	}

	public boolean getOrientation() {
		return mMDT_MdtData.LandscapeOrientation;
	}

	// 20140731 ADD-E For Memo
	// 20150210 ADD-S For 2015年度Ver. 音声メモ
	/***
	 * public String getPrintUnitID() { if(mQuestionControl != null) { return
	 * mQuestionControl.PrintUnitID; } return null; }
	 ***/
	public String GetSoundCommentFileName() {
		String soundcomment = "";
		if (mLearningMode == KumonDataCtrl.SF_DATATYPE_WEBVIEW) {
			soundcomment = TblSoundCommentData.DB_GetSoundCommentFileName(
					mCurrentUser.mStudentID, mQuestionControl.KyozaiID,
					mQuestionControl.PrintUnitID, 1);
		} else {
			soundcomment = TblSoundCommentData.DB_GetSoundCommentFileName(
					mCurrentUser.mStudentID, mQuestionControl.KyozaiID,
					mQuestionControl.PrintUnitID, 0);
		}

		return soundcomment;
	}

	// 20150210 ADD-E For 2015年度Ver. 音声メモ

	// 20140731 ADD-S For 学習済み教材の表示は,PrintSet単位でＤＢ参照
	private void ClearLastPrintSetData(ArrayList<DResultData> resultdatalist,
			String printsetid) {
		for (int i = 0; i < resultdatalist.size(); i++) {
			DResultData resultdata = resultdatalist.get(i);
			if (resultdata.mPrintSetID.equalsIgnoreCase(printsetid)) {
				// インクデータクリア
				resultdata.mInkData = null;
				// 20150416 ADD-S InkData To Binary
				resultdata.mInkBinary = null;
				// 20150416 ADD-E InkData To Binary
				// 問題データクリア
				resultdata.mQuestion = null;
			}
		}
	}

	// 20140731 ADD-E For 学習済み教材の表示は,PrintSet単位でＤＢ参照
	// 20150212 ADD-S For 参照中は先頭ページへ遷移
	public int getIndicatorStartPage() {
		int pos = mStudyIndicator.getIndicatorStartPage();
		return pos;
	}

	// 20150212 ADD-E For 参照中は先頭ページへ遷移

	// 20150303 ADD-S For 2015年度Ver. 音声メモステータス
	private int checkSoundRecordStatus() {
		final int STAT_SOUNDRECORD_NO = 0; // 録音設定無し
		final int STAT_SOUNDRECORD_INVALID = 1; // 不適
		final int STAT_SOUNDRECORD_VALID = 2; // 適

		int status = STAT_SOUNDRECORD_NO;

		File recordolder = StudentClientCommData.getRecordFolder(
				mCurrentUser.mStudentID, mQuestionControl.KyozaiID,
				mQuestionControl.PrintUnitID, 0);

		PageData pdata = null;
		int page = mMDT_MdtData.PageDatas.size();
		for (int i = 0; i < page; i++) {
			pdata = mMDT_MdtData.PageDatas.get(i);
			int len = pdata.RecordDatas.size();
			for (int j = 0; j < len; j++) {
				if (status == STAT_SOUNDRECORD_NO) {
					status = STAT_SOUNDRECORD_VALID;
				}

				RecordData record = pdata.RecordDatas.get(j);
				String RecordFileName = RecordDataControl.makeRecordFileName(
						recordolder, record, mCurrentUser.mLoginID);
				File soundfile = new File(RecordFileName);
				if (soundfile.exists()) {
					int rectime = getSoundRecordDuration(soundfile
							.getAbsolutePath());
					if (record.ProperRangeMin > 0) {
						if (rectime < record.ProperRangeMin * 1000) {
							// 録音時間が短いので不適
							status = STAT_SOUNDRECORD_INVALID;
							break;
						}
					}
					if (record.ProperRangeMax > 0) {
						if (rectime > (record.ProperRangeMax + 2) * 1000) {
							// 録音時間が長いので不適
							status = STAT_SOUNDRECORD_INVALID;
							break;
						}
					}
				} else {
					// 録音していないので不適
					status = STAT_SOUNDRECORD_INVALID;
					break;
				}
			}
			if (status == STAT_SOUNDRECORD_INVALID) {
				break;
			}
		}

		return status;
	}

	private int getSoundRecordDuration(String RecordFileName) {
		int rectime = 0;

		MediaPlayer Player = null;
		try {
			Player = new MediaPlayer();
			Player.setDataSource(RecordFileName);
			Player.prepare();
			rectime = Player.getDuration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Player != null) {
			Player.reset();
			Player.release();
			Player = null;
		}
		return rectime;
	}

	// 20150303 ADD-E For 2015年度Ver. 音声メモステータス

}
