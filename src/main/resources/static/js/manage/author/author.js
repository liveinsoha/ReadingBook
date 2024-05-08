$(function () {
    $('.register').on("click", function () {
        const name = $('#name').val();
        const authorOption = $('#authorOption').val();
        const nationality = $('#nationality').val();
        const birthYear = $('#birthYear').val();
        const gender = $('#gender').val();
        const description = $('#description').val();

        const result = validateForm(name, nationality, birthYear, description);
        if (result == false) {
            return false;
        }

        const data = {
            "name": name,
            "authorOption": authorOption,
            "nationality": nationality,
            "birthYear": birthYear,
            "gender": gender,
            "description": description
        }

        callAjaxWithRedirect('post', '/manage/author', data, "/manage/home");
    });

    $('.update').on("click", function () {
        const authorId = $('#authorId').val();
        const name = $('#name').val();
        const authorOption = $('#authorOption').val();
        const nationality = $('#nationality').val();
        const birthYear = $('#birthYear').val();
        const gender = $('#gender').val();
        const description = $('#description').val();

        validateForm(name, nationality, birthYear, description);

        if (authorId.trim() == '') {
            alert('작가 아이디를 입력하세요.');
            return false;
        }

        const data = {
            "name": name,
            "authorOption": authorOption,
            "nationality": nationality,
            "birthYear": birthYear,
            "gender": gender,
            "description": description
        }

        callAjax('patch', '/manage/author/' + authorId, data);
    });

    $('.search').on("click", function () {
        const name = $('#name').val();

        if (name.trim() == '') {
            alert('검색어를 입력하세요.');
            return false;
        }

        $('.author-form').submit();
    });

    $('.delete').on("click", function () {
        const authorId = $('#authorId').val();

        if (authorId.trim() == '') {
            alert('작가 아이디를 입력하세요.');
            return false;
        }

        callAjax('delete', '/manage/author/' + authorId);
    });

});

function validateForm(name, nationality, birthYear, description) {
    if (name.trim() == '') {
        alert('이름을 입력하세요.');
        return false;
    }
    if (nationality.trim() == '') {
        alert('국적을 입력하세요.');
        return false;
    }
    if (birthYear.trim() == '') {
        alert('생년을 입력하세요.');
        return false;
    }
    if (description.trim() == '') {
        alert('설명을 입력하세요.');
        return false;
    }
}

$(document).ready(function () {
    $('#authorSearch').keyup(searchAuthors);
    // 동적 이벤트 연결

    $('#authorSuggestions').on('click', '.authorSuggestion', selectAuthor);

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

    // 선택된 작가 이름을 검색창에 위치시킴
    $('#authorSearch').val(selectedAuthorName);
    $('#authorSuggestions').empty();
}

// $(function () {
//     $('#authorForm').submit(function (event) { // 폼 제출 이벤트 핸들러 추가
//         event.preventDefault(); // 폼 제출 방지
//         $('#authorSuggestions').empty();

//         var name = $('#authorSearch').val(); // 입력된 작가 이름 가져오기
//         if (name.trim() !== '') { // 입력된 작가 이름이 비어있지 않은 경우에만 검색
//             $.ajax({
//                 url: '/result/author',
//                 type: 'GET',
//                 data: { name: name },
//                 dataType: 'json',
//                 success: function (response) {
                  
//                     generateAuthorResults(response.data);
//                 },
//                 error: function (xhr, status, error) {
//                     console.error('AJAX request error:', error);
//                 }
//             });
//         } else {
//             alert('작가 이름을 입력하세요.');
//         }
//     });

//     // 기존 코드는 유지됩니다.
// });


// function generateAuthorResults(response) {
//     var tableBody = $('.category-form .table tbody'); // 결과를 표시할 테이블의 tbody 요소

//     // 기존 결과 제거
//     tableBody.empty();

//     // 검색어 가져오기
//     var searchQuery = $('#authorSearch').val().trim();

//     // 결과가 없는 경우
//     if (response.length === 0) {
//         var noResultMessage = $('<tr><td colspan="5">' +
//             '<span>' + searchQuery + '</span>에 대한 검색 결과가 없습니다.' +
//             '</td></tr>');
//         tableBody.append(noResultMessage);
//         return;
//     }

//     // 결과가 있는 경우
//     $.each(response, function (index, author) {
//         var row = $('<tr>' +
//             '<td class="border-end">' + author.authorId + '</td>' +
//             '<td class="border-end">' + author.authorName + '</td>' +
//             '<td class="border-end">' + author.birthYear + '</td>' +
//             '<td class="border-end">' + author.gender + '</td>' +
//             '<td>' + author.authorOption + '</td>' +
//             // 수정 버튼 추가
//             '<td><a href="/update/author/' + author.authorId + '" class="btn btn-primary">수정</a></td>' +
//             '</tr>');
//         tableBody.append(row);
//     });
// }

