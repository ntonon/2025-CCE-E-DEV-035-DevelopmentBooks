package com.example.developmentbooks.constant;

import com.example.developmentbooks.model.dto.BookDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Catalog {

    CLEAN_CODE(1L, "Clean Code", "Robert Martin", 2008, 50.0),
    THE_CLEAN_CODER(2L, "The Clean Coder", "Robert Martin", 2011, 50.0),
    CLEAN_ARCHITECTURE(3L, "Clean Architecture", "Robert Martin", 2017, 50.0),
    TEST_DRIVEN_DEVELOPMENT_BY_EXAMPLE(4L, "Test Driven Development by Example", "Kent Beck", 2003, 50.0),
    WORKING_EFFECTIVELY_WITH_LEGACY_CODE(5L, "Working Effectively With Legacy Code", "Michael C. Feathers", 2004, 50.0);

    final Long id;
    final String title;
    final String author;
    final Integer creationYear;
    final Double price;

    public BookDTO getBookDTO() {
        return BookDTO.builder()
                .id(id)
                .title(title)
                .author(author)
                .creationYear(creationYear)
                .price(price)
                .build();
    }
}
