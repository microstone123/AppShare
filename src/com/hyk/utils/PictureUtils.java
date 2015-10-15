package com.hyk.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;

/**
 * @author BigWanng
 * @version 2013-6-6 下午1:55:33 TODO 处理图片压缩的工具类
 */
public class PictureUtils {
	private final static int compressVal = 75;

	/**
	 * 把bitmap转换成String 将图片保存到本地
	 * 
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, compressVal, baos);

		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/**
	 * 计算图片的缩放值 如果图片的原始高度或者宽度大与我们期望的宽度和高度，我们需要计算出缩放比例的数值。否则就不缩放。
	 * heightRatio是图片原始高度与压缩后高度的倍数， widthRatio是图片原始宽度与压缩后宽度的倍数。
	 * inSampleSize就是缩放值 ，取heightRatio与widthRatio中最小的值。
	 * inSampleSize为1表示宽度和高度不缩放，为2表示压缩后的宽度与高度为原来的1/2(图片为原1/4)。
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions(尺寸) larger than or equal to
			// the requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 根据路径获得图片并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int w, int h) {
		final BitmapFactory.Options options = new BitmapFactory.Options();

		// 该值设为true那么将不返回实际的bitmap不给其分配内存空间而里面只包括一些解码边界信息即图片大小信息
		options.inJustDecodeBounds = true;// inJustDecodeBounds设置为true，可以不把图片读到内存中,但依然可以计算出图片的大小
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, w, h);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;// 重新读入图片，注意这次要把options.inJustDecodeBounds
											// 设为 false
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);// BitmapFactory.decodeFile()按指定大小取得图片缩略图

		return bitmap;
	}

	/**
	 * 保存到本地
	 * 
	 * @param bitmap
	 */
	public static void saveBitmap(Bitmap bitmap, String savePath) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(savePath);
		try {
			file.createNewFile();
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));

			bitmap.compress(Bitmap.CompressFormat.JPEG, compressVal, baos);
			os.write(baos.toByteArray());

			os.flush();
			os.close();

		} catch (IOException e) {
			Log.d("BITMAP", e.getMessage());
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
	}

	/***
	 * 根据路径删除图片
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteTempFile(String path) throws IOException {
		boolean isOk = true;
		File file = new File(path);
		if (file != null) {
			if (file.exists()) {
				if (!file.delete()) {
					isOk = false;
				}
			}
		}
		return isOk;
	}

	/***
	 * 根据路径删除图片
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static void deleteFile(File file) {
		try {
			if (file != null) {
				if (file.exists()) {
					file.delete();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/***
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return 返回文件扩展名
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	public static Bitmap comp(Bitmap image, int hh, int ww) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		// float hh = 800f;//这里设置高度为800f
		// float ww = 480f;//这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap,0);// 压缩好比例大小后再进行质量压缩
	}

	public static Bitmap getimage(String srcPath, int hh, int ww) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		// float hh = 800f;//这里设置高度为800f
		// float ww = 480f;//这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		int degree = getBitmapDegree(srcPath);
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if(bitmap==null){
			return null;
		}
		return compressImage(bitmap,degree);// 压缩好比例大小后再进行质量压缩
		// return bitmap;
	}

	public static Bitmap compressImage(Bitmap image,int degree) {
		Bitmap bitmap = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 90;
			while (baos.toByteArray().length / 1024 > 80) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;// 每次都减少10
				if (options < 0) {
					break;
				}
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			if(degree!=0){
				bitmap = rotateBitmapByDegree(bitmap,degree);
			}
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		return bitmap;
	}

	// public static void deleteFile(Context context,File file) {
	// if (file.exists()) { // 判断文件是否存在
	// if (file.isFile()) { // 判断是否是文件
	// file.delete(); // delete()方法 你应该知道 是删除的意思;
	// } else if (file.isDirectory()) { // 否则如果它是一个目录
	// File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
	// for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
	// context.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
	// }
	// }
	// file.delete();
	// }
	// }

	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

}
