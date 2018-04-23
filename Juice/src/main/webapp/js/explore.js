/**
 * Created by I Like Milk on 2017/5/30.
 */
var animationEnd = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';
var loggedIn = false;
var $shade, $logInBtn;
var $content, $topCard, $bottomCard;
$(function () {
    $shade = $('.shade');
    $logInBtn = $('.log-in-btn');
    $content = $('.content');

    if ($.cookie('user') !== undefined)
        loggedIn = true;
    if (!loggedIn)
        promptLogin();

    var $optBar = $('.opt-bar *'), $bottomBtns = $('.opt-bar > *');
    $optBar.prop('disabled', 'true');
    $bottomBtns.css('cursor', 'wait');
    var buffer;
    var p = 0;
    $topCard = $('.card[data-show]');
    $bottomCard = $('.card.preload');
    getItems(function (data) {
        makeCard($topCard, data[0]);
        makeCard($bottomCard, data[1]);
        buffer = data;
        p = 2;
        $optBar.removeAttr('disabled');
        $bottomBtns.removeAttr('style');
    });

    $('#next-btn').click(function (event, param) {
        $optBar.prop('disabled', 'true');
        $bottomBtns.css('cursor', 'wait');
        if (loggedIn && param === undefined)
            $.get('/skim?' + $topCard.find('input[name="movieid"]').serialize());
        showNext(param === undefined ? 0 : 1);
        if ($topCard.find('.loader').length === 0) {
            $optBar.removeAttr('disabled');
            $bottomBtns.removeAttr('style');
        }
        if (p < buffer.length && $bottomCard.find('.loader').length > 0)
            makeCard($bottomCard, buffer[p++]);
        if (p >= buffer.length)
            getItems(function (data) {
                buffer = data;
                p = 0;
                if ($topCard.find('.loader').length > 0)
                    makeCard($topCard, data[p++]);
                if ($bottomCard.find('.loader').length > 0)
                    makeCard($bottomCard, data[p++]);
                $optBar.removeAttr('disabled');
                $bottomBtns.removeAttr('style');
            });
    });
    if (loggedIn) {
        $('#like-btn input').change(function () {
            $.get('/like?' + $topCard.find('input[name="movieid"]').serialize() + '&' + $(this).serialize());
        });
        $('#dislike-btn').click(function () {
            $.get('/drop?' + $topCard.find('input[name="movieid"]').serialize());
            $('#next-btn').trigger('click', 1);
        });
    } else {
        var temp = function () {
            $(this).prop('checked', false);
            $('.opt-bar').append(
                $('<div></div>').text('Only if you log in we can remember you preference.')
                    .addClass('tooltip').one(animationEnd, function () {
                    $(this).remove();
                })
            );
        };
        $('#like-btn input').change(temp);
        $('#dislike-btn').click(temp);
    }
});
function showNext(type) {
    $topCard.addClass('animated ' + (type === 0 ? 'fadeOutLeft' : 'my-zoom-out-down')).one(animationEnd, function () {
        $(this).remove();
    });
    $('#like-btn input').prop('checked', false);
    var $nextTemp = $('<div></div>').append(newLoader()).addClass('card preload');
    $topCard = $bottomCard.removeClass('preload').before($nextTemp);
    $bottomCard = $nextTemp;
    $topCard.attr('data-show', '');
}

function newLoader() {
    var $loader = $('<div></div>').addClass('loader line-scale-pulse-out-rapid');
    var $div = [];
    for (var i = 0; i < 5; i++)
        $div.push($('<div></div>'));
    $loader.append($div);
    return $loader;
}

var runningFlag = 0;
function getItems(callback) {
    if (runningFlag === 1)
        return;
    runningFlag = 1;
    $.get('/wander?size=' + $('input[name="size"]').val(), function (data) {
        if (callback !== undefined && typeof callback === 'function')
            callback(data);
        runningFlag = 0;
    });
}

