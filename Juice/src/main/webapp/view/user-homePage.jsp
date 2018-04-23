<%@ page import="cn.cseiii.model.Page" %>
<%@ page import="cn.cseiii.model.ReviewVO" %>
<%@ page import="cn.cseiii.model.MovieDetailVO" %>
<%@ page import="cn.cseiii.service.MovieService" %>
<%@ page import="cn.cseiii.service.impl.MovieServiceImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.cseiii.model.MovieShowVO" %><%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/5/28
  Time: 9:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${userName}'s home</title>
    <link rel="shortcut icon" href="/images/favicon.ico">
    <link rel="bookmark" href="/images/favicon.ico">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/animate.min.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/jquery.cookie.js"></script>
    <script src="/js/jquery.waypoints.min.js"></script>
    <link href="/css/user.css" rel="stylesheet">
</head>
<body>
<jsp:include page="user.jsp" flush="true">
    <jsp:param name="index" value="0"/>
</jsp:include>
<div class="content">
    <div id="user-homePage" style="width:600px;float:left">
        <div id="movie-watched">
            <h2 class="little-title">Watched movies<span class="tip-number">( <span
                    id="watched-movies-num">${watchedMovieList.size()}</span> in total ）</span>
                <%if(((List)request.getAttribute("watchedMovieList")).size()!=0){%>
                <span><table class="turn-page" align="center">
                                <tr>
                                    <td>
                                        <span class="tip-number" style="position: relative;bottom: 10px">
                                            <span id="movie-watched-page-index">1</span> / <span
                                                id="movie-watched-total-page">${watchedMoviePageSize}</span> </span>
                                    </td>
                                    <td style="padding-right: 7px">
                                        <img class="last-page" id="movie-watched-last-page" src="/images/lastPage.png"
                                             alt="last page" width="20px" height="20px"/>
                                    </td>
                                    <td style="padding-right: 10px">
                                        <img class="next-page" id="movie-watched-next-page" src="/images/nextPage.png"
                                             alt="next page" width="20px" height="20px"/>
                                    </td>
                                </tr>
                            </table></span>
                <%}%>
            </h2>
            <%if(((List)request.getAttribute("watchedMovieList")).size()!=0){%>
            <table cellspacing="15px" class="watched-movie-table">
                <tr>
                    <%int desIndex=Math.min(4,((List)request.getAttribute("watchedMovieList")).size());
                        for(int index=0;index<desIndex;index++){%>
                    <td>
                        <div style="max-width: 130px;max-height: 200px;">
                            <a href="/movie/j<%=((MovieShowVO)((List)request.getAttribute("watchedMovieList")).get(index)).getId()%>">
                                <img src="<%=((MovieShowVO)((List)request.getAttribute("watchedMovieList")).get(index)).getPoster()%>"
                                     alt="movie pic" width="126" height="180" onerror="javascript:this.src='../images/movie_error.png'"/>
                            <p><%=((MovieShowVO)((List)request.getAttribute("watchedMovieList")).get(index)).getName()%></p></a>
                        </div>
                    </td>
                    <%}%>
                    <%--<td>
                        <div>
                            <a href="">
                                <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                            <p>movie name</p></a>
                        </div>
                    </td>--%>
                </tr>
                <tr>
                    <td><div id="movie-collect-jump"></div></td>
                </tr>
            </table>
            <%}else{%>
            <h3 style="font-weight: 500" align="center">╮(￣▽￣)╭ you haven't watched movie yet.</h3>
            <%}%>
        </div>
        <br/>
        <div id="movie-collect">
            <h2 class="little-title" style="margin-bottom: -20px">Collected movies
                <span class="tip-number">( <span
                        id="collected-movies-num">${preferMoviewList.size()}</span> in total )</span>
                <%if(((List)request.getAttribute("preferMoviewList")).size()!=0){%>
                <span><table class="turn-page" align="center">
                                <tr>
                                    <td>
                                        <span class="tip-number" style="position: relative;bottom: 10px">
                                            <span id="movie-collect-page-index">1</span> / <span
                                                id="movie-collect-total-page">${preferMoviePageSize}</span> </span>
                                    </td>
                                    <td style="padding-right: 7px">
                                        <img class="last-page" id="movie-collect-last-page" src="/images/lastPage.png"
                                             alt="last page" width="20px" height="20px" onerror="javascript:this.src='../images/movie_error.png'"/>
                                    </td>
                                    <td style="padding-right: 10px">
                                        <img class="next-page" id="movie-collect-next-page" src="/images/nextPage.png"
                                             alt="next page" width="20px" height="20px"/>
                                    </td>
                                </tr>
                            </table></span>
                <%}%>
            </h2>
            <%if(((List)request.getAttribute("preferMoviewList")).size()!=0){%>
            <table cellspacing="15px" class="collected-movie-table">
                <tr>
                    <%int desIndex=Math.min(4,((List)request.getAttribute("preferMoviewList")).size());
                        for(int index=0;index<desIndex;index++){%>
                    <td>
                        <div style="max-width: 130px;max-height: 200px">
                            <a href="/movie/j<%=((MovieShowVO)((List)request.getAttribute("preferMoviewList")).get(index)).getId()%>">
                                <img src="<%=((MovieShowVO)((List)request.getAttribute("preferMoviewList")).get(index)).getPoster()%>"
                                     alt="movie pic" width="126" height="180" onerror="javascript:this.src='../images/movie_error.png'"/>
                            <p><%=((MovieShowVO)((List)request.getAttribute("preferMoviewList")).get(index)).getName()%></p></a>
                        </div>
                    </td>
                    <%}%>
                    <%--<td>
                        <div>
                            <a href="">
                                <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                                <p>movie name</p></a>
                        </div>
                    </td>--%>
                </tr>
            </table>
            <%}else{%>
            <h3 style="font-weight: 500" align="center">╮(￣▽￣)╭ you haven't collected movie yet.</h3>
            <%}%>
        </div>
        <br/>
        <div class="reviews">
            <h2 class="little-title" style="margin-bottom: -20px">Reviews
                <span class="tip-number">( <a href="/user/j${userID}/reviews"><span id="reviews-number">${reviewsNum}</span> in total </a>)
                </span>
            </h2>
            <%--<div id="reviews-type">
                <label class="selected">all reviews</label>
                <label>IMDB's</label>
                <label>Doban's</label>
                <label>Juice's</label>
            </div>--%>
            <hr color="lightgrey" size="1px" width="590px" style="float:left"/>
            <br/><br/>
            <table class="show-reviews">
                <%
                    int showReviewsSize =(int)request.getAttribute("showReviewsSize");
                    for(int index=0;index<showReviewsSize;index++){
                        ReviewVO reviewVO=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index);
                %>
                <tr>
                    <td>
                        <div class="review-piece">
                            <div class="movie-pic">
                                <a href="/movie/j<%=((List)request.getAttribute("movieIDList")).get(index)%>">
                                    <img src="<%=((List)request.getAttribute("moviePosterList")).get(index)%>" alt="movie pic" width="91" height="130" onerror="javascript:this.src='../images/movie_error.png'"/>
                                </a>
                            </div>
                            <p style="font-size: 17px;font-weight: 600;margin-top:10px;margin-bottom: 0px">
                                <a href="" class="review-title"><%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getSummary()%></a></p>
                            <p style="font-style: oblique;font-size: 12pt;margin-top:10px;margin-bottom: 0px">
                                <a href="/user/j<%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getUserID()%>/home" class="user-name">
                                    <%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getUserName()%></a>
                                <span style="white-space: pre">  review:  </span><a class="movie-name"
                                        href="/movie/j<%=((List)request.getAttribute("movieIDList")).get(index)%>">
                                    <%=((List)request.getAttribute("movieNameList")).get(index)%></a></p>
                            <%--<p style="font-style: italic; font-size: 10pt">
                                <span class="helpful-num">${showReviews.list.get(index).helpfulness}</span> out of
                                <span class="helpness-num">${showReviews.list.get(index).allVotes}</span> users found this review helpful</p>--%>
                            <p class="review-time" style="margin-top:10px;margin-bottom: 10px"><%=((List)request.getAttribute("reviewTimeList")).get(index)%>
                                <label> from <a href="/"><%=((List)request.getAttribute("userTypeList")).get(index)%></a></label>
                            </p>
                            <div>
                                <label style="white-space: pre">rate:</label>
                                <img class="rate-pic" src="<%=((List)request.getAttribute("ratePicList")).get(index)%>" alt="rate image"
                                     width="135.7px" height="20px">
                            </div>
                            <p class="review-content" style="margin-top: 10px;margin-bottom: 10px">
                                <%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getReview()%></p>
                            <div class="like-div">
                                <%if(reviewVO.isHasThumbsup()){%>
                                <img class="like-pic selected" alt="like" src="/images/like-click.png" width="20px"/>
                                <%}else{%>
                                <img class="like-pic" alt="like" src="/images/like.png" width="20px"/>
                                <%}%>
                                <span class="like-num" name="unclicked" style="padding-left: 10px">10</span>
                            </div>
                        </div>
                    </td>
                </tr>
                <%}%>
                <%--<tr>
                    <td>
                        <div class="review-piece">
                            <div class="movie-pic">
                                <a href="">
                                    <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                                </a>
                            </div>
                            <p style="font-size: 17px;font-weight: 600"><a>review title</a></p>
                            <p style="font-style: oblique;font-size: 12pt"><a>user name</a><span
                                    style="white-space: pre">  review:  <a>movie name</a></span></p>
                            <p style="font-style: italic; font-size: 10pt"><span
                                    class="helpful-num">10</span> out of
                                <span class="helpness-num">20</span> users found this review helpful</p>
                            <div>
                                <span style="white-space: pre">rate:</span>
                                <img class="rate-pic" src="/images/rate5.0.png" alt="rate image"
                                     width="135.7px" height="20px">
                            </div>
                            <br/>
                            <p class="review-content">review content.....</p>
                            <span class="review-time">2017-05-12 13:04</span>
                            <label> from <a href>IMDB</a></label>
                            <div class="like-div">
                                <img class="like-pic" alt="like" src="/images/like.png" width="20px"
                                     height="20px"/>
                                <span class="like-num" name="unclicked" style="padding-left: 10px">10</span>
                            </div>
                        </div>
                    </td>
                </tr>--%>
            </table>
            <%if((int)request.getAttribute("reviewsNum")==0){%>
                <h3 align="center" style="font-weight: 500">╮(￣▽￣)╭ you have no reviews yet.</h3>
            <%}else{
                if((int)request.getAttribute("reviewsNum")>(int)request.getAttribute("showReviewsSize")){%>
                <button class="show-more" style="width: 585px;height: 40px;font-size: 20px"
                        onclick="javascript:window.location.href='/user/j${userID}/reviews'">show more&nbsp
                    <img src="/images/showmore.png" alt="show more" ${showReviewsSize}width="18px" height="18px">
                </button>
                <%}else{%>
                <h3 align="center" style="font-weight: 500; margin-top: 0px">╮(￣▽￣)╭ no more reviews.</h3>
                <%}%>
            <%}%>
        </div>
    </div>
