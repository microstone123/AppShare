package com.hyk.xmpp.model;

import java.util.ArrayList;
import java.util.List;


public class OneFridenMessages {

	//����ĳ�����ѵĵ�ǰ�Ự��Ϣ�������������յ�����
	public List<Msg> MessageList = new ArrayList<Msg>();
	
	//������ѵĻ�����Ϣ
//	public RosterEntry FriendEntry;
	
	//������ѷ���������Ϣ(δ��)����
	public int NewMessageCount = 0;

	public List<Msg> getMessageList() {
		return MessageList;
	}

	public void setMessageList(List<Msg> messageList) {
		MessageList = messageList;
	}

	public int getNewMessageCount() {
		return NewMessageCount;
	}

	public void setNewMessageCount(int newMessageCount) {
		NewMessageCount = newMessageCount;
	}
	
	
	
	
}
