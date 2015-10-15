package com.hyk.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.app.APPCommentList;
import com.hyk.baseadapter.AppCommentAdapter;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForComment;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.ToastUtil;
import com.hyk.view.DragListView;
import com.hyk.xmpp.XmppApplication;

/**
 * @ClassName: DetailsFragment
 * @Description: TODO(应用评论Fragment)
 * @author linhs
 * @date 2013-12-9 下午3:51:49
 * 
 */
public class AppReviewFragment extends Fragment implements DragListView.OnRefreshLoadingMoreListener {

	private AppCommentAdapter appCommentAdapter;
	private DragListView commentList;
	private String appId;
	private ProgressBar the_bar;
	private int pageSize = 10;
	private int currentPage = 1;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private boolean isrefes = false;
	private int pageCount = 1;
	private RelativeLayout re_re;
	private APPCommentList aPPCommentList;

	// private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
		return inflater.inflate(R.layout.app_review_xml, container, true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupViews();

		// requestQueue = Volley.newRequestQueue(getActivity());
		// imageLoaderShow = new ImageLoaderShow(requestQueue);

		httpForReviewData();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public void setupViews() {
		re_re = (RelativeLayout) getActivity().findViewById(R.id.re_re);
		commentList = (DragListView) getActivity().findViewById(R.id.comment_list);
		the_bar = (ProgressBar) getActivity().findViewById(R.id.the_bar);
		commentList.setOnRefreshListener(this);
		appId = getActivity().getIntent().getExtras().getString("appId");
	}

	@Override
	public void onLoadMore() {
		isrefes = true;
		currentPage++;
		if (currentPage > pageCount) {
			commentList.setFinalPage();
		} else {
			httpForReviewData();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void setPage(int total) {
		pageCount = total / pageSize;
		int s = total % pageSize;
		if (s != 0) {
			pageCount++;
		}
	}

	private void httpForReviewData() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APPID_REVIEW_ADDRESS;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", appId);
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String httpUrl = HttpUtils.checkUrl(url, params);

		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				the_bar.setVisibility(View.GONE);
				re_re.setVisibility(View.GONE);
				ResultStringForComment resultStringForComment = binder.fromJson(response, ResultStringForComment.class);
				if (resultStringForComment.getResult() == 0) {
					aPPCommentList = resultStringForComment.getObj();
					setPage(resultStringForComment.getObj().getTotal());
				}

				if (aPPCommentList != null) {
					if (aPPCommentList.getList().size() <= 0) {
						commentList.setFinalPage();
					}
				}
				if (!isrefes) {
					appCommentAdapter = new AppCommentAdapter(getActivity(), aPPCommentList.getList());
					commentList.setAdapter(appCommentAdapter);
					isrefes = true;
				} else {
					for (int i = 0; i < aPPCommentList.getList().size(); i++) {
						appCommentAdapter.addAPPComment(aPPCommentList.getList().get(i));
					}
					commentList.onLoadMoreComplete(false);
				}
				if (currentPage >= pageCount) {
					commentList.setFinalPage();
					commentList.onLoadMoreComplete(false);
				} else {
					commentList.setShowPage();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (NetworkUtil.checkNetworkState(getActivity())) {
					// httpForData();
					if (!isrefes) {
						httpForReviewData();
					} else {
						--currentPage;
						httpForReviewData();
					}
				} else {
					ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
				}
			}
		});

		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForReviewData");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForReviewData");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForReviewData");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}