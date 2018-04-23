/**
 * Created by sparkler on 2017/6/11.
 */


var searchType = 0;

$(document).ready(function () {
    $("#search-type button").mouseenter(function () {
        $(this).css({
            "border": "2px solid skyblue"
        });
    });

    $("#search-type button").mouseout(function () {
        $(this).css({
            "border-color": "transparent"
        });
    });

    $(".search-type-but").click(function () {
        if ($(this).attr("name").indexOf("selected") == 0) {
            return;
        }
        searchType = $(this).attr("id");
        chooseSearchType(searchType);
        $("#movie-searchResult-page-index").text(1);
        // alert(searchType);
        var index = $(".search-type-but").index(this);
        for (i = 0; i < 3; i++) {
            if (index == i) {
                continue;
            }
            name = $(".search-type-but").eq(i).attr("name");
            if (name.indexOf("selected") == 0) {
                $(".search-type-but").eq(i).css({
                    "color": "gray",
                    "background-color": "transparent"
                });
                $(".search-type-but").eq(i).attr("name", "unselected");
            }
        }
        $(this).css({
            "color": "white",
            "background-color": "skyblue"
        });
        $(this).attr("name", "selected");
        searchType = $(this).attr("id");

    });


    //实现翻页时改变当前页数
    $("#movie-searchResult-last-page").click(function () {
        $("#movie-searchResult-page-index").text(function (i, origText) {
            if (parseInt(origText) == 1) {
                return origText;
            }
            return parseInt(origText) - 1;
        });
        var curPageIndex = parseInt($("#movie-searchResult-page-index").text()) - 1;
        getSearchResultByType(searchType, curPageIndex);
        if (curPageIndex == 0) {
            $(".last-page").hide();
        }
        $(".next-page").slideDown();

    });
    $("#movie-searchResult-next-page").click(function () {
        $("#movie-searchResult-page-index").text(function (i, origText) {
            // totalPage = $("#movie-relativePeople-total-page").text();
            // if (parseInt(origText) == parseInt(totalPage)) {
            //     return origText;
            // }
            return parseInt(origText) + 1;
        });
        var curPageIndex = parseInt($("#movie-searchResult-page-index").text()) - 1;
        getSearchResultByType(searchType, curPageIndex);
        if (curPageIndex == 0) {
            $(".last-page").hide();
        }
        $(".last-page").slideDown();
    });

});

function chooseSearchType(index) {
    getSearchResultByType(index, 0);
}

//得到搜索结果
function getSearchResultByType(searchType, curPageIndex) {
    // for (var i = 0; i < 10; i++) {
    //     $(".movie-result-list tr").eq(i).slideUp();
    // }
    $.get("/getSearchResultByType?keyword=" + keyword + "&searchType=" + searchType + "&curPageIndex=" + curPageIndex, function (data) {
        loadResults(searchType, data);
        // alert("hahaha");
    });
}

function loadResults(searchType, data) {
    for (var i = 0; i < 10; i++) {
        $("table.movie-result-list tr").eq(i).hide();
        $("table.filmMaker-result-list tr").eq(i).hide();
        $("table.movieSheet-result-list tr").eq(i).hide();
    }
    $("div.turn-page").slideUp();
    if (data.length - 2 == 0) {
        $("p.tip-noComments").text("No results");
        // alert("no");
    } else {
        $("p.tip-noComments").text("");
        // $("span#movie-searchResult-num").text(data[data.length-2]);
        // $("span#movie-searchResult-total-page").text(data[data.length-1]);
        $("span#movie-searchResult-num").text("...");
        $("span#movie-searchResult-total-page").text("...");
        if (searchType == 0) {
            for (var i = 0; i < data.length - 2; i++) {
                $("table.movie-result-list tr").eq(i).slideDown();
                $("table.movie-result-list div.movie-piece .movie-pic").eq(i).attr("src", data[i].poster);
                $("table.movie-result-list div.movie-piece a").eq(i).attr("href", "/movie/j" + data[i].id);
                $("table.movie-result-list div.movie-piece .movie-name").eq(i).text(data[i].name);
                $("table.movie-result-list div.movie-piece .detail").eq(i).html('<div><strong>Director: </strong>' + data[i].director.join(' / ') + '<br>'
                    + '<strong>Writer: </strong>' + data[i].writer.join(' / ') + '<br/>'
                    + '<strong>Star: </strong>' + data[i].actor.join(' / ') + '</div>');
                $("table.movie-result-list div.movie-piece #rate-IMDB").eq(i).text(data[i].imdbRating);
                $("table.movie-result-list div.movie-piece #rate-DOUBAN").eq(i).text(data[i].doubanRating);
            }
            if (data.length - 2 == 10) {
                $("div.turn-page").slideDown();
            } else if (data.length - 2 < 10) {
                $("div.turn-page").slideDown();
                $(".next-page").hide();
            }
        } else if (searchType == 1) {
            for (var i = 0; i < data.length - 2; i++) {
                $("table.filmMaker-result-list tr").eq(i).slideDown();
                $("table.filmMaker-result-list div.movie-piece .filmMaker-pic").eq(i).attr("src", data[i].avatar);
                $("table.filmMaker-result-list div.movie-piece a").eq(i).attr("href", "/figure/j" + data[i].figureID);
                $("table.filmMaker-result-list div.movie-piece .filmMaker-name").eq(i).text(data[i].name);
                var born = data[i].born;
                var death = data[i].death;
                if (born == "") {
                    born = "unknown";
                }
                if (death == "") {
                    death = "unknown";
                }
                $("table.filmMaker-result-list div.movie-piece .detail").eq(i).html('<div><strong>Figure: </strong>' + data[i].allFigureTypes.join(' / ') + '<br><br/>'
                    + '<strong>Birth: </strong>' + born + '<br/>'
                    + '<strong>Death: </strong>' + death + '</div>');
            }
            if (data.length - 2 == 10) {
                $("div.turn-page").slideDown();
            } else if (data.length - 2 < 10) {
                $("div.turn-page").slideDown();
                $(".next-page").hide();
            }
        } else {
            for (var i = 0; i < data.length - 2; i++) {
                $("table.movieSheet-result-list tr").eq(i).slideDown();
                $("table.movieSheet-result-list div.movie-piece .movie-pic").eq(i).attr("src", data[i].poster);
                $("table.movieSheet-result-list div.movie-piece a").eq(i).attr("href", "/movielist/j" + data[i].id);
                $("table.movieSheet-result-list div.movie-piece .movieSheet-name").eq(i).text(data[i].movieSheetName);
                $("table.movieSheet-result-list div.movie-piece .create-time").eq(i).text(data[i].createDate)
                $("table.movieSheet-result-list div.movie-piece .update-time").eq(i).text(data[i].lastUpDate);
                $("table.movieSheet-result-list div.movie-piece .description").eq(i).text(data[i].sheetDescription);
            }
            if (data.length - 2 == 10) {
                $("div.turn-page").slideDown();
            } else if (data.length - 2 < 10) {
                $("div.turn-page").slideDown();
                $(".next-page").hide();
            }
        }
    }
}