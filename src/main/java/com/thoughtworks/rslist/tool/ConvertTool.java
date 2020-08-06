package com.thoughtworks.rslist.tool;

import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.entity.UserEntity;

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
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone()).build();
    }

    public static UserEntity convertUserToUserEntity(User user) {
        return UserEntity.builder()
                .userName(user.getUserName())
                .age(user.getAge())
                .genderEnum(user.getGenderEnum())
                .email(user.getEmail())
                .phone(user.getPhone()).build();
    }

    public static Trending convertTrendingEntityToTrending(TrendingEntity trendingEntity) {
        return Trending.builder()
                .id(trendingEntity.getId())
                .trendingName(trendingEntity.getTrendingName())
                .keyWord(trendingEntity.getKeyWord())
                .user(convertUserEntityToUser(trendingEntity.getUser()))
                .build();
    }

    public static TrendingEntity convertTrendingToTrendingEntity(Trending trending) {
        return TrendingEntity.builder()
                .trendingName(trending.getTrendingName())
                .keyWord(trending.getKeyWord())
                .user(convertUserToUserEntity(trending.getUser()))
                .build();
    }
}
