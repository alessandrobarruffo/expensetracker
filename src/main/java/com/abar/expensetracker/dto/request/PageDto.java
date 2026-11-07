package com.abar.expensetracker.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PageDto<T>(List<T> content, @JsonProperty("total_count") Long totalCount) {}
