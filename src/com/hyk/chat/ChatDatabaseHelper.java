package com.hyk.chat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
	// private SQLiteDatabase database;
	public ChatDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, "chatdatabase.db", null, 1);
	}

	/**
	 * name：app名称 packName:包名 app_img：图片二进制 opentime:打开时间或统计时间 static：标示 0:正在运行
	 * 1:我的应用 2：系统应用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type = ? AND name = ?", new String[] { "table",
					"chatRecord" });
			if (!cursor.moveToNext()) {
				db.execSQL("CREATE TABLE chatRecord (_id INTEGER PRIMARY KEY AUTOINCREMENT,username VARCHAR(1024)"
						+ ",msg text,sendTime VARCHAR(1024),inOrOut VARCHAR(1024),friend text,whosRecord text)");
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
