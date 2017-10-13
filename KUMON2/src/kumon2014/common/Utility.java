package kumon2014.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.nio.channels.FileChannel;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Utility {

	/**
	 * �Í���
	 */
	public static byte[] encode(byte[] src, Key skey) {
		try {
//			MyTimingLogger logger = new MyTimingLogger("Utility#encode");
			Cipher cipher = Cipher.getInstance("AES");
//			logger.addSplit("Cipher.getInstance");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
//			logger.addSplit("cipher.init");
			byte[] ret = cipher.doFinal(src);
//			logger.addSplit("cipher.doFinal");
//			logger.dumpToLog();
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ������
	 */
	public static byte[] decode(byte[] src, Key skey) {
		try {
//			MyTimingLogger logger = new MyTimingLogger("Utility#decode");
			Cipher cipher = Cipher.getInstance("AES");
//			logger.addSplit("Cipher.getInstance");
			cipher.init(Cipher.DECRYPT_MODE, skey);
//			logger.addSplit("cipher.init");
			byte [] ret = cipher.doFinal(src);
//			logger.addSplit("cipher.doFinal");
//			logger.dumpToLog();
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static final SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPAN);
	public static String getFormatDate(Date date) {
		if (date == null) {
			return "";
		}
		return mSdf.format(date);
	}

	public static Date getDateFromString(String formatdate) {
		Date date = null;
		try {
			date = mSdf.parse(formatdate);
		} catch (Exception e) {

		}
		return date;
	}

	public static String getSoapFormatDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.JAPAN);
		return sdf.format(date);
	}

	public static String getSoapFormatDateFromString(String date) {
		String ret = "";
		
		if(date.length() != 8) {
			Date wkdate = new Date();
			ret = getSoapFormatDate(wkdate);
		}
		else {
			ret = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + "T00:00:00.000Z";
		}
		
		return ret;
	}

	public static Date getDateSoapFromString(String formatdate) {
		Date date = null;
		SimpleDateFormat sdf = null;
		String temp = formatdate.replace('T', ' ');
		if(temp.indexOf(".") < 0) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPAN);
		}
		else {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.JAPAN);
		}
		try {
			date = sdf.parse(temp);
		} catch (Exception e) {
//			String s = e.getMessage();
		}
		return date;
	}

	/**
	 * Object���o�C�g�z���
	 */
	public static byte[] cvtObjToByteArray(Object obj) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream oout;
		try {
			oout = new ObjectOutputStream(bout);
			oout.writeObject(obj);
			oout.close();
			bout.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			return null;
		}

		return bout.toByteArray();
	}

	/**
	 * InputStream���o�C�g�z���
	 */
	public static byte[] cvtStreamToByteArray(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream os = new BufferedOutputStream(baos);
		int c;
		try {
			while ((c = is.read()) != -1) {
				os.write(c);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (Exception e) {
				}
			}
		}

		return baos.toByteArray();
	}

	public static String digest(String str) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1"); // ���邢��MD5�Ȃǁ��A���S���Y����
			md.reset();
			md.update(str.getBytes());
			byte[] hash = md.digest();

			// �n�b�V����16�i��������ɕϊ�
			String h = new String(Hex.encodeHex(hash)).toUpperCase(Locale.JAPAN);
			return h;
			
		} catch (Exception e) {
		}
		return "";
	}
	public static String digest256(String str) {
		MessageDigest md256;
		try {
			md256 = MessageDigest.getInstance("SHA-256");
			md256.reset();
			md256.update(str.getBytes());
			byte[] hash256 = md256.digest();
			// �n�b�V����16�i��������ɕϊ�
			String h256 = new String(Hex.encodeHex(hash256)).toUpperCase(Locale.JAPAN);

			return h256;
			
		} catch (Exception e) {
		}
		return "";
	}

	public static boolean copyDirectry(File sDirectry, File tDirectry) {
		// �R�s�[�����f�B���N�g���łȂ��ꍇ��false��Ԃ�
		if (!sDirectry.exists() || !sDirectry.isDirectory())
			return false;
		// �f�B���N�g�����쐬����
		tDirectry.mkdirs();
		// �f�B���N�g�����̃t�@�C�������ׂĎ擾����
		File[] files = sDirectry.listFiles();

		// �f�B���N�g�����̃t�@�C���ɑ΂��R�s�[�������s��
		for (int i = 0; files.length > i; i++) {
			if (files[i].isDirectory()) {
				// �f�B���N�g���������ꍇ�͍ċA�Ăяo�����s��
				copyDirectry(
						new File(sDirectry.toString(), files[i].getName()),
						new File(tDirectry.toString(), files[i].getName()));
			} else {
				// �t�@�C���������ꍇ�̓t�@�C���R�s�[�������s��
				copyFile(new File(sDirectry.toString(), files[i].getName()),
						new File(tDirectry.toString(), files[i].getName()));
			}
		}
		return true;
	}

	public static boolean copyFile(File src, File dst) {
		boolean ret = false;
		FileChannel ifc = null;
		FileChannel ofc = null;
		try {
			// ���͌��t�@�C���̃X�g���[������`���l�����擾
			FileInputStream fis = new FileInputStream(src);
			ifc = fis.getChannel();

			// �o�͐�t�@�C���̃`���l�����擾
			FileOutputStream fos = new FileOutputStream(dst);
			ofc = fos.getChannel();

			// �o�C�g��]�����܂��B
			ifc.transferTo(0, ifc.size(), ofc);
			fis.close();
			fos.close();
			ret = true;

		} catch (Exception e) {
		} finally {
			if (ifc != null) {
				try {
					// ���̓`���l���� close ���܂��B
					ifc.close();
				} catch (Exception e) {
				}
			}
			if (ofc != null) {
				try {
					// �o�̓`���l���� close ���܂��B
					ofc.close();
				} catch (Exception e) {
				}
			}
		}

		return ret;
	}

	public static void deleteDirectry(File folder) {
		if (folder.exists() == false) {
			return;
		}

		if (folder.isFile()) {
			folder.delete();
		}

		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteDirectry(files[i]);
			}
			folder.delete();
		}
	}

	public static int strToInt(String text) {
		try {
			return Integer.valueOf(text);
		} catch (Exception e) {

		}
		return 0;
	}

	public static void memory(Context con) {
		// �V�X�e���ŗ��p�\�ȋ󂫃������[
		ActivityManager am = (ActivityManager) con
				.getSystemService(Activity.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		// ���p�\�ȃ������[�T�C�Y
		Log.i("MemoryInfo", "availMem:" + mi.availMem);
		// ���p�\�ȃ������[�T�C�Y���s���̎��A������邩���f����A�������l(B)
		Log.i("MemoryInfo", "threshold:" + mi.threshold);
		// �V�X�e�����������s���Ɣ��f���Ă��邩
		Log.i("MemoryInfo", "lowMemory:" + mi.lowMemory);
		// ���v���Z�X���g�p���̃������[
		int[] pids = new int[1];
		pids[0] = android.os.Process.myPid();
		android.os.Debug.MemoryInfo[] dmi = am.getProcessMemoryInfo(pids);
		// �g�p���̃������[�T�C�Y(KB)
		Log.i("ProcessMemoryInfo",
				"TotalPrivate:" + dmi[0].getTotalPrivateDirty());
		// �v���Z�X�̎g�p���������v�T�C�Y(KB)
		Log.i("ProcessMemoryInfo", "TotalPss:" + dmi[0].getTotalPss());
		// ���L�������[�̎g�p���v�T�C�Y(KB)
		Log.i("ProcessMemoryInfo",
				"TotalShared:" + dmi[0].getTotalSharedDirty());
	}

	public static void onLowMemory(Context con) {
		// �V�X�e���ŗ��p�\�ȋ󂫃������[
		ActivityManager am = (ActivityManager) con
				.getSystemService(Activity.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		// ���p�\�ȃ������[�T�C�Y
		// Log.i("MemoryInfo","availMem:" + mi.availMem);
		// ���p�\�ȃ������[�T�C�Y���s���̎��A������邩���f����A�������l(B)
		// Log.i("MemoryInfo","threshold:" + mi.threshold);
		// �V�X�e�����������s���Ɣ��f���Ă��邩
		// Log.i("MemoryInfo","lowMemory:" + mi.lowMemory);
		// ���v���Z�X���g�p���̃������[
		int[] pids = new int[1];
		pids[0] = android.os.Process.myPid();
		android.os.Debug.MemoryInfo[] dmi = am.getProcessMemoryInfo(pids);
		// �g�p���̃������[�T�C�Y(KB)
		// Log.i("ProcessMemoryInfo","TotalPrivate:" +
		// dmi[0].getTotalPrivateDirty());
		// �v���Z�X�̎g�p���������v�T�C�Y(KB)
		// Log.i("ProcessMemoryInfo","TotalPss:" + dmi[0].getTotalPss());
		// ���L�������[�̎g�p���v�T�C�Y(KB)
		// Log.i("ProcessMemoryInfo","TotalShared:" +
		// dmi[0].getTotalSharedDirty());
		Toast.makeText(
				con,
				"Private" + dmi[0].getTotalPrivateDirty() + "�v���Z�X"
						+ dmi[0].getTotalPss(), Toast.LENGTH_LONG).show();

	}

	public static final void cleanupView(View view) {
		if (view instanceof ImageButton) {
			ImageButton ib = (ImageButton) view;
			ib.setImageDrawable(null);
		} else if (view instanceof ImageView) {
			ImageView iv = (ImageView) view;
			iv.setImageDrawable(null);
		}
		// view.setBackground(null);
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			int size = vg.getChildCount();
			for (int i = 0; i < size; i++) {
				cleanupView(vg.getChildAt(i));
			}
		}
	}
	public static void DebugTimePass(String msg) {
		long time = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS", Locale.JAPAN);
		Log.e("PASS TIME", msg + ":" + sdf.format(time));
	}
/*	
	public static float parseFloat(String f) {
		final int len   = f.length();
		float     ret   = 0f;         // return value
		int       pos   = 0;          // read pointer position
		int       part  = 0;          // the current part (int, float and sci parts of the number)
		boolean   neg   = false;      // true if part is a negative number
	 
		// find start
		while (pos < len && (f.charAt(pos) < '0' || f.charAt(pos) > '9') && f.charAt(pos) != '-' && f.charAt(pos) != '.')
			pos++;
	 
		// sign
		if (f.charAt(pos) == '-') { 
			neg = true; 
			pos++; 
		}
	 
		// integer part
		while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0'))
			part = part*10 + (f.charAt(pos++) - '0');
		ret = neg ? (float)(part*-1) : (float)part;
	 
		// float part
		if (pos < len && f.charAt(pos) == '.') {
			pos++;
			int mul = 1;
			part = 0;
			while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0')) {
				part = part*10 + (f.charAt(pos) - '0'); 
				mul*=10; pos++;
			}
			ret = neg ? ret - (float)part / (float)mul : ret + (float)part / (float)mul;
		}
	 
		// scientific part
		if (pos < len && (f.charAt(pos) == 'e' || f.charAt(pos) == 'E')) {
			pos++;
			neg = (f.charAt(pos) == '-'); pos++;
			part = 0;
			while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0')) {
				part = part*10 + (f.charAt(pos++) - '0'); 
			}
			if (neg)
				ret = ret / (float)Math.pow(10, part);
			else
				ret = ret * (float)Math.pow(10, part);
		}	
		return ret;
	}
*/
	static Boolean isDebug = null;
	/**
	 * �f�o�b�O�r���h���ۂ�
	 * @param context
	 * @return
	 */
	public static boolean isDebugBuild(Context context) {
		PackageManager manager = context.getPackageManager();
        ApplicationInfo info = null;
        if (isDebug == null) {
        	isDebug = Boolean.FALSE;
	        try {
	            info = manager.getApplicationInfo(context.getPackageName(), 0);
		        if ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE) {
		            isDebug = Boolean.TRUE;
		        }
	        } catch (NameNotFoundException e) {
//	            isDebug = Boolean.FALSE;
	        }
        }
        return isDebug;
	}
	
	/**
	 * ������̃G�X�P�[�v
	 * @param s
	 * @return
	 */
	public static String EscapeString(String s) {
		String ret = s.replaceAll("'", "''");
		return ret;
	}
}
