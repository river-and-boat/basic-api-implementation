package com.thoughtworks.rslist.tool;

import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.entity.PurchaseEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;

import java.time.LocalDateTime;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/6 12:36
 * @Description ***
 **/
public class ConvertTool {

    public static User convertUserEntityToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .age(userEntity.getAge())
                .genderEnum(userEntity.getGenderEnum())
                .voteNum(userEntity.getVoteNum())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone()).build();

    }

    public static UserEntity convertUserToUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .age(user.getAge())
                .genderEnum(user.getGenderEnum())
                .voteNum(user.getVoteNum())
                .email(user.getEmail())
                .phone(user.getPhone()).build();
    }

    public static Trending convertTrendingEntityToTrending(TrendingEntity trendingEntity) {
        return Trending.builder()
                .id(trendingEntity.getId())
                .userId(trendingEntity.getUserId())
                .trendingName(trendingEntity.getTrendingName())
                .keyWord(trendingEntity.getKeyWord())
                .purchaseDegree(trendingEntity.getPurchaseDegree())
                .purchasePrice(trendingEntity.getPurchasePrice())
                .totalVotes(trendingEntity.getTotalVotes())
                .user(convertUserEntityToUser(trendingEntity.getUser()))
                .build();
    }

    public static TrendingEntity convertTrendingToTrendingEntity(Trending trending) {
        return TrendingEntity.builder()
                .id(trending.getId())
                .userId(trending.getUser().getId())
                .trendingName(trending.getTrendingName())
                .keyWord(trending.getKeyWord())
                .purchaseDegree(trending.getPurchaseDegree())
                .purchasePrice(trending.getPurchasePrice())
                .totalVotes(trending.getTotalVotes())
                .user(convertUserToUserEntity(trending.getUser()))
                .build();
    }

    public static VoteEntity convertVoteToVoteEntity(Vote vote) {
        return VoteEntity.builder()
                .id(vote.getId())
                .num(vote.getNum())
                .trendingId(vote.getTrendingId())
                .userId(vote.getUserId())
                .voteTime(vote.getVoteTime())
                .build();
    }

    public static Vote convertVoteEntityToVote(VoteEntity voteEntity) {
        return Vote.builder()
                .id(voteEntity.getId())
                .num(voteEntity.getNum())
                .trendingId(voteEntity.getTrendingId())
                .userId(voteEntity.getUserId())
                .voteTime(voteEntity.getVoteTime())
                .build();
    }

    public static PurchaseEntity convertTrendingToPurchaseEvent(TrendingEntity entity,
                                                                LocalDateTime time) {
        return PurchaseEntity.builder()
                .purchaseDegree(entity.getPurchaseDegree())
                .purchasePrice(entity.getPurchasePrice())
                .trendingName(entity.getTrendingName())
                .trendingId(entity.getId())
                .time(time).build();
    }
}
