<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>豆瓣电影评论爬虫</title>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.1/jquery.jgrowl.min.css" />
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <%--<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular.min.js"></script>--%>
    <script src="/common/angular.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.1/jquery.jgrowl.min.js"></script>
<%--<script src="http://code.angularjs.org/angular-1.0.1.min.js"></script>--%>

    <style>
        .row{
            margin-top:30px;
        }

        h2{
            margin-bottom:20px;
        }

        #underline{
            border-bottom: 1px rgb(221, 221, 221) solid;
        }

        .article{
            margin-top: 20px;
        }

        .media{
            border-bottom: 1px dashed #ddd;
            padding-bottom: 20px;
        }

        .thumbnail{
            border: none;
        }

        p{
            margin-top:10px;
            font: 12px Helvetica,Arial,sans-serif;
            line-height: 1.62;
        }

        #userName{
            color:rgb(51, 119, 170);
            font-family: Arial, Helvetica, sans-serif;
            font-size: 12px;
        }

        #starAndTime{
            color: rgb(102, 102, 102);
            font-family: Arial, Helvetica, sans-serif;
            font-size: 12px;
        }

        .media-content{
            color: rgb(17, 17, 17);
            font-family: Arial, Helvetica, sans-serif;
            font-size: 12px;
            line-height: 19.44px;
        }
    </style>
</head>
<body ng-app="myApp" >

  <div class="container" ng-controller="doubanMovieComment">
      <div clas="row">
          <div class="page-header text-center">
              <h1>豆瓣电影评论</h1>
          </div>
      </div>
      <div class="row">
      <%--搜索框--%>
        <div class="col-md-6 nav-search" style="margin-left: 300px;">
          <form class="input-group">
                  <input type="text" class="form-control" placeholder="输入电影名字" name="movieName" ng-model="movieName" style="height: 60px;font-size: 25px;">
                  <span class="input-group-btn">
                    <button class="btn btn-default" type="button" ng-click="getMovieInfo()" style="height: 60px;" data-loading-text="查询中"><span style="font-size: 25px;">搜索</span></button>
                </span>
          </form>
        </div>
      </div>

      <%--整个评论和电影详细信息的具体页面--%>
    <div class="row">
      <%--整个具体的影评内容--%>
      <div id="wrap">
        <div id="content">

          <div class="col-md-8">
              <div clas="row">
                  <div class="col-md-12">
                      <h2>{{movieName}}的影评</h2>
                      <div>全部:{{totalComment}}条<button type="button" class="btn btn-small btn-primary" ng-click="download()" style="margin-left: 620px;">下载评论</button></div>
                      <div id="underline"></div>
                  </div>
              </div>
              <div clas="row">
                  <div class="col-md-12">
                      <div class="article">
                      </div>
                  </div>
                  <div class="col-md-12" style="margin-top:10px;">
                      <div class="footer">
                          <ul class="pager">
                              <li><a href='#' ng-click='getPreviousComment()'>previous</a></li>
                              <li><a href='#' ng-click='getNextComment()'>next</a></li>
                          </ul>
                      </div>
                  </div>
              </div>
          </div>

            <%--这边是电影的详细信息--%>
          <div class="col-md-4">
            <div class="aside">
                <div class="thumbnail">
                  <div class="caption">
                  </div>
                </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>

