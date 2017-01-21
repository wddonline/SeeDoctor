package org.wdd.app.android.seedoctor.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

/**
 * 图片缓存
 * @author richard wang
 *
 */
public class ImageCache implements com.android.volley.toolbox.ImageLoader.ImageCache {

	private static ImageCache cache;
	
	public static ImageCache instance(Context context) {
		if(cache == null) {
			synchronized (ImageCache.class) {
				if(cache == null) {
					cache = new ImageCache(context);
				}
			}
		}
		return cache;
	}
	
	private MemCache memCache;
	private DiskCache diskCache;

	private ImageCache(Context context) {
		memCache = new MemCache();
		diskCache = new DiskCache(context);
	}

	/**
	 * 从缓存中获取Bitmap
	 * 
	 * @param url
	 * @return bitmap
	 */
	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = null;
		if(TextUtils.isEmpty(url)) {
			return null;
		}
		bitmap = memCache.getBitmap(url);
		if (bitmap != null) {
			return bitmap;
		}
		if(!isExternalStorageAvaliable()) {
			return null;
		}
		bitmap = diskCache.getBitmap(url);
		return bitmap;
	}

	/**
	 * 添加Bitmap到内存缓存
	 * 
	 * @param url
	 * @param bitmap
	 */
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		memCache.putBitmap(url, bitmap);
		if(!isExternalStorageAvaliable()) {
			return;
		}
		diskCache.putBitmap(url, bitmap);
	}

	private boolean isExternalStorageAvaliable() {
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public void clear() {
		memCache.recycle();
	}

	public void removeBitmap(String key) {
		memCache.removeBitmap(key);
	}

	public long getDiskCacheSize() {
		return diskCache.getDiskCacheSize();
	}

	public void cleanDiskCache() {
		diskCache.clean();
	}
}
