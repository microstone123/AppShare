package com.hyk.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageLoaderPartner {

	private static final String IMAGES = "images";

	// Initialize ImageLoader with created configuration. Do it once on
	// Application start.
	public static void init(Context context) {
		File cacheDir = getOwnCacheDirectory(context.getApplicationContext(), IMAGES);
		ImageLoader imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
				.memoryCacheExtraOptions(480, 800) // max width, max height
				.denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You
																														// can
																														// pass
																														// your
																														// own
																														// memory
																														// cache
																														// implementation
				.discCache(new UnlimitedDiscCache(cacheDir)) // You can pass
																// your own disc
																// cache
																// implementation
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()).build();
		imageLoader.init(config);

		// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75) // Can slow
		// ImageLoader, use it carefully (Better don't use it)
		// .threadPoolSize(3)
		// .threadPriority(Thread.NORM_PRIORITY - 1)
		// .offOutOfMemoryHandling()
		// .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
		// .imageDownloader(new URLConnectionImageDownloader(5 * 1000, 20 *
		// 1000)) // connectTimeout (5 s), readTimeout (20 s)
		// .tasksProcessingOrder(QueueProcessingType.FIFO)
	}

	public static File getOwnCacheDirectory(Context context, String name) {
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			appCacheDir = new File(context.getExternalCacheDir(), name);
		}
		if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	private static final int DURATION = 500;

	public static DisplayImageOptions getOptions(int resId) {
		return new DisplayImageOptions.Builder().showImageOnLoading(resId).showImageForEmptyUri(resId)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public static DisplayImageOptions getOptionsNoAnimation(int resId) {
		return new DisplayImageOptions.Builder().showImageOnLoading(resId).showImageForEmptyUri(resId)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	public static DisplayImageOptions getOptionsNoCache(int resId) {
		return new DisplayImageOptions.Builder().showImageOnLoading(resId).showImageForEmptyUri(resId)
				.displayer(new FadeInBitmapDisplayer(DURATION)).build();
	}

	public static DisplayImageOptions getOptions() {
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(DURATION)).build();
	}

}
