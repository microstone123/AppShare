package com.hyk.xmpp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.model.Msg;
import com.hyk.xmpp.model.OneFridenMessages;

public class DbHelper {
	private static DbHelper instance = null;

	private SqlLiteHelper helper;
	private SQLiteDatabase db;
	private final int SHOW_MSG_COUNT = 15;
	private final int MORE_MSG_COUNT = 5;

	public DbHelper(Context context) {
		helper = new SqlLiteHelper(context);
		db = helper.getWritableDatabase();
	}

	public void closeDb() {
		db.close();
		helper.close();
	}

	public static DbHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbHelper(context);
		}
		return instance;
	}

	private class SqlLiteHelper extends SQLiteOpenHelper {

		private static final int DB_VERSION = 1;
		private static final String DB_NAME = "chatRecord";

		public SqlLiteHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			String sql = "CREATE TABLE  IF NOT EXISTS chatRecord"
					+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT, username text , msg text , sendTime text , inOrOut text,friend text,whosRecord text)";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			dropTable(db);
			onCreate(db);
		}

		private void dropTable(SQLiteDatabase db) {
			String sql = "DROP TABLE IF EXISTS chatRecord";
			db.execSQL(sql);
		}

	}

	public void saveChatMsg(Msg msg, String friend) {
		ContentValues values = new ContentValues();
		values.put("username", msg.getUsername());
		values.put("msg", msg.getMsg());
		values.put("sendTime", msg.getSendDate());
		values.put("inOrOut", msg.getInOrOut());
		values.put("friend", friend);
		values.put("whosRecord", XmppApplication.user);
		db.insert("chatRecord", "_id", values);
	}

	public void getChatMsg() {
		Msg msg;
		String sql = " select username,msg,sendTime,inOrOut,friend from chatRecord where whosRecord = ?";
		Cursor cursor = db.rawQuery(sql, new String[] { XmppApplication.user });
		cursor.getCount();
		while (cursor.moveToNext()) {
			msg = new Msg(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			// �����Ǻ�˭�������¼
			String friendUser = cursor.getString(4);
//			OneFridenMessages oneFridenMessages = XmppApplication.AllFriendsMessageMapData.get(friendUser);
//			if (oneFridenMessages == null) {
//				oneFridenMessages = new OneFridenMessages();
//				XmppApplication.AllFriendsMessageMapData.put(friendUser, oneFridenMessages);
//			}
//			oneFridenMessages.MessageList.add(msg);
			msg = null;
		}
		cursor.close();
	}

	// ȡ��ǰ���ѵ������¼������count
	public void getChatMsg(String friendName, String userName) {
		Cursor cursor = null;
		try {
			Msg msg;
			String sql = "select a.username,a.msg,a.sendTime,a.inOrOut from(select * from chatRecord"
					+ " where whosRecord = ? and friend = ? order by _id desc LIMIT " + SHOW_MSG_COUNT
					+ ")a order by a._id";
			// String userName = XmppApplication.user;
			// if (userName == null) {
			// userName = UserLoginInfo.getUserLoginInfo().getUserName();
			// }
			cursor = db.rawQuery(sql, new String[] { userName, friendName });
			while (cursor.moveToNext()) {
				msg = new Msg(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				// �����Ǻ�˭�������¼
				OneFridenMessages oneFridenMessages = XmppApplication.AllFriendsMessageMapData.get(friendName);
				if (oneFridenMessages == null) {
					oneFridenMessages = new OneFridenMessages();
					XmppApplication.AllFriendsMessageMapData.put(friendName, oneFridenMessages);
				}
				oneFridenMessages.MessageList.add(msg);
				msg = null;
			}
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			cursor.close();
		}
	}
	
	public OneFridenMessages getMsg(String friendName, String userName,int curtPage,int pageSize){
		OneFridenMessages oneFridenMessages = new OneFridenMessages();
		Cursor cursor = null;
		try {
			Msg msg;
			String sql = "select a.username,a.msg,a.sendTime,a.inOrOut from(select * from chatRecord"
					+ " where whosRecord = ? and friend = ? order by _id desc LIMIT " + SHOW_MSG_COUNT
					+ ")a order by a._id limit "+pageSize+" offset "+curtPage;
			cursor = db.rawQuery(sql, new String[] { userName, friendName });
			while (cursor.moveToNext()) {
				msg = new Msg(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				// �����Ǻ�˭�������¼
				oneFridenMessages.getMessageList().add(msg);
				msg = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			cursor.close();
		}
		return oneFridenMessages;
	}

	// ��ȡ������������¼,��ʾ��5��
	public void getChatMsgByFriendMore(int count, String friendName) {
		Msg msg;
		String sql = "select a.username,a.msg,a.sendTime,a.inOrOut from(select * from chatRecord"
				+ " where whosRecord = ? and friend = ? order by _id desc LIMIT " + MORE_MSG_COUNT + " offset " + count
				+ ")a order by a._id";
		Cursor cursor = db.rawQuery(sql, new String[] { XmppApplication.user, friendName });

		// �����Ǻ�˭�������¼
		// XmppApplication.AllFriendsMessageMapData.get(friendName).MessageList.clear();
		int i = 0;
		while (cursor.moveToNext()) {
			msg = new Msg(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			XmppApplication.AllFriendsMessageMapData.get(friendName).MessageList.add(i, msg);
			msg = null;
			i++;
		}
		cursor.close();
	}

}
