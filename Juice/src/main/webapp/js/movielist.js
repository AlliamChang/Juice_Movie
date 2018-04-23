/**
 * Created by lenovo on 2017/5/29.
 */
$(document).ready(function () {
    // getMovieListRankPage(0, "BY_HEAT");

//收藏按钮的响应
    $(".collect-div img").mouseenter(function () {
        $(this).attr("src", "/images/collect-hover.png");
    });
    $(".collect-div img").mouseout(function () {
        if ($(this).hasClass("selected")) {
            $(this).attr("src", "/images/collected.png");
        } else {
            $(this).attr("src", "/images/uncollected.png");
        }
    });
    $(".rank-movielist-piece .collect-div img").click(function () {
        var movielistIndex = indexOfAll(this, $(".rank-movielist-piece .collect-div .collect-pic"));
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.")
                return;
            }
            var movieSheetID = -1;
            var sortStrategy = "";
            if ($("#heat-new-choose span").eq(0).hasClass("selected")) {
                sortStrategy = "BY_HEAT";
            } else {
                sortStrategy = "BY_NEWEST";
            }
            var pageIndex = -1;
            for (var index = 0; index < $(".scott .movielist-page-index").length; index++) {
                if ($(".scott .movielist-page-index").eq(index).hasClass("current")) {
                    pageIndex = index;
                    break;
                }
            }
            $.get("/movielist/getMovieIDFromIndex?movielistIndex=" + movielistIndex
                + "&pageIndex=" + pageIndex + "&sortStrategy=" + sortStrategy, function (data) {
                movieSheetID = data;
                var collectPic = $(".rank-movielist-piece .collect-div img").eq(movielistIndex);
                if (collectPic.hasClass("selected")) {
                    $.get("/movielist/removeCollectThisMovielist?movieSheetID=" + movieSheetID, function (data) {
                        if (data == "SUCCESS") {
                            collectPic.attr("src", "/images/uncollected.png");
                            collectPic.removeClass("selected");
                            collectPic.next(".collect-num").text(function (i, origText) {
                                return parseInt(origText) - 1;
                            });
                        } else {
                            alert(data);
                        }
                    });
                } else {
                    $.get("/movielist/collectThisMovielist?movieSheetID=" + movieSheetID, function (data) {
                        if (data == "SUCCESS") {
                            collectPic.attr("src", "/images/collected.png");
                            collectPic.addClass("selected");
                            collectPic.next(".collect-num").text(function (i, origText) {
                                return parseInt(origText) + 1;
                            });
                        } else {
                            alert(data);
                        }
                    });
                }
            });
        });
    });

    $(".recommand-movielist-piece .collect-div img").click(function () {
        var movielistIndex = indexOfAll(this, $(".recommand-movielist-piece .collect-div .collect-pic"));
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.")
                return;
            }
            $.get("/movielist/getRecommandMovieIDFromIndex?movielistIndex=" + movielistIndex, function (data) {
                var movieSheetID = data;
                var collectPic = $(".recommand-movielist-piece .collect-div img").eq(movielistIndex);
                if (collectPic.hasClass("selected")) {
                    $.get("/movielist/removeCollectThisMovielist?movieSheetID=" + movieSheetID, function (data) {
                        if (data == "SUCCESS") {
                            collectPic.attr("src", "/images/uncollected.png");
                            collectPic.removeClass("selected");
                            collectPic.next(".collect-num").text(function (i, origText) {
                                return parseInt(origText) - 1;
                            });
                        } else {
                            alert(data);
                        }
                    });
                } else {
                    $.get("/movielist/collectThisMovielist?movieSheetID=" + movieSheetID, function (data) {
                        if (data == "SUCCESS") {
                            collectPic.attr("src", "/images/collected.png");
                            collectPic.addClass("selected");
                            collectPic.next(".collect-num").text(function (i, origText) {
                                return parseInt(origText) + 1;
                            });
                        } else {
                            alert(data);
                        }
                    });
                }
            });
        });
    });

