$(function () {
    // 판매자 신청 버튼 클릭 시
    $('.apply-seller').on("click", function () {
        // 판매자 신청 버튼 아래의 비밀번호 확인 영역을 토글
        $('.password-confirmation').toggle();
    });

    // 확인 버튼 클릭 시
    $('.modify-wrap').on('click', '.btn-confirm', function () {
        const password = $('.password').val();

        if (password.trim() == '') {
            alert('비밀번호를 입력해주세요');
            return false;
        }

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        $('.password-confirmation').html('<p class="text-center">확인코드 전송 중...</p>');

        $.ajax({
            method: "post",
            url: "/user/send-email",
            data: {
                password: password
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                console.log(data);
                $('.password-confirmation').hide();
                // 성공했을 때, 확인 코드 입력하는 요소를 나오게 함
                $('.verification-code-input').show();
            },
            error: function (data) {
                if (data.status == 401) {
                    const returnUrl = window.location.href;
                    location.href = '/account/login?returnUrl=' + returnUrl;
                    return false;
                }
                if (data.status == 403) {
                    const returnUrl = window.location.href;
                    location.href = '/account/login?returnUrl=' + returnUrl;
                    return false;
                }
                const response = data.responseJSON;
                alert(response.message); // 400은 여기서 잘못된 비밀번호임을 알려준다.
                location.reload();
            }
        });
        // 비밀번호 확인 로직 수행
        // 여기에 비밀번호 확인 로직을 추가하고, 성공 시 다음 단계로 이동하는 등의 작업을 수행할 수 있습니다.
    });
});

$(function () {
    // 확인 코드 제출 버튼 클릭 시
    $('.btn-submit-code').on('click', function () {
        const verificationCode = $('.verification-code').val();

        if (verificationCode.trim() === '') {
            alert('확인 코드를 입력해주세요');
            return false;
        }

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            method: "post",
            url: "/user/verify-seller-code",
            data: {
                verificationCode: verificationCode
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                console.log(data);
                alert('확인 코드가 일치합니다. 판매자 신청이 완료되었습니다.');
                window.location.href = '/manage/home'; // 성공 시 이동할 URL로 변경
            },
            error: function (data) {
                const response = data.responseJSON;
                alert(response.message); // 실패한 경우 메시지를 표시합니다.
            }
        });
    });
});



function showPasswordConfirmation() {
    // 판매자 신청 화면을 생성하여 보여줌
    const passwordConfirmationHTML = `
        <div class="modify-wrap">
            <h3 class="fs-5">비밀번호 재확인</h3>
            <p class="fw-bold">보안을 위해 비밀번호를 한번 더 입력해 주세요.</p>
            <div class="d-flex">
                <input type="password" class="password form-control w-25 me-2">
                <button type="button" class="btn btn-primary btn-confirm">확인</button>
            </div>
        </div>
    `;
    // 판매자 신청 버튼 바로 아래에 비밀번호 확인 칸을 추가하여 화면에 보여줌
    $('.apply-seller').after(passwordConfirmationHTML);
}