package com.example.demo.web.repository;

import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.dto.response.BookSearchResponse;
import com.example.demo.web.dto.response.QBookSearchResponse;
import com.example.demo.web.repository.searchCond.SearchCondUtils;
import com.example.demo.web.service.search.BookSearchCondition;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.web.domain.entity.QBook.book;
import static com.example.demo.web.domain.entity.QBookAuthorList.bookAuthorList;
import static com.querydsl.jpa.JPAExpressions.select;

@Repository
public class SearchBookRepositoryImpl implements SearchBookRepository{
    private final JPAQueryFactory queryFactory;

    public SearchBookRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 조회 메소드
     * @param query
     * @param pageable
     * @param condition
     * @return 페이징 DTO
     */
    @Override
    public Page<BookSearchResponse> search(String query, Pageable pageable, BookSearchCondition condition) {
        /* --- select절의 서브쿼리에서 limit을 먹지 않아서 where절에 서브 쿼리를 추가하여 limit을 대체 --- */
        JPQLQuery<Long> limitAuthorQuery = select(bookAuthorList.ordinal.min()) //그런 작가들 중에서 ordinal컬럼을 택하고 ordinal 가장 작은 거 리턴 ex.1
                .from(bookAuthorList) //작가-책 매핑 테이블로부터 찾는다
                .join(bookAuthorList.author) // 작가 테이블 조인
                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)));
                //책 id와 작가-책 매핑의 책id 같은 애들 중(여기서 book은 최종 where절에 검색어로 인해 결과가 나온 상황?), 작가의 테이블도 조인 되어 있는데, Option은 AUTHOR만 취급

        JPQLQuery<Long> limitTranslatorQuery = select(bookAuthorList.ordinal.min())
                .from(bookAuthorList) // 작가-책 매핑 테이블
                .join(bookAuthorList.author) //작가 테이블 조인(왜래키는 다대일에서 다쪽에 있다. bookAuthorList에 있다. 다대다 매핑 테이블)
                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.TRANSLATOR)));
                    //번역가들도 여러명 중에 ordinal이 가장 작은 숫자 리턴.

        /* --- 페이지 count total 구하는 쿼리 --- */
        JPAQuery<Long> countQuery = queryFactory
                .select(book.count())
                .from(book)
                .where(SearchCondUtils.contains(book.title, query));

        /* --- 출판사로 검색했는지, 제목으로 검색했는지 확인하는 메소드 --- */
        boolean isPublisherSearching = isPublisherSearching(query); //'출판사_' 로 시작하는 검색어로 검색한 경우 true
        StringPath path = initPath(isPublisherSearching); //일반 검색어는 Qbook.title
        query = initQuery(isPublisherSearching, query); //일반 검색어는 초기화 할 필요 없음. 출판사의 경우 앞에 '출판사_'제거

        List<BookSearchResponse> content = queryFactory
                .select(new QBookSearchResponse(
                                book.id,
                                book.isbn,
                                book.savedImageName,
                                book.title,
                                book.publisher,
                                book.description,
                                book.ebookPrice,
                                book.discountRate,
                                ExpressionUtils.as(
                                        select(bookAuthorList.author.name) //결국엔 한 명의 작가의 이름(column) 리턴됨 why? 아래 where절에 ordinal이 limitAuthorQuery의 결과랑 같은 건 1개거든.
                                                .from(bookAuthorList) // 작가-책 매핑 테이블로 부터 찾음
                                                .join(bookAuthorList.author) //작가 테이블 조인
                                                .where(
                                                        bookAuthorList.book.id.eq(book.id)
                                                                .and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR))
                                                                .and(bookAuthorList.ordinal.eq(
                                                                                limitAuthorQuery // 1 -> 그냥 1L 대입하면 안 되나?
                                                                        )
                                                                )
                                                )
                                                .orderBy(bookAuthorList.ordinal.asc()),
                                        "author"
                                ),
                                ExpressionUtils.as(
                                        select(bookAuthorList.author.name.count())
                                                .from(bookAuthorList)
                                                .join(bookAuthorList.author)
                                                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)))
                                                .groupBy(bookAuthorList.book.id),
                                        "authorCount"
                                ),
                                ExpressionUtils.as(
                                        select(bookAuthorList.author.name)
                                                .from(bookAuthorList)
                                                .join(bookAuthorList.author)
                                                .where(
                                                        bookAuthorList.book.id.eq(book.id)
                                                                .and(bookAuthorList.author.authorOption.eq(AuthorOption.TRANSLATOR))
                                                                .and(bookAuthorList.ordinal.eq(
                                                                                limitTranslatorQuery
                                                                        )
                                                                )
                                                )
                                                .orderBy(bookAuthorList.ordinal.asc())
                                                .limit(1),
                                        "translator"
                                ),
                                book.category.categoryGroup.name
                        )
                )
                .from(book)
                .where(
                        SearchCondUtils.contains(path, query) //제목(QBook.title), 검색어(query)  // Or 출판사(QBook.publisher) ,검색어
                )
                .orderBy(SearchCondUtils.bookOrder(book, condition.getOrder()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private String initQuery(boolean isPublisherSearching, String query) {
        if(isPublisherSearching == false){
            return query;
        }

        query = query.substring(4);
        return query;
    }

    private StringPath initPath(boolean isPublisherSearching) {
        if(isPublisherSearching == true){
            return book.publisher;
        }
        return book.title;
    }

    private boolean isPublisherSearching(String query) {
        if(query == null){
            return false;
        }
        return query.contains("출판사_");
    }
}
