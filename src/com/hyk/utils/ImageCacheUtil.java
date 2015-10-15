package com.hyk.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ImageCacheUtil {
	public static Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
}
