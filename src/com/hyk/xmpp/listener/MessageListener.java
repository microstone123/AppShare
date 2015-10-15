package com.hyk.xmpp.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.hyk.activity.R;
import com.hyk.activity.news.ChatActivity;
import com.hyk.utils.CHString;
import com.hyk.xmpp.NotiUtil;
import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppConnection;
import com.hyk.xmpp.data.DateUtil;
import com.hyk.xmpp.model.Msg;
import com.hyk.xmpp.model.OneFridenMessages;

public class MessageListener implements PacketListener {

	OneFridenMessages mOneFridenMessages;

	private Context context;
	private static final int UPDATA_NEWS = 10021;
	public static Handler handler;
	public static int onChat;
	public static NotificationManager manger;

	@SuppressWarnings("static-access")
	public MessageListener(Context context) {
		super();
		this.context = context;
		this.manger = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void processPacket(Packet packet) {
		try {
			// 在这里处理所有我们收到的消息
			Message nowMessage = (Message) packet;
			// nowMessage.get
			// nowMessage.setBody();
			String body = CHString.checkMark(nowMessage.getBody());
			String friendUser = nowMessage.getFrom();
			if (friendUser.contains("/")) {
				friendUser = friendUser.substring(0, friendUser.indexOf("/"));
			}
			// 系统消息
			if (friendUser.equals("admin@" + XmppConnection.SERVER_NAME)) {
				NotiUtil.setNotiType(XmppApplication.getsInstance().getApplicationContext(), R.drawable.user_delf, body);
			} else {
				// 获取和此好友的对话信息
				mOneFridenMessages = XmppApplication.AllFriendsMessageMapData.get(friendUser);

				if (mOneFridenMessages == null) {
					mOneFridenMessages = new OneFridenMessages();
					XmppApplication.AllFriendsMessageMapData.put(friendUser, mOneFridenMessages);
				}
				// 加入
				Msg nowMsg = new Msg(nowMessage.getFrom().substring(0, friendUser.indexOf("@")), nowMessage.getBody(),
						DateUtil.now_MM_dd_HH_mm_ss(), "IN");
				mOneFridenMessages.MessageList.add(nowMsg);
				// 保存到sql
				XmppApplication.chatDatabaseUtil.insert(nowMsg, friendUser);
				mOneFridenMessages.NewMessageCount++;
				// 保存到本地share，以免非正常关闭程序丢失新消息条数
				XmppApplication.sharedPreferences.edit()
						.putInt(friendUser + XmppApplication.user, mOneFridenMessages.NewMessageCount).commit();

				XmppApplication.sharedPreferenceNews.edit().putString(friendUser, nowMessage.getBody()).commit();

				// 发送广播
				Intent messageIntent = new Intent(XmppApplication.XMPP_UP_MESSAGE_ACTION);

				messageIntent.setData(Uri.parse("xmpp://" + friendUser.substring(0, friendUser.indexOf("@"))));

				XmppApplication.getsInstance().sendBroadcast(messageIntent);
				XmppApplication.getsInstance().sendBroadcast(new Intent("newMsg"));

				if (onChat == 1) {
					String friendName = friendUser;
					if (friendUser.contains("@")) {
						friendName = friendUser.substring(0, friendUser.indexOf("@"));
					}
					// 构建一个通知对象，指定了图标，标题，和时间
					Notification notification = new Notification(R.drawable.the_default, friendName + ":" + body,
							System.currentTimeMillis());
					// TODO 处理消息
					// 界面跳转
					Intent intent = new Intent();
					intent.setClass(context, ChatActivity.class);
					// 消息重复接收关键
					intent.setData(Uri.parse("custom://" + System.currentTimeMillis()));

					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("user_regid", friendName);

					// 触发界面跳转
					PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
					// 消息栏
					notification.setLatestEventInfo(context, "您有新的消息", body, mPendingIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
					notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
					// 消息叠加
					manger.notify(52025, notification);
				}
				android.os.Message msg = new android.os.Message();
				msg.what = UPDATA_NEWS;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void dimssNotification() {
		manger.cancel(52025);
	}

}
