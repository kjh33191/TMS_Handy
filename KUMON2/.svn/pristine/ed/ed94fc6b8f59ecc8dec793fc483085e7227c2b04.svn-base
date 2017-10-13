package kumon2014.markcontroltool.control;

import java.io.File;
import java.io.FileOutputStream;

import android.widget.ImageView;
import kumon2014.common.StudentClientCommData;
import kumon2014.markcontroltool.RotateImage;
import pothos.mdtcommon.DataStructs.SoundData;

public class IconSound {

	public SoundData mSoundData;

    public ImageView ImageButton = null;

	public byte[]	mSound = null;

    public boolean IsPalying = false;

	public IconSound() {
	}

	public void  Play() {
		IsPalying = true;
		//20140731 MOD-S For アイコン回転
		//ImageButton.setImageResource(R.drawable.icon_speaker_on);
		ImageButton.setImageBitmap(RotateImage.micon_speaker_on);
		//20140731 MOD-E For アイコン回転

	}
	public void Stop() {
		IsPalying = false;
		//20140731 MOD-S For アイコン回転
		//ImageButton.setImageResource(R.drawable.icon_speaker_off);
		ImageButton.setImageBitmap(RotateImage.micon_speaker_off);
		//20140731 MOD-E For アイコン回転
	}
	public void Save() {

		mSoundData.SoundFilePath = "";
		File soundfile = new File(StudentClientCommData.getSoundFolder(), mSoundData.MdtDataID + mSoundData.SoundFileName);
		if(soundfile.exists() == false) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(soundfile);
				fos.write(mSound);
				fos.close();
				mSoundData.SoundFilePath = soundfile.toString();
			} catch (Exception e) {
				try {
					if (fos!=null) fos.close();
				} catch (Exception e2) {
				}
			}
		}
	}
}
