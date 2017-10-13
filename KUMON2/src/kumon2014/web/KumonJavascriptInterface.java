package kumon2014.web;

import android.webkit.JavascriptInterface;


public class KumonJavascriptInterface {
	public static String ACTION_NAME = "JAVASCRIPT_ACTION";

	private KumonJavascriptEventListenerInterface mEventListener = null;

    /** Instantiate the interface and set the context */    
    public KumonJavascriptInterface() 
    {        
    }    
    public void setEventListener(KumonJavascriptEventListenerInterface listener) {
    	mEventListener = listener;
   	 }
    
    @JavascriptInterface
    public void Login(String id, String pswd) 
    {     
    	if (mEventListener != null) {
    		mEventListener.onLogIn(id, pswd);
    	}
    }
    @JavascriptInterface
    public void GoBack()
    {
    	if (mEventListener != null) {
    		mEventListener.onBack();
    	}
    }

    @JavascriptInterface
    public void DisplayPrint(String kyozaiId, String printSetId, String printId)
	{
    	if (mEventListener != null) {
    		mEventListener.onDisplayPrint(kyozaiId, printSetId, printId);
    	}
	}


}
