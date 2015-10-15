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
	 * name��app���� packName:���� app_img��ͼƬ������ opentime:��ʱ���ͳ��ʱ�� static����ʾ 0:��������
	 * 1:�ҵ�Ӧ�� 2��ϵͳӦ��
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
