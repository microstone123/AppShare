package com.hyk.baseadapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.myapp.LocationApp;
import com.hyk.myapp.MyappDatabaseUtil;
import com.hyk.utils.ImageTools;
import com.hyk.view.RoundedImageView;
import com.hyk.view.ViewSetClick;
import com.readystatesoftware.viewbadger.BadgeView;

@SuppressLint("NewApi")
public class AppUserListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<LocationApp> appInfoList;
	private boolean isLongClik = false;
	private MyappDatabaseUtil myappDatabaseUtil;
	private ImageView imag;
	private Handler handler;
	private static final int UNINSTALL_APK = 900001;

	private List<RelativeLayout> relatList = new ArrayList<RelativeLayout>();

	private List<BadgeView> badgeList = new ArrayList<BadgeView>();
	
	private List<Bitmap> bitmapList = new ArrayList<Bitmap>();

	public AppUserListAdapter(Context context, List<LocationApp> appInfoList, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
		this.appInfoList = appInfoList;
		inflater = LayoutInflater.from(context);
		this.myappDatabaseUtil = new MyappDatabaseUtil(context);
	}

	@Override
	public int getCount() {
		return appInfoList.size();
	}

	public void setData(List<LocationApp> appInfoList) {
		if (appInfoList != null) {
			if (appInfoList.size() > 0) {
				this.appInfoList = appInfoList;
				notifyDataSetChanged();
			}
		}
	}

	public LocationApp getData(int position) {
		return appInfoList.get(position);
	}

	@Override
	public Object getItem(int position) {
		return appInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void getBadgeShow(int position) {
		badgeList.get(position).show();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.app_list_item, null);
			holder.my_logo_image = (ImageView) convertView.findViewById(R.id.my_logo_image);
			holder.my_down_num = (TextView) convertView.findViewById(R.id.my_down_num);
			holder.my_size = (TextView) convertView.findViewById(R.id.my_size);
			holder.my_name = (TextView) convertView.findViewById(R.id.my_name);
			holder.my_updata_btn = (Button) convertView.findViewById(R.id.my_updata_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final LocationApp locationApp = appInfoList.get(position);
		if (locationApp != null) {

			if (locationApp.getAppDrawable() != null) {
				holder.my_logo_image.setBackground(locationApp.getAppDrawable());
			} else if (locationApp.getApp_img() != null) {
				byte[] imagequery = locationApp.getApp_img();
				// 将字节数组转化为位图
				Bitmap imagebitmap = BitmapFactory.decodeByteArray(imagequery, 0, imagequery.length);
				Drawable dr = ImageTools.bitmapToDrawable(imagebitmap);
				holder.my_logo_image.setBackground(dr);
				bitmapList.add(imagebitmap);
			}

			holder.my_name.setText(locationApp.getName());

			try {

				if (locationApp.getAppSize() != null) {
					double size = Double.valueOf(locationApp.getAppSize());
					if (size > 1024) {
						DecimalFormat df = new DecimalFormat("#.00");
						holder.my_down_num.setText(df.format(size / 1024) + "MB");
					} else {
						holder.my_down_num.setText(String.valueOf(size) + "KB");
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			holder.my_updata_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ViewSetClick.getAlpha(arg0, context);

					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("packName", locationApp.getPackName());
					bundle.putString("appName", locationApp.getName());
					msg.setData(bundle);// bundle传值，耗时，效率低
					msg.what = UNINSTALL_APK;
					handler.sendMessage(msg);
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		public ImageView my_logo_image;
		public TextView my_down_num, my_size;
		public TextView my_name;
		public Button my_updata_btn;
	}
	
	public void recycleBitmap(){
		if(bitmapList!=null){
			for (int i = 0; i < bitmapList.size(); i++) {
				if(bitmapList.get(i)!=null){
					bitmapList.get(i).recycle();
				}
			}
		}
	}
}
