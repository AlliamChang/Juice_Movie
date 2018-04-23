<%@ page import="cn.cseiii.model.FilmMakerVO" %>
<%@ page import="cn.cseiii.model.MovieSheetVO" %>
<%@ page import="cn.cseiii.model.MovieShowVO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/5/28
  Time: 18:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${movieSheetVO.movieSheetName}</title>
    <link rel="shortcut icon" href="/images/favicon.ico">
    <link rel="bookmark" href="/images/favicon.ico">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/animate.min.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/jquery.cookie.js"></script>
    <script src="/js/jquery.waypoints.min.js"></script>
    <link href="/css/movielistDetail.css" rel="stylesheet">
</head>
<body>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="-1"/>
    </jsp:include>
    <div style="width: 900px">
        <div class="content-left">
            <div id="movielist-baseInfo">
                <h1 style="margin-bottom: 15px;margin-top: 15px" id="movielist-name">${movieSheetVO.movieSheetName}</h1>
                <a href="/user/j${ownerVO.userID}/home">
                    <img class="user-pic" src="${ownerVO.imgPath}" width="30px"/>
                ${ownerVO.userName}</a>
                <label style="white-space: pre;color: grey"> create at: </label><label class="create-time">${createTime}</label>
                <label style="white-space: pre;color: grey">  update at: </label><label class="update-time">${updateTime}</label>
                <p style="margin-top: 10px" id="movielist-description">${movieSheetVO.sheetDescription}</p>
                <div class="collect-div">
                    <img class="collect-pic" alt="like" src="/images/uncollected.png" width="25px"/>
                    <span class="collect-num" name="unclicked"
                          style="padding-left: 10px;font-size: 18px;position: relative;bottom: 3px">${movieSheetVO.numOfCollectors}</span>
                </div>
                <%if((Boolean) request.getAttribute("isOwner")){%>
                <a class="delete-movielist">
                    <img alt="delete" src="/images/delete.png" style="position: relative;top:5px" width="22px"/>
                    <span>delete</span>
                </a>
                <a class="add-movie-div" style="padding-left: 20px">
                    <img alt="add" src="../images/addImage.png" style="position: relative;top:5px" width="22px"/>
                    <span>add more</span>
                </a>
                <%}%>
            </div>
            <div id="contain-movies">
                <h2>contain movies
                    <span class="tip-number">( <span id="contain-movie-number">${movieSheetVO.movieList.size()}</span> in total )
                        </span>
                </h2>
                <%  MovieSheetVO movieSheetVO=(MovieSheetVO) request.getAttribute("movieSheetVO");
                    for(int index=0;index<movieSheetVO.getMovieList().size();index++){
                        MovieShowVO movie = movieSheetVO.getMovieList().get(index);
                        SimpleDateFormat format=new SimpleDateFormat("yyyy");%>
                <div class="contain-movies-piece">
                    <div class="movie-image-div">
                        <a href="/movie/j<%=movie.getId()%>">
                            <img src="<%=movie.getPoster()%>" alt="movie image"
                                 style="position: relative;top:10px" width="112" height="160"/></a>
                    </div>
                    <p style="margin-bottom: 5px"><a class="piece-movie-name" href="/movie/j<%=movie.getId()%>" style="font-size: 17px;font-weight: 600"><%=movie.getName()%></a>
                        (<label class="movie-time"><%=format.format(movie.getReleased())%></label>)
                    </p>
                    <img src="<%=((List)request.getAttribute("doubanRatePicList")).get(index)%>" alt="rate image" width="135.7px" height="20px"/>
                    <label class="douban-rate-num"><%=((List)request.getAttribute("doubanRateList")).get(index)%></label>
                    <br/>
                    <table>
                        <tr>
                            <td>Type:</td>
                            <td><%=((List)request.getAttribute("typeList")).get(index)%></td>
                        </tr>
                        <tr>
                            <td>Directors:</td>
                            <td><%=((List)request.getAttribute("directorsList")).get(index)%></td>
                        </tr>
                        <tr>
                            <td>Writers:</td>
                            <td><%=((List)request.getAttribute("writersList")).get(index)%></td>
                        </tr>
                        <tr>
                            <td>Actors:</td>
                            <td><%=((List)request.getAttribute("actorsList")).get(index)%></td>
                        </tr>
                    </table>
                    <p><label>Ps: &nbsp</label><label class="movie-description"><%=movieSheetVO.getEachMovieDescription().get(index).getDescription()%></label></p>
                    <%format=new SimpleDateFormat("yyyy-MM-dd HH:mm");%>
                    <label>created at: <%=format.format(movieSheetVO.getEachMovieDescription().get(index).getCreated())%></label>
                    <%--<a class="modify-movie" style="float: right;padding-right: 20px">
                        <img alt="add" src="/images/modify.png" style="position: relative;bottom: 7px" width="15px"/>
                        <span>modify</span>
                    </a>--%>
                    <a class="add-to-movielist" style="float: right;padding-right: 20px">
                        <img alt="add" src="/images/addImage.png" style="position: relative;bottom: 7px" width="20px"/>
                        <span>add to my movielist</span>
                    </a>
                    <%if((Boolean) request.getAttribute("isOwner")){%>
                    <a class="delete-movie" style="float: right;padding-right: 20px">
                        <img alt="add" src="/images/delete.png" style="position: relative;bottom: 7px" width="20px"/>
                        <span>remove</span>
                    </a>
                    <%}%>
                </div>
                <br/>
                <%}%>
                <%if(movieSheetVO.getMovieList().size()==0){%>
                <h3 style="font-weight: 500" align="center">╮(￣▽￣)╭owner haven't add movies to this movielist.</h3>
                <%}%>
            </div>
        </div>
        <div class="content-right">
            <h2>relative movielist: </h2>
            <div class="relative-movielist-piece">
                <a href="" style="font-size: 20px">movielist name </a><br/>
                <label>by &nbsp</label>
                <a href=""><img class="user-pic" src="/images/user.jpg" width="30px"/>user name</a><br/>
                <label style="color: grey">update at: </label><label class="create-time">2017-05-12 13:04</label>
            </div>
        </div>
    </div>
    <div id="add-movies" class="round">
        <div class="close-button"></div>
        <span id="tip-movie" class="tip-text">Hey, there's nothing!</span>
        <span class="tip-discription" class="tip-text">Hey, there's nothing!</span>
        <form action="" method="get" id="create-movielist-form">
            <span class="movieName">movie name: &nbsp;
                <input type="text" class="text-line title-line" name="title" placeholder="movie name">
                <input type="hidden" class="pre-search-movieID" value="-1">
            </span>
            <br/>
            <ul id="pre-search-ul" class="round">
            </ul>
            <span class="description">description: <br/>
                    <textarea class="description-textarea" name="description" placeholder="description"></textarea></span>
        </form>
        <button class="add-button round">add</button>
    </div>
    <div id="delete" class="round">
        <input type="hidden" class="delete-type">
        <p id="delete-warnning" style="font-size: 18px">are you sure to delete this movielist? T﹏T </p>
        <button id="cancel-button" class="round" style="float: right">cancel</button>
        <button id="yes-button" class="round" style="float: right">yes</button>
    </div>
    <div id="add-to-own-movielist" class="round">
        <div id="own-movielists-table">
            <span id="tag-movielist">your movielists:</span>
            <form>
            <ul>
                <li class="round"><input type="radio" name="own-movielists" checked=true>1</li>
            </ul>
            </form>
        </div>
        <hr width="2px" color="grey"/>
        <div id="right-div">
            <div class="close-button"></div>
            <span class="tip-discription" class="tip-text">Hey, there's nothing!</span>
            <span class="description">description: <br/>
            <textarea class="description-textarea" name="description" placeholder="description"></textarea>
             </span>
            <button class="add-to-movielist-button round">add to this movielist</button>
        </div>
    </div>
    <%--<div id="modify">
        <div class="close-button"></div>
        <span class="tip-discription" class="tip-text">Hey, there's nothing!</span>
        <form action="" method="get">
            <input type="hidden" class="target-type">
            <span class="target-name">modify: &nbsp;
            <label id="modify-target"></label></span>
            <br/>
            <span class="description">description: <br/>
                    <textarea class="description-textarea" name="description" placeholder="description"></textarea></span>
        </form>
        <button class="modify-button round">modify</button>
    </div>--%>
    <div id="background-shade"></div>
</div>
<script>
    $(document).ready(function () {
        $("#add-movies").hide();
        $("#delete").hide();
        $("#add-to-own-movielist").hide();
        $("#add-movies #tip-movie").hide();
        $("#pre-search-ul").hide();
        $("#add-movies .tip-discription").hide();
        $("#add-to-own-movielist .tip-discription").hide();
        <%if(((MovieSheetVO)request.getAttribute("movieSheetVO")).isHasCollected()){%>
        $(".collect-pic").addClass("selected");
        <%}%>
        <%if(movieSheetVO.isHasCollected()){%>
            $(".collect-div img").attr("src","/images/collected.png");
            $(".collect-div img").addClass("selected");
        <%}%>
    });
    var movieSheetID=${movieSheetVO.id};
    var userID=${ownerVO.userID};
</script>
<script src="/js/animation.js"></script>
<script src="/js/movielistDetail.js"></script>
<script src="/js/main.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="/js/particles/particles.min.js"></script>
<script src="/js/particles/app.js"></script>
</body>
</html>
