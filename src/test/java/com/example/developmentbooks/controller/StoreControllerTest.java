package com.example.developmentbooks.controller;

import com.example.developmentbooks.exception.NotFoundException;
import com.example.developmentbooks.exception.NotNullException;
import com.example.developmentbooks.model.dto.BasketDTO;
import com.example.developmentbooks.service.BasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StoreControllerTest {

    private StoreController storeController;
    private BasketService basketService;

    @BeforeEach
    void setUp() {
        basketService = mock(BasketService.class);

        storeController = new StoreController(basketService);
    }

    @Test
    @DisplayName("Get basket price for null basket")
    public void getBasketPriceForNullBasket() {
        BasketDTO basketDTO = null;

        when(basketService.getPrice(basketDTO)).thenThrow(NotNullException.class);

        ResponseEntity<Double> result = storeController.getBasketPrice(basketDTO);

        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Get basket price for basket with invalid books")
    public void getBasketPriceForBasketWithInvalidBooks() {
        BasketDTO basketDTO = mock(BasketDTO.class);

        when(basketService.getPrice(basketDTO)).thenThrow(NotFoundException.class);

        ResponseEntity<Double> result = storeController.getBasketPrice(basketDTO);

        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Get basket price for basket")
    public void getBasketPriceForBasket() {
        BasketDTO basketDTO = mock(BasketDTO.class);

        when(basketService.getPrice(basketDTO)).thenReturn(277.5);

        ResponseEntity<Double> result = storeController.getBasketPrice(basketDTO);

        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), 277.5);
    }
}