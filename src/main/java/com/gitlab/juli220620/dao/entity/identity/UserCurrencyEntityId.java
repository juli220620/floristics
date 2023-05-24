package com.gitlab.juli220620.dao.entity.identity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserCurrencyEntityId implements Serializable {

    private Long userId;
    private String currencyId;
}
