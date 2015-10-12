package com.abben.yunziyuan.model;

import com.abben.yunziyuan.model.Movie;

import java.util.List;

/**存放一个类型数据的类*/
public class Movies {
    /**电影类型数组*/
    public String[] movies_type;
    /**电影封面的URL数组*/
    public String[] imageUrls ;
    /**电影名字的数组*/
    public String[] movies_name;
    /**
     * 电影简介数组
     */
    public String[] movies_summary;
    /**
     * 电影截图URL数组
     */
    public String[] movies_printscreen;
    /**
     * 电影百度云URL数组
     */
    public String[] movies_baiduyun;

    /**
     * 电影百度云提取码数组
     */

    public String[] movies_yunPassword;

    /**电影数据数组的初始化函数*/
    public void init(List<Movie> movies,String type) {
        int size = 0;
        if (type.equals("所有电影")) {
            size = movies.size();
            imageUrls = new String[size];
            movies_name = new String[size];
            movies_type = new String[size];
            movies_summary = new String[size];
            movies_printscreen = new String[size];
            movies_baiduyun = new String[size];
            movies_yunPassword = new String[size];
            for (int i = 0; i < movies.size(); i++) {
                movies_type[i] = movies.get(i).getType();
                imageUrls[i] = movies.get(i).getImageUrls();
                movies_name[i] = movies.get(i).getMovie_name();
                movies_summary[i] = movies.get(i).getSummaryOfMovie();
                movies_printscreen[i] = movies.get(i).getPrintscreen();
                movies_baiduyun[i] = movies.get(i).getBaiduyun();
                movies_yunPassword[i] = movies.get(i).getYunPassword();
            }
        }else{
                for (int i = 0; i < movies.size(); i++) {
                    if ((movies.get(i).getType()).equals(type)) {
                        size++;
                    }
                }

                imageUrls = new String[size];
                movies_name = new String[size];
                movies_type = new String[size];
                movies_summary = new String[size];
                movies_printscreen = new String[size];
                movies_baiduyun = new String[size];
                movies_yunPassword = new String[size];
                int j = 0;
                while (j < size) {
                    for (int i = 0; i < movies.size(); i++) {
                        if ((movies.get(i).getType()).equals(type)) {
                            movies_type[j] = movies.get(i).getType();
                            imageUrls[j] = movies.get(i).getImageUrls();
                            movies_name[j] = movies.get(i).getMovie_name();
                            movies_summary[j] = movies.get(i).getSummaryOfMovie();
                            movies_printscreen[j] = movies.get(i).getPrintscreen();
                            movies_baiduyun[j] = movies.get(i).getBaiduyun();
                            movies_yunPassword[j] = movies.get(i).getYunPassword();
                            j++;
                        }
                    }
                }
            }
        }
}
