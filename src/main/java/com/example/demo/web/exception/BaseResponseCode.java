package com.example.demo.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum BaseResponseCode {

    /**
     * 200
     */
    OK(HttpStatus.OK, "요청 성공하였습니다."),


    /**
     * 400
     */
    INVALID_FORM_EMAIL(HttpStatus.BAD_REQUEST, "이메일을 올바르게 입력해주세요."),
    INVALID_FORM_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다."),
    INVALID_FORM_NAME(HttpStatus.BAD_REQUEST, "이름을 올바르게 입력해주세요."),
    INVALID_FORM_BIRTH_YEAR(HttpStatus.BAD_REQUEST, "생년을 올바르게 입력해주세요."),
    INVALID_FORM_PHONE_NO(HttpStatus.BAD_REQUEST, "핸드폰 번호를 올바르게 입력해주세요."),
    INVALID_FORM_GENDER(HttpStatus.BAD_REQUEST, "성별을 올바르게 입력해주세요."),


    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    USER_DUPLICATE(HttpStatus.BAD_REQUEST, "중복된 사용자가 있습니다"),
    AUTHORIZATION_NOT_VALID(HttpStatus.BAD_REQUEST, "인증정보가 일치하지 않습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다. 다시 입력해주세요."),

    EMPTY_NAME(HttpStatus.BAD_REQUEST, "이름을 입력해주세요."),
    INVALID_NAME_LENGTH(HttpStatus.BAD_REQUEST, "이름을 올바르게 입력해주세요."),
    EMPTY_OPTION(HttpStatus.BAD_REQUEST, "옵션을 선택해주세요."),
    EMPTY_NATIONALITY(HttpStatus.BAD_REQUEST, "국적을 입력해주세요."),
    EMPTY_DESCRIPTION(HttpStatus.BAD_REQUEST, "설명을 입력해주세요."),
    EMPTY_BIRTH_YEAR(HttpStatus.BAD_REQUEST, "생년을 입력해주세요."),
    INVALID_BIRTH_YEAR_LENGTH(HttpStatus.BAD_REQUEST, "생년을 올바르게 입력해주세요."),
    INVALID_BIRTH_YEAR_FORMAT(HttpStatus.BAD_REQUEST, "생년을 올바르게 입력해주세요."),
    EMPTY_GENDER(HttpStatus.BAD_REQUEST, "성별을 입력해주세요."),
    DUPLICATE_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리명입니다."),
    SUBCATEGORIES_EXIST(HttpStatus.BAD_REQUEST, "해당 카테고리 그룹 아래 하위 카테고리들이 존재합니다. 하위 카테고리를 모두 삭제한 다음에 카테고리 그룹을 삭제해주세요."),
    AUTHOR_BOOKS_EXIST(HttpStatus.BAD_REQUEST, "해당 작가에 도서가 등록되었습니다. 하위 도서를 모두 삭제한 다음에 작가을 삭제해주세요.")
,



    /**
     * 401
     */
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    NO_TOKEN_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),



    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "검색되는 카테고리가 없습니다. 카테고리 아이디를 다시 확인해주세요."),
    CATEGORY_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요."),
    /**
     * 404
     */
    BOOK_AUTHOR_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "검색되는 결과가 없습니다. 도서 아이디와 작가 아이디를 다시 확인해주세요."),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "검색되는 도서가 없습니다. 도서 아이디를 다시 확인해주세요."),
    BOOK_GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "검색되는 도서 그룹이 없습니다. 도서 그룹 아이디를 다시 확인해주세요."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    AUTHOR_NOT_FOUND(HttpStatus.NOT_FOUND, "작가를 찾을 수 없습니다.");

    private HttpStatus httpStatus;
    private String message;
}