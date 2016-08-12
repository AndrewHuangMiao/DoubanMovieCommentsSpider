<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1.0" />
  <title>Bootstrap响应式登录界面模板</title>

  <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
  <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.1/jquery.jgrowl.min.css" />
  <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
  <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.1/jquery.jgrowl.min.js"></script>

  <style type="text/css">
    .form-group,.input-group {
      margin-bottom: 15px;
    }
    .row {
      margin-right: -15px;
      margin-left: 350px;
      margin-top: 60px;
    }

    .input-group .form-control:last-child{
      height: 50px;
    }
    button{
      height: 50px;
    }

  </style>
</head>

<body>
<div class="container">
  <div class="page-header">
    <h1>豆瓣电影评论<small>注册</small></h1>
  </div>

  <div class="row">
    <div class="col-md-6">
      <h3 style="margin-left: 100px;">欢迎注册</h3>
      <div class="form">
        <form action="http://localhost:8080/user/register" method="post" id="registerForm">
          <div class="form-group">
            <div class="col-xs-12  ">
              <div class="input-group">
                <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
                <input type="text" id="userName" name="userName" class="form-control" placeholder="用户名">
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12  ">
              <div class="input-group">
                <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                <input type="text" id="password" name="password" class="form-control" placeholder="密码">
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12  ">
              <div class="input-group">
                <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                <select class="form-control" id="role" name="role">
                  <option value="admin">管理员</option>
                  <option value="user">用户</option>
                </select>
              </div>
            </div>
          </div>
          <div class="form-group form-actions">
            <div class="col-xs-4 col-xs-offset-4 ">
              <button type="submit" class="btn btn-sm btn-info" onclick="checkUser()"><span class="glyphicon glyphicon-off"></span> 注册</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
<script type="javascript">
  function login(){
    window.location.href= "login.jsp";
  }


  function checkUser(){
    var userName = $('#userName').val();
    var password = $('#password').val();
    var role = $('#role').val();
    $.ajax({
      url: "http://localhost:8080/user/checkUserNameAndPassword",
      type: 'post',
      data: {userName: userName, password:password, role:role},
      success: function (isExist) {
        if(isExist==true){
          $.jGrowl("成功注册!");
          $('#registerForm').submit();
        }else{
          $.jGrowl("该用户已存在!");
        }

      }
    });
  }



</script>
</body>

</html>