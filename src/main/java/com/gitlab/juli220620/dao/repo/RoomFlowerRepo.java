package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.ConnectionFactory;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoomFlowerRepo extends AutomatedRepo<RoomFlowerEntity, Long> {
    //language=MySQL
    public static final String FIND_BY_ID_QUERY = "select * from room_flower where id = ?";
    //language=MySQL
    public static final String GET_ALL_QUERY = "select * from room_flower";
    //language=MySQL
    public static final String SAVE_QUERY = "insert into room_flower " +
            "( room_id, flower_id, pot_id, water, nutrient, growth, updated, status ) " +
            "VALUE ( ?, ?, ?, ?, ?, ?, ?, ? )";
    //language=MySQL
    public static final String UPDATE_QUERY = "update room_flower " +
            "set room_id = ?, flower_id = ?, pot_id = ?, water = ?, nutrient = ?, growth = ?, updated = ?, status = ? " +
            "where id = ?";
    //language=MySQL
    public static final String DELETE_QUERY = "delete from room_flower where id = ?";

    private final BaseFlowerDictRepo baseFlowerRepo;
    private final PotDictRepo potRepo;
    private final UserRoomRepo roomRepo;

    public RoomFlowerRepo(ConnectionFactory factory,
                          BaseFlowerDictRepo baseFlowerRepo,
                          PotDictRepo potRepo,
                          UserRoomRepo roomRepo) {
        super(factory);
        this.baseFlowerRepo = baseFlowerRepo;
        this.potRepo = potRepo;
        this.roomRepo = roomRepo;
    }

    @Override
    protected void setEntityId(RoomFlowerEntity entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getLong(1));
    }

    protected RoomFlowerEntity convert(ResultSet set) throws SQLException {
        return new RoomFlowerEntity(
                set.getLong("id"),
                set.getInt("water"),
                set.getInt("nutrient"),
                set.getLong("growth"),
                DateUtils.convertToLocalDateTimeViaInstant(set.getTimestamp("updated")),
                set.getString("status"),
                roomRepo.findById(set.getLong("room_id")),
                baseFlowerRepo.findById(set.getString("flower_id")),
                potRepo.findById(set.getString("pot_id"))
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
    protected void setSaveQueryParams(PreparedStatement statement, RoomFlowerEntity entity) throws SQLException {
        statement.setLong(1, entity.getRoom().getId());
        statement.setString(2, entity.getBaseFlower().getId());
        statement.setString(3, entity.getBasePot().getId());
        statement.setInt(4, entity.getWater());
        statement.setInt(5, entity.getNutrient());
        statement.setLong(6, entity.getGrowth());
        statement.setTimestamp(7, new java.sql.Timestamp(DateUtils.convertToDateViaInstant(entity.getUpdated()).getTime()));
        statement.setString(8, entity.getStatus());
    }

    @Override
    protected String saveQuery() {
        return SAVE_QUERY;
    }

    @Override
    protected void setUpdateQueryParams(PreparedStatement statement, RoomFlowerEntity entity) throws SQLException {
        statement.setLong(1, entity.getRoom().getId());
        statement.setString(2, entity.getBaseFlower().getId());
        statement.setString(3, entity.getBasePot().getId());
        statement.setInt(4, entity.getWater());
        statement.setInt(5, entity.getNutrient());
        statement.setLong(6, entity.getGrowth());
        statement.setDate(7, new java.sql.Date(DateUtils.convertToDateViaInstant(entity.getUpdated()).getTime()));
        statement.setString(8, entity.getStatus());
        statement.setLong(9, entity.getId());
    }

    @Override
    protected String updateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected void setDeleteQueryParams(PreparedStatement statement, RoomFlowerEntity entity) throws SQLException {
        statement.setLong(1, entity.getId());
    }

    @Override
    protected String deleteQuery() {
        return DELETE_QUERY;
    }
}
