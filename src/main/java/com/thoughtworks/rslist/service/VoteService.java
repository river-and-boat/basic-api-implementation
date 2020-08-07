package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.exception.VotingEventException;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.tool.ConvertTool;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/7 8:28
 * @Description ***
 **/
@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final TrendingRepository trendingRepository;

    public VoteService(UserRepository userRepository,
                       VoteRepository voteRepository,
                       TrendingRepository trendingRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.trendingRepository = trendingRepository;
    }

    @Transactional
    public Vote voteATrending(Optional<Integer> trendingId, Optional<Vote> vote)
            throws BadIndexParamException, VotingEventException {
        if (trendingId.isPresent() && vote.isPresent()) {
            Integer eventId = trendingId.get();
            // 查询用户
            Vote votingEvent = vote.get();
            votingEvent.setTrendingId(eventId);
            Optional<UserEntity> votingUser = userRepository.findById(votingEvent.getUserId());
            if (votingUser.isPresent()) {
                UserEntity userEntity = votingUser.get();
                Integer remainingVoteNum = userEntity.getVoteNum();
                if (remainingVoteNum.compareTo(votingEvent.getNum()) >= 0) {
                    // 投票:
                    // 1. 从用户中减去投的票，并更新数据库
                    userEntity.setVoteNum(remainingVoteNum - votingEvent.getNum());
                    userRepository.save(userEntity);
                    //2. 在热搜列表中找到相应的热搜事件，并增加投票数
                    Optional<TrendingEntity> trendingEvent = trendingRepository.findById(eventId);
                    if (trendingEvent.isPresent()) {
                        TrendingEntity trendingEntity = trendingEvent.get();
                        trendingEntity.setTotalVotes(trendingEntity.getTotalVotes() + votingEvent.getNum());
                        trendingRepository.save(trendingEntity);
                    } else {
                        throw new VotingEventException("Can not find event in trending list, vote failed");
                    }
                    //3. 增加投票记录
                    VoteEntity voteEntity = ConvertTool.convertVoteToVoteEntity(votingEvent);
                    VoteEntity resultEntity = voteRepository.save(voteEntity);
                    return ConvertTool.convertVoteEntityToVote(resultEntity);
                }
            }
        }
        throw new BadIndexParamException("invalid request param");
    }
}
