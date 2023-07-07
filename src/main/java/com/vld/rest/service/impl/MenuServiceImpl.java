package com.vld.rest.service.impl;

import com.vld.rest.model.Alcohol;
import com.vld.rest.repository.MenuRepository;
import com.vld.rest.service.MenuService;

import java.util.List;

public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository = new MenuRepository();

    @Override
    public List<Alcohol> getBarAlcohol(Long barId) {
        return menuRepository.getBarAlcohol(barId);
    }

    @Override
    public void addAlcoholToBar(Long barId, Long alcoholId) {
        menuRepository.addAlcoholToBar(barId, alcoholId);
    }

    @Override
    public void deleteAlcoholFromBar(Long barId, Long alcoholId) {
        menuRepository.deleteAlcoholFromBar(barId, alcoholId);
    }
}
