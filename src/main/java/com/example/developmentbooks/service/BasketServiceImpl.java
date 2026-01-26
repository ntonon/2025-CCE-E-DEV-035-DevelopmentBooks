package com.example.developmentbooks.service;

import com.example.developmentbooks.constant.Catalog;
import com.example.developmentbooks.exception.NotFoundException;
import com.example.developmentbooks.exception.NotNullException;
import com.example.developmentbooks.model.dto.BasketDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

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
        Pre: Basket containing books
        Post: Return price with applied discount of books in basket
        Throws:
            - NotNullException if basket is null
            - NotFoundException if book is unknown

        Discount:
            - It only applies one time per book reference
            - It applies based on the number of different books
     */
    @Override
    public double getPrice(BasketDTO basketDTO) {
        if (isNull(basketDTO) || isNull(basketDTO.getBookIdToQuantityMap())) {
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
        // As 4 and 5 have the best discount, we only need to take the minimum between the two solutions to find the optimized answer
        return Math.min(getPriceWithMaxDifferentBooksNum(basketDTO, 4), getPriceWithMaxDifferentBooksNum(basketDTO, 5));
    }

    private double getPriceWithMaxDifferentBooksNum(BasketDTO basketDTO, int maxDifferentBooksNum) {
        HashMap<Long, Integer> booksToProcess = new HashMap<>(basketDTO.getBookIdToQuantityMap()); //Make modifiable copy of input

        double total = 0.0;
        boolean bookToProcess = true; //Is there still books we need to process in the basket?
        while (bookToProcess) {
            Pair<Boolean, Double> rotationResult = processBooks(booksToProcess, maxDifferentBooksNum);

            bookToProcess = rotationResult.getLeft();
            total += rotationResult.getRight();
        }

        return total;
    }

    /*
        Pre: A modifiable map of book's ids and their respective quantity and the maximum number different books allowed in one set
        Post: A tuple with
            - 1 : bookToProcess
            - 2 : the discounted price for the processed books in the iteration

        On each call, this method process one set of books (according to the maximum number of different books allowed in one set),
        compute the price and remove them from the map
     */
    private Pair<Boolean, Double> processBooks(HashMap<Long, Integer> booksToProcess, int maxDifferentBooksNum) {
        boolean bookToProcess = false;

        double priceSum = 0.0;
        int differentBooksNumInRotation = 0;
        for (Long id : booksToProcess.keySet()) {
            Double price = bookIdToPriceCacheMap.get(id);
            Integer quantity = booksToProcess.get(id);

            //Process book
            if (quantity > 0) {
                if (differentBooksNumInRotation < maxDifferentBooksNum) {
                    priceSum += price;

                    quantity--;
                    differentBooksNumInRotation++;

                    booksToProcess.put(id, quantity); //Update quantity in map
                }

                bookToProcess = bookToProcess || quantity > 0; //Keep going if in the current iteration there is at least one book with more than 1 quantity
            }
        }

        return Pair.of(bookToProcess, computeSalePercentage(differentBooksNumInRotation) * priceSum);
    }

    private double computeSalePercentage(int numberOfDifferentBooks) {
        Double salePercentage = BOOK_QUANTITY_TO_SALE_PERCENTAGE.get(numberOfDifferentBooks);

        if (salePercentage == null) {
            return 1.0; //This quantity has no reduction
        }

        return salePercentage;
    }
}
