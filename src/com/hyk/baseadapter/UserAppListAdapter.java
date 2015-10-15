package com.hyk.baseadapter;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.activity.app.AppDetailsActivity;
import com.hyk.download.DownloadConstants;
import com.hyk.download.DownloadForHttp;
import com.hyk.download.DownloadHandle;
import com.hyk.download.DownloadManagerDatabaseUtil;
import com.hyk.download.DownloadManagerInfo;
import com.hyk.download.DownloadManagerPro;
import com.hyk.user.UserAppInfo;
import com.hyk.utils.CHString;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.utils.ToastUtil;
import com.hyk.view.CustomMarqueeTextView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;

@SuppressLint({ "NewApi", "UseSparseArrays" })
public class UserAppListAdapter extends BaseAdapter {
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	private Context context;
	private LayoutInflater inflater;
	private List<UserAppInfo> appInfoList;
	// protected ImageLoader imageLoader = ImageLoader.getInstance();
	// protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private int currentPosition = -1;
	private MyScrollHandler scrollHandler;
	public String myId, regId,rImei;
	private DownloadManagerPro downloadManagerPro;
	private static final int DOWN_TILE = 1010;
	private DownloadManagerDatabaseUtil downloadManagerDatabaseUtil;
	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;

	public UserAppListAdapter(Context context, List<UserAppInfo> appInfoList, MyScrollHandler scrollHandler,
			String redId, String myRegId,String rImei) {
		super();
		this.context = context;
		this.appInfoList = appInfoList;
		this.scrollHandler = scrollHandler;
		this.rImei = rImei;
		inflater = LayoutInflater.from(context);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
		downloadManagerPro = new DownloadManagerPro(context);
		downloadManagerDatabaseUtil = new DownloadManagerDatabaseUtil(context);
		this.regId = redId;
		this.myId = myRegId;
		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
	}

	@Override
	public int getCount() {
		return appInfoList.size();
	}

	public void setData(List<UserAppInfo> appInfoList) {
		this.appInfoList = appInfoList;
	}

	public void addUserAppInfo(UserAppInfo appInfoList) {
		this.appInfoList.add(appInfoList);
		notifyDataSetChanged();
	}

