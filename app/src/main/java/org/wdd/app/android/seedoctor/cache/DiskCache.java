package org.wdd.app.android.seedoctor.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DiskCache {

	private static final long DEFAULT_MAX_CACHE_SIZE = 419430400;//400 * 1024 * 1024
	private final long PURGE_CACHE_INTERVAL_TIME = 259200000;//1000 * 60 * 60 * 24 * 3

	private File imageCacheDir;
	private Context context;

	private long maxCacheSize;

	DiskCache(Context context, long maxCacheSize) {
		this.context = context;
		this.maxCacheSize = maxCacheSize;
		imageCacheDir = context.getExternalCacheDir();
	}

	DiskCache(Context context) {
		this(context, DEFAULT_MAX_CACHE_SIZE);
	}

	public Bitmap getBitmap(String url) {
		String filename;
		if(url.startsWith("file://")) {
			filename = url;
		} else {
			filename = CacheUtils.url2Filename(url);
		}
		File file = new File(imageCacheDir, filename);
		if(!file.exists()) {
			return null;
		}
		Bitmap bitmap = null;
		InputStream input = null;
		ByteArrayOutputStream output = null;
		try {
			input = new FileInputStream(file);
			output = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len;
			while ((len = input.read(buff)) != -1) {
				output.write(buff, 0, len);
			}
			buff = output.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	public void putBitmap(String url, Bitmap bitmap) {
		String filename = CacheUtils.url2Filename(url);
		purgeCacheFilesIfNeeded(bitmap.getRowBytes() * bitmap.getHeight());
		File file = new File(imageCacheDir, filename);
		if(file.exists()) {
			return;
		}
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, output);
			output.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 清除缓存目录
	 * @param neededSize 将要缓存的文件的大小
     */
	private void purgeCacheFilesIfNeeded(int neededSize) {
		long totalSize = getTotalSize();
		if (totalSize + neededSize < maxCacheSize) {
			return;
		}
		File[] files = imageCacheDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (System.currentTimeMillis() - pathname.lastModified() > PURGE_CACHE_INTERVAL_TIME) {//删除三天前的缓存文件
					return true;
				}
				return false;
			}
		});
		for (File file : files) {
			file.delete();
		}
	}

	/**
	 * 获取当前缓存目录文件大小
	 * @return
     */
	private long getTotalSize() {
		long totalSize = 0;
		File[] files = imageCacheDir.listFiles();
		for (File file : files) {
			totalSize += file.length();
		}
		return totalSize;
	}
}