<script>
    var myapp = angular.module('myApp',[]);
    myapp.controller('doubanMovieComment',function($scope) {
        $scope.movieName = "澳门风云";
        $scope.url = "http://localhost:8080/crawel/getDoubanComments?";
        $scope.pageNum = $scope.totalCount / $scope.pageSize;
        $scope.currentPage = 0;
        $scope.pageSize = 20;

        function getTotalCount(movieName) {
            $.ajax({
                url: "http://localhost:8080/crawel/getTotalCount",
                type: 'post',
                data: {movieName: movieName},
                success: function (totalCount) {
                    $scope.totalCount = totalCount;
                }
            });
        }

        function initPage() {
            var html = "";
            html += "<li><a href='javascript:void(0);' ng-click='getPreviousComment()'>&laquo;</a></li>";
            for (var i = 0; i < 5; i++) {
                html += "<li><a href='javascript:void(0);'  ng-click='getIndexComment(this)'>" + (i + 1) + "</a></li>";
            }
            html += "<li><a href='javascript:void(0);' ng-click='getNextComment()'>&raquo;</a></li>";
            $('.pagination').html(html);
        }

        $scope.getMovieInfo = function () {
            var movieName = $scope.movieName;
            if (movieName != undefined && movieName != null) {
                $.jGrowl("开始获取数据!");
                getDoubanComments(movieName, $scope.currentPage, $scope.pageSize);
                getMovieDetail(movieName);
//                initPage();
                $('#wrap').css("display", "block");
                $('#wrap').css("display", "block");
            }
        }

        /*根据页码得到对应的评论*/
        $scope.getIndexComment = function (a) {
            console.log("index");
            debugger;
            var index = a.val();
            var currentPage = index;
            getDoubanComments($scope.movieName, currentPage, $scope.pageSize);
        }

        $scope.getPreviousComment = function () {
            console.log("previous");
            debugger;
            if ($scope.currentPage == 0) {
                $scope.currentPage = 0;
            } else {
                $scope.currentPage = $scope.currentPage - 1;
            }
            getDoubanComments($scope.movieName, $scope.currentPage, $scope.pageSize);
        }

        $scope.getNextComment = function () {
            debugger;
            if ($scope.currentPage == $scope.pageNum) {
                $scope.currentPage = $scope.pageNum;
            } else {
                $scope.currentPage = $scope.currentPage + 1;
            }
            getDoubanComments($scope.movieName, $scope.currentPage, $scope.pageSize);
        }

        $scope.download = function () {
            window.location.href = "http://localhost:8080/download/downloadComment?movieName=" + encodeURI($scope.movieName);
        }

        /*获取电影评论*/
        var getDoubanComments = function (movieName, currentPage, pageSize) {
            $.ajax({
                url: "http://localhost:8080/crawel/checkMovie",
                type: 'post',
                data: {movieName: movieName},
                success: function (isExist) {
                    if(isExist=="true"){
                        $.jGrowl("已经抓取过此类电影,请直接去到已抓取电影页面查看!");
                    }else{
                        $scope.totalCount = getTotalCount($scope.movieName);
                        var html = "";
                        $.ajax({
                            url: "http://localhost:8080/crawel/getDoubanComments",
                            type: 'post',
                            data: {movieName: movieName, currentPage: currentPage, pageSize: pageSize},
                            success: function (doubanComments) {
                                $.each(doubanComments, function (index, doubanComment) {
                                    var commentItem = "<div class='media'><div class='media-left' style='width:48px;'><img class='media-object' src=\"" + doubanComment.headUrl + "\"/></div>";
                                    commentItem += "<div class='media-body'><h4 class='media-heading'>" +
                                            "<span id='userName'>" + doubanComment.userName + "</span>" +
                                            "<span id='starAndTime'>" + " 评分是:" + doubanComment.star + "  " + doubanComment.time + "</span>"
                                            + "</h4>";
                                    commentItem += "<div class='media-content'>" + doubanComment.comment + "</div></div></div>";
                                    html += commentItem;
                                });

                                $('.article').html(html);
                                $.jGrowl("获取评论!");
                            },
                            error: function () {
                                $.jGrowl("请输入正确的电影名字!");
                            }
                        });
                    }
                }
                });
            }


        /*获取电影的详细信息*/
        var getMovieDetail = function (movieName) {
            $.ajax({
                url: "http://localhost:8080/crawel/checkMovie",
                type: 'post',
                data: {movieName: movieName},
                success: function (isExist) {
                    if (isExist == "true") {
                        $.jGrowl("已经抓取过此类电影,请直接去到已抓取电影页面查看!");
                    } else {
                        $.ajax({
                            url: "http://localhost:8080/crawel/getMovieDetail",
                            type: 'post',
                            data: {movieName: movieName},
                            success: function (movieDetail) {
                                $scope.totalComment = movieDetail.totalComment;
                                var html = "<div><img src=\"" + movieDetail.movieImg + "\"" + "/></div>";
                                for (var attr in movieDetail) {
                                    if (attr == "movieName") {
                                        html += "<p><span>电影:" + $scope.movieName + "</span></p>";
                                    }
                                    if (attr == "director") {
                                        html += "<p><span>导演:" + movieDetail.director + "</span></p>";
                                    }
                                    if (attr == "actors") {
                                        html += "<p><span>演员:" + movieDetail.actors + "</span></p>";
                                    }
                                    if (attr == "type") {
                                        html += "<p><span>类型:" + movieDetail.type + "</span></p>";
                                    }
                                    if (attr == "area") {
                                        html += "<p><span>地区:" + movieDetail.area + "</span></p>";
                                    }
                                    if (attr == "duration") {
                                        html += "<p><span>片长:" + movieDetail.duration + "</span></p>";
                                    }
                                    if (attr == "time") {
                                        html += "<p><span>上映:" + movieDetail.time + "</span></p>";
                                    }
                                }
                                $('.caption').html(html);
                                $.jGrowl("获取电影信息!");
                            },
                            error: function () {
                                $.jGrowl("请输入正确的电影名字!");
                            }
                        });
                    }
                }
            });
        }
    });
</script>
</html>

