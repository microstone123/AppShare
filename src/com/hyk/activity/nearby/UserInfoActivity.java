package com.hyk.activity.nearby;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyk.activity.R;
import com.hyk.activity.news.ChatActivity;
import com.hyk.baseadapter.MyFragmentPagerAdapter;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.dialog.LoginDiao;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.fragment.nearby.UserAppFragment;
import com.hyk.fragment.nearby.UserDYFragment;
import com.hyk.http.Httpaddress;
import com.hyk.party.BaseUiListener;
import com.hyk.resultforjson.LoginResult;
import com.hyk.resultforjson.ResultString;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.resultforjson.ResultStringForUserInfo;
import com.hyk.user.StatCommentHandle;
import com.hyk.user.UserInfo;
import com.hyk.utils.AppManager;
import com.hyk.utils.AsyncImageLoader;
import com.hyk.utils.AsyncImageLoader.ImageCallback;
import com.hyk.utils.CHString;
import com.hyk.utils.FileHelper;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.CustomMarqueeTextView;
import com.hyk.view.RoundImageViewExtNetw;
import com.hyk.view.ViewSetClick;
import com.hyk.weibo.Constants;
import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppConnection;
import com.hyk.xmpp.XmppService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.models.ErrorInfo;
import com.sina.weibo.sdk.models.User;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class UserInfoActivity extends FragmentActivity implements OnClickListener {

	private com.tencent.connect.UserInfo mInfo;
	public RelativeLayout relat_layout;
	private Tencent mTencent;
	public static QQAuth mQQAuth;
	private SharedPreferencesHelper spqq;
	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;
	private ImageButton btn_back, news_btnhover;
	private AnimationDrawable animationDrawable;
	public RoundImageViewExtNetw user_user_img_dy;
	public ImageView relation_img;
	private TextView attention_me_num, me_attention_num, user_user_name_dy, user_sign_name_dy;
	private CustomMarqueeTextView userName_tv;
	public String uName, uSign, redId, uImg;
	private int relation;
	// protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private AsyncImageLoader asyncImageLoader;
	private List<View> listViews;
	private ResultStringForLoadap resultStringForLoadap;
	private ResultStringForUserInfo resultString;
	private String jsonString;
	private int mark;
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	public String myId;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private static final int START_LOAD_MARK = 100121;
	private LoginDiao loginDiao;
	private FileHelper fileHelper;
	private String headPic = "";
	private static final int LOGIN_QQ = 1003;
	private static final int LOGIN_WEIBO = 1004;
	private LoginResult loginResult;
	public WeiboAuth mWeiboAuth;
	/** 登陆认证对应的listener */
	public AuthListener mLoginListener = new AuthListener();
	public SsoHandler mSsoHandler;
	/** 用户信息接口 */
	private UsersAPI mUsersAPI;
	private int sendAtt = 101;

	private static final String TAG = "MainActivity";
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private TextView tvTabActivity, tvTabGroups;
	private int currIndex = 0;
	private int position_one;
	private int position_two;
	private int position_three;
	private Resources resources;
	public String rImei;
//	private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				break;
			case SUCC_NETWORK:
				framelayout.setVisibility(View.GONE);
				break;
			case START_LOAD_MARK:
				if (loginDiao.isShowing()) {
					loginDiao.dismiss();
				}
				startLoad();
				break;
			case LOGIN_QQ:
				if (loginDiao.isShowing()) {
					loginDiao.dismiss();
				}
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
		setContentView(R.layout.user_dynamic);
		AppManager.getAppManager().addActivity(this);
		mQQAuth = QQAuth.createInstance(getResources().getString(R.string.qq_appid), this);
		mTencent = Tencent.createInstance(getResources().getString(R.string.qq_appid), this);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = ImageLoaderPartner.getOptions(R.drawable.the_default);
		uName = getIntent().getExtras().getString("userName");
		uSign = getIntent().getExtras().getString("sign_name");
		rImei = getIntent().getExtras().getString("rImei");
		redId = getIntent().getExtras().getString("redId");
		uImg = getIntent().getExtras().getString("user_headpic");
		relation = getIntent().getExtras().getInt("relation");
		spqq = new SharedPreferencesHelper(this, "loginInfo");
		asyncImageLoader = new AsyncImageLoader();
		if (relation == 0) {
			mark = 1;
		} else {
			mark = 0;
		}
		fileHelper = new FileHelper(this);
		if (!"0".equals(redId)) {
			httpForUserInfo();
		}
		loginDiao = new LoginDiao(this, handler);
		loginDiao.setCanceledOnTouchOutside(true);

//		imageLoaderShow = new ImageLoaderShow(requestQueue);

		myId = spqq.getValue("redId");
		setupViews();
		sendRetation();

		resources = getResources();
		InitWidth();
		InitTextView();
		InitViewPager();
	}

	public void setupViews() {
		relat_layout = (RelativeLayout) findViewById(R.id.relat_layout);
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());
		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat);
		loading_img = (ImageView) findViewById(R.id.loading_img);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		relation_img = (ImageView) findViewById(R.id.relation_img);
		relation_img.setOnClickListener(this);
		user_user_img_dy = (RoundImageViewExtNetw) findViewById(R.id.user_user_img_dy);
		user_user_name_dy = (TextView) findViewById(R.id.user_user_name_dy);
		userName_tv = (CustomMarqueeTextView) findViewById(R.id.userName_tv);
		user_sign_name_dy = (TextView) findViewById(R.id.user_sign_name_dy);
		me_attention_num = (TextView) findViewById(R.id.user_me_attention_num);
		attention_me_num = (TextView) findViewById(R.id.user_attention_me_num);
		news_btnhover = (ImageButton) findViewById(R.id.news_btnhover);
		news_btnhover.setOnClickListener(this);
		user_sign_name_dy.setText(uSign);
		if ("0".equals(redId)) {
			userName_tv.setText(CHString.TOURIST_NAME);
			user_user_name_dy.setText(CHString.TOURIST_NAME);
		} else {
			user_user_name_dy.setText(uName);
			userName_tv.setText(uName);
		}

		user_user_img_dy.setErrorImageResId(R.drawable.the_def_img);
		user_user_img_dy.setDefaultImageResId(R.drawable.the_def_img);
		user_user_img_dy.setImageUrl(uImg, XmppApplication.getsInstance().imageLoader);

	}

	@Override
	protected void onResume() {
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, this);
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		case R.id.relation_img:
			if (StringUtils.isNotEmpty(myId)) {
				if (!"0".equals(myId)) {
					if (NetworkUtil.checkNetworkState(this)) {
						if (StringUtils.isNotEmpty(redId)) {
							if (!"0".equals(redId)) {
								sendRetationForing();
								animationDrawable = (AnimationDrawable) relation_img.getDrawable();
								animationDrawable.start();
								httpForAttention();
							} else {
								ToastUtil.show(this, CHString.TOURIST_BECFORE_ERROR);
							}
						} else {
							ToastUtil.show(this, CHString.TOURIST_BECFORE_ERROR);
						}
					} else {
						ToastUtil.show(this, CHString.NETWORK_ERROR);
					}
				} else {
					sendAtt = 10021;
					// ToastUtil.show(this, CHString.NOT_LOGIN);
					loginDiao.show();
				}
			} else {
				sendAtt = 10021;
				// ToastUtil.show(this, CHString.NOT_LOGIN);
				loginDiao.show();
			}
			break;
		case R.id.news_btnhover:
			if (!"0".equals(redId)) {
				if (StringUtils.isEmpty(myId)) {
					sendAtt = 10022;
					loginDiao.show();
				} else {
					ViewSetClick.getAlpha(v, this);
					Intent intent = new Intent();
					intent.putExtra("user_name", uName);
					intent.putExtra("user_img", uImg);
					intent.putExtra("user_regid", redId);
					intent.setClass(this, ChatActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			} else {
				ToastUtil.show(this, CHString.UNABLE_TO_COMMUNICATE);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_err_connet, fragment);
		tration.commitAllowingStateLoss();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForAttention");
			XmppApplication.getsInstance().cancelPendingRequests("getFromLogin");
			XmppApplication.getsInstance().cancelPendingRequests("httpForUserInfo");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForAttention");
			XmppApplication.getsInstance().cancelPendingRequests("getFromLogin");
			XmppApplication.getsInstance().cancelPendingRequests("httpForUserInfo");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void sendRetation() {
		switch (mark) {
		case 1:
			relation_img.setImageResource(R.drawable.relation_01);
			break;
		case 0:
			relation_img.setImageResource(R.drawable.relation_07);
			break;
		default:
			break;
		}
	}

	public void sendRetationForing() {
		switch (mark) {
		case 1:
			relation_img.setImageResource(R.drawable.retation_fly);
			break;
		case 0:
			relation_img.setImageResource(R.drawable.retation_flying);
			break;
		default:
			break;
		}
	}

	public void chectHttp() {
		if (NetworkUtil.checkNetworkState(UserInfoActivity.this)) {
			httpForAttention();
		} else {
			animationDrawable.stop();
			ToastUtil.show(UserInfoActivity.this, CHString.NETWORK_ERROR);
		}
	}

	public void chectHttpInfo() {
		if (NetworkUtil.checkNetworkState(UserInfoActivity.this)) {
			httpForUserInfo();
		} else {
			ToastUtil.show(UserInfoActivity.this, CHString.NETWORK_ERROR);
		}
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
		if (loginDiao.isShowing()) {
			loginDiao.dismiss();
		}

		switch (sendAtt) {
		case 10022:
			Intent intent = new Intent();
			intent.putExtra("user_name", uName);
			intent.putExtra("user_img", uImg);
			intent.putExtra("user_regid", redId);
			intent.setClass(this, ChatActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case 10021:
			if (StringUtils.isNotEmpty(redId)) {
				if (!"0".equals(redId)) {
					sendRetationForing();
					animationDrawable = (AnimationDrawable) relation_img.getDrawable();
					animationDrawable.start();
					httpForAttention();
				} else {
					ToastUtil.show(this, CHString.TOURIST_BECFORE_ERROR);
				}
			} else {
				ToastUtil.show(this, CHString.TOURIST_BECFORE_ERROR);
			}
			break;
		default:
			break;
		}
	}

	public void startLoad() {
		sendRetationForing();
		loading_relat.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		getFromLogin();
	}

	private void getFromLogin() {
		String userId = spqq.getValue("userId");
		if (StringUtils.isNotEmpty(userId)) {
			String url = Httpaddress.IP_ADDRESS + Httpaddress.LOGIN_ADDRESS;
			Map<String, String> params = new HashMap<String, String>();
			params.put("imei", UserInfo.getUserInfo().getIMEI());
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
			String httpUrl = HttpUtils.checkUrl(url, params);
			jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						ResultString resultString = new ResultString();
						resultString = binder.fromJson(response, ResultString.class);
						if (resultString != null) {
							loginResult = resultString.getObj();

							spqq.putStrValue("sign_name", loginResult.getSignName());
							spqq.putStrValue("user_name", loginResult.getUserName());
							spqq.putStrValue("redId", loginResult.getRegId());
							myId = loginResult.getRegId();
							spqq.putStrValue("focusme", loginResult.getFocusMe());
							spqq.putStrValue("focushim", loginResult.getFocusHim());

							headPic = loginResult.getHeadPic();
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
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					if (NetworkUtil.checkNetworkState(UserInfoActivity.this)) {
						getFromLogin();
					} else {
						stopLoad();
					}
				}
			});
//			jsonObjectRequest.setTag("getFromLogin");
//			requestQueue.add(jsonObjectRequest);
			XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "getFromLogin");
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
			mInfo = new com.tencent.connect.UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);
		}
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
			Toast.makeText(UserInfoActivity.this, "onWeiboException", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			// Toast.makeText(UserInfoActivity.this, "onCancel",
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
					Toast.makeText(UserInfoActivity.this, "获取User信息成功，用户昵称：" + user.screen_name, Toast.LENGTH_LONG)
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
				} else {
					Toast.makeText(UserInfoActivity.this, response, Toast.LENGTH_LONG).show();
				}
				startLoad();
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(UserInfoActivity.this, info.toString(), Toast.LENGTH_LONG).show();
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

	private void InitTextView() {
		tvTabActivity = (TextView) findViewById(R.id.tv_tab_activity);
		tvTabGroups = (TextView) findViewById(R.id.tv_tab_groups);

		tvTabActivity.setOnClickListener(new MyOnClickListener(0));
		tvTabGroups.setOnClickListener(new MyOnClickListener(1));
	}

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();

		fragmentsList.add(new UserAppFragment());
		fragmentsList.add(new UserDYFragment());
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
	}

	public void setClosePage() {
		// mPager.setOnPageChangeListener(null);
		// mPager.setAdapter(new
		// MyFragmentPagerAdapter(getSupportFragmentManager(), null));
	}

	public void setOpenPage() {
		// mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// mPager.setAdapter(new
		// MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
	}

	private void InitWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		position_one = (int) (screenW / 4.0);
		position_two = position_one * 2;
		position_three = position_one * 3;
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					tvTabGroups.setTextColor(resources.getColor(R.color.text_col));
					tvTabGroups.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
				}
				tvTabActivity.setTextColor(resources.getColor(R.color.myapp_text));
				tvTabActivity.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
				break;
			case 1:
				if (currIndex == 0) {
					tvTabActivity.setTextColor(resources.getColor(R.color.text_col));
					tvTabActivity.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
				}
				tvTabGroups.setTextColor(resources.getColor(R.color.myapp_text));
				tvTabGroups.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
				break;
			}
			currIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void httpForAttention() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_FIREND;
		Map<String, String> params = new HashMap<String, String>();
		params.put("myId", myId);
		params.put("friendId", redId);
		params.put("mark", String.valueOf(mark));
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					resultStringForLoadap = new ResultStringForLoadap();
					resultStringForLoadap = binder.fromJson(response, ResultStringForLoadap.class);

					if (resultStringForLoadap != null) {
						if (resultStringForLoadap.getResult() == 0) {
							int num = Integer.valueOf(me_attention_num.getText().toString());
							if (mark == 1) {
								mark = 0;
								++num;
								me_attention_num.setText(String.valueOf(num));
							} else {
								mark = 1;
								--num;
								if (num < 0) {
									num = 0;
								}
								me_attention_num.setText(String.valueOf(num));
							}
							sendRetation();
							animationDrawable.stop();
						} else {
							sendRetation();
							animationDrawable.stop();
							if (resultStringForLoadap != null) {
								ToastUtil.show(UserInfoActivity.this, resultStringForLoadap.getErrMsg());
							}
						}
					} else {
						chectHttp();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				stopLoad();
				chectHttp();
			}
		});
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForAttention");
	}

	private void httpForUserInfo() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_INFO;
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", redId);
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					resultString = new ResultStringForUserInfo();
					resultString = binder.fromJson(response, ResultStringForUserInfo.class);
					if (resultString != null) {
						if (resultString.getResult() == 0) {
							user_sign_name_dy.setText(resultString.getObj().getSignName());
							me_attention_num.setText(resultString.getObj().getFocusMe());
							attention_me_num.setText(resultString.getObj().getFocusHim());
						}
					} else {
						chectHttpInfo();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				stopLoad();
				chectHttpInfo();
			}
		});
//		jsonObjectRequest.setTag("httpForUserInfo");
//		requestQueue.add(jsonObjectRequest);
		
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForUserInfo");
	}

}