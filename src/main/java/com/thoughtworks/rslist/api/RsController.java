package com.thoughtworks.rslist.api;
import com.thoughtworks.rslist.model.Trending;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RsController {

    public List<Trending> trendingList = Stream.of(
            new Trending(1, "热搜事件1", "无分类"),
            new Trending(2, "热搜事件2", "无分类"),
            new Trending(3, "热搜事件3", "无分类"),
            new Trending(4, "热搜事件4", "无分类"),
            new Trending(5, "热搜事件5", "无分类")
    ).collect(Collectors.toList());

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

    @GetMapping("/trendings/range")
    public List<Trending> accessTrendingListFromStartToEnd(@RequestParam Optional<Integer> startId,
                                                           @RequestParam Optional<Integer> endId) {
        List<Trending> trendings = trendingList.stream()
                .filter(t -> t.getId().compareTo(startId.orElse(1)) >= 0
                        && t.getId().compareTo(endId.orElse(trendingList.size())) <= 0)
                .collect(Collectors.toList());
        return trendings;
    }

    @PostMapping("/trendings/newTrending")
    public void addNewTrending(@RequestBody Optional<Trending> newTrending) throws Exception {
        newTrending.ifPresent(t -> {
            Optional<Trending> trending = trendingList.stream()
                    .filter(s -> s.getId()
                            .compareTo(t.getId()) == 0)
                    .findFirst();
            if (trending.isPresent()) {
                // 修改
                trending.get().updateFields(t);
            } else {
                // 新增
                trendingList.add(newTrending.get());
            }
        });
    }

    @DeleteMapping("/trendings/{id}")
    public void deleteOneTrending(@PathVariable("id") Optional<Integer> id) {
        id.ifPresent(index -> {
            trendingList.stream()
                    .filter(t -> t.getId().compareTo(index) == 0)
                    .findFirst().ifPresent(s -> {
                        trendingList.remove(s);
            });
        });
    }
}
