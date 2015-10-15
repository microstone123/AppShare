package com.hyk.broadcast;

import com.hyk.utils.ACache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShutdownBroad extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		ACache mAcache = ACache.get(context);
		mAcache.put("XmppApplication", "destroy");
	}

}
