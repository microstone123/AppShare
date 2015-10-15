package com.hyk.activity.nearby;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.hyk.activity.R;
import com.hyk.user.UserList;
import com.hyk.utils.ACache;
import com.hyk.utils.DisplayUtil;
import com.hyk.utils.RoundPoint;

@SuppressLint("DrawAllocation")
public class Circleview extends View {
	// 界面需要的图片
	private Bitmap panpic;
	// private Bitmap scan;
	private Timer timer = null;
	private TimerTask task;
	public int x = 0;
	private Matrix matx = new Matrix();
	private Handler radioHandler;
	private Matrix panhandTrans = new Matrix();
	private List<UserList> userLists = new ArrayList<UserList>();
	private List<long[]> xyList = new ArrayList<long[]>();
	Bitmap panpicdd;
	int count = 0;
	int timerCount = 0;
	private int timerNum = 0;
	private boolean canveDian = false;
	private final static int GET_POINT = 1004;
	private int getPoint = 6;
	private final static int GET_DATA = 1003;
	private ACache mAcache;
	private int width ;// 屏幕宽度
	private int height ;
	private float w;
	private float h;
	
	private	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1002:
				x += 2;
				canveDian = true;
				timerNum = timerNum + 50;
				if (timerNum == timerCount * 500 + 1000) {
					timerCount++;
					if (userLists != null) {
						if (userLists.size() > 0) {
							if (count < userLists.size()) {
								count++;

								if (count == 1) {
									Message msgPoint = new Message();
									msgPoint.what = GET_DATA;
									radioHandler.sendMessage(msgPoint);
								}

								if (count == getPoint) {
									Message msgPoint = new Message();
									msgPoint.what = GET_POINT;
									radioHandler.sendMessage(msgPoint);
								}
							}
						}
					}
				}
				postInvalidate(); // 这个函数强制UI线程刷新界面
				break;
			default:
				break;
			}
		}
	};
	
	@SuppressWarnings("deprecation")
	@SuppressLint("HandlerLeak")
	public Circleview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mAcache = ACache.get(context);
		panpic = BitmapFactory.decodeResource(getResources(), R.drawable.therader);
		panpicdd = BitmapFactory.decodeResource(getResources(), R.drawable.lineaa);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		height = wm.getDefaultDisplay().getHeight();
//		
		w = (float)(width-panpic.getWidth())/2;
		h = (float)(height-panpic.getHeight()-DisplayUtil.dip2px(context, 135))/2;
//		h=1;
//		Log.e("w", h+" "+w+" "+DisplayUtil.dip2px(context, 135)+" "+width+" "+panpic.getWidth());
	}

	// 重写View类的onDraw()函数
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		matx.reset();
		Paint localPaint = new Paint();
		localPaint.setAntiAlias(true); // 设置取消锯齿效果
		localPaint.setFilterBitmap(true);
		/**
		 * 设置绕点旋转
		 */
//		Integer width = Integer.valueOf(mAcache.getAsString("screen_width"));
		
		matx.setTranslate(w, h);
		matx.preRotate(x, panpic.getWidth() / 2, panpic.getHeight() / 2);
		canvas.drawBitmap(panpic, matx, localPaint);
		if (canveDian) {
			canveDian = false;
			for (int i = 0; i < count; i++) {
				long[] xy = xyList.get(i);
				canvas.drawBitmap(panpicdd, xy[0], xy[1], localPaint);
			}
		}
	}

	public void stopRotate() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
		postInvalidate();
	}

	public void startRotate() {
		timerCount = 0;
		count = 0;
		timerNum = 0;
		if (userLists != null) {
			for (int i = 0; i < userLists.size(); i++) {
				long[] xy = new long[2];
				xy = RoundPoint.getPoinXY(userLists.get(i));
				xyList.add(xy);
			}
		}
		if (timer == null) {
			timer = new Timer();
			if (task == null) {
				task = new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message message = new Message();
						message.what = 1002;
						handler.sendMessage(message);
					}
				};
			}
			timer.schedule(task, 100, 50);
		}
	}

	public void setRadioHandler(Handler radioHandler) {
		this.radioHandler = radioHandler;
	}

	public List<UserList> getUserLists() {
		return userLists;
	}

	public void setUserLists(List<UserList> userLists) {
		this.userLists = userLists;
	}
}