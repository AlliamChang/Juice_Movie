/**
 * Created by I Like Milk on 2017/5/11.
 */
$.fn.extend({
    animateCss: function (animationName) {
        var animationEnd = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';
        this.addClass('animated ' + animationName).one(animationEnd, function() {
            $(this).removeClass('animated ' + animationName);
        });
    }
});

var loggedIn = false;
$(function () {
    if ($.cookie('user') !== undefined)
        loggedIn = true;

    var logInBtn = $(".log-in-btn"), shade = $('.shade');
    var stupid = $('.stupid'), header = $('header'), goUp = $('.go-up');
    $(window).on('scroll', function () {
        if ($(window).scrollTop() > 60) {
            if (!stupid.hasClass('opaque')) {
                stupid.addClass('opaque');
                header.addClass('shrink');
                goUp.fadeIn();
            }
        } else {
            if (stupid.hasClass('opaque')) {
                stupid.removeClass('opaque');
                header.removeClass('shrink');
                goUp.fadeOut();
            }
        }
    });

    var yesBtn = $('.log-in-btn-back > .yes');
    var emailLine = $('.log-in-btn-back .email-line'), pwLine = $('.log-in-btn-back .password-line');
    var emptyTip = $('.log-in-btn-back > .tip-text.empty');

    $(".log-in-btn-front").click(function () {
        shade.fadeIn();
        logInBtn.addClass("is-open");
        $('.log-in-btn > .log-in-btn-back').one('transitionend webkitTransitionEnd', function () {
            emailLine.focus();
        });
    });

    var func1 = function () {
        if (emailLine.val() == "" || emailLine.val() == null) {
            emptyTip.removeClass('password-empty');
            emptyTip.addClass('email-empty show');
        } else if (pwLine.val() == "" || pwLine.val() == null) {
            emptyTip.removeClass('email-empty');
            emptyTip.addClass('password-empty show');
        } else {
            yesBtn.off('click', func1);
            $.post('/user/login', $('#login-form').serialize(), function (data) {
                if (data.length === 2) {
                    if ($('.log-in-btn-back > .remember-forgot :checkbox').prop('checked')) {
                        $.cookie('user', data[0], {expires: 7, path: '/'});
                        $.cookie('token', data[1], {expires: 7, path: '/'});
                    } else {
                        $.cookie('user', data[0], {path: '/'});
                        $.cookie('token', data[1], {path: '/'});
                    }
                    location.reload();
                } else  {
                    if (data !== undefined && data !== null && data.length === 1)
                        if (data[0] == '-1')
                            $('.log-in-btn-back > .wrong-email').addClass('show');
                        else if (data[0] == '-2')
                            $('.log-in-btn-back > .wrong-password').addClass('show');
                    yesBtn.click(func1);
                }
            });
        }
    };

    yesBtn.click(func1);

    var allTips = $('.log-in-btn-back > .tip-text');

    emailLine.on('input propertychange', function () {
        allTips.removeClass('show');
    });

    pwLine.on('input propertychange', function () {
        allTips.removeClass('show');
    });

    $(".log-in-btn-back").keypress(function (event) {
        if (event.which === 13)
            yesBtn.click();
    });

    $(".log-in-btn-back > .close-button").click(function () {
        allTips.removeClass('show');
        logInBtn.removeClass("is-open");
        shade.fadeOut();
        $(".log-in-btn-back input").val("");
    });

    shade.click(function () {
        $(".log-in-btn-back > .close-button").click();
    });

    var $searchBox = $('#search-box');
    $searchBox.focus(function () {
        $("#magnifier, #guidance").addClass("typing");
    });

    $searchBox.blur(function (e) {
        if (e.relatedTarget === $searchBox.next()[0])
            $searchBox.next().click();
        $("#magnifier, #guidance").removeClass("typing");
    });

    $searchBox.keypress(function (e) {
        if (e.which === 13 && $searchBox.val().trim().length)
            location.href = '/s?' + $searchBox.serialize();
    });

    $searchBox.next().click(function () {
        if ($searchBox.val().trim().length)
            location.href = '/s?' + $searchBox.serialize();
    });


    $(".log-in-btn-back .email-line, .log-in-btn-back .password-line").focus(function () {
        $(this).parent().addClass("is-focused");
    });

    $(".log-in-btn-back .email-line, .log-in-btn-back .password-line").blur(function () {
        $(this).parent().removeClass("is-focused");
    });

    $('.filter .clear-btn').click(function () {
        $('.filter .user-choose span').fadeOut(function () {
            $('.filter .user-choose span').remove();
        });
        $(this).fadeOut();
        $('.filter .tags :checkbox').prop('checked', false);
        $('.filter .tags .chosen path').remove();
        $('.filter .tags .chosen').removeClass('chosen');
        flushList();
    });

    $('.filter input[name="watched"][disabled="true"]').parent().click(function () {
        var $checkDiv = $(this);
        $checkDiv.addClass('tooltip');
        setTimeout(function () {
            $checkDiv.removeClass('tooltip');
        }, 1500);
    });

    $('#logo').animateCss('bounce');

    moreBtn = $(".underpants a");

    var inProfileBtn, outProfile;
    var $dropDown = $('.drop-down');
    $('#profile-btn').hover(
        function () {
            if (outProfile) {
                clearTimeout(outProfile);
                outProfile = 0;
            }
            inProfileBtn = setTimeout(function () {
                inProfileBtn = 0;
                $dropDown.slideDown('fast');
            }, 300);
        },
        function () {
            if (inProfileBtn) {
                clearTimeout(inProfileBtn);
                inProfileBtn = 0;
            }
            outProfile = setTimeout(function () {
                outProfile = 0;
                $dropDown.slideUp('fast');
            }, 300);
        }
    );

    $dropDown.hover(
        function () {
            if (outProfile) {
                clearTimeout(outProfile);
                outProfile = 0;
            }
        },
        function () {
            outProfile = setTimeout(function () {
                outProfile = 0;
                $dropDown.slideUp('fast');
            }, 300);
        }
    );

    $($dropDown).find('#log-out-btn').click(function () {
        $.removeCookie('user');
        $.removeCookie('token');
        $.post('/user/logout', function () {
            location.reload();
        });
    });
});

