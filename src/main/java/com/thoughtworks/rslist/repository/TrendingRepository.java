package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.Trending;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:40
 * @Description ***
 **/
@Repository
public class TrendingRepository {
    public ArrayList<Trending> getTrendingList() {
        return new ArrayList<>();
    }
}
