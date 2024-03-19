package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Wishlist;
import com.example.demo.web.dto.response.WishlistResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.LibraryRepository;
import com.example.demo.web.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final LibraryRepository libraryRepository;

    /**
     * 위시리스트에 도서를 담는 메소드
     * @param member
     * @param book
     * @return
     */
    public Long addBook(Member member, Book book) {
        Long memberId = member.getId();
        Long bookId = book.getId();

        /* --- 도서가 이미 위시리스트에 담겨 있는지 확인 --- */
        validateIsBookExisted(bookId, memberId);

        /* --- 위시리스트에 담으려고 하는 도서를 구매했는지 확인 --- */
        validateAddingBookAlreadyBuyed(book);


        Wishlist wishlist = Wishlist.createWishlist(book, member);

        return wishlistRepository.save(wishlist).getId();
    }

    /**
     * 구매목록(라이브러리)에 있을 경우 이미 구매 예외 발생
     * @param book
     */
    private void validateAddingBookAlreadyBuyed(Book book) {
        Long bookId = book.getId();
        boolean isExistsBook = libraryRepository.existsByBookId(bookId);
        if(isExistsBook == true){
            throw new BaseException(BaseResponseCode.BOOK_ALREADY_PURCHASED);
        }
    }

    /**
     * 위시리스트에서 도서를 제거하는 메소드
     * @param wishlistIdList
     * @param memberId
     * @return isDeleted
     */
    public boolean delete(List<Long> wishlistIdList, Long memberId) {
        List<Wishlist> wishlists = findWishlist(wishlistIdList);

        if(wishlists.size() == 0){
            throw new BaseException(BaseResponseCode.BOOK_NOT_IN_WISHLIST);
        }

        for (Wishlist wishlist : wishlists) {
            Long findMemberId = wishlist.getMember().getId();
            if(findMemberId != memberId){
                throw new BaseException(BaseResponseCode.UNAUTHORIZED_BOOK_REMOVAL);
            }
        }

        wishlistRepository.deleteAllById(wishlistIdList);
        return true;
    }

    private List<Wishlist> findWishlist(List<Long> wishlistIdList) {
        return wishlistRepository.findAllById(wishlistIdList);
    }

    private void validateIsBookExisted(Long bookId, Long memberId) {
        boolean isExists = wishlistRepository.existsByBookIdAndMemberId(bookId, memberId);
        if(isExists == true){
            throw new BaseException(BaseResponseCode.BOOK_ALREADY_IN_WISHLIST);
        }
    }

    public Wishlist findWishlist(Long wishlistId){
        return wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_WISHLIST_ID));
    }

    public List<WishlistResponse> findBookResponses(Long memberId) {
        return wishlistRepository.findByMemberId(memberId).stream()
                .map(WishlistResponse::new)
                .collect(Collectors.toList());
    }
}
