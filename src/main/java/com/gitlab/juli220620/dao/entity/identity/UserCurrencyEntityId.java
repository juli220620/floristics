package com.gitlab.juli220620.dao.entity.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserCurrencyEntityId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "currency_id")
    private String currencyId;
}
