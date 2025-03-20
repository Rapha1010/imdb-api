package com.globo.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieDto {

    private String movieId;

    private String movieName;

    private String storyLine;

    private String genres;

    private String directorName;

    private String castNames;

}