//影单排序类别切换
    $("#heat-new-choose span").click(function () {
        if ($(this).hasClass("selected")) {
            return;
        }
        index = $("#heat-new-choose span").index(this);
        for (i = 0; i < 2; i++) {
            if (index == i) {
                continue;
            }
            if ($("#heat-new-choose span").eq(i).hasClass("selected")) {
                $("#heat-new-choose span").eq(i).removeClass("selected");
            }
        }
        $(this).addClass("selected");
        $(".scott .movielist-page-index").eq(0).click();
    });

    function getMovieListRankPage(pageIndex, sortStrategy) {
        var pageSize = $(".rank-movielist-piece").length;
        for (var i = 0; i < pageSize; i++) {
            $(".rank-movielist-piece").eq(i).slideUp();
        }
        $(".scott").slideUp();
        $.get("/movielist/getMovieListRankPage?pageIndex=" + pageIndex + "&sortStrategy=" + sortStrategy, function (data) {
            $.each(data, function (index, sheetJson) {
                $("#movielist-rank .rank-movielist-piece .movielist-name").eq(index).attr("href", "/movielist/j" + sheetJson.movieSheetID);
                $("#movielist-rank .rank-movielist-piece .movielist-name").eq(index).text(sheetJson.movieSheetName);
                $("#movielist-rank .rank-movielist-piece .movieNum").eq(index).text(sheetJson.movieNum);
                $("#movielist-rank .rank-movielist-piece .user-name").eq(index).attr("href", "user/j" + sheetJson.userID + "/home");
                $("#movielist-rank .rank-movielist-piece .user-name").eq(index).text(sheetJson.userName);
                $("#movielist-rank .rank-movielist-piece .userImage").eq(index).attr("src", sheetJson.userImage);
                $("#movielist-rank .rank-movielist-piece .create-time").eq(index).text(sheetJson.createTime);
                $("#movielist-rank .rank-movielist-piece .update-time").eq(index).text(sheetJson.updateTime);
                $("#movielist-rank .rank-movielist-piece .sheetDescription").eq(index).text(sheetJson.sheetDescription);
                // if(sheetJson.isOwner){
                //     $("#movielist-rank .rank-movielist-piece .is-Owner").eq(index).show();
                //     $("#movielist-rank .rank-movielist-piece .not-Owner").eq(index).hide();
                // }else{
                //     $("#movielist-rank .rank-movielist-piece .is-Owner").eq(index).hide();
                //     $("#movielist-rank .rank-movielist-piece .not-Owner").eq(index).show();
                if (sheetJson.isCollected) {
                    $("#movielist-rank .rank-movielist-piece .collect-div").eq(index)
                        .find(".collect-pic").addClass("selected").attr("src", "/images/collected.png");
                } else {
                    var collectPic = $("#movielist-rank .rank-movielist-piece .collect-div").eq(index).find(".collect-pic");
                    collectPic.attr("src", "/images/uncollected.png");
                    if (collectPic.hasClass("selected")) {
                        collectPic.removeClass("selected");
                    }
                }
                $("#movielist-rank .rank-movielist-piece .collect-num").eq(index).text(sheetJson.collectorNum);
                // }
                $("#movielist-rank .rank-movielist-piece").eq(index).slideDown();
            })
            if (totalPageSize != 1) {
                $(".scott").slideDown();
            }
        });
    }

//翻页
    $(".scott #last-page").click(function () {
        if ($(this).hasClass("disabled")) {
            return;
        }
        var curPageIndex = -1;
        for (var index = 0; index < $(".scott .movielist-page-index").length; index++) {
            if ($(".scott .movielist-page-index").eq(index).hasClass("current")) {
                curPageIndex = index;
                break;
            }
        }
        $(".scott .movielist-page-index").eq(curPageIndex - 1).click();
    });

    $(".scott #next-page").click(function () {
        if ($(this).hasClass("disabled")) {
            return;
        }
        var curPageIndex = -1;
        for (var index = 0; index < $(".scott .movielist-page-index").length; index++) {
            if ($(".scott .movielist-page-index").eq(index).hasClass("current")) {
                curPageIndex = index;
                break;
            }
        }
        $(".scott .movielist-page-index").eq(curPageIndex + 1).click();
    });

    function indexOfAll(current, all) {
        for (var i = 0; i < all.length; i++) {
            if (all[i] == current) {
                return i;
            }
        }
        return -1;
    }

    $(".scott .movielist-page-index").click(function () {
        var pageIndex = indexOfAll(this, $(".scott .movielist-page-index"));
        for (var i = 0; i < totalPageSize; i++) {
            if ($(".scott .movielist-page-index").eq(i).hasClass("current")) {
                if (i == pageIndex) {
                    continue;
                } else {
                    $(".scott .movielist-page-index").eq(i).removeClass("current");
                }
            }
        }
        $(this).addClass("current");
        if (pageIndex == totalPageSize - 1) {
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
        if ($("#heat-new-choose span").eq(0).hasClass("selected")) {
            getMovieListRankPage(pageIndex, "BY_HEAT");
        } else {
            getMovieListRankPage(pageIndex, "BY_NEWEST");
        }
    });


});