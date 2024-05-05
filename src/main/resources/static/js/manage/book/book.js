$(document).ready(function () {
    $('#authorName').keyup(searchAuthors);
    // 동적 이벤트 연결
    $('#authorSuggestions').on('click', '.authorSuggestion', function () {
        var selectedAuthorId = $(this).data('author-id');
        var selectedAuthorName = $(this).text();

        // 작가 선택 폼 추가
        $('#authorForms').append(`
        <div class="authorForm mb-3 d-flex align-items-center">
            <input type="hidden" name="selectedAuthorId" value="${selectedAuthorId}">
            <label class="mr-3">${selectedAuthorName}</label>
            <select class="form-control ordinalSelect">
                <option value="1">1st</option>
                <option value="2">2nd</option>
                <option value="3">3rd</option>
                <!-- 필요한 만큼 옵션을 추가하세요 -->
            </select>
        </div>
    `);


        $('#authorSuggestions').empty();
    });
});

function searchAuthors() {
    var name = $(this).val();
    if (name != '') {
        $.ajax({
            url: '/manage/book/search/author',
            type: 'GET',
            data: { name: name },
            dataType: 'json',
            success: function (response) {
                var authors = response.data;
                $('#authorSuggestions').empty();
                $.each(authors, function (index, author) {
                    $('#authorSuggestions').append('<div class="authorSuggestion" data-author-id="' + author.authorId + '">' + author.authorName + '</div>');
                });
            }
        });
    }
}

function selectAuthor() {
    var selectedAuthorId = $(this).data('author-id');
    var selectedAuthorName = $(this).text();

    // 이 부분에서 선택된 작가의 ID와 이름을 POST 요청할 데이터로 사용하거나 다른 곳에 저장할 수 있습니다.
    console.log("선택된 작가의 ID:", selectedAuthorId);
    console.log("선택된 작가의 이름:", selectedAuthorName);
}


$(function () {
    $('.search').on('click', function () {
        var title = $('#title').val(); // 입력된 도서 제목 가져오기
        if (title.trim() !== '') { // 입력된 도서 제목이 비어있지 않은 경우에만 검색
            window.location.href = '/result/book?title=' + title; // 검색 결과 페이지로 이동
        } else {
            alert('도서 제목을 입력하세요.'); // 도서 제목이 입력되지 않은 경우 알림 표시
        }
    });
});


$(function () {
    $('.register').on("click", function () {
        var authors = [];

        $('.authorForm').each(function (index) {
            var selectedAuthorId = $(this).find('input[name="selectedAuthorId"]').val();
            var ordinal = $(this).find('.ordinalSelect').val();

            authors.push({
                authorId: selectedAuthorId,
                ordinal: ordinal
            });
        });

        const title = $('#title').val();
        const isbn = $('#isbn').val();
        const publisher = $('#publisher').val();
        const publishingDate = $('#publishingDate').val();
        const paperPrice = $('#paperPrice').val();
        const ebookPrice = $('#ebookPrice').val();
        const discountRate = $('#discountRate').val();
        const categoryId = $('#categoryId').val();
        const description = ckeditorInstance.getData()
        const bookGroupId = $('#bookGroupId').val();
        const file = $('#file')[0].files[0];

        const isValidForm = validateRequestForm(title, isbn, publisher, publishingDate, ebookPrice, discountRate, categoryId, description);
        if (isValidForm == false) {
            return false;
        }

        if (paperPrice.trim() == '') {
            paperPrice = 0;
        }
        if (bookGroupId.trim() == '') {
            bookGroupId = 0;
        }

        const isValidFile = validateFile(file);
        if (isValidFile == false) {
            return false;
        }

        const formData = new FormData();
        formData.append('title', title);
        formData.append('isbn', isbn);
        formData.append('publisher', publisher);
        formData.append('publishingDate', publishingDate);

        // authors 배열을 formData에 추가
        authors.forEach((author, index) => {
            formData.append(`authors[${index}].authorId`, author.authorId);
            formData.append(`authors[${index}].ordinal`, author.ordinal);
        });

        formData.append('paperPrice', paperPrice);
        formData.append('ebookPrice', ebookPrice);
        formData.append('discountRate', discountRate);
        formData.append('categoryId', categoryId);
        formData.append('bookGroupId', bookGroupId);
        formData.append("description", description);
        formData.append('file', file);

        uploadFileUsingAjax('post', '/manage/book', formData);
    });
});


