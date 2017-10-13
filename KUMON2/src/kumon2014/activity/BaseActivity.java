package kumon2014.activity;

import java.lang.ref.WeakReference;
import java.util.Vector;

import kumon2014.common.DebugToast;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.message.KumonMessage;
import kumon2014.message.KumonMessage.KumonMessageDetail;
import kumon2014.webservice.KumonSoap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * アプリ内すべてのアクティビティのベースクラス
 *
 */
@SuppressLint("Registered")
public class BaseActivity extends Activity {
	/***
	 * ボタンの一覧
	 */
	protected Vector<View> mUIButtons = null; 
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (!BuildConfig.DEBUG) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				int keyCode = event.getKeyCode();
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					// 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
					return true;
					// 20130723 ADD-S For SoftKeyboard
				case KeyEvent.KEYCODE_MENU:
					// 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
					return true;
					// 20130723 ADD-E For SoftKeyboard
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 作成時
	 * setSystemUiVisibilityをナビゲーションバー字表示に設定
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  /*| View.SYSTEM_UI_FLAG_FULLSCREEN*/ | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        canceled = false;
	}

	/**
	 * 再開時
	 * setSystemUiVisibilityをonCreate時と同様に
	 */
	@Override
	protected void onResume() {
		super.onResume();
		View decor = this.getWindow().getDecorView();
		int vis = decor.getSystemUiVisibility();
		if (((vis & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) ||
//				((vis & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) ||
				((vis & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == 0)) {
	        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  /*| View.SYSTEM_UI_FLAG_FULLSCREEN*/ | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);		
		}
	}


	/**
	 * 戻るボタン押下
	 * リリースビルド時は無視するように
	 */
	@Override
	public void onBackPressed() {
		if (BuildConfig.DEBUG) {
			super.onBackPressed();
		}
	}

	/**
	 * トップビュー以下に含まれるボタン類の列挙
	 * @return
	 */
	protected Vector<View> enumViews() {
		View view = getWindow().getDecorView();
		if (view != null) {
			Vector<View> list = new Vector<View>();
			enumViewRecursive(view, list);
			return list;
		}
		return null;
	}
	protected static void enumViewRecursive(View view, Vector<View> list) {
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup)view;
			int count = vg.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = vg.getChildAt(i);
				list.add(child);
				if (child instanceof ViewGroup) {
					enumViewRecursive(child, list);
				}
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if (mUIButtons == null) {
			mUIButtons = enumViews();
	        Log.d(this.getClass().getSimpleName(), "count of Button = " + mUIButtons.size());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(getClass().getSimpleName()+"#onDestroy", "destroy");
	}

	/**
	 * クリック時maintenanceCheckを呼ぶ時に使用するハンドラ
	 * @author shinm
	 *
	 */
	static class ClickHandler<T> extends Handler {
		public WeakReference<T> mActivityRef;
		public ClickHandler(T act) {
			mActivityRef = new WeakReference<T>(act);
		}
	}
	
	// 20150110 ADD-S For 2015年度Ver. メンテナンス中チェック
	boolean maintenanceCheck(final Handler handler) {
		// 20160217↓
		boolean stat = false;
		
		KumonLoaderCallbacks<Boolean> maintenanceCallback = new KumonLoaderCallbacks<Boolean>() {
			@Override
			public Loader<Boolean> onCreateLoader(int arg0, Bundle arg1) {
				AsyncTaskLoader<Boolean> loader = new AsyncTaskLoader<Boolean>(BaseActivity.this) {
					@Override
					public Boolean loadInBackground() {
						KumonSoap soap = new KumonSoap();
						boolean ret = soap.SoapMaintenanceCheck();
						return ret;
					}
				};
				loader.forceLoad();
				return loader;
			}
			@Override
			public void onLoadFinished(Loader<Boolean> loader,
					Boolean Result) {
				if (Result.booleanValue()) {
					DebugToast.makeText(BaseActivity.this, "Maintenancing", Toast.LENGTH_SHORT);
				}
				handler.sendEmptyMessage(Result ? 1 : 0);
			}
		};
        KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_MAINTENANCECHECK, this, null, maintenanceCallback);

		return stat;
		// 20160217↑
		// boolean stat = false;

		// KumonSoap soap = new KumonSoap();
		// stat = soap.SoapMaintenanceCheck();
		// if(stat == true) {
		// ScreenChange.doScreenChange(this, ScreenChange.SCNO_TOP,
		// ScreenChange.SCNO_MAINTENANCE, false, mCurrentUser, 0,
		// ScreenChange.SF_NEXT);
		// }
		// return stat;
	}

	/**
	 * すべてのViewをクリッカブル/非クリッカブルにする
	 * @param clickable
	 */
	void setAllViewsClickable(boolean clickable)
	{
		Vector<View> views = this.enumViews();
		for(View v : views) {
			v.setClickable(clickable);
		}
	}
	
	Vector<View> mViews = null;
	View mProgressView = null;
	View.OnClickListener mYesListener = null;
	View.OnClickListener mNoListener = null;
	View.OnClickListener mOkListener = null;
	
	public boolean canceled = false; 
	/**
	 * プログレスバーの表示
	 * @param layoutID
	 * @param msgID
	 */
	void showProgress(int layoutID, int msgID) {
		canceled = false; 
		View view = this.getLayoutInflater().inflate(layoutID, null);
		ViewGroup decor = (ViewGroup)getWindow().getDecorView();
		
		if (mProgressView != null) {
			decor.removeView(mProgressView);
		}
		mViews = this.enumViews();
		for(View v : mViews) {
			v.setClickable(false);
		}
		
		decor.addView(view);
		if (msgID != 0) {
			KumonMessageDetail msg = KumonMessage.getKumonMessageDetail(msgID);
			if (msg != null) {
				if (msg.mTitle == null || msg.mTitle.length() == 0) {
					findViewById(R.id.progress_title).setVisibility(View.GONE);
				}
				else
					((TextView)findViewById(R.id.progress_title)).setText(msg.mTitle);
				((TextView)findViewById(R.id.progress_message)).setText(msg.mMessage);
			}
		}
		if (layoutID == R.layout.progress_msg_spinner) {
			ProgressBar pb = (ProgressBar)findViewById(R.id.progress_progress);
			if (pb != null) {
				pb.setMax(100);
				pb.incrementProgressBy(0);
			}
			Button btn = (Button)findViewById(R.id.cancel_button);
			if (btn != null) {
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						canceled = true;
					}
				});
			}
		}
		mProgressView = view;
	}
	void showYesNoDialog(int layoutID, int msgID, int yesTextID, View.OnClickListener yesListener, int noTextID, View.OnClickListener noListener) {
		View view = this.getLayoutInflater().inflate(layoutID, null);
		ViewGroup decor = (ViewGroup)getWindow().getDecorView();
		mYesListener = yesListener;
		mNoListener = noListener;
		
		if (mProgressView != null) {
			decor.removeView(mProgressView);
		}
		mViews = this.enumViews();
		for(View v : mViews) {
			v.setClickable(false);
		}
		
		decor.addView(view);
		if (msgID != 0) {
			KumonMessageDetail msg = KumonMessage.getKumonMessageDetail(msgID);
			if (msg != null) {
				if (msg.mTitle == null || msg.mTitle.length() == 0) {
					findViewById(R.id.progress_title).setVisibility(View.GONE);
				}
				else
					((TextView)findViewById(R.id.progress_title)).setText(msg.mTitle);
				((TextView)findViewById(R.id.progress_message)).setText(msg.mMessage);
			}
		}
		Button btn = (Button)findViewById(R.id.progress_yes_button);
		if (yesTextID != 0) {
			btn.setText(yesTextID);
		}
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeProgress();
				if (mYesListener != null) {
					mYesListener.onClick(v);
				}
			}
		});
		
		btn = (Button)findViewById(R.id.progress_no_button);
		if (noTextID != 0) {
			btn.setText(noTextID);
		}
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeProgress();
				if (mNoListener != null) {
					mNoListener.onClick(v);
				}
			}
		});
		mProgressView = view;
	}

	void showOkDialog(int layoutID, String title, String msg, int okTextID, View.OnClickListener okListener) {
		View view = this.getLayoutInflater().inflate(layoutID, null);
		ViewGroup decor = (ViewGroup)getWindow().getDecorView();
		mOkListener = okListener;
		
		if (mProgressView != null) {
			decor.removeView(mProgressView);
		}
		mViews = this.enumViews();
		for(View v : mViews) {
			v.setClickable(false);
		}
		
		decor.addView(view);
		if (title == null || title.length() == 0) {
			findViewById(R.id.progress_title).setVisibility(View.GONE);
		} else
			((TextView) findViewById(R.id.progress_title)).setText(title);
		((TextView) findViewById(R.id.progress_message)).setText(msg);
		Button btn = (Button)findViewById(R.id.progress_ok_button);
		if (okTextID != 0) {
			btn.setText(okTextID);
		}
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeProgress();
				if (mOkListener != null) {
					mOkListener.onClick(v);
				}
			}
		});
		
		mProgressView = view;
	}
	
	
	void showOkDialog(int layoutID, int msgID, int okTextID, View.OnClickListener okListener) {
		String title = "";
		String message = "";
		if (msgID != 0) {
			KumonMessageDetail msg = KumonMessage.getKumonMessageDetail(msgID);
			if (msg != null) {
				title = msg.mTitle;
				message = msg.mMessage;
			}
		}
		showOkDialog(layoutID, title, message, okTextID, okListener);
	}
	
	/**
	 * プログレスバーの更新
	 * @param n
	 */
	void updateProgress(int n) {
		ProgressBar pb = (ProgressBar)findViewById(R.id.progress_progress);
		if (pb != null) {
			pb.setProgress(n);
		}
	}

	/**
	 * プログレスバーを閉じる
	 */
	void closeProgress() {
		ViewGroup decor = (ViewGroup)getWindow().getDecorView();
		if (mProgressView != null) {
			decor.removeView(mProgressView);
		}
		if (mViews != null)
			for(View v : mViews) {
				v.setClickable(true);
			}
		mViews = null;
		mProgressView = null;
	}
}
