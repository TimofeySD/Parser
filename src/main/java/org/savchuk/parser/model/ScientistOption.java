package org.savchuk.parser.model;

public record ScientistOption(
        long id,
        String name,
        Integer birthYear,
        Integer deathYear,
        String biography,
        String sourceUrl
) {
}