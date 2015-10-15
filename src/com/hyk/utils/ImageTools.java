package com.hyk.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Tools for handler picture
 * 
 * @author Ryan.Tang
 * 
 */
public final class ImageTools {

	/**
	 * Transfer drawable to bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap to drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * Input stream to bitmap
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static Bitmap inputStreamToBitmap(InputStream inputStream) throws Exception {
		return BitmapFactory.decodeStream(inputStream);
	}

	/**
	 * Byte transfer to bitmap
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	/**
	 * Byte transfer to drawable
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = null;
		if (byteArray != null) {
			ins = new ByteArrayInputStream(byteArray);
		}
		return Drawable.createFromStream(ins, null);
	}

	/**
	 * Bitmap transfer to bytes
	 * 
	 * @param byteArray
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		byte[] bytes = null;
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			bytes = baos.toByteArray();
		}
		return bytes;
	}

	/**
	 * Drawable transfer to bytes
	 * 
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToBytes(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		byte[] bytes = bitmapToBytes(bitmap);
		;
		return bytes;
	}

	/**
	 * Base64 to byte[] //
	 */
	// public static byte[] base64ToBytes(String base64) throws IOException {
	// byte[] bytes = Base64.decode(base64);
	// return bytes;
	// }
	//
	// /**
	// * Byte[] to base64
	// */
	// public static String bytesTobase64(byte[] bytes) {
	// String base64 = Base64.encode(bytes);
	// return base64;
	// }

	/**
	 * Create reflection images
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * Get rounded corner images
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            5 10
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Resize the bitmap
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * Resize the drawable
	 * 
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		matrix.postScale(sx, sy);
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * Get images from SD card by path and the name of image
	 * 
	 * @param photoName
	 * @return
	 */
	public static Bitmap getPhotoFromSDCard(String path, String photoName) {
		Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName + ".png");
		if (photoBitmap == null) {
			return null;
		} else {
			return photoBitmap;
		}
	}

	/**
	 * Check the SD card
	 * 
	 * @return
	 */
	public static boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * Get image from SD card by path and the name of image
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean findPhotoFromSDCard(String path, String photoName) {
		boolean flag = false;

		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (dir.exists()) {
				File folders = new File(path);
				File photoFile[] = folders.listFiles();
				for (int i = 0; i < photoFile.length; i++) {
					String fileName = photoFile[i].getName().split("\\.")[0];
					if (fileName.equals(photoName)) {
						flag = true;
					}
				}
			} else {
				flag = false;
			}
			// File file = new File(path + "/" + photoName + ".jpg" );
			// if (file.exists()) {
			// flag = true;
			// }else {
			// flag = false;
			// }

		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * Save image to the SD card
	 * 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File photoFile = new File(path, photoName + ".png");
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
						// fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Delete the image from SD card
	 * 
	 * @param context
	 * @param path
	 *            file:///sdcard/temp.jpg
	 */
	public static void deleteAllPhoto(String path) {
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}

	public static void deletePhotoAtPathAndName(String path, String fileName) {
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i].getName());
				if (files[i].getName().equals(fileName)) {
					files[i].delete();
				}
			}
		}
	}

	public boolean isFolderExists(String strFolder) {
		File file = new File(strFolder);
		if (!file.exists()) {
			if (file.mkdir()) {
				return true;
			} else
				return false;
		}
		return true;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap getBitmap(String imageFile) {
		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imageFile, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
		opts.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(imageFile, opts);
			// imageView.setImageBitmap(bmp);
		} catch (OutOfMemoryError err) {
		}
		return bmp;
	}

}