	public UserAppInfo getData(int position) {
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

	// @Override
	// public boolean areAllItemsEnabled() {
	// return false;
	// }
	//
	// @Override
	// public boolean isEnabled(int position) {
	// return false;
	// }

	/**
	 * 各种ItemView的模板总数
	 */
	@Override
	public int getViewTypeCount() {
		return appInfoList.size();
	}

	// /**
	// * 当前ItemView的模板类型
	// */
	// @Override
	// public int getItemViewType(int position) {
	// return position;
	// }

	@SuppressLint("HandlerLeak")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (lmap.get(position) == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.grid_user, null);
			holder.userapp_logo_image = (NetworkImageView) convertView.findViewById(R.id.userapp_logo_image);
			holder.userapp_name = (CustomMarqueeTextView) convertView.findViewById(R.id.userapp_name);
			holder.userapp_down_num = (TextView) convertView.findViewById(R.id.userapp_down_num);
			holder.userapp_size = (TextView) convertView.findViewById(R.id.userapp_size);
			holder.app_introduction = (TextView) convertView.findViewById(R.id.app_introduction);
			holder.open_introduction = (ImageView) convertView.findViewById(R.id.open_introduction);
			holder.relat_userapp = (RelativeLayout) convertView.findViewById(R.id.relat_userapp);
			holder.relat_img_text = (RelativeLayout) convertView.findViewById(R.id.relat_img_text);
			holder.relat_img = (RelativeLayout) convertView.findViewById(R.id.relat_img);
			holder.userapp_dowble = (Button) convertView.findViewById(R.id.userapp_dowble);
			holder.userapp_progressBar = (ProgressBar) convertView.findViewById(R.id.userapp_progressBar);

			lmap.put(position, convertView);
			convertView.setTag(holder);
		} else {
			convertView = lmap.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		final UserAppInfo userAppInfo = appInfoList.get(position);
		final ProgressBar progressBar = holder.userapp_progressBar;
		final Button dowblebtn = holder.userapp_dowble;
		if (userAppInfo != null) {
			holder.userapp_name.setText(userAppInfo.getAppName());
			String downnu = Double.valueOf(userAppInfo.getDownNum()) / 10000 + "";
			if (StringUtils.isNotEmpty(downnu)) {
				holder.userapp_down_num.setText(downnu + CHString.THOUSAND_STR);
			} else {
				holder.userapp_down_num.setText("0.1" + CHString.THOUSAND_STR);
			}

			holder.userapp_size.setText(String.valueOf(userAppInfo.getAppSize()));
			final String str = userAppInfo.getIntroduce().replaceAll("<br />", "");
			holder.app_introduction.setText(str.replaceAll("-&gt", ""));

			holder.userapp_logo_image.setErrorImageResId(R.drawable.the_def_img);
			holder.userapp_logo_image.setDefaultImageResId(R.drawable.the_def_img);
			holder.userapp_logo_image.setImageUrl(userAppInfo.getAppImage(), XmppApplication.getsInstance().imageLoader);

			// Log.e("userAppInfo.getAppImage()", userAppInfo.getAppImage());

			if (position == currentPosition) {
				holder.app_introduction.setVisibility(View.VISIBLE);
				holder.open_introduction.setBackgroundResource(R.drawable.up_btn);
				holder.relat_userapp.startAnimation(AnimationUtils.loadAnimation(context, R.anim.footer_appear));
				holder.relat_userapp.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ViewSetClick.getShortAlpha(v, context);
						currentPosition = -1;// 关闭
						notifyDataSetChanged();
					}
				});
				holder.relat_img_text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ViewSetClick.getShortAlpha(v, context);
						currentPosition = -1;// 关闭
						notifyDataSetChanged();
					}
				});
			} else {
				holder.app_introduction.setVisibility(View.GONE);
				holder.open_introduction.setBackgroundResource(R.drawable.down_btn);
				holder.relat_img_text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ViewSetClick.getShortAlpha(v, context);
						currentPosition = position;// 展开
						notifyDataSetChanged();

						Message msg = new Message();
						Bundle b = new Bundle();// 存放数据
						b.putInt("item", currentPosition);
						msg.setData(b);
						scrollHandler.sendMessage(msg); // 向Handler发送消息,更新UI
					}
				});
				holder.relat_userapp.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ViewSetClick.getShortAlpha(v, context);
						currentPosition = position;// 展开
						notifyDataSetChanged();

						Message msg = new Message();
						Bundle b = new Bundle();// 存放数据
						b.putInt("item", currentPosition);
						msg.setData(b);
						scrollHandler.sendMessage(msg); // 向Handler发送消息,更新UI
					}
				});
			}

			holder.userapp_logo_image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ViewSetClick.getShortAlpha(v, context);
					Intent intent = new Intent(context, AppDetailsActivity.class);
					intent.putExtra("appId", String.valueOf(userAppInfo.getAppId()));
					intent.putExtra("get_return", 0);
					intent.putExtra("appForRegId", regId);
					intent.putExtra("rImei", rImei);
					context.startActivity(intent);
					((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

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

			holder.userapp_dowble.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ViewSetClick.getShortAlpha(v, context);
					DownloadManagerInfo downloadManagerInfo = downloadManagerDatabaseUtil.queryForUrl(userAppInfo
							.getDownUrl());
					if (StringUtils.isNotEmpty(userAppInfo.getDownUrl())) {
						if (CHString.DOWN_STR.equals(dowblebtn.getText().toString())) {

							TelephonyManager telManager = (TelephonyManager) context
									.getSystemService(Context.TELEPHONY_SERVICE);
							DownloadForHttp.httpForDownapp(String.valueOf(userAppInfo.getAppId()), myId,
									regId, telManager.getDeviceId(),rImei);

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
					holder.userapp_progressBar.setMax(Integer.valueOf(downloadManagerInfo.getTotalSize()));
				}
				if (StringUtils.isNotEmpty(downloadManagerInfo.getCurrentSize())) {
					holder.userapp_progressBar.setProgress(Integer.valueOf(downloadManagerInfo.getCurrentSize()));
				}
				if (downloadManagerInfo.getStatics() == 4) {
					holder.userapp_dowble.setText(CHString.APP_CONTINUE);
				} else if (downloadManagerInfo.getStatics() == 8) {
					holder.userapp_dowble.setText(CHString.DOWN_INSTALLATION);
					holder.userapp_progressBar.setMax(100);
					holder.userapp_progressBar.setProgress(100);
				} else if (downloadManagerInfo.getStatics() == 2) {
					holder.userapp_dowble.setText(CHString.APP_PAUSE);
					XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downloadManagerInfo
							.getDownId(), diaoHandler, downloadManagerInfo.getAppUrl(), downloadManagerInfo
							.getAppName(), 0));
				}
			}
		}

		return convertView;
	}

	class ViewHolder {
		public NetworkImageView userapp_logo_image;
		public CustomMarqueeTextView userapp_name;
		public TextView userapp_down_num, userapp_size, app_introduction;
		public ImageView open_introduction;
		public Button userapp_dowble;
		public RelativeLayout relat_userapp, relat_img, relat_img_text;
		public ProgressBar userapp_progressBar;

	}
}
