package com.hyk.http;

import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppConnection;
import com.hyk.xmpp.XmppService;

public class XmppUtils {

	/**
	 * 提交账号密码信息到服务器
	 */
	public static void submit(final String name) {

		new Thread() {
			@Override
			public void run() {
				if (!XmppConnection.getConnection().isAuthenticated()) {
					if (XmppService.login(name)) {
						XmppApplication.user = name;
					}
				}
			}
		}.start();
	}

}
