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

    protected RoomFlowerEntity convert(ResultSet set) throws SQLException {
        return new RoomFlowerEntity(
                set.getLong("id"),
                set.getInt("water"),
                set.getInt("nutrient"),
                set.getLong("growth"),
                DateUtils.convertToLocalDateTimeViaInstant(set.getTimestamp("updated")),
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
}
