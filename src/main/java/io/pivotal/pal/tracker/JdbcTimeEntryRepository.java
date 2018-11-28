package io.pivotal.pal.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcTimeEntryRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry entry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection ->
                Optional.of(connection.prepareStatement("INSERT INTO TIME_ENTRIES (PROJECT_ID, USER_ID, DATE, HOURS) VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS))
                        .map(statement -> {
                            try {
                                statement.setLong(1, entry.getProjectId());
                                statement.setLong(2, entry.getUserId());
                                statement.setDate(3, Date.valueOf(entry.getDate()));
                                statement.setInt(4, entry.getHours());

                                return statement;
                            } catch (SQLException e) {
                                log.error(e.getLocalizedMessage());
                                return statement;
                            }
                        }).get(), generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(Long id) {
        return jdbcTemplate.query("SELECT ID, PROJECT_ID, USER_ID, DATE, HOURS FROM TIME_ENTRIES WHERE ID = ?", new Object[]{id}, timeEntryResultSetExtractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("SELECT ID, PROJECT_ID, USER_ID, DATE, HOURS FROM TIME_ENTRIES", timeEntryRowMapper);
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE TIME_ENTRIES SET PROJECT_ID = ?, USER_ID = ?, DATE = ?, HOURS = ? WHERE ID = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM TIME_ENTRIES WHERE ID = ?", id);
    }

    private final RowMapper<TimeEntry> timeEntryRowMapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("ID"),
            rs.getLong("PROJECT_ID"),
            rs.getLong("USER_ID"),
            rs.getDate("DATE").toLocalDate(),
            rs.getInt("HOURS")
    );

    private final ResultSetExtractor<TimeEntry> timeEntryResultSetExtractor =
            (rs) -> rs.next() ? timeEntryRowMapper.mapRow(rs, 1) : null;
}
