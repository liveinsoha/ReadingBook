package com.example.demo.web.service.search;

import com.example.demo.web.dto.response.BookSearchResponse;
import com.example.demo.web.service.TestInitClass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SearchServiceTest {

    @Autowired
    SearchService searchService;

    @Autowired
    TestInitClass TestInitClass;

    @Test
    void test() {

        TestInitClass.initReviewData(); // 리뷰 데이터

        /**
         * pageNumber : 요청하는 페이지 너버
         * pageSize : 한 페이지에 담을 컨텐츠 수
         */
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "createdTime");
        Page<BookSearchResponse> search = searchService.search("해리포터", pageRequest, new BookSearchCondition("price")); // 가격 순으로 검색
        print(search);
    }

    private void print(Page<BookSearchResponse> search){
        System.out.println("Page content:");
        search.getContent().forEach(System.out::println);

// 페이지 정보 출력
        System.out.println("Page information:");
        System.out.println("Page number: " + search.getNumber());
        System.out.println("Total elements: " + search.getTotalElements());
        System.out.println("Total pages: " + search.getTotalPages());
        System.out.println("Number of elements: " + search.getNumberOfElements());
        System.out.println("Size of page: " + search.getSize());
        System.out.println("Sorting: " + search.getSort());
        System.out.println("First page: " + search.isFirst());
        System.out.println("Last page: " + search.isLast());
        System.out.println("Has next page: " + search.hasNext());
        System.out.println("Has previous page: " + search.hasPrevious());

//        if (search.hasNext()) {
//            printNextPage(search.nextPageable());
//        }
    }

    private void printNextPage(Pageable nextPageable) {
        Page<BookSearchResponse> nextPage = searchService.search("해리포터", nextPageable, new BookSearchCondition("price"));
        print(nextPage);
    }
}