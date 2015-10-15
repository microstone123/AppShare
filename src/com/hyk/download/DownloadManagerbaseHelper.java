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
	 * downid:����Id appurl:���ص�ַ appname��app���� totalsize:�����ܳ��� currentsize:��ǰ���س���
	 * statics������״̬ static����ʾ 0:�������� 1:�ҵ�Ӧ�� 2��ϵͳӦ��
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
