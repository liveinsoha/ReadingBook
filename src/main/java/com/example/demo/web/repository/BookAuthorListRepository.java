package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.BookAuthorList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookAuthorListRepository extends JpaRepository<BookAuthorList, Long> {

    Optional<BookAuthorList> findByBookIdAndAuthorId(Long bookId, Long authorId);
    boolean existsByAuthorId(Long authorId);

    @Query("select bal " +
            "from BookAuthorList bal " +
            "join fetch bal.book b " +
            "join fetch bal.author a " +
            "where b.isbn = :isbn and a.authorOption = com.example.demo.web.domain.enums.AuthorOption.AUTHOR " +
            "order by bal.ordinal asc " +
            "limit 1")
    BookAuthorList getMainAuthor(@Param("isbn") String isbn); //해당 책의 작가 중 1순위 작가를 구한다. (메인 작가) (작가옵션: 작가)

    @Query("select count(bal) " +
            "from BookAuthorList bal " +
            "join bal.book b " +
            "where bal.book.isbn = :isbn ")
    Long getAuthorCount(@Param("isbn") String isbn); //해당 책의 작가 수 구한다

    @Query("select bal " +
            "from BookAuthorList bal " +
            "join bal.book " +
            "join fetch bal.author " +
            "where bal.book.isbn = :isbn and bal.author.authorOption = com.example.demo.web.domain.enums.AuthorOption.TRANSLATOR " +
            "order by bal.ordinal asc " +
            "limit 1")
    BookAuthorList getMainTranslator(@Param("isbn") String isbn); //메인 번역가, 작가 옵션(번역가)

    @Query(
            "select bal " +
                    "from BookAuthorList bal " +
                    "join bal.author a " +
                    "join bal.book b " +
                    "where b.isbn = :isbn"
    )
    List<BookAuthorList> getAuthorNameAndIdList(@Param("isbn") String isbn); // 해당 책의 모든 작가들

    @Query(
            "select bal " +
                    "from BookAuthorList bal " +
                    "join fetch bal.author a " +
                    "join bal.book b " +
                    "where b.isbn = :isbn and a.id = :authorId"
    )
    Optional<BookAuthorList> getAuthorInformation(@Param("isbn") String isbn, @Param("authorId") Long authorId);
}