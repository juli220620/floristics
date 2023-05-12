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
    protected void setFindByIdParams(PreparedStatement statement, String id) throws SQLException {
        statement.setString(1, id);
    }

    @Override
    protected String findByIdQuery() {
        return "select * from pot_dict where id = ?";
    }

    @Override
    protected String getAllQuery() {
        return "select * from pot_dict";
    }
}
