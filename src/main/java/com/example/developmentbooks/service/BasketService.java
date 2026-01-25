package com.example.developmentbooks.service;

import com.example.developmentbooks.model.dto.BasketDTO;
import org.springframework.stereotype.Service;

@Service
public interface BasketService {

    Double getPrice(BasketDTO basketDTO);
}
