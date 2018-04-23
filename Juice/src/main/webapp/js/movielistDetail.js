/**
 * Created by lenovo on 2017/6/9.
 */
$(document).ready(function () {
    //收藏按钮
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
    $(".collect-div img").click(function () {
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.")
                return;
            }
            if ($(".collect-div img").hasClass("selected")) {
                $.get("/movielist/removeCollectThisMovielist?movieSheetID=" + movieSheetID, function (data) {
                    if (data == "SUCCESS") {
                        $(".collect-div img").attr("src", "/images/uncollected.png");
                        $(".collect-div img").removeClass("selected");
                        $(".collect-div img").next(".collect-num").text(function (i, origText) {
                            return parseInt(origText) - 1;
                        });
                    } else {
                        alert(data);
                    }
                });
            } else {
                $.get("/movielist/collectThisMovielist?movieSheetID=" + movieSheetID, function (data) {
                    if (data == "SUCCESS") {
                        $(".collect-div img").attr("src", "/images/collected.png");
                        $(".collect-div img").addClass("selected");
                        $(".collect-div img").next(".collect-num").text(function (i, origText) {
                            return parseInt(origText) + 1;
                        });
                    } else {
                        alert(data);
                    }
                });
            }
        });
    });

    $("a.add-movie-div").click(function () {
        $("#background-shade").fadeIn();
        $("#add-movies").show();
    });

    $("a.delete-movielist").click(function () {
        $("#background-shade").fadeIn();
        $("#delete p").text("are you sure to delete this movielist? T﹏T ");
        $("#delete input[class='delete-type']").val("movielist");
        $("#delete").show();
    });

    var deleteMovieIndex = -1;
    $(".delete-movie").click(function () {
        $("#background-shade").fadeIn();
        $("#delete p").text("are you sure to remove this movie from movielist? T﹏T ");
        $("#delete input[class='delete-type']").val("movie");
        $("#delete").show();
        deleteMovieIndex = indexOfAll(this, $(".delete-movie"));
    });

    function indexOfAll(current, all) {
        for (var i = 0; i < all.length; i++) {
            if (all[i] == current) {
                return i;
            }
        }
        return -1;
    }

    /*$("a.modify-movie").click(function () {
     $("#background-shade").fadeIn();
     var index=$("a.modify-movie").indexOf(this);
     var movieName=$(".contain-movies-piece a.piece-movie-name").eq(index).text();
     var description=$(".contain-movies-piece .movie-description").eq(index).text();
     $("#modify #modify-target").text(movieName);
     $("#modify .description-textarea").val(description);
     $("#modify .target-type").val("movie");
     $("#modify").show();
     });
     $("a.modify-movielist").click(function () {
     $("#background-shade").fadeIn();
     var targetName=$("#movielist-name").text();
     var description=$("#movielist-description").text();
     $("#modify #modify-target").text(targetName);
     $("#modify .description-textarea").val(description);
     $("#modify .target-type").val("movielist");
     $("#modify").show();
     });
     */

    //关闭取消按钮
    $("#add-movies .close-button").click(function () {
        $("#background-shade").fadeOut();
        $("#add-movies").hide();
    });
    $("#delete #cancel-button").click(function () {
        $("#background-shade").fadeOut();
        $("#delete").hide();
        deleteMovieIndex = -1;
    });

    $("#background-shade").click(function () {
        $("#add-movies .close-button").click();
        $("#delete #cancel-button").click();
        $("#add-to-own-movielist .close-button").click();
    });

    $("input[name='title']").on('input propertychange', function () {
        $("#tip-movie").hide();
        $("#add-movies input.pre-search-movieID").val(-1);
        var movieName = $(this).val();
        if (movieName != null && movieName != "") {
            $.get("/movielist/getPreSeach?movieName=" + movieName, function (data) {
                $("#pre-search-ul").empty();
                $.each(data, function (index, movie) {
                    var image = $("<img/>").attr({
                        "src": movie.imageLink,
                        "height": "30px"
                    });
                    var name = $("<label></label>").text(" " + movie.name);
                    var li = $("<li></li>").append(image, name);
                    $("#pre-search-ul").append(li);
                    li.click(function () {
                        $("#add-movies input[name='title']").val(movie.name);
                        $("#add-movies input.pre-search-movieID").val(movie.id);
                        $("#add-movies #pre-search-ul").hide();
                    });
                });
                $("#pre-search-ul").show();
            });
        } else {
            $("#pre-search-ul").hide();
        }
    });
    $(".description-textarea").on('input propertychange', function () {
        $(".tip-discription").hide();
    });

    $("#add-movies .add-button").click(function () {
        if ($("input[name='title']").val() == "" || $("input[name='title']").val() == null) {
            $("#tip-movie").text("Hey, there's nothing!")
            $("#tip-movie").show();
        } else if ($("input.pre-search-movieID").val() == -1) {
            $("#tip-movie").text("please select one movie.")
            $("#tip-movie").show();
        } else if ($("#add-movies .description-textarea").val() == "" || $("#add-movies .description-textarea").val() == null) {
            $("#add-movies .tip-discription").show();
        }else{
                $.get("/movielist/addMovieToSheet?movieSheetID=" + movieSheetID + "&movieID=" + $("input.pre-search-movieID").val() +
                    "&description=" + $("#add-movies .description-textarea").val(), function (data) {
                    if (data == "SUCCESS") {
                        window.location.href = "/movielist/j" + movieSheetID;
                    } else {
                        alert(data);
                        $("#add-movies .close-button").click();
                    }
                });
            }
        });

    $("#delete #yes-button").click(function () {
        if ($("#delete input[class='delete-type']").val().indexOf("movielist") >= 0) {
            $.get("/movielist/deleteSheet?movieSheetID=" + movieSheetID, function (data) {
                if (data == "SUCCESS") {
                    window.location.href = "/user/j" + userID + "/movielist";
                } else {
                    alert(data);
                    $("#delete #cancel-button").click();
                }
            });
        } else {
            $.get("/movielist/deleteMovieFromSheet?movieSheetID=" + movieSheetID + "&movieIndex=" + deleteMovieIndex, function (data) {
                if (data == "SUCCESS") {
                    window.location.href = "/movielist/j" + movieSheetID;
                } else {
                    alert(data);
                    $("#delete #cancel-button").click();
                }
            });
        }
    });

    var movieIndex = -1;
    $(".contain-movies-piece .add-to-movielist").click(function () {
        movieIndex = indexOfAll(this, $(".contain-movies-piece .add-to-movielist"));
        $.get("/movielist/isLogIn", function (data) {
            if (data == false) {
                alert("please log in first.");
                return;
            } else {
                $.get("/movielist/getUserMovieSheet?movieSheetID=" + movieSheetID, function (data) {
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
            $.get("/movielist/addMovieToMovielist?movieSheetID=" + movieSheetID +
                "&movieIndex=" + movieIndex + "&selectedMovielistIndex=" + selectedIndex + "&description=" + description, function (data) {
                if (data == "SUCCESS") {
                    window.location.href = "/movielist/j" + movieSheetID;
                } else {
                    alert(data);
                    $("#add-to-own-movielist .close-button").click();
                }
            });
        }
    });
});