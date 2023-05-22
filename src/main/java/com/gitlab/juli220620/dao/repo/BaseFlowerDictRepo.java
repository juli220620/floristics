package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class BaseFlowerDictRepo extends AutomatedRepo<BaseFlowerDictEntity, String> {
    private final CurrencyDictRepo currencyDictRepo;

    //language=MySQL
    public static final String FIND_BY_ID_QUERY = "select * from base_flower_dict where id = ?";
    //language=MySQL
    public static final String GET_ALL_QUERY = "select * from base_flower_dict";
    //language=MySQL
    public static final String SAVE_QUERY = "insert into base_flower_dict " +
            "( id, name, growth_time, water_consumption, nutrient_consumption, price ) " +
            "value ( ?, ?, ?, ?, ?, ? )";
    //language=MySQL
    public static final String UPDATE_QUERY = "update base_flower_dict " +
            "set name = ?, growth_time = ?, water_consumption = ?, " +
            "nutrient_consumption = ?, price = ? where id = ?";
    //language=MySQL
    public static final String DELETE_QUERY = "delete from base_flower_dict where id = ?";

    public BaseFlowerDictRepo(ConnectionFactory factory, CurrencyDictRepo currencyDictRepo) {
        super(factory);
        this.currencyDictRepo = currencyDictRepo;
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
    protected void setSaveQueryParams(PreparedStatement statement, BaseFlowerDictEntity entity) throws SQLException {
        statement.setString(1, entity.getId());
        statement.setString(2, entity.getName());
        statement.setLong(3, entity.getGrowthTime());
        statement.setInt(4, entity.getWaterConsumption());
        statement.setInt(5, entity.getNutrientConsumption());
        statement.setInt(6, entity.getPrice());
    }

    @Override
    protected String saveQuery() {
        return SAVE_QUERY;
    }

    @Override
    protected void setUpdateQueryParams(PreparedStatement statement, BaseFlowerDictEntity entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setLong(2, entity.getGrowthTime());
        statement.setInt(3, entity.getWaterConsumption());
        statement.setInt(4, entity.getNutrientConsumption());
        statement.setInt(5, entity.getPrice());
        statement.setString(6, entity.getId());
    }

    @Override
    protected String updateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected void setDeleteQueryParams(PreparedStatement statement, BaseFlowerDictEntity entity) throws SQLException {
        statement.setString(1, entity.getId());
    }

    @Override
    protected String deleteQuery() {
        return DELETE_QUERY;
    }

    @Override
    protected void setEntityId(BaseFlowerDictEntity entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getString(1));
    }

    protected BaseFlowerDictEntity convert(ResultSet set) throws SQLException {
        return new BaseFlowerDictEntity(
                set.getString("id"),
                set.getString("name"),
                set.getLong("growth_time"),
                set.getInt("water_consumption"),
                set.getInt("nutrient_consumption"),
                prepareStatement("select * from flower_harvest where flower_id = ?", st -> {
                    Map<String, Integer> harvest = new HashMap<>();
                    st.setString(1, set.getString("id"));
                    ResultSet harvRes = st.executeQuery();
                    while (harvRes.next()) {
                        harvest.put(harvRes.getString("currency_id"), harvRes.getInt("amount"));
                    }
                    harvRes.close();
                    return harvest;
                }),
                set.getInt("price")
        );
    }

    @Override
    public BaseFlowerDictEntity save(BaseFlowerDictEntity entity) {
        BaseFlowerDictEntity saved = super.save(entity);
        if (entity.getHarvest() == null) return saved;
        saved.getHarvest().forEach((curr, amount) -> {
            prepareStatement("insert into flower_harvest (flower_id, currency_id, amount) value (?, ?, ?)", st -> {
                st.setString(1, saved.getId());
                st.setString(2, curr);
                st.setInt(3, amount);
                st.executeUpdate();
                st.close();
                return 1;
            });
        });
        return saved;
    }

    @Override
    public BaseFlowerDictEntity update(BaseFlowerDictEntity entity) {
        BaseFlowerDictEntity saved = super.update(entity);
        if (saved.getHarvest() == null) return saved;
        saved.getHarvest().forEach((curr, amount) -> {
            prepareStatement("update user_currency set amount = ? where user_id = ? and currency_id = ?", st -> {
                st.setInt(1, amount);
                st.setString(2, saved.getId());
                st.setString(3, curr);
                st.executeUpdate();
                st.close();
                return 1;
            });
        });
        return saved;
    }

    @Override
    public boolean delete(BaseFlowerDictEntity entity) {
        boolean currRes = prepareStatement("delete from flower_harvest where flower_id = ?", st -> {
            st.setString(1, entity.getId());
            boolean r = st.executeUpdate() > 0;
            st.close();
            return r;
        });
        boolean res = super.delete(entity);
        return res && currRes;
    }
}
