package com.hyk.baseadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
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
import com.hyk.app.UpdataApp;
import com.hyk.download.DownloadConstants;
import com.hyk.download.DownloadHandle;
import com.hyk.download.DownloadManagerDatabaseUtil;
import com.hyk.download.DownloadManagerInfo;
import com.hyk.download.DownloadManagerPro;
import com.hyk.utils.CHString;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.utils.ToastUtil;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;
import com.readystatesoftware.viewbadger.BadgeView;

@SuppressLint("NewApi")
public class AppManagementAdapter extends BaseAdapter {
	private HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	private Context context;
	private LayoutInflater inflater;
	private List<UpdataApp> appInfoList;
	// protected ImageLoader imageLoader = ImageLoader.getInstance();
	// protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private List<BadgeView> badgeList = new ArrayList<BadgeView>();
	private Handler mhandler;
	private int isUpdata = 0;
	private DownloadManagerPro downloadManagerPro;
	private static final int DOWN_TILE = 1010;
	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;
	private DownloadManagerDatabaseUtil downloadManagerDatabaseUtil;

	public AppManagementAdapter(Context context, List<UpdataApp> appInfoList, Handler handler) {
		super();
		this.context = context;
		this.appInfoList = appInfoList;
		this.mhandler = handler;
		inflater = LayoutInflater.from(context);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);

