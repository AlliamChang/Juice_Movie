<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/5/11
  Time: 16:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User</title>
</head>
<body>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="-1"/>
    </jsp:include>
    <div style="width: 900px">
        <div class="content-left">
            <div id="user-profile">
                <div id="user-pic">
                    <img src="${userImage}" width="100px" height="100px"
                         alt="userpic" style="cursor:pointer;padding-bottom: 10px;padding-right: 20px" onerror="javascript:this.src='../images/default_avatar.png'"/>
                </div>
                <div  style="pointer-events: none;" id="user-name"><p>${userName}</p></div>
                <table id="user-nav" cellspacing="30px">
                    <tr>
                        <td><a href="/user/j${userID}/home">home</a></td>
                        <div id="movie-watched-jump"></div>
                        <td><a href="/user/j${userID}/home#movie-watched-jump">watched</a></td>
                        <td><a href="/user/j${userID}/home#movie-collect-jump">collected</a></td>
                        <td><a href="/user/j${userID}/movielist">movielist</a></td>
                        <td><a href="/user/j${userID}/reviews">reviews</a></td>
                        <td><a href="/user/j${userID}/home">statistics</a></td>
                    </tr>
                </table>
            </div>
            <br/>
        </div>
        <%--<div class="content-right">--%>
            <%--<table class="three-num">--%>
                <%--<tr>--%>
                    <%--<td>--%>
                        <%--<p class="three-num" id="followers-num"><a href="#user-followers-jump">5</a></p>--%>
                        <%--<p class="three-num-name"><a href="#user-followers-jump">followers</a></p>--%>
                    <%--</td>--%>
                    <%--<td>--%>
                        <%--<p class="three-num" id="following-num"><a href="#user-following-jump">5</a></p>--%>
                        <%--<p class="three-num-name"><a href="#user-following-jump">following</a></p>--%>
                    <%--</td>--%>
                    <%--<td>--%>
                        <%--<p class="three-num" id="reviews-num"><a href="/user/j${userID}/reviews">${reviewsNum}</a></p>--%>
                        <%--<p class="three-num-name"><a href="/user/j${userID}/reviews">reviews</a></p>--%>
                    <%--</td>--%>
                <%--</tr>--%>
            <%--</table>--%>
            <%--<br/>--%>
            <%--<div id="user-followers-jump"></div>--%>
            <%--<button id="change-info" class="round" onclick="window.location.href='/view/user-info.jsp'">Profile Settings</button>--%>
            <%--<br/><br/>--%>
            <%--<div id="user-followers">--%>
                <%--<div style="padding-left: 20px">--%>
                    <%--<h4>his/her followers:--%>
                        <%--<span class="tip-number">( <span--%>
                                <%--id="user-followers-num">6</span> in total )</span>--%>
                    <%--</h4>--%>
                    <%--<table>--%>
                        <%--<tr>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        <%--<tr>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                    <%--</table>--%>
                    <%--&lt;%&ndash;<button class="show-more">show more&nbsp&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<img src="/images/showmore.png" alt="show more" width="13px" height="13px"></button>&ndash;%&gt;--%>
                    <%--<div id="user-following-jump"></div>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<br/><br/>--%>
            <%--<div id="user-following">--%>
                <%--<div style="padding-left: 20px">--%>
                    <%--<h4>his/her following:--%>
                        <%--<span class="tip-number">( <a><span--%>
                                <%--id="user-following-num">6</span> in total </a>)</span>--%>
                    <%--</h4>--%>
                    <%--<table>--%>
                        <%--<tr>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        <%--<tr>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px" onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<a href=""><img src="/images/userImage.jpg" alt="follower" width="60px"--%>
                                                <%--height="60px"onerror="javascript:this.src='../images/default_avatar.png'"/>--%>
                                    <%--<p class="follower-name">user</p></a>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                    <%--</table>--%>
                    <%--&lt;%&ndash;<button class="show-more">show more&nbsp&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<img src="/images/showmore.png" alt="show more" width="13px" height="13px"></button>&ndash;%&gt;--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    </div>
</div>
<script>
    $(function () {
        var index = <%=request.getParameter("index")%>;
        $("#user-nav a").eq(index).addClass("selected");
    });
</script>

</body>
</html>
