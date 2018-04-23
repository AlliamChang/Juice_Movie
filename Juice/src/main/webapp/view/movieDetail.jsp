<%@ page import="cn.cseiii.model.FilmMakerVO" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.cseiii.model.MovieShowVO" %>
<%--
  Created by IntelliJ IDEA.
  User: sparkler
  Date: 2017/5/13
  Time: AM8:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${movieDetailVO.name}</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/movieDetail.css">
    <link rel="stylesheet" href="../css/animate.min.css">
    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/jquery.cookie.js"></script>
    <script src="../js/jquery.waypoints.min.js"></script>
    <script src="../js/jquery.raty.js"></script>
</head>
<body>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="-1"/>
    </jsp:include>
    <div id="add-to-own-movielist" class="round">
            <div id="own-movielists-table">
                <span id="tag-movielist">your movielists:</span>
                <form>
                    <ul>
                        <li class="round"><input type="radio" name="own-movielists" checked=true>1</li>
                    </ul>
                </form>
            </div>
            <hr width="2px" color="grey"/>
            <div id="right-div">
                <div class="close-button"></div>
                <span class="tip-discription" class="tip-text">Hey, there's nothing!</span>
                <span class="description">description: <br/>
            <textarea class="description-textarea" name="description" placeholder="description"></textarea>
             </span>
                <button class="add-to-movielist-button round">add to this movielist</button>
            </div>
        </div>
    <div class="movie-basicInfo">
        <div id="movie-name">
            <h1 class="movieName">${movieDetailVO.name}</h1>
            <%--<h2 id="testTEST">啊咧咧hhhhh</h2>--%>
        </div>
        <div id="movie-detail">
            <div id="movie-poster">
                <%--<img class="moviePoster" src="../images/yourName.jpg" alt="poster" width="140" height="196"/>--%>
                <img class="moviePoster" src="${movieDetailVO.poster}" alt=${movieDetailVO.name} width="140"
                     height="196" onerror="javascript:this.src='../images/movie_error.png'"/>
                <br/><br/>
                <div align="center">
                    <img class="movie-like" alt="like" src="../images/like.png" width="20px" height="20px"
                         name="unselected" aria-disabled="false"/>
                    &nbsp&nbsp&nbsp
                    <img class="movie-dislike" alt="dislike" src="../images/dislike.png" width="20px" height="20px"
                         name="unselected" aria-disabled="false"/>
                </div>
            </div>
            <table id="movie-info-table">
                <%--<div id="movie-infoID">--%>
                <tr>
                    <td>
                        <p class="directorID">Director</p>
                    </td>
                    <td>
                        <p class="filmMaker" id="director">${array[0]}</p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="writerID">Writer</p>
                    </td>
                    <td>
                        <p class="filmMaker" id="writer">${array[1]}</p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="starID">Stars</p>
                    </td>
                    <td>
                        <p class="filmMaker" id="star">${array[2]}</p>

                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="genreID">Genres</p>
                    </td>
                    <td>
                        <p id="genre">${array[3]}</p>
                    </td>
                </tr>
                <tr>
                    <td>

                        <p class="countryID">Country</p>
                    </td>
                    <td>
                        <p class="country">${movieDetailVO.country}</p>

                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="languageID">Language</p>
                    </td>
                    <td>
                        <p class="language">${movieDetailVO.language}</p>

                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="releaseTimeID">Release time</p>
                    </td>
                    <td>
                        <p class="releaseTime">${releasedTime}</p>

                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="runtimeID">Runtime</p>
                    </td>
                    <td>
                        <p class="runtime">${runtime}</p></td>
                </tr>

                <%--</div>--%>
                <%--<div id="movie-infoDetail">--%>
                <%--</div>--%>
            </table>
            <div id="movie-rate">
                <h3 id="movie-rate-ID">Rate</h3>
                <p class="movieRate">${movieDetailVO.doubanRating}</p>
                <div id="rate-picAndNum">
                    <div id="rate-pic">
                        <img src=${ratePic} alt="rate" width="120"/>
                    </div>
                    <div id="rate-allNum">
                        <span class="numOfComments">${movieDetailVO.doubanVotes}</span>
                        <span>comments</span>
                    </div>
                </div>
                <table class="rate-chart" align="left">
                    <tr>
                        <td>
                            <p>5 star</p>
                        </td>
                        <td>
                            <progress value="${percentage[0]}" max="100" style="width: 100px;height: 10px"></progress>
                        </td>
                        <td>
                            <p>${percentage[0]}%</p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>4 star</p>
                        </td>
                        <td>
                            <progress value="${percentage[1]}" max="100" style="width: 100px;height: 10px"></progress>
                        </td>
                        <td>
                            <p>${percentage[1]}%</p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>3 star</p>
                        </td>
                        <td>
                            <progress value="${percentage[2]}" max="100" style="width: 100px;height: 10px"></progress>
                        </td>
                        <td>
                            <p>${percentage[2]}%</p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>2 star</p>
                        </td>
                        <td>
                            <progress value="${percentage[3]}" max="100" style="width: 100px;height: 10px"></progress>
                        </td>
                        <td>
                            <p>${percentage[3]}%</p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>1 star</p>
                        </td>
                        <td>
                            <progress value="${percentage[4]}" max="100" style="width: 100px;height: 10px"></progress>
                        </td>
                        <td>
                            <p>${percentage[4]}%</p>
                        </td>
                    </tr>
                </table>
                <%--<div class="power" style="width: 8px" ></div>--%>
                <hr width="220px" align="left">
                <div id="better-than" align="left">
                    <p>${betterThan1}</p>
                    <p>${betterThan2}</p>
                    <%--<p>better than--%>
                    <%--<span class="betterThan1" style="color: dodgerblue">${per1}</span> %--%>
                    <%--<span class="genre1" style="color: dodgerblue">${genre1}</span>--%>
                    <%--</p>--%>
                    <%--<p>better than--%>
                    <%--<span class="betterThan2" style="color: dodgerblue">${per2}</span> %--%>
                    <%--<span class="genre2" style="color: dodgerblue">${genre2}</span>--%>
                    <%--</p>--%>
                </div>
            </div>
        </div>
    </div>
    <div class="movie-otherInfo">
        <div id="choose-type">
            <button class="choose-but" id="have-watched" name="unselected">Have seen</button>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
            <span><image class="add-pic" src="../images/add.png" width="20px" height="20px"/>
                <button class="choose-but" id="add" name="unselected">Add to sheet</button>
            </span>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
            <button class="choose-but" id="write-comment" name="unselected" disabled="disabled">Write comment</button>


            <div id="write-commentW" class="info-tool-position round">
                <div class="close-button"></div>
                <span id="tip-title" class="tip-text">Hey, there's nothing!</span>
                <span id="tip-comment" class="tip-text">Hey, there's nothing!</span>
                <form action="" method="get" id="write-commentW-form">
                    <span class="title">Title: &nbsp;<input type="text" class="text-line title-line" name="title"
                                                            placeholder="title"></span>
                    <br/>
                    <%--评分打分--%>
                    <div class="rating">
                        <span>Rate: &nbsp;&nbsp;&nbsp;</span><span id="star-rate" data-num="4"></span>
                    </div>
                    <span class="comment">Comment: <br/>
                    <textarea class="description-textarea" name="comment" placeholder="comment"></textarea></span><br/>
                </form>

                <button class="create round">create</button>
            </div>
            <div id="background-shade"></div>

        </div>
        <br/>


        <div id="movie-storyline">
            <h2>Storyline</h2>
            <p class="movieStoryline">${movieDetailVO.plot}</p>
        </div>
        <br/>
        <div id="relativePeople">
            <h2 class="little-title">Relative people<span class="tip-number">( <a><span
                    id="movie-relativePeople-num">${relativePeopleSize}</span> in total </a>）</span>
                <%--<h2 class="little-title">Relative people<span class="tip-number">( <a><span--%>
                <%--id="movie-relativePeople-num"></span> in total </a>）</span>--%>
                <span>
                    <table class="turn-page" align="center">
                        <tr>
                            <td>
                                <span class="tip-number" style="position: relative;bottom: 10px">
                                    <span id="movie-relativePeople-page-index">1</span> / <span
                                        id="movie-relativePeople-total-page">${relativePeoplePageSize}</span> </span></td>
                            <td style="padding-right: 7px">
                                <img class="last-page" id="movie-relativePeople-last-page" src="../images/lastPage.png"
                                     alt="last page" width="20px" height="20px"/></td>
                            <td style="padding-right: 10px">
                                <img class="next-page" id="movie-relativePeople-next-page" src="../images/nextPage.png"
                                     alt="next page" width="20px" height="20px"/></td></tr>
                    </table>
                </span>
            </h2>

            <table cellspacing="15px" class="relativePeople-table">
                <tr>
                    <%
                        //                        int desIndex2 = Math.min(5, ((List) request.getAttribute("relativePeople")).size());
//                        for (int i = 0; i < desIndex2; i++) {
                        for (int i = 0; i < 5; i++) {
                    %>
                    <%--<td>--%>
                    <%--<div>--%>
                    <%--<a href="/figure/j<%=((FilmMakerVO)((List)request.getAttribute("relativePeople")).get(i)).getFigureID()%>">--%>
                    <%--<img src="<%=((FilmMakerVO)((List)request.getAttribute("relativePeople")).get(i)).getAvatar()%>"--%>
                    <%--alt="relative people" width="100" height="140"/>--%>
                    <%--<br/>--%>
                    <%--<span><%=((FilmMakerVO) ((List) request.getAttribute("relativePeople")).get(i)).getName()%></span></a>--%>
                    <%--</div>--%>
                    <%--</td>--%>
                    <td>
                        <div>
                            <a href="">
                                <img src=""
                                     alt="relative people" width="100" height="140"
                                     onerror="javascript:this.src='../images/default_avatar.png'"/>
                                <br/>
                                <span></span></a>
                        </div>
                    </td>
                    <%}%>
                </tr>
            </table>
        </div>
        <div id="alsoLike">
            <h2>People also like</h2>
            <div>
                <table cellspacing="15px" class="alsoLike-table">
                    <tr>
                        <%
                            //                        int desIndex3 = Math.min(5, ((List) request.getAttribute("movieShowVOList")).size());
//                        for (int i = 0; i < desIndex3; i++) {
                            for (int i = 0; i < 5; i++) {
                        %>
                        <%--<td>--%>
                        <%--<div>--%>
                        <%--<a href="/movie/j<%=((List<MovieShowVO>)request.getAttribute("movieShowVOList")).get(i).getId()%>">--%>
                        <%--<img src="<%=((List<MovieShowVO>)request.getAttribute("movieShowVOList")).get(i).getPoster()%>" alt="also like" width="100" height="140"/>--%>
                        <%--<br/>--%>
                        <%--<span ><%=((List<MovieShowVO>)request.getAttribute("movieShowVOList")).get(i).getName()%></span>--%>
                        <%--</a>--%>
                        <%--</div>--%>
                        <%--</td>--%>
                        <td>
                            <div>
                                <a href="">
                                    <img src="" alt="also like" width="100" height="140"
                                         onerror="javascript:this.src='../images/movie_error.png'" title=""/>
                                    <br/>
                                    <span></span>
                                </a>
                            </div>
                        </td>
                        <%}%>

                    </tr>
                </table>
            </div>
            <div class="reviews-area">
                <%--<h2 class="little-title">Reviews<span class="tip-number">( <a><span--%>
                <%--id="movie-reviews-num">${reviewsNum}</span> in total </a>）</span></h2>--%>
                <h2 class="little-title">Reviews<span class="tip-number">( <a><span
                        id="movie-reviews-num"></span> in total </a>）</span></h2>
                <table id="reviews-type">
                    <tr>
                        <td>
                            <button class="reviews-type-but selected" id="all-reviews" name="selected"
                                    style="color: white; background-color: skyblue"
                            >
                                All reviews
                            </button>
                        </td>
                        <td>
                            <button class="reviews-type-but" id="imdb-reviews" name="unselected"
                            >
                                IMDB reviews
                            </button>
                        </td>
                        <td>
                            <button class="reviews-type-but" id="douban-reviews" name="unselected">Douban
                                reviews
                            </button>
                        </td>
                        <td>
                            <button class="reviews-type-but" id="juice-reviews" name="unselected">Juice
                                reviews
                            </button>
                        </td>
                    </tr>
                </table>
                <hr color="lightgrey" size="1px" width="600px" align="center"/>
                <br/>
                <table class="reviews-table">
                    <%
                        //                    int showReviewsSize = (int) request.getAttribute("showReviewsSize");
//                    for (int i = 0; i < showReviewsSize; i++) {
                        for (int i = 0; i < 10; i++) {
                    %>
                    <%--问题已解决(这里有问题，index不随上面的变化而变化)--%>
                    <%--<tr>--%>
                    <%--<td>--%>
                    <%--<div class="review-piece">--%>
                    <%--<p style="font-size: 17px;font-weight: 600"><a class="review-title"><%=((List)request.getAttribute("reviewTitleList")).get(i)%></a></p>--%>
                    <%--<p style="font-style: oblique;font-size: 12pt">--%>
                    <%--<a class="user-pic-a" href="<%=((List)request.getAttribute("userHrefList")).get(i)%>"><img class="user-pic-img"--%>
                    <%--src="<%=((List)request.getAttribute("userPosterList")).get(i)%>"--%>
                    <%--width="40px"/> </a>--%>
                    <%--<a href="<%=((List)request.getAttribute("userHrefList")).get(i)%>"--%>
                    <%--class="user-name"><%=((List) request.getAttribute("userNameList")).get(i)%>--%>
                    <%--</a>--%>
                    <%--<img class="rate-pic" src="<%=((List)request.getAttribute("ratePicList")).get(i)%>"--%>
                    <%--alt="rate image" width="120px">--%>
                    <%--<span class="review-time"><%=((List) request.getAttribute("reviewTimeList")).get(i)%></span>--%>
                    <%--<label> from <a--%>
                    <%--class="user-type"><%=((List) request.getAttribute("userTypeList")).get(i)%>--%>
                    <%--</a></label>--%>
                    <%--</p>--%>
                    <%--<p class="helpful"--%>
                    <%--style="font-style: italic; font-size: 10pt"><%=((List) request.getAttribute("helpfulList")).get(i)%>--%>
                    <%--</p>--%>
                    <%--<p class="review-content"><%=((List) request.getAttribute("reviewList")).get(i)%>--%>
                    <%--</p>--%>
                    <%--<div class="like-div"><%=((List) request.getAttribute("thumbUpList")).get(i)%>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--</td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td>
                            <div class="review-piece">
                                <p style="font-size: 17px;font-weight: 600"><a
                                        class="review-title">
                                </a></p>
                                <p style="font-style: oblique;font-size: 12pt">
                                    <a class="user-pic-a"
                                       href=""><img
                                            class="user-pic-img"
                                            src=""
                                            width="40px" onerror="javascript:this.src='../images/default_avatar.png'"/> </a>
                                    <a href=""
                                       class="user-name">
                                    </a>
                                    <img class="rate-pic" src=""
                                         alt="rate image" width="120px">
                                    <span class="review-time"></span>
                                    <label> from <a
                                            class="user-type">
                                    </a></label>
                                </p>
                                <p class="helpful"
                                   style="font-style: italic; font-size: 10pt">
                                </p>
                                <p class="review-content">
                                </p>
                                <div class="like-div">
                                    <img class='like-pic' alt='like' src='/images/like.png' width='20px' height='20px'/>
                                    <span class='like-num' name='unclicked' style='padding-left: 10px'>10</span>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <%
                        }
                    %>


                </table>
                <p class="tip-noComments" align="center"></p><br/><br/>
                <%--<button class="show-more" style="width: 585px;height: 40px;font-size: 20px">show more&nbsp--%>
                <%--<img src="../images/showmore.png" alt="show more" width="18px" height="18px"></button>--%>
                <div id="reviews-turn-page" align="center">
                    <p style="font-size: 18px;">
                        <img class="last-page" id="reviews-last-page" src="../images/lastPage.png"
                             alt="last page" width="20px" height="20px"/>&nbsp;&nbsp;&nbsp;&nbsp;
                        <span class="page-span" style="font-size: 22px"><span id="reviews-page-index">1</span>/<span
                                id="reviews-total-page">10</span></span>&nbsp;&nbsp;&nbsp;&nbsp;
                        <img class="next-page" id="reviews-next-page" src="../images/nextPage.png"
                             alt="next page" width="20px" height="20px"/>&nbsp;&nbsp;&nbsp;&nbsp;
                        <%--<span>Turn to <input type="text" name="pageIndex" class="jump-page" id="page-index-jump"/></span>--%>
                    </p>

                </div>
            </div>
        </div>
    </div>
    <script src="../js/animation.js"></script>
    <script src="../js/main.js"></script>
    <script src="../js/movieDetail.js"></script>
    <script>
        $("#write-commentW").hide();
        <%--var num = ${reviewsNum};--%>

        var movieIDM = "${movieDetailVO.id}";
        $(function () {
            $("#star-rate").raty({
                score: function () {
                    return $(this).attr("data-num");
                },
                starOn: '../images/star-on.png',
                starOff: '../images/star-off.png',
                readOnly: false,
                size: 30
            })
        });
    </script>
    <script>
        getRelativePeoplePage(0);
        chooseReviewType(0, 0);
        getAlsoLikeMovies();
        $("#add-to-own-movielist").hide();
        $("#add-to-own-movielist .tip-discription").hide();
    </script>
    <%--<script>--%>
    <%--function update() {--%>
    <%--document.getElementsByClassName("movieName").innerHTML = $("<h1></h1>").text(${movieDetailVO.name});--%>
    <%--};--%>
    <%--</script>--%>

    <%--background effect--%>
    <div id="particles-js"></div>
    <script src="../js/particles/particles.min.js"></script>
    <script src="../js/particles/app.js"></script>
</body>
</html>