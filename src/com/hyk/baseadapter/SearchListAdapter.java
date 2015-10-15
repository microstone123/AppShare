package com.hyk.baseadapter;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.download.DownloadConstants;
import com.hyk.download.DownloadHandle;
import com.hyk.download.DownloadManagerDatabaseUtil;
import com.hyk.download.DownloadManagerInfo;
import com.hyk.download.DownloadManagerPro;
import com.hyk.user.UserAppInfo;
import com.hyk.utils.CHString;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.utils.ToastUtil;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @ClassName: AppListAdapter
 * @Description: TODO(搜索列表ListView Adapter)
 * @author linhaishi
 * @date 2013-8-16 下午3:51:50
 * 
 */
public class SearchListAdapter extends BaseAdapter {
	private List<UserAppInfo> appsList;
	private LayoutInflater listContainer; // 视图容器
	private Context context;
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	
	private DownloadManagerPro downloadManagerPro;
	private static final int DOWN_TILE = 1010;
	private DownloadManagerDatabaseUtil downloadManagerDatabaseUtil;
	
	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;

	public final class ListItemView { // 自定义控件集合
		public NetworkImageView search_logo_image;
		public TextView search_down_num, search_size;
		public TextView search_name;
		public ProgressBar search_progressBar;
		public Button search_dowble;
	}

	public SearchListAdapter(Context context, List<UserAppInfo> apps) {
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.appsList = apps;
		this.context = context;
		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
//		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//		options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
		
		downloadManagerPro = new DownloadManagerPro(context);
		downloadManagerDatabaseUtil = new DownloadManagerDatabaseUtil(context);
	}

	public void addUserAppInfo(UserAppInfo userAppInfo) {
		appsList.add(userAppInfo);
	}