function initMovieChooser() {
    $.get("/movie/tags", function (data) {
        setFilter(data);
        flushList();
    });
}

function initTVShowChooser() {
    $.get("/tv-show/tags", function (data) {
        setFilter(data);
        flushList();
    });
}

var permissionFlag = 1;
var $pageIndex;
function setFilter(data) {
    $pageIndex = $('.filter form > input[name="pageindex"]');
    var $clearBtn = $('.filter .clear-btn');
    $(".filter form .tool").change(function () {
        flushList();
    });
    //打钩
    // var path = 'M16.667,62.167c3.109,5.55,7.217,10.591,10.926,15.75 c2.614,3.636,5.149,7.519,8.161,10.853c-0.046-0.051,1.959,2.414,2.692,2.343c0.895-0.088,6.958-8.511,6.014-7.3 c5.997-7.695,11.68-15.463,16.931-23.696c6.393-10.025,12.235-20.373,18.104-30.707C82.004,24.988,84.802,20.601,87,16';
    //画圈
    var path = 'M34.745,7.183C25.078,12.703,13.516,26.359,8.797,37.13 c-13.652,31.134,9.219,54.785,34.77,55.99c15.826,0.742,31.804-2.607,42.207-17.52c6.641-9.52,12.918-27.789,7.396-39.713 C85.873,20.155,69.828-5.347,41.802,13.379';
    var anim = {speed: .2, easing: 'ease-in-out'};
    for (var i = 0; i < data.length; i++) {
        var tag = $("<input type='checkbox' name='tag'>").val(data[i]);
        var tagLabel = $("<label></label>").append(tag).append(data[i]);
        var svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
        svg.setAttributeNS(null, 'viewBox', '0 0 100 100' );
        svg.setAttribute( 'xmlns', 'http://www.w3.org/2000/svg' );
        tagLabel.append(svg);
        $(".filter .tags").append(tagLabel);
        tag.change(function () {
            var label = $(this).parent();
            if (label.hasClass("chosen")) {
                flushList();
                label.removeClass("chosen");
                $(this).next().html("");
                var content = label.text();
                var chosenTags = $(".filter .user-choose span");
                $clearBtn.fadeOut();
                chosenTags.filter(function () {
                    return $(this).text() === content;
                }).fadeOut(function () {
                    $(this).remove();
                    if (chosenTags.length > 1)
                        $clearBtn.fadeIn();
                });
            } else {
                if ($(".filter .tags :checked").length < 5) {
                    flushList();
                    $(this).parent().addClass("chosen");
                    var pathHtml = document.createElementNS('http://www.w3.org/2000/svg', 'path');
                    $(this).next().append(pathHtml);
                    pathHtml.setAttributeNS(null, 'd', path);
                    var length = pathHtml.getTotalLength();
                    pathHtml.style.strokeDasharray = length + ' ' + length;
                    pathHtml.style.strokeDashoffset = length;
                    pathHtml.getBoundingClientRect();
                    pathHtml.style.transition = pathHtml.style.WebkitTransition = pathHtml.style.MozTransition = 'stroke-dashoffset ' + anim.speed + 's ' + anim.easing;
                    pathHtml.style.strokeDashoffset = '0';
                    var span = $("<span></span>").css("display", "none").text($(this).parent().find("input").val());
                    span.click(function () {
                        $(".filter .tags label").filter(function () {
                            return span. text() === $(this).text();
                        }).click();
                    });
                    $(".filter .user-choose").append(span);
                    $clearBtn.fadeOut('fast', function () {
                        span.fadeIn(function () {
                            $clearBtn.fadeIn();
                        });
                    });
                } else {
                    $(this).prop('checked', false);
                    var prompt = $("<span></span>").addClass("prompt").text("Oops~too many!");
                    prompt.on('animationEnd', function () {
                        $(this).remove();
                    });
                    prompt.on('webkitAnimationEnd', function () {
                        $(this).remove();
                    });
                    $(this).parent().append(prompt);
                }
            }
        });
    }
}

