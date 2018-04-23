/**
 * Created by I Like Milk on 2017/6/11.
 */
$(function () {
    $('#email, #name, #password').focus(function () {
        $(this).addClass('active');
        $(this).siblings('.tip-icon').removeClass('correct loading wrong show');
        $(this).siblings('.tip').removeClass('show');
    }).blur(function () {
        if (!$(this).val().length)
            $(this).removeClass('active');
    });

    var emailFlag = false, nameFlag = false, pwFlag = false;
    var $email = $('#email'), $password = $('#password'), $name = $('#name');
    $email.blur(function () {
        if (isEmail($email.val())) {
            $email.siblings('.tip-icon').addClass('loading show');
            $.get('/user/emailtest?' + $email.serialize(), function (data) {
                if (data) {
                    emailFlag = true;
                    $email.siblings('.tip-icon').removeClass('loading').addClass('correct');
                } else if ($email.siblings('.tip-icon').hasClass('show')) {
                    emailFlag = false;
                    $email.siblings('.tip-icon').removeClass('loading').addClass('wrong');
                    $email.siblings('.tip').text('The email address has been registered!').addClass('show');
                }
            })
        } else {
            emailFlag = false;
            $email.siblings('.tip-icon').addClass('wrong show');
            if ($email.val().length)
                $email.siblings('.tip').text('Invalid email address!').addClass('show');
            else
                $email.siblings('.tip').text('Empty!').addClass('show');
        }
        check();
    });

    $name.blur(function () {
        if (!$name.val().length) {
            nameFlag = false;
            $name.siblings('.tip-icon').addClass('wrong show');
            $name.siblings('.tip').text('Empty!').addClass('show');
        } else if (!isName($name.val())) {
            nameFlag = false;
            $name.siblings('.tip-icon').addClass('wrong show');
            $name.siblings('.tip').html('The name should consists of less than 20 letters, numbers, or underscores!<br>And it should be started with letters.').addClass('show');
        } else {
            nameFlag = true;
            $name.siblings('.tip-icon').addClass('correct show');
        }
        check();
    });

    $password.blur(function () {
        if (!$password.val().length) {
            pwFlag = false;
            $password.siblings('.tip-icon').addClass('wrong show');
            $password.siblings('.tip').text('Empty!').addClass('show');
        } else if (!isPW($password.val())) {
            pwFlag = false;
            $password.siblings('.tip-icon').addClass('wrong show');
            $password.siblings('.tip').text('Invalid password!').addClass('show');
        } else {
            pwFlag = true;
            $password.siblings('.tip-icon').addClass('correct show');
        }
        check();
    });

    var check = function () {
        if (emailFlag && pwFlag && nameFlag)
            $(':submit').removeAttr('disabled');
        else
            $(':submit').prop('disabled', 'true');
    }
});

function isEmail(str){
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return reg.test(str);
}

function isPW(str) {
    var reg = /^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>\/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){6,20}$/;
    return reg.test(str);
}

function isName(str) {
    var reg = /^[a-zA-Z]\w{0,19}$/;
    return reg.test(str);
}