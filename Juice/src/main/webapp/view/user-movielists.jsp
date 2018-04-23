<%@ page import="java.util.List" %>
<%@ page import="cn.cseiii.model.MovieSheetVO" %>
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
    <title>${userName}'s movielists</title>
    <link rel="shortcut icon" href="/images/favicon.ico">
    <link rel="bookmark" href="/images/favicon.ico">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/animate.min.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/jquery.cookie.js"></script>
    <script src="/js/jquery.waypoints.min.js"></script>
    <link href="/css/user.css" rel="stylesheet">
    <script src="/js/user.js"></script>
</head>
<body>
<jsp:include page="user.jsp" flush="true">
    <jsp:param name="index" value="3"/>
</jsp:include>
<div class="content">
    <div id="user-movielists" style="width:600px;float:left">
        <h2>her/his movielists:
            <span class="tip-number">( <label><span id="create-movielist-number">${movieSheetVOList.size()}</span> created, <span
                    id="collected-movielist-number">${collectedSheetVOList.size()}</span> collected </label>)</span>
        </h2>
        <br/>
        <div id="create-collect-choose">
            <label name="selected" class="selected">created</label>
            <label>collected</label>
        </div>
        <hr color="lightgrey" size="1px" width="580px"/>
        <div id="create-movielist" class="movielist-tool-position round">
            <div class="close-button"></div>
            <span id="tip-title" class="tip-text">Hey, there's nothing!</span>
            <span id="tip-discription" class="tip-text">Hey, there's nothing!</span>
            <form action="" method="get" id="create-movielist-form">
                <span class="title">title: &nbsp;<input type="text" class="text-line title-line" name="title"
                                                        placeholder="title"></span>
                <br/>
                <span class="description">description: <br/>
                    <textarea class="description-textarea" name="description"
                              placeholder="description"></textarea></span>
            </form>
            <button class="create round">create</button>
        </div>
        <div id="delete-movielist" class="round">
            <p style="font-size: 18px">are you sure to delete this movielist? T﹏T </p>
            <button id="cancel-button" class="round" style="float: right">cancel</button>
            <button id="yes-button" class="round" style="float: right">yes</button>
        </div>
        <div id="background-shade"></div>
        <div id="movielist-div">
            <div id="created-movielist-div">
                <%if((Boolean)request.getAttribute("isOwner")){%>
                <a href="javascript:void(0)" class="add-movielist-div"
                   style="float:right;padding-right: 20px;cursor: hand">
                    <img alt="add" src="/images/addImage.png" style="position: relative;top:5px" width="22px"/>
                    create movielist</a>
                <%}%>
                <br/><br/>
                <% for (int index = 0; index < ((List) request.getAttribute("movieSheetVOList")).size(); index++) {
                    MovieSheetVO movieSheetVO = (MovieSheetVO) ((List) request.getAttribute("movieSheetVOList")).get(index);%>
                <div class="created-movielist-piece">
                    <a href="/movielist/j<%=((MovieSheetVO)((List)request.getAttribute("movieSheetVOList")).get(index)).getId()%>"
                       style="font-size: 20px;font-weight: 600">
                        <%=((MovieSheetVO) ((List) request.getAttribute("movieSheetVOList")).get(index)).getMovieSheetName()%>
                    </a>
                    <div class="collect-div">
                        <%if ((Boolean) request.getAttribute("isOwner")) {%>
                        <div class="is-Owner">
                            <span class="collector-num"><%=movieSheetVO.getNumOfCollectors()%></span>
                            <span> collectors</span>
                        </div>
                        <%} else {%>
                        <div class="not-Owner">
                            <%if (movieSheetVO.isHasCollected()) {%>
                            <img class="collect-pic selected" alt="like" src="/images/collected.png" width="22px"/>
                            <%} else {%>
                            <img class="collect-pic" alt="like" src="/images/uncollected.png" width="22px"/>
                            <%}%>
                            <span class="collect-num"
                                  style="padding-left: 10px"><%=movieSheetVO.getNumOfCollectors()%></span>
                        </div>
                        <%}%>
                    </div>
                    <br/>
                    <label style="color: grey">by &nbsp</label>
                    <a href="/user/j<%=((UserVO)((List)request.getAttribute("ownerInfoList")).get(index)).getUserID()%>/home">
                        <img class="user-pic"
                             src="<%=((UserVO)((List)request.getAttribute("ownerInfoList")).get(index)).getImgPath()%>"
                             width="30px" onerror="javascript:this.src='../images/default_avatar.png'"/>
                        <%=((UserVO) ((List) request.getAttribute("ownerInfoList")).get(index)).getUserName()%>
                    </a>
                    <br/>
                    <p style="margin-top: 5px;margin-bottom: 0px">
                        <label style="color: grey"> create at: </label><label class="create-time">
                        <%=((List) request.getAttribute("createDateList")).get(index)%>
                    </label>
                        <label style="white-space: pre;color: grey"> update at: </label><label class="update-time">
                        <%=((List) request.getAttribute("updateDateList")).get(index)%>
                    </label>
                    </p>
                    <p style="margin-top: 5px"><%=((MovieSheetVO) ((List) request.getAttribute("movieSheetVOList")).get(index)).getSheetDescription()%>
                    </p>
                    <%if((Boolean)request.getAttribute("isOwner")){%>
                    <a class="del-movielist-div" href="javascript:void(0)" style="float:right;padding-right: 20px">
                        <img alt="delete" src="/images/delete.png" style="position: relative;top:5px" width="22px"/>
                        <span>delete</span>
                    </a>
                    <%}%>
                </div>
                <br/>
                <%
                    }
                    if (((List) request.getAttribute("movieSheetVOList")).size() == 0) {
                %>
                <h3 style="font-weight: 500" align="center">╮(￣▽￣)╭ you haven't created movielist yet.</h3>
                <%}%>
            </div>
            <div id="collected-movielist-div">
                <br/>
                <%for (int index = 0; index < ((List) request.getAttribute("collectedSheetVOList")).size(); index++) {
                    MovieSheetVO collectedMovieSheetVO=(MovieSheetVO)((List)request.getAttribute("collectedSheetVOList")).get(index);%>
                <div class="collected-movielist-piece">
                    <a href="/movielist/j<%=((MovieSheetVO)((List)request.getAttribute("collectedSheetVOList")).get(index)).getId()%>"
                       style="font-size: 20px;font-weight: 600">
                        <%=((MovieSheetVO) ((List) request.getAttribute("collectedSheetVOList")).get(index)).getMovieSheetName()%>
                    </a>
                    <div class="collect-div">
                        <%if (collectedMovieSheetVO.isHasCollected()) {%>
                        <img class="collect-pic selected" alt="like" src="/images/collected.png" width="22px"/>
                        <%} else {%>
                        <img class="collect-pic" alt="like" src="/images/uncollected.png" width="22px"/>
                        <%}%>
                        <span class="collect-num" name="unclicked" style="padding-left: 10px">
                            <%=((MovieSheetVO) ((List) request.getAttribute("collectedSheetVOList")).get(index)).getNumOfCollectors()%></span>
                    </div>
                    <br/>
                    <label style="color: grey">by &nbsp</label>
                    <a href="/user/j<%=((UserVO)((List)request.getAttribute("collectedOwnerInfo")).get(index)).getUserID()%>/home">
                        <img class="user-pic"
                             src="<%=((UserVO)((List)request.getAttribute("collectedOwnerInfo")).get(index)).getImgPath()%>"
                             width="30px" onerror="javascript:this.src='../images/default_avatar.png'"/>
                        <%=((UserVO) ((List) request.getAttribute("collectedOwnerInfo")).get(index)).getUserName()%>
                    </a>
                    <br/>
                    <p style="margin-top: 5px;margin-bottom: 0px">
                        <label style="color: grey"> create at: </label><label
                            class="create-time"><%=((List) request.getAttribute("collectedCreateDateList")).get(index)%>
                    </label>
                        <label style="white-space: pre;color: grey"> update at: </label><label
                            class="update-time"><%=((List) request.getAttribute("collectedUpdateDateList")).get(index)%>
                    </label>
                    </p>
                    <p style="margin-top: 5px"><%=((MovieSheetVO) ((List) request.getAttribute("collectedSheetVOList")).get(index)).getSheetDescription()%>
                    </p>
                </div>
                <br/>
                <%
                    }
                    if (((List) request.getAttribute("collectedSheetVOList")).size() == 0) {
                %>
                <h3 style="font-weight: 500" align="center">╮(￣▽￣)╭ you haven't collected movielist yet.</h3>
                <%}%>
            </div>
            <%--<div class="movielist-piece">
                <a href="" style="font-size: 20px;font-weight: 600">
                    movielist name</a>
                <label style="font-size: 15px">(<label>10 movies</label>)</label>
                <div class="collect-div">
                    <img class="collect-pic" alt="like" src="/images/uncollected.png" width="22px"/>
                    <span class="collect-num" name="unclicked" style="padding-left: 10px">10</span>
                </div>
                <span class="collector-num" style="float: right;padding-right: 10px">10 collectors</span>
                <br/>
                <label style="color: grey">by &nbsp</label>
                <a href="">
                    <img class="user-pic" src="/images/userImage.jpg" width="30px"/>
                    user name</a>
                <br/>
                <p style="margin-top: 5px;margin-bottom: 0px">
                    <label style="color: grey"> create at: </label><label class="create-time">2017-03-12 12:04</label>
                    <label style="white-space: pre;color: grey"> update at: </label><label class="update-time">2017-03-12
                    12:04</label>
                </p>
                <p style="margin-top: 5px;margin-bottom: 5px">movielist description</p>
                <a class="del-movielist-div" style="float:right;padding-right: 20px">
                    <img alt="add" src="/images/delete.png" style="position: relative;top:5px" width="22px"/>
                    <span>delete</span>
                </a>
            </div>
            <br/>--%>
        </div>
    </div>
