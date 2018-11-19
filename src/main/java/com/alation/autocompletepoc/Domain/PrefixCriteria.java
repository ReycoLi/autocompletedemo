package com.alation.autocompletepoc.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrefixCriteria implements Serializable {
    @JsonProperty("Prefix")
    private String prefix;
}