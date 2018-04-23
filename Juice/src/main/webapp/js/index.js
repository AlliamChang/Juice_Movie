/**
 * Created by I Like Milk on 2017/6/6.
 */
$(function () {
    initPopInfo();

    var scrollThreshold = 4, delta = 0;
    var transitionEnd = 'transitionend webkitTransitionEnd';
    var $aboveSec = $('.above'), $belowSec = $('.below'), $body = $('body');
    $(window).on('mousewheel DOMMouseScroll', function (e) {
        delta += (e.originalEvent.wheelDelta && (e.originalEvent.wheelDelta > 0 ? 1 : -1)) || (e.originalEvent.detail && (e.originalEvent.detail > 0 ? -1 : 1));
        if ($(window).scrollTop() > 0)
            delta = 0;
        if (delta <= -scrollThreshold) {
            delta = 0;
            if ($body.attr('data-hidden') !== undefined && $belowSec.hasClass('hidden')) {
                $aboveSec.addClass('hidden');
                $belowSec.removeClass('hidden').on(transitionEnd, function () {
                    $body.removeAttr('data-hidden');
                    $belowSec.off(transitionEnd);
                });
            }
        }

        if (delta >= scrollThreshold) {
            delta = 0;
            if ($body.attr('data-hidden') === undefined && $aboveSec.hasClass('hidden')) {
                $body.attr('data-hidden', '');
                $belowSec.addClass('hidden');
                $aboveSec.removeClass('hidden');
            }
        }
    });

    var $graph = $('#graph'), $sliderWp = $('#slider'), $slider = $('#slider > ul'), items = [], nowShowing = 0;
    var chart = echarts.init($graph[0]);
    chart.setOption({
        title: {
            text: 'Box Office And Rating'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            }
        },
        toolbox: {
            feature: {
                saveAsImage: {show: true}
            }
        },
        legend: {
            data:['Box Office', 'IMDb Rating', 'Douban Rating']
        },
        xAxis: {
            data: []
        },
        yAxis: [
            {
                name: 'Box Office',
                position: 'left',
                splitLine: {show: false},
                axisLabel: {
                    formatter: function (value) {
                        if (value === 0)
                            return '￥0';
                        return '￥' + value / 1000000 + 'M';
                    }
                }
            },
            {
                name: 'Rating',
                position: 'right',
                splitLine: {show: false},
                type: 'value',
                interval: 1,
                min: 0,
                max: 10
            }
        ],
        series: [
            {
                name: 'Box Office',
                type: 'line',
                data: []
            },
            {
                name: 'IMDb Rating',
                type: 'line',
                yAxisIndex: 1,
                data: []
            },
            {
                name: 'Douban Rating',
                type: 'line',
                yAxisIndex: 1,
                data: []
            }
        ]
    });
    chart.showLoading();
    var setChart = function (data) {
        chart.hideLoading();
        chart.setOption({
            xAxis: {
                data: data[0]
            },
            series: [
                {
                    name: 'Box Office',
                    data: data[1]
                },
                {
                    name: 'IMDb Rating',
                    data: data[3]
                },
                {
                    name: 'Douban Rating',
                    data: data[2]
                }
            ]
        });
    };
    var chartData = {}, firstId;
    $.get('/movie/nowplaying', function (data) {
        firstId = data[0].id;
        data.forEach(function (val) {
            $.get('/movie/getdata?movieid=' + val.id, function (data1) {
                chartData[val.id] = data1;
                for (var i = 0; i < chartData[val.id][1].length; i++)
                    chartData[val.id][1][i] = Math.round(chartData[val.id][1][i] * 10000);
                if (firstId === val.id)
                    setChart(chartData[val.id]);
            });
            items.push($('<li></li>').append(
                $('<img>').prop({
                    'src': val.poster,
                    'alt': val.name
                }).on('error', function () {
                    $(this).prop({
                        'src': '../images/movie_error.png',
                        'alt': 'no poster'
                    })
                }),
                $('<div></div>').addClass('slider-dropdown').append(
                    $('<a></a>').prop({
                        'href': 'https://movie.douban.com/subject/' + val.doubanID + '/cinema/',
                        'title': 'Buy tickets.',
                        'target': '_blank'
                    }).addClass('ticket'),
                    $('<a></a>').prop({
                        'href': '../movie/j' + val.id,
                        'title': 'Detail page ->',
                        'target': '_blank'
                    }).addClass('detail')
                )
            ).hover(function () {
                $(this).find('.slider-dropdown').slideDown('fast');
            }, function () {
                $(this).find('.slider-dropdown').slideUp('fast');
            }).attr('data-movie', val.id))
        });


        $slider.append(items).animate({left: ($(window).width() - items[0].outerWidth()) * .5 + 'px'}, 'fast', 'swing', function () {
            items[0].addClass('show');
            $slider.css('left', 'calc(50% - ' + items[0].outerWidth() * .5 + 'px');

            var totalWidth = 20 * (items.length - 1);
            for (var i = 0; i < items.length; i++)
                totalWidth += items[i].outerWidth();
            $slider.css('width', totalWidth + 'px');
        });
        items.forEach(function (v, i) {
            v.click(function () {
                setChart(chartData[v.attr('data-movie')]);
                var width = $(this).outerWidth(), marginL = i === 0 ? 0 : 20;
                if (i !== nowShowing)
                    width *= .8;
                items[nowShowing].removeClass('show');
                $slider.css('left', 'calc(50% - ' + (width * .5 + $(this).position().left + marginL) + 'px');
                $(this).addClass('show');
                nowShowing = i;
            });
        });
    });


    var recItems = new Array(20), $recUl = $('.recommendation > .movie-slider > ul');
    var $recSlideCtrl = $('.recommendation > .movie-slider > .slide-ctrl');
    for (var i = 0; i < 20; i++)
        recItems[i] = $('<li></li>').append($('<a></a>').append(
            $('<div></div>').addClass('image-wp').append($('<img>').prop({
                'src': '../images/loading.gif',
                'alt': 'loading'
            })),
            $('<p></p>')
        ));
    $recUl.append(recItems);
    if ($recUl.length)
        $.get('/movie/recommendation', function (data) {
            var slideInterval = 0;
            data.forEach(function (val, index) {
                recItems[index].children('a').prop({
                    'href': (val.type === "SERIES" ? '../tv-show/j' : '../movie/j') + val.id,
                    'target': '_blank'
                }).hover(function () {
                    if (slideInterval)
                        clearInterval(slideInterval);
                }, function () {
                    slideInterval = setInterval(autoSlide, 3000);
                });
                recItems[index].find('img').prop({
                    'src': val.poster,
                    'alt': val.name
                }).on('error', function () {
                    $(this).prop({
                        'src': '../images/' + (val.type === 'SERIES' ? 'tv-show_error.png' : 'movie_error.png'),
                        'alt': 'no poster'
                    });
                });
                recItems[index].find('p').text(val.name);
                setPopInfo(recItems[index], val);
            });
            var $dots = $recSlideCtrl.children('span'), pageIndex = 0;
            $dots.click(function () {
                if (slideInterval)
                    clearInterval(slideInterval);
                $recUl.css('left', (-(pageIndex = 1 * $(this).attr('data-index')) * 860) + 'px');
                $dots.removeClass('active');
                $(this).addClass('active');
                slideInterval = setInterval(autoSlide, 3000);
            });

            slideInterval = setInterval(autoSlide, 3000);
            function autoSlide() {
                if ($('#detail-pop').css('display') === 'block')
                    return;
                pageIndex += 1;
                if (pageIndex > 3)
                    pageIndex = 0;
                $dots.eq(pageIndex).click();
            }
        });

    var movieItems = new Array(40), $movieUl = $('.hottest-movies > .movie-slider > ul');
    var $movieSlideCtrl = $('.hottest-movies > .movie-slider > .slide-ctrl');
    for (var i = 0; i < 40; i++)
        movieItems[i] = $('<li></li>').append($('<a></a>').append(
            $('<div></div>').addClass('image-wp').append($('<img>').prop({
                'src': '../images/loading.gif',
                'alt': 'loading'
            })),
            $('<p></p>')
        ));
    $movieUl.append(movieItems);
    $movieSlideCtrl.children('span').click(function () {
        $movieUl.css('left', (-$(this).attr('data-index') * 860) + 'px');
        $movieSlideCtrl.children('span').removeClass('active');
        $(this).addClass('active');
    });

    for (var i = 0; i < 4; i++)
        $.get('/get?' + 'type=0&pagesize=10&sort=0&pageindex=' + i, function (data) {
            var i = this.url.charAt(this.url.length - 1) * 1;
            for (var j = 0; j < 2; j++)
                for (var k = 0; k < 5; k++) {
                    var cardIndex = j * 20 + i * 5 + k;
                    var dataIndex = j * 5 + k;
                    movieItems[cardIndex].children().prop({
                        'href': '../movie/j' + data[dataIndex].id,
                        'target': '_blank'
                    });
                    movieItems[cardIndex].find('img').prop({
                        'src': data[dataIndex].poster,
                        'alt': data[dataIndex].name
                    }).on('error', function () {
                        $(this).prop({
                            'src': '../images/movie_error.png',
                            'alt': 'no poster'
                        });
                    });
                    movieItems[cardIndex].find('p').text(data[dataIndex].name);
                    setPopInfo(movieItems[cardIndex], data[dataIndex]);
                }
        });

    var tvshowItems = new Array(40), $tvshowUl = $('.hottest-tv-shows > .movie-slider > ul');
    var $tvshowSlideCtrl = $('.hottest-tv-shows > .movie-slider > .slide-ctrl');
    for (var i = 0; i < 40; i++)
        tvshowItems[i] = $('<li></li>').append($('<a></a>').append(
            $('<div></div>').addClass('image-wp').append($('<img>').prop({
                'src': '../images/loading.gif',
                'alt': 'loading'
            })),
            $('<p></p>')
        ));
    $tvshowUl.append(tvshowItems);
    $tvshowSlideCtrl.children('span').click(function () {
        $tvshowUl.css('left', (-$(this).attr('data-index') * 860) + 'px');
        $tvshowSlideCtrl.children('span').removeClass('active');
        $(this).addClass('active');
    });

    for (var i = 0; i < 4; i++)
        $.get('/get?' + 'type=1&pagesize=10&sort=0&pageindex=' + i, function (data) {
            var i = this.url.charAt(this.url.length - 1) * 1;
            for (var j = 0; j < 2; j++)
                for (var k = 0; k < 5; k++) {
                    var cardIndex = j * 20 + i * 5 + k;
                    var dataIndex = j * 5 + k;
                    tvshowItems[cardIndex].children().prop({
                        'href': '../tv-show/j' + data[dataIndex].id,
                        'target': '_blank'
                    });
                    tvshowItems[cardIndex].find('img').prop({
                        'src': data[dataIndex].poster,
                        'alt': data[dataIndex].name
                    }).on('error', function () {
                        $(this).prop({
                            'src': 'tv-show_error.png',
                            'alt': 'no poster'
                        });
                    });
                    tvshowItems[cardIndex].find('p').text(data[dataIndex].name);
                    setPopInfo(tvshowItems[cardIndex], data[dataIndex]);
                }
        });

});