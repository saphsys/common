package com.saphsys.string.batchfilter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterDto {
    @JsonProperty
    String type;
    @JsonProperty
    String length;
    @JsonProperty
    String regex;
    @JsonProperty
    String replace;
    @JsonProperty
    String flags;
}
