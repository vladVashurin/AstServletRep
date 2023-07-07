package com.vld.rest.service.impl;

import com.vld.rest.model.AlcoholType;
import com.vld.rest.repository.AlcoholTypeRepository;
import com.vld.rest.service.AlcoholTypeService;

import java.util.List;

public class AlcoholTypeServiceImpl implements AlcoholTypeService {

    private final AlcoholTypeRepository alcoholTypeRepository = new AlcoholTypeRepository();

    @Override
    public AlcoholType get(Long id) {
        return alcoholTypeRepository.get(id);
    }

    @Override
    public List<AlcoholType> getAll() {
        return alcoholTypeRepository.getAll();
    }

    @Override
    public AlcoholType create(AlcoholType alcoholType) {
        return alcoholTypeRepository.create(alcoholType);
    }

    @Override
    public AlcoholType update(AlcoholType alcoholType) {
        return alcoholTypeRepository.update(alcoholType);
    }

    @Override
    public void delete(Long id) {
        alcoholTypeRepository.delete(id);
    }
}