</div>
<script>
    var userID = "${userID}";
    $(document).ready(function () {
        $("#tip-title").hide();
        $("#tip-discription").hide();
        $("#create-movielist").hide();
        $("#delete-movielist").hide();
        $("#collected-movielist-div").hide();
    });
    <%--function makeCreateMovieListDiv() {
        $("#movielist-div").removeAll();
        var br = $("<br/>");
        $("#movielist-div").append(br);
        <%for(int index=0;index<((List)request.getAttribute("movieSheetVOList")).size();index++){%>
        var movieName = $("<a></a>").text("<%=((MovieSheetVO)((List)request.getAttribute("movieSheetVOList")).get(index)).getMovieSheetName()%>");
        movieName.attr({
            "href": "/movielist/j<%=((MovieSheetVO)((List)request.getAttribute("movieSheetVOList")).get(index)).getId()%>",
            "style": "font-size: 20px;font-weight: 600"
        });
        var movieNum = $("<label></label>").text("( <%=((MovieSheetVO)((List)request.getAttribute("movieSheetVOList")).get(index)).getMovieList().size()%> movies )");
        movieNum.attr("style", "font-size: 15px");
        var collector = $("<span></span>").text("10 collectors").style("float:right");
        $("#movielist-div").append(movieName, movieNum, collector, $("<br/>"));
        var by = $("<label></label>").text("by &nbsp").attr("style", "color:grey");
        var userImage = $("<img/>").attr({
            "src": "<%=((UserVO)((List)request.getAttribute("ownerInfoList")).get(index)).getImgPath()%>",
            "width": "30px"
        });
        var userName = $("<label></label>").text("<%=((UserVO)((List)request.getAttribute("ownerInfoList")).get(index)).getUserName()%>");
        var userLink = $("<a></a>").attr("href", "/user/j<%=((UserVO)((List)request.getAttribute("ownerInfoList")).get(index)).getUserID()%>/home");
        userLink.append(userImage, userName);
        var createTag = $("<label></label>").text("create at: ").attr("style", "color:grey");
        var createTime = $("<label></label>").text("<%=((List)request.getAttribute("createDateList")).get(index)%>");
        var updateTag = $("<label></label>").text("      update at: ").attr("style", "white-space: pre;color: grey");
        var updateTime = $("<label></label>").text("<%=((List)request.getAttribute("updateDateList")).get(index)%>");
        var time = $("<p></p>").attr("style", "margin-top: 5px;margin-bottom: 0px").append(createTag, createTime, updateTag, updateTime);
        var description = $("<p></p>").text("<%=((MovieSheetVO)((List)request.getAttribute("movieSheetVOList")).get(index)).getMovieListDescription()%>");
        description.attr("style", "margin-top: 5px");

        <%}%>
    }--%>
</script>
<script src="/js/animation.js"></script>
<script src="/js/main.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="/js/particles/particles.min.js"></script>
<script src="/js/particles/app.js"></script>
</body>
</html>