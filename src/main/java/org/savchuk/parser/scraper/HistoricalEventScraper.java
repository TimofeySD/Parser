package org.savchuk.parser.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.savchuk.parser.model.HistoricalEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class HistoricalEventScraper {

    private static final String TIMELINE_URL =
            "https://history.ru/timeline?century=16&end_year=1599&page=1&start_year=1500";

    private static final By CENTURY_SELECTOR = By.xpath(
            "//div[contains(concat(' ', normalize-space(@class), ' '), " +
                    "' dropdown-select__selected ')]"
    );

    private static final By EVENT_CARDS = By.xpath(
            "//article[contains(concat(' ', normalize-space(@class), ' '), " +
                    "' timeline-card ')]"
    );

    public List<HistoricalEvent> readEvents() {
        WebDriver driver = new ChromeDriver();

        try {
            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(20));

            driver.get(TIMELINE_URL);

            List<HistoricalEvent> events = new ArrayList<>();

            events.addAll(readCentury(wait, "XVIII век"));
            events.addAll(readCentury(wait, "XIX век"));
            events.addAll(readCentury(wait, "XX век"));

            return events;

        } finally {
            driver.quit();
        }
    }

    private List<HistoricalEvent> readCentury(
            WebDriverWait wait,
            String century
    ) {
        WebElement oldCard = wait.until(
                ExpectedConditions.presenceOfElementLocated(EVENT_CARDS)
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(CENTURY_SELECTOR)
        ).click();

        By centuryOption = By.xpath(
                "//div[contains(@class, 'cursor-pointer') " +
                        "and normalize-space(.)='" + century + "']"
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(centuryOption)
        ).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                CENTURY_SELECTOR,
                century
        ));

        wait.until(ExpectedConditions.stalenessOf(oldCard));

        List<WebElement> cards = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(EVENT_CARDS)
        );

        System.out.println("Выбран: " + century);
        System.out.println("Найдено карточек: " + cards.size());

        List<HistoricalEvent> events = new ArrayList<>();

        for (WebElement card : cards) {
            events.add(readEvent(card));
        }

        return events;
    }

    private HistoricalEvent readEvent(WebElement card) {
        WebElement textLink = card.findElement(By.xpath(
                ".//a[contains(concat(' ', normalize-space(@class), ' '), " +
                        "' item-link ')]"
        ));

        String year = textLink.findElement(By.xpath("./div[1]"))
                .getDomProperty("textContent").trim();

        String title = textLink.findElement(By.xpath("./div[2]"))
                .getDomProperty("textContent").trim();

        String description = textLink.findElement(By.xpath("./div[3]"))
                .getDomProperty("textContent").trim();

        String url = textLink.getAttribute("href");

        return new HistoricalEvent(
                Integer.parseInt(year),
                title,
                description,
                url
        );
    }
}