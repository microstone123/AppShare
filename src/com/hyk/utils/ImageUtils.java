package com.hyk.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

public class ImageUtils {
	
	private static final String TAG = "ImageUtils";
	private static final boolean DEBUG = false;
	
	// 获得圆形图片
	public static Bitmap circleBitmap(Bitmap bitmapimg) {
		Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
	}
	
	 // 获得圆角图片的方法
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    // 获得带倒影的图片方法
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }
    
    /**
     * 获取图片
     */
    public static Bitmap getBitmap(String path){
    	Bitmap imageBitmap = BitmapFactory.decodeFile(path);
    	return imageBitmap;
    }

    /**
     * 保存图片成JPEG格式
     */
	public static boolean saveBitmap(Context context, String path, Bitmap bmp) {
		return saveBitmap(context, new File(path), bmp);
	}
	
	public static boolean saveBitmap(Context context, File file, Bitmap bmp) {
		try {
			if (!file.exists()) {
				File p = file.getParentFile();
				if (p != null) {
					p.mkdirs();
				}
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			// 100 means no compression
			boolean isCompress = bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			
			context.getContentResolver().notifyChange(Uri.fromFile(file), null);
			
			return isCompress;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	public static void notifySdMounted(Context context) { 
		Uri sdCardUri = Uri.fromFile(Environment.getExternalStorageDirectory()); 
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, sdCardUri));
	}
	*/
	
	// 添加到gallery
	/*
	 * When you create a photo through an intent, 
	 * you should know where your image is located, 
	 * because you said where to save it in the first place. 
	 * For everyone else, perhaps the easiest way to make 
	 * your photo accessible is to make it accessible 
	 * from the system's Media Provider.
	 * The following example method demonstrates 
	 * how to invoke the system's media scanner to add 
	 * your photo to the Media Provider's database, 
	 * making it available in the Android Gallery application and to other apps.
	 */
	/*
	public static void galleryAddPic(Context context, String photoPath) {
	   galleryAddPic(context, new File(photoPath));
	}
	
	public static void galleryAddPic(Context context, File photoFile) {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri contentUri = Uri.fromFile(photoFile);
	    mediaScanIntent.setData(contentUri);
	    context.sendBroadcast(mediaScanIntent);
	}
	*/
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);
	    
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledBitmapFromFilePath(Context context, String path, int reqWidth, int reqHeight){
		if (reqWidth == 0 && reqHeight == 0) {
			return null;
		}
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		computeSize(options, reqWidth, reqHeight);
		
	    options.inJustDecodeBounds = false;
	    Bitmap sampleBimap = BitmapFactory.decodeFile(path, options);
	    
	    return sampleBimap;
	}
	
	public static Bitmap decodeSampleBitmapFromUri(Context context, Uri uri, int reqWidth, int reqHeight) throws IOException{
		if (reqWidth == 0 && reqHeight == 0) {
			return null;
		}
		
		InputStream input = context.getContentResolver().openInputStream(uri);
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(input, null, options);
		input.close();
		
		computeSize(options, reqWidth, reqHeight);
	    
	    input = context.getContentResolver().openInputStream(uri);
	    options.inJustDecodeBounds = false;
	    Bitmap sampleBimap = BitmapFactory.decodeStream(input, null, options);
	    input.close();
	    return sampleBimap;
	}
	
	private static void computeSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int newWidth = reqWidth;
		int newHeight = reqHeight;
		if (reqWidth != reqHeight) {
			final int bitmapHeight = options.outHeight;
		    final int bitmapWidth = options.outWidth;
			
			if (reqHeight == 0) {
				float scaleWidht = ((float) reqWidth / bitmapWidth);
				newHeight = (int) (bitmapHeight * scaleWidht);
			}
			
			if (reqWidth == 0) {
				float scaleHeight = ((float) reqHeight / bitmapHeight);
				newWidth = (int) (bitmapWidth * scaleHeight);
			}
		}
		options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
		if (DEBUG) Log.i(TAG, "原宽高:" + options.outWidth + "*" + options.outHeight 
	    		+ " 压缩比例:" + options.inSampleSize
	    		+ " 压缩后宽高:" + newWidth + "*" + newHeight);
	}
	
	public static Bitmap resizeBitmap(Context context, Uri uri, int reqWidth, int reqHeight) throws IOException {
		if (reqWidth == 0 && reqHeight == 0) {
			return null;
		}
		
		Bitmap bmp = decodeSampleBitmapFromUri(context, uri, reqWidth, reqHeight);
		
		int bitmapWidth = bmp.getWidth();
		int bitmapHeight = bmp.getHeight();
		
		float scaleWidth = 0;
		float scaleHeight = 0;
		
		scaleWidth = ((float) reqWidth) / bitmapWidth;
		scaleHeight = ((float) reqHeight) / bitmapHeight;
		
		if (reqHeight == 0) {
			reqHeight = (int) (bitmapHeight * scaleWidth); 
			scaleHeight = ((float) reqHeight) / bitmapHeight; 
		} 
		
		if (reqWidth == 0) {
			reqWidth = (int) (bitmapWidth * scaleHeight);
			scaleWidth = ((float) reqWidth) / bitmapWidth;
		}
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		
		Bitmap resizeBmp = null;
		if (bmp != null) {
			resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
		}
		bmp.recycle();
		
		if (DEBUG) Log.d(TAG, "原大小:" + bitmapWidth + "*" + bitmapHeight +
				" 改变大小后宽高:" + resizeBmp.getWidth() + "*" + resizeBmp.getHeight());
		
		return resizeBmp;
	}
	
	/** 获取ImageView的图片原大小
	 * @return int[0] = height, int[1] = width*/
	public static int[] getImageViewBitmapHeightAndWidth(ImageView imageView) {
		BitmapDrawable bd = (BitmapDrawable)imageView.getDrawable();
		Bitmap bitmap = bd.getBitmap();
		final int height = bitmap.getHeight();
		final int width = bitmap.getWidth();
		
		return new int[]{height, width};
	}
	
	// Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotate(Bitmap b, int degrees) {
        return rotateAndMirror(b, degrees, false);
    }

    // Rotates and/or mirrors the bitmap. If a new bitmap is created, the
    // original bitmap is recycled.
    public static Bitmap rotateAndMirror(Bitmap b, int degrees, boolean mirror) {
        if ((degrees != 0 || mirror) && b != null) {
            Matrix m = new Matrix();
            // Mirror first.
            // horizontal flip + rotation = -rotation + horizontal flip
            if (mirror) {
                m.postScale(-1, 1);
                degrees = (degrees + 360) % 360;
                if (degrees == 0 || degrees == 180) {
                    m.postTranslate(b.getWidth(), 0);
                } else if (degrees == 90 || degrees == 270) {
                    m.postTranslate(b.getHeight(), 0);
                } else {
                    throw new IllegalArgumentException("Invalid degrees=" + degrees);
                }
            }
            if (degrees != 0) {
                // clockwise
                m.postRotate(degrees,
                        (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            }

            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }
    /** 
     * 获取视频的缩略图 
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。 
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。 
     * @param videoPath 视频的路径 
     * @param width 指定输出视频缩略图的宽度 
     * @param height 指定输出视频缩略图的高度度 
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96 
     * @return 指定大小的视频缩略图 
     *  videoThumbnail.setImageBitmap(getVideoThumbnail(videoPath, 100, 100,  
	                MediaStore.Images.Thumbnails.MICRO_KIND));  
     */  
	private Bitmap getVideoThumbnail(String videoPath, int width, int height,  
            int kind) {  
        Bitmap bitmap = null;  
        // 获取视频的缩略图  
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  
}
