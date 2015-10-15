package com.hyk.baseadapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.app.UpdataApp;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.view.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.readystatesoftware.viewbadger.BadgeView;

@SuppressLint("NewApi")
public class AppUpdataAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<UpdataApp> appInfoList;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private static final int UPDATA_APK_ING = 900003;
	private static final int UPDATA_APK_END = 900004;
	private static final int UPDATA_APK_END_COMM = 9000041;
	private List<BadgeView> badgeList = new ArrayList<BadgeView>();
	private Handler mhandler;

	public AppUpdataAdapter(Context context, List<UpdataApp> appInfoList,Handler handler) {
		super();
		this.context = context;
		this.appInfoList = appInfoList;
		this.mhandler = handler;
		inflater = LayoutInflater.from(context);

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
	}

	@Override
	public int getCount() {
		return appInfoList.size();
	}

	public void setData(List<UpdataApp> appInfoList) {
		this.appInfoList = appInfoList;
	}
	
	public void addData(UpdataApp updataApp){
		this.appInfoList.add(updataApp);
	}

	public UpdataApp getData(int position) {
		return appInfoList.get(position);
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("HandlerLeak")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.search_list_item, null);
			holder.search_logo_image = (RoundedImageView) convertView.findViewById(R.id.search_logo_image);
			holder.search_down_num = (TextView) convertView.findViewById(R.id.search_down_num);
			holder.search_size = (TextView) convertView.findViewById(R.id.search_size);
			holder.search_name = (TextView) convertView.findViewById(R.id.search_name);

			final UpdataApp locationApp = appInfoList.get(position);
			if (locationApp != null) {
				
				imageLoader.displayImage(locationApp.getAppImage(), holder.search_logo_image, options);
				holder.search_name.setText(locationApp.getAppName());
				holder.search_size.setText(locationApp.getAppSize());
				
//				final String appName = locationApp.getAppName();
//				final Button teView = holder.item_mage_comm;
//				final RelativeLayout itemRelat_comm = holder.itemRelat_comm;
//				final Handler handler = new Handler() {
//					@SuppressLint("NewApi")
//					public void handleMessage(Message msg) {
//						switch (msg.what) {
//						case UPDATA_APK_ING:
//							teView.setVisibility(View.VISIBLE);
//							break;
//						case UPDATA_APK_END:
//							ToastUtil.show(context, appName+"");
//							teView.setVisibility(View.INVISIBLE);
//							
//							Message msgs = new Message();
//							msgs.what = UPDATA_APK_END_COMM;
//							Bundle bundle=new Bundle();  
//				            bundle.putString("appName", appName);  
//				            msgs.setData(bundle);//bundle传值，耗时，效率低 
//							mhandler.sendMessage(msgs);
//							
//							break;
//						}
//					}
//				};
//				holder.itemText_comm.setText(locationApp.getAppName());
//				imageLoader.displayImage(locationApp.getAppImage(), holder.itemImage_comm, options);
//				holder.itemRelat_comm.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						ViewSetClick.getAlpha(itemRelat_comm, context);
//						// teView.setVisibility(View.VISIBLE);
//						TheWaringDialog theWaringDialog = new TheWaringDialog(context, handler);
//						theWaringDialog.show();
//					}
//				});
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	class ViewHolder {
		public RoundedImageView search_logo_image;
		public TextView search_down_num, search_size;
		public TextView search_name;

	}
}
