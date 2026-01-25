package com.example.developmentbooks.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BasketDTO {

    private Map<Long, Integer> bookIdToQuantityMap;
}
