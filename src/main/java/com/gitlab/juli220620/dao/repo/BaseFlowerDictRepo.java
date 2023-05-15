package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Component
public class BaseFlowerDictRepo extends AutomatedRepo<BaseFlowerDictEntity, String> {
    //language=MySQL
    public static final String FIND_BY_ID_QUERY = "select * from base_flower_dict where id = ?";
    //language=MySQL
    public static final String GET_ALL_QUERY = "select * from base_flower_dict";

    public BaseFlowerDictRepo(ConnectionFactory factory) {
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

    protected BaseFlowerDictEntity convert(ResultSet set) throws SQLException {
        return new BaseFlowerDictEntity(
                set.getString("id"),
                set.getString("name"),
                set.getLong("growth_time"),
                set.getInt("water_consumption"),
                set.getInt("nutrient_consumption")
        );
    }
}
