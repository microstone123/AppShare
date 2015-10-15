package com.hyk.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.hyk.activity.R;
import com.hyk.utils.Expressions;
import com.hyk.view.ViewSetClick;

public class SetDyPopupWindow extends PopupWindow implements OnClickListener {

	private View mMenuView;
	private ViewPager viewPager;
	private ArrayList<GridView> grids;
	private int[] expressionImages;
	private String[] expressionImageNames;
	private int[] expressionImages1;
	private String[] expressionImageNames1;
	private int[] expressionImages2;
	private String[] expressionImageNames2;
	private ImageView page0;
	private ImageView page1;
	private ImageView page2;
	private GridView gView1;
	private GridView gView2;
	private GridView gView3;
	private Context context;
	private int setFace = 0;
	private static final int SET_FACE = 10013;
	private Handler handler;
	private ImageButton biaoqingBtn;
	private LinearLayout page_select;
	private EditText dynaic_text;
	private Button formclient_btsend;

	@SuppressWarnings("deprecation")
	public SetDyPopupWindow(Activity context, Handler handler) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.set_dy_te, null);
		this.context = context;
		this.handler = handler;
		setupViews();
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				Log.e("height", height + "");
				// int height =
				// mMenuView.findViewById(R.id.pop_layout).getTop();
				// int y = (int) event.getY();
				// if (event.getAction() == MotionEvent.ACTION_UP) {
				// Log.e("height", height+"");
				// dismiss();
				// }
				return false;
			}
		});
		initViewPager();
	}

	public void setupViews() {
		formclient_btsend = (Button) mMenuView.findViewById(R.id.formclient_btsend);
		page_select = (LinearLayout) mMenuView.findViewById(R.id.page_select);
		dynaic_text = (EditText) mMenuView.findViewById(R.id.dynaic_text);
		// 表情
		biaoqingBtn = (ImageButton) mMenuView.findViewById(R.id.chatting_biaoqing_btn);
		biaoqingBtn.setOnClickListener(this);
		formclient_btsend.setOnClickListener(this);
		// 创建ViewPager
		viewPager = (ViewPager) mMenuView.findViewById(R.id.viewpager);
		page0 = (ImageView) mMenuView.findViewById(R.id.page0_select);
		page1 = (ImageView) mMenuView.findViewById(R.id.page1_select);
		page2 = (ImageView) mMenuView.findViewById(R.id.page2_select);
		// 引入表情
		expressionImages = Expressions.expressionImgs;
		expressionImageNames = Expressions.expressionImgNames;
		expressionImages1 = Expressions.expressionImgs1;
		expressionImageNames1 = Expressions.expressionImgNames1;
		expressionImages2 = Expressions.expressionImgs2;
		expressionImageNames2 = Expressions.expressionImgNames2;
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

	public void setPage(ImageView pageFocused, GridView gridView, final int[] expressionImages, final String[] expressionImageNames) {
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

		SimpleAdapter simpleAdapter1 = new SimpleAdapter(context.getApplicationContext(), listItems1, R.layout.singleexpression, new String[] { "image" }, new int[] { R.id.image });
		gridView.setAdapter(simpleAdapter1);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ViewSetClick.getAlpha(arg1, context);
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(context.getResources(), expressionImages[arg2 % expressionImages.length]);
				ImageSpan imageSpan = new ImageSpan(context.getApplicationContext(), bitmap);
				SpannableString spannableString = new SpannableString(expressionImageNames[arg2].substring(1, expressionImageNames[arg2].length() - 1));
				spannableString.setSpan(imageSpan, 0, expressionImageNames[arg2].length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				// handler.sendMessage(handler.obtainMessage(SET_FACE, 0, 0,
				// spannableString));
				// 编辑框设置数据
				dynaic_text.append(spannableString);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chatting_biaoqing_btn:
			if (setFace == 0) {
				showExpression();
			} else if (setFace == 1) {
				removeExpression();
			}
			break;
		case R.id.dynaic_text:
			biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_pressed);
			viewPager.setVisibility(View.GONE);
			page_select.setVisibility(View.GONE);
			break;
		case R.id.formclient_btsend:
			handler.sendMessage(handler.obtainMessage(SET_FACE, 0, 0, dynaic_text.getText()));
			break;
		default:
			break;
		}
	}

	private void removeExpression() {
		setFace = 0;
		biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_pressed);
		viewPager.setVisibility(View.GONE);
		page_select.setVisibility(View.GONE);
		// InputMethodManager inputMethodManager = (InputMethodManager)
		// context.getSystemService(
		// Context.INPUT_METHOD_SERVICE);
		// inputMethodManager.toggleSoftInput(0,
		// InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void showExpression() {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(dynaic_text.getWindowToken(), 0);

		setFace = 1;
		biaoqingBtn.setBackgroundResource(R.drawable.chatting_setmode_biaoqing_btn_focused);
		viewPager.setVisibility(View.VISIBLE);
		page_select.setVisibility(View.VISIBLE);
	}

	public void clearEdit() {
		dynaic_text.setText("");
		removeExpression();
	}
}
