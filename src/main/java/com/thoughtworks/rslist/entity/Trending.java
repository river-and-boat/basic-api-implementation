package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/4 20:17
 * @Description ***
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trending {

    private Integer id;

    private String trendingName;

    private String keyWord;

    private User user;

    public void updateFields(Trending newTrending) {
        String newName = newTrending.getTrendingName();
        String newKeyWord = newTrending.getKeyWord();
        if (newName != null) {
            this.trendingName = newName;
        }
        if (newKeyWord != null) {
            this.keyWord = newKeyWord;
        }
    }
}
