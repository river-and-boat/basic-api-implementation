package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.model.Trending;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

    private List<Trending> trendingList = Arrays.asList(
            new Trending(1, "热搜事件1", "无分类"),
            new Trending(2, "热搜事件2", "无分类"),
            new Trending(3, "热搜事件3", "无分类"),
            new Trending(4, "热搜事件4", "无分类"),
            new Trending(5, "热搜事件5", "无分类")
    );

    @GetMapping("/trendings/{id}")
    public Trending accessOneTrendingById(@PathVariable("id") Optional<Integer> id)
            throws Exception {
        if (id.isPresent()) {
            return trendingList.stream()
                    .filter(t -> t.getId().compareTo(id.get()) == 0)
                    .findFirst()
                    .orElse(null);
        }
        throw new Exception("请输入热搜ID");
    }
}
