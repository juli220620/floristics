package com.gitlab.juli220620.dao.entity;

import com.gitlab.juli220620.dao.entity.identity.UserCurrencyEntityId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserCurrencyEntityId.class)
@Table(name = "user_currency")
public class UserCurrencyEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "currency_id")
    private String currencyId;

    private Integer amount;
}
