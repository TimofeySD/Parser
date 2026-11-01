package org.savchuk.parser.scraper;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ScientistHeadingParser {

    private static final Pattern LIFE_YEARS = Pattern.compile(
            "^(.+?)\\s*\\((\\d{4})\\s*[–—-]\\s*(\\d{4})\\)\\s*$"
    );

    private static final Pattern BIRTH_YEAR_ONLY = Pattern.compile(
            "^(.+?)\\s*\\(род\\.\\s*(\\d{4})\\)\\s*$"
    );

    public record ParsedHeading(
            String name,
            Integer birthYear,
            Integer deathYear
    ) {
    }

    public Optional<ParsedHeading> parse(String heading) {
        String text = heading.replace('\u00A0', ' ').trim();

        Matcher lifeYearsMatcher = LIFE_YEARS.matcher(text);
        if (lifeYearsMatcher.matches()) {
            return Optional.of(new ParsedHeading(
                    lifeYearsMatcher.group(1).trim(),
                    Integer.parseInt(lifeYearsMatcher.group(2)),
                    Integer.parseInt(lifeYearsMatcher.group(3))
            ));
        }

        Matcher birthOnlyMatcher = BIRTH_YEAR_ONLY.matcher(text);
        if (birthOnlyMatcher.matches()) {
            return Optional.of(new ParsedHeading(
                    birthOnlyMatcher.group(1).trim(),
                    Integer.parseInt(birthOnlyMatcher.group(2)),
                    null
            ));
        }

        return Optional.empty();
    }
}