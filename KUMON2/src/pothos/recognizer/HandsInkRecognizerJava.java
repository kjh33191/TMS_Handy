/*
 * Copyright (C) 2012 Pothos Corporation
 */

package pothos.recognizer;

import pothos.markcontroltool.Filter.CRecogCategory;

public class HandsInkRecognizerJava {

    private static String dicpath = android.os.Environment.getExternalStorageDirectory().getPath() + "/Kumon2/dic/";


	private static HandsInkRecognizer recognizer = null;
	private static boolean initialized = false;


	public static boolean Open()
	{
		if(initialized == false) {
			try {
				recognizer = new HandsInkRecognizer();
				recognizer.HandsInkSetDictionaryPath(dicpath);
				recognizer.HandsInkInitialize();
				initialized = true;
			}
			catch(Exception e) {
				initialized = false;
			}
		}

		return initialized;
	}
	public static boolean Close() {
		if(initialized) {
			recognizer.HandsInkClose();
		}
		initialized = false;
		return true;
	}
	public static void SetMultiCategory(CRecogCategory category)
	{
		if(initialized) {
			if (category.m_All)
			{
				setCategoryFilter(true, true, true, true, true, true, true, true, true);
				clearCustomCharacterFilter();
			}
			else
			{
				clearCustomCharacterFilter();

				setCategoryFilter(category.m_Hiragana, category.m_Katakana, category.m_Alphabet, category.m_Kanji, category.m_Kanji,
															category.m_Numeric, category.m_Symbol, category.m_Greek, category.m_Punctuation);
				if (category.m_Custom)
				{
					setCustomCharacterFilter(category.m_CustomString);
				}
			}
		}
	}


//	public static String MultRecognize(ArrayList<CInkStroke> inkdata, Rect rct, boolean is1Line)
//	{
//		String recogstring = "";
//		if(initialized) {
//			try {
//				setOneLineRecognition(is1Line);
//
//				recognizer.HandsInkClear();
//
//				for(int i = 0; i < inkdata.size(); i++) {
//					CInkStroke inkstroke = inkdata.get(i);
//					if(inkstroke.Cells.size() > 0) {
//
//						Point[] pts = new Point[inkstroke.Cells.size()];
//						for(int j = 0; j < inkstroke.Cells.size(); j++) {
//							pts[j] = new Point();
//							CInkCell c = (CInkCell) inkstroke.Cells.get(j);
//
//							pts[j].x = (int)c.mfPointX;
//							pts[j].y = (int)c.mfPointY;
//						}
//						recognizer.HandsInkAddStroke(pts);
//					}
//				}
//				recogstring =  recognizer.HandsInkRecognize();
//			}
//			catch(Exception e) {
//				recogstring = "";
//			}
//		}
//		return recogstring;
//	}

//	public static String SingleRecognize(ArrayList<CInkStroke> inkdata, Rect rct)
//	{
//		String recogstring = "";
//		if(initialized) {
//			try {
//				recognizer.HandsInkClear();
//
//				for(int i = 0; i < inkdata.size(); i++) {
//					CInkStroke inkstroke = inkdata.get(i);
//					if(inkstroke.Cells.size() > 0) {
//
//						Point[] pts = new Point[inkstroke.Cells.size()];
//						for(int j = 0; j < inkstroke.Cells.size(); j++) {
//							pts[j] = new Point();
//							CInkCell c = (CInkCell) inkstroke.Cells.get(j);
//
//							pts[j].x = (int)c.mfPointX;
//							pts[j].y = (int)c.mfPointY;
//						}
//						recognizer.HandsInkAddStroke(pts);
//					}
//				}
//				recogstring =  recognizer.HandsInkRecognize();
//			}
//			catch(Exception e) {
//				recogstring = "";
//			}
//		}
//
//		return recogstring;
//	}

	public static RecognitionResult getRecognitionResult() {
		return recognizer.HandsInkGetRecognitionResult();
	}

	public static void setOneLineRecognition(boolean flag) {
		recognizer.HandsInkSetOneLineRecognition(flag);
	}

	public static String recognizeAsSingleCharacter() {
		return recognizer.HandsInkRecognizeAsSingleCharacter();
	}

	public static ProcessingStatus addBoundingBox(int left, int top, int right, int bottom) {
		return recognizer.HandsInkAddBoundingBox(left, top, right, bottom);
	}

	public static void setCategoryFilter(boolean checkHiragana, boolean checkKatakana, boolean checkAlphabet,
			boolean checkJis1Kanji, boolean checkJis2Kanji, boolean checkNumeric, boolean checkSymbol,
			boolean checkGreek, boolean checkPunctuation) {

		recognizer.HandsInkSetHiragana(checkHiragana);
		recognizer.HandsInkSetKatakana(checkKatakana);
		recognizer.HandsInkSetAlphabet(checkAlphabet);
		recognizer.HandsInkSetJis1Kanji(checkJis1Kanji);
		recognizer.HandsInkSetJis2Kanji(checkJis2Kanji);
		recognizer.HandsInkSetNumeric(checkNumeric);
		recognizer.HandsInkSetSymbol(checkSymbol);
		recognizer.HandsInkSetGreek(checkGreek);
		recognizer.HandsInkSetPunctuation(checkPunctuation);
	}

	public static void setCustomCharacterFilter(String str) {
		recognizer.HandsInkSetCustomCharacterFilter(str);
	}

	public static String getCustomCharacterFilter() {
		return recognizer.HandsInkGetCustomCharacterFilter();
	}
	public static void clearCustomCharacterFilter() {
		recognizer.HandsInkClearCustomFilter();
	}
}
