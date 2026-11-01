package org.savchuk.parser.service;

import org.savchuk.parser.model.Scientist;
import org.savchuk.parser.repository.ScientistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScientistSaveService {

    private final ScientistRepository repository;

    public ScientistSaveService(ScientistRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveAll(List<Scientist> scientists) {
        for (Scientist scientist : scientists) {
            repository.save(scientist);
        }
    }
}