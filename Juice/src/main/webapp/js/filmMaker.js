/**
 * Created by lenovo on 2017/5/17.
 */
$(document).ready(function () {

    $("#co-workers table tr").mousemove(function () {
        $(this).find(".recommand-filmMaker-name").css({
            "text-decoration": "underline",
            "text-underline-color": "black"
        });
    });
    $("#co-workers table tr").mouseout(function () {
        $(this).find(".recommand-filmMaker-name").css({
            "text-decoration": "none"
        });
    });

//实现翻页时改变当前页数
    $("#production-last-page").click(function () {
        if (parseInt($("#production-page-index").text()) == 1) {
            return;
        }
        $("#production-page-index").text(function (i, origText) {
            return parseInt(origText) - 1;
        });
        getProductionPage();
    });
    $("#production-next-page").click(function () {
        totalPage = $("#production-total-page").text();
        if (parseInt($("#production-page-index").text()) == parseInt(totalPage)) {
            return;
        }
        $("#production-page-index").text(function (i, origText) {
            return parseInt(origText) + 1;
        });
        getProductionPage();
    });

    $("#co-workers-last-page").click(function () {
        if (parseInt($("#co-workers-page-index").text()) == 1) {
            return;
        }
        $("#co-workers-page-index").text(function (i, origText) {
            return parseInt(origText) - 1;
        });
        getCoWorkerPage();
    });
    $("#co-workers-next-page").click(function () {
        totalPage = $("#co-workers-total-page").text();
        if (parseInt($("#co-workers-page-index").text()) == parseInt(totalPage)) {
            return;
        }
        $("#co-workers-page-index").text(function (i, origText) {
            return parseInt(origText) + 1;
        });
        getCoWorkerPage();
    });

    function getProductionPage() {
        var curPageIndex=parseInt($("#production #production-page-index").text())-1;
        for(var i=0;i<4;i++){
            $("#production .production-table td").eq(i).fadeOut();
        }
        $.get("/figure/getProduction?figureID="+figureID+"&pageIndex="+curPageIndex,function (data) {
            for(var index=0;index<data.length;index++){
                $("#production .production-table td").eq(index).find("a").attr("href","/movie/j"+data[index].productionPiece.id);
                $("#production .production-table td").eq(index).find("img").attr("src",data[index].productionPiece.poster);
                $("#production .production-table td").eq(index).find("p.production-name").text(data[index].productionPiece.name);
                $("#production .production-table td").eq(index).find("p.role").text(data[index].roles);
                $("#production .production-table td").eq(index).find("p.year").text(data[index].year);
                $("#production .production-table td").eq(index).fadeIn();
            }
        });
    }

    function getCoWorkerPage() {
        var curPageIndex=parseInt($("#co-workers #co-workers-page-index").text())-1;
        for(var i=0;i<4;i++){
            $("#co-workers .co-workers-tr-piece").eq(i).fadeOut();
        }
        $.get("/figure/getCoWorkers?figureID="+figureID+"&pageIndex="+curPageIndex,function (data) {
            for(var index=0;index<data.length;index++){
                $("#co-workers .co-workers-tr-piece").eq(index).find("a").attr("href","/figure/j"+data[index].figureID);
                $("#co-workers .co-workers-tr-piece").eq(index).find("img").attr("src",data[index].avatar);
                $("#co-workers .co-workers-tr-piece").eq(index).find("p").text(data[index].name);
                $("#co-workers .co-workers-tr-piece").eq(index).fadeIn();
            }
        });
    }


    var chart = echarts.init($('#chart')[0]);
    chart.setOption({
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
                type: 'bar',
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
    $.get('/statistics/about?figureId=' + figureID, function (d) {
        chart.hideLoading();
        chart.setOption({
            xAxis: {
                data: d[0]
            },
            series: [
                {
                    name: 'Box Office',
                    data: d[3]
                },
                {
                    name: 'IMDb Rating',
                    data: d[1]
                },
                {
                    name: 'Douban Rating',
                    data: d[2]
                }
            ]
        });
    })
});