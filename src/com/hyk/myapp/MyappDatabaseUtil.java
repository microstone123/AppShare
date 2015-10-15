package com.hyk.myapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.hyk.utils.DateUtils;
import com.hyk.utils.ImageTools;

/**
 * @ClassName: MyappDatabaseUtil
 * @Description: TODO(Database工具类)
 * @author linhaishi
 * @date 2013-8-27 下午7:08:08
 * 
 */
public class MyappDatabaseUtil {
	private MyAppbaseHelper helper;
	public static final String lock = "访问";
	private Context context;

	public MyappDatabaseUtil(Context context) {
		helper = new MyAppbaseHelper(context, "myappoperating.db", null, 1);
		this.context = context;
	}

	// 添加记录
	public void insert(String name, String packName, Drawable img, byte[] blob, String appSize, int statics) {
		LocationApp app = query(packName, statics);
		String openTime = DateUtils.getSysDate("yyyy-MM-dd HH:mm:ss");
		if (app == null) {
			if (blob == null) {
				try {
					if (img != null) {
						// 将图片转化为位图
						Bitmap bitmap1 = ImageTools.drawableToBitmap(img);
						int size = bitmap1.getWidth() * bitmap1.getHeight() * 4;
						// 创建一个字节数组输出流,流的大小为size
						ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
						// 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
						bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
						// 将字节数组输出流转化为字节数组byte[]
						blob = baos.toByteArray();
						bitmap1.recycle();
						baos.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			SQLiteDatabase db = helper.getWritableDatabase();
			db.execSQL("INSERT INTO myapp(name,packName,app_img,opentime,appSize,statics) VALUES (?,?,?,?,?,?)",
					new Object[] { name, packName, blob, openTime, appSize, statics });
			db.close();
		} else {
			update(name, openTime, statics);
		}
	}

	// 查询线程是否存在
	public synchronized LocationApp query(String packName, int statics) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT name,statics FROM myapp WHERE packName = ? and statics = ?", new String[] {
				packName, String.valueOf(statics) });
		LocationApp record = null;
		if (c.moveToNext()) {
			record = new LocationApp(c.getString(0));
		}
		c.close();
		db.close();
		return record;
	}

	// 按statics查询
	public synchronized List<LocationApp> queryForTime(int statics) {
		List<LocationApp> appList = new ArrayList<LocationApp>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT name,packName,app_img,opentime,appSize,statics FROM myapp WHERE statics = ? ORDER BY  opentime  desc",
						new String[] { String.valueOf(statics) });
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				LocationApp app = new LocationApp();
				app.setName(cursor.getString(cursor.getColumnIndex("name")));
				app.setPackName(cursor.getString(cursor.getColumnIndex("packName")));
				app.setApp_img(cursor.getBlob(cursor.getColumnIndex("app_img")));
				app.setOpenTime(cursor.getString(cursor.getColumnIndex("opentime")));
				app.setStatics(cursor.getInt(cursor.getColumnIndex("statics")));
				app.setAppSize(cursor.getString(cursor.getColumnIndex("appSize")));
				appList.add(app);
			}
		}
		return appList;
	}

	// // 按次数查询
	// public List<LocationApp> queryForNum() {
	// List<LocationApp> appList = new ArrayList<LocationApp>();
	// SQLiteDatabase db = helper.getWritableDatabase();
	// Cursor cursor =
	// db.rawQuery("SELECT name,num,opentime FROM myapp ORDER BY  num desc ",
	// null);
	// if (cursor != null && cursor.getCount() > 0) {
	// for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
	// LocationApp app = new LocationApp();
	// app.setName(cursor.getString(0));
	// app.setNum(cursor.getInt(1));
	// app.setOpenTime(cursor.getString(2));
	// appList.add(app);
	// }
	// }
	// return appList;
	// }

	// 删除记录 根据app名称
	public synchronized void delete(String name) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("DELETE FROM myapp WHERE name = ?", new Object[] { name });
		db.close();
	}

	// 删除记录 根据包名
	public synchronized void deleteByPackName(String packName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("DELETE FROM myapp WHERE packName = ?", new Object[] { packName });
		db.close();
	}

	// 更新记录
	public synchronized void update(String name, String openTime, int statics) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("UPDATE myapp SET openTime = ? WHERE name = ? and statics = ? ", new Object[] { openTime, name,
				statics });
		db.close();
	}

	// 更新时间
	public synchronized void updateTime(String name, String openTime) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("UPDATE myapp SET openTime = ? WHERE name = ? ", new Object[] { openTime, name });
		db.close();
	}

	// 清空表数据
	public synchronized void truncate() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("DELETE from myapp");
		db.close();
	}
}
