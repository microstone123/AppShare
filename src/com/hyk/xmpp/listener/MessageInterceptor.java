package com.hyk.xmpp.listener;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.data.DateUtil;
import com.hyk.xmpp.model.Msg;
import com.hyk.xmpp.model.OneFridenMessages;

public class MessageInterceptor implements PacketInterceptor {

	OneFridenMessages mOneFridenMessages;

	@Override
	public void interceptPacket(Packet packet) {
		Message nowMessage = (Message) packet;
		
		String friendUser = nowMessage.getTo();
		if (friendUser.contains("/")) {
			friendUser = friendUser.substring(0, friendUser.indexOf("/"));
		}

		mOneFridenMessages = XmppApplication.AllFriendsMessageMapData
				.get(friendUser);

		// ���Ƿ���ȥ��Message
		if (mOneFridenMessages == null) {
			// ����һ�����ѵ�OneFriendMessage
			mOneFridenMessages = new OneFridenMessages();
			XmppApplication.AllFriendsMessageMapData.put(friendUser,
					mOneFridenMessages);
		}
		// ��¼���Ƿ���ȥ����Ϣ
		Msg nowMsg = new Msg(nowMessage.getFrom().substring(0,
				friendUser.indexOf("@")), nowMessage.getBody(),
				DateUtil.now_MM_dd_HH_mm_ss(), "OUT");
		
		mOneFridenMessages.MessageList.add(nowMsg);
		XmppApplication.chatDatabaseUtil.insert(nowMsg,friendUser);

		Intent messageIntent = new Intent(
				XmppApplication.XMPP_UP_MESSAGE_ACTION);
		messageIntent.setData(Uri.parse("xmpp://"
				+ friendUser.substring(0, friendUser.indexOf("@"))));
		XmppApplication.getsInstance().sendBroadcast(messageIntent);
	}

}
