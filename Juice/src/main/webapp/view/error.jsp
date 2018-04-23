<%--
  Created by IntelliJ IDEA.
  User: I Like Milk
  Date: 2017/6/10
  Time: 23:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Unknown error</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
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

    <h1>An unknown error occured, have another try please. orz</h1>
</div>

<script src="../js/main.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="../js/particles/particles.min.js"></script>
<script src="../js/particles/app.js"></script>
</body>
</html>