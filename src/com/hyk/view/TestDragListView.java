package com.hyk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyk.activity.R;

/***
 * �Զ�������ListView
 * 
 * @author zhangjia
 * 
 */
public class TestDragListView extends ListView implements OnClickListener {

	// ������ظ���ö������״̬
	private enum DListViewLoadingMore {
		LV_NORMAL, // ��ͨ״̬
		LV_LOADING, // ����״̬
		LV_OVER; // ����״̬
	}

	private View mFootView;// β��mFootView
	private View mLoadMoreView;// mFootView ��view(mFootView)
	private TextView mLoadMoreTextView;// ���ظ���.(mFootView)
	private RelativeLayout drag_relat;
	private View mLoadingView;// ������...View(mFootView)
	
	private View mHeadView;

	private DListViewLoadingMore loadingMoreState = DListViewLoadingMore.LV_NORMAL;// ���ظ���Ĭ��״̬.

	private OnRefreshLoadingMoreListener onRefreshLoadingMoreListener;// ����ˢ�½ӿڣ��Զ��壩

	public TestDragListView(Context context) {
		super(context, null);
		initDragListView(context);
	}

	public TestDragListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDragListView(context);
	}

	// ע������ˢ�½ӿ�
	public void setOnRefreshListener(OnRefreshLoadingMoreListener onRefreshLoadingMoreListener) {
		this.onRefreshLoadingMoreListener = onRefreshLoadingMoreListener;
	}

	/***
	 * ��ʼ��ListView
	 */
	public void initDragListView(Context context) {

		initLoadMoreView(context);// ��ʼ��footer
	}

	/***
	 * ��ʼ��ͷ��HeadView
	 * 
	 * @param context
	 *            ������
	 * @param time
	 *            �ϴθ���ʱ��
	 */
	public void initHeadView(Context context) {
		mHeadView = LayoutInflater.from(context).inflate(R.layout.test_dynamic, null);
		addHeaderView(mHeadView);
	}
	
	/***
	 * ��ʼ���ײ����ظ���ؼ�
	 */
	private void initLoadMoreView(Context context) {
		mFootView = LayoutInflater.from(context).inflate(R.layout.dragfooter, null);
		mLoadMoreView = mFootView.findViewById(R.id.load_more_view);
		mLoadMoreTextView = (TextView) mFootView.findViewById(R.id.load_more_tv);
		mLoadingView = (LinearLayout) mFootView.findViewById(R.id.loading_layout);
		drag_relat = (RelativeLayout) mFootView.findViewById(R.id.drag_relat);
		mLoadMoreView.setOnClickListener(this);
		addFooterView(mFootView);
	}

	/***
	 * ������ظ���
	 * 
	 * @param flag
	 *            �����Ƿ���ȫ���������
	 */
	public void onLoadMoreComplete(boolean flag) {
		if (flag) {
			updateLoadMoreViewState(DListViewLoadingMore.LV_OVER);
		} else {
			updateLoadMoreViewState(DListViewLoadingMore.LV_NORMAL);
		}

	}
	public void setFinalPage(){
		drag_relat.setVisibility(View.GONE);
	}

	// ����Footview��ͼ
	private void updateLoadMoreViewState(DListViewLoadingMore state) {
		switch (state) {
		// ��ͨ״̬
		case LV_NORMAL:
			mLoadingView.setVisibility(View.GONE);
			mLoadMoreTextView.setVisibility(View.VISIBLE);
			mLoadMoreTextView.setText("�鿴����");
			break;
		// ������״̬
		case LV_LOADING:
			mLoadingView.setVisibility(View.VISIBLE);
			mLoadMoreTextView.setVisibility(View.GONE);
			break;
		// �������״̬
		case LV_OVER:
			mLoadingView.setVisibility(View.GONE);
			mLoadMoreTextView.setVisibility(View.VISIBLE);
			mLoadMoreTextView.setText("�������");
			break;
		default:
			break;
		}
		loadingMoreState = state;
	}

	/***
	 * �ײ�����¼�
	 */
	@Override
	public void onClick(View v) {
		// ��ֹ�ظ����
		if (onRefreshLoadingMoreListener != null && loadingMoreState == DListViewLoadingMore.LV_NORMAL) {
			updateLoadMoreViewState(DListViewLoadingMore.LV_LOADING);
			onRefreshLoadingMoreListener.onLoadMore();// �����ṩ�������ظ���.
		}

	}

	/***
	 * �Զ���ӿ�
	 */
	public interface OnRefreshLoadingMoreListener {

		/***
		 * ������ظ���
		 */
		void onLoadMore();
	}

}
