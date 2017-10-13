package kumon2014.message;

public class KumonMessage {
	public static String SF_TTL_NOCONNECT = "通信エラー";
	
	public static String SF_WORNING = "***注意!***";
	public static int MSG_SF_WORNING = 1000;
	
	public static int MSG_No1 = 1;
	public static int MSG_No2 = 2;
	public static int MSG_No3 = 3;
	public static int MSG_No4 = 4;
	public static int MSG_No5 = 5;
	public static int MSG_No6 = 6;
	public static int MSG_No7 = 7;
	public static int MSG_No8 = 8;
	public static int MSG_No9 = 9;

	public static int MSG_No10 = 10;
	public static int MSG_No14 = 14;
	public static int MSG_No16 = 16;
	public static int MSG_No18 = 18;
	public static int MSG_No20 = 20;

	public static int MSG_No21 = 21;
	public static int MSG_No22 = 22;
	public static int MSG_No23 = 23;
	public static int MSG_No24 = 24;
	public static int MSG_No25 = 25;
	public static int MSG_No26 = 26;
	public static int MSG_No27 = 27;
	public static int MSG_No28 = 28;
	public static int MSG_No29 = 29;
	public static int MSG_No30 = 30;
	public static int MSG_No31 = 31;
	public static int MSG_No32 = 32;
	
	public static int MSG_No40 = 40;
	public static int MSG_No41 = 41;
	public static int MSG_No42 = 42;
	public static int MSG_No43 = 43;

	public static int MSG_No44 = 44;
	public static int MSG_No45 = 45;
	public static int MSG_No46 = 46;
	public static int MSG_No47 = 47;
	public static int MSG_No48 = 48;
	public static int MSG_No49 = 49;
	public static int MSG_No50 = 50;
	//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
	public static int MSG_No51 = 51;
	//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告

    //20150409 ADD-S For 2015年度Ver. 未読コメント
	public static int MSG_No52 = 52;
    //20150409 ADD-E For 2015年度Ver. 未読コメント

	public static int MSG_No53 = 53;
	
	public static int MSG_No79 = 79;
	
	public static int MSG_No80 = 80;
	public static int MSG_No90 = 90;
	public static int MSG_No91 = 91;
	
