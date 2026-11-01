package org.savchuk.parser.controller;

import org.savchuk.parser.service.HistoricalEventImportService;
import org.savchuk.parser.service.ScientistImportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StartController {

    private final ScientistImportService scientistImportService;
    private final HistoricalEventImportService eventImportService;

    public StartController(
            ScientistImportService scientistImportService,
            HistoricalEventImportService eventImportService
    ) {
        this.scientistImportService = scientistImportService;
        this.eventImportService = eventImportService;
    }

    @GetMapping("/")
    public String startPage() {
        return "start";
    }

    @PostMapping("/import")
    public String importData(Model model) {
        boolean scientistsImported = false;
        boolean eventsImported = false;

        try {
            scientistImportService.importScientists();
            scientistsImported = true;
        } catch (Exception exception) {
            model.addAttribute(
                    "scientistsError",
                    "Не удалось собрать и сохранить данные об учёных."
            );
            exception.printStackTrace();
        }

        try {
            eventImportService.importEvents();
            eventsImported = true;
        } catch (Exception exception) {
            model.addAttribute(
                    "eventsError",
                    "Не удалось собрать и сохранить данные о событиях."
            );
            exception.printStackTrace();
        }

        model.addAttribute("importStarted", true);
        model.addAttribute("scientistsImported", scientistsImported);
        model.addAttribute("eventsImported", eventsImported);

        return "start";
    }
}