function makeCard($wrapper, model) {
    if (model === undefined || model === null)
        return;
    $wrapper.empty();
    var $hiddenId = $('<input>').prop({
        'type': 'hidden',
        'name': 'movieid'
    }).val(model.id);
    var $name = $('<div></div>').append($('<a></a>').text(model.name).prop({
        'href': '/movie/j' + model.id,
        'target': '_blank'
    })).addClass('title');
    var $posterImg = $('<img>').prop({
        'width': 200,
        'src': model.poster,
        'alt': model.name,
        'title': 'Watch the trailer.'
    }).click(function () {
        popVideo('http://139.199.172.167/mp4/' + model.id + '.mp4');
    });
    var $poster = $('<div></div>').append($posterImg).addClass('poster-wp');
    var $info = $('<table></table>').addClass('info');
    var $genre = $('<tr></tr>').append(
        $('<th></th>').text('Genre'),
        $('<td></td>').text(model.genre.join(' / '))
    );
    var $directors = $('<tr></tr>').append(
        $('<th></th>').text('Directors'),
        $('<td></td>').text(resolve(model.director))
    );
    var $writers = $('<tr></tr>').append(
        $('<th></th>').text('Writers'),
        $('<td></td>').text(resolve(model.writer))
    );
    var $stars = $('<tr></tr>').append(
        $('<th></th>').text('Stars'),
        $('<td></td>').text(resolve(model.actor))
    );
    var $plot = $('<tr></tr>').append(
        $('<th></th>').text('Plot'),
        $('<td></td>').text(cut(model.plot))
    );

    var $watchedInput = $('<label></label>').append(
        $('<input>').prop({
            'type': 'checkbox',
            'name': 'watched'
        }).val(1).change((!loggedIn) ? function () {
            $wrapper.append(
                $('<div></div>').text('Only if you log in we can remember which you\'ve watched.')
                    .addClass('tooltip').one(animationEnd, function () {
                    $(this).remove();
                })
            )
        } : function () {
            var $thisInput = $(this);
            $.get('/watched?movieid=' + model.id + '&' + $thisInput.serialize(), function (data) {
                if (data === 1)
                    $watchedInput.toggleClass('checked');
            });
        }),
        $('<span></span>').addClass('yes'),
        $('<span></span>').addClass('no'),
        $('<span></span>').addClass('toggle')
    );

    var $watched = $('<div></div>').addClass('watched-area').append(
        $('<p></p>').text('Have you watched it?'),
        $watchedInput
    );
    $info.append($genre, $directors, $writers, $stars, $plot);
    if (model.genre.length === 0)
        $genre.remove();
    if (model.director.length === 0)
        $directors.remove();
    if (model.writer.length === 0)
        $writers.remove();
    if (model.actor.length === 0)
        $stars.remove();
    if (model.plot.length === 0 || model.plot === 'N/A')
        $stars.remove();
    $wrapper.append($hiddenId, $name, $poster, $info, $watched);
}

function promptLogin() {
    var $model;
    $shade.fadeIn('fast');
    $logInBtn.addClass('animated tada').one(animationEnd, function () {
       $(this).removeClass('animated tada');
    });
    $('body').append(
        $model = $('<div></div>').append(
            $('<span></span>').text('x').click(function () {
                $shade.click();
            }),
            $('<p></p>').html('We can provide more suitable recommendation for you if you log in.<br><br>You can just browse the random movies.')
        ).addClass('compatible-modal animated bounceIn')
    );
    $shade.one('click', function () {
        $model.addClass('bounceOut').one(animationEnd, function () {
            $(this).remove();
        });
    });
}

function popVideo(src) {
    var $source;
    var $player = $('<video></video>').prop({
        'width': 900,
        'controls': 'controls',
    }).append(
        $source = $('<source>').prop({
            'src': src,
            'type': 'video/mp4'
        })
    ).addClass('animated zoomInDown').one(animationEnd, function () {
        $(this)[0].play();
    });

    var $videoShade = $('<div></div>').addClass('video-shade animated fadeIn').click(function () {
        $player.addClass('zoomOutUp');
        $(this).addClass('fadeOut').one(animationEnd, function () {
            $(this).remove();
        });
        $content.removeClass('modal-active');
    });

    $('body').append($videoShade);
    $content.addClass('modal-active');
    var video = $player[0];
    var $playController = $('<div></div>').addClass('play-controller');
    video.onplaying = function () {
        $playController.removeClass('show');
        $loaderWp.css('display', 'none');
    };
    video.onpause = video.onended = function () {
        if (video.readyState === 4)
            $playController.addClass('show');
    };
    video.volume = .5;
    $player.click(function (e) {
        if (video.paused)
            video.play();
        else
            video.pause();
        e.stopPropagation();
    });
    var $loaderWp = $('<div></div>').addClass('loader-wp').append(newLoader()).css('display', 'none');

    $videoShade.append($player, $playController, $loaderWp);


    $source.on('error', function () {
        $player.css('pointer-events', 'none');
        $loaderWp.css('display', 'none');
        $videoShade.append($('<h2></h2>').addClass('no-video animated bounceIn').text('Oops, the trailer of this movie is\'t found.'));
    });

    video.onwaiting = video.onstalled = video.onloadstart = video.onloadedmetadata = video.ondurationchange = function () {
        if (video.readyState)
            $loaderWp.css('display', 'block');
    };
}