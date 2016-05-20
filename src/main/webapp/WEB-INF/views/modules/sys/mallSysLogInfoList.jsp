<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单表管理</title>
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
	<form:form id="searchForm" modelAttribute="mallSysLogInfo" action="${ctx}/sys/log/gatewayList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		<li><label>来源渠道：</label>
			<form:select id="clientType" path="clientType" class="input-medium">
				<form:option value="" label="全部"/>
				 <form:option value="pc" label="PC端"/>
				 <form:option value="wx" label="微信"/>
				 <form:option value="android" label="Android"/>
				 <form:option value="ios" label="apple ios"/>
				 
			 </form:select>
		&nbsp;&nbsp;
			</li>
			<li><label>模块：</label>
				<form:input path="module" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>操作时间：</label>
				<input name="beginOperTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallSysLogInfo.beginOperTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endOperTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallSysLogInfo.endOperTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>模块</th><th>来源ip</th><th>设备号</th><th>来源入口渠道</th><th>操作说明</th><th>操作结果</th><th>broswer</th><th>操作时间</th></thead>
		<tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		<c:forEach items="${page.list}" var="log">
			<tr>
				<td>${log.module}</td>
				<td>${log.userIp}</td>
				<td>${log.cuid}</td>
				<td>${log.clientType}</td>
				<td><strong>${log.methods}</strong></td>
				<td>${log.description}</td>
				<td>${log.broswer}</td>
				<td><fmt:formatDate value="${log.operTime}" type="both"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<shiro:hasPermission name="sys:mallSysLogInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallSysLogInfo">
			<tr>
				<shiro:hasPermission name="sys:mallSysLogInfo:edit"><td>
    				<a href="${ctx}/sys/mallSysLogInfo/form?id=${mallSysLogInfo.id}">修改</a>
					<a href="${ctx}/sys/mallSysLogInfo/delete?id=${mallSysLogInfo.id}" onclick="return confirmx('确认要删除该单表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>