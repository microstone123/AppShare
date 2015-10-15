package com.hyk.party;

import org.json.JSONObject;

import android.content.Context;

import com.hyk.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class BaseUiListener implements IUiListener {
	
	private Context context;

	public BaseUiListener(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onComplete(Object response) {
//		Util.showResultDialog(context, response.toString(), "µÇÂ¼³É¹¦");
		doComplete((JSONObject)response);
	}

	protected void doComplete(JSONObject values) {

	}

	@Override
	public void onError(UiError e) {
//		Util.toastMessage(context, "onError: " + e.errorDetail);
		Util.dismissDialog();
	}

	@Override
	public void onCancel() {
//		Util.toastMessage(context, "onCancel: ");
		Util.dismissDialog();
	}
}
