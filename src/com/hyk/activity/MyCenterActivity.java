package com.hyk.activity;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.dialog.LoadDialog;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.fragment.mycenter.CenterOfuserFragment;
import com.hyk.fragment.mycenter.LoginFragment;
import com.hyk.http.Httpaddress;
import com.hyk.party.BaseUiListener;
import com.hyk.utils.AppManager;
import com.hyk.utils.CHString;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.weibo.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.models.ErrorInfo;
import com.sina.weibo.sdk.models.User;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class MyCenterActivity extends BaseActivity {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	private static final int LOGIN_QQ = 1003;
	private static final int LOGIN_WEIBO = 1004;
	// private static final int SUCC_QQ = 1006;
	public FrameLayout framelayout, framelayout_center;
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private TextView mycenter_tv;
	public Tencent mTencent;
	private String redId;
	public WeiboAuth mWeiboAuth;
	public SsoHandler mSsoHandler;
	public static QQAuth mQQAuth;
	public LoadDialog loadDialog;
	private UserInfo mInfo;
	/** 用户信息接口 */
	private UsersAPI mUsersAPI;
	private String wuid;

	/** 登陆认证对应的listener */
	public AuthListener mLoginListener = new AuthListener();
	public SharedPreferencesHelper spqq;
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				break;
			case SUCC_NETWORK:
				framelayout.setVisibility(View.GONE);
				break;
			case LOGIN_QQ:
				loginForQQ();
				break;
			case LOGIN_WEIBO:
				LoginForWeiBo();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycenter);
		AppManager.getAppManager().addActivity(this);
		spqq = new SharedPreferencesHelper(MyCenterActivity.this, "loginInfo");
		mQQAuth = QQAuth.createInstance(getResources().getString(R.string.qq_appid), this);
		mTencent = Tencent.createInstance(getResources().getString(R.string.qq_appid), this);
		setupViews();
	}

	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(R.id.framelayout_err_connet, new ErrorConnetFragment());

		framelayout_center = (FrameLayout) findViewById(R.id.framelayout_center);
		mycenter_tv = (TextView) findViewById(R.id.mycenter_tv);
	}

	// 登陆跳转
	public void loginGoTo() {
		mycenter_tv.setText(CHString.APP_CENTER);
		ReplaceFragmentMethod(R.id.framelayout_center, new CenterOfuserFragment());
	}

	// 登陆跳转
	public void loginGoOut() {
		mycenter_tv.setText(CHString.APP_LOGIN);
		ReplaceFragmentMethod(R.id.framelayout_center, new LoginFragment());
	}

	@Override
	protected void onResume() {
		super.onResume();
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		
		redId = spqq.getValue("redId");
		if (StringUtils.isNotEmpty(redId)) {
			mycenter_tv.setText(CHString.APP_CENTER);
			ReplaceFragmentMethod(R.id.framelayout_center, new CenterOfuserFragment());
		} else {
			mycenter_tv.setText(CHString.APP_LOGIN);
			ReplaceFragmentMethod(R.id.framelayout_center, new LoginFragment());
		}
	}

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod(int viewId, Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(viewId, fragment);
		tration.commitAllowingStateLoss();
	}

	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 登入按钮的监听器，接收授权结果。
	 */
	private class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
			if (accessToken != null && accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(accessToken
						.getExpiresTime()));
				// AccessTokenKeeper.writeAccessToken(getApplicationContext(),
				// accessToken);
				showDialog();
				long uid = Long.parseLong(accessToken.getUid());
				// 获取用户信息接口
				mUsersAPI = new UsersAPI(accessToken);
				mUsersAPI.show(uid, mListener);
				// wuid = accessToken.getUid();
				// ToastUtil.show(MyCenterActivity.this,
				// accessToken.getUid()+"    "+date);
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(MyCenterActivity.this, "onWeiboException", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
//			Toast.makeText(MyCenterActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
		}
	}

	public void LoginForWeiBo() {
		// 创建授权认证信息
		mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(this, mWeiboAuth);
		mSsoHandler.authorize(mLoginListener);
	}

	public void showDialog() {
		loadDialog = new LoadDialog(this, null);
		loadDialog.show();
	}

	public void dismissDialog() {
		if (loadDialog != null) {
			if (loadDialog.isShowing()) {
				loadDialog.dismiss();
			}
		}
	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				dismissDialog();
				// 调用 User#parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					Toast.makeText(MyCenterActivity.this, "获取User信息成功，用户昵称：" + user.screen_name, Toast.LENGTH_LONG)
							.show();

					String name = user.screen_name;
					String uSex = "男";
					if (user.gender.endsWith("m")) {
						uSex = "男";
					} else {
						uSex = "女";
					}
					String img = user.avatar_hd;
					spqq.putStrValue("userId", user.id);
					spqq.putStrValue("logintype", Httpaddress.WEIBO_TYPE);
					spqq.putStrValue("uname", name);
					spqq.putStrValue("uSex", uSex);
					spqq.putStrValue("signName", user.description);
					spqq.putStrValue("userImg", img);
					dismissDialog();
					loginGoTo();
				} else {
					Toast.makeText(MyCenterActivity.this, response, Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(MyCenterActivity.this, info.toString(), Toast.LENGTH_LONG).show();
		}
	};

	private void loginForQQ() {
		if (!mQQAuth.isSessionValid()) {
			IUiListener listener = new BaseUiListener(this) {
				@Override
				protected void doComplete(JSONObject values) {
					try {
						String openId = values.getString("openid");
						spqq.putStrValue("userId", openId);
						spqq.putStrValue("logintype", Httpaddress.QQ_TYPE);
						updateUserInfo();
						showDialog();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			mTencent.loginWithOEM(this, "all", listener, "10000144", "10000144", "xxxx");
		} else {
			mQQAuth.logout(this);
			updateUserInfo();
		}
	}

	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				@Override
				public void onError(UiError e) {
				}

				@Override
				public void onComplete(final Object response) {
					JSONObject responseS = (JSONObject) response;
					try {
						String name = responseS.getString("nickname");
						String uSex = responseS.getString("gender");
						String img = responseS.getString("figureurl_qq_2");
						spqq.putStrValue("uname", name);
						spqq.putStrValue("uSex", uSex);
						spqq.putStrValue("userImg", img);
						dismissDialog();
						loginGoTo();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);

		}
	}

}
