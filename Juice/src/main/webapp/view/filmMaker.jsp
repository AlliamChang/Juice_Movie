<%@ page import="cn.cseiii.model.FilmMakerVO" %>
<%@ page import="cn.cseiii.enums.FigureType" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.cseiii.model.MovieShowVO" %>
<%@ page import="cn.cseiii.model.MovieMadeVO" %><%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/5/17
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${filmMakerVO.name}</title>
    <link rel="shortcut icon" href="/images/favicon.ico">
    <link rel="bookmark" href="/images/favicon.ico">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/animate.min.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/jquery.cookie.js"></script>
    <script src="/js/jquery.waypoints.min.js"></script>
    <link href="/css/filmMaker.css" rel="stylesheet">
</head>
<body>
<div class="content">
    <jsp:include page="header.jsp" flush="true">
        <jsp:param name="index" value="-1"/>
    </jsp:include>
    <div style="width: 900px">
        <div class="content-left">
            <div id="filmMaker-basicInfo">
                <div id="filmMaker-poster">
                    <img style="padding-right: 15px" src="${filmMakerVO.avatar}" alt="poster" width="100" onerror="javascript:this.src='../images/default_avatar.png'"/>
                </div>
                <div id="filmMaker-name">
                    <p style="margin-top: 15px">${filmMakerVO.name}</p>
                </div>
                <label id="fileMaker-role">
                    <%FigureType[] allFigureType = (FigureType[]) request.getAttribute("figureTypes");
                        for(int i=0;i<allFigureType.length;i++){
                            String figure=allFigureType[i].name().toLowerCase();%>
                    <label><%=figure%></label>
                    <%if(i!=allFigureType.length-1){%>
                    <label> | </label>
                    <%}%>
                    <%}%>
                </label>
                <p style="margin-top: 10px;margin-bottom: 10px"><label style="color:grey">born: </label>${born}</p>
                <p style="margin-top: 0px;margin-bottom: 10px"><label style="color: grey">death: </label>${death}</p>
                <%--<div id="filmMaker-intro">
                    <h2>brief introduction</h2>
                    <p class="'movieStoryline">${filmMakerVO.brief}</p>
                </div>--%>
            </div>
            <div id="production">
                <h2>Production<span class="tip-number">( <span
                        id="production-num">${filmMakerVO.filmHasMade.size()}</span> in total ）</span>
                    <span><table class="turn-page-production" align="center">
                                <tr>
                                    <td>
                                        <span class="tip-number" style="position: relative;bottom: 10px">
                                            <span id="production-page-index">1</span> / <span
                                                id="production-total-page">${productionPageSize}</span> </span>
                                    </td>
                                    <td style="padding-right: 7px">
                                        <img class="last-page" id="production-last-page" src="/images/lastPage.png"
                                             alt="last page" width="20px" height="20px"/>
                                    </td>
                                    <td style="padding-right: 10px">
                                        <img class="next-page" id="production-next-page" src="/images/nextPage.png"
                                             alt="next page" width="20px" height="20px"/>
                                    </td>
                                </tr>
                            </table></span>
                </h2>
                <table cellspacing="15px" class="production-table">
                    <tr>
                        <%int desIndex=Math.min(4,((FilmMakerVO)request.getAttribute("filmMakerVO")).getFilmHasMade().size());
                            for(int i=0;i<desIndex;i++){%>
                        <td>
                            <div style="max-width: 130px;max-height: 230px">
                                <a href="/movie/j<%=((MovieMadeVO[])request.getAttribute("productionArray"))[i].getId()%>">
                                    <img src="<%=((MovieMadeVO[])request.getAttribute("productionArray"))[i].getPoster()%>" alt="movie pic" width="126" height="180" onerror="javascript:this.src='../images/movie_error.png'"/>
                                <p class="production-name"><%=((MovieMadeVO[])request.getAttribute("productionArray"))[i].getName()%></p></a>
                                <p class="role">
                                    <%String[] roles=(String[])((List)request.getAttribute("rolesInMovieList")).get(i);
                                        for(int index=0;index<roles.length;index++){%>
                                    <label><%=roles[index]%></label>
                                    <%if(index+1!=roles.length){%>
                                    <label> | </label>
                                    <%}%>
                                    <%}%>
                                </p>
                                <p class="year"><%=((String[])request.getAttribute("releaseYearList"))[i]%></p>
                            </div>
                        </td>
                        <%}%>
                    </tr>
                </table>
            </div><br/>
        <%--<td>
                            <div>
                                <a href="">
                                    <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                                </a>
                                <p><a href="">movie name</a></p>
                                <p class="role">Director</p>
                                <p class="year">2017</p>
                            </div>
                        </td>
                        <td>
                            <div>
                                <a href="">
                                    <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                                </a>
                                <p><a href="">movie name</a></p>
                                <p class="role">Writer</p>
                                <p class="year">2017</p>
                            </div>
                        </td>
                        <td>
                            <div>
                                <a href="">
                                    <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                                </a>
                                <p><a href="">movie name</a></p>
                                <p class="Writer">Director</p>
                                <p class="year">2017</p>
                            </div>
                        </td>--%>
            <%--<div id="co-workers">
                <h2>cooperate with<span class="tip-number">( <span
                        id="co-workers-num">${coFilmMaker.size()}</span> in total ）</span>
                    <span><table class="turn-page-co-workers" align="center">
                                <tr>
                                    <td>
                                        <span class="tip-number" style="position: relative;bottom: 10px">
                                            <span id="co-workers-page-index">1</span> / <span
                                                id="co-workers-total-page">${coFilmMakerPageSize}</span> </span>
                                    </td>
                                    <td style="padding-right: 7px">
                                        <img class="last-page" id="co-workers-last-page" src="/images/lastPage.png"
                                             alt="last page" width="20px" height="20px"/>
                                    </td>
                                    <td style="padding-right: 10px">
                                        <img class="next-page" id="co-workers-next-page" src="/images/nextPage.png"
                                             alt="next page" width="20px" height="20px"/>
                                    </td>
                                </tr>
                            </table></span>
                </h2>
                <table cellspacing="15px" class="co-workers-table">
                    <tr>
                        <%int desIndex2=Math.min(4,((List)request.getAttribute("coFilmMaker")).size());
                            for(int i=0;i<desIndex2;i++){%>
                        <td>
                            <div style="max-width: 130px;max-height: 210px">
                                <a href="/figure/j<%=((FilmMakerVO)((List)request.getAttribute("coFilmMaker")).get(i)).getFigureID()%>">
                                    <img src="<%=((FilmMakerVO)((List)request.getAttribute("coFilmMaker")).get(i)).getAvatar()%>" alt="user pic" width="126"/>
                                <p><%=((FilmMakerVO)((List)request.getAttribute("coFilmMaker")).get(i)).getName()%></p></a>
                            </div>
                        </td>
                        <%}%>
                    </tr>
                </table>
            </div>--%>
            <%--<td>
                            <div>
                                <a href="">
                                    <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                                </a>
                                <p><a href="">co-worker name</a></p>
                            </div>
                        </td>
                        <td>
                            <div>
                                <a href="">
                                    <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                                </a>
                                <p><a href="">co-worker name</a></p>
                            </div>
                        </td>
                        <td>
                            <div>
                                <a href="">
                                    <img src="/images/movieImage.jpg" alt="movie pic" width="112" height="160"/>
                                </a>
                                <p><a href="">co-worker name</a></p>
                            </div>
                        </td>--%>
            <div id="statistics: how good am I?">
                <h2>statistics</h2>
                <div id="chart" style="width: 600px; height: 500px;"></div>
            </div>
            <br/><br/>
        </div>
        <div class="content-right">
            <br/><br/><br/><br/><br/><br/>
            <div id="co-workers">
                <div style="margin-left: 20px">
                    <h3>cooperate with<span class="tip-number">( <span>${coFilmMaker.size()}</span> in total ）</span>
                        <br/><span><table class="turn-page-co-workers" align="center">
                                <tr>
                                    <td>
                                        <span class="tip-number" style="position: relative;bottom: 10px">
                                            <span id="co-workers-page-index">1</span> / <span
                                                id="co-workers-total-page">${coFilmMakerPageSize}</span> </span>
                                    </td>
                                    <td style="padding-right: 7px">
                                        <img class="last-page" id="co-workers-last-page" src="/images/lastPage.png"
                                             alt="last page" width="20px" height="20px"/>
                                    </td>
                                    <td style="padding-right: 10px">
                                        <img class="next-page" id="co-workers-next-page" src="/images/nextPage.png"
                                             alt="next page" width="20px" height="20px"/>
                                    </td>
                                </tr>
                            </table></span>
                    </h3>
                    <table style="padding-right: 5px" class="co-workers-table">
                        <%int desIndex3=Math.min(4,((List)request.getAttribute("coFilmMaker")).size());
                            for(int i=0;i<desIndex3;i++){
                                FilmMakerVO filmMakerVO=(FilmMakerVO)((List)request.getAttribute("coFilmMaker")).get(i);
                                FigureType[] figureTypes = filmMakerVO.getAllFigureTypes().toArray(new FigureType[filmMakerVO.getAllFigureTypes().size()]);
                                String roles="";
                                for(int index=0;index<figureTypes.length;index++) {
                                    roles += figureTypes[index].name().toLowerCase();
                                    if(index!=figureTypes.length-1){
                                        roles+=" | ";
                                    }
                                }
                        %>
                        <tr class="co-workers-tr-piece">
                            <td>
                                <a href="/figure/j<%=filmMakerVO.getFigureID()%>">
                                    <table>
                                        <tr>
                                            <td>
                                                <img src="<%=filmMakerVO.getAvatar()%>" alt="follower" width="100px" onerror="javascript:this.src='../images/default_avatar.png'"/>
                                            </td>
                                            <td style="padding-left: 15px">
                                                <label class="recommand-filmMaker-name" style="margin-bottom: 10px">
                                                    <%=filmMakerVO.getName()%></label><br/>
                                                <label class="recommand-filmMaker-tags" style="text-decoration: none"><%=roles%></label>
                                            </td>
                                        </tr>
                                    </table>
                                </a>
                                <br/>
                            </td>
                        </tr>
                        <%}%>
                        <%--<tr>
                            <td>
                                <a href="">
                                    <table>
                                        <tr>
                                            <td>
                                                <img src="/images/userImage.jpg" alt="follower" width="60px"
                                                     height="60px" style="float:  left"/>
                                            </td>
                                            <td>
                                                <label class="recommand-filmMaker-name" style="margin-bottom: 10px">
                                                    filmMaker name</label>
                                                <label class="recommand-filmMaker-tags">director|actor|writer</label>
                                            </td>
                                        </tr>
                                    </table>
                                </a>
                                <br/>
                            </td>

                        </tr>--%>
                    </table>
                </div>
            </div>
            <br/><br/>
        </div>
    </div>
</div>
<script>
    var figureID=${filmMakerVO.figureID};
    //画production表格
    <%--function makeProductionTable(pageIndex) {
        $("#production table tr").removeAll();
        <%int pageIndex=0;%>
        <%=pageIndex%>=pageIndex;
        <%for(int i=0;i<4;i++) {%>
            var image = $("<img/>").attr({
                "href": "<%=((FilmMakerVO)request.getAttribute("filmMakerVO")).getFilmHasMade().get(pageIndex*4+i).getPoster()%>",
                "width": "112",
                "height": "160"
            });
            var movieName = $("<p></p>").text(<%=((FilmMakerVO)request.getAttribute("filmMakerVO")).getFilmHasMade().get(pageIndex*4+i).getName()%>);
            var link = $("<a></a>").attr("href", "<%=((FilmMakerVO)request.getAttribute("filmMakerVO")).getFilmHasMade().get(pageIndex*4+i).getId()%>");
            link.append(image,movieName);
            var td=$("<td></td>").append(link);
            $("#production table tr").append(td);
        <%}%>
    }--%>
    //画coworker表格
    <%--function makeCoWorkerTable(pageIndex) {
        $("#co-workers table tr").removeAll();
        <%=pageIndex%>=pageIndex;
        <%for(int i=0;i<4;i++) {%>
        var image = $("<img/>").attr({
            "href": "<%=((FilmMakerVO)((List)request.getAttribute("coFilmMaker")).get(pageIndex*4+i)).getAvatar()%>",
            "width": "112",
            "height": "160"
        });
        var movieName = $("<p></p>").text(<%=((FilmMakerVO)((List)request.getAttribute("coFilmMaker")).get(pageIndex*4+i)).getName()%>);
        var link = $("<a></a>").attr("href", "<%=((FilmMakerVO)((List)request.getAttribute("coFilmMaker")).get(pageIndex*4+i)).getFigureID()%>");
        link.append(image,movieName);
        var td=$("<td></td>").append(link);
        $("#co-workers table tr").append(td);
        <%}%>
    }--%>
</script>
<script src="/js/echarts.min.js"></script>
<script src="/js/filmMaker.js"></script>
<script src="/js/main.js"></script>

<%--background effect--%>
<div id="particles-js"></div>
<script src="/js/particles/particles.min.js"></script>
<script src="/js/particles/app.js"></script>
</body>
</html>
