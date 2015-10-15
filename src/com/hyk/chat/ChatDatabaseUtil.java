package com.hyk.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.model.Msg;
import com.hyk.xmpp.model.OneFridenMessages;

/**
 * @ClassName: MyappDatabaseUtil
 * @Description: TODO(Database工具类)
 * @author linhaishi
 * @date 2013-8-27 下午7:08:08
 * 
 */
public class ChatDatabaseUtil {
	private ChatDatabaseHelper helper;
	public static final String lock = "访问";

	public ChatDatabaseUtil(Context context) {
		helper = new ChatDatabaseHelper(context, "chatdatabase.db", null, 1);
	}

	// 添加记录
	public void insert(Msg msg, String friend) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", msg.getUsername());
		values.put("msg", msg.getMsg());
		values.put("sendTime", msg.getSendDate());
		values.put("inOrOut", msg.getInOrOut());
		values.put("friend", friend);
		values.put("whosRecord", XmppApplication.user);
		db.insert("chatRecord", "_id", values);
		db.close();

	}

	// 查询线程是否存在
	public synchronized Msg query(String username, String sendDate, String inOrOut) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db
				.rawQuery(
						"SELECT username,msg,sendDate,inOrOut FROM chatRecord WHERE username = ? and sendDate = ? and inOrOut = ?",
						new String[] { username, sendDate, inOrOut });
		Msg record = null;
		if (c.moveToNext()) {
			record = new Msg(c.getString(0));
		}
		c.close();
		db.close();
		return record;
	}

	// 取当前好友的聊天记录，限量count
	public OneFridenMessages getMsg(String friendName, String userName, int curtPage, int pageSize) {
		SQLiteDatabase db = helper.getWritableDatabase();
		OneFridenMessages oneFridenMessages = new OneFridenMessages();
		Cursor cursor = null;
		try {
			Msg msg;
			// String sql =
			// "SELECT a.username,a.msg,a.sendTime,a.inOrOut FROM(SELECT * from chatRecord"
			// + " WHERE whosRecord = ? AND friend = ? ORDER BY _id desc " +
			// ")a ORDER BY a._id LIMIT " + pageSize
			// + " OFFSET " + curtPage;

			String sql = " select * from (SELECT a.username,a.msg,a.sendTime,a.inOrOut FROM "
					+ "chatRecord a WHERE a.whosRecord = ? AND a.friend = ? ORDER BY a.sendTime desc"
					+ " LIMIT " + pageSize + " OFFSET " + curtPage
					+") b ORDER BY b.sendTime ";
			
			cursor = db.rawQuery(sql, new String[] { userName, friendName });
			while (cursor.moveToNext()) {
				msg = new Msg(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				// 设置是和谁的聊天记录
				oneFridenMessages.getMessageList().add(msg);
				msg = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			cursor.close();
			db.close();
		}
		return oneFridenMessages;
	}

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
	public synchronized void update(String username, String sendDate, String inOrOut) {
		// SQLiteDatabase db = helper.getWritableDatabase();
		// db.execSQL("UPDATE chatdata SET username = ? WHERE name = ? and statics = ? ",
		// new Object[] { openTime, name,
		// statics });
		// db.close();
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
