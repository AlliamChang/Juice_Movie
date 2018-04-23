<%--
  Created by IntelliJ IDEA.
  User: I Like Milk
  Date: 2017/4/27
  Time: 14:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Juice Movie</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/animate.min.css">
    <link rel="stylesheet" href="../css/index.css">
    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/jquery.cookie.js"></script>
    <script src="../js/jquery.waypoints.min.js"></script>
</head>
<body data-hidden>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="-1"/>
    </jsp:include>

    <section class="above">
        <div class="sec-content">
            <h1>Now Playing</h1>
            <div id="graph"></div>
            <div id="slider"><ul></ul></div>
            <div id="scroll-down">
                <img src="../images/scroll.png" height="32px">
                Scroll down ↓
            </div>
        </div>
    </section>

    <section class="below hidden">
        <% if (session.getAttribute("id") != null) { %>
        <div class="recommendation">
            <h1><span>BET YOU LIKE</span></h1>
            <div class="movie-slider">
                <ul></ul>
                <div class="slide-ctrl">
                    <span class="dot active" data-index="0"></span>
                    <span class="dot" data-index="1"></span>
                    <span class="dot" data-index="2"></span>
                    <span class="dot" data-index="3"></span>
                </div>
            </div>
        </div>
        <% } %>
        <div class="hottest-movies">
            <h1><span>HOTTEST MOVIES</span><a href="/movie">More</a></h1>
            <div class="movie-slider double">
                <ul></ul>
                <div class="slide-ctrl">
                    <span class="dot active" data-index="0"></span>
                    <span class="dot" data-index="1"></span>
                    <span class="dot" data-index="2"></span>
                    <span class="dot" data-index="3"></span>
                </div>
            </div>
        </div>
        <div class="hottest-tv-shows">
            <h1><span>HOTTEST TV SHOWS</span><a href="/tv-show">More</a></h1>
            <div class="movie-slider double">
                <ul></ul>
                <div class="slide-ctrl">
                    <span class="dot active" data-index="0"></span>
                    <span class="dot" data-index="1"></span>
                    <span class="dot" data-index="2"></span>
                    <span class="dot" data-index="3"></span>
                </div>
            </div>
        </div>
    </section>
</div>
<script src="../js/main.js"></script>
<script src="../js/echarts.min.js"></script>
<script src="../js/index.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="../js/particles/particles.min.js"></script>
<script src="../js/particles/app.js"></script>
</body>
</html>
