package kumon2014.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpException;

import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import kumon2014.activity.BaseActivity;
import kumon2014.common.KumonEnv;
import kumon2014.common.KumonLoaderManager;
import kumon2014.common.KumonLoaderManager.KumonLoaderCallbacks;
import kumon2014.common.MyTimingLogger;
import kumon2014.common.StudentClientCommData;

public class ModuleUpdate {
	public static String SF_DOWNAPK = StudentClientCommData.getTopFolder() + "/Download/Kumon2.apk";
	private static final String PF_VERSION = "[VERSION]";
	private static final String PF_DATE = "[DATE]";
			
	public static boolean VersionCheck(final int versioncode, final Handler handler, final BaseActivity context) 
	{
		if(KumonEnv.G_UPDATE_CHECKFILE.isEmpty()) {
			handler.sendEmptyMessage(0);
			return false;
		}
		 
		KumonLoaderCallbacks<Boolean> callback = new KumonLoaderCallbacks<Boolean>() {
			@Override
			public Loader<Boolean> onCreateLoader(int arg0, Bundle arg1) {
				AsyncTaskLoader<Boolean> loader = new AsyncTaskLoader<Boolean>(context) {
					@Override
					public Boolean loadInBackground() {
						MyTimingLogger logger = new MyTimingLogger(getClass().getSimpleName()+"#VersionCheck");
						try {
							//20140626 ADD-S For Undo
							downloadPropertyFile();
							logger.addSplit("downloadPropertyFile");
							//20140626 ADD-E For Undo
							
					    	ArrayList<String> stringList = doVersionCheck(KumonEnv.G_UPDATE_CHECKFILE);
							logger.addSplit("doVersionCheck");
					    	
					    	int ver = 0;
					    	Date date = null;
							int spos = -1;
							int epos = -1;
					    	for(int i = 0; i < stringList.size(); i++) {
					    		String str = stringList.get(i);
					    		
					    		spos = str.indexOf(PF_VERSION);
					    		if(spos >= 0) {
					    			String temp = str.substring(spos + PF_VERSION.length());
					        		epos = temp.indexOf(" ");
					        		if(epos >= 1) {
					        			temp = temp.substring(0, epos);
					        		}
					    			try {
					    				ver = Integer.parseInt(temp);
					    			}
					    			catch(Exception e) {
					    				ver = 0;
					    			}
					    		}
					    		else {
					        		spos = str.indexOf(PF_DATE);
					        		if(spos >= 0) {
					        			String temp = str.substring(spos + PF_DATE.length());
					            		epos = temp.indexOf(" ");
					            		if(epos >= 1) {
					            			temp = temp.substring(0, epos);
					            		}
					        			try {
					        				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
					        				 date = sdf.parse(temp);
					        			}
					        			catch(Exception e) {
					        				date = null;
					        			}
					        		}
					    		}
					    	}
							if(ver > versioncode) {
								if(date == null) {
									return true;
								}
								else {
									if(date.before(new Date())) {
										return true;
									}
								}
							}
							logger.addSplit("check end");
						}
						finally {
							logger.dumpToLog();
						}
						return false;
					}
					
				};
				loader.forceLoad();
				return loader;
			}
			
			@Override
			public void onLoadFinished(Loader<Boolean> arg0, Boolean arg1) {
				handler.sendEmptyMessage(arg1 ? 1 : 0);
			}
			
		};
		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_VERSIONCHECK, context, null, callback);
		return true;
	}
	
	public static boolean DownLoadApk(final Handler handler, final BaseActivity context)	{
		KumonLoaderCallbacks<Boolean> callback = new KumonLoaderCallbacks<Boolean>() {
			@Override
			public Loader<Boolean> onCreateLoader(int id, Bundle args) {
				AsyncTaskLoader<Boolean> loader = new AsyncTaskLoader<Boolean>(context) {
					@Override
					public Boolean loadInBackground() {
						boolean ret = false;
						try {
							// URL設定           
							URL url = new URL(KumonEnv.G_UPDATE_APKFILE);
							// HTTP接続開始           
							HttpURLConnection c = (HttpURLConnection) url.openConnection();
							c.setRequestMethod("GET");            
							c.connect();             
							// SDカードの設定           
							File folder = StudentClientCommData.getUpdateFolder();
							// テンポラリファイルの設定            
							File outputFile = new File(folder, "Kumon2.apk");
							FileOutputStream fos = new FileOutputStream(outputFile);
							// ダウンロード開始                        
							InputStream is = c.getInputStream();
							byte[] buffer = new byte[1024];
							int len = 0;            
							while ((len = is.read(buffer)) != -1) {
								fos.write(buffer, 0, len);            
							}
							fos.close();
							is.close();
							ret = true;
						}
						catch (Exception e) {
						}
						return ret;
					}
				};
				loader.forceLoad();
				return loader;
			}

			@Override
			public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
				handler.sendEmptyMessage(data ? 1 : 0);
			}
			
		};
		KumonLoaderManager.startLoader(KumonLoaderManager.TASKID_DOWNLOADAPK, context, null, callback);
		return false;
	}
	
	private static ArrayList<String> doVersionCheck(String urlString)
	{
		ArrayList<String> stringList = new ArrayList<String>();
		
		try {
       	  	Authenticator.setDefault(new Authenticator() 
   	  			{   
   	  				protected PasswordAuthentication getPasswordAuthentication() {   
   	  					return new PasswordAuthentication(KumonEnv.G_BASIC_ID, KumonEnv.G_BASIC_Password.toCharArray());   
   	  				}   
   	  			}
       	  	);
			
			// HTTP Connection
			URL url = new URL(urlString);
		    URLConnection conn = url.openConnection();
		      
		    HttpURLConnection httpConn = (HttpURLConnection)conn;
		    httpConn.setAllowUserInteraction(false);
		    httpConn.setInstanceFollowRedirects(true);
		    httpConn.setRequestMethod("GET");
		    httpConn.connect();
		    int response = httpConn.getResponseCode();

		    // Check Response
		    if(response != HttpURLConnection.HTTP_OK){
		        throw new HttpException();
		    }
		    InputStream in = httpConn.getInputStream();
		     
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
 		    String str = br.readLine();
		    while(str != null){
		    	stringList.add(str);
		    	str = br.readLine();
		    }

		    br.close();
		    httpConn.disconnect();
	    } catch (Exception e){
	    	stringList.clear();
			return stringList;
	    }
		return stringList;
	}
	//20140626 ADD-S For Undo
	private static void downloadPropertyFile()
	{
		try {
       	  	Authenticator.setDefault(new Authenticator() 
   	  			{   
   	  				protected PasswordAuthentication getPasswordAuthentication() {   
   	  					return new PasswordAuthentication(KumonEnv.G_BASIC_ID, KumonEnv.G_BASIC_Password.toCharArray());   
   	  				}   
   	  			}
       	  	);
			
			// HTTP Connection
			URL url = new URL(KumonEnv.G_UPDATE_PROPERTYFILE);
		    URLConnection conn = url.openConnection();
		      
		    HttpURLConnection httpConn = (HttpURLConnection)conn;
		    httpConn.setAllowUserInteraction(false);
		    httpConn.setInstanceFollowRedirects(true);
		    httpConn.setRequestMethod("GET");
		    httpConn.connect();
		    int response = httpConn.getResponseCode();

		    // Check Response
		    if(response != HttpURLConnection.HTTP_OK){
		        throw new HttpException();
		    }
		    InputStream in = httpConn.getInputStream();
		     
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
			// SDカードの設定           
			File folder = StudentClientCommData.getTopFolder();
			// テンポラリファイルの設定            
			File outputFile = new File(folder, StudentClientCommData.SF_PropertyFileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
 		    String str = br.readLine();
		    while(str != null){
		    	bw.write(str);
		    	bw.newLine();
		    	str = br.readLine();
		    }
		    bw.close();
		    httpConn.disconnect();
	    } catch (Exception e){
	    }
		return ;
	}
	//20140626 ADD-E For Undo

}
