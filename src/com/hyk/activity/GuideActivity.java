package com.hyk.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyk.utils.AppManager;

/**
 * @author linhs 功能描述：引导界面activity类
 */
public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener {
	// 定义ViewPager对象
	private ViewPager viewPager;

	protected long mExitTime;

	// 定义ViewPager适配器
	private ViewPagerAdapter vpAdapter;

	// 定义一个ArrayList来存放View
	private ArrayList<View> views;

	// // 定义各个界面View对象
	// private View view1, view2, view3, view4, view5;

	// 底部小点的图片
	private ImageView[] points;

	// 记录当前选中位置
	private int currentIndex;

	private View[] pics = null;

	// 定义开始按钮对象
	private Button startBt;

	private final static int LOGIN_CODE = 1002;

	// private Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case LOGIN_CODE:
	// break;
	// }
	// }
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_guide);
		initView();
		initData();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 实例化各个界面的布局对象
		pics = new View[5];
		LayoutInflater mLi = LayoutInflater.from(this);
		pics[0] = mLi.inflate(R.layout.guide_view01, null);
		pics[1] = mLi.inflate(R.layout.guide_view02, null);
		pics[2] = mLi.inflate(R.layout.guide_view03, null);
		pics[3] = mLi.inflate(R.layout.guide_view04, null);
		pics[4] = mLi.inflate(R.layout.guide_view05, null);

		// 实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpagertt);

		// 实例化ArrayList对象
		views = new ArrayList<View>();

		// 实例化ViewPager适配器
		vpAdapter = new ViewPagerAdapter(views);

		// 实例化开始按钮
		startBt = (Button) pics[4].findViewById(R.id.startBtn);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 设置监听
		viewPager.setOnPageChangeListener(this);
		// 设置适配器数据
		viewPager.setAdapter(vpAdapter);

		// 初始化引导图片列表
		for (int i = 0; i < pics.length; i++) {
			views.add(pics[i]);
		}
		// 初始化底部小点
		initPoint();

		// // 给开始按钮设置监听
		startBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sp = GuideActivity.this.getSharedPreferences("CreateShortcut", 0);
				sp.edit().putBoolean("firstUse", false).commit();
				startbutton();
			}
		});
	}

	/**
	 * 初始化底部小点
	 */
	private void initPoint() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		points = new ImageView[pics.length];

		// 循环取得小点图片
		for (int i = 0; i < pics.length; i++) {
			// 得到一个LinearLayout下面的每一个子元素
			points[i] = (ImageView) linearLayout.getChildAt(i);
			// 默认都设为灰色
			points[i].setEnabled(true);
			// 给每个小点设置监听
			points[i].setOnClickListener(this);
			// 设置位置tag，方便取出与当前位置对应
			points[i].setTag(i);
		}

		// 设置当面默认的位置
		currentIndex = 0;
		// 设置为白色，即选中状态
		points[currentIndex].setEnabled(false);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/**
	 * 当新的页面被选中时调用
	 */

	@Override
	public void onPageSelected(int position) {
		// 设置底部小点选中状态
		setCurDot(position);
	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	/**
	 * 设置当前的小点的位置
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}
		points[positon].setEnabled(false);
		points[currentIndex].setEnabled(true);
		currentIndex = positon;
	}

	/**
	 * 相应按钮点击事件
	 */
	private void startbutton() {
		Intent intent = new Intent();
		intent.setClass(GuideActivity.this, TabMenuActivity.class);
		intent.putExtra("isPic", false);
		startActivity(intent);
		overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
		this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
			intent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
