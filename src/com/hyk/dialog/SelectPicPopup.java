package com.hyk.dialog;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hyk.activity.R;

public class SelectPicPopup extends Activity implements OnClickListener {

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pict_dialog);
		intent = getIntent();
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

		// 添加按钮监听
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
	}

	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode != RESULT_OK) {
//			return;
//		}
//
//		switch (requestCode) {
//		case 1:
//			File temp = new File("/storage/sdcard0/hyk/camera_raw.jpg");
//			startPhotoZoom(Uri.fromFile(temp));
//			break;
//		case 2:
//			startPhotoZoom(data.getData());
//			break;
//		case 200:
//			setResult(1, data);
//			finish();
//			break;
//		}
//
//	}

	@SuppressLint("SdCardPath")
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_photo:
			try {
				// 拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
				// 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/storage/sdcard0/hyk/camera_raw.jpg")));
				startActivityForResult(intent, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.btn_pick_photo:
			try {
				// 选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
				// 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 2);
			} catch (ActivityNotFoundException e) {

			}
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		File tempFile = new File("/storage/sdcard0/hyk/camera.jpg");
		intent.putExtra("output", Uri.fromFile(tempFile));// 保存到原文件
		intent.putExtra("outputFormat", "JPEG");// 返回格式
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 200);
	}

}
