package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Category;
import com.example.demo.web.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, SearchBookRepository, HomeRepository {
    boolean existsByCategoryId(Long categoryId);

    List<Book> findByIsRequested(boolean isRequested);

    List<Book> findByIsAcceptedAndIsOnSaleAndIdIn(boolean isAccepted, boolean isOnSale, List<Long> bookIdList);

    boolean existsByBookGroupId(Long bookGroupId);

    Optional<Book> findByIsAcceptedAndIsOnSaleAndId(boolean isAccepted, boolean isOnSale, Long id);

    Optional<Book> findByIsAcceptedAndIsOnSaleAndIsbn(boolean isAccepted, boolean isOnSale, String isbn);


    List<Book> findByTitle(String title);

    @Query(
            "select b from Book b " +
                    "join fetch b.category " +
                    "join fetch b.category.categoryGroup " + //지연로딩하지 않고 쿼리 날리는 순간 프록시 대신 해당 객체를 리턴
                    "where b.isbn = :isbn and b.isOnSale = true and b.isAccepted = true" //항상 카테고리 정보가 필요하므로 지연로딩 하지 않았다.
    )
    Optional<Book> getBookInformation(@Param("isbn") String isbn);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByBookGroupId(Long bookGroupId);

    @Modifying //수정하는 쿼리임을 의미
    @Query("DELETE " +
            "FROM Book b " +
            "WHERE b.category = :category")
    void deleteByCategory(Category category);

    @Modifying(clearAutomatically = true)
    @Query("update Book b " +
            "set b.reviewCount = b.reviewCount - 1 " +
            "where b in (:books)")
    void updateReviewCountByBookIdInQuery(@Param("books") List<Book> books);

    List<Book> findByCategory(Category category);
}