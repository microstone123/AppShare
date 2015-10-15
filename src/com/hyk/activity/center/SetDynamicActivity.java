package com.hyk.activity.center;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.activity.photo.AlbumPhotoActivity;
import com.hyk.activity.photo.PhotoActivity;
import com.hyk.baseadapter.SeleGridAdapter;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.dialog.SelectPicPopupWindow;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.service.SetDynamicService;
import com.hyk.utils.ACache;
import com.hyk.utils.AppManager;
import com.hyk.utils.BimpUtils;
import com.hyk.utils.CHString;
import com.hyk.utils.DateUtils;
import com.hyk.utils.DisplayUtil;
import com.hyk.utils.FileHelper;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.PictureUtils;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.ViewSetClick;

/**
 * @ClassName: SetDynamicActivity
 * @Description: TODO(���?̬)
 * @author linhs
 * @date 2014-3-8 ����11:48:04
 */
public class SetDynamicActivity extends FragmentActivity implements OnClickListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	private static final int SUCC_DY = 1003;
	private static final int ERR_DY = 1004;
	private static final int FAILURE_DY = 1005;
	public FrameLayout framelayout;
	private ImageButton btn_back;
	private TextView txt_num;
	private EditText edit_dy;
	private GridView noScrollgridview;
	private SeleGridAdapter seleAdapter;
	private int textNum = 120;
	private SelectPicPopupWindow selectPicPopupWindow;
	private Button set_dy_btn;
	private FileHelper fileHelper;
	private File tempFile;
	private ACache mAcache;
	private static final int PHOTO_REQUEST_CAREMA = 101;
	private boolean isClick = false;
	private SharedPreferencesHelper spqq;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				break;
			case SUCC_NETWORK:
				framelayout.setVisibility(View.GONE);
				break;
			case SUCC_DY:
				finish();
				stopService();
				overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
				isClick = false;
				break;
			case ERR_DY:
				stopService();
				stopLoad();
				ToastUtil.show(SetDynamicActivity.this, CHString.ERROR_SETDY);
				isClick = false;
				break;
			case FAILURE_DY:
				ToastUtil.show(SetDynamicActivity.this, CHString.ERROR_SETDY);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_dynamic);
