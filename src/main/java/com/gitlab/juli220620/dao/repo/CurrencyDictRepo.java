package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CurrencyDictRepo extends AutomatedRepo<CurrencyDictEntity, String> {
    //language=MySQL
    public static final String FIND_BY_ID_QUERY = "select * from currency_dict where id = ?";
    //language=MySQL
    public static final String GET_ALL_QUERY = "select * from currency_dict";
    //language=MySQL
    public static final String SAVE_QUERY = "insert into currency_dict " +
            "( id, name ) " +
            "value ( ?, ? )";
    //language=MySQL
    public static final String UPDATE_QUERY = "update currency_dict " +
            "set name = ? where id = ?";
    //language=MySQL
    public static final String DELETE_QUERY = "delete from currency_dict where id = ?";

    public CurrencyDictRepo(ConnectionFactory factory) {
        super(factory);
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
    protected void setSaveQueryParams(PreparedStatement statement, CurrencyDictEntity entity) throws SQLException {
        statement.setString(1, entity.getId());
        statement.setString(2, entity.getName());
    }

    @Override
    protected String saveQuery() {
        return SAVE_QUERY;
    }

    @Override
    protected void setUpdateQueryParams(PreparedStatement statement, CurrencyDictEntity entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setString(5, entity.getId());
    }

    @Override
    protected String updateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected void setDeleteQueryParams(PreparedStatement statement, CurrencyDictEntity entity) throws SQLException {
        statement.setString(1, entity.getId());
    }

    @Override
    protected String deleteQuery() {
        return DELETE_QUERY;
    }

    @Override
    protected void setEntityId(CurrencyDictEntity entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getString(1));
    }

    protected CurrencyDictEntity convert(ResultSet set) throws SQLException {
        return new CurrencyDictEntity(
                set.getString("id"),
                set.getString("name")
        );
    }
}
