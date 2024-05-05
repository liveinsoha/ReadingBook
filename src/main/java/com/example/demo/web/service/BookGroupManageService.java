package com.example.demo.web.service;

import com.example.demo.utils.ImageUploadUtil;
import com.example.demo.web.domain.entity.BookGroup;
import com.example.demo.web.dto.request.BookGroupRegisterRequest;
import com.example.demo.web.dto.response.BookGroupSearchResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookGroupRepository;
import com.example.demo.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookGroupManageService {


    private final BookGroupRepository bookGroupRepository;

    private final BookRepository bookRepository;

    private final ImageUploadUtil imageUploadUtil;

    public Long registerBookGroup(BookGroupRegisterRequest request, MultipartFile file) {
        String title = request.getTitle();
        if (title == null || title.trim().equals("")) {
            throw new IllegalArgumentException("도서 그룹명을 입력해주세요.");
        }
        validateTitle(title);

        String savedImageName = imageUploadUtil.uploadImage(file);

        BookGroup bookGroup = BookGroup.createBookGroup(request, savedImageName);
        return bookGroupRepository.save(bookGroup).getId();
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().equals("")) {
            throw new IllegalArgumentException("도서 그룹명을 입력해주세요.");
        }
    }

    private void validateId(Long bookGroupId) {
        if (bookGroupId == null) {
            throw new IllegalArgumentException("도서 그룹 아이디를 입력해주세요");
        }
    }

    @Transactional(readOnly = true)
    public BookGroup findBookGroupById(Long bookGroupId) {
        return bookGroupRepository.findById(bookGroupId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOK_GROUP_NOT_FOUND));
    }

    public void updateBookGroupImage(MultipartFile file, Long bookGroupId) {
        validateId(bookGroupId);
        BookGroup bookGroup = findBookGroupById(bookGroupId);
        String existingImageName = bookGroup.getSavedImageName();

        String updatedImageName = imageUploadUtil.updateImage(file, existingImageName);
        bookGroup.updateImage(updatedImageName);

    }

    public void updateBookGroupTitle(String updatedTitle, Long bookGroupId) {
        validateTitle(updatedTitle);
        validateId(bookGroupId);
        BookGroup bookGroup = findBookGroupById(bookGroupId);

        bookGroup.updateTitle(updatedTitle);
    }

    /**
     * 도서 그룹 조회 메소드
     *
     * @param title
     * @return
     */
    @Transactional(readOnly = true)
    public List<BookGroupSearchResponse> searchByBookGroupTitle(String title) {
        List<BookGroup> bookGroups = bookGroupRepository.findByTitleContaining(title);
        return bookGroups.stream()
                .map(b -> new BookGroupSearchResponse(b.getId(), b.getTitle(), b.getSavedImageName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookGroupSearchResponse> searchAllBookGroup() {
        List<BookGroup> bookGroups = bookGroupRepository.findAll();
        return bookGroups.stream()
                .map(b -> new BookGroupSearchResponse(b.getId(), b.getTitle(), b.getSavedImageName()))
                .collect(Collectors.toList());
    }

    /**
     * 도서 그룹 삭제 메소드
     *
     * @param bookGroupId
     * @return isDeleted
     */
    public boolean delete(Long bookGroupId) {
        validateId(bookGroupId);

        boolean hasBooks = bookRepository.existsByBookGroupId(bookGroupId);

        if (hasBooks) {
            throw new BaseException(BaseResponseCode.SUBBOOKS_EXIST);
        }

        BookGroup bookGroup = findBookGroupById(bookGroupId);
        String savedImageName = bookGroup.getSavedImageName();
        imageUploadUtil.deleteImage(savedImageName);
        bookGroupRepository.delete(bookGroup);
        return true;
    }

}