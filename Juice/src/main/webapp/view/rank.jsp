<%--
  Created by IntelliJ IDEA.
  User: sparkler
  Date: 2017/5/29
  Time: PM7:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>rank</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/rank.css">
    <link rel="stylesheet" href="../css/animate.min.css">
    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/jquery.cookie.js"></script>
    <script src="../js/jquery.waypoints.min.js"></script>
</head>
<body>

<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="3"/>
    </jsp:include>
    <div class="ranking-list">
        <div class="content-left">
            <div class="choose-type">
                <table>
                    <tr>
                        <td>
                            <button class="rankType-but" id="0" name="selected" style="color: white;
                            background-color: cornflowerblue">
                                IMDb list
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button class="rankType-but" id="1" name="unselected">
                                DOUBAN list
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button class="rankType-but" id="2" name="unselected">
                                Rate difference list
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button class="rankType-but" id="3" name="unselected">
                                Best selling filmMaker list
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button class="rankType-but" id="4" name="unselected">
                                Top rate FilmMaker list
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button class="rankType-but" id="5" name="unselected">
                                Rate/box-office list
                            </button>
                        </td>
                    </tr>
                </table>

                <%--<hr   style="height:100px; layout-flow :vertical-ideographic "/>--%>
            </div>
        </div>
        <div class="content-right">
            <h1 class="rank-title">Opening this week</h1>
            <table class="rank-movies">
                <%
                    //        int showReviewsSize = (int) request.getAttribute("showReviewsSize");
                    for (int index = 0; index < 10; index++) {
                %>
                <tr>
                    <td>
                        <div class="movie-piece">
                            <div>
                                <a href="">
                                    <div class="movie-poster" style="float: left">
                                        <img class="movie-pic" src="../images/yourName.jpg" width="100px" onerror="javascript:this.src='../images/movie_error.png'"/>
                                    </div>
                                    <div class="basic-info">
                                        <span class="movie-name"
                                              style="font-size: 17px;font-weight: 600">movie name</span>
                                        <div class="detail">review content.....<br/>review content.....<br/>review
                                            content.....
                                        </div>
                                        <p class="rate-span">
                                            <span class="tip-rate">DOUBAN rate:</span>
                                            <span class="rate" id="rate-DOUBAN">4.0</span>
                                            &nbsp;&nbsp;&nbsp;
                                            <span class="tip-rate">IMDb rate:</span>
                                            <span class="rate" id="rate-IMDB">4.0</span>
                                        </p>
                                    </div>
                                </a>
                            </div>
                        </div>
                        <br/>
                    </td>
                </tr>
                <%}%>
            </table>

            <table class="rank-filmMaker">
                <%
                    //        int showReviewsSize = (int) request.getAttribute("showReviewsSize");
                    for (int index = 0; index < 10; index++) {
                %>
                <tr>
                    <td>
                        <div class="movie-piece">
                            <div>
                                <a href="">
                                    <div class="movie-poster" style="float: left">
                                        <img class="movie-pic" src="../images/yourName.jpg" width="100px" onerror="javascript:this.src='../images/default_avatar.png'"/>
                                    </div>
                                    <div class="basic-info">
                                        <span class="movie-name"
                                              style="font-size: 17px;font-weight: 600">movie name</span><br/><br/>
                                        <div class="detail">review content.....<br/>review content.....<br/>review
                                            content.....
                                        </div>
                                        <br/>
                                        <span><span class="rate-or-boxOffice"
                                                    style="color: grey">Average rate: </span><span class="average-rate"
                                                                                                   style="color: orangered"></span></span>
                                    </div>
                                </a>
                            </div>
                        </div>
                        <br/>
                    </td>
                </tr>
                <%}%>
            </table>

            <%--<table class="rank-boxOffice">--%>
                <%--<%--%>
                    <%--//        int showReviewsSize = (int) request.getAttribute("showReviewsSize");--%>
                    <%--for (int index = 0; index < 10; index++) {--%>
                <%--%>--%>
                <%--<tr>--%>
                    <%--<td>--%>
                        <%--<div class="movie-piece">--%>
                            <%--<div>--%>
                                <%--<a href="">--%>
                                    <%--<div class="movie-poster" style="float: left">--%>
                                        <%--<img class="movie-pic" src="../images/yourName.jpg" width="100px"/>--%>
                                    <%--</div>--%>
                                    <%--<table class="basic-info">--%>
                                        <%--<tr>--%>
                                            <%--<td>--%>
                                                <%--<p class="movie-name" style="font-size: 17px;font-weight: 600">movie--%>
                                                    <%--name</p>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>--%>
                                                <%--<div class="basic-info">review content.....<br/>review content.....<br/>review--%>
                                                    <%--content.....--%>
                                                <%--</div>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>--%>
                                                <%--<p>--%>
                                                    <%--<img class="rate-pic" src="../images/rate4.0.png" alt="rate image"--%>
                                                         <%--width="120px">--%>
                                                    <%--<span class="rate">4.0</span>--%>
                                                <%--</p>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>
                                    <%--</table>--%>
                                <%--</a>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<br/>--%>
                    <%--</td>--%>
                <%--</tr>--%>
                <%--<%}%>--%>
            <%--</table>--%>
            <p class="tip-noComments" align="left"></p>
            <p>
            <div class="scott">
                <span id="last-page" class="disabled"> < </span>
                <%for (int index = 0; index < 10; index++) {%>
                <span class="reviews-page-index"><%=index + 1%></span>
                <%}%>
                <span id="next-page"> > </span>
            </div>
            </p>
        </div>
    </div>
</div>

<script src="../js/animation.js"></script>
<script src="../js/main.js"></script>
<script src="../js/rank.js"></script>
<script>
    //$("button#0.rankType-but").click();
    //    getRankListPage(0,0);
    $(".scott .reviews-page-index").eq(0).addClass("current");
    $("table.rank-movies").hide();
    $("table.rank-filmMaker").hide();
    $("table.rank-boxOffice").hide();
    $("div.scott").hide();
    getRankListPage(0, 0);
    $(".scott .reviews-page-index").eq(0).addClass("current");
</script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="../js/particles/particles.min.js"></script>
<script src="../js/particles/app.js"></script>
</body>
</html>
