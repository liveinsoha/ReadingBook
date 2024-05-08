package com.example.demo.web.service;

import com.example.demo.web.repository.BookMemberMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class BookMemberMapService {

    private final BookMemberMapRepository bookMemberMapRepository;


}
