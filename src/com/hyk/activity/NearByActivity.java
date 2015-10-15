package com.hyk.activity;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hyk.activity.nearby.AppListActivity;
import com.hyk.activity.nearby.Circleview;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.utils.AppManager;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.utils.ImageTools;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.PictureUtils;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @ClassName: NearByActivity
 * @Description: TODO(身边界面)
 * @author linhs
 * @date 2014-2-18 下午3:41:43
 */
public class NearByActivity extends BaseActivity implements OnClickListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;

	private final static int GET_DATA = 1003;
	private final static int GET_POINT = 1004;
	public Circleview iv_scan;
	public ImageButton search_btn_img;
	public RoundImageView the_radar_btn;
	private ImageView the_radar_d;
	public boolean isStartRadar = false;
	public boolean isGetData = false;
	private boolean isGo = false;
	private SharedPreferencesHelper spqq;
	private Drawable dra;
	private String myurl;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				stopRadar();
				break;
			case SUCC_NETWORK:
				framelayout.setVisibility(View.GONE);
				break;
			case GET_DATA:
				isGetData = true;
				break;
			case GET_POINT:
				if (!isGo) {
					Intent intent = new Intent();
					intent.setClass(NearByActivity.this, AppListActivity.class);
					AnimationUtil.setLayout(R.anim.push_left_in, R.anim.push_left_out);
					startActivity(intent);
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nearby);
		AppManager.getAppManager().addActivity(this);
		setupViews();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = ImageLoaderPartner.getOptions(R.drawable.the_def_img);
	}

	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());

		search_btn_img = (ImageButton) findViewById(R.id.search_btn_img);
		iv_scan = (Circleview) findViewById(R.id.iv_scan);
		the_radar_btn = (RoundImageView) findViewById(R.id.the_radar_btn);
		the_radar_d = (ImageView) findViewById(R.id.the_radar_d);
		
		iv_scan.setRadioHandler(handler);
		iv_scan.setOnClickListener(this);
		search_btn_img.setOnClickListener(this);

	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		isGo = false;
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		spqq = new SharedPreferencesHelper(this, "loginInfo");

		String redId = spqq.getValue("redId");

		myurl = spqq.getValue(redId + "_logo");
		if (!myurl.startsWith("http")) {
			if (hasSdcard()) {
				if (StringUtils.isNotEmpty(myurl)) {
					if (StringUtils.isNotEmpty(redId)) {
						File file = new File(spqq.getValue(redId + "_logo"));
						if (file.exists()) {
							Bitmap bm = BitmapFactory.decodeFile(spqq.getValue(redId + "_logo"));
							bm = PictureUtils.comp(bm, 100, 100);
							dra = ImageTools.bitmapToDrawable(bm);
							the_radar_btn.setImageDrawable(dra);
						}
					}
				} else {
					the_radar_btn.setImageResource(R.drawable.the_default);
				}
			} else {
				the_radar_btn.setImageResource(R.drawable.the_default);
			}
		} else {
			imageLoader.displayImage(myurl, the_radar_btn, options);
		}
	}

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_err_connet, fragment);
		tration.commitAllowingStateLoss();
	}

	public void stopRadar() {
		// isGetData = false;
		isStartRadar = false;
		iv_scan.stopRotate();
		the_radar_d.setVisibility(View.GONE);
		// the_radar_btn.setBackgroundResource(R.drawable.off_down);
	}

	public void startRadar() {
		isStartRadar = true;
		iv_scan.startRotate();
		the_radar_d.setVisibility(View.VISIBLE);
		// the_radar_btn.setBackgroundResource(R.drawable.off_top);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_scan:
			if (isGetData) {
				isGo = true;
				Intent intent = new Intent();
				intent.setClass(NearByActivity.this, AppListActivity.class);
				AnimationUtil.setLayout(R.anim.push_left_in, R.anim.push_left_out);
				startActivity(intent);
			}
			break;
		case R.id.search_btn_img:
			Intent intent1 = new Intent();
			intent1.setClass(NearByActivity.this, TheSearchActivity.class);
			AnimationUtil.setLayout(R.anim.push_left_in, R.anim.push_left_out);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	/*
	 * 判断sdcard是否被挂载
	 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
