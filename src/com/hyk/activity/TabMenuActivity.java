package com.hyk.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.hyk.activity.myapp.MyAPPTabActivity;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.utils.ACache;
import com.hyk.utils.AppManager;
import com.hyk.utils.AppServiceManager;
import com.hyk.xmpp.XmppConnection;

@SuppressWarnings("deprecation")
public class TabMenuActivity extends TabActivity {
	private FrameLayout frameLayout;
	// 创建TabHost
	public AnimationTabHost mth;
	public static final String TAB_NEARBY = "身边";
	public static final String TAB_NEWS = "消息";
	public static final String TAB_MYAPP = "我的应用";
	public static final String TAB_MYCENTER = "个人中心";
	public RadioGroup radioGroup;
	private ACache mAcache;
	private RadioButton radio_button0, radio_button3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.tab_menu);
		// 初始化底部菜单
		init();
		// 底部菜单点击事件
		clickevent();
		mAcache = ACache.get(this);
		mAcache.put("XmppApplication", "start");

		boolean isPic = getIntent().getExtras().getBoolean("isPic");

		if (isPic) {
			mth.setCurrentTabByTag(TAB_MYCENTER);
			radio_button3.setChecked(true);
		}
	}

	/**
	 * 每一个底部按钮点击事件，切换相应的界面
	 */
	private void clickevent() {
		this.radioGroup = (RadioGroup) findViewById(R.id.main_radio);
		radio_button3 = (RadioButton) findViewById(R.id.radio_button3);
		radio_button0 = (RadioButton) findViewById(R.id.radio_button0);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 根据点击的按钮跳转到相应的界面
				switch (checkedId) {
				case R.id.radio_button0:
					mth.setCurrentTabByTag(TAB_NEARBY);
					break;
				case R.id.radio_button1:
					mth.setCurrentTabByTag(TAB_NEWS);
					break;
				case R.id.radio_button2:
					mth.setCurrentTabByTag(TAB_MYAPP);
					break;
				case R.id.radio_button3:
					mth.setCurrentTabByTag(TAB_MYCENTER);
					break;
				}
			}
		});
	}

	/**
	 * 实例化TabHost,往TabHost添加4个界面
	 */
	private void init() {
		// 实例化TabHost
		mth = (AnimationTabHost) findViewById(android.R.id.tabhost);
		TabSpec ts1 = mth.newTabSpec(TAB_NEARBY).setIndicator(TAB_NEARBY);
		ts1.setContent(new Intent(TabMenuActivity.this, NearByActivity.class));
		mth.addTab(ts1);// 往TabHost中第一个底部菜单添加界面

		TabSpec ts2 = mth.newTabSpec(TAB_NEWS).setIndicator(TAB_NEWS);
		ts2.setContent(new Intent(TabMenuActivity.this, NewsActivity.class));
		mth.addTab(ts2);

		TabSpec ts3 = mth.newTabSpec(TAB_MYAPP).setIndicator(TAB_MYAPP);
		ts3.setContent(new Intent(TabMenuActivity.this, MyAPPTabActivity.class));
		mth.addTab(ts3);

		TabSpec ts4 = mth.newTabSpec(TAB_MYCENTER).setIndicator(TAB_MYCENTER);
		ts4.setContent(new Intent(TabMenuActivity.this, MyCenterActivity.class));
		mth.addTab(ts4);
		frameLayout = mth.getTabContentView();
	}

	@Override
	protected void onPause() {
		if (AnimationUtil.ANIM_IN != 0 && AnimationUtil.ANIM_OUT != 0) {
			super.overridePendingTransition(AnimationUtil.ANIM_IN, AnimationUtil.ANIM_OUT);
			AnimationUtil.clear();
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		// UploadApp.httpForUserAppInfo(this);
		// Log.e("", msg);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mAcache.put("XmppApplication", "destroy");
		ConnectionChangeReceiver.setHandler(null);
		AppManager.getAppManager().AppExit(this);
		AppServiceManager.getAppManager().AppExit(this);
		XmppConnection.closeConnection();
		// android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// String hot_String = mAcache.getAsString("hot_string");
		// if (StringUtils.isEmpty(hot_String)) {
		// new ThreadUtils(this).start();
		// }

		// String updata_String = mAcache.getAsString("updata_string");
		// if (StringUtils.isEmpty(updata_String)) {
		// new UpdataThreadUtils(this).start();
		// }
	}
}
