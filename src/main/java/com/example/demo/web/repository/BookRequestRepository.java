package com.example.demo.web.repository;

import com.example.demo.web.dto.response.RequestedBookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRequestRepository {

    Page<RequestedBookDto> findRequestedBooks(Pageable pageable);
}
