package com.abben.yunziyuan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.abben.yunziyuan.model.Movies;

import java.io.File;

/**
 * Created by abbenyyyyyy on 2015/10/6.
 */
public class ClearCacheActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup radioGroup;
    private ProgressDialog progressDialog;
    private MyApplication myApplication;
    private Movies movies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_cache);
        myApplication = MyApplication.getMyApplication();
        movies = myApplication.getMovies("所有电影");
        radioGroup = (RadioGroup) findViewById(R.id.clear_cache_radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==1){
                progressDialog.dismiss();
                dailog();
            }
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.clear_cache_determine:
                clera_progressdialog();
                break;
            case R.id.clear_cache_cancel:
                ClearCacheActivity.this.finish();
                break;
        }
    }

    private void clera_progressdialog(){
        progressDialog = new ProgressDialog(ClearCacheActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("正在清除缓存");

        progressDialog.show();


        new Thread() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator
                        +"YunZiYuan";
                File file = new File(path);
                delete(file);
                Message message = new Message();
                message.arg1 = 1;
                handler.sendMessage(message);
            }
        }.start();
    }

    /**使用递归来删除整个文件夹*/
    private void delete(File file){
        if (file.isFile()) {
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**弹出提示完成删除缓存，点击确定后返回桌面*/
    private void dailog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ClearCacheActivity.this);
        builder.setTitle("提示");
        builder.setMessage("已完成删除缓存，点击确定后返回桌面，再次打开软件将重新下载图片。");
        builder.setPositiveButton(getString(R.string.determine), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                ClearCacheActivity.this.finish();
            }
        });
        builder.create().show();
    }

}
