package com.hyk.baseadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.hyk.activity.R;
import com.hyk.utils.BimpUtils;
import com.hyk.utils.DisplayUtil;
import com.hyk.utils.FileUtils;
import com.hyk.utils.PictureUtils;

public class SeleGridAdapter extends BaseAdapter {
	private LayoutInflater inflater; // 视图容器
	private int selectedPosition = -1;// 选中的位置
	private boolean shape;
	private Context context;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public SeleGridAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void update() {
		loading();
	}

	public int getCount() {
		if(BimpUtils.bmp.size() + 1<6){
			return (BimpUtils.bmp.size() + 1);
		}else{
			return 6;
		}
		
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == BimpUtils.bmp.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_addpic_unfocused));
			if (position == 6) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(BimpUtils.bmp.get(position));
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			@SuppressLint("NewApi")
			public void run() {
				while (true) {
					if (BimpUtils.max == BimpUtils.drr.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						String path = BimpUtils.drr.get(BimpUtils.max);
						Bitmap bm = PictureUtils.getimage(path, DisplayUtil.dip2px(context,800),
								DisplayUtil.dip2px(context,480));
						BimpUtils.bmp.add(bm);
						
						String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
						String newsPath = FileUtils.saveBitmap(bm, "" + newStr);
						
						for (int i = 0; i < BimpUtils.drr.size(); i++) {
							if(path.equals(BimpUtils.drr.get(i))){
								BimpUtils.drr.set(i, newsPath);
								break;
							}
						}
						
						BimpUtils.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}
}