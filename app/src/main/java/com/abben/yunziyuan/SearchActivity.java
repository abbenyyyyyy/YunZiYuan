package com.abben.yunziyuan;



import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.abben.yunziyuan.model.Movie;
import com.abben.yunziyuan.model.Movies;

import java.util.ArrayList;

/**
 * Created by abbenyyyyyy on 2015/10/11.
 */
public class SearchActivity extends AppCompatActivity{
    private MyApplication myApplication;
    private Movies thisMovies;
    private Object[] moviesName;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        listView = (ListView) findViewById(R.id.auto_search);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        myApplication = MyApplication.getMyApplication();
        thisMovies = myApplication.getMovies("所有电影");
        Object[] objs = initName(thisMovies);
        updateLayout(objs);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arrayList.get(position)!=null){
                    final Movie this_movie = new Movie();
                    for(int i =0;i< thisMovies.movies_name.length;i++){
                        if (arrayList.get(position)== thisMovies.movies_name[i]){
                            this_movie.setType(thisMovies.movies_type[i]);
                            this_movie.setMovie_name(thisMovies.movies_name[i]);
                            this_movie.setImageUrls(thisMovies.imageUrls[i]);
                            this_movie.setSummaryOfMovie(thisMovies.movies_summary[i]);
                            this_movie.setBaiduyun(thisMovies.movies_baiduyun[i]);
                            this_movie.setYunPassword(thisMovies.movies_yunPassword[i]);
                            this_movie.setPrintscreen(thisMovies.movies_printscreen[i]);
                        }
                    }
                    Intent intent = new Intent(SearchActivity.this, MovieActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie",this_movie);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search2).getActionView();
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Object[] obj = searchItem(newText);
                updateLayout(obj);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                SearchActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**初始化匹配的电影名*/
    private Object[] initName(Movies movies){
        for (int i=0;i<movies.movies_name.length;i++) {
            arrayList.add(movies.movies_name[i]);
        }
        return arrayList.toArray();
    }

    /**查询是否有与输入字符匹配的电影名字*/
    private Object[] searchItem(String name){
        ArrayList<String> this_list = new ArrayList<String>();
        for(int i=0;i<arrayList.size();i++) {
            int index = arrayList.get(i).indexOf(name);
            if (index != -1) {
                this_list.add(arrayList.get(i));
            }
        }
        return this_list.toArray();
    }

    /**更新ListView*/
    private void updateLayout(Object[] objects){
        listView.setAdapter(new ArrayAdapter<Object>(SearchActivity.this,android.R.layout.simple_expandable_list_item_1
        ,objects));
    }
}
