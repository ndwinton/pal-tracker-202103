package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        var keyHolder = new GeneratedKeyHolder();
        var updated = jdbcTemplate.update((connection) -> {
            var stmt = connection.prepareStatement(
                    "insert into time_entries(project_id, user_id, date, hours) values (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, timeEntry.getProjectId());
            stmt.setLong(2, timeEntry.getUserId());
            stmt.setDate(3, Date.valueOf(timeEntry.getDate()));
            stmt.setInt(4, timeEntry.getHours());
            return stmt;
        }, keyHolder);

        return safeFind(keyHolder.getKeyAs(BigInteger.class));
    }

    private TimeEntry safeFind(BigInteger id) {
        if (id == null) return null;
        return find(id.longValue());
    }

    @Override
    public TimeEntry find(long id) {
        var found = jdbcTemplate.query(
                "select id, project_id, user_id, date, hours from time_entries where id = ?",
                mapper,
                id
        );
        return found.size() > 0 ? found.get(0) : null;
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong(1),
            rs.getLong(2),
            rs.getLong(3),
            rs.getDate(4).toLocalDate(),
            rs.getInt(5)
    );

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("select id, project_id, user_id, date, hours from time_entries", mapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        jdbcTemplate.update(
                "update time_entries " +
                        "set project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "where id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id
        );

        return find(id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from time_entries where id = ?", id);
    }
}
