package org.savchuk.parser.service;

import org.savchuk.parser.model.HistoricalEvent;
import org.savchuk.parser.repository.HistoricalEventRepository;
import org.savchuk.parser.repository.ScientistRepository;
import org.springframework.stereotype.Service;
import org.savchuk.parser.model.ScientistOption;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    private final ScientistRepository scientistRepository;
    private final HistoricalEventRepository eventRepository;

    public HistoryService(
            ScientistRepository scientistRepository,
            HistoricalEventRepository eventRepository
    ) {
        this.scientistRepository = scientistRepository;
        this.eventRepository = eventRepository;
    }

    public List<ScientistOption> getScientists() {
        return scientistRepository.findWithKnownDeathYear();
    }

    public Optional<ScientistOption> getScientist(long id) {
        return scientistRepository.findById(id);
    }

    public List<HistoricalEvent> getEventsForScientist(
            long scientistId,
            String sort
    ) {
        boolean sortByTitle = "title".equals(sort);

        return eventRepository.findByScientistId(scientistId, sortByTitle);
    }
}