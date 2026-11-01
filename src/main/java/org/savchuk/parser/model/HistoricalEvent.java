package org.savchuk.parser.model;

public record HistoricalEvent(
        int year,
        String title,
        String description,
        String url
) {
}