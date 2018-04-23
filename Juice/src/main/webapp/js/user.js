/**
 * Created by lenovo on 2017/5/14.
 */
/********************user-homePage*************************/

$(document).ready(function () {

    $("p.three-num a").mouseenter(function () {
        index = $("p.three-num a").index(this);
        $("p.three-num-name a").eq(index).addClass("selected");
    });
    $("p.three-num a").mouseout(function () {
        index = $("p.three-num a").index(this);
        $("p.three-num-name a").eq(index).removeClass("selected");
    });
    $("p.three-num-name a").mouseenter(function () {
        index = $("p.three-num-name a").index(this);
        $("p.three-num a").eq(index).addClass("selected");
    });
    $("p.three-num-name a").mouseout(function () {
        index = $("p.three-num-name a").index(this);
        $("p.three-num a").eq(index).removeClass("selected");
    });


    //实现翻页时改变当前页数
    $("#movie-watched-last-page").click(function () {
        if (parseInt($("#movie-watched-page-index").text()) == 1) {
            return;
        }
        $("#movie-watched-page-index").text(function (i, origText) {
            return parseInt(origText) - 1;
        });
        getWatchedMoviePage();
    });
    $("#movie-watched-next-page").click(function () {
        totalPage = $("#movie-watched-total-page").text();
        if (parseInt($("#movie-watched-page-index").text()) == parseInt(totalPage)) {
            return;
        }
        $("#movie-watched-page-index").text(function (i, origText) {
            return parseInt(origText) + 1;
        });
        getWatchedMoviePage();
    });

    $("#movie-collect-last-page").click(function () {
        if (parseInt($("#movie-collect-page-index").text()) == 1) {
            return;
        }
        $("#movie-collect-page-index").text(function (i, origText) {
            return parseInt(origText) - 1;
        });
        getCollectedMoviePage();
    });
    $("#movie-collect-next-page").click(function () {
        totalPage = $("#movie-collect-total-page").text();
        if (parseInt($("#movie-collect-page-index").text()) == parseInt(totalPage)) {
            return;
        }
        $("#movie-collect-page-index").text(function (i, origText) {
            return parseInt(origText) + 1;
        });
        getCollectedMoviePage();
    });

    function getWatchedMoviePage() {
        var curPageIndex = parseInt($("#movie-watched-page-index").text()) - 1;
        for (i = 0; i < 4; i++) {
            $("#movie-watched .watched-movie-table td").eq(i).fadeOut();
        }
        $.get("/user/getWatchedMoviePage?userID=" + userID + "&pageIndex=" + curPageIndex, function (data) {
            for (var index = 0; index < data.length; index++) {
                $("#movie-watched .watched-movie-table td").eq(index).find("a").attr("href", "/movie/j" + data[index].id);
                $("#movie-watched .watched-movie-table td").eq(index).find("img").attr("src", data[index].poster);
                $("#movie-watched .watched-movie-table td").eq(index).find("p").text(data[index].name);
                $("#movie-watched .watched-movie-table td").eq(index).fadeIn();
            }
        });
    }

    function getCollectedMoviePage() {
        var curPageIndex = parseInt($("#movie-collect-page-index").text()) - 1;
        for (i = 0; i < 4; i++) {
            $("#movie-collect .collected-movie-table td").eq(i).fadeOut();
        }
        $.get("/user/getPreferMoviePage?userID=" + userID + "&pageIndex=" + curPageIndex, function (data) {
            for (var index = 0; index < data.length; index++) {
                $("#movie-collect .collected-movie-table td").eq(index).find("a").attr("href", "/movie/j" + data[index].id);
                $("#movie-collect .collected-movie-table td").eq(index).find("img").attr("src", data[index].poster);
                $("#movie-collect .collected-movie-table td").eq(index).find("p").text(data[index].name);
                $("#movie-collect .collected-movie-table td").eq(index).fadeIn();
            }
        });
    }

    /****************user-movielists******************/
    //影单类别
    $("#create-collect-choose label").click(function () {
        if ($(this).hasClass("selected")) {
            return;
        }
        index = $("#create-collect-choose label").index(this);
        for (i = 0; i < 2; i++) {
            if (index == i) {
                continue;
            }
            if ($("#create-collect-choose label").eq(i).hasClass("selected")) {
                $("#create-collect-choose label").eq(i).removeClass("selected");
            }
        }
        $(this).addClass("selected");
        if ($(this).text().indexOf("created") == 0) {
            $("#created-movielist-div").show();
            $("#collected-movielist-div").hide();
        } else {
            $("#created-movielist-div").hide();
            $("#collected-movielist-div").show();
        }
    });

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
    $("#collected-movielist-div .collect-div img").click(function () {
        var movielistIndex = indexOfAll(this, $("#collected-movielist-div .collect-div .collect-pic"));
        var collectPic = $(this);
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.")
                return;
            }
            $.get("/user/getCollectedMovielistIDFromIndex?movielistIndex=" + movielistIndex
                + "&userID=" + userID, function (data) {
                var movieSheetID = data;
                if (collectPic.hasClass("selected")) {
                    $.get("/movielist/removeCollectThisMovielist?movieSheetID=" + movieSheetID, function (data) {
                        $.get("/user/isOwner?userID=" + userID, function (isOwner) {
                            if (isOwner == false) {
                                if (data == "SUCCESS") {
                                    collectPic.attr("src", "/images/uncollected.png");
                                    collectPic.removeClass("selected");
                                    collectPic.next(".collect-num").text(function (i, origText) {
                                        return parseInt(origText) - 1;
                                    });
                                } else {
                                    alert(data);
                                }
                            } else {
                                if (data == "SUCCESS") {
                                    window.location.href = "/user/j" + userID + "/movielist";
                                } else {
                                    alert(data);
                                }
                            }
                        });
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
        // if($(this).hasClass("selected")){
        //     var collectPic=$(this);
        //     $.get("")
        //     $(this).attr("src","/images/uncollected.png");
        //     $(this).removeClass("selected");
        //     $(this).next(".collect-num").text(function (i, origText) {
        //         return parseInt(origText)-1;
        //     });
        // }
        // else{
        //     $(this).attr("src","/images/collected.png");
        //     $(this).addClass("selected");
        //     $(this).next(".collect-num").text(function (i, origText) {
        //         return parseInt(origText)+1;
        //     });
        // }
    });

    $("#created-movielist-div .collect-div img").click(function () {
        var movielistIndex = indexOfAll(this, $("#created-movielist-div .collect-div .collect-pic"));
        var collectPic = $(this);
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.")
                return;
            }
            $.get("/user/getCreatedMovielistIDFromIndex?movielistIndex=" + movielistIndex
                + "&userID=" + userID, function (data) {
                var movieSheetID = data;
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

    $("a.add-movielist-div").click(function () {
        createFunc();
    });

    function createFunc() {
        $("#background-shade").fadeIn();
        $("#create-movielist").show();
    };

    $("#create-movielist .close-button").click(function () {
        $("#background-shade").fadeOut();
        $("#create-movielist").hide();
    });

    $("#background-shade").click(function () {
        $("#create-movielist .close-button").click();
        $("#delete-movielist #cancel-button").click();
    });

    $("#create-movielist .create").click(function () {
        if ($("input[name='title']").val() == "" || $("input[name='title']").val() == null) {
            $("#tip-title").show();
        } else if ($(".description-textarea").val() == "" || $(".description-textarea").val() == null) {
            $("#tip-discription").show();
        } else {
            $.get("/user/createMovielist?userID=" + userID + "&title=" + $("input[name='title']").val()
                + "&description=" + $(".description-textarea").val(), function (data) {
                if (data == "SUCCESS") {
                    location.href = "/user/j" + userID + "/movielist";
                } else {
                    alert(data);
                }
            });
        }
    });

    $("input[name='title']").on('input propertychange', function () {
        $("#tip-title").hide();
    });
    $(".description-textarea").on('input propertychange', function () {
        $("#tip-discription").hide();
    });

    var deleteMovielistIndex = -1;

    $("#created-movielist-div a.del-movielist-div").click(function () {
        deleteMovielistIndex = indexOfAll(this, $("#created-movielist-div a.del-movielist-div"));
        $("#background-shade").fadeIn();
        $("#delete-movielist").show();
    });

    function indexOfAll(current, all) {
        for (var i = 0; i < all.length; i++) {
            if (all[i] == current) {
                return i;
            }
        }
    }

    $("#delete-movielist #cancel-button").click(function () {
        $("#background-shade").fadeOut();
        $("#delete-movielist").hide();
        deleteMovielistIndex = -1;
    });

    $("#delete-movielist #yes-button").click(function () {
        if (deleteMovielistIndex != -1) {
            $.get("/user/deleteMovielist?userID=" + userID + "&createdMovielistIndex=" + deleteMovielistIndex, function (data) {
                if (data == "SUCCESS") {
                    window.location.href = "/user/j" + userID + "/movielist";
                } else {
                    alert(data);
                }
            });
        }
    });

    /****************user-reviews**********************/
//评论类别
    $("#sort-div span").click(function () {
        //css样式
        if ($(this).hasClass("selected")) {
            return;
        }
        index = $("#sort-div span").index(this);
        for (i = 0; i < 2; i++) {
            if (index == i) {
                continue;
            }
            if ($("#sort-div span").eq(i).hasClass("selected")) {
                $("#sort-div span").eq(i).removeClass("selected");
            }
        }
        $(this).addClass("selected");
        //获取数据
        $(".scott .reviews-page-index").eq(0).click();
    });

    function getReviewPage(pageIndex, sortStrategy) {
        for (index = curPageSize - 1; index >= 0; index--) {
            $("div#reviews table.reviews tr").eq(index).slideUp();
        }
        $(".scott").slideUp();
        $.get("/user/getReviewPage?userID=" + userID + "&pageIndex=" + pageIndex + "&sortStrategy=" + sortStrategy, function (json) {
            $.each(json, function (index, reviewJson) {
                $("div#reviews .movie-pic img").eq(index).attr("src", reviewJson.movieImage);
                $("div#reviews .movie-pic a").eq(index).attr("href", "/movie/j" + reviewJson.movieID);
                $("div#reviews .review-title").eq(index).text(reviewJson.reviewTitle);
                $("div#reviews .user-name").eq(index).text(reviewJson.userName);
                $("div#reviews .user-name").eq(index).attr("href", "/user/j" + reviewJson.userID);
                $("div#reviews .movie-name").eq(index).text(reviewJson.movieName);
                $("div#reviews .movie-name").eq(index).attr("href", "/movie/j" + reviewJson.movieID);
                $("div#reviews .review-time").eq(index).text(reviewJson.date);
                $("div#reviews .rate-pic").eq(index).attr("src", reviewJson.ratePic);
                $("div#reviews .review-content").eq(index).text(reviewJson.reviewContent);
                $("div#reviews table.reviews tr").slideDown();
            });
            if (totalPageSize != 1) {
                $(".scott").slideDown();
            }
            //如果是最后一页且length小于12则隐藏多余的review-piece
            // if(totalPageNum==pageIndex+1 && totalPageNum>1){
            //     var nextPageSize=json.length;
            //     for(index=nextPageSize;index<curPageSize-1;index++){
            //         $("div#reviews table.reviews tr").hide();
            //     }
            // }else{
            //     for(index=0;index<curPageSize;index++){
            //         $("div#reviews table.reviews tr").show();
            //     }
            // }
        });

    }

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
        for (var i = 0; i < totalPageNum; i++) {
            if ($(".scott .reviews-page-index").eq(i).hasClass("current")) {
                if (i == pageIndex) {
                    continue;
                } else {
                    $(".scott .reviews-page-index").eq(i).removeClass("current");
                }
            }
        }
        $(this).addClass("current");
        if (pageIndex == totalPageNum - 1) {
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
        if ($("#sort-div span").eq(0).hasClass("selected")) {
            getReviewPage(pageIndex, "BY_NEWEST");
        } else {
            getReviewPage(pageIndex, "BY_HEAT");
        }
    });

    //点赞事件
    // $(".like-pic").mouseenter(function () {
    //     $(this).attr({
    //         "src": "/images/like-hover.png",
    //         "opacity": "1"
    //     });
    // });
    // $(".like-pic").mouseout(function () {
    //     index = $(".like-pic").index(this);
    //     name = $(".like-num").eq(index).attr("name");
    //     if (name.indexOf("unclicked") != -1) {
    //         $(this).attr({
    //             "src": "/images/like.png",
    //             "opacity": "1"
    //         });
    //     } else {
    //         $(this).attr({
    //             "src": "/images/like-click.png",
    //             "opacity": "1"
    //         });
    //     }
    // });
    $("#reviews .like-pic").click(function () {
        var likePic = $(this);
        var reviewIndex = indexOfAll(this, $("#reviews .like-pic"));
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.")
                return;
            }
            var sortStrategy = "";
            if ($("#sort-div span").eq(0).hasClass("selected")) {
                sortStrategy = "BY_NEWEST";
            } else {
                sortStrategy = "BY_HEAT";
            }
            var pageIndex = -1;
            for (var index = 0; index < $(".scott .reviews-page-index").length; index++) {
                if ($(".scott .reviews-page-index").eq(index).hasClass("current")) {
                    pageIndex = index;
                    break;
                }
            }
            if (likePic.hasClass("selected")) {
                $.get("/user/getReviewID?userID="+userID+"&reviewIndex="+reviewIndex
                    + "&pageIndex=" + pageIndex + "&sortStrategy=" + sortStrategy, function (reviewID) {
                    var reviewID=reviewID;
                    $.get("/user/removeThumbThisMovie?reviewID="+reviewID,function (data) {
                        if (data == "SUCCESS") {
                            likePic.removeClass("selected").attr("src","/images/like.png");
                            likePic.next(".like-num").text(function (i, origText) {
                                return parseInt(origText) - 1;
                            });
                        } else {
                            alert(data);
                        }
                    });
                });
            } else {
                $.get("/user/getReviewID?userID="+userID+"&reviewIndex=" + reviewIndex
                    + "&pageIndex=" + pageIndex + "&sortStrategy=" + sortStrategy, function (reviewId) {
                    var reviewID=reviewId;
                    $.get("/user/thumbThisMovie?reviewID="+reviewID,function (data) {
                        if (data == "SUCCESS") {
                            likePic.addClass("selected").attr("src","/images/like-click.png");
                            likePic.next(".like-num").text(function (i, origText) {
                                return parseInt(origText) + 1;
                            });
                        } else {
                            alert(data);
                        }
                    });

                });
            }
        });
    });

    //首页的点赞
    $(".show-reviews .like-pic").click(function () {
        var likePic = $(this);
        var reviewIndex = indexOfAll(this, $(".show-reviews .like-pic"));
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.")
                return;
            }
            if (likePic.hasClass("selected")) {
                $.get("/user/getShowReviewID?userID="+userID+"&reviewIndex="+reviewIndex, function (reviewID) {
                    var reviewID=reviewID;
                    $.get("/user/removeThumbThisMovie?reviewID="+reviewID,function (data) {
                        if (data == "SUCCESS") {
                            likePic.removeClass("selected").attr("src","/images/like.png");
                            likePic.next(".like-num").text(function (i, origText) {
                                return parseInt(origText) - 1;
                            });
                        } else {
                            alert(data);
                        }
                    });
                });
            } else {
                $.get("/user/getShowReviewID?userID="+userID+"&reviewIndex="+reviewIndex, function (reviewID) {
                    var reviewID=reviewID;
                    $.get("/user/thumbThisMovie?reviewID="+reviewID,function (data) {
                        if (data == "SUCCESS") {
                            likePic.addClass("selected").attr("src","/images/like-click.png");
                            likePic.next(".like-num").text(function (i, origText) {
                                return parseInt(origText) + 1;
                            });
                        } else {
                            alert(data);
                        }
                    });

                });
            }
        });
    });
    /*$("div.scott span").click(function(){
     var curIndex=parseInt($("div.scott .current").text())-1;
     if($(this).attr("id").indexOf("last-page")==0){
     if(!$(this).hasClass("disabled")) {
     if (curIndex != 0) {
     showPageIndexButton(curIndex - 1);
     }
     }
     }else if($(this).attr("id").indexOf("next-page")==0){
     if(!$(this).hasClass("disabled")) {
     if (curIndex != totalPageNum - 1) {
     showPageIndexButton(curIndex + 1);
     }
     }
     }else{
     showPageIndexButton(parseInt($(this).text())-1);
     }
     // var desIndex=parseInt($("div.scott .current").text())-1;
     // if($("#sort-div span.selected label").text().indexOf("sort by newest")==0){
     //     getReviewPage(desIndex, "BY_NEWEST");
     // }else{
     //     getReviewPage(desIndex,"BY_HEAT");
     // }
     });*/
    /*$(".scott span").click(function(){
     var curIndex=parseInt($(".scott .current").text())-1;
     <%int index=0;%>
     if($(this).attr("id").indexOf("last-page")==0){
     if(!$(this).hasClass("disabled")) {
     if (curIndex != 0) {
     showPageIndexButton(curIndex - 1);
     }
     }
     }else if($(this).attr("id").indexOf("next-page")==0){
     if(!$(this).hasClass("disabled")) {
     if (curIndex != totalPageNum - 1) {
     showPageIndexButton(curIndex + 1);
     }
     }
     }else{
     showPageIndexButton(parseInt($(this).text())-1);
     }
     // var desIndex=parseInt($("div.scott .current").text())-1;
     // if($("#sort-div span.selected label").text().indexOf("sort by newest")==0){
     //     getReviewPage(desIndex, "BY_NEWEST");
     // }else{
     //     getReviewPage(desIndex,"BY_HEAT");
     // }
     });*/
})
;