	// setData()要在MyAdapter类里设置，用于设置数据源
	public void setData(List<UserAppInfo> apps) {
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
	
	public String getAppId(int position) {
		// TODO Auto-generated method stub
		return String.valueOf(appsList.get(position).getAppId());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.search_list_item, null);
			listItemView.search_logo_image = (NetworkImageView) convertView.findViewById(R.id.search_logo_image);
			listItemView.search_down_num = (TextView) convertView.findViewById(R.id.search_down_num);
			listItemView.search_size = (TextView) convertView.findViewById(R.id.search_size);
			listItemView.search_name = (TextView) convertView.findViewById(R.id.search_name);
			listItemView.search_progressBar = (ProgressBar) convertView.findViewById(R.id.search_progressBar);
			listItemView.search_dowble= (Button) convertView.findViewById(R.id.search_dowble);
			
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

//		imageLoader.displayImage(appsList.get(position).getAppImage(), listItemView.search_logo_image, options);
		listItemView.search_logo_image.setErrorImageResId(R.drawable.transparent_d);
		listItemView.search_logo_image.setDefaultImageResId(R.drawable.transparent_d);
		listItemView.search_logo_image.setImageUrl(appsList.get(position).getAppImage(), XmppApplication.getsInstance().imageLoader);
		
		listItemView.search_name.setText(appsList.get(position).getAppName());
		listItemView.search_down_num.setText((Double.valueOf(appsList.get(position).getDownNum())/10000.00)+ CHString.THOUSAND_STR);
		listItemView.search_size.setText(appsList.get(position).getAppSize());
		
		final UserAppInfo userAppInfo = appsList.get(position);
		final ProgressBar progressBar = listItemView.search_progressBar;
		final Button dowblebtn = listItemView.search_dowble;
		
		
		
		
		final Handler diaoHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_TILE:
					DownloadHandle downloadHandle = XmppApplication.downloadsChangeObserver
							.getDownloadHandleById(msg.arg1);
					if (StringUtils.isNotEmpty(downloadHandle.getAppName())) {
						if (downloadHandle.getDownlUrl().equals(userAppInfo.getDownUrl())) {
							int[] data = (int[]) msg.obj;
							int statics = data[2];
							int totalSize = data[1];
							int currentSize = data[0];
							Log.e("下载", msg.arg1 + " " + data[2] + "  " + data[1] + "  " + data[0]);
							progressBar.setMax(data[1]);
							progressBar.setProgress(data[0]);

							if (statics == DownloadConstants.STATUS_FAILED) {
								statics = downloadManagerPro.getErrorCode(msg.arg1);
							}

							if (data[1] == data[0] && data[0] != -1 && data[0] != 0) {
								dowblebtn.setText(CHString.DOWN_INSTALLATION);
								downloadManagerDatabaseUtil.update(totalSize, currentSize, 8, msg.arg1);
								XmppApplication.downloadsChangeObserver.removeHandlerById(msg.arg1);
							}

							switch (statics) {
							case DownloadConstants.ERROR_CANNOT_RESUME:
								ToastUtil.show(context, CHString.STATUS_FAILED);
								dowblebtn.setText(CHString.DOWN_RUMM);
								break;
							case DownloadConstants.STATUS_SUCCESSFUL:
								// diao_dowble.setText(CHString.DOWN_INSTALLATION);
								// downloadManagerDatabaseUtil.update(totalSize,
								// currentSize, 8, downId);
								// DownloadConstants.installationApp(context,
								// downloadManagerPro.getFileUriString(downId,
								// appName));
								break;
							case DownloadConstants.STATUS_PAUSED:
								dowblebtn.setText(CHString.APP_CONTINUE);
								break;
							case DownloadConstants.ERROR_INSUFFICIENT_SPACE:
								ToastUtil.show(context, CHString.ERROR_INSUFFICIENT_SPACE);
								dowblebtn.setText(CHString.DOWN_RUMM);
								break;
							case DownloadConstants.ERROR_DEVICE_NOT_FOUND:
								ToastUtil.show(context, CHString.NOT_SDKA);
								dowblebtn.setText(CHString.DOWN_RUMM);
								break;
							case DownloadConstants.ERROR_FILE_ALREADY_EXISTS:
								progressBar.setMax(100);
								progressBar.setProgress(100);
								dowblebtn.setText(CHString.DOWN_INSTALLATION);
								downloadManagerDatabaseUtil.update(totalSize, currentSize, 8, msg.arg1);
								DownloadConstants.installationApp(context,
										downloadManagerPro.getFileUriString(msg.arg1, userAppInfo.getAppName()));
								ToastUtil.show(context, CHString.ERROR_FILE_ALREADY_EXISTS);
								break;
							case DownloadConstants.ERROR_URL:
								ToastUtil.show(context, CHString.ERROR_URL);
								dowblebtn.setText(CHString.STATUS_FAILED);
								break;
							default:
								break;
							}
						}
					}
					break;
				}
			}
		};

		listItemView.search_dowble.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewSetClick.getAlpha(v, context);
				DownloadManagerInfo downloadManagerInfo = downloadManagerDatabaseUtil.queryForUrl(userAppInfo
						.getDownUrl());
				if (StringUtils.isNotEmpty(userAppInfo.getDownUrl())) {
					if (CHString.DOWN_STR.equals(dowblebtn.getText().toString())) {
						int downId = downloadManagerPro.startDown(userAppInfo.getDownUrl(), diaoHandler, DOWN_TILE,
								userAppInfo.getAppName());
						dowblebtn.setText(CHString.APP_PAUSE);
						XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, diaoHandler,
								userAppInfo.getDownUrl(), userAppInfo.getAppName(), 0));
						// downloadHandle =
						// XmppApplication.downloadsChangeObserver
						// .getDownloadHandleByUrl(userAppInfo.getDownUrl());
					} else if (CHString.APP_PAUSE.equals(dowblebtn.getText().toString())) {
						downloadManagerPro.pauseDownload(0, 0, downloadManagerInfo.getDownId());
						XmppApplication.downloadsChangeObserver.removeHandlerById(downloadManagerInfo.getDownId());
						dowblebtn.setText(CHString.APP_CONTINUE);
					} else if (CHString.APP_CONTINUE.equals(dowblebtn.getText().toString())) {
						downloadManagerPro.resumeDownload(diaoHandler, downloadManagerInfo.getDownId(), DOWN_TILE);
						XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downloadManagerInfo
								.getDownId(), diaoHandler, userAppInfo.getDownUrl(), userAppInfo.getAppName(), 0));
						dowblebtn.setText(CHString.APP_PAUSE);
					} else if (CHString.DOWN_INSTALLATION.equals(dowblebtn.getText().toString())) {
						Uri fileUri = downloadManagerPro.getFileUriString(downloadManagerInfo.getDownId(),
								userAppInfo.getAppName());
						if (fileUri != null) {
							DownloadConstants.installationApp(context, fileUri);
						} else {
							int downId = downloadManagerPro.startDown(userAppInfo.getDownUrl(), diaoHandler,
									DOWN_TILE, userAppInfo.getAppName());
							XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId,
									diaoHandler, userAppInfo.getDownUrl(), userAppInfo.getAppName(), 0));
							dowblebtn.setText(CHString.APP_PAUSE);
						}
					} else if (CHString.DOWN_RUMM.equals(dowblebtn.getText().toString())) {
						if (downloadManagerInfo.getDownId() > 0) {
							downloadManagerPro.restartDownload(diaoHandler, DOWN_TILE,
									downloadManagerInfo.getDownId());
						} else {
							int downId = downloadManagerPro.startDown(userAppInfo.getDownUrl(), diaoHandler,
									DOWN_TILE, userAppInfo.getAppName());
							XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId,
									diaoHandler, userAppInfo.getDownUrl(), userAppInfo.getAppName(), 0));
						}
						dowblebtn.setText(CHString.APP_PAUSE);
					}
				} else {
					ToastUtil.show(context, CHString.ERROR_SEARCH);
				}
			}
		});

		final DownloadManagerInfo downloadManagerInfo = downloadManagerDatabaseUtil.queryForUrl(userAppInfo
				.getDownUrl());
		if (downloadManagerInfo != null) {
			if (StringUtils.isNotEmpty(downloadManagerInfo.getTotalSize())) {
				listItemView.search_progressBar.setMax(Integer.valueOf(downloadManagerInfo.getTotalSize()));
			}
			if (StringUtils.isNotEmpty(downloadManagerInfo.getCurrentSize())) {
				listItemView.search_progressBar.setProgress(Integer.valueOf(downloadManagerInfo.getCurrentSize()));
			}
			if (downloadManagerInfo.getStatics() == 4) {
				listItemView.search_dowble.setText(CHString.APP_CONTINUE);
			} else if (downloadManagerInfo.getStatics() == 8) {
				listItemView.search_dowble.setText(CHString.DOWN_INSTALLATION);
				listItemView.search_progressBar.setMax(100);
				listItemView.search_progressBar.setProgress(100);
			} else if (downloadManagerInfo.getStatics() == 2) {
				listItemView.search_dowble.setText(CHString.APP_PAUSE);
				XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downloadManagerInfo
						.getDownId(), diaoHandler, downloadManagerInfo.getAppUrl(), downloadManagerInfo
						.getAppName(), 0));
			}
		}
		
		
		
		
		
		return convertView;
	}
}
