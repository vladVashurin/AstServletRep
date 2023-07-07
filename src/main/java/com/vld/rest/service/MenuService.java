package com.vld.rest.service;

import com.vld.rest.model.Alcohol;
import com.vld.rest.model.Bar;

import java.util.List;

public interface MenuService {

    List<Alcohol> getBarAlcohol(Long barId);

    void addAlcoholToBar(Long barId, Long alcoholId);

    void deleteAlcoholFromBar(Long barId, Long alcoholId);
}
