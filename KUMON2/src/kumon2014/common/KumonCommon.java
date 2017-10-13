package kumon2014.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import pothos.mdtcommon.IO;
import pothos.mdtcommon.DataStructs.MdtData;
import android.graphics.Bitmap;
import android.graphics.Color;

import kumon2014.common.StudentClientCommData;
import kumon2014.database.data.TblSoundRecordData;
import kumon2014.database.master.MQuestion2;
import kumon2014.database.master.MQuestionImage;
import kumon2014.database.master.MQuestionSound;


public class KumonCommon {

	
	public static String GetDataWorkPath() {
		File basepath = StudentClientCommData.getTopFolder();
		String path = "DataWork";
		File dir = new File(basepath, path);
		dir.mkdirs();
		
		return dir.toString();
	}
	
	public static void DeleteFolder(String path){
		File f = new File(path);
		
		if( f.exists()==false ){
			return ;
		}

		if(f.isFile()){
			f.delete();
		}
			
		if(f.isDirectory()){
			File[] files=f.listFiles();
			for(int i=0; i<files.length; i++){
				DeleteFolder( files[i].toString() );
			}
			f.delete();
		}
	}
	/// <summary>
	/// Zip形式で圧縮された文字列データを返します。
	/// </summary>
	/// <param name="data">圧縮されたバイナリデータ</param>
	/// <param name="entryName">エントリ名</param>
	/// <returns></returns>
	public static String GetZipDecompressedText(byte[] data, String entryName)
	{
		String ret = "";

		if (data == null)
		{
			return ret;
		}

		if (entryName == null || entryName.isEmpty())
		{
			return ret;
		}
		
		ZipInputStream in = null;  
		try {
			in = new ZipInputStream(new ByteArrayInputStream(data));
			ZipEntry zipEntry = null;
			int len = 0; 
			byte[] buffer = new byte[4096];
			ByteArrayOutputStream out = null;      //解凍ファイル出力用ストリーム
			while((zipEntry = in.getNextEntry()) != null )
            {
				if(entryName.equalsIgnoreCase(zipEntry.getName())) {
					out = new ByteArrayOutputStream();
					while( (len = in.read(buffer)) != -1 )
					{
						out.write(buffer, 0, len);
					}
					ret = new String(out.toByteArray(), "UTF-8");
	                out.close();
	                out = null;
	                break;
				}
			}
			in.closeEntry();
			in.close();
		}
		catch(Exception e)
        {
			return "";
        }
		return ret;
	}
	public byte[] GetZipDecompressed(byte[] data, String entryName)
	{
		byte[] ret = null;

		if (data == null)
		{
			return ret;
		}

		if (entryName == null || entryName.isEmpty())
		{
			return ret;
		}
		
		ZipInputStream in = null;  
		try {
			in = new ZipInputStream(new ByteArrayInputStream(data));
			ZipEntry zipEntry = null;
			int len = 0; 
			byte[] buffer = new byte[4096];
			ByteArrayOutputStream out = null;      //解凍ファイル出力用ストリーム
			while((zipEntry = in.getNextEntry()) != null )
            {
				if(entryName.equalsIgnoreCase(zipEntry.getName())) {
					out = new ByteArrayOutputStream();
					while( (len = in.read(buffer)) != -1 )
					{
						out.write(buffer, 0, len);
					}
					ret = out.toByteArray();
					out.close();
	                out = null;
	                break;
				}
			}
			in.closeEntry();
			in.close();
		}
		catch(Exception e)
        {
			return null;
        }
		return ret;
	}

