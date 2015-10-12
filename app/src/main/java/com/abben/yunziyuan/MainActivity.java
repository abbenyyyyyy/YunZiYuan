package com.abben.yunziyuan;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.abben.yunziyuan.model.Movie;
import com.abben.yunziyuan.view.FirstFragment;
import com.abben.yunziyuan.view.FourthFragment;
import com.abben.yunziyuan.view.SecondFragment;
import com.abben.yunziyuan.view.ThirdFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirstFragment mFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FourthFragment fourthFragment;
    private List<Movie> mMovies;
    private Movie mMovie;
    private RadioGroup mRadioGroup;
    private MyApplication myApplication;
    private Handler handler;
    private SearchView searchView;
    public static int page=1;//控制传入scollView的图片URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.getMyApplication();
        myApplication.addMovies();

        setContentView(R.layout.activity_main);
        FragmentManager fa = getFragmentManager();
        FragmentTransaction fat = fa.beginTransaction();
        mFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        fourthFragment = new FourthFragment();
        fat.replace(R.id.id_content, mFragment);
        fat.commit();

        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fmt = fm.beginTransaction();
                switch (checkedId){
                    case R.id.first_button:
                        MainActivity.page=1;
                        fmt.replace(R.id.id_content,mFragment).commit();
                        break;
                    case R.id.second_button:
                        MainActivity.page=2;
                        fmt.replace(R.id.id_content,secondFragment).commit();
                        break;
                    case R.id.third_button:
                        MainActivity.page=3;
                        fmt.replace(R.id.id_content,thirdFragment).commit();
                        break;
                    case R.id.fourth_button:
                        MainActivity.page=4;
                        fmt.replace(R.id.id_content,fourthFragment).commit();
                        break;
                }
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==1){
                    searchView.onActionViewCollapsed();
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchableInfo info= searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                Message message = new Message();
                message.arg1=1;
                handler.sendMessage(message);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                dialog();
                break;
            case R.id.clear_cache:
                Intent intent = new Intent(MainActivity.this,ClearCacheActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**弹出关于的窗口*/
    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

}
