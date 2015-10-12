package com.abben.yunziyuan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abben.yunziyuan.model.Movie;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abbenyyyyyy on 2015/9/20.
 */
public class MovieActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView name_view,type_view,summary_view,pw_view;
    private ImageView movie_view,printscreen_view;
    private Button baiduyun_button,printscreen_button;
    private Movie movie;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_fragment);
        name_view = (TextView) findViewById(R.id.name);
        type_view = (TextView) findViewById(R.id.type);
        summary_view = (TextView) findViewById(R.id.summaryOfMovie);
        pw_view = (TextView) findViewById(R.id.yunPassword);
        movie_view = (ImageView) findViewById(R.id.imageOfMovie);
        printscreen_view = (ImageView) findViewById(R.id.printscreen);
        baiduyun_button = (Button) findViewById(R.id.baiDuYun);
        printscreen_button = (Button) findViewById(R.id.printscreenOfVisibity);

        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        movie = (Movie) intent.getSerializableExtra("movie");
        String imageDir = Environment.getExternalStorageDirectory().getPath()+File.separator +
                "YunZiYuan" + File.separator;
        String imageName = movie.getMovie_name() + ".jpg";
        String imagePath = imageDir + imageName;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        name_view.setText(movie.getMovie_name());
        type_view.setText(movie.getType());
        summary_view.setText(movie.getSummaryOfMovie());
        pw_view.setText(movie.getYunPassword());
        movie_view.setImageBitmap(bitmap);
        baiduyun_button.setOnClickListener(this);
        printscreen_button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                dialog();
                break;

            case R.id.clear_cache:
                Intent intent = new Intent(MovieActivity.this,ClearCacheActivity.class);
                startActivity(intent);
                break;

            case android.R.id.home:
                MovieActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.baiDuYun:
                Uri baiduyunUrl = Uri.parse(movie.getBaiduyun());
                Intent intent = new Intent(Intent.ACTION_VIEW,baiduyunUrl);
                startActivity(intent);
                break;

            case R.id.printscreenOfVisibity:
                LoadPrintscreen loadPrintscreen = new LoadPrintscreen();
                loadPrintscreen.execute(movie.getPrintscreen());
                printscreen_view.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**弹出关于的窗口*/
    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MovieActivity.this);
        builder.setTitle(getString(R.string.about));
        builder.setMessage(getString(R.string.detail_about));
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    /**
     * 下载电影截图的异步方法
     */
    class LoadPrintscreen extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap;
            InputStream is;
            try {
                URL mUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
                is = new BufferedInputStream(connection.getInputStream());
                bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            printscreen_view.setImageBitmap(bitmap);
        }
    }
}
