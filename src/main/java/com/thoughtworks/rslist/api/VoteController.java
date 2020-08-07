package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.exception.VotingEventException;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity voteATrending(@PathVariable("trendingId")Optional<Integer> trendingId,
                                        @RequestBody Optional<Vote> vote)
            throws BadIndexParamException, VotingEventException {
        Vote voteResult = voteService.voteATrending(trendingId, vote);
        if (voteResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("add", voteResult.getId().toString()).body(null);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
