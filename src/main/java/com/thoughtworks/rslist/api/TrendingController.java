package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.Trending;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.exception.CommonErrorMessage;
import com.thoughtworks.rslist.service.TrendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TrendingController {

    @Autowired
    private TrendingService trendingService;

    @GetMapping("/trendings/{id}")
    public ResponseEntity<Trending> accessOneTrendingById(@PathVariable("id") Optional<Integer> id)
            throws Exception {
        return ResponseEntity.ok(trendingService.accessOneTrendingByIdService(id));
    }

    @GetMapping("/trendings/range")
    public ResponseEntity<List<Trending>> accessTrendingListFromStartToEnd(@RequestParam Optional<Integer> startId,
                                                                           @RequestParam Optional<Integer> endId)
            throws BadIndexParamException {
        return ResponseEntity.ok(trendingService
                .accessTrendingListFromStartToEndService(startId, endId));
    }

    @PostMapping("/trendings/newTrending")
    public ResponseEntity addNewTrending(@RequestBody Optional<Trending> newTrending) {
        Integer index = trendingService.addOrUpdateTrending(newTrending);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("add", index.toString())
                .body(null);
    }

    @PutMapping("/trendings/exist")
    public ResponseEntity updateTrending(@RequestBody Optional<Trending> existTrending) {
        Integer index = trendingService.addOrUpdateTrending(existTrending);
        return ResponseEntity.status(HttpStatus.OK)
                .header("update", index.toString()).body(null);
    }

    @DeleteMapping("/trendings/{id}")
    public ResponseEntity deleteOneTrending(@PathVariable("id") Optional<Integer> id) {
        Integer index = trendingService.deleteTrendingById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .header("delete", index.toString()).body(null);
    }
}
