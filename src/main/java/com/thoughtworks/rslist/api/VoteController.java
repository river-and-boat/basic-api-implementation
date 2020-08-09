package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.exception_type.BadIndexParamException;
import com.thoughtworks.rslist.exception.exception_type.VotingEventException;
import com.thoughtworks.rslist.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/7 0:32
 * @Description ***
 **/
@RestController
@Api(tags = "热搜投票操作")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/trending/{trendingId}/vote")
    @ApiOperation(value = "热搜投票", notes = "输入指定热搜id和投票数，可进行投票")
    public ResponseEntity voteATrending(@PathVariable("trendingId") Optional<Integer> trendingId,
                                        @RequestBody Optional<Vote> vote)
            throws BadIndexParamException, VotingEventException {
        Vote voteResult = voteService.voteATrending(trendingId, vote);
        if (voteResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("add", voteResult.getId().toString()).body(null);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/time/voteEvents")
    @ApiOperation(value = "查询投票记录", notes = "根据输入时间范围查询热搜投票记录")
    public ResponseEntity<List<Vote>> getVoteEventsByTimeZones(@RequestParam("startTime") Optional<LocalDateTime> startTime,
                                                               @RequestParam("endTime") Optional<LocalDateTime> endTime)
            throws BadIndexParamException {
        List<Vote> votes = voteService.getVotesBetweenTimeSpan(startTime, endTime);
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/pageable/voteRecords")
    @ApiOperation(value = "查询投票记录", notes = "根据用户id和热搜id分页查询投票记录")
    public ResponseEntity<List<Vote>> getVotesByUserAndTrendingId(@RequestParam("userId") Optional<Integer> userId,
                                                                  @RequestParam("trendingId") Optional<Integer> trendingId,
                                                                  @RequestParam("pageIndex") Optional<Integer> pageIndex,
                                                                  @RequestParam("pageSize") Optional<Integer> pageSize)
            throws BadIndexParamException {
        List<Vote> votes = voteService.getVotesWithPageable(userId, trendingId, pageIndex, pageSize);
        return ResponseEntity.ok(votes);
    }
}
