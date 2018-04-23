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
    <title>Sign up</title>
    <link rel="shortcut icon" href="../images/favicon.ico">
    <link rel="bookmark" href="../images/favicon.ico">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/sign-up.css">
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

    <div class="join-card">
        <h1>Join Juice Movie</h1>
        <div class="form-wrapper">
            <form autocomplete="false" method="post" action="/user/register">
                <div class="input-wrapper">
                    <input type="text" name="email" id="email">
                    <span class="label">Email</span>
                    <span class="line"></span>
                    <div class="tip-icon"></div>
                    <div class="tip"></div>
                </div>
                <div class="input-wrapper">
                    <input type="text" name="name" id="name">
                    <span class="label">Name</span>
                    <span class="line"></span>
                    <div class="tip-icon"></div>
                    <div class="tip"></div>
                </div>
                <div class="input-wrapper">
                    <input type="password" name="password" id="password">
                    <span class="label">Password</span>
                    <span class="line"></span>
                    <div class="tip-icon"></div>
                    <div class="tip"></div>
                </div>
                <div class="submit-wrapper">
                    <input type="submit" value="Sign Up" disabled>
                </div>
            </form>
        </div>
    </div>


</div>
<script src="../js/main.js"></script>
<script src="../js/sign-up.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="../js/particles/particles.min.js"></script>
<script src="../js/particles/app.js"></script>
</body>
</html>
