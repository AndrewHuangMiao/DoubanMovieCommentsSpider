<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.1/jquery.jgrowl.min.css" />
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.1/jquery.jgrowl.min.js"></script>
    <script src="http://code.angularjs.org/angular-1.0.1.min.js"></script>
    <title>个人中心</title>
    <style>
        th,td{
            border: black 1px solid;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="page-header">
        <h1>豆瓣电影评论管理</h1>
    </div>

    <nav class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">豆瓣电影评论爬虫</a>
        </div>
        <div>
            <ul class="nav navbar-nav">

                <li><a href="<c:url value="/user/${user.role}"/>">查看已爬电影</a></li>
                <li><a href="http://localhost:8080/crawel/crawelFromWeb">从网上爬取电影</a></li>
                <li><a href="http://localhost:8080/user/login">退出登录</a></li>
                <li class="text-right" style="margin-left: 400px;"><a href="#">${user.userName}欢迎你!</a></li>
            </ul>
        </div>
    </nav>

    <div class="row">
        <div class="col-lg-3" style="margin-left: 875px;margin-bottom: 10px;">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="电影名称" id="searchMovie">
                 <span class="input-group-btn">
                   <button class="btn btn-default" type="button" onclick="searchMovie()">查询</button>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <table class="table table-bordered" id="movieTable">
            </table>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <ul class="pager">
                <li><a href='#commentModal' onclick='getPreviousMovie()'>上一页</a></li>
                <li><a href='#commentModal' onclick='getNextMovie()'>下一页</a></li>
            </ul>
        </div>
    </div>
</div>


<!-- Modal -->
<div class="modal fade" id="showMovieDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">电影信息</h4>
            </div>
            <div class="modal-body">
                <form class="form" id="showTable">
                    <div class="form-group">
                        <label for="movieName_show">电影名称</label>
                        <input type="text" class="form-control" id="movieName_show">
                    </div>
                    <div class="form-group">
                        <label for="director_show">导演</label>
                        <input type="text" class="form-control" id="director_show">
                    </div>

                    <div class="form-group">
                        <label for="actors_show">演员</label>
                        <input type="text" class="form-control" id="actors_show">
                    </div>

                    <div class="form-group">
                        <label for="type_show">类型</label>
                        <input type="text" class="form-control" id="type_show">
                    </div>

                    <div class="form-group">
                        <label for="time_show">上映时间</label>
                        <input type="text" class="form-control" id="time_show">
                    </div>

                    <div class="form-group">
                        <label for="duration_show">时长</label>
                        <input type="text" class="form-control" id="duration_show">
                    </div>

                    <div class="form-group">
                        <label for="area_show">地区</label>
                        <input type="text" class="form-control" id="area_show">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="commentModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title"></h4>
            </div>
            <div class="modal-body" id="commentBody">

            </div>
            <div class="modal-footer">
                <ul class="pager">
                    <li><a href='#commentModal' onclick='getPreviousComment(this)' id="previous">previous</a></li>
                    <li><a href='#commentModal' onclick='getNextComment(this)' id="next">next</a></li>
                </ul>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="analyseModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="analyseTitle"></h4>
            </div>
            <div class="modal-body" id="analyseBody">

            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


</body>

<script>
    var movieCurrentPage = 0;
    var moviePageSize = 2;
    var movieCount = getTotalMovieCount();
    var moviePageNum = 0;

    /*
     电影总数和总的页数
     */
    function getTotalMovieCount(){
        $.ajax({
            url: "http://localhost:8080/crawel/getMovieCount",
            type: 'post',
            success: function (movieCountDB) {
                debugger;
                movieCount = movieCountDB;
                moviePageNum = parseInt((movieCount % moviePageSize==0)?(movieCount/moviePageSize-1):(movieCount/moviePageSize));
            }
        });
    }

    function getPreviousMovie(){
        if(movieCurrentPage == 0){
            movieCurrentPage = 0;
            $.jGrowl("第一页!");
        }else{
            movieCurrentPage = movieCurrentPage - 1;
        }
        debugger;
        getMovieDetail();
    }

    function getNextMovie(){
        debugger;
        if(movieCurrentPage == moviePageNum){
            movieCurrentPage = moviePageNum;
            $.jGrowl("已经是最后一页!");
        }else{
            movieCurrentPage = movieCurrentPage + 1;
        }
        debugger;
        getMovieDetail();
    }

    function getMovieDetail(){
        var html = " <tr><th>电影名称</th>"+
                "<th>类型</th>"+
                "<th>演员</th>"+
                "<th>导演</th>"+
                "<th>上映时间</th>"+
                "<th>地区</th>"+
                "<th>持续时间</th>" +
                "<th>操作</th>"+
                "</tr>";
        $.ajax({
            url: "http://localhost:8080/crawel/getMovieByPage",
            type: 'post',
            data:{currentPage:movieCurrentPage, pageSize:moviePageSize},
            success: function (allMovieDetail) {

                $.each(allMovieDetail, function(index, movieDetail){
                    var movieStr = JSON.stringify(movieDetail);
                    var movieHtml = "";
                    movieHtml += "<tr><td>" + movieDetail.movieName + "</td>" +
                            "<td>" + movieDetail.type + "</td>"+
                            "<td>" + movieDetail.actors + "</td>" +
                            "<td>" + movieDetail.director + "</td>" +
                            "<td>" + movieDetail.time + "</td>" +
                            "<td>" + movieDetail.area + "</td>" +
                            "<td>" + movieDetail.duration + "</td>" +
                            "<td><button class='btn btn-small btn-primary' data='"+ movieStr +"' data-toggle=\"modal\" data-target=\"#showMovieDetail\" onclick='getMovieDetailFromDB(this)'>查看电影详细信息</button>" +
                            "<button class='btn btn-small btn-primary' data='"+ movieStr +"' data-toggle=\"modal\" data-target=\"#commentModal\" onclick='getDoubanCommentFromDB(this)'>查看电影评论</button>" +
                            "<button class='btn btn-small btn-primary' data='"+ movieStr +"' onclick='downloadComment(this);'>下载评论</button>" +
                            "<button class='btn btn-small btn-primary' data='"+ movieStr +"' data-toggle=\"modal\" data-target=\"#analyseModal\" onclick='analyse(this);'>分析</button></td>" +
                            "</tr>";
                    html += movieHtml;
                });

                $('#movieTable').html(html);
            }
        });
    }

    $(function(){
       getMovieDetail();
    });

    var currentPage = 0;
    var pageSize = 20;
    var pageNum = 0;
    var totalCount = 0;

    function getPreviousComment(obj){
        var movieName = obj.getAttribute("movieName");
        if(currentPage == 0){
            currentPage = 0;
        }else{
            currentPage = currentPage - 1;
        }
        debugger;
        getDoubanComment(movieName);
    }

    function getNextComment(obj){
        var movieName = obj.getAttribute("movieName");
        if(currentPage == pageNum){
            currentPage = pageNum;
        }else{
            currentPage = currentPage + 1;
        }
        debugger;
        getDoubanComment(movieName);
    }

    /*
    评论总数和总的页数
     */
    function getTotalCount(movieName) {
        debugger;
        $.ajax({
            url: "http://localhost:8080/crawel/getTotalCount",
            type: 'post',
            data: {movieName: movieName},
            success: function (newtotalCount) {
                totalCount = newtotalCount;
                pageNum = parseInt(totalCount/pageSize);
            }
        });
    }

    function downloadComment(obj) {
        var json = obj.getAttribute("data");
        var movieDetail = eval('('+json+')');
        window.location.href = "http://localhost:8080/download/downloadComment?movieName=" + encodeURI(movieDetail.movieName);
    }

    function getMovieDetailFromDB(obj) {
        var json = obj.getAttribute("data");
        var movieDetail = eval('('+json+')');
        debugger;
        $('#movieName_show').val(movieDetail.movieName);
        $('#type_show').val(movieDetail.type);
        $('#actors_show').val(movieDetail.actors);
        $('#director_show').val(movieDetail.director);
        $('#time_show').val(movieDetail.time);
        $('#area_show').val(movieDetail.area);
        $('#duration_show').val(movieDetail.duration);
    }

    function getDoubanCommentFromDB(obj) {
        var json = obj.getAttribute("data");
        var movieDetail = eval('('+json+')');
        var movieName = movieDetail.movieName;
        $('#previous').attr("movieName", movieName);
        $('#next').attr("movieName", movieName);
        currentPage = 0;
        getTotalCount(movieName);
        getDoubanComment(movieName);
    }

    function getDoubanComment(movieName){
        var html = "";
        $.ajax({
            url: "http://localhost:8080/crawel/getCommentsFromDB",
            type: 'post',
            data: {movieName: movieName, currentPage: currentPage, pageSize: pageSize},
            success: function (doubanComments) {
                debugger;
                $('.modal-title').text(doubanComments[0].movieName + "电影评论");
                $.each(doubanComments, function (index, doubanComment) {
                    var commentItem = "<div class='media'><div class='media-left' style='width:48px;'><img class='media-object' src=\"" + doubanComment.headUrl + "\"/></div>";
                    commentItem += "<div class='media-body'><h4 class='media-heading'>" +
                            "<span id='userName'>" + doubanComment.userName + "</span>" +
                            "<span id='starAndTime'>" + " 评分是:" + doubanComment.star + "  " + doubanComment.time + "</span>"
                            + "</h4>";
                    commentItem += "<div class='media-content'>" + doubanComment.comment + "</div></div></div>";
                    html += commentItem;
                });

                $('#commentBody').html(html);
                $.jGrowl("获取评论!");
            }
        });
    }

    function searchMovie() {
        var html = " <tr><th>电影名称</th>"+
                "<th>类型</th>"+
                "<th>演员</th>"+
                "<th>导演</th>"+
                "<th>上映时间</th>"+
                "<th>地区</th>"+
                "<th>持续时间</th>" +
                "<th>操作</th>"+
                "</tr>";
        debugger;
        var searchMovieName = $('#searchMovie').val();
        $.ajax({
            url: "http://localhost:8080/crawel/getMovieDetailFromDB",
            type: 'post',
            data: {movieName: searchMovieName},
            success: function (movieDetail) {
                debugger;
                var movieStr = JSON.stringify(movieDetail);
                var movieHtml = "";
                movieHtml += "<tr><td>" + movieDetail.movieName + "</td>" +
                        "<td>" + movieDetail.type + "</td>" +
                        "<td>" + movieDetail.actors + "</td>" +
                        "<td>" + movieDetail.director + "</td>" +
                        "<td>" + movieDetail.time + "</td>" +
                        "<td>" + movieDetail.area + "</td>" +
                        "<td>" + movieDetail.duration + "</td>" +
                        "<td><button class='btn btn-small btn-primary' data='" + movieStr + "' data-toggle=\"modal\" data-target=\"#showMovieDetail\" onclick='getMovieDetailFromDB(this)'>查看电影详细信息</button>" +
                        "<button class='btn btn-small btn-primary' data='" + movieStr + "' data-toggle=\"modal\" data-target=\"#commentModal\" onclick='getDoubanCommentFromDB(this)'>查看电影评论</button>" +
                        "<button class='btn btn-small btn-primary' data='" + movieStr + "' onclick='downloadComment(this);'>下载评论</button>" +
                        "<button class='btn btn-small btn-primary' data='"+ movieStr +"' data-toggle=\"modal\" data-target=\"#analyseModal\" onclick='analyse(this);'>分析</button></td>" +
                        "</tr>";
                html += movieHtml;
                $('#movieTable').html(html);
            }
        });
    }

    function analyse(obj){
        var json = obj.getAttribute("data");
        var movieDetail = eval('('+json+')');
        var movieName = movieDetail.movieName;
        $.ajax({
            url: "http://localhost:8080/crawel/analyseComment",
            type: 'post',
            data: {movieName: movieName},
            success: function (avg) {
                if(avg > 25){
                    $('#analyseBody').text("该电影平均评分"+avg+",推荐观看");
                }else{
                    $('#analyseBody').text("该电影平均评分"+avg+",不推荐观看");
                }
                $('#analyseTitle').text(movieName+"分析报告(满分50,大于25推荐观看)");
            }
        });
    }


</script>
</html>
