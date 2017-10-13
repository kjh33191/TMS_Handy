package kumon2014.markcontroltool.control;

import kumon2014.activity.R;
import kumon2014.kumondata.KumonDataCtrl;
import pothos.markcontroltool.MarkStuct.MdtTestMarkData;
import kumon2014.markcontroltool.RotateImage;
import android.view.View;
import android.widget.ImageView;

public class IconCheckPictureBox {
    /// </summary>
    public enum IconMarkNumber
    {
        FirstCheck,
        SecondCheck,
    }
    /// <summary>
    /// ���̍̓_���
    /// </summary>
    private enum Status
    {
        /// <summary>�ŏ�����</summary>
        Right,
        /// <summary>�ŏ��s����</summary>
        Wrong,
        /// <summary>�ŏ��O�p</summary>
        UnRight,
        /// <summary>�s��������C�����Đ���</summary>
        WrongRight,
        /// <summary>�O�p����C�����Đ���</summary>
        UnRightRight,
    }

    /// �e�X�g���[�h
    //20140811 ADD-S
    public int mLearningMode = KumonDataCtrl.SF_DATATYPE_NONE;
    //20140811 ADD-E

    public boolean SemiAttention = false;

    //public IconCheckPictureBox MdtIconCheckCooperation;

    /// �y�[�W�ԍ�
    public int MdtPageNumber;
    /// ���ԍ�
    public int MdtQuestionNumber;
    /// ��Ԗڂ̃`�F�b�N����Ԗڂ̃`�F�b�N����ݒ肵�܂�
    public IconMarkNumber MdtMarkNumber;
    /// ���Ԗڂ̍̓_��ݒ肷��
    public int MdtTrialNumber;
    /// ���Ԗڂ̏C����ݒ肷��
    public int MdtAmendCount;

    /// �������s������
    /// </summary>
    //20140731 MOD-S For �O�p�Ή�
    //public boolean MdtRightX;
    public int LastMdtRightX; 	//�Ō�A����
    public int FirstMdtRightX;	//����A����
    public int SecondMdtRightX;	//����A����
    //20140731 MOD-E For �O�p�Ή�
    //20140811 ADD-S
    public int LastOrgMdtRightX; //�Ō�A����
    //20140811 ADD-E

    /// <summary>
    /// ���݂̃e�X�g���[�h
    /// </summary>
    public boolean MdtCanChange;

    public ImageView ImageButton = null;

    private Status m_Status;


    private Boolean m_Visible;

    public Boolean GetVisible() {
    	return m_Visible;
    }
    public void SetVisible(boolean value) {
    	m_Visible = value;
    	if(value == true) {
    		ImageButton.setVisibility(View.VISIBLE);
    	}
    	else {
    		ImageButton.setVisibility(View.INVISIBLE);
    	}
    }


