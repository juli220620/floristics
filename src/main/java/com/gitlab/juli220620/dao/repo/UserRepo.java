package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserRepo extends AutomatedRepo<UserEntity, Long> {
    private final CurrencyDictRepo currencyDictRepo;

    //language=MySQL
    public static final String FIND_BY_ID_QUERY = "select * from user where id = ?";
    //language=MySQL
    public static final String GET_ALL_QUERY = "select * from user";
    //language=MySQL
    public static final String SAVE_QUERY = "insert into user ( username, password ) value ( ?, ? )";
    //language=MySQL
    public static final String UPDATE_QUERY = "update user set username = ?, password = ? where id = ?";
    //language=MySQL
    public static final String DELETE_QUERY = "delete from user where id = ?";

    private final UserRoomRepo roomRepo;

    public UserRepo(ConnectionFactory factory, CurrencyDictRepo currencyDictRepo, UserRoomRepo roomRepo) {
        super(factory);
        this.currencyDictRepo = currencyDictRepo;
        this.roomRepo = roomRepo;
    }

    public UserEntity findByUsername(String username) {
        return prepareStatement("select * from user where username = ?", statement -> {
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();

            if (!set.next()) return null;
            UserEntity entity = convert(set);
            set.close();
            return entity;
        });
    }

    @Override
    protected void setEntityId(UserEntity entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getLong(1));
    }

    protected UserEntity convert(ResultSet set) throws SQLException {
        return new UserEntity(
                set.getLong("id"),
                set.getString("username"),
                set.getString("password"),
                roomRepo.findAllByUserId(set.getLong("id")),
                prepareStatement("select * from user_currency where user_id = ?", st -> {
                    Map<String, Integer> wallet = new HashMap<>();
                    st.setLong(1, set.getLong("id"));
                    ResultSet currSet = st.executeQuery();
                    while (currSet.next()) {
                        wallet.put(currSet.getString("currency_id"), currSet.getInt("amount"));
                    }
                    currSet.close();
                    return wallet;
                })
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
    protected void setSaveQueryParams(PreparedStatement statement, UserEntity entity) throws SQLException {
        statement.setString(1, entity.getUsername());
        statement.setString(2, entity.getPassword());
    }

    @Override
    protected String saveQuery() {
        return SAVE_QUERY;
    }

    @Override
    protected void setUpdateQueryParams(PreparedStatement statement, UserEntity entity) throws SQLException {
        statement.setString(1, entity.getUsername());
        statement.setString(2, entity.getPassword());
        statement.setLong(3, entity.getId());
    }

    @Override
    protected String updateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected void setDeleteQueryParams(PreparedStatement statement, UserEntity entity) throws SQLException {
        statement.setLong(1, entity.getId());
    }

    @Override
    protected String deleteQuery() {
        return DELETE_QUERY;
    }

    @Override
    public UserEntity save(UserEntity entity) {
        UserEntity saved = super.save(entity);
        if (saved.getWallet() == null) return saved;
        Map<String, Integer> all = currencyDictRepo.getAll().stream()
                .collect(Collectors.toMap(CurrencyDictEntity::getId, currencyDictEntity -> 0));
        Map<String, Integer> data = new HashMap<>();
        data.putAll(all);
        data.putAll(saved.getWallet());
        data.forEach((curr, amount) -> {
            prepareStatement("insert into user_currency (user_id, currency_id, amount) value ( ?, ?, ?)", st -> {
                st.setLong(1, saved.getId());
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
    public UserEntity update(UserEntity entity) {
        UserEntity saved = super.update(entity);
        if (saved.getWallet() == null) return saved;
        saved.getWallet().forEach((curr, amount) -> {
            prepareStatement("update user_currency set amount = ? where user_id = ? and currency_id = ?", st -> {
                st.setLong(1, amount);
                st.setLong(2, saved.getId());
                st.setString(3, curr);
                st.executeUpdate();
                st.close();
                return 1;
            });
        });
        return saved;
    }

    @Override
    public boolean delete(UserEntity entity) {
        boolean currRes = prepareStatement("delete from user_currency where user_id = ?", st -> {
            st.setLong(1, entity.getId());
            boolean r = st.executeUpdate() > 0;
            st.close();
            return r;
        });
        boolean res = super.delete(entity);
        return res && currRes;
    }
}
