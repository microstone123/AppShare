package com.hyk.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.hyk.activity.R;
import com.hyk.utils.CHString;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.ToastUtil;
import com.hyk.view.ViewSetClick;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.Tencent;

/**
 * @ClassName: AppdiaosActivity
 * @Description: TODO(应用简介界面)
 * @author linhs
 * @date 2013-12-6 下午2:28:40
 */

@SuppressLint("HandlerLeak")
public class LoginDiao extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private Button QQ_login_d_btn;
	private Button xinlang_login_d_btn;
	private Tencent mTencent;
	public static QQAuth mQQAuth;
	private UserInfo mInfo;
	private static final int START_LOAD_MARK = 100121;
	private Handler handler;
	private static final int LOGIN_QQ = 1003;
	private static final int LOGIN_WEIBO = 1004;

	public LoginDiao(Context context, Handler handler) {
		super(context, R.style.customDialog);
		getWindow().setWindowAnimations(R.style.AnimBottom);
		setCancelable(false); // 阻止返回键的响应
		setContentView(R.layout.login_dialog);
		this.context = context;
		this.handler = handler;
		setupViews();
	}

	public void setupViews() {
		QQ_login_d_btn = (Button) findViewById(R.id.QQ_login_d_btn);
		xinlang_login_d_btn = (Button) findViewById(R.id.xinlang_login_d_btn);
		QQ_login_d_btn.setOnClickListener(this);
		xinlang_login_d_btn.setOnClickListener(this);
		mQQAuth = QQAuth.createInstance(this.context.getResources().getString(R.string.qq_appid), this.context);
		mTencent = Tencent.createInstance(this.context.getResources().getString(R.string.qq_appid), this.context);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, context);
		switch (v.getId()) {
		case R.id.QQ_login_d_btn:
			if (NetworkUtil.checkNetworkState(context)) {
				Message msg = new Message();
				msg.what = LOGIN_QQ;
				handler.sendMessage(msg);
			} else {
				ToastUtil.show(context, CHString.NETWORK_ERROR);
			}
			break;

		case R.id.xinlang_login_d_btn:
			if (NetworkUtil.checkNetworkState(context)) {
				Message msg = new Message();
				msg.what = LOGIN_WEIBO;
				handler.sendMessage(msg);
			} else {
				ToastUtil.show(context, CHString.NETWORK_ERROR);
			}
			break;

		default:
			break;
		}
	}
}
