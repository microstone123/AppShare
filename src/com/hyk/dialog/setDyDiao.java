package com.hyk.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.hyk.activity.R;
import com.hyk.utils.Expressions;

/**
 * @ClassName: AppdiaosActivity
 * @Description: TODO(应用简介界面)
 * @author linhs
 * @date 2013-12-6 下午2:28:40
 */

@SuppressLint("HandlerLeak")
public class setDyDiao extends Dialog implements android.view.View.OnClickListener {

	private int setFace = 0;
	private ViewPager viewPager;
	private ArrayList<GridView> grids;
	private int[] expressionImages;
	private String[] expressionImageNames;
	private int[] expressionImages1;
	private String[] expressionImageNames1;
	private int[] expressionImages2;
	private String[] expressionImageNames2;
	private ImageButton biaoqingBtn;
	private Button btsend;
	private EditText msgText;
	private LinearLayout page_select;
	private ImageView page0;
	private ImageView page1;
	private ImageView page2;
	private GridView gView1;
	private GridView gView2;
	private GridView gView3;
	private Context context;

	public setDyDiao(Context context, Handler handler) {
		super(context, R.style.customDialog);
//		setCancelable(false); // 阻止返回键的响应
		setContentView(R.layout.set_dy_te);
		this.context = context;
		setupViews();
		initViewPager();
	}

	public void setupViews() {

		// 获取文本信息
		msgText = (EditText) findViewById(R.id.dynaic_text);
		msgText.setOnClickListener(this);
		// 发送消息
		btsend = (Button) findViewById(R.id.formclient_btsend);
		btsend.setOnClickListener(this);
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

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chatting_biaoqing_btn:
			if (setFace == 0) {
				showExpression(v);
			} else if (setFace == 1) {
				removeExpression();
			}
			break;
		// case R.id.formclient_btsend:
		// content = msgText.getText().toString();
		// if (StringUtils.isNotEmpty(content)) {
		// if (NetworkUtil.checkNetworkState(getActivity())) {
		// Message msg1 = new Message();
		// Bundle b1 = new Bundle();// 存放数据
		// b1.putInt("redId", Integer.valueOf(redId));
		// b1.putString("nickName", uName);
		// b1.putString("content", content);
		// msg1.setData(b1);
		// msg1.what = 102;
		// StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
		// relat_layout.setVisibility(View.GONE);
		// appDynamicAdapter.notifyDataSetChanged();
		// StatCommentHandle.getStatCommentHandle().getHandler().sendMessage(msg1);
		// InputMethodManager imm = (InputMethodManager)
		// getActivity().getSystemService(
		// Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(relat_layout.getWindowToken(), 0); //
		// 强制隐藏键盘
		// sendComment();
		// } else {
		// ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
		// }
		// } else {
		// ToastUtil.show(getActivity(), CHString.ERROR_COMMENT);
		// }
		// break;
		default:
			break;
		}

	}

	private void removeExpression() {
		viewPager.setVisibility(View.GONE);
		page_select.setVisibility(View.GONE);
		setFace = 0;
		biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_pressed);
	}

	private void showExpression(View view) {
		// InputMethodManager inputMethodManager = (InputMethodManager)
		// getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		// inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
		setFace = 1;
		biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_focused);
		// setDyDiao = new setDyDiao(getActivity(),handler);
		// setDyDiao.show();
		viewPager.setVisibility(View.VISIBLE);
		page_select.setVisibility(View.VISIBLE);
	}

	private void initViewPager() {
		LayoutInflater inflater = LayoutInflater.from(context);
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
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
			}

			@Override
			public Parcelable saveState() {
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
			}

		};
		viewPager.setAdapter(mPagerAdapter);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	public void setPage(ImageView pageFocused, GridView gridView, final int[] expressionImages,
			final String[] expressionImageNames) {
		page0.setImageDrawable(context.getResources().getDrawable(R.drawable.page_unfocused));
		page1.setImageDrawable(context.getResources().getDrawable(R.drawable.page_unfocused));
		page2.setImageDrawable(context.getResources().getDrawable(R.drawable.page_unfocused));
		pageFocused.setImageDrawable(context.getResources().getDrawable(R.drawable.page_focused));
		List<Map<String, Object>> listItems1 = new ArrayList<Map<String, Object>>();
		// 生成24个表情
		for (int i = 0; i < 24; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", expressionImages[i]);
			listItems1.add(listItem);
		}

		SimpleAdapter simpleAdapter1 = new SimpleAdapter(context.getApplicationContext(), listItems1,
				R.layout.singleexpression, new String[] { "image" }, new int[] { R.id.image });
		gridView.setAdapter(simpleAdapter1);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(context.getResources(), expressionImages[arg2
						% expressionImages.length]);
				ImageSpan imageSpan = new ImageSpan(context.getApplicationContext(), bitmap);
				SpannableString spannableString = new SpannableString(expressionImageNames[arg2].substring(1,
						expressionImageNames[arg2].length() - 1));
				spannableString.setSpan(imageSpan, 0, expressionImageNames[arg2].length() - 2,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 编辑框设置数据
				msgText.append(spannableString);
			}
		});
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
				page0.setImageDrawable(context.getResources().getDrawable(R.drawable.page_focused));
				page1.setImageDrawable(context.getResources().getDrawable(R.drawable.page_unfocused));
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

}