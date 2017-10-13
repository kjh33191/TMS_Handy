package kumon2014.markcontroltool.control;

import java.io.File;
import java.io.IOException;

import kumon2014.view.RecordCallback;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class KesAudioPlayer implements OnPreparedListener, OnCompletionListener, MediaRecorder.OnInfoListener{
//	private static String			SOUNDFILENAME = "MEMO.mp3";
	
    private MediaPlayer 			mPlayer = null;
    private RecordCallback			mRecordCallback = null;
    private String 					mSoundFileName = null;
	
	public KesAudioPlayer()
	{
		Clear();
	}
	@Override
	protected void finalize() throws Throwable {
	    try{
	        super.finalize();
	    }finally{
	        dispose();
	    }
	}
	 private void dispose(){
		 mPlayer = null;
		 mRecordCallback = null;
    }
	 
	private void Clear() {
		if(mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
		else {
			if(mPlayer.isPlaying()) {
				mPlayer.stop();
				try {
					mPlayer.prepare();
				} catch (IOException e){}
			}
			mPlayer.reset();
		}
		mSoundFileName = null;
	}
	
	public void setRecordCallBack(final RecordCallback callback) {
		mRecordCallback = callback;
	}
	public void SetSound(String soundfile) {
		mSoundFileName = soundfile;
		
	}
	public void Play_Stop() {
		if(mPlayer != null) {
			if(mPlayer.isPlaying()) {
				mPlayer.stop();
				try {
					mPlayer.prepare();
				} catch (IOException e){}
			}
			mPlayer.reset();
		}
	}
    public void Play_Start() {

    	if(mSoundFileName != null) {
    		try {
    			if(mPlayer == null) {
    				mPlayer = new MediaPlayer();
    			}
    			else {
    				if(mPlayer.isPlaying()) {
    					mPlayer.stop();
    					try {
    						mPlayer.prepare();
    					} catch (IOException e){}
    				}
    				mPlayer.reset();
    			}
    			mPlayer.setDataSource(mSoundFileName);
    			mPlayer.prepareAsync();
    			mPlayer.setOnPreparedListener(this);				
    			mPlayer.setOnCompletionListener(this);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }
    public void Play_ReStart() {
		if(mPlayer != null) {
			mPlayer.start();
		}
    }
    public void Play_Pause() {
		if(mPlayer != null) {
			mPlayer.pause();
		}
    }
    
	public int Get_Duration() {
		int rectime = 0;
		File soundfile = new File(mSoundFileName);
		if(soundfile.exists()) {
			try {
				if(mPlayer == null) {
					mPlayer = new MediaPlayer();
				}
				else {
					if(mPlayer.isPlaying()) {
						mPlayer.stop();
						try {
							mPlayer.prepare();
						} catch (IOException e){}
					}
					mPlayer.reset();
				}
				mPlayer.setDataSource(soundfile.getAbsolutePath());
				mPlayer.prepare(); 
				rectime = mPlayer.getDuration();
			} catch (Exception e) {
			}
			
			if(mPlayer != null) {
				if(mPlayer.isPlaying()) {
					mPlayer.stop();
					try {
						mPlayer.prepare();
					} catch (IOException e){}
				}
				mPlayer.reset();
				mPlayer = null;
			}
		}	
		return rectime;
	}
	@Override
	public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		if(mRecordCallback != null) {
			mRecordCallback.recordCallback(RecordCallback.SF_RECORD_STOP, 0);
		}
	}
	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		mPlayer.start();		
	}
	
}
