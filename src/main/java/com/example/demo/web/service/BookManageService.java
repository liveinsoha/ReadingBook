package com.example.demo.web.service;

import com.example.demo.utils.ImageUploadUtil;
import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookGroup;
import com.example.demo.web.domain.entity.Category;
import com.example.demo.web.dto.request.BookRegisterRequest;
import com.example.demo.web.dto.request.BookUpdateRequest;
import com.example.demo.web.dto.response.BookUpdateResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookManageService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final ImageUploadUtil imageUploadUtil;
    private final BookGroupManageService bookGroupManagementService;

    /**
     * 도서 등록 메소드
     * @param request
     * @param file
     * @return bookId
     */
    public Long registerBook(BookRegisterRequest request, MultipartFile file) {

        validateForm(
                request.getTitle(), request.getIsbn(), request.getPublisher(),
                request.getPublishingDate(), request.getEbookPrice(), request.getCategoryId(),
                request.getBookGroupId()
        );

        String savedImageName = imageUploadUtil.uploadImage(file);

        Category category = getCategory(request.getCategoryId());
        BookGroup bookGroup = getBookGroup(request.getBookGroupId());
        Book book = Book.createBook(request, category, bookGroup, savedImageName);

        return bookRepository.save(book).getId();
    }

    @Transactional(readOnly = true)
    public Book findBookById(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOK_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public BookUpdateResponse searchUpdateBook(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);

        if(book.isEmpty()){
            return null;
        }

        return book.map(b -> new BookUpdateResponse(b.getId(), b.getTitle(), b.getIsbn(), b.getPublisher(),
                b.getPublishingDate(), b.getPaperPrice(), b.getEbookPrice(), b.getDiscountRate(), b.getSavedImageName(),
                b.getCategory().getId(), b.getBookGroup().getId())).get();
    }

    @Transactional(readOnly = true)
    private Category getCategory(Long categoryId) {
        Category category = categoryService.findCategory(categoryId);
        return category;
    }
    @Transactional(readOnly = true)
    private BookGroup getBookGroup(Long bookGroupId) {
        BookGroup bookGroup = null;
        if(bookGroupId != null){
            bookGroup = bookGroupManagementService.findBookGroupById(bookGroupId);
        }
        return bookGroup;
    }

    private void validateForm(String title, String isbn, String publisher, String publishingDate,
                              int ebookPrice, Long categoryId, Long bookGroupId) {

        if(title.trim().equals("") || title == null){
            throw new IllegalArgumentException("제목을 입력해주세요");
        }

        if(isbn.trim().equals("") || isbn == null){
            throw new IllegalArgumentException("isbn을 입력해주세요");
        }

        if(publisher.trim().equals("") || publisher == null){
            throw new IllegalArgumentException("출판사를 입력해주세요");
        }

        if(publishingDate.trim().equals("") || publishingDate == null){
            throw new IllegalArgumentException("출판일을 입력해주세요");
        }

        if(ebookPrice == 0){
            throw new IllegalArgumentException("e-book 판매 가격을 입력해주세요");
        }

        if(categoryId == null){
            throw new IllegalArgumentException("카테고리 아이디를 입력해주세요");
        }

    }

    /**
     * 도서의 이미지 수정 메소드
     * @param file
     * @param bookId
     */
    public void updateBookImage(MultipartFile file, Long bookId) {
        Book book = findBookById(bookId);
        String existingImageName = book.getSavedImageName();
        String updatedImageName = imageUploadUtil.updateImage(file, existingImageName);

        book.updateImage(updatedImageName);
    }

    /**
     * 도서의 내용 수정 메소드
     * @param request
     * @param bookId
     */
    public void updateBookContent(BookUpdateRequest request, Long bookId) {
        validateForm(
                request.getTitle(), request.getIsbn(), request.getPublisher(),
                request.getPublishingDate(), request.getEbookPrice(), request.getCategoryId(),
                request.getBookGroupId()
        );

        Book book = findBookById(bookId);

        Category category = getCategory(request.getCategoryId());
        BookGroup bookGroup = getBookGroup(request.getBookGroupId());

        book.updateContent(request, category, bookGroup);
    }
}
