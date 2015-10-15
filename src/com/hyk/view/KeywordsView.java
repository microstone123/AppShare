package com.hyk.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.app.AppHotList;
import com.hyk.utils.ACache;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.xmpp.XmppApplication;

//import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class KeywordsView extends AbsoluteLayout implements OnGlobalLayoutListener {

	public static final int IDX_X = 0;
	public static final int IDX_Y = 1;
	public static final int IDX_W = 2;
	public static final int IDX_H = 3;
	public static final int IDX_SW = 4;
	public static final int APP_ID = 5;
	public static final int IDX_TXT_LENGTH = 2;
	public static final int IDX_DIS_Y = 3;
	public List<Integer> colorRady = new ArrayList<Integer>();
	public int colorRadyCount = 0;
	public boolean randBol = true;
	public int randStatic = 0;

	/** 从外至内的动?? */
	public static final int ANIMATION_IN = 1;
	/** 从内至外的动?? */
	public static final int ANIMATION_OUT = 2;

	/*** 位移的动画类??从外围移动到坐标?? */
	public static final int OUTSIDE_TO_LOCATION = 1;
	/** 位移的动画类??从坐标点移动的外?? */
	public static final int LOCATION_TO_OUTSIDE = 2;
	/** 位移的动画类??从中心点移动到坐标点 */
	public static final int CENTER_TO_LOCATION = 3;
	/** 位移的动画类??从坐标点移动到中心点 */
	public static final int LOCATION_TO_CENTER = 4;
	LinkedList<Integer> listX = new LinkedList<Integer>();
	public static final long ANIM_DURATION = 800l;
	public static final int MAX = 9; // 最大显示条数
	private int imgNum = 60;
	public static final int TEXT_SIZE_MAX = 18;
	public static final int TEXT_SIZE_MIN = 8;

	private int yCenter;
	private int xCenter;
	// private AsyncImageLoader asyncImageLoader;
	private OnClickListener itemClickListener;
	private static Interpolator interpolator;
	private static AlphaAnimation animAlpha2Opaque;
	private static AlphaAnimation animAlpha2Transparent;
	private static ScaleAnimation animScaleNormal2Large, animScaleZero2Normal;
	// protected ImageLoader imageLoader = ImageLoader.getInstance();

	/** 存储显示的关键字 */
	private List<AppHotList> vecKeywords;
	// private Vector<AppListForResult> appList;
	private int width, height;
	private Bitmap bitmapimg = null;
	private List<Integer> radmRayyList = new ArrayList<Integer>();
	private int[] RadmRayyqq = { R.color.text_color02, R.color.text_color03, R.color.text_color04,
			R.color.text_color05, R.color.text_color06, R.color.text_color07, R.color.text_color08,
			R.color.text_color09, R.color.text_color10, R.color.text_color11, R.color.text_color12,
			R.color.text_color13, R.color.text_color14, R.color.text_color15, R.color.text_color16,
			R.color.text_color17, R.color.text_color18, R.color.text_color19, R.color.text_color20 };

	/**
	 * go2Show()中被赋??为true,标识????人员触发其开始动画显??
	 * 本标识的作用是防止在填充keywrods为完成的过程中获取到width和height后提前启动动?? 在show()方法中其被赋值为false.
	 * 真正能够动画显示的另????要条??width和height不为0.
	 */
	private boolean enableShow;
	private Random random;
	private boolean ranColorbol = true;
	// protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	/**
	 * @see ANIMATION_IN
	 * @see ANIMATION_OUT
	 * @see OUTSIDE_TO_LOCATION
	 * @see LOCATION_TO_OUTSIDE
	 * @see LOCATION_TO_CENTER
	 * @see CENTER_TO_LOCATION
	 * */
	private int txtAnimINType, txtAnimOutType;
	/** ????????启动动画显示的时间?? */
	private long lastStartAnimationTime;
	/** 动画运行时间 */
	private long animDuration;

	private ACache mAcache;

	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;

	public KeywordsView(Context context) {
		super(context);
		// asyncImageLoader = new AsyncImageLoader();
		mAcache = ACache.get(context);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);

		init();
	}

	public KeywordsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mAcache = ACache.get(context);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
		// asyncImageLoader = new AsyncImageLoader();

		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);

		init();
	}

	public KeywordsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mAcache = ACache.get(context);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);

		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
		// asyncImageLoader = new AsyncImageLoader();
		init();
	}

	private void init() {
		lastStartAnimationTime = 0l;
		animDuration = ANIM_DURATION;
		random = new Random();
		vecKeywords = new Vector<AppHotList>(MAX);
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		interpolator = AnimationUtils.loadInterpolator(getContext(), android.R.anim.decelerate_interpolator);
		animAlpha2Opaque = new AlphaAnimation(0.0f, 1.0f);
		animAlpha2Transparent = new AlphaAnimation(1.0f, 0.0f);
		animScaleNormal2Large = new ScaleAnimation(1, 2, 1, 2);
		animScaleZero2Normal = new ScaleAnimation(0, 1, 0, 1);
	}

	public long getDuration() {
		return animDuration;
	}

	public void setDuration(long duration) {
		animDuration = duration;
	}

	public boolean feedKeyword(AppHotList keyword) {

		boolean result = false;
		if (vecKeywords.size() < MAX) {
			result = vecKeywords.add(keyword);
		}
		return result;
	}

	/**
	 * ????动画显示??br/> 之前已经存在的ImageView将会显示????动画??br/>
	 * 
	 * @return 正常显示动画返回true；反之为false。返回false原因如下??br/>
	 *         1.时间上不允许,受lastStartAnimationTime的制约；<br/>
	 *         2.未获取到width和height的????br/>
	 */
	public boolean go2Shwo(int animType) {
		if (System.currentTimeMillis() - lastStartAnimationTime > animDuration) {
			enableShow = true;
			if (animType == ANIMATION_OUT) {
				txtAnimINType = CENTER_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_OUTSIDE;
			}
			disapper();
			boolean result = show();
			return result;
		}
		return false;
	}

	private void disapper() {
		int size = getChildCount();
		for (int i = size - 1; i >= 0; i--) {
			final NetworkImageView txt = (NetworkImageView) getChildAt(i);
			if (txt.getVisibility() == View.GONE) {
				removeView(txt);
				continue;
			}
			AbsoluteLayout.LayoutParams layParams = (LayoutParams) txt.getLayoutParams();
			int[] xy = new int[] { layParams.x, layParams.y, txt.getWidth() };
			AnimationSet animSet = getAnimationSet(xy, (width >> 1), (height >> 1), txtAnimOutType);
			txt.startAnimation(animSet);
			/************/
			animSet.setFillBefore(true);
			/************/
			animSet.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					txt.setOnClickListener(null);
					txt.setClickable(true);
					txt.setVisibility(View.GONE);
				}
			});
		}
	}

	private boolean checkNum(int count, int num) {
		for (int i = 0; i < count; i++) {
			if (num == radmRayyList.get(i)) {
				ranColorbol = false;
			}
		}
		return ranColorbol;
	}

	private boolean show() {
		if (width > 0 && height > 0 && vecKeywords != null && vecKeywords.size() > 0 && enableShow) {
			enableShow = false;
			Integer hright = Integer.valueOf(mAcache.getAsString("screen_height"));
			if (hright == null) {
				hright = 1280;
			}
			lastStartAnimationTime = System.currentTimeMillis();
			xCenter = width >> 1;
			yCenter = height >> 1;
			int size = vecKeywords.size();
			int xItem = width / size, yItem = height / size;
			LinkedList<Integer> listY = new LinkedList<Integer>();
			for (int i = 0; i < size; i++) {
				listX.add(i * xItem);
				listY.add(i * yItem);
			}
			LinkedList<NetworkImageView> listButton = new LinkedList<NetworkImageView>();
			for (int i = 0; i < size; i++) {
				// 随机位置,粗糙
				final int xy[] = randomXY(random, listX, listY, xItem);

				final NetworkImageView txt = new NetworkImageView(getContext());
				txt.setOnClickListener(itemClickListener);

				try {
					txt.setDefaultImageResId(R.drawable.transparent_d);
					txt.setErrorImageResId(R.drawable.transparent_d);
					txt.setImageUrl(vecKeywords.get(i).getAppImage(), XmppApplication.getsInstance().imageLoader);

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
				}

				xy[IDX_W] = 80;
				xy[IDX_H] = 80;
				xy[IDX_SW] = 10;
				xy[APP_ID] = vecKeywords.get(i).getAppid();
				listButton.add(txt);
				txt.setTag(xy);
			}
			attch2Screen(listButton, xCenter, yCenter);
			return true;
		}
		return false;
	}

	/**
	 * 修正边缘
	 * 
	 * @param xy
	 *            坐标数组
	 * @param h
	 *            图片高度
	 * @param w
	 *            图片宽度
	 * @param textW
	 *            文本长度
	 * @return
	 */
	private int[] correctEdge(int[] xy, int h, int w, int textW) {
		// 判断x右边缘
		if (xy[IDX_X] > width - textW - w - 10) {
			int baseX = xy[IDX_X] - (width - textW - w - 10);
			xy[IDX_X] = xy[IDX_X] - baseX;
		}
		// 判断x左边缘
		if (xy[IDX_X] < 5) {
			xy[IDX_X] = 5;
		}
		// 判断y下边缘
		if (xy[IDX_Y] > height - h - 5) {
			int baseX = xy[IDX_Y] - (height - h - 5);
			xy[IDX_Y] = xy[IDX_Y] - baseX;
		}
		// 判断y上边缘
		if (xy[IDX_Y] < 5) {
			xy[IDX_Y] = 5;
		}
		xy[IDX_TXT_LENGTH] = textW;
		return xy;
	}

	/**
	 * 在屏幕上显示
	 * 
	 * @param listTxt
	 * @param xCenter
	 * @param yCenter
	 * @param yItem
	 * @param whList
	 */
	private void attch2Screen(LinkedList<NetworkImageView> listTxt, int xCenter, int yCenter) {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		int w = (int) (70 * (Double.valueOf(width)/ 720));
		int size = listTxt.size();
		listTxt = sortXYList(listTxt);
		for (int i = 0; i < size; i++) {
			NetworkImageView txt = listTxt.get(i);
			int[] iXY = (int[]) txt.getTag();
			txt.setTag(iXY[APP_ID]);
			AbsoluteLayout.LayoutParams layParams = new AbsoluteLayout.LayoutParams(w, w, iXY[IDX_X], iXY[IDX_Y]);
			addView(txt, layParams);
			// 动画
			AnimationSet animSet = getAnimationSet(iXY, xCenter, yCenter, txtAnimINType);
			txt.startAnimation(animSet);
		}
	}

	private LinkedList<NetworkImageView> sortXYList(LinkedList<NetworkImageView> listTxt) {
		int size = listTxt.size();
		for (int i = 0; i < size; i++) {
			for (int k = i + 1; k < size; k++) {
				if (((int[]) listTxt.get(k).getTag())[IDX_Y] < ((int[]) listTxt.get(i).getTag())[IDX_Y]) {
					NetworkImageView iTmp = listTxt.get(i);
					NetworkImageView kTmp = listTxt.get(k);
					listTxt.set(i, kTmp);
					listTxt.set(k, iTmp);
					vecKeywords.set(i, vecKeywords.get(k));
					vecKeywords.set(k, vecKeywords.get(i));
				}
			}
		}
		listTxt = AlternatelyXYList(listTxt);
		return listTxt;
	}

	private LinkedList<NetworkImageView> AlternatelyXYList(LinkedList<NetworkImageView> listTxt) {
		int size = listTxt.size();
		for (int i = 0; i < size; i++) {
			int[] iXY = (int[]) listTxt.get(i).getTag();
			int x = iXY[IDX_X];
			// int y = iXY[IDX_Y];
			int w = iXY[IDX_W];
			int h = iXY[IDX_H];
			int sw = iXY[IDX_SW];

			if (i == 0) {
				if (x > xCenter) {
					randStatic = 0;
				} else {
					randStatic = 1;
				}
			} else {
				if (randStatic == 0) {
					if (x > xCenter) {
						iXY[IDX_X] = width - iXY[IDX_X];
					}

					if (iXY[IDX_X] > xCenter - 40) {
						iXY[IDX_X] = xCenter - 40;
					}
					randStatic = 1;
				} else {
					if (x < xCenter) {
						iXY[IDX_X] = width - iXY[IDX_X];
					}
					if (iXY[IDX_X] < xCenter + 50) {
						iXY[IDX_X] = xCenter + 50;
					}
					randStatic = 0;
				}
			}

			iXY = correctEdge(iXY, h, w, sw);
			listTxt.get(i).setTag(iXY);
		}
		return listTxt;
	}

	private AnimationSet getAnimationSet(int[] xy, int xCenter, int yCenter, int type) {
		AnimationSet animSet = new AnimationSet(true);
		animSet.setInterpolator(interpolator);
		if (type == LOCATION_TO_OUTSIDE) {
			animSet.addAnimation(animAlpha2Transparent);
			animSet.addAnimation(animScaleNormal2Large);
			TranslateAnimation translate = new TranslateAnimation(0,
					(xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, (xy[IDX_Y] - yCenter) << 1);
			animSet.addAnimation(translate);
		}
		if (type == CENTER_TO_LOCATION) {
			animSet.addAnimation(animAlpha2Opaque);
			animSet.addAnimation(animScaleZero2Normal);
			TranslateAnimation tranlate = new TranslateAnimation(xCenter - xy[IDX_X], 0, yCenter - xy[IDX_Y], 0);
			animSet.addAnimation(tranlate);
		}
		animSet.setDuration(animDuration);
		animSet.setFillBefore(true);
		return animSet;
	}

	private int[] randomXY(Random ran, LinkedList<Integer> listX, LinkedList<Integer> listY, int xItem) {
		int[] arr = new int[6];
		arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));
		arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));
		return arr;
	}

	public void onGlobalLayout() {
		int tmpW = getWidth();
		int tmpH = getHeight();
		if (width != tmpW || height != tmpH) {
			width = tmpW;
			height = tmpH;
			show();
		}
	}

	public List<AppHotList> getKeywords() {
		return vecKeywords;
	}

	public void rubKeywords() {
		vecKeywords.clear();
	}

	public void rubAllViews() {
		removeAllViews();
	}

	public void setOnClickListener(OnClickListener listener) {
		itemClickListener = listener;
	}

	public boolean checkRanColor(int ranColor) {
		if (colorRady.size() > 0) {
			for (int i = 0; i < colorRady.size(); i++) {
				if (colorRady.get(i) == ranColor) {
					ranColorbol = true;
				}
			}
		} else {
			ranColorbol = true;
		}
		return ranColorbol;
	}

	public void clearMemoryCache() {
		// imageLoader.recycleBitmap();
	}

	public void newImageLoader(Context context) {
		// imageLoader = ImageLoader.getInstance();
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	public void recycleBitmap() {
		// imageLoaderShow.recycleBitmap();
	}
}
