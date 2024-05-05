function starRatingEvent(){
    $('input[name=rating]').on("change", function () {
        let rating = $('input[name=rating]:checked').val();
        if (rating == 1) {
            $(".star-comment").html("별로예요");
        }
        else if (rating == 2) {
            $(".star-comment").html("그저 그래요");
        }
        else if (rating == 3) {
            $(".star-comment").html("보통이에요");
        }
        else if (rating == 4) {
            $(".star-comment").html("좋아요");
        }
        else if (rating == 5) {
            $(".star-comment").html("최고의 책!");
        }
    });
}
$(function () {
    $('input[name=rating]').on("change", function () {
        starRatingEvent();
    });

    $('.wishlist-btn').on("click", function (){
        const bookId = $(this).val();
        const data = {
            "bookId" : bookId
        }
        callAjax("post", "/wishlist", data);
    });

    $('.author-btn').on("click", async function () {
        const authorId = $(this).val();
        const currentUrl = window.location.href;
        const data = await getData("get", currentUrl + "/" + authorId);
        console.log(data);
        $('.author-name').html(data.name);
        $('.author-nationality').html(data.nationality);
        $('.author-birthYear').html(data.birthYear);
        $('.author-gender').html(data.gender);
        $('.author-description').html(data.description);
    });
});


$(document).ready(function () {
    $('#bookSearch').keyup(searchBooks); // 동적 이벤트 연결

    $('#bookSuggestions').on('click', '.bookSuggestion', selectBook);
});

function searchBooks() {
    var name = $(this).val();
    if (name != '') {
        $.ajax({
            url: '/manage/book/search-query',
            type: 'GET',
            data: { name: name },
            dataType: 'json',
            success: function (response) {
                var books = response.data;
                $('#bookSuggestions').empty();
                $.each(books, function (index, book) { // 여기서 'authors'를 'books'로 변경
                    $('#bookSuggestions').append('<div class="bookSuggestion" data-book-id="' + book.bookId + '">' + book.title + '</div>');
                });
            }
        });
    }
}

function selectBook() {
    var selectedBookId = $(this).data('book-id'); // 여기서 'selectedAuthorId'를 'selectedBookId'로 변경
    var selectedBookTitle = $(this).text(); // 여기서 'selectedAuthorName'를 'selectedBookTitle'로 변경

    // 선택된 책 제목을 검색창에 위치시킴
    $('#bookSearch').val(selectedBookTitle); // 여기서 '#authorSearch'를 '#bookSearch'로 변경
    $('#bookSuggestions').empty();
}

$(function () {
    $('#bookForm').submit(function (event) { // 폼 제출 이벤트 핸들러 추가
        event.preventDefault(); // 폼 제출 방지
        $('#bookSuggestions').empty();

        var name = $('#bookSearch').val(); // 입력된 책 제목 가져오기
        if (name.trim() !== '') { // 입력된 책 제목이 비어있지 않은 경우에만 검색
            $.ajax({
                url: '/result/book',
                type: 'GET',
                data: { name: name },
                dataType: 'json',
                success: function (response) {
                    generateBookResults(response.data);
                },
                error: function (xhr, status, error) {
                    console.error('AJAX request error:', error);
                }
            });
        } else {
            alert('책 제목을 입력하세요.');
        }
    });
    // 기존 코드는 유지됩니다.
});

function generateBookResults(response) {
    var tableBody = $('.category-form .table tbody'); // 결과를 표시할 테이블의 tbody 요소

    // 기존 결과 제거
    tableBody.empty();

    // 검색어 가져오기
    var searchQuery = $('#bookSearch').val().trim();

    // 결과가 없는 경우
    if (response.length === 0) {
        var noResultMessage = $('<tr><td colspan="5">' +
            '<span>' + searchQuery + '</span>에 대한 검색 결과가 없습니다.' +
            '</td></tr>');
        tableBody.append(noResultMessage);
        return;
    }

    // 결과가 있는 경우
    $.each(response, function (index, book) {
        var row = $('<tr>' +
            '<td class="border-end">' + book.bookId + '</td>' +
            '<td class="border-end">' + book.bookTitle + '</td>' +
            '<td class="border-end">' + book.author + '</td>' + // 예시로 'author' 필드를 표시
            '<td class="border-end">' + book.publishedYear + '</td>' +
            '<td>' + book.genre + '</td>' +
            // 수정 버튼 추가
            '<td><a href="/update/book/' + book.bookId + '" class="btn btn-primary">수정</a></td>' +
            '</tr>');
        tableBody.append(row);
    });
}
