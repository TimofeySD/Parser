package org.savchuk.parser.service;

import org.savchuk.parser.model.HistoricalEvent;
import org.savchuk.parser.repository.HistoricalEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoricalEventSaveService {

    private final HistoricalEventRepository repository;

    public HistoricalEventSaveService(
            HistoricalEventRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional
    public void saveAll(List<HistoricalEvent> events) {
        for (HistoricalEvent event : events) {
            repository.save(event);
        }
    }
}