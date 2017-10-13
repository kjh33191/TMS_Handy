package kumon2014.markcontroltool.control;

import java.io.File;
import android.widget.ImageView;

import pothos.mdtcommon.DataStructs.RecordData;
import kumon2014.markcontroltool.RotateImage;

public class IconRecord {

	public RecordData mRecordData;
	
    public ImageView ImageButton = null;

    public boolean IsRecording = false;
    
    public boolean mCanShowPlayer = false;
    public boolean mCanRecord = false;
    
	public IconRecord(boolean canRecord) {
		mCanRecord = canRecord;
	}

	public void  RecordStart() {
		IsRecording = true;
		//20140731 MOD-S For アイコン回転
		//ImageButton.setImageResource(R.drawable.icon_record_play);
		ImageButton.setImageBitmap(RotateImage.micon_record_play);
		//20140731 MOD-E For アイコン回転
		
	}
	public void RecordStop() {
		IsRecording = false;
		//ImageButton.setImageResource(R.drawable.icon_record_usually);
	}
	public void setImage() {
		File recordFile = new File(mRecordData.RecordFileName);
		if(recordFile.exists()) {
			//20140731 MOD-S For アイコン回転
			//ImageButton.setImageResource(R.drawable.icon_record_play);
			ImageButton.setImageBitmap(RotateImage.micon_record_play);
			//20140731 MOD-E For アイコン回転
			mCanShowPlayer = true;
		}
		else {
			if(mCanRecord) {
				//20140731 MOD-S For アイコン回転
				//ImageButton.setImageResource(R.drawable.icon_record_usually);
				ImageButton.setImageBitmap(RotateImage.micon_record_usually);
				//20140731 MOD-E For アイコン回転
				mCanShowPlayer = true;
			}
			else {
				ImageButton.setImageBitmap(RotateImage.micon_record_off);
				mCanShowPlayer = false;
			}
		}
				
	}
	public boolean IsFileExist() {
		boolean stat = false;
		File recordFile = new File(mRecordData.RecordFileName);
		if(recordFile.exists()) {
			stat = true;
		}
		return stat;
	}
}