	public static String GetExtension(String fileName) {
	    if (fileName == null) {
	        return null;
	    }	
	 
	    // 最後の『 . 』の位置を取得します。
	    int lastDotPosition = fileName.lastIndexOf(".");

	    // 『 . 』が存在する場合は、『 . 』以降を返します。
	    if (lastDotPosition != -1) {
	        return fileName.substring(lastDotPosition + 1);
	    }
	    return null;
	}
	public static String GetFileNameOnly(String fullfileName) {
	    if (fullfileName == null) {
	        return "";
	    }
//	    File f = new File("/" + fullfileName);
//	    String s = f.getName();
	    
	    // 最後の『 . 』の位置を取得します。
	    int lastDotPosition = fullfileName.lastIndexOf("\\");

	    // 『 . 』が存在する場合は、『 . 』以降を返します。
	    if (lastDotPosition != -1) {
	        return fullfileName.substring(lastDotPosition + 1);
	    }
	    return "";
	}
	/// <summary>
	/// 文字列データをZip形式で圧縮したバイトデータを返します。
	/// </summary>
	/// <param name="text">データ文字列</param>
	/// <param name="entryName">エントリ名</param>
	/// <returns>圧縮されたバイト配列</returns>
	public static byte[] GetZipCompressedDataBytes(String text, String entryName)
	{
		//byte[] source = Encoding.Unicode.GetBytes(text);
		byte[] source;
		try {
//			source = text.getBytes("Unicode");
			source = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			return null;
		}

		
		// 入出力用のストリームを生成
		ByteArrayOutputStream baos  = null;      //解凍ファイル出力用ストリーム
		java.util.zip.ZipOutputStream  zos = null;  
		try {
			baos = new ByteArrayOutputStream();
			zos = new java.util.zip.ZipOutputStream(baos);
			
			ZipEntry entry = new ZipEntry(entryName);
			
			zos.putNextEntry(entry); 
			zos.write(source, 0, source.length); 
			zos.closeEntry(); 
			zos.finish(); 

			// 圧縮データを取り出す 
			byte[] ret = baos.toByteArray(); 
			
			return ret;
		}
		catch(Exception e)
        {
        }
		return null;
	}
	public byte[] GetZipCompressedDataBytes(byte[] source, String entryName) 
	{
		// 入出力用のストリームを生成
		ByteArrayOutputStream baos  = null;      //解凍ファイル出力用ストリーム
		java.util.zip.ZipOutputStream  zos = null;  
		try {
			baos = new ByteArrayOutputStream();
			zos = new java.util.zip.ZipOutputStream(baos);
			
			ZipEntry entry = new ZipEntry(entryName);
			
			zos.putNextEntry(entry); 
			zos.write(source, 0, source.length); 
			zos.closeEntry(); 
			// 圧縮データを取り出す 
			byte[] ret = baos.toByteArray(); 

			
			return ret;
		}
		catch(Exception e)
        {
        }
		finally {
			try {
				if(zos != null) {
					zos.close(); 
					zos.finish(); 
					zos = null; 
				}
			}
			catch(Exception e)
	        {
	        }
			try {
				if(baos != null) {
					baos.close(); 
					baos = null; 
				}
			}
			catch(Exception e)
	        {
	        }
		}

		return null;
	}
	
