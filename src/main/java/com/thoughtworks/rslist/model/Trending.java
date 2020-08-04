package com.thoughtworks.rslist.model;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/4 20:17
 * @Description ***
 **/
public class Trending {

    private Integer id;

    private String trendingName;

    private String keyWord;

    public Trending() {
    }

    public Trending(Integer id, String trendingName, String keyWord) {
        this.id = id;
        this.trendingName = trendingName;
        this.keyWord = keyWord;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrendingName() {
        return trendingName;
    }

    public void setTrendingName(String trendingName) {
        this.trendingName = trendingName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
