package com.hyk.xmpp;

public class XmppConnet {
	private static XmppConnet xmppConnet = new XmppConnet();

	private boolean isConnet;

	public static XmppConnet getXmppConnet() {
		return xmppConnet;
	}

	public static void setXmppConnet(XmppConnet xmppConnet) {
		XmppConnet.xmppConnet = xmppConnet;
	}

	public boolean isConnet() {
		return isConnet;
	}

	public void setConnet(boolean isConnet) {
		this.isConnet = isConnet;
	}

}
