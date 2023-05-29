package com.gitlab.juli220620.web.controllers.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellCurrencyRq {

    private Integer amount;
    private String currencyId;
}
