package com.hyk.activity.photo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.hyk.activity.R;
import com.hyk.baseadapter.ImageGridAdapter;
import com.hyk.baseadapter.ImageGridAdapter.TextCallback;
import com.hyk.photo.AlbumHelper;
import com.hyk.photo.ImageItem;
import com.hyk.utils.BimpUtils;
import com.hyk.utils.ToastUtil;
import com.hyk.view.ViewSetClick;

public class ImageGridActivity extends Activity implements OnClickListener {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	public List<ImageItem> dataList;
	public GridView gridView;
	public ImageGridAdapter adapter;
	public AlbumHelper helper;
	public Button bt;
	public ImageButton btn_back;

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtil.show(ImageGridActivity.this, "最多选择6张图片");
				break;

			default:
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);
		BimpUtils.act_bool = true;
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		btn_back = (ImageButton) findViewById(R.id.btn_back);

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	/**
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt:
			ArrayList<String> list = new ArrayList<String>();
			Collection<String> c = adapter.map.values();
			Iterator<String> it = c.iterator();
			for (; it.hasNext();) {
				list.add(it.next());
			}
			for (int i = 0; i < list.size(); i++) {
				if (BimpUtils.drr.size() < 6) {
					BimpUtils.drr.add(list.get(i));
				}
			}

			if (BimpUtils.act_bool) {
				BimpUtils.act_bool = false;
			}
			finish();
			overridePendingTransition(R.anim.push_rigt_out,
					R.anim.push_right_in);
			break;
		case R.id.btn_back:
			ViewSetClick.getAlpha(v, this);
			Intent intent = new Intent(ImageGridActivity.this,
					AlbumPhotoActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_rigt_out,
					R.anim.push_right_in);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(ImageGridActivity.this,
					AlbumPhotoActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_rigt_out,
					R.anim.push_right_in);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
