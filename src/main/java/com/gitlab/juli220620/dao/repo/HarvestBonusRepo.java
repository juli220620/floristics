package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.HarvestBonusEntity;
import com.gitlab.juli220620.dao.entity.identity.HarvestBonusEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestBonusRepo extends JpaRepository<HarvestBonusEntity, HarvestBonusEntityId> {

    List<HarvestBonusEntity> findAllByBaseFlower_Id(String flowerId);
}
