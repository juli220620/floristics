package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.UserEntity;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class UserRepo extends HibernateRepo<UserEntity, Long> {
    public static final String FIND_BY_ID_QUERY = "select u from UserEntity u where u.id = :id";

    public static final String GET_ALL_QUERY = "select u from UserEntity u";

    public UserRepo(SessionFactory factory) {
        super(factory);
    }

    public UserEntity findByUsername(String username) {
        try (Session session = factory.openSession()) {
            TypedQuery<UserEntity> query = session.createQuery(
                    "select e from UserEntity e where e.username = :username", UserEntity.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }
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
    protected Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }
}
