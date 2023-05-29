package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

@Component
public interface RoomFlowerRepo extends JpaRepository<RoomFlowerEntity, Long> {

    @Modifying
    @Query("delete from RoomFlowerEntity e where e.id = :id")
    void customDelete(Long id);
}
