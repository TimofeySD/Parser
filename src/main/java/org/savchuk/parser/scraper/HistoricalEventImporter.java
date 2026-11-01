package org.savchuk.parser.scraper;

import org.savchuk.parser.ParserApplication;
import org.savchuk.parser.service.HistoricalEventImportService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class HistoricalEventImporter {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context =
                     new SpringApplicationBuilder(ParserApplication.class)
                             .web(WebApplicationType.NONE)
                             .run(args)) {

            HistoricalEventImportService importService =
                    context.getBean(HistoricalEventImportService.class);

            importService.importEvents();
        }
    }
}