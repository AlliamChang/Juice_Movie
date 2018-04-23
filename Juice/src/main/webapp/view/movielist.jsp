<%@ page import="cn.cseiii.model.Page" %>
<%@ page import="cn.cseiii.model.FilmMakerVO" %>
<%@ page import="cn.cseiii.model.MovieSheetVO" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.cseiii.model.UserVO" %><%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/5/28
  Time: 9:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Movielist</title>
    <link rel="shortcut icon" href="/images/favicon.ico">
    <link rel="bookmark" href="/images/favicon.ico">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/animate.min.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/jquery.cookie.js"></script>
    <script src="/js/jquery.waypoints.min.js"></script>
    <link href="/css/movielist.css" rel="stylesheet">
</head>
<body>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="2"/>
    </jsp:include>
    <div id="movielist-recommand" style="width: 900px">
        <label style="font-size: 22px;font-weight:600">you may like: </label>
        <%--
        <div id="have-a-change" style="float:right">
            <a href=""><img src="/images/change.png" alt="change-pic" width="20px"/>have a change</a>
        </div>--%>
        <br/><br/>
        <%
            if(request.getAttribute("recommandMovieSheetVOList")!=null){
            for(int index=0;index<((Page)request.getAttribute("recommandMovieSheetVOList")).getList().size();index++){
                MovieSheetVO recommandMovieSheetVO=(MovieSheetVO)((Page)request.getAttribute("recommandMovieSheetVOList")).getList().get(index);
        %>
        <div class="recommand-movielist-piece">
            <a href="/movielist/j<%=recommandMovieSheetVO.getId()%>"
               class="movielist-name">
                <%=recommandMovieSheetVO.getMovieSheetName()%> </a>
            <div class="collect-div">
                <%if(recommandMovieSheetVO.isHasCollected()){%>
                <img class="collect-pic selected" alt="like" src="/images/collected.png" width="22px"/>
                <%}else{%>
                <img class="collect-pic" alt="like" src="/images/uncollected.png" width="22px"/>
                <%}%>
                <span class="collect-num" style="padding-left: 10px"><%=recommandMovieSheetVO.getNumOfCollectors()%></span>
            </div>
            <br/>
            <label style="color: grey">by &nbsp</label>
            <%UserVO userVO=(UserVO)((List)request.getAttribute("recommandOwnerInfoList")).get(index);%>
            <a href="/user/j<%=userVO.getUserID()%>/home" class="user-name">
                <img class="userImage" alt="user-pic" src="<%=userVO.getImgPath()%>" width="30px" onerror="javascript:this.src='../images/default_avatar.png'"/>
                <%=userVO.getUserName()%></a>
            <label style="white-space:pre; color: grey">     create at: </label><label class="create-time"><%=((List)request.getAttribute("recommandCreateDateList")).get(index)%></label>
            <label style="white-space: pre;color: grey">     update at: </label><label class="update-time"><%=((List)request.getAttribute("recommandUpdateDateList")).get(index)%></label>
            <p class="sheetDescription" style="margin-top: 10px"><%=recommandMovieSheetVO.getSheetDescription()%></p>
        </div>
        <br/>
        <%}}else{%>
        <h3 style="font-weight: 500" align="center">╮(￣▽￣)╭you have to log in first.</h3>
        <%}%>
        <%--<div class="recommand-movielist-piece">
            <a href="" style="font-size: 20px;font-weight: 600;text-decoration: none;color: black">
                movielist name </a>
            <div class="collect-div">
                <img class="collect-pic" alt="like" src="/images/uncollected.png" width="22px"/>
                <span class="collect-num" style="padding-left: 10px">10</span>
            </div>
            <br/>
            <label style="color: grey">by &nbsp</label>
            <a href="" class="user-name"><img alt="user-pic" src="/images/user.jpg" width="30px"/>user name</a>
            <label style="white-space:pre; color: grey">     create at: </label><label class="create-time">2017-05-12 13:04</label>
            <label style="white-space: pre;color: grey">     update at: </label><label class="create-time">2017-05-12 13:04</label>
            <p style="margin-top: 10px">description of movielist.</p>
        </div>--%>
    </div>
    <br/>
    <div id="movielist-rank" style="width: 900px">
        <h2>ranking movielist</h2>
        <div id="heat-new-choose">
            <span class="selected">
                <img src="/images/sort.png" alt="sort image" width="20px"/>
                <label name="selected" class="selected">sort by heat</label>
            </span>
            <span>
                <img src="/images/sort.png" alt="sort image" width="20px"/>
                <label>sort by latest</label>
            </span>
        </div>
        <hr color="lightgrey" size="1px" width="900px"/>
        <div id="movielist-div">
            <br/>
            <%
                for(int index=0;index<((Page)request.getAttribute("movieSheetSortByHeat")).getList().size();index++){
                MovieSheetVO movieSheetVO=(MovieSheetVO)((Page)request.getAttribute("movieSheetSortByHeat")).getList().get(index);
            %>
            <div class="rank-movielist-piece">
                <a href="/movielist/j<%=movieSheetVO.getId()%>"
                   class="movielist-name">
                    <%=movieSheetVO.getMovieSheetName()%> </a>
                <div class="collect-div">
                    <%--<div class="is-Owner">--%>
                        <%--<span class="collector-num"><%=movieSheetVO.getNumOfCollectors()%></span>--%>
                        <%--<span> collectors</span>--%>
                    <%--</div>--%>
                    <%--<div class="not-Owner">--%>
                        <%if(movieSheetVO.isHasCollected()){%>
                        <img class="collect-pic selected" alt="like" src="/images/collected.png" width="22px"/>
                        <%}else{%>
                        <img class="collect-pic" alt="like" src="/images/uncollected.png" width="22px"/>
                        <%}%>
                        <span class="collect-num" style="padding-left: 10px"><%=movieSheetVO.getNumOfCollectors()%></span>
                    <%--</div>--%>
                </div>
                <br/>
                <label style="color: grey">by &nbsp</label>
                <%UserVO userVO=(UserVO)((List)request.getAttribute("ownerInfoList")).get(index);%>
                <a href="/user/j<%=userVO.getUserID()%>/home" class="user-name">
                    <img class="userImage" alt="user-pic" src="<%=userVO.getImgPath()%>" width="30px" onerror="javascript:this.src='../images/default_avatar.png'"/>
                    <%=userVO.getUserName()%></a>
                <label style="white-space:pre; color: grey">     create at: </label><label class="create-time"><%=((List)request.getAttribute("createDateList")).get(index)%></label>
                <label style="white-space: pre;color: grey">     update at: </label><label class="update-time"><%=((List)request.getAttribute("updateDateList")).get(index)%></label>
                <p class="sheetDescription" style="margin-top: 10px"><%=movieSheetVO.getSheetDescription()%></p>
            </div>
            <br/>
            <%}%>
        </div>
        <%int totalPage=((Page)request.getAttribute("movieSheetSortByHeat")).getTotalSize();%>
        <p><div class="scott">
        <span id="last-page" class="disabled"> < </span>
        <%for(int index=0;index<totalPage;index++){%>
        <span class="movielist-page-index"><%=index+1%></span>
        <%}%>
        <span id="next-page"> > </span>
        </div></p>
    </div>
