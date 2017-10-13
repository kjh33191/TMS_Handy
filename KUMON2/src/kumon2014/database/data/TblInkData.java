package kumon2014.database.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;

import pothos.markcontroltool.InkControlTool.CInkData;
import pothos.markcontroltool.InkControlTool.CInkIO;

import kumon2014.common.KumonCommon;

import kumon2014.common.StudentClientCommData;
import kumon2014.kumondata.DResultData;

public class TblInkData {
	// テーブル名
	public static final String SF_TBLNAME = "D_InkData";

	public static final String SF_ZIP_ENTRY = "InkData";
	
	// カラム
	public static final String SF_COL_STUDENTID = "CStudentID"; // Text
	public static final String SF_COL_KYOKAID = "CKyokaID"; // Text
	public static final String SF_COL_KYOZAIID = "CKyozaiID"; // Text
	public static final String SF_COL_PRINTUNITID = "CPrintUnitID"; // Text
	
	public static final String SF_COL_COUNT = "CCount"; // integer
	public static final String SF_COL_INKDATA = "CInkData"; // Text
	public static final String SF_COL_INKDATAZIP = "CInkDataZip"; // blob

	//20150416 ADD-S InkData To Binary
	public static final String SF_COL_INKBINARY = "CInkBinary"; // blob
	//20150416 ADD-E InkData To Binary

	public static final String SF_CREATE_TBL_SQL_INKDATA = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_KYOKAID + " text not null,  "  
			+ SF_COL_KYOZAIID + " text not null, "
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ SF_COL_COUNT + " integer,  " 
			+ SF_COL_INKDATA + " text,  " 
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID
			+ " , " + SF_COL_KYOZAIID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";
	public static final String SF_CREATE_TBL_SQL_INKDATA_V4 = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_KYOKAID + " text not null,  "  
			+ SF_COL_KYOZAIID + " text not null, "
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ SF_COL_COUNT + " integer,  " 
			+ SF_COL_INKDATA + " text,  " 
			+ SF_COL_INKDATAZIP + " blob,  " 
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID
			+ " , " + SF_COL_KYOZAIID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";
	public static final String SF_CREATE_TBL_SQL_INKDATA_V6 = "create table "
			+ SF_TBLNAME + "( " + SF_COL_STUDENTID + " text not null, "
			+ SF_COL_KYOKAID + " text not null,  "  
			+ SF_COL_KYOZAIID + " text not null, "
			+ SF_COL_PRINTUNITID + " text not null, " 
			+ SF_COL_COUNT + " integer,  " 
			+ SF_COL_INKDATA + " text,  " 
			+ SF_COL_INKDATAZIP + " blob,  " 
			+ SF_COL_INKBINARY + " blob,  " 
			+ " primary key( " + SF_COL_STUDENTID + " , " + SF_COL_KYOKAID
			+ " , " + SF_COL_KYOZAIID + " , " + SF_COL_PRINTUNITID + " )"
			+ " );";

