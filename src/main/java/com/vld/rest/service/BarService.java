package com.vld.rest.service;

import com.vld.rest.model.Bar;

import java.util.List;

public interface BarService {

    Bar get(Long id);

    List<Bar> getAll();

    Bar create(Bar bar);

    Bar update(Bar bar);

    void delete(Long id);
}
