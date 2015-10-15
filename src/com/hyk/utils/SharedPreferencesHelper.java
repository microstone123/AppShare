package com.hyk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @ClassName: SharedPreferencesHelper
 * @Description: TODO(SharedPreferences包装类)
 * @author linhaishi
 * @date 2013-9-16 下午4:41:47
 * 
 */
public class SharedPreferencesHelper {
	SharedPreferences sp;
	SharedPreferences.Editor editor;

	Context context;

	public SharedPreferencesHelper(Context c, String name) {
		context = c;
		sp = context.getSharedPreferences(name, 0);
		editor = sp.edit();
	}

	public void putIntValue(String key, int value) {
		editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
		// Common.month_total = value;
	}

	public void putStrValue(String key, String value) {
		editor = sp.edit();
//		editor.remove(key);
		editor.putString(key, value);
		editor.commit();
	}
	
	public void removeKey(String key){
		editor.clear();
		editor.commit();
	}

	public String getValue(String key) {
		String value = sp.getString(key, "");
		// Common.month_total = value;
		return value;
	}
	
	public void putUserValue(String nameKey, String nameValue,String passWKey, String passWValue) {
		editor = sp.edit();
		editor.putString(nameKey, nameValue);
		editor.putString(passWKey, passWValue);
		editor.commit();
	}
}
