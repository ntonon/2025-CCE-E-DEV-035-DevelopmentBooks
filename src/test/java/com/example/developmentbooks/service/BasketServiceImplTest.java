package com.example.developmentbooks.service;

import com.example.developmentbooks.exception.NotFoundException;
import com.example.developmentbooks.exception.NotNullException;
import com.example.developmentbooks.model.dto.BasketDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static com.example.developmentbooks.constant.Catalog.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BasketServiceImplTest {

    private static long UNKNOWN_BOOK_ID = 0L;

    BasketService basketService;

    @BeforeEach
    void setUp() {
        basketService = new BasketServiceImpl();
    }

    @Test
    @DisplayName("Get price for NULL basket")
    public void getPriceForNullBasket() {
        assertThrows(NotNullException.class, () -> basketService.getPrice(null));
    }

    @Test
    @DisplayName("Get price for basket with unknown book")
    public void getPriceForBasketWithUnknownBook() {
        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        CLEAN_ARCHITECTURE.getId(), 1,
                        TEST_DRIVEN_DEVELOPMENT_BY_EXAMPLE.getId(), 1,
                        UNKNOWN_BOOK_ID, 1
                )
        );

        assertThrows(NotFoundException.class, () -> basketService.getPrice(basketDTO));
    }

    @Test
    @DisplayName("Get price for empty basket")
    public void getPriceForEmptyBasket() {
        BasketDTO basketDTO = buildBasketDTO(Collections.emptyMap());

        Double result = basketService.getPrice(basketDTO);

        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Get price for basket with 1 book")
    public void getPriceForOneBook() {
        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        THE_CLEAN_CODER.getId(), 1
                )
        );

        Double result = basketService.getPrice(basketDTO);

        assertEquals(50.0, result);
    }

    @Test
    @DisplayName("Get price for basket with 2 same books")
    public void getPriceFor2SameBooks() {
        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        THE_CLEAN_CODER.getId(), 2
                )
        );

        Double result = basketService.getPrice(basketDTO);

        assertEquals(100.0, result);
    }

    @Test
    @DisplayName("Get price for basket with 2 different books")
    public void getPriceFor2DifferentBooks() {
        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        CLEAN_CODE.getId(), 1,
                        THE_CLEAN_CODER.getId(), 1
                )
        );

        Double result = basketService.getPrice(basketDTO);

        assertEquals(95.0, result);
    }

    @Test
    @DisplayName("Get price for basket with 3 different books")
    public void getPriceFor3DifferentBooks() {
        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        CLEAN_ARCHITECTURE.getId(), 1,
                        TEST_DRIVEN_DEVELOPMENT_BY_EXAMPLE.getId(), 1,
                        WORKING_EFFECTIVELY_WITH_LEGACY_CODE.getId(), 1
                )
        );

        Double result = basketService.getPrice(basketDTO);

        assertEquals(135.0, result);
    }

    @Test
    @DisplayName("Get price for basket with 4 different books")
    public void getPriceFor4DifferentBooks() {
        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        CLEAN_CODE.getId(), 1,
                        THE_CLEAN_CODER.getId(), 1,
                        CLEAN_ARCHITECTURE.getId(), 1,
                        TEST_DRIVEN_DEVELOPMENT_BY_EXAMPLE.getId(), 1
                )
        );

        Double result = basketService.getPrice(basketDTO);

        assertEquals(160.0, result);
    }

    @Test
    @DisplayName("Get price for basket with 5 different books")
    public void getPriceFor5DifferentBooks() {
        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        CLEAN_CODE.getId(), 1,
                        THE_CLEAN_CODER.getId(), 1,
                        CLEAN_ARCHITECTURE.getId(), 1,
                        TEST_DRIVEN_DEVELOPMENT_BY_EXAMPLE.getId(), 1,
                        WORKING_EFFECTIVELY_WITH_LEGACY_CODE.getId(), 1
                )
        );

        Double result = basketService.getPrice(basketDTO);

        assertEquals(187.5, result);
    }

    @Test
    @DisplayName("Get price for complex basket")
    public void getPriceForComplexBasket() {
        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        CLEAN_CODE.getId(), 2,
                        THE_CLEAN_CODER.getId(), 2,
                        CLEAN_ARCHITECTURE.getId(), 2,
                        TEST_DRIVEN_DEVELOPMENT_BY_EXAMPLE.getId(), 1,
                        WORKING_EFFECTIVELY_WITH_LEGACY_CODE.getId(), 1
                )
        );

        Double result = basketService.getPrice(basketDTO);

        assertEquals(320.0, result);
    }

    static BasketDTO buildBasketDTO(Map<Long, Integer> bookIdToQuantityMap) {
        return BasketDTO.builder()
                .bookIdToQuantityMap(bookIdToQuantityMap)
                .build();
    }
}
