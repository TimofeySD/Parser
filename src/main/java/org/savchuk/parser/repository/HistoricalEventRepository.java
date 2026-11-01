package org.savchuk.parser.repository;

import org.savchuk.parser.model.HistoricalEvent;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class HistoricalEventRepository {

    private final JdbcClient jdbcClient;

    public HistoricalEventRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public int save(HistoricalEvent event) {
        String sql = """
                INSERT INTO historical_events
                    (event_year, title, description, source_url)
                VALUES
                    (:year, :title, :description, :url)
                ON CONFLICT (source_url) DO UPDATE SET
                    event_year = EXCLUDED.event_year,
                    title = EXCLUDED.title,
                    description = EXCLUDED.description
                """;

        return jdbcClient.sql(sql)
                .param("year", event.year())
                .param("title", event.title())
                .param("description", event.description())
                .param("url", event.url())
                .update();
    }

    public List<HistoricalEvent> findByScientistId(
            long scientistId,
            boolean sortByTitle
    ) {
        String orderBy = sortByTitle
                ? "e.title, e.event_year"
                : "e.event_year, e.title";

        String sql = """
            SELECT e.event_year, e.title, e.description, e.source_url
            FROM historical_events e
            JOIN scientists s
                ON e.event_year BETWEEN s.birth_year AND s.death_year
            WHERE s.id = :scientistId
              AND s.death_year IS NOT NULL
            ORDER BY
            """ + " " + orderBy;

        return jdbcClient.sql(sql)
                .param("scientistId", scientistId)
                .query((rs, rowNum) -> new HistoricalEvent(
                        rs.getInt("event_year"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("source_url")
                ))
                .list();
    }
}