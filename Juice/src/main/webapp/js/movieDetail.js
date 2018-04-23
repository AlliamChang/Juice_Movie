/**
 * Created by sparkler on 2017/5/17.
 */

$(document).ready(function () {
    // document.getElementById('testTEST').innerHTML = "啊哈哈哈哈哈哈哈哈哈哈";
    // $("h2#testTEST").text("wocwocwoc");

    // alert("have");

    //点赞事件
    // $(".like-pic").mouseenter(function () {
    //     $(this).attr({
    //         "src": "../images/like-hover.png",
    //         "opacity": "1"
    //     });
    // });
    // $(".like-pic").mouseout(function () {
    //     index = $(".like-pic").index(this);
    //     name = $(".like-num").eq(index).attr("name");
    //     if (name.indexOf("unclicked") != -1) {
    //         $(this).attr({
    //             "src": "../images/like.png",
    //             "opacity": "1"
    //         });
    //     } else {
    //         $(this).attr({
    //             "src": "../images/like-click.png",
    //             "opacity": "1"
    //         });
    //     }
    // });
    movieID=-1;
    // $(".like-pic").click(function () {
    //
    //     if($(this).hasClass("selected")){
    //
    //     }else{
    //
    //     }
    //     // $(this).attr({
    //     //     "src": "/images/like-click.png",
    //     // });
    //     // index = $(".like-pic").indexOf(this);
    //     // alert(index);
    //     // $(".like-num").eq(index).text(function (i, origText) {
    //     //     name = $(this).attr("name");
    //     //     if (name.indexOf("unclicked") != -1) {
    //     //         $(this).attr("name", "clicked");
    //     //         return parseInt(origText) + 1;
    //     //     } else {
    //     //         $(this).attr("name", "unclicked");
    //     //         $(".like-pic").eq(index).attr("src", "../images/like.png");
    //     //         return parseInt(origText) - 1;
    //     //     }
    //     // });
    // });
    function indexOfAll(current, all) {
        for (var i = 0; i < all.length; i++) {
            if (all[i] == current) {
                return i;
            }
        }
        return -1;
    }
    $(" .like-pic").click(function () {
        var likePic = $(this);
        var reviewIndex = indexOfAll(this, $(".like-pic"));
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.")
                return;
            }
            var userType = "";
            if ($(".reviews-type-but").eq(0).hasClass("selected")) {
                userType = "DEFAULT";
            } else if($(".reviews-type-but").eq(1).hasClass("selected")){
                userType = "IMDB";
            }else if($(".reviews-type-but").eq(2).hasClass("selected")){
                userType = "DOUBAN";
            }else{
                userType = "SELF";
            }
            var pageIndex = parseInt($("#reviews-page-index").text())-1;
            if (likePic.hasClass("selected")) {
                $.get("/movie/getReviewID?movieID="+movieID+"&reviewIndex="+reviewIndex
                    + "&pageIndex=" + pageIndex + "&userType=" + userType, function (reviewID) {
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
                $.get("/movie/getReviewID?movieID="+movieID+"&reviewIndex="+reviewIndex
                    + "&pageIndex=" + pageIndex + "&userType=" + userType, function (reviewID) {
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

    //选择按钮
    $("#choose-type button").mouseenter(function () {
        $(this).css({
            "border": "2px solid skyblue",
            "color": "gray",
            "background-color": "transparent"
        });
    });

    $("#choose-type button").mouseout(function () {
        $(this).css({
            "border-color": "transparent",
            "color": "gray",
            "background-color": "transparent"
        });
        index = $("#choose-type button").index(this);
        name = $("#choose-type button").eq(index).attr("name");
        if (name.indexOf("unselected") != -1) {
            $(this).css({
                "border-color": "transparent",
                "color": "gray",
                "background-color": "transparent"
            });
        } else {
            $(this).css({
                "color": "white",
                "background-color": "skyblue"
            });
        }
    });

    //评论类别
    $("#reviews-type button").mouseenter(function () {
        $(this).css({
            "border": "2px solid skyblue"
        });
    });

    $("#reviews-type button").mouseout(function () {
        $(this).css({
            "border-color": "transparent"
        });
    });


    //实现翻页时改变当前页数
    $("#movie-relativePeople-last-page").click(function () {
        $("#movie-relativePeople-page-index").text(function (i, origText) {
            if (parseInt(origText) == 1) {
                return origText;
            }
            return parseInt(origText) - 1;
        });
        var curPageIndex = parseInt($("#relativePeople #movie-relativePeople-page-index").text()) - 1;
        getRelativePeoplePage(curPageIndex);
    });
    $("#movie-relativePeople-next-page").click(function () {
        $("#movie-relativePeople-page-index").text(function (i, origText) {
            totalPage = $("#movie-relativePeople-total-page").text();
            if (parseInt(origText) == parseInt(totalPage)) {
                return origText;
            }
            return parseInt(origText) + 1;
        });
        var curPageIndex = parseInt($("#relativePeople #movie-relativePeople-page-index").text()) - 1;
        getRelativePeoplePage(curPageIndex);
    });


    //喜欢or不喜欢
    //like
    $(".movie-like").mouseenter(function () {
        $(this).attr({
            "src": "../images/like-hover.png",
            "opacity": "1"
        });
    });
    $(".movie-like").mouseout(function () {
        var name = $(".movie-like").attr("name");
        if (name.indexOf("unselected") != -1) {
            $(this).attr({
                "src": "../images/like.png",
                "opacity": "1"
            });
        } else {
            $(this).attr({
                "src": "../images/like-click.png",
                "opacity": "1"
            });
        }
    });
    $(".movie-like").click(function () {
        $(this).attr({
            "src": "../images/like-click.png",
        });
        var ifLike = 0;//0未操作,1喜欢,2不喜欢
        var name = $(this).attr("name");
        if (name.indexOf("unselected") != -1) {
            $(this).attr("name", "selected");
            // $(".movie-dislike").attr("aria-disabled", true);
            ifLike = 1;
            $(".movie-dislike").attr("src", "../images/dislike.png");
            $(".movie-dislike").attr("name", "unselected");
        } else {
            $(this).attr("name", "unselected");
            $(".movie-like").attr("src", "../images/like.png");
            ifLike = 0;
        }
        likeOrDislike(ifLike);
    });
    //dislike
    $(".movie-dislike").mouseenter(function () {
        $(this).attr({
            "src": "../images/dislike-hover.png",
            "opacity": "1"
        });
    });
    $(".movie-dislike").mouseout(function () {
        var name = $(".movie-dislike").attr("name");
        if (name.indexOf("unselected") != -1) {
            $(this).attr({
                "src": "../images/dislike.png",
                "opacity": "1"
            });
        } else {
            $(this).attr({
                "src": "../images/dislike-click.png",
                "opacity": "1"
            });
        }
    });
    $(".movie-dislike").click(function () {
        $(this).attr({
            "src": "../images/dislike-click.png",
        });
        var ifLike = 0;//0未操作,1喜欢,2不喜欢
        var name = $(this).attr("name");
        if (name.indexOf("unselected") != -1) {
            $(this).attr("name", "selected");
            // $(".movie-like").attr("aria-disabled", true);
            ifLike = 2;
            $(".movie-like").attr("src", "../images/like.png");
            $(".movie-like").attr("name", "unselected");
        } else {
            $(this).attr("name", "unselected");
            $(".movie-dislike").attr("src", "../images/dislike.png");
            ifLike = 0;
        }
        likeOrDislike(ifLike);
    });


    //是否已看过
    $("div#choose-type #have-watched").click(function () {
        $(this).css({
            "color": "white",
            "background-color": "skyblue"
        });
        var ifWatched = 0;//0没看过（未点亮），1已看过（点亮）
        var name = $("#have-watched").attr("name");
        if (name.indexOf("unselected") != -1) {
            $(this).attr("name", "selected");
            ifWatched = 1;
        } else {
            $(this).attr("name", "unselected");
            ifWatched = 0;
            $(this).css({
                "border-color": "transparent",
                "color": "gray",
                "background-color": "transparent"
            });
        }
        haveWatched(ifWatched);
    });


    //添加收藏
    $("div#choose-type #add").click(function () {

        addToCollection();
    });


    //写评论
    $("button#write-comment").click(function () {
        writeComment();
    });
    function writeComment() {
        $("#background-shade").fadeIn();
        $("#write-commentW").show();
    };

    $("#write-commentW .close-button").click(function () {
        $("#background-shade").fadeOut();
        $("#write-commentW").hide();
    });

    $("#background-shade").click(function () {
        $("#write-commentW .close-button").click();
        $("#add-to-own-movielist .close-button").click();
    });

    $("#write-commentW .create").click(function () {
        if ($("input[name='title']").val() == "" || $("input[name='title']").val() == null) {
            $("#tip-title").show();
        } else if ($(".description-textarea").val() == "" || $(".description-textarea").val() == null) {
            $("#tip-comment").show();
        } else {
            $.get("/movie/writeComment?title=" + $("input[name='title']").val() + "&rate=" + $("#star-rate").raty('score') +
                "&comment=" + $(".description-textarea").val()+"&movieID="+movieIDM, function (data) {
                if (data == "SUCCESS") {
                    alert(data);
                    $("button#write-comment").hide();
                } else {
                    alert(data);
                    $("button#write-comment").css({
                        "border-color": "transparent",
                        "color": "gray",
                        "background-color": "transparent"
                    });

                }
            });
        }
    });
    $("input[name='title']").on('input propertychange', function () {
        $("#tip-title").hide();
    });
    $(".description-textarea").on('input propertychange', function () {
        $("#tip-comment").hide();
    });

});

$(".reviews-type-but").click(function () {
    if($(this).hasClass("selected")){
        return;
    }
    var index = $(".reviews-type-but").index(this);
    chooseReviewType(index,0);
    for (i = 0; i < 4; i++) {
        if (index == i) {
            continue;
        }
        name = $(".reviews-type-but").eq(i).attr("name");
        if ($(".reviews-type-but").eq(i).hasClass("selected")) {
            $(".reviews-type-but").eq(i).css({
                "color": "gray",
                "background-color": "transparent"
            });
            $(".reviews-type-but").eq(i).attr("name", "unselected");
            $(".reviews-type-but").eq(i).removeClass("selected");
        }
    }
    $(this).css({
        "color": "white",
        "background-color": "skyblue"
    });
    $(this).attr("name", "selected");
    $(this).addClass("selected");
});


$("#reviews-last-page").click(function () {
    $("#reviews-page-index").text(function (i, origText) {
        if (parseInt(origText) == 1) {
            return origText;
        }
        return parseInt(origText) - 1;
    });
    var curPageIndex = parseInt($("#reviews-page-index").text()) - 1;
    var userType=-1;
    for(var index=0;index<$(".reviews-type-but").length;index++){
        if($(".reviews-type-but").eq(index).attr("name").indexOf("selected") == 0){
            userType=index;
        }
    }
    chooseReviewType(userType, curPageIndex);
});
$("#reviews-next-page").click(function () {
    $("#reviews-page-index").text(function (i, origText) {
        totalPage = $("#reviews-total-page").text();
        if (parseInt(origText) == parseInt(totalPage)) {
            return origText;
        }
        return parseInt(origText) + 1;
    });
    var curPageIndex = parseInt($("#reviews-page-index").text()) - 1;
    var userType=-1;
    for(var index=0;index<$(".reviews-type-but").length;index++){
        if($(".reviews-type-but").eq(index).attr("name").indexOf("selected") == 0){
            userType=index;
        }
    }
    chooseReviewType(userType, curPageIndex);
});

//更换评论类别
function chooseReviewType(userType,pageIndex) {//0,1,2,3
    //更新评论数据
    for (var index = 0; index < $("div.reviews-area table.reviews-table tr").length; index++) {
        $("div.reviews-area table.reviews-table tr").eq(index).slideUp();
    }
    $("#reviews-turn-page").slideUp();
    $.get("/movie/getReviewsByType?movieID=" + movieIDM + "&userType=" + userType
        +"&pageIndex="+pageIndex, function (data) {
        loadReviews(data);
        // if (data[9] <= 9 || data[10] <= 10) {
        //     $("button.show-more").hide();
        // }

    });
}

//加载评论数据
function loadReviews(data) {
    if (data[9] == 0) {
        $("p.tip-noComments").text("No comments");
        $("span#movie-reviews-num").text(0);
        // alert(data[9]);
    } else {
        for (var i = 0; i < data[9]; i++) {
            //16个
            //ratePic,reviewTime,userType,userPoster,userName,reviewTitle,helpful,thumbUp,review,showReviewSize,totalReviewSize,userHref,totalSize,reviewHrefList,thumUpNumList,isTumbUpList
            $("div.reviews-area table.reviews-table tr").eq(i).slideDown();
            $("div.review-piece .rate-pic").eq(i).attr("src", data[0][i]);
            $("div.review-piece .review-time").eq(i).text(data[1][i]);
            $("div.review-piece .user-type").eq(i).text(data[2][i]);
            $("div.review-piece .user-pic-a").eq(i).attr("href", data[11][i]);
            $("div.review-piece .user-pic-img").eq(i).attr("src", data[3][i]);
            $("div.review-piece .user-name").eq(i).text(data[4][i]);
            $("div.review-piece .user-name").eq(i).attr("href", data[11][i]);
            $("div.review-piece .review-title").eq(i).text(data[5][i]);
            $("div.review-piece .helpful").eq(i).innerHTML = data[6][i];
            if(!data[7][i]) {
                $("div.review-piece .like-div").eq(i).hide();
            }else{
                if(data[15][i]==true){
                    $("div.review-piece .like-div .like-pic").eq(i).addClass("selected").attr("src","/images/like-click.png");
                }
                $("div.review-piece .like-div .like-num").eq(i).text(data[14][i]);
                $("div.review-piece .like-div").eq(i).show();
            }
            $("div.review-piece .review-content").eq(i).text(data[8][i]);
            // $("div.review-piece .review-title").eq(i).attr("href",data[13][i]);
            // alert(data[9]);
        }
        $("#reviews-turn-page").slideDown();
        $("span#movie-reviews-num").text(data[10]);
        $("p.tip-noComments").text("");
        $("#reviews-total-page").text(data[12]);
        movieID=data[16];
    }
}

//得到相关影人信息
function getRelativePeoplePage(curPageIndex) {//curPageIndex从0开始
    for (var i = 0; i < 5; i++) {
        $("#relativePeople .relativePeople-table td").eq(i).fadeOut();
    }
    $.get("/movie/getRelativePeople?movieID=" + movieIDM + "&pageIndex=" + curPageIndex, function (data) {
        for (var index = 0; index < data.length; index++) {
            $("#relativePeople .relativePeople-table td").eq(index).find("a").attr("href", "/figure/j" + data[index].figureID);
            $("#relativePeople .relativePeople-table td").eq(index).find("span").text(data[index].name);
            $("#relativePeople .relativePeople-table td").eq(index).find("img").attr("src", data[index].avatar);
            $("#relativePeople .relativePeople-table td").eq(index).fadeIn();
        }
    });
}

//得到alsoLike信息
function getAlsoLikeMovies() {
    for (var i = 0; i < 5; i++) {
        $("#alsoLike .alsoLike-table td").eq(i).fadeOut();
    }
    $.get("/movie/getAlsoLikeMovies?movieIDM=" + movieIDM, function (data) {
        for (var index = 0; index < data.length; index++) {
            $("#alsoLike .alsoLike-table td").eq(index).find("a").attr("href", "/movie/j" + data[index].id);
            $("#alsoLike .alsoLike-table td").eq(index).find("img").attr("title",data[index].name);
            $("#alsoLike .alsoLike-table td").eq(index).find("img").attr("src", data[index].poster);
            $("#alsoLike .alsoLike-table td").eq(index).fadeIn();
        }
    });
}

function likeOrDislike(ifLike) {//ifLike:0(removePreference),1(doLike=true),2(doLike=false)
    $.get("/movie/likeOrDislike?movieID" + movieIDM + "&ifLike=" + ifLike, function (data) {
        if (data == "Please sign in!") {
            alert(data);
            $(".movie-like").attr("name", "unselected");
            $(".movie-dislike").attr("name", "unselected");
        } else {
            alert(data);
        }
    });
}


function haveWatched(ifWatched) {
    $.get("/movie/haveWatched?movieID=" + movieIDM + "&ifWatched=" + ifWatched, function (data) {
        if (data == "Please sign in!") {
            alert(data);
            $("#have-watched").attr("name", "unselected");
        } else {
            alert(data);
        }
    });
}

function addToCollection() {
    $.get("/movie/addToCollection?", function (data) {
        alert(data);
    })
}
$(".description-textarea").on('input propertychange', function () {
    $(".tip-discription").hide();
});

var movieIndex = -1;
$("#add").click(function () {
    movieIndex=movieID;
    $.get("/movielist/isLogIn", function (data) {
        if (data == false) {
            alert("please log in first.");
            return;
        } else {
            $.get("/movielist/getUserMovieSheet?movieSheetID=" + -1, function (data) {
                if (data == null || data.length == 0) {
                    alert("you haven't create movielist yet.");
                    return;
                }
                $("#add-to-own-movielist ul").empty();
                for (var index = 0; index < data.length; index++) {
                    var radio = $("<input type='radio'>");
                    var movielistName = $("<label></label>").text(data[index].movieSheetName);
                    var li = $("<li></li>").addClass("round").append(radio, movielistName);
                    li.click(function () {
                        var index = indexOfAll(this, $("#add-to-own-movielist ul li"));
                        $("#add-to-own-movielist ul input").eq(index).attr("checked", true);
                        for (var i = 0; i < $("#add-to-own-movielist ul li").length; i++) {
                            if ($("#add-to-own-movielist ul li").eq(i).hasClass("selected") && i != index) {
                                $("#add-to-own-movielist ul li").eq(i).removeClass("selected");
                                $("#add-to-own-movielist ul input").eq(i).attr("checked", false);
                            }
                        }
                        $(this).addClass("selected");
                    });
                    $("#add-to-own-movielist ul").append(li);
                }
                $("#add-to-own-movielist ul input").first().attr("checked", true);
                $("#add-to-own-movielist ul li").first().addClass("selected");
                $("#background-shade").fadeIn();
                $("#add-to-own-movielist").show();
            });
        }
    });
});
$("#add-to-own-movielist .close-button").click(function () {
    $("#background-shade").fadeOut();
    $("#add-to-own-movielist").hide();
    movieIndex = -1;
});

$("#add-to-own-movielist .add-to-movielist-button").click(function () {
    if ($("#add-to-own-movielist .description-textarea").val() == "" ||
        $("#add-to-own-movielist .description-textarea").val() == null) {
        $("#add-to-own-movielist .tip-discription").show();
    } else {
        var description = $("#add-to-own-movielist .description-textarea").val();
        var selectedIndex = -1;
        for (var index = 0; index < $("#add-to-own-movielist ul li").length; index++) {
            if ($("#add-to-own-movielist ul li").eq(index).hasClass("selected")) {
                selectedIndex = index;
                break;
            }
        }
        $.get("/movielist/addMovieToMovielist?movieSheetID=" + -1 +
            "&movieIndex=" + movieIndex + "&selectedMovielistIndex=" + selectedIndex + "&description=" + description, function (data) {
            if (data == "SUCCESS") {
                window.location.href = "/movie/j" + movieID;
            } else {
                alert(data);
                $("#add-to-own-movielist .close-button").click();
            }
        });
    }
});
