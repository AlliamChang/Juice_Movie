<%--
  Created by IntelliJ IDEA.
  User: sparkler
  Date: 2017/6/9
  Time: PM1:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${userName}'s info</title>
    <link href="/css/main.css" rel="stylesheet">
    <link href="/css/animation.css" rel="stylesheet">
    <link href="/css/user.css" rel="stylesheet">
    <link href="/css/test.css" rel="stylesheet">
    <script src="/js/jquery-3.2.1.min.js"></script>
</head>
<body>
<jsp:include page="user.jsp" flush="true">
    <jsp:param name="index" value="0"/>
</jsp:include>
<div class="content">
    <div class="user-info" style="margin-left: 100px;margin-right: 400px">
        <table cellpadding="20px" cellspacing="20px">
            <tr>
                <td>
                    username
                </td>
                <td>
                    ${userName}
                </td>
            </tr>
            <tr>
                <td>
                    E-mail
                </td>
                <td>
                    ${userEmail}
                </td>
            </tr>
            <tr>
                <td>
                    password
                </td>
                <td>
                    ******
                    <button class="modify-password">Modify password</button>
                </td>
            </tr>
        </table><br/><br/>
        <button class="modify-info" style="float: right">Modify info</button>


        <div id="modify-infoM" class="info-tool-position round">
            <div class="close-button"></div>
            <span id="tip-title" class="tip-text">Hey, there's nothing!</span>
            <form action="" method="get" id="modify-infoM-form">
                <span class="title">userName: &nbsp;<input type="text" class="text-line title-line" name="title" placeholder="title"></span>
                <br/>
                <span class="user-img">userImg: &nbsp;<image id="image" src="../images/user.jpg" alt="userPoster" width="140px" height="140px"/></span>
                <br/>
                <input class="choose-file" type="file"onchange="selectImage(this);"/>
                <br/>
            </form>
            <button class="create round">create</button>
        </div>
        <div id="background-shade"></div>
    </div>
</div>




<script>
    var image = '';
    function selectImage(file) {
        if (!file.files || !file.files[0]) {
            return;
        }
        var reader = new FileReader();
        reader.onload = function (evt) {
            document.getElementById('image').src = evt.target.result;
            image = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
</script>


<script>
    $("button.modify-info").click(function () {
        modifyInfo();
    });

    function modifyInfo() {
        $("#background-shade").fadeIn();
        $("#modify-infoM").show();
    };

    $("#modify-infoM .close-button").click(function () {
        $("#background-shade").fadeOut();
        $("#modify-infoM").hide();
    });

    $("#background-shade").click(function () {
        $("#modify-infoM .close-button").click();
    });

    $("#modify-infoM .create").click(function () {
//        if($("input[name='title']").val()==""||$("input[name='title']").val()==null){
//            $("#tip-title").show();
//        }else if($(".description-textarea").val()==""||$(".description-textarea").val()==null){
//            $("#tip-description").show();
//        }else{
//            $.get("/user/createMovielist?userID="+userID+"&title="+$("input[name='title']").val()+
//                "&description="+$(".description-textarea").val(),function (data) {
//                if(data=="SUCCESS"){
//                    location.href="/user/j"+userID+"/movielist";
//                }else{
//                    alert(data);
//                }
//            });
//        }
    });

//    $("input[name='title']").on('input propertychange', function () {
//        $("#tip-title").hide();
//    });
//    $(".description-textarea").on('input propertychange', function () {
//        $("#tip-discription").hide();
//    });
</script>
</body>
</html>
