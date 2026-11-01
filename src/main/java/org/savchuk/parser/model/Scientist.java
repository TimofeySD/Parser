package org.savchuk.parser.model;

public record Scientist(
        String name,
        Integer birthYear,
        Integer deathYear,
        String biography,
        String sourceUrl
) {
}