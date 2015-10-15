package com.hyk.activity.center.set;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.utils.AppManager;
import com.hyk.view.ViewSetClick;

public class FeaturesActivity extends Activity  implements OnClickListener{

	private TextView update_text,title_text;
	private ImageButton btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_updata);
		AppManager.getAppManager().addActivity(this);
		setupViews();
	}

	public void setupViews() {
		update_text = (TextView) findViewById(R.id.update_text);
		update_text
				.setText("APP快搜是一款关于app应用分享的应用，鉴于每个用户手机所拥有的app应用，快搜提供让用户间互相分享彼此拥有的app应用平台。因有分享应用信息，快搜拥有弱社交，可以互相发送信息，分享心情。快搜还拥有管理用户本身手机应用的功能，更新与卸载等功能方便用户管理好自己的手机应用。");
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("功能介绍");
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
