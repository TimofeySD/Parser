package org.savchuk.parser.service;

import org.junit.jupiter.api.Test;
import org.savchuk.parser.model.Scientist;
import org.savchuk.parser.scraper.ScientistScraper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ScientistImportServiceTest {

    @Test
    void savesScientistsReadFromSite() {
        ScientistScraper scraper = mock(ScientistScraper.class);
        ScientistSaveService saveService = mock(ScientistSaveService.class);

        ScientistImportService importService =
                new ScientistImportService(scraper, saveService);

        List<Scientist> scientists = List.of(
                new Scientist(
                        "Михаил Васильевич Ломоносов",
                        1711,
                        1765,
                        "Описание",
                        "https://example.com/scientists"
                )
        );

        when(scraper.readScientists()).thenReturn(scientists);

        importService.importScientists();

        verify(saveService).saveAll(scientists);
    }

    @Test
    void doesNotSaveWhenReadingSiteFails() {
        ScientistScraper scraper = mock(ScientistScraper.class);
        ScientistSaveService saveService = mock(ScientistSaveService.class);

        ScientistImportService importService =
                new ScientistImportService(scraper, saveService);

        when(scraper.readScientists())
                .thenThrow(new RuntimeException("Ошибка чтения сайта"));

        assertThrows(
                RuntimeException.class,
                importService::importScientists
        );

        verifyNoInteractions(saveService);
    }
}