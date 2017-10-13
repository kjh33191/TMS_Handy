package kumon2014.webservice;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class RegistGradingResultRequest extends BaseSoapRequest {
	@Annotation.DataMember
	public String SessionID;
	@Annotation.DataMember
	public String StudentAdminID;
	@Annotation.DataMember
	public String KyokaID;
	@Annotation.DataMember
	public int PenThickness;
	@Annotation.DataMember
	public AnswerData AnswerData;	
	
	
	public RegistGradingResultRequest() {
		super();
	}

}
class AnswerData extends Vector<AnswerDataInfo> implements KvmSerializable{
	private static final long serialVersionUID = 1L;
	
	@Override
	public Object getProperty(int arg0) {
		return this.get(arg0);	
	}

	@Override
	public int getPropertyCount() {
		 return this.size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0)
	    {
		    case 0:
		    	arg2.type = new AnswerDataInfo().getClass();
		    	arg2.name = "AnswerDataInfo";
		        break;        
		    default:
		        break;
	    }

		
	}
	
	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((AnswerDataInfo) arg1);		
	}
}


class AnswerDataInfo extends BaseSoapRequest {
	@Annotation.DataMember
	public String 	PrintUnitID;
	@Annotation.DataMember
	public String 	KyozaiID;
	@Annotation.DataMember
	public String 	PrintSetID;
	@Annotation.DataMember
	public String 	PrintID;
	@Annotation.DataMember
	public int		LearningPlace;
	@Annotation.DataMember
	public int		Count;
	@Annotation.DataMember
	public int		Score;
	@Annotation.DataMember
	public int		GradingMethod;
	@Annotation.DataMember
	public int		GradingStatus;
	@Annotation.DataMember
	public int		Grade;
	@Annotation.DataMember
	public String	GradingResultData;
	@Annotation.DataMember
	public String	InkData;
	@Annotation.DataMember
	public String	RedComment;
	@Annotation.DataMember
	public String	TagComment;
	@Annotation.DataMember
	public String	TagText;
	@Annotation.DataMember
	public String 	StartDate;
	@Annotation.DataMember
	public String 	EndDate;
	@Annotation.DataMember
	public long 	AnswerTime;
	@Annotation.DataMember
	public String 	ScheduledDate;
	@Annotation.DataMember
	public int 	ScheduledIndex;
	@Annotation.DataMember
	public int 	ScheduledNum;
	@Annotation.DataMember
	public int 	PrintNo;
	@Annotation.DataMember
	public String 	SoundRecord;
    //20150409 ADD-S For 2015年度Ver. 未読コメント
	@Annotation.DataMember
	public int     CommentUnreadFlg;
	//20150409 ADD-E For 2015年度Ver. 未読コメント
    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
	@Annotation.DataMember
	public int		SoundRecordStatus;
	//20150303 ADD-E For 2015年度Ver. 音声メモステータス

	public AnswerDataInfo() {
		super();
	}
	
}