//		AppManager.getAppManager().addActivity(this);
		AppManager.setActivity(this);
		// btn_take_photo
		BimpUtils.handler = handler;
		spqq = new SharedPreferencesHelper(SetDynamicActivity.this, "loginInfo");
		fileHelper = new FileHelper(this);
		mAcache = ACache.get(this);
		// mAcache.remove("btn_take_photo");
		if ("start".equals(mAcache.getAsString("set_dy"))) {
			BimpUtils.clearBtm();
		}
		setupViews();

	}

	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());

		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat);
		loading_img = (ImageView) findViewById(R.id.loading_img);
		set_dy_btn = (Button) findViewById(R.id.set_dy_btn);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		set_dy_btn.setOnClickListener(this);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		edit_dy = (EditText) findViewById(R.id.edit_dy);
		txt_num = (TextView) findViewById(R.id.txt_num);
		txt_num.setText("0/" + textNum);

		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		seleAdapter = new SeleGridAdapter(this);
		noScrollgridview.setAdapter(seleAdapter);

		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (!isClick) {
					ViewSetClick.getAlpha(arg1, SetDynamicActivity.this);
					if (arg2 == BimpUtils.bmp.size()) {
						selectPicPopupWindow = new SelectPicPopupWindow(SetDynamicActivity.this, itemsOnClick);
						selectPicPopupWindow.showAtLocation(findViewById(R.id.relat_setdynamic), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0); // ����layout��PopupWindow����ʾ��λ��
					} else {
						ViewSetClick.getAlpha(arg1, SetDynamicActivity.this);
						Intent intent = new Intent(SetDynamicActivity.this, PhotoActivity.class);
						intent.putExtra("ID", arg2);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
				}
			}
		});

		edit_dy.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int MAXLINES = 12;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionStart = edit_dy.getSelectionStart();
				selectionEnd = edit_dy.getSelectionEnd();
				int lines = edit_dy.getLineCount();
				if (lines > MAXLINES) {
					ToastUtil.show(SetDynamicActivity.this, CHString.MAX_LINES);
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					edit_dy.setText(s);
					edit_dy.setSelection(tempSelection);// ���ù�������
				} else {
					if (temp.length() > textNum) {
						s.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionEnd;
						edit_dy.setText(s);
						edit_dy.setSelection(tempSelection);// ���ù�������
					}
				}
				txt_num.setText(temp.length() + "/" + textNum);
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * ���س�ʼ����Fragment�ķ���
	 */
	private void ReplaceFragmentMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_err_connet, fragment);
		tration.commitAllowingStateLoss();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isClick = false;
		seleAdapter.update();
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ViewSetClick.getAlpha(v, this);
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		case R.id.set_dy_btn:
			if (!isClick) {
				if (NetworkUtil.checkNetworkState(this)) {
					startLoad();
					String content = edit_dy.getText().toString();
					// String strings[] = new String[BimpUtils.drr.size()];
					if (BimpUtils.drr.size() != 0 || StringUtils.isNotEmpty(content)) {
						ViewSetClick.getAlpha(v, this);
						Intent intent = new Intent();
						intent.setClass(this, SetDynamicService.class);
						intent.putExtra("dy_content", content);
						intent.putExtra("dy_redId", spqq.getValue("redId"));
						startService(intent);
						isClick = true;
					} else {
						stopLoad();
						ToastUtil.show(this, CHString.NOT_CONTENT);
					}
				}
			}
			break;
		default:
			break;
		}
	}

	// Ϊ��������ʵ�ּ�����
	private OnClickListener itemsOnClick = new OnClickListener() {
		@SuppressLint("SdCardPath")
		public void onClick(View v) {
			selectPicPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				// ����
				try {
					// �������
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					String path = DateUtils.getSysDate("yyyyMMddHHmmss") + ".jpg";

					tempFile = new File(fileHelper.creatSDDir("hyk"), path);
					mAcache.put("btn_take_photo", fileHelper.creatSDDir("hyk") + "/" + path);
					// ���ļ��д���uri
					Uri uri = Uri.fromFile(tempFile);

					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					// ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_CAREMA
					startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
					mAcache.put("set_dy", "off");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.btn_pick_photo:
				// ���
				Intent intent = new Intent();
				intent.setClass(SetDynamicActivity.this, AlbumPhotoActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == PHOTO_REQUEST_CAREMA) {
			// �����ص����
			if (hasSdcard()) {
				// crop(Uri.fromFile(tempFile));\
				// �õ�ͼƬ��ȫ·��
				// Log.e("tempFile", tempFile.getParent());
				if (tempFile != null) {
					Uri mImageCaptureUri = Uri.fromFile(tempFile);
					if (mImageCaptureUri != null) {
						Bitmap image;
						try {
							// ��������Ǹ��Uri��ȡBitmapͼƬ�ľ�̬����
							image = PictureUtils.getimage(tempFile.getPath(), DisplayUtil.dip2px(this, 800),
									DisplayUtil.dip2px(this, 480));
							getPhotoBack(image);
						} catch (OutOfMemoryError e) {
							e.printStackTrace();
						}
					} else {
						Bundle extras = data.getExtras();
						if (extras != null) {
							// ��������Щ���պ��ͼƬ��ֱ�Ӵ�ŵ�Bundle�е��������ǿ��Դ��������ȡBitmapͼƬ
							Bitmap image = extras.getParcelable("data");
							getPhotoBack(image);
						}
					}
				} else {
					Bitmap image;
					String path = mAcache.getAsString("btn_take_photo");
					image = PictureUtils.getSmallBitmap(path, DisplayUtil.dip2px(this, 800),
							DisplayUtil.dip2px(this, 480));
					getPhotoBack(image);
				}

				// PictureUtils.deleteFile(tempFile);
			}
		}
	}

	private void getPhotoBack(Bitmap image) {
		if (image != null) {
			if (checkSD()) {
				String path = fileHelper.creatSDDir("hyk");
				String url = fileHelper.saveBitmap(image, path, DateUtils.getSysDate("yyyyMMddHHmmss"));
				BimpUtils.drr.add(url);
				BimpUtils.bmp.add(image);
				BimpUtils.max++;
			}
		}
	}

	private boolean checkSD() {
		boolean isSd = true;
		if (fileHelper.ExistSDCard()) {
			isSd = true;
		} else {
			ToastUtil.show(this, "SD������ʧ��");
			isSd = true;
		}
		return isSd;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
	}

	/*
	 * �ж�sdcard�Ƿ񱻹���
	 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService();
		BimpUtils.clearBtm();
	}

	private void stopService() {
		try {
			Intent intent = new Intent();
			intent.setClass(this, SetDynamicService.class);
			stopService(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
