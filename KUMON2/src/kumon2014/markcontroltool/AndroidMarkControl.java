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
	// 20140731 ADD-S For �w�K�ς݋��ނ̕\����,PrintSet�P�ʂłc�a�Q��
	// ����A�C���N�f�[�^�͓ǂݍ��܂�
	// PrintSetID���ς������A�C���N�f�[�^�̂ݍēǂݍ���
	private String LastPrintSetID = "";
	// 20140731 ADD-E For �w�K�ς݋��ނ̕\����,PrintSet�P�ʂłc�a�Q��

	// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
	// private DResultData mTopQuestionData = null;
	private ArrayList<DResultData> mTopQuestionDataList = null;
	// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷

	public boolean mBShowTopQuestionData = false;

	// / ���R���g���[���z����C���f�b�N�X
	public int mPageIndex = 0;
	public int mSideIndex = 0;

	private CurrentUser mCurrentUser = null;
	private int mLearningMode = KumonDataCtrl.SF_DATATYPE_NONE;
	private int mNextPage = 0;
	// 20140915 DEL-S For Bug
	// private int mReStart = 0;
	// 20140915 DEL-E For Bug
	private MdtMode mMDT_Mode = MdtMode.None; // �񓚁A�̓_���[�h

	public boolean mGradingMethod_Instructer = false;
	public boolean mGradingMethod_Self = false;
	public boolean mGradingMethod_InstructerOnClient = false;

	private TableRow mPagebar = null;
	private Button mRredpen = null;

	private static final int SF_BAR_BLUE = Color.parseColor("#FF7DCEF4");
	private static final int SF_BAR_RED = Color.parseColor("#FFFFC0CB");

	public TestTime[] mTestTimes = null;
	private Stopwatch[] mSuspendLearningStopWatch = null;

	// For �����F��
	// private boolean m_bSemiAutoChangeManual = false;

	// 20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
	private ArrayList<String> mLogList = null;

	// 20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

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
		// ����Ƃ��A�j���Ƃ�
		mMarkControl = null;
		mQuestionControl = null;
	}

	// ������
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
					// 20150126 ADD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
					mBShowTopQuestionData = testsavedata.mBShowTopQuestionData;
					mSideIndex = testsavedata.mSideIndex;
					// 20150126 ADD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
				}
				// 20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
				if (mLogList == null) {
					mLogList = new ArrayList<String>();
				}
				mLogList.clear();
				KumonLog.AddAndroidLog(mLogList,
						mResultDataList.get(mPageIndex).mPrintUnitID,
						mPageIndex, "START Restart ");

				logger.addSplit("!restart KumonLog.AddAndroidLog");
				// 20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

			} else {
				if (mLearningMode == KumonDataCtrl.SF_DATATYPE_GRADESELF
						|| mLearningMode == KumonDataCtrl.SF_GRADEINSTRUCTORONCLIENT) {
					mResultDataList = DataDBIO
							.DB_GetGradePrintSet(mCurrentUser.mStudentID);
					logger.addSplit("restart DataDBIO.DB_GetGradePrintSet");
					bShowStudyIndicator = StudyIndicator.SF_MODE_NONE;
				} else {
					// 20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
					if (mLogList == null) {
						mLogList = new ArrayList<String>();
					}
					mLogList.clear();
					KumonLog.AddAndroidLog(mLogList, "XXXXXXXX", 0, "START ");
					// 20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

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
					// 20150409 ADD-S For 2015�N�xVer. ���ǃR�����g
					else if (mLearningMode == KumonDataCtrl.SF_DATATYPE_DONE_UNREAD) {
						mResultDataList = DataDBIO
								.DB_GetUnreadData(mCurrentUser.mStudentID);
						logger.addSplit("restart DataDBIO.DB_GetUnreadData");
						bShowStudyIndicator = StudyIndicator.SF_MODE_NONE;
					}
					// 20150409 ADD-S For 2015�N�xVer. ���ǃR�����g
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
				// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
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
				// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
			} else {
				// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
				// mTopQuestionData = null;
				mTopQuestionDataList = null;
				// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
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

		// �X�L�b�v���ꂽ�y�[�W�͍Ō�̎��Ԃ��Z�b�g
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
						// ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
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
							// ����
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
							// �ُ�
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
						// ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
					} else {
						resultdata.mIsLearning = KumonDataCtrl.SF_LEARNING_NO;
					}
				}
				if (resultdata.mIsLearning == KumonDataCtrl.SF_LEARNING_YES) { // �w�K�@���M�Ώ�
					resultdata.mPenThickness = mMarkControl.getPenWidth();
					// resultdata.mCount++;
					// ����w�K
					if (resultdata.mCount == 1) {
						resultdata.mPrintSetID = newPrintSetID;
						// �w�K�ꏊ
						if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT
								|| mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY) {
							resultdata.mLearningPlace = KumonDataCtrl.SF_LEARNINGPLACE_HOME;
						} else {
							resultdata.mLearningPlace = KumonDataCtrl.SF_LEARNINGPLACE_SCHOOL;
						}
					}
					resultdata.mScore = -1;

					if (resultdata.mGradingMethod == KumonDataCtrl.SF_GradingMethod_Auto) {
						// �����̓_�͖���
						resultdata.mGradingMethod = KumonDataCtrl.SF_GradingMethod_Instrucore;
					}

					if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT
							|| mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY) {
						if (resultdata.mGradingMethod == 3) {
							// ����� 3:�w���ҍ̓_�i���k�[���j�͕s��
							resultdata.mGradingMethod = 2;
						}
					}

					resultdata.mStatus = KumonDataCtrl.SF_STATUS_GRADEREADY; // �̓_�҂�
					resultdata.mGradingStatus = KumonDataCtrl.SF_GREADINGSTATUS_LEARNED; // �w�K�ς�
					resultdata.mGrade = 0; // �ŏI�e�X�g�p
					resultdata.mIsLearned = 1; // �w�K�ς�

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

				// 20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
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
				// 20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
			} else {
				ret = true;
				// 20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
				KumonLog.ClearAndroidLog(mLogList);
				// 20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
			}
			saveDatalist.clear();
			saveDatalist = null;

			// 20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
			KumonLog.PrintoutAndroidLog(mLogList);
			// 20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

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
					// ���ȍ̓_
					if (resultdata.mGradingMethod != 1) {
						continue;
					}
				} else if (mLearningMode == KumonDataCtrl.SF_GRADEINSTRUCTORONCLIENT) {
					/***
					 * DEL-S �w���ҍ̓_�i���k�[���j�͑S�Ă̍̓_�悪�\ //�w���ҍ̓_�i���k�[���j
					 * if(resultdata.mGradingMethod != 3) { continue; } DEL-E
					 ***/
				} else {
					continue;
				}

				// resultdata.mScore = �擾�ς�;
				resultdata.mGradingStatus = KumonDataCtrl.SF_GREADINGSTATUS_GRADED; // �̓_�ς�
				resultdata.mGrade = 0; // �ŏI�e�X�g�p

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

	// �e�X�g���f�[�^��Save
	public void saveHalfwayTestData() throws Exception {
		// 20150126 ADD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
		if (mBShowTopQuestionData == false) {
			// 20150126 ADD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
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
			// 20150126 ADD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
		}
		// 20150126 ADD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷

		STestSaveData testsavedata = new STestSaveData();
		testsavedata.mResultDataList = mResultDataList;
		testsavedata.mQuestionControlIndex = mPageIndex;
		testsavedata.mTestTimes = mTestTimes;
		testsavedata.mArySuspendLearningStopWatch = mSuspendLearningStopWatch;
		// 20140915 ADD-S For Bug
		testsavedata.mLearningMode = mLearningMode;
		// 20140915 ADD-S For Bug

		// 20150126 ADD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
		testsavedata.mBShowTopQuestionData = mBShowTopQuestionData;
		testsavedata.mSideIndex = mSideIndex;
		// 20150126 ADD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷

		testsavedata.writeObject();
	}

	// / <summary>
	// / ���ݕ\�����Ă���v�����g�̊w�K���ʂ̕ۑ�
	// / </summary>
	public void SaveCurrentPrintLearningData() {
		try {

			EndTest();
			// �w�K���Ԃ̐ݒ�Ɗw�K���Ԃ̎Z�o
			Date startTime = new Date();
			Date endTime = new Date();
			long testTimeSpan;

			if (mTestTimes[mPageIndex] == null) {
				mTestTimes[mPageIndex] = new TestTime();
			}
			startTime = mTestTimes[mPageIndex].m_dtStart;

			if (mSuspendLearningStopWatch[mPageIndex] != null) {
				testTimeSpan = ((endTime.getTime() - startTime.getTime()) - mSuspendLearningStopWatch[mPageIndex]
						.getTime()) * 10000; // 100�i�m�b�P��
			} else {
				testTimeSpan = (endTime.getTime() - startTime.getTime()) * 10000; // 100�i�m�b�P��
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
			// 20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
			// mResultDataList.get(mPageIndex).mOrgCount =
			// mQuestionControl.mOrgCount;
			// 20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

			// 20150303 ADD-S For 2015�N�xVer. ���������X�e�[�^�X
			mResultDataList.get(mPageIndex).mSoundRecordStatus = checkSoundRecordStatus();
			// 20150303 ADD-E For 2015�N�xVer. ���������X�e�[�^�X
		} catch (Exception e) {
			SLog.DB_AddException(e);
		}
	}

	public void SaveCurrentPrintGradingData() {
		try {
			mMarkControl.REPLAY_TouchEnd();

			// �O�y�[�W���̓_��̏�Ԃɂ���
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
			
			// 20140731 ADD-S For �w�K�ς݋��ނ̕\����,PrintSet�P�ʂłc�a�Q��
			if (mLearningMode == KumonDataCtrl.SF_DATATYPE_DONE) {
				if (LastPrintSetID.equalsIgnoreCase(resultData.mPrintSetID) == false) {
					// PrintSet���Ⴄ����A�O���PrintSetID�̃C���N�f�[�^��PrintData���N���A���A
					// ����Ώۂ�PrintSetID�̃C���N�f�[�^��PrintData��DB����擾����
					if (LastPrintSetID.length() > 0) {
						ClearLastPrintSetData(mResultDataList, LastPrintSetID);
					}
					mResultDataList = DataDBIO.DB_GetInkDataByPrintSetID(
							mResultDataList, resultData.mPrintSetID);
					logger.addSplit("DataDBIO.DB_GetInkDataByPrintSetID");
				}
			}
			LastPrintSetID = resultData.mPrintSetID;
			// 20140731 ADD-E For �w�K�ς݋��ނ̕\����,PrintSet�P�ʂłc�a�Q��

			if (mQuestionControl == null) {
				mQuestionControl = new QuestionControl();
			}
			mQuestionControl.SetResultData(resultData);
			logger.addSplit("mQuestionControl.SetResultData");

			// �y�[�W�o�[�̐F�ݒ�
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

			// 20141208 MOD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
			String msg = "[InitializeQuestionControl] (START)";
			KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
					mPageIndex, msg);
			// 20141208 MOD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

			// ���_�A�w���ҍ̓_�҂��A3��ڂ̊w�K���I�����Ă���ꍇ�͎Q�Ƃ̂݉\�Ƃ���
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
						// ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
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
						// ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
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
							// �x�~�����Ԍv���p�̃X�g�b�v�E�H�b�`���~
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

						// �̓_�҂�
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

			// �y�[�W�o�[�̐F�ݒ�
			setPageBarColor(resultData);

			// SetUp
			loadDataFromPrintData(resultData);
			setDataForView(resultData);
			resultData.InitPageWork(mMDT_TestMark.getPageCount());
			mMarkControl.setManualMark(resultData.ManualMarked);

			mSideIndex = isTop ? 0 : (int) getSideNumberMax();

			// �Ή����@�F�̓_�f�[�^������΁A�����̓_���~�߂āA
			boolean bCancle = false;
			ArrayList<Integer> trials = mMDT_TestMark.GetTestTrialsByResult();
			for (int i = 0; i < trials.size(); i++) {
				if (trials.get(i) == mQuestionControl.TrialCount - 1) {
					bCancle = true;
				}
			}
			if (!bCancle) {
				// �哱�̓_�̑O�Ɏ����̓_
				// �Ȃ���΁@�f�[�^��ǉ�����@�O��͑S��������
				// Android4.4�Ή�
				// DoSemiAutoMark(mQuestionControl.TrialCount-1);
				KRecognizer recognizer = new KRecognizer();
				recognizer.SetDictionaryPath(StudentClientCommData
						.getDictionaryFolder().toString());
				recognizer.DoSemiAutoMark(mMDT_TestMark, mMarkControl,
						mQuestionControl.TrialCount - 1);
				mMDT_TestMark
						.AddQuesesMarkData(mQuestionControl.TrialCount - 1);
			}
			// �̓_�O�̏�Ԃɖ߂�
			for (int i = 0; i < mMDT_TestMark.getPageCount(); i++) {
				// �y�[�W�ύX�O�Ɂ@ManualMarked��true�ɂ���
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

	// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
	// public boolean InitializeQuestionControlTopQuestion(int sideIndex) {
	public boolean InitializeQuestionControlTopQuestion(int page, int sideIndex) {
		// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
		// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
		// if(mTopQuestionData == null) return false;
		if (mTopQuestionDataList == null || mTopQuestionDataList.size() < page)
			return false;
		// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
		try {
			mBShowTopQuestionData = true;
			// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
			// mPageIndex = -1;
			// DResultData resultData = mTopQuestionData;
			mPageIndex = page;
			DResultData resultData = mTopQuestionDataList.get(page);
			mStudyIndicator.SetCurrent(resultData.mPrintNo, sideIndex);
			// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
			resultData.mCount = -1;
			resultData.mLearningPlace = KumonDataCtrl.SF_LEARNINGPLACE_HOME;
			// �y�[�W�o�[�̐F�ݒ�
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
	// / �w�K�Ώۂ̖�肩�ǂ�����Ԃ�
	// / </summary>
	// / <param name="questionControl">���R���g���[��</param>
	// / <returns>�w�K�Ώۂ��ǂ���</returns>
	private boolean IsLearnAble(QuestionControl questionControl) {
		// 100�_
		// ������Over
		// �̓_�҂�
		// �����M
		if (questionControl.Score >= 100 // ���_
				|| questionControl.TrialCount >= questionControl.MaxTrialCount // 3��ڂ̊w�K���I���ς�
				|| questionControl.Status == KumonDataCtrl.SF_STATUS_GRADEREADY // �̓_�҂�
				|| questionControl.Status == KumonDataCtrl.SF_STATUS_GRADING // �̓_��
				|| questionControl.Status == KumonDataCtrl.SF_STATUS_END // ����
				|| questionControl.IsRegist == 1 // �̓_�҂�
				|| questionControl.IsLearned == 1 // �w�K�ς�
		) {
			return false;
		}
		return true;
	}

	private boolean IsLearnAble(DResultData resultData) {

		// 20140704 MOD-S For Bug
		/***
		 * if (resultData.mScore >= 100 // ���_ || resultData.mCount >=
		 * resultData.mLimitCount // 3��ڂ̊w�K���I���ς� || resultData.mStatus ==
		 * KumonDataCtrl.SF_STATUS_GRADEREADY //�̓_�҂� || resultData.mStatus ==
		 * KumonDataCtrl.SF_STATUS_GRADING //�̓_�� || resultData.mStatus ==
		 * KumonDataCtrl.SF_STATUS_END //���� || resultData.mIsRegist == 1 //�̓_�҂�
		 * || resultData.mIsLearned == 1 //�w�K�ς� ) { return false; } return true;
		 ***/
		if (resultData.mIsLearning == KumonDataCtrl.SF_LEARNING_NOCHECK) {
			if (resultData.mScore >= 100 // ���_
					|| resultData.mCount >= resultData.mLimitCount // 3��ڂ̊w�K���I���ς�
					|| resultData.mStatus == KumonDataCtrl.SF_STATUS_GRADEREADY // �̓_�҂�
					|| resultData.mStatus == KumonDataCtrl.SF_STATUS_GRADING // �̓_��
					|| resultData.mStatus == KumonDataCtrl.SF_STATUS_END // ����
					|| resultData.mIsRegist == 1 // �̓_�҂�
					|| resultData.mIsLearned == 1 // �w�K�ς�
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
		// �x�~�����Ԍv���p�̃X�g�b�v�E�H�b�`���X�^�[�g
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
		// �x�~�����Ԍv���p�̃X�g�b�v�E�H�b�`���X�^�[�g
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

		// �x�~�����Ԍv���p�̃X�g�b�v�E�H�b�`���X�^�[�g
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
		// 20141208 MOD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
		String msg = "[DoOnClickBack] (START) (mSideIndex = " + mSideIndex
				+ ")";
		KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
				mPageIndex, msg);
		// 20141208 MOD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

		mMarkControl.REPLAY_TouchEnd();
		try {
			if (mBShowTopQuestionData) {
				// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
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
				// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
				return false;
			}

			if (mSideIndex > 0) {
				PageBack();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
			} else {
				// ���ݕ\�����Ă�����̃f�[�^�̕ۑ�
				if (mMDT_Mode == MdtMode.Test) {
					StopRunningStopwatch();
					// �v�����g��؂�ւ���x�Ƀe�X�g�I��(�����̓_�͍̓_���s��)�������ʂ�ێ�����B
					// ����ʑJ�ڎ��ɂ܂Ƃ߂ďI����Ԃɂ���ƁA���f�[�^�ǂݍ��݂��w�K�������s�����ƂɂȂ�A�������Ԃ������肷���邽��
					SaveCurrentPrintLearningData();

					// �x�~�����Ԍv���p�̃X�g�b�v�E�H�b�`���X�^�[�g
					StartRunningStopwatch();
				}

				//
				// �O�̖��R���g���[����\��
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
			// �O�y�[�W���̓_��̏�Ԃɂ���
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

		// 20141208 MOD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
		String msg = "[refreshPage] (START) (mSideIndex = " + mSideIndex
				+ ")";
		KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
				mPageIndex, msg);
		// 20141208 MOD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

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

		// 20141208 MOD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
		String msg = "[DoOnClickNext] (START) (mSideIndex = " + mSideIndex
				+ ")";
		KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
				mPageIndex, msg);
		// 20141208 MOD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

		try {
			if (mBShowTopQuestionData) {
				// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
				/***
				 * if(mSideIndex == 0) { mSideIndex++;
				 * mMarkControl.movePage(mSideIndex); }
				 ***/
				if (mSideIndex == 0) {
					PageNext();
				} else {
					InitializeQuestionControlTopQuestion(mPageIndex + 1, 0);
				}
				// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
				return false;
			}

			if (getSideNumberMax() > mSideIndex) {
				PageNext();
				mMarkControl.setSoundIcon(mSideIndex, layout, context);
			} else {
				// ���ݕ\�����Ă���v�����g�̃C���N�f�[�^�E�̓_���ʃf�[�^�̕ۑ�
				if (mMDT_Mode == MdtMode.Test) {
					StopRunningStopwatch();

					// �v�����g��؂�ւ���x�Ƀe�X�g�I��(�����̓_�͍̓_���s��)�������ʂ�ێ�����B
					// ����ʑJ�ڎ��ɂ܂Ƃ߂ďI����Ԃɂ���ƁA���f�[�^�ǂݍ��݂��w�K�������s�����ƂɂȂ�A�������Ԃ������肷���邽��
					SaveCurrentPrintLearningData();

					// �x�~�����Ԍv���p�̃X�g�b�v�E�H�b�`���X�^�[�g
					StartRunningStopwatch();
				}

				//
				// ���̖��R���g���[��
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
			// �O�y�[�W���̓_��̏�Ԃɂ���
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

			// 20141208 MOD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
			String msg = "[DoOnClickIndicator] (START) (sideidx = " + sideidx
					+ ")";
			KumonLog.AddAndroidLog(mLogList, mQuestionControl.PrintUnitID,
					pageidx, msg);
			// 20141208 MOD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p

			if (pageidx < 0) {
				// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
				// InitializeQuestionControlTopQuestion(0);

				pageidx = mStudyIndicator.getRefPageIndex(pos);
				if (mBShowTopQuestionData == true) {
					// �Q�Ɩ��\����
					if (mPageIndex == pageidx) {
						if (mSideIndex == sideidx) {
							// ���݃y�[�W
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
					// �ʏ�e�X�g����̑J��
					if (mMDT_Mode == MdtMode.Test) {
						StopRunningStopwatch();
						SaveCurrentPrintLearningData();
					}
					InitializeQuestionControlTopQuestion(pageidx, sideidx);
				}
				// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
				return false;
			}
			// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
			// if(mPageIndex == pageidx) {
			if (mBShowTopQuestionData == false && mPageIndex == pageidx) {
				// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
				if (mSideIndex == sideidx) {
					// ���݃y�[�W
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
					// ���ݕ\�����Ă���v�����g�̃C���N�f�[�^�E�̓_���ʃf�[�^�̕ۑ�
					if (mMDT_Mode == MdtMode.Test) {
						StopRunningStopwatch();

						// �v�����g��؂�ւ���x�Ƀe�X�g�I��(�����̓_�͍̓_���s��)�������ʂ�ێ�����B
						// ����ʑJ�ڎ��ɂ܂Ƃ߂ďI����Ԃɂ���ƁA���f�[�^�ǂݍ��݂��w�K�������s�����ƂɂȂ�A�������Ԃ������肷���邽��
						SaveCurrentPrintLearningData();

						// �x�~�����Ԍv���p�̃X�g�b�v�E�H�b�`���X�^�[�g
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

	// 20150413 ADD-E For 2015�N�xVer. ���ǑΉ�
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

	// 20150413 ADD-E For 2015�N�xVer. ���ǑΉ�

	public boolean getGradeFinishStatus() {
		// �ŏI�y�[�W B�ʂ̂�
		boolean ret = false;
		if (mPageIndex >= getMaxPageIndex()) {
			// �ŏI�y�[�W�̏ꍇ
			if (mSideIndex >= getSideNumberMax()) {
				ret = true;
			}
		}
		return ret;
	}

	public boolean getBackStatus() {
		boolean ret = true;
		if (mBShowTopQuestionData) {
			// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
			/***
			 * if(mSideIndex == 0) { ret = false; }
			 ***/
			if (mSideIndex <= 0 && mPageIndex <= 0) {
				ret = false;
			}
			// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
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
			// 20150126 MOD-S 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
			/***
			 * if(mSideIndex > 0) { ret = false; }
			 ***/
			if (mSideIndex >= 1
					&& mPageIndex >= mTopQuestionDataList.size() - 1) {
				ret = false;
			}
			// 20150126 MOD-E 2015�N�xVer. �Q�ƃy�[�W�𑝂₷
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

		// �X�L�b�v(�O��)�{�^��
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

	// 20150413 ADD-S For 2015�N�xVer. ���ǑΉ�
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

	// 20150413 ADD-E For 2015�N�xVer. ���ǑΉ�

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

		// 20140704 ADD-S For �_���\�����̖��
		mMarkControl.setlimitCnt(resultData.mLimitCount);
		// 20140704 ADD-E

		// 20140731 ADD-S For �^���Ή�
		// 20150310 MOD-S For Web�����f�[�^�W�J
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
		// 20150310 MOD-E For Web�����f�[�^�W�J
		mMarkControl.setRecordFolder(recordolder);
		// 20140731 ADD-E For �^���Ή�

		// 20140731 ADD-S For Memo
		SetMemoBtnStaus(resultData);
		// 20140731 ADD_E For Memo
	}

	// / <summary>
	// / ���f�[�^�̓ǂݍ���
	// / </summary>
	// / <param name="questionData">���f�[�^</param>
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
	// / ���̉𓚂��ꎞ���f���܂��B
	// / </summary>
	public void SleepTest() {
		if (mMDT_Mode == MdtMode.TestSleep) {
			return;
		}
		mMDT_Mode = MdtMode.TestSleep;
	}

	// / <summary>
	// / ���̉𓚂��I�����܂��B
	// / </summary>
	public void EndTest() {
		mMDT_Mode = MdtMode.TestEnd;
	}

	// / <summary>
	// / �C���N�f�[�^��Base64����������k����byte�z��̎擾
	// / </summary>
	// / <returns>�C���N�f�[�^����������k�����o�C�g�z��</returns>
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
	// / �̓_���ʃf�[�^�̕�������擾
	// / </summary>
	// / <returns>�̓_���ʃf�[�^�̕���������k�����o�C�g�z��</returns>
	public String GetMarkData() {
		String ret = "";
		try {
			ret = MdtTestMarkData.SaveToJson(mMDT_TestMark);
		} catch (Exception e) {
		}
		return ret;
	}

	// / <summary>
	// / �_���̎擾
	// / </summary>
	// / <returns>�_��</returns>
	public int GetScore() {
		int ret = 0;

		ret = mMDT_TestMark.GetScore(mQuestionControl.TrialCount - 1);

		// ���̓_�̏ꍇ��0�_�œo�^
		// ��0�_�͉�ʏ�\�����Ȃ����߁A�̓_�̌���0�_�ɂȂ����ꍇ�Ƌ�ʂł���K�v�͂Ȃ�
		return (ret < 0) ? 0 : ret;
	}

	// / <summary>
	// / �����Đ���~���̃C���N�f�[�^�ւ̖��ߍ���
	// / </summary>
	public void SoundFinished() {
		// KYON _uctInkMarking.soundFinished();
	}

	// / <summary>
	// / ���̃y�[�W�\��
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
	// / �O�̃y�[�W
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
	// / �ŏ��̃y�[�W
	// / </summary>
	public void ShowFirstPage() {
		mSideIndex = 0;
		mMarkControl.movePage(mSideIndex);
		mStudyIndicator.MovePageSide(mSideIndex);
	}

	// / <summary>
	// / �Ō�̃y�[�W
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
			// 20140917 MOD-S For ���w�K���Ԃ��Ȃ�
			// if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT ||
			// mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY) {
			if (mLearningMode == KumonDataCtrl.SF_DATATYPE_NEXT
					|| mLearningMode == KumonDataCtrl.SF_DATATYPE_RETRY
					|| mLearningMode == KumonDataCtrl.SF_DATATYPE_WEBVIEW) {
				// 20140917 MOD-E For ���w�K���Ԃ��Ȃ�
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
	// *** �C���N�Đ��p
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

	// 20140731 ADD-S For �^���Ή�
	// ****************************************************************************************************
	// *** �����^���p
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

	// 20140805 ADD-S For �^��
	// 20150115 MOD-S �Đ������_�C�A���O�\��
	/***
	 * public int REC_Get_Duration(int pos) { return
	 * mMarkControl.REC_Get_Duration(pos); }
	 ***/
	public int REC_Get_Duration(boolean recordable, int pos) {
		return mMarkControl.REC_Get_Duration(recordable, pos);
	}

	// 20150115 MOD-E �Đ������_�C�A���O�\��
	public boolean REC_Get_CanRecordStatus(int pos) {
		return mMarkControl.REC_Get_CanRecordStatus(pos);
	}

	public boolean REC_Get_CanPlay(int pos) {
		return mMarkControl.REC_Get_CanPlay(pos);
	}

	// 20140805 ADD-E For �^��

	// 20140731 ADD-S For Memo
	// ****************************************************************************************************
	// *** �w���҃����p
	// ****************************************************************************************************
	private ImageButton mImageBtnMemo = null;

	public void SetMemoBtn(ImageButton btn) {
		mImageBtnMemo = btn;
	}

	public void SetMemoBtnStaus(DResultData resultData) {
		if (mImageBtnMemo != null) {

			// 20150210 MOD-S For 2015�N�xVer. ��������
			// if(CInkMain.HaveMemoStroke(resultData.mTagComment) ||
			// TextUtils.isEmpty(resultData.mTagText) == false) {

			// 20150310 MOD-S For Web�����f�[�^�W�J
			// String soundcomment =
			// TblSoundCommentData.DB_GetSoundCommentFileName(mCurrentUser.mStudentID,
			// mCurrentUser.mCurrentKyozaiID, mQuestionControl.PrintUnitID);

			String soundcomment = GetSoundCommentFileName();
			// 20150310 MOD-E For Web�����f�[�^�W�J
			if (CInkMain.HaveMemoStroke(resultData.mTagComment)
					|| TextUtils.isEmpty(resultData.mTagText) == false
					|| (soundcomment != null && soundcomment.length() > 0)) {
				// 20150210 MOD-E For 2015�N�xVer. ��������
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
	// 20150210 ADD-S For 2015�N�xVer. ��������
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

	// 20150210 ADD-E For 2015�N�xVer. ��������

	// 20140731 ADD-S For �w�K�ς݋��ނ̕\����,PrintSet�P�ʂłc�a�Q��
	private void ClearLastPrintSetData(ArrayList<DResultData> resultdatalist,
			String printsetid) {
		for (int i = 0; i < resultdatalist.size(); i++) {
			DResultData resultdata = resultdatalist.get(i);
			if (resultdata.mPrintSetID.equalsIgnoreCase(printsetid)) {
				// �C���N�f�[�^�N���A
				resultdata.mInkData = null;
				// 20150416 ADD-S InkData To Binary
				resultdata.mInkBinary = null;
				// 20150416 ADD-E InkData To Binary
				// ���f�[�^�N���A
				resultdata.mQuestion = null;
			}
		}
	}

	// 20140731 ADD-E For �w�K�ς݋��ނ̕\����,PrintSet�P�ʂłc�a�Q��
	// 20150212 ADD-S For �Q�ƒ��͐擪�y�[�W�֑J��
	public int getIndicatorStartPage() {
		int pos = mStudyIndicator.getIndicatorStartPage();
		return pos;
	}

	// 20150212 ADD-E For �Q�ƒ��͐擪�y�[�W�֑J��

	// 20150303 ADD-S For 2015�N�xVer. ���������X�e�[�^�X
	private int checkSoundRecordStatus() {
		final int STAT_SOUNDRECORD_NO = 0; // �^���ݒ薳��
		final int STAT_SOUNDRECORD_INVALID = 1; // �s�K
		final int STAT_SOUNDRECORD_VALID = 2; // �K

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
							// �^�����Ԃ��Z���̂ŕs�K
							status = STAT_SOUNDRECORD_INVALID;
							break;
						}
					}
					if (record.ProperRangeMax > 0) {
						if (rectime > (record.ProperRangeMax + 2) * 1000) {
							// �^�����Ԃ������̂ŕs�K
							status = STAT_SOUNDRECORD_INVALID;
							break;
						}
					}
				} else {
					// �^�����Ă��Ȃ��̂ŕs�K
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

	// 20150303 ADD-E For 2015�N�xVer. ���������X�e�[�^�X

}
