package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.entity.Trending;
import com.thoughtworks.rslist.entity.User;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.repository.TrendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private TrendingRepository trendingRepository;

    @Autowired
    private UserService userService;

    private final static Integer NAN_TRENDING = -1;

    public Trending accessOneTrendingByIdService(Optional<Integer> id)
            throws Exception {
        if (id.isPresent()) {
            return trendingRepository.getTrendingList()
                    .stream().filter(t -> t.getId().compareTo(id.get()) == 0)
                    .findFirst()
                    .orElse(null);
        }
        throw new Exception("请输入热搜ID");
    }

    public List<Trending> accessTrendingListFromStartToEndService(Optional<Integer> startId, Optional<Integer> endId)
            throws BadIndexParamException {
        if (startId.isPresent() && endId.isPresent()) {
            ArrayList<Trending> trendingList = trendingRepository.getTrendingList();
            if (startId.get() >= 1 && endId.get() <= trendingList.size()) {
                List<Trending> trendings = trendingList.stream()
                    .filter(t -> t.getId().compareTo(startId.orElse(1)) >= 0
                            && t.getId().compareTo(endId.orElse(trendingList.size())) <= 0)
                    .collect(Collectors.toList());
                return trendings;
            }
        }
        throw new BadIndexParamException("invalid request param");
    }

    public Integer addOrUpdateTrending(Optional<Trending> newTrending) {
        if (newTrending.isPresent()) {
            ArrayList<Trending> trendingList = trendingRepository.getTrendingList();
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

    public Integer deleteTrendingById(Optional<Integer> id) {
        if (id.isPresent()) {
            ArrayList<Trending> trendingList = trendingRepository.getTrendingList();
            Integer index = id.get();
            trendingList.stream()
                    .filter(t -> t.getId().compareTo(index) == 0)
                    .findFirst().ifPresent(s -> {
                trendingList.remove(s);
            });
            return index;
        }
        return NAN_TRENDING;
    }
}