	private static KumonMessage mInstance = null;
	private	KumonMessageDetail[] mMsgList = {
			new KumonMessageDetail(1, "通信エラー", "ネットワークにつながっていません。(001)"),
			new KumonMessageDetail(2, "通信エラー", "ネットワークにつながっていません。(002)"),
			new KumonMessageDetail(3, "ログイン失敗", "IDかパスワードが違います。(003)"),
			new KumonMessageDetail(4, "通信エラー", "ネットワークにつながっていません。(004)"),
			new KumonMessageDetail(5, "オートログイン失敗", "保存されているIDかパスワードが違います。\n入れ直してください。(005)"),
//			new KumonMessageDetail(6, "新しい教材の取得中", "次の教材を受け取っています。\nしばらくお待ちください… (006)"),
			new KumonMessageDetail(6, "受け取っています。", "しばらくお待ちください… (006)"),
//			new KumonMessageDetail(7, "採点の依頼中", "採点をお願いしています。\nしばらくお待ちください… (007)"),
			new KumonMessageDetail(7, "送っています。", "しばらくお待ちください… (007)"),
			new KumonMessageDetail(8, "通信エラー", "ネットワークにつながっていません。(008)"),
			new KumonMessageDetail(9, "通信エラー", "ネットワークにつながっていません。(009)"),
			new KumonMessageDetail(10, "通信エラー", "ネットワークにつながっていません。(010)"),
//			new KumonMessageDetail(14, "学習の終了", "ここで学習を終えますか？\n今回の学習は保存されません。(014)"),
			new KumonMessageDetail(14, "注意！", "ここでは終了できません。\n「No」をおして、学習した最後のb面で\n■をおしてください。(014)"),
//			new KumonMessageDetail(16, "学習の終了", "ここで学習を終えますか？\nこのページ以降の学習は保存されません。(016)"),
			new KumonMessageDetail(16, "注意！", "a面では終了できません。\n「No」をおして、b面まで学習し\n終えてから■をおしてください。(016)"),
//			new KumonMessageDetail(18, "学習の終了", "ここで学習を終えますか？\nこのページまでの学習が保存されます。(018)"),
			new KumonMessageDetail(18, "ここで学習を終えますか？", "このページまでの学習が保存されます。(018)"),
			new KumonMessageDetail(20, "学習の終了", "ここで学習を終えますか？\nこのページまでの学習が保存されます。(020)"),
			new KumonMessageDetail(21, "", "しばらくお待ちください… (021)"),
			//new KumonMessageDetail(22, "新しい教材の取得中", "次の教材を問い合わせています。\nしばらくお待ちください… (022)"),
			new KumonMessageDetail(22, "受け取っています。", "しばらくお待ちください… (022)"),
//			new KumonMessageDetail(23, "", "休止中　　(023)"),
			new KumonMessageDetail(23, "", "一時停止中…　　(023)"),
			new KumonMessageDetail(24, "初期化", "全データを初期化します。\n宜しいですか？ (024)"),
			new KumonMessageDetail(25, "初期化", "指定された学習者データを初期化します。\n宜しいですか？ (025)"),
			new KumonMessageDetail(26, "初期化", "問題データを初期化します。\n宜しいですか？ (026)"),
			new KumonMessageDetail(27, "問題データ", "全問題データを取得します。\n宜しいですか？ (027)"),
			new KumonMessageDetail(28, "ログ", "ログを消去します。\n宜しいですか？ (028)"),
			new KumonMessageDetail(29, "ログイン", "問い合わせ中\nしばらくお待ちください… (029)"),
//			new KumonMessageDetail(30, "教材の取得中", "教材を受け取っています。\nしばらくお待ちください… (030)"),
			new KumonMessageDetail(30, "受け取っています。", "しばらくお待ちください… (030)"),
//			new KumonMessageDetail(31, "教材の取得", "次の教材を受け取ります。\nよろしいですか？(031)"),
			new KumonMessageDetail(31, "教材の取得", "受け取ります。\nよろしいですか？(031)"),
			new KumonMessageDetail(32, "教材の取得中", "キャンセル中です。\nしばらくお待ちください… (032)"),
			
			new KumonMessageDetail(40, "採点", "採点を終えますか？(040)"),
			new KumonMessageDetail(41, "採点状況確認中", "採点状況確認しています。(041)"),
			new KumonMessageDetail(42, "採点状況", "入室状態にありません。(042)"),
			new KumonMessageDetail(43, "採点状況", "まだ学習結果を送信していない教材があります。\n先に学習結果を送信してください。(043)"),

//			new KumonMessageDetail(44, "テストの終了", "ここでテストを終えますか？\n今回のテストは保存されません。(044)"),
			new KumonMessageDetail(44, "注意！", "ここでテストを終えますか？\n今回のテストは保存されません。(044)"),
			new KumonMessageDetail(45, "テストの終了", "テストを終えますか？(045)"),
			new KumonMessageDetail(46, "テストの終了", "時間がきたので、テストはこれで終わりです。(046)"),
			new KumonMessageDetail(47, "準備中", "録音準備中です(047)"),
			new KumonMessageDetail(48, "録音", "録音中です(048)"),
			new KumonMessageDetail(49, "録音終了", "録音終了処理中です(049)"),
			new KumonMessageDetail(50, "録音終了", "録音時間は３０秒で終わりです\n録音終了処理中です(050)"),
			//20150110 ADD-S For 2015年度Ver. グレードの高い教材を選択したら、警告
			new KumonMessageDetail(51, "学習", "この教材を先に学習してもよいですか？(051)"),
			//20150110 ADD-E For 2015年度Ver. グレードの高い教材を選択したら、警告
		    //20150409 ADD-S For 2015年度Ver. 未読コメント
			new KumonMessageDetail(52, "学習", "未読の付箋があります。(052)"),
		    //20150409 ADD-E For 2015年度Ver. 未読コメント
			
			new KumonMessageDetail(53, "処理中", "しばらくお待ちください… (053)"),

			new KumonMessageDetail(79, "Clean Up", "お待ちください…"),
			
			new KumonMessageDetail(80, "UPDATE", "更新ファイルが存在します。\n更新しますか？(080)"),
			new KumonMessageDetail(90, "システムエラー", "データが保存できません。(090)"),
			new KumonMessageDetail(91, "システムエラー", "データが削除できません。(091)"),
			};