    public static Bitmap makeTransparentBitmap(Bitmap orgBitmap, int transColor) {
    	Bitmap bitmap = null;
    	
	    int width = orgBitmap.getWidth(); 
        int height = orgBitmap.getHeight(); 
        int[] pixels = new int[width * height]; 
        bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888 );
        orgBitmap.getPixels(pixels, 0, width, 0, 0, width, height); 
        for (int y = 0; y < height; y++) { 
          for (int x = 0; x < width; x++) { 
            if( pixels[x + y * width]== transColor){ pixels[x + y * width] = 0; } 
          } 
        } 
        bitmap.eraseColor(Color.argb(0, 0, 0, 0)); 
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height); 
    	
        return bitmap;
    }

    public static MQuestion2 DecompressQuestion(MQuestion2 question)
    {
    	question.mImageList = new ArrayList<MQuestionImage>();
    	question.mSoundList = new ArrayList<MQuestionSound>();
    	question.mMDTData = null;
    	
    	if(question.mQuestionData == null || question.mQuestionData.length == 0) {
    		return question;
    	}
    
//    	MyTimingLogger logger = new MyTimingLogger("DecompressQuestion");
    	
    	ArrayList<ZippedFile> zippedFilelist = new ArrayList<ZippedFile>();
    	
    	ZipInputStream in = null;  
    	MdtData mdtData = null;
    	try {
    		in = new ZipInputStream(new ByteArrayInputStream(question.mQuestionData));
//    		logger.addSplit("new ZipInputStream");
    		ZipEntry zipEntry = null;
//    		ByteArrayOutputStream out = null;      //解凍ファイル出力用ストリーム
    		while((zipEntry = in.getNextEntry()) != null )
    		{
    			String ext = KumonCommon.GetExtension(zipEntry.getName());
    			if(ext.equalsIgnoreCase("kad") == true) {
    				long fileLen = zipEntry.getSize();
    	    		byte [] buff = new byte[(int) fileLen]; 
    				long readLen = 0;
    				while (readLen < fileLen) {
        				int len = in.read(buff, (int)readLen, (int)(fileLen - readLen));
    					readLen += len;
    				}
    				question.mMDTData = buff;
//    	    		logger.addSplit("read " + question.mMDTData.length);
    				try {
    					mdtData = IO.JsonToMdtData(new String(question.mMDTData, "UTF-8"));
//        	    		logger.addSplit("IO.JsonToMdtData");
    				} catch (Exception e) {
    					mdtData = null;
    				}
    			}
    			else {
    				ZippedFile zippedFile = new ZippedFile();
    				zippedFile.FileName = zipEntry.getName();
    				long fileLen = zipEntry.getSize();
    	    		byte [] buff = new byte[(int) fileLen]; 
    				long readLen = 0;
    				while (readLen < fileLen) {
        				int len = in.read(buff, (int)readLen, (int)(fileLen - readLen));
    					readLen += len;
    				}
    				zippedFile.byteArray = buff;
//    	    		logger.addSplit("read " + zippedFile.byteArray.length);
    				zippedFilelist.add(zippedFile);
    			}
    		}
    		in.closeEntry();
    		in.close();

  //  		logger.addSplit("input end");
    		
    		for(int i = 0; i < mdtData.getPageCnt(); i++) {
    			//画像ファイル
    			String fileName = mdtData.getPageImageFileName(i);
    			for(int j = 0; j < zippedFilelist.size(); j++) {
    				if(fileName.equalsIgnoreCase(zippedFilelist.get(j).FileName)) {
    					MQuestionImage questionImage = new MQuestionImage();
        				questionImage.mImage = zippedFilelist.get(j).byteArray;
        				questionImage.mPageNo = i;
        				question.mImageList.add(questionImage);
//        	    		logger.addSplit("mImageList.add " + i + ", " + j);
        				break;
    				}
    			}
    			
    			//Soundファイル
    			for(int j = 0; j < mdtData.getSoundDatasCnt(i); j++) {
    				fileName = mdtData.getSoundFileName(i, j);
    				int soundno = mdtData.getSoundNo(i, j);
        			for(int k = 0; k < zippedFilelist.size(); k++) {
        				if(fileName.equalsIgnoreCase(zippedFilelist.get(k).FileName)) {
            				MQuestionSound questionSound = new MQuestionSound();
            				questionSound.mSound = zippedFilelist.get(k).byteArray;
            				questionSound.mPageNo = i;
            				questionSound.mSoundNo = soundno;
            				question.mSoundList.add(questionSound);
//            	    		logger.addSplit("mSoundList.add " + i + ", " + j + ", " + k);
            				break;
        				}
        			}
    			}                    
    		}
    		zippedFilelist.clear();
    		zippedFilelist = null;
  //  		logger.dumpToLog();
    	}
    	catch(Exception e)
    	{
    		return question;
    	}
		return question;
    }
    
	//20140731 ADD-S For 録音対応
	public static byte[] CompressRecordData(File soundFolder)
	{
		byte[] sounddata = null;
		
		File outputFile = new File(soundFolder, TblSoundRecordData.SF_SOUND_ZIP);
		File dataFolder = new File(soundFolder, TblSoundRecordData.SF_DATAFOLDER);
		// 入力ストリーム
	    InputStream is = null;
	 
	    // 入出力用のバッファを作成
	    byte[] buf = new byte[4096];
	 
	    // ZipOutputStreamオブジェクトの作成
	    try {
	    	if(outputFile.exists()) {
		    	outputFile.delete();
	    	}
	    } catch (Exception e) {
	    }
	    
		ByteArrayOutputStream baos = null;      //解凍ファイル出力用ストリーム
		java.util.zip.ZipOutputStream zos = null;  
	    try {
	    	baos = new ByteArrayOutputStream();
			zos = new java.util.zip.ZipOutputStream(baos);
	    	
	    	
	    	final File[] filelist = dataFolder.listFiles();
	        for (int i = 0; i < filelist.length; i++) {
	            // 入力ストリームのオブジェクトを作成
	            is = new FileInputStream(filelist[i].getAbsolutePath());
	            // Setting Filename
	            String filename = filelist[i].getName();
	            // ZIPエントリを作成
	            ZipEntry ze = new ZipEntry(filename);
	            // 作成したZIPエントリを登録
	            zos.putNextEntry(ze);

	 
	            // 入力ストリームからZIP形式の出力ストリームへ書き出す
	            int len = 0;
	            while ((len = is.read(buf)) != -1) {
	                zos.write(buf, 0, len);
	            }
	            // 入力ストリームを閉じる
	            is.close();
	            // エントリをクローズする
	            zos.closeEntry();
	        }
	        // 出力ストリームを閉じる
			zos.finish(); 
			sounddata = baos.toByteArray(); 

	        zos.close();
	    } catch (Exception e) {
	    }
		return sounddata;
	}
    
    public static void DecompressRecordData(File filename, File targetfolder)
    {
    	ZipInputStream in = null;
        BufferedOutputStream out = null;
     
        ZipEntry zipEntry = null;
        int len = 0;
        try {
            in = new ZipInputStream(new FileInputStream(filename));
            // ZIPファイルに含まれるエントリに対して順にアクセス
            while ((zipEntry = in.getNextEntry()) != null) {
                File newfile = new File(zipEntry.getName());
                // 出力用ファイルストリームの生成
                File fullpath = new File(targetfolder, newfile.getName());
                out = new BufferedOutputStream(
                        new FileOutputStream(fullpath)
                      );
     
                // エントリの内容を出力
                byte[] buffer = new byte[4096];
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
     
                in.closeEntry();
                out.close();
                out = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class ZippedFile {
	String FileName = "";
	byte[] byteArray = null;
}


	