package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Library;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    /**
     * 이 책들 중 구매한 책이 카운트 센다.
     *
     * @param bookIdList
     * @return
     */
    @Query(
            "select count(l.id) > 0 " +
                    "from Library l " +
                    "where l.book.id in(:bookIdList)"
    )
    boolean existsByBookIds(@Param("bookIdList") List<Long> bookIdList);

    boolean existsByBookId(Long bookId);

    boolean existsByBookAndMember(Book book, Member member);


    boolean existsByBookInAndMember(List<Book> books, Member member);

    List<Library> findAllByMember(Member member);


}
