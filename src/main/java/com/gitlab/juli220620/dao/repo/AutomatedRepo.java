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

    public E save(E entity) {
        throw new RuntimeException("NIY");
    }

    public E findById(I id) {
        try (Connection connection = factory.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(findByIdQuery())) {

            setFindByIdParams(statement, id);
            ResultSet set = statement.executeQuery();
            set.next();
            E entity = convert(set);
            set.close();

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<E> getAll() {
        try (Connection connection = factory.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(getAllQuery())) {

            ResultSet set = statement.executeQuery();

            List<E> list = new ArrayList<>();

            while (set.next()) {
                list.add(convert(set));
            }
            set.close();

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public E update(E entity) {
        throw new RuntimeException("NIY");
    }

    public boolean delete(E entity) {
        throw new RuntimeException("NIY");
    }

    protected abstract E convert(ResultSet set) throws SQLException;

    protected abstract void setFindByIdParams(PreparedStatement statement, I id) throws SQLException;

    protected abstract String findByIdQuery();
    protected abstract String getAllQuery();
}
