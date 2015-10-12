package com.abben.yunziyuan;

import android.app.Application;
import android.os.Environment;
import android.support.v4.util.ArrayMap;

import com.abben.yunziyuan.controller.XmlParser;
import com.abben.yunziyuan.model.Movie;
import com.abben.yunziyuan.model.Movies;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by abbenyyyyyy on 2015/10/3.
 */
public class MyApplication extends Application {

    private static Map<String,Movies> moviesMap;
    private static MyApplication myApplication = null;
    private List<Movie> movies;

    @Override
    public void onCreate() {
        moviesMap = new ArrayMap<>();
        super.onCreate();
        myApplication=this;
    }

    /**通过此方法获得MyApplication实例*/
    public static MyApplication getMyApplication(){
        return myApplication;
    }

    public void addMovies(){
        movies = new ArrayList<Movie>();
        String thisDir = Environment.getExternalStorageDirectory().getPath() + File.separator +
                "YunZiYuan" + File.separator;
        String xmlPath = thisDir + "resources.xml";
        File xmlFlie = new File(xmlPath);
        try {
            InputStream inputStream = new FileInputStream(xmlFlie);
            movies = XmlParser.getMovies(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Movies first_movies = new Movies();
        Movies second_movies = new Movies();
        Movies third_movies = new Movies();
        Movies fourth_movies = new Movies();

        first_movies.init(movies, "所有电影");
        moviesMap.put("所有电影", first_movies);
        second_movies.init(movies, "欧美电影");
        moviesMap.put("欧美电影", second_movies);
        third_movies.init(movies, "日韩电影");
        moviesMap.put("日韩电影", third_movies);
        fourth_movies.init(movies, "国产电影");
        moviesMap.put("国产电影", fourth_movies);

        //置空等待回收
        first_movies=null;
        second_movies=null;
        third_movies=null;
        fourth_movies=null;
    }

    public Movies getMovies(String key){
        return moviesMap.get(key);
    }

}
