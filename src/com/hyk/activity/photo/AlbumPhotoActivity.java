package com.hyk.activity.photo;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;

import com.hyk.activity.R;
import com.hyk.photo.AlbumHelper;
import com.hyk.photo.ImageBucket;
import com.hyk.photo.ImageBucketAdapter;
import com.hyk.utils.AppManager;
import com.hyk.view.ViewSetClick;

public class AlbumPhotoActivity extends Activity implements OnClickListener{
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	private ImageButton btn_back;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_bucket);
		AppManager.getAppManager().addActivity(this);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}

	private void initData() {
		dataList = helper.getImagesBucketList(false);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	private void initView() {
		btn_back  = (ImageButton) findViewById(R.id.btn_back);
		gridView = (GridView) findViewById(R.id.gridview);
		btn_back.setOnClickListener(this);
		gridView.setSelector(R.drawable.hide_listview_yellow_selector);
		adapter = new ImageBucketAdapter(AlbumPhotoActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				finish();
				ViewSetClick.getAlpha(view, AlbumPhotoActivity.this);
				Intent intent = new Intent(AlbumPhotoActivity.this,
						ImageGridActivity.class);
				intent.putExtra(AlbumPhotoActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_rigt_out,
					R.anim.push_right_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, this);
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			overridePendingTransition(R.anim.push_rigt_out,
					R.anim.push_right_in);
			break;

		default:
			break;
		}
	}
}
