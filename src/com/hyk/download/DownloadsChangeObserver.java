package com.hyk.download;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.hyk.myapp.UpdateList;
import com.hyk.user.UserAppInfo;

public class DownloadsChangeObserver extends ContentObserver {
	private DownloadManagerPro downloadManagerPro;
	private static final int DOWN_UN_TILE = 1010;
	private Context context;
	private DownloadManagerDatabaseUtil downloadManagerDatabaseUtil;
	private List<DownloadHandle> downloadList = new ArrayList<DownloadHandle>();
	private UpdateList update = new UpdateList();

	public DownloadsChangeObserver(Context context) {
		super(new Handler());
		this.context = context;
		this.downloadManagerPro = new DownloadManagerPro(context);
		this.downloadManagerDatabaseUtil = new DownloadManagerDatabaseUtil(context);
	}

	@Override
	public void onChange(boolean selfChange) {
		if (downloadList.size() > 0) {
			for (int i = 0; i < downloadList.size(); i++) {
				int downId = downloadList.get(i).getDownloadId();
				if (downId != 0) {
					int[] bytesAndStatus = downloadManagerPro.getBytesAndStatus(downId); // 0:当前长度
					Log.e("selfChange", downId + " " + bytesAndStatus[0] + " " + bytesAndStatus[1]);
					downloadManagerDatabaseUtil.insert(downId, downloadList.get(i).getDownlUrl(), downloadList.get(i)
							.getAppName(), bytesAndStatus[1], bytesAndStatus[0], bytesAndStatus[2]);
					if (downloadList.get(i).getStatics() == 0) {
						downloadList
								.get(i)
								.getHandler()
								.sendMessage(
										downloadList.get(i).getHandler()
												.obtainMessage(DOWN_UN_TILE, downId, 0, bytesAndStatus));
					}
					if (bytesAndStatus[0] == bytesAndStatus[1] && bytesAndStatus[0] != -1 && bytesAndStatus[0] != 0) {
						DownloadConstants.installationApp(context,
								downloadManagerPro.getFileUriString(downId, downloadList.get(i).getAppName()));
						downloadManagerDatabaseUtil.update(bytesAndStatus[1], bytesAndStatus[0], 8, downId);
						if (downloadList.get(i).getStatics() == 0) {
							downloadList
									.get(i)
									.getHandler()
									.sendMessage(
											downloadList.get(i).getHandler()
													.obtainMessage(DOWN_UN_TILE, downId, 0, bytesAndStatus));
							downloadList.get(i).setStatics(1);
						} else {
							removeHandler(i);
						}
					}
				}
			}
		}

	}

	public void addHandler(DownloadHandle downloadHandle) {
		for (int i = 0; i < downloadList.size(); i++) {
			if (downloadHandle.getDownlUrl().equals(downloadList.get(i).getDownlUrl())) {
				downloadList.remove(i);
				break;
			}
		}
		downloadList.add(downloadHandle);
	}

	public void removeHandler(int item) {
		downloadList.remove(item);
	}

	public void setStatics(int downId, int statics) {
		for (int i = 0; i < downloadList.size(); i++) {
			if (downId == downloadList.get(i).getDownloadId()) {
				downloadList.get(i).setStatics(statics);
				break;
			}
		}
	}

	public void removeHandlerById(int downId) {
		for (int i = 0; i < downloadList.size(); i++) {
			if (downId == downloadList.get(i).getDownloadId()) {
				downloadList.remove(i);
				break;
			}
		}
	}

	public void setStaticsByUserAppInfo(List<UserAppInfo> list, int statics) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < downloadList.size(); j++) {
				if (list.get(i).getDownUrl().equals(downloadList.get(j).getDownlUrl())) {
					downloadList.get(j).setStatics(statics);
				}
			}
		}
	}

	public DownloadHandle getDownloadHandleById(int downId) {
		DownloadHandle downloadHandle = new DownloadHandle();
		for (int i = 0; i < downloadList.size(); i++) {
			if (downloadList.get(i).getDownloadId() == downId) {
				downloadHandle = downloadList.get(i);
				break;
			}
		}
		return downloadHandle;
	}

	public DownloadHandle getDownloadHandleByUrl(String url) {
		DownloadHandle downloadHandle = new DownloadHandle();
		for (int i = 0; i < downloadList.size(); i++) {
			if (downloadList.get(i).getDownlUrl().equals(url)) {
				downloadHandle = downloadList.get(i);
				break;
			}
		}
		return downloadHandle;
	}

	public UpdateList getUpdate() {
		return update;
	}

	public void setUpdate(UpdateList update) {
		this.update = update;
	}

	public void addDownId(int downId) {
		boolean isSave = true;
		if (this.update.getList().size() > 0) {
			for (int i = 0; i < this.update.getList().size(); i++) {
				if (this.update.getList().get(i) == downId) {
					isSave = false;
				}
			}
		}
		if (isSave) {
			this.update.getList().add(downId);
		}
	}

}
