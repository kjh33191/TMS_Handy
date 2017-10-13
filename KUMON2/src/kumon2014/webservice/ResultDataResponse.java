package kumon2014.webservice;

import java.util.ArrayList;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.util.Base64;

import kumon2014.common.Utility;
import kumon2014.kumondata.DResultData;

public class ResultDataResponse {
	
	public KumonSoapResult mRresult;
	public ResultDataResponseData mResponsedata;
	
	public ResultDataResponse() {
		mRresult = new KumonSoapResult();
		mResponsedata = new ResultDataResponseData();
	}
	
	public boolean Parser(SoapObject response) {
		boolean bret = false;
		
    	for(int i=0; i< response.getPropertyCount();i++){    
    		PropertyInfo pinfo = new PropertyInfo();
    		response.getPropertyInfo(i, pinfo);
    		
    		if(pinfo.getName().equals("Result")) {
    			SoapObject resultSoap = (SoapObject)response.getProperty(i);
    	    	for(int j=0; j< resultSoap.getPropertyCount();j++){    
    	    		PropertyInfo pi = new PropertyInfo();
    	    		resultSoap.getPropertyInfo(j, pi);
    	    		
    	    		if(pi.getName().equals("Status")) {
    	    			mRresult.setStatus(resultSoap.getProperty("Status").toString());
    	    		}
    	    		else if(pi.getName().equals("Error")) {
    	    			mRresult.setError(resultSoap.getProperty("Error").toString());
    	    		}
    	    	}
    		}
    		else if(pinfo.getName().equals("ResponseData")) {
    			Parser_ResponseData((SoapObject)response.getProperty(i));
    		}
    	}        	
    	bret = true;
    	
		return bret;
	}
	public boolean Parser_ResponseData(SoapObject responsedataSoap) {
		boolean bret = false;
		
		for(int i=0; i< responsedataSoap.getPropertyCount(); i++){    
			PropertyInfo pi = new PropertyInfo();
    		responsedataSoap.getPropertyInfo(i, pi);

    		if(pi.getName().equals("PrintData")) {
    			Parser_PrintData((SoapObject)responsedataSoap.getProperty(i));
    		}
    	}        	
		
    	bret = true;
		return bret;
	}
	public boolean Parser_PrintData(SoapObject printdataListSoap) {
		boolean bret = false;
		
		int cnt = printdataListSoap.getPropertyCount();
   		for(int i=0; i < cnt; i++){    
   			SoapObject printdataSoap = (SoapObject)printdataListSoap.getProperty(i);
			DResultData resultdata = new DResultData();
   			
   			for(int j=0; j < printdataSoap.getPropertyCount(); j++){    
   				PropertyInfo pi = new PropertyInfo();
   				printdataSoap.getPropertyInfo(j, pi);
   				
   				String work;
   				
    			if(pi.getName().equals("KyokaID")) {
    				resultdata.mKyokaID =  printdataSoap.getProperty("KyokaID").toString();
    			}
    			else if(pi.getName().equals("KyokaOrderNo")) {
    				work =  printdataSoap.getProperty("KyokaOrderNo").toString();
    				resultdata.mKyokaOrderNo = Integer.parseInt(work); 
    			}
    			else if(pi.getName().equals("KyokaName")) {
    				resultdata.mKyokaName = printdataSoap.getProperty("KyokaName").toString();
    			}
    			else if(pi.getName().equals("KyozaiID")) {
    				resultdata.mKyozaiID =  printdataSoap.getProperty("KyozaiID").toString();
    			}
    			else if(pi.getName().equals("KyozaiOrderNo")) {
    				work =  printdataSoap.getProperty("KyozaiOrderNo").toString();
    				resultdata.mKyozaiOrderNo = Integer.parseInt(work); 
    			}
    			else if(pi.getName().equals("KyozaiName")) {
    				resultdata.mKyozaiName = printdataSoap.getProperty("KyozaiName").toString();
    			}
    			else if(pi.getName().equals("PrintUnitID")) {
    				resultdata.mPrintUnitID = printdataSoap.getProperty("PrintUnitID").toString();
    			}
    			else if(pi.getName().equals("PrintSetID")) {
    				resultdata.mPrintSetID = printdataSoap.getProperty("PrintSetID").toString();
    			}
    			else if(pi.getName().equals("PrintID")) {
    				resultdata.mPrintID = printdataSoap.getProperty("PrintID").toString();
    			}
    			else if(pi.getName().equals("PrintNo")) {
    				work =  printdataSoap.getProperty("PrintNo").toString();
    				resultdata.mPrintNo = Integer.parseInt(work); 
    			}
    			else if(pi.getName().equals("Status")) {
    				work =  printdataSoap.getProperty("Status").toString();
    				resultdata.mStatus = Integer.parseInt(work); 
    	    	}
    			else if(pi.getName().equals("Count")) {
    				work =  printdataSoap.getProperty("Count").toString();
    				resultdata.mCount = Integer.parseInt(work); 
    	    	}
    			else if(pi.getName().equals("GradingMethod")) {
    				work =  printdataSoap.getProperty("GradingMethod").toString();
    				resultdata.mGradingMethod = Integer.parseInt(work); 
    	    	}
    			else if(pi.getName().equals("GradingStatus")) {
    				work =  printdataSoap.getProperty("GradingStatus").toString();
    				resultdata.mGradingStatus = Integer.parseInt(work); 
    	    	}
    			else if(pi.getName().equals("LearningPlace")) {
    				work =  printdataSoap.getProperty("LearningPlace").toString();
    				resultdata.mLearningPlace = Integer.parseInt(work); 
    	    	}
    			else if(pi.getName().equals("ScheduledDate")) {
    				work = printdataSoap.getProperty("ScheduledDate").toString();
    				if(work.length() > 10) {
    					work =  work.substring(0, 10);
    				}
    				work = work.replaceAll("-", "");
    				resultdata.mScheduledDate = work;
    	    	}
    			else if(pi.getName().equals("ScheduledIndex")) {
    				work =  printdataSoap.getProperty("ScheduledIndex").toString();
    				resultdata.mScheduledIndex = Integer.parseInt(work); 
    	    	}
    			else if(pi.getName().equals("ScheduledNum")) {
    				work =  printdataSoap.getProperty("ScheduledNum").toString();
    				resultdata.mScheduledNum = Integer.parseInt(work); 
    	    	}
    			else if(pi.getName().equals("LimitCount")) {
    				work =  printdataSoap.getProperty("LimitCount").toString();
    				resultdata.mLimitCount = Integer.parseInt(work); 
    	    	}
    			
    			else if(pi.getName().equals("StartDate")) {
    				work = printdataSoap.getProperty("StartDate").toString();
    				if(work != null && work.length() > 0) {
    					resultdata.mStartDate = Utility.getDateSoapFromString(work);
    				}
    				else {
    					resultdata.mStartDate = null;
    				}
    	    	}
    			else if(pi.getName().equals("EndDate")) {
    				work = printdataSoap.getProperty("EndDate").toString();
    				if(work != null && work.length() > 0) {
    					resultdata.mEndDate = Utility.getDateSoapFromString(work);
    				}
    				else {
    					resultdata.mEndDate = null;
    				}
    	    	}
    			else if(pi.getName().equals("AnswerTime")) {
    				work =  printdataSoap.getProperty("AnswerTime").toString();
    				//20140605 MOD-S ForBug
    				//resultdata.mAnswerTime = Integer.parseInt(work); 
    				resultdata.mAnswerTime = Long.parseLong(work); 
    				//20140605 MOD-E
    	    	}
    			else if(pi.getName().equals("Score")) {
    				work =  printdataSoap.getProperty("Score").toString();
    				resultdata.mScore = Integer.parseInt(work); 
    	    	}
    			
    			else if(pi.getName().equals("QuestionData")) {
    				//resultdata.mQuestionData = Base64.decode(printdataSoap.getProperty("QuestionData").toString(), Base64.NO_WRAP);
    	    	}
    			else if(pi.getName().equals("GradingResultData")) {
    				//20140801 MOD-S For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    				//resultdata.mGradingResultData = printdataSoap.getProperty("GradingResultData").toString();
    				if (pi.getType()==SoapPrimitive.class){
    					resultdata.mGradingResultData = pi.getValue().toString();
   					} else {
   						resultdata.mGradingResultData = "";
   					}
    				//20140801 MOD-E For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    	    	}
    			else if(pi.getName().equals("InkData")) {
    				//20140801 MOD-S For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    				//resultdata.mInkData = printdataSoap.getProperty("InkData").toString();
    				if (pi.getType()==SoapPrimitive.class){
    					resultdata.mInkData = pi.getValue().toString();
   					} else {
   						resultdata.mInkData = "";
   					}
    				//20140801 MOD-E For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    	    	}
    			else if(pi.getName().equals("RedComment")) {
    				//20140801 MOD-S For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    				//resultdata.mRedComment = printdataSoap.getProperty("RedComment").toString();
    				if (pi.getType()==SoapPrimitive.class){
    					resultdata.mRedComment = pi.getValue().toString();
   					} else {
   						resultdata.mRedComment = "";
   					}
    				//20140801 MOD-E For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    	    	}
    			else if(pi.getName().equals("TagComment")) {
    				//20140801 MOD-S For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    				//resultdata.mTagComment = printdataSoap.getProperty("TagComment").toString();
    				if (pi.getType()==SoapPrimitive.class){
    					resultdata.mTagComment = pi.getValue().toString();
   					} else {
   						resultdata.mTagComment = "";
   					}
    				//20140801 MOD-E For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    	    	}
    			else if(pi.getName().equals("TagText")) {
    				//20140801 MOD-S For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    				//resultdata.mTagText = printdataSoap.getProperty("TagText").toString();
    				if (pi.getType()==SoapPrimitive.class){
    					resultdata.mTagText = pi.getValue().toString();
   					} else {
   						resultdata.mTagText = "";
   					}
    				//20140801 MOD-E For Bug 戻り値の型がStringで,Nullの場合"anyType{}" が戻る
    	    	}
    			else if(pi.getName().equals("PrintType")) {
    				work =  printdataSoap.getProperty("PrintType").toString();
    				resultdata.mPrintType = Integer.parseInt(work); 
    	    	}
    			//20140731 ADD-S For 録音対応
    			else if(pi.getName().equals("SoundRecord")) {
    				resultdata.mSoundRecord = Base64.decode(printdataSoap.getProperty("SoundRecord").toString(), Base64.NO_WRAP);
    	    	}
    			//20140731 ADD-E For 録音対応
    			
    		    //20150121 ADD-S For 2015年度Ver. 教材更新
    			else if(pi.getName().equals("PrintUpdateTime")) {
    				work = printdataSoap.getProperty("PrintUpdateTime").toString();
    				if(work != null && work.length() > 0) {
    					resultdata.mPrintUpdateTime = Utility.getDateSoapFromString(work);
    				}
    				else {
    					resultdata.mPrintUpdateTime = null;
    				}
    	    	}
    		    //20150121 ADD-E For 2015年度Ver. 教材更新
    			
    	        //20150210 ADD-S For 2015年度Ver. 音声メモ
    			else if(pi.getName().equals("SoundComment")) {
    				resultdata.mSoundComment = Base64.decode(printdataSoap.getProperty("SoundComment").toString(), Base64.NO_WRAP);
    	    	}
    	        //20150210 ADD-E For 2015年度Ver. 音声メモ
    			
    		    //20150303 ADD-S For 2015年度Ver. 音声メモステータス
    			else if(pi.getName().equals("SoundRecordStatus")) {
    				work =  printdataSoap.getProperty("SoundRecordStatus").toString();
    				resultdata.mSoundRecordStatus = Integer.parseInt(work); 
    	    	}
    		    //20150303 ADD-E For 2015年度Ver. 音声メモステータス
    		    //20150409 ADD-S For 2015年度Ver. 未読コメント
    			else if(pi.getName().equals("CommentUnreadFlg")) {
    				work =  printdataSoap.getProperty("CommentUnreadFlg").toString();
    				resultdata.mCommentUnreadFlg = Integer.parseInt(work); 
    				if(resultdata.mCommentUnreadFlg != 0) {
//    					int a = 0;
//    					a++;
    				}
    	    	}
    		    //20150409 ADD-E For 2015年度Ver. 未読コメント
    		    //20150423 ADD-S For 2015年度Ver. 未読コメント
    			else if(pi.getName().equals("PrintSetDate")) {
    				work = printdataSoap.getProperty("PrintSetDate").toString();
    				if(work != null && work.length() > 0) {
    					resultdata.mPrintSetDate = Utility.getDateSoapFromString(work);
    				}
    				else {
    					resultdata.mPrintSetDate = null;
    				}
    	    	}
    		    //20150423 ADD-E For 2015年度Ver. 未読コメント
    			
    		}
			mResponsedata.mResultDataList.add(resultdata);
    	}        	
    	bret = true;
		return bret;
	}
}


class ResultDataResponseData {
	ArrayList<DResultData> 	mResultDataList = null;
    
	public ResultDataResponseData() {
		mResultDataList = new ArrayList<DResultData>();
	}
}
