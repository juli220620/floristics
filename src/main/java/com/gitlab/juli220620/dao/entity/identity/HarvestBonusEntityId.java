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
public class HarvestBonusEntityId implements Serializable {

    @Column(name = "flower_id")
    private String flowerId;
    @Column(name = "count")
    private Long count;
    @Column(name = "currency_id")
    private String currencyId;
}
