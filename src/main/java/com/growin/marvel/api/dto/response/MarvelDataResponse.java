package com.growin.marvel.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelDataResponse {
    private Integer offset;
    private Integer limit;
    private Integer total;
    private Integer count;
    private List<CharacterDataResponse> results;
}
