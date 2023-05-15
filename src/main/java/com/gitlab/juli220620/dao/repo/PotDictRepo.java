package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import com.gitlab.juli220620.dao.entity.PotDictEntity;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Component
public class PotDictRepo extends AutomatedRepo<PotDictEntity, String> {
    //language=MySQL
    public static final String FIND_BY_ID_QUERY = "select * from pot_dict where id = ?";
    //language=MySQL
    public static final String GET_ALL_QUERY = "select * from pot_dict";
    //language=MySQL
    public static final String SAVE_QUERY = "insert into pot_dict (id, name, capacity) value ( ?, ?, ? )";
    //language=MySQL
    public static final String UPDATE_QUERY = "update pot_dict set name = ?, capacity = ? where id = ?";
    //language=MySQL
    public static final String DELETE_QUERY = "delete from pot_dict where id = ?";

    public PotDictRepo(ConnectionFactory factory) {
        super(factory);
    }

    protected PotDictEntity convert(ResultSet set) throws SQLException {
        return new PotDictEntity(
                set.getString("id"),
                set.getString("name"),
                set.getInt("capacity")
        );
    }

    @Override
    protected void setEntityId(PotDictEntity entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getString("id"));
    }

    @Override
    protected void setFindByIdParams(PreparedStatement statement, String id) throws SQLException {
        statement.setString(1, id);
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
    protected void setSaveQueryParams(PreparedStatement statement, PotDictEntity entity) throws SQLException {
        statement.setString(1, entity.getId());
        statement.setString(2, entity.getName());
        statement.setInt(3, entity.getCapacity());
    }

    @Override
    protected String saveQuery() {
        return SAVE_QUERY;
    }

    @Override
    protected void setUpdateQueryParams(PreparedStatement statement, PotDictEntity entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setInt(2, entity.getCapacity());
        statement.setString(3, entity.getId());
    }

    @Override
    protected String updateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected void setDeleteQueryParams(PreparedStatement statement, PotDictEntity entity) throws SQLException {
        statement.setString(1, entity.getId());
    }

    @Override
    protected String deleteQuery() {
        return DELETE_QUERY;
    }
}
