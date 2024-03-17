package com.example.demo.web.service;

import com.example.demo.utils.ImageUploadUtil;
import com.example.demo.web.domain.entity.BookGroup;
import com.example.demo.web.dto.request.BookGroupRegisterRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookGroupManageService {

    private final BookGroupRepository bookGroupRepository;
    private final ImageUploadUtil imageUploadUtil;

    public Long registerBookGroup(BookGroupRegisterRequest request, MultipartFile file) {
        String title = request.getTitle();
        if(title == null || title.trim().equals("")){
            throw new IllegalArgumentException("도서 그룹명을 입력해주세요.");
        }

        String savedImageName = imageUploadUtil.uploadImage(file);

        BookGroup bookGroup = BookGroup.createBookGroup(request, savedImageName);
        return bookGroupRepository.save(bookGroup).getId();
    }

    public BookGroup findBookGroupById(Long bookGroupId){
        return bookGroupRepository.findById(bookGroupId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOK_GROUP_NOT_FOUND));
    }

    public void updateBookGroupImage(MultipartFile file, Long bookGroupId) {
        BookGroup bookGroup = findBookGroupById(bookGroupId);
        String existingImageName = bookGroup.getSavedImageName();

        String updatedImageName = imageUploadUtil.updateImage(file, existingImageName);
        bookGroup.updateImage(updatedImageName);
    }

    public void updateBookGroupTitle(String updatedTitle, Long bookGroupId) {
        BookGroup bookGroup = findBookGroupById(bookGroupId);

        bookGroup.updateTitle(updatedTitle);
    }
}