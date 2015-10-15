package com.hyk.baseadapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.activity.nearby.UserInfoActivity;
import com.hyk.dialog.DetailsDiao;
import com.hyk.user.UserList;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.ImageLoaderShow;
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
public class AppListAdapter extends BaseAdapter {
	private List<UserList> appsList;
	private LayoutInflater listContainer; // 视图容器
	private Context context;
	private int imgNum = 5;
	// protected ImageLoader imageLoader = ImageLoader.getInstance();
	// protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private ACache mACache;

	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;
	private String regId;

	public final class ListItemView { // 自定义控件集合
		public RoundImageViewExtNetw user_img;
		public TextView tv_app_distance, user_name;
		public GridView the_applist_grid;
	}

	public AppListAdapter(Context context, String regId, List<UserList> apps) {
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.appsList = apps;
		this.context = context;
		mACache = ACache.get(context);
		this.regId = regId;
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// options = ImageLoaderPartner.getOptions(R.drawable.the_def_img);

		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
	}

	public void addUserList(UserList userList) {
		appsList.add(userList);
	}

	// setData()要在MyAdapter类里设置，用于设置数据源
	public void setData(List<UserList> apps) {
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
		try {
			// 自定义视图
			ListItemView listItemView = null;

			if (convertView == null) {
				listItemView = new ListItemView();
				// 获取list_item布局文件的视图
				convertView = listContainer.inflate(R.layout.applist_item, null);
				// 获取控件对象
				listItemView.tv_app_distance = (TextView) convertView.findViewById(R.id.the_count);
				listItemView.user_img = (RoundImageViewExtNetw) convertView.findViewById(R.id.the_userimg);
				listItemView.user_name = (TextView) convertView.findViewById(R.id.the_username);
				listItemView.the_applist_grid = (GridView) convertView.findViewById(R.id.the_applist_grid);
				convertView.setTag(listItemView);
			} else {
				listItemView = (ListItemView) convertView.getTag();
			}

			final UserList appInfoList = appsList.get(position);
			final int userId = appInfoList.getRegId();
			if (userId == 0) {
				listItemView.user_name.setText(CHString.TOURIST_NAME);
			} else {
				listItemView.user_name.setText(appInfoList.getUserName());
			}

			final AppImgListAdapter gridAppListAdapter = new AppImgListAdapter(context, appInfoList.getApps());
			listItemView.the_applist_grid.setAdapter(gridAppListAdapter);
			listItemView.the_applist_grid.setSelector(R.drawable.hide_listview_yellow_selector);
			listItemView.the_applist_grid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					ViewSetClick.getAlpha(arg1, context);
					DetailsDiao detailsDiaoFragment = new DetailsDiao(context, null, gridAppListAdapter.getAppId(arg2),
							regId, String.valueOf(appInfoList.getRegId()), appInfoList.getImei());
					detailsDiaoFragment.setCanceledOnTouchOutside(true);
					detailsDiaoFragment.show();
				}
			});

			final ImageView userImg = listItemView.user_img;
			// imageLoader.displayImage(appInfoList.getHeadPic(),
			// listItemView.user_img, options);

			// if (StringUtils.isNotEmpty(appInfoList.getHeadPic())) {
			listItemView.user_img.setErrorImageResId(R.drawable.the_def_img);
			listItemView.user_img.setDefaultImageResId(R.drawable.the_def_img);

			// if (!appInfoList.getHeadPic().startsWith("http")) {
			listItemView.user_img.setImageUrl(appInfoList.getHeadPic(), XmppApplication.getsInstance().imageLoader);
			// }
			// }
			// ImageListener listener =
			// ImageLoader.getImageListener(listItemView.user_img,
			// R.drawable.the_def_img,
			// R.drawable.the_def_img);
			// // File file = new File(path);
			// imageLoaderShow.imageLoader.get(appInfoList.getHeadPic(),
			// listener);
			listItemView.user_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ViewSetClick.getAlpha(userImg, context);
					Intent intent = new Intent();
					intent.setClass(context, UserInfoActivity.class);
					intent.putExtra("userName", appInfoList.getUserName());
					intent.putExtra("relation", appInfoList.getRelation());
					intent.putExtra("sign_name", appInfoList.getSignName());
					intent.putExtra("user_headpic", appInfoList.getHeadPic());
					intent.putExtra("redId", userId + "");
					intent.putExtra("rImei", appInfoList.getImei());
					mACache.put("user_imei", appInfoList.getImei());
					mACache.put("dy_regid", userId + "");
					context.startActivity(intent);
					((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			// 设置文字和图片
			listItemView.tv_app_distance.setText(CHString.APP_LIST_JULI + appInfoList.getDistance() + "  "
					+ appInfoList.getAppCount() + CHString.APP_LIST_GEYYONG);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return convertView;
	}
}
