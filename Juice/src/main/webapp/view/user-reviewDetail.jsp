<%@ page import="java.util.List" %>
<%@ page import="cn.cseiii.model.ReviewVO" %>
<%@ page import="cn.cseiii.model.Page" %>
<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/5/28
  Time: 9:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${userName}'s all reviews</title>
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
    <jsp:param name="index" value="4"/>
</jsp:include>
<div class="content">
<div id="reviews" style="width:600px;float:left">
    <h2 class="little-title">All Reviews
        <span class="tip-number">( <label><span id="reviews-number">10</span> in total </label>)
                </span>
    </h2>
    <br>
    <%--<div id="reviews-type">
        <label name="selected" class="selected">all reviews</label>
        <label>IMDB's</label>
        <label>Doban's</label>
        <label>Juice's</label>
    </div>--%>
    <div id="sort-div">
        <span class="selected">
            <img src="/images/sort.png" alt="sort image" width="20px"/>
            <label>sort by newest</label>
        </span>
        <span>
            <img src="/images/sort.png" alt="sort image" width="20px"/>
            <label>sort by helpful</label>
        </span>
    </div>
    <hr color="lightgrey" size="1px" width="590px" style="float:left"/>
    <br/><br/>
    <table class="reviews">
        <%
            int pageReviewsNum =(int)request.getAttribute("pageReviewsNum");
            for(int index=0;index<pageReviewsNum;index++){
                ReviewVO reviewVO=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index);
        %>
        <tr>
            <td style=" padding-bottom: 20px;">
                <div class="review-piece">
                    <div class="movie-pic">
                        <a href="/movie/j<%=((List)request.getAttribute("movieIDList")).get(index)%>">
                            <img src="<%=((List)request.getAttribute("moviePosterList")).get(index)%>" alt="movie pic" width="91" height="130" onerror="javascript:this.src='../images/movie_error.png'"/>
                        </a>
                    </div>
                    <p style="font-size: 17px;font-weight: 600;margin-top:10px;margin-bottom: 0px">
                        <label class="review-title">
                            <%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getSummary()%></label></p>
                    <p style="font-style: oblique;font-size: 12pt;margin-top:10px;margin-bottom: 0px">
                        <a href="/user/j<%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getUserID()%>/home" class="user-name">
                            <%=((Page<ReviewVO>)request.getAttribute("showReviews")).getList().get(index).getUserName()%></a>
                        <span style="white-space: pre">  review:  </span><a class="movie-name"
                                                                            href="/movie/j<%=((List)request.getAttribute("movieIDList")).get(index)%>">
                        <%=((List)request.getAttribute("movieNameList")).get(index)%></a></p>
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
                        <span class="like-num" style="padding-left: 10px">10</span>
                    </div>
                </div>
            </td>
        </tr>
        <%}%>
        <%--<tr>
            <td>
                <div class="review-piece">
                    <div class="movie-pic">
                        <a href="/movie/j319">
                            <img src="/images/movieImage.jpg" alt="movie pic" width="91" height="130"/>
                        </a>
                    </div>
                    <p style="font-size: 17px;font-weight: 600;margin-top:10px;margin-bottom: 0px">
                        <a href="" class="review-title">reviewTitle</a></p>
                    <p style="font-style: oblique;font-size: 12pt;margin-top:10px;margin-bottom: 0px">
                        <a href="/user/j1/home" class="user-name">userName</a>
                        <span style="white-space: pre">  review:  </span><a class="movie-name" href="/movie/j319">
                        movie Name</a></p>
                    <p class="review-time" style="margin-top:10px;margin-bottom: 10px">2017-12-12 11:04
                        <label> from <a href="/">Juice</a></label>
                    </p>
                    <div>
                        <label style="white-space: pre">rate:</label>
                        <img class="rate-pic" src="/images/rate5.0.png" alt="rate image"
                             width="135.7px" height="20px">
                    </div>
                    <p class="review-content" style="margin-top: 10px;margin-bottom: 10px">
                        review Content</p>
                    <div class="like-div">
                        <img class="like-pic" alt="like" src="/images/like.png" width="20px"
                             height="20px"/>
                        <span class="like-num" name="unclicked" style="padding-left: 10px">10</span>
                    </div>
                </div>
            </td>
        </tr>--%>
    </table>
    <br/>
    <p><div class="scott">
    <%int totalPage=((Page<ReviewVO>)request.getAttribute("showReviews")).getTotalSize();%>
    <span id="last-page" class="disabled"> < </span>
    <%for(int index=0;index<totalPage;index++){%>
    <span class="reviews-page-index"><%=index+1%></span>
    <%}%>
    <span id="next-page"> > </span>
    </div></p>
    <br/>
