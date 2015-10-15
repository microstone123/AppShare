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
 * �Զ�������ListView
 * 
 * 
 */
@SuppressLint("NewApi")
public class DyPullListView extends ListView implements OnScrollListener, OnClickListener {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions����������ͼƬ��ʾ����

	// ����ListViewö������״̬
	private enum DListViewState {
		LV_NORMAL, // ��ͨ״̬
		LV_PULL_REFRESH, // ����״̬��Ϊ����mHeadViewHeight��
		LV_RELEASE_REFRESH, // �ɿ���ˢ��״̬������mHeadViewHeight��
		LV_LOADING;// ����״̬
	}

	private OnRefreshLoadingMoreListener onRefreshLoadingMoreListener;// ����ˢ�½ӿڣ��Զ��壩

	// ������ظ���ö������״̬
	private enum DListViewLoadingMore {
		LV_NORMAL, // ��ͨ״̬
		LV_PULL_REFRESH, // ����״̬��Ϊ����mHeadViewHeight��
		LV_RELEASE_REFRESH, // �ɿ���ˢ��״̬������mHeadViewHeight��
		LV_LOADING, // ����״̬
	}

	private View mHeadView, mFootView, dynamicView;// ͷ��headView

	private int mHeadViewWidth; // headView�Ŀ�mHeadView��
	private int mHeadViewHeight;// headView�ĸߣ�mHeadView��
	private int mFooterViewWidth; // headView�Ŀ�mHeadView��
	private int mFooterViewHeight;// headView�ĸߣ�mHeadView��
	private int mFirstItemIndex = -1;// ��ǰ��ͼ�ܿ����ĵ�һ���������

	private int mLastItemIndex = -1;// ��ǰ��ͼ���Ƿ������һ��.
	private boolean mBack = false;// headView�Ƿ񷵻�.

	private boolean mFootBack = false;// headView�Ƿ񷵻�.

	// ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
	private boolean mIsRecord = false;// �������
	private ImageView mArrowImageView;// ����ͼ�꣨mHeadView��
	private ProgressBar mHeadProgressBar;// ˢ�½����壨mHeadView��
	private TextView mRefreshTextview; // ˢ��msg��mHeadView��
	private TextView mLastUpdateTextView;// �����¼���mHeadView��

	private ImageView mFooterArrowImageView;// ����ͼ�꣨mHeadView��
	private ProgressBar mFooterProgressBar;// ˢ�½����壨mHeadView��
	private TextView mFooterRefreshTextview; // ˢ��msg��mHeadView��
	// private TextView mFooterLastUpdateTextView;// �����¼���mHeadView��

	private boolean mIsRecord_B = false;// �������

	private int mStartY, mMoveY;// �����ǵ�y����,moveʱ��y����

	private DListViewState mlistViewState = DListViewState.LV_NORMAL;// ����״̬.(�Զ���ö��)

	private DListViewLoadingMore loadingMoreState = DListViewLoadingMore.LV_NORMAL;// ���ظ���Ĭ��״̬.

	private final static int RATIO = 2;// �������������.
	private final static int DOWNDRATIO = 4;// �������������.
	private Animation animation, reverseAnimation;// ��ת��������ת����֮����ת����.

	private boolean isScroller = true;// �Ƿ�����ListView������

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

	// ע������ˢ�½ӿ�
	public void setOnRefreshListener(OnRefreshLoadingMoreListener onRefreshLoadingMoreListener) {
		this.onRefreshLoadingMoreListener = onRefreshLoadingMoreListener;
	}

