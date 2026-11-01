package org.savchuk.parser.service;

import org.savchuk.parser.model.Scientist;
import org.savchuk.parser.scraper.ScientistScraper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScientistImportService {

    private final ScientistScraper scraper;
    private final ScientistSaveService saveService;

    public ScientistImportService(
            ScientistScraper scraper,
            ScientistSaveService saveService
    ) {
        this.scraper = scraper;
        this.saveService = saveService;
    }

    public void importScientists() {
        List<Scientist> scientists = scraper.readScientists();

        saveService.saveAll(scientists);

        System.out.println("Всего сохранено: " + scientists.size());
    }
}