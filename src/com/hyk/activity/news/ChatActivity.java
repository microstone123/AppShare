package com.hyk.activity.news;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPException;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyk.activity.R;
import com.hyk.baseadapter.ChatAdapter;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.chat.ChatHistryString;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultForChat;
import com.hyk.utils.CHString;
import com.hyk.utils.Expressions;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.ImageTools;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.TopDownListView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppConnection;
import com.hyk.xmpp.XmppService;
import com.hyk.xmpp.listener.MessageListener;
import com.hyk.xmpp.model.Msg;
import com.hyk.xmpp.model.OneFridenMessages;

public class ChatActivity extends FragmentActivity implements TopDownListView.OnRefreshLoadingMoreListener,
		OnClickListener {

	private ChatAdapter chatAdapter;
	private String pFRIENDID;
	private EditText msgText;
	private TextView chat_name;
	private ImageButton mBtnBack;
	private Button btsend;
	private TopDownListView listview;
	// private MyScrollHandler scrollHandler;
	private Chat newchat;
	// 我们需要知道，收到了新的消息和发出了新的消息
	UpMessageReceiver mUpMessageReceiver;
	OneFridenMessages friendMessageBean;

	private ViewPager viewPager;
	private ArrayList<GridView> grids;
	private int[] expressionImages;
	private String[] expressionImageNames;
	private int[] expressionImages1;
	private String[] expressionImageNames1;
	private int[] expressionImages2;
	private String[] expressionImageNames2;
	private ImageButton biaoqingBtn;
	// private ImageButton biaoqingfocuseBtn;
	private int setFace = 0;
	private LinearLayout page_select;
	private ImageView page0;
	private ImageView page1;
	private ImageView page2;
	private GridView gView1;
	private GridView gView2;
	private ImageButton btn_back;
	private GridView gView3;
	private Drawable myBitmap = null;
	public static String img_path;
	public String myRegId;
	private int currentPage = 0;
	private int pageSize = 15;
	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;
	private String user_img;
	private String user_name;
	private String myurl;
	// private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;
	private String hisId;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				break;
			case SUCC_NETWORK:
				framelayout.setVisibility(View.GONE);
				XmppConnection.isConnected();
				break;
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_xml);

		user_name = getIntent().getStringExtra("user_name");
		user_img = getIntent().getStringExtra("user_img");
		hisId = getIntent().getStringExtra("user_regid");
		this.pFRIENDID = hisId + "@" + XmppConnection.SERVER_NAME;
		// requestQueue = Volley.newRequestQueue(this);
		SharedPreferencesHelper spqq = new SharedPreferencesHelper(this, "loginInfo");
		spqq = new SharedPreferencesHelper(this, "loginInfo");
		if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
			myRegId = spqq.getValue("redId");
		}
		// 会话内容改变，接受广播
		mUpMessageReceiver = new UpMessageReceiver();
		IntentFilter messageIntentFilter = new IntentFilter(XmppApplication.XMPP_UP_MESSAGE_ACTION);
		messageIntentFilter.addDataScheme("xmpp");
		messageIntentFilter.addDataAuthority(XmppService.getUsername(pFRIENDID), null);
		registerReceiver(mUpMessageReceiver, messageIntentFilter);

		// 消息监听
		friendMessageBean = new OneFridenMessages();

		OneFridenMessages newFriendMessageBean = XmppApplication.chatDatabaseUtil.getMsg(pFRIENDID, myRegId,
				currentPage, pageSize);

		if (newFriendMessageBean != null) {
			if (newFriendMessageBean.getMessageList() != null) {
				for (int i = 0; i < newFriendMessageBean.getMessageList().size(); i++) {
					friendMessageBean.getMessageList().add(newFriendMessageBean.getMessageList().get(i));
				}
			}
		}
		XmppApplication.AllFriendsMessageMapData.put(pFRIENDID, friendMessageBean);

		String redId = spqq.getValue("redId");
		myurl = spqq.getValue(redId + "_logo");
		if (!myurl.startsWith("http")) {
			if (hasSdcard()) {
				if (StringUtils.isNotEmpty(myurl)) {
					File file = new File(spqq.getValue(redId + "_logo"));
					if (file.exists()) {
						Bitmap bm = BitmapFactory.decodeFile(spqq.getValue(redId + "_logo"));
						myBitmap = ImageTools.bitmapToDrawable(bm);
					}
				} else {
					myBitmap = getResources().getDrawable(R.drawable.the_default);
				}
			} else {
				myBitmap = getResources().getDrawable(R.drawable.the_default);
			}
			myurl = null;
		}
		initView();
		chat_name.setText(user_name); // 会话的名称，好友名字
		initViewPager();
		try {

			// 新消息设为0
			// XmppApplication.AllFriendsMessageMapData.get(pFRIENDID).NewMessageCount
			// = 0;
			XmppApplication.sharedPreferences.edit().putInt(pFRIENDID + XmppApplication.user, 0).commit();
			XmppApplication.sharedPreferenceNews.edit().putString(pFRIENDID, "").commit();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@SuppressLint("CutPasteId")
	private void initView() {
		final RelativeLayout activityRootView = (RelativeLayout) findViewById(R.id.activityRoot);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
				if (heightDiff > 100) { // 如果高度差超过100像素，就很有可能是有软键盘...
					// ... do something here
					// ToastUtil.show(ChatActivity.this, "软键盘");
					listview.setSelectionFromTop(listview.getCount() - 1, 0);
					biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_pressed);
					viewPager.setVisibility(View.GONE);
					page_select.setVisibility(View.GONE);
					setFace = 0;

				}
			}
		});
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());
		listview = (TopDownListView) findViewById(R.id.formclient_listview);
		listview.setOnRefreshListener(this);
		chat_name = (TextView) findViewById(R.id.chat_name);
		// 获取文本信息
		msgText = (EditText) findViewById(R.id.dynaic_text);
		msgText.setOnClickListener(this);
		// 返回按钮
		mBtnBack = (ImageButton) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		// 发送消息
		btsend = (Button) findViewById(R.id.formclient_btsend);
		btsend.setOnClickListener(this);
		this.chatAdapter = new ChatAdapter(this, friendMessageBean, myBitmap, user_img, myurl, pFRIENDID);
		listview.setAdapter(chatAdapter);
		// if (listview.getCount() > 3) {
		listview.setSelectionFromTop(listview.getCount() - 1, 0);
		// }
		// 表情导航
		page_select = (LinearLayout) findViewById(R.id.page_select);
		page0 = (ImageView) findViewById(R.id.page0_select);
		page1 = (ImageView) findViewById(R.id.page1_select);
		page2 = (ImageView) findViewById(R.id.page2_select);
		// 引入表情
		expressionImages = Expressions.expressionImgs;
		expressionImageNames = Expressions.expressionImgNames;
		expressionImages1 = Expressions.expressionImgs1;
		expressionImageNames1 = Expressions.expressionImgNames1;
		expressionImages2 = Expressions.expressionImgs2;
		expressionImageNames2 = Expressions.expressionImgNames2;
		// 创建ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		// 表情
		biaoqingBtn = (ImageButton) findViewById(R.id.chatting_biaoqing_btn);
		biaoqingBtn.setOnClickListener(this);
		// biaoqingfocuseBtn = (ImageButton)
		// findViewById(R.id.chatting_biaoqing_focuse_btn);
		// biaoqingfocuseBtn.setOnClickListener(this);

		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Log.e("onScroll", "e");
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_pressed);
				viewPager.setVisibility(View.GONE);
				page_select.setVisibility(View.GONE);
				setFace = 0;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// setFace = 0;
				// biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_pressed);
				// viewPager.setVisibility(View.GONE);
				// page_select.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.formclient_text:
			removeExpression();
			break;
		case R.id.dynaic_text:
			listview.setSelectionFromTop(listview.getCount() - 1, 0);
			setFace = 0;
			break;
		case R.id.btn_back:
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		case R.id.formclient_btsend:
			if (XmppConnection.isConnected()) {
				if (XmppConnection.getConnection().isAuthenticated()) {
					ViewSetClick.getShortAlpha(v, this);
					listview.setSelectionFromTop(listview.getCount() - 1, 0);
					sendMsg();
				} else {
					XmppService.login(myRegId);
				}
			} else {
				Toast.makeText(ChatActivity.this, "当前网络不可用，请检查你的网络设置", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.chatting_biaoqing_btn:
			listview.setSelectionFromTop(listview.getCount() - 1, 0);
			if (setFace == 0) {
				showExpression(v);
			} else if (setFace == 1) {
				removeExpression();
			}
			break;
		case R.id.chatting_biaoqing_focuse_btn:
			removeExpression();
			break;
		default:
			break;
		}
	}

	private void sendMsg() {
		if (StringUtils.isNotEmpty(msgText.getText().toString())) {
			String msg = msgText.getText().toString() + CHString.getChatMark(); // 获取text文本
			Log.e("XmppConnection.isConnected()", XmppConnection.isConnected() + " " + XmppConnection.getConnection());
			if (!XmppConnection.isConnected()) {
				Toast.makeText(ChatActivity.this, "当前网络不可用，请检查你的网络设置", Toast.LENGTH_SHORT).show();
			} else if (msg.length() > 0) {

				try {
					XmppApplication.AllFriendsMessageMapData.put(pFRIENDID, friendMessageBean);
					// 创建回话
					ChatManager cm = XmppConnection.getConnection().getChatManager();
					// 发送消息给pc服务器的好友（获取自己的服务器，和好友）
					newchat = cm.createChat(pFRIENDID, null);
					newchat.sendMessage(msg);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				msgText.setText("");
			} else {
				Toast.makeText(ChatActivity.this, "发送信息不能为空", Toast.LENGTH_SHORT).show();
			}
			// 清空text
		} else {
			Toast.makeText(ChatActivity.this, "发送信息不能为空", Toast.LENGTH_SHORT).show();
		}
	}

	private void showExpression(View view) {
		// InputMethodManager inputMethodManager = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(msgText.getWindowToken(), 0);

		setFace = 1;
		biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_focused);
		viewPager.setVisibility(View.VISIBLE);
		page_select.setVisibility(View.VISIBLE);
	}

	private void removeExpression() {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

		setFace = 0;
		biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_pressed);
		viewPager.setVisibility(View.GONE);
		page_select.setVisibility(View.GONE);
	}

	private void initViewPager() {
		LayoutInflater inflater = LayoutInflater.from(this);
		grids = new ArrayList<GridView>();
		gView1 = (GridView) inflater.inflate(R.layout.grid1, null);

		setPage(page0, gView1, expressionImages, expressionImageNames);
		grids.add(gView1);

		gView2 = (GridView) inflater.inflate(R.layout.grid2, null);
		grids.add(gView2);

		gView3 = (GridView) inflater.inflate(R.layout.grid3, null);
		grids.add(gView3);

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return grids.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(grids.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(grids.get(position));
				return grids.get(position);
			}

			@Override
			public void finishUpdate(View arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public Parcelable saveState() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
				// TODO Auto-generated method stub

			}

		};
		viewPager.setAdapter(mPagerAdapter);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	@Override
	protected void onResume() {
		if (!XmppConnection.getConnection().isAuthenticated()) {
			ToastUtil.show(this, CHString.ERR_FUWQ);
		}
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		MessageListener.onChat = 0;
		super.onResume();
	}

	@Override
	protected void onPause() {
		MessageListener.onChat = 1;
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try {
			// 新消息设为0
			XmppApplication.AllFriendsMessageMapData.get(pFRIENDID).NewMessageCount = 0;
			XmppApplication.sharedPreferences.edit().putInt(pFRIENDID + XmppApplication.user, 0).commit();
			XmppApplication.sharedPreferenceNews.edit().putString(pFRIENDID, "").commit();
			unregisterReceiver(mUpMessageReceiver);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private class UpMessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到廣播更新我们的界面
			chatAdapter.notifyDataSetChanged();
			listview.setSelectionFromTop(listview.getCount() - 1, 0);

			XmppApplication.AllFriendsMessageMapData.get(pFRIENDID).NewMessageCount = 0;
			XmppApplication.sharedPreferences.edit().putInt(pFRIENDID + XmppApplication.user, 0).commit();
			XmppApplication.sharedPreferenceNews.edit().putString(pFRIENDID, "").commit();
			// listview.setSelectionFromTop(listview.getCount() - 1, 0);
		}
	}

	// ** 指引页面改监听器 */
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				page0.setImageDrawable(getResources().getDrawable(R.drawable.page_focused));
				page1.setImageDrawable(getResources().getDrawable(R.drawable.page_unfocused));
				break;
			case 1:
				setPage(page1, gView2, expressionImages1, expressionImageNames1);
				break;
			case 2:
				setPage(page2, gView3, expressionImages2, expressionImageNames2);
				break;

			}
		}
	}

	public void setPage(ImageView pageFocused, GridView gridView, final int[] expressionImages,
			final String[] expressionImageNames) {
		page0.setImageDrawable(getResources().getDrawable(R.drawable.page_unfocused));
		page1.setImageDrawable(getResources().getDrawable(R.drawable.page_unfocused));
		page2.setImageDrawable(getResources().getDrawable(R.drawable.page_unfocused));
		pageFocused.setImageDrawable(getResources().getDrawable(R.drawable.page_focused));
		List<Map<String, Object>> listItems1 = new ArrayList<Map<String, Object>>();
		// 生成24个表情
		for (int i = 0; i < 24; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", expressionImages[i]);
			listItems1.add(listItem);
		}

		SimpleAdapter simpleAdapter1 = new SimpleAdapter(getApplicationContext(), listItems1,
				R.layout.singleexpression, new String[] { "image" }, new int[] { R.id.image });
		gridView.setAdapter(simpleAdapter1);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ViewSetClick.getAlpha(arg1, ChatActivity.this);
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(), expressionImages[arg2 % expressionImages.length]);
				ImageSpan imageSpan = new ImageSpan(getApplicationContext(), bitmap);
				SpannableString spannableString = new SpannableString(expressionImageNames[arg2].substring(1,
						expressionImageNames[arg2].length() - 1));
				spannableString.setSpan(imageSpan, 0, expressionImageNames[arg2].length() - 2,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 编辑框设置数据
				msgText.append(spannableString);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * 判断sdcard是否被挂载
	 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
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
	public void onRefresh() {
		currentPage++;
		if (currentPage == 1) {
			pageSize = pageSize * 2;
		} else {
			pageSize = 15;
		}
		httpForChatHistory();
	}

	private void httpForChatHistory() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_CHATHISTORY;
		Map<String, String> params = new HashMap<String, String>();
		params.put("myId", myRegId);
		params.put("hisId", hisId);
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					ResultForChat resultString = new ResultForChat();
					resultString = binder.fromJson(response, ResultForChat.class);
					if (resultString != null) {
						if (resultString.getObj().getList() != null) {
							ChatData(resultString.getObj().getList());
						} else {
							listview.onRefreshComplete();
						}
					} else {
						listview.onRefreshComplete();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
		// jsonObjectRequest.setTag("httpForChatHistory");
		// requestQueue.add(jsonObjectRequest);

		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForChatHistory");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForChatHistory");
		} catch (Exception e) {
			// TODO: handle exception
		}
		// requestQueue.cancelAll("httpForChatHistory");
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForChatHistory");
			unregisterReceiver(mUpMessageReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void ChatData(List<ChatHistryString> chatString) {
		if (currentPage == 1) {
			friendMessageBean.getMessageList().clear();
			for (int i = chatString.size() - 1; i >= 0; i--) {
				Msg msg = new Msg();
				msg.setMsg(chatString.get(i).getContent());
				msg.setSendDate(chatString.get(i).getSendTime());
				msg.setUsername(chatString.get(i).getRegId());
				if (hisId.equals(chatString.get(i).getRegId())) {
					msg.setInOrOut("IN");
				} else {
					msg.setInOrOut("OUT");
				}
				friendMessageBean.getMessageList().add(msg);
			}
			currentPage++;
		} else {
			OneFridenMessages oneFridenMessages = new OneFridenMessages();
			for (int i = 0; i < friendMessageBean.getMessageList().size(); i++) {
				oneFridenMessages.getMessageList().add(friendMessageBean.getMessageList().get(i));
			}
			friendMessageBean.getMessageList().clear();
			for (int i = chatString.size() - 1; i >= 0; i--) {
				Msg msg = new Msg();
				msg.setMsg(chatString.get(i).getContent());
				msg.setSendDate(chatString.get(i).getSendTime());
				msg.setUsername(chatString.get(i).getRegId());
				if (hisId.equals(chatString.get(i).getRegId())) {
					msg.setInOrOut("IN");
				} else {
					msg.setInOrOut("OUT");
				}
				friendMessageBean.getMessageList().add(msg);
			}
			friendMessageBean.getMessageList().addAll(oneFridenMessages.getMessageList());
		}
		listview.onRefreshComplete();
		this.chatAdapter = new ChatAdapter(this, friendMessageBean, myBitmap, user_img, myurl, pFRIENDID);
		listview.setAdapter(chatAdapter);
		// XmppApplication.AllFriendsMessageMapData.put(pFRIENDID,
		// friendMessageBean);
	}

}