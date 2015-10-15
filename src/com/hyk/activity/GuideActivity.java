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
 * @author linhs ������������������activity��
 */
public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener {
	// ����ViewPager����
	private ViewPager viewPager;

	protected long mExitTime;

	// ����ViewPager������
	private ViewPagerAdapter vpAdapter;

	// ����һ��ArrayList�����View
	private ArrayList<View> views;

	// // �����������View����
	// private View view1, view2, view3, view4, view5;

	// �ײ�С���ͼƬ
	private ImageView[] points;

	// ��¼��ǰѡ��λ��
	private int currentIndex;

	private View[] pics = null;

	// ���忪ʼ��ť����
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
	 * ��ʼ�����
	 */
	private void initView() {
		// ʵ������������Ĳ��ֶ���
		pics = new View[5];
		LayoutInflater mLi = LayoutInflater.from(this);
		pics[0] = mLi.inflate(R.layout.guide_view01, null);
		pics[1] = mLi.inflate(R.layout.guide_view02, null);
		pics[2] = mLi.inflate(R.layout.guide_view03, null);
		pics[3] = mLi.inflate(R.layout.guide_view04, null);
		pics[4] = mLi.inflate(R.layout.guide_view05, null);

		// ʵ����ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpagertt);

		// ʵ����ArrayList����
		views = new ArrayList<View>();

		// ʵ����ViewPager������
		vpAdapter = new ViewPagerAdapter(views);

		// ʵ������ʼ��ť
		startBt = (Button) pics[4].findViewById(R.id.startBtn);
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		// ���ü���
		viewPager.setOnPageChangeListener(this);
		// ��������������
		viewPager.setAdapter(vpAdapter);

		// ��ʼ������ͼƬ�б�
		for (int i = 0; i < pics.length; i++) {
			views.add(pics[i]);
		}
		// ��ʼ���ײ�С��
		initPoint();

		// // ����ʼ��ť���ü���
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
	 * ��ʼ���ײ�С��
	 */
	private void initPoint() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		points = new ImageView[pics.length];

		// ѭ��ȡ��С��ͼƬ
		for (int i = 0; i < pics.length; i++) {
			// �õ�һ��LinearLayout�����ÿһ����Ԫ��
			points[i] = (ImageView) linearLayout.getChildAt(i);
			// Ĭ�϶���Ϊ��ɫ
			points[i].setEnabled(true);
			// ��ÿ��С�����ü���
			points[i].setOnClickListener(this);
			// ����λ��tag������ȡ���뵱ǰλ�ö�Ӧ
			points[i].setTag(i);
		}

		// ���õ���Ĭ�ϵ�λ��
		currentIndex = 0;
		// ����Ϊ��ɫ����ѡ��״̬
		points[currentIndex].setEnabled(false);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/**
	 * ���µ�ҳ�汻ѡ��ʱ����
	 */

	@Override
	public void onPageSelected(int position) {
		// ���õײ�С��ѡ��״̬
		setCurDot(position);
	}

	/**
	 * ���õ�ǰҳ���λ��
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	/**
	 * ���õ�ǰ��С���λ��
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
	 * ��Ӧ��ť����¼�
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
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ע��
			intent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
