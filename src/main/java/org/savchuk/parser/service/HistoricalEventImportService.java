package org.savchuk.parser.service;

import org.savchuk.parser.model.HistoricalEvent;
import org.savchuk.parser.scraper.HistoricalEventScraper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricalEventImportService {

    private final HistoricalEventScraper scraper;
    private final HistoricalEventSaveService saveService;

    public HistoricalEventImportService(
            HistoricalEventScraper scraper,
            HistoricalEventSaveService saveService
    ) {
        this.scraper = scraper;
        this.saveService = saveService;
    }

    public void importEvents() {
        List<HistoricalEvent> events = scraper.readEvents();

        saveService.saveAll(events);

        System.out.println("Всего сохранено: " + events.size());
    }
}