/**
 * Created by sparkler on 2017/6/11.
 */

var indexOfRankType = 0;
$(document).ready(function () {
    //rank类别
    $(".choose-type button").mouseenter(function () {
        $(this).css({
            "border": "2px solid cornflowerblue"
        });
    });

    $(".choose-type button").mouseout(function () {
        if ($(this).attr("name") == "selected") {
            $(this).css({
                "color": "white",
                "background-color": "cornflowerblue"
            });
        } else {
            $(this).css({
                "color": "gray",
                "border-color": "transparent",
                "background-color": "powderblue"
            });
        }
    });


    $(".rankType-but").click(function () {
        if ($(this).attr("name").indexOf("selected") == 0) {
            return;
        }
        indexOfRankType = $(this).attr("id");
        // alert($(this).attr("id"));
        $(".scott .reviews-page-index").eq(0).click();
        $(".rankType-but").css({
            "color": "gray",
            "border-color": "transparent",
            "background-color": "powderblue"
        });
        $(".rankType-but").attr("name", "unselected");
        $(this).css({
            "color": "white",
            "background-color": "cornflowerblue"
        });
        $(this).attr("name", "selected");

    });


    /*
     scott翻页
     */

    $(".scott #last-page").click(function () {
        if ($(this).hasClass("disabled")) {
            return;
        }
        var curPageIndex = -1;
        for (var index = 0; index < $(".scott .reviews-page-index").length; index++) {
            if ($(".scott .reviews-page-index").eq(index).hasClass("current")) {
                curPageIndex = index;
                break;
            }
        }
        $(".scott .reviews-page-index").eq(curPageIndex - 1).click();
    });

    $(".scott #next-page").click(function () {
        if ($(this).hasClass("disabled")) {
            return;
        }
        var curPageIndex = -1;
        for (var index = 0; index < $(".scott .reviews-page-index").length; index++) {
            if ($(".scott .reviews-page-index").eq(index).hasClass("current")) {
                curPageIndex = index;
                break;
            }
        }
        $(".scott .reviews-page-index").eq(curPageIndex + 1).click();
    });

    function indexOfAll(current, all) {
        for (var i = 0; i < all.length; i++) {
            if (all[i] == current) {
                return i;
            }
        }
        return -1;
    }

    $(".scott .reviews-page-index").click(function () {
        var pageIndex = indexOfAll(this, $(".scott .reviews-page-index"));
        for (var i = 0; i < 10; i++) {
            if ($(".scott .reviews-page-index").eq(i).hasClass("current")) {
                if (i == pageIndex) {
                    continue;
                } else {
                    $(".scott .reviews-page-index").eq(i).removeClass("current");
                }
            }
        }
        $(this).addClass("current");
        if (pageIndex == 10 - 1) {
            $(".scott #next-page").addClass("disabled");
        } else {
            if ($(".scott #next-page").hasClass("disabled")) {
                $(".scott #next-page").removeClass("disabled");
            }
        }
        if (pageIndex == 0) {
            $(".scott #last-page").addClass("disabled");
        } else {
            if ($(".scott #last-page").hasClass("disabled")) {
                $(".scott #last-page").removeClass("disabled");
            }
        }
        getRankListPage(indexOfRankType, pageIndex);
    });

});

function getRankListPage(rankType, curPageIndex) {
    $.get("/getRankListPage?rankType=" + rankType + "&curPageIndex=" + curPageIndex, function (data) {
        loadRankList(rankType, data);
    });

}
function loadRankList(rankType, data) {
    $("table.rank-movies").hide();
    $("table.rank-filmMaker").hide();
    $("table.rank-boxOffice").hide();
    $("div.scott").slideUp();
    if (rankType == 0) {
        $("h1.rank-title").text("IMDb top 100 list");
    } else if (rankType == 1) {
        $("h1.rank-title").text("DOUBAN top 100 list");
    } else if (rankType == 2) {
        $("h1.rank-title").text("Rate difference list");
    } else if (rankType == 3) {
        $("h1.rank-title").text("Best selling film maker list");
    } else if (rankType == 4) {
        $("h1.rank-title").text("Top rate film maker list");
    } else if (rankType == 5) {
        $("h1.rank-title").text("Rate/boxOffice list");
    }
    if (data.length == 0) {
        $("p.tip-noComments").text("No ranks");
        alert("No ranks");
    } else {
        $("p.tip-noComments").text("");
        if (rankType <= 2 || rankType == 5) {
            $("table.rank-movies").slideDown();
            for (var i = 0; i < data.length; i++) {
                $("table.rank-movies div.movie-piece .movie-pic").eq(i).attr("src", data[i].poster);
                $("table.rank-movies div.movie-piece a").eq(i).attr("href", "/movie/j" + data[i].id);
                $("table.rank-movies div.movie-piece .movie-name").eq(i).text(data[i].name);
                $("table.rank-movies div.movie-piece .detail").eq(i).html('<div><strong>Director: </strong>' + data[i].director.join(' / ') + '<br>'
                    + '<strong>Writer: </strong>' + data[i].writer.join(' / ') + '<br/>'
                    + '<strong>Star: </strong>' + data[i].actor.join(' / ') + '</div>');
                $("table.rank-movies div.movie-piece #rate-IMDB").eq(i).text(data[i].imdbRating);
                $("table.rank-movies div.movie-piece #rate-DOUBAN").eq(i).text(data[i].doubanRating);
            }
            $("div.scott").slideDown();
        } else if (rankType <= 4 && rankType >= 3) {
            $("table.rank-filmMaker").slideDown();
            $("table.rank-movies").hide();
            $("table.rank-boxOffice").hide();
            for (var i = 0; i < data.length; i++) {
                $("table.rank-filmMaker div.movie-piece .movie-pic").eq(i).attr("src", data[i].avatar);
                $("table.rank-filmMaker div.movie-piece a").eq(i).attr("href", "/figure/j" + data[i].id);
                $("table.rank-filmMaker div.movie-piece .movie-name").eq(i).text(data[i].name);
                $("table.rank-filmMaker div.movie-piece .detail").eq(i).html('<div><strong>Figure: </strong>' + data[i].roleHasPlayed.join(' / ') + '<br></div>');
                $("table.rank-filmMaker div.movie-piece .average-rate").eq(i).text(data[i].data);
                if (rankType == 3) {
                    $("span.rate-or-boxOffice").eq(i).text("Box office:");
                }
            }
            $("div.scott").slideDown();
        }

    }
}
