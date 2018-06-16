<!DOCTYPE html>
<html>

<head>
<#include 'common/head.ftl'>
</head>

<body>

<div id="wrapper">
<#include 'common/left.ftl'>
    <div id="page-wrapper" class="gray-bg" style="padding-left: 0;padding-right: 0;">
    <#include 'common/top.ftl'>

        <iframe name="iFrame" frameborder="0" width="100%" scrolling="no"
                src="${rc.contextPath}/user" data-id="${rc.contextPath}/user" id="iFrame"
                onload="this.height=this.contentWindow.document.body.scrollHeight;">
        </iframe>

    <#include 'common/foot.ftl'>
    </div>
<#include 'common/sidebar.ftl'>
</div>
<#include "common/script.ftl">
</body>

</html>
