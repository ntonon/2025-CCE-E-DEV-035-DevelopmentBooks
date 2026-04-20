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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final BasketService basketService;

    public StoreController(BasketService basketService) {
        this.basketService = basketService;
    }

    /*
        Pre: Basket containing books
        Post:
            200 - Return price of books in basket
            400 - Invalid basket : book(s) not found or null basket

     */
    @Operation(summary = "Get price for basket",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The basket and its products. A map of book id and quantity.",
                    content = @Content(
                            schema = @Schema(implementation = BasketDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Simple basket",
                                            summary = "Simple basket with only 1 'Clean Code' books.",
                                            value = """
                                                    {
                                                        "bookIdToQuantityMap": {
                                                            "1": 1
                                                        }
                                                    }"""
                                    ),
                                    @ExampleObject(
                                            name = "Complex basket",
                                            summary = "Complex basket with " +
                                                    "2 'Clean Code' books, " +
                                                    "2 'The Clean Coder' books, " +
                                                    "2 'Clean Architecture' books, " +
                                                    "1 'Test Driven Development' book " +
                                                    "and 1 'Working Effectively' book.",
                                            value = """
                                                    {
                                                        "bookIdToQuantityMap": {
                                                            "1": 2,
                                                            "2": 2,
                                                            "3": 2,
                                                            "4": 1,
                                                            "5": 1
                                                        }
                                                    }"""
                                    ),
                                    @ExampleObject(
                                            name = "Invalid basket",
                                            summary = "Invalid basket with unknown book.",
                                            value = """
                                                    {
                                                        "bookIdToQuantityMap": {
                                                            "6": 2
                                                        }
                                                    }"""
                                    )
                            }
                    ),

                    required = true
            )
    )
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
    @PostMapping(value = "/basket/price", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getBasketPrice(@RequestBody BasketDTO basketDTO) {
        Double price = basketService.getPrice(basketDTO);

        return ResponseEntity.ok(price);
    }
}
