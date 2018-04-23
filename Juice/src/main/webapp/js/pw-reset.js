/**
 * Created by I Like Milk on 2017/6/12.
 */
$(function () {
    var $email = $('#email'), $button = $(':button');
    var email;
    $email.focus(function () {
        $(this).addClass('active');
        $(this).siblings('.tip-icon').removeClass('correct loading wrong show');
        $(this).siblings('.tip').removeClass('show');
    }).blur(function () {
        if (!$(this).val().length)
            $(this).removeClass('active');
    });

    var a;
    $email.blur(a = function () {
        if (isEmail($email.val())) {
            $email.siblings('.tip-icon').addClass('loading show');
            $.get('/user/emailtest?' + $email.serialize(), function (data) {
                if (!data) {
                    $(':button').removeAttr('disabled');
                    $email.siblings('.tip-icon').removeClass('loading').addClass('correct');
                } else if ($email.siblings('.tip-icon').hasClass('show')) {
                    $(':button').prop('disabled', 'true');
                    $email.siblings('.tip-icon').removeClass('loading').addClass('wrong');
                    $email.siblings('.tip').text('The email address doesn\'t exist!').addClass('show');
                }
            })
        } else {
            $button.prop('disabled', 'true');
            $email.siblings('.tip-icon').addClass('wrong show');
            if ($email.val().length)
                $email.siblings('.tip').text('Invalid email address!').addClass('show');
            else
                $email.siblings('.tip').text('Empty!').addClass('show');
        }
    });

    var $card = $('.join-card'), $box = $('.box');
    $button.click(function () {
        $card.css({
            '-webkit-filter': 'brightness(80%) blur(4px)',
            'filter': 'brightness(80%) blur(4px);',
            'pointer-events': 'none'
        });
        $box.append($('<div></div>').addClass('loader'));
        $.get('/send?email=' + $email.val(), function (data) {
            if (data === 1) {
                email = $email.val();
                var $vcode = $email;
                $vcode.val('').off('blur', a);
                $vcode.focus().blur().on('input', function () {
                    if ($vcode.val().length)
                        $button.removeAttr('disabled');
                    else
                        $button.prop('disabled', 'true');
                });
                $button.prop('disabled', 'true');
                $('p').text('A verification code has been sent to you, please check your email and enter it.')
                $vcode.prop({
                    'name': 'vcode',
                    "id": 'vcode'
                }).next().text('Verification code');

                $button.val('Submit').off('click').click(function () {
                    $card.css({
                        '-webkit-filter': 'brightness(80%) blur(4px)',
                        'filter': 'brightness(80%) blur(4px);',
                        'pointer-events': 'none'
                    });
                    $box.append($('<div></div>').addClass('loader'));
                    $.post('/verify', {
                        'email': email,
                        'code': $vcode.val()
                    }, function (data1) {
                        if (data1 === 1) {
                            var $password = $vcode.prop('type', 'password');
                            $password.val('');
                            $password.focus().blur().blur(function () {
                                if (!$password.val().length) {
                                    $button.prop('disabled', true);
                                    $password.siblings('.tip-icon').addClass('wrong show');
                                    $password.siblings('.tip').text('Empty!').addClass('show');
                                } else if (!isPW($password.val())) {
                                    $button.prop('disabled', true);
                                    $password.siblings('.tip-icon').addClass('wrong show');
                                    $password.siblings('.tip').text('Invalid password!').addClass('show');
                                } else {
                                    $button.removeProp('disabled').removeAttr('disabled');
                                    $password.siblings('.tip-icon').addClass('correct show');
                                }
                            });
                            $button.off('click').prop('disabled', 'true').click(function () {
                                var $input0 = $('<input type="hidden" name="email">').val(email);
                                var $input1 = $('<input type="hidden" name="password">').val($password.val());
                                var $form = $('<form method="post" action="/reset"></form>').append($input0, $input1);
                                $('body').append($form);
                                $form.submit();
                            });
                            $('p').text('Enter a new password.');
                            $password.prop({
                                'name': 'password',
                                "id": 'password'
                            }).next().text('New password');
                            $('.loader').remove();
                            $card.removeAttr('style');

                        } else {
                            $vcode.siblings('.tip-icon').addClass('wrong show');
                            $vcode.siblings('.tip').text('Wrong').addClass('show');
                            $('.loader').remove();
                            $card.removeAttr('style');
                        }
                    });
                });

                $('.loader').remove();
                $card.removeAttr('style');
            }
        });
    });
});

function isEmail(str){
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return reg.test(str);
}

function isPW(str) {
    var reg = /^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>\/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){6,20}$/;
    return reg.test(str);
}