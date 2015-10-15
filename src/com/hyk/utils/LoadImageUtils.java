package com.hyk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class LoadImageUtils {
	/**
	 * 获取网络中图片资源
	 * 
	 * @param src
	 *            图片地址
	 * @return drawable对象
	 */
	public static Bitmap loadImage(String src) {
		URL url = null;
		Bitmap drawable = null;
		InputStream is = null;
		try {
			url = new URL(src);
			is = url.openStream();
			// drawable = Drawable.createFromStream(is, "img");
			is.close();
		} catch (IOException e) {
			drawable = null;
		}

		return drawable;
	}

	/**
	 * 获取网络中图片资源
	 * 
	 * @param src
	 *            图片地址
	 * @return Bitmap对象
	 */
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;  
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			Log.e("", "", e);
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 从网络上下载
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitMapFromUrl(String url) {
		Bitmap bitmap = null;
		URL u = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 获取图片列表 从缓存中读取 Bitmap
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static List<Drawable> getImgFromCacheList(List<String> url, Map<String, SoftReference<Drawable>> imgCache) {
		List<Drawable> bitmapList = new ArrayList<Drawable>();
		try {
			for (int i = 0; i < url.size(); i++) {
				// 从内存中读取
				if (imgCache.containsKey(url.get(i))) {
					synchronized (imgCache) {
						SoftReference<Drawable> bitmapReference = imgCache.get(url.get(i));
						if (null != bitmapReference) {
							bitmapList.add(bitmapReference.get());
						}
					}
				} else {// 获取网络中图片资源
//					Drawable drawable = loadImage(url.get(i));
//					bitmapList.add(drawable);
					// 将图片保存进内存中
//					imgCache.put(url.get(i), new SoftReference<Drawable>(drawable));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmapList;
	}

	/**
	 * 获取单个图片 从缓存中读取 Bitmap
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getImgFromCache(String url, Map<String, SoftReference<Bitmap>> imgCache) {
		Bitmap bitmap = null;
		try {
			// 从内存中读取
			if (imgCache.containsKey(url)) {
				synchronized (imgCache) {
					SoftReference<Bitmap> bitmapReference = imgCache.get(url);
					if (null != bitmapReference) {
						bitmap = bitmapReference.get();
					}
				}
			} else {

				// 获取网络中图片资源
				Bitmap drawable = returnBitMap(url);
				// 将图片保存进内存中
				imgCache.put(url, new SoftReference<Bitmap>(drawable));
				bitmap = drawable;

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

	/**
	 * 从缓存中读取 Drawable
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	// public static Drawable getImgFromCache(String
	// url,Map<String,SoftReference<Drawable>> imgCache) throws Exception {
	// Drawable drawable = null;
	// //从内存中读取
	// if(imgCache.containsKey(url)) {
	// synchronized (imgCache) {
	// SoftReference<Drawable> bitmapReference = imgCache.get(url);
	// if(null != bitmapReference) {
	// drawable = bitmapReference.get();
	// }
	// }
	// } else {//从网络中下载
	// drawable = loadImage(url);
	// //将图片保存进内存中
	// imgCache.put(url, new SoftReference<Drawable>(drawable));
	// }
	// return drawable;
	// }

	/**
	 * 获取图片列表
	 * 
	 * @param urlList
	 * @return
	 */
	// public static List<Drawable> getDrawableList(List<String> urlList) {
	// List<Drawable> drawableList = new ArrayList<Drawable>();
	// try {
	// for (int i = 0; i < urlList.size(); i++) {
	// Drawable drawable = LoadImageUtils.getImgFromCache(urlList.get(i),
	// ImageCacheUtil.imageCache);
	// drawableList.add(drawable);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return drawableList;
	// }
}
