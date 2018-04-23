<%--
  Created by IntelliJ IDEA.
  User: sparkler
  Date: 2017/6/10
  Time: AM12:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>search result=${keyword}</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/rank.css">
    <link rel="stylesheet" href="../css/searchResult.css">
    <link rel="stylesheet" href="../css/animate.min.css">
    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/jquery.cookie.js"></script>
    <script src="../js/jquery.waypoints.min.js"></script>
</head>
<body>

<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="-1"/>
    </jsp:include>

    <div class="movie-results" width="900px" style="margin-left: 100px">
        <table id="search-type">
            <tr>
                <td>
                    <button class="search-type-but" id="0" name="selected"
                            style="color: white; background-color: skyblue">Movies
                    </button>
                </td>
                <td>
                    <button class="search-type-but" id="1" name="unselected">Film makers
                    </button>
                </td>
                <td>
                    <button class="search-type-but" id="2" name="unselected">Movie sheets
                    </button>
                </td>
            </tr>
        </table>
        <hr color="lightgrey" size="1px" width="600px" align="left"/>
        <br/>
        <p class="searchResult">
        <h2 class="little-title">Search results<span class="tip-number">( <a><span
                id="movie-searchResult-num">...</span> in total </a>）</span>
        </h2>
        <table class="movie-result-list">
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
            <%
                }
            %>
        </table>

        <table class="filmMaker-result-list">
            <%
                //        int showReviewsSize = (int) request.getAttribute("showReviewsSize");
                for (int index = 0; index < 10; index++) {
            %>
            <tr>
                <td>
                    <div class="movie-piece">
                        <a href="">
                        <div class="movie-poster" style="float: left">
                            <img class="filmMaker-pic" src="../images/yourName.jpg" width="100px" onerror="javascript:this.src='../images/default_avatar.png'"/></div>
                        <div class="basic-info">
                            <span class="filmMaker-name" style="font-size: 17px;font-weight: 600">movie name</span><br/><br/>
                            <div class="detail">review content.....<br/>review content.....<br/>review
                                content.....
                            </div>
                        </div>
                        </a>
                    </div>
                    <br/>
                </td>
            </tr>
            <%
                }
            %>
        </table>

        <table class="movieSheet-result-list">
            <%
                //        int showReviewsSize = (int) request.getAttribute("showReviewsSize");
                for (int index = 0; index < 10; index++) {
            %>
            <tr>
                <td>

                    <div class="movie-piece">
                        <a href="">
                            <h3 class="movieSheet-name" style="font-size: 20px;font-weight: 600"></h3>
                            <br/><br/>
                            <p style="margin-top: 5px;margin-bottom: 0px">
                                <label style="color: grey"> create at: </label>
                                <label class="create-time"></label>
                                <label style="white-space: pre;color: grey"> update at: </label>
                                <label class="update-time"></label></p>
                            <p class="description" style="margin-top: 5px"></p>
                        </a>
                    </div>
                    <br/>

                </td>
            </tr>
            <%
                }
            %>
        </table>

        <p class="tip-noComments" align="left"></p><br/>

        <div class="turn-page" align="center" style="margin-right: 150px">
            <p>
                <img class="last-page" id="movie-searchResult-last-page" src="../images/lastPage.png"
                     alt="last page" width="20px" height="20px"/>&nbsp;&nbsp;&nbsp;&nbsp;
                <span class="page-span" style="font-size: 22px"><span id="movie-searchResult-page-index">1</span>/<span
                        id="movie-searchResult-total-page">10</span></span>&nbsp;&nbsp;&nbsp;&nbsp;
                <img class="next-page" id="movie-searchResult-next-page" src="../images/nextPage.png"
                     alt="next page" width="20px" height="20px"/>&nbsp;&nbsp;&nbsp;&nbsp;
                <%--<span>Turn to <input type="text" name="pageIndex" class="jump-page" id="page-index-jump"/></span>--%>
            </p>

        </div>
    </div>
</div>
</div>

<script src="../js/animation.js"></script>
<script src="../js/main.js"></script>
<script src="../js/searchResult.js"></script>

<script>
    var keyword = "${keyword}";
    for (var i = 0; i < 10; i++) {
        $("table.movie-result-list tr").eq(i).hide();
        $("table.filmMaker-result-list tr").eq(i).hide();
        $("table.movieSheet-result-list tr").eq(i).hide();
    }
    chooseSearchType(0);
    $(".last-page").hide();
    //    alert("hahaha");
</script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="../js/particles/particles.min.js"></script>
<script src="../js/particles/app.js"></script>
</body>
</html>
