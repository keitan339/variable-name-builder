$(document).ready(function () {

    $(document).ready(function () {

        // コンテキストパスを取得
        window.setting = {
            contextPath: $('#contextPath').val()
        }

        // フォーマットの指定（Cookieに前回指定値があれば復元。なければ、メンバー変数モード）
        const format = Cookies.get('format') !== undefined ? Cookies.get('format') : 'field';
        $('input[name="format"]').val([format]);

        // フォーカスを設定
        $('#srcText').focus();

        // ローダーを非表示
        $('#loading').hide();
    })

    $('#generateBtn').on('click', function () {

        const srcText = $('#srcText').val().trim().split(/\r\n|\n/);
        const format = $('input[name="format"]:checked').val();
        const data = {texts: srcText, format: format};

        // フォーマット指定をCookieに保存
        Cookies.set('format', format, {expires: 365});

        // ajax通信
        $.ajax({
            url: window.setting.contextPath + '/service/generates',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            beforeSend: function () {
                // ローダーを表示
                $('#loading').show();
            },
            success: function (data) {
                let resultText = '';
                for (let i = 0; i < data.length; i++) {
                    resultText += data[i] + '\n';
                }
                // 結果を設定
                $('#resultText').val(resultText);
                $('#resultText').focus();
                $('#resultText').select();

                // ローダーを非表示
                $('#loading').hide();

            },
            error: function (XMLHttpRequest, textStatus) {

                // ローダーを非表示
                $('#loading').hide();

                // エラーダイアログを表示
                let message = null;
                if (XMLHttpRequest.status == 0) {
                    message = "接続不可";
                } else {
                    message = XMLHttpRequest.responseJSON.error + "<br>" + XMLHttpRequest.responseJSON.message;
                }
                $('#message').html(message);
                $('#errorDialog').modal('show');

            },
            complete: function () {
                // ローダーを非表示
                $('#loading').hide();
            }
        });
    });
});