package com.abben.yunziyuan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abben.yunziyuan.controller.ImageLoader;
import com.abben.yunziyuan.model.Movie;
import com.abben.yunziyuan.model.Movies;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


class AllScrollView extends ScrollView implements View.OnTouchListener {

    /**每页要加载的图片数量*/
    public static final int PAGE_SIZE = 4;

    /**记录当前已加载到第几页*/
    private int page;

    /**每一列的宽度*/
    private int columnWidth;

    /**当前第一列的高度*/
    private int firstColumnHeight;

    /**当前第二列的高度*/
    private int secondColumnHeight;

    /**是否已加载过一次layout，这里onLayout中的初始化只需加载一次*/
    private boolean loadOnce;

    /**对图片进行管理的工具类*/
    private ImageLoader imageLoader;

    /**第一列的布局*/
    private LinearLayout firstColumn;

    /**第二列的布局*/
    private LinearLayout secondColumn;

    /**记录所有正在下载或等待下载的任务。*/
    private static Set<LoadImageTask> taskCollection;

    /**MyScrollView下的直接子布局。*/
    private static View scrollLayout;

    /**MyScrollView布局的高度。*/
    private static int scrollViewHeight;

    /**记录上垂直方向的滚动距离。*/
    private static int lastScrollY ;

    /**记录所有界面上的图片，用以可以随时控制对图片的释放。*/
    private List<ImageView> imageViewList = new ArrayList<ImageView>();

    /**记录是否已经到达一次底部*/
    private boolean mark ;

    /**记录电影封面数量*/
    private int imagelength;

    /**记录电影封面的Url*/
    private String[] imageurl;

    private MyApplication myApplication;

    private Movies thisMovies;


