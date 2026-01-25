package com.example.developmentbooks.controller;

import com.example.developmentbooks.exception.NotFoundException;
import com.example.developmentbooks.exception.NotNullException;
import com.example.developmentbooks.model.dto.BasketDTO;
import com.example.developmentbooks.service.BasketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.example.developmentbooks.constant.Catalog.THE_CLEAN_CODER;
import static com.example.developmentbooks.model.dto.BasketDTO.buildBasketDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
public class StoreControllerSemiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BasketService basketService;

    @Test
    @DisplayName("Get basket price for null basket")
    public void getBasketPriceForNullBasket() throws Exception {
        NotNullException notNullException = new NotNullException("Custom message");

        when(basketService.getPrice(any())).thenThrow(notNullException);

        mockMvc.perform(get("/store/basket/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(notNullException.getMessage()));

        verify(basketService, times(1)).getPrice(any());
    }

    @Test
    @DisplayName("Get basket price for basket with invalid books")
    public void getBasketPriceForBasketWithInvalidBooks() throws Exception {
        NotFoundException notFoundException = new NotFoundException("Custom message");

        when(basketService.getPrice(any())).thenThrow(notFoundException);

        mockMvc.perform(get("/store/basket/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(notFoundException.getMessage()));

        verify(basketService, times(1)).getPrice(any());
    }

    @Test
    @DisplayName("Get basket price for mock basket")
    public void getBasketPriceForMockBasket() throws Exception {
        Double price = 277.5;

        when(basketService.getPrice(any())).thenReturn(price);

        mockMvc.perform(get("/store/basket/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(String.valueOf(price)));

        verify(basketService, times(1)).getPrice(any());
    }

    @Test
    @DisplayName("Get basket price for deserialized basket")
    public void getBasketPriceForDeserializedBasket() throws Exception {
        Double price = 50.0;

        BasketDTO basketDTO = buildBasketDTO(
                Map.of(
                        THE_CLEAN_CODER.getId(), 1
                )
        );

        when(basketService.getPrice(basketDTO)).thenReturn(price);

        mockMvc.perform(get("/store/basket/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(basketDTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(String.valueOf(price)));

        verify(basketService, times(1)).getPrice(any());
    }
}