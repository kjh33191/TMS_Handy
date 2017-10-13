package kumon2014.markcontroltool;


import kumon2014.kumondata.*;

public class QuestionControl {
	// / <summary>
	// / ����ID
	// / </summary>
	public String KyokaID;

	// / <summary>
	// / ����ID
	// / </summary>
	public String KyozaiID;

	// / <summary>
	// / �v�����g���j�b�gID
	// / </summary>
	public String PrintUnitID;
	
	// / <summary>
	// / �v�����g�Z�b�gID
	// / </summary>
	public String PrintSetID;

	// / <summary>
	// / �e�X�gID
	// / </summary>
	public String PrintID;

	// / <summary>
	// / �e�X�gNo
	// / </summary>
	public int PrintNo;
	
	// / <summary>
	// / �w�K��
	// / </summary>
	public int TrialCount;
	// / <summary>
	// / �ő�w�K��
	// / </summary>
	public int MaxTrialCount;

	// / <summary>
	// / �̓_���@
	// / </summary>
	public int QuestionGradingMethod;

	// / <summary>
	// / ���k���ꂽ�̓_���ʃf�[�^�̃o�C�g�z��
	// / </summary>
	public String GradingResultData;

	// / <summary>
	// / ���k���ꂽ�C���N�f�[�^�̃o�C�g�z��
	// / </summary>
	public String InkData;
	//20150416 ADD-S InkData To Binary
	public byte[] InkBinary;
	//20150416 ADD-E InkData To Binary

	// / <summary>
	// / �̓_���ꂽ���ǂ���
	// / </summary>
	public int GradingStatus;

	//���
	public int Status;
	
	//�_��
	public int Score;
	
	public int PrintType;
	
	public String RedComment;
	public String TagComment;
	public String TagText;

	public int	IsRegist;			//0=���M�ς�, 1=�����M
	public int 	IsLearned;			//�w�K�������ǂ���
	public int 	IsGreaded;			//�̓_�������ǂ���

	
    //20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
	public int mOrgCount = 0;		//�e�X�g�̊w�K��(���Z�O)�B
    //20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p


	
	// / <summary>
	// / �v�����g���̐ݒ�
	// / </summary>
	// / <param name="printData">�v�����g�f�[�^</param>
	public void SetResultData(DResultData resultData) {

		if (resultData == null) {
			return;
		}

		// ����ID
		KyokaID = resultData.mKyozaiID;
		// ����ID
		KyozaiID = resultData.mKyozaiID;
		// �v�����g���j�b�g
		PrintUnitID = resultData.mPrintUnitID;

		
		// �v�����g�Z�b�gID
		if (resultData.mPrintSetID != null) {
			PrintSetID = resultData.mPrintSetID;
		}

		// �v�����gID
		PrintID = resultData.mPrintID;
		PrintNo = resultData.mPrintNo;

		// �w�K��
		TrialCount = resultData.mCount;
		MaxTrialCount = resultData.mLimitCount;
		
		// �̓_���@
		QuestionGradingMethod = resultData.mGradingMethod;

		GradingStatus = resultData.mGradingStatus;
		Status = resultData.mStatus;
		Score  = resultData.mScore;

		GradingResultData = resultData.mGradingResultData;
		InkData = resultData.mInkData;
		//20150416 ADD-S InkData To Binary
		InkBinary = resultData.mInkBinary;
		//20150416 ADD-E InkData To Binary
		RedComment = resultData.mRedComment;
		TagComment = resultData.mTagComment;
		TagText = resultData.mTagText;

		IsRegist = resultData.mIsRegist;
		IsLearned = resultData.mIsLearned;
		IsGreaded = resultData.mIsGreaded;
		
		PrintType = resultData.mPrintType;
		
	    //20141208 ADD-S For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
		mOrgCount = resultData.mOrgCount;		//�e�X�g�̊w�K��(���Z�O)�B
	    //20141208 ADD-E For DebugLog ����w�K���ɁACount���Q��ɂȂ��Ă��܂����������p
		
	}
}