	public KumonMessage ()
	{
	}
	private KumonMessageDetail getMessage (int no)
	{
		KumonMessageDetail detail = null;
		for(int i = 0; i < mMsgList.length; i ++) 
		{
			if(mMsgList[i].mNo == no)
			{
				return mMsgList[i];
			}
		}
		return detail;
	}
	
	public static String getKumonMessage(int no)
	{
		String msg = "";
		if(mInstance == null) {
			mInstance = new KumonMessage();
		}
		KumonMessageDetail detail = mInstance.getMessage(no);
		
		if(detail != null) {
			msg = detail.mMessage;
		}
		return msg;
	}
	public static KumonMessageDetail getKumonMessageDetail(int no)
	{
		if(mInstance == null) {
			mInstance = new KumonMessage();
		}
		KumonMessageDetail detail = mInstance.getMessage(no);
		
		return detail;
	}
/*
	public static void showKumonMsgBoxOk(Context con, int no, String btnMsg, DialogInterface.OnClickListener listener)
	{
		if(mInstance == null) {
			mInstance = new KumonMessage();
		}
		KumonMessageDetail detail = mInstance.getMessage(no);
		if(detail != null) {
			AlertDialog.Builder alertDialog= new AlertDialog.Builder(con);  
			alertDialog.setTitle(detail.mTitle);
			alertDialog.setMessage(detail.mMessage);
			alertDialog.setPositiveButton(btnMsg, listener);
			alertDialog.create();    
			alertDialog.show();
		}
	}
	public static void showKumonMsgBoxOk(Context con,String title, String msg, String btnMsg, DialogInterface.OnClickListener listener)
	{
		if(mInstance == null) {
			mInstance = new KumonMessage();
		}
		AlertDialog.Builder alertDialog= new AlertDialog.Builder(con);  
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setPositiveButton(btnMsg, listener);
		alertDialog.create();    
		alertDialog.show();
	}
	*/
	/*
	public static void showKumonMsgBoxYesNo(Activity activity, int no, 
									String btnMsgLeft, DialogInterface.OnClickListener leftlistener, 
									String btnMsgRight, DialogInterface.OnClickListener Rightlistener) 
	{
		if(mInstance == null) {
			mInstance = new KumonMessage();
		}
		
		//20130702 MOD-S For 学習終了時A面の場合、警告メッセージ追加
		//KumonMessageDetail detail = mInstance.getMessage(no);
		KumonMessageDetail detail = null;
		if(no >= MSG_SF_WORNING) {
			detail = mInstance.getMessage(no - MSG_SF_WORNING);
		}
		else {
			detail = mInstance.getMessage(no);
		}
		//20130702 MOD-E For 学習終了時A面の場合、警告メッセージ追加
		if(detail != null) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);  
			alertDialogBuilder.setTitle(detail.mTitle);
			//20130702 MOD-S For 学習終了時A面の場合、警告メッセージ追加
			//alertDialog.setMessage(detail.mMessage);
			String msg = "";
			if(no >= MSG_SF_WORNING) {
				msg = SF_WORNING + "\n";
			}
			msg += detail.mMessage ;
			alertDialogBuilder.setMessage(msg);
			//20130702 MOD-E For 学習終了時A面の場合、警告メッセージ追加
			alertDialogBuilder.setPositiveButton(btnMsgLeft, leftlistener);
			alertDialogBuilder.setNegativeButton(btnMsgRight, Rightlistener);
			alertDialogBuilder.create();    
			AlertDialog dialog = alertDialogBuilder.show();

			dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
			dialog.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
		}
		
	}
	*/
	/*
	public static void showProgress(ProgressDialog dialog, Activity activity, int no)
	{
		if(mInstance == null) {
			mInstance = new KumonMessage();
		}
		KumonMessageDetail detail = mInstance.getMessage(no);
		if(detail != null) {
			dialog.setTitle(detail.mTitle);
			dialog.setMessage(detail.mMessage);
			dialog.show();
			
			dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		    dialog.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
		}
	}
	*/
	
    public class KumonMessageDetail {
		public int mNo = 0;
		public String mTitle = "";
		public String mMessage = "";
		public  KumonMessageDetail(int no, String title, String message)
		{
			mNo = no;
			mTitle = title;
			mMessage = message;
		}
    }

	
}
