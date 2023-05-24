package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoomRepo extends CrudRepository<UserRoomEntity, Long> {
}
