$(function () {
    $('.toggle-button').on("click", function () {
        // 클릭된 버튼의 부모 요소인 toggle-buttons를 찾습니다.
        var toggleButtons = $(this).closest('.toggle-buttons');

        // toggleButtons의 다음 형제 요소인 book-content-row를 찾습니다.
        var bookContentRow = toggleButtons.closest('tr').next('.book-content-row');

        // bookContentRow의 상태를 토글합니다.
        bookContentRow.toggle();

        // 버튼의 텍스트를 변경합니다.
        if ($(this).text().trim() === "내용 보기") {
            $(this).text("내용 숨기기");
        } else {
            $(this).text("내용 보기");
        }
    });


    // "승인" 버튼에 대한 AJAX 요청
    $('.request-accept').on("click", function (e) {
        const bookId = $(this).data('request');
        callAjax('post', "/accept/book/" + bookId);
    });

    // "거부" 버튼에 대한 AJAX 요청
    $('.request-reject').on("click", function (e) {
        const bookId = $(this).data('request');
        callAjax('post', "/reject/book/" + bookId);
    });
});
