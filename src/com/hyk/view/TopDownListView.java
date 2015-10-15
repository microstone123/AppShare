package com.hyk.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyk.activity.R;

/***
 * �Զ�����-ListView
 * 
 * 
 */
@SuppressWarnings("static-access")
@SuppressLint("SimpleDateFormat")
public class TopDownListView extends ListView implements OnScrollListener, OnClickListener {
	// ��-ListViewö������״̬
	private enum DListViewState {
		LV_NORMAL, // ��ͨ״̬
		LV_PULL_REFRESH, // ��-״̬��Ϊ����mHeadViewHeight��
		LV_RELEASE_REFRESH, // �ɿ���ˢ��״̬������mHeadViewHeight��
		LV_LOADING;// ����״̬
	}

	private OnRefreshLoadingMoreListener onRefreshLoadingMoreListener;// ��-ˢ�½ӿڣ��Զ��壩
//	private DListViewLoadingMore loadingMoreState = DListViewLoadingMore.LV_NORMAL;// 加载更多默认状�?.

//	// 点击加载更多枚举�?��状�?
//	private enum DListViewLoadingMore {
//		LV_NORMAL, // 普�?状�?
//		LV_PULL_REFRESH, // 上拉状�?（为超过mHeadViewHeight�?
//	}

	private View mHeadView;// ͷ��headView

	private int mHeadViewHeight;// headView�ĸߣ�mHeadView��
	private int mFirstItemIndex = -1;// ��ǰ��ͼ�ܿ����ĵ�һ���������?

//	private int mLastItemIndex = -1;// ��ǰ��ͼ���Ƿ������һ��?
	private boolean mBack = false;// headView�Ƿ񷵻�.

	// ���ڱ�֤startY��ֵ��һ�������touch�¼���ֻ����¼һ��
	private boolean mIsRecord = false;// �����?
	private ImageView mArrowImageView;// ��-ͼ�꣨mHeadView��
	private ProgressBar mHeadProgressBar;// ˢ�½���壨mHeadView��
	private TextView mRefreshTextview; // ˢ��msg��mHeadView��
	private TextView mLastUpdateTextView;// �����¼���mHeadView��

	// private TextView mFooterLastUpdateTextView;// �����¼���mHeadView��

//	private boolean mIsRecord_B = false;// �����?

	private int mStartY, mMoveY;// �����ǵ�y���?moveʱ��y���?
//	private int mStartY_B, mMoveY_B;// �����ǵ�y���?moveʱ��y���?
	private DListViewState mlistViewState = DListViewState.LV_NORMAL;// ��-״̬.(�Զ���ö��)

	// private DListViewLoadingMore loadingMoreState =
	// DListViewLoadingMore.LV_NORMAL;// ���ظ��Ĭ��״�?

	private final static int RATIO = 2;// ������-�����?
//	private final static int DOWNDRATIO = 4;// ������-�����?
	private Animation animation, reverseAnimation;// ��ת��������ת����֮����ת����.

	private boolean isScroller = true;// �Ƿ��q�ListView������

	private String refreshTime;
//	private final static int DRAG_UP = 1, DRAG_DOWN = 2;

	public TopDownListView(Context context) {
		super(context, null);
		initDragListView(context);
	}

