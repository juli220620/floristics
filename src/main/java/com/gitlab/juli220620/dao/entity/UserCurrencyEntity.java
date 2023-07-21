package com.gitlab.juli220620.dao.entity;

import com.gitlab.juli220620.dao.entity.identity.UserCurrencyEntityId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_currency")
public class UserCurrencyEntity {

    @EmbeddedId
    private UserCurrencyEntityId id;

    @MapsId("currencyId")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {REFRESH, PERSIST, MERGE}, optional = false)
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private CurrencyDictEntity currency;

    private Long amount;
}
