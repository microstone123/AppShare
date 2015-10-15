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
				.setText("APP������һ�����appӦ�÷����Ӧ�ã�����ÿ���û��ֻ���ӵ�е�appӦ�ã������ṩ���û��以�����˴�ӵ�е�appӦ��ƽ̨�����з���Ӧ����Ϣ������ӵ�����罻�����Ի��෢����Ϣ���������顣���ѻ�ӵ�й����û������ֻ�Ӧ�õĹ��ܣ�������ж�صȹ��ܷ����û�������Լ����ֻ�Ӧ�á�");
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("���ܽ���");
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
