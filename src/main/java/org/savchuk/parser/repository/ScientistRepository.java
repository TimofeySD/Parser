package org.savchuk.parser.repository;

import org.savchuk.parser.model.Scientist;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.savchuk.parser.model.ScientistOption;

import java.util.List;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ScientistRepository {

    private final JdbcClient jdbcClient;

    public ScientistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public int save(Scientist scientist) {
        String sql = """
                INSERT INTO scientists
                    (name, birth_year, death_year, biography, source_url)
                VALUES
                    (:name, :birthYear, :deathYear, :biography, :sourceUrl)
                ON CONFLICT (source_url, name) DO UPDATE SET
                    birth_year = EXCLUDED.birth_year,
                    death_year = EXCLUDED.death_year,
                    biography = EXCLUDED.biography
                """;

        return jdbcClient.sql(sql)
                .param("name", scientist.name())
                .param("birthYear", scientist.birthYear())
                .param("deathYear", scientist.deathYear())
                .param("biography", scientist.biography())
                .param("sourceUrl", scientist.sourceUrl())
                .update();
    }


    public List<ScientistOption> findWithKnownDeathYear() {
        String sql = """
                SELECT id, name, birth_year, death_year, biography, source_url
                FROM scientists
                WHERE death_year IS NOT NULL
                ORDER BY name
                """;

        return jdbcClient.sql(sql)
                .query(this::mapScientist)
                .list();
    }

    public Optional<ScientistOption> findById(long id) {
        String sql = """
                SELECT id, name, birth_year, death_year, biography, source_url
                FROM scientists
                WHERE id = :id
                  AND death_year IS NOT NULL
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> new ScientistOption(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("birth_year"),
                        rs.getInt("death_year"),
                        rs.getString("biography"),
                        rs.getString("source_url")
                ))
                .optional();
    }

    private ScientistOption mapScientist(ResultSet rs, int rowNum)
            throws SQLException {

        return new ScientistOption(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("birth_year"),
                rs.getInt("death_year"),
                rs.getString("biography"),
                rs.getString("source_url")
        );
    }
}