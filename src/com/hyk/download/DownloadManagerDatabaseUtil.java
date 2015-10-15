package com.hyk.download;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @ClassName: MyappDatabaseUtil
 * @Description: TODO(Database工具�?
 * @author linhaishi
 * @date 2013-8-27 下午7:08:08
 * 
 */
public class DownloadManagerDatabaseUtil {
	private DownloadManagerbaseHelper helper;
	public static final String lock = "访问";
	private Context context;

	public DownloadManagerDatabaseUtil(Context context) {
		helper = new DownloadManagerbaseHelper(context, "downappdatabase.db", null, 1);
		this.context = context;
	}

	// 添加记录
	public void insert(int downId, String appUrl, String appName, int totalSize, int currentSize, int statics) {
		DownloadManagerInfo app = query(downId, appUrl, appName);
		String downtime = DateUtils.getSysDate("yyyy-MM-dd HH:mm:ss");
		if (app == null) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.execSQL(
					"INSERT INTO downappdata(downid,appurl,appname,totalsize,currentsize,statics,downtime) VALUES (?,?,?,?,?,?,?)",
					new Object[] { downId, appUrl, appName, totalSize, currentSize, statics, downtime });
			db.close();
		} else {
			update(totalSize, currentSize, statics, app.getDownId());
		}
	}

	// 查询线程是否存在
	public DownloadManagerInfo query(int downId, String appUrl, String appName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = null;
		if (StringUtils.isNotEmpty(appName)) {
			cursor = db
					.rawQuery(
							"SELECT downid,appurl,appname,totalsize,currentsize,statics,downtime FROM downappdata WHERE downid = ? and appUrl = ? and appName = ?",
							new String[] { String.valueOf(downId), appUrl, appName });
		} else {
			cursor = db
					.rawQuery(
							"SELECT downid,appurl,appname,totalsize,currentsize,statics,downtime FROM downappdata WHERE downid = ? and appUrl = ? ",
							new String[] { String.valueOf(downId), appUrl });
		}

		DownloadManagerInfo app = new DownloadManagerInfo();
		if (cursor.moveToNext()) {
			// String name = cursor.getString(cursor.getColumnIndex("appname"));
			app.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
			app.setDownId(cursor.getInt(cursor.getColumnIndex("downid")));
			app.setAppUrl(cursor.getString(cursor.getColumnIndex("appurl")));
			app.setCurrentSize(cursor.getString(cursor.getColumnIndex("currentsize")));
			app.setTotalSize(cursor.getString(cursor.getColumnIndex("totalsize")));
			app.setStatics(cursor.getInt(cursor.getColumnIndex("statics")));
			app.setDownTime(cursor.getString(cursor.getColumnIndex("downtime")));
		} else {
			app = null;
		}
		cursor.close();
		db.close();
		return app;
	}

	// 查询线程是否存在
	public DownloadManagerInfo queryForUrl(String appUrl) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT downid,appurl,appname,totalsize,currentsize,statics,downtime FROM downappdata WHERE appUrl = ?",
						new String[] { appUrl });
		DownloadManagerInfo app = new DownloadManagerInfo();
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				app.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
				app.setDownId(cursor.getInt(cursor.getColumnIndex("downid")));
				app.setAppUrl(cursor.getString(cursor.getColumnIndex("appurl")));
				app.setCurrentSize(cursor.getString(cursor.getColumnIndex("currentsize")));
				app.setTotalSize(cursor.getString(cursor.getColumnIndex("totalsize")));
				app.setStatics(cursor.getInt(cursor.getColumnIndex("statics")));
				app.setDownTime(cursor.getString(cursor.getColumnIndex("downtime")));
			}
		}
		cursor.close();
		db.close();
		return app;
	}

	// 按statics查询
	public synchronized List<DownloadManagerInfo> queryForTime(int statics) {
		List<DownloadManagerInfo> appList = new ArrayList<DownloadManagerInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT downid,appurl,appname,totalsize,currentsize,statics FROM downappdata WHERE statics = ? ORDER BY  opentime  desc",
						new String[] { String.valueOf(statics) });
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				DownloadManagerInfo app = new DownloadManagerInfo();
				app.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
				app.setDownId(cursor.getInt(cursor.getColumnIndex("downid")));
				app.setAppUrl(cursor.getString(cursor.getColumnIndex("appUrl")));
				app.setCurrentSize(cursor.getString(cursor.getColumnIndex("currentsize")));
				app.setTotalSize(cursor.getString(cursor.getColumnIndex("totalsize")));
				app.setStatics(cursor.getInt(cursor.getColumnIndex("statics")));
				app.setDownTime(cursor.getString(cursor.getColumnIndex("downtime")));
				appList.add(app);
			}
		}
		return appList;
	}

	// 更新记录
	public synchronized void update(int totalSize, int currentSize, int statics, int downid) {
		
		String downtime = DateUtils.getSysDate("yyyy-MM-dd HH:mm:ss");
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("UPDATE downappdata SET totalSize = ?, currentSize = ? ,statics = ?,downtime=? WHERE downid = ? ",
				new Object[] { totalSize, currentSize, statics, downtime, downid });
		db.close();
	}
	
	// 更新记录
	public synchronized void update(int statics, int downid) {
		
		String downtime = DateUtils.getSysDate("yyyy-MM-dd HH:mm:ss");
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("UPDATE downappdata SET statics = ?,downtime=? WHERE downid = ? ",
				new Object[] {statics, downtime, downid });
		db.close();
	}

	// 清空表数�?
	public synchronized void truncate() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("DELETE from downappdata");
		db.close();
	}
}
