package com.hyk.baseadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.activity.nearby.UserInfoActivity;
import com.hyk.dialog.DetailsDiao;
import com.hyk.news.AttentionApp;
import com.hyk.news.AttentionInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.view.NewsGridView;
import com.hyk.view.RoundImageViewExtNetw;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;

/**
 * @ClassName: AppListAdapter
 * @Description: TODO(搜索列表ListView Adapter)
 * @author linhaishi
 * @date 2013-8-16 下午3:51:50
 * 
 */
public class FansUserAdapter extends BaseAdapter {
	@SuppressLint("UseSparseArrays")
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	private List<AttentionInfo> appsList;
	private LayoutInflater listContainer; // 视图容器
	private Context context;
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private ACache mACache;
	private int relation;
	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;
	private String regId;
	
	public FansUserAdapter(Context context, List<AttentionInfo> apps, int relation,String regId) {
		try {
			this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
			this.appsList = apps;
			this.context = context;
			this.relation = relation;
			mACache = ACache.get(context);
			this.regId=regId;
//			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//			options = ImageLoaderPartner.getOptions(R.drawable.the_def_img);
			requestQueue = Volley.newRequestQueue(context);
			imageLoaderShow = new ImageLoaderShow(requestQueue);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void addAttentionInfo(AttentionInfo userList) {
		appsList.add(userList);
	}

	// setData()要在MyAdapter类里设置，用于设置数据源
	public void setData(List<AttentionInfo> apps) {
		this.appsList = apps;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView listItemView = null;

		List<AttentionApp> imageURLList = new ArrayList<AttentionApp>();
		if (lmap.get(position) == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.attention_item, null);
			// 获取控件对象
			listItemView.the_attention_content = (TextView) convertView.findViewById(R.id.the_attention_content);
			listItemView.the_attention_img = (RoundImageViewExtNetw) convertView.findViewById(R.id.the_attention_img);
			listItemView.the_attention_name = (TextView) convertView.findViewById(R.id.the_attention_name);
			listItemView.the_attention_grid = (NewsGridView) convertView.findViewById(R.id.the_attention_grid);

			lmap.put(position, convertView);
			convertView.setTag(listItemView);
		} else {
			convertView = lmap.get(position);
			listItemView = (ListItemView) convertView.getTag();
		}

		final AttentionInfo appInfoList = appsList.get(position);
		imageURLList = appInfoList.getList();
		listItemView.the_attention_name.setText(appInfoList.getUserName());

		final AttentionGridAdapter gridAppListAdapter = new AttentionGridAdapter(context, imageURLList);
		listItemView.the_attention_grid.setAdapter(gridAppListAdapter);
		listItemView.the_attention_grid.setSelector(R.drawable.hide_listview_yellow_selector);
		listItemView.the_attention_grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ViewSetClick.getAlpha(arg1, context);
				DetailsDiao detailsDiaoFragment = new DetailsDiao(context, null, gridAppListAdapter.getAppId(arg2)
						,regId,String.valueOf(appInfoList.getRegId()),appInfoList.getImei());
				detailsDiaoFragment.setCanceledOnTouchOutside(true);
				detailsDiaoFragment.show();
			}
		});

		listItemView.the_attention_img.setErrorImageResId(R.drawable.the_def_img);
		listItemView.the_attention_img.setDefaultImageResId(R.drawable.the_def_img);
		listItemView.the_attention_img.setImageUrl(appInfoList.getHeadPic(), XmppApplication.getsInstance().imageLoader);

		listItemView.the_attention_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					ViewSetClick.getAlpha(v, context);

					switch (relation) {
					case 0:
						relation = 1;
						break;
					case 1:
						relation = 0;
						break;
					default:
						break;
					}

					Intent intent = new Intent();
					intent.setClass(context, UserInfoActivity.class);
					intent.putExtra("userName", appInfoList.getUserName());
					intent.putExtra("relation", relation);
					intent.putExtra("sign_name", appInfoList.getSignName());
					intent.putExtra("user_headpic", appInfoList.getHeadPic());
					intent.putExtra("redId", appInfoList.getRegId() + "");
					intent.putExtra("rImei", appInfoList.getImei());
					mACache.put("user_imei", appInfoList.getImei());
					mACache.put("dy_regid", appInfoList.getRegId() + "");
					context.startActivity(intent);
					((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				} catch (Exception e) {
					// TODO: handle exception
				}
				// Intent intent = new Intent();
				// intent.putExtra("user_name", appInfoList.getUserName());
				// intent.putExtra("user_img", appInfoList.getHeadPic());
				// intent.putExtra("user_regid",
				// String.valueOf(appInfoList.getRegId()));
				// intent.setClass(context, ChatActivity.class);
				// context.startActivity(intent);
				// ((Activity)
				// context).overridePendingTransition(R.anim.push_left_in,
				// R.anim.push_left_out);
			}
		});

		return convertView;
	}

	public final class ListItemView { // 自定义控件集合
		public RoundImageViewExtNetw the_attention_img;
		public TextView the_attention_content, the_attention_name;
		public NewsGridView the_attention_grid;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
