package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class AutomatedRepo <E, I> implements CrudRepo<E, I> {

    protected final ConnectionFactory factory;

    public E save(E entity) { return prepareStatement(saveQuery(), statement -> {
        setSaveQueryParams(statement, entity);

        int count = statement.executeUpdate();
        if (count == 0) return entity;

        ResultSet keys = statement.getGeneratedKeys();
        if (!keys.next()) return entity;
        setEntityId(entity, keys);
        keys.close();
        return entity;
    }); }

    public E findById(I id) { return prepareStatement(findByIdQuery(), statement -> {
        setFindByIdParams(statement, id);
        ResultSet set = statement.executeQuery();
        set.next();
        E entity = convert(set);
        set.close();

        return entity;
    }); }

    public List<E> getAll() { return prepareStatement(getAllQuery(), statement -> {
        ResultSet set = statement.executeQuery();

        List<E> list = new ArrayList<>();

        while (set.next()) {
            list.add(convert(set));
        }
        set.close();

        return list;
    }); }

    public E update(E entity) { return prepareStatement(updateQuery(), statement -> {
        setUpdateQueryParams(statement, entity);
        statement.executeUpdate();
        return entity;
    }); }

    public boolean delete(E entity) { return prepareStatement(deleteQuery(), statement -> {
        setDeleteQueryParams(statement, entity);
        return statement.executeUpdate() > 0;
    }); }

    protected abstract void setEntityId(E entity, ResultSet keys) throws SQLException;

    protected abstract E convert(ResultSet set) throws SQLException;

    protected abstract void setFindByIdParams(PreparedStatement statement, I id) throws SQLException;

    protected abstract String findByIdQuery();

    protected abstract String getAllQuery();

    protected abstract void setSaveQueryParams(PreparedStatement statement, E entity) throws SQLException;

    protected abstract String saveQuery();
    protected abstract void setUpdateQueryParams(PreparedStatement statement, E entity) throws SQLException;

    protected abstract String updateQuery();

    protected abstract void setDeleteQueryParams(PreparedStatement statement, E entity) throws SQLException;

    protected abstract String deleteQuery();

    private <E> E prepareStatement(String query, SqlFunction<PreparedStatement, E> function) {
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            return function.apply(statement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private interface SqlFunction<T, R> {
        R apply(T argument) throws SQLException;
    }
}
