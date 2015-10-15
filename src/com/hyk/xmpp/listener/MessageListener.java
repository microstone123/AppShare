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
			// �����ﴦ�����������յ�����Ϣ
			Message nowMessage = (Message) packet;
			// nowMessage.get
			// nowMessage.setBody();
			String body = CHString.checkMark(nowMessage.getBody());
			String friendUser = nowMessage.getFrom();
			if (friendUser.contains("/")) {
				friendUser = friendUser.substring(0, friendUser.indexOf("/"));
			}
			// ϵͳ��Ϣ
			if (friendUser.equals("admin@" + XmppConnection.SERVER_NAME)) {
				NotiUtil.setNotiType(XmppApplication.getsInstance().getApplicationContext(), R.drawable.user_delf, body);
			} else {
				// ��ȡ�ʹ˺��ѵĶԻ���Ϣ
				mOneFridenMessages = XmppApplication.AllFriendsMessageMapData.get(friendUser);

				if (mOneFridenMessages == null) {
					mOneFridenMessages = new OneFridenMessages();
					XmppApplication.AllFriendsMessageMapData.put(friendUser, mOneFridenMessages);
				}
				// ����
				Msg nowMsg = new Msg(nowMessage.getFrom().substring(0, friendUser.indexOf("@")), nowMessage.getBody(),
						DateUtil.now_MM_dd_HH_mm_ss(), "IN");
				mOneFridenMessages.MessageList.add(nowMsg);
				// ���浽sql
				XmppApplication.chatDatabaseUtil.insert(nowMsg, friendUser);
				mOneFridenMessages.NewMessageCount++;
				// ���浽����share������������رճ���ʧ����Ϣ����
				XmppApplication.sharedPreferences.edit()
						.putInt(friendUser + XmppApplication.user, mOneFridenMessages.NewMessageCount).commit();

				XmppApplication.sharedPreferenceNews.edit().putString(friendUser, nowMessage.getBody()).commit();

				// ���͹㲥
				Intent messageIntent = new Intent(XmppApplication.XMPP_UP_MESSAGE_ACTION);

				messageIntent.setData(Uri.parse("xmpp://" + friendUser.substring(0, friendUser.indexOf("@"))));

				XmppApplication.getsInstance().sendBroadcast(messageIntent);
				XmppApplication.getsInstance().sendBroadcast(new Intent("newMsg"));

				if (onChat == 1) {
					String friendName = friendUser;
					if (friendUser.contains("@")) {
						friendName = friendUser.substring(0, friendUser.indexOf("@"));
					}
					// ����һ��֪ͨ����ָ����ͼ�꣬���⣬��ʱ��
					Notification notification = new Notification(R.drawable.the_default, friendName + ":" + body,
							System.currentTimeMillis());
					// TODO ������Ϣ
					// ������ת
					Intent intent = new Intent();
					intent.setClass(context, ChatActivity.class);
					// ��Ϣ�ظ����չؼ�
					intent.setData(Uri.parse("custom://" + System.currentTimeMillis()));

					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("user_regid", friendName);

					// ����������ת
					PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
					// ��Ϣ��
					notification.setLatestEventInfo(context, "�����µ���Ϣ", body, mPendingIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;// ������Զ���ʧ
					notification.defaults = Notification.DEFAULT_SOUND;// ����Ĭ��
					// ��Ϣ����
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
