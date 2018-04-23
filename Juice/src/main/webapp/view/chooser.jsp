<%--
  Created by IntelliJ IDEA.
  User: I Like Milk
  Date: 2017/5/14
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="filter">
    <form action="get" id="filter-form-movie">
        <input type="hidden" name="type" value="<%=request.getParameter("type")%>">
        <div class="user-choose">
            <div style="display: inline">ALL >> </div>
        </div>
        <a href="javascript:void(0)" style="display: none" class="clear-btn"><img src="../images/dustbin.png"></a>
        <div class="tags"></div>
        <div class="tool">
            <div class="sort">
                <label><input type="radio" name="sort" value="0" checked="checked">Sort by recommendation</label>
                <label><input type="radio" name="sort" value="1">Sort by time</label>
                <label><input type="radio" name="sort" value="2">Sort by IMDb</label>
                <label><input type="radio" name="sort" value="3">Sort by douban</label>
            </div>
            <div class="check">
                <label><input type="checkbox" name="watched"<%=session.getAttribute("id") == null ? " disabled=\"true\"" : ""%> value="1">Never watched</label>
            </div>
        </div>
        <input type="hidden" name="pagesize" value="12">
        <input type="hidden" name="pageindex" value="0">
    </form>
</div>

<div class="list-wp">
    <div class="list"></div>
    <div class="underpants">
        <a class="more"></a>
    </div>
</div>

<script>
    $(function () {
        var type = <%=request.getParameter("type")%>;
        if (type == 0)
            initMovieChooser();
        else
            initTVShowChooser();
        setAutoGetMore();
    });
</script>