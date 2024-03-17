package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Author;
import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.AuthorRegisterRequest;
import com.example.demo.web.dto.request.AuthorUpdateRequest;
import com.example.demo.web.dto.response.AuthorSearchResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.AuthorRepository;
import com.example.demo.web.repository.BookAuthorListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthorManageService {
    private final AuthorRepository authorRepository;
    private final BookAuthorListRepository bookAuthorListRepository;

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

    @Transactional(readOnly = true)
    public List<AuthorSearchResponse> searchByAuthorName(String name) {
        List<Author> authors = authorRepository.findAllByName(name);

        return authors.stream()
                .map(a -> new AuthorSearchResponse(a.getId(), a.getName(), a.getBirthYear(), a.getGender().getKorean(), a.getAuthorOption().getKorean()))
                .collect(Collectors.toList());
    }

    /**
     * 작가 삭제 메소드
     * @param authorId
     * @return boolean
     */
    public boolean delete(Long authorId) {
        validateAuthorId(authorId);

        boolean isWrittenBooks = bookAuthorListRepository.existsByAuthorId(authorId);

        if(isWrittenBooks == true){
            throw new BaseException(BaseResponseCode.AUTHOR_BOOKS_EXIST);
        }

        Author author = findAuthorById(authorId);
        authorRepository.delete(author);
        return true;
    }

    @Transactional(readOnly = true)
    private void validateAuthorId(Long authorId) {
        if(authorId == null){
            throw new IllegalArgumentException("작가 아이디를 입력해주세요.");
        }
    }
}