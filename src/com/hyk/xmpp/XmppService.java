package com.hyk.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.util.Log;

/**
 * xmpp方法
 */
public class XmppService {
	
	private static String paword="123456";
	
	/**
	 * 删除当前用户
	 * 
	 * @param connection
	 * @return
	 */
	public static boolean deleteAccount(XMPPConnection connection) {
		try {
			connection.getAccountManager().deleteAccount();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// /**
	// * 返回所有组信息 <RosterGroup>
	// * @return List(RosterGroup)
	// */
	// public static List<RosterGroup> getGroups(Roster roster) {
	// List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
	// Collection<RosterGroup> rosterGroup = roster.getGroups();
	// Iterator<RosterGroup> i = rosterGroup.iterator();
	// while (i.hasNext())
	// groupsList.add(i.next());
	// return groupsList;
	// }

	// /**
	// * 返回相应(groupName)组里的所有用户<RosterEntry>
	// * @return List(RosterEntry)
	// */
	// public static List<RosterEntry> getEntriesByGroup(Roster roster,
	// String groupName) {
	// List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
	// RosterGroup rosterGroup = roster.getGroup(groupName);
	// Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
	// Iterator<RosterEntry> i = rosterEntry.iterator();
	// while (i.hasNext())
	// EntriesList.add(i.next());
	// return EntriesList;
	// }

	/**
	 * 返回所有用户信息 <RosterEntry>
	 * 
	 * @return List(RosterEntry)
	 */
	public static List<RosterEntry> getAllEntries(Roster roster) {
		List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntry = roster.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext())
			EntriesList.add(i.next());
		return EntriesList;
	}

	//
	// /**
	// * 创建一个组
	// */
	// public static boolean addGroup(Roster roster,String groupName)
	// {
	// try {
	// roster.createGroup(groupName);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	//
	// /**
	// * 删除一个组
	// */
	// public static boolean removeGroup(Roster roster,String groupName)
	// {
	// return false;
	// }
	//
	/**
	 * 添加一个好友 无分组
	 */
	public static boolean addUser(Roster roster, String userName, String name) {
		try {
			roster.createEntry(userName, name, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// /**
	// * 添加一个好友到分组
	// * @param roster
	// * @param userName
	// * @param name
	// * @return
	// */
	// public static boolean addUsers(Roster roster,String userName,String
	// name,String groupName)
	// {
	// try {
	// roster.createEntry(userName, name,new String[]{ groupName});
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	//
	// }

	/**
	 * 删除一个好友
	 * 
	 * @param roster
	 * @param userJid
	 * @return
	 */
	public static boolean removeUser(Roster roster, String userJid) {
		try {
			RosterEntry entry = roster.getEntry(userJid);
			roster.removeEntry(entry);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//
	// /**
	// * 把一个好友添加到一个组中
	// * @param userJid
	// * @param groupName
	// */
	// public static void addUserToGroup(final String userJid, final String
	// groupName,
	// final XMPPConnection connection) {
	// RosterGroup group = connection.getRoster().getGroup(groupName);
	// // 这个组已经存在就添加到这个组，不存在创建一个组
	// RosterEntry entry = connection.getRoster().getEntry(userJid);
	// try {
	// if (group != null) {
	// if (entry != null)
	// group.addEntry(entry);
	// } else {
	// RosterGroup newGroup = connection.getRoster().createGroup("我的好友");
	// if (entry != null)
	// newGroup.addEntry(entry);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// /**
	// * 把一个好友从组中删除
	// * @param userJid
	// * @param groupName
	// */
	// public static void removeUserFromGroup(final String userJid,final String
	// groupName, final XMPPConnection connection) {
	// RosterGroup group = connection.getRoster().getGroup(groupName);
	// if (group != null) {
	// try {
	// RosterEntry entry = connection.getRoster().getEntry(userJid);
	// if (entry != null)
	// group.removeEntry(entry);
	// } catch (XMPPException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * 修改心情
	 * 
	 * @param connection
	 * @param status
	 */
	public static void changeStateMessage(final XMPPConnection connection, final String status) {
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus(status);
		connection.sendPacket(presence);
	}

	public static IQ regist(String accounts, String password, String email, String nicheng) {
		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(XmppConnection.getConnection().getServiceName());
		reg.setUsername(accounts);
		reg.setPassword(password);
		reg.addAttribute("name", nicheng);
		reg.addAttribute("email", email);

		PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = XmppConnection.getConnection().createPacketCollector(filter);
		XmppConnection.getConnection().sendPacket(reg);
		IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		// Stop queuing results
		collector.cancel();// 停止请求results（是否成功的结果）

		return result;
	}

	public static synchronized boolean login(String accounts) {
		boolean isLogin = false;
		if (!XmppConnection.getConnection().isAuthenticated()) {
			try {
				// XmppConnection.getConnection().is
				XmppConnection.getConnection().login(accounts, paword);
				Presence presence = new Presence(Presence.Type.available); // 设置为登录状态
				XmppConnection.getConnection().sendPacket(presence);
				// XmppConnection.getConnection().isAuthenticated();

				XmppApplication.user = accounts;
				isLogin = true;
			} catch (XMPPException e) {
				isLogin = false;
			}
		}else{
			isLogin = true;
			Log.e("isLogin 222", isLogin+"");
		}
		return isLogin;
	}

	public static String getUsername(String fullUsername) {
		return fullUsername.split("@")[0];
	}

	public static String getFullUsername(String username) {
		return username + "@" + XmppConnection.SERVER_NAME;
	}

}
