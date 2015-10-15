package com.hyk.activity.center.set;

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
import com.hyk.view.ViewSetClick;

public class CheckUpdataActivity extends Activity implements OnClickListener{

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
		update_text.setText("当前版本（@2014 APPkuaisou inc.版本V1.1）");
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("检查更新");
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
