package com.gitlab.juli220620.web.controllers.dict;

import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    private String id;
    private String name;
    private Integer factorToCash;

    public CurrencyDto(CurrencyDictEntity currency) {
        id = currency.getId();
        name = currency.getName();
        factorToCash = currency.getFactorToCash();
    }

}

