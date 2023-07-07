package com.vld.rest.service.impl;

import com.vld.rest.model.Alcohol;
import com.vld.rest.repository.AlcoholRepository;
import com.vld.rest.service.AlcoholService;

import java.util.List;

public class AlcoholServiceImpl implements AlcoholService {

    private final AlcoholRepository alcoholRepository = new AlcoholRepository();

    @Override
    public Alcohol get(Long id) {
        return alcoholRepository.get(id);
    }

    @Override
    public List<Alcohol> getAll() {
        return alcoholRepository.getAll();
    }

    @Override
    public Alcohol create(Alcohol alcohol) {
        return alcoholRepository.create(alcohol);
    }

    @Override
    public Alcohol update(Alcohol alcohol) {
        return alcoholRepository.update(alcohol);
    }

    @Override
    public void delete(Long id) {
        alcoholRepository.delete(id);
    }
}
