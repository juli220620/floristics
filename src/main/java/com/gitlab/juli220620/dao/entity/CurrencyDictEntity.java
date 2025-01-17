package com.gitlab.juli220620.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency_dict")
public class CurrencyDictEntity {
    public static final String CASH_ID = "CASH";
    public static final String GREEN_ID = "GREEN";
    public static final String BLUE_ID = "BLUE";
    public static final String RED_ID = "RED";
    public static final String DELORIAN_ID = "DELORIAN";
    public static final String GEMSTONES_ID = "GEMSTONES";
    public static final String STONES_AND_BOARDS_ID = "STONES_AND_BOARDS";

    @Id
    private String id;
    private String name;
    private Integer factorToCash;
}
