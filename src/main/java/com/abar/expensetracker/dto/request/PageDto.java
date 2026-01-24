package com.abar.expensetracker.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PageDto<T> {

    private List<T> content;

    @JsonProperty("total_count")
    private Integer totalCount;

}
