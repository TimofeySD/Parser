package org.savchuk.parser.controller;

import org.savchuk.parser.service.HistoryService;
import org.savchuk.parser.repository.ScientistRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.savchuk.parser.model.ScientistOption;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/results")
    public String index(
            @RequestParam(name = "scientistId", required = false) Long scientistId,
            @RequestParam(name = "sort", defaultValue = "year") String sort,
            Model model
    ){
        model.addAttribute(
                "scientists",
                historyService.getScientists()
        );

        if (scientistId != null) {
            ScientistOption scientist =
                    historyService.getScientist(scientistId)
                            .orElseThrow(() -> new ResponseStatusException(
                                    NOT_FOUND,
                                    "Учёный не найден"
                            ));

            model.addAttribute("selectedScientist", scientist);
            model.addAttribute(
                    "events",
                    historyService.getEventsForScientist(scientistId, sort)
            );
        }
        model.addAttribute("sort", sort);
        return "index";
    }
}