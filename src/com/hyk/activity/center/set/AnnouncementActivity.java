package com.hyk.activity.center.set;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.utils.AppManager;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.view.ViewSetClick;

public class AnnouncementActivity extends Activity implements OnClickListener {

	private TextView update_text, title_text;
	private ImageButton btn_back;
	private String announcementStr;
	private SharedPreferencesHelper spqq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_updata);
		AppManager.getAppManager().addActivity(this);
		spqq = new SharedPreferencesHelper(this, "loginInfo");
		announcementStr = spqq.getValue("announcementStr");
//		announcementStr = getIntent().getExtras().getString("announcementStr");
		setupViews();
	}

	public void setupViews() {
		update_text = (TextView) findViewById(R.id.update_text);
		if (StringUtils.isNotEmpty(announcementStr)) {
			update_text.setText(announcementStr);
		} else {
			update_text.setText("APP快搜产品下周有新版本更新，请亲爱的用户们做好更新的准备，同时抽奖活动已开启，good luck！");
		}
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("公告");

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ViewSetClick.getAlpha(btn_back, this);
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		default:
			break;
		}
	}
}
