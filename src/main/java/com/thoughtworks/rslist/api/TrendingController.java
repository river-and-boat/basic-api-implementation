package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Trade;
import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.exception.exception_type.BadIndexParamException;
import com.thoughtworks.rslist.exception.exception_type.TrendingShowSortException;
import com.thoughtworks.rslist.service.TrendingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@RestController
@Api(tags = "热搜列表操作")
public class TrendingController {

    private final TrendingService trendingService;

    public TrendingController(TrendingService trendingService) {
        this.trendingService = trendingService;
    }

    @GetMapping("/trendings/{id}")
    @ApiOperation(value = "查找热搜", notes = "根据id查找热搜")
    public ResponseEntity<Trending> accessOneTrendingById(@PathVariable("id") Optional<Integer> id)
            throws Exception {
        return ResponseEntity.ok(trendingService.accessOneTrendingByIdService(id));
    }

    @GetMapping("/trendings/range")
    @ApiOperation(value = "查找热搜范围", notes = "根据指定的id范围查找热搜")
    public ResponseEntity<List<Trending>> accessTrendingListFromStartToEnd(@RequestParam Optional<Integer> startId,
                                                                           @RequestParam Optional<Integer> endId)
            throws BadIndexParamException {
        return ResponseEntity.ok(trendingService
                .accessTrendingListFromStartToEndService(startId, endId));
    }

    @GetMapping("/trendings/order/all")
    @ApiOperation(value = "查看所有热搜", notes = "根据排名显示热搜(包含投票排名和购买排名)")
    public ResponseEntity<TreeMap<Integer, Trending>> accessAllTrendingInOrder()
            throws TrendingShowSortException {
        return ResponseEntity.ok(trendingService.accessAllTrendingListInOrder());
    }

    @PostMapping("/trendings/newTrending")
    @ApiOperation(value = "新增热搜")
    public ResponseEntity addNewTrending(@RequestBody @Validated Optional<Trending> newTrending, BindingResult bindingResult)
            throws BadIndexParamException {
        if (bindingResult.hasErrors()) {
            throw new BadIndexParamException("trending valid fail. name: [TrendingController.addNewTrending]");
        }
        Trending trendingResult = trendingService.addNewTrending(newTrending);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("add", trendingResult.getId().toString())
                .body(null);
    }

    @PutMapping("/trendings/exist/{trendingId}")
    @ApiOperation(value = "更新热搜", notes = "根据id更新指定热搜")
    public ResponseEntity updateTrending(@PathVariable("trendingId") Optional<Integer> trendingId,
                                         @RequestBody Optional<Trending> existTrending)
            throws BadIndexParamException {
        Trending trendingResult = trendingService.updateExistTrending(existTrending, trendingId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("update", trendingResult.getId().toString()).body(null);
    }

    @DeleteMapping("/trendings/{id}")
    @ApiOperation(value = "删除热搜", notes = "根据id删除指定热搜")
    public ResponseEntity deleteOneTrending(@PathVariable("id") Optional<Integer> id)
            throws BadIndexParamException {
        Integer index = trendingService.deleteTrendingById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .header("delete", index.toString()).body(null);
    }

    @PostMapping("/trending/purchase")
    @ApiOperation(value = "购买热搜", notes = "输入购买热搜id和金额，满足条件即可购买")
    public ResponseEntity purchaseTrendingEvent(@RequestBody Optional<Trade> trade)
            throws BadIndexParamException {
        Trending trending = trendingService.purchaseTrendingEvent(trade);
        if (trending != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("add", trending.getId().toString()).body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
