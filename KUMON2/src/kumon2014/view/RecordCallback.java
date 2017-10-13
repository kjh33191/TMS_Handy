package kumon2014.view;

public interface RecordCallback {
	public static final int SF_MODE_HIDE = 0;
	public static final int SF_MODE_STOPPING = 1;
	public static final int SF_MODE_RECORDING = 2;
	public static final int SF_MODE_PLAYING = 3;
	public static final int SF_MODE_PAUSE = 4;
	
	
	public static final int SF_RECORDICON_CLICK = 0;
	public static final int SF_RECORD_START = 1;
	public static final int SF_RECORD_STOP = 2;
	public static final int SF_RECORD_PLAY = 3;
	public static final int SF_RECORD_COMPLETION = 4;
	public static final int SF_RECORD_PAUSE = 5;

	//20150115 ADD-S 再生時もダイアログ表示
	public static final int SF_SOUNDICON_CLICK = 5;
	public static final int SF_SOUND_PAUSE = 6;
	//20150115 ADD-E 再生時もダイアログ表示
	/** MODE */  
	public static final int MODE = 0;  
	
	/** 
	* コールバックメソッド 
	*/  
	public void recordCallback(final int MODE, int pos);  
	
}
