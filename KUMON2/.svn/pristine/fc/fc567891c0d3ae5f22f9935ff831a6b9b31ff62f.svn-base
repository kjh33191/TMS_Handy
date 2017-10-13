package kumon2014.markcontroltool;

import kumon2014.activity.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

//20140731 ADD-S アイコン回転
public class RotateImage {
	//採点
	public static Bitmap    	mgd_right_orange;
	public static Bitmap    	mgd_right_white;
	public static Bitmap    	mgd_wrong_norect_white;
	public static Bitmap    	mgd_wrong_orange;
	public static Bitmap    	mgd_wrong_white;
	public static Bitmap    	mgd_wrongright_norect_white;
	public static Bitmap    	mgd_wrongright_orange;
	public static Bitmap    	mgd_wrongright_white;

	public static Bitmap    	mgd_triangle;
	public static Bitmap    	mgd_triangle_orange;
	public static Bitmap    	mgd_triangle_white;
	public static Bitmap    	mgd_triangleright;
	public static Bitmap    	mgd_triangleright_orange;
	public static Bitmap    	mgd_triangleright_white;
	
	//Sound
	public static Bitmap    	micon_speaker_off;
	public static Bitmap    	micon_speaker_on;

	//Record
	public static Bitmap    	micon_record_play;
	public static Bitmap    	micon_record_usually;
	public static Bitmap    	micon_record_off;

	public static void makeRotateImageComm(boolean LandscapeOrientation, Resources res) {

	  	if(LandscapeOrientation) {
	  		Matrix mMatrix = new Matrix();
	  		mMatrix.reset();
	  		mMatrix.postRotate(270);
	
	  		Bitmap work ;
	 				
	  		work = BitmapFactory.decodeResource(res, R.drawable.icon_speaker_off);
	  		micon_speaker_off = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  
	
	  		work = BitmapFactory.decodeResource(res, R.drawable.icon_speaker_on);
	  		micon_speaker_on = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.icon_record_play);
	  		micon_record_play = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  
	
	  		work = BitmapFactory.decodeResource(res, R.drawable.icon_record_usually);
	  		micon_record_usually = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  
	  		
	  		work = BitmapFactory.decodeResource(res, R.drawable.icon_record_off);
	  		micon_record_off = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  
	  		
	  	}
	  	else {
	  		micon_speaker_off = BitmapFactory.decodeResource(res, R.drawable.icon_speaker_off);
	  		micon_speaker_on = BitmapFactory.decodeResource(res, R.drawable.icon_speaker_on);

	  		micon_record_play = BitmapFactory.decodeResource(res, R.drawable.icon_record_play);
	  		micon_record_usually = BitmapFactory.decodeResource(res, R.drawable.icon_record_usually);
	  		micon_record_off = BitmapFactory.decodeResource(res, R.drawable.icon_record_off);
	  	}
	}
	public static void makeRotateImageGrade(boolean LandscapeOrientation, Resources res) {
	
		if(LandscapeOrientation) {
	  		Matrix mMatrix = new Matrix();
	  		mMatrix.reset();
	  		mMatrix.postRotate(270);
	
	  		Bitmap work ;
	  		
	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_right_orange);
	  		mgd_right_orange = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_right_white);
	  		mgd_right_white = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_wrong_norect_white);
	  		mgd_wrong_norect_white = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_wrong_orange);
	  		mgd_wrong_orange = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_wrong_white);
	  		mgd_wrong_white = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_wrongright_norect_white);
	  		mgd_wrongright_norect_white = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_wrongright_orange);
	  		mgd_wrongright_orange = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_wrongright_white);
	  		mgd_wrongright_white = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  
	  		//
	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_triangle);
	  		mgd_triangle = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_triangle_orange);
	  		mgd_triangle_orange = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_triangle_white);
	  		mgd_triangle_white = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_triangleright);
	  		mgd_triangleright = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_triangleright_orange);
	  		mgd_triangleright_orange = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  		work = BitmapFactory.decodeResource(res, R.drawable.gd_triangleright_white);
	  		mgd_triangleright_white = Bitmap.createBitmap(work, 0, 0, work.getWidth(), work.getHeight(), mMatrix, true);  

	  	}
	  	else {
	  		mgd_right_orange = BitmapFactory.decodeResource(res, R.drawable.gd_right_orange);
	  		mgd_right_white = BitmapFactory.decodeResource(res, R.drawable.gd_right_white);
	  		mgd_wrong_norect_white = BitmapFactory.decodeResource(res, R.drawable.gd_wrong_norect_white);
	  		mgd_wrong_orange = BitmapFactory.decodeResource(res, R.drawable.gd_wrong_orange);
	  		mgd_wrong_white = BitmapFactory.decodeResource(res, R.drawable.gd_wrong_white);
	  		mgd_wrongright_norect_white = BitmapFactory.decodeResource(res, R.drawable.gd_wrongright_norect_white);
	  		mgd_wrongright_orange = BitmapFactory.decodeResource(res, R.drawable.gd_wrongright_orange);
	  		mgd_wrongright_white = BitmapFactory.decodeResource(res, R.drawable.gd_wrongright_white);

	  		mgd_triangle = BitmapFactory.decodeResource(res, R.drawable.gd_triangle);
	  		mgd_triangle_orange = BitmapFactory.decodeResource(res, R.drawable.gd_triangle_orange);
	  		mgd_triangle_white = BitmapFactory.decodeResource(res, R.drawable.gd_triangle_white);
	  		mgd_triangleright = BitmapFactory.decodeResource(res, R.drawable.gd_triangleright);
	  		mgd_triangleright_orange = BitmapFactory.decodeResource(res, R.drawable.gd_triangleright_orange);
	  		mgd_triangleright_white = BitmapFactory.decodeResource(res, R.drawable.gd_triangleright_white);
	  	}
	}
}
//20140731 ADD-E