function flushList() {
    $(".list").empty();
    $pageIndex.val(0);
    permissionFlag = 1;
    getList();
}

function getList(callback) {
    if (permissionFlag === 0)
        return;
    permissionFlag = 0;
    moreBtn.text('(〜￣△￣)〜 Wait a second...').removeClass('more').removeAttr('href');
    var list = $(".list");
    var form = $('.filter form');
    form.css('pointer-events', 'none');
    var type = form.find('input[name="type"]').val() == 0 ? 'movie' : 'tv-show';
    var prefix = '/' + type + '/';
    $.get("/get?" + form.serialize(), function (data) {
        if (data === undefined || data === null || data.length === 0) {
            moreBtn.text('╮(￣▽￣)╭ Sorry, no more ' + type.replace('-', ' ') + 's...');
            form.removeAttr('style');
        } else {
            var row;
            for (var i = 0; i < data.length; i++) {
                if (!(i % 4))
                    row = $("<div></div>").addClass('item-row');
                var item = $("<a></a>").prop({'href': prefix + 'j' + data[i].id, 'target': '_blank'}).addClass('item');
                var imgWp = $("<div></div>").addClass('img-wrap');
                var poster = $("<img>").prop({'src': data[i].poster, 'alt': data[i].name});
                poster.on('error', function () {
                    $(this).prop({'src': '../images/' + type + '_error.png', 'alt': 'no poster'});
                })
                var name = $("<p></p>").text(data[i].name);
                var info = $('<div></div>').addClass('info');
                var imdbScore = $('<span></span>').text(data[i].imdbRating === 0 ? '-' : data[i].imdbRating).addClass('score');
                var imdbLabel = $('<span></span>').text('IMDb').addClass('score-label');
                var doubanScore = $('<span></span>').text(data[i].doubanRating === 0 ? '-' : data[i].doubanRating).addClass('score');
                var doubanLabel = $('<span></span>').text('Douban').addClass('score-label');
                var scores = $('<div></div>').addClass('scores').append(imdbScore, doubanScore, imdbLabel, doubanLabel);
                var detail = $('<div></div>').addClass('detail').html(
                    '<strong>Directors: </strong>' + data[i].director.join(' / ') + '<br><br>'
                    + '<strong>Writers: </strong>' + data[i].writer.join(' / ') + '<br><br>'
                    + '<strong>Stars: </strong>' + data[i].actor.join(' / ')
                );
                item.append(imgWp.append(poster), info.append(scores, detail), name);
                row.append(item);
                if (i % 4 == 3) {
                    list.append(row);
                    row.waypoint(function () {
                        $(this.element).addClass('animated fadeInUp');
                        this.destroy();
                    }, {
                        offset: function () {
                            return $(window).height() - 150;
                        }
                    });
                }
            }
            if (data.length % 4) {
                list.append(row);
                row.waypoint(function () {
                    $(this.element).addClass('animated fadeInUp');
                    this.destroy();
                }, {
                    offset: function () {
                        return $(window).height() - 150;
                    }
                });
            }
            $pageIndex.val(parseInt($pageIndex.val()) + 1);
            if (data.length < form.find('input[name="pagesize"]').val())
                moreBtn.text('╮(￣▽￣)╭ Sorry, no more ' + type + 's...');
            else {
                moreBtn.text('More ').addClass('more').attr('href', 'javascript:void(getMore())');
                permissionFlag = 1;
            }
            if (callback && typeof callback === 'function')
                callback();
            form.removeAttr('style');
        }
    });
}

