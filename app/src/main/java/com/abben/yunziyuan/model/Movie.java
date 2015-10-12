package com.abben.yunziyuan.model;

import java.io.Serializable;

/**
 * 一个封装一部电影各种数据的类
 * @type
 * @movie_name
 * @imageUrl
 * @summaryOfMovie
 * @printscreen
 * @baiduyun
 * @yunPassword
 */
public class Movie implements Serializable{
    /**
     * 电影类型
     */
    public String type;
    /**
     * 电影名
     */
    public String movie_name;
    /**
     * 电影封面URL
     */
    public String imageUrls;
    /**
     * 电影简介
     */
    public String summaryOfMovie;
    /**
     * 电影截图URL
     */
    public String printscreen;
    /**
     * 电影百度云URL
     */
    public String baiduyun;
    /**
     * 电影百度云提取码
     */
    public String yunPassword;

    @Override
    public String toString() {
        return "Movie{" +
                "type='" + type + '\'' +
                ", movie_name='" + movie_name + '\'' +
                ", imageUrls='" + imageUrls + '\'' +
                ", summaryOfMovie='" + summaryOfMovie + '\'' +
                ", printscreen='" + printscreen + '\'' +
                ", baiduyun='" + baiduyun + '\'' +
                ", yunPassword='" + yunPassword + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getSummaryOfMovie() {
        return summaryOfMovie;
    }

    public void setSummaryOfMovie(String summaryOfMovie) {
        this.summaryOfMovie = summaryOfMovie;
    }

    public String getPrintscreen() {
        return printscreen;
    }

    public void setPrintscreen(String printscreen) {
        this.printscreen = printscreen;
    }

    public String getBaiduyun() {
        return baiduyun;
    }

    public void setBaiduyun(String baiduyun) {
        this.baiduyun = baiduyun;
    }

    public String getYunPassword() {
        return yunPassword;
    }

    public void setYunPassword(String yunPassword) {
        this.yunPassword = yunPassword;
    }
}
