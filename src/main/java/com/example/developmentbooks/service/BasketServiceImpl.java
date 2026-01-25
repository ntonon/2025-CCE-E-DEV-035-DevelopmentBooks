package com.example.developmentbooks.service;

import com.example.developmentbooks.model.dto.BasketDTO;
import org.springframework.stereotype.Service;

@Service
public class BasketServiceImpl implements BasketService {

    /*
        Pre: Receive basket
        Post: Return price with applied discount of articles in basket

        Discount:
            - It only applies one time per article reference
            - It applies based on the number of different articles
            - Starts at 5% for 2 different articles
            - Increments of 5% for each new different article added
            - Stops at 25% for 5 different articles
     */
    @Override
    public double getPrice(BasketDTO basketDTO) {
        return -1.0;
    }
}