function getMore() {
    getList(function () {
        $('body').animate({scrollTop: '+=720'}, 'slow');
    });
}

function setAutoGetMore() {
    var $body = $('body'), $window = $(window);
    $window.on('scroll', function () {
        if ($window.scrollTop() + $window.height() > $body.height() - 300) {
            getList();
        }
    });
}

var $detailPop, popFlag = 0, fadeFlag = 0, popId = 0, hoverId = 0;
function initPopInfo() {
    $detailPop = $('#detail-pop');
    if ($detailPop.length > 0)
        return;
    $detailPop = $('<div></div>').prop('id', 'detail-pop').css('display', 'none');
    $detailPop.append(
        $('<h3></h3>'),
        $('<p></p>').addClass('scores').append(
            $('<strong></strong>').prop('title', 'IMDB Rating'),
            $('<strong></strong>').prop('title', 'Douban Rating')
        ),
        $('<p></p>').addClass('directors'),
        $('<p></p>').addClass('writers'),
        $('<p></p>').addClass('stars'),
        $('<p></p>').addClass('plot'),
        $('<input>').prop({'type': 'checkbox', 'id': 'pop-watched', 'name': 'watched'}).val(1).change(function () {
            $.get('/watched?movieid=' + popId + '&' + $(this).serialize());
        }),
        $('<label></label>').text('Watched').prop('for', 'pop-watched'),
        $('<input>').prop({'type': 'checkbox', 'id': 'pop-like', 'name': 'like'}).val(1).change(function () {
            if ($(this).prop('checked'))
                $(this).siblings('#pop-dislike').prop('checked', false);
            $.get('/like?movieid=' + popId + '&' + $(this).serialize());
        }),
        $('<label></label>').text('Like').prop('for', 'pop-like'),
        $('<input>').prop({'type': 'checkbox', 'id': 'pop-dislike', 'name': 'dislike'}).val(1).change(function () {
            if ($(this).prop('checked'))
                $(this).siblings('#pop-like').prop('checked', false);
            $.get('/dislike?movieid=' + popId + '&' + $(this).serialize());
        }),
        $('<label></label>').text('Dislike').prop('for', 'pop-dislike'),
        $('<div></div>').text('It seems you haven\'t logged in. ^_^').addClass('tooltip').css('display', 'none')
    );
    if (!loggedIn) {
        $detailPop.children('input').prop('disabled', true);
        $detailPop.children('label').click(function () {
            $detailPop.children('.tooltip').fadeIn('fast', function () {
                setTimeout(function () {
                    $detailPop.children('.tooltip').fadeOut();
                }, 1000);
            })
        });
    }
    $detailPop.mouseenter(
        function () {
            if (fadeFlag) {
                clearTimeout(fadeFlag);
                fadeFlag = 0;
            }
        }
    );
    $('body').append($detailPop);
}

