package com.hyk.activity.center;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.activity.center.set.AnnouncementActivity;
import com.hyk.activity.center.set.CheckUpdataActivity;
import com.hyk.activity.center.set.FeaturesActivity;
import com.hyk.activity.center.set.FeedbackActivity;
import com.hyk.center.TheMoreItem;
import com.hyk.utils.AppManager;
import com.hyk.view.ViewSetClick;


/**
 * @ClassName: SetUpActivity
 * @Description: TODO(设置界面 )
 * @author linhs
 * @date 2014-3-6 下午2:03:21
 */
public class SetUpActivity extends Activity implements OnClickListener {
	private ListView the_more_list;
	private ImageButton btn_back;
	private MoreAdapter moreAdapter;

	// private ImageButton main_location_view, main_search_view,
	// main_application_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.the_more);
		AppManager.getAppManager().addActivity(this);
		setupViews();

	}

	public void setupViews() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		the_more_list = (ListView) findViewById(R.id.the_more_list);
		moreAdapter = new MoreAdapter(this, TheMoreItem.THEMORE_ITEMLIST);
		the_more_list.setAdapter(moreAdapter);
		the_more_list.setSelector(R.drawable.hide_listview_yellow_selector);
		btn_back.setOnClickListener(this);
		the_more_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ViewSetClick.getAlpha(arg1, SetUpActivity.this);
				
				switch ((int)arg3) {
				case 0:
					setActivity(CheckUpdataActivity.class);
					break;
				case 1:
					setActivity(FeaturesActivity.class);
					break;
				case 2:
					setActivity(FeedbackActivity.class);
					break;
				case 3:
					setActivity(AnnouncementActivity.class);
					break;
				default:
					break;
				}
			}
		});

	}
	
	private void setActivity(@SuppressWarnings("rawtypes") Class cls){
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

	}

	class MoreAdapter extends BaseAdapter {
		private String[] TheMore_ItemList;
		private LayoutInflater listContainer; // 视图容器

		public final class ListItemView { // 自定义控件集合
			public TextView more_text;
//			public ImageView more_line_img;
		}

		MoreAdapter(Context context, String[] TheMore_ItemList) {
			this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
			this.TheMore_ItemList = TheMore_ItemList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TheMore_ItemList.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 自定义视图
			ListItemView listItemView = null;
			if (convertView == null) {
				listItemView = new ListItemView();
				// 获取list_item布局文件的视图
				convertView = listContainer.inflate(R.layout.the_more_item, null);
				// 获取控件对象
				listItemView.more_text = (TextView) convertView.findViewById(R.id.more_text);
//				listItemView.more_line_img = (ImageView) convertView.findViewById(R.id.more_line_img);
				listItemView.more_text.setText(TheMore_ItemList[position]);

//				if (position == TheMore_ItemList.length - 1) {
//					listItemView.more_line_img.setBackgroundResource(R.drawable.transparent_d);
//				}
				convertView.setTag(listItemView);
			} else {
				listItemView = (ListItemView) convertView.getTag();
			}
			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ViewSetClick.getAlpha(btn_back, this);
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}