package com.vld.rest.service;

import com.vld.rest.model.AlcoholType;
import com.vld.rest.model.Bar;

import java.util.List;

public interface AlcoholTypeService {

    AlcoholType get(Long id);

    List<AlcoholType> getAll();

    AlcoholType create(AlcoholType alcoholType);

    AlcoholType update(AlcoholType alcoholType);

    void delete(Long id);
}
