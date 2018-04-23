/**
 * Created by I Like Milk on 2017/6/6.
 */
$(function () {
    var $buttons = $('.chart-descriptions a');
    var $shades = $('.s-shade');
    var $closeBtn = $('.close-button');
    var $contentBox = $('body > .content');
    $buttons.click(function () {
        $shades.eq($buttons.index(this)).addClass('show');
        $contentBox.addClass('modal-active');
    });
    $closeBtn.click(function () {
        $(this).parent().removeClass('show');
        $contentBox.removeClass('modal-active');
    });


    var chart1 = echarts.init($('.chart.fir')[0], 'dark');
    chart1.setOption({
        title: {
            text: 'Votes and Rating'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            }
        },
        axisPointer: {
            label: {
                textStyle: {
                    color: '#000'
                }
            }
        },
        toolbox: {
            feature: {
                saveAsImage: {show: true}
            }
        },
        legend: {
            data:['The Number of Votes', 'Average Rating']
        },
        xAxis: {
            data: []
        },
        yAxis: [
            {
                name: 'The Number of Votes',
                position: 'left',
                splitLine: {show: false}
            },
            {
                name: 'Average Rating',
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
                name: 'The Number of Votes',
                type: 'bar',
                data: []
            },
            {
                name: 'Average Rating',
                type: 'line',
                yAxisIndex: 1,
                data: []
            }
        ]
    });
    chart1.showLoading();
    var setChart1 = function (d) {
        if (d === undefined)
            return;
        chart1.hideLoading();
        chart1.setOption({
            xAxis: {
                data: d[0]
            },
            series: [
                {
                    name: 'Average Rating',
                    data: d[1]
                },
                {
                    name: 'The Number of Votes',
                    data: d[2]
                }
            ]
        });
    };
    var $inputs = $('.tab > input').prop('disabled', true);

    var dataImdb, dataDouban, dataCountry = [];
    $.get('/statistics/get?type=0', function (data) {
        dataImdb = data;
        setChart1(dataImdb);
        $inputs.prop('disabled', false);
    });
    $.get('/statistics/get?type=1', function (data1) {
        dataDouban = data1;
        if ($('#douban').prop('checked'))
            setChart1(dataDouban);
    });

    $inputs.change(function () {
        if ($inputs.eq(0).prop('checked'))
            setChart1(dataImdb);
        else
            setChart1(dataDouban);
    });

    var chart2 = echarts.init($('.chart.sec')[0], 'dark');
    chart2.setOption({
        title: {
            text: 'How many movies your country released? (2017)',
            left: 'center',
            top: 'top'
        },
        tooltip: {
            trigger: 'item',
            formatter: function (params) {
                return params.seriesName + '<br/>' + params.name + ' : ' + params.value;
            }
        },
        toolbox: {
            show: true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
            }
        },
        visualMap: {
            min: 0,
            max: 500,
            text:['Many','Few'],
            textStyle: {
                color: '#fff'
            },
            realtime: false,
            calculable: true,
            inRange: {
                color: ['lightskyblue','yellow', 'orangered']
            }
        },
        series: [
            {
                name: 'How many movies your country released? (2017)',
                type: 'map',
                mapType: 'world',
                roam: true,
                itemStyle: {
                    emphasis: {label: {show: true}}
                },
                data: []
            }
        ]
    });
    chart2.showLoading();

    $.get('/statistics/get?type=2', function (data2) {
        for (var i = 0; i < data2.length; i++) {
            dataCountry[i] = {};
            dataCountry[i]['name'] = data2[i][0];
            dataCountry[i]['value'] = data2[i][1];
        }
        chart2.hideLoading();
        chart2.setOption({
            series: [
                {
                    name: 'How many movies your country released? (2017)',
                    data: dataCountry
                }
            ]
        });
    })
});