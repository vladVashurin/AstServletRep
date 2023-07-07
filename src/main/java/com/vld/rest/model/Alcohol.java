package com.vld.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alcohol {

    private Long id;
    private String name;
    private AlcoholType alcoholType;
}
