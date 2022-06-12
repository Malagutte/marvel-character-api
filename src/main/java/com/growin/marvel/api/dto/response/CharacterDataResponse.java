package com.growin.marvel.api.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterDataResponse {
    private Integer id;
    private String name;
    private String description;
    private MarvelThumbnailResponse thumbnail;
}
