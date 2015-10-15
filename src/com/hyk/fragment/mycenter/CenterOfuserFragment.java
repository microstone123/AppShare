package com.hyk.fragment.mycenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.hyk.activity.AnimationUtil;
import com.hyk.activity.MyCenterActivity;
import com.hyk.activity.R;
import com.hyk.activity.TabMenuActivity;
import com.hyk.activity.center.SetUpActivity;
import com.hyk.activity.center.TheDynamicActivity;
import com.hyk.dialog.SelectPicPopupWindow;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.LoginResult;
import com.hyk.resultforjson.ResultString;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.resultforjson.ResultStringForUserInfo;
import com.hyk.user.UserData;
import com.hyk.user.UserInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.AsyncHttpUtil;
import com.hyk.utils.CHString;
import com.hyk.utils.DateUtils;
import com.hyk.utils.DisplayUtil;
import com.hyk.utils.ExpressionUtil;
import com.hyk.utils.FileHelper;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.utils.ImageTools;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.PictureUtils;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.RoundImageView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppConnection;
import com.hyk.xmpp.XmppService;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tencent.tauth.Tencent;

/**
 * @ClassName: AppCommentFragment
 * @Description: TODO(应用管理)
 * @author linhs
 * @date 2014-2-26 下午2:59:21
 */
@SuppressLint("NewApi")
public class CenterOfuserFragment extends Fragment implements OnClickListener, OnEditorActionListener {

//	private AsyncImageLoader asyncImageLoader;
	private EditText sign_name_edit;
	private Tencent mTencent;
	private TextView user_name;
	private Button canlce_btn;
	private RelativeLayout relat_02, relat_03, the_man_img;
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private RoundImageView user_img;
	private SharedPreferencesHelper spqq;
	private SelectPicPopupWindow selectPicPopupWindow;
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private static final int PHOTO_REQUEST_CAREMA = 101;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 102;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 103;// 结果
	private File tempFile;
	private FileHelper fileHelper;
	private Drawable dra;
	private UserData userData;
	private LoginResult loginResult;
//	private boolean isSelePhoto = false;
	private ACache mAcache;
	private String myurl;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private ImageLoaderShow imageLoaderShow;
//	private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	// private int

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
		return inflater.inflate(R.layout.center_user, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mAcache = ACache.get(getActivity());
//		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = ImageLoaderPartner.getOptions(R.drawable.the_default);
//		requestQueue = Volley.newRequestQueue(getActivity());
//		imageLoaderShow = new ImageLoaderShow(requestQueue);
		mTencent = ((MyCenterActivity) getActivity()).mTencent;
		setupViews();
		startLoad();
		fileHelper = new FileHelper(getActivity());
		spqq = new SharedPreferencesHelper(getActivity(), "loginInfo");

		if (NetworkUtil.checkNetworkState(getActivity())) {
			if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
				getInfo();
			} else {
				if(StringUtils.isNotEmpty(spqq.getValue("userImg"))){
					ImageListener listener = ImageLoader.getImageListener(user_img, R.drawable.the_default, R.drawable.the_default);
//					File file = new File(path);
					XmppApplication.getsInstance().imageLoader.get(spqq.getValue("userImg"), listener);
				}
				sendHttp();
			}
		}
		
