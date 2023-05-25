package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface RoomFlowerRepo extends CrudRepository<RoomFlowerEntity, Long> {

}
