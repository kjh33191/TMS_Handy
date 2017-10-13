package kumon2014.kumondata;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;

import kumon2014.common.StudentClientCommData;
import kumon2014.common.Utility;

import kumon2014.markcontroltool.Stopwatch;
import pothos.markcontroltool.MarkStuct.TestTime;

public class STestSaveData implements Serializable {
	public static final long serialVersionUID = 1L;
	
	public String 					mPrintSetId;
	
	public ArrayList<DResultData> 	mResultDataList;
	public int 						mQuestionControlIndex;
	public Stopwatch[] 				mArySuspendLearningStopWatch;
    public TestTime[] 				mTestTimes = null;
	//20140915 ADD-S For Bug 
	public int					 	mLearningMode = KumonDataCtrl.SF_DATATYPE_NONE;
	//20140915 ADD-S For Bug 
	//20150126 ADD-S 2015年度Ver. 参照ページを増やす
	public boolean				 	mBShowTopQuestionData = false;
	public int				 		mSideIndex = 0;
	//20150126 ADD-E 2015年度Ver. 参照ページを増やす


	public static STestSaveData readObject() 
	{
		File lasttestFile = null;
		try {
			lasttestFile = StudentClientCommData.getLastTestFile();
			FileInputStream inFile = new FileInputStream(lasttestFile);
			byte[] buff = Utility.cvtStreamToByteArray(inFile);
			Key skey = StudentClientCommData.GetKey();
			byte[] dataObj = Utility.decode(buff, skey);
			ByteArrayInputStream bais = new ByteArrayInputStream(dataObj);
			ObjectInputStream inObject = new ObjectInputStream(bais);
			STestSaveData testsavedata = (STestSaveData)inObject.readObject();
			inObject.close();
			inFile.close();
			return testsavedata;
		} 
		catch (Exception e) {
//			String s = e.getMessage();
		}
		finally{
			if(lasttestFile != null) {
				lasttestFile.delete();
			}
		}
		
		return null;
	}
	public boolean writeObject() 
	{
		try {
			File lasttestFile = StudentClientCommData.getLastTestFile();
			FileOutputStream outFile = new FileOutputStream(lasttestFile);
			BufferedOutputStream bos = new BufferedOutputStream(outFile);
			
			byte[] buff1 = Utility.cvtObjToByteArray(this);
			Key skey = StudentClientCommData.GetKey();
			byte[] dataObj = Utility.encode(buff1, skey);
			
			bos.write(dataObj);
			bos.flush();
			bos.close();
		} 
		catch (Exception e) {
//			String s = e.getMessage();
		}
		return false;
	}
}
