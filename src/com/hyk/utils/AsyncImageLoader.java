package com.hyk.utils;

import java.lang.ref.SoftReference;
import java.util.Random;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {
	public Bitmap loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
		if (ImageCacheUtil.imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = ImageCacheUtil.imageCache.get(imageUrl);
			Bitmap drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap drawable = LoadImageUtils.getImgFromCache(imageUrl, ImageCacheUtil.imageCache);
				ImageCacheUtil.imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				try {
					Thread.sleep(new Random().nextInt(3000));
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap imageDrawable, String imageUrl);
	}
}