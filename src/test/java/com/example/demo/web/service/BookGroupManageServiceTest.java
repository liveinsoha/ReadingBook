package com.example.demo.web.service;

import com.example.demo.utils.ImageUploadUtil;
import com.example.demo.web.domain.entity.BookGroup;
import com.example.demo.web.dto.request.BookGroupRegisterRequest;
import com.example.demo.web.dto.response.BookGroupSearchResponse;
import com.example.demo.web.repository.BookContentRepository;
import com.example.demo.web.repository.BookGroupRepository;
import com.example.demo.web.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Transactional
@SpringBootTest
class BookGroupManageServiceTest {

    @Autowired
    private BookGroupRepository bookGroupRepository;

    @Autowired
    private BookRepository bookRepository;

    private BookGroupManageService bookGroupManagementService;

    private ImageUploadUtil imageUploadUtil;


    @BeforeEach
    void beforeEach(){
        imageUploadUtil = Mockito.mock(ImageUploadUtil.class);
        bookGroupManagementService = getBookGroupManagementService();
    }

    @Test
    void whenTitleIsNullOrBlank_thenThrowException(){
        MockMultipartFile file = getMockMultipartFile();

        BookGroupRegisterRequest nullRequest = new BookGroupRegisterRequest(null);

        Assertions.assertThatThrownBy(() -> bookGroupManagementService.registerBookGroup(nullRequest, file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도서 그룹명을 입력해주세요.");

        BookGroupRegisterRequest blankRequest = new BookGroupRegisterRequest("");

        Assertions.assertThatThrownBy(() -> bookGroupManagementService.registerBookGroup(blankRequest, file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도서 그룹명을 입력해주세요.");
    }

    @Test
    void whenBookGroupRegistered_thenVerifyIsRegistered(){
        MockMultipartFile file = getMockMultipartFile();
        BookGroupRegisterRequest request = new BookGroupRegisterRequest("홍길동전");

        Long bookGroupId = bookGroupManagementService.registerBookGroup(request, file);

        BookGroup bookGroup = bookGroupManagementService.findBookGroupById(bookGroupId);

        Assertions.assertThat(bookGroup.getId()).isEqualTo(bookGroupId);
        Assertions.assertThat(bookGroup.getTitle()).isEqualTo("홍길동전");
        verify(imageUploadUtil).uploadImage(file);
    }

    @Test
    void whenBookGroupTitleUpdated_thenVerifyChangingTitle(){
        MockMultipartFile file = getMockMultipartFile();
        BookGroupRegisterRequest request = new BookGroupRegisterRequest("홍길동전");

        Long bookGroupId = bookGroupManagementService.registerBookGroup(request, file);

        bookGroupManagementService.updateBookGroupTitle("어린왕자", bookGroupId);

        BookGroup bookGroup = bookGroupManagementService.findBookGroupById(bookGroupId);
        Assertions.assertThat(bookGroup.getTitle()).isEqualTo("어린왕자");
    }

    @Test
    void whenBookGroupSearched_thenVerifyFields(){
        MockMultipartFile file = getMockMultipartFile();
        BookGroupRegisterRequest request = new BookGroupRegisterRequest("홍길동전");
        bookGroupManagementService.registerBookGroup(request, file);

        List<BookGroupSearchResponse> responses = bookGroupManagementService.searchByBookGroupTitle("홍길동전");

        Assertions.assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    void whenBookGroupDeleted_thenVerifyIsDeleted(){
        MockMultipartFile file = getMockMultipartFile();
        BookGroupRegisterRequest request = new BookGroupRegisterRequest("홍길동전");
        Long bookGroupId = bookGroupManagementService.registerBookGroup(request, file);

        boolean isDeleted = bookGroupManagementService.delete(bookGroupId);

        Assertions.assertThat(isDeleted).isTrue();
    }

    private BookGroupManageService getBookGroupManagementService() {
        imageUploadUtil = mock(ImageUploadUtil.class);
        BookGroupManageService bookGroupManagementService = new BookGroupManageService(bookGroupRepository, bookRepository, imageUploadUtil);
        return bookGroupManagementService;
    }

    private static MockMultipartFile getMockMultipartFile() {
        return new MockMultipartFile(
                "홍길동전 썸네일 이미지",
                "thumbnail.png",
                MediaType.IMAGE_PNG_VALUE,
                "thumbnail".getBytes()
        );
    }
}