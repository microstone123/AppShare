package com.hyk.download;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadManagerbaseHelper extends SQLiteOpenHelper {
	// private SQLiteDatabase database;
	public DownloadManagerbaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, "downappdatabase.db", null, 1);
	}

	/**
	 * downid:下载Id appurl:下载地址 appname：app名称 totalsize:下载总长度 currentsize:当前下载长度
	 * statics：下载状态 static：标示 0:正在运行 1:我的应用 2：系统应用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type = ? AND name = ?", new String[] { "table",
					"downappdata" });
			if (!cursor.moveToNext()) {
				db.execSQL("CREATE TABLE downappdata (downid INTEGER PRIMARY KEY AUTOINCREMENT,appurl VARCHAR(1024),"
						+ "appname VARCHAR(1024),totalsize VARCHAR(1024),currentsize VARCHAR(1024), statics INTEGER,downtime VARCHAR(1024))");
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
