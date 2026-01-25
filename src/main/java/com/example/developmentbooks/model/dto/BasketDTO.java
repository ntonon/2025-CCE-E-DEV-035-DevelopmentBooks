package com.example.developmentbooks.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BasketDTO {

    private List<BookDTO> bookDTOList;
}
