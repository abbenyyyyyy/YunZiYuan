package com.abben.yunziyuan.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

import com.abben.yunziyuan.MyApplication;
import com.abben.yunziyuan.model.Movies;

import java.io.File;


/**实现图片的缓存管理*/
public class ImageLoader {
    private static LruCache<String,Bitmap> cache;
    private static ImageLoader mImageLoader;
    private static Movies allMovies;

    public ImageLoader(){

        int maxSize = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxSize/8;
        cache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        allMovies = MyApplication.getMyApplication().getMovies("所有电影");
    }

    /**实例化ImageLoader*/
    public  static ImageLoader getmImageLoader(){
        if(mImageLoader==null){
            mImageLoader = new ImageLoader();
        }
        return mImageLoader;
    }

    /**此方法在图片不变形的情况下获取到图片指定大小的缩略图
     * 即是压缩图片，防止内存溢出（OOM）
     * */
    public static Bitmap decodeSampledBitmapFromResource(String pathName,
                                                         int reqWidth) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用下面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth) {
        // 源图片的宽度
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > reqWidth) {
            // 计算出实际宽度和目标宽度的比率
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }
    /**获得文件存储目录的方法*/
    public static String  getImagePath(String imageUrl) {
        int index = 0;
        for(int i =0;i< allMovies.imageUrls.length;i++){
            if (imageUrl== allMovies.imageUrls[i]){
                index = i;
            }
        }
        String imageDir = Environment.getExternalStorageDirectory().getPath()+ File.separator +
                "YunZiYuan" + File.separator;
        File file = new File(imageDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String imageName = allMovies.movies_name[index] + ".jpg";
        String imagePath = imageDir + imageName;
        return imagePath;
    }



    /**将图片加入到缓存的方法*/
    public void addBitmapToCache(Bitmap bitmap,String url){
        if(getBitmapFromCache(url)==null){
            cache.put(url,bitmap);
        }
    }

    /**将图片从缓存取出的方法*/
    public Bitmap getBitmapFromCache(String url){
        return cache.get(url);
    }


}
