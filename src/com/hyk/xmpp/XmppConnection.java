package com.hyk.xmpp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.content.Context;
import android.util.Log;

import com.hyk.utils.CHString;
import com.hyk.utils.SharedPreferencesHelper;

/**
 * xmpp配置页面
 */
public class XmppConnection {
	public static int SERVER_PORT = 5222;// 服务端口 可以在openfire上设置
	public static String SERVER_HOST = "14.17.99.197";// 你openfire服务器所在的ip
	// public static String SERVER_HOST = "192.168.0.222";// 你openfire服务器所在的ip
	public static String SERVER_NAME = "14.17.99.197";// 设置openfire时的服务器名
	// public static String SERVER_NAME = "pc2013072911dkr";// 设置openfire时的服务器名
	public static String SAVE_PATH = "sdcard/together/images";// 设置保存图片文件的路径
	private static XMPPConnection connection = null;
	private static Context context;
	private static SharedPreferencesHelper sp;

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		XmppConnection.context = context;
	}

	// 断线重连,需要加载
	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openConnection() {
		if (connection == null) {
			try {
				sp = new SharedPreferencesHelper(context, "xmppconnection");

				XMPPConnection.DEBUG_ENABLED = true;// 开启DEBUG模式
				// 配置连接
				ConnectionConfiguration config = new ConnectionConfiguration(SERVER_HOST, SERVER_PORT, SERVER_NAME);
				// 设置我们连接的使用的安全类型
				config.setSendPresence(true);
				config.setReconnectionAllowed(true);
				config.setSecurityMode(SecurityMode.required);
				config.setSASLAuthenticationEnabled(false);
				config.setCompressionEnabled(false);
				connection = new XMPPConnection(config);

				connection.disconnect();
				connection.connect();// 连接到服务器
				// 配置各种Provider，如果不配置，则会无法解析数据
				configureConnection(ProviderManager.getInstance());
				sp.putStrValue("connection", "connent");

				SharedPreferencesHelper spqq = new SharedPreferencesHelper(context, "loginInfo");
				if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
					if (!connection.isAuthenticated()) {
						XmppService.login(spqq.getValue("redId"));
					}
				}
				// getHisMessage();
			} catch (Exception xe) {
				// sp.putStrValue("connection", "error");
				connection = null;
				xe.printStackTrace();
			}
		}
	}

	/**
	 * 创建连接
	 */
	public static XMPPConnection getConnection() {
//		try {
//			if (connection == null) {
//				openConnection();
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		return connection;
	}
	
	/**
	 * 判断connection是否为空
	 */
	public static boolean havaConnection() {
		if (connection == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 关闭连接
	 */
	public static void closeConnection() {
		if (connection != null) {
			connection.disconnect();
		}
		connection = null;
	}

	public static void setConnection() {
		connection = null;
	}

	/*
	 * 是否连接
	 */
	public static boolean isConnected() {
		if (connection == null) {
//			new Thread() {
//				@Override
//				public void run() {
//					super.run();
//					try {
//						XmppApplication.setConnten(context);
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//				}
//			}.start();
			Log.e("connection", "connection == null");
			return false;
		} else {
			return connection.isConnected();
		}
	}

	/**
	 * xmpp配置
	 */
	private static void configureConnection(ProviderManager pm) {
		try {
			// Private Data Storage
			pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());
			// Time
			try {
				pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Roster Exchange
			pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());
			// Message Events
			pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());
			// Chat State
			pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates",
					new ChatStateExtension.Provider());
			pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates",
					new ChatStateExtension.Provider());
			pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates",
					new ChatStateExtension.Provider());
			pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates",
					new ChatStateExtension.Provider());
			pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
			// XHTML
			pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());
			// Group Chat Invitations
			pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());
			// Service Discovery # Items //解析房间列表
			pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
			// Service Discovery # Info //某一个房间的信息
			pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());
			// Data Forms
			pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
			// MUC User
			pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());
			// MUC Admin
			pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
			// MUC Owner
			pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
			// Delayed Delivery
			pm.addExtensionProvider("x", "jabber:x:delay", new DelayInformationProvider());
			// Version
			try {
				pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
			} catch (ClassNotFoundException e) {
				// Not sure what's happening here.
			}
			// VCard
			pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
			// Offline Message Requests
			pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());
			// Offline Message Indicator
			pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());
			// Last Activity
			pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
			// User Search
			pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
			// SharedGroupsInfo
			pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup",
					new SharedGroupsInfo.Provider());
			// JEP-33: Extended Stanza Addressing
			pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());
			pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());
			pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
			pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
			pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
			pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands",
					new AdHocCommandDataProvider.MalformedActionError());
			pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands",
					new AdHocCommandDataProvider.BadLocaleError());
			pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands",
					new AdHocCommandDataProvider.BadPayloadError());
			pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands",
					new AdHocCommandDataProvider.BadSessionIDError());
			pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands",
					new AdHocCommandDataProvider.SessionExpiredError());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// /**
	// * 判断XMPP服务器连接
	// */
	// public static void checkXMPP(final Context context) {
	// SharedPreferencesHelper sp = new SharedPreferencesHelper(context,
	// "xmppconnection");
	// String xmc = sp.getValue("connection");
	// if ("error".equals(xmc)) {
	// // ToastUtil.show(context, "error.equals(xmc)");
	// new Thread() {
	// @Override
	// public void run() {
	// XmppConnection.setConnection();
	// // 连接Xmpp服务器
	// XmppApplication.setConnten(context);
	// }
	// }.start();
	// } else {
	// if (!XmppConnection.havaConnection()) {
	// // ToastUtil.show(context, "!XmppConnection.havaConnection()");
	// new Thread() {
	// @Override
	// public void run() {
	// XmppConnection.setConnection();
	// // 连接Xmpp服务器
	// XmppApplication.setConnten(context);
	// }
	// }.start();
	// }
	// }
	// }

	/**
	 * 获取离线消息
	 * 
	 * @return
	 */
	public static Map<String, List<HashMap<String, String>>> getHisMessage() {
		if (getConnection() == null)
			return null;
		Map<String, List<HashMap<String, String>>> offlineMsgs = null;

		try {
			OfflineMessageManager offlineManager = new OfflineMessageManager(getConnection());
			Iterator<Message> it = offlineManager.getMessages();

			int count = offlineManager.getMessageCount();
			if (count <= 0)
				return null;
			offlineMsgs = new HashMap<String, List<HashMap<String, String>>>();

			while (it.hasNext()) {
				Message message = it.next();
				Log.e("message.getFrom()", message.getBody());
				// String fromUser = StringUtils.parseName(message.getFrom());
				// HashMap<String, String> histrory = new HashMap<String,
				// String>();
				// histrory.put("useraccount",
				// StringUtils.parseName(getConnection().getUser()));
				// histrory.put("friendaccount", fromUser);
				// histrory.put("info", message.getBody());
				// histrory.put("type", "left");
				// if (offlineMsgs.containsKey(fromUser)) {
				// offlineMsgs.get(fromUser).add(histrory);
				// } else {
				// List<HashMap<String, String>> temp = new
				// ArrayList<HashMap<String, String>>();
				// temp.add(histrory);
				// offlineMsgs.put(fromUser, temp);
				// }
			}
			offlineManager.deleteMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return offlineMsgs;
	}

}
