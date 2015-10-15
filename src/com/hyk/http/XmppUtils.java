package com.hyk.http;

import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppConnection;
import com.hyk.xmpp.XmppService;

public class XmppUtils {

	/**
	 * �ύ�˺�������Ϣ��������
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
