<%--
  Created by IntelliJ IDEA.
  User: I Like Milk
  Date: 2017/6/6
  Time: 0:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Statistics</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/animate.min.css">
    <link rel="stylesheet" href="../css/statistics.css">
    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/jquery.cookie.js"></script>
    <script src="../js/jquery.waypoints.min.js"></script>
</head>
<body>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="5"/>
    </jsp:include>

    <h2 class="title">Here are some interesting charts. ￣ω￣=</h2>
    <ul class="chart-descriptions">
        <li>Do you want to know which genre the world's most beloved? click <a href="javascript:;"><strong>here</strong></a></li>
        <li>Do you want to know which country like filming most? click <a href="javascript:;"><strong>here</strong></a></li>
        <li>...</li>
    </ul>

</div>

<div class="s-shade">
    <span class="close-button">X</span>
    <div class="chart fir"></div>
    <div class="tab">
        <input type="radio" name="site" value="0" id="imdb" checked><label for="imdb">IMDb</label>
        <input type="radio" name="site" value="1" id="douban"><label for="douban">Douban</label>
    </div>
</div>
<div class="s-shade">
    <span class="close-button">X</span>
    <div class="chart sec"></div>
</div>
<script src="../js/main.js"></script>
<script src="../js/echarts.min.js"></script>
<script src="../js/world.js"></script>
<script src="../js/dark.js"></script>
<script src="../js/statistics.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="../js/particles/particles.min.js"></script>
<script src="../js/particles/app.js"></script>
</body>
</html>