	public TopDownListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDragListView(context);
	}

	// ע����-ˢ�½ӿ�
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
	 * ��ʼ��ListView
	 */
	public void initDragListView(Context context) {

		refreshTime = getSysDate();// ����ʱ��

		initHeadView(context, refreshTime);// ��ʼ����head.
//		initFooterView(context);// 初始化footer
		setOnScrollListener(this);// ListView�����?
	}

	/***
	 * ��ʼ��ͷ��HeadView
	 * 
	 * @param context
	 *            ������
	 * @param time
	 *            �ϴθ���ʱ��
	 */
	public void initHeadView(Context context, String time) {
		mHeadView = LayoutInflater.from(context).inflate(R.layout.pull_head, null);
		mArrowImageView = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(60);

		mHeadProgressBar = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);

		mRefreshTextview = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);

		mLastUpdateTextView = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);
		// 显示更新事件
		mLastUpdateTextView.setText("最近更新:" + time);

		measureView(mHeadView);
		mHeadViewHeight = mHeadView.getMeasuredHeight();

		addHeaderView(mHeadView, null, false);
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

		initAnimation();
	}

	/***
	 * ��ʼ������
	 */
	private void initAnimation() {
		// ��ת����
		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());// ����
		animation.setDuration(250);
		animation.setFillAfter(true);// ͣ�������״�?
		// ������ת����
		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(250);
		reverseAnimation.setFillAfter(true);
	}

	/***
	 * ���ã��� headView�Ŀ�͸�?
	 * 
	 * @param child
	 */
	@SuppressWarnings("deprecation")
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
	 * touch �¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (getFirstVisiblePosition() == 0) {
			switch (ev.getAction()) {
			// 按下
			case MotionEvent.ACTION_DOWN:
//				doActionDown_B(ev);
				doActionDown(ev);
				break;
			// 移动
			case MotionEvent.ACTION_MOVE:
//				doActionMove_B(ev);
				doActionMove(ev);
				break;
			// 抬起
			case MotionEvent.ACTION_UP:
//				doActionUp_B(ev);
				doActionUp(ev);
				break;
			default:
				break;
			}
		}
		/***
		 * 如果是ListView本身的拉动，那么返回true，这样ListView不可以拖�?
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
		// ����ǵ�һ������һ��touch
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecord = true;
		}
	}


	// �л�headview��ͼ
	private void switchHeadViewState(DListViewState state) {

		switch (state) {
		// ��ͨ״̬
		case LV_NORMAL: {
			mArrowImageView.clearAnimation();// ���?
			mArrowImageView.setImageResource(R.drawable.arrow);
		}
			break;
		// ��-״̬
		case LV_PULL_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// ���ؽ����?
			mArrowImageView.setVisibility(View.VISIBLE);// ��-ͼ��
			mRefreshTextview.setText("下拉刷新");
			mArrowImageView.clearAnimation();// ���?

			// ���п�ˢ��״̬��LV_RELEASE_REFRESH��תΪ���״̬��ִ�У���ʵ��������?������-��ִ��.
			if (mBack) {
				mBack = false;
				mArrowImageView.clearAnimation();// ���?
				mArrowImageView.startAnimation(reverseAnimation);// ���ת����?
			}
		}
			break;
		// �ɿ�ˢ��״̬
		case LV_RELEASE_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// ���ؽ����?
			mArrowImageView.setVisibility(View.VISIBLE);// ��ʾ��-ͼ��
			mRefreshTextview.setText("释放立即刷新");
			mArrowImageView.clearAnimation();// ���?
			mArrowImageView.startAnimation(animation);// �����?
		}
			break;
		// ����״̬
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
		// �мǲ�Ҫ���ʱʱ����״̬��?
		mlistViewState = state;

	}

	/***
	 * ��ק�ƶ�����
	 * 
	 * @param event
	 */
	void doActionMove(MotionEvent event) {
		mMoveY = (int) event.getY();// ��ȡʵʱ����y���?
		// ����Ƿ���һ��touch�¼�.
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecord = true;
		}
		/***
		 * ���touch�رջ�������Loading״̬�Ļ� return.
		 */
		if (mIsRecord == false || mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// ����2headview�ƶ�����Ϊy�ƶ���һ��.���Ƚ��Ѻã�
		int offset = (mMoveY - mStartY) / RATIO;

		switch (mlistViewState) {
		// ��ͨ״̬
		case LV_NORMAL: {
			// ���?0������ζ���ϻ���.
			if (offset > 0) {
				// ����headView��padding����.
				mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
				switchHeadViewState(DListViewState.LV_PULL_REFRESH);// ��-״̬
			}

		}
			break;
		// ��-״̬
		case LV_PULL_REFRESH: {
			setSelection(0);// ѡ�е�һ���ѡ.
			// ����headView��padding����.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			if (offset < 0) {
				/***
				 * Ҫ���ΪʲôisScroller = false;
				 */
				isScroller = false;
				switchHeadViewState(DListViewState.LV_NORMAL);// ��ͨ״̬
			} else if (offset > mHeadViewHeight) {// �����?��offset����headView�ĸ߶���Ҫִ��ˢ��.
				switchHeadViewState(DListViewState.LV_RELEASE_REFRESH);// ����Ϊ��ˢ�µ���-״̬.
			}
		}
			break;
		// ��ˢ��״̬
		case LV_RELEASE_REFRESH: {
			setSelection(0);
			// ����headView��padding����.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			// ��-offset>0������û�г���headView�ĸ߶�.��ôҪgoback ԭװ.
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

	/***
	 * ����̧�����?
	 * 
	 * @param event
	 */
	@SuppressWarnings("incomplete-switch")
	public void doActionUp(MotionEvent event) {
		mIsRecord = false;// ��ʱ��touch�¼���ϣ�Ҫ�رա�?
		isScroller = true;// ListView����Scrooler����.
		mBack = false;
		// �����?״̬����loading״̬.
		if (mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// ������Ӧ״̬.
		switch (mlistViewState) {
		// ��ͨ״̬
		case LV_NORMAL:

			break;
		// ��-״̬
		case LV_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			switchHeadViewState(mlistViewState.LV_NORMAL);
			break;
		// ˢ��״̬
		case LV_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchHeadViewState(mlistViewState.LV_LOADING);
			onRefresh();// ��-ˢ��
			break;
		}

	}

	/***
	 * ��-ˢ��
	 */
	private void onRefresh() {
		if (onRefreshLoadingMoreListener != null) {
			onRefreshLoadingMoreListener.onRefresh();
		}
	}

	/***
	 * ListView ��������
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisibleItem;

	}

	@Override
	public void onClick(View v) {

	}

	/***
	 * �Զ���ӿ�?
	 */
	public interface OnRefreshLoadingMoreListener {
		/***
		 * // ��-ˢ��ִ��
		 */
		void onRefresh();

	}

	/**
	 * ��ȡϵͳʱ��
	 */
	public String getSysDate() {
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dfs.format(curDate);
	}

	public void startScrollListview(int height) { //
		smoothScrollBy(height, 100);// ���������ʱ��?�������»���
	}

}