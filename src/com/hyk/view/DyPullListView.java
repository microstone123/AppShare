package com.hyk.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.activity.center.SetDynamicActivity;
import com.hyk.utils.ACache;
import com.hyk.utils.ImageLoaderPartner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/***
 * 自定义拖拉ListView
 * 
 * 
 */
@SuppressLint("NewApi")
public class DyPullListView extends ListView implements OnScrollListener, OnClickListener {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	// 下拉ListView枚举所有状态
	private enum DListViewState {
		LV_NORMAL, // 普通状态
		LV_PULL_REFRESH, // 下拉状态（为超过mHeadViewHeight）
		LV_RELEASE_REFRESH, // 松开可刷新状态（超过mHeadViewHeight）
		LV_LOADING;// 加载状态
	}

	private OnRefreshLoadingMoreListener onRefreshLoadingMoreListener;// 下拉刷新接口（自定义）

	// 点击加载更多枚举所有状态
	private enum DListViewLoadingMore {
		LV_NORMAL, // 普通状态
		LV_PULL_REFRESH, // 上拉状态（为超过mHeadViewHeight）
		LV_RELEASE_REFRESH, // 松开可刷新状态（超过mHeadViewHeight）
		LV_LOADING, // 加载状态
	}

	private View mHeadView, mFootView, dynamicView;// 头部headView

	private int mHeadViewWidth; // headView的宽（mHeadView）
	private int mHeadViewHeight;// headView的高（mHeadView）
	private int mFooterViewWidth; // headView的宽（mHeadView）
	private int mFooterViewHeight;// headView的高（mHeadView）
	private int mFirstItemIndex = -1;// 当前视图能看到的第一个项的索引

	private int mLastItemIndex = -1;// 当前视图中是否是最后一项.
	private boolean mBack = false;// headView是否返回.

	private boolean mFootBack = false;// headView是否返回.

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean mIsRecord = false;// 针对下拉
	private ImageView mArrowImageView;// 下拉图标（mHeadView）
	private ProgressBar mHeadProgressBar;// 刷新进度体（mHeadView）
	private TextView mRefreshTextview; // 刷新msg（mHeadView）
	private TextView mLastUpdateTextView;// 更新事件（mHeadView）

	private ImageView mFooterArrowImageView;// 下拉图标（mHeadView）
	private ProgressBar mFooterProgressBar;// 刷新进度体（mHeadView）
	private TextView mFooterRefreshTextview; // 刷新msg（mHeadView）
	// private TextView mFooterLastUpdateTextView;// 更新事件（mHeadView）

	private boolean mIsRecord_B = false;// 针对上拉

	private int mStartY, mMoveY;// 按下是的y坐标,move时的y坐标

	private DListViewState mlistViewState = DListViewState.LV_NORMAL;// 拖拉状态.(自定义枚举)

	private DListViewLoadingMore loadingMoreState = DListViewLoadingMore.LV_NORMAL;// 加载更多默认状态.

	private final static int RATIO = 2;// 手势下拉距离比.
	private final static int DOWNDRATIO = 4;// 手势下拉距离比.
	private Animation animation, reverseAnimation;// 旋转动画，旋转动画之后旋转动画.

	private boolean isScroller = true;// 是否屏蔽ListView滑动。

	private String refreshTime;
	private final static int DRAG_UP = 1, DRAG_DOWN = 2;
	private RoundImageView user_img_dy;
	private TextView attention_me_num, me_attention_num, user_name_dy, sign_name_dy;
	private ImageButton set_dy_sign;

	private Context context;
	private ACache mAcache;

	public DyPullListView(Context context) {
		super(context, null);
		this.context = context;
		mAcache = ACache.get(context);
		initDragListView(context);
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = ImageLoaderPartner.getOptions(R.drawable.the_default);
	}

