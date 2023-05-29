package com.gitlab.juli220620.web.controllers.user;

import com.gitlab.juli220620.dao.entity.UserCurrencyEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;
    private Long id;
    private Map<String, Integer> wallet;

    public UserDto(UserEntity entity) {
        username = entity.getUsername();
        id = entity.getId();
        wallet = entity.getWallet().stream().collect(Collectors.toMap(
                UserCurrencyEntity::getCurrencyId,
                UserCurrencyEntity::getAmount));
    }
}
