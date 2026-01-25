package com.example.developmentbooks.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketDTO {

    private Map<Long, Integer> bookIdToQuantityMap;

    public static BasketDTO buildBasketDTO(Map<Long, Integer> bookIdToQuantityMap) {
        return BasketDTO.builder()
                .bookIdToQuantityMap(bookIdToQuantityMap)
                .build();
    }
}
