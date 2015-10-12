package com.abben.yunziyuan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.abben.yunziyuan.controller.XmlParser;


public class OpeningActivity extends AppCompatActivity {
    public ActionBar openActionBar;
    private XmlParser xmlParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening);
        openActionBar = getSupportActionBar();
        openActionBar.hide();
        /**启动更新xml的异步进程*/
        xmlParser = XmlParser.getXmlparser();
        xmlParser.updataXml(this.getApplicationContext());

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(OpeningActivity.this, MainActivity.class);
                startActivity(intent);
                OpeningActivity.this.finish();
            }
        }.start();
    }
}