		String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
		try {
			SpannableString spannableString = ExpressionUtil.getExpressionString(getActivity(),
					spqq.getValue("sign_name"), zhengze);
			sign_name_edit.setText(spannableString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user_name.setText(spqq.getValue("user_name"));

		String redId = spqq.getValue("redId");

		try {
			myurl = spqq.getValue(redId + "_logo");
			if (!myurl.startsWith("http")) {
				if (hasSdcard()) {
					if (StringUtils.isNotEmpty(myurl)) {
						File file = new File(spqq.getValue(redId + "_logo"));
						if (file.exists()) {
							Bitmap bm = BitmapFactory.decodeFile(spqq.getValue(redId + "_logo"));
							bm = PictureUtils.comp(bm, 95, 95);
							dra = ImageTools.bitmapToDrawable(bm);
						}
					} else {
						dra = getResources().getDrawable(R.drawable.the_default);
					}
				} else {
					dra = getResources().getDrawable(R.drawable.the_default);
				}
				user_img.setImageDrawable(dra);
			} else {
				ImageListener listener = ImageLoader.getImageListener(user_img, R.drawable.the_default, R.drawable.the_default);
				XmppApplication.getsInstance().imageLoader.get(myurl, listener);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}

	public void setupViews() {
		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.loading_relat);
		loading_img = (ImageView) getActivity().findViewById(R.id.loading_img);
		the_man_img = (RelativeLayout) getActivity().findViewById(R.id.the_man_img);
		canlce_btn = (Button) getActivity().findViewById(R.id.canlce_btn);
		sign_name_edit = (EditText) getActivity().findViewById(R.id.sign_name_edit);
		relat_02 = (RelativeLayout) getActivity().findViewById(R.id.dy_relat);
		relat_03 = (RelativeLayout) getActivity().findViewById(R.id.about_relat);
		user_name = (TextView) getActivity().findViewById(R.id.user_name);
		user_img = (RoundImageView) getActivity().findViewById(R.id.user_img);
		relat_02.setOnClickListener(this);
		relat_03.setOnClickListener(this);
		canlce_btn.setOnClickListener(this);
		the_man_img.setOnClickListener(this);
		sign_name_edit.setOnEditorActionListener(this);
		sign_name_edit.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int textNum = 50;
			private int MAXLINES = 2;

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
				selectionStart = sign_name_edit.getSelectionStart();
				selectionEnd = sign_name_edit.getSelectionEnd();
				int lines = sign_name_edit.getLineCount();
				if (lines > MAXLINES) {
					ToastUtil.show(getActivity(), CHString.TEXT_LINES);
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					// sign_name_edit.setText(s);
					// sign_name_edit.setSelection(tempSelection);// 设置光标在最后

					String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
					try {
						SpannableString spannableString = ExpressionUtil.getExpressionString(getActivity(),
								s.toString(), zhengze);
						sign_name_edit.setText(spannableString);
						sign_name_edit.setSelection(tempSelection);// 设置光标在最后
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (temp.length() > textNum) {
						s.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionEnd;
						String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
						try {
							SpannableString spannableString = ExpressionUtil.getExpressionString(getActivity(),
									s.toString(), zhengze);
							sign_name_edit.setText(spannableString);
							sign_name_edit.setSelection(tempSelection);// 设置光标在最后
						} catch (Exception e) {
							e.printStackTrace();
						}
						ToastUtil.show(getActivity(), CHString.TEXT_MAXNUM);
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, getActivity());
		switch (v.getId()) {
		case R.id.dy_relat:
			mAcache.put("dy_regid", spqq.getValue("redId"));
			setIntent(relat_02, TheDynamicActivity.class, true);
			break;

		case R.id.about_relat:
			setIntent(relat_03, SetUpActivity.class, false);
			break;
		case R.id.the_man_img:
			if (NetworkUtil.checkNetworkState(getActivity())) {
				selectPicPopupWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
				selectPicPopupWindow.showAtLocation(getActivity().findViewById(R.id.the_center_rel), Gravity.CENTER, 0,
						0); // 设置layout在PopupWindow中显示的位置
			} else {
				ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
			}
			break;
		case R.id.canlce_btn:
			try {
				if ("0".equals(spqq.getValue("logintype"))) {
					mTencent.logout(getActivity());
				}
				spqq.removeKey("loginInfo");
				((MyCenterActivity) getActivity()).loginGoOut();
			} catch (Exception e) {
				Log.i("mTencent", e.getMessage());
				spqq.removeKey("loginInfo");
				((MyCenterActivity) getActivity()).loginGoOut();
			}
			break;
		default:
			break;
		}
	}

	private void setIntent(View view, Class<?> cls, boolean isData) {
		ViewSetClick.getAlpha(view, getActivity());
		Intent intent = new Intent();

		if (isData) {
			intent.putExtra("redId", spqq.getValue("redId"));
			intent.putExtra("userName", spqq.getValue("uname"));
			intent.putExtra("sign_name", spqq.getValue("sign_name"));
			intent.putExtra("focushim", spqq.getValue("focushim"));
			intent.putExtra("focusme", spqq.getValue("focusme"));
		}

		intent.setClass(getActivity(), cls);
		AnimationUtil.setLayout(R.anim.push_left_in, R.anim.push_left_out);
		startActivity(intent);
	}

	/**
	 * 提交服务器
	 */
	private void sendHttp() {
		if (NetworkUtil.checkNetworkState(getActivity())) {
			// String redId = spqq.getValue("redId");
			// if (StringUtils.isEmpty(redId)) {
			getFromLogin();
			// }
		} else {

		}
	}

	/**
	 * 查询用户信息
	 */
	private void getInfo() {
		if (NetworkUtil.checkNetworkState(getActivity())) {
			getFromUserInfo();
		} else {

		}
	}

	private void getFromUserInfo() {
		String redId = spqq.getValue("redId");
		if (StringUtils.isNotEmpty(redId)) {
			String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_INFO;
			Map<String, String> params = new HashMap<String, String>();
			params.put("regId", redId);
			String httpUrl = HttpUtils.checkUrl(url, params);
			jsonObjectRequest = new StringRequest(httpUrl,  new Response.Listener<String>() {  
	            @Override  
	            public void onResponse(String response) {  
	            	stopLoad();
					ResultStringForUserInfo resultString = new ResultStringForUserInfo();
					resultString = binder.fromJson(response, ResultStringForUserInfo.class);
					if (resultString != null) {
						userData = resultString.getObj();
						user_name.setText(userData.getUserName());

						String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
						try {
							SpannableString spannableString = ExpressionUtil.getExpressionString(getActivity(),
									userData.getSignName(), zhengze);
							sign_name_edit.setText(spannableString);
						} catch (Exception e) {
							e.printStackTrace();
						}
						spqq.putStrValue("user_name", userData.getUserName());
						spqq.putStrValue("sign_name", userData.getSignName());
						spqq.putStrValue("redId", userData.getRegId());
						spqq.putStrValue("focusme", userData.getFocusMe());
						spqq.putStrValue("focushim", userData.getFocusHim());

						ImageListener listener = new ImageListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								if (R.drawable.the_default != 0) {
									user_img.setImageResource(R.drawable.the_default);
								}
							}
							@Override
							public void onResponse(ImageContainer response, boolean isImmediate) {
								if (response.getBitmap() != null) {
									user_img.setImageBitmap(response.getBitmap());
									String path = fileHelper.creatSDDir("hyk");
									String photo_name = spqq.getValue("redId") + "_logo";
									String url = fileHelper.saveBitmap(response.getBitmap(), path, photo_name);
									spqq.putStrValue(spqq.getValue("redId") + "_logo", url);
									
								} else if (R.drawable.the_default != 0) {
									user_img.setImageResource(R.drawable.the_default);
								}
							}
						};
						XmppApplication.getsInstance().imageLoader.get(userData.getHeadPic(), listener);
						
						if (StringUtils.isNotEmpty(userData.getRegId())) {
							if (XmppConnection.isConnected()) {
								if (!XmppConnection.getConnection().isAuthenticated()) {
									XmppService.login(spqq.getValue("redId"));
								}
							}
						}
					}
	            }  
	        }, new Response.ErrorListener() {  
	            @Override  
	            public void onErrorResponse(VolleyError error) {  
	                Log.e("TAG", error.getMessage(), error);  
	            }  
	        }); 
			XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest,"httpFromUserInfo");
		}
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			ViewSetClick.getAlpha(v, getActivity());
			selectPicPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				if (NetworkUtil.checkNetworkState(getActivity())) {
					// 判断存储卡是否可以用，可用进行存储
					if (hasSdcard()) {
						// 激活相机
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						String path = DateUtils.getSysDate("yyyyMMddHHmmss") + ".jpg";

						tempFile = new File(fileHelper.creatSDDir("hyk"), path);
						mAcache.put("btn_take_ticphoto", path);
						// 从文件中创建uri
						Uri uri = Uri.fromFile(tempFile);

						intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
						// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
						startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
					} else {
						ToastUtil.show(getActivity(), CHString.NOT_SDKA);
					}
				} else {
					ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
				}
				break;
			case R.id.btn_pick_photo:
				if (NetworkUtil.checkNetworkState(getActivity())) {
					// 激活系统图库，选择一张图片
					Intent intent1 = new Intent(Intent.ACTION_PICK);
					intent1.setType("image/*");
					// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
					startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
				} else {
					ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
				}
				break;
			default:
				break;
			}
		}
	};

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

	@SuppressLint({ "NewApi", "ShowToast" })
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			// 从相册返回的数据
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				if (uri != null) {

					String[] proj = { MediaStore.Images.Media.DATA };
					@SuppressWarnings("deprecation")
					Cursor actualimagecursor = getActivity().managedQuery(uri, proj, null, null, null);
					int actual_image_column_index = actualimagecursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();
					String img_path = actualimagecursor.getString(actual_image_column_index);

					saveTic(img_path, false);
				}
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// 从相机返回的数据
			if (hasSdcard()) {
				// Log.e(tag, msg)
				if (tempFile == null) {
					tempFile = new File(fileHelper.creatSDDir("hyk"), mAcache.getAsString("btn_take_ticphoto"));
					// crop(Uri.fromFile(tempFile));
					saveTic(tempFile.getPath(), true);
				} else {
					saveTic(tempFile.getPath(), true);
					// crop(Uri.fromFile(tempFile));
				}
			} else {
				Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！", 0).show();
			}
		} else if (requestCode == PHOTO_REQUEST_CUT) {
			// 从剪切图片返回的数据
			if (data != null) {
				if (hasSdcard()) {
					Bitmap btm = data.getParcelableExtra("data");
					btm = PictureUtils.comp(btm, 95, 95);
					String path = fileHelper.creatSDDir("hyk");
					String photo_name = spqq.getValue("redId") + "_logo";
					String url = fileHelper.saveBitmap(btm, path, photo_name);
					spqq.putStrValue(photo_name, url);
					httpForUploadHeadpic(url);
					user_img.setImageBitmap(btm);
				}
			}

			try {
				// 将临时文件删除
				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
//
//	/*
//	 * 剪切图片
//	 */
//	private void crop(Uri uri) {
//		// 裁剪图片意图
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		intent.putExtra("crop", "true");
//		// 裁剪框的比例，1：1
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// 裁剪后输出图片的尺寸大小
//		intent.putExtra("outputX", 250);
//		intent.putExtra("outputY", 250);
//
//		intent.putExtra("outputFormat", "jpg");// 图片格式
//		intent.putExtra("noFaceDetection", true);// 取消人脸识别
//		intent.putExtra("return-data", true);
//		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
//		startActivityForResult(intent, PHOTO_REQUEST_CUT);
//	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_DONE:
			setSingn();
			break;
		}
		return true;
	}

	/**
	 * 上传头像
	 */
	private void httpForUploadHeadpic(String path) {
		if (NetworkUtil.checkNetworkState(getActivity())) {
			startLoad();
			RequestParams params = new RequestParams();
			File file = new File(path);
			String url = Httpaddress.IP_ADDRESS + Httpaddress.UPLOAD_IMAGE;
			try {
				params.put("file", file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			params.put("regId", spqq.getValue("redId"));
			AsyncHttpUtil.postImg(url, params, new AsyncHttpResponseHandler() {
				public void onSuccess(String response) { // 返回的是JSONObject，会调用这里
					stopLoad();
					ResultStringForLoadap resultString = new ResultStringForLoadap();
					resultString = binder.fromJson(response, ResultStringForLoadap.class);
					if (resultString != null) {
						if (resultString.getResult() != 0) {

						}
					}
				};

				public void onFailure(Throwable arg0) {
					try {
						stopLoad();
						ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
						// String string = arg0.getMessage();
					} catch (Exception e) {
						// TODO: handle exception
					}
				};

				public void onFinish() {
				};
			});
		} else {
			ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
		}
	}

	/**
	 * 修改签名
	 */
	private void setSingn() {
		final String signText = sign_name_edit.getText().toString().trim();
		if (NetworkUtil.checkNetworkState(getActivity())) {
			String url = Httpaddress.IP_ADDRESS + Httpaddress.SET_SIGN;
			Map<String, String> params = new HashMap<String, String>();
			params.put("regId", spqq.getValue("redId"));
			params.put("signName", signText);
			String httpUrl = HttpUtils.checkUrl(url, params);
			jsonObjectRequest = new StringRequest(httpUrl,  new Response.Listener<String>() {  
	            @Override  
	            public void onResponse(String response) {  
	            	ResultStringForLoadap resultString = new ResultStringForLoadap();
					resultString = binder.fromJson(response, ResultStringForLoadap.class);
					if (resultString.getResult() == 0) {
						ToastUtil.show(getActivity(), CHString.SUCESS_SIGN);
						spqq.putStrValue("signName", signText);
					} else {
						ToastUtil.show(getActivity(), CHString.SYS_ERROR);
					}
	            }  
	        }, new Response.ErrorListener() {  
	            @Override  
	            public void onErrorResponse(VolleyError error) {  
	            	if (NetworkUtil.checkNetworkState(getActivity())) {
//	            		setSingn();
					}
	            }  
	        }); 
//			jsonObjectRequest.setTag("setSingn");
//			requestQueue.add(jsonObjectRequest);
			XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest,"setSingn");
		}
	}

	private void getFromLogin() {
		String userId = spqq.getValue("userId");
		if (StringUtils.isNotEmpty(userId)) {
			startLoad();
			String url = Httpaddress.IP_ADDRESS + Httpaddress.LOGIN_ADDRESS;
			Map<String, String> params = new HashMap<String, String>();
			params.put("imei", UserInfo.getUserInfo().getIMEI());
			params.put("userName", spqq.getValue("uname"));
			params.put("accountId", spqq.getValue("logintype"));
			params.put("userKey", spqq.getValue("userId"));
			params.put("signName", spqq.getValue("signName"));
			params.put("phoneType", Httpaddress.PHONE_TYPE);
			params.put("headPic", spqq.getValue("userImg"));
			String sexSign = spqq.getValue("uSex");
			if ("男".equals(sexSign)) {
				params.put("sex", "1");
			} else {
				params.put("sex", "0");
			}
			String httpUrl = HttpUtils.checkUrl(url, params);
			jsonObjectRequest = new StringRequest(httpUrl,  new Response.Listener<String>() {  
	            @Override  
	            public void onResponse(String response) {  
	            	stopLoad();
					ResultString resultString = new ResultString();
					resultString = binder.fromJson(response, ResultString.class);
					if (resultString != null) {
						loginResult = resultString.getObj();
						user_name.setText(loginResult.getUserName());

						// sign_name_edit.setText(loginResult.getSignName());

						String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
						try {
							SpannableString spannableString = ExpressionUtil.getExpressionString(getActivity(),
									loginResult.getSignName(), zhengze);
							sign_name_edit.setText(spannableString);
						} catch (Exception e) {
							e.printStackTrace();
						}

						spqq.putStrValue("sign_name", loginResult.getSignName());
						spqq.putStrValue("user_name", loginResult.getUserName());
						spqq.putStrValue("redId", loginResult.getRegId());

						spqq.putStrValue("focusme", loginResult.getFocusMe());
						spqq.putStrValue("focushim", loginResult.getFocusHim());

						ImageListener listener = new ImageListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								if (R.drawable.the_default != 0) {
									user_img.setImageResource(R.drawable.the_default);
								}
							}

							@Override
							public void onResponse(ImageContainer response, boolean isImmediate) {
								if (response.getBitmap() != null) {
									user_img.setImageBitmap(response.getBitmap());
									String path = fileHelper.creatSDDir("hyk");
									String photo_name = spqq.getValue("redId") + "_logo";
									String url = fileHelper.saveBitmap(response.getBitmap(), path, photo_name);
									spqq.putStrValue(spqq.getValue("redId") + "_logo", url);
									
								} else if (R.drawable.the_default != 0) {
									user_img.setImageResource(R.drawable.the_default);
								}
							}
						};
						XmppApplication.getsInstance().imageLoader.get(loginResult.getHeadPic(), listener);
						
						if (StringUtils.isNotEmpty(loginResult.getRegId())) {
							if (XmppConnection.isConnected()) {
								if (!XmppConnection.getConnection().isAuthenticated()) {
									XmppService.login(spqq.getValue("redId"));
								}
							}
						}
					}else{
						((MyCenterActivity)getActivity()).loginGoTo();
					}
	            }  
	        }, new Response.ErrorListener() {  
	            @Override  
	            public void onErrorResponse(VolleyError error) {  
	            	if (NetworkUtil.checkNetworkState(getActivity())) {
//						getFromLogin();
	            		ToastUtil.show(getActivity(), error.getMessage());
					}
	            }  
	        }); 
			XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest,"getFromLogin");
			
		}
	}

	/**
	 * 保存头像
	 */
	private void saveTic(String urlPath, boolean isPic) {
		Bitmap btm = PictureUtils.getimage(urlPath, DisplayUtil.dip2px(getActivity(), 95),
				DisplayUtil.dip2px(getActivity(), 95));
		// Bitmap btm = data.getParcelableExtra("data");
		// btm = PictureUtils.comp(btm, 95, 95);
		if (btm != null) {
			String path = fileHelper.creatSDDir("hyk");
			String photo_name = spqq.getValue("redId") + "_logo";
			String url = fileHelper.saveBitmap(btm, path, photo_name);
			spqq.putStrValue(photo_name, url);
			httpForUploadHeadpic(url);
			user_img.setImageBitmap(btm);
			if (isPic) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), TabMenuActivity.class);
				intent.putExtra("isPic", true);
				startActivity(intent);
			}
		}
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpFromUserInfo");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpFromUserInfo");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
	}
}
