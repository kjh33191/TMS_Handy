package kumon2014.common;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

/**
 * LoaderManager#restartLoader��ID�������Ǘ�����}�l�[�W���[
 * @author shinm
 *
 */
public class KumonLoaderManager {
	public static final int TASKID_MAINTENANCECHECK = 0;
	public static final int TASKID_GETRETRYRESULT = 1;
	public static final int TASKID_STUDENTGRADINGSTATUS = 2;
	public static final int TASKID_INITIALTASK = 3;
	public static final int TASKID_DOWNLOADPRINTSET = 4;
	public static final int TASKID_INITIALIZEQUESTION = 5;
	public static final int TASKID_REGISTGRADING = 6;
	public static final int TASKID_SETKYOZAILIST = 7;
	public static final int TASKID_STUDENTENTRANCE = 8;
	public static final int TASKID_RECEIVEPRINTSETID = 9;
	public static final int TASKID_RECEIVEPRINTSET = 10;
	public static final int TASKID_REGISTGRADINGRESULT = 11;
	public static final int TASKID_GETREADCOMMENT = 12;
	public static final int TASKID_LOGIN = 13;
	public static final int TASKID_VERSIONCHECK = 14;
	public static final int TASKID_GETDBPRINTSET = 15;
	public static final int TASKID_DOWNLOADAPK = 16;
	
//	HashMap<Loader<?>, Integer> mMap = new HashMap<Loader<?>, Integer>();
	static KumonLoaderManager mInstance = null;
	static KumonLoaderManager getInstance() {
		if (mInstance == null)
			mInstance = new KumonLoaderManager();
		return mInstance;
	}
	
	/**
	 * Loader�̊J�n
	 * @param activity �A�N�e�B�r�e�B
	 * @param arg �p�����[�^
	 * @param callback �R�[���o�b�N�C���X�^���X
	 * @return Loader
	 */
	public static Loader<?> startLoader(int TaskID, Activity activity, Bundle arg, LoaderCallbacks<?> callback) {
		Log.d("KumonLoaderManager#startLoader", "id = " + TaskID);
		Loader<?> loader = activity.getLoaderManager().restartLoader(TaskID, arg, callback);
//		getInstance().mMap.put(loader, TaskID);
		return loader;
	}
	
	/**
	 * �}�b�v����Loader/ID���폜
	 * KumonLoaderCallbacks#onLoaderReset����Ă΂��
	 * @param loader Loader
	 */
	static void resetLoader(Loader<?> loader) {
		/*
		Integer id = getInstance().mMap.get(loader);
		if (id != null) {
			Log.d("KumonLoaderManager#resetLoader", "id = " + id);
			getInstance().mMap.remove(loader);
		}
		else {
			Log.e("KumonLoaderManager#resetLoader", "id not found");
		}
		*/
	}
	
	public static abstract class KumonLoaderCallbacks<T> implements LoaderCallbacks<T> {
		@Override
		public final void onLoaderReset(Loader<T> loader) {
			KumonLoaderManager.resetLoader(loader);
		}
	}
}
