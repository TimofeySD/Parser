package org.savchuk.parser.scraper;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ScientistHeadingParserTest {

    private final ScientistHeadingParser parser =
            new ScientistHeadingParser();

    @Test
    void parsesNameAndBothYears() {
        Optional<ScientistHeadingParser.ParsedHeading> result =
                parser.parse("Михаил Васильевич Ломоносов (1711–1765)");

        assertTrue(result.isPresent());

        ScientistHeadingParser.ParsedHeading heading = result.get();

        assertEquals("Михаил Васильевич Ломоносов", heading.name());
        assertEquals(1711, heading.birthYear());
        assertEquals(1765, heading.deathYear());
    }

    @Test
    void parsesBirthYearWithoutDeathYear() {
        Optional<ScientistHeadingParser.ParsedHeading> result =
                parser.parse("Алфёров Жорес Иванович (род. 1930)");

        assertTrue(result.isPresent());

        ScientistHeadingParser.ParsedHeading heading = result.get();

        assertEquals("Алфёров Жорес Иванович", heading.name());
        assertEquals(1930, heading.birthYear());
        assertNull(heading.deathYear());
    }

    @Test
    void returnsEmptyForUnknownFormat() {
        Optional<ScientistHeadingParser.ParsedHeading> result =
                parser.parse("Учёный без указанных дат");

        assertTrue(result.isEmpty());
    }

    @Test
    void handlesNonBreakingSpaceAtTheEnd() {
        Optional<ScientistHeadingParser.ParsedHeading> result =
                parser.parse("Ландау Лев Давидович (1908–1968)\u00A0");

        assertTrue(result.isPresent());
        assertEquals(1968, result.get().deathYear());
    }
}