	/***
	 * ����ˢ�����
	 */
	public void onRefreshComplete() {
		mLastUpdateTextView.setText("�������:" + refreshTime);
		refreshTime = getSysDate();// ����ʱ��
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);// �ع�.
		switchHeadViewState(mlistViewState.LV_NORMAL);//
	}

	/***
	 * ������ظ���
	 * 
	 * @param flag
	 *            �����Ƿ���ȫ���������
	 */
	public void onLoadMoreComplete(boolean flag) {
		try {
			mFootView.setPadding(0, 0, 0, -mFooterViewHeight);// �ع�.
			switchFooterViewState(loadingMoreState.LV_NORMAL);//
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/***
	 * ��ʼ��ListView
	 */
	public void initDragListView(Context context) {

		refreshTime = getSysDate();// ����ʱ��

		initHeadView(context, refreshTime);// ��ʼ����head.

		initFooterView(context);// ��ʼ��footer

		setOnScrollListener(this);// ListView��������
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
		// ��ʾ�����¼�
		mLastUpdateTextView.setText("�������:" + time);

		measureView(mHeadView);
		// ��ȡ��͸�
		mHeadViewWidth = mHeadView.getMeasuredWidth();
		mHeadViewHeight = mHeadView.getMeasuredHeight();

		addHeaderView(mHeadView, null, false);// ����ʼ�õ�ListView add����קListView
		// ����������Ҫ����headView���õ���������ʾλ��.
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

		addHeaderView(dynamicView);

		initAnimation();// ��ʼ������

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
	 * ��ʼ���ײ����ظ���ؼ�
	 */
	private void initFooterView(Context context) {

		mFootView = LayoutInflater.from(context).inflate(R.layout.pull_footer, null);
		mFooterArrowImageView = (ImageView) mFootView.findViewById(R.id.footer_arrowImageView);
		mFooterArrowImageView.setMinimumWidth(60);

		mFooterProgressBar = (ProgressBar) mFootView.findViewById(R.id.footer_progressBar);

		mFooterRefreshTextview = (TextView) mFootView.findViewById(R.id.footer_tipsTextView);

		// mFooterLastUpdateTextView = (TextView)
		// mFootView.findViewById(R.id.footer_lastUpdatedTextView);
		// ��ʾ�����¼�
		// mLastUpdateTextView.setText("�������:" + time);

		measureView(mFootView);
		// ��ȡ��͸�
		mFooterViewWidth = mFootView.getMeasuredWidth();
		mFooterViewHeight = mFootView.getMeasuredHeight();
		addFooterView(mFootView, null, false);// ����ʼ�õ�ListView add����קListView
		// ����������Ҫ����headView���õ���������ʾλ��.
		mFootView.setPadding(0, -1 * mFooterViewHeight, 0, 0);

		initAnimation();// ��ʼ������
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
		animation.setFillAfter(true);// ͣ�������״̬.
		// ������ת����
		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(250);
		reverseAnimation.setFillAfter(true);
	}

	/***
	 * ���ã����� headView�Ŀ�͸�.
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
	 * touch �¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		// ����
		case MotionEvent.ACTION_DOWN:
			doActionDown_B(ev);
			doActionDown(ev);
			break;
		// �ƶ�
		case MotionEvent.ACTION_MOVE:
			doActionMove_B(ev);
			doActionMove(ev);
			break;
		// ̧��
		case MotionEvent.ACTION_UP:
			doActionUp_B(ev);
			doActionUp(ev);
			break;
		default:
			break;
		}

		/***
		 * �����ListView�������������ô����true������ListView�������϶�.
		 * �������ListView����������ô���ø��෽���������Ϳ�������ִ��.
		 */
		if (isScroller) {
			return super.onTouchEvent(ev);
		} else {
			return true;
		}

	}

	/***
	 * ���²���
	 * 
	 * ���ã���ȡ�����ǵ�y����
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

	/***
	 * ���²��� �ײ�
	 * 
	 * ���ã���ȡ�����ǵ�y����
	 */
	void doActionDown_B(MotionEvent event) {
		// ����ǵ�һ������һ��touch
		if (mIsRecord_B == false && mLastItemIndex == getCount()) {
			mStartY = (int) event.getY();
			mIsRecord_B = true;
		}
	}

	// �л�headview��ͼ
	private void switchHeadViewState(DListViewState state) {

		switch (state) {
		// ��ͨ״̬
		case LV_NORMAL: {
			mArrowImageView.clearAnimation();// �������
			mArrowImageView.setImageResource(R.drawable.arrow);
		}
			break;
		// ����״̬
		case LV_PULL_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// ���ؽ�����
			mArrowImageView.setVisibility(View.VISIBLE);// ����ͼ��
			mRefreshTextview.setText("����ˢ��");
			mArrowImageView.clearAnimation();// �������

			// ���п�ˢ��״̬��LV_RELEASE_REFRESH��תΪ���״̬��ִ�У���ʵ��������������������ִ��.
			if (mBack) {
				mBack = false;
				mArrowImageView.clearAnimation();// �������
				mArrowImageView.startAnimation(reverseAnimation);// ������ת����
			}
		}
			break;
		// �ɿ�ˢ��״̬
		case LV_RELEASE_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// ���ؽ�����
			mArrowImageView.setVisibility(View.VISIBLE);// ��ʾ����ͼ��
			mRefreshTextview.setText("�ͷ�����ˢ��");
			mArrowImageView.clearAnimation();// �������
			mArrowImageView.startAnimation(animation);// ��������
		}
			break;
		// ����״̬
		case LV_LOADING: {
			// Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_LOADING");
			mHeadProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mRefreshTextview.setText("������...");
		}
			break;
		default:
			return;
		}
		// �мǲ�Ҫ����ʱʱ����״̬��
		mlistViewState = state;

	}

	// �л�Footerview��ͼ
	private void switchFooterViewState(DListViewLoadingMore state) {

		switch (state) {
		// ��ͨ״̬
		case LV_NORMAL: {
			mFooterArrowImageView.clearAnimation();// �������
			mFooterArrowImageView.setImageResource(R.drawable.arrow);
		}
			break;
		// ����״̬
		case LV_PULL_REFRESH: {
			mFooterProgressBar.setVisibility(View.GONE);// ���ؽ�����
			mFooterArrowImageView.setVisibility(View.VISIBLE);// ����ͼ��
			mFooterRefreshTextview.setText("�������ظ���");
			mFooterArrowImageView.clearAnimation();// �������

			// ���п�ˢ��״̬��LV_RELEASE_REFRESH��תΪ���״̬��ִ�У���ʵ��������������������ִ��.
			if (mFootBack) {
				mFootBack = false;
				mFooterArrowImageView.clearAnimation();// �������
				mFooterArrowImageView.startAnimation(reverseAnimation);// ������ת����
			}
		}
			break;
		// �ɿ�ˢ��״̬
		case LV_RELEASE_REFRESH: {
			mFooterProgressBar.setVisibility(View.GONE);// ���ؽ�����
			mFooterArrowImageView.setVisibility(View.VISIBLE);// ��ʾ����ͼ��
			mFooterRefreshTextview.setText("�ͷ��������ظ���");
			mFooterArrowImageView.clearAnimation();// �������
			mFooterArrowImageView.startAnimation(animation);// ��������
		}
			break;
		// ����״̬
		case LV_LOADING: {
			// Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_LOADING");
			mFooterProgressBar.setVisibility(View.VISIBLE);
			mFooterArrowImageView.clearAnimation();
			mFooterArrowImageView.setVisibility(View.GONE);
			mFooterRefreshTextview.setText("������...");
		}
			break;
		default:
			return;
		}
		// �мǲ�Ҫ����ʱʱ����״̬��
		loadingMoreState = state;

	}

	/***
	 * ��ק�ƶ�����
	 * 
	 * @param event
	 */
	void doActionMove(MotionEvent event) {
		mMoveY = (int) event.getY();// ��ȡʵʱ����y����
		// ����Ƿ���һ��touch�¼�.
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecord = true;
		}
		/***
		 * ���touch�رջ���������Loading״̬�Ļ� return.
		 */
		if (mIsRecord == false || mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// ������headview�ƶ�����Ϊy�ƶ���һ��.���Ƚ��Ѻã�
		int offset = (mMoveY - mStartY) / RATIO;

		switch (mlistViewState) {
		// ��ͨ״̬
		case LV_NORMAL: {
			// ���<0������ζ���ϻ���.
			if (offset > 0) {
				// ����headView��padding����.
				mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
				switchHeadViewState(DListViewState.LV_PULL_REFRESH);// ����״̬
			}

		}
			break;
		// ����״̬
		case LV_PULL_REFRESH: {
			setSelection(0);// ѡ�е�һ���ѡ.
			// ����headView��padding����.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			if (offset < 0) {
				/***
				 * Ҫ����ΪʲôisScroller = false;
				 */
				isScroller = false;
				switchHeadViewState(DListViewState.LV_NORMAL);// ��ͨ״̬
			} else if (offset > mHeadViewHeight) {// ���������offset����headView�ĸ߶���Ҫִ��ˢ��.
				switchHeadViewState(DListViewState.LV_RELEASE_REFRESH);// ����Ϊ��ˢ�µ�����״̬.
			}
		}
			break;
		// ��ˢ��״̬
		case LV_RELEASE_REFRESH: {
			setSelection(0);
			// ����headView��padding����.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			// ����offset>0������û�г���headView�ĸ߶�.��ôҪgoback ԭװ.
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
		// mMoveY = (int) event.getY();// ��ȡʵʱ����y����
		// // ����Ƿ���һ��touch�¼�.
		// if (mIsRecord_B == false && mLastItemIndex == 0) {
		// mStartY = (int) event.getY();
		// mIsRecord_B = true;
		// }
		// /***
		// * ���touch�رջ���������Loading״̬�Ļ� return.
		// */
		// if (mIsRecord_B == false || loadingMoreState ==
		// DListViewLoadingMore.LV_LOADING) {
		// return;
		// }
		// // ������headview�ƶ�����Ϊy�ƶ���һ��.���Ƚ��Ѻã�
		// int offset = (mMoveY - mStartY) / RATIO;
		//
		// switch (loadingMoreState) {
		// // ��ͨ״̬
		// case LV_NORMAL: {
		// // ˵������
		// if (offset < 0) {
		// int distance = Math.abs(offset);
		// // ����headView��padding����.
		// mFootView.setPadding(0, distance - mFooterViewHeight, 0, 0);
		// // loadingMoreState = loadingMoreState.LV_PULL_REFRESH;// ����״̬
		// switchFooterViewState(loadingMoreState.LV_PULL_REFRESH);
		// }
		// }
		// break;
		// // ����״̬
		// case LV_PULL_REFRESH: {
		// setSelection(getCount() - 1);// ʱʱ������ײ�
		// // ����headView��padding����.
		// int distance = Math.abs(offset);
		// mFootView.setPadding(0, distance - mFooterViewHeight, 0, 0);
		// // mFootView.setPadding(0, 0, 0, distance-mFooterViewHeight);
		// // ˵���»�
		// if (offset > 0) {
		// /***
		// * Ҫ����ΪʲôisScroller = false;
		// */
		// isScroller = false;
		// switchFooterViewState(DListViewLoadingMore.LV_RELEASE_REFRESH);//
		// ��ͨ״̬
		// } else if (offset <= 0) {// ���������offset����headView�ĸ߶���Ҫִ��ˢ��.
		// switchFooterViewState(DListViewLoadingMore.LV_RELEASE_REFRESH);//
		// ����Ϊ��ˢ�µ�����״̬.
		// }
		//
		// }
		// break;
		// case LV_RELEASE_REFRESH: {
		// setSelection(getCount() - 1);// ʱʱ������ײ�
		// // ����headView��padding����.
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
		// // ����headView��padding����.
		// mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
		// // ����offset>0������û�г���headView�ĸ߶�.��ôҪgoback ԭװ.
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

		mMoveY = (int) event.getY();// ��ȡʵʱ����y����
		// ����Ƿ���һ��touch�¼�.(��mFirstItemIndexΪ0��Ҫ��ʼ��mStartY)
		if (mIsRecord_B == false && mLastItemIndex == getCount()) {
			mStartY = (int) event.getY();
			mIsRecord_B = true;
		}
		// ֱ�ӷ���˵���������һ��
		if (mIsRecord_B == false)
			return;

		// ������headview�ƶ�����Ϊy�ƶ���һ��.���Ƚ��Ѻã�
		int offset = (mMoveY - mStartY) / RATIO;

		switch (loadingMoreState) {
		// ��ͨ״̬
		case LV_NORMAL: {
			// ˵������
			if (offset < 0) {
				int distance = Math.abs(offset);
				// ����headView��padding����.
				mFootView.setPadding(0, distance - mFooterViewHeight, 0, 0);
				loadingMoreState = loadingMoreState.LV_RELEASE_REFRESH;// ����״̬
				// Log.e("loadingMoreState", "loadingMoreState");
			}
		}
			break;
		// ����״̬
		case LV_PULL_REFRESH: {
			// setSelection(getCount() - 1);// ʱʱ������ײ�
			// // ����headView��padding����.
			// int distance = Math.abs(offset);
			// mFootView.setPadding(0, distance - mFooterViewHeight, 0, 0);
			// ˵���»�
			if (offset > 0) {
				/***
				 * Ҫ����ΪʲôisScroller = false;
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
	 * ����̧�����
	 * 
	 * @param event
	 */
	public void doActionUp(MotionEvent event) {
		mIsRecord = false;// ��ʱ��touch�¼���ϣ�Ҫ�رա�
		isScroller = true;// ListView����Scrooler����.
		mBack = false;
		// �������״̬����loading״̬.
		if (mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// ������Ӧ״̬.
		switch (mlistViewState) {
		// ��ͨ״̬
		case LV_NORMAL:

			break;
		// ����״̬
		case LV_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			switchHeadViewState(mlistViewState.LV_NORMAL);
			break;
		// ˢ��״̬
		case LV_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchHeadViewState(mlistViewState.LV_LOADING);
			onRefresh();// ����ˢ��
			break;
		}

	}

	/***
	 * ����ˢ��
	 */
	private void onRefresh() {
		if (onRefreshLoadingMoreListener != null) {
			onRefreshLoadingMoreListener.onRefresh();
		}
	}

	/***
	 * ����ˢ��
	 */
	private void onLoadMore() {
		if (onRefreshLoadingMoreListener != null) {
			onRefreshLoadingMoreListener.onLoadMore();// �����ṩ�������ظ���.
		}
	}

	private void doActionUp_B(MotionEvent event) {
		mIsRecord_B = false; // ��ʱ��touch�¼���ϣ�Ҫ�رա�
		isScroller = true;// ListView����Scrooler����.

		mFootBack = false;
		// �������״̬����loading״̬.
		if (loadingMoreState == DListViewLoadingMore.LV_LOADING) {
			return;
		}
		// ������Ӧ״̬.
		switch (loadingMoreState) {
		// ��ͨ״̬
		case LV_NORMAL:

			break;
		// ����״̬
		case LV_PULL_REFRESH:
			mFootView.setPadding(0, -1 * mFooterViewHeight, 0, 0);
			switchFooterViewState(loadingMoreState.LV_NORMAL);
			break;
		// ˢ��״̬
		case LV_RELEASE_REFRESH:
			mFootView.setPadding(0, 0, 0, 0);
			switchFooterViewState(loadingMoreState.LV_LOADING);
			onLoadMore();// ����ˢ��
			break;
		}
		// mIsRecord = false;// ��ʱ��touch�¼���ϣ�Ҫ�رա�
		// isScroller = true;// ListView����Scrooler����.
		// mlistViewState = mlistViewState.LV_NORMAL;// ״̬Ҳ�ع����״̬

		// mIsRecord_B = false; // ��ʱ��touch�¼���ϣ�Ҫ�رա�
		// isScroller = true;// ListView����Scrooler����.
		// // mFootView.setPadding(0, 0, 0, 0);
		// switchFooterViewState(loadingMoreState.LV_LOADING);
		// onLoadMore();// ����ˢ��
		// loadingMoreState = loadingMoreState.LV_NORMAL;// ״̬Ҳ�ع����״̬

		// ִ����Ӧ����.
		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute(DRAG_DOWN);

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
	 * �Զ���ӿ�
	 */
	public interface OnRefreshLoadingMoreListener {
		/***
		 * // ����ˢ��ִ��
		 */
		void onRefresh();

		/***
		 * ������ظ���
		 */
		void onLoadMore();
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
		smoothScrollBy(height, 100);// ���������ʱ�� ���������»���
	}
}
