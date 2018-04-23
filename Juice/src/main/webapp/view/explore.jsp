<%--
  Created by IntelliJ IDEA.
  User: I Like Milk
  Date: 2017/5/30
  Time: 12:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Explore</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/animate.min.css">
    <link rel="stylesheet" href="../css/explore.css">
    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/jquery.cookie.js"></script>
    <script src="../js/jquery.waypoints.min.js"></script>
</head>
<body>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="4"/>
    </jsp:include>
    <input type="hidden" name="size" value="5">
    <div class="cards-wp">
        <div class="card preload">
            <div class="loader line-scale-pulse-out-rapid">
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
            </div>
        </div>
        <div class="card" data-show>
            <div class="loader line-scale-pulse-out-rapid">
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
            </div>
        </div>
    </div>


    <div class="opt-bar">
        <label id="like-btn" title="I like it.">
            <input type="checkbox" value="1" name="like">
            <img alt="like" src="../images/heart.png" class="empty-heart animated infinite pulse">
            <img alt="like" src="../images/heart_full.png" class="full-heart animated infinite pulse">
        </label>
        <button id="dislike-btn" class="dislike" title="Goodbye!"><img alt="dislike" src="../images/bin_top.png" class="animated infinite light-swing"></button>
        <button id="next-btn" title="Next."><img alt="next" src="../images/next_left.png" class="animated infinite shake-left"><img alt="next" src="../images/next_right.png" class="animated infinite shake-right"></button>
    </div>
</div>

<script src="../js/main.js"></script>
<script src="../js/explore.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="../js/particles/particles.min.js"></script>
<script src="../js/particles/app.js"></script>
</body>
</html>
