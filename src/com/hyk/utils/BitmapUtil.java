package com.hyk.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * @ClassName: BitmapUtil
 * @Description: TODO(图片处理)
 * @author linhaishi
 * @date 2013-9-6 下午3:18:28
 * 
 */
public class BitmapUtil {

	// bitmap 图片的缩小
	@SuppressWarnings("deprecation")
	public static Drawable resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(resizedBitmap);

	}

	// // bitmap 图片的压缩
	// public static Bitmap compressImage(Bitmap image) {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// image.compress(Bitmap.CompressFormat.JPEG, 10, baos);//
	// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
	// ByteArrayInputStream isBm = new
	// ByteArrayInputStream(baos.toByteArray());//
	// 把压缩后的数据baos存放到ByteArrayInputStream中
	// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
	// 把ByteArrayInputStream数据生成图片
	// return bitmap;
	// }

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
			if(options<=0){
				break;
			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
}