$('.update-search').on("click", function () {
    const bookId = $('#bookId').val();
    if (bookId.trim() == '') {
        alert('도서 아이디를 입력해주세요');
        return false;
    }

    $('#bookSearchForm').submit();
});

$(function () {
    $('.toggle-all-buttons').on('click', function () {
        $('.toggle-buttons').toggle(); // 검색 결과의 수정 버튼을 토글
    });
});



$(function () {

    // 도서 콘텐츠 수정 버튼 클릭 이벤트
    $('#update-content').on("click", function () {
        const bookId = $('#bookId-content').val();
        const title = $('#title').val();
        const isbn = $('#isbn').val();
        const publisher = $('#publisher').val();
        const publishingDate = $('#publishingDate').val();
        let paperPrice = $('#paperPrice').val();
        const ebookPrice = $('#ebookPrice').val();
        const discountRate = $('#discountRate').val();
        const categoryId = $('#categoryId').val();
        let bookGroupId = $('#bookGroupId').val();
        const description = ckeditorInstance.getData()

        if (bookId.trim() == '') {
            alert('도서 아이디를 입력해주세요');
            return false;
        }

        validateRequestForm(title, isbn, publisher, publishingDate, ebookPrice, discountRate, categoryId, description);

        if (paperPrice.trim() == '') {
            paperPrice = 0;
        }
        if (bookGroupId.trim() == '') {
            bookGroupId = 0;
        }

        const data = {
            "title": title,
            "isbn": isbn,
            "publisher": publisher,
            "publishingDate": publishingDate,
            "paperPrice": paperPrice,
            "ebookPrice": ebookPrice,
            "discountRate": discountRate,
            "categoryId": categoryId,
            "bookGroupId": bookGroupId,
            "description": description
        }

        callAjax('patch', '/manage/book/content/' + bookId, data);
    });

    // 도서 이미지 수정 버튼 클릭 이벤트
    $('#update-image').on("click", function () {
        const bookId = $('#bookId-image').val();
        const file = $('#file')[0].files[0];

        if (bookId.trim() == '') {
            alert('도서 아이디를 입력해주세요');
            return false;
        }

        const isValidFile = validateFile(file);
        if (isValidFile == false) {
            return false;
        }

        const formData = new FormData();
        formData.append('file', file);

        uploadFileUsingAjax('patch', '/manage/book/image/' + bookId, formData);
    });

    $('.search').on("click", function () {
        const title = $('#title').val();
        if (title.trim() == '') {
            alert('도서 제목을 입력하세요.');
            return false;
        }

        $(".book-form").submit();
    });

    $('.delete-button').on("click", function () {
        const result = confirm('삭제하면 복구할 수 없습니다. 삭제하시겠습니까?');
        if (result == false) {
            return false;
        }
    
        const bookId = $(this).closest('tr').find('td:first').text().trim(); // 해당 행의 첫 번째 열에서 도서 ID를 가져옴
    
        if (bookId === '') {
            alert('도서 아이디를 찾을 수 없습니다.');
            return false;
        }
    
        console.log(bookId);
    
        callAjax('delete', '/manage/book/' + bookId);
    });
    
});


function validateFile(file) {
    const MAX_SIZE = 5 * 1024 * 1024;
    let fileSize = 0;

    if (file != undefined) {
        fileSize = file.size;
    }

    if (fileSize == 0) {
        alert('이미지를 업로드해주세요.');
        return false;
    }

    if (fileSize > MAX_SIZE) {
        alert('이미지의 크기는 5MB까지 업로드할 수 있습니다.');
        return false;
    }
}

function validateRequestForm(title, isbn, publisher, publishingDate, ebookPrice, discountRate, categoryId, description) {
    if (title.trim() == '') {
        alert('도서 제목을 입력해주세요.')
        return false;
    }

    if (isbn.trim() == '') {
        alert('isbn을 입력해주세요.')
        return false;
    }

    if (publisher.trim() == '') {
        alert('출판사를 입력해주세요.')
        return false;
    }

    if (publishingDate.trim() == '') {
        alert('출판일을 입력해주세요.')
        return false;
    }

    if (ebookPrice.trim() == '') {
        alert('전자책 가격을 입력해주세요.')
        return false;
    }

    if (discountRate.trim() == '') {
        alert('할인율을 입력해주세요.')
        return false;
    }

    if (categoryId.trim() == '') {
        alert('카테고리 아이디를 입력해주세요.')
        return false;
    }

    if (description.trim() == '') {
        alert('도서 설명을 입력해주세요.')
        return false;
    }
}