	// クリアALL
	protected static boolean DB_ClearAll(SQLiteDatabase Writabledb) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, null, null);
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By Studentid
	protected static boolean DB_DeleteByStudentID(SQLiteDatabase Writabledb, String studentid) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, SF_COL_STUDENTID + " = ? ",
					new String[] { studentid });
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By PrintUnit
	protected static boolean DB_DeleteByPrintUnit(SQLiteDatabase Writabledb, String studentid, String kyoka, String kyozai, String printunit) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, SF_COL_STUDENTID + " = ? " + " AND "
					+ SF_COL_KYOKAID + " = ? " + " AND " + SF_COL_KYOZAIID
					+ " = ? " + " AND " + SF_COL_PRINTUNITID + " = ? ",
					new String[] { studentid, kyoka, kyozai, printunit });
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Delete By KyozaiID
	protected static boolean DB_DeleteByKyozaiID(SQLiteDatabase Writabledb,	String studentid, String kyoka, String kyozai) {
		boolean ret = false;
		try {
			Writabledb.delete(SF_TBLNAME, SF_COL_STUDENTID + " = ? " + " AND "
					+ SF_COL_KYOKAID + " = ? " + " AND " + SF_COL_KYOZAIID
					+ " = ? " + " AND " + SF_COL_COUNT + " != 0 ",
					new String[] { studentid, kyoka, kyozai });
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	// Insert
	//20150416 MOD-S InkData To Binary
	/*****
	protected static boolean DB_InsertInkDataList(SQLiteDatabase Writabledb, ArrayList<DResultData> resultdatalist) {
		boolean ret = false;
		long cnt = 0;
		int putStatus = 0;

		
		try {
			for (int i = 0; i < resultdatalist.size(); i++) {
				//20140630 ADD-S For Ink Lost
				String inkfilepath =  StudentClientCommData.getInkTextFile(resultdatalist.get(i));
				File delfile = new File(inkfilepath);
				try {
					delfile.delete();
				}
				catch(Exception e){
				}
				//20140630 ADD-E For Ink Lost
				
				//20140606 ADD-S For1メガ超える
				byte[] inkzip = KumonCommon.GetZipCompressedDataBytes(resultdatalist.get(i).mInkData, SF_ZIP_ENTRY);
				//20140630 ADD-S For Ink Lost
				if(resultdatalist.get(i).mInkData.length() > 0 && (inkzip == null || inkzip.length == 0)) {
					KumonLog.AddLog("InkData", "Can not Zip", resultdatalist.get(i));
					if(resultdatalist.get(i).mInkData.length() < 800000) {
						putStatus = 1;
					}
					else {
						//保存できないから、Textファイル出力
						try {
							KumonLog.AddLog("InkData", "Save To File", resultdatalist.get(i));
							
						    //BufferedWriterを利用してテキスト書き込み
						    //※FileOutputStreamの第二引数をTrueにすると
						    //追加書き込みし、Falseにすると上書き作成します。
						    BufferedWriter bw = new BufferedWriter(
						            new OutputStreamWriter(
						            new FileOutputStream(inkfilepath, false), "UTF-8"));
						    bw.write(resultdatalist.get(i).mInkData);
						    bw.close();
						} catch (Exception e) {
							return false;
						}					
						putStatus = 2;
					}
				}
				//20140630 ADD-E For Ink Lost
				//20140606 ADD-E For1メガ超える
				
				
				ContentValues values = new ContentValues();
				values.put(SF_COL_STUDENTID, resultdatalist.get(i).mStudentID);
				values.put(SF_COL_KYOKAID, resultdatalist.get(i).mKyokaID);
				values.put(SF_COL_KYOZAIID, resultdatalist.get(i).mKyozaiID);
				values.put(SF_COL_PRINTUNITID,	resultdatalist.get(i).mPrintUnitID);
				values.put(SF_COL_COUNT, resultdatalist.get(i).mCount);
				//20140606 MOD-S For1メガ超える
				//values.put(SF_COL_INKDATA, resultdatalist.get(i).mInkData);
				
				//20140630 MOD-S For Ink Lost
				//values.put(SF_COL_INKDATA, "");
				//values.put(SF_COL_INKDATAZIP, inkzip);
				if(putStatus == 0) {
					values.put(SF_COL_INKDATA, "");
					values.put(SF_COL_INKDATAZIP, inkzip);
				}
				else if(putStatus == 1) {
					values.put(SF_COL_INKDATA, resultdatalist.get(i).mInkData);
					values.putNull(SF_COL_INKDATAZIP);
				}
				else {
					values.put(SF_COL_INKDATA, "");
					values.putNull(SF_COL_INKDATAZIP);
				}
				//20140630 MOD-E For Ink Lost
				
				//20140606 MOD-E For1メガ超える
			
				long rets = Writabledb.insert(SF_TBLNAME, null, values);
				if (rets >= 0) {
					cnt++;
				} else {
					break;
				}
			}
			if (cnt == resultdatalist.size()) {
				ret = true;
			}
		} catch (Exception e) {
			String s = e.toString();
		}
		return ret;
	}
	*****/
	//InkDataをBinaryで保持
	protected static boolean DB_InsertInkDataList(SQLiteDatabase Writabledb, ArrayList<DResultData> resultdatalist) {
		boolean ret = false;
		long cnt = 0;
		int putStatus = 0;

		
		try {
			for (int i = 0; i < resultdatalist.size(); i++) {
				String inkfilepath =  StudentClientCommData.getInkTextFile(resultdatalist.get(i));
				File delfile = new File(inkfilepath);
				try {
					delfile.delete();
				}
				catch(Exception e){
				}
				
				String inkbinarypath =  StudentClientCommData.getInkBinaryFile(resultdatalist.get(i));
				File delbinaryfile = new File(inkbinarypath);
				try {
					delbinaryfile.delete();
				}
				catch(Exception e){
				}

				//InkDataをJsonからBinaryへ変換
				if( resultdatalist.get(i).mInkData == null || resultdatalist.get(i).mInkData.length() ==0) {
					resultdatalist.get(i).mInkBinary = null;
				}
				else {
					CInkIO inkIO = new CInkIO();
					CInkData inkData = inkIO.LoadInkFromJson(resultdatalist.get(i).mInkData);
					if(inkData != null) {
				    	try {
				    		ByteArrayOutputStream ms = new ByteArrayOutputStream();
				    		inkIO.SaveInk(ms, inkData);
				    		resultdatalist.get(i).mInkBinary = ms.toByteArray();
				    		ms.close();
				    		ms = null;
				    	}
				    	catch(Exception e) {
							resultdatalist.get(i).mInkBinary = null;
				    	}
						inkData.Clear();
						inkData = null;
					}
					else {
						resultdatalist.get(i).mInkBinary = null;
					}
					inkIO = null;
				}
				
				byte[] zipbuff = null;
				//圧縮して保持
				if(resultdatalist.get(i).mInkBinary == null) {
					//InkData無し
					putStatus = 2;
				}
				else {
					KumonCommon kc = new KumonCommon();
					zipbuff = kc.GetZipCompressedDataBytes(resultdatalist.get(i).mInkBinary, TblInkData.SF_ZIP_ENTRY);
					kc = null;
	
					if(zipbuff.length < 800000) {
						//DB保存
						putStatus = 0;
					}
					else {
						//ファイル保存
						try {
							FileOutputStream os = new FileOutputStream(inkbinarypath);
						    os.write(zipbuff);
						    os.flush();
						    os.close();
						} catch (Exception e) {
							return false;
						}					
						putStatus = 2;
					}
				}
				
				ContentValues values = new ContentValues();
				values.put(SF_COL_STUDENTID, resultdatalist.get(i).mStudentID);
				values.put(SF_COL_KYOKAID, resultdatalist.get(i).mKyokaID);
				values.put(SF_COL_KYOZAIID, resultdatalist.get(i).mKyozaiID);
				values.put(SF_COL_PRINTUNITID,	resultdatalist.get(i).mPrintUnitID);
				values.put(SF_COL_COUNT, resultdatalist.get(i).mCount);

				if(putStatus == 0) {
					values.put(SF_COL_INKDATA, "");
					values.putNull(SF_COL_INKDATAZIP);
					values.put(SF_COL_INKBINARY, zipbuff);
				}
				else {
					values.put(SF_COL_INKDATA, "");
					values.putNull(SF_COL_INKDATAZIP);
					values.putNull(SF_COL_INKBINARY);
				}
			
				long rets = Writabledb.insert(SF_TBLNAME, null, values);
				
				//Clear
				zipbuff = null;
				resultdatalist.get(i).mInkData = null;
				resultdatalist.get(i).mInkBinary = null;
				
				if (rets >= 0) {
					cnt++;
				} else {
					break;
				}
			}
			if (cnt == resultdatalist.size()) {
				ret = true;
			}
		} catch (Exception e) {
//			String s = e.toString();
		}
		return ret;
	}
	//20150416 MOD-E InkData To Binary

	protected static DResultData DB_GetInkData(SQLiteDatabase readbledb, String studentid, String KyokaID, String KyozaiID, String PrintUnitID) {
		DResultData resultData = null;
		Cursor cursor = null;
		try {
			String cond = "";
			String[] where = null;

			cond = SF_COL_STUDENTID + " = ? " + " AND "
					+ SF_COL_KYOKAID + " = ? " + " AND "
					+ SF_COL_KYOZAIID + " = ? " + " AND "
					+ SF_COL_PRINTUNITID + " = ? ";
			
			where = new String[] { studentid, KyokaID, KyozaiID, PrintUnitID };

			String[] columns = {SF_COL_INKDATA, SF_COL_INKDATAZIP , SF_COL_INKBINARY};
			
			cursor = readbledb.query(
			// Table名
					SF_TBLNAME,
					// 項目名
					columns, 
					// 条件式
					cond,
					// 条件
					where,
					// group by
					null,
					// Having
					null,
					// order by
					null, 
					// limit
					null);

			// 検索結果をcursorから読み込んで返す
			resultData = DB_InkData_readCursor(cursor, studentid, KyokaID, KyozaiID, PrintUnitID);

		} catch (Exception e) {
//			String s = e.getMessage();
			resultData = null;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return resultData;
	}


	//20150416 MOD-S InkData To Binary
	/***
	private static String DB_InkData_readCursor(Cursor cursor, String studentid, String KyokaID, String KyozaiID, String PrintUnitID) {
		String inkdate = "";
		//20140606 ADD-S For1メガ超える
		byte[] inkzip = null;
		//20140606 ADD-S For1メガ超える
		
		if( cursor.moveToNext()) {
			inkdate = cursor.getString(0);
			inkzip = cursor.getBlob(1);
			//20140630 MOD-S For Ink Lost
			//if(inkdate.equals("") && inkzip != null) {
			//	inkdate = KumonCommon.GetZipDecompressedText(inkzip, SF_ZIP_ENTRY);
			//}
			if(inkdate.equals("")) {
				if(inkzip == null) {
					inkdate = "";
					try{
					    String msg = "StudentID=" + studentid + " KyokaID=" + KyokaID + " KyozaiID=" + KyozaiID + " PrintUnitID=" + PrintUnitID + "\r\n" + "Load From File" + "\r\n"; 
						KumonLog.AddLog("InkData", msg);
						
						String inkfilepath =  StudentClientCommData.getInkTextFile(studentid, KyokaID, KyozaiID, PrintUnitID);
					    FileInputStream fis = new FileInputStream(inkfilepath);
					    InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
					    BufferedReader br= new BufferedReader(isr);
					    String s;
					    while ((s = br.readLine() ) != null){
					    	inkdate += s;
					    }
					    br.close();
					    isr.close();
					    fis.close();        
					}
					catch(Exception e){
					}
				}
				else {
					inkdate = KumonCommon.GetZipDecompressedText(inkzip, SF_ZIP_ENTRY);
				}
			}
			//20140630 MOD-E For Ink Lost
		}
		
		return inkdate;
	}
	***/
	//InkData 保管場所 
	//1:D_InkDataの SF_COL_INKBINARY
	//2:SDカード XXX.inb;(バイナリ)
	//3:D_InkDataの SF_COL_INKDATA(JSON)
	//4:D_InkDataの SF_COL_INKDATAZIP(JSON圧縮)
	//5:SDD_InkData XXX.ink;(JSON)
	private static DResultData DB_InkData_readCursor(Cursor cursor, String studentid, String KyokaID, String KyozaiID, String PrintUnitID) {
		DResultData resultData =  new DResultData();
		
		String inkdate = "";
		byte[] inkzip = null;
		byte[] inkBinary = null;
		
		if( cursor.moveToNext()) {
			inkdate = cursor.getString(0);
			inkzip = cursor.getBlob(1);
			inkBinary = cursor.getBlob(2);

			if(inkBinary == null || inkBinary.length == 0) {
				String inkbinaryfilepath =  StudentClientCommData.getInkBinaryFile(studentid, KyokaID, KyozaiID, PrintUnitID);
				File binaryfile = new File(inkbinaryfilepath);
				if(binaryfile.exists()) {
					//2:SDカード XXX.inb;(バイナリ)
					try {
						 BufferedInputStream fis = new BufferedInputStream(new FileInputStream(binaryfile));
						 ByteArrayOutputStream baos = new ByteArrayOutputStream();
						 
						 int avail;
			            // 読み込み可能なバイト数づつ読み込む
			            while ((avail = fis.available()) > 0) {
			                byte[] bytes = new byte[avail];
			                fis.read(bytes);
			                baos.write(bytes);
			            }
			            fis.close();
			            baos.close();
			            resultData.mInkBinary = baos.toByteArray();
					}
					catch(Exception e) {
						resultData.mInkBinary = null;
					}
				}
				else {
					if(inkdate != null && inkdate.length() > 0) {
						//3:D_InkDataの SF_COL_INKDATA(JSON)
						resultData.mInkData = inkdate;
					}
					else if(inkzip != null && inkzip.length > 0) {
						//4:D_InkDataの SF_COL_INKDATAZIP(JSON圧縮)
						resultData.mInkData = KumonCommon.GetZipDecompressedText(inkzip, SF_ZIP_ENTRY);
					}
					else {
						//5:SDD_InkData XXX.ink;(JSON)
						try{
							String inkfilepath =  StudentClientCommData.getInkTextFile(studentid, KyokaID, KyozaiID, PrintUnitID);
						    FileInputStream fis = new FileInputStream(inkfilepath);
						    InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
						    BufferedReader br= new BufferedReader(isr);
						    String s;
						    while ((s = br.readLine() ) != null){
						    	inkdate += s;
						    }
						    br.close();
						    isr.close();
						    fis.close();        
							resultData.mInkData = inkdate;
						}
						catch(Exception e){
							resultData.mInkData = "";
						}
					}
				}
			}
			else {
				//1:D_InkDataの SF_COL_INKBINARY
				resultData.mInkBinary = inkBinary;
			}
		}
		if(resultData.mInkBinary != null && resultData.mInkBinary.length > 0) {
			KumonCommon kc = new KumonCommon();
			resultData.mInkBinary = kc.GetZipDecompressed(resultData.mInkBinary, TblInkData.SF_ZIP_ENTRY);
		}
		return resultData;
	}
	//20150416 MOD-E InkData To Binary
	protected static boolean DB_UpdateInkData(SQLiteDatabase writable, DResultData resultdata) {
		boolean ret = false;
		int putStatus = 0;
		//20140630 ADD-S For Ink Lost
		String inkfilepath =  StudentClientCommData.getInkTextFile(resultdata);
		File delfile = new File(inkfilepath);
		try {
			delfile.delete();
		}
		catch(Exception e){
		}
		//20140630 ADD-E For Ink Lost
		String inkbinarypath =  StudentClientCommData.getInkBinaryFile(resultdata);
		File delbinaryfile = new File(inkbinarypath);
		try {
			delbinaryfile.delete();
		}
		catch(Exception e){
		}

		//圧縮して保持
		byte[] zipbuff = null;
		try {
			//圧縮して保持
			if(resultdata.mInkBinary == null) {
				//InkData無し
				putStatus = 2;
			}
			else {
				KumonCommon kc = new KumonCommon();
				zipbuff = kc.GetZipCompressedDataBytes(resultdata.mInkBinary, TblInkData.SF_ZIP_ENTRY);
				kc = null;

				if(zipbuff.length < 800000) {
					//DB保存
					putStatus = 0;
				}
				else {
					try {
						FileOutputStream os = new FileOutputStream(inkbinarypath);
					    os.write(zipbuff);
					    os.flush();
					    os.close();
					} catch (Exception e) {
						return false;
					}					
					putStatus = 2;
				}
			}
			
			ContentValues values = new ContentValues();
			values.put(SF_COL_COUNT, resultdata.mCount);
			if(putStatus == 0) {
				values.put(SF_COL_INKDATA, "");
				values.putNull(SF_COL_INKDATAZIP);
				values.put(SF_COL_INKBINARY, zipbuff);
			}
			else {
				values.put(SF_COL_INKDATA, "");
				values.putNull(SF_COL_INKDATAZIP);
				values.putNull(SF_COL_INKBINARY);
			}

			if (writable.update(SF_TBLNAME, values, SF_COL_STUDENTID + " = ? "
					+ " AND " + SF_COL_KYOKAID + " = ? " + " AND "
					+ SF_COL_KYOZAIID + " = ? " + " AND " 
					+ SF_COL_PRINTUNITID + " = ? "
					, new String[] { resultdata.mStudentID,
					resultdata.mKyokaID, resultdata.mKyozaiID,
					resultdata.mPrintUnitID }) == 1) {
				ret = true;
			}
		} catch (Exception e) {
		}
		return ret;
	}
	
		
}
