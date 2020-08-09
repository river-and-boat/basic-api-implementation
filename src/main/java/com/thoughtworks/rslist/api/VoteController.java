package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.exception_type.BadIndexParamException;
import com.thoughtworks.rslist.exception.exception_type.VotingEventException;
import com.thoughtworks.rslist.service.VoteService;
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
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/trending/{trendingId}/vote")
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
    public ResponseEntity<List<Vote>> getVoteEventsByTimeZones(@RequestParam("startTime") Optional<LocalDateTime> startTime,
                                                               @RequestParam("endTime") Optional<LocalDateTime> endTime)
            throws BadIndexParamException {
        List<Vote> votes = voteService.getVotesBetweenTimeSpan(startTime, endTime);
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/pageable/voteRecords")
    public ResponseEntity<List<Vote>> getVotesByUserAndTrendingId(@RequestParam("userId") Optional<Integer> userId,
                                                                  @RequestParam("trendingId") Optional<Integer> trendingId,
                                                                  @RequestParam("pageIndex") Optional<Integer> pageIndex,
                                                                  @RequestParam("pageSize") Optional<Integer> pageSize)
            throws BadIndexParamException {
        List<Vote> votes = voteService.getVotesWithPageable(userId, trendingId, pageIndex, pageSize);
        return ResponseEntity.ok(votes);
    }
}
