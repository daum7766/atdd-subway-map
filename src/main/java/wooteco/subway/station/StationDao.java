package wooteco.subway.station;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.UseForeignKeyException;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Station> stationRowMapper = (resultSet, rowNumber) -> new Station(
        resultSet.getLong("id"),
        resultSet.getString("name")
    );

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Station save(Station station) {
        String sql = "insert into STATION (name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(con -> {
                PreparedStatement prepareStatement = con.prepareStatement(sql, new String[]{"id"});
                prepareStatement.setString(1, station.getName());
                return prepareStatement;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DuplicateException();
        }

        return createNewObject(station, Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, stationRowMapper);
    }

    private Station createNewObject(Station station, Long id) {
        Field field = ReflectionUtils.findField(Station.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, station, id);
        return station;
    }

    public void delete(Long id) {
        try {
            String sql = "delete from STATION where id = ?";
            jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new UseForeignKeyException();
        }
    }

    public List<Station> findByLineId(Long lineId) {
        String sql = "select distinct id, name "
            + "from STATION join "
            + "(select distinct up_station_id, down_station_id from SECTION where line_id = ?) as t "
            + "on id = up_station_id or id = down_station_id";
        return jdbcTemplate.query(sql, stationRowMapper, lineId);
    }
}
