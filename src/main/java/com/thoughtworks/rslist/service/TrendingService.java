package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.entity.Trending;
import com.thoughtworks.rslist.entity.User;
import com.thoughtworks.rslist.repository.TrendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:36
 * @Description ***
 **/
@Service
public class TrendingService {

    private final TrendingRepository trendingRepository;

    private final UserService userService;

    private List<Trending> trendingList;

    private final static Integer NAN_TRENDING = -1;

    public TrendingService(TrendingRepository trendingRepository, UserService userService) {
        this.trendingRepository = trendingRepository;
        trendingList = trendingRepository.getTrendingList();
        this.userService = userService;
    }

    public Trending accessOneTrendingByIdService(Optional<Integer> id)
            throws Exception {
        if (id.isPresent()) {
            return trendingList.stream().filter(t -> t.getId().compareTo(id.get()) == 0)
                    .findFirst()
                    .orElse(null);
        }
        throw new Exception("请输入热搜ID");
    }

    public List<Trending> accessTrendingListFromStartToEndService(Optional<Integer> startId, Optional<Integer> endId) {
        List<Trending> trendings = trendingList.stream()
                .filter(t -> t.getId().compareTo(startId.orElse(1)) >= 0
                        && t.getId().compareTo(endId.orElse(trendingList.size())) <= 0)
                .collect(Collectors.toList());
        return trendings;
    }

    public Integer addOrUpdateTrending(Optional<Trending> newTrending) {
        if (newTrending.isPresent()) {
            Trending trending = newTrending.get();
            Optional<String> userName = Optional.ofNullable(trending.getUser().getUserName());
            User user = userService.getUserByUserNameService(userName);
            userService.addNewUser(Optional.ofNullable(user));

            Optional<Trending> trendings = trendingList.stream()
                    .filter(s -> s.getId()
                            .compareTo(trending.getId()) == 0)
                    .findFirst();
            if (!trendings.isPresent()) {
                trendingList.add(newTrending.get());
            } else {
                trendings.get().updateFields(trending);
            }
            return trending.getId();
        }
        return NAN_TRENDING;
    }

    public void deleteTrendingById(Optional<Integer> id) {
        id.ifPresent(index -> {
            trendingList.stream()
                    .filter(t -> t.getId().compareTo(index) == 0)
                    .findFirst().ifPresent(s -> {
                trendingList.remove(s);
            });
        });
    }
}
