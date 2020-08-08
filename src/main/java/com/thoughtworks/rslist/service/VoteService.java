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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static Integer DEFAULT_PAGE_INDEX = 0;

    private final static Integer DEFAULT_PAGE_SIZE = 5;

    public VoteService(UserRepository userRepository,
                       VoteRepository voteRepository,
                       TrendingRepository trendingRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.trendingRepository = trendingRepository;
    }

    @Transactional(rollbackOn = {Exception.class})
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

    public List<Vote> getVotesBetweenTimeSpan(Optional<String> startTime, Optional<String> endTime)
            throws BadIndexParamException {
        if (startTime.isPresent() && endTime.isPresent()) {
            // 正则表达式验证是否为日期格式的字符串
            if (!isDateFormat(startTime.get()) || !isDateFormat(endTime.get())) {
                throw new BadIndexParamException("The datetime is bad format");
            }

            LocalDateTime start = LocalDateTime.parse(startTime.get(), DF);
            LocalDateTime end = LocalDateTime.parse(endTime.get(), DF);
            if (start.isAfter(end)) {
                throw new BadIndexParamException("End time can't be earlier than start time");
            }
            List<VoteEntity> voteEntities = voteRepository.getVoteEntitiesByVoteTimeBetween(start, end);

            return voteEntities.stream().map(v -> ConvertTool.convertVoteEntityToVote(v))
                    .collect(Collectors.toList());
        }
        throw new BadIndexParamException("invalid request param");
    }

    public List<Vote> getVotesWithPageable(Optional<Integer> userId, Optional<Integer> trendingId,
                                           Optional<Integer> pageIndex, Optional<Integer> pageSize)
            throws BadIndexParamException {
        Integer pageId = DEFAULT_PAGE_INDEX;
        Integer pageItem = DEFAULT_PAGE_SIZE;
        if (pageIndex.isPresent() && pageSize.isPresent()) {
            pageId = pageIndex.get() - 1;
            pageItem = pageSize.get();
        }

        if (pageId.compareTo(0) < 0 || pageItem.compareTo(0) < 0) {
            throw new BadIndexParamException("invalid request param");
        }

        if (userId.isPresent() && trendingId.isPresent()) {
            Integer uId = userId.get();
            Integer tId = trendingId.get();
            Pageable pageable = PageRequest.of(pageId, pageItem);
            List<VoteEntity> voteEntities = voteRepository
                    .getVoteEntitiesByUserIdAndTrendingId(uId, tId, pageable);
            return voteEntities.stream()
                    .map(v -> ConvertTool.convertVoteEntityToVote(v))
                    .collect(Collectors.toList());
        }
        throw new BadIndexParamException("userId or trendingId can not be null");
    }

    private boolean isDateFormat(String dateFormatStr) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?" +
                "((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?" +
                "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))" +
                "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|" +
                "(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher matcher = pattern.matcher(dateFormatStr);
        return matcher.matches();
    }
}
