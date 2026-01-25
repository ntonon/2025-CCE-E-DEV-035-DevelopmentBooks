package com.example.developmentbooks.service;

import com.example.developmentbooks.constant.Catalog;
import com.example.developmentbooks.exception.NotFoundException;
import com.example.developmentbooks.exception.NotNullException;
import com.example.developmentbooks.model.dto.BasketDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class BasketServiceImpl implements BasketService {

    final static Map<Integer, Double> BOOK_QUANTITY_TO_SALE_PERCENTAGE =
            Map.of(
                    2, 0.95,
                    3, 0.90,
                    4, 0.80,
                    5, 0.75
            );

    Map<Long, Double> bookIdToPriceCacheMap;

    public BasketServiceImpl() {
        bookIdToPriceCacheMap = Arrays
                .stream(Catalog.values())
                .collect(
                        Collectors.toMap(
                                Catalog::getId,
                                Catalog::getPrice
                        )
                );
    }

    /*
        Pre: Basket
        Post: Return price with applied discount of books in basket
        Throws:
            - NotNullException if basket is null
            - NotFoundException if unknown book is found

        Discount:
            - It only applies one time per book reference
            - It applies based on the number of different books
     */
    @Override
    public double getPrice(BasketDTO basketDTO) {
        if(isNull(basketDTO) || isNull(basketDTO.getBookIdToQuantityMap())) {
            throw new NotNullException("Basket cannot be null");
        }

        Set<Long> basketIdSet = new HashSet<>(basketDTO.getBookIdToQuantityMap().keySet());
        basketIdSet.removeAll(bookIdToPriceCacheMap.keySet());
        if (!basketIdSet.isEmpty()) {
            throw new NotFoundException("Book(s) with id " +
                    basketIdSet.stream().map(String::valueOf).collect(Collectors.joining(", ")) +
                    " cannot be found in the catalog");
        }

        // Because the discount is evolutive with a gap between 2;3 and 4;5 discount's percentage, the overall discount is not linear.
        // As 4 and 5 have the best discount, we only need to choose between them to find the optimized answer
        return Math.min(getPriceWithMaxDifferentBooksNum(basketDTO, 4), getPriceWithMaxDifferentBooksNum(basketDTO, 5));
    }

    private double getPriceWithMaxDifferentBooksNum(BasketDTO basketDTO, int maxDifferentBooksNum) {
        HashMap<Long, Integer> booksToProcess = new HashMap<>(basketDTO.getBookIdToQuantityMap()); //Make modifiable copy of input

        double total = 0.0;
        boolean bookToProcess = true; //Is there still books we need to process in the basket?
        while(bookToProcess) {
            Tuple<Boolean, Double> rotationResult = processBooks(booksToProcess, maxDifferentBooksNum);

            bookToProcess = rotationResult._1();
            total += rotationResult._2();
        }

        return total;
    }

    /*
        Pre: A modifiable map of book's ids and their respective quantity
        Post: A tuple with
            - 1 : bookToProcess
            - 2 : the price with discount of the processed book in the rotation

        On each call, this method process one set of books (considering the maximum) and remove them from the map
     */
    private Tuple<Boolean, Double> processBooks(HashMap<Long, Integer> booksToProcess, int maxDifferentBooksNum) {
        boolean bookToProcess = false;

        double priceSum = 0.0;
        int differentBooksNumInRotation = 0;
        for (Long id : booksToProcess.keySet()) {
            Double price = bookIdToPriceCacheMap.get(id);
            Integer quantity = booksToProcess.get(id);

            //Process book
            if (quantity > 0) {
                if(differentBooksNumInRotation < maxDifferentBooksNum) {
                    priceSum += price;

                    quantity--;
                    differentBooksNumInRotation++;

                    booksToProcess.put(id, quantity); //Update quantity in map
                }

                bookToProcess = bookToProcess || quantity > 0; //Keep ongoing if in the current rotation there is at least one book with more than 1 quantity
            }
        }

        return new Tuple<>(bookToProcess, computeSalePercentage(differentBooksNumInRotation) * priceSum);
    }

    private double computeSalePercentage(int numberOfDifferentBooks) {
        Double salePercentage = BOOK_QUANTITY_TO_SALE_PERCENTAGE.get(numberOfDifferentBooks);

        if(salePercentage == null) {
            return 1.0; //Quantity has no reduction
        }

        return salePercentage;
    }
}
