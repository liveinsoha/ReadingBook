package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Author;
import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.AuthorRegisterRequest;
import com.example.demo.web.dto.request.AuthorUpdateRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthorManageService {
    private final AuthorRepository authorRepository;

    public Long registerAuthor(AuthorRegisterRequest request) {
        validateForm(request.getName(), request.getAuthorOption(), request.getNationality(), request.getDescription(), request.getBirthYear(), request.getGender());
        Author author = Author.createAuthor(request);
        return authorRepository.save(author).getId();
    }

    private static void validateForm(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        if(name == null || name.trim().equals("")){
            throw new BaseException(BaseResponseCode.EMPTY_NAME);
        }

        if(name.length() < 2 || name.length() > 24){
            throw new BaseException(BaseResponseCode.INVALID_NAME_LENGTH);
        }

        if(option == null){
            throw new BaseException(BaseResponseCode.EMPTY_OPTION);
        }

        if(nationality == null || nationality.trim().equals("")){
            throw new  BaseException(BaseResponseCode.EMPTY_NATIONALITY);
        }

        if(description == null || description.trim().equals("")){
            throw new  BaseException(BaseResponseCode.EMPTY_DESCRIPTION);
        }

        if(birthYear == null || birthYear.trim().equals("")){
            throw new BaseException(BaseResponseCode.EMPTY_BIRTH_YEAR);
        }

        if(!(birthYear.length() == 4)){
            throw new  BaseException(BaseResponseCode.INVALID_FORM_BIRTH_YEAR);
        }

        birthYear = birthYear.substring(0, 2);
        if(!(birthYear.equals("19") || birthYear.equals("20"))){
            throw new  BaseException(BaseResponseCode.INVALID_BIRTH_YEAR_FORMAT);
        }

        if(gender == null){
            throw new  BaseException(BaseResponseCode.EMPTY_GENDER);
        }
    }

    public void updateAuthor(AuthorUpdateRequest request, Long authorId) {
        Author author = findAuthorById(authorId);

        validateForm(request.getName(), request.getAuthorOption(), request.getNationality(), request.getDescription(), request.getBirthYear(), request.getGender());

        author.updateAuthor(request);
    }

    public Author findAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.AUTHOR_NOT_FOUND));
    }
}