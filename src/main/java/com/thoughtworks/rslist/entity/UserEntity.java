package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/6 11:55
 * @Description ***
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private Integer age;
    private GenderEnum genderEnum;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<TrendingEntity> trendings;
}
