package kumon2014.common;

import android.content.Context;
import android.widget.Toast;

public class DebugToast extends Toast {
	Context mContext;
	public DebugToast(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public void show() {
		if (Utility.isDebugBuild(mContext)) {
			super.show();
		}
	}

}
