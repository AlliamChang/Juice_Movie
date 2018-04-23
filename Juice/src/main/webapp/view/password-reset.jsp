<%--
  Created by IntelliJ IDEA.
  User: I Like Milk
  Date: 2017/6/12
  Time: 0:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Modify password</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/animate.min.css">
    <link rel="stylesheet" href="../css/sign-up.css">
    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/jquery.cookie.js"></script>
    <script src="../js/jquery.waypoints.min.js"></script>
</head>
<body>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="-1"/>
    </jsp:include>

    <div class="box">
    <div class="join-card">
        <h1 style="color: #000">Reset your password</h1>
        <p style="width: 300px; margin-left: auto; margin-right: auto">Enter your email address and we will send you a verification code to reset your password.</p>
        <div class="form-wrapper">
            <div class="input-wrapper">
                <input type="text" name="email" id="email">
                <span class="label">Email</span>
                <span class="line"></span>
                <div class="tip-icon"></div>
                <div class="tip"></div>
            </div>

            <div class="submit-wrapper">
                <input type="button" value="Send email" disabled>
            </div>
        </div>
    </div>
    </div>
</div>

<script src="../js/main.js"></script>
<script src="../js/pw-reset.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="../js/particles/particles.min.js"></script>
<script src="../js/particles/app.js"></script>
</body>
</html>