    public void SetStatus()
    {
        MdtCanChange = true;
        switch (MdtTrialNumber)
        {
            case 0:
                if (MdtMarkNumber == IconMarkNumber.FirstCheck)
                {
                    SetVisible(true);
                    //20140731 MOD-S For �O�p�Ή�
                    /***
                    if (MdtRight) {
                        MdtMarkStatusImage(Status.Right);
                    }
                    else {
                        MdtMarkStatusImage(Status.Wrong);
                    }
                    ***/
                    if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                    	//����
                        MdtMarkStatusImage(Status.Right);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    	//�O�p
                        MdtMarkStatusImage(Status.UnRight);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//�s����
                        MdtMarkStatusImage(Status.Wrong);
                    }
                    //20140731 MOD-E For �O�p�Ή�
                }
                else
                    SetVisible(false);
                break;
            case 1:
                if (MdtMarkNumber == IconMarkNumber.FirstCheck)
                {
                    SetVisible(true);

                    //20140731 MOD-S For �O�p�Ή�
                    /***
                    if (MdtRight)
                        MdtMarkStatusImage(Status.WrongRight);
                    else
                        MdtMarkStatusImage(Status.Wrong);
                    ***/
                    if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                    	//����
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRightRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.WrongRight);
                    	}
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    	//�O�p
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//�s����
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                    //20140731 MOD-E For �O�p�Ή�
                }
                else
                {
                    SetVisible(true);
                    //20140731 MOD-S For �O�p�Ή�
                    /***
                    if (MdtRight)
                    {
                        MdtMarkStatusImage(Status.Right);
                    }
                    else
                    {
                        if (MdtAmendCount <= 1)
                        {
                        	MdtMarkStatusImage(Status.Right);
                            MdtRight = true;    // ADD K.Iwakura 2011/2/21
                        }
                        else {
                            MdtMarkStatusImage(Status.Wrong);
                        }
                    }
                    ***/
                    if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                    	//����
                        MdtMarkStatusImage(Status.Right);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    	//�O�p
                        MdtMarkStatusImage(Status.UnRight);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//�s����
                        MdtMarkStatusImage(Status.Wrong);
                    }
                    //20140731 MOD-E For �O�p�Ή�
                }
                break;
            //20140528 MOD-S For �����񐔂R��ȏ�Ή�
            //case 2:
            default:
            //20140528 MOD-E For �����񐔂R��ȏ�Ή�
                if (MdtMarkNumber == IconMarkNumber.FirstCheck)
                {
                    SetVisible(true);
                    MdtCanChange = false;
                    //20140731 MOD-S For �O�p�Ή�
                    /***
                    if (MdtRight)
                    {
                        if (MdtAmendCount > 1)
                        {
                            MdtMarkStatusImage(Status.Wrong);
                            MdtRight = false;    // ADD K.Iwakura 2011/2/21
                        }
                        else
                        {
                            MdtMarkStatusImage(Status.WrongRight);
                        }
                    }
                    else
                    {
                        MdtMarkStatusImage(Status.Wrong);
                    }
                    ***/
                    if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                    	//����
                        if (MdtAmendCount > 1)
                        {
                        	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                        		MdtMarkStatusImage(Status.UnRight);
                        		LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Unright;
                        	}
                        	else {
                        		MdtMarkStatusImage(Status.Wrong);
                        		LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Wrong;
                        	}
                        }
                        else
                        {
                        	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                        		MdtMarkStatusImage(Status.UnRightRight);
                        	}
                        	else {
                        		MdtMarkStatusImage(Status.WrongRight);
                        	}
                        	//20140826 DEl-S For Bug
                            //MdtMarkStatusImage(Status.WrongRight);
                        	//20140826 DEl-E
                        }
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    	//�O�p
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//�s����
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                    //20140731 MOD-E For �O�p�Ή�
                }
                else
                {
                    //20140731 MOD-S For �O�p�Ή�
                	/***
                    if (MdtRight)
                    {
                        if (MdtAmendCount > 1)
                        {
                            SetVisible(true);
                            MdtMarkStatusImage(Status.WrongRight);
                        }
                        else
                        {
                            SetVisible(false);
                        }
                    }
                    else
                    {
                        SetVisible(true);
                        MdtMarkStatusImage(Status.Wrong);
                    }
                    ***/
                    if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                    	//����
                        if (MdtAmendCount > 1) {
                            SetVisible(true);
	                        if (MdtAmendCount == MdtTrialNumber)
	                        {
	                        	if(SecondMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
	                        		MdtMarkStatusImage(Status.UnRightRight);
	                        	}
	                        	else {
	                        		MdtMarkStatusImage(Status.WrongRight);
	                        	}
	                        }
	                        else {
	                            MdtCanChange = false;
	                        	if(SecondMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
	                        		MdtMarkStatusImage(Status.UnRightRight);
	                        	}
	                        	else {
	                        		MdtMarkStatusImage(Status.WrongRight);
	                        	}
	                        }
                        }
                        else
                        {
                            SetVisible(false);
                        }
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    	//�O�p
                        SetVisible(true);
                        MdtMarkStatusImage(Status.UnRight);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//�s����
                        SetVisible(true);
                        MdtMarkStatusImage(Status.Wrong);
                    }

                    //20140731 MOD-E For �O�p�Ή�
                }
                break;
        }
    }
    private void MdtMarkStatusImage(Status status)
    {
    	m_Status = status;
        //�\���A�C�R����ύX����
        switch (m_Status)
        {
            case Right:
            	if(SemiAttention) {
            		//�I�����W�̐���(�`�F�b�N�Ȃ�)
           			//20140731 MOD-S For �A�C�R����]
            		//ImageButton.setImageResource(R.drawable.gd_right_orange);
            		ImageButton.setImageBitmap(RotateImage.mgd_right_orange);
           			//20140731 MOD-E For �A�C�R����]
            	}
            	else {
            		//���̐���(�`�F�b�N�Ȃ�)
           			//20140731 MOD-S For �A�C�R����]
            		//ImageButton.setImageResource(R.drawable.gd_right_white);
            		ImageButton.setImageBitmap(RotateImage.mgd_right_white);
           			//20140731 MOD-E For �A�C�R����]
            	}
                break;
            case Wrong:
                if (MdtTrialNumber > 1 && MdtMarkNumber == IconMarkNumber.FirstCheck)
                {
               		//���̕s����(�g�Ȃ�)
           			//20140731 MOD-S For �A�C�R����]
            		//ImageButton.setImageResource(R.drawable.gd_wrong_norect_white);
            		ImageButton.setImageBitmap(RotateImage.mgd_wrong_norect_white);
           			//20140731 MOD-E For �A�C�R����]
                }
                else
                {
                	if(SemiAttention) {
                   		//�I�����W�̕s����(�`�F�b�N����)
               			//20140731 MOD-S For �A�C�R����]
                		//ImageButton.setImageResource(R.drawable.gd_wrong_orange);
                		ImageButton.setImageBitmap(RotateImage.mgd_wrong_orange);
               			//20140731 MOD-E For �A�C�R����]
                   	}
                   	else {
                   		//���̕s����(�`�F�b�N����)
                   		//�I�����W�̕s����(�`�F�b�N����)
               			//20140731 MOD-S For �A�C�R����]
                		ImageButton.setImageResource(R.drawable.gd_wrong_white);
                		ImageButton.setImageBitmap(RotateImage.mgd_wrong_white);
               			//20140731 MOD-E For �A�C�R����]
                   	}
                }
                break;
            case WrongRight:

                if (MdtTrialNumber > 1 && MdtMarkNumber == IconMarkNumber.FirstCheck || MdtCanChange == false) {
               		//���̕s����(�`�F�b�N����,�g�Ȃ�)+�Z
           			//20140731 MOD-S For �A�C�R����]
            		//ImageButton.setImageResource(R.drawable.gd_wrongright_norect_white);
            		ImageButton.setImageBitmap(RotateImage.mgd_wrongright_norect_white);
           			//20140731 MOD-E For �A�C�R����]
                }
                else {
                	if(SemiAttention) {
                   		//�I�����W�̕s����(�`�F�b�N����)+�Z
               			//20140731 MOD-S For �A�C�R����]
                		//ImageButton.setImageResource(R.drawable.gd_wrongright_orange);
                		ImageButton.setImageBitmap(RotateImage.mgd_wrongright_orange);
               			//20140731 MOD-E For �A�C�R����]
                   	}
                   	else {
                   		//���̕s����(�`�F�b�N����)+�Z
               			//20140731 MOD-S For �A�C�R����]
                		//ImageButton.setImageResource(R.drawable.gd_wrongright_white);
                		ImageButton.setImageBitmap(RotateImage.mgd_wrongright_white);
               			//20140731 MOD-E For �A�C�R����]
                   	}
                }
                break;
            //20140731 MOD-S For �O�p�Ή�
            case UnRight:
                if (MdtTrialNumber > 1 && MdtMarkNumber == IconMarkNumber.FirstCheck )
                {
               		//���̎O�p(�g�Ȃ�)
           			//20140731 MOD-S For �A�C�R����]
            		//ImageButton.setImageResource(R.drawable.gd_triangle);
            		ImageButton.setImageBitmap(RotateImage.mgd_triangle);
           			//20140731 MOD-E For �A�C�R����]
                }
                else
                {
                	if(SemiAttention) {
                   		//�I�����W�̕s����(�`�F�b�N����)
               			//20140731 MOD-S For �A�C�R����]
                		//ImageButton.setImageResource(R.drawable.gd_triangle_orange);
                		ImageButton.setImageBitmap(RotateImage.mgd_triangle_orange);
               			//20140731 MOD-E For �A�C�R����]
                   	}
                   	else {
                   		//���̕s����(�`�F�b�N����)
               			//20140731 MOD-S For �A�C�R����]
                		//ImageButton.setImageResource(R.drawable.gd_triangle_white);
                		ImageButton.setImageBitmap(RotateImage.mgd_triangle_white);
               			//20140731 MOD-E For �A�C�R����]
                   	}
                }
                break;
            case UnRightRight:
                if (MdtTrialNumber > 1 && MdtMarkNumber == IconMarkNumber.FirstCheck || MdtCanChange == false) {
               		//���̎O�p(�`�F�b�N����,�g�Ȃ�)+�Z
           			//20140731 MOD-S For �A�C�R����]
            		//ImageButton.setImageResource(R.drawable.gd_triangleright);
            		ImageButton.setImageBitmap(RotateImage.mgd_triangleright);
           			//20140731 MOD-E For �A�C�R����]
                }
                else {
                	if(SemiAttention) {
                   		//�I�����W�̕s����(�`�F�b�N����)+�Z
                   		//���̎O�p(�`�F�b�N����,�g�Ȃ�)+�Z
               			//20140731 MOD-S For �A�C�R����]
                		//ImageButton.setImageResource(R.drawable.gd_triangleright_orange);
                		ImageButton.setImageBitmap(RotateImage.mgd_triangleright_orange);
               			//20140731 MOD-E For �A�C�R����]
                   	}
                   	else {
                   		//���̕s����(�`�F�b�N����)+�Z
               			//20140731 MOD-S For �A�C�R����]
                		//ImageButton.setImageResource(R.drawable.gd_triangleright_white);
                		ImageButton.setImageBitmap(RotateImage.mgd_triangleright_white);
               			//20140731 MOD-E For �A�C�R����]
                   	}
                }
                break;
            //20140731 MOD-S For �O�p�Ή�
        }
    }

    public void OnClick(IconCheckPictureBox checkcooperation) {
        //�\����Ԃ�؂�ւ���
        //IconCheckPictureBox check =  (IconCheckPictureBox)sender;
        if (MdtCanChange == false) {
            return;
        }

        if(MdtTrialNumber == 1)
        {
            if (this.MdtMarkNumber == IconMarkNumber.SecondCheck &&
            		this.m_Status == Status.Right &&
            		checkcooperation.m_Status == Status.Wrong)
            {
            	LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
            }
            //20140731 ADD-S For �O�p�Ή�
            else if (this.MdtMarkNumber == IconMarkNumber.SecondCheck &&
            		this.m_Status == Status.Right &&
            		checkcooperation.m_Status == Status.UnRight)
            {
            	LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
            }
            //20140731 ADD-E For �O�p�Ή�
        }

        //20140731 MOD-S For �O�p�Ή�
        //MdtRight = !MdtRight;

        //20140731 ADD-E For �O�p�Ή�

        switch (LastMdtRightX)
        {
        	case(MdtTestMarkData.SF_AnswerResultCheck_Right):
        		LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Wrong;
        		break;
        	case(MdtTestMarkData.SF_AnswerResultCheck_Wrong):
        		//20140811 MOD-S �{�l�̓_�́��Ȃ�
        		//LastMdtRightX = MdtQuestionAnswerResult.SF_AnswerResultCheck_Unright;
                if(mLearningMode == KumonDataCtrl.SF_DATATYPE_GRADESELF) {
	        		if(LastOrgMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
	                	LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Unright;
	        		}
	        		else {
	                	LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
	        		}
                }
                else {
                	LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Unright;
                }
    			//20140811 MOD-E �{�l�̓_�́��Ȃ�

        		break;
        	case(MdtTestMarkData.SF_AnswerResultCheck_Unright):
        		LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
        		break;
        }
        //20140731 MOD- For �O�p�Ή�

        //20140731 MOD-E For �O�p�Ή�
        switch (MdtTrialNumber)
        {
            case 0:
                //20140731 MOD-S For �O�p�Ή�
            	/***
                if (MdtRight) {
                	MdtMarkStatusImage(Status.Right);
	            	checkcooperation.MdtRight = true; // ADD K.Iwakura 2011/2/21
	                checkcooperation.MdtMarkStatusImage(Status.Right);
                }
                else {
                	MdtMarkStatusImage(Status.Wrong);
	            	checkcooperation.MdtRight = false; // ADD K.Iwakura 2011/2/21
	                checkcooperation.MdtMarkStatusImage(Status.Wrong);
                }
                ***/
                if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                	MdtMarkStatusImage(Status.Right);
	            	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
	                checkcooperation.MdtMarkStatusImage(Status.Right);
                }
                else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                	MdtMarkStatusImage(Status.UnRight);
	            	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Unright;
	            	checkcooperation.MdtMarkStatusImage(Status.UnRight);
                }
                else {
                	MdtMarkStatusImage(Status.Wrong);
	            	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Wrong;
	            	checkcooperation.MdtMarkStatusImage(Status.Wrong);
                }
                //20140731 MOD-E For �O�p�Ή�
                break;
            case 1:
                //20140731 MOD-S For �O�p�Ή�
            	/***
                if (MdtRight)
                {
                    if (this.MdtMarkNumber == IconMarkNumber.FirstCheck)
                    {
                    	MdtMarkStatusImage(Status.WrongRight);

                    	checkcooperation.MdtRight = true; // ADD K.Iwakura 2011/2/21
                        checkcooperation.MdtMarkStatusImage(Status.Right);
                    }
                    else
                    {
                    	MdtMarkStatusImage(Status.Right);

                    	checkcooperation.MdtRight = true; // ADD K.Iwakura 2011/2/21
                        checkcooperation.MdtMarkStatusImage(Status.WrongRight);
                    }
                }
                else
                {
                    if (this.MdtMarkNumber == IconMarkNumber.FirstCheck)
                    {
                    	MdtMarkStatusImage(Status.Wrong);
                       	checkcooperation.MdtRight = false;
                       	checkcooperation.MdtMarkStatusImage(Status.Wrong);
                    }
                    else
                    {
                    	MdtMarkStatusImage(Status.Wrong);
                       	checkcooperation.MdtRight = false;
                       	checkcooperation.MdtMarkStatusImage(Status.Wrong);
                    }
                }
                ***/
                if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                    if (this.MdtMarkNumber == IconMarkNumber.FirstCheck)
                    {
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRightRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.WrongRight);
                    	}

                    	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
                        checkcooperation.MdtMarkStatusImage(Status.Right);
                    }
                    else
                    {
                    	MdtMarkStatusImage(Status.Right);
                    	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		checkcooperation.MdtMarkStatusImage(Status.UnRightRight);
                    	}
                    	else {
                    		checkcooperation.MdtMarkStatusImage(Status.WrongRight);
                    	}

                    }
                }
                else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    if (this.MdtMarkNumber == IconMarkNumber.FirstCheck)
                    {
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                       	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Unright;
                       	checkcooperation.MdtMarkStatusImage(Status.UnRight);
                    }
                    else
                    {
                    	MdtMarkStatusImage(Status.UnRight);
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                           	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Unright;
                           	checkcooperation.MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                           	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Wrong;
                           	checkcooperation.MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                }
                else {
                    if (this.MdtMarkNumber == IconMarkNumber.FirstCheck)
                    {
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                       	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Wrong;
                       	checkcooperation.MdtMarkStatusImage(Status.Wrong);
                    }
                    else
                    {
                    	MdtMarkStatusImage(Status.Wrong);
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                           	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Unright;
                           	checkcooperation.MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                           	checkcooperation.LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Wrong;
                           	checkcooperation.MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                }

                //20140731 MOD-E For �O�p�Ή�
                break;
            //20140528 MOD-S For �����񐔂R��ȏ�Ή�
            //case 2:         //FirstCheck�̕ύX���ł��Ȃ�����
            default:
            //20140528 MOD-E For �����񐔂R��ȏ�Ή�
                //20140731 MOD-S For �O�p�Ή�
            	/***
                if (MdtRight)
                {
                	MdtMarkStatusImage(Status.WrongRight);
                }
                else
                {
                	MdtMarkStatusImage(Status.Wrong);
                }
                ***/
                if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                   	if(SecondMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                   		MdtMarkStatusImage(Status.UnRightRight);
                   	}
                   	else {
                   		MdtMarkStatusImage(Status.WrongRight);
                   	}
                }
                else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                	MdtMarkStatusImage(Status.UnRight);
                }
                else {
                	MdtMarkStatusImage(Status.Wrong);
                }
                //20140731 MOD-E For �O�p�Ή�
                break;
        }
    }

}
