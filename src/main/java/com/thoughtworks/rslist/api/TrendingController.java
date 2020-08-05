package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.Trending;
import com.thoughtworks.rslist.service.TrendingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TrendingController {

    private final TrendingService trendingService;

    public TrendingController(TrendingService trendingService) {
        this.trendingService = trendingService;
    }

    @GetMapping("/trendings/{id}")
    public Trending accessOneTrendingById(@PathVariable("id") Optional<Integer> id)
            throws Exception {
        return trendingService.accessOneTrendingByIdService(id);
    }

    @GetMapping("/trendings/range")
    public List<Trending> accessTrendingListFromStartToEnd(@RequestParam Optional<Integer> startId,
                                                           @RequestParam Optional<Integer> endId) {
        return trendingService
                .accessTrendingListFromStartToEndService(startId, endId);
    }

    @PostMapping("/trendings/newTrending")
    public ResponseEntity addNewTrending(@RequestBody Optional<Trending> newTrending) {
        Integer index = trendingService.addOrUpdateTrending(newTrending);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("index", index.toString())
                .body(null);
    }

    @PutMapping("/trendings/exist")
    public void updateTrending(@RequestBody Optional<Trending> existTrending) {
        trendingService.addOrUpdateTrending(existTrending);
    }

    @DeleteMapping("/trendings/{id}")
    public void deleteOneTrending(@PathVariable("id") Optional<Integer> id) {
        trendingService.deleteTrendingById(id);
    }
}
