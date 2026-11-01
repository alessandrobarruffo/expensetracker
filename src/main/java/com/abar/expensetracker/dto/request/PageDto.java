package com.abar.expensetracker.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PageDto<T>(List<T> content, @JsonProperty("total_count") Long totalCount){ }