</div>
<script>
    <%--$(document).ready(function(){
        <%for(int index=0;index<showReviewsSize;index++){%>
            $("#user-homePage").find(".movie-pic img").eq(<%=index%>).attr("src", <%=((List)request.getAttribute("moviePosterList")).get(index)%>);
            $("#user-homePage").find(".movie-pic a").eq(<%=index%>).attr("href","/movie/j"+<%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getMovieID()%>);
            $("#user-homePage").find(".review-title").eq(<%=index%>).text(<%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getSummary()%>);
            $("#user-homePage").find(".user-name").eq(<%=index%>).text(<%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getUserName()%>);
            $("#user-homePage").find(".user-name").eq(<%=index%>).attr("href","/user/j"+<%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getUserID()%>);
            $("#user-homePage").find(".movie-name").eq(<%=index%>).text(<%=((List)request.getAttribute("movieNameList")).get(index)%>);
            $("#user-homePage").find(".movie-name").eq(<%=index%>).attr("href","/movie/j"+<%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getMovieID()%>);
            $("#user-homePage").find(".review-time").eq(<%=index%>).text(<%=((List)request.getAttribute("reviewTimeList")).get(index)%>);
            $("#user-homePage").find(".rate-pic").eq(<%=index%>).attr("src",<%=((List)request.getAttribute("ratePicList")).get(index)%>);
            $("#user-homePage").find(".review-content").eq(<%=index%>).text(<%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getReview()%>);
        <%}%>
    });--%>
    var userID=${userID};
    var watchedTotalPage=${watchedMoviePageSize};
    var collectedTotalPage=${preferMoviePageSize};

</script>
<script src="/js/user.js"></script>
<script src="/js/main.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="/js/particles/particles.min.js"></script>
<script src="/js/particles/app.js"></script>
</body>
</html>