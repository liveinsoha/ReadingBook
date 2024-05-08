package com.example.demo.web.dto.response.paging;


import com.example.demo.web.dto.response.RequestedBookDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PagingRequestBookDto extends PagingDto{
    // 최초 페이지 번호
    private int firstPageNumber;
    // 마지막 페이지 번호
    private int lastPageNumber;

    private List<Integer> pageGroupNumbers = new ArrayList<>();

    public PagingRequestBookDto(Page<RequestedBookDto> responses) {
        // Page 인터페이스의 페이지 총 수량 구함
        int totalPages = responses.getTotalPages();

        pageGroupSize = MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP;

        totalPageGroups = calculateTotalPageGroups(totalPages);
        pageGroupNumber = calculatePageGroupNumber(responses.getNumber());

        startPageNumberInThisPageGroup = calculateStartPageNumber();
        lastPageNumberInThisPageGroup = calculateLastPageNumber(totalPages, startPageNumberInThisPageGroup);

        prevPageNumber = responses.getNumber() - 1;
        nextPageNumber = responses.getPageable().getPageNumber() + 1;

        firstPageNumber = 0;
        // 페이지는 0부터 시작하므로 -을 해줘야함.
        lastPageNumber = responses.getTotalPages() - 1;
    }
}
