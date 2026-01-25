package com.example.developmentbooks.controller;

import com.example.developmentbooks.exception.NotFoundException;
import com.example.developmentbooks.exception.NotNullException;
import com.example.developmentbooks.model.dto.BasketDTO;
import com.example.developmentbooks.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get price for basket")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Basket processed, price computed",
                    content = @Content(
                            schema = @Schema(implementation = Double.class),
                            examples = {
                                    @ExampleObject(
                                            name = "sample-price",
                                            value = "{ 55.0 }"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Basket provided is invalid",
                    content = @Content(
                            schema = @Schema(anyOf = {NotFoundException.class, NotNullException.class}),
                            examples = {
                                    @ExampleObject(
                                            name = "sample-error",
                                            value = "{ Error message }"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/basket/price")
    public ResponseEntity<Double> getBasketPrice(@RequestBody BasketDTO basketDTO) {
        Double price = basketService.getPrice(basketDTO);

        return ResponseEntity.ok(price);
    }
}
