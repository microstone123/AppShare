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
 * @Description: TODO(ͼƬ����)
 * @author linhaishi
 * @date 2013-9-6 ����3:18:28
 * 
 */
public class BitmapUtil {

	// bitmap ͼƬ����С
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

	// // bitmap ͼƬ��ѹ��
	// public static Bitmap compressImage(Bitmap image) {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// image.compress(Bitmap.CompressFormat.JPEG, 10, baos);//
	// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
	// ByteArrayInputStream isBm = new
	// ByteArrayInputStream(baos.toByteArray());//
	// ��ѹ���������baos��ŵ�ByteArrayInputStream��
	// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
	// ��ByteArrayInputStream��������ͼƬ
	// return bitmap;
	// }

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
			if(options<=0){
				break;
			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		return bitmap;
	}
}
