package com.vld.rest.service;

import com.vld.rest.model.Alcohol;

import java.util.List;

public interface AlcoholService {

    Alcohol get(Long id);

    List<Alcohol> getAll();

    Alcohol create(Alcohol alcohol);

    Alcohol update(Alcohol alcohol);

    void delete(Long id);
}