function setPopInfo($item, model) {
    var temp = function () {
        hoverId = 0;
        var offsetLeft = $item.offset().left;
        if (popFlag) {
            clearTimeout(popFlag);
            popFlag = 0;
        } else
            fadeFlag = setTimeout(function () {
                fadeFlag = 0;
                $detailPop.animate({
                        opacity: 0,
                        left: (offsetLeft > $(window).width() / 2) ? '-=40' : '+=40'
                    },
                    'fast',
                    function () {
                        $detailPop.css('display', 'none');
                    }
                );
            }, 200);
    };
    $item.hover(function () {
        hoverId = model.id;
        if (fadeFlag && popId === model.id) {
            clearTimeout(fadeFlag);
            fadeFlag = 0;
        } else {
            var offsetLeft = $item.offset().left, offsetTop = $item[0].getBoundingClientRect().top;
            popFlag = setTimeout(function () {
                popFlag = 0;
                var popping = function () {
                    popId = model.id;
                    writePopDetail(model);
                    if (offsetLeft > $(window).width() / 2)
                        $detailPop.css('left', offsetLeft - $detailPop.outerWidth() - 50);
                    else
                        $detailPop.css('left', offsetLeft + $item.outerWidth() + 50);
                    var top = offsetTop + ($item.outerHeight() - $detailPop.outerHeight()) / 2;
                    if (top < 0)
                        top = 0;
                    else if (top + $detailPop.outerHeight() > $(window).height())
                        top = $(window).height() - $detailPop.outerHeight();
                    $detailPop.css({'display': 'block', 'opacity': '0', 'top': top});
                    if (offsetLeft > $(window).width() / 2)
                        $detailPop.animate({opacity: 1, left: '+=40'}, 'fast');
                    else
                        $detailPop.animate({opacity: 1, left: '-=40'}, 'fast');
                };
                if (loggedIn)
                    $.get('/movie/getstatus?movieid=' + model.id, function (d) {
                        if (hoverId === model.id) {
                            popping();
                            $detailPop.find('#pop-watched').prop('checked', d[0]);
                            $detailPop.find('#pop-like').prop('checked', d[1] === null ? false : d[1]);
                            $detailPop.find('#pop-dislike').prop('checked', d[1] === null ? false : !d[1]);
                        }
                    });
                else
                    popping();
            }, 500);
            $detailPop.off('mouseleave');
            $detailPop.mouseleave(temp);
        }
    }, temp);
}


function resolve(peopleObjects) {
    if (peopleObjects.length === 0)
        return;
    if (typeof peopleObjects[0] === 'string')
        return peopleObjects.join(' / ');
    var s = peopleObjects[0].name;
    for (var i = 1; i < peopleObjects.length; i++)
        s += ' / ' + peopleObjects[i].name;
    return s;
}

function writePopDetail(model) {
    $detailPop.children('h3').text(model.name);
    $detailPop.children('.scores').children().eq(0).text(model.imdbRating === 0 ? '-' : model.imdbRating);
    $detailPop.children('.scores').children().eq(1).text(model.doubanRating === 0 ? '-' : model.doubanRating);
    $detailPop.children('.directors').html('<strong>Directors: </strong>' + resolve(model.director))
        .css('display', model.director.length === 0 ? 'none' : 'block');
    $detailPop.children('.writers').html('<strong>Writers: </strong>' + resolve(model.writer))
        .css('display', model.writer.length === 0 ? 'none' : 'block');
    $detailPop.children('.stars').html('<strong>Stars: </strong>' + resolve(model.actor))
        .css('display', model.actor.length === 0 ? 'none' : 'block');
    $detailPop.children('.plot').html('<strong>Plot: </strong>' + cut(model.plot))
        .css('display', model.plot === undefined || model.plot.length === 0 || model.plot === 'N/A' ? 'none' : 'block');
}

function cut(str) {
    if (str === undefined || str === null || str.trim().length === 0)
        return '';
    var words = str.split(' ');
    if (words.length > 35)
        return words.slice(0, 35).join(' ') + '...';
    return str;
}

function modifyAvatar() {
    var $shade = $('<div></div>').addClass('x-shade');
    var $pop = $('<div></div>').addClass('avatar-pop');
    var $img = $('<img>').prop('src', '/images/default.jpg');
    var $form = $('<form action="/upload" method="post" enctype="multipart/form-data"></form>');
    var $input = $('<input type="file" name="file">');
    var $btn = $('<input type="submit" value="Upload">');
    $shade.append($pop.append($img, $form.append($input, $btn))).css('display', 'none');
    $('body').append($shade);
    $shade.fadeIn('fast').click(function () {
        $('content').removeClass('modal-active');
        $(this).remove();
    });
    $('content').addClass('modal-active');
}