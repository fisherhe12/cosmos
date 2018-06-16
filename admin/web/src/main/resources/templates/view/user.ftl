<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Data Tables</title>

    <link href="${rc.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${rc.contextPath}/static/css/font-awesome.css" rel="stylesheet">

    <link href="${rc.contextPath}/static/css/plugins/dataTables/datatables.min.css" rel="stylesheet">

    <link href="${rc.contextPath}/static/css/animate.css" rel="stylesheet">
    <link href="${rc.contextPath}/static/css/style.css" rel="stylesheet">

</head>

<body class="gray-bg">

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>用户管理</h2>
        <ol class="breadcrumb">
            <li>
                <a href="index.html">主页</a>
            </li>
            <li>
                <a>系统管理</a>
            </li>
            <li class="active">
                <strong>用户管理</strong>
            </li>
        </ol>
    </div>
    <div class="col-lg-2">

    </div>
</div>
<div class="wrapper wrapper-content animated fadeInRight">
    <form action="${rc.contextPath}/user" method="post">
        <div class="ibox-content m-b-sm border-bottom">
            <div class="row">
                <div class="col-sm-3">
                    <div class="form-group">
                        <input type="text" id="name" name="name" value="${q.name!''}" placeholder="输入姓名"
                               class="form-control">
                    </div>
                </div>
                <div class="col-sm-2">
                    <button class="btn btn-primary" type="submit">
                        <i class="fa fa-search"></i>&nbsp;搜索
                    </button>
                </div>
            </div>

        </div>
    </form>
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">

                    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover dataTables-example">
                            <thead>
                            <tr>
                                <th>姓名</th>
                                <th>账号</th>
                                <th>性别</th>
                                <th>电子邮箱</th>
                                <th>是否激活</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list userList as u>
                            <tr class="gradeX">
                                <td>${u.name}</td>
                                <td>${u.account}</td>
                                <td>${u.gender}</td>
                                <td>${u.email}</td>
                                <td>
                                    <#if u.active>是
                                    <#else> 否
                                    </#if>
                                </td>
                                <td>
                                    <a title="编辑" href="#"> <i class="fa fa-edit text-navy"></i>
                                </a>
                                    <span class="pipe">|</span>
                                    <a title="禁用" href="#"> <i class="fa fa-ban text-navy"></i>
                                    </a>
                                </td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>


<!-- Mainly scripts -->
<script src="${rc.contextPath}/static/js/jquery-3.1.1.min.js"></script>
<script src="${rc.contextPath}/static/js/bootstrap.min.js"></script>
<script src="${rc.contextPath}/static/js/plugins/dataTables/datatables.min.js"></script>
<!-- Page-Level Scripts -->
<script>
    $(document).ready(function () {
        $('.dataTables-example').DataTable({
            pageLength: 10,
            responsive: true,
            language: {
                "url": "/static/datatables-CN.json"
            }
        });
    });
</script>
</body>
</html>
