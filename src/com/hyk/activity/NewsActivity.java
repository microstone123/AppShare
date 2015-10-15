package com.hyk.activity;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyk.activity.news.ContactsActivity;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.fragment.news.LoginFragmentdioa;
import com.hyk.fragment.news.NewsFragment;
import com.hyk.http.Httpaddress;
import com.hyk.party.BaseUiListener;
import com.hyk.resultforjson.LoginResult;
import com.hyk.resultforjson.ResultString;
import com.hyk.utils.AppManager;
import com.hyk.utils.AsyncHttpUtil;
import com.hyk.utils.AsyncImageLoader;
import com.hyk.utils.AsyncImageLoader.ImageCallback;
import com.hyk.utils.FileHelper;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.view.ViewSetClick;
import com.hyk.weibo.Constants;
import com.hyk.xmpp.XmppConnection;
import com.hyk.xmpp.XmppService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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

/**
 * @ClassName: NewsActivity
 * @Description: TODO(消息界面)
 * @author linhs
 * @date 2014-3-10 下午5:51:34
 */
public class NewsActivity extends BaseActivity implements OnClickListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	private static final int LOGIN_QQ = 1003;
	private static final int LOGIN_WEIBO = 1004;
	public FrameLayout framelayout;
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private SharedPreferencesHelper spqq;
	private ImageButton friends_book_img;
	public String redId;
	private Tencent mTencent;
	public static QQAuth mQQAuth;
	private boolean isStart = false;
	private UserInfo mInfo;
	private AsyncImageLoader asyncImageLoader;
	private FileHelper fileHelper;
	private int httpCode = 0;
	private int theMark;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private LoginResult loginResult;
	public WeiboAuth mWeiboAuth;
	/** 登陆认证对应的listener */
	public AuthListener mLoginListener = new AuthListener();
	public SsoHandler mSsoHandler;
	/** 用户信息接口 */
	private UsersAPI mUsersAPI;
	private String wuid;

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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_xml);
		spqq = new SharedPreferencesHelper(this, "loginInfo");
		fileHelper = new FileHelper(this);
		asyncImageLoader = new AsyncImageLoader();
		mQQAuth = QQAuth.createInstance(getResources().getString(R.string.qq_appid), this);
		mTencent = Tencent.createInstance(getResources().getString(R.string.qq_appid), this);
		AppManager.getAppManager().addActivity(this);
		setupViews();
	}

	public void setupViews() {
		loading_img = (ImageView) findViewById(R.id.loading_img_news);
		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat_news);
		friends_book_img = (ImageButton) findViewById(R.id.friends_book_img);
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());
		friends_book_img.setOnClickListener(this);
	}

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_err_connet, fragment);
		tration.commitAllowingStateLoss();
	}

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceNewsMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_about, fragment);
		tration.commitAllowingStateLoss();
	}

	@Override
	protected void onResume() {
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}

		spqq = new SharedPreferencesHelper(this, "loginInfo");
		redId = spqq.getValue("redId");
		if (StringUtils.isNotEmpty(redId)) {
			if (!isStart) {
				friends_book_img.setVisibility(View.VISIBLE);
				ReplaceNewsMethod(new NewsFragment());
				isStart = true;
			}
		} else {
			isStart = false;
			friends_book_img.setVisibility(View.INVISIBLE);
			ReplaceNewsMethod(new LoginFragmentdioa());
		}
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, this);
		switch (v.getId()) {
		case R.id.friends_book_img:
			Intent intent = new Intent();
			intent.setClass(NewsActivity.this, ContactsActivity.class);
			AnimationUtil.setLayout(R.anim.push_left_in, R.anim.push_left_out);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

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
						startLoad();
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

	private void getFromLogin() {
		String userId = spqq.getValue("userId");
		if (StringUtils.isNotEmpty(userId)) {
			theMark = ++httpCode;
			String url = Httpaddress.IP_ADDRESS + Httpaddress.LOGIN_ADDRESS;
			RequestParams params = new RequestParams();
			params.put("imei", com.hyk.user.UserInfo.getUserInfo().getIMEI());
			params.put("userName", spqq.getValue("uname"));
			params.put("accountId", spqq.getValue("logintype"));
			params.put("userKey", spqq.getValue("userId"));
			params.put("signName", spqq.getValue("signName"));
			params.put("phoneType", Httpaddress.PHONE_TYPE);
			params.put("headPic", spqq.getValue("userImg"));
			String sexSign = spqq.getValue("uSex");
			if ("男".equals(sexSign)) {
				params.put("sex", "1");
			} else {
				params.put("sex", "0");
			}
			AsyncHttpUtil.getJson(url, params, new JsonHttpResponseHandler() {
				public void onSuccess(JSONObject arg0) { // 返回的是JSONObject，会调用这里
					if (theMark == httpCode) {
						ResultString resultString = new ResultString();
						resultString = binder.fromJson(arg0.toString(), ResultString.class);
						if (resultString != null) {
							loginResult = resultString.getObj();

							spqq.putStrValue("sign_name", loginResult.getSignName());
							spqq.putStrValue("user_name", loginResult.getUserName());
							spqq.putStrValue("redId", loginResult.getRegId());
							redId = loginResult.getRegId();
							spqq.putStrValue("focusme", loginResult.getFocusMe());
							spqq.putStrValue("focushim", loginResult.getFocusHim());
							String headPic = loginResult.getHeadPic();
							Bitmap cachedImage = asyncImageLoader.loadDrawable(loginResult.getHeadPic(),
									new ImageCallback() {
										public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
											if (imageDrawable != null) {
												String path = fileHelper.creatSDDir("hyk");
												String photo_name = spqq.getValue("redId") + "_logo";
												String url = fileHelper.saveBitmap(imageDrawable, path, photo_name);
												spqq.putStrValue(photo_name, url);
											}
										}
									});
							if (cachedImage != null) {
								String path = fileHelper.creatSDDir("hyk");
								String photo_name = spqq.getValue("redId") + "_logo";
								String url = fileHelper.saveBitmap(cachedImage, path, photo_name);
								spqq.putStrValue(photo_name, url);
							}
							String photo_name = spqq.getValue("redId") + "_logo";
							spqq.putStrValue(photo_name, headPic);
							if (StringUtils.isNotEmpty(loginResult.getRegId())) {
								if (XmppConnection.isConnected()) {
									if (!XmppConnection.getConnection().isAuthenticated()) {
										XmppService.login(spqq.getValue("redId"));
									}
								}
							}
							stopLoad();
						}
					}
				};

				public void onFailure(Throwable arg0) {
					try {
						if (theMark == httpCode) {
							if (NetworkUtil.checkNetworkState(NewsActivity.this)) {
								getFromLogin();
							}
							loading_relat.setVisibility(View.GONE);
							if (animationDrawable != null) {
								animationDrawable.stop();
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				};

				public void onFinish() {

				};
			});
		}
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
		friends_book_img.setVisibility(View.VISIBLE);
		ReplaceNewsMethod(new NewsFragment());
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		getFromLogin();
	}

	public void startLoadWeiBo() {
		loading_relat.setVisibility(View.VISIBLE);
		getFromLogin();
	}

	public void LoginForWeiBo() {
		// 创建授权认证信息
		mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(this, mWeiboAuth);
		mSsoHandler.authorize(mLoginListener);
	}

	/**
	 * 登入按钮的监听器，接收授权结果。
	 */
	private class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			animationDrawable = (AnimationDrawable) loading_img.getDrawable();
			animationDrawable.start();
			Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
			if (accessToken != null && accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(accessToken
						.getExpiresTime()));
				long uid = Long.parseLong(accessToken.getUid());
				// 获取用户信息接口
				mUsersAPI = new UsersAPI(accessToken);
				mUsersAPI.show(uid, mListener);
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(NewsActivity.this, "onWeiboException", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			// Toast.makeText(NewsActivity.this, "onCancel",
			// Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				// 调用 User#parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					Toast.makeText(NewsActivity.this, "获取User信息成功，用户昵称：" + user.screen_name, Toast.LENGTH_LONG).show();

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
				} else {
					Toast.makeText(NewsActivity.this, response, Toast.LENGTH_LONG).show();
				}
				startLoadWeiBo();
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(NewsActivity.this, info.toString(), Toast.LENGTH_LONG).show();
		}
	};

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

}
