package com.hyk.fragment.mycenter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyk.activity.MyCenterActivity;
import com.hyk.activity.R;
import com.hyk.utils.CHString;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.ViewSetClick;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.Tencent;

/**
 * @ClassName: AppCommentFragment
 * @Description: TODO(登陆)
 * @author linhs
 * @date 2014-2-26 下午2:59:21
 */
public class LoginFragment extends Fragment implements OnClickListener {

	private Button QQ_login_btn;
	private Button xinlang_login_btn;
	private Tencent mTencent;
	public static QQAuth mQQAuth;
	private UserInfo mInfo;
	private Handler handler;
	private static final int SUCC_QQ = 1006;
	private SharedPreferencesHelper spqq;
	private static final int LOGIN_QQ = 1003;
	private static final int LOGIN_WEIBO = 1004;
	/**
	 * 该按钮用于记录当前点击的是哪一个 Button，用于在 {@link #onActivityResult}
	 * 函数中进行区分。通常情况下，我们的应用中只需要一个合适的 {@link LoginButton} 或者
	 * {@link LoginoutButton} 即可。
	 */
	// /** 登陆认证对应的listener */
	// private AuthListener mLoginListener = new AuthListener();
	// private WeiboAuth mWeiboAuth;
	// private SsoHandler mSsoHandler ;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_x, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		handler = ((MyCenterActivity) getActivity()).handler;
		spqq = new SharedPreferencesHelper(getActivity(), "loginInfo");
		setupViews();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		// getData();
		super.onResume();
	}

	public void setupViews() {
		xinlang_login_btn = (Button) getActivity().findViewById(R.id.xinlang_login_btn);
		QQ_login_btn = (Button) getActivity().findViewById(R.id.QQ_login_btn);

		xinlang_login_btn.setOnClickListener(this);
		QQ_login_btn.setOnClickListener(this);

		mQQAuth = QQAuth.createInstance(getResources().getString(R.string.qq_appid), getActivity());
		mTencent = Tencent.createInstance(getResources().getString(R.string.qq_appid), getActivity());

		// // 创建授权认证信息
		// mWeiboAuth = new WeiboAuth(getActivity(), Constants.APP_KEY,
		// Constants.REDIRECT_URL, Constants.SCOPE);
		// // xinlang_login_btn.setWeiboAuthInfo(authInfo, mLoginListener);
		// //
		// xinlang_login_btn.setExternalOnClickListener(mButtonClickListener);
		// mWeiboAuth.anthorize(((MyCenterActivity)
		// getActivity()).mLoginListener);
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getLongAlpha(v, getActivity());
		switch (v.getId()) {
		case R.id.QQ_login_btn:
			if (NetworkUtil.checkNetworkState(getActivity())) {
				Message msg = new Message();
				msg.what = LOGIN_QQ;
				handler.sendMessage(msg);
			} else {
				ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
			}
			break;
		case R.id.xinlang_login_btn:
			if (NetworkUtil.checkNetworkState(getActivity())) {
				Message msg = new Message();
				msg.what = LOGIN_WEIBO;
				handler.sendMessage(msg);
			} else {
				ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
			}
			break;
		default:
			break;
		}
	}

	// /**
	// * 当 SSO 授权 Activity 退出时，该函数被调用。
	// *
	// * @see {@link Activity#onActivityResult}
	// */
	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (mSsoHandler != null) {
	// mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	// }
	// }

	// /**
	// * 登入按钮的监听器，接收授权结果。
	// */
	// private class AuthListener implements WeiboAuthListener {
	// @Override
	// public void onComplete(Bundle values) {
	// Oauth2AccessToken accessToken =
	// Oauth2AccessToken.parseAccessToken(values);
	// if (accessToken != null && accessToken.isSessionValid()) {
	// String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new
	// java.util.Date(accessToken
	// .getExpiresTime()));
	// // String format =
	// getString(R.string.weibosdk_demo_token_to_string_format_1);
	// // mTokenView.setText(String.format(format,
	// // accessToken.getToken(), date));
	// //
	// // AccessTokenKeeper.writeAccessToken(getApplicationContext(),
	// // accessToken);
	// }
	// }
	//
	// @Override
	// public void onWeiboException(WeiboException e) {
	// Toast.makeText(getActivity(), "onWeiboException",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// @Override
	// public void onCancel() {
	// Toast.makeText(getActivity(), "onCancel", Toast.LENGTH_SHORT).show();
	// }
	// }
}
