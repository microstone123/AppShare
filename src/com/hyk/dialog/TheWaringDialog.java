package com.hyk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.utils.CHString;

public class TheWaringDialog extends Dialog implements android.view.View.OnClickListener {
	private Button the_dialog_ok, the_dialog_cancle;
	private TextView top_text;
	private Context context;
	private Handler handler;
	private static final int UPDATA_APK_ING = 900003;
	private static final int UPDATA_APK_END = 900004;

	public TheWaringDialog(Context context, Handler handler) {
		super(context, R.style.customDialog);
		setCancelable(false); // 阻止返回键的响应
		setContentView(R.layout.the_waring_dialog);
		this.handler = handler;
		setUpView();
	}

	private void setUpView() {
		the_dialog_ok = (Button) findViewById(R.id.the_dialog_ok);
		the_dialog_cancle = (Button) findViewById(R.id.the_dialog_cancle);
		the_dialog_ok.setOnClickListener(this);
		the_dialog_cancle.setOnClickListener(this);
		top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText(CHString.IS_UNINSTALL);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.the_dialog_cancle:
			dismiss();
			Message msg1 = new Message();
			msg1.what = UPDATA_APK_END;
			handler.sendMessage(msg1);
			break;
		case R.id.the_dialog_ok:
			dismiss();
			Message msg = new Message();
			msg.what = UPDATA_APK_ING;
			handler.sendMessage(msg);
			break;
		default:
			break;
		}
	}

}
