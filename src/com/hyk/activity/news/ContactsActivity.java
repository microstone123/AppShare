package com.hyk.activity.news;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.baseadapter.MyFragmentPagerAdapter;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.fragment.news.NewsAttentionFragment;
import com.hyk.fragment.news.NewsFansFragment;
import com.hyk.fragment.news.NewsFriendFragment;
import com.hyk.user.StatCommentHandle;
import com.hyk.utils.AppManager;
import com.hyk.utils.NetworkUtil;
import com.hyk.view.ViewSetClick;

public class ContactsActivity extends FragmentActivity implements OnClickListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;
	public ImageButton btn_back;
	private Resources resources;
	private ViewPager mPager;
	private TextView tvTabActivity, tvTabGroups, friend_text_t;
	private ArrayList<Fragment> fragmentsList;
	private int currIndex = 0;

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
			}
		}
	};

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_contacts);
		AppManager.getAppManager().addActivity(this);

		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		resources = getResources();
		InitTextView();
		InitViewPager();
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
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, this);
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		default:
			break;
		}
	}

	private void InitTextView() {
		tvTabActivity = (TextView) findViewById(R.id.attention_me_text);
		tvTabGroups = (TextView) findViewById(R.id.me_attention_text);
		friend_text_t = (TextView) findViewById(R.id.friend_text_t);

		tvTabActivity.setOnClickListener(new MyOnClickListener(0));
		tvTabGroups.setOnClickListener(new MyOnClickListener(1));
		friend_text_t.setOnClickListener(new MyOnClickListener(2));
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			try {
				mPager.setCurrentItem(index);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPagernews);
		fragmentsList = new ArrayList<Fragment>();

		fragmentsList.add(new NewsAttentionFragment());
		fragmentsList.add(new NewsFansFragment());
		fragmentsList.add(new NewsFriendFragment());
//		this.getc
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				if (currIndex == 2) {
					friend_text_t.setTextColor(resources.getColor(R.color.text_col));
					friend_text_t.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
				}
				if (currIndex == 1) {
					tvTabGroups.setTextColor(resources.getColor(R.color.text_col));
					tvTabGroups.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
				}
				tvTabActivity.setTextColor(resources.getColor(R.color.myapp_text));
				tvTabActivity.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
				break;
			case 1:
				if (currIndex == 2) {
					friend_text_t.setTextColor(resources.getColor(R.color.text_col));
					friend_text_t.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
				}
				if (currIndex == 0) {
					tvTabActivity.setTextColor(resources.getColor(R.color.text_col));
					tvTabActivity.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
				}
				tvTabGroups.setTextColor(resources.getColor(R.color.myapp_text));
				tvTabGroups.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
				break;
			case 2:
				if (currIndex == 0) {
					tvTabActivity.setTextColor(resources.getColor(R.color.text_col));
					tvTabActivity.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
				}
				if (currIndex == 1) {
					tvTabGroups.setTextColor(resources.getColor(R.color.text_col));
					tvTabGroups.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
				}
				friend_text_t.setTextColor(resources.getColor(R.color.myapp_text));
				friend_text_t.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
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
}
