package org.savchuk.parser.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.savchuk.parser.model.Scientist;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ScientistScraper {

    private static final String SCIENTISTS_URL =
            "https://history.ru/read/articles/uchenye-rossii";

    private static final By SCIENTIST_HEADINGS = By.xpath(
            "//div[contains(concat(' ', normalize-space(@class), ' '), " +
                    "' hstrf-editor ')]" +
                    "//h2[contains(concat(' ', normalize-space(@class), ' '), " +
                    "' text-container ')]"
    );

    private final ScientistHeadingParser headingParser;

    public ScientistScraper(ScientistHeadingParser headingParser) {
        this.headingParser = headingParser;
    }

    public List<Scientist> readScientists() {
        WebDriver driver = new ChromeDriver();

        try {
            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(20));

            driver.get(SCIENTISTS_URL);

            List<WebElement> headings = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            SCIENTIST_HEADINGS
                    )
            );

            System.out.println("Найдено заголовков: " + headings.size());

            List<Scientist> scientists = new ArrayList<>();

            for (WebElement heading : headings) {
                Optional<Scientist> result = readScientist(heading);
                result.ifPresent(scientists::add);
            }

            return scientists;

        } finally {
            driver.quit();
        }
    }

    private Optional<Scientist> readScientist(WebElement heading) {
        String text = heading.getDomProperty("textContent");

        Optional<ScientistHeadingParser.ParsedHeading> result =
                headingParser.parse(text);

        if (result.isEmpty()) {
            System.out.println("Не удалось разобрать: " + text);
            return Optional.empty();
        }

        ScientistHeadingParser.ParsedHeading parsed = result.get();

        String biography = heading.findElement(By.xpath(
                "following-sibling::*[1][self::p]"
        )).getDomProperty("textContent").trim();

        return Optional.of(new Scientist(
                parsed.name(),
                parsed.birthYear(),
                parsed.deathYear(),
                biography,
                SCIENTISTS_URL
        ));
    }
}