    /**在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。*/
    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            AllScrollView allScrollView = (AllScrollView) msg.obj;
            int scrollY = allScrollView.getScrollY();
            // 如果当前的滚动位置和上次相同，表示已停止滚动
            if (scrollY == lastScrollY) {
                // 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
                if (scrollViewHeight + scrollY >= scrollLayout.getHeight()
                        && taskCollection.isEmpty()) {
                    allScrollView.loadMoreImages();
                }

                allScrollView.loadMoreImages();
                allScrollView.checkVisibility();
            } else {
                Message message = new Message();
                message.obj = allScrollView;
                // 因为手指离开后滚屏还能进行一段距离，所以100毫秒后再次对滚动位置进行判断
                handler.sendMessageDelayed(message, 100);
                lastScrollY = scrollY;
            }
        };

    };

    /**
     * MyScrollView的构造函数。
     *
     * @param context
     * @param attrs
     */
    public AllScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageLoader = ImageLoader.getmImageLoader();
        taskCollection = new HashSet<LoadImageTask>();
        setOnTouchListener(this);
        mark = false;
        imageViewList.clear();
        firstColumnHeight=0;
        secondColumnHeight=0;
        loadOnce = false;
        lastScrollY = 0;
        page = 0;
        myApplication=MyApplication.getMyApplication();
        //判断Application里的全局变量是否被回收
        if(myApplication.getMovies("所有电影")==null){
            myApplication.addMovies();
        }

        switch (MainActivity.page) {
            case 1:
                thisMovies = myApplication.getMovies("所有电影");
                imagelength = thisMovies.imageUrls.length;
                imageurl = new String[imagelength];
                for (int i = 0; i < imagelength; i++) {
                    imageurl[i] = thisMovies.imageUrls[i];
                }
                break;
            case 2:
                thisMovies = myApplication.getMovies("欧美电影");
                imagelength = thisMovies.imageUrls.length;
                imageurl = new String[imagelength];
                for (int i = 0; i < imagelength; i++) {
                    imageurl[i] = thisMovies.imageUrls[i];
                }
                break;
            case 3:
                thisMovies = myApplication.getMovies("日韩电影");
                imagelength = thisMovies.imageUrls.length;
                imageurl = new String[imagelength];
                for (int i = 0; i < imagelength; i++) {
                    imageurl[i] = thisMovies.imageUrls[i];
                }
                break;
            case 4:
                thisMovies = myApplication.getMovies("国产电影");
                imagelength = thisMovies.imageUrls.length;
                imageurl = new String[imagelength];
                for (int i = 0; i < imagelength; i++) {
                    imageurl[i] = thisMovies.imageUrls[i];
                }
                break;
        }
    }

    /**
     * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed&&!loadOnce) {
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);
            loadOnce = true;
            switch (MainActivity.page) {
                case 1:
                    firstColumn = (LinearLayout) findViewById(R.id.first_column);
                    secondColumn = (LinearLayout) findViewById(R.id.second_column);
                    columnWidth = firstColumn.getWidth();
                    break;
                case 2:
                    firstColumn = (LinearLayout) findViewById(R.id.Second_first_column);
                    secondColumn = (LinearLayout) findViewById(R.id.Second_second_column);
                    columnWidth = firstColumn.getWidth();
                    break;
                case 3:
                    firstColumn = (LinearLayout) findViewById(R.id.Third_first_column);
                    secondColumn = (LinearLayout) findViewById(R.id.Third_second_column);
                    columnWidth = firstColumn.getWidth();
                    break;
                case 4:
                    firstColumn = (LinearLayout) findViewById(R.id.Fourth_first_column);
                    secondColumn = (LinearLayout) findViewById(R.id.Fourth_second_column);
                    columnWidth = firstColumn.getWidth();
                    break;
            }
            loadMoreImages();

        }
    }

    /**
     * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Message message = new Message();
            message.obj = this;
            handler.sendMessageDelayed(message, 5);
        }
        return false;
    }

    /**
     * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
     */
    public void loadMoreImages() {
        if (hasSDCard()) {
            if (!mark) {
                int startIndex = page * PAGE_SIZE;
                int endIndex = page * PAGE_SIZE + PAGE_SIZE;
                if (startIndex < imagelength) {
                    Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
                    if (endIndex > imagelength) {
                        endIndex = imagelength;
                    }
                    for (int i = startIndex; i < endIndex; i++) {
                        LoadImageTask task = new LoadImageTask();
                        taskCollection.add(task);
                        task.execute(imageurl[i]);
                    }
                    page++;
                } else {
                    if (firstColumnHeight!=0) {
                        Toast.makeText(getContext(), "已没有更多电影", Toast.LENGTH_SHORT).show();
                        mark = true;
                    }else{
                        Toast.makeText(getContext(), "网络不可用，请先设置网络。", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }else {
            Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
     */
    public void checkVisibility() {
        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            int borderTop = (Integer) imageView.getTag(R.string.border_top);
            int borderBottom = (Integer) imageView.getTag(R.string.border_bottom);
            if (borderBottom > getScrollY()
                    && borderTop < getScrollY() + scrollViewHeight) {
                String imageUrl = (String) imageView.getTag(R.string.image_url);
                Bitmap bitmap = imageLoader.getBitmapFromCache(imageUrl);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    LoadImageTask task = new LoadImageTask(imageView);
                    task.execute(imageUrl);
                }
            }
            else {
                imageView.setImageResource(R.mipmap.null_png);
            }
        }
    }

    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    private boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 异步下载图片的任务。
     *
     * @author guolin
     */
    class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * 图片的URL地址
         */
        private String mImageUrl;

        /**
         * 可重复使用的ImageView
         */
        private ImageView mImageView;

        public LoadImageTask() {
        }

        /**
         * 将可重复使用的ImageView传入
         *
         * @param imageView
         */
        public LoadImageTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            mImageUrl = params[0];
            Bitmap imageBitmap = imageLoader.getBitmapFromCache(mImageUrl);
            if (imageBitmap == null) {
                imageBitmap = loadImage(mImageUrl);
            }
            return imageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                double ratio = bitmap.getWidth() / (columnWidth * 1.0);
                int scaledHeight = (int) (bitmap.getHeight() / ratio);
                addImage(bitmap, columnWidth, scaledHeight);
            }
            taskCollection.remove(this);
        }

        /**
         * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
         *
         * @param imageUrl
         *            图片的URL地址
         * @return 加载到内存的图片。
         */
        private Bitmap loadImage(String imageUrl) {
            File imageFile = new File(imageLoader.getImagePath(imageUrl));
            if (!imageFile.exists()) {
                downloadImage(imageUrl);
            }
            if (imageUrl != null) {
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(
                        imageFile.getPath(), columnWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToCache(bitmap,imageUrl);
                    return bitmap;
                }
            }
            return null;
        }

        /**
         * 向ImageView中添加一张图片
         *
         * @param bitmap
         *            待添加的图片
         * @param imageWidth
         *            图片的宽度
         * @param imageHeight
         *            图片的高度
         */
        private void addImage(final Bitmap bitmap, int imageWidth, int imageHeight) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    imageWidth, imageHeight);
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            } else {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(5, 10, 5, 5);
                imageView.setTag(R.string.image_url, mImageUrl);


                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        imageWidth,70);
                TextView textView = new TextView(getContext());
                textView.setLayoutParams(textParams);

                final Movie this_movie = new Movie();
                for(int i =0;i< thisMovies.imageUrls.length;i++){
                    if (mImageUrl== thisMovies.imageUrls[i]){
                        this_movie.setType(thisMovies.movies_type[i]);
                        this_movie.setMovie_name(thisMovies.movies_name[i]);
                        this_movie.setImageUrls(mImageUrl);
                        this_movie.setSummaryOfMovie(thisMovies.movies_summary[i]);
                        this_movie.setBaiduyun(thisMovies.movies_baiduyun[i]);
                        this_movie.setYunPassword(thisMovies.movies_yunPassword[i]);
                        this_movie.setPrintscreen(thisMovies.movies_printscreen[i]);
                    }
                }
                textView.setText(this_movie.getMovie_name());
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setGravity(Gravity.CENTER);

                findColumnToAdd(textView, imageView, imageHeight);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), MovieActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("movie",this_movie);
                        intent.putExtras(bundle);
                        getContext().startActivity(intent);
                    }
                });
                imageViewList.add(imageView);
            }
        }

        /**
         * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
         * Border标签是imageview的所处scorllview的顶部高度和底部高度
         * @param imageView
         * @param imageHeight
         * @return 应该添加图片的一列
         */
        private boolean findColumnToAdd(TextView textView,ImageView imageView,
                                             int imageHeight) {
            if (firstColumnHeight <= secondColumnHeight) {
                    imageView.setTag(R.string.border_top, firstColumnHeight);
                    firstColumnHeight += 70;
                    firstColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, firstColumnHeight);
                    firstColumn.addView(imageView);
                    firstColumn.addView(textView);
                    return true;
            }
            else {
                    imageView.setTag(R.string.border_top, secondColumnHeight);
                    secondColumnHeight += 70;
                    secondColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, secondColumnHeight);
                    secondColumn.addView(imageView);
                    secondColumn.addView(textView);
                    return true;
            }
        }

        /**
         * 将图片下载到SD卡缓存起来。
         *
         * @param imageUrl
         *            图片的URL地址。
         */
        private void downloadImage(String imageUrl) {
            FileOutputStream fos = null;
            File imageFile = null;
            try {
                URL url= new URL(imageUrl);
                // 打开URL 对应的资源输入流
                InputStream is= url.openStream();
                // 把InputStream 转化成ByteArrayOutputStream
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte [] buffer = new byte [1024];
                int len;
                while ((len = is.read(buffer)) > -1 ) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                is.close(); // 关闭输入流
                // 将ByteArrayOutputStream 转化成InputStream
                /**将 URL 获取的资源输入流转换成了 ByteArrayInputStream ，当需要使用输入流时，
                 * 再将 ByteArrayInputStream 转换成输入流即可 。这样就可以做到一次访问网络资源多次使用的目的，
                 * 从而避免了客户端不必要的流量开支。*/
                is = new ByteArrayInputStream(baos.toByteArray());
                // 再次将ByteArrayOutputStream 转化成InputStream
                is= new ByteArrayInputStream(baos.toByteArray());
                baos.close();
                // 打开手机文件对应的输出流
                imageFile = new File(imageLoader.getImagePath(imageUrl));
                fos = new FileOutputStream(imageFile);
                byte []buff= new byte [1024];
                int count;
                // 将URL 对应的资源下载到本地
                while ((count=is.read(buff))>0) {
                    fos.write(buff, 0, count);
                }
                fos.flush();
                // 关闭输入输出流
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }if (imageFile != null) {
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(
                        imageFile.getPath(), columnWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToCache(bitmap, imageUrl);
                }
            }
        }
    }
    /**
     * 改写的ScrollView是实现瀑布流照片墙的核心类，这里我来重点给大家介绍一下。首先它是继承自ScrollView的，这样就允许用户
     * 可以通过滚动的方式来浏览更多的图片。这里提供了一个loadMoreImages()方法，是专门用于加载下一页的图片的，因此在
     * onLayout()方法中我们要先调用一次这个方法，以初始化第一页的图片。然后在onTouch方法中每当监听到手指离开屏幕的事
     * 件，就会通过一个handler来对当前ScrollView的滚动状态进行判断，如果发现已经滚动到了最底部，就会再次调用
     * loadMoreImages()方法去加载下一页的图片。那我们就要来看一看loadMoreImages()方法的内部细节了。在这个方法中，
     * 使用了一个循环来加载这一页中的每一张图片，每次都会开启一个LoadImageTask，用于对图片进行异步加载。
     * 然后在LoadImageTask中，首先会先检查一下这张图片是不是已经存在于SD卡中了，如果还没存在，就从网络上下载，
     * 然后把这张图片存放在LruCache中。接着将这张图按照一定的比例进行压缩，并找出当前高度最小的一列，把压缩后的图片
     * 添加进去就可以了。
     * 另外，为了保证照片墙上的图片都能够合适地被回收，这里还加入了一个可见性检查的方法，即checkVisibility()方法。
     * 这个方法的核心思想就是检查目前照片墙上的所有图片，判断出哪些是可见的，哪些是不可见。然后将那些不可见的图片都替换
     * 成一张空图，这样就可以保证程序始终不会占用过高的内存。当这些图片又重新变为可见的时候，只需要再从LruCache中将这些
     * 图片重新取出即可。如果某张图片已经从LruCache中被移除了，就会开启一个LoadImageTask，将这张图片重新加载到内存中。
     */
}