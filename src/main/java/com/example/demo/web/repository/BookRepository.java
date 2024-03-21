package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, SearchBookRepository, HomeRepository {
    boolean existsByCategoryId(Long categoryId);

    boolean existsByBookGroupId(Long bookGroupId);

    List<Book> findByTitle(String title);

    @Query(
            "select b from Book b " +
                    "join fetch b.category " +
                    "join fetch b.category.categoryGroup " + //지연로딩하지 않고 쿼리 날리는 순간 프록시 대신 해당 객체를 리턴
                    "where b.isbn = :isbn" //항상 카테고리 정보가 필요하므로 지연로딩 하지 않았다.
    )
    Optional<Book> getBookInformation(@Param("isbn") String isbn);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByBookGroupId(Long bookGroupId);
}