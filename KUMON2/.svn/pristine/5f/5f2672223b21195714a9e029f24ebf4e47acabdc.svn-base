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
    /// 問題の採点状態
    /// </summary>
    private enum Status
    {
        /// <summary>最初正解</summary>
        Right,
        /// <summary>最初不正解</summary>
        Wrong,
        /// <summary>最初三角</summary>
        UnRight,
        /// <summary>不正解から修正して正解</summary>
        WrongRight,
        /// <summary>三角から修正して正解</summary>
        UnRightRight,
    }

    /// テストモード
    //20140811 ADD-S
    public int mLearningMode = KumonDataCtrl.SF_DATATYPE_NONE;
    //20140811 ADD-E

    public boolean SemiAttention = false;

    //public IconCheckPictureBox MdtIconCheckCooperation;

    /// ページ番号
    public int MdtPageNumber;
    /// 問題番号
    public int MdtQuestionNumber;
    /// 一番目のチェックか二番目のチェックかを設定します
    public IconMarkNumber MdtMarkNumber;
    /// 何番目の採点を設定する
    public int MdtTrialNumber;
    /// 何番目の修正を設定する
    public int MdtAmendCount;

    /// 正解か不正解か
    /// </summary>
    //20140731 MOD-S For 三角対応
    //public boolean MdtRightX;
    public int LastMdtRightX; 	//最後、結果
    public int FirstMdtRightX;	//初回、結果
    public int SecondMdtRightX;	//初回、結果
    //20140731 MOD-E For 三角対応
    //20140811 ADD-S
    public int LastOrgMdtRightX; //最後、結果
    //20140811 ADD-E

    /// <summary>
    /// 現在のテストモード
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
                    //20140731 MOD-S For 三角対応
                    /***
                    if (MdtRight) {
                        MdtMarkStatusImage(Status.Right);
                    }
                    else {
                        MdtMarkStatusImage(Status.Wrong);
                    }
                    ***/
                    if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                    	//正解
                        MdtMarkStatusImage(Status.Right);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    	//三角
                        MdtMarkStatusImage(Status.UnRight);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//不正解
                        MdtMarkStatusImage(Status.Wrong);
                    }
                    //20140731 MOD-E For 三角対応
                }
                else
                    SetVisible(false);
                break;
            case 1:
                if (MdtMarkNumber == IconMarkNumber.FirstCheck)
                {
                    SetVisible(true);

                    //20140731 MOD-S For 三角対応
                    /***
                    if (MdtRight)
                        MdtMarkStatusImage(Status.WrongRight);
                    else
                        MdtMarkStatusImage(Status.Wrong);
                    ***/
                    if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Right) {
                    	//正解
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRightRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.WrongRight);
                    	}
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    	//三角
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//不正解
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                    //20140731 MOD-E For 三角対応
                }
                else
                {
                    SetVisible(true);
                    //20140731 MOD-S For 三角対応
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
                    	//正解
                        MdtMarkStatusImage(Status.Right);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    	//三角
                        MdtMarkStatusImage(Status.UnRight);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//不正解
                        MdtMarkStatusImage(Status.Wrong);
                    }
                    //20140731 MOD-E For 三角対応
                }
                break;
            //20140528 MOD-S For 訂正回数３回以上対応
            //case 2:
            default:
            //20140528 MOD-E For 訂正回数３回以上対応
                if (MdtMarkNumber == IconMarkNumber.FirstCheck)
                {
                    SetVisible(true);
                    MdtCanChange = false;
                    //20140731 MOD-S For 三角対応
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
                    	//正解
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
                    	//三角
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//不正解
                    	if(FirstMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Unright) {
                    		MdtMarkStatusImage(Status.UnRight);
                    	}
                    	else {
                    		MdtMarkStatusImage(Status.Wrong);
                    	}
                    }
                    //20140731 MOD-E For 三角対応
                }
                else
                {
                    //20140731 MOD-S For 三角対応
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
                    	//正解
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
                    	//三角
                        SetVisible(true);
                        MdtMarkStatusImage(Status.UnRight);
                    }
                    else if (LastMdtRightX == MdtTestMarkData.SF_AnswerResultCheck_Wrong) {
                    	//不正解
                        SetVisible(true);
                        MdtMarkStatusImage(Status.Wrong);
                    }

                    //20140731 MOD-E For 三角対応
                }
                break;
        }
    }
    private void MdtMarkStatusImage(Status status)
    {
    	m_Status = status;
        //表示アイコンを変更する
        switch (m_Status)
        {
            case Right:
            	if(SemiAttention) {
            		//オレンジの正解(チェックなし)
           			//20140731 MOD-S For アイコン回転
            		//ImageButton.setImageResource(R.drawable.gd_right_orange);
            		ImageButton.setImageBitmap(RotateImage.mgd_right_orange);
           			//20140731 MOD-E For アイコン回転
            	}
            	else {
            		//白の正解(チェックなし)
           			//20140731 MOD-S For アイコン回転
            		//ImageButton.setImageResource(R.drawable.gd_right_white);
            		ImageButton.setImageBitmap(RotateImage.mgd_right_white);
           			//20140731 MOD-E For アイコン回転
            	}
                break;
            case Wrong:
                if (MdtTrialNumber > 1 && MdtMarkNumber == IconMarkNumber.FirstCheck)
                {
               		//白の不正解(枠なし)
           			//20140731 MOD-S For アイコン回転
            		//ImageButton.setImageResource(R.drawable.gd_wrong_norect_white);
            		ImageButton.setImageBitmap(RotateImage.mgd_wrong_norect_white);
           			//20140731 MOD-E For アイコン回転
                }
                else
                {
                	if(SemiAttention) {
                   		//オレンジの不正解(チェックあり)
               			//20140731 MOD-S For アイコン回転
                		//ImageButton.setImageResource(R.drawable.gd_wrong_orange);
                		ImageButton.setImageBitmap(RotateImage.mgd_wrong_orange);
               			//20140731 MOD-E For アイコン回転
                   	}
                   	else {
                   		//白の不正解(チェックあり)
                   		//オレンジの不正解(チェックあり)
               			//20140731 MOD-S For アイコン回転
                		ImageButton.setImageResource(R.drawable.gd_wrong_white);
                		ImageButton.setImageBitmap(RotateImage.mgd_wrong_white);
               			//20140731 MOD-E For アイコン回転
                   	}
                }
                break;
            case WrongRight:

                if (MdtTrialNumber > 1 && MdtMarkNumber == IconMarkNumber.FirstCheck || MdtCanChange == false) {
               		//白の不正解(チェックあり,枠なし)+〇
           			//20140731 MOD-S For アイコン回転
            		//ImageButton.setImageResource(R.drawable.gd_wrongright_norect_white);
            		ImageButton.setImageBitmap(RotateImage.mgd_wrongright_norect_white);
           			//20140731 MOD-E For アイコン回転
                }
                else {
                	if(SemiAttention) {
                   		//オレンジの不正解(チェックあり)+〇
               			//20140731 MOD-S For アイコン回転
                		//ImageButton.setImageResource(R.drawable.gd_wrongright_orange);
                		ImageButton.setImageBitmap(RotateImage.mgd_wrongright_orange);
               			//20140731 MOD-E For アイコン回転
                   	}
                   	else {
                   		//白の不正解(チェックあり)+〇
               			//20140731 MOD-S For アイコン回転
                		//ImageButton.setImageResource(R.drawable.gd_wrongright_white);
                		ImageButton.setImageBitmap(RotateImage.mgd_wrongright_white);
               			//20140731 MOD-E For アイコン回転
                   	}
                }
                break;
            //20140731 MOD-S For 三角対応
            case UnRight:
                if (MdtTrialNumber > 1 && MdtMarkNumber == IconMarkNumber.FirstCheck )
                {
               		//白の三角(枠なし)
           			//20140731 MOD-S For アイコン回転
            		//ImageButton.setImageResource(R.drawable.gd_triangle);
            		ImageButton.setImageBitmap(RotateImage.mgd_triangle);
           			//20140731 MOD-E For アイコン回転
                }
                else
                {
                	if(SemiAttention) {
                   		//オレンジの不正解(チェックあり)
               			//20140731 MOD-S For アイコン回転
                		//ImageButton.setImageResource(R.drawable.gd_triangle_orange);
                		ImageButton.setImageBitmap(RotateImage.mgd_triangle_orange);
               			//20140731 MOD-E For アイコン回転
                   	}
                   	else {
                   		//白の不正解(チェックあり)
               			//20140731 MOD-S For アイコン回転
                		//ImageButton.setImageResource(R.drawable.gd_triangle_white);
                		ImageButton.setImageBitmap(RotateImage.mgd_triangle_white);
               			//20140731 MOD-E For アイコン回転
                   	}
                }
                break;
            case UnRightRight:
                if (MdtTrialNumber > 1 && MdtMarkNumber == IconMarkNumber.FirstCheck || MdtCanChange == false) {
               		//白の三角(チェックあり,枠なし)+〇
           			//20140731 MOD-S For アイコン回転
            		//ImageButton.setImageResource(R.drawable.gd_triangleright);
            		ImageButton.setImageBitmap(RotateImage.mgd_triangleright);
           			//20140731 MOD-E For アイコン回転
                }
                else {
                	if(SemiAttention) {
                   		//オレンジの不正解(チェックあり)+〇
                   		//白の三角(チェックあり,枠なし)+〇
               			//20140731 MOD-S For アイコン回転
                		//ImageButton.setImageResource(R.drawable.gd_triangleright_orange);
                		ImageButton.setImageBitmap(RotateImage.mgd_triangleright_orange);
               			//20140731 MOD-E For アイコン回転
                   	}
                   	else {
                   		//白の不正解(チェックあり)+〇
               			//20140731 MOD-S For アイコン回転
                		//ImageButton.setImageResource(R.drawable.gd_triangleright_white);
                		ImageButton.setImageBitmap(RotateImage.mgd_triangleright_white);
               			//20140731 MOD-E For アイコン回転
                   	}
                }
                break;
            //20140731 MOD-S For 三角対応
        }
    }

    public void OnClick(IconCheckPictureBox checkcooperation) {
        //表示状態を切り替える
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
            //20140731 ADD-S For 三角対応
            else if (this.MdtMarkNumber == IconMarkNumber.SecondCheck &&
            		this.m_Status == Status.Right &&
            		checkcooperation.m_Status == Status.UnRight)
            {
            	LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
            }
            //20140731 ADD-E For 三角対応
        }

        //20140731 MOD-S For 三角対応
        //MdtRight = !MdtRight;

        //20140731 ADD-E For 三角対応

        switch (LastMdtRightX)
        {
        	case(MdtTestMarkData.SF_AnswerResultCheck_Right):
        		LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Wrong;
        		break;
        	case(MdtTestMarkData.SF_AnswerResultCheck_Wrong):
        		//20140811 MOD-S 本人採点は△なし
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
    			//20140811 MOD-E 本人採点は△なし

        		break;
        	case(MdtTestMarkData.SF_AnswerResultCheck_Unright):
        		LastMdtRightX = MdtTestMarkData.SF_AnswerResultCheck_Right;
        		break;
        }
        //20140731 MOD- For 三角対応

        //20140731 MOD-E For 三角対応
        switch (MdtTrialNumber)
        {
            case 0:
                //20140731 MOD-S For 三角対応
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
                //20140731 MOD-E For 三角対応
                break;
            case 1:
                //20140731 MOD-S For 三角対応
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

                //20140731 MOD-E For 三角対応
                break;
            //20140528 MOD-S For 訂正回数３回以上対応
            //case 2:         //FirstCheckの変更ができないから
            default:
            //20140528 MOD-E For 訂正回数３回以上対応
                //20140731 MOD-S For 三角対応
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
                //20140731 MOD-E For 三角対応
                break;
        }
    }

}
