<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统参数管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/parameter/mallSysParameter/">系统参数列表</a></li>
		<shiro:hasPermission name="parameter:mallSysParameter:edit"><li><a href="${ctx}/parameter/mallSysParameter/form">系统参数添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallSysParameter" action="${ctx}/parameter/mallSysParameter/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		
		<div>
			<label>参数类型：</label><input id="paraType" name="paraType" type="text" maxlength="80" class="input-medium" value="${mallSysParameter.paraType}"/>
			<label>参数编码：</label><input id="paraCode" name="paraCode" type="text" maxlength="150" class="input-medium" value="${mallSysParameter.paraCode}"/>
			<label>参数值：</label><input id="paraValue" name="paraValue" type="text" maxlength="150" class="input-medium" value="${mallSysParameter.paraValue}"/>
		&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<shiro:hasPermission name="parameter:mallSysParameter:edit"><th>参数类型</th><th>参数编码</th><th>参数值</th><th>描述内容</th><th>操作人</th><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="MallSysParameter">
			<tr>
				<td>${MallSysParameter.paraType}</td>
				<!--  <td><a href="${ctx}/sys/dict/form?id=${dict.id}">${dict.label}</a></td>
				<td><a href="javascript:" onclick="$('#type').val('${dict.type}');$('#searchForm').submit();return false;">${dict.type}</a></td>-->
				<td>${MallSysParameter.paraCode}</td>
				<td>${MallSysParameter.paraValue}</td>
				<td>${MallSysParameter.paraDesc}</td>
				<td>${MallSysParameter.operUser}</td>
				<shiro:hasPermission name="parameter:mallSysParameter:edit"><td>
    				<a href="${ctx}/parameter/mallSysParameter/form?id=${MallSysParameter.paraId}">修改</a>
					<a href="${ctx}/parameter/mallSysParameter/delete?id=${MallSysParameter.paraId}" onclick="return confirmx('确认要删除该系统参数吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>