	public DyPullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initDragListView(context);
		mAcache = ACache.get(context);
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = ImageLoaderPartner.getOptions(R.drawable.the_default);
	}

	// 注入下拉刷新接口
	public void setOnRefreshListener(OnRefreshLoadingMoreListener onRefreshLoadingMoreListener) {
		this.onRefreshLoadingMoreListener = onRefreshLoadingMoreListener;
	}

	/***
	 * 下拉刷新完毕
	 */
	public void onRefreshComplete() {
		mLastUpdateTextView.setText("最近更新:" + refreshTime);
		refreshTime = getSysDate();// 更新时间
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);// 回归.
		switchHeadViewState(mlistViewState.LV_NORMAL);//
	}

	/***
	 * 点击加载更多
	 * 
	 * @param flag
	 *            数据是否已全部加载完毕
	 */
	public void onLoadMoreComplete(boolean flag) {
		try {
			mFootView.setPadding(0, 0, 0, -mFooterViewHeight);// 回归.
			switchFooterViewState(loadingMoreState.LV_NORMAL);//
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/***
	 * 初始化ListView
	 */
	public void initDragListView(Context context) {

		refreshTime = getSysDate();// 更新时间

		initHeadView(context, refreshTime);// 初始化该head.

		initFooterView(context);// 初始化footer

		setOnScrollListener(this);// ListView滚动监听
	}

	/***
	 * 初始话头部HeadView
	 * 
	 * @param context
	 *            上下文
	 * @param time
	 *            上次更新时间
	 */
	public void initHeadView(Context context, String time) {
		mHeadView = LayoutInflater.from(context).inflate(R.layout.pull_head, null);
		dynamicView = LayoutInflater.from(context).inflate(R.layout.test_dynamic, null);

		sign_name_dy = (TextView) dynamicView.findViewById(R.id.sign_name_dy);
		user_name_dy = (TextView) dynamicView.findViewById(R.id.user_name_dy);
		me_attention_num = (TextView) dynamicView.findViewById(R.id.me_attention_num);
		attention_me_num = (TextView) dynamicView.findViewById(R.id.attention_me_num);
		user_img_dy = (RoundImageView) dynamicView.findViewById(R.id.user_img_dy);
		set_dy_sign = (ImageButton) dynamicView.findViewById(R.id.set_dy_sign);
		set_dy_sign.setOnClickListener(this);
		mArrowImageView = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(60);

		mHeadProgressBar = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);

		mRefreshTextview = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);

		mLastUpdateTextView = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);
		// 显示更新事件
		mLastUpdateTextView.setText("最近更新:" + time);

		measureView(mHeadView);
		// 获取宽和高
		mHeadViewWidth = mHeadView.getMeasuredWidth();
		mHeadViewHeight = mHeadView.getMeasuredHeight();

		addHeaderView(mHeadView, null, false);// 将初始好的ListView add进拖拽ListView
		// 在这里我们要将此headView设置到顶部不显示位置.
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

		addHeaderView(dynamicView);

		initAnimation();// 初始化动画

	}

	public void setDy(String name, String sign, String meAtt, String attMe, Drawable draw, String url) {
		if (StringUtils.isEmpty(meAtt)) {
			meAtt = "0";
		}
		if (StringUtils.isEmpty(attMe)) {
			attMe = "0";
		}
		sign_name_dy.setText(sign);
		user_name_dy.setText(name);
		me_attention_num.setText(attMe);
		attention_me_num.setText(meAtt);
		if (StringUtils.isNotEmpty(url)) {
			if (url.startsWith("http")) {
				imageLoader.displayImage(url, user_img_dy, options);
			} else {
				user_img_dy.setImageDrawable(draw);
			}
		} else {
			user_img_dy.setImageDrawable(draw);
		}
	}

	/***
	 * 初始化底部加载更多控件
	 */
	private void initFooterView(Context context) {

		mFootView = LayoutInflater.from(context).inflate(R.layout.pull_footer, null);
		mFooterArrowImageView = (ImageView) mFootView.findViewById(R.id.footer_arrowImageView);
		mFooterArrowImageView.setMinimumWidth(60);

		mFooterProgressBar = (ProgressBar) mFootView.findViewById(R.id.footer_progressBar);

		mFooterRefreshTextview = (TextView) mFootView.findViewById(R.id.footer_tipsTextView);

		// mFooterLastUpdateTextView = (TextView)
		// mFootView.findViewById(R.id.footer_lastUpdatedTextView);
		// 显示更新事件
		// mLastUpdateTextView.setText("最近更新:" + time);

		measureView(mFootView);
		// 获取宽和高
		mFooterViewWidth = mFootView.getMeasuredWidth();
		mFooterViewHeight = mFootView.getMeasuredHeight();
		addFooterView(mFootView, null, false);// 将初始好的ListView add进拖拽ListView
		// 在这里我们要将此headView设置到顶部不显示位置.
		mFootView.setPadding(0, -1 * mFooterViewHeight, 0, 0);

		initAnimation();// 初始化动画
	}

	/***
	 * 初始化动画
	 */
	private void initAnimation() {
		// 旋转动画
		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());// 匀速
		animation.setDuration(250);
		animation.setFillAfter(true);// 停留在最后状态.
		// 反向旋转动画
		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(250);
		reverseAnimation.setFillAfter(true);
	}

	/***
	 * 作用：测量 headView的宽和高.
	 * 
	 * @param child
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/***
	 * touch 事件监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		// 按下
		case MotionEvent.ACTION_DOWN:
			doActionDown_B(ev);
			doActionDown(ev);
			break;
		// 移动
		case MotionEvent.ACTION_MOVE:
			doActionMove_B(ev);
			doActionMove(ev);
			break;
		// 抬起
		case MotionEvent.ACTION_UP:
			doActionUp_B(ev);
			doActionUp(ev);
			break;
		default:
			break;
		}

		/***
		 * 如果是ListView本身的拉动，那么返回true，这样ListView不可以拖动.
		 * 如果不是ListView的拉动，那么调用父类方法，这样就可以上拉执行.
		 */
		if (isScroller) {
			return super.onTouchEvent(ev);
		} else {
			return true;
		}

	}

	/***
	 * 摁下操作
	 * 
	 * 作用：获取摁下是的y坐标
	 * 
	 * @param event
	 */
	void doActionDown(MotionEvent event) {
		// 如果是第一项且是一次touch
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecord = true;
		}
	}

	/***
	 * 摁下操作 底部
	 * 
	 * 作用：获取摁下是的y坐标
	 */
	void doActionDown_B(MotionEvent event) {
		// 如果是第一项且是一次touch
		if (mIsRecord_B == false && mLastItemIndex == getCount()) {
			mStartY = (int) event.getY();
			mIsRecord_B = true;
		}
	}

	// 切换headview视图
	private void switchHeadViewState(DListViewState state) {

		switch (state) {
		// 普通状态
		case LV_NORMAL: {
			mArrowImageView.clearAnimation();// 清除动画
			mArrowImageView.setImageResource(R.drawable.arrow);
		}
			break;
		// 下拉状态
		case LV_PULL_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// 隐藏进度条
			mArrowImageView.setVisibility(View.VISIBLE);// 下拉图标
			mRefreshTextview.setText("下拉刷新");
			mArrowImageView.clearAnimation();// 清除动画

			// 是有可刷新状态（LV_RELEASE_REFRESH）转为这个状态才执行，其实就是你下拉后在上拉会执行.
			if (mBack) {
				mBack = false;
				mArrowImageView.clearAnimation();// 清除动画
				mArrowImageView.startAnimation(reverseAnimation);// 启动反转动画
			}
		}
			break;
		// 松开刷新状态
		case LV_RELEASE_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// 隐藏进度条
			mArrowImageView.setVisibility(View.VISIBLE);// 显示下拉图标
			mRefreshTextview.setText("释放立即刷新");
			mArrowImageView.clearAnimation();// 清除动画
			mArrowImageView.startAnimation(animation);// 启动动画
		}
			break;
		// 加载状态
		case LV_LOADING: {
			// Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_LOADING");
			mHeadProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mRefreshTextview.setText("加载入...");
		}
			break;
		default:
			return;
		}
		// 切记不要忘记时时更新状态。
		mlistViewState = state;

	}

	// 切换Footerview视图
	private void switchFooterViewState(DListViewLoadingMore state) {

		switch (state) {
		// 普通状态
		case LV_NORMAL: {
			mFooterArrowImageView.clearAnimation();// 清除动画
			mFooterArrowImageView.setImageResource(R.drawable.arrow);
		}
			break;
		// 下拉状态
		case LV_PULL_REFRESH: {
			mFooterProgressBar.setVisibility(View.GONE);// 隐藏进度条
			mFooterArrowImageView.setVisibility(View.VISIBLE);// 下拉图标
			mFooterRefreshTextview.setText("下拉加载更多");
			mFooterArrowImageView.clearAnimation();// 清除动画

			// 是有可刷新状态（LV_RELEASE_REFRESH）转为这个状态才执行，其实就是你下拉后在上拉会执行.
			if (mFootBack) {
				mFootBack = false;
				mFooterArrowImageView.clearAnimation();// 清除动画
				mFooterArrowImageView.startAnimation(reverseAnimation);// 启动反转动画
			}
		}
			break;
		// 松开刷新状态
		case LV_RELEASE_REFRESH: {
			mFooterProgressBar.setVisibility(View.GONE);// 隐藏进度条
			mFooterArrowImageView.setVisibility(View.VISIBLE);// 显示下拉图标
			mFooterRefreshTextview.setText("释放立即加载更多");
			mFooterArrowImageView.clearAnimation();// 清除动画
			mFooterArrowImageView.startAnimation(animation);// 启动动画
		}
			break;
		// 加载状态
		case LV_LOADING: {
			// Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_LOADING");
			mFooterProgressBar.setVisibility(View.VISIBLE);
			mFooterArrowImageView.clearAnimation();
			mFooterArrowImageView.setVisibility(View.GONE);
			mFooterRefreshTextview.setText("加载入...");
		}
			break;
		default:
			return;
		}
		// 切记不要忘记时时更新状态。
		loadingMoreState = state;

	}

	/***
	 * 拖拽移动操作
	 * 
	 * @param event
	 */
	void doActionMove(MotionEvent event) {
		mMoveY = (int) event.getY();// 获取实时滑动y坐标
		// 检测是否是一次touch事件.
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecord = true;
		}
		/***
		 * 如果touch关闭或者正处于Loading状态的话 return.
		 */
		if (mIsRecord == false || mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// 向下啦headview移动距离为y移动的一半.（比较友好）
		int offset = (mMoveY - mStartY) / RATIO;

		switch (mlistViewState) {
		// 普通状态
		case LV_NORMAL: {
			// 如果<0，则意味着上滑动.
			if (offset > 0) {
				// 设置headView的padding属性.
				mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
				switchHeadViewState(DListViewState.LV_PULL_REFRESH);// 下拉状态
			}

		}
			break;
		// 下拉状态
		case LV_PULL_REFRESH: {
			setSelection(0);// 选中第一项，可选.
			// 设置headView的padding属性.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			if (offset < 0) {
				/***
				 * 要明白为什么isScroller = false;
				 */
				isScroller = false;
				switchHeadViewState(DListViewState.LV_NORMAL);// 普通状态
			} else if (offset > mHeadViewHeight) {// 如果下拉的offset超过headView的高度则要执行刷新.
				switchHeadViewState(DListViewState.LV_RELEASE_REFRESH);// 更新为可刷新的下拉状态.
			}
		}
			break;
		// 可刷新状态
		case LV_RELEASE_REFRESH: {
			setSelection(0);
			// 设置headView的padding属性.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			// 下拉offset>0，但是没有超过headView的高度.那么要goback 原装.
			if (offset >= 0 && offset <= mHeadViewHeight) {
				mBack = true;
				switchHeadViewState(DListViewState.LV_PULL_REFRESH);
			} else if (offset < 0) {
				switchHeadViewState(DListViewState.LV_NORMAL);
			} else {

			}
		}
			break;
		default:
			return;
		}
		;
	}

	void doActionMove_B(MotionEvent event) {
		// mMoveY = (int) event.getY();// 获取实时滑动y坐标
		// // 检测是否是一次touch事件.
		// if (mIsRecord_B == false && mLastItemIndex == 0) {
		// mStartY = (int) event.getY();
		// mIsRecord_B = true;
		// }
		// /***
		// * 如果touch关闭或者正处于Loading状态的话 return.
		// */
		// if (mIsRecord_B == false || loadingMoreState ==
		// DListViewLoadingMore.LV_LOADING) {
		// return;
		// }
		// // 向下啦headview移动距离为y移动的一半.（比较友好）
		// int offset = (mMoveY - mStartY) / RATIO;
		//
		// switch (loadingMoreState) {
		// // 普通状态
		// case LV_NORMAL: {
		// // 说明上拉
		// if (offset < 0) {
		// int distance = Math.abs(offset);
		// // 设置headView的padding属性.
		// mFootView.setPadding(0, distance - mFooterViewHeight, 0, 0);
		// // loadingMoreState = loadingMoreState.LV_PULL_REFRESH;// 上拉状态
		// switchFooterViewState(loadingMoreState.LV_PULL_REFRESH);
		// }
		// }
		// break;
		// // 上拉状态
		// case LV_PULL_REFRESH: {
		// setSelection(getCount() - 1);// 时时保持最底部
		// // 设置headView的padding属性.
		// int distance = Math.abs(offset);
		// mFootView.setPadding(0, distance - mFooterViewHeight, 0, 0);
		// // mFootView.setPadding(0, 0, 0, distance-mFooterViewHeight);
		// // 说明下滑
		// if (offset > 0) {
		// /***
		// * 要明白为什么isScroller = false;
		// */
		// isScroller = false;
		// switchFooterViewState(DListViewLoadingMore.LV_RELEASE_REFRESH);//
		// 普通状态
		// } else if (offset <= 0) {// 如果下拉的offset超过headView的高度则要执行刷新.
		// switchFooterViewState(DListViewLoadingMore.LV_RELEASE_REFRESH);//
		// 更新为可刷新的下拉状态.
		// }
		//
		// }
		// break;
		// case LV_RELEASE_REFRESH: {
		// setSelection(getCount() - 1);// 时时保持最底部
		// // 设置headView的padding属性.
		// int distance = Math.abs(offset);
		// mFootView.setPadding(0, 0, 0, distance - mFooterViewHeight);
		// Log.e("offset", offset+"");
		// if (offset < 0 && offset >= - mFooterViewHeight) {
		// mFootBack = true;
		// switchFooterViewState(DListViewLoadingMore.LV_PULL_REFRESH);
		// } else if (offset > 0) {
		// switchFooterViewState(DListViewLoadingMore.LV_NORMAL);
		// } else {
		//
		// }

		// setSelection(0);
		// // 设置headView的padding属性.
		// mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
		// // 下拉offset>0，但是没有超过headView的高度.那么要goback 原装.
		// if (offset >= 0 && offset <= mHeadViewHeight) {
		// mBack = true;
		// switchHeadViewState(DListViewState.LV_PULL_REFRESH);
		// } else if (offset < 0) {
		// switchHeadViewState(DListViewState.LV_NORMAL);
		// } else {
		//
		// }
		// }
		// break;
		// default:
		// return;
		// }

		mMoveY = (int) event.getY();// 获取实时滑动y坐标
		// 检测是否是一次touch事件.(若mFirstItemIndex为0则要初始化mStartY)
		if (mIsRecord_B == false && mLastItemIndex == getCount()) {
			mStartY = (int) event.getY();
			mIsRecord_B = true;
		}
		// 直接返回说明不是最后一项
		if (mIsRecord_B == false)
			return;

		// 向下啦headview移动距离为y移动的一半.（比较友好）
		int offset = (mMoveY - mStartY) / RATIO;

		switch (loadingMoreState) {
		// 普通状态
		case LV_NORMAL: {
			// 说明上拉
			if (offset < 0) {
				int distance = Math.abs(offset);
				// 设置headView的padding属性.
				mFootView.setPadding(0, distance - mFooterViewHeight, 0, 0);
				loadingMoreState = loadingMoreState.LV_RELEASE_REFRESH;// 下拉状态
				// Log.e("loadingMoreState", "loadingMoreState");
			}
		}
			break;
		// 上拉状态
		case LV_PULL_REFRESH: {
			// setSelection(getCount() - 1);// 时时保持最底部
			// // 设置headView的padding属性.
			// int distance = Math.abs(offset);
			// mFootView.setPadding(0, distance - mFooterViewHeight, 0, 0);
			// 说明下滑
			if (offset > 0) {
				/***
				 * 要明白为什么isScroller = false;
				 */
				isScroller = false;
				loadingMoreState = loadingMoreState.LV_NORMAL;
			}
		}
			break;
		default:
			return;
		}

	}

	/***
	 * 手势抬起操作
	 * 
	 * @param event
	 */
	public void doActionUp(MotionEvent event) {
		mIsRecord = false;// 此时的touch事件完毕，要关闭。
		isScroller = true;// ListView可以Scrooler滑动.
		mBack = false;
		// 如果下拉状态处于loading状态.
		if (mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// 处理相应状态.
		switch (mlistViewState) {
		// 普通状态
		case LV_NORMAL:

			break;
		// 下拉状态
		case LV_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			switchHeadViewState(mlistViewState.LV_NORMAL);
			break;
		// 刷新状态
		case LV_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchHeadViewState(mlistViewState.LV_LOADING);
			onRefresh();// 下拉刷新
			break;
		}

	}

	/***
	 * 下拉刷新
	 */
	private void onRefresh() {
		if (onRefreshLoadingMoreListener != null) {
			onRefreshLoadingMoreListener.onRefresh();
		}
	}

	/***
	 * 下拉刷新
	 */
	private void onLoadMore() {
		if (onRefreshLoadingMoreListener != null) {
			onRefreshLoadingMoreListener.onLoadMore();// 对外提供方法加载更多.
		}
	}

	private void doActionUp_B(MotionEvent event) {
		mIsRecord_B = false; // 此时的touch事件完毕，要关闭。
		isScroller = true;// ListView可以Scrooler滑动.

		mFootBack = false;
		// 如果下拉状态处于loading状态.
		if (loadingMoreState == DListViewLoadingMore.LV_LOADING) {
			return;
		}
		// 处理相应状态.
		switch (loadingMoreState) {
		// 普通状态
		case LV_NORMAL:

			break;
		// 下拉状态
		case LV_PULL_REFRESH:
			mFootView.setPadding(0, -1 * mFooterViewHeight, 0, 0);
			switchFooterViewState(loadingMoreState.LV_NORMAL);
			break;
		// 刷新状态
		case LV_RELEASE_REFRESH:
			mFootView.setPadding(0, 0, 0, 0);
			switchFooterViewState(loadingMoreState.LV_LOADING);
			onLoadMore();// 下拉刷新
			break;
		}
		// mIsRecord = false;// 此时的touch事件完毕，要关闭。
		// isScroller = true;// ListView可以Scrooler滑动.
		// mlistViewState = mlistViewState.LV_NORMAL;// 状态也回归最初状态

		// mIsRecord_B = false; // 此时的touch事件完毕，要关闭。
		// isScroller = true;// ListView可以Scrooler滑动.
		// // mFootView.setPadding(0, 0, 0, 0);
		// switchFooterViewState(loadingMoreState.LV_LOADING);
		// onLoadMore();// 下拉刷新
		// loadingMoreState = loadingMoreState.LV_NORMAL;// 状态也回归最初状态

		// 执行相应动画.
		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute(DRAG_DOWN);

	}

	/***
	 * ListView 滑动监听
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisibleItem;
		mLastItemIndex = firstVisibleItem + visibleItemCount;

	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, context);
		switch (v.getId()) {
		case R.id.set_dy_sign:
			Intent intent = new Intent();
			intent.setClass(context, SetDynamicActivity.class);
			context.startActivity(intent);
			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			mAcache.put("set_dy", "start");
			break;

		default:
			break;
		}
	}

	/***
	 * 自定义接口
	 */
	public interface OnRefreshLoadingMoreListener {
		/***
		 * // 下拉刷新执行
		 */
		void onRefresh();

		/***
		 * 点击加载更多
		 */
		void onLoadMore();
	}

	/**
	 * 获取系统时间
	 */
	public String getSysDate() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dfs.format(curDate);
	}

	public void startScrollListview(int height) { //
		smoothScrollBy(height, 100);// 滑动距离和时间 负数将向下滑动
	}
}
