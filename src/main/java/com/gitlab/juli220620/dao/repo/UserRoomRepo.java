package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class UserRoomRepo extends HibernateRepo<UserRoomEntity, Long> {

    public static final String FIND_BY_ID_QUERY = "select r from UserRoomEntity r where r.id = :id";

    public static final String GET_ALL_QUERY = "select r from UserRoomEntity r";

    public UserRoomRepo(SessionFactory factory) {
        super(factory);
    }

    @Override
    protected String findByIdQuery() {
        return FIND_BY_ID_QUERY;
    }

    @Override
    protected String getAllQuery() {
        return GET_ALL_QUERY;
    }

    @Override
    protected Class<UserRoomEntity> getEntityClass() {
        return UserRoomEntity.class;
    }
}
