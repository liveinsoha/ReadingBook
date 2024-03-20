package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Library;
import lombok.Data;
import lombok.Getter;

@Data
public class LibraryResponse { //구매 목록
    private Long bookId;
    private String title;
    private String savedImageName;

    public LibraryResponse(Library library) {
        this.bookId = library.getBook().getId();
        this.title = library.getBook().getTitle();
        this.savedImageName = library.getBook().getSavedImageName();
    }
}