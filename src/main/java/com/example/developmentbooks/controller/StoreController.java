package com.example.developmentbooks.controller;

import com.example.developmentbooks.model.dto.BasketDTO;
import com.example.developmentbooks.service.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final BasketService basketService;

    public StoreController(BasketService basketService) {
        this.basketService = basketService;
    }

    /*
        Pre: Receive valid basket
        Post: Return price of books in basket
     */
    @GetMapping("/basket/price")
    public ResponseEntity<Double> getBasketPrice(@RequestBody BasketDTO basketDTO) {
        Double price = basketService.getPrice(basketDTO);

        return ResponseEntity.ok(price);
    }
}
