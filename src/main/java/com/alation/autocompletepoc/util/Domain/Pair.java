package com.alation.autocompletepoc.util.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class Pair implements Serializable {
    private final String name;
    private final Integer score;
}
