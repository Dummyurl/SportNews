package com.kunleen.sn.sportnewsapplication.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.EmptySignature;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;

/**
 * Glide缓存工具类
 * Created by Trojx on 2016/10/10 0010.
 */

public class GlideCacheUtil {
    private static GlideCacheUtil inst;

    public static GlideCacheUtil getInstance() {
        if (inst == null) {
            inst = new GlideCacheUtil();
        }
        return inst;
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
// BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void LoadImageInWifi(Activity activity, ImageView imageView, String url) {
        if (!SNApplication.userInfo.isWifiLoadImg() || SNApplication.WIFIState) {
            if (!activity.isDestroyed()) {
                Glide.with(activity).load(url).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1)).into(imageView);
            } else {
                Logger.t("GlideUtils").i("Picture loading failed,activity is Destroyed");
            }
        } else {
            loadCacheImage(activity, url, imageView);
        }
    }

    public static void LoadImage(Context context, ImageView imageView, String url, boolean isHead) {
        if (isHead) {
            Glide.with(context).load(url).apply(new RequestOptions().placeholder(R.mipmap.img_loading1).error(R.mipmap.wd_touxiang)).into(imageView);
        } else {
            Glide.with(context).load(url).apply(new RequestOptions().placeholder(R.mipmap.img_loading1)).into(imageView);
        }
    }

    public static void LoadImage(Context context, ImageView imageView, Uri uri, boolean isHead) {
        if (isHead) {
            Glide.with(context).load(uri).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1).error(R.mipmap.wd_touxiang)).into(imageView);
        } else {
            Glide.with(context).load(uri).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1)).into(imageView);
        }
    }

    private static void loadCacheImage(Context context, String url, ImageView imageView) {
        // 寻找缓存图片
        File file = DiskLruCacheWrapper.get(Glide.getPhotoCacheDir(context), 250 * 1024 * 1024).get(new OriginalKey(url, EmptySignature.obtain()));
        if (file != null) {
            Glide.with(context).load(url).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1).diskCacheStrategy(DiskCacheStrategy.RESOURCE).skipMemoryCache(false)).into(imageView);
        } else {
            imageView.setImageResource(R.mipmap.img_loading1);
        }
    }


    /**
     * Glide原图缓存Key
     */
    private static class OriginalKey implements Key {

        private final String id;
        private final Key signature;

        private OriginalKey(String id, Key signature) {
            this.id = id;
            this.signature = signature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            OriginalKey that = (OriginalKey) o;

            return id.equals(that.id) && signature.equals(that.signature);
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + signature.hashCode();
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            try {
                messageDigest.update(id.getBytes(STRING_CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            signature.updateDiskCacheKey(messageDigest);
        }
    }

}
