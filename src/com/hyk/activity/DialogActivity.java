package com.hyk.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hyk.utils.AppManager;
import com.hyk.utils.CHString;

/**
 * @ClassName: DialogActivity
 * @Description: TODO(更新提示界面)
 * @author linhs
 * @date 2014-3-6 下午2:04:23
 */
public class DialogActivity extends Activity implements android.view.View.OnClickListener {
	private Button the_dialog_ok, the_dialog_cancle;
	private TextView top_text;
	private Handler handler;
	private static final int UPDATA_APK_ING = 900003;
	private static final int UPDATA_APK_END = 900004;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.the_waring_dialog);
		AppManager.getAppManager().addActivity(this);
		setUpView();
	}
	
//	public DialogActivity(Context context, Handler handler) {
//		setContentView(R.layout.the_waring_dialog);
//		this.handler = handler;
//		setUpView();
//	}

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
			Message msg1 = new Message();
			msg1.what = UPDATA_APK_END;
			handler.sendMessage(msg1);
			break;
		case R.id.the_dialog_ok:
			Message msg = new Message();
			msg.what = UPDATA_APK_ING;
			handler.sendMessage(msg);
			break;
		default:
			break;
		}
	}

}