</div>
</div>
<script src="/js/user.js"></script>
<script>
    var userID = "${userID}";
    var totalPageNum = ${showReviews.totalSize};
//    var totalPageNum=9;
    var curPageSize="${pageReviewsNum}";
    $(".scott .reviews-page-index").eq(0).addClass("current");
    <%if(totalPage==1){%>
        $(".scott").hide();
    <%}%>
    <%--$(document).ready(function () {
        showPageIndexButton(0);
    });
    function showPageIndexButton(index) {//index=页数-1
        if($(".scott").length>0) {
            $(".scott").empty();
        }
        var lastPage=$("<span></span>").text(" < ").attr("id","last-page");
        if(index==0){//第一页把上一页设为disabled
            lastPage.addClass("disabled");
        }
        $(".scott").append(lastPage);
        if(totalPageNum<10){
            for(var i=0;i<totalPageNum;i++){
                var pageIndex=$("<span></span>").text(i+1);
                if(i==index){
                    pageIndex.addClass("current");
                }
                $(".scott").append(pageIndex);
            }
        }else{
            if(index<=4){
                for(var i=0;i<index+2;i++){
                    var pageIndex=$("<span></span>").text(i+1);
                    if(i==index){
                        pageIndex.addClass("current");
                    }
                    $(".scott").append(pageIndex);
                }
                $(".scott").append($("<label></label>").text(" ... "));
                for(var i=totalPageNum-2;i<totalPageNum;i++){
                    var pageIndex=$("<span></span>").text(i+1);
                    $(".scott").append(pageIndex);
                }
            }else if(index>=totalPageNum-5){
                for(var i=0;i<2;i++){
                    var pageIndex=$("<span></span>").text(i+1);
                    $(".scott").append(pageIndex);
                }
                $(".scott").append($("<label></label>").text(" ... "));
                for(var i=index-2;i<totalPageNum;i++){
                    var pageIndex=$("<span></span>").text(i+1);
                    if(i==index){
                        pageIndex.addClass("current");
                    }
                    $(".scott").append(pageIndex);
                }
            }else{
                for(var i=0;i<2;i++){
                    var pageIndex=$("<span></span>").text(i+1);
                    $(".scott").append(pageIndex);
                }
                $(".scott").append($("<label></label>").text(" ... "));
                for(var i=index-2;i<index+2;i++){
                    var pageIndex=$("<span></span>").text(i+1);
                    if(i==index){
                        pageIndex.addClass("current");
                    }
                    $(".scott").append(pageIndex);
                }
                $(".scott").append($("<label></label>").text(" ... "));
                for(var i=totalPageNum-2;i<totalPageNum;i++){
                    var pageIndex=$("<span></span>").text(i+1);
                    $(".scott").append(pageIndex);
                }
            }
        }
        var nextPage=$("<span></span>").text(" > ").attr("id","next-page");
        if(index==totalPageNum-1){//最后一页则设为disabled
            nextPage.addClass("disabled");
        }
        $(".scott").append(nextPage);

    }
    --%>
</script>
<script src="/js/animation.js"></script>
<script src="/js/main.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="/js/particles/particles.min.js"></script>
<script src="/js/particles/app.js"></script>
</body>
</html>
