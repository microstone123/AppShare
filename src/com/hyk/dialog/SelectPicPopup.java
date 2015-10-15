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

		// ��Ӱ�ť����
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
	}

	// ʵ��onTouchEvent���������������Ļʱ���ٱ�Activity
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
				// ����������ActionΪMediaStore.ACTION_IMAGE_CAPTURE��
				// ��Щ��ʹ��������Action���ҷ�������Щ�����л�����⣬��������ѡ�����
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
				// ѡ����Ƭ��ʱ��Ҳһ����������ActionΪIntent.ACTION_GET_CONTENT��
				// ��Щ��ʹ��������Action���ҷ�������Щ�����л�����⣬��������ѡ�����
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
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		File tempFile = new File("/storage/sdcard0/hyk/camera.jpg");
		intent.putExtra("output", Uri.fromFile(tempFile));// ���浽ԭ�ļ�
		intent.putExtra("outputFormat", "JPEG");// ���ظ�ʽ
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 200);
	}

}
