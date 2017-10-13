package kumon2014.kumondata;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;

import kumon2014.database.master.MQuestion2;
import kumon2014.database.master.MastDBIO;

public class WDownloadResultData {
	
	public int 		mSoapStatus = 0;;
	public String 	mSoapError = "";
	public String	mLastSessionID = "";
	
	public ArrayList<DResultData>			mDownLoadResultDataList = null;
	public ArrayList<MQuestion2>				mDownLoadQuestionList = null;
	public ArrayList<MQuestion2>				mQuestionList = null;
	public ArrayList<MQuestion2>				mTopQuestionList = null;

	public WDownloadResultData()
	{
		mSoapStatus = 0;
		mSoapError = "";
		mLastSessionID = "";
		
		mDownLoadResultDataList = new ArrayList<DResultData>();
		mDownLoadQuestionList = new ArrayList<MQuestion2>();
		mQuestionList = new ArrayList<MQuestion2>();
		mTopQuestionList = new ArrayList<MQuestion2>();
	}
	public void CheckQuestionData()
	{
		SQLiteDatabase readable = MastDBIO.DB_GetReadable(); 
		
		for(int i = 0; i < mDownLoadResultDataList.size(); i++) {
			DResultData resultdata = mDownLoadResultDataList.get(i);
			
	        //20150121 MOD-S For 2015年度Ver. 教材更新
			//if(MastDBIO.DB_IsExistQuestionData(readable, resultdata.mPrintID) == false) {
			if(MastDBIO.DB_IsExistQuestionData(readable, resultdata.mPrintID, resultdata.mCount, resultdata.mPrintUpdateTime) == false) {
	        //20150121 MOD-E For 2015年度Ver. 教材更新
				MQuestion2 question = new MQuestion2();
				question.mKyokaID = resultdata.mKyokaID;
				question.mKyozaiID = resultdata.mKyozaiID;
				question.mPrintID = resultdata.mPrintID;
				mQuestionList.add(question);
			}
			
	        //20150121 DEL-S For 2015年度Ver. １頁以外も参照可能に
			/***
			//先頭ページの問題も取得
			if(resultdata.mPrintNo > 0) {
				int topprintno = (int)Math.floor((resultdata.mPrintNo - 1) / 10) * 10 + 1;
				if(MastDBIO.DB_IsExistQuestionDataByNo(readable, resultdata.mKyokaID, resultdata.mKyozaiID, topprintno) == false) {
					MQuestion2 question = new MQuestion2();
					question.mKyokaID = resultdata.mKyokaID;
					question.mKyozaiID = resultdata.mKyozaiID;
					question.mPrintNo = topprintno;
					if(mTopQuestionList.contains(question) == false) {
						mTopQuestionList.add(question);
					}
				}
			}
			***/
	        //20150121 DEL-E For 2015年度Ver. １頁以外も参照可能に
		}
        //20150121 ADD-S For 2015年度Ver. １頁以外も参照可能に
		if(mDownLoadResultDataList.size() > 0) {
			DResultData resultdata = mDownLoadResultDataList.get(0);
			if(resultdata.mPrintNo > 0) {
				int topprintno = (int)Math.floor((resultdata.mPrintNo - 1) / 10) * 10 + 1;
				for(int j = topprintno; j < resultdata.mPrintNo; j++) {
					if(MastDBIO.DB_IsExistQuestionDataByNo(readable, resultdata.mKyokaID, resultdata.mKyozaiID, j) == false) {
						MQuestion2 question = new MQuestion2();
						question.mKyokaID = resultdata.mKyokaID;
						question.mKyozaiID = resultdata.mKyozaiID;
						question.mPrintNo = j;
						if(mTopQuestionList.contains(question) == false) {
							mTopQuestionList.add(question);
						}
					}
				}
			}
		}
        //20150121 ADD-E For 2015年度Ver. １頁以外も参照可能に
		

		readable.close();
		readable = null;
	}
	public void ClearDownLoadData()
	{
		if(mDownLoadResultDataList != null) {
			for(int i= 0; i < mDownLoadResultDataList.size(); i++)
			{
				DResultData resultdata = mDownLoadResultDataList.get(i);
				resultdata.ClearAll();
				resultdata = null;
			}
			mDownLoadResultDataList.clear();
			mDownLoadResultDataList = null;
		}
		
		if(mDownLoadQuestionList != null) {
			for(int i= 0; i < mDownLoadQuestionList.size(); i++)
			{
				MQuestion2 question = mDownLoadQuestionList.get(i);
				question.ClearAll();
				question = null;
			}
			mDownLoadQuestionList.clear();
			mDownLoadQuestionList = null;
		}
		if(mQuestionList != null) {
			for(int i= 0; i < mQuestionList.size(); i++)
			{
				MQuestion2 question = mQuestionList.get(i);
				question.ClearAll();
				question = null;
			}
			mQuestionList.clear();
			mQuestionList = null;
		}
		if(mTopQuestionList != null) {
			for(int i= 0; i < mTopQuestionList.size(); i++)
			{
				MQuestion2 question = mTopQuestionList.get(i);
				question.ClearAll();
				question = null;
			}
			mTopQuestionList.clear();
			mTopQuestionList = null;
		}
	}
}
