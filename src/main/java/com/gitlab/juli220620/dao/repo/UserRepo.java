package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import com.gitlab.juli220620.dao.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRepo extends AutomatedRepo<UserEntity, Long> {
    public static final String FIND_BY_ID_QUERY = "select * from user where id = ?";
    public static final String GET_ALL_QUERY = "select * from user";

    private final UserRoomRepo roomRepo;

    public UserRepo(ConnectionFactory factory, UserRoomRepo roomRepo) {
        super(factory);
        this.roomRepo = roomRepo;
    }

    protected UserEntity convert(ResultSet set) throws SQLException {
        return new UserEntity(
                set.getLong("id"),
                set.getString("username"),
                set.getString("password"),
                roomRepo.findAllByUserId(set.getLong("id"))
        );
    }

    @Override
    protected void setFindByIdParams(PreparedStatement statement, Long id) throws SQLException {
        statement.setLong(1, id);
    }

    @Override
    protected String findByIdQuery() {
        return FIND_BY_ID_QUERY;
    }

    @Override
    protected String getAllQuery() {
        return GET_ALL_QUERY;
    }
}
