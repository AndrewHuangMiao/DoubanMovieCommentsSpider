<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户管理</title>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.1/jquery.jgrowl.min.css" />
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.1/jquery.jgrowl.min.js"></script>
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
        <li><a href="http://localhost:8080/user/manageUser">用户管理</a></li>
        <li><a href="http://localhost:8080/user/login">退出登录</a></li>
        <li class="text-right" style="margin-left: 400px;"><a href="#">${user.userName}欢迎你!</a></li>
      </ul>
    </div>
  </nav>

  <div class="row">
    <div class="col-md-6">
      <table class="table table-bordered" id="userTable" style="margin-left:300px;">

      </table>
    </div>
  </div>
  </div>

<div class="modal fade" id="createUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">用户信息</h4>
      </div>
      <div class="modal-body">
        <form class="form" action="http://localhost:8080/user/modifyUser" method="post">
          <div class="form-group">
            <label for="userName">用户名称</label>
            <input type="text" class="form-control" id="userName_create" name="userName_create">
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input type="text" class="form-control" id="password_create" name="password_create">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            <button type="button" class="btn btn-primary" onclick="createUser()">提交</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Modal -->
<div class="modal fade" id="modifyUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">用户信息</h4>
      </div>
      <div class="modal-body">
        <form class="form" action="http://localhost:8080/user/modifyUser" method="post">
          <div class="form-group">
            <label for="userName">用户名称</label>
            <input type="text" class="form-control" id="userName" name="userName">
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input type="text" class="form-control" id="password" name="password">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            <button type="button" class="btn btn-primary" onclick="updateUser()">提交</button>
          </div>
        </form>
      </div>
    </div>
  </div>

  </div>
</body>

<script>
  $(function(){
    var html = "<tr><th>用户名</th>" +
            "<th>密码</th>" +
            "<th>操作</th>";
    $.ajax({
      url: "http://localhost:8080/user/getAllUser",
      type: 'post',
      success: function (userList) {
        $.each(userList, function(index, user) {
          var userStr = JSON.stringify(user);
          var userHtml = "<tr><td>" + user.userName + "</td>" +
                          "<td>" + user.password + "</td>"+
                  "<td><button class='btn btn-small btn-primary' data='"+ userStr +"' onclick='deleteUser(this)'>删除用户</button>"+
                  "<button class='btn btn-small btn-primary' data='"+ userStr +"' data-toggle=\"modal\" data-target=\"#modifyUser\" onclick='modifyUser(this)'>修改用户</button>"+
                  "<button class='btn btn-small btn-primary' data='"+ userStr +"' data-toggle=\"modal\" data-target=\"#createUser\"'>新增用户</button></td></tr>";
          html += userHtml;
        });
        $('#userTable').html(html);
      }
    });
  });

  function deleteUser(obj){
    if (!confirm("确认要删除？")) {
      return;
    }

    debugger;
    var json = obj.getAttribute("data");
    var user = eval('(' + json + ')');
    var userName = user.userName;

    $.ajax({
      url: "http://localhost:8080/user/deleteUser",
      type: 'post',
      data:{userName:userName},
      success: function (result) {
        if(result==true){
          $.jGrowl("删除成功!");
          location.reload();
        }else{
          $.jGrowl("删除失败!");
        }

      }
    });

  }
  var user;
  function modifyUser(obj) {
    var json = obj.getAttribute("data");
    user = eval('(' + json + ')');

    $('#userName').val(user.userName);
    $('#password').val(user.password);

  }

  function updateUser(){
    var initialUserName = user.userName;
    var userName = $('#userName').val();
    var password = $('#password').val();

    $.ajax({
      url: "http://localhost:8080/user/updateUser",
      type: 'post',
      data: {userName:userName, password:password, initialUserName:initialUserName},
      success: function (result) {
        if(result==true){
          $.jGrowl("修改成功!");
          location.reload();
        }else{
          $.jGrowl("修改失败!");
        }
      }
    });
  }

  function createUser(){
    var userName = $('#userName_create').val();
    var password = $('#password_create').val();

    $.ajax({
      url: "http://localhost:8080/user/createUser",
      type: 'post',
      data: {userName:userName, password:password},
      success: function (result) {
        if(result==true){
          $.jGrowl("新增成功!");
          location.reload();
        }else{
          $.jGrowl("新增失败!");
        }
      }
    });
  }

</script>
</html>
