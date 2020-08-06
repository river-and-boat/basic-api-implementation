package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.service.TrendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity addNewTrending(@RequestBody @Validated Optional<Trending> newTrending)
            throws BadIndexParamException {
        Trending trendingResult = trendingService.addNewTrending(newTrending);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("add", trendingResult.getId().toString())
                .body(null);
    }

    @PutMapping("/trendings/exist/{trendingId}")
    public ResponseEntity updateTrending(@PathVariable("trendingId") Optional<Integer> trendingId,
                                         @RequestBody Optional<Trending> existTrending)
            throws BadIndexParamException {
        Trending trendingResult = trendingService.updateExistTrending(existTrending, trendingId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("update", trendingResult.getId().toString()).body(null);
    }

    @DeleteMapping("/trendings/{id}")
    public ResponseEntity deleteOneTrending(@PathVariable("id") Optional<Integer> id)
            throws BadIndexParamException {
        Integer index = trendingService.deleteTrendingById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .header("delete", index.toString()).body(null);
    }
}
