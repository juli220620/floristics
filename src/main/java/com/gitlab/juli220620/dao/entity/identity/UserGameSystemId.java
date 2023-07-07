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
public class UserGameSystemId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "system_id")
    private String systemId;

}