		downloadManagerPro = new DownloadManagerPro(context);
		downloadManagerDatabaseUtil = new DownloadManagerDatabaseUtil(context);

		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
	}

	@Override
	public int getCount() {
		return appInfoList.size();
	}

	public void setData(List<UpdataApp> appInfoList) {
		this.appInfoList = appInfoList;
	}

	public void addData(UpdataApp updataApp) {
		this.appInfoList.add(updataApp);
	}

	public UpdataApp getData(int position) {
		return appInfoList.get(position);
	}

	public void setUpdata(int statics) {
		isUpdata = statics;
		notifyDataSetChanged();
	}

	public int getUpdata() {
		return isUpdata;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 各种ItemView的模板总数
	 */
	@Override
	public int getViewTypeCount() {
		return appInfoList.size();
	}

	@SuppressLint("HandlerLeak")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (lmap.get(position) == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.app_management_item, null);
			holder.manage_logo_image = (NetworkImageView) convertView.findViewById(R.id.manage_logo_image);
			holder.manage_down_num = (TextView) convertView.findViewById(R.id.manage_down_num);
			holder.manage_size = (TextView) convertView.findViewById(R.id.manage_size);
			holder.manage_name = (TextView) convertView.findViewById(R.id.manage_name);
			holder.manage_updata_btn = (Button) convertView.findViewById(R.id.manage_updata_btn);
			holder.usermanage_progressBar = (ProgressBar) convertView.findViewById(R.id.usermanage_progressBar);

			lmap.put(position, convertView);
			convertView.setTag(holder);
		} else {
			convertView = lmap.get(position);
			holder = (ViewHolder) convertView.getTag();
		}

		final UpdataApp locationApp = appInfoList.get(position);
		if (locationApp != null) {

			// imageLoader.displayImage(locationApp.getAppImage(),
			// holder.manage_logo_image, options);

			holder.manage_logo_image.setErrorImageResId(R.drawable.transparent_d);
			holder.manage_logo_image.setDefaultImageResId(R.drawable.transparent_d);
			holder.manage_logo_image.setImageUrl(locationApp.getAppImage(), XmppApplication.getsInstance().imageLoader);

			holder.manage_name.setText(locationApp.getAppName());
			holder.manage_down_num.setText(locationApp.getAppSize());

			final ProgressBar progressBar = holder.usermanage_progressBar;
			final Button dowblebtn = holder.manage_updata_btn;

			final Handler diaoHandler = new Handler() {
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case DOWN_TILE:
						DownloadHandle downloadHandle = XmppApplication.downloadsChangeObserver
								.getDownloadHandleById(msg.arg1);
						if (StringUtils.isNotEmpty(downloadHandle.getAppName())) {
							if (downloadHandle.getDownlUrl().equals(locationApp.getAppUrl())) {
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
											downloadManagerPro.getFileUriString(msg.arg1, locationApp.getAppName()));
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

			holder.manage_updata_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ViewSetClick.getAlpha(v, context);
					DownloadManagerInfo downloadManagerInfo = downloadManagerDatabaseUtil.queryForUrl(locationApp
							.getAppUrl());
					if (StringUtils.isNotEmpty(locationApp.getAppUrl())) {
						if (CHString.APP_UPDATA.equals(dowblebtn.getText().toString())) {
							int downId = downloadManagerPro.startDown(locationApp.getAppUrl(), diaoHandler, DOWN_TILE,
									locationApp.getAppName());
							dowblebtn.setText(CHString.APP_PAUSE);
							XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, diaoHandler,
									locationApp.getAppUrl(), locationApp.getAppName(), 0));
						} else if (CHString.APP_PAUSE.equals(dowblebtn.getText().toString())) {
							downloadManagerPro.pauseDownload(0, 0, downloadManagerInfo.getDownId());
							XmppApplication.downloadsChangeObserver.removeHandlerById(downloadManagerInfo.getDownId());
							dowblebtn.setText(CHString.APP_CONTINUE);
						} else if (CHString.APP_CONTINUE.equals(dowblebtn.getText().toString())) {
							downloadManagerPro.resumeDownload(diaoHandler, downloadManagerInfo.getDownId(), DOWN_TILE);
							XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downloadManagerInfo
									.getDownId(), diaoHandler, locationApp.getAppUrl(), locationApp.getAppName(), 0));
							dowblebtn.setText(CHString.APP_PAUSE);
						} else if (CHString.DOWN_INSTALLATION.equals(dowblebtn.getText().toString())) {
							Uri fileUri = downloadManagerPro.getFileUriString(downloadManagerInfo.getDownId(),
									locationApp.getAppName());
							if (fileUri != null) {
								DownloadConstants.installationApp(context, fileUri);
							} else {
								int downId = downloadManagerPro.startDown(locationApp.getAppUrl(), diaoHandler,
										DOWN_TILE, locationApp.getAppName());
								XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId,
										diaoHandler, locationApp.getAppUrl(), locationApp.getAppName(), 0));
								dowblebtn.setText(CHString.APP_PAUSE);
							}
						} else if (CHString.DOWN_RUMM.equals(dowblebtn.getText().toString())) {
							if (downloadManagerInfo.getDownId() > 0) {
								downloadManagerPro.restartDownload(diaoHandler, DOWN_TILE,
										downloadManagerInfo.getDownId());
							} else {
								int downId = downloadManagerPro.startDown(locationApp.getAppUrl(), diaoHandler,
										DOWN_TILE, locationApp.getAppName());
								XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId,
										diaoHandler, locationApp.getAppUrl(), locationApp.getAppName(), 0));
							}
							dowblebtn.setText(CHString.APP_PAUSE);
						}
					} else {
						ToastUtil.show(context, CHString.ERROR_SEARCH);
					}
				}
			});

			final DownloadManagerInfo downloadManagerInfo = downloadManagerDatabaseUtil.queryForUrl(locationApp
					.getAppUrl());
			if (isUpdata != 0) {
				XmppApplication.downloadsChangeObserver.addDownId(downloadManagerInfo.getDownId());
			}

			if (downloadManagerInfo != null) {
				if (StringUtils.isNotEmpty(downloadManagerInfo.getTotalSize())) {
					holder.usermanage_progressBar.setMax(Integer.valueOf(downloadManagerInfo.getTotalSize()));
				}
				if (StringUtils.isNotEmpty(downloadManagerInfo.getCurrentSize())) {
					holder.usermanage_progressBar.setProgress(Integer.valueOf(downloadManagerInfo.getCurrentSize()));
				}
				if (downloadManagerInfo.getStatics() == 4) {
					if (isUpdata == 3) {
						downloadManagerPro.resumeDownload(diaoHandler, downloadManagerInfo.getDownId(), DOWN_TILE);
						XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downloadManagerInfo
								.getDownId(), diaoHandler, locationApp.getAppUrl(), locationApp.getAppName(), 0));
						holder.manage_updata_btn.setText(CHString.APP_PAUSE);
					} else {
						holder.manage_updata_btn.setText(CHString.APP_CONTINUE);
					}
				} else if (downloadManagerInfo.getStatics() == 8) {
					holder.manage_updata_btn.setText(CHString.DOWN_INSTALLATION);
					holder.usermanage_progressBar.setMax(100);
					holder.usermanage_progressBar.setProgress(100);
				} else if (downloadManagerInfo.getStatics() == 2) {
					if (isUpdata == 2) {
						downloadManagerPro.pauseDownload(0, 0, downloadManagerInfo.getDownId());
						XmppApplication.downloadsChangeObserver.removeHandlerById(downloadManagerInfo.getDownId());
						holder.manage_updata_btn.setText(CHString.APP_CONTINUE);
					} else {
						holder.manage_updata_btn.setText(CHString.APP_PAUSE);
						XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downloadManagerInfo
								.getDownId(), diaoHandler, downloadManagerInfo.getAppUrl(), downloadManagerInfo
								.getAppName(), 0));
					}

				} else if (isUpdata == 1) {
					holder.manage_updata_btn.setText(CHString.APP_PAUSE);
					int downId = downloadManagerPro.startDown(locationApp.getAppUrl(), diaoHandler, DOWN_TILE,
							locationApp.getAppName());
					dowblebtn.setText(CHString.APP_PAUSE);
					XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, diaoHandler,
							locationApp.getAppUrl(), locationApp.getAppName(), 0));
				}
			}

		}
		return convertView;
	}

	class ViewHolder {
		public NetworkImageView manage_logo_image;
		public TextView manage_down_num, manage_size;
		public TextView manage_name;
		public Button manage_updata_btn;
		public ProgressBar usermanage_progressBar;
	}
}
