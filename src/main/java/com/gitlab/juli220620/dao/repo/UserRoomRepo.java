package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoomRepo extends JpaRepository<UserRoomEntity, Long> {
}
