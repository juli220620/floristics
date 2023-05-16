package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRoomRepo extends AutomatedRepo<UserRoomEntity, Long> {
    //language=MySQL
    public static final String FIND_BY_ID_QUERY = "select * from user_room where id = ?";
    //language=MySQL
    public static final String FIND_BY_USER_ID_QUERY = "select * from user_room where user_id = ?";
    //language=MySQL
    public static final String GET_ALL_QUERY = "select * from user_room";
    //language=MySQL
    public static final String SAVE_QUERY = "insert into user_room " +
            "( user_id, area, name ) value ( ?, ?, ? )";
    //language=MySQL
    public static final String UPDATE_QUERY = "update user_room set user_id = ?, " +
            "area = ?, name = ? where id = ?";
    //language=MySQL
    public static final String DELETE_QUERY = "delete from user_room where id = ?";

    public UserRoomRepo(ConnectionFactory factory) {
        super(factory);
    }

    public List<UserRoomEntity> findAllByUserId(Long userId) {
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_ID_QUERY)) {

            statement.setLong(1, userId);

            ResultSet set = statement.executeQuery();

            List<UserRoomEntity> list = new ArrayList<>();
            while (set.next()) {
                list.add(convert(set));
            }
            set.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setEntityId(UserRoomEntity entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getLong(1));
    }

    protected UserRoomEntity convert(ResultSet set) throws SQLException {
        return new UserRoomEntity(
                set.getLong("id"),
                set.getInt("area"),
                set.getString("name"),
                set.getLong("user_id")
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

    @Override
    protected void setSaveQueryParams(PreparedStatement statement, UserRoomEntity entity) throws SQLException {
        statement.setLong(1, entity.getUserId());
        statement.setInt(2, entity.getArea());
        statement.setString(3, entity.getName());
    }

    @Override
    protected String saveQuery() {
        return SAVE_QUERY;
    }

    @Override
    protected void setUpdateQueryParams(PreparedStatement statement, UserRoomEntity entity) throws SQLException {
        statement.setLong(1, entity.getUserId());
        statement.setInt(2, entity.getArea());
        statement.setString(3, entity.getName());
        statement.setLong(4, entity.getId());
    }

    @Override
    protected String updateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected void setDeleteQueryParams(PreparedStatement statement, UserRoomEntity entity) throws SQLException {
        statement.setLong(1, entity.getId());
    }

    @Override
    protected String deleteQuery() {
        return DELETE_QUERY;
    }
}
