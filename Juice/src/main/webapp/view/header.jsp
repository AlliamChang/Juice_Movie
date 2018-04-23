<%--
  Created by IntelliJ IDEA.
  User: I Like Milk
  Date: 2017/4/28
  Time: 13:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="cn.cseiii.service.impl.UserServiceImpl" %>
<%@ page import="cn.cseiii.util.Encode" %>
<%@ page import="cn.cseiii.po.UserPO" %>
<%
    String name = (String)session.getAttribute("name");
    Integer id = (Integer)session.getAttribute("id");
    if (id == null) {
        Cookie[] cookies = request.getCookies();
        String userInfo = null, token = null;
        if (cookies != null)
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals("user"))
                    userInfo = URLDecoder.decode(cookie.getValue(), "UTF-8");
                else if (cookie.getName().equals("token"))
                    token = URLDecoder.decode(cookie.getValue(), "UTF-8");
            }
        if (userInfo != null && token != null) {
            String newToken = Encode.getRandomToken(Encode.TOKEN_LEN);
            UserPO userPO = new UserServiceImpl().updateToken(
                    Integer.parseInt(Encode.xorEncode(userInfo)), token, newToken);
            if (userPO != null) {
                id = userPO.getId();
                session.setAttribute("id", id);
                name = userPO.getName();
                session.setAttribute("name", name);
%>
                <script>
                    $.cookie('token', '<%=newToken%>', {expires: 7, path: '/'});
                </script>
<%
            } else {
%>
                <script>
                    $.removeCookie('user');
                    $.removeCookie('token');
                </script>
<%
            }
        }
    }
%>
<div class="stupid"></div>
<header>
    <div id="logo"><a href="/"><img src="/images/logo.png" alt="logo" width="65"></a></div>
    <ul class="nav-search">
        <li>
            <nav id="nav-wrap">
                <div class="nav-term"><a href="/movie">MOVIE.</a></div>
                <div class="nav-term"><a href="/tv-show">TV SHOW.</a></div>
                <div class="nav-term"><a href="/movielist">SHEET.</a></div>
                <div class="nav-term"><a href="/rank">RANK.</a></div>
                <div class="nav-term"><a href="/explore">EXPLORE.</a></div>
                <div class="nav-term"><a href="/statistics">STATISTICS.</a></div>
            </nav>
        </li>
        <li class="search-row">
            <input type="search" placeholder="Search on Juice..." name="search_text" class="text-line search-line" id="search-box">
            <a href="javascript:void(0);"><img alt="magnifier" src="/images/search.png" height="20px" id="magnifier"></a>
            <img alt="guidance" src="/images/guidance.png" height="25px" id="guidance">
        </li>
    </ul>
</header>

<% if (id == null) { %>
<div class="user-tool-position log-in-btn round">
    <div class="log-in-btn-back round">
        <span class="tip-text empty">Hey, there's nothing!</span>
        <span class="tip-text wrong-email">Unregistered！</span>
        <span class="tip-text wrong-password">Wrong password!</span>
        <div class="close-button"></div>
        <form action="" method="post" id="login-form">
            <span class="input-wrap email-wrap"><input type="text" class="text-line email-line" name="email" placeholder="Email"></span>
            <span class="input-wrap pw-wrap"><input type="password" class="text-line password-line" name="password" placeholder="Password"></span>
        </form>
        <div class="remember-forgot">
            <label><input type="checkbox" name="remember_me" checked="checked">Remember me</label>
            <span> · </span>
            <a href="/password_reset">Forgot password?</a>
        </div>
        <button class="yes round">Sign in</button>
        <hr class="in-up-line"/>
        <p>Don't have an account? <a href="/join">Sign up»</a></p>
    </div>
    <div class="log-in-btn-front round">Sign in/up</div>
</div>
<%} else {%>
<div class="user-tool-position avatar">
    <a href="javascript:;" id="profile-btn"><img src="/images/default_avatar.png" alt="default avatar" height="40px"></a>
    <div class="drop-down round">
        <div class="avatar-wrap"><a href="/user/j${id}/home"><img src="/images/${id}.jpg" height="100px" onerror="this.src='/images/default.jpg'; this.alt='no avatar'"></a></div>
        <p>${name}</p>
        <a href="/user/j${id}/movielist">Sheet.</a>
        <a href="/password_reset">Settings and privacy.</a>
        <span class="separator"></span>
        <a href="javascript:;" id="log-out-btn">Log out.</a>
    </div>
</div>
<% } %>

<div class="shade"></div>
<div class="usurp"></div>
<a href="javascript:void($('body').animate({scrollTop: 0}, 'slow'))" class="go-up"><img src="/images/go_up.png" height="40px"></a>
<script>
    $(function () {
        var index = <%=request.getParameter("index")%>;
        if (index !== -1)
            $("#nav-wrap a").eq(index).addClass("current");
    });
</script>