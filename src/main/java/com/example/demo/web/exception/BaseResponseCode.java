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
    SOME_BOOKS_ALREADY_PURCHASED(HttpStatus.BAD_REQUEST, "주문하고자 하는 도서 중 일부를 이미 구입하셨습니다."),
    ORDER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "주문 아이디를 찾을 수 없습니다."),
    ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, "이미 해당 도서에 리뷰를 작성했습니다."),



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
    AUTHOR_BOOKS_EXIST(HttpStatus.BAD_REQUEST, "해당 작가에 도서가 등록되었습니다. 하위 도서를 모두 삭제한 다음에 작가을 삭제해주세요."),
    SUBBOOKS_EXIST(HttpStatus.BAD_REQUEST, "해당 도서 그룹을 지정한 하위 도서가 존재합니다. 하위 도서를 모두 삭제한 다음에 도서 그룹을 삭제해주세요."),

    BOOK_CONTENT_EXIST(HttpStatus.BAD_REQUEST, "해당 도서에는 도서 내용이 있습니다. 도서 내용을 삭제한 다음에 도서를 삭제해주세요."),
    BOOK_CONTENT_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 도서 내용이 등록된 도서 아이디입니다."),
    BOOK_ALREADY_IN_WISHLIST(HttpStatus.BAD_REQUEST, "위시리스트에 이미 해당 도서가 존재합니다."),
    INVALID_WISHLIST_ID(HttpStatus.BAD_REQUEST, "위시리스트 아이디를 다시 확인해주세요."),
    UNAUTHORIZED_BOOK_REMOVAL(HttpStatus.BAD_REQUEST, "본인이 추가하지 않은 도서를 장바구니에서 제거할 수 없습니다."),

    ONLY_OWN_REVIEW_MODIFIABLE(HttpStatus.BAD_REQUEST, "본인이 작성한 리뷰만 수정할 수 있습니다."),
    ONLY_OWN_REVIEW_MANAGEABLE(HttpStatus.BAD_REQUEST, "본인이 작성한 리뷰만 관리할 수 있습니다."),
    REVIEW_FOR_PURCHASED_BOOK_ONLY(HttpStatus.BAD_REQUEST, "구매한 책에만 리뷰를 작성할 수 있습니다."),

    /**
     * 401
     */
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인 하셔야 이용할 수 있습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    NO_TOKEN_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    BOOK_PURCHASE_REQUIRED(HttpStatus.UNAUTHORIZED, "구매하지 않은 도서를 열람할 수 없습니다."),


    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "검색되는 카테고리가 없습니다. 카테고리 아이디를 다시 확인해주세요."),

    CATEGORY_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요."),

    /**
     * 404
     */
    BOOK_AUTHOR_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "검색되는 결과가 없습니다. 도서 아이디와 작가 아이디를 다시 확인해주세요."),

    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "검색되는 도서가 없습니다. 도서 아이디를 다시 확인해주세요."),

    BOOK_GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "검색되는 도서 그룹이 없습니다. 도서 그룹 아이디를 다시 확인해주세요."),

    BOOK_NOT_IN_WISHLIST(HttpStatus.BAD_REQUEST, "위시리스트에 삭제하고자 하는 도서가 존재하지 않습니다."),

    BOOK_ALREADY_PURCHASED(HttpStatus.BAD_REQUEST, "이미 구매한 도서입니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    AUTHOR_NOT_FOUND(HttpStatus.NOT_FOUND, "작가를 찾을 수 없습니다."),

    REVIEW_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰 아이디로 리뷰를 찾을 수 없습니다."),

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다. 회원 아이디와 도서 아이디를 확인해주세요."),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다. 댓글 아이디를 다시 확인해주세요."),

    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 조회할 수 없습니다. 회원 아이디와 리뷰 아이디를 확인해주세요."),


    /**
     * 500
     */

    EMAIL_ERROR_OCCURRED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 시 에러 발생");
    private HttpStatus httpStatus;
    private String message;
}