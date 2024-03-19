package com.example.demo.web.dto.response;

import lombok.Getter;

@Getter
public class BookInformationResponse {
    private BookDto bookDto;
    private AuthorDto authorDto;

    public BookInformationResponse(BookDto bookDto, AuthorDto authorDto) {
        this.bookDto = bookDto;
        this.authorDto = authorDto;
    }

}