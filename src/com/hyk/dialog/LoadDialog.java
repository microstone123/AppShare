package com.hyk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;

import com.hyk.activity.R;

public class LoadDialog extends Dialog {
	private static final int loadCode = 9001;
	private Handler handler;

	public LoadDialog(Context context, Handler handler) {
		super(context, R.style.item_load_dialog);
		setCancelable(false); // 阻止返回键的响应
		setContentView(R.layout.dialog_loader);
		// this.loadCode = loadCode;
		this.handler = handler;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//			Message msg = new Message();
//			msg.what = loadCode;
//			handler.sendMessage(msg);
			dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
