package com.hyk.activity.photo;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.activity.center.SetDynamicActivity;
import com.hyk.photo.HackyViewPager;
import com.hyk.utils.AppManager;
import com.hyk.utils.BimpUtils;
import com.hyk.utils.FileUtils;

public class PhotoActivity extends Activity {

	private ArrayList<View> listViews = null;
	private HackyViewPager pager;
	private MyPageAdapter adapter;
	private int count;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	public int max;
	private TextView photo_num;
	RelativeLayout photo_relativeLayout;
	private PhotoViewAttacher mAttacher;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);

		photo_num = (TextView) findViewById(R.id.photo_num);
		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);

		for (int i = 0; i < BimpUtils.bmp.size(); i++) {
			bmp.add(BimpUtils.bmp.get(i));
		}
		for (int i = 0; i < BimpUtils.drr.size(); i++) {
			drr.add(BimpUtils.drr.get(i));
		}
		max = BimpUtils.max;

		Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		photo_bt_del.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (listViews.size() == 1) {
					try {
						AppManager.getActivity().finish();
					} catch (Exception e) {
						// TODO: handle exception
					}
					BimpUtils.bmp.clear();
					BimpUtils.drr.clear();
					BimpUtils.max = 0;
					FileUtils.deleteDir();
					Intent intent = new Intent();
					intent.setClass(PhotoActivity.this, SetDynamicActivity.class);
					startActivity(intent);
					finish();
				} else {
					String newStr = drr.get(count).substring(drr.get(count).lastIndexOf("/") + 1,
							drr.get(count).lastIndexOf("."));
					bmp.remove(count);
					drr.remove(count);
					del.add(newStr);
					max--;
					pager.removeAllViews();
					listViews.remove(count);
					adapter.setListViews(listViews);
					adapter.notifyDataSetChanged();
				}
				photo_num.setText((count + 1) + "/" + max);
			}
		});
		Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
		photo_bt_enter.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				BimpUtils.bmp = bmp;
				BimpUtils.drr = drr;
				BimpUtils.max = max;
				for (int i = 0; i < del.size(); i++) {
					FileUtils.delFile(del.get(i) + ".JPEG");
				}
				finish();
				overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			}
		});

		pager = (HackyViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < bmp.size(); i++) {
			initListViews(bmp.get(i));//
		}

		adapter = new MyPageAdapter(listViews);// 构造adapter
		pager.setAdapter(adapter);// 设置适配器
		Intent intent = getIntent();
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
		photo_num.setText((id + 1) + "/" + max);

	}

	@SuppressWarnings("deprecation")
	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(this);// 构造textView对象
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mAttacher = new PhotoViewAttacher(img);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				finish();
			}
		});
		listViews.add(img);// 添加view
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			count = arg0;
			photo_num.setText((arg0 + 1) + "/" + max);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
															// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
