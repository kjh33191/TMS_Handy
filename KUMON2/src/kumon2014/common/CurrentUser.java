package kumon2014.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Key;


public class CurrentUser implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String	mLoginID = "";
	public String	mPassword = "";
	public String	mStudentName = "";
	public String	mStudentID = "";
	
	public String	mCurrentKyokaID = "";
	public String	mCurrentKyozaiID = "";
	public String	mCurrentKyokaKyozaiName = "";
	public String	mCurrentKyokaName = "";
	public String	mCurrentKyozaiName = "";
	public int		mCurrentPrintType = 0;

	public String	mLastSessionID = "";
	
	//20130324 ADD-S For ペンの太さは前回使用の太さをキープ
	public int		mPenWidth = 3;
	//20130324 ADD-E
	
	public static CurrentUser readObject() 
	{
		try {
			File lastuserFile = StudentClientCommData.getLastUserFile();
			FileInputStream inFile = new FileInputStream(lastuserFile);
			byte[] buff = Utility.cvtStreamToByteArray(inFile);
			Key skey = StudentClientCommData.GetKey();
			byte[] dataObj = Utility.decode(buff, skey);
			ByteArrayInputStream bais = new ByteArrayInputStream(dataObj);
			ObjectInputStream inObject = new ObjectInputStream(bais);
			CurrentUser user = (CurrentUser)inObject.readObject();
			inObject.close();
			inFile.close();
			return user;
		} catch (Exception e) {
//			String s = e.getMessage();
		}
		
		return null;
	}
	public boolean writeObject() 
	{
		try {
			File lastuserFile = StudentClientCommData.getLastUserFile();
			FileOutputStream outFile = new FileOutputStream(lastuserFile);
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