</div>
<script>
    //收藏过
    var totalPageSize=${movieSheetSortByHeat.totalSize};
    $(".scott .movielist-page-index").eq(0).addClass("current");
    <%if(totalPage==1){%>
        $(".scott").hide();
    <%}%>
    <%--<%for(int index=0;index<((Page)request.getAttribute("movieSheetSortByHeat")).getList().size();index++){%>)
    var i=<%=index%>;
    <%if((Boolean)((List)request.getAttribute("isOwner")).get(index)){%>
        $("#movielist-rank .rank-movielist-piece .collect-div").eq(i).find(".is-Owner").show();
        $("#movielist-rank .rank-movielist-piece .collect-div").eq(i).find(".not-Owner").hide();
    <%}else{%>
        $("#movielist-rank .rank-movielist-piece .collect-div").eq(i).find(".is-Owner").hide();
        $("#movielist-rank .rank-movielist-piece .collect-div").eq(i).find(".not-Owner").show();
        <%if((Boolean)((List)request.getAttribute("isCollected")).get(index)){%>
            $("#movielist-rank .rank-movielist-piece .collect-div").eq(i)
            .find(".collect-pic").addClass("selected").attr("src","/images/collected.png");
        <%}%>
    <%}%>
    <%}%>--%>
</script>
<script src="/js/animation.js"></script>
<script src="/js/movielist.js"></script>
<script src="/js/main.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="/js/particles/particles.min.js"></script>
<script src="/js/particles/app.js"></script>
